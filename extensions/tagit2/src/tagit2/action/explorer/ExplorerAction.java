/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2008-2009, The KiWi Project (http://www.kiwi-project.eu)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of the KiWi Project nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contributor(s):
 * 
 * 
 */
 package tagit2.action.explorer;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.config.ConfigurationService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.geo.Location;
import kiwi.api.triplestore.TripleStore;
import kiwi.context.CurrentContentItemFactory;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.facades.PointOfInterestFacade;

import org.apache.solr.client.solrj.response.FacetField.Count;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

import tagit2.action.query.LayeringAction;
import tagit2.action.query.SearchAction;
import tagit2.api.query.IconService;
import tagit2.api.query.QueryService;
import tagit2.api.tagcloud.TagitTagCloudService;
import tagit2.util.exchange.MapMarker;
import tagit2.util.exchange.MapSettings;
import tagit2.util.geo.Area;
import tagit2.util.geo.Ratio;
import tagit2.util.query.Cluster;
import tagit2.util.query.Point;
import tagit2.util.query.TagitSearchResult;
import tagit2.util.tagcloud.TagCloudItem;

@Name("tagit2.explorerAction")
@Scope(ScopeType.PAGE)
//@Transactional
@Synchronized(timeout=1000000000)
public class ExplorerAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Mode {NONE, CREATE_LOCATION, CREATE_ROUTE}
	
	//Statics
	private static final String HOMEBASE_LAT = "47.8";
	private static final String HOMEBASE_LNG = "13.03";
	private static final String HOMEBASE_ZOOM = "13";
	private static final Ratio RATIO = new Ratio(7,5);
	private static final int MAX_CLUSTER_SIZE = 20;
	
	public static final String KEY = Constants.GOOGLE_KEY;
	
	private Mode mode = Mode.NONE;
	
	//Injections
	@Logger
	Log log;
	
    @In
    private TripleStore tripleStore;
    
    @In(create=true)
    private ContentItem currentContentItem;
    
    @In
    private KiWiEntityManager kiwiEntityManager;
    
    @In(create=true)
    private CurrentContentItemFactory currentContentItemFactory;
    
    @In(create=true)
    private ConfigurationService configurationService;
    
    @In(value="tagit2.layeringAction",create=true)
    private LayeringAction layeringAction;
    
    @In(value="tagit2.searchAction",create=true)
    private SearchAction searchAction;
    
    @In(value="tagit2.queryService")
    private QueryService queryService;
    
    @In(value="tagit2.tagCloudService",create=true)
    private TagitTagCloudService tagCloudService;
    
    @In(value="tagit2.clusterAction",create=true)
    private ClusterAction clusterAction;
    
    @In(value="tagit2.iconService",create=true)
    private IconService iconService;
	
    //Other Properties
	/**
	 * center of map. This value is used to center the map on a configured point (if current
	 * ContentItem has no Geoposition) or the Geoposition of the CurrentContentItem
	 */
	private Location center;
	private int zoom;
	private Area bounds;
	private double swLat,swLng,neLat,neLng;
	private long currentId;
	private long currentClusterId;
	
	//something for return
	private List<Count> tagFacets = Collections.EMPTY_LIST;
	private List<Point> allMarkers = Collections.EMPTY_LIST;
	private List<Cluster> clusters = Collections.EMPTY_LIST;
	
	//Getters and Setters
	public Location getCenter() {
		return center;
	}
	
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public int getZoom() {
		return zoom;
	}

	public void setSwLat(double swLat) {
		this.swLat = swLat;
	}

	public void setSwLng(double swLng) {
		this.swLng = swLng;
	}

	public void setNeLat(double neLat) {
		this.neLat = neLat;
	}

	public void setNeLng(double neLng) {
		this.neLng = neLng;
	}

	public void setCurrentId(long currentId) {
		this.currentId = currentId;
	}
	
    public String getGmapkey() {
        return configurationService.getConfiguration("tagit.googlekey", KEY)
                .getStringValue();
    }

	//other public methods
	
	public void setCurrentClusterId(long currentClusterId) {
		this.currentClusterId = currentClusterId;
	}

	/**
	 * set currentContentItem by id
	 * @param currentId
	 */
	public void setPoint() {
        currentContentItemFactory.setCurrentItemId(currentId);
        currentContentItemFactory.refresh();
        log.info("set point to id #0", currentId);
	}
	
	/**
	 * unselect a point via contentItemFactory
	 */
	public void unselectPoint() {
		
		//reset mode
		mode = Mode.NONE;
			
        currentContentItemFactory.setCurrentItemId(null);
        currentContentItemFactory.setCurrentItemKiWiId(null);
        currentContentItemFactory.setCurrentItemTitle(null);
        currentContentItemFactory.setCurrentItemUri(null);
        currentContentItemFactory.refresh();
	}
	
	/**
	 * returns a JSON representation of current cluster, if there is one; an empty cluster
	 * otherwise
	 * @return
	 */
	public void setCluster() {
		if( currentClusterId == -1 ) {
			clusterAction.resetCluster();
		} else {
			for( Cluster c : clusters ) {
				if( c.getId() == currentClusterId ) {
					log.info("Cluster: #0", c.getId());
					clusterAction.setCluster(c);
				}
			}
		}

	}
	
	public Area getClusterArea() {
		for( Cluster c : clusters ) {
			if( c.getId() == currentClusterId ) {
				log.info("Cluster: #0", c.getId());
				clusterAction.setCluster(c);
				if( c.getSize() > MAX_CLUSTER_SIZE ) {
					return c.getArea();
				} else {
					return null;
				}
			}
		}
		return null;
	}
	
	/**
	 * if currentContentItem has a Geolocation, center is set to this location
	 * @return id of currentContentItem, if it has a Geolocation, -1 otherwise
	 */
	public MapSettings checkCurrent() {
		zoom = configurationService.getConfiguration("tagit.zoom",HOMEBASE_ZOOM).getIntValue();
        if (currentContentItem != null
                && currentContentItem.getResource().hasType(
                        tripleStore.createUriResource(Constants.NS_GEO
                                + "Point"))) {
        	
        	//set center
        	PointOfInterestFacade currentPoi = kiwiEntityManager.createFacade(currentContentItem,
                        							PointOfInterestFacade.class);
        	center = new Location(currentPoi.getLatitude(),currentPoi.getLongitude());

        	//return settings
        	return new MapSettings(center,zoom,currentPoi.getId());
        } else {
        	//get lat/lng by configService
    		double lat = configurationService.getConfiguration("tagit.center.lat",HOMEBASE_LAT).getDoubleValue();
    		double lng = configurationService.getConfiguration("tagit.center.lng",HOMEBASE_LNG).getDoubleValue();
    		center = new Location(lat,lng);
        	return new MapSettings(center,zoom,-1);
        }
	}
	
	/**
	 * bounds are set by properties, that are binded on jsFunction. After that markers that are initialized by
	 * a clustering algorithm are returned.
	 * @return
	 */
	public List<MapMarker> setBoundsAndZoom() {
		this.bounds = new Area(swLat,swLng,neLat,neLng);
		
		//is no layer selected?
		if( layeringAction.getLayerQuery() == null ) {
			tagFacets = Collections.EMPTY_LIST;
			return Collections.EMPTY_LIST;
		} else {
			//return getSinglePois();
			return getClusteredPois();
		}
	}
	
	/**
	 * returns the minimal area that is needed to display the whole search on a map
	 * @return
	 */
	public Area getBounds() {
		return queryService.getBounds(searchAction.getKeyword(), layeringAction.getLayerQuery());
	}
	
	/**
	 * returns a List of tagCloudItema based on the tagFacets
	 * @return
	 */
	public List<TagCloudItem> getTagCloud() {
		return tagCloudService.getTagCloud(tagFacets);
	}

	public List<MapMarker> getAllMarkers() {
		//TODO there should be a type of paging
		return Collections.EMPTY_LIST;
	}

	/**
	 * this is a test function, witch uses the query method that returns unclustered results
	 * @return
	 */
	private List<MapMarker> getSinglePois() {
		//to test it, get unclustered points
		TagitSearchResult result = queryService.getMarkers(bounds, searchAction.getKeyword(), layeringAction.getLayerQuery());

		List<MapMarker> l = new LinkedList<MapMarker>();
		
		for( Point p : result.getPoints() ) {
			l.add( p.getMapMarker() );
		}

		log.info("#0 markers found", l.size());

		//set tagFacets
		allMarkers = result.getAllPoints();
		tagFacets = result.getTagFacets();

		log.info("#0 factes found", tagFacets.size());

		return l;
	}
	
	/**
	 * This method returns a list of points and clusters get by the clustering of queryService.
	 * It stores the facets, clusters and a list of all pois (maybe usable for a list view of results)
	 * @return
	 */
	private List<MapMarker> getClusteredPois() {
		TagitSearchResult result = queryService.getClusteredMarkers(bounds, RATIO ,searchAction.getKeyword(), layeringAction.getLayerQuery());
		
		//init result lists
		List<MapMarker> l = new LinkedList<MapMarker>();
		
		//store clusters and facets
		this.clusters = result.getClusters();
		this.allMarkers = result.getAllPoints();
		this.tagFacets = result.getTagFacets();
		
		//set icons to visible single markers
		iconService.setIconsOf( result.getPoints() );
		
		for( Point p : result.getPoints() ) {
			l.add( p.getMapMarker() );
		}
		
		for( Cluster c : this.clusters ) {
			l.add( c.getMapMarker() );
		}
		
		return l;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Mode getMode() {
		return mode;
	}
	
	public void startCreateLocation() {
		mode = Mode.CREATE_LOCATION;
	}
	
	public void startCreateRoute() {
		mode = Mode.CREATE_ROUTE;
	}

}
