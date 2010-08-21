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
package kiwi.test.service.exporter;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;

import kiwi.api.content.ContentItemService;
import kiwi.api.importexport.exporter.Exporter;
import kiwi.api.multimedia.MultimediaService;
import kiwi.model.content.ContentItem;
import kiwi.test.base.KiWiTest;

import org.apache.commons.io.FileUtils;
import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.annotations.Test;

/**
 * MultimediaServiceTest - Test the functionality of the MultimediaService
 *
 * @author Sebastian Schaffert
 *
 */
@Test
public class KiWiExporterTest extends KiWiTest {

	
	/**
	 * Test whether extracting metadata works; for this purpose, we call 
	 * ContentItemService.createMultimediaItem, which in turn uses extractMetadata from 
	 * MultimediaService
	 * 
	 * @throws Exception
	 */
	@Test
	public void testExportKiWi() throws Exception {
		
		final URL datapath = this.getClass().getResource("data");
		
    	String[] ontologies = {
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
            	
            	MultimediaService  ms = (MultimediaService) Component.getInstance("multimediaService");
            	ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	File f1 = new File(datapath.getPath()+File.separator+"kiwis_in_prague-4.jpg");
            	            	
            	byte[] data     = FileUtils.readFileToByteArray(f1);
            	String mimeType = ms.getMimeType(f1);
            	String fileName = f1.getName();
            	
            	ContentItem c1 = cs.createMediaContentItem(data, mimeType, fileName);
            	
            	File f2 = new File(datapath.getPath()+File.separator+"kiwi1.png");
            	
            	byte[] data2     = FileUtils.readFileToByteArray(f2);
            	String mimeType2 = ms.getMimeType(f2);
            	String fileName2 = f2.getName();
            	
            	ContentItem c2 = cs.createMediaContentItem(data2, mimeType2, fileName2);
            	
            	File f3 = new File(datapath.getPath()+File.separator+"KiWi_ESWC08.pdf");
            	
            	byte[] data3     = FileUtils.readFileToByteArray(f3);
            	String mimeType3 = ms.getMimeType(f3);
            	String fileName3 = f3.getName();
            	
            	ContentItem c3 = cs.createMediaContentItem(data3, mimeType3, fileName3);
            	
            	File f4 = new File(datapath.getPath()+File.separator+"KiWi_ESWC08.doc");
            	
            	byte[] data4     = FileUtils.readFileToByteArray(f4);
            	String mimeType4 = ms.getMimeType(f4);
            	String fileName4 = f4.getName();
            	
            	ContentItem c4 = cs.createMediaContentItem(data4, mimeType4, fileName4);
            	
              }
    	}.run();

    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);
            	
            	MultimediaService  ms = (MultimediaService) Component.getInstance("multimediaService");
            	ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	Exporter exporter = (Exporter) Component.getInstance("kiwi.service.exporter.kiwi");
            	
            	Collection<ContentItem> items = cs.getContentItems();
            	
            	File f = new File("/tmp/export.kiwi");
            	OutputStream out = FileUtils.openOutputStream(f);
            	exporter.exportItems(items, "application/x-kiwi", out);
            	out.close();
              }
    	}.run();

		clearDatabase();
	}
	
	


}
