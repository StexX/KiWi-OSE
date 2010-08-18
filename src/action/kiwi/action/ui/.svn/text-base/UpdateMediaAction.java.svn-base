/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 * 
 * Contributor(s):
 * 
 * 
 */

package kiwi.action.ui;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.EntityManager;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.multimedia.MultimediaService;
import kiwi.model.content.ContentItem;
import kiwi.model.content.MediaContent;
import kiwi.model.user.User;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

/**
 * TODO: this media action should probably be renamed into something like "UpdateMediaAction or similar.
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("mediaAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class UpdateMediaAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2925298947305627340L;

	@Logger
	private static Log log;
		
	// inject services
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private EntityManager entityManager;

	@In
	private FacesMessages facesMessages;
	
	@In
	private MultimediaService multimediaService;
	
    @Out(value="mediaActionCI",required=false)
	private ContentItem contentItem;
    
    @In
    private ContentItemService contentItemService;
    
    private MediaContent mediaContent;
    
    
    private boolean unsaved;

	private String onSaveEvent, onCancelEvent, onRemoveEvent;

	@In
	private User currentUser;

	/**
	 * initializes the action. 
	 * @param ciId is contentItem.id
	 * @param onSaveEvent The action will be called after persisting the mediaContent. "" resets the value, null doesn't change it. 
	 * @param onCancelEvent The action will be called when the user cancels the operation. "" resets the value, null doesn't change it.
	 */
	public void init(Long ciId,String onSaveEvent,String onCancelEvent, String onRemoveEvent) {
		this.contentItem = entityManager.find(ContentItem.class, ciId);
		log.info("contentItemId: #0", ciId);
		
		if(onSaveEvent == "")
			this.onSaveEvent = "";
		else
			this.onSaveEvent = onSaveEvent;
		
		if(onCancelEvent == "")
			this.onCancelEvent = "";
		else
			this.onCancelEvent = onCancelEvent;
		
		if(onRemoveEvent == "")
			this.onRemoveEvent = "";
		else
			this.onRemoveEvent = onRemoveEvent;
		
		
		if(contentItem==null){
			facesMessages.add("The contentItem for the MediaContent mustn't be null at this point!");
			return;
		}
		mediaContent = contentItem.getMediaContent();
    	unsaved = false;
    }
    
    /**
	 * Listener watching file uploads. When a file upload is complete, a new MediaContent object
	 * is created based on the uploaded data.
	 * 
	 * @param event
	 */
    public void listener(UploadEvent event){

    	UploadItem item = event.getUploadItem();

    	log.info("File: '#0' with type '#1' was uploaded",item.getFileName(),item.getContentType());


    	String name = FilenameUtils.getName(item.getFileName());
    	String type = item.getContentType();
    	byte[] data = item.getData();

    	if (item.isTempFile()) {
    		try {
    			data = FileUtils.readFileToByteArray( item.getFile() );
    		} catch (IOException e) {
    			log.error("error reading file #0",item.getFile().getAbsolutePath());
    		}
    	}

    	type = multimediaService.getMimeType(name, data);

    	if(contentItem==null)
    		init(((ContentItem)Component.getInstance("currentContentItem")).getId(), "", "", "");

//    	contentItemService.updateMediaContentItem(contentItem, data, type, name);
    	
    	mediaContent = new MediaContent(contentItem);
    	mediaContent.setData(data);
    	mediaContent.setMimeType(type);
    	mediaContent.setFileName(name);

    	unsaved=true;
    }

    /**
     * Actually store the modified content item in the database and triple store.
     * @return
     */
//    @Transactional(TransactionPropagationType.REQUIRED)
    public String storeContentItem() {
		
    	// remove ContentItem from old media content; we get exceptions later on otherwise
//    	MediaContent old = contentItem.getMediaContent();
//    	if(old != null) {
////    		old.setContentItem(null);
//    		kiwiEntityManager.persist(old);
//    	}
//    	
//    	entityManager.persist(mediaContent);
//    	
//    	contentItem.setMediaContent(mediaContent);
    	
    	contentItemService.updateMediaContentItem(contentItem, mediaContent.getData(), mediaContent.getMimeType(), mediaContent.getFileName());
    	
//     	contentItem.setAuthor(kiwiEntityManager.merge(currentUser));
//    	
//     	contentItemService.saveContentItem(contentItem);
     	
    	unsaved = false;
    	    	
    	Events.instance().raiseEvent(onSaveEvent);
    	return "success";
    }

//    @Transactional
    public String removeMedia(){
    	contentItem = kiwiEntityManager.merge(contentItem);
//    	contentItem.getMediaContent().setContentItem(null);
    	contentItem.setMediaContent(null);
    	kiwiEntityManager.persist(contentItem);
    	multimediaService.extractMetadata(contentItem);
    	this.mediaContent=null;
    	Events.instance().raiseEvent(onRemoveEvent);
    	return "success";
    }
    
    public void cancel() {
    	Events.instance().raiseEvent(onCancelEvent);
    }

	/**
	 * @return the mediaContent
	 */
    @BypassInterceptors
	public MediaContent getMediaContent() {
		return mediaContent;
	}

	/**
	 * @param mediaContent the mediaContent to set
	 */
    @BypassInterceptors
	public void setMediaContent(MediaContent mediaContent) {
		this.mediaContent = mediaContent;
	}

	/**
	 * @return the unsaved
	 */
    @BypassInterceptors
	public boolean isUnsaved() {
		return unsaved;
	}

	/**
	 * @param unsaved the unsaved to set
	 */
    @BypassInterceptors
	public void setUnsaved(boolean unsaved) {
		this.unsaved = unsaved;
	}

    @BypassInterceptors
	public ContentItem getContentItem() {
		return contentItem;
	}

	public void setContentItem(ContentItem contentItem) {
		log.info("setting contentItem #0", contentItem.toString());
		this.contentItem = contentItem;
		init(contentItem.getId(),null,null,null);
	}
}
