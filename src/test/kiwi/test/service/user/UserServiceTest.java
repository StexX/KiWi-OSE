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

import java.util.List;

import kiwi.api.config.ConfigurationService;
import kiwi.api.user.UserService;
import kiwi.exception.UserExistsException;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * UserServiceTests
 *
 * @author Sebastian Schaffert
 *
 */
@Test
public class UserServiceTest extends KiWiTest {
	
	/**
	 * Test creation of users using userService.createUser(login, firstName, Password)
	 * @throws Exception
	 */
	@Test
	public void testCreateUserFull() throws Exception {
		/*
		 * Create a User using the UserService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService        us = (UserService) Component.getInstance("userService");
            	
            	User u = us.createUser("testusr1", "Hans", "Mustermann", "hansPW");

            	Assert.assertEquals(u.getLogin(), "testusr1");
            	Assert.assertEquals(u.getFirstName(),"Hans");
            	Assert.assertEquals(u.getLastName(),"Mustermann");
            	Assert.assertNull(u.getEmail());
            	
            	ConfigurationService cs = (ConfigurationService) Component.getInstance("configurationService");
            	
            	String uri1 = cs.getBaseUri()+"/user/testusr1";
            	Assert.assertEquals(((KiWiUriResource)u.getResource()).getUri(),uri1);
            	
            	// test whether user is author of his own content item
            	//Assert.assertEquals(u.getAuthoredContent().size(),1);
             }
    	}.run();
    	
		/*
		 * Query for user and test whether he has been persisted properly
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService        us = (UserService) Component.getInstance("userService");
            	ConfigurationService cs = (ConfigurationService) Component.getInstance("configurationService");
            	
            	User u = us.getUserByLogin("testusr1");

            	Assert.assertEquals(u.getLogin(), "testusr1");
            	Assert.assertEquals(u.getFirstName(),"Hans");
            	Assert.assertEquals(u.getLastName(),"Mustermann");
            	Assert.assertNull(u.getEmail());
            	
               	String uri1 = cs.getBaseUri()+"/user/testusr1";
            	Assert.assertEquals(((KiWiUriResource)u.getResource()).getUri(),uri1);
             	// test whether user is author of his own content item
            	//Assert.assertEquals(u.getAuthoredContent().size(),1);
             }
    	}.run();
	}
	
	/**
	 * Test creation of users using userService.createUser(login)
	 * @throws Exception
	 */
	@Test
	public void testCreateUserLogin() throws Exception {
		/*
		 * Create a User using the UserService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService        us = (UserService) Component.getInstance("userService");
            	ConfigurationService cs = (ConfigurationService) 
            		Component.getInstance("configurationService");
            	
            	User u = us.createUser("testusr2", "pw");

            	Assert.assertEquals(u.getLogin(), "testusr2");
            	Assert.assertEquals(u.getFirstName(),"");
            	Assert.assertEquals(u.getLastName(),"testusr2");
            	Assert.assertNull(u.getEmail());
            	
               	String uri1 = cs.getBaseUri()+"/user/testusr2";
            	Assert.assertEquals(((KiWiUriResource)u.getResource()).getUri(),uri1);
             	// test whether user is author of his own content item
            	//Assert.assertEquals(u.getAuthoredContent().size(),1);
             }
    	}.run();
    	
		/*
		 * Query for user and test whether he has been persisted properly
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService        us = (UserService) Component.getInstance("userService");
            	ConfigurationService cs = (ConfigurationService) Component.getInstance("configurationService");
            	
            	User u = us.getUserByLogin("testusr2");

            	Assert.assertEquals(u.getLogin(), "testusr2");
            	Assert.assertEquals(u.getFirstName(),"");
            	Assert.assertEquals(u.getLastName(),"testusr2");
            	Assert.assertNull(u.getEmail());
            	
               	String uri1 = cs.getBaseUri()+"/user/testusr2";
            	Assert.assertEquals(((KiWiUriResource)u.getResource()).getUri(),uri1);
            	// test whether user is author of his own content item
            	//Assert.assertEquals(u.getAuthoredContent().size(),1);
             }
    	}.run();
	}

	/**
	 * Test creation of users using userService.createUser(login) and verify whether a 
	 * UserExistsException is thrown when the user already exists
	 * @throws Exception
	 */
	@Test
	public void testCreateUserExists() throws Exception {
		/*
		 * Create a User using the UserService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService        us = (UserService) Component.getInstance("userService");
            	
            	User u = us.createUser("testusr7", "pw");
             }
    	}.run();
    	
		/*
		 * Query for user and test whether he has been persisted properly
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService        us = (UserService) Component.getInstance("userService");
            	
            	try {
            		User u = us.createUser("testusr7", "pw");
            		
            		Assert.fail("user already exists but no exception is thrown!");
            	} catch(UserExistsException ex) {
            		// do nothing, excpeted exception
            	}
             }
    	}.run();
	}

	
	/**
	 * Test querying of users using userService.getUsers(), userService.getUserByLogin(),
	 * userService.getUserByName(), userService.getUserByUri(), userService.userExists()
	 * @throws Exception
	 */
	@Test
	public void testQueryUser() throws Exception {
		/*
		 * Create a User using the UserService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService        us = (UserService) Component.getInstance("userService");
            	
            	User u1 = us.createUser("testusr3", "Hans", "Mustermann", "musterPW");
            	User u2 = us.createUser("testusr4", "Otto", "Mustermann", "musterPW2");
             }
    	}.run();
    	
		/*
		 * Query for testusr3 and testusr4 using userExists
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService        us = (UserService) Component.getInstance("userService");
            	
            	Assert.assertTrue(us.userExists("testusr3"));
            	Assert.assertTrue(us.userExists("testusr4"));
            	
            	Assert.assertFalse(us.userExists("blablub"));
               }
    	}.run();
    	
		/*
		 * Query for testusr3 and testusr4 using getUserByLogin
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService        us = (UserService) Component.getInstance("userService");
            	
            	User u1 = us.getUserByLogin("testusr3");
            	User u2 = us.getUserByLogin("testusr4");
            	User u3 = us.getUserByLogin("blablub");
            	
            	Assert.assertNotNull(u1);
            	Assert.assertNotNull(u2);
            	Assert.assertNull(u3);
            	
            	Assert.assertEquals(u1.getLogin(), "testusr3");
            	Assert.assertEquals(u2.getLogin(), "testusr4");
            	
               }
    	}.run();

		/*
		 * Query for testusr3 and testusr4 using getUserByLogin
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService          us = (UserService) Component.getInstance("userService");
            	ConfigurationService cs = (ConfigurationService) Component.getInstance("configurationService");
            	
            	String uri1 = cs.getBaseUri()+"/user/testusr3";
            	String uri2 = cs.getBaseUri()+"/user/testusr4";
            	String uri3 = cs.getBaseUri()+"/user/blablub";
           	
            	User u1 = us.getUserByUri(uri1);
            	User u2 = us.getUserByUri(uri2);
            	User u3 = us.getUserByUri(uri3);
            	
            	Assert.assertNotNull(u1);
            	Assert.assertNotNull(u2);
            	Assert.assertNull(u3);
            	
            	Assert.assertEquals(u1.getLogin(), "testusr3");
            	Assert.assertEquals(u2.getLogin(), "testusr4");
            	
               }
    	}.run();
    	
    	
		/*
		 * Query for testusr3 and testusr4 using getUserByName
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService        us = (UserService) Component.getInstance("userService");
            	
            	List<User> u1 = us.getUsersByName("Hans","Mustermann");
            	List<User> u2 = us.getUsersByName("Otto","Mustermann");
            	List<User> u3 = us.getUsersByName("Wolf","Mustermann");
            	
            	Assert.assertTrue(u1.size() >= 1);
            	Assert.assertTrue(u2.size() >= 1);
            	Assert.assertTrue(u3.isEmpty());
            	
            	User us1 = null;
            	for (User u : u1) {
            		if(u.getLogin().equals("testusr3")) {
            			us1 = u;
            		}
            		Assert.assertEquals(u.getFirstName(),"Hans");
            		Assert.assertEquals(u.getLastName(),"Mustermann");
            	}
            	Assert.assertNotNull(us1);
             	Assert.assertEquals(us1.getLogin(), "testusr3");             	
            	
               }
    	}.run();
    	
		/*
		 * Query for list of users and check that it at least contains testusr3 and testusr4
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	UserService        us = (UserService) Component.getInstance("userService");
            	
            	List<User> users = us.getUsers();
            	
            	Assert.assertTrue(users.size() >= 2);
            	
            	User u1 = null, u2 = null;
            	for (User u : users) {
            		if(u.getLogin().equals("testusr3")) {
            			u1 = u;
            		}
            		if(u.getLogin().equals("testusr4")) {
            			u2 = u;
            		}
            	}
            	
            	Assert.assertNotNull(u1);
            	Assert.assertNotNull(u2);
              }
    	}.run();
	}

}
