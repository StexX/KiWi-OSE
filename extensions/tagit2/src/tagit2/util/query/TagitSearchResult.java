package tagit2.util.query;

import java.util.Collections;
import java.util.List;

import org.apache.solr.client.solrj.response.FacetField.Count;

/**
 * this class represents a tagit search. it includes allPoints for a list view,
 * clusters and points for a map view and tagFacets for faceted search
 * @author tkurz
 *
 */
public class TagitSearchResult {
	
	private List<Point> allPoints;
	private List<Cluster> clusters;
	private List<Point> points;
	private List<Count> tagFacets;
	
	public TagitSearchResult() {
		allPoints = Collections.EMPTY_LIST;
		clusters = Collections.EMPTY_LIST;
		points = Collections.EMPTY_LIST;
		tagFacets = Collections.EMPTY_LIST;
	}
	
	public List<Point> getAllPoints() {
		return allPoints;
	}
	public void setAllPoints(List<Point> allPoints) {
		this.allPoints = allPoints;
	}
	public List<Cluster> getClusters() {
		return clusters;
	}
	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}
	public List<Point> getPoints() {
		return points;
	}
	public void setPoints(List<Point> points) {
		this.points = points;
	}
	public List<Count> getTagFacets() {
		return tagFacets;
	}
	public void setTagFacets(List<Count> list) {
		if( list != null ) {
			this.tagFacets = list;
		}
	}
}
