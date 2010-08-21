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
package kiwi.api.content;

import java.util.Date;
import java.util.List;
import java.util.Set;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.exception.TextContentNotChangedException;
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;
import kiwi.model.user.User;
import nu.xom.Document;

/**
 * The ContentItemService provides common methods for accessing, querying, creating, and storing
 * ContentItems in KiWi.
 * 
 * @author Sebastian Schaffert
 *
 */
public interface ContentItemService {

		
	/**
	 * Create a new content item with the current system's base URI and a random unique postfix.
	 * No text or media content is created (see createMediaItem and createTextItem below).
	 * <p>
	 * This method is guaranteed to always return a new content item. The new content item is
	 * automatically persisted in the database when this method is called.
	 * 
	 * @return a newly initialised content item that can be used for further operations
	 */
	public ContentItem createContentItem();
	
	/**
	 * Create or return a new content item with the current system's base URI and the uriPostfix appended.
	 * No text or media content is created (see createMediaItem and createTextItem below).
	 * <p>
	 * If the URI passed as argument identifies an already existing content item, this content item
	 * is returned. If the URI does not yet identify a content item, a new content item with the
	 * passed URI is created and persisted to the database.
	 * 
	 * @param uriPostfix the postfix of the URI to use; should not start with /
	 * @return a newly initialised content item that can be used for further operations
	 */
	public ContentItem createContentItem(String uriPostfix);
	
	
	/**
	 * Create or return a non-local content item (i.e. with a URI that does not resolve to the current 
	 * system's base URI.
	 * <p>
	 * If the URI passed as argument identifies an already existing content item, this content item
	 * is returned. If the URI does not yet identify a content item, a new content item with the
	 * passed URI is created and persisted to the database.
	 * 
	 * @param uri the uri of the external content item to create
	 * @return a newly initialised content item that can be used for further operations
	 */
	public ContentItem createExternContentItem(String uri);
	
	
	/**
	 * Create a new multimedia content item, using the data passed as argument.
	 * 
	 * @param data the data as byte array
	 * @param mimeType the mime type in string format
	 * @param fileName the file name of the media content (for downloading)
	 */
	public ContentItem createMediaContentItem(byte[] data, String mimeType, String fileName);
	
	
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
	public void updateMediaContentItem(ContentItem item, byte[] data, String mimeType, String fileName);
	
	/**
	 * Create a new content item with the passed string as text content.
	 * <p>
	 * This method automatically takes care of parsing the content into the internal KiWi format.
	 * The content item and content are automatically stored in the database, and the new content
	 * item is returned.
	 * 
	 * @param content
	 */
	public ContentItem createTextContentItem(String content);
	
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
	public void updateTextContentItem(ContentItemI item, String content);
	
	
	/**
	 * Update the text content of item using the internal KiWi format
	 * <p>
	 * and creates a new revision of the text content in case the content has changes.
	 * @param item
	 * @param content
	 * @throws TextContentNotChangedException
	 */
	public void updateTextContentItem(ContentItemI item, Document content);
	
	
	
	/**
	 * Return a set of all content items in the KiWi system. Should be used only rarely
	 * because it can potentially be a very expensive operation.
	 * 
	 * @return a set of all content items in the KiWi system
	 */
	public Set<ContentItem> getContentItems();
	
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
	public ContentItem getContentItemByKiwiId(String kiwiId);
	
	/**
	 * Return a content item by the title of the resource that is associated
	 * with it. Obviously only works for content items with a URI resource, not
	 * with anonymous resources.
	 * 
	 * @param title the title to search for
	 * @return the content item with the given title; if there is more than one result, 
	 *         returns an arbitrary content item of the result set
	 */
	public ContentItem getContentItemByTitle(String title);
	
	/**
	 * Return a content item by the URI of the resource that is associated with
	 * it. Obviously only works for content items with a URI resource, not with
	 * anonymous resources.
	 * 
	 * @param uri the uri of the resource associated with the content item
	 * @return the content item with the given URI, or null if it does not exist
	 */
	public ContentItem getContentItemByUri(String uri);
	
	public ContentItem getContentItemByUriIncludeDeleted(String uri);
	
	
	/**
	 * Return a content item by the AnonID of the resource that is associated
	 * with it. Obviously only works for content items with a anonymous
	 * resource, not with URI resources.
	 * 
	 * @param anonId the internal id of this anonymous resource
	 * @return the content item with the given anonymous id
	 */
	public ContentItem getContentItemByAnonId(String anonId);

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
	 * @see KiWiEntityManager.search
	 */
	public List<ContentItem> searchContentItems(String searchQuery, List<String> searchTags, String searchAuthor);
	
	/**
	 * TODO: what is the purpose of this method? It is called by ComponentSavelet and by DataAccessService,
	 *       but is it necessary in these cases to actually have it in ContentItemService?
	 * 
	 * Use updateTextContentItem (ContentItemI, xom.Document content) instead.
	 * 
	 * @param item
	 * @param content
	 * @throws TextContentNotChangedException
	 */
	@Deprecated
	public void updateTextContentWithoutStoringPipeline(ContentItemI item, String content) throws TextContentNotChangedException;
	
	
	public void updateTitle(ContentItemI item, String title);
	
	/**
	 * Store a content item in the database. This method distinguishes between content items that
	 * are already managed by the entity manager and content items that are newly created and
	 * stores them accordingly.
	 * 
	 * @param item the content item to save to the database
	 */
	public ContentItem saveContentItem(ContentItem item);


	
	/**
	 * Remove a content item. This method mainly marks the content item as deleted and then 
	 * saves it through the kiwiEntityManager.
	 */
	public void removeContentItem(ContentItem item);
	
	
	// event listeners
	public void listenMetadataUpdate(ContentItem item);
	
	/**
	 * Lists all content items authored by an user.
	 * @param user
	 * @return
	 */
	public List<ContentItem> getContentItemByAuthor(User user);
	
	/**
	 *  Lists all content items tagged by an user.
	 * @param user
	 * @return
	 */
	public List<ContentItem> getContentItemTaggedByUser(User user);
	
	/**
	 * Loads the tag labels of a given content item
	 * @param contentItem
	 * @return
	 */
	public List<String> getTagLabelsByContentItemAndAuthor(ContentItem contentItem, User user);
	
	
	public List<ContentItem> getContentItemsByDate(Date since, Date until);

}
