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
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.geo.Location;
import kiwi.api.multimedia.MultimediaService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.facades.PointOfInterestFacade;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.SKOSConcept;
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

import tagit2.api.category.CategoryService;
import tagit2.util.error.Message;

@Name("tagit2.newLocationAction")
//@Transactional
@Scope(ScopeType.CONVERSATION)
public class NewLocationAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String TITLE_TEXT = "Titel eingeben";
	private static final String DESC_TEXT = "<p>Beschreibung eingeben</p>";
	
	private static final int NONE = 0;
	private static final int IMAGE_UPLOAD = 1;
	
	private static final String ADRESS_TEXT = "Adresse zuweisen";
	
	private PointOfInterestFacade poi;
	
	private String title;
	private String description;
	private Location location;
	private String adress;
	
	private String category;
	private String subCategory;
	
	private int mode = -1;

	private LinkedList<ContentItem> upload;
	private LinkedList<ContentItem> images;
	
	private final String CAT_DEFAULT = "Bitte auswählen";
	private final String SUBCAT_DEFAULT = "Bitte auswählen";
	
	@Logger
	Log log;
    
    @In(value="tagit2.categoryService", create=true)
    private CategoryService categoryService;
    
    @In(create=true)
    private ContentItem currentContentItem;
    
	@In
	private TripleStore tripleStore;
	
	@In
    private KiWiEntityManager kiwiEntityManager;
	
    @In
    private MultimediaService multimediaService;
    
	@In
	private ContentItemService contentItemService;
	
	@In
	private User currentUser;
	
	@In(create=true)
	private TaggingService taggingService;
	
	//************** conversation actions
	@Begin(join=true)
	public void begin() {
	    description = DESC_TEXT;
	    title = TITLE_TEXT;
		
		location = new Location(0,0);
		adress = ADRESS_TEXT;

		upload = new LinkedList<ContentItem>();
		images = new LinkedList<ContentItem>();
		
		mode = NONE;
		
		log.info("startet NewLocationAction with new item");

	}
	
	@End
	public void cancel() {
		log.info("cancel poi");
	}
	
	@End
	public void save() {
		//create poi
    	poi = kiwiEntityManager.createFacade(
    			contentItemService.createContentItem("/tagit/poi/" + UUID.randomUUID().toString()), 
    			PointOfInterestFacade.class);
    	poi.setAuthor(currentUser);
		//some save stuff
		poi.setLatitude(location.getLatitude());
		poi.setLongitude(location.getLongitude());
		poi.setAddress(adress);
		
		final KiWiUriResource locationType = tripleStore.createUriResource(Constants.NS_FCP_CORE + "Location");
		poi.addType(locationType);
		
		kiwiEntityManager.persist( poi );
		
		contentItemService.updateTitle(poi, title);
		contentItemService.updateTextContentItem(poi, description);
		
		if( !images.isEmpty() ) {
			for(ContentItem media : images) {
				kiwiEntityManager.persist(media);
				multimediaService.extractMetadata(media);
			}
			
			poi.setMultimedia(images);
		}
		
		SKOSConcept cat = categoryService.getCategory( category );
		
		if( cat != null ) {
			poi.setCategory( cat );
			taggingService.createTagging(category, poi.getDelegate(), cat.getDelegate(), currentUser);
			if( !subCategory.equals( "" ) && !subCategory.equals( SUBCAT_DEFAULT ) ) {	
				SKOSConcept subcat = categoryService.getSubcategory( subCategory , cat);
				poi.setSubCategory( subcat );
				taggingService.createTagging(subCategory, poi.getDelegate(), subcat.getDelegate(), currentUser);
				
			}
		}
		
		kiwiEntityManager.persist( poi );
		
		log.info("saved poi");
		
	}

	//************* check storing
	public Message check(boolean loggedIn) {
		Message m = new Message();
		//check title
		if( title.equals("") || title.equals(TITLE_TEXT) ) {
			m.addLine("Titel nicht vergeben!");
			m.setType(Message.ERROR);
			log.info("titel failure: #0",title);
		}
		
		if( description.equals("") || description.equals(DESC_TEXT) ) {
			m.addLine("Beschreibung fehlt!");
			m.setType(Message.ERROR);
			log.info("description failure: #0",description);
		}
		
		if( location.getLatitude() == 0 && location.getLongitude() == 0 ) {
			m.addLine("Verortung fehlt!");
			m.setType(Message.ERROR);
			log.info("location failure: #0",location);
		}
		
		if( !m.hasMessages() ) {
			m = new Message("Neue Location speichern?");
		}
		
		log.info(m.getValue());
		
		return m;
	}
	
	//************* image and comment event modes
	public void startImageUpload() {
		mode = IMAGE_UPLOAD;
	}
	
	public void saveImageUpload() {
		images.addAll( upload );
		
		//refresh multimedialist
		upload = new LinkedList<ContentItem>();
		
		log.info("'saved' images");
		
		mode = NONE;
	}
	
	public void cancelImageUpluad() {
		upload = new LinkedList<ContentItem>();
//		kiwiEntityManager.flush();
		mode = NONE;
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
		this.description = description.trim();
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	
	public List<String> getCatgoryStrings() {
		return categoryService.listCategoryStrings();
	}
	
	public List<String> getSubCategoryStrings() {
		log.info("get subcategories for #0", category);
		return categoryService.listSubcategoryStrings( categoryService.getCategory(category) );
	}
	
	public String getCAT_DEFAULT() {
		return CAT_DEFAULT;
	}

	public String getSUBCAT_DEFAULT() {
		return SUBCAT_DEFAULT;
	}
	
}
