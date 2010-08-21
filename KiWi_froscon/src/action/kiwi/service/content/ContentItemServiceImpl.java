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
package kiwi.service.content;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.content.ContentItemServiceLocal;
import kiwi.api.content.ContentItemServiceRemote;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.multimedia.MultimediaService;
import kiwi.api.render.RenderingService;
import kiwi.api.render.StoringService;
import kiwi.api.revision.RenameContentItemService;
import kiwi.api.revision.UpdateMediaContentService;
import kiwi.api.revision.UpdateTextContentService;
import kiwi.api.role.RoleService;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.TextContentNotChangedException;
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;
import kiwi.model.content.MediaContent;
import kiwi.model.content.TextContent;
import kiwi.model.revision.TextContentUpdate;
import kiwi.model.user.User;
import nu.xom.Document;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.security.Delete;
import org.jboss.seam.annotations.security.Insert;
import org.jboss.seam.annotations.security.Update;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * The ContentItemService provides common methods for accessing, querying, creating, and storing
 * ContentItems in KiWi.
 * 
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("contentItemService")
@Scope(ScopeType.STATELESS)
@AutoCreate
//@Transactional
public class ContentItemServiceImpl implements ContentItemServiceLocal, ContentItemServiceRemote {

	@Logger
	private static Log log;

    @In FacesMessages facesMessages;
    

	// the triple store used by this KiWi system
	@In(value = "tripleStore", create = true)
	private TripleStore tripleStore;

	// the entity manager used by this KiWi system
	@In
	private EntityManager entityManager;

	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private SolrService solrService;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private RenderingService renderingPipeline;

	@In
	private TransactionService transactionService;
	
	@In(create=true)
	private User currentUser;
	
	@In
	private RoleService roleService;
	
	/**
	 * Create a new content item with the current system's base URI and a random unique postfix.
	 * This method does not call persist - the created content item must be persisted manually.
	 * 
	 * @return a newly initialised content item that can be used for further operations
	 */
	public ContentItem createContentItem() {
		return createContentItem("content/"+UUID.randomUUID().toString());
	}

	/**
	 * Create a new content item with the current system's base URI and the uriPostfix appended. The actual
	 * creation of the content item is currently performed by tripleStore.createUriResource ...
	 * 
	 * @param uriPostfix the postfix of the URI to use; should not start with /
	 * @return a newly initialised content item that can be used for further operations
	 */
	@Insert(ContentItem.class)
	public ContentItem createContentItem(String uriPostfix) {
		ContentItem result = getContentItemByUri(configurationService.getBaseUri()+"/"+uriPostfix);
		if(result == null) {
			result = tripleStore.createUriResource(configurationService.getBaseUri()+"/"+uriPostfix).getContentItem();
			result.setAuthor(currentUser);
			
			// TODO: workaround, needs to be fixed properly (KIWI-684)
//			transactionService.getCurrentTransactionData().setCurrentContentItem(result);
			
			Events.instance().raiseEvent(KiWiEvents.ACTIVITY_CREATECONTENTITEM, currentUser, result);
			Events.instance().raiseTransactionSuccessEvent(KiWiEvents.ITEM_CREATED, result);
		}
		return result;
	}
	
	
	
	/**
	 * Create a new non-local content item (i.e. with a URI that does not resolve to the current 
	 * system's base URI.
	 * <p>
	 * This method does not call persist, the created content item must be persisted manually.
	 * 
	 * @param uri the uri of the external content item to create
	 * @return a newly initialised content item that can be used for further operations
	 */
	@Insert(ContentItem.class)
	public ContentItem createExternContentItem(String uri) {
		ContentItem result = getContentItemByUri(uri);
		if(result == null) {
			result = tripleStore.createUriResource(uri).getContentItem();
			result.setAuthor(currentUser);
			
			// TODO: workaround, needs to be fixed properly (KIWI-684)
//			transactionService.getCurrentTransactionData().setCurrentContentItem(result);
			
			Events.instance().raiseEvent(KiWiEvents.ACTIVITY_CREATECONTENTITEM, currentUser, result);
			Events.instance().raiseTransactionSuccessEvent(KiWiEvents.ITEM_CREATED, result);
		}
		return result;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see kiwi.backend.KnowledgeSpaceI#listContentItems()
	 */
	@SuppressWarnings("unchecked")
	public Set<ContentItem> getContentItems() {
		Set<ContentItem> result = new HashSet<ContentItem>();
		javax.persistence.Query q = entityManager.createNamedQuery("contentItemService.list");
		result.addAll ((List<ContentItem>) q.getResultList());
		return result;
	}
	
	
	
	
	/**
	 * returns true if user exists and false if not
	 * 
	 * @param title the title of the content item that is tested
	 * @return true if content item with given title exists
	 */
	public boolean contentItemExists(String title) {
		javax.persistence.Query q = entityManager.createNamedQuery("contentItemService.byTitle");
		q.setParameter("title", title);
	
		try {
			q.getSingleResult();
			return true;
		} catch(NoResultException ex) {
			return false;
		}
	}

	

	/**
	 * Return a content item by the AnonID of the resource that is associated
	 * with it. Obviously only works for content items with a anonymous
	 * resource, not with URI resources.
	 * 
	 * @param anonId the internal id of this anonymous resource
	 * @return the content item with the given anonymous id
	 */
	public ContentItem getContentItemByAnonId(String anonId) {
		return _getContentItemByQuery("contentItemService.byAnonId","anonId",anonId);
	}


	/**
	 * Return a content item by its KiWi identifier. The KiWi identifier is either
	 * <ul>
	 * <li>for URI resources: uri::URI, where URI is an arbitrary URI following the 
	 *     W3C standard</li>
	 * <li>for anonymous resources: blank::ANONID, where ANONID is the internal
	 *     anonymous id of the blank node</li>
	 * </ul>
	 * @param kiwiId
	 * @return the content item with the given kiwi id, or null if it does not exist
	 */
	public ContentItem getContentItemByKiwiId(String kiwiId) {
		log.debug("requested KiWi content item with KiWi ID #0", kiwiId);
		// TODO Is it healthy to return null instead of throwing an exception?
		if (kiwiId == null) {
			return null;
		}
		String[] components = kiwiId.split("::");
		if (components[0].equals("uri")) {
			return getContentItemByUri(components[1]);
		} else if (components[0].equals("bnode")) {
			return getContentItemByAnonId(components[1]);
		} else {
			return null;
		}
	}
	
	@Override
	public List<ContentItem> getContentItemsByDate(Date since, Date until) {
		Query q = entityManager.createNamedQuery("contentItemService.getAllContentItemsByDate");
		q.setParameter("since", since);
		q.setParameter("until", until);
		
		return q.getResultList();
	}


	/**
	 * Return a content item by the URI of the resource that is associated with
	 * it. Obviously only works for content items with a URI resource, not with
	 * anonymous resources.
	 * 
	 * @param uri the uri of the resource associated with the content item
	 * @return the content item with the given URI, or null if it does not exist
	 */
	public ContentItem getContentItemByUri(String uri) {
		return _getContentItemByQuery("contentItemService.byUri","uri",uri);
	}
	
	public ContentItem getContentItemByUriIncludeDeleted(String uri) {
		return _getContentItemByQuery("contentItemService.byUriIncludeDeleted","uri",uri);
	}


	/**
	 * Return a content item by the title of the resource that is associated
	 * with it. Obviously only works for content items with a URI resource, not
	 * with anonymous resources.
	 * 
	 * @param title the title to search for
	 * @return the content item with the given title; if there is more than one result, 
	 *         returns an arbitrary content item of the result set
	 */
	public ContentItem getContentItemByTitle(String title) {
		return _getContentItemByQuery("contentItemService.byTitle","title",title);
	}

	/* (non-Javadoc)
	 * @see kiwi.api.content.ContentItemService#getContentItemByAuthor(kiwi.model.user.User)
	 */
	public List<ContentItem> getContentItemByAuthor(User user) {
		javax.persistence.Query q = entityManager.createNamedQuery("contentItemService.byAuthor");
		q.setParameter("login",user.getLogin());
		return q.getResultList();
	}	

	
	/* (non-Javadoc)
	 * @see kiwi.api.content.ContentItemService#getContentItemTaggedByUser(kiwi.model.user.User)
	 */
	public List<ContentItem> getContentItemTaggedByUser(User user) {
		javax.persistence.Query q = entityManager.createNamedQuery("contentItemService.taggedByUser");
		q.setParameter("login",user.getLogin());
		return q.getResultList();
	}

	/**
	 * Query for a single content item using the named query passed as first argument and setting
	 * the query parameter "parameter" to the value "value".
	 * 
	 * @param namedQuery the named query to use for querying in the entityManager
	 * @param parameter the name of the query parameter to set (e.g. "title", "uri", ...)
	 * @param value the value of the query parameter
	 * @return the content item returned by the named query, or null
	 */
	private ContentItem _getContentItemByQuery(String namedQuery, String parameter, String value) {
		javax.persistence.Query q = entityManager.createNamedQuery(namedQuery);
		q.setParameter(parameter,value);
		q.setMaxResults(1);
		q.setHint("org.hibernate.cacheable", true);
		try {
			ContentItem res = (ContentItem) q.getSingleResult();
			tripleStore.refresh(res, false);
			return res;
		} catch (NoResultException ex) {
			return null;
		}

	}
	
	
	/**
	 * Search function combining fulltext search, tag search, and author search.
	 * <p>
	 * We might need to expand this function for further criteria, particulary SPARQL, and possibly
	 * for paging.
	 * 
	 * @param searchQuery a lucene query string to use for fulltext search
	 * @param searchTags a list of tag titles used in the search
	 * @param searchAuthor the login of an author to search for
	 * @return a list of content items matching the given search criteria
	 */
	public List<ContentItem> searchContentItems(String searchQuery, List<String> searchTags, String searchAuthor) {
		KiWiSearchCriteria criteria = new KiWiSearchCriteria();
		if(searchQuery != null)
			criteria.setKeywords(searchQuery);
		if(searchTags != null)
			criteria.getTags().addAll(searchTags);
		if(searchAuthor != null)
			criteria.setPerson(searchAuthor);
		
		KiWiSearchResults results = solrService.search(criteria);
		
		LinkedList<ContentItem> _result = new LinkedList<ContentItem>();
		for(SearchResult r : results.getResults()) {
			if(r.getItem() != null) {
				_result.add(r.getItem());
			}
		}
		
		return _result;
	}
	
	 /** Update the text content of item using the internal KiWi format
	 * <p>
	 * and creates a new revision of the text content in case the content has changes.
	 * @param item
	 * @param content
	 * @throws TextContentNotChangedException
	 */
//	@Transactional(TransactionPropagationType.REQUIRED)
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateTextContentItem(ContentItemI item, Document content) {
		ContentItem _item = item.getDelegate();
		
		TextContent tc = new TextContent(_item);
    	tc.setXmlDocument(content);
    	
    	// To check if the content has changed, check whether both contents produce the same output for the editor
    	if(_item.getTextContent() != null && _item.getTextContent().getXmlString(true).equals(tc.getXmlString(true))) {
    		log.error("!!!!!!!!!!!!!!!!! renderingPipeline fails");
    		return;
			//throw new TextContentNotChangedException("Could not create TextContentUpdate for an unchanged text content");
		}
    	entityManager.persist(tc);
    	
    	// TODO: check whether content has changed and create version
		UpdateTextContentService utcs = (UpdateTextContentService) Component.getInstance("updateTextContentService");
		if(content != null) {
			utcs.updateTextContent(_item, tc);
		}
		
    	_item.setTextContent(tc);
		
    	Events.instance().raiseEvent(KiWiEvents.ACTIVITY_EDITCONTENTITEM, currentUser, _item);
    	Events.instance().raiseTransactionSuccessEvent(KiWiEvents.CONTENT_UPDATED, _item);

	}
	
	
//	@Transactional(TransactionPropagationType.REQUIRED)
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateTextContentWithoutStoringPipeline(ContentItemI item, String content) 
		throws TextContentNotChangedException {
		
		ContentItem _item = item.getDelegate();
		
		if(renderingPipeline.renderEditor(_item, currentUser).equals(content)) {
			log.error("!!!!!!!!!!!!!!!!! renderingPipeline fails");
			throw new TextContentNotChangedException("Could not create TextContentUpdate for an unchanged text content");
		}
		
		TextContent tc = new TextContent(_item);
		// TODO: can this cause some damage? Maybe we should at least do some regex checking
    	tc.setXmlString(content);
    	
    	// TODO: check whether content has changed and create version
		UpdateTextContentService utcs = (UpdateTextContentService)
				Component.getInstance("updateTextContentService");
		if(content != null) {
			utcs.updateTextContent(_item, tc);
		}
		
    	_item.setTextContent(tc);
		
    	Events.instance().raiseEvent(KiWiEvents.ACTIVITY_EDITCONTENTITEM, currentUser, _item);
    	Events.instance().raiseTransactionSuccessEvent(KiWiEvents.CONTENT_UPDATED, _item);
	}
	
	
	
	
	/**
	 * Create a new content item with the passed string as text content.
	 * <p>
	 * This method automatically takes care of parsing the content into the internal KiWi format.
	 * The content item and content are automatically stored in the database, and the new content
	 * item is returned.
	 * 
	 * @param content
	 */
//	@Transactional(TransactionPropagationType.REQUIRED)
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public ContentItem createTextContentItem(String content) {
		ContentItem item = createContentItem();
		
		StoringService storingPipeline = (StoringService) Component.getInstance("storingPipeline");

		TextContent tc = new TextContent(item);
		tc.setXmlString(storingPipeline.processHtmlSource(item.getResource(),content));
		tc = storingPipeline.processTextContent(item.getResource(),tc);
    	entityManager.persist(tc);
		
		UpdateTextContentService utcs = (UpdateTextContentService) Component.getInstance("updateTextContentService");
		if(content != null) {
			utcs.updateTextContent(item, tc);
		}
		
		item.setTextContent(tc);
		
		Events.instance().raiseEvent(KiWiEvents.ACTIVITY_EDITCONTENTITEM, currentUser, item);
		Events.instance().raiseTransactionSuccessEvent(KiWiEvents.CONTENT_UPDATED, item);	
		return item;
	}

	/**
	 * Update the text content of item using the html string content.
	 * <p>
	 * This method automatically takes care of parsing the content into the internal KiWi format
	 * and creates a new revision of the text content in case the content has changed.
	 * 
	 * @param item the item to update
	 * @param content a string containing the HTML content to use as new text content
	 * @throws TextContentNotChangedException 
	 */
//	@Transactional(TransactionPropagationType.REQUIRED)
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateTextContentItem(ContentItemI item, String content)  {

		ContentItem _item = item.getDelegate();
		
		if(renderingPipeline.renderEditor(_item, currentUser).equals(content)) {
			log.error("!!!!!!!!!!!!!!!!! renderingPipeline fails");
			return;
			//throw new TextContentNotChangedException("Could not create TextContentUpdate for an unchanged text content");
		}
		StoringService storingPipeline = (StoringService) Component.getInstance("storingPipeline");
		
		TextContent tc = new TextContent(_item);
		tc.setXmlString(storingPipeline.processHtmlSource(_item.getResource(),content));
    	tc = storingPipeline.processTextContent(_item.getResource(),tc);
    	
    	entityManager.persist(tc);
    	
    	// TODO: check whether content has changed and create version
    	// TODO: maybe we can check this even before the pipeline runs?
		UpdateTextContentService utcs = (UpdateTextContentService) Component.getInstance("updateTextContentService");
		if(content != null) {
			utcs.updateTextContent(_item, tc);
		}
		
    	_item.setTextContent(tc);
		
    	Events.instance().raiseEvent(KiWiEvents.ACTIVITY_EDITCONTENTITEM, currentUser, _item);
		Events.instance().raiseTransactionSuccessEvent(KiWiEvents.CONTENT_UPDATED, _item);	
	}
	
	
	
	
	/**
	 * Create a new multimedia content item, using the data passed as argument.
	 * 
	 * @param data the data as byte array
	 * @param mimeType the mime type in string format
	 * @param fileName the file name of the media content (for downloading)
	 */
//	@Transactional(TransactionPropagationType.REQUIRED)
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public ContentItem createMediaContentItem(byte[] data, String mimeType, String fileName) {
		
		ContentItem contentItem = createContentItem();
		
		UpdateMediaContentService updateMediaContentService = 
			(UpdateMediaContentService) Component.getInstance("updateMediaContentService");
		
		MediaContent mediaContent = new MediaContent(contentItem);
		mediaContent.setData(data);
		mediaContent.setMimeType(mimeType);
		mediaContent.setFileName(fileName);
		entityManager.persist(mediaContent);
		
		updateMediaContentService.createMediaContentUpdate(contentItem, mediaContent);
//		entityManager.persist(mediaContent);
//		
//		contentItem.setMediaContent(mediaContent);
//
		saveContentItem(contentItem);
		
		Events.instance().raiseEvent(KiWiEvents.ACTIVITY_EDITCONTENTITEM, currentUser, contentItem);
		Events.instance().raiseTransactionSuccessEvent(KiWiEvents.CONTENT_UPDATED, contentItem);	
		return contentItem;
	}

	/**
	 * Update the multimedia content of the item passed as argument, using the data, mime type and
	 * filename provided. The method persists the updated content item and media content and
	 * creates a new revision for the content item.
	 * 
	 * @param item the content item containing the media data
	 * @param data the data as byte array
	 * @param mimeType the mime type in string format
	 * @param fileName the file name of the media content (for downloading)
	 */
//	@Transactional(TransactionPropagationType.REQUIRED)
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void updateMediaContentItem(ContentItem item, byte[] data, String mimeType, String fileName) {
		// TODO: this code does not do versioning!
//		if(item.getMediaContent() != null) {
//			MediaContent old = item.getMediaContent();
//			item.setMediaContent(null);
////			old.setContentItem(null);
////			entityManager.remove(old);
//		}
		
		UpdateMediaContentService updateMediaContentService = 
			(UpdateMediaContentService) Component.getInstance("updateMediaContentService");
		
//		contentItemService.updateTitle(item, fileName);
		MediaContent mediaContent = new MediaContent(item);
		mediaContent.setData(data);
		mediaContent.setMimeType(mimeType);
		mediaContent.setFileName(fileName);
		entityManager.persist(mediaContent);
		
		updateMediaContentService.createMediaContentUpdate(item, mediaContent);
		
		saveContentItem(item);
		
		Events.instance().raiseEvent(KiWiEvents.ACTIVITY_EDITCONTENTITEM, currentUser, item);
		Events.instance().raiseTransactionSuccessEvent(KiWiEvents.CONTENT_UPDATED, item);	
	}

	/**
	 * Update the title of item
	 * <br/>
	 * This method automatically takes care of parsing the content into the internal KiWi format
	 * and creates a new revision of the text content in case the content has changed.
	 * <br/>
	 * If you want to version the title, please call this method instead of setting the title manually
	 * @param item the item to update
	 * @param title a string that represents the new title
	 */
//	@Transactional(TransactionPropagationType.REQUIRED)
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void updateTitle(ContentItemI item, String title) {
		
		item.setModified(new Date());
		
		RenameContentItemService renameContentItemService = 
			(RenameContentItemService) Component.getInstance("renameContentItemService");
		
		renameContentItemService.renameContentItem(item, title);
		
		// notify other components
		Events.instance().raiseEvent(KiWiEvents.ACTIVITY_EDITCONTENTITEM, currentUser, item.getDelegate());
		Events.instance().raiseTransactionSuccessEvent(KiWiEvents.TITLE_UPDATED, item.getDelegate());

	}

	/**
	 * Store a content item in the database. This method distinguishes between content items that
	 * are already managed by the entity manager and content items that are newly created and
	 * stores them accordingly.
	 * 
	 * @param item the content item to save to the database
	 * @see kiwi.api.content.ContentItemService#saveContentItem(kiwi.model.content.ContentItem)
	 */
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public ContentItem saveContentItem(@Update ContentItem item) {
		
		// TODO: this should rather be solved declaratively by an appropriate modification of @RDF
		item.getResource().setLabel(null, item.getTitle());
		item.setModified(new Date());
		
		// TODO: this method should raise an event to invalidate all caches that are
		// currently holding an instance of this content item
		if(item.getId() == null) {
			log.debug("Persisting new ContentItem #0 ", item);
			kiwiEntityManager.persist(item);
		} else if(!entityManager.contains(item)){
			log.info("Merging ContentItem #0 ", item.getId());
			
			item = entityManager.merge(item);
		} else {
			log.debug("Do nothing with ContentItem #0 ", item.getId());
		}
		
		// if there is multimedia data in this content item, extract it...
		if(item.getMediaContent() != null) {
			MultimediaService ms = (MultimediaService) Component.getInstance("multimediaService");
			ms.extractMetadata(item);
		}
		return item;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.content.ContentItemService#getLastTextContentUpdate(kiwi.model.content.ContentItem)
	 */
	@SuppressWarnings("unchecked")
	public TextContentUpdate getLastTextContentUpdate(ContentItem item) {
		javax.persistence.Query q = entityManager.createNamedQuery(
				"kiwiEntityManager.queryLastTextContentUpdates");
		q.setParameter("ci",item);
		List<TextContentUpdate> tcus = (List<TextContentUpdate>) q.getResultList();
		if(tcus != null && tcus.size() > 0) {
			return tcus.get(0);
		} else {
			return null;
		}
	}
	
	public void removeContentItem(@Delete ContentItem item) {
		kiwiEntityManager.remove(item);
		
		Events.instance().raiseEvent(KiWiEvents.ACTIVITY_DELETECONTENTITEM, currentUser, item);
		Events.instance().raiseTransactionSuccessEvent(KiWiEvents.ITEM_REMOVED, item);
		// TODO add contentitem to updateTransactionBean
	}	
	
	
	/**
	 * Refresh content item when metadata is updated
	 */
	@Observer(KiWiEvents.METADATA_UPDATED)
	public void listenMetadataUpdate(ContentItem item) {
		tripleStore.refresh(item, true);
	}
	
	/**
	 * TODO: Check whether this method already exist and remove this.
	 * @param contentItem
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getTagLabelsByContentItemAndAuthor(ContentItem contentItem, User user) {
		javax.persistence.Query q = entityManager.createNamedQuery("contentItemService.getTagLabelsByContentItemAndAuthor");
	    q.setParameter("contentItemId", contentItem.getId());
	    q.setParameter("author", user);
	    return q.getResultList();
	}
}
