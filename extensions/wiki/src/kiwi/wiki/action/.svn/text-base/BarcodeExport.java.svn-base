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

package kiwi.wiki.action;


import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.user.User;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


/**
 * Reads the current content item and exports its content like 2d
 * barcode. This implementation can export two kinds of barcode
 * <ul>
 * <li>contact - name, email, memo
 * <li>location - latitude and longitude
 * </ul>
 * 
 * @author mradules
 * @version 04-pre
 * @since 04-pre
 */
@Name("barcodeExportAction")
@Scope(ScopeType.CONVERSATION)
public class BarcodeExport implements Serializable {

    private static final long serialVersionUID = -3628049977502899019L;

    @Logger
    private static Log log;

    @In(required = true)
    private ContentItem currentContentItem;

    @In
    private KiWiEntityManager kiwiEntityManager;

    private byte[] contactBarcode;

    private byte[] locationBarcode;

    public BarcodeExport() {
        // TODO Auto-generated constructor stub
    }

    @Create
    public void init()  {
        final User author = currentContentItem.getAuthor();
        final String firstName = author.getFirstName().replace(' ', '+');
        final String lastName = author.getLastName().replace(' ', '+');
        final String name = firstName + "+" + lastName;
        final String email = author.getEmail() != null ? author.getEmail().replaceAll("[@]", "%40") : "" ;
        final TextContent textContent = currentContentItem.getTextContent();
        final String contentXML =
                textContent == null ? "" :
                textContent.getXmlString();
        final String content =
                getNoteContent(contentXML).replaceAll("[@]", "%40");
// http://chart.apis.google.com/chart?cht=qr&chs=350x350&chl=MECARD%3AN%3AMihai+Radulescu+kKK%3BEMAIL%3Adddd%40sss.com%3BNOTE%3AXXXXXX%3B%3B
// http://chart.apis.google.com/chart?cht=qr&chs=350x350&chl=geo%3A46.936257195048825%2C9.84375
        final String contactURI = buildContactUri(name, email, content);
        
        try {
	        contactBarcode = generateBarcode(contactURI);
	
	        final LocationFacade poi =
	                kiwiEntityManager.createFacade(currentContentItem,
	                        LocationFacade.class);
	        final double latitude = poi.getLatitude();
	        final double longitude = poi.getLongitude();
	        final String locationURI = buildLocationUri(latitude, longitude);
	        locationBarcode = generateBarcode(locationURI);
        } catch(IOException ex) {
        	log.error("could not create barcodes:",ex);
        }
    }

    private String buildContactUri(String name, String email, String content) {

        final StringBuilder result = new StringBuilder();
        result
                .append("http://chart.apis.google.com/chart?cht=qr&chs=350x350&chl=MECARD%3AN%3A");
        result.append(name);
        result.append("%3B%3AEMAIL%3A");
        result.append(email);
        result.append("%3B%3ANOTE%3A");
        result.append(content);
        result.append("3B%3B");

        return result.toString();
    }

    private String buildLocationUri(double lat, double lon) {

        final StringBuilder result = new StringBuilder();
        result
                .append("http://chart.apis.google.com/chart?cht=qr&chs=350x350&chl=geo%3A");
        result.append(lat);
        result.append("%2C");
        result.append(lon);


        return result.toString();
    }


    private String getNoteContent(String in) {
        final int start = in.indexOf("<p>");
        final int end = in.indexOf("</p>");

        final String result = in.substring(start + 3, end);
        return result;

    }

    private byte[] generateBarcode(String uri) throws IOException {


        final URL url = new URL(uri);

        final InputStream inputStream = url.openStream();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[16368];

        for (int bytesRead = 0; bytesRead != -1; bytesRead =
                inputStream.read(buffer)) {
            outputStream.write(buffer, 0, bytesRead);
        }

        final byte[] result = outputStream.toByteArray();
        return result;
    }

    public byte[] getLocationBarcode() {
        return locationBarcode;
    }

    public void setLocationBarcode(byte[] locationBarcode) {
        this.locationBarcode = locationBarcode;
    }

    public byte[] getContactBarcode() {
        return contactBarcode;
    }

    public void setContactBarcode(byte[] contactBarcode) {
        this.contactBarcode = contactBarcode;
    }

}
