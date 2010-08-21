package tagit2.service.query;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.geo.Location;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.SolrService;
import kiwi.model.Constants;
import kiwi.util.MD5;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

import tagit2.api.query.QueryService;
import tagit2.util.geo.Area;
import tagit2.util.geo.Ratio;
import tagit2.util.query.Cluster;
import tagit2.util.query.Point;
import tagit2.util.query.TagitSearchResult;

@Name("tagit2.queryService")
@Scope(ScopeType.STATELESS)
//@Transactional
@AutoCreate
public class QueryServiceImpl implements QueryService {
	
	private static final int MIN_CLUSTERSIZE = 5;
	
    @In
    private SolrService solrService;
    
    @Logger
    private Log log;
    
    private TagitSearchResult result;
	
	/**
	 * this method clusters all points in an area. The cluster bounds depend on the
	 * area size and a given ratio.
	 * Algorithm:
	 * 		
	 * 	
	 * @param area
	 * @param ratio
	 * @param searchString
	 * @return
	 */
	public TagitSearchResult getClusteredMarkers(Area area, Ratio ratio, String searchString,String layerSearchString) {
		
		//clusterId
		long clusterId = 0;
		
		result = new TagitSearchResult();
		
		double wStep = area.getWidth() / ratio.getWidth();
		double hStep = area.getHeight() / ratio.getHeight();
		double wStart = area.getLowerLng();
		double hStart = area.getLowerLat();
		
		//result lists
		List<Cluster> clusters = new LinkedList<Cluster>();
		List<Point> points = new LinkedList<Point>();
		
		//outer for = divide height into ratio.height parts (-> lat parts)
		//for each part search points, points must be sorted ascending by lng
		for( int i = 0; i < ratio.getHeight(); i++ ) {
			//set row
			Area row = new Area( new Location( hStart,area.getLowerLng() ),
								new Location( hStart+hStep, area.getUpperLng() ));
			//get list for this row
			LinkedList<Point> rowPoints = this.searchList(searchString, layerSearchString, row, false);
			
			//divide row into ratio.width parts (-> lng parts)
			//for each part, test if there must be a cluster
			
			double upperBound = wStart;
			for( int j = 0; j < ratio.getWidth(); j++ ) {
				
				double formerBound = upperBound;
				//set upperBound (lng) from quadrant
				upperBound = upperBound+wStep;
				
				//points of this quadrant
				LinkedList<Point> quadrantPoints = new LinkedList<Point>();
				
				//for each point in this quadrant
				while( !rowPoints.isEmpty() && rowPoints.getFirst().getLng() < upperBound ) {
					//remove point
					log.debug("found point: #0", rowPoints.getFirst().toString());
					quadrantPoints.add( rowPoints.removeFirst() );
				}
				
				//if it is not a cluster:
				//are there less than MIN_CLUSTERSIZE points and are they not overlapping (that must only be tested if there are less than 2 points)
				if( quadrantPoints.size() < MIN_CLUSTERSIZE && ( quadrantPoints.size() < 2 || !pointsOverlap( quadrantPoints ))  ) {
					points.addAll(quadrantPoints);
				} else {
					Area clusterArea = new Area(new Location(hStart,formerBound),new Location(hStart+hStep,upperBound));
					clusters.add(new Cluster( clusterId, quadrantPoints, clusterArea ));
					clusterId++;
				}

				//break if there are no more points
				if( rowPoints.isEmpty() ) {
					break;
				}
				
			}
			
			//set new hStart
			hStart = hStart+hStep;
			
		}
		
		log.info("#0 clusters and #1 points must be displayed", clusters.size(), points.size());
		
		//set result
		result.setPoints(points);
		result.setClusters(clusters);
		
		//search for all points to get them and the tag facets
		result.setAllPoints( this.searchList(searchString, layerSearchString, area, true));
		
		return result;
	}
	

	/**
	 * this method returns all points in an area (without clustering)
	 * @param area
	 * @param ratio
	 * @param searchString
	 * @return
	 */
	public TagitSearchResult getMarkers(Area area, String searchString, String layerSearchString) {
		result = new TagitSearchResult();
		
		//get solrSearch result List
		//this is a List of Points
		//tagFacet is also built
		result.setAllPoints( this.searchList(searchString, layerSearchString, area, true));
		result.setPoints(result.getAllPoints());
		
		return result;
	}
	
	/**
	 * this method returns an minimal area, where all points (depending on the given searchString)
	 * resides
	 * @param searchString
	 * @return
	 */
	public Area getBounds( String searchString, String layerSearchString ) {
		//search horizontal and vertical extreme points
		Double swLat,swLng,neLat,neLng;
		swLat = this.getMaxMin(0, searchString, layerSearchString);
		if( swLat != null ) {
			swLng = this.getMaxMin(1, searchString, layerSearchString);
			neLat = this.getMaxMin(2, searchString, layerSearchString);
			neLng = this.getMaxMin(3, searchString, layerSearchString);
			return new Area( new Location(swLat,swLng), new Location(neLat,neLng) );
		} else {
			return null;
		}
	}
	
	/**
	 * Improved search function that uses the Solr service for performing the search. It can be 
	 * passed four arguments: a string containing search keywords to look for in title and content,
	 * a query string using the Lucene query syntax for more sophisticated queries, an area to limit 
	 * the search to, and a collection of layers used for restricting to certain types only. 
	 * All parameters may be null.
	 * <p>
	 * The returned list consists of quadruples where the first element is a Long value representing
	 * the content item id, the second and third are Doubles representing latitude and longitude,
	 * respectively, and the fourth is a Double representing the score in relation to the issued
	 * search.
	 * <p>
	 * TODO: we might want to return more complex results including the facets for tags and the
	 * like...
	 * TODO: this method is a quick hack and contains some copy and paste from the core search 
	 * functionality; we should find a more generic way to implement it and avoid redundancies...
	 * TODO: might sort differently, or return just a limited number of results ... we can just
	 * return the total number of results in a first run, for example
	 * <p>
	 * Documentation: http://wiki.apache.org/solr/
	 * 
	 * @param searchKeywords keywords to search in the title and content of the content items
	 * @param searchString Lucene search string to use to look for
	 * @param area the area to limit the search results to (uses the RDF geo:lat and geo:long literal values)
	 * @param layers the layers to limit the search to (uses the type index field)
	 * @return
	 */
	private LinkedList<Point> searchList(String searchString, String layerSearchString, Area area, boolean addTagFacet) {
		
		//if no layer is set
		if( layerSearchString == null ) {
			if( addTagFacet ) {
				result.setTagFacets(new LinkedList<Count>());
			}
			return new LinkedList<Point>();
		}
		String uri_lng = Constants.NS_GEO + "long";
		String uri_lat = Constants.NS_GEO + "lat";
		
		// fields for lng and lat
		String f_lng = "d_"+MD5.md5sum(uri_lng);
		String f_lat = "d_"+MD5.md5sum(uri_lat);
		
		// 1. build the query string from the search criteria
		
		// keyword search: title weighs 4 times as much as content
		KiWiSearchCriteria criteria = solrService.parseSearchString(searchString);

		// additional raw SOLR query parameters
		StringBuilder qString = new StringBuilder();

		if(area != null) {
			
			// range queries over fields:
			qString.append(f_lng);
			qString.append(":[");
			qString.append(area.getLowerLng());
			qString.append(" TO ");
			qString.append(area.getUpperLng());
			qString.append("] ");
			
			qString.append(f_lat);
			qString.append(":[");
			qString.append(area.getLowerLat());
			qString.append(" TO ");
			qString.append(area.getUpperLat());
			qString.append("] ");			
		}
		
		//append type
		qString.append(layerSearchString);
		criteria.setSolrSearchString(qString.toString());
		
		SolrQuery query = solrService.buildSolrQuery(criteria);
		
		
		// 4. set offset and limit so that we get all results back
		query.setStart(0);
		query.setRows(Integer.MAX_VALUE);
		query.setSortField(f_lng, ORDER.asc);

		query.setFields("id","title",f_lng,f_lat,"score","modified");
		
		QueryResponse rsp = solrService.search(query);
		
		LinkedList<Point> res = new LinkedList<Point>();
		
		SolrDocumentList docs = rsp.getResults();
		
		//set facets
		if( addTagFacet ) {
			result.setTagFacets( rsp.getFacetField("tag").getValues() );
		}
		
		for(SolrDocument doc : docs) {
			try {
				Long id = Long.parseLong(doc.getFieldValue("id").toString());
				//TODO should not be neccessary
				String title = "no title";
				if( doc.getFieldValue("title") != null ) {
					title = doc.getFieldValue("title").toString();
				}
				 
				Double lng = (Double) doc.getFieldValue(f_lng);
				Double lat = (Double) doc.getFieldValue(f_lat);
				
				Date lm = (Date) doc.getFieldValue("modified");
				
				//TODO not in use
				//Double score = Double.parseDouble(doc.getFieldValue("score").toString());
				
				res.add(new Point(id, lat, lng, title,lm));
				
			} catch(NumberFormatException ex) {
				log.error("id field was not a valid integer value: #0",doc.getFieldValue("id").toString());
			}
		}
		return res;	
	}

	/**
	 * This method returns an extrema of a given direction
	 *   2
	 * 1   3
	 *   0
	 * If null is returned, the search has no result
	 * @param quadrant
	 * @param searchString
	 * @param layerQuery
	 * @return
	 */
	private Double getMaxMin(int direction, String searchString, String layerSearchString) {
		
		//if no layers selected
		if( layerSearchString == null ) {
			return null;
		}
		
		final String uri_lng = Constants.NS_GEO + "long";
		final String uri_lat = Constants.NS_GEO + "lat";
		
		//settings for direction -> the first result of resultSet is the wanted value
		ORDER order = null;
		String field = null;
		String latLngBounds = null;
		
		switch( direction ) {
		case 0:
			order = ORDER.asc;
			field = "d_"+MD5.md5sum(uri_lat);
			latLngBounds = field+":[-90 TO 90] ";
			break;
		case 1:
			order = ORDER.asc;
			field = "d_"+MD5.md5sum(uri_lng);
			latLngBounds = field+":[-180 TO 180] ";
			break;
		case 2:
			order = ORDER.desc;
			field = "d_"+MD5.md5sum(uri_lat);
			latLngBounds = field+":[-90 TO 90] ";
			break;
		case 3:
			order = ORDER.desc;
			field = "d_"+MD5.md5sum(uri_lng);
			latLngBounds = field+":[-180 TO 180] ";
			break;
		}
		
		// build the query string from the search criteria
		
		// keyword search: title weighs 4 times as much as content
		KiWiSearchCriteria criteria = solrService.parseSearchString(searchString);
		
		// additional raw SOLR query parameters
		StringBuilder qString = new StringBuilder();
		
		//append lat lng bounds (because maybe there are null values)
		qString.append(latLngBounds);
		//append type
		qString.append(layerSearchString);
		criteria.setSolrSearchString(qString.toString());
		
		SolrQuery query = solrService.buildSolrQuery(criteria);
		
		// set offset and limit so that we get all results back
		query.setStart(0);
		query.setRows(1);
		query.setSortField(field, order);

		query.setFields(field);
		
		QueryResponse rsp = solrService.search(query);
		SolrDocumentList docs = rsp.getResults();
		
		Double res = null;
		
		//get query result
		if( !docs.isEmpty() ) {
			SolrDocument doc = docs.get(0);
			res = (Double) doc.getFieldValue(field);
		}
		
		return res;
	}
	
	/**
	 * tests if two points in the list have same lat and same lng
	 * @param quadrantPoints
	 * @return
	 */
	private boolean pointsOverlap(LinkedList<Point> quadrantPoints) {
		//points are ordered by lng -> every point must be tested only one times
		Point lastPoint = quadrantPoints.get(0);
		for( int i = 1; i < quadrantPoints.size(); i++ ) {
			Point curPoint = quadrantPoints.get(i);
			if( lastPoint.getLng() == curPoint.getLng() ) {
				//have the two points same lat too?
				if( lastPoint.getLat() == curPoint.getLat() ) {
					return true;
				}
			}
			lastPoint = curPoint;
		}
		return false;
	}
	
}
