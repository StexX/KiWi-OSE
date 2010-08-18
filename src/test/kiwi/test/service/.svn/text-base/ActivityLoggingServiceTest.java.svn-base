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
package kiwi.test.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.content.ContentItemService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.user.UserService;
import kiwi.context.CurrentContentItemFactory;
import kiwi.model.activity.AddTagActivity;
import kiwi.model.activity.CreateActivity;
import kiwi.model.activity.DeleteActivity;
import kiwi.model.activity.EditActivity;
import kiwi.model.activity.RemoveTagActivity;
import kiwi.model.activity.VisitActivity;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * ActivityLoggingTests
 *
 * @author Sebastian Schaffert
 *
 */
@Test
public class ActivityLoggingServiceTest extends KiWiTest {


	/**
	 * Test whether the content item create activity is properly logged.
	 * @throws Exception
	 */
	@Test
	public void testCreateContentItemActivity() throws Exception {
		/*
		 * Create a ContentItem using the ContentItemService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	Component.getInstance("currentUser");

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.createContentItem();
            	cs.updateTitle(c, "MyContentItem1");

            	cs.saveContentItem(c);

            	cs.updateTextContentItem(c, "<p>Hello World!</p>");
             }
    	}.run();

    	/*
    	 * Check whether the creation of the content item resulted in a CreateActivity
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	final Query q = em.createQuery("select a from CreateActivity a");

            	final List<CreateActivity> activities = q.getResultList();

            	Assert.assertTrue(activities.size() > 0);

            	final CreateActivity a = activities.get(activities.size()-1);

            	Assert.assertEquals(a.getContentItem().getTitle(), "MyContentItem1");
             }
    	}.run();


	}

	/**
	 * Test whether the content item edit activity is properly logged.
	 * @throws Exception
	 */
	@Test
	public void testEditContentItemActivity() throws Exception {
		/*
		 * Create a ContentItem using the ContentItemService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	Component.getInstance("currentUser");

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.createContentItem();
            	cs.updateTitle(c, "MyContentItem2");

            	cs.saveContentItem(c);

            	cs.updateTextContentItem(c, "<p>Hello World!</p>");
             }
    	}.run();


    	/*
    	 * Check whether the updating of the text content of the content item resulted in a EditActivity
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	final Query q = em.createQuery("select a from EditActivity a");

            	final List<EditActivity> activities = q.getResultList();

            	Assert.assertTrue(activities.size() > 0);

            	final EditActivity a = activities.get(activities.size()-1);

            	Assert.assertEquals(a.getContentItem().getTitle(), "MyContentItem2");
             }
    	}.run();

	}

	/**
	 * Test whether the content item visit activity is properly logged.
	 * @throws Exception
	 */
	@Test
	public void testVisitContentItemActivity() throws Exception {
		/*
		 * Create a ContentItem using the ContentItemService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);


            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.createContentItem();
            	cs.updateTitle(c, "MyContentItem6");

            	cs.saveContentItem(c);

            	cs.updateTextContentItem(c, "<p>Hello World!</p>");
             }
    	}.run();

    	/*
    	 * create a new user and log him in
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final UserService        us = (UserService) Component.getInstance("userService");
            	us.createUser("testusr3", "Hans", "Mustermann", "pass123");


            }
    	}.run();

    	/*
    	 * check whether the new user is created in UserService
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final UserService        us = (UserService) Component.getInstance("userService");

            	final User u = us.getUserByLogin("testusr3");

            	Assert.assertNotNull(u);
            	Assert.assertEquals(u.getLogin(), "testusr3");

            }
    	}.run();

    	/*
    	 * Visit MyContentItem6 by making it the current content item via CurrentContentItemFactory
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	Identity.instance().getCredentials().setUsername("testusr3");
            	Identity.instance().getCredentials().setPassword("pass123");
            	Identity.instance().login();

            	final User u = (User) Component.getInstance("currentUser");
              	Assert.assertEquals(u.getLogin(), "testusr3");

            	final CurrentContentItemFactory f = (CurrentContentItemFactory) Component.getInstance("currentContentItemFactory");

            	f.setCurrentItemTitle("MyContentItem6");

            	final ContentItem item = (ContentItem) Component.getInstance("currentContentItem");

            	Assert.assertEquals(item.getTitle(), "MyContentItem6");

            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	// no real KiWi transaction here, ensure a flush before querying (TODO: is this a bug?)
            	em.flush();
            }
    	}.run();


    	/*
    	 * Check whether the visit of the content item resulted in a VisitActivity
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	final Query q = em.createQuery("select a from VisitActivity a");

            	final List<VisitActivity> activities = q.getResultList();

            	Assert.assertTrue(activities.size() > 0);

            	final VisitActivity a = activities.get(activities.size()-1);

            	Assert.assertEquals(a.getContentItem().getTitle(), "MyContentItem6");
            	Assert.assertEquals(a.getUser().getLogin(), "testusr3");
             }
    	}.run();


	}


	/**
	 * Test whether the content item delete activity is properly logged
	 * @throws Exception
	 */
	@Test
	public void testDeleteContentItemActivity() throws Exception {
		/*
		 * Create a ContentItem using the ContentItemService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.createContentItem();
            	cs.updateTitle(c, "MyContentItem3");

            	cs.saveContentItem(c);

            	cs.updateTextContentItem(c, "<p>Hello World!</p>");
             }
    	}.run();

		/*
		 * Remove the ContentItem again using the ContentItemService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.getContentItemByTitle("MyContentItem3");
            	cs.removeContentItem(c);

             }
    	}.run();


    	/*
    	 * Check whether the removal of the content item resulted in a DeleteActivity
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	final Query q = em.createQuery("select a from DeleteActivity a");

            	final List<DeleteActivity> activities = q.getResultList();

            	Assert.assertTrue(activities.size() > 0);

            	final DeleteActivity a = activities.get(activities.size()-1);

            	Assert.assertEquals(a.getContentItem().getTitle(), "MyContentItem3");
             }
    	}.run();

	}



	/**
	 * Test whether the tagging of a content item is properly logged.
	 * @throws Exception
	 */
	@Test
	public void testTaggingCreatedActivity() throws Exception {
		/*
		 * Create a ContentItem using the ContentItemService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	final TaggingService     ts = (TaggingService) Component.getInstance("taggingService");
            	final UserService        us = (UserService) Component.getInstance("userService");

            	final User u = us.createUser("testusr1", "Hans", "Mustermann", "pass123");

            	final ContentItem c = cs.createContentItem();
            	cs.updateTitle(c, "MyContentItem4");

            	cs.saveContentItem(c);

            	cs.updateTextContentItem(c, "<p>Hello World!</p>");

				// create a tag
            	final ContentItem t = cs.createContentItem();
            	cs.updateTitle(t, "Tag 1");

            	ts.createTagging("Tag 1", c, t, u);

             }
    	}.run();

    	/*
    	 * Check whether the tagging of the content item resulted in a AddTagActivity
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	final Query q = em.createQuery("select a from AddTagActivity a");

            	final List<AddTagActivity> activities = q.getResultList();

            	Assert.assertTrue(activities.size() > 0);

            	final AddTagActivity a = activities.get(activities.size()-1);

            	Assert.assertEquals(a.getTag().getTaggingResource().getTitle(), "Tag 1");
            	Assert.assertEquals(a.getTag().getTaggedResource().getTitle(), "MyContentItem4");
            	Assert.assertEquals(a.getTag().getTaggedBy().getLogin(), "testusr1");
             }
    	}.run();

 	}

	/**
	 * Test whether the tagging of a content item is properly logged.
	 * @throws Exception
	 */
	@Test
	public void testTaggingRemovedActivity() throws Exception {

		/*
		 * Create a ContentItem using the ContentItemService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	final TaggingService     ts = (TaggingService) Component.getInstance("taggingService");
            	final UserService        us = (UserService) Component.getInstance("userService");

            	final User u = us.createUser("testusr2", "Hans", "Mustermann", "pass123");

            	final ContentItem c = cs.createContentItem();
            	cs.updateTitle(c, "MyContentItem5");

            	cs.saveContentItem(c);

            	cs.updateTextContentItem(c, "<p>Hello World!</p>");

				// create a tag
            	final ContentItem t = cs.createContentItem();
            	cs.updateTitle(t, "Tag 2");

            	ts.createTagging("Tag 2", c, t, u);

             }
    	}.run();

    	/*
    	 * remove the tag again
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	Component.getInstance("contentItemService");
            	final TaggingService     ts = (TaggingService) Component.getInstance("taggingService");

            	final List<Tag> tags = ts.getTagsByLabel("Tag 2");

            	Assert.assertEquals(tags.size(),1);

            	final Tag t = tags.get(0);

            	Assert.assertEquals(t.getTaggingResource().getTitle(), "Tag 2");
            	Assert.assertEquals(t.getTaggedResource().getTitle(), "MyContentItem5");
            	Assert.assertEquals(t.getTaggedBy().getLogin(), "testusr2");

            	ts.removeTagging(t);

             }
    	}.run();


	/*
	 * Check whether the tag removal resulted in a RemoveTagActivity
	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	final Query q = em.createQuery("select a from RemoveTagActivity a");

            	final List<RemoveTagActivity> activities = q.getResultList();

            	Assert.assertTrue(activities.size() > 0);

            	final RemoveTagActivity a = activities.get(activities.size()-1);

            	Assert.assertEquals(a.getTag().getTaggingResource().getTitle(), "Tag 2");
            	Assert.assertEquals(a.getTag().getTaggedResource().getTitle(), "MyContentItem5");
            	Assert.assertEquals(a.getTag().getTaggedBy().getLogin(), "testusr2");
             }
    	}.run();

 	}


}
