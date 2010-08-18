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
package kiwi.test.service.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import kiwi.api.user.CountryFacade;
import kiwi.api.user.KiWiProfileFacade;
import kiwi.api.user.ProfileService;
import kiwi.api.user.UserService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * ProfileServiceTest
 *
 * @author Sebastian Schaffert
 *
 */
@Test
public class ProfileServiceTest extends KiWiTest {


	@Test
	public void testGetProfile() throws Exception {
		final Date now = new Date();

		/*
		 * Create a User using the UserService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final UserService        us = (UserService) Component.getInstance("userService");

            	final User u              = us.createUser("p-testusr1", "Hans", "Mustermann", "hansPW");

            	Assert.assertEquals(u.getLogin(), "p-testusr1");
            	Assert.assertEquals(u.getFirstName(),"Hans");
            	Assert.assertEquals(u.getLastName(),"Mustermann");
            	Assert.assertNull(u.getEmail());


              }
    	}.run();

		/*
		 * Create a Profile for the user using the UserService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final UserService        us = (UserService) Component.getInstance("userService");
            	final ProfileService     ps = (ProfileService) Component.getInstance("profileService");

            	final User u              = us.getUserByLogin("p-testusr1");
            	final KiWiProfileFacade p = ps.getProfile(u);

            	Assert.assertEquals(p.getFirstName(),u.getFirstName());
            	Assert.assertEquals(p.getLastName(),u.getLastName());

            	p.setBirthday(now);
            	p.setHomepage("http://www.kiwi-project.eu");
            	p.setLanguage(Locale.ENGLISH);

              }
    	}.run();

		/*
		 * check whether profile contains the right information
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final UserService        us = (UserService) Component.getInstance("userService");
            	final ProfileService     ps = (ProfileService) Component.getInstance("profileService");

            	final User u              = us.getUserByLogin("p-testusr1");
            	final KiWiProfileFacade p = ps.getProfile(u);

            	Assert.assertEquals(p.getFirstName(),u.getFirstName());
            	Assert.assertEquals(p.getLastName(),u.getLastName());

            	Assert.assertEquals(p.getFirstName(),"Hans");
            	Assert.assertEquals(p.getLastName(),"Mustermann");

            	Assert.assertEquals(p.getBirthday().getTime(),now.getTime());
            	Assert.assertEquals(p.getHomepage(),"http://www.kiwi-project.eu");
            	Assert.assertEquals(p.getLanguage(),Locale.ENGLISH);

              }
    	}.run();


	}


	@Test
	public void testGetInterest() throws Exception {


		final Set<String> ids = new HashSet<String>();

		/*
		 * Create a interest by calling getInterest for the first time
		 */
     	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

             	final ProfileService     ps = (ProfileService) Component.getInstance("profileService");

            	final ContentItem ci = ps.getInterest("mountaineering");

            	Assert.assertEquals(ci.getTitle(), "mountaineering");

            	ids.add(ci.getKiwiIdentifier());
              }
    	}.run();

		/*
		 * get a interest again by calling getInterest for the second time
		 */
     	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

             	final ProfileService     ps = (ProfileService) Component.getInstance("profileService");

            	final ContentItem ci = ps.getInterest("mountaineering");

            	Assert.assertEquals(ci.getTitle(), "mountaineering");

            	final String id = ids.iterator().next();

            	Assert.assertEquals(ci.getKiwiIdentifier(),id);

            	Assert.assertTrue(ci.getTypes().size() >= 2);

            	boolean type1 = false, type2 = false;
            	for(final KiWiResource type : ci.getTypes()) {
            		if(type instanceof KiWiUriResource) {
            			final KiWiUriResource type_u = (KiWiUriResource) type;

            			if(type_u.getUri().equals(Constants.NS_KIWI_CORE+"Tag")) {
            				type1 = true;
            			}
            			if(type_u.getUri().equals(Constants.NS_KIWI_CORE+"Interest")) {
            				type2 = true;
            			}
            		}
            	}

            	Assert.assertTrue(type1);
            	Assert.assertTrue(type2);

             }
    	}.run();

	}


	@Test
	public void testGetInterests() throws Exception {


		final Set<String> ids = new HashSet<String>();

		/*
		 * Create a set of interests by calling getInterests
		 */
     	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

             	final ProfileService     ps = (ProfileService) Component.getInstance("profileService");

            	final HashSet<ContentItem> cis = ps.getInterests("mountaineering, skiing, riding");

            	Assert.assertEquals(cis.size(),3);

            	boolean c1 = false, c2 = false, c3 = false;
            	for(final ContentItem ci : cis) {
            		if("mountaineering".equals(ci.getTitle())) {
            			c1 = true;
            		}
            		if("skiing".equals(ci.getTitle())) {
            			c2 = true;
            		}
            		if("riding".equals(ci.getTitle())) {
            			c3 = true;
            		}

            		ids.add(ci.getKiwiIdentifier());
            	}
            	Assert.assertTrue(c1);
            	Assert.assertTrue(c2);
            	Assert.assertTrue(c3);
              }
    	}.run();

		/*
		 * get a interest again by calling getInterests for the second time
		 */
     	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

             	final ProfileService     ps = (ProfileService) Component.getInstance("profileService");

            	final HashSet<ContentItem> cis = ps.getInterests("mountaineering, skiing, riding");

            	Assert.assertEquals(cis.size(),3);

            	for(final ContentItem ci : cis) {

            		Assert.assertTrue(ids.contains(ci.getKiwiIdentifier()));

	            	Assert.assertTrue(ci.getTypes().size() >= 2);

	            	boolean type1 = false, type2 = false;
	            	for(final KiWiResource type : ci.getTypes()) {
	            		if(type instanceof KiWiUriResource) {
	            			final KiWiUriResource type_u = (KiWiUriResource) type;

	            			if(type_u.getUri().equals(Constants.NS_KIWI_CORE+"Tag")) {
	            				type1 = true;
	            			}
	            			if(type_u.getUri().equals(Constants.NS_KIWI_CORE+"Interest")) {
	            				type2 = true;
	            			}
	            		}
	            	}
	            	Assert.assertTrue(type1);
	            	Assert.assertTrue(type2);
            	}


             }
    	}.run();

	}


	@Test
	public void testGetCountry() throws Exception {


		final Set<String> ids = new HashSet<String>();
		/*
		 * Create a set of interests by calling getInterests
		 */
     	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

             	final ProfileService     ps = (ProfileService) Component.getInstance("profileService");

            	final CountryFacade c1 = ps.getCountry("Austria");

            	Assert.assertEquals(c1.getISOCode(), "AT");
            	Assert.assertEquals(c1.getName(), "Austria");

            	final CountryFacade c2 = ps.getCountry("DE");

            	Assert.assertEquals(c2.getISOCode(), "DE");
            	Assert.assertEquals(c2.getName(), "Germany");

            	ids.add(c1.getKiwiIdentifier());
              }
    	}.run();

		/*
		 * Create a set of interests by calling getInterests
		 */
     	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

             	final ProfileService     ps = (ProfileService) Component.getInstance("profileService");

            	final CountryFacade c1 = ps.getCountry("Austria");
            	final CountryFacade c3 = ps.getCountry("AT");

            	Assert.assertTrue(ids.contains(c1.getKiwiIdentifier()));
            	Assert.assertEquals(c1.getKiwiIdentifier(), c3.getKiwiIdentifier());
              }
    	}.run();

	}

}
