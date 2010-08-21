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
package kiwi.wiki.action;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.content.ContentItemService;
import kiwi.api.fragment.FragmentFacade;
import kiwi.api.fragment.FragmentService;
import kiwi.api.tagging.ExtendedTagCloudEntry;
import kiwi.api.tagging.TagCloudService;
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
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;

/**
 * An action for creating/editing fragments in the editor. 
 * @author Marek Schmidt
 *
 */
@Name("fragmentAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional(value=TransactionPropagationType.MANDATORY)
@Synchronized
public class FragmentAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	Log log;
	
	@In
	private TagCloudService tagCloudService;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private EntityManager entityManager;
	
	@In
	private FragmentService fragmentService;
	
	@In(create=true)
	private ContentItem currentContentItem;
	
	@In
	private TripleStore tripleStore;
	
	@In
	private TaggingService taggingService;
	
	@In(create = true)
	private User currentUser;
	
	/** The content item the edited fragment is a fragment of 
	 * (may be different from the currentContentItem, if editing nested content item fragment.
	 */
	private ContentItem currentContext;
	
	private FragmentFacade fragmentFacade;
	
	private String tagLabel;
	
	boolean creating;
	
	private List<ExtendedTagCloudEntry> tagCloud;
	
	public void setCurrentContext (ContentItem currentContext) {
		this.currentContext = currentContext;
		log.info("set current context: #0", this.currentContext);
	}
	
	public void setCurrentContextKiwiId (String kiwiId) {
		log.info("setCurrentContextKiwiId: \"#0\"", kiwiId);
		if ("".equals(kiwiId) || kiwiId == null) {
			this.setCurrentContext(currentContentItem);
		}
		else {
			this.setCurrentContext(contentItemService.getContentItemByKiwiId(kiwiId));
		}
	}
	
	public void create() {
		
		//TODO: parse the tags.
		if (fragmentFacade != null) {
			fragmentService.saveFragment(fragmentFacade);
		}
	}
	
	public void delete() {
		if (fragmentFacade != null) {
			fragmentService.removeFragment(fragmentFacade);
		}
	}
	
	public void cancel() {
		if (creating) {
			if (fragmentFacade != null) {
				fragmentService.removeFragment(fragmentFacade);
			}
		}
	}
	
	/** 
	 * Set the currently edited fragment from javascript client, 
	 * format: <context kiwiid> " " <fragment kiwiid>
	 * if <context kiwiid> is an empty string, current content item is considered a context
	 * if <fragment kiwiid> is an empty string, a new fragment CI shall be created.
	 * @param js
	 */
	public void setFragmentJS(String js) {
		String[] split = js.split(" ", 2);
		setCurrentContextKiwiId (split[0]);
		if ("".equals(split[1])) {
			fragmentFacade = fragmentService.createFragment(currentContext, FragmentFacade.class);
			fragmentFacade.setAuthor(currentUser);
			creating = true;
		}
		else {
			fragmentFacade = fragmentService.getContentItemFragment(currentContext, split[1], FragmentFacade.class);
		}
		
		tagLabel = "";
		tagCloud = null;
	}
	
	/**
	 * Returns the current information about the fragment for the javascript client
	 * it consists of <fragment kiwiid>, or an empty string if none was created
	 * @return
	 */
	public String getFragmentJS() {
		if (fragmentFacade == null) {
			return "";
		}
		else {
			return fragmentFacade.getKiwiIdentifier();
		}
	}
	
	public List<ExtendedTagCloudEntry> getTagCloud() {
		if(tagCloud == null && fragmentFacade != null) {
			tagCloud = tagCloudService.aggregateTags(taggingService.getTags(fragmentFacade.getDelegate()));
		}
		
		return tagCloud;
	}
	
	public List<String> autocomplete(Object param) {
		return taggingService.autocomplete(param.toString().toLowerCase());
	}
	
	public void addTag(ContentItem taggingItem) {
		log.info("endorsing tag with label #0", taggingItem.getTitle());
					
		taggingService.createTagging(taggingItem.getTitle(), fragmentFacade.getDelegate(), taggingItem, currentUser);
			
		tagLabel = "";
		tagCloud = null;

		Events.instance().raiseEvent("tagUpdate");
	}
	
	public void removeTag(ContentItem taggingItem) {
		log.info("removing tag #0 by user #1",taggingItem.getTitle(),currentUser.getLogin());
		
		// avoid detached entities
		if(!entityManager.contains(currentUser) && currentUser.getId() != null) {
			currentUser        = entityManager.merge(currentUser);
		}

		Query q = entityManager.createNamedQuery("taggingAction.getTagByIdAuthor");
		q.setParameter("login", currentUser.getLogin());
		q.setParameter("id",taggingItem.getId());
		
		List<Tag> l = (List<Tag>)q.getResultList();
		for(Tag t : l) {
			taggingService.removeTagging(t);
		}
			
		tagLabel = "";
		tagCloud = null;

		Events.instance().raiseEvent("tagUpdate");
	}
	
	public String getTagLabel() {
		return tagLabel;
	}

	public void setTagLabel(String tagLabel) {
		this.tagLabel = tagLabel;
	}
	
	public void addTag(String label) {
		
		// TODO: query by uri instead of by title
		ContentItem taggingItem = contentItemService.getContentItemByTitle(label);

		if(taggingItem == null) {
			// create new Content Item of type "tag" if the tag does not yet exist
			taggingItem = contentItemService.createContentItem("content/"+label.toLowerCase().replace(" ","_")+"/"+UUID.randomUUID().toString());
			taggingItem.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag"));
			contentItemService.updateTitle(taggingItem, label);
			contentItemService.saveContentItem(taggingItem);
			log.info("created new content item for non-existant tag");
		}

		taggingService.createTagging(label, fragmentFacade.getDelegate(), taggingItem, currentUser);
					
		Events.instance().raiseEvent("tagUpdate");
	}
	
	public void addTag() {
		log.info("adding new tags for input #0", tagLabel);
		
		String[] components = tagLabel.split(",");
		
		for(String component : components) {
		
			log.info("adding tag #0",component);
			
			String label = component.trim();
			
			addTag(label);
		}
//		entityManager.flush();
			
		tagLabel = "";
		tagCloud = null;
		
		log.info("tag added successfully");
	}
	
	public FragmentFacade getFragment() {
		return fragmentFacade;
	}
}
