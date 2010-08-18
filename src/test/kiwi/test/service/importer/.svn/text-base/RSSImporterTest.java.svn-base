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
import javax.persistence.Query;

import junit.framework.Assert;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.importexport.importer.Importer;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.exception.UserExistsException;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Identity;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Sebastian Schaffert
 *
 */
public class RSSImporterTest extends KiWiTest {

	
	@BeforeMethod
	@Override
	public void begin() {
		super.begin();
		String[] ontologies = { "ontology_kiwi.owl" };
		try {
			setupDatabase(ontologies);		
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@AfterMethod
	@Override
	public void end() {
		try {
			clearDatabase();	
		} catch(Exception ex) {
			ex.printStackTrace();
		}
    	super.end();
	}
	
	@Test
	public void testImportRSS() throws Exception {
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>>> testImportRSS (1)");
            	Identity.setSecurityEnabled(false);
            	
             	UserService        us = (UserService) Component.getInstance("userService");
            	KiWiEntityManager  km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	
            	log.info(">>>>>>>>>>> (2)");
            	
            	User user = null;
            	
            	try {
            		user = us.createUser("wastl","Sebastian", "Schaffert", "WastlGotAPasswordToo");
            		km.persist(user);
            		log.info(">>>>>>>>>>> (2)");
            	} catch(UserExistsException ex) {
            	}
            }
    	}.run();
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>>> testImportRSS (3)");
            	Identity.setSecurityEnabled(false);
            	
             	UserService us  = (UserService) Component.getInstance("userService");
               	Importer    rss = (Importer)    Component.getInstance("kiwi.service.importer.rss");
                
               	log.info(">>>>>>>>>>> testImportRSS (4)");
            	User user = us.getUserByLogin("wastl");
            	
            	log.info(">>>>>>>>>>> testImportRSS (5)");
            	InputStream is = this.getClass().getResourceAsStream("feed.rss");
            	
            	log.info(">>>>>>>>>>> testImportRSS (6)");
            	rss.importData(is, null, null, null, user, null);
            	log.info(">>>>>>>>>>> testImportRSS (7)");
            	
            	
            }
    	}.run();

       	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>>> testImportRSS (8)");
            	Identity.setSecurityEnabled(false);
            	
            	KiWiEntityManager  km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	EntityManager      em = (EntityManager) Component.getInstance("entityManager");
            	TripleStore        ts = (TripleStore)       Component.getInstance("tripleStore");
            	UserService        us = (UserService) Component.getInstance("userService");
            	ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	
            	log.info(">>>>>>>>>>> testImportRSS (9)");
            	User user = us.getUserByLogin("wastl");

            	ContentItem entry1 = cs.getContentItemByTitle("1st KiWi Programming Camp");
            	
            	log.info(">>>>>>>>>>> testImportRSS (10)");
            	// check whether an entry can be retrieved as content item
            	Assert.assertNotNull(entry1);
            	Assert.assertEquals(user, entry1.getAuthor());
            	
            	log.info(">>>>>>>>>>> testImportRSS (11)");
            	// check that the kiwiknows tag has been created exactly once
            	Query q = em.createQuery("select count(ci) from ContentItem ci where ci.title='kiwiknows'");
            	log.info(">>>>>>>>>>> count ci title kiwiknows: #0 ", q.getSingleResult());
            	Assert.assertEquals(1L,q.getSingleResult());
            	log.info(">>>>>>>>>>> testImportRSS (12)");
            	
            }
    	}.run();
	}
	
	@Test
	public void testImportReuters() throws Exception {
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
             	UserService        us = (UserService) Component.getInstance("userService");
            	KiWiEntityManager  km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	
            	
            	User user = null;
            	
            	try {
            		user = us.createUser("wastl","Sebastian", "Schaffert", "WastlGotAPasswordToo");
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
                            	
            	User user = us.getUserByLogin("wastl");
            	
            	
            	InputStream is = this.getClass().getResourceAsStream("reuters.atom");
            	
            	rss.importData(is, null, null, null, user, null);
            	
            	
            }
    	}.run();

       	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	KiWiEntityManager  km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	EntityManager      em = (EntityManager) Component.getInstance("entityManager");
            	TripleStore        ts = (TripleStore)       Component.getInstance("tripleStore");
            	UserService        us = (UserService) Component.getInstance("userService");
            	ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	
            	
            	User user = us.getUserByLogin("wastl");

            	ContentItem entry1 = cs.getContentItemByTitle("Husband of Philippine massacre victim files candidacy");
            	
            	// check whether an entry can be retrieved as content item
            	Assert.assertNotNull(entry1);
            	Assert.assertEquals(user, entry1.getAuthor());
            	
            	// check that the article URI is right
            	Assert.assertEquals("http://feeds.reuters.com/~r/reuters/worldNews/~3/ZJD_5vxqjrc/idUSTRE5AO0MW20091127",
            			            ((KiWiUriResource)entry1.getResource()).getUri());
            	
            }
    	}.run();
	}
	

}
