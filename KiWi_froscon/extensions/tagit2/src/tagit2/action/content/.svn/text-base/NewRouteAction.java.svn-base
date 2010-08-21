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

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.UUID;

import javax.imageio.ImageIO;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.importexport.ImportService;
import kiwi.context.CurrentContentItemFactory;
import kiwi.model.user.User;
import kiwi.service.importexport.KiWiImportException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import tagit2.api.content.route.RouteFacade;
import tagit2.api.content.route.TrackPointFacade;
import tagit2.util.error.Message;
import tagit2.util.route.AltitudeService;
import tagit2.util.route.DistanceService;
import tagit2.util.route.ProfileService;
import tagit2.util.update.LCSLocation;

@Name("tagit2.newRouteAction")
@Scope(ScopeType.CONVERSATION)
public class NewRouteAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	Log log;

	private static final int IMPORT = 1;
	private static final int DRAW = 2;
	
	private int mode;
	private String format = "application/gpx+xml";
	
	private InputStream stream;
	
	@In("kiwi.core.importService")
	private ImportService importService;
    
	@In(create=true)
    private CurrentContentItemFactory currentContentItemFactory;
	
	@In
	private User currentUser;
	
    @In
    private KiWiEntityManager kiwiEntityManager;
    
    @In(create = true)
    private ContentItemService contentItemService;
	
	@Begin(join=true)
	public void start() {
		title = "Titel";
		description="Beschreibung...";
		routeString = null;
		mode = IMPORT;
	}
	
	@End
	public void cancel() {
		mode = IMPORT;
	}
	
	private String title;
	private String description;
	private String routeString;
	
	private void startNewRoute() {
		title = "Titel";
		description="Beschreibung...";
		routeString = null;
	}
	
	public Message checkRoute() {
		Message m = new Message();
		//log.info("check new route with title:#0, desc:#1, route:#2", title,description,routeString);
		if( routeString == null || routeString.equals("null")) {
			m.addLine("Route nicht definiert");
			m.setType(Message.ERROR);
		}
		if (title.equals("") || title.equals("Titel") ) {
			m.addLine("Titel nicht definiert");
			m.setType(Message.ERROR);
		}
		if (description.equals("") || description.equals("<p>Beschreibung...</p>") ) {
			m.addLine("Beschreibung nicht deniniert");
			m.setType(Message.ERROR);
		}

		return m;
	}
	
	@End
	public void save() {
		//get routeString to array
		routeString = routeString.substring(1,routeString.length()-1);
		String[] points = routeString.split("\\);\\(");
		//log.info("update route #0", route.getTitle());
		
		LinkedList<LCSLocation> y = new LinkedList<LCSLocation>();
		
		//create list of new track
		for( String s : points ) {
			String[] loc = s.split(", ");
			//log.info("point: lat:#0, lng:#1", loc[0],loc[1]);
			y.add(new LCSLocation( Double.valueOf( loc[0]),Double.valueOf( loc[1]) ));
		}
		//create Route
    	RouteFacade currentRoute = kiwiEntityManager.createFacade(contentItemService.createContentItem("/tagit/route/" + UUID.randomUUID().toString()), RouteFacade.class);
    	currentRoute.setAuthor(currentUser);
    	
    	//set startPoint
    	currentRoute.setLatitude(y.getFirst().getLat());
    	currentRoute.setLongitude(y.getFirst().getLng());
    	
    	contentItemService.updateTitle(currentRoute, title);
    	
    	//persist route
    	kiwiEntityManager.persist(currentRoute);
    	contentItemService.updateTextContentItem(currentRoute, description);
    	
    	LinkedList<TrackPointFacade> trackpoints = new LinkedList<TrackPointFacade>();
    	for( LCSLocation tp : y ) {
    		TrackPointFacade tpf = kiwiEntityManager.createFacade( contentItemService.createContentItem("/tagit/trackPoint/" + UUID.randomUUID().toString()), TrackPointFacade.class);
	    	tpf.setAuthor(currentUser);
	    	tpf.setLatitude(tp.getLat());
	    	tpf.setLongitude(tp.getLng());
    		tpf.setAltitude(0);
			trackpoints.add(tpf);
    	}
    	
    	//set altitude and distance
    	//test if altService is active
		boolean altitudeServiceIsActive = true;
		if( AltitudeService.getAltitude(0, 0) == 0 ) {
			log.info("AltitudeSerivce is not active");
			altitudeServiceIsActive = false;
		}
		
		if( altitudeServiceIsActive ) {
			//get elevations
			//TODO if first value is not defined!!!
			double lastValidElevation = 0;
			for( TrackPointFacade tp : trackpoints ) {
				if( tp.getAltitude() == 0 ) {
					tp.setAltitude( AltitudeService.getAltitude(tp.getLatitude(),tp.getLongitude()));
					if( tp.getAltitude() == -32768 ) {
						tp.setAltitude(lastValidElevation);
					}
				}
				lastValidElevation = tp.getAltitude();
			}
			log.info("Altitudes were setted");
		}
		
		if( !trackpoints.isEmpty() ) {
			double dist = DistanceService.getTotalDistance( trackpoints);
			log.info("orig dist: #0", dist);
			dist = Math.round( dist * 100.0 ) / 100.0;
			log.info(dist);
	        currentRoute.setDistance(  dist );
	        currentRoute.setVerticalClimb( AltitudeService.getVerticalClimb( trackpoints ) );
		}
		
		//get profile
    	byte [] profile = getImageBytes( ProfileService.getProfile(trackpoints,currentRoute).createBufferedImage(540, 180) );
    	//create multimedia (profile)
    	//some string ops for fileName
    	String name = title;
    	
    	contentItemService.updateMediaContentItem(currentRoute.getDelegate(), profile, "image/png", name);
    	
		//persist trackpoints
		int ordinal = 0;
		for( TrackPointFacade tp : trackpoints ) {

			//set ordinal
	    	tp.setOrdinal(ordinal);
	    	contentItemService.updateTitle(tp, "R:"+currentRoute.getId()+"_TP:"+ordinal);
			++ordinal;
			
			//persist
			kiwiEntityManager.persist(tp);

		}
		
		currentRoute.setTrackPoints(trackpoints);
		
//		kiwiEntityManager.flush();

		// save again to process updates
    	kiwiEntityManager.refresh(currentRoute);
    	
    	//set route as currentContentItem
    	currentContentItemFactory.setCurrentItemId(currentRoute.getId());
    	currentContentItemFactory.refresh();
    	
		log.info("save new route with title:#0, desc:#1, route:#2", title,description,routeString);
	}
	
	@End
	public String importRoute() {
		try {
			importService.importData(stream, format, null, null, currentUser, null);
		} catch (KiWiImportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("import route with mimetype #0 was successfull", format);
		return "home";
	}

	public void setMode(int mode) {
		if( this.mode != DRAW && mode == DRAW ) {
			startNewRoute();
		}
		this.mode = mode;
	}

	public int getMode() {
		return mode;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}
	
	public void listener(UploadEvent event) throws Exception{
        UploadItem item = event.getUploadItem();
        stream = new BufferedInputStream(new FileInputStream(item.getFile()));
        log.info("fileupload successfull");
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

	public String getRouteString() {
		return routeString;
	}

	public void setRouteString(String routeString) {
		this.routeString = routeString;
	}
	
	private byte[] getImageBytes(BufferedImage image) {
		
		byte[] resultImageAsRawBytes = new byte[0];
			
			//write
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				ImageIO.write( image, "png", baos );
				//close
				baos.flush();
				resultImageAsRawBytes = baos.toByteArray();
				
				baos.flush();
				baos.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		return resultImageAsRawBytes;
	}

}
