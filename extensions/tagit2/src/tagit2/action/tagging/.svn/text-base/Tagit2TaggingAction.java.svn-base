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
 package tagit2.action.tagging;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

@Name("tagit2.taggingAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class Tagit2TaggingAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private static Log log;
	
    @In FacesMessages facesMessages;
	
	// input current user; might affect the loading of the content item
	@In(create = true)
	private User currentUser;

	@In(create=true)
	@Out
	private ContentItem currentContentItem;
	
	// the entity manager used by this KiWi system
	@In
	private EntityManager entityManager;
	
	@In
	private TripleStore tripleStore;

	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private ContentItemService contentItemService;
	
	
	@In
	private TaggingService taggingService;
 
    // newly created tag
	private String tagLabel;
	
	private boolean shown = false;

	
	/**
	 * Action called by tagging.xhtml add button when submitting a new tag for the resource
	 */
	public void addTag() {
		log.info("adding new tags for input #0", tagLabel);

		// avoid detached entities
//		currentUser = entityManager.merge(currentUser);
		if(!entityManager.contains(currentUser)) {
			currentUser = entityManager.find(User.class, currentUser.getId());
		}
//		currentContentItem = entityManager.merge(currentContentItem);
		if(!entityManager.contains(currentContentItem)) {
			currentContentItem = entityManager.find(ContentItem.class, 
					currentContentItem.getId());
		}
		
		String[] components = tagLabel.split(",");
		
		for(String component : components) {
		
			log.info("adding tag #0",component);
			
			String label = component.trim();
			
			addTag(label);
		}
//		entityManager.flush();
		
		entityManager.refresh(currentContentItem);
		tripleStore.persist(currentContentItem);
		
	
		entityManager.refresh(currentUser);
		tripleStore.persist(currentUser);
		
		// do we need to refresh/merge?
		tagLabel = "";
		
		log.info("tag added successfully");
	}

	/**
	 * Action called by tagging.xhtml add button when endorsing an existing tag for the resource
	 */
	public void addTag(ContentItem taggingItem) {
		log.info("endorsing tag with label #0", taggingItem.getTitle());
					
		// avoid detached entities
//		currentUser        = entityManager.merge(currentUser);
		if(!entityManager.contains(currentUser)) {
			currentUser = entityManager.find(User.class, currentUser.getId());
		}
//		currentContentItem = entityManager.merge(currentContentItem);
		if(!entityManager.contains(currentContentItem)) {
			currentContentItem = entityManager.find(ContentItem.class, currentContentItem.getId());
		}
		
		taggingService.createTagging(taggingItem.getTitle(), currentContentItem, taggingItem, currentUser);
		
		entityManager.refresh(currentContentItem);

		Events.instance().raiseEvent("tagUpdate");

	}

	public void addTag(String label) {
			
		// TODO: query by uri instead of by title
		ContentItem taggingItem = contentItemService.getContentItemByTitle(label);

		if(taggingItem == null) {
			// create new Content Item of type "tag" if the tag does not yet exist
			taggingItem = contentItemService.createContentItem("content/"+label.toLowerCase().replace(" ","_")+"/"+UUID.randomUUID().toString());
			taggingItem.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag"));
			contentItemService.updateTitle(taggingItem, label);
			kiwiEntityManager.persist(taggingItem);
			log.info("created new content item for non-existant tag");
		}


		taggingService.createTagging(label, currentContentItem, taggingItem, currentUser);
					
		entityManager.refresh(currentContentItem);

		Events.instance().raiseEvent("tagUpdate");
	}
	
	
	public void addTagLine(String label) {
		if(tagLabel != null && !"".equals(tagLabel)) {
			tagLabel += ", ";
		}
		tagLabel += label;
	}
	
	public void removeTag(ContentItem taggingItem) {
		log.info("removing tag #0 by user #1",taggingItem.getTitle(),currentUser.getLogin());
		
		// avoid detached entities
		if(!entityManager.contains(currentUser)) {
			currentUser        = entityManager.merge(currentUser);
		}
		if(!entityManager.contains(currentContentItem)) {
			currentContentItem = entityManager.merge(currentContentItem);
		}

		Query q = entityManager.createNamedQuery("taggingAction.getTagByIdAuthor");
		q.setParameter("login", currentUser.getLogin());
		q.setParameter("id",taggingItem.getId());
		
		List<Tag> l = (List<Tag>)q.getResultList();
		for(Tag t : l) {
			taggingService.removeTagging(t);
		}
		
		entityManager.refresh(currentContentItem);

		Events.instance().raiseEvent("tagUpdate");
	}
	
	
	/**
	 * @return the tagLabel
	 */
	public String getTagLabel() {
		return tagLabel;
	}

	/**
	 * @param tagLabel the tagLabel to set
	 */
	public void setTagLabel(String tagLabel) {
		this.tagLabel = tagLabel;
	}
	
	public void onShow() {
		shown=true;
	}
	
	
	
	public boolean isShown() {
		return shown;
	}

	public void setShown(boolean shown) {
		this.shown = shown;
	}

	public List<String> autocomplete(Object param) {
		return taggingService.autocomplete(param.toString().toLowerCase());
	}
		
}