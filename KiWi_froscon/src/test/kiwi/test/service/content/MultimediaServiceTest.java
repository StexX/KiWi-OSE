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
package kiwi.test.service.content;

import java.io.File;
import java.net.URL;

import kiwi.api.content.ContentItemService;
import kiwi.api.multimedia.MultimediaService;
import kiwi.model.content.ContentItem;
import kiwi.test.base.KiWiTest;

import org.apache.commons.io.FileUtils;
import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * MultimediaServiceTest - Test the functionality of the MultimediaService
 *
 * @author Sebastian Schaffert
 *
 */
@Test
public class MultimediaServiceTest extends KiWiTest {

	/**
	 * Test whether getMimeType(File) returns the correct mime type for different kinds of content
	 * @throws Exception
	 */
	@Test
	public void testGetMimeTypeJPEG() throws Exception {
		final URL datapath = this.getClass().getResource("data");


		/*
		 * Test a JPEG file
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final MultimediaService ms = (MultimediaService) Component.getInstance("multimediaService");

            	final File f = new File(datapath.getPath()+File.separator+"kiwis_in_prague-4.jpg");

            	final String type = ms.getMimeType(f);
            	Assert.assertEquals(type, "image/jpeg");
              }
    	}.run();

	}

	/**
	 * Test whether getMimeType(File) returns the correct mime type for different kinds of content
	 * @throws Exception
	 */
	@Test
	public void testGetMimeTypePNG() throws Exception {
		final URL datapath = this.getClass().getResource("data");


		/*
		 * Test a PNG file
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final MultimediaService ms = (MultimediaService) Component.getInstance("multimediaService");

            	final File f = new File(datapath.getPath()+File.separator+"kiwi1.png");
            	Assert.assertTrue(f.exists());

            	final String type = ms.getMimeType(f);
            	Assert.assertEquals(type, "image/png");
              }
    	}.run();

	}

	/**
	 * Test whether getMimeType(File) returns the correct mime type for different kinds of content
	 * @throws Exception
	 */
	@Test
	public void testGetMimeTypePDF() throws Exception {
		final URL datapath = this.getClass().getResource("data");

		/*
		 * Test a PDF file
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final MultimediaService ms = (MultimediaService) Component.getInstance("multimediaService");

            	final File f = new File(datapath.getPath()+File.separator+"KiWi_ESWC08.pdf");
            	Assert.assertTrue(f.exists());

            	final String type = ms.getMimeType(f);
            	Assert.assertEquals(type, "application/pdf");
              }
    	}.run();
	}

	/**
	 * Test whether getMimeType(File) returns the correct mime type for different kinds of content
	 * @throws Exception
	 */
	@Test
	public void testGetMimeTypeWord() throws Exception {
		final URL datapath = this.getClass().getResource("data");

		/*
		 * Test a Word file
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final MultimediaService ms = (MultimediaService) Component.getInstance("multimediaService");

            	final File f = new File(datapath.getPath()+File.separator+"KiWi_ESWC08.doc");
            	Assert.assertTrue(f.exists());

            	final String type = ms.getMimeType(f);
            	Assert.assertEquals(type, "application/msword");
              }
    	}.run();
	}

	/**
	 * Test whether extracting metadata works; for this purpose, we call
	 * ContentItemService.createMultimediaItem, which in turn uses extractMetadata from
	 * MultimediaService
	 *
	 * @throws Exception
	 */
	@Test
	public void testExtractMetadataJPEG() throws Exception {

		final URL datapath = this.getClass().getResource("data");

    	final String[] ontologies = {
    			"ontology_kiwi.owl",
    			"imports/foaf.owl",
    			"imports/sioc.owl",
    			"imports/hgtags.owl",
    			"imports/skos-core.rdf",
    			"imports/exif.rdf"
    	};
		setupDatabase(ontologies);

		/*
		 * Test a JPEG file
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final MultimediaService  ms = (MultimediaService) Component.getInstance("multimediaService");
            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final File f = new File(datapath.getPath()+File.separator+"kiwis_in_prague-4.jpg");

            	final byte[] data     = FileUtils.readFileToByteArray(f);
            	final String mimeType = ms.getMimeType(f);
            	final String fileName = f.getName();

            	final ContentItem c = cs.createMediaContentItem(data, mimeType, fileName);

            	Assert.assertEquals(c.getTitle(), fileName);
            	Assert.assertNotNull(c.getMediaContent());
            	Assert.assertEquals(c.getMediaContent().getMimeType(), "image/jpeg");
            	Assert.assertEquals(c.getMediaContent().getFileName(), fileName);
            	Assert.assertEquals(c.getMediaContent().getData(), data);
              }
    	}.run();

    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	Component.getInstance("multimediaService");
            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.getContentItemByTitle("kiwis_in_prague-4.jpg");

             	Assert.assertEquals(c.getTitle(), "kiwis_in_prague-4.jpg");
            	Assert.assertNotNull(c.getMediaContent());
            	Assert.assertEquals(c.getMediaContent().getMimeType(), "image/jpeg");
            	Assert.assertEquals(c.getMediaContent().getFileName(), "kiwis_in_prague-4.jpg");

            	//TODO fix exif extraction
            	// check for presence of certain RDF properties
            	//Assert.assertNotNull(c.getResource().getProperty("exif:fNumber"));
            	//Assert.assertNotNull(c.getResource().getProperty("exif:focalLength"));
              }
    	}.run();

		clearDatabase();
	}


	/**
	 * Test whether extracting metadata works; for this purpose, we call
	 * ContentItemService.createMultimediaItem, which in turn uses extractMetadata from
	 * MultimediaService
	 *
	 * @throws Exception
	 */
	@Test
	public void testExtractMetadataPDF() throws Exception {

		final URL datapath = this.getClass().getResource("data");

    	final String[] ontologies = {
    			"ontology_kiwi.owl",
    			"imports/dcelements.rdf"
    	};
		setupDatabase(ontologies);

		/*
		 * Test a JPEG file
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final MultimediaService  ms = (MultimediaService) Component.getInstance("multimediaService");
            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final File f = new File(datapath.getPath()+File.separator+"KiWi_ESWC08.pdf");

            	final byte[] data     = FileUtils.readFileToByteArray(f);
            	final String mimeType = ms.getMimeType(f);
            	final String fileName = f.getName();

            	final ContentItem c = cs.createMediaContentItem(data, mimeType, fileName);

            	Assert.assertEquals(c.getTitle(), fileName);
            	Assert.assertNotNull(c.getMediaContent());
            	Assert.assertEquals(c.getMediaContent().getMimeType(), "application/pdf");
            	Assert.assertEquals(c.getMediaContent().getFileName(), fileName);
            	Assert.assertEquals(c.getMediaContent().getData(), data);
              }
    	}.run();

    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	Component.getInstance("multimediaService");
            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.getContentItemByTitle("KiWi_ESWC08.pdf");

             	Assert.assertEquals(c.getTitle(), "KiWi_ESWC08.pdf");
            	Assert.assertNotNull(c.getMediaContent());
            	Assert.assertEquals(c.getMediaContent().getMimeType(), "application/pdf");
            	Assert.assertEquals(c.getMediaContent().getFileName(), "KiWi_ESWC08.pdf");

            	// check for presence of certain RDF properties
            	Assert.assertNotNull(c.getResource().getProperty("dc:subject"));
            	Assert.assertNotNull(c.getResource().getProperty("dc:title"));
              }
    	}.run();

		clearDatabase();
	}

	/**
	 * Test whether extracting metadata works; for this purpose, we call
	 * ContentItemService.createMultimediaItem, which in turn uses extractMetadata from
	 * MultimediaService
	 *
	 * @throws Exception
	 */
	@Test
	public void testExtractMetadataWord() throws Exception {

		final URL datapath = this.getClass().getResource("data");

    	final String[] ontologies = {
    			"ontology_kiwi.owl",
    			"imports/dcelements.rdf"
    	};
		setupDatabase(ontologies);

		/*
		 * Test a JPEG file
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final MultimediaService  ms = (MultimediaService) Component.getInstance("multimediaService");
            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final File f = new File(datapath.getPath()+File.separator+"KiWi_ESWC08.doc");

            	final byte[] data     = FileUtils.readFileToByteArray(f);
            	final String mimeType = ms.getMimeType(f);
            	final String fileName = f.getName();

            	final ContentItem c = cs.createMediaContentItem(data, mimeType, fileName);

            	Assert.assertEquals(c.getTitle(), fileName);
            	Assert.assertNotNull(c.getMediaContent());
            	Assert.assertEquals(c.getMediaContent().getMimeType(), "application/msword");
            	Assert.assertEquals(c.getMediaContent().getFileName(), fileName);
            	Assert.assertEquals(c.getMediaContent().getData(), data);
              }
    	}.run();

    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	Component.getInstance("multimediaService");
            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.getContentItemByTitle("KiWi_ESWC08.doc");

             	Assert.assertEquals(c.getTitle(), "KiWi_ESWC08.doc");
            	Assert.assertNotNull(c.getMediaContent());
            	Assert.assertEquals(c.getMediaContent().getMimeType(), "application/msword");
            	Assert.assertEquals(c.getMediaContent().getFileName(), "KiWi_ESWC08.doc");

            	// check for presence of certain RDF properties
            	Assert.assertNotNull(c.getResource().getProperty("dc:date"));
            	Assert.assertNotNull(c.getResource().getProperty("dc:title"));
              }
    	}.run();

		clearDatabase();
	}


}
