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

import java.util.Collection;

import javax.ejb.Stateless;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiNode;
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
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwi.service.render.renderlet.AnnotationLinksRenderlet")
@AutoCreate
public class AnnotationLinksRenderlet implements XOMRenderlet {

	
	@In
	private ConfigurationService configurationService;
	
	@In
	private ContentItemService contentItemService;

	@Logger
	private static Log log;

	/* (non-Javadoc)
	 * @see kiwi.backend.render.renderlet.Renderlet#apply(kiwi.model.kbase.KiWiResource, java.lang.Object)
	 */
	public Document apply(KiWiResource context, Document content) {

		// get all outgoing triples for the ContentItem.
		Collection<KiWiTriple> contentItemTriples=context.listOutgoing();

		XPathContext namespaces = new XPathContext();
		namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
		namespaces.addNamespace("html", Constants.NS_XHTML);
		
		Nodes links = content.query("//html:a[@kiwi:kind='intlink']", namespaces);
		

		for(int i=0; i<links.size(); i++) {
			Element link = (Element)links.get(i);

			// find target kiwiid or title from kiwi:target attribute
			String ci_kiwiid = link.getAttributeValue("target", Constants.NS_KIWI_HTML);
			
			// if title found, look for content item with that title and try to reference it as href attribute of the link
			// if no content item found, use the title for linking; ContentItemAction creates a new empty content item
			if(ci_kiwiid != null) {
				ContentItem ci = contentItemService.getContentItemByKiwiId(ci_kiwiid);
				
				if(ci != null && ci.getResource() != null) {
					insertLinkAnnotation(link,ci,contentItemTriples);
				}
				
			}
			
		}
		
		
		return content;
	}
	
	/**
	 * replace link element with the structure 
	 * <span><link/>&nbsp;<annotation links/></span>
	 * where the annotation links can consist of add and remove annotation.
	 * @param link
	 * @param ci
	 */
	private void insertLinkAnnotation(Element link, ContentItem ci, Collection<KiWiTriple> contentItemTriples) {
		Element span = new Element("span");
		ParentNode coveringNode = link.getParent();
		coveringNode.replaceChild(link, span);
		span.addAttribute(new Attribute("class","kiwiLink"));
		span.addAttribute(new Attribute("style","white-space:nowrap;"));
		span.appendChild(link);
		Element annotationLinks = new Element("span");
		annotationLinks.addAttribute(new Attribute("class","annotationButtons"));
		// setting up the annotationLinks
		
		// addAnnotation
		Element addAnnotation = new Element("a");
		addAnnotation.addAttribute(new Attribute("href","javascript:setSelectedLinkToAnnotateAndShowAddLink('"+ ci.getId() + "')"));
		Element addAnnotationImage=new Element("img");
		addAnnotationImage.addAttribute(new Attribute("src","img/type_add.png"));
		addAnnotationImage.addAttribute(new Attribute("style","padding:2px;border:0;"));
		addAnnotation.appendChild(addAnnotationImage);
		annotationLinks.appendChild(addAnnotation);
		
		// removeAnnotation
		// show only if there are outgoing triples to the linked resource.
		if(hasOutgoingTriples(ci.getResource(),contentItemTriples)){
			Element removeAnnotation = new Element("a");
			removeAnnotation.addAttribute(new Attribute("href","javascript:setSelectedLinkToAnnotateAndShowRemoveLink('"+ ci.getId() + "')"));
			Element removeAnnotationImage=new Element("img");
			removeAnnotationImage.addAttribute(new Attribute("src","img/type_delete.png"));
			removeAnnotationImage.addAttribute(new Attribute("style","padding:2px;border:0;"));
			removeAnnotation.appendChild(removeAnnotationImage);
			annotationLinks.appendChild(removeAnnotation);
		}
		
		span.appendChild(annotationLinks);
		
	}
	
	/**
	 * Tells if there is an outgoing triple from the currentContentItem to the
	 * link target (object).
	 */ 
	private boolean hasOutgoingTriples(KiWiNode object,Collection<KiWiTriple> contentItemTriples){
		if(contentItemTriples==null){
			log.debug("hasOutgoingTriples: contentItemTriples was null!");
			return false;
		}
		
		
		for(KiWiTriple triple : contentItemTriples) {
			try {
				if(triple.getObject().equals(object) && !triple.isInferred())
					return true;
			} catch (Exception e) {
				log.debug("hasOutgoingTriples: possibly not all annotation links are shown.");
			}
		}
		
		return false;
	}

}
