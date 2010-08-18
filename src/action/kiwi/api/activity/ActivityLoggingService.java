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
package kiwi.api.activity;

import java.util.Set;

import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

/**
 * A service that takes care of logging user activities in the KiWi system. 
 * Implemented using Seam observers that listen for certain activity events:
 * <ul>
 * <li>TODO</li>
 * </ul>
 * 
 * @author Sebastian Schaffert
 * @see kiwi.model.activity.Activity
 */
public interface ActivityLoggingService {

	
	/**
	 * Register a tagging action performed by a user.
	 * 
	 * @param user the user who added the tag
	 * @param tag the tag that has been added
	 */
	public void tagAdded(User user, Tag tag);
	
	/**
	 * Register the removal of a tag performed by a user.
	 * 
	 * @param user the user who removed the tag
	 * @param tag the tag that has been removed
	 */
	public void tagRemoved(User user, Tag tag);
	
	/**
	 * Register the creation of a new content item by a user.
	 * 
	 * @param user the user who created the new content item
	 * @param item the item that has been created by the user
	 */
	public void contentItemCreated(User user, ContentItem item);
	
	/**
	 * Register editing of a content item performed by a user.
	 * 
	 * @param user the user who edited the content item
	 * @param item the content item that has been edited
	 */
	public void contentItemEdited(User user, ContentItem item);
	
	/**
	 * Register the deletion of a content item by a user.
	 * 
	 * @param user the user who deleted the content item
	 * @param item the content item has been deleted
	 */
	public void contentItemDeleted(User user, ContentItem item);
	
	/**
	 * Register that a user has visited a content item (content item has been currentContentItem).
	 * 
	 * @param user the user who visited the ContentItem
	 * @param item the content item that has been visited
	 */
	public void contentItemVisited(User user, ContentItem item);
	
	/**
	 * Register that a user has shared a certain content item with a set of other users.
	 * 
	 * @param user the user who shared the content item
	 * @param item the content item that has been shared
	 * @param sharedWith the users this content item has been shared with
	 */
	public void contentItemShared(User user, ContentItem item, Set<User> sharedWith);

	
	/**
	 * Register that a user has annotated a certain content item with semantic metadata
	 * 
	 * @param user the user who annotated the content item
	 * @param item the content item that has been annotated
	 */
	public void contentItemAnnotated(User user, ContentItem item);
	
	/**
	 * Register that a user has logged in to the KiWi system.
	 * 
	 * @param user the user who logged in to the KiWi system
	 */
	public void userLogin(User user);
	
	/**
	 * Register that a user has logged out from the KiWi system.
	 * 
	 * @param user the user who has logged out of the KiWi system
	 */
	public void userLogout(User user);
	
	/**
	 * Register that a user has registered with the KiWi system.
	 * 
	 * @param user the user who has registered with the KiWi system
	 */
	public void userRegistered(User user);
	
	/**
	 * Register that a user has performed a free-form activity inside the KiWi system
	 * that he manually specified. The activity is passed as argument "message".
	 * 
	 * @param user the user who performed the activity
	 * @param message the description of the activity performed
	 */
	public void tweetActivity(User user, ContentItem message);

	
	/**
	 * Register that a user has performed a search inside the KiWi system. The search string
	 * must be passed as argument to this method.
	 * 
	 * @param user the user who performed the search
	 * @param search the search string used by the user
	 */
	public void searchPerformed(User user, String search);
	
	/**
	 * Register that a user has added a comment to a content item. The user, the commented
	 * content item and the comment are passed as arguments.
	 * 
	 * @param user the user who added the comment
	 * @param parent the parent content item to which the comment was added
	 * @param comment the content item used as comment
	 */
	public void commentAdded(User user, ContentItem parent, ContentItem comment);
	
	/**
	 * Register that a user has established a connection to a friend.
	 * 
	 * @param user the user who established the connection
	 * @param friend the user added as contact
	 */
	public void friendAdded(User user, User friend);
	
	/**
	 * Register that a user has removed a connection to another user.
	 * 
	 * @param user the user removing the connection
	 * @param friend the user adding the connection
	 */
	public void friendRemoved(User user, User friend);
}
