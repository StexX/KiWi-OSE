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
package kiwi.service.render;

import static kiwi.model.kbase.KiWiQueryLanguage.SPARQL;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.render.RenderingService;
import kiwi.context.CurrentUserFactory;
import kiwi.exception.NonUniqueRelationException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.user.User;
import kiwi.service.render.renderlet.SFRenderlet;
import nu.xom.Builder;
import nu.xom.Document;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

/**
 * This is a RESTful webservice rendering the current content item as HTML,
 * including support for JSF components. The webservice is called by an
 * appropriate facelets ui:include in JSF files as
 * 
 * <ui:include src="/KiWi/seam/resources/services/render/html" />
 * 
 * @author Sebastian Schaffert
 * 
 */
@Name("renderJSFWebService")
@Path("/render")
@Scope(ScopeType.EVENT)
//@Transactional
public class RenderJSFWebService {

	@Logger
	private Log log;

	@In(create=true)
	private EntityManager entityManager;

	@In
	private RenderingService renderingPipeline;

	private String currentContentHtml;
	private String currentTemplateHtml;

	@In
	ContentItemService contentItemService;

	@In
	private KiWiEntityManager kiwiEntityManager;

	@In(value = "kiwi.service.render.renderlet.templateRenderlet")
	private SFRenderlet templateRenderlet;

	// private static final Pattern wrapRE =
	// Pattern.compile(".{0,79}(?:\\S(?:-| |$)|$)");

	/**
	 * Generate all information templates to the item
	 * 
	 * @return the currentContentHtml
	 */
	@GET
	@Path("/templateHtml/{id}")
	// @Produces("application/xhtml+xml; charset=utf-8")
	@Produces("text/xml; charset=utf-8")
	public byte[] getTemplateHtml(@PathParam("id") long id) {

		boolean templateExists = false;

		if (currentTemplateHtml == null) {
			ContentItem currentContentItem = entityManager.find(
					ContentItem.class, id);

			log.debug(currentContentItem.getTitle());

			currentTemplateHtml = "<div style='font-size:9px;'>Templates...<br/></div>";

			// specifies type FromTemplate
			ContentItem ciType = contentItemService
					.getContentItemByUri("http://www.kiwi-project.eu/kiwi/core/FromTemplate");

			if (ciType != null) {
				// get all types of currentContentItem and iterate through them
				// check if there is a tempalate for that type,
				// check if the content item contains a FromTemplate type

				// the template is only embedded in the case the contentitem
				// has the type fromTemplate
				templateExists = hasTemplate(currentContentItem, ciType
						.getResource());
				if (templateExists) {
					addTemplateToContentItem(currentContentItem);
				}

			}

			// if there is, render JSF out of it using the currentContentItem
			// and put the rendered template at the end of currentTemplateHtml.
		}

		// hackish workaround for Facelets-Bug (331), fixes KIWI-528
		currentTemplateHtml = currentTemplateHtml.replaceAll("\\s<", "#{' '}<");

		// strip out all kiwi xml namespace declarations, workaround for
		// KIWI-540 / Facelets-350
		// currentContentHtml =
		// currentContentHtml.replaceAll("xmlns(:[a-zA-Z]+)?=\"[^\"]+\"", "");
		currentTemplateHtml = currentTemplateHtml.replaceAll(
				"xmlns:kiwi=\"[^\"]+\"", "");

		StringBuffer jsFunkt = new StringBuffer();
		jsFunkt.append("<s:remote include=\"templateEditingAction\" />");
		jsFunkt.append("<script type=\"text/javascript\">");
		jsFunkt
				.append("var templateEditingAction = Seam.Component.getInstance(\"templateEditingAction\");");
		jsFunkt.append("function save(value,property,id){");
		jsFunkt.append("templateEditingAction.save(value,property,id,");
		jsFunkt.append("function(){");
		jsFunkt.append("window.location.reload();");
		jsFunkt.append("}");
		jsFunkt.append(");");
		jsFunkt.append("}");
		jsFunkt.append("</script>");

		// fixes KIWI-500, prevents currentContentHtml from being surrounded
		// with any <html>...</html>
		String result = "<html><head></head><body><ui:composition "
				+ "xmlns:ui=\"http://java.sun.com/jsf/facelets\" "
				+ "xmlns:rich=\"http://richfaces.org/rich\" "
				+ "xmlns:s=\"http://jboss.com/products/seam/taglib\" "
				+ "xmlns:h=\"http://java.sun.com/jsf/html\" " + "xmlns:kiwi=\""
				+ Constants.NS_KIWI_HTML + "\" " + "xmlns=\""
				+ Constants.NS_XHTML + "\">\n" + jsFunkt + " "
				+ currentTemplateHtml + "</ui:composition></body></html>\n";

		log.debug("getTemplateHtml: #0", result);

		// calls the templateRenderlet
		if (templateExists) {
			try {
				StringReader sr = new StringReader(result);
				final Builder parser = new Builder(false);
				Document doc = parser.build(sr);
				log.info("Current document #0", doc.toXML());
				doc = templateRenderlet.applySf(null, doc, id);
				result = doc.toXML();
				log.info("This is the resultx: #0", result);

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try {
			return result.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			log
					.error("unsupported encoding: utf-8, falling back to default encoding");
			return result.getBytes();
		}
	}

	private boolean hasTemplate(ContentItem currentContentItem,
			KiWiResource templateType) {
		for (KiWiResource kr : currentContentItem.getTypes()) {
			// the template is only embedded in the case the contentitem
			// has the type fromTemplate
			if (kr == templateType) {
				return true;
			}
		}
		return false;
	}

	// This method is called when a template of the contentitem exists,
	// this is when the contentitem it has a type "FromTemplate"
	private void addTemplateToContentItem(ContentItem currentContentItem) {
		assert (currentContentItem != null);
		// Queries the according template of the instance
		Query query = kiwiEntityManager.createQuery(
				"SELECT ?C WHERE {:ci kiwi:instancesTemplate ?C}", SPARQL,
				ContentItem.class);
		query.setParameter("ci", currentContentItem);
		try {
			ContentItem templateContentItem = (ContentItem) query
					.getSingleResult();

			if (templateContentItem != null) {
				log
						.info("Titel of Template #0", templateContentItem
								.getTitle());
				// adds the templateTextContent to the instance
				currentTemplateHtml += "------ Begin Template ----- <br/>";
				currentTemplateHtml += "This page has the template "
						+ templateContentItem.getTitle();
				currentTemplateHtml += templateContentItem.getTextContent()
						.getHtmlContent();
				currentTemplateHtml += "------ End Template ------";
				log.info(currentTemplateHtml);

			}
		} catch (NoResultException e) {
			currentTemplateHtml += "Error initializing a template ! \n this page should be an instance of a template, but according property is not set ";
		}
	}

	/**
	 * @return the currentContentHtml
	 */
	@GET
	@Path("/html/{id}/user/{uid}")
	// @Produces("application/xhtml+xml; charset=utf-8")
	@Produces("text/xml; charset=utf-8")
	public byte[] getContentHtml(@PathParam("id") long id, @PathParam("uid") Long uid) {
		if (currentContentHtml == null) {
			ContentItem currentContentItem = entityManager.find(
					ContentItem.class, id);

			log.debug(currentContentItem.getTitle());
			
			if (currentContentItem.getTextContent() != null) {
				if(uid != null) {
					User currentUser = entityManager.find(
							User.class, uid);
					currentContentHtml = renderingPipeline.renderHTML(currentContentItem, currentUser);
				} else {
					currentContentHtml = renderingPipeline.renderHTML(currentContentItem);
					log.debug("rendering html content: #0", currentContentHtml);
				} 
			} else {
					currentContentHtml = "<p>Please add initial content</p>";
			}
		}

		// hackish workaround for Facelets-Bug (331), fixes KIWI-528
		currentContentHtml = currentContentHtml.replaceAll("\\s<", "#{' '}<");

		// strip out all kiwi xml namespace declarations, workaround for
		// KIWI-540 / Facelets-350
		// currentContentHtml =
		// currentContentHtml.replaceAll("xmlns(:[a-zA-Z]+)?=\"[^\"]+\"", "");
		currentContentHtml = currentContentHtml.replaceAll(
				"xmlns:kiwi=\"[^\"]+\"", "");
		currentContentHtml = currentContentHtml.replaceAll(
				"kiwi:[a-zA-Z0-9_]+=\"[^\"]+\"", "");

		// fixes KIWI-500, prevents currentContentHtml from being surrounded
		// with any <html>...</html>
		String result = "<html><head></head><body><ui:composition "
				+ "xmlns:ui=\"http://java.sun.com/jsf/facelets\" "
				+ "xmlns:kiwi=\"" + Constants.NS_KIWI_HTML + "\" " + "xmlns=\""
				+ Constants.NS_XHTML + "\">\n" + currentContentHtml
				+ "</ui:composition></body></html>\n";

		// // line wrapping; needed so that RESTEasy does not break

		log.debug("getHtml: #0", result);

		// fixes KIWI-522
		try {
			return result.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			log
					.error("unsupported encoding: utf-8, falling back to default encoding");
			return result.getBytes();
		}
	}

	public List<ContentItem> getType(ContentItem ci, String type) {
		log.debug("getType");
		
		List<ContentItem> sc = new LinkedList<ContentItem>();
		// specifies type Person
		ContentItem ciType = contentItemService.getContentItemByUri(type);
		
		//  checks if contentItem is from type Person
		if ( (ciType != null) && (hasTemplate(ci, ciType.getResource())) ) {
			sc.add(ci);
			return sc;
		} else {
		//  if not from type person, return an empty list	
			return sc;
		}
	}

		
	public String getProperty(ContentItem ci, String rdfProperty) {
		String value = ""; // by default no value is returned
		try {
			value = ci.getResource().getProperty(rdfProperty);
		} catch (NonUniqueRelationException e) {
			e.printStackTrace();
		}
		return value;
	}
}
