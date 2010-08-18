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
package kiwi.dashboard.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.config.ConfigurationService;
import kiwi.api.importexport.ImportService;
import kiwi.api.socialnetworking.TwitterService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.AuthenticationFailedException;
import kiwi.model.Constants;
import kiwi.model.importexport.ImportTask;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.util.MD5;
import net.unto.twitter.TwitterProtos;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJsonRestClient;

/**
 * IntegrationAction - implements integration with other social software platforms like Facebook,
 * Twitter, ... and allows users to subscribe to their status updates and blog posts.
 *
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.dashboard.integrationAction")
@Scope(ScopeType.PAGE)
public class IntegrationAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* update twitter feeds every 5 minutes */
	public static final int TWITTER_IMPORT_INTERVAL = 5;
	
	/* update facebook feeds every 5 minutes */
	public static final int FACEBOOK_IMPORT_INTERVAL = 5;
	
	/* update blog feeds every 30 minutes */
	public static final int BLOG_IMPORT_INTERVAL = 30;

	@Logger
	private Log log;
	
	@In
	private FacesMessages facesMessages;
	
	@In("kiwi.core.twitterService")
	private TwitterService twitterService;
	
	@In
	private EntityManager entityManager;
	
	@In
	private TripleStore tripleStore;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private User currentUser;
	
	
	@In("kiwi.core.importService")
	private ImportService importService;
	
	private String twitterLogin, twitterPassword;
	
	private String blogRSSUrl;
	
	private List<ImportTask> tasks;

	
	/**
	 * Take over profile data from twitter and update the KiWi user profile with the data. Uses
	 * the twitterLogin and twitterPassword parameters to authenticate with Twitter.
	 * 
	 * @param overwrite if true, existing profile data will be overwritten with values from twitter;
	 *        if false, only undefined features will be filled
	 */
	public void addTwitterProfile(boolean overwrite) {
		
	}
	
	/**
	 * Subscribe to the user's twitter feed using the twitterLogin and twitterPassword parameters.
	 */
//	@Transactional
	public void addTwitterSubscription() {
		long id = 0;
		try {
			TwitterProtos.User tu = twitterService.getPrivateUserInfo(twitterLogin, twitterPassword);
			
			log.debug("Twitter User: #0 (#1)",tu.getId(), tu.getScreenName());

			id = tu.getId();
			
			// construct the URL of the twitter feed from the user id and
			URL rss_url = new URL("http://twitter.com/statuses/user_timeline/"+id+".rss");
			
			Date interval = new Date(TWITTER_IMPORT_INTERVAL * 60 * 1000);
			
			KiWiUriResource type = tripleStore.createUriResource(Constants.NS_KIWI_CORE+"TwitterPost");
			
			importService.scheduleImport("Twitter Feed for Twitter User "+tu.getScreenName(), 
					                     interval, 
					                     rss_url, 
					                     "application/rss+xml", 
					                     Collections.singleton(type), 
					                     Collections.EMPTY_SET, 
					                     currentUser);
			
			entityManager.flush();
			
			facesMessages.add("Import of tweets for Twitter user #0 scheduled for importing every 5 minutes (RSS feed: #1)",tu.getScreenName(), rss_url.toString());
			
		} catch (AuthenticationFailedException e) {
			facesMessages.add("Twitter authentication failed; wrong login or password");
		} catch (MalformedURLException e) {
			facesMessages.add("Could not parse Twitter URL (http://twitter.com/statuses/user_timeline/"+id+".rss)");
		}
	}	
	
	/**
	 * Subscribe to the user's facebook status updates (only if the user has logged in using 
	 * Facebook connect?)
	 */
//	@Transactional
	public void addFacebookSubscription() {
		
		FacebookJsonRestClient fbClient = (FacebookJsonRestClient)Contexts.getSessionContext().get("facebook.user.client");

		if(fbClient != null) {
			// these are set by FacebookUserFilter in case a facebook connection has been established;
			// if no facebook connection is there, we issue an error and ask the user to connect with
			// facebook first
			String app_id = (String)Contexts.getSessionContext().get(Constants.FACEBOOK_APP_ID);
			String api_key = (String)Contexts.getSessionContext().get(Constants.FACEBOOK_API_KEY);
			String session_key = (String)Contexts.getSessionContext().get(Constants.FACEBOOK_SESSION_KEY);
			String secret = (String)Contexts.getSessionContext().get(Constants.FACEBOOK_API_SECRET);
			
			
	
			
			
			try {
				final long userId = fbClient.users_getLoggedInUser();
				
				// compute MD5 signature to login; 
				// see http://wiki.developers.facebook.com/index.php/Using_Activity_Streams
				String sig_input = "app_id="+app_id+"session_key="+session_key+"source_id="+userId+secret;
	
				String sig_md5 = MD5.md5sum(sig_input);
				
				String facebookUrl = "http://www.facebook.com/activitystreams/feed.php?source_id=" +
									 userId + 
									 "&app_id=" +
									 app_id + 
									 "&session_key=" + 
									 session_key +
									 "&sig=" + 
									 sig_md5 + 
									 "&v=0.7&read";
				
				log.info("Facebook activity stream url: #0", facebookUrl);
				
				URL url = new URL(facebookUrl);
				URLConnection con = url.openConnection();
				
				if(con instanceof HttpURLConnection) { 
					int code = ((HttpURLConnection)con).getResponseCode();
					
					switch(code) {
					case 401:
						facesMessages.add("Not scheduling facebook import; required parameter missing (URL: #0)",facebookUrl); 
						return;
					case 403:
						facesMessages.add("Not scheduling facebook import; user hasn't granted the required permission");
						return;
					case 404:
						facesMessages.add("Not scheduling facebook import; signature incorrect or session key invalid (URL: #0)",facebookUrl);
						return;
					}
				}
				
				Date interval = new Date(FACEBOOK_IMPORT_INTERVAL * 60 * 1000);

				KiWiUriResource type = tripleStore.createUriResource(Constants.NS_KIWI_CORE+"FacebookPost");

				importService.scheduleImport("Facebook Feed for User "+currentUser.getFirstName() + " " + currentUser.getLastName(), 
						interval, 
						url, 
						"application/atom+xml", 
						Collections.singleton(type), 
						Collections.EMPTY_SET, 
						currentUser);

				entityManager.flush();
				
				facesMessages.add("Import of Facebook Feed for user #0 #1 scheduled for importing every 30 minutes",currentUser.getFirstName(), currentUser.getLastName());
				

			} catch(FacebookException ex) {
				log.error("error while retrieving facebook response",ex);
				facesMessages.add("the response received from Facebook is invalid; please try again later");

			} catch (IOException e) {
				log.error("error while retrieving facebook response",e);
				facesMessages.add("the response received from Facebook is invalid; please try again later");
			}
		} else {
			facesMessages.add("no Facebook connection established; subscribing to Facebook feeds is currently only available to users logged in via Facebook");
		}
	}
	
	/**
	 * Subscribe to the RSS feed of the user's blog. 
	 */
//	@Transactional
	public void addBlogSubscription() {
		try {
			URL rss_url = new URL(blogRSSUrl);

			// test the connection now ...
			URLConnection con = rss_url.openConnection();
			
			if(con instanceof HttpURLConnection && ((HttpURLConnection)con).getResponseCode() != 200) {
				facesMessages.add("Not scheduling blog import; could not connect to HTTP URL (response message; #0)",((HttpURLConnection)con).getResponseMessage());
				return;
			}
			
			Date interval = new Date(BLOG_IMPORT_INTERVAL * 60 * 1000);

			KiWiUriResource type = tripleStore.createUriResource(Constants.NS_KIWI_CORE+"BlogPost");

			importService.scheduleImport("Blog Feed for User "+currentUser.getFirstName() + " " + currentUser.getLastName(), 
					interval, 
					rss_url, 
					"application/rss+xml", 
					Collections.singleton(type), 
					Collections.EMPTY_SET, 
					currentUser);

			entityManager.flush();
			
			facesMessages.add("Import of blog for user #0 #1 scheduled for importing every 30 minutes (RSS feed: #2)",currentUser.getFirstName(), currentUser.getLastName(), rss_url.toString());
		} catch (MalformedURLException e) {
			facesMessages.add("Could not parse Blog URL (#0)",blogRSSUrl);
		} catch (IOException e) {
			facesMessages.add("Not scheduling blog import; could not connect to HTTP URL (I/O error: #0)",e.getMessage());
		}
		
	}
	
	
	public void removeSubscription() {
		
	}

	public TwitterService getTwitterService() {
		return twitterService;
	}

	public void setTwitterService(TwitterService twitterService) {
		this.twitterService = twitterService;
	}

	public String getTwitterLogin() {
		return twitterLogin;
	}

	public void setTwitterLogin(String twitterLogin) {
		this.twitterLogin = twitterLogin;
	}

	public String getTwitterPassword() {
		return twitterPassword;
	}

	public void setTwitterPassword(String twitterPassword) {
		this.twitterPassword = twitterPassword;
	}

	public String getBlogRSSUrl() {
		return blogRSSUrl;
	}

	public void setBlogRSSUrl(String blogRSSUrl) {
		this.blogRSSUrl = blogRSSUrl;
	}
	
	public List<ImportTask> getTasks() {
		if(tasks == null) {
			Query q = entityManager.createNamedQuery("importTask.listTasksByUser");
			q.setParameter("user", currentUser);
			tasks = q.getResultList();
		}
		return tasks;
	}

	
	public String getTaskIcon(ImportTask t) {
		for(KiWiUriResource r : t.getTypes()) {
			if(r.getUri().equals(Constants.NS_KIWI_CORE+"BlogPost")) {
				return "/img/rss_logo-small.png";
			} else if(r.getUri().equals(Constants.NS_KIWI_CORE+"TwitterPost")) {
				return "/img/twitter-logo-small.png";
			} else if(r.getUri().equals(Constants.NS_KIWI_CORE+"FacebookPost")) {
				return "/img/facebook-logo-small.png";
			}
		}
		return "/img/rss_logo-small.png";
	}
	
	public String getTaskType(ImportTask t) {
		for(KiWiUriResource r : t.getTypes()) {
			if(r.getUri().equals(Constants.NS_KIWI_CORE+"BlogPost")) {
				return "Blog";
			} else if(r.getUri().equals(Constants.NS_KIWI_CORE+"TwitterPost")) {
				return "Twitter";
			} else if(r.getUri().equals(Constants.NS_KIWI_CORE+"FacebookPost")) {
				return "Facebook";
			}
		}
		return "Unknown";
		
	}
	
	/**
	 * Delete a import task from the system.
	 * @param t
	 */
	public void removeTask(ImportTask t) {
		tasks.remove(t);
		entityManager.remove(t);
		entityManager.flush();
	}
	
	public String formatUrl(String url) {
		if(url.startsWith("http://www.facebook.com/activitystreams")) {
			return "http://www.facebook.com/activitystreams/feed.php";
		} else if(url.length() > 50) {
			return url.substring(0, 47) + "...";
		} else {
			return url;
		}
	}
}
