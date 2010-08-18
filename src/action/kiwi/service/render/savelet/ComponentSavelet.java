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

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.user.User;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * @author Marek Schmidt
 *
 */
@Stateless
@AutoCreate
@Name("kiwi.service.render.savelet.ComponentSavelet")
public class ComponentSavelet implements TextContentSavelet {
	
	@Logger
	private static Log log;
	
	@In
	private ContentItemService contentItemService;
	
	@In(create=true) 
	private User currentUser;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
		
	private boolean isKiWiRootTag (Node n) {
		if (!(n instanceof Element)) return false;
		Element e = (Element)n;
		return "div".equals(e.getLocalName()) && "page".equals(e.getAttributeValue("type", Constants.NS_KIWI_HTML));
	}
	
	private void applyRecursive(Element e, KiWiResource context, Set<KiWiResource> components) {
		
		/* The context for all the nodes below, the context does not change by default */
		KiWiResource nextContext = context;
		Set<KiWiResource> nextComponents = components;
		
		String componentKiwiId = e.getAttributeValue("component", Constants.NS_KIWI_HTML);
		ContentItem componentCi = null; 
		
		if (componentKiwiId != null) {
			componentCi = contentItemService.getContentItemByKiwiId(componentKiwiId);
			if (componentCi == null) {
				log.info("component #0 not found", componentKiwiId);
				return;
			}
			
			/* We are at the "about div" element, below as is the content of this component */
			nextContext = componentCi.getResource();
			
			/* Store all the components of this new component. */
			nextComponents = new HashSet<KiWiResource> ();
		}
		
		Elements children = e.getChildElements();
		for (int i = 0; i < children.size(); ++i) {
			applyRecursive (children.get(i), nextContext, nextComponents);
		}
		
		if (componentKiwiId != null) {
			
			/* update the list of components of the component "above" */
			components.add(componentCi.getResource());
			
			/* and update the nested content items property of _this_ content item. */
			List<ContentItem> nestedContentItems = new LinkedList<ContentItem> ();
			for (KiWiResource component: nextComponents) {
				nestedContentItems.add(component.getContentItem());
			}
			componentCi.setNestedContentItems(nestedContentItems);
			
			/* TODO: We should delete the components which are not standalone content items and not contained anywhere anymore
			 * (there is no such thing as "non-standalone" content item yet) */
			
			/* We have transformed all the content below, so now store the component content. */
			Document componentDoc = null;
			
			if (e.getChildCount() == 1 && isKiWiRootTag(e.getChild(0))) {
				Element root = children.get(0);
				
				root.detach();
				componentDoc = new Document(root);		
			}
			else {
				Element root = new Element("div", Constants.NS_XHTML);
				root.addAttribute(new Attribute("kiwi:type", Constants.NS_KIWI_HTML, "page"));
				while (e.getChildCount() > 0) {
					Node n = e.getChild(0);
					n.detach();
					root.appendChild(n);
				}
				
				componentDoc = new Document(root);
			}
			
			if (componentCi.getTextContent() == null) {
				log.info("previous textcontent is null");
			}
			else {
				log.info("previous textcontent: #0", componentCi.getTextContent().getXmlString());
			}
			
			log.info("storing component #0: #1", componentKiwiId, componentDoc.toXML());
			
			/* FIXME: deletes all textual contents :( 
			 * If you want to update a textcontent you should use contentItemService.updateTextContent(), 
			 * but why do you want to update that anyway?! The content did not change 
			 * 
			 * MS: How about this? 
			 * 
			 * (the content of the component may change, the ComponentRenderlet put the components to the text content,
			 *  so the user may edit the content of the components directly on the page of the containing content item, this Savelet tries to
			 *  put the (modified) content to where it belongs) 
			 * */
			contentItemService.updateTextContentItem(componentCi, componentDoc);

			currentUser = kiwiEntityManager.merge(currentUser);

			// update last author
			componentCi.setAuthor(currentUser);

			// update modification
			componentCi.setModified(new Date());

			contentItemService.saveContentItem(componentCi);

			log.info("stored textcontent: #0", componentCi.getTextContent().getXmlString());
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.service.render.savelet.Savelet#apply(kiwi.model.kbase.KiWiResource, java.lang.Object)
	 */
	@Override
	public TextContent apply(KiWiResource context, TextContent content) {
		Document doc = content.getXmlDocument();
		
		Set<KiWiResource> components = new HashSet<KiWiResource> ();
		
		/* Update the content of the nested content items (components) */
		applyRecursive(doc.getRootElement(), context, components);
		
		/* Update the list of all nested content items of this "context" content item */
		List<ContentItem> nestedContentItems = new LinkedList<ContentItem> ();
		for (KiWiResource component: components) {
			nestedContentItems.add(component.getContentItem());
		}
		context.getContentItem().setNestedContentItems(nestedContentItems);
		
		return content;
	}

}
