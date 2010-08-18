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
package kiwi.service.socialnetworking;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;

import kiwi.api.socialnetworking.TwitterServiceLocal;
import kiwi.api.socialnetworking.TwitterServiceRemote;
import kiwi.exception.AuthenticationFailedException;
import net.unto.twitter.Api;
import net.unto.twitter.TwitterProtos.Results;
import net.unto.twitter.TwitterProtos.User;
import net.unto.twitter.TwitterProtos.Results.Result;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * TwitterServiceImpl
 *
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.core.twitterService")
@AutoCreate
@Stateless
@Scope(ScopeType.STATELESS)
public class TwitterServiceImpl implements TwitterServiceLocal, TwitterServiceRemote {

	/* (non-Javadoc)
	 * @see kiwi.api.socialnetworking.TwitterService#searchTweets(java.lang.String)
	 */
	@Override
	public List<Result> searchTweets(String query) {
		LinkedList<Result> result = new LinkedList<Result>();
		
		Api api = Api.builder().build();

		 Results results = api.search("@dewitt").build().get();
		 for (Result tweet : results.getResultsList()) {
			 result.add(tweet);
		 }

		
		return result;
	}

	@Override
	public User getPrivateUserInfo(String login, String password) throws AuthenticationFailedException {
		User user;
		try {
			// get a Twitter API connection 
			Api api = Api.builder().username(login).password(password).build();
			
			user = api.verifyCredentials().build().get();
		} catch(Exception ex) {
			throw new AuthenticationFailedException("error while connecting to twitter");
		}
		
		if(user == null) {
			throw new AuthenticationFailedException("authentication failed for user "+login);
		} else {
			return user;
		}
	}

	@Override
	public User getPublicUserInfo(String screenName) {
		Api api = Api.builder().build();
		
		return api.showUser().screenName(screenName).build().get();
	}

	
	
}
