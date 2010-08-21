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

package kiwi.action.inspector;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;

import kiwi.api.config.ConfigurationService;
import kiwi.api.importexport.ExportService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.tagging.Tag;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * The KiWi Inspector.
 * 
 * Allows to show details about the current content item and user
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("inspectorAction")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class InspectorAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@In(create = true)
	private ContentItem currentContentItem;
	
	@In
	private TaggingService taggingService;

	@In
	private ConfigurationService configurationService;
	
	@In("kiwi.core.exportService")
	private ExportService exportService;
	
	@In
	private TripleStore tripleStore;

	@In FacesMessages facesMessages;

	
	
	public String getContentItemRDF() {
		String result = "";
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		List<ContentItem> items = new LinkedList<ContentItem>();
		items.add(currentContentItem);
		
		exportService.exportItems(items, "application/rdf+xml", out);
		
		try {
			result += new String(out.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("exception while dumping RDF: #0",e.getMessage());
		}
		result = result.replaceAll("&","&amp;");
		result = result.replaceAll("<","&lt;");
		
		StringBuffer buf = new StringBuffer();
		String local_url = configurationService.getBaseUri();
		Pattern pat = Pattern.compile("http://[a-zA-Z0-9-#:_/.]+");
		Matcher m = pat.matcher(result);
		while(m.find()) {
			String uri = m.group();
			m.appendReplacement(buf, "<a href=\""+local_url+"/home.seam?uri="+uri+"\">"+uri+"</a>");
		}
		m.appendTail(buf);
		
		return buf.toString();
	}
	
	public List<Tag> listTags() {
		return taggingService.getTags(currentContentItem);
	}
	
	public String getTagRDF(Tag t) {
		String result = "";
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		List<ContentItem> items = new LinkedList<ContentItem>();
		items.add(t.getResource().getContentItem());
		
		exportService.exportItems(items, "application/rdf+xml", out);
		
		try {
			result += new String(out.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("exception while dumping RDF: #0",e.getMessage());
		}
		return result;
	}
	
	// Add triple
	private String subject;
	private String property;
	private String object;
	private String triple;


	private boolean isAbsoluteURI(String uri) {
		try {
			URI u = new URI(uri);
			return u.isAbsolute();
		} catch (URISyntaxException e) {
			return false;
		}		
	}
	
	public String addTriple() {
		KiWiResource subject;
		KiWiUriResource property;
		KiWiNode object;
		
		if (isAbsoluteURI(this.subject)) {
			subject = tripleStore.createUriResource(this.subject);
		} else if (this.subject.startsWith("_:")) {
			subject = tripleStore.createAnonResource(this.subject.substring(2));
		} else {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, "Subject must be either an absolute URI or an anonymous node starting with \"_:\".");
			return "failed";
		}
		
		if (isAbsoluteURI(this.property)) {
			property = tripleStore.createUriResource(this.property);
		} else {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, "Property must be an absolute URI.");
			return "failed";
		}
		
		if (isAbsoluteURI(this.object)) {
			object = tripleStore.createUriResource(this.object);
		} else if (this.object.startsWith("_:")) {
			object = tripleStore.createAnonResource(this.object.substring(2));
		} else
			object = tripleStore.createLiteral(this.object);
		
		KiWiTriple triple = tripleStore.createTriple(subject, property, object);
		
		if (triple.isNew())
			facesMessages.add(FacesMessage.SEVERITY_INFO, "Successfuly created new triple: {0}", triple.toString());
		else
			facesMessages.add(FacesMessage.SEVERITY_INFO, "Triple has already existed. It has id {0}", triple.getId());			
		
		return "success";
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getTriple() {
		return triple;
	}

	public void setTriple(String triple) {
		this.triple = triple;
		
		String[] s = triple.split(" +", 3);
		if (s.length > 0)
			subject = s[0];
		if (s.length > 1)
			property = s[1];
		if (s.length > 2)
			object = s[2];
	}	
}





