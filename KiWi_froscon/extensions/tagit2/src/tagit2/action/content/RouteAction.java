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
 package tagit2.action.content;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.swing.ImageIcon;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.geo.Location;
import kiwi.api.multimedia.MultimediaService;
import kiwi.api.render.RenderingService;
import kiwi.model.content.ContentItem;
import kiwi.model.content.MediaContent;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import tagit2.api.content.route.GeoLocatedImageFacade;
import tagit2.api.content.route.RouteFacade;
import tagit2.api.content.route.TrackPointFacade;
import tagit2.api.content.route.WayPointFacade;
import tagit2.api.update.RouteUpdateService;
import tagit2.util.exchange.SimpleWaypoint;
import tagit2.util.geo.GPSUtils;

@Name("tagit2.routeAction")
//@Transactional
@Scope(ScopeType.CONVERSATION)
public class RouteAction<TrachPointFacade> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int NONE = 0;
	private static final int ADD_WAYPOINT = 1;
	private static final int EDIT_DESCRIPTION = 2;
	
	private RouteFacade route;
	private String date;
	private LinkedList<WayPointFacade> waypoints;
	
	private String title;
	private String description;
	private double distance;
	private double vertClimb;
	
	private String points;
	
	@In
    private KiWiEntityManager kiwiEntityManager;
	
    @In
    private MultimediaService multimediaService;
    
    @In(create=true,value="tagit2.routeUpdateService")
    private RouteUpdateService routeUpdateService;
    
	@In
	private ContentItemService contentItemService;
	
    @In(create=true)
    private ContentItem currentContentItem;
	
    @In
    private RenderingService renderingPipeline;

    @In(create = true)
    private User currentUser;
    
    @Logger
    Log log;
	
	private int mode = -1;
	
	@Begin(join=true)
	public void begin() {
		if( route==null || route.getId() != currentContentItem.getId() ) {
		route = kiwiEntityManager.createFacade(currentContentItem, RouteFacade.class);
		if( route.getTextContent() != null ) {
			description =  renderingPipeline.renderEditor(route.getDelegate(), currentUser);
		} else {
			description = "<p>Beschreibung...</p>";
		}
		title = route.getTitle();
		editTitle = title;
		editDesc = description;
		distance = route.getDistance();
		vertClimb = route.getVerticalClimb();
		waypoints = route.getWayPoints();
		
		DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		date = formatter.format(route.getModified());
		
		mode = NONE;
		log.info("started routeAction with item #0",route.getTitle());
		} else {
			log.info("routeAction is already running");
		}
	}
	
	@End
	public void cancel() {
		route = null;
		mode = NONE;
	}
	
	@End
	public void save() {
		routeUpdateService.updateRoute(this.route, points);
		distance = route.getDistance();
		vertClimb = route.getVerticalClimb();
	}
	
	public int getNbOfWaypoints() {
		return waypoints.size();
	}
	
	/**
	 * returns an ordered list of locations
	 * @return
	 */
	public List<Location> getTrackpoints() {
		List<Location> l = new LinkedList<Location>();
		List<TrackPointFacade> tps = route.getTrackPoints();
			
		Collections.sort( tps,new Comparator<TrackPointFacade>(){

			@Override
			public int compare(TrackPointFacade o1, TrackPointFacade o2) {
				return o1.getOrdinal() - o2.getOrdinal();
			}
			
		});
		
		for( TrackPointFacade tp : tps ) {
			l.add(new Location(tp.getLatitude(),tp.getLongitude()));
		}
		return l;
	}
	
	/*description and title*/
	private String editTitle;
	private String editDesc;
	
	public void startEditDescription() {
		mode = EDIT_DESCRIPTION;
		editTitle = this.title;
		editDesc = this.description;
	}
	
	public void saveEditDescription() {
		contentItemService.updateTextContentItem(this.route,editDesc);
		this.description = editDesc;
		contentItemService.updateTitle(route, editTitle);
		this.title = editTitle;
		mode = NONE;
	}
	
	public void cancelEditDescription() {
		mode = NONE;
	}
	
	/**
	 * returns a list of SimpleWaypoints (POJO)
	 * @return
	 */
	public List<SimpleWaypoint> getWayPointLocations() {
		List<SimpleWaypoint> l = new LinkedList<SimpleWaypoint>();
		log.info(route);
		for( WayPointFacade w : route.getWayPoints() ) {
			SimpleWaypoint wp = new SimpleWaypoint(w.getId(),w.getLatitude(),w.getLongitude(),w.getTitle());
			wp.setId(w.getId());
			wp.setEmpty(w.getMultimedia().isEmpty());
			l.add(wp);
		}
		return l;
	}
	
	public List<WayPointFacade> getWaypoints() {
		return waypoints;
	}
	
	private Location newWaypointLocation;
	private String newWaypointTitle;
	private String newWaypointDescripton;
	private LinkedList<ContentItem> newWaypointImages;
	
	public void startAddWaypoint() {
		mode = ADD_WAYPOINT;
		setNewWaypointTitle("");
		setNewWaypointDescripton("<p></p>");
		newWaypointImages = new LinkedList<ContentItem>();
		setNewWaypointLocation(new Location(0,0));
		log.info("startet add Waypoint");
	}
	
	public void saveAddWaypoint() {
		
		WayPointFacade newWaypointFacade = kiwiEntityManager.createFacade( contentItemService.createContentItem("/tagit/wayPoint/" + UUID.randomUUID().toString()), WayPointFacade.class);
    	newWaypointFacade.setAuthor(currentUser);
		newWaypointFacade.setLatitude(newWaypointLocation.getLatitude());
		newWaypointFacade.setLongitude(newWaypointLocation.getLongitude());
		contentItemService.updateTitle(newWaypointFacade, newWaypointTitle);
		contentItemService.updateTextContentItem(newWaypointFacade, newWaypointDescripton);
		
		kiwiEntityManager.persist(newWaypointFacade);
		
		try {
		for( ContentItem m: newWaypointImages ) {
			kiwiEntityManager.persist(m);
			multimediaService.extractMetadata(m);
		}
		newWaypointFacade.setMultimedia(newWaypointImages);
		} catch(Exception e) {
			log.error("some failure while persisting media content",e);
		}
		
		//persist
		kiwiEntityManager.persist(newWaypointFacade);
		
		
		waypoints.add(newWaypointFacade);
		
		route.setWayPoints(waypoints);
		
		kiwiEntityManager.persist(route);
//		kiwiEntityManager.flush();
		
		log.info("current route(#0) persisted", route.getId());
		
		mode = NONE;
		
		log.info("location: #0, title: #1, desc: #2",newWaypointLocation, newWaypointTitle, newWaypointDescripton);
	}
	
	public void cancelAddWaypoint() {
		mode = NONE;
		log.info("quit add Waypoint");
	}
	
    public void listener(UploadEvent event){
    	
    	UploadItem item = event.getUploadItem();

		log.info("File: '#0' with type '#1' was uploaded", item.getFileName(), item.getContentType());

		String name = FilenameUtils.getName(item.getFileName());
		String type = item.getContentType();
		byte[] data = item.getData();

		if (item.isTempFile()) {
			try {
				data = FileUtils.readFileToByteArray(item.getFile());
			} catch (IOException e) {
				log.error("error reading file #0", item.getFile()
						.getAbsolutePath());
			}
		}
		
		type = multimediaService.getMimeType(name, data);
		
		ContentItem contentItem = contentItemService.createMediaContentItem(data, type, name);
		contentItem.setAuthor(currentUser);

		newWaypointImages.add(contentItem);
		
		log.info("waypoint contains now #0 images",newWaypointImages.size());
    }
    
    public void cancelImageUpload() {
    	newWaypointImages = new LinkedList<ContentItem>();
    }
    
    public void saveImageUpload() {
    	//nothing to do so far
    }
    
    public int getNewWaypointImageSize() {
    	if( newWaypointImages != null ) {
    		return newWaypointImages.size();
    	} else return 0;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getMode() {
		return mode;
	}

	public void setNewWaypointTitle(String newWaypointTitle) {
		this.newWaypointTitle = newWaypointTitle;
	}

	public String getNewWaypointTitle() {
		return newWaypointTitle;
	}

	public void setNewWaypointDescripton(String newWaypointDescripton) {
		this.newWaypointDescripton = newWaypointDescripton;
	}

	public String getNewWaypointDescripton() {
		return newWaypointDescripton;
	}

	public void setNewWaypointLocation(Location newWaypointLocation) {
		this.newWaypointLocation = newWaypointLocation;
	}

	public Location getNewWaypointLocation() {
		return newWaypointLocation;
	}
	
	public MediaContent getProfile() {
		if( route != null )
			return route.getMediaContent();
		else return null;
	}

	public double getDistance() {
		return distance;
	}

	public double getVertClimb() {
		return vertClimb;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setEditTitle(String editTitle) {
		this.editTitle = editTitle;
	}

	public String getEditTitle() {
		return editTitle;
	}

	public void setEditDesc(String editDesc) {
		this.editDesc = editDesc;
	}

	public String getEditDesc() {
		return editDesc;
	}

	public void setPoints(String points) {
		this.points = points;
	}
	
	//TODO return
	public void startImageUploadFake() {
		newWaypointImages = new LinkedList<ContentItem>();
	}
	//title of simple waypoint could be imageUrl
	public List<SimpleWaypoint> saveImageUploadFake() {
		List<SimpleWaypoint> images = new LinkedList<SimpleWaypoint>();
		
		try {
			for( ContentItem m: newWaypointImages ) {
				kiwiEntityManager.persist(m);
		}
			route.setMultimedia(newWaypointImages);
		} catch(Exception e) {
				log.error("some failure while persisting media content",e);
		}
			
		kiwiEntityManager.persist(route);
//		kiwiEntityManager.flush();
		
		for( ContentItem c : newWaypointImages ) {
			GeoLocatedImageFacade poi = kiwiEntityManager.createFacade(c, GeoLocatedImageFacade.class);
			Location l = GPSUtils.toLocation(poi.getExifLatitude()+" "+poi.getExifLatitudeRef(), poi.getExifLongitude()+" "+poi.getExifLongitudeRef());
			
			SimpleWaypoint wp = new SimpleWaypoint(poi.getId(), l.getLatitude(), l.getLongitude(), ((KiWiUriResource)poi.getResource()).getUri());
			
			int[] wh = getWidthHeight(c);
			wp.setWidth(wh[0]);
			wp.setHeight(wh[1]);
			
			images.add(wp);
		}
		
		return images;
	}
	
	private int[] getWidthHeight(ContentItem ci) {
		int[] wh = new int[2];
		
		double maxHeight = 200;
		double maxWidth = 200;
		
		ImageIcon img = new ImageIcon(ci.getMediaContent().getData());
		
		double imgHeight = img.getIconHeight();
		double imgWidth = img.getIconWidth();
		
		double origRatio = imgHeight / imgWidth;
		double necRatio = maxHeight / maxWidth;
		
		long height = 0;
		long width = 0;
		
		if( origRatio > necRatio ) {
			//hight must be min
			if( imgHeight < maxHeight ) {
				height = Math.round( imgHeight );
			} else {
				height = Math.round( maxHeight );
			}
		} else {
			if( imgWidth < maxWidth ) {
				height = Math.round( imgHeight );
			} else {
				height = Math.round( ( maxWidth / imgWidth ) * imgHeight );
			}
		}
		width =  Math.round( height / origRatio );
		
		wh[0] = (int)width;
		wh[1] =(int)height;
		
		return wh;
	}
	
}
