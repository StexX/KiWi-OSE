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

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
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
 * A renderlet that renders images inside the content. It searches for img tags that have the 
 * attribute kiwi:kind="intlink" and rewrites them to Seam s:graphicImage elements that are
 * rendered by JSF.
 * <p>
 * A minimal correct internal image tag in the KiWi notation thus looks as follows:
 * <p>
 * <code>
 * &lt;img kiwi:kind="intlink" kiwi:target="http://www.example.com/multimedia/MyImage" /&gt;
 * </code>
 * In addition, width and height attributes are respected and trigger a server-side scaling of the
 * image to reduce transferred data size.
 * 
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwi.service.render.renderlet.ImageLinkRenderlet")
@AutoCreate
public class ImageLinkRenderlet implements XOMRenderlet {

	@In
	private ConfigurationService configurationService;
	
	@In
	private ContentItemService contentItemService;

	@Logger
	private static Log log;
	
	@Override
	public Document apply(KiWiResource context, Document content) {
		XPathContext namespaces = new XPathContext();
		namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
		namespaces.addNamespace("html", Constants.NS_XHTML);
		
		Nodes links = content.query("//html:img[@kiwi:kind='intlink']", namespaces);

		log.debug("apply: processing #0 imagelinks", links.size());
		for(int i=0; i<links.size(); i++) {
			Element link = (Element)links.get(i);
			
			// find target kiwiid or title from kiwi:target attribute
			String ci_kiwiid = link.getAttributeValue("target", Constants.NS_KIWI_HTML).trim();

			if(ci_kiwiid != null) {
				ContentItem ci = contentItemService.getContentItemByKiwiId(ci_kiwiid);
				
				
				if(ci != null) {
					// convert img element to seam graphicImage
					link.setNamespaceURI(Constants.NS_JSF_SEAM);
					link.setLocalName("graphicImage");
					
					Attribute att_value = new Attribute("value","#{kiwi.ui.mediaAction.getMediaContent("+ci.getId()+").data}");
					Attribute att_name = new Attribute("filename","#{kiwi.ui.mediaAction.getMediaContent("+ci.getId()+").fileName}");
					
					link.addAttribute(att_value);
					link.addAttribute(att_name);
					
					// check whether height and/or width are present and add appropriate imageTransforms
					String img_height = link.getAttributeValue("height");
					String img_width  = link.getAttributeValue("width");
					if( (img_height != null  && img_height.matches("[0-9]+")) || 
						(img_width != null   && img_width.matches("[0-9]+"))     ) {
						Element trans = new Element("transformImageSize", Constants.NS_JSF_SEAM);
						
						boolean ratio = false;
						if(img_height != null && img_height.matches("[0-9]+")) {
							Attribute att_height = new Attribute("height",img_height);
							trans.addAttribute(att_height);
							ratio = !ratio;
						}
						
						if(img_width != null && img_width.matches("[0-9]+")) {
							Attribute att_width = new Attribute("width",img_width);
							trans.addAttribute(att_width);	
							ratio = !ratio;
						}
						
						if(ratio) {
							Attribute att_ratio = new Attribute("maintainRatio","true");
							trans.addAttribute(att_ratio);
						}
						link.appendChild(trans);
					}
					
					log.info("rendering internal image link: #0", link.toXML());
				} else {
					log.error("error: link to non-existant internal image #0",ci_kiwiid);
				}
				
			}
		}		
		
		return content;
	}

	
	
}
