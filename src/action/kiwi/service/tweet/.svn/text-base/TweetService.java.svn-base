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
package kiwi.service.tweet;

import java.util.UUID;

import javax.ejb.Stateless;

import kiwi.api.content.ContentItemService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.tweet.TweetServiceLocal;
import kiwi.api.tweet.TweetServiceRemote;
import kiwi.exception.TextContentNotChangedException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;

/**
 * A component that allows to post tweets to the stream of activities.
 * 
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("tweetService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class TweetService implements TweetServiceLocal, TweetServiceRemote {

	@Logger
	private Log log;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private TripleStore tripleStore;
	
	/**
	 * Create a microblogging post of user with content "message". Creates a new content item
	 * with author "user" and title "message" and body "message" and stores it in the entity manager.
	 * The content item will have the type kiwi:Tweet.
	 * <p>
	 * On completion, the event activity.user is raised.
	 * 
	 * @param user the user posting the tweet
	 * @param message the message of the tweet.
	 */
	@Override
	public void postTweet(User user, String message) {
		ContentItem tweet = contentItemService.createContentItem(user.getLogin()+"/tweet/"+UUID.randomUUID().toString());
		
		contentItemService.updateTitle(tweet, message);
		tweet.setAuthor(user);
		contentItemService.updateTextContentItem(tweet, message);
		
		
		KiWiUriResource type = tripleStore.createUriResource(Constants.NS_KIWI_CORE+"KiWiTweet");
		
		tweet.getResource().addType(type);

		contentItemService.saveContentItem(tweet);
		
		Events.instance().raiseEvent("activity.tweet", user, tweet);
	}

}
