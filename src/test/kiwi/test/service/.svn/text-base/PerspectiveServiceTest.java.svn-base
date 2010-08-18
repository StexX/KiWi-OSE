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

import javax.persistence.EntityManager;

import kiwi.api.perspectives.PerspectiveService;
import kiwi.model.perspective.Perspective;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * PerspectiveServiceTest
 *
 * @author Sebastian Schaffert
 *
 */
@Test
public class PerspectiveServiceTest extends KiWiTest {

	@Test
	public void testPerspectiveService() throws Exception {
		/*
		 * Check creation of new perspective
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	PerspectiveService ps = (PerspectiveService) Component.getInstance("perspectiveService");
            	EntityManager      em = (EntityManager) Component.getInstance("entityManager");
            	
            	
            	Perspective p = new Perspective("Test","test");
            	p.setDescription("Test Description");
            	
            	ps.addPerspective(p);
            	
            	em.flush();
            	
            	Assert.assertEquals(p.getViewTemplate(),"test/view.xhtml");
            	Assert.assertEquals(p.getEditTemplate(),"test/edit.xhtml");
            }
    	}.run();

		/*
		 * Check querying of new perspective
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	PerspectiveService ps = (PerspectiveService) Component.getInstance("perspectiveService");
            	
            	Perspective p = ps.getPerspective("Test");
            	
            	Assert.assertNotNull(p);
            	Assert.assertEquals(p.getName(), "Test");
            	Assert.assertEquals(p.getDescription(), "Test Description");
            	Assert.assertEquals(p.getPath(), "test");
            	
            }
    	}.run();
		

		/*
		 * Check updating of new perspective
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	PerspectiveService ps = (PerspectiveService) Component.getInstance("perspectiveService");
            	
            	Perspective p = ps.getPerspective("Test");
            	p.setDescription("updated description");
            	ps.updatePerspective(p);            	
            }
    	}.run();
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	PerspectiveService ps = (PerspectiveService) Component.getInstance("perspectiveService");
            	
            	Perspective p = ps.getPerspective("Test");
            	
            	Assert.assertNotNull(p);
            	Assert.assertEquals(p.getName(), "Test");
            	Assert.assertEquals(p.getDescription(), "updated description");
            	Assert.assertEquals(p.getPath(), "test");
            	
            }
    	}.run();

		/*
		 * Check deleting of new perspective
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	PerspectiveService ps = (PerspectiveService) Component.getInstance("perspectiveService");
            	
            	Perspective p = ps.getPerspective("Test");
            	ps.removePerspective(p);            	
            }
    	}.run();
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	PerspectiveService ps = (PerspectiveService) Component.getInstance("perspectiveService");
            	
            	Perspective p = ps.getPerspective("Test");
            	
            	Assert.assertNull(p);
            	
            }
    	}.run();
   	
	}
	
}
