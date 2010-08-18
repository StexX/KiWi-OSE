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
package kiwi.api.socialnetworking;

import java.util.List;

import org.apache.shindig.social.opensocial.model.Message;
import org.apache.shindig.social.opensocial.model.Person;

/**
 * A unified interface for accessing social networking services like Facebook, Twitter, or 
 * OpenSocial services. In order to offer a unified view on different social networking services,
 * we make use of the OpenSocial API implementation for social networking containers as offered
 * by Apache Shindig.
 * 
 * @author Sebastian Schaffert
 *
 */
public interface SocialNetworkingService {

	/**
	 * Get the public user information of the user with the given screen name (if available).
	 * Returns null if no user information for the given screen name is publicly available; for
	 * some social networking services, this method may always return null
	 * 
	 * @param screenName the screen name of the user whose information to retrieve
	 * @return an OpenSocial Person object; fields that are not available in the respective social
	 *         networking service will return null as value
	 */
	public Person getPublicUserInfo(String screenName);

	
	/**
	 * Get the private user information of the user authenticated with screenName and password.
	 * 
	 * @param screenName the screen name of the user whose information to retrieve
	 * @param password the password of the user whose information to retrieve
	 * @return an OpenSocial Person object; fields that are not available in the respective social
	 *         networking service will return null as value
	 */
	public Person getPrivateUserInfo(String screenName, String password);
	
	/**
	 * Get the status messages of the user identified by the given screen name. Returns a list of
	 * OpenSocial Message objects containing the status messages, or an empty list if no status
	 * messages are available for the given user.
	 * <p>
	 * For public sites like Facebook, the status messages should always be available, and the
	 * password can be omitted. For closed sites like  Facebook, retrieving the information might 
	 * require that the user is logged in and grants permission to retrieve the status messages in 
	 * advance.
	 * <p>
	 * 
	 * 
	 * @param screenName the screen name of the user whose status messages to retrieve
	 * @param password   the password of the user whose status messages to retrieve
	 * @param count      the maximum number of status messages to retrieve
	 * @return a list of OpenSocial Message objects representing the last status updates
	 */
	public List<Message> getStatusMessages(String screenName, String password, int count);
}
