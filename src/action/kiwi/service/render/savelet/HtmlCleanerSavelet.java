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

package kiwi.service.render.savelet;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;

import kiwi.model.Constants;
import kiwi.model.kbase.KiWiResource;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleXmlSerializer;
import org.htmlcleaner.TagNode;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;

/**
 * Clean up dirty HTML code so that it becomes proper XHTML, ready for parsing.
 * 
 * @author sschaffe
 *
 */
@Stateless
@Name("kiwi.service.render.savelet.HtmlCleanerSavelet")
@AutoCreate
public class HtmlCleanerSavelet implements SourceSavelet {

	public HtmlCleanerSavelet() {
		super();
	}

	/* (non-Javadoc)
	 * @see kiwi.backend.render.savelet.Savelet#apply(java.lang.Object)
	 */
	public String apply(KiWiResource context, String content) {

		
		HtmlCleaner cleaner = new HtmlCleaner();
		 
		// take default cleaner properties
		CleanerProperties props = cleaner.getProperties();
		props.setNamespacesAware(true);
		props.setOmitHtmlEnvelope(true);
		props.setOmitXmlDeclaration(true);
		
		// Clean HTML taken from simple string, file, URL, input stream, 
		// input source or reader. Result is root node of created 
		// tree-like structure. Single cleaner instance may be safely used
		// multiple times.
		try {
			TagNode node = null;
			
			String divOpen = "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\""+Constants
			.NS_KIWI_HTML+"\" kiwi:type=\"page\">";
			String divClose = "</div>";
			
			// if the content starts with the same
			// div tag it should be surrounded with another
//			if (content.startsWith(divOpen) && 
//					content.endsWith(divClose)) {
			if(hasKiWiRootTag(content)) {
				node = cleaner.clean(content);
			} else {
				node = cleaner.clean(divOpen + content + divClose);
			}
			
			return (new SimpleXmlSerializer(props)).getXmlAsString(node);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return content;
		}		
	}

	/**
	 * Check whether the content has the correct KiWi root tag (i.e. a html div with kiwi:type page).
	 * Evaluated by extracting the root element and trying to parse it using XOM.
	 * 
	 * @param content
	 * @return true if the kiwi root tag is present
	 */
	public static boolean hasKiWiRootTag(String content) {
		Document doc = parseRootTag(content);
		
		if(doc != null) {
			Element r = doc.getRootElement();

			if(r.getAttribute("type", Constants.NS_KIWI_HTML) != null && 
			   "page".equals(r.getAttribute("type", Constants.NS_KIWI_HTML).getValue()) &&
			   Constants.NS_XHTML.equals(r.getNamespaceURI("")) &&
			   "div".equals(r.getLocalName())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private static final Pattern ps = Pattern.compile("<([a-zA-Z][a-zA-Z0-9_:-]*)\\s*[^>]*>");
	
	private static Document parseRootTag(String tag) {
		
		String startTag = null, endTag = null;
		
		Matcher m1 = ps.matcher(tag);
		if(m1.find()) {
			startTag = m1.group();
			endTag = "</"+m1.group(1)+">";
		}
		
		
		
		if(startTag != null && endTag != null) {
			String rootTag = startTag + endTag;
			try {
				  Builder parser = new Builder();
				  Document doc = parser.build(rootTag, Constants.NS_KIWI_HTML);
				  return doc;
			} catch (ParsingException ex) {
				  System.err.println("XML is not wellformed: "+rootTag);
				  return null;
			} catch (IOException ex) {
				  System.err.println("Could not connect to Cafe con Leche. The site may be down.");
				  return null;
			}
		} else {
			return null;
		}
		
	}
}
