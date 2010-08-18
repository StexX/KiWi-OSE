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
import java.util.LinkedList;
import java.util.List;

import kiwi.api.comment.CommentService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.geo.Location;
import kiwi.api.multimedia.MultimediaService;
import kiwi.api.render.RenderingService;
import kiwi.model.content.ContentItem;
import kiwi.model.facades.PointOfInterestFacade;
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

import tagit2.util.error.Message;

@Scope(ScopeType.CONVERSATION)
@Name("tagit2.newsItemAction")
//@Transactional
public class NewsItemAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int NONE = 0;
	private static final int IMAGE_UPLOAD = 1;
	private static final int ADD_COMMENT = 2;
	
	private static final String ADRESS_TEXT = "Adresse zuweisen";
	
	private PointOfInterestFacade poi;
	
	private String title;
	private String description;
	private Location location;
	private String adress;
	private String date;
	
	private int mode = -1;

	private LinkedList<ContentItem> images;
	private LinkedList<ContentItem> upload;
	
	@Logger
	Log log;
	
	@In
    private KiWiEntityManager kiwiEntityManager;
	
    @In
    private MultimediaService multimediaService;
    
	@In
	private ContentItemService contentItemService;
	
    @In(create=true)
    private ContentItem currentContentItem;
    	
	@In(create=true)
	private CommentService commentService;
	
    @In
    private RenderingService renderingPipeline;
	
	@In
	private User currentUser;
	
	//************** conversation actions
	@Begin(join=true)
	public void begin() {
		if( poi == null || poi.getId() != currentContentItem.getId() ) {
		poi = kiwiEntityManager.createFacade(currentContentItem, PointOfInterestFacade.class);
		title = poi.getTitle();
		description = renderingPipeline.renderEditor(poi.getDelegate(), currentUser);
		location = new Location(poi.getLatitude(),poi.getLongitude());
		if( poi.getAddress() == null || poi.getAddress().equals("") ) {
			adress = ADRESS_TEXT;
		} else {
			adress = poi.getAddress();
		}
		images = poi.getMultimedia();
		upload = new LinkedList<ContentItem>();
		
		DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		setDate(formatter.format(poi.getModified()));
		
		mode = NONE;
		log.info("startet NewsItemAction with item: #0", title);
		}
	}
	
	@End
	public void cancel() {
		log.info("cancel: poi is ");
	}
	
	@End
	public void save() {
		//some save stuff
		poi.setLatitude(location.getLatitude());
		poi.setLongitude(location.getLongitude());
		poi.setAddress(adress);
		
		kiwiEntityManager.persist( poi );
//		kiwiEntityManager.flush();
	}
	
	//************* check storing
	public Message check() {
		//check if location has changed
		Message m = new Message();
		if( location.getLatitude() != poi.getLatitude() || location.getLongitude() != poi.getLongitude() )
			m.addLine("Verschiebung des Punktes nach " + location.toString());
		if( poi.getAddress() != null ) {
			if( !poi.getAddress().equals( adress ) && adress != null ) {
				m.addLine("Adresse geändert in '"+adress+"'");
			}
		} else if( !adress.equals("") && !adress.equals(ADRESS_TEXT) ) {
			m.addLine("Adresse geändert in '"+adress+"'");
		}
		return m;
	}
	
	public void saveLocation() {
		poi.setLatitude(location.getLatitude());
		poi.setLongitude(location.getLongitude());
		poi.setAddress(adress);
	}
	
	//************* image and comment event modes
	public void startImageUpload() {
		mode = IMAGE_UPLOAD;
	}
	
	public void saveImageUpload() {
		if( !upload.isEmpty() ) {
			for(ContentItem media : upload) {
				kiwiEntityManager.persist(media);
				multimediaService.extractMetadata(media);
				images.addFirst(media);
			}
			
			poi.setMultimedia(images);
    	
//			kiwiEntityManager.flush();
		
			//refresh multimedialist
			upload = new LinkedList<ContentItem>();
		
			log.info("saved images");
		}
		mode = NONE;
	}
	
	public void cancelImageUpluad() {
		upload = new LinkedList<ContentItem>();
//		kiwiEntityManager.flush();
		mode = NONE;
	}
	
	private String commenttitle;
	private String comment;
	
	public void startComment() {
		comment = "";
		commenttitle = "";
		mode = ADD_COMMENT;
	}
	
	public void saveComment() {
		if( !title.equals("") ) {
			log.info("creating new comment: #0", commenttitle);
			
			commentService.createReply(currentContentItem, currentUser, commenttitle, comment);
			
			contentItemService.saveContentItem(currentContentItem);
		}
		mode = NONE;
	}
	
	public void cancelComment() {
		mode = NONE;
	}
	
	//************* other action
	/**
	 * To order comments (from new to old)
	 */
	public List<ContentItem>getAllComments() {
//		List<ContentItem> cl = new LinkedList<ContentItem>();
//		for( ContentItem c :currentContentItem.getComments()) {
//			cl.add(c);
//		}
//		//sort must be happen in onother list
//		Collections.sort(cl, new Comparator<ContentItem>(){
//			@Override
//			public int compare(ContentItem o1, ContentItem o2) {
//				return o2.getCreated().compareTo(o1.getCreated());
//			}
//		});
//		return cl;
		return commentService.listComments(currentContentItem);
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

		upload.add(contentItem);
    }
	
	//************** getters and setters
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getAdress() {
		return adress;
	}

	public LinkedList<ContentItem> getImages() {
		return images;
	}
	
	public int getImageListSize() {
		return images.size();
	}

	public int getMode() {
		return mode;
	}

	public void setCommenttitle(String commenttitle) {
		this.commenttitle = commenttitle;
	}

	public String getCommenttitle() {
		return commenttitle;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

}
