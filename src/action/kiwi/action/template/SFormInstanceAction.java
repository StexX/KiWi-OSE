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

import kiwi.api.content.ContentItemService;
import kiwi.exception.NamespaceResolvingException;
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
@Name("sFormInstanceAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
@AutoCreate
public class SFormInstanceAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private static Log log;

	@In(required = false)
	private ContentItemService contentItemService;

	@In(create = true)
	@Out(required = false)
	private ContentItem currentContentItem;

	private ContentItem selectedTemplate;

	public ContentItem getSelectedTemplate() {
		return selectedTemplate;
	}

	public void setSelectedTemplate(ContentItem selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}

	public void deleteSelectedTemplate(){
		contentItemService.removeContentItem(selectedTemplate);
	}
	
	public void createNewInstance() {
		final String defaultText = "<html>This is a concrete instance of a form template.<br/></html>";
		
		log.info("Creating new instance from template #0",selectedTemplate.getTitle());
		
		ContentItem fromTemplateItem = contentItemService
										.getContentItemByUri("http://www.kiwi-project.eu/kiwi/core/FromTemplate");

		ContentItem contentItem = contentItemService.createTextContentItem(defaultText);
		
		//add all the types from the template to the instance, expect the type Template
		Collection<KiWiResource> resourceCollection = selectedTemplate.getTypes();
		for(KiWiResource kr:  resourceCollection){
			KiWiUriResource kuri = (KiWiUriResource) kr;
			if (!kuri.getUri().equals("http://www.kiwi-project.eu/kiwi/core/Template")){
				contentItem.addType((KiWiUriResource) kr);
			}
		}
		
		try {
			selectedTemplate.getResource().addOutgoingNode(
					"http://www.kiwi-project.eu/kiwi/core/hasTemplateInstance", 
					contentItem.getResource());
			contentItem.getResource().addOutgoingNode(
					"http://www.kiwi-project.eu/kiwi/core/instancesTemplate", 
					selectedTemplate.getResource());
		} catch (NamespaceResolvingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		contentItemService.updateTitle(contentItem, 
				"Instances template " + selectedTemplate.getTitle());

		KiWiResource fromTemplateResource = fromTemplateItem.getResource();
		contentItem.addType((KiWiUriResource) fromTemplateResource);

		contentItemService.saveContentItem(contentItem);

		currentContentItem = contentItem;
	}
}
