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
package kiwi.action.template;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import kiwi.api.content.ContentItemService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

/**
 * @author Rolf Sint
 * 
 */
@Name("sFormTemplAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
@AutoCreate
public class SFormTemplAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private static Log log;

	@In
	private EntityManager entityManager;

	private String templateTitle;

	@In(required = false)
	private ContentItemService contentItemService;

	@In(create = true)
	@Out(required = false)
	private ContentItem currentContentItem;

	public void createNewTemplate() {

		ContentItem templateItem = contentItemService
				.getContentItemByUri("http://www.kiwi-project.eu/kiwi/core/Template");

		if (templateItem != null) {

			String defaultText = "<html>This is a template for a semantic form.<br/>"
				+ "Use RDFA to define placeholders which should be used "
				+ "as form fields in the instance of the template"
				+ "</html>";
			
			KiWiResource templateResource = templateItem.getResource();

			ContentItem contentItem = contentItemService.createTextContentItem(defaultText);
			contentItemService.updateTitle(contentItem, templateTitle);
			
			
			contentItem.addType((KiWiUriResource) templateResource);
			// contentItem.getTypes().add(arg0);

			contentItemService.saveContentItem(contentItem);
			currentContentItem = contentItem;

		} else {
			log.error("Ontologies must be loaded");
		}

	}

	public Collection<ContentItem> getTemplates() {

		javax.persistence.Query q = entityManager
				.createQuery("select t.subject.contentItem "
						+ "from KiWiTriple t " + "where t.property.uri = '"
						+ Constants.NS_RDF + "type' "
						+ "  and t.object.uri = '" + Constants.NS_KIWI_CORE
						+ "Template'" + "  and t.deleted = false "
						+ "  and t.subject.contentItem.title is not null "
						+ " order by t.subject.contentItem.title asc");

		// javax.persistence.Query q =
		// entityManager.createQuery("select c from ContentItem c, KiWiTriple t where t.subject.id = c.resource.id and t.property.uri = :rdftype and t.object.uri = :template");
		// q.setParameter("rdftype", Constants.NS_RDF+"type");
		// q.setParameter("template",
		// "http://www.kiwi-project.eu/kiwi/core/Template");
		// q.setParameter("template", Constants.NS_RDFS + "Class'");

		List<ContentItem> result = q.getResultList();

		return result;
	}

	public String getTemplateTitle() {
		return templateTitle;
	}

	public void setTemplateTitle(String templateTitle) {
		this.templateTitle = templateTitle;
	}

}
