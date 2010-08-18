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
import kiwi.model.content.ContentItem;
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
 * RDFImporterTest
 *
 * @author Sebastian Schaffert
 *
 */
@Test
public class RDFImporterTest extends KiWiTest {

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
	public void testImportRDF() throws Exception {
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	Log log = Logging.getLog(this.getClass());
            	
             	UserService        us = (UserService) Component.getInstance("userService");
            	KiWiEntityManager  km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	
            	
            	User user = null;
            	
            	try {
            		user = us.createUser("wastl","Sebastian", "Schaffert", "WastlGotAPasswordToo");
            		km.persist(user);
            	} catch(UserExistsException ex) {
            	}
            	
            	log.info("end of test case 1");
            }
    	}.run();
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	Log log = Logging.getLog(this.getClass());
            	
             	UserService us  = (UserService) Component.getInstance("userService");
               	Importer    rss = (Importer)    Component.getInstance("kiwi.service.importer.rdf");
                            	
            	User user = us.getUserByLogin("wastl");
            	
            	
            	InputStream is = this.getClass().getResourceAsStream("test_xmlliteral.rdf");
            	
            	rss.importData(is, null, null, null, user, null);
            	
            	log.info("End of test case 2");
            }
    	}.run();

       	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	Log log = Logging.getLog(this.getClass());
            	
            	KiWiEntityManager  km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	EntityManager      em = (EntityManager) Component.getInstance("entityManager");
            	TripleStore        ts = (TripleStore)       Component.getInstance("tripleStore");
            	UserService        us = (UserService) Component.getInstance("userService");
            	ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	
            	
            	User user = us.getUserByLogin("wastl");

            	ContentItem entry1 = cs.getContentItemByUri("http://example.org/item01");
            	
            	Assert.assertNotNull(entry1);
            	
            	// check content of content item
            	log.info("Content of Item: #0 ", entry1.getTextContent().getHtmlContent());
            }
    	}.run();
	}

	
}
