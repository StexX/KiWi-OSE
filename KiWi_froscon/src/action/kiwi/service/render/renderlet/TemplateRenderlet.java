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
package kiwi.service.render.renderlet;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.XPathContext;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * Used for the Semantic Forms
 * 
 * This template is called "directly" by the RenderJSFWebservice and 
 * renders the template of a contentitem
 * It is used to replace the rdfa properties within an Instance of a template with
 * richfaces inplaceinput fields
 * 
 * @author Rolf Sint
 *	
 */

@Name("kiwi.service.render.renderlet.templateRenderlet")
@AutoCreate
@Stateless
public class TemplateRenderlet implements SFRenderlet {
	
	@In
	private EntityManager entityManager;
	
	@Logger
	private static Log log;
	
	//get the contentItemid to get the current ContentItem, passinf
	public Document applySf(KiWiResource context,Document content,long id){
		log.debug("Applying renderlet ...");
		log.debug("id "+id);
		
		
		ContentItem currentContentItem = entityManager.find(ContentItem.class, id);
		
		if(currentContentItem != null){
			log.info(currentContentItem.getTitle());
		}
		else{
			log.info("Currentcontentitem = null");
		}
		
		XPathContext namespaces = new XPathContext();
		namespaces.addNamespace("html", Constants.NS_XHTML);
		namespaces.addNamespace("ui", "http://java.sun.com/jsf/facelets");
		namespaces.addNamespace("h", "http://java.sun.com/jsf/html");
		namespaces.addNamespace("rich", "http://richfaces.org/rich");
		namespaces.addNamespace("xmnls", Constants.NS_XHTML);
		
		
		log.debug("XPath query to get all properties ...");
		
		
		//search for all rdfa property fields in the document
		Nodes nodes = content.query("//node()[@property]", namespaces);

		//iterate through each of them
		for (int i = 0; i < nodes.size(); ++i) {	
			log.info("Iterating Nodes");
				
			Element e = (Element) nodes.get(i);
			
			//gets the value of the rdfa property, like e.g. foaf:firstName
			String property = e.getAttributeValue("property");
			assert(property != null);
			
			Element hForm = new Element("form");
			hForm.setNamespaceURI("http://java.sun.com/jsf/html");	
			Attribute at = new Attribute("ajaxSubmit","true");
			hForm.addAttribute(at);
			
			//builds the Element inplaceInput, which is used to replace the rdfa field
			Element inplaceInput = new Element("inplaceInput");
			Attribute inputLabelAttr = new Attribute("defaultLabel","#{templateEditingAction.getFieldValue(\""+property+"\",\""+id+"\" )}");
			Attribute inputValueAttr = new Attribute("value","#{templateEditingAction.getFieldValue(\""+property+"\",\""+id+"\" )}");
			Attribute showControls = new Attribute("showControls", "true");
			Attribute controlsHorizontalPosition = new Attribute("controlsHorizontalPosition", "left");	
			Attribute controlsVerticalPosition = new Attribute("controlsVerticalPosition", "bottom");
			Attribute maxInputWidth = new Attribute("maxInputWidth", "500px");	
			Attribute ova = new Attribute("onviewactivated","save(this.component.getValue(),\""+property+"\",\""+id+"\" );");
			
			inplaceInput.addAttribute(inputLabelAttr);
			inplaceInput.addAttribute(inputValueAttr);
			inplaceInput.addAttribute(showControls);
			inplaceInput.addAttribute(controlsHorizontalPosition);
			inplaceInput.addAttribute(controlsVerticalPosition);
			inplaceInput.addAttribute(maxInputWidth);
			inplaceInput.addAttribute(ova);
			inplaceInput.setNamespaceURI("http://richfaces.org/rich");			
			
			hForm.appendChild(inplaceInput);
			
			//replace the rdfa property with the inputtext element
			e.getParent().replaceChild(nodes.get(i), hForm);
			
			log.info(e.getValue());	
		}
		
		return content;
	}
}
