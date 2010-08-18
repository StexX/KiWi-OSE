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


package kiwi.service.socialnetworking;

import java.util.List;

import javax.ejb.Stateless;

import kiwi.api.socialnetworking.FacebookServiceLocal;
import kiwi.api.socialnetworking.FacebookServiceRemote;

import org.apache.shindig.social.opensocial.model.Message;
import org.apache.shindig.social.opensocial.model.Person;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJsonRestClient;

/**
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.core.facebookService")
@AutoCreate
@Stateless
@Scope(ScopeType.STATELESS)
public class FacebookServiceImpl implements FacebookServiceLocal, FacebookServiceRemote {

	
	private static FacebookJsonRestClient getUserClient() {
		// initialised in FacebookUserFilter
		return (FacebookJsonRestClient)Contexts.getSessionContext().get("facebook.user.client");
	}
	
	public boolean isFacebookUserLoggedIn() {
		try {
			return getUserClient() != null && getUserClient().users_getLoggedInUser() > 0;
		} catch(FacebookException ex) {
			return false;
		}
	}

	@Override
	public Person getPrivateUserInfo(String screenName, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person getPublicUserInfo(String screenName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Message> getStatusMessages(String screenName, String password,
			int count) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
