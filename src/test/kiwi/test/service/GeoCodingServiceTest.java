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

import static org.junit.Assert.assertEquals;
import kiwi.api.geo.GeoCodingFailedException;
import kiwi.api.geo.GeoCodingService;
import kiwi.api.geo.Location;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * GeoCodingServiceTest
 *
 * @author Sebastian Schaffert
 *
 */
@Test
public class GeoCodingServiceTest extends KiWiTest {

	@Test
	public void testGoogleCityFirst() throws Exception {
	   	/*
		 * Check whether we can set configuration options and they are immediately set correctly
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	GeoCodingService gs = (GeoCodingService) Component.getInstance("kiwi.core.geoCodingService");
            	
                final Location expected = new Location(47.8, 13.03);
                final Location actual = gs.getLocationGoogle("Salzburg");
                assertEquals(expected.getLatitude(), actual.getLatitude(), 0.02);
                assertEquals(expected.getLongitude(), actual.getLongitude(), 0.02);

             }
    	}.run();

	}

	/**
	 * This test is know to fail, because Google does not return the right answer
	 * @throws Exception
	 */
	@Test(enabled=false)
	public void testGoogleCityNearest() throws Exception {
	   	/*
		 * Check whether we can set configuration options and they are immediately set correctly
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	GeoCodingService gs = (GeoCodingService) Component.getInstance("kiwi.core.geoCodingService");
            	
                final Location salzburg = new Location(47.417, 13.25);
                final Location expected = new Location(47.35, 13.2);
                final Location actual = gs.getLocationGoogle("St. Johann",salzburg);
                assertEquals(expected.getLatitude(), actual.getLatitude(), 0.1);
                assertEquals(expected.getLongitude(), actual.getLongitude(), 0.1);

             }
    	}.run();

	}

	@Test
	public void testGoogleCityFailed() throws Exception {
	   	/*
		 * Check whether we can set configuration options and they are immediately set correctly
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	try {
	            	Identity.setSecurityEnabled(false);
	            	
	            	GeoCodingService gs = (GeoCodingService) Component.getInstance("kiwi.core.geoCodingService");
	            	
	                final Location actual = gs.getLocationGoogle("brzlbrnft");
	                
	                Assert.fail("GeoCodingFailedException expected!");
            	} catch(GeoCodingFailedException ex) {
            		
            	}
             }
    	}.run();

	}
	
	
	@Test
	public void testGeonamesCityFirst() throws Exception {
	   	/*
		 * Check whether we can set configuration options and they are immediately set correctly
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	GeoCodingService gs = (GeoCodingService) Component.getInstance("kiwi.core.geoCodingService");
            	
                final Location expected = new Location(47.8, 13.03);
                final Location actual = gs.getLocationGeonames("Salzburg");
                assertEquals(expected.getLatitude(), actual.getLatitude(), 0.02);
                assertEquals(expected.getLongitude(), actual.getLongitude(), 0.02);

             }
    	}.run();

	}

	@Test
	public void testGeonamesCityNearest() throws Exception {
	   	/*
		 * Check whether we can set configuration options and they are immediately set correctly
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	GeoCodingService gs = (GeoCodingService) Component.getInstance("kiwi.core.geoCodingService");
            	
                final Location salzburg = new Location(47.417, 13.25);
                final Location expected = new Location(47.35, 13.2);
                final Location actual = gs.getLocationGeonames("St. Johann",salzburg);
                assertEquals(expected.getLatitude(), actual.getLatitude(), 0.1);
                assertEquals(expected.getLongitude(), actual.getLongitude(), 0.1);

             }
    	}.run();

	}

	
	@Test
	public void testGeonamesCityFailed() throws Exception {
	   	/*
		 * Check whether we can set configuration options and they are immediately set correctly
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	GeoCodingService gs = (GeoCodingService) Component.getInstance("kiwi.core.geoCodingService");
            	
            	try {
            		final Location actual = gs.getLocationGeonames("brzlbrnft");
            		
            		Assert.fail("GeoCodingFailedException expected!");
            	} catch(GeoCodingFailedException ex) {
            		
            	}
             }
    	}.run();

	}

}
