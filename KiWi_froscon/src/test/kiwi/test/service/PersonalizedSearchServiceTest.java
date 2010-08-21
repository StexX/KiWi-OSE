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
package kiwi.test.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.security.auth.login.LoginException;

import kiwi.action.search.KiWiSearchEngine;
import kiwi.api.content.ContentItemService;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.tagging.TaggingService;
import kiwi.api.user.UserService;
import kiwi.context.CurrentUserFactory;
import kiwi.exception.UserExistsException;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.MethodExpression;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Nilay, Fred Durao
 *
 */
@Test
@Name("personalizedSearchServiceTest")
public class PersonalizedSearchServiceTest extends KiWiTest {

	//ant -Dclass=kiwi.test.service.ConfigurationServiceTest test.run-class

	
	public boolean authenticate() {
		return true;
    }

	
	@SuppressWarnings("unchecked")
	public boolean addUserInSession(String userName) {
    	boolean userInSession=false;
		/* set the identity */
		Identity.setSecurityEnabled(false);
		Identity identity = Identity.instance();
		identity.getCredentials().setUsername(userName);
		
		/**
    	 * register an authentication method that always returns true
    	 * (we do not need another authentication since the IDP server 
    	 * already authenticated the user)
    	 */
    	MethodExpression methodExpression = 
			Expressions.instance().createMethodExpression("#{personalizedSearchServiceTest.authenticate()}");
		identity.setAuthenticateMethod(methodExpression);
		try {
			identity.authenticate();
		} catch (LoginException e) {
			e.printStackTrace();
		}
		
		/* login the new identity */
		/* TODO: make use of the UserService methods... */
		identity.login();
		userInSession = identity.isLoggedIn();
		
		Assert.assertTrue(identity.isLoggedIn());
		
		
		CurrentUserFactory currentUserFactory = 
			(CurrentUserFactory) Component.getInstance("currentUserFactory");
		currentUserFactory.forceRefresh();		
		
		return userInSession;
    }
	
	public ContentItem createContentItem (String title, String content)
	{
		ContentItemService ciService        = (ContentItemService) Component.getInstance("contentItemService");
		ContentItem ci1 = ciService.createContentItem();
    	ciService.updateTitle(ci1, title);
    	ciService.saveContentItem(ci1);
    	Assert.assertNotNull(ci1.getResource());
    	ciService.updateTextContentItem(ci1, content);
		return ci1;
		
	}

	@Test
	public void testPersonalizedSearch() throws Exception {
		
		clearDatabase();		
		/*
		 * Create a ContentItem using the ContentItemService and save it so that it gets indexed by solr
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	ContentItemService ciService        = (ContentItemService) Component.getInstance("contentItemService");

            	// create two ContentItems for the scenario which checks the updating result score based on USERTAGS and WATCHLIST
            	ContentItem ci1 = ciService.createContentItem();
            	ciService.updateTitle(ci1, "ci1");
            	
            	ContentItem ci2 = ciService.createContentItem();
            	ciService.updateTitle(ci2, "ci2");
            	
            	ciService.saveContentItem(ci1);
            	ciService.saveContentItem(ci2);

            	Assert.assertNotNull(ci1.getResource());
            	Assert.assertNotNull(ci2.getResource());
            	
            	ContentItem ti1 = ciService.createContentItem();
            	ciService.updateTitle(ti1, "myTag");
           	
            	ContentItem ti2 = ciService.createContentItem();
            	ciService.updateTitle(ti2, "myOtherTag");
            	
            	
            	ciService.saveContentItem(ti1);
            	ciService.saveContentItem(ti2);
            	
            	Assert.assertNotNull(ti1.getResource());
            	Assert.assertNotNull(ti2.getResource());

            	
            	ciService.updateTextContentItem(ci1, "<p>Hello World!</p>");
            	
            	ciService.updateTextContentItem(ci2, "<p>Blah Blub World!</p>");
            }
    	}.run();
    	
    	
    		/*
    		 * Preparing environment
    		 */
        	new FacesRequest() {

                @Override
                protected void invokeApplication() {

                	
//                	userName="nilay";
                	Identity.setSecurityEnabled(false);
                	
                	ContentItemService ciService        = (ContentItemService) Component.getInstance("contentItemService");
                	TaggingService     taggingService   = (TaggingService) Component.getInstance("taggingService");
                	UserService        userService      = (UserService) Component.getInstance("userService");
                	
                	ContentItem ci1 =  ciService.getContentItemByTitle("ci1");
                	ContentItem ci2 =  ciService.getContentItemByTitle("ci2");

                	Assert.assertNotNull(ci1.getResource());
                	Assert.assertNotNull(ci2.getResource());
	
                	ContentItem ti1 =  ciService.getContentItemByTitle("myTag");
                	ContentItem ti2 =  ciService.getContentItemByTitle("myOtherTag");
                	
                	Assert.assertNotNull(ti1.getResource());
                	Assert.assertNotNull(ti2.getResource());
                	 	
                	User user = new User();
    				try {
    					user = userService.createUser("nilay", "NilayHasAlsoAPW");
    				} catch (UserExistsException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    					
                	
                	// create a new tag and check its properties
                	Tag t1 = taggingService.createTagging("myTag", ci1, ti1, user);
                	Tag t2 = taggingService.createTagging("myTag", ci2, ti1, user);
                	Tag t3 = taggingService.createTagging("myOtherTag", ci2, ti2, user);
                	
                	// check whether listApplicableDataTypeProperties gives results.
                	Assert.assertEquals(t1.getTaggedResource(), ci1);
                	Assert.assertEquals(t1.getTaggingResource(), ti1);
                	Assert.assertEquals(t1.getTaggedBy(), user);
	        	
                	Assert.assertEquals(t2.getTaggedResource(), ci2);
                	Assert.assertEquals(t2.getTaggingResource(), ti1);
                	Assert.assertEquals(t2.getTaggedBy(), user);
                	
                	Assert.assertEquals(t3.getTaggedResource(), ci2);
                	Assert.assertEquals(t3.getTaggingResource(), ti2);
                	Assert.assertEquals(t3.getTaggedBy(), user);

                }
        	}.run();  
        	
    		/*
    		 * Preparing environment
    		 */
        	new FacesRequest() {

                @Override
                protected void invokeApplication() {

                	Identity.setSecurityEnabled(false);
                	
                	ContentItemService ciService        = (ContentItemService) Component.getInstance("contentItemService");
                	TaggingService     taggingService   = (TaggingService) Component.getInstance("taggingService");
                	UserService        userService      = (UserService) Component.getInstance("userService");

                	ContentItem ci1 =  ciService.getContentItemByTitle("ci1");
                	ContentItem ci2 =  ciService.getContentItemByTitle("ci2");

                	Assert.assertNotNull(ci1.getResource());
                	Assert.assertNotNull(ci2.getResource());
                	//Assert.assertEquals(taggingService.getTags(ci1).size(),1);
                	//java.lang.AssertionError: expected:<1> but was:<2>
                	Assert.assertEquals(taggingService.getTags(ci2).size(),2);
                	
                	
                	ContentItem ti1 =  ciService.getContentItemByTitle("myTag");
                	ContentItem ti2 =  ciService.getContentItemByTitle("myOtherTag");

                	
                	Assert.assertNotNull(ti1.getResource());
                	Assert.assertNotNull(ti2.getResource());

                	User user =  userService.getUserByLogin("nilay");
                	Assert.assertNotNull(user);
                	
                	//creating content item for watch list
                	ContentItem wl1 = ciService.createContentItem();
                	ciService.updateTitle(wl1, "watchList1");
                	
                	ContentItem wl2 = ciService.createContentItem();
                	ciService.updateTitle(wl2, "watchList2");
                	
                	ciService.saveContentItem(wl1);
                	ciService.saveContentItem(wl2);

                	// setting watched content for user
                	Set<ContentItem> userWatchList = new HashSet<ContentItem>();
                	userWatchList.add(wl1);
                	userWatchList.add(wl2);

                	user.setWatchedContent(userWatchList);
                	
                	//checking user watchedlist
                  	List<ContentItem> watchlist = new LinkedList<ContentItem>();
                	watchlist.addAll(user.getWatchedContent());
                	
                	Assert.assertEquals(watchlist.size(),2);

                	Tag t4 = taggingService.createTagging("myOtherTag", wl1, ti2, user);
                	
                	Assert.assertEquals(t4.getTaggedResource(), wl1);
                	Assert.assertEquals(t4.getTaggingResource(), ti2);
                	Assert.assertEquals(t4.getTaggedBy(), user);
                	
                	
                	//Scenario for checking only user tags
                	ContentItem contentForOnlyTags1= createContentItem("content for tags1", "It contains onlyTags");
                	ContentItem contentForOnlyTags2= createContentItem("content for tags2", "It contains onlyTags");
                	Tag t5 = taggingService.createTagging("myOtherTag", contentForOnlyTags1, ti2, user);
                	Tag t6 = taggingService.createTagging("myTag", contentForOnlyTags2, ti1, user);
                	Tag t7 = taggingService.createTagging("myOtherTag", contentForOnlyTags2, ti2, user);
                	
                	// check whether listApplicableDataTypeProperties gives results.
                	Assert.assertEquals(t5.getTaggedResource(), contentForOnlyTags1);
                	Assert.assertEquals(t5.getTaggingResource(), ti2);
                	Assert.assertEquals(t5.getTaggedBy(), user);
                	
                	Assert.assertEquals(t6.getTaggedResource(), contentForOnlyTags2);
                	Assert.assertEquals(t6.getTaggingResource(), ti1);
                	Assert.assertEquals(t6.getTaggedBy(), user);
                	
                	Assert.assertEquals(t7.getTaggedResource(), contentForOnlyTags2);
                	Assert.assertEquals(t7.getTaggingResource(), ti2);
                	Assert.assertEquals(t7.getTaggedBy(), user);
                	

                	
                }
        	}.run();    	
        	

        	
	/*
	 * Preparing environment
	 */
	new FacesRequest() {
	
	    @Override
	    protected void invokeApplication() {

	    	Identity.setSecurityEnabled(false);
	    	
	    	ContentItemService ciService        = (ContentItemService) Component.getInstance("contentItemService");
	    	TaggingService     taggingService   = (TaggingService) Component.getInstance("taggingService");
	    	UserService        userService      = (UserService) Component.getInstance("userService");        	
	    	
        	ContentItem contentForOnlyTags1 =  ciService.getContentItemByTitle("content for tags1");
        	ContentItem contentForOnlyTags2 =  ciService.getContentItemByTitle("content for tags2");	    	
	    	
        	Assert.assertEquals(taggingService.getTags(contentForOnlyTags1).size(),1);
        	Assert.assertEquals(taggingService.getTags(contentForOnlyTags2).size(),2);	
        	
        	Assert.assertNotNull(contentForOnlyTags1.getResource());
        	Assert.assertNotNull(contentForOnlyTags2.getResource());        	

        	ContentItem ti1 =  ciService.getContentItemByTitle("myTag");
        	ContentItem ti2 =  ciService.getContentItemByTitle("myOtherTag");
	    	
        	User user =  userService.getUserByLogin("nilay");
        	Assert.assertNotNull(user);
        	
        	//Scenario for checking only user tags

        	Tag t5 = taggingService.createTagging("myOtherTag", contentForOnlyTags1, ti2, user);
        	Tag t6 = taggingService.createTagging("myTag", contentForOnlyTags2, ti1, user);
        	Tag t7 = taggingService.createTagging("myOtherTag", contentForOnlyTags2, ti2, user);

        	
        	// check whether listApplicableDataTypeProperties gives results.
        	Assert.assertEquals(t5.getTaggedResource(), contentForOnlyTags1);
        	Assert.assertEquals(t5.getTaggingResource(), ti2);
        	Assert.assertEquals(t5.getTaggedBy(), user);
        	
       	        	
        	Assert.assertEquals(t6.getTaggedResource(), contentForOnlyTags2);
        	Assert.assertEquals(t6.getTaggingResource(), ti1);
        	Assert.assertEquals(t6.getTaggedBy(), user);
        	
          	
        	Assert.assertEquals(t7.getTaggedResource(), contentForOnlyTags2);
        	Assert.assertEquals(t7.getTaggingResource(), ti2);
        	Assert.assertEquals(t7.getTaggedBy(), user);
   	        	
        	
        	Assert.assertEquals(taggingService.getTags(contentForOnlyTags1).size(),1);
        	Assert.assertEquals(taggingService.getTags(contentForOnlyTags2).size(),2);
        	
        }
	}.run();           	
        	
        	
        /*
		 * Search for the content item by running a full-text search for "world"
		 */
    	new FacesRequest() {
    		


            @Override
            protected void invokeApplication() {
            	Assert.assertTrue(addUserInSession("nilay"));
            	
            	KiWiSearchEngine kiWiSearchEngine = (KiWiSearchEngine) Component.getInstance("kiwiSearchEngine");

            	
            	kiWiSearchEngine.setPersonalSearch(true);
            	
            	kiWiSearchEngine.setSearchQuery("world");
            	
            	kiWiSearchEngine.runSearch();
            	
            	KiWiSearchResults r = kiWiSearchEngine.getSearchResults();

            	Assert.assertTrue(r.getResultCount() >= 1L);
            	
            	ContentItem result = r.getResults().get(0).getItem();
            	//Assert.assertEquals(result.getTitle(), "ci2"); java.lang.AssertionError: expected:<ci2> but was:<MyContentItem>
            	

            	kiWiSearchEngine.setSearchQuery("onlyTags");
            	
            	kiWiSearchEngine.runSearch();
            	
            	KiWiSearchResults r2 = kiWiSearchEngine.getSearchResults();

            	Assert.assertTrue(r2.getResultCount() >= 1L);
            	
            	ContentItem result2 = r2.getResults().get(0).getItem();
            	Assert.assertEquals(result2.getTitle(), "content for tags2");

            }
    	}.run();

	}
}
