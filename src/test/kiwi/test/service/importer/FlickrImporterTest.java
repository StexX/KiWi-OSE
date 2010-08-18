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
package kiwi.test.service.importer;

import java.io.InputStream;

import javax.persistence.EntityManager;

import junit.framework.Assert;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.importexport.importer.Importer;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.exception.UserExistsException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Sebastian Schaffert
 *
 */
@Test
public class FlickrImporterTest extends KiWiTest {
	
	@BeforeMethod
	@Override
	public void begin() {
		super.begin();
	}
	
	@AfterMethod
	@Override
	public void end() {
    	super.end();
	}
	
	@Test
	public void testImportFlickr() throws Exception {
		String[] ontologies = { "ontology_kiwi.owl", "imports/exif.rdf" };
		try {
			setupDatabase(ontologies);		
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
             	UserService        us = (UserService) Component.getInstance("userService");
            	KiWiEntityManager  km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	
            	
            	User user = null;
            	
            	try {
            		user = us.createUser("kiwiknows1","Sebastian", "Schaffert", "WastlGotAPasswordToo");
            		km.persist(user);
            	} catch(UserExistsException ex) {
            	}
            }
    	}.run();
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
             	UserService us  = (UserService) Component.getInstance("userService");
               	Importer    rss = (Importer)    Component.getInstance("kiwi.service.importer.rss");
                            	
            	User user = us.getUserByLogin("kiwiknows1");
            	
            	
            	InputStream is = this.getClass().getResourceAsStream("flickr.rss");
            	
            	rss.importData(is, null, null, null, user, null);
            	
            	
            }
    	}.run();

       	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	KiWiEntityManager  km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	EntityManager      em = (EntityManager) Component.getInstance("entityManager");
            	TripleStore        ts = (TripleStore)       Component.getInstance("tripleStore");
            	UserService        us = (UserService) Component.getInstance("userService");
            	ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	
            	
            	User user = us.getUserByLogin("kiwiknows1");

            	ContentItem entry1 = cs.getContentItemByTitle("kiwi_annual-087");
            	
            	// check whether an entry can be retrieved as content item
            	Assert.assertNotNull(entry1);
            	Assert.assertEquals(user, entry1.getAuthor());
            	
            	Assert.assertEquals(entry1.getResource().getProperty(Constants.NS_EXIF + "model"), "Canon EOS 20D");
             	
            }
    	}.run();
	
		try {
			clearDatabase();	
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
