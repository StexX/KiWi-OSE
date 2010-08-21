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

import kiwi.api.content.ContentItemService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.ParentNode;
import nu.xom.XPathContext;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * Initial implementation of the RDFa renderlet. The renderlet should replace
 * the content of RDFa annotations based on actual values of RDF properties.
 * 
 * This initial implementation supports only the "property" attributes, such as
 * <span property="foaf:name">John</span> and only literals.
 * 
 * Instead of a CURIE in a property attribute, a kiwiId of
 * the property is stored instead.
 * 
 * TODO: pretty much everything
 * 
 * @author Marek Schmidt
 * 
 */
@Stateless
@Name("kiwi.service.render.renderlet.RdfaRenderlet")
@AutoCreate
public class RdfaRenderlet implements XOMRenderlet {

	@In
	TripleStore tripleStore;
	
	@In
	ContentItemService contentItemService;

	@Logger
	Log log;
	
	/**
	 * Get the current "about" context. The nearest about attribute on the way
	 * up the tree.
	 * 
	 * @param e
	 * @return
	 */
	private KiWiResource getContext(KiWiResource rootContext, Element e) {
		ParentNode p = e.getParent();
		while (p != null) {
			if (p instanceof Element) {
				e = (Element) p;
				String about = e.getAttributeValue("about");
				if (about != null) {
					ContentItem ci = contentItemService
							.getContentItemByKiwiId(about);
					if (ci == null) {
						log.info("getContext about attribute references non-existing CI #0", about);
					} else {
						return ci.getResource();
					}
				}
			}

			p = p.getParent();
		}

		return rootContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kiwi.service.render.renderlet.Renderlet#apply(kiwi.model.kbase.KiWiResource
	 * , java.lang.Object)
	 */
	@Override
	public Document apply(KiWiResource context, Document content) {
		content = updateNestedItems(context, content);
		content = updateProperties(context, content);
		content = updateLinkRelations(context, content);
		return content;
	}
	
	/**
	 * Creates RDFa `about' attributes from the internal kiwi:component nested content items.
	 */
	private Document updateNestedItems (KiWiResource context, Document content) {
		
		XPathContext namespaces = new XPathContext();
        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
        namespaces.addNamespace("html", Constants.NS_XHTML);
	
		Nodes nodes;
		
		nodes = content.query("//html:div[@kiwi:component]", namespaces);
		for (int i = 0; i < nodes.size(); ++i) {
			Element node = (Element)nodes.get(i);
			String kiwiid = node.getAttributeValue("component", Constants.NS_KIWI_HTML);
			node.addAttribute(new Attribute("about", kiwiid));
		}
		
		return content;
	}
	
	private Document updateProperties(KiWiResource context, Document content) {
		Nodes nodes = content.query("//node()[@property]");
		for (int i = 0; i < nodes.size(); ++i) {
			Element e = (Element) nodes.get(i);

			/* Mercilessly replace with the RDF property, if exists */
			
			/* First get the actual property... seems like using the kiwiid 
			 * instead of property labels has its cons... */
			String propertyKiwiId = e.getAttributeValue("property");
			ContentItem propertyCi = contentItemService.getContentItemByKiwiId(propertyKiwiId);
			if (propertyCi == null) {
				log.info("Property #0 does not exist.", propertyKiwiId);
				continue;
			}
			String propertySeRQLID = propertyCi.getResource().getSeRQLID();
			String value = null;
			
			/* Get the "about" context of this property */
			KiWiResource eContext = getContext(context, e);

			/* Update if we have exactly one property value. */
			try {
				for (KiWiTriple triple : eContext.listOutgoing(propertySeRQLID)) {
					if (triple.getObject().isLiteral()) {
						if (value != null) {
							log.info("multiple property values for #0, ignoring value #1 ", propertySeRQLID, triple.getObject());
							value = null;
							break;
						}
						value = ((KiWiLiteral) triple.getObject()).getContent();
					}
				}
			} catch (NamespaceResolvingException e1) {
				e1.printStackTrace();
			}

			if (value != null) {
				// So remove all the subtree and replace it with a simple text
				// node
				e.removeChildren();
				e.appendChild(value);

				log.info("modifying text based on property #0 value: #1",
						propertySeRQLID, value);
			} else {
				// there are valid reasons for this happening
				log.info("no property value for #0 in #1", propertySeRQLID,
						eContext);
			}
		}

		return content;
	}
	
	private Document updateLinkRelations(KiWiResource context, Document content) {

		XPathContext namespaces = new XPathContext();
        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
        namespaces.addNamespace("html", Constants.NS_XHTML);
	
		Nodes nodes = content.query("//html:span[@rel]", namespaces);
		for (int i = 0; i < nodes.size(); ++i) {
			Element e = (Element) nodes.get(i);
			
			String relKiwiId = e.getAttributeValue("rel");
			String resourceKiwiId = e.getAttributeValue("resource");
			
			ContentItem relCi = contentItemService.getContentItemByKiwiId(relKiwiId);

			if (relCi == null) {
				log.info("ContentItem with kiwiid #0 does not exist.", relKiwiId);
				continue;
			}
			
			KiWiResource resource = null;
			if (resourceKiwiId != null) {
				ContentItem resourceCi = contentItemService.getContentItemByKiwiId(resourceKiwiId);
				if (resourceCi != null) {
					resource = resourceCi.getResource();
				}
			}
			
			/* Get the "about" context of this property */
			KiWiResource eContext = getContext(context, e);
			
			String relSeRQLID = relCi.getResource().getSeRQLID();
			
			KiWiResource value = null;
			
			/* Update if we have exactly one property value. */
			try {
				for (KiWiTriple triple : eContext.listOutgoing(relSeRQLID)) {
					if (triple.getObject().isAnonymousResource() || triple.getObject().isUriResource()) {
						if (value != null) {
							log.info("multiple property values for #0, ignoring value #1 ", relSeRQLID, triple.getObject());
							value = null;
							break;
						}
						value = (KiWiResource)triple.getObject();
					}
				}
			} catch (NamespaceResolvingException e1) {
				e1.printStackTrace();
			}
			
			/* update the reference and the text label, which should be the title of the related CI */
			// Do it even if the uri has changed, the title may have changed since the last time...
			// if (value != null && !value.equals(resource)) {
			if (value != null) {
				log.info("updateing object property #0 to #1", relSeRQLID, value.getKiwiIdentifier());
				e.addAttribute(new Attribute("resource", value.getKiwiIdentifier()));
			
				// So remove all the subtree and replace it with a simple text node
				e.removeChildren();
				
				if (value.getContentItem() != null && value.getContentItem().getTitle() != null) {
					e.appendChild(value.getContentItem().getTitle());
				}
			}
			else {
				log.info("not updating object property #0", relSeRQLID);
			}
		}
		
		return content;
	}
}
