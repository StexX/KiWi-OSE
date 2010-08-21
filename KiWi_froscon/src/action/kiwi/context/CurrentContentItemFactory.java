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

package kiwi.context;

import java.io.Serializable;

import javax.persistence.EntityManager;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.event.KiWiEvents;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.service.transaction.KiWiSynchronizationImpl;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;

/**
 * The CurrentContentItemFactory is a component that takes care of loading the currently
 * selected content item into the conversation scope. Together with the CurrentUserFactory
 * and the CurrentPerspectiveFactory, it forms the context of the KiWi system, which
 * consists of the currently active user, the currently active content item, and the currently
 * active perspective.
 * <p>
 * Like CurrentUserFactory and CurrentPerspectiveFactory, this component is implemented as
 * a factory
 * 
 * @author Sebastian Schaffert
 * 
 */
@Name("currentContentItemFactory")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
@Synchronized // avoid that two calls to the factory are issued
//@Transactional
public class CurrentContentItemFactory implements Serializable {

	/** The serial number of this component. */
	private static final long serialVersionUID = 3407152404934610271L;

	
	/**
	 * Inject a Seam logger for logging purposes inside this component.
	 */
	@Logger
	private static Log log;

	/**
	 * Faces Messages to display status results.
	 */
	@In
	FacesMessages facesMessages;

	/**
	 * Input current user as this might affect the loading of the content item.
	 */
	@In(create=true)
	private User currentUser;

	/**
	 * The current content item. Outjected to the conversation scope.
	 */
	@In(required = false)
	@Out(scope = ScopeType.CONVERSATION, required = false)
	private ContentItem currentContentItem;

	/** 
	 * The entity manager used by this KiWi system. We could use kiwiEntityManager,
	 * put for the purpose of just loading users, the ordinary entityManager is
	 * sufficient and more efficient.
	 */
	@In
	private EntityManager entityManager;
	
	@In
	private TransactionService transactionService;

	/**
	 * The triple store used by this KiWi system. Used for loading additional content item
	 * data that is not persisted in the database.
	 */
	@In
	private TripleStore tripleStore;

	@In
	private ContentItemService contentItemService;

	/**
	 * The configuration service of the KiWi system. Used for retrieving the base 
	 * URI of the system for constructing new content items.
	 */
	@In(value = "configurationService")
	private ConfigurationService configurationService;

	/*
	 * four alternatives for retrieving content items: by DB id, by KiWi id, by
	 * title, by URI
	 */
	
	/**
	 * The database id of the content item to load from the database. May be used as parameter
	 * for loading the content item (priority 1).
	 */
	private Long currentItemId;

	/**
	 * The KiWi id of the content item to load from the database. May be used as parameter
	 * for loading the content item (priority 2).
	 * <p>
	 * The KiWi id is uri::URI for KiWiUriResources and bnode::ID for KiWiAnonResources.
	 */
	private String currentItemKiWiId;

	/**
	 * The title of the content item to load from the database. May be used as parameter for
	 * loading content items (priority 4). Note that the title of a content item
	 * may be changed by the user and is possibly not unique.
	 */
	private String currentItemTitle;

	/**
	 * The URI of the content item to load from the database. May be used as a parameter for
	 * loading content items (priority 3). Note that the URI can only be used to load
	 * content items that represent KiWiUriResources.
	 */
	private String currentItemUri;


	/**
	 * A factory method for loading the current content item
	 */
	@Factory("currentContentItem")
	public void loadContentItem() {
		
		// when content item is not yet loaded, load it
		if (currentContentItem == null) {
			if (currentItemId != null && !currentItemId.equals("")) {
				// if database ID is given, load from database
				currentContentItem = entityManager.find(ContentItem.class,currentItemId);
				tripleStore.refresh(currentContentItem, false);
				
				log.debug("loaded content item #0 by internal id", currentContentItem.getId());
			} else if (currentItemKiWiId != null && !currentItemKiWiId.equals("")) {
				// else if a KiWi ID is given, ask content item service for
				// content item by KiWi ID
				currentContentItem = contentItemService.getContentItemByKiwiId(currentItemKiWiId);
				if (currentContentItem != null) {
					log.debug("loaded content item #0 by KiWi id", currentContentItem.getId());
				} else {
					String[] components = currentItemKiWiId.split("::");
					if (components[0].equals("uri")) {
						currentContentItem = tripleStore.createUriResource(components[1]).getContentItem();
					} else if (components[0].equals("bnode")) {
						currentContentItem = tripleStore.createAnonResource(components[1]).getContentItem();
					}
					currentContentItem.setAuthor(currentUser);
				}
			} else if (currentItemUri != null && !currentItemUri.equals("")) {
				// else if a URI is given, ask content item service for content
				// item by URI
				currentContentItem = contentItemService.getContentItemByUri(currentItemUri);
				if (currentContentItem != null) {
					log.info("loaded content item #0 based on uri",currentContentItem.getId());
				} else {
					currentContentItem = tripleStore.createUriResource(currentItemUri).getContentItem();
					currentContentItem.setAuthor(currentUser);
				}
			} else if (currentItemTitle != null && !currentItemTitle.equals("")) {
				// else if a title is given, ask content item service for
				// content item by title
				currentContentItem = contentItemService.getContentItemByTitle(currentItemTitle);
				if (currentContentItem != null) {
					log.info("loaded content item #0 based on title",currentContentItem.getId());
				} else {
					currentContentItem = contentItemService.createContentItem();
					currentContentItem.setAuthor(currentUser);
					contentItemService.updateTitle(currentContentItem, currentItemTitle);
				}
			} else {
				// otherwise, return the start page; if it doesn't exist, create
				// and save it
				currentContentItem = contentItemService.getContentItemByUri(configurationService.getStartPage());
				if (currentContentItem != null) {
					log.info("loaded start page #0 based on uri",currentContentItem.getId());
				} else {
					if(!entityManager.contains(currentUser) && currentUser.getId() != null) {
						currentUser = entityManager.merge(currentUser);
					}
					KiWiResource currentContentItemReosurce = tripleStore.createUriResource(configurationService.getStartPage());
					currentContentItem = currentContentItemReosurce.getContentItem();
					currentContentItem.setAuthor(currentUser);
					contentItemService.updateTitle(currentContentItem, "StartPage");
					entityManager.persist(currentContentItem);
					tripleStore.persist(currentContentItem);

					//need to manually outject currentContentItem so that KiWiSynchronizationImpl can find it in the context and commit the transaction
					Contexts.getConversationContext().set("currentContentItem", currentContentItem);
					//have to commit manually because RenderJSFWebService will try to grab start page sooner than it is available via entityManager.find automatically  
					try {
						Transaction.instance().commit();
						Transaction.instance().begin();
						transactionService.registerSynchronization(
		                		KiWiSynchronizationImpl.getInstance(), 
		                		transactionService.getUserTransaction() );
					} catch (Exception e) {
						log.error("Exception trying to commit transaction after creating StartPage: #0",e,e.getMessage());
					}
				}
			}

			// set output variables
			if (currentContentItem.getResource().isUriResource()) {
				currentItemUri = ((KiWiUriResource) currentContentItem.getResource()).getUri();
			} else if(currentContentItem.getResource() != null) {
				currentItemKiWiId = currentContentItem.getResource().getKiwiIdentifier();
				currentItemId = currentContentItem.getId();
			}
			
			// notify other components that a new content item has been loaded
			Events.instance().raiseEvent(KiWiEvents.CONTENTITEM_CHANGED, currentContentItem);

			if(!currentUser.getLogin().equals("anonymous")) {
				Events.instance().raiseEvent(KiWiEvents.ACTIVITY_VISITCONTENTITEM, currentUser,currentContentItem);
			}
		
		} else if(!entityManager.contains(currentContentItem) && currentContentItem.getId() != null) {
			currentContentItem = entityManager.find(ContentItem.class, currentContentItem.getId());
		}
		// the ContentRenderService then takes care of appropriately rendering
		// the content for the current display
		Conversation.instance().setDescription("contentItem: " + currentContentItem.getTitle());
	}

	/**
	 * Force reload of the current content item.
	 */
	public void refresh() {
		currentContentItem = null;
		loadContentItem();
	}
	
	/**
	 * @return the currentItemId
	 */
	public Long getCurrentItemId() {
		return currentItemId;
	}

	/**
	 * Set the current item id to a new value.
	 * <p>
	 * When setting the currentItemId to a new value, it is necessary to call refresh() to take 
	 * the changes into effect.
	 * 
	 * @param currentItemId
	 *            the currentItemId to set
	 */
	public void setCurrentItemId(Long currentItemId) {
		this.currentItemId = currentItemId;
	}

	/**
	 * @return the currentItemKiWiId
	 */
	public String getCurrentItemKiWiId() {
		return currentItemKiWiId;
	}

	/**
	 * Set the current item kiwi id to a new value.
	 * <p>
	 * When setting the currentItemKiWiId to a new value, it is necessary to call refresh() to take 
	 * the changes into effect.
	 * @param currentItemKiWiId
	 *            the currentItemKiWiId to set
	 */
	public void setCurrentItemKiWiId(String currentItemKiWiId) {
		this.currentItemKiWiId = currentItemKiWiId;
	}

	/**
	 * @return the currentItemTitle
	 */
	public String getCurrentItemTitle() {
		return currentItemTitle;
	}

	/**
	 * Set the current item title to a new value.
	 * <p>
	 * When setting the currentItemTitle to a new value, it is necessary to call refresh() to take 
	 * the changes into effect.
	 * @param currentItemTitle
	 *            the currentItemTitle to set
	 */
	public void setCurrentItemTitle(String currentItemTitle) {
		this.currentItemTitle = currentItemTitle;
	}

	/**
	 * @return the currentItemUri
	 */
	public String getCurrentItemUri() {
		return currentItemUri;
	}

	/**
	 * Set the current item uri to a new value.
	 * <p>
	 * When setting the currentItemUri to a new value, it is necessary to call refresh() to take 
	 * the changes into effect.
	 * @param currentItemUri
	 *            the currentItemUri to set
	 */
	public void setCurrentItemUri(String currentItemUri) {
		this.currentItemUri = currentItemUri;
	}

	
}
