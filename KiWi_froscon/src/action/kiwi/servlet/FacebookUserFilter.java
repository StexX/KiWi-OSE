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
package kiwi.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kiwi.model.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJsonRestClient;

/**
 * The Facebook User Filter ensures that a Facebook client that pertains to
 * the logged in user is available in the session object named "facebook.user.client".
 * 
 * The session ID is stored as "facebook.user.session". It's important to get
 * the session ID only when the application actually needs it. The user has to 
 * authorise to give the application a session key.
 * 
 * @author Dave
 */
public class FacebookUserFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(FacebookUserFilter.class);

	private String api_key;
	private String secret;
	private String appid;


	public void init(FilterConfig filterConfig) throws ServletException {
		api_key = filterConfig.getServletContext().getInitParameter("facebook_api_key");
		secret = filterConfig.getServletContext().getInitParameter("facebook_secret");
		appid = filterConfig.getServletContext().getInitParameter("facebook_app_id");
		if(api_key == null || secret == null) {
			throw new ServletException("Cannot initialise Facebook User Filter because the " +
					"facebook_api_key or facebook_secret context init " +
					"params have not been set. Check that they're there " +
			"in your servlet context descriptor.");
		} else {
			logger.info("Using facebook API key: " + api_key);
		}
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		HttpSession session = request.getSession(true);
		
		String oldSessionKey = (String)session.getAttribute(Constants.FACEBOOK_SESSION_KEY);
		String sessionKey = getCookieValue(api_key+"_session_key",request);

		if(session.getAttribute(Constants.FACEBOOK_USER_CLIENT) == null || 
				(sessionKey != null && !sessionKey.equals(oldSessionKey))  ) {
	
			if(sessionKey != null) {
				// we have a logged in facebook user
				
				FacebookJsonRestClient userClient = new FacebookJsonRestClient(api_key,secret,sessionKey);
				
				// store facebook client and attributes in session
				session.setAttribute(Constants.FACEBOOK_USER_CLIENT, userClient);
				session.setAttribute(Constants.FACEBOOK_SESSION_KEY,sessionKey);
				session.setAttribute(Constants.FACEBOOK_API_KEY,api_key);
				session.setAttribute(Constants.FACEBOOK_APP_ID,appid);
				session.setAttribute(Constants.FACEBOOK_API_SECRET,secret);
				
				long facebookUserID;
				try {
					facebookUserID = userClient.users_getLoggedInUser();
					
					logger.info("logged in facebook user with ID {}",facebookUserID);
				} catch(FacebookException ex) {
					//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while fetching user's facebook ID");
					logger.error("Error while getting cached (supplied by request params) value " +
							"of the user's facebook ID or while fetching it from the Facebook service " +
							"if the cached value was not present for some reason. Cached value = {}", userClient.getCacheUserId());
					//return;
				}		
			}
		}
		
		chain.doFilter(request, response);
	}

	
	public static String getCookieValue(String name, HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		
		if(cookies != null) {
			for(Cookie c : cookies) {
				if(c.getName().equals(name)) {
					return c.getValue();
				}
			}
		}
		
		return null;
	}


	public static FacebookJsonRestClient getUserClient(HttpSession session) {
		return (FacebookJsonRestClient)session.getAttribute(Constants.FACEBOOK_USER_CLIENT);
	}
}