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
import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.Assert;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.importexport.ExportService;
import kiwi.api.importexport.ImportService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.exception.UserExistsException;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.annotations.Test;

/**
 * @author Sebastian Schaffert
 *
 */
public class ImportExportServiceTest extends KiWiTest {

	
	@Test
	public void testExportRDF() throws Exception {
		String[] ontologies = { "ontology_kiwi.owl" };
		setupDatabase(ontologies);
		
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	ContentItemService cis = (ContentItemService) Component.getInstance("contentItemService");
            	
                ContentItem ci = cis.createContentItem();
                cis.updateTitle(ci, "Content Item 1");
                cis.saveContentItem(ci);
            }
    	}.run();

    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	ContentItemService cis = (ContentItemService) Component.getInstance("contentItemService");
                ExportService      ies = (ExportService) Component.getInstance("kiwi.core.exportService");             	
            	
                ContentItem ci = cis.getContentItemByTitle("Content Item 1");
                
                String result = ies.exportItems(Collections.singleton(ci), "application/rdf+xml");
                
                Assert.assertNotNull(result);
                Assert.assertTrue(result.contains("<rdf:RDF"));
            }
    	}.run();
	
    	clearDatabase();
	}	
	
//	@Test
//	public void testImportRSS() throws Exception {
//		String[] ontologies = { "ontology_kiwi.owl" };
//		setupDatabase(ontologies);
//		
//    	new FacesRequest() {
//
//            @Override
//            protected void invokeApplication() {
//
//            	Identity.setSecurityEnabled(false);
//            	
//             	UserService        us = (UserService) Component.getInstance("userService");
//            	KiWiEntityManager  km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
//            	
//            	
//            	User user = null;
//            	
//            	try {
//            		user = us.createUser("wastl","Sebastian", "Schaffert", "WastlGotAPasswordToo");
//            		km.persist(user);
//            	} catch(UserExistsException ex) {
//            	}
//            }
//    	}.run();
//    	
//    	new FacesRequest() {
//
//            @Override
//            protected void invokeApplication() throws Exception {
//
//            	Identity.setSecurityEnabled(false);
//            	
//             	UserService us  = (UserService) Component.getInstance("userService");
//                ImportService ies = (ImportService) Component.getInstance("kiwi.core.importService");             	
//             	
//            	User user = us.getUserByLogin("wastl");
//            	
//            	
//            	InputStream is = this.getClass().getResourceAsStream("feed.rss");
//            	
//            	ies.importData(is, "application/rss+xml", null, null, user, null);
//            	
//            	
//            }
//    	}.run();
//
//       	new FacesRequest() {
//
//            @Override
//            protected void invokeApplication() {
//
//            	Identity.setSecurityEnabled(false);
//            	
//            	KiWiEntityManager  km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
//            	EntityManager      em = (EntityManager) Component.getInstance("entityManager");
//            	TripleStore        ts = (TripleStore)       Component.getInstance("tripleStore");
//            	UserService        us = (UserService) Component.getInstance("userService");
//            	ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
//            	
//            	
//            	User user = us.getUserByLogin("wastl");
//
//            	ContentItem entry1 = cs.getContentItemByTitle("1st KiWi Programming Camp");
//            	
//            	// check whether an entry can be retrieved as content item
//            	Assert.assertNotNull(entry1);
//            	Assert.assertEquals(user, entry1.getAuthor());
//            	
//            	// check that the kiwiknows tag has been created exactly once
//            	Query q = em.createQuery("select count(ci) from ContentItem ci where ci.title='kiwiknows'");
//            	Assert.assertEquals(1L,q.getSingleResult());
//            	
//            }
//    	}.run();
//	
//    	clearDatabase();
//	}

}
