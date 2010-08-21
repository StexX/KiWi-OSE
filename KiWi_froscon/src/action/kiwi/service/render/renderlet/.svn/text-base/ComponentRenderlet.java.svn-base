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

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import kiwi.api.content.ContentItemService;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiNode;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.XPathContext;

/**
 * @author Marek Schmidt
 *
 */
@Stateless
@Name("kiwi.service.render.renderlet.ComponentRenderlet")
@AutoCreate
public class ComponentRenderlet implements XOMRenderlet {
	
	@Logger
	private static Log log;
	
	@In
	private ContentItemService contentItemService;
	
	public void applyRecursive (KiWiResource context, Element root, Set<String> kiwiids) {
		XPathContext namespaces = new XPathContext();
		namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);

		// First, include the missing components in iterated includes....
		Nodes iteratedincludes = root.query("descendant::node()[@kiwi:iteratedinclude]", namespaces);
		for (int i = 0; i < iteratedincludes.size(); ++i) {
			Element e = (Element)iteratedincludes.get(i);
			String relKiwiId = e.getAttributeValue("iteratedinclude", Constants.NS_KIWI_HTML).trim();
			
			ContentItem relCi = contentItemService.getContentItemByKiwiId (relKiwiId);
			if (relCi == null) {
				log.info("applyRecursive, iteratedinclude CI #0 does not exist!", relKiwiId);
				continue;
			}
	
			// Get the list of resources that should be included (according to knowledge base)	
			Set<KiWiResource> toInclude = new HashSet<KiWiResource> ();
			try {
				for (KiWiNode node : context.listOutgoingNodes(relCi.getResource().getSeRQLID())) {
					if (node.isUriResource() || node.isAnonymousResource()) {
						toInclude.add ((KiWiResource) node);
					}
				}
			} catch (NamespaceResolvingException e1) {
				e1.printStackTrace();
			}
	
			// filter out those that are actually included
			Elements children = e.getChildElements();
			for (int j = 0; j < children.size(); ++j) {
				Element child = children.get(j);
                String includeKiwiId = child.getAttributeValue("component", Constants.NS_KIWI_HTML).trim();
                if (includeKiwiId != null) {
                    ContentItem includeCi = contentItemService.getContentItemByKiwiId(includeKiwiId);
                    if (includeCi != null) {
                        toInclude.remove (includeCi.getResource());
                    }
                }
            }	

			// include the rest
			for (KiWiResource include : toInclude) {
				Element eInclude = new Element("div", Constants.NS_XHTML);
				eInclude.addAttribute(new Attribute("kiwi:component", Constants.NS_KIWI_HTML, include.getKiwiIdentifier()));
				e.appendChild(eInclude);
			}
		}
		
		Nodes components = root.query("descendant::node()[@kiwi:component]", namespaces);
		for (int i = 0; i < components.size(); ++i) {
			Element e = (Element)components.get(i);
			String componentKiwiId = e.getAttributeValue("component", Constants.NS_KIWI_HTML).trim();
			
			if (kiwiids.contains(componentKiwiId)) {
				log.info("applyRecursive, component #0 contained in itself!", componentKiwiId);
				continue;
			}
			
			ContentItem componentCi = contentItemService.getContentItemByKiwiId(componentKiwiId);
			if (componentCi == null) {
				log.info("applyRecursive, component CI #0 does not exist!", componentKiwiId);
				continue;
			}
			
			kiwiids.add(componentKiwiId);
			
			TextContent componentTc = componentCi.getTextContent();
			if (componentTc == null) {
				log.info("textContent for #0 is null!", componentKiwiId);
				continue;
			}
			
			Document componentDoc = componentTc.copyXmlDocument();
			if (componentDoc == null) {
				log.info("textContent for #0 has empty Document!", componentKiwiId);
				continue;
			}
			
			log.info("adding content #0: #1", componentKiwiId, componentDoc.toXML());
			/* Add the document contents to our document, not including the root div element */
			Element componentRoot = componentDoc.getRootElement();
			while(componentRoot.getChildCount() > 0) {
				Node child = componentRoot.getChild(0);
				child.detach();
				e.appendChild(child);
			}
			
			applyRecursive (componentCi.getResource(), e, kiwiids);
		}
	}
	
	/* (non-Javadoc)
	 * @see kiwi.service.render.renderlet.Renderlet#apply(kiwi.model.kbase.KiWiResource, java.lang.Object)
	 */
	@Override
	public Document apply(KiWiResource context, Document content) {
		
		applyRecursive (context, content.getRootElement(), new HashSet<String> ());
		
		return content;
	}

}
