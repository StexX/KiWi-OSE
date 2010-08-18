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
 * sschaffe
 * 
 */
package kiwi.api.socialnetworking;

import java.util.List;

import kiwi.exception.AuthenticationFailedException;
import net.unto.twitter.TwitterProtos.User;
import net.unto.twitter.TwitterProtos.Results.Result;

/**
 * A KiWi service for accessing the Twitter API. Provides methods for retrieving/updating user 
 * information and status/activity updates. It is mostly a wrapper around the somewhat awkward
 * Java Twitter API.
 *
 * @author Sebastian Schaffert
 *
 */
public interface TwitterService { // extends SocialNetworkingService {

	
	/**
	 * Return the public user information of the user identified by screenName. If there is no
	 * public information available, returns null.
	 * 
	 * @param screenName the twitter screen name of the user to get info from
	 * @return a twitter API User object representing the user with the given screen name, or null 
	 *         if the user does not exist or does not have a public profile
	 */
	public User getPublicUserInfo(String screenName);
	
	/**
	 * Return the private user information of the user authenticated by the given login and
	 * password. If authentication fails, an AuthenticationFailedException is thrown.
	 * 
	 * @param login     the twitter login of the user to get information for
	 * @param password  the password of the user to get information for
	 * @return a twitter API User object representing the user with the given screen name, or null 
	 *         if the user does not exist
	 * @throws AuthenticationFailedException when the login/password combination is wrong
	 */
	public User getPrivateUserInfo(String login, String password) throws AuthenticationFailedException;
	
	
	/**
	 * Search twitter for tweets matching a certain query. May e.g. be used by the KiWi importer
	 * for retrieving tweets.
	 * 
	 * @param query a Twitter search string
	 * @return a list of tweets as OpenSocial Message objects
	 */
	public List<Result> searchTweets(String query);
}
