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
import kiwi.api.tagging.TaggingService;
import kiwi.model.tagging.Tag;
import java.util.List;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.kbase.KiWiResource;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.XPathContext;

/**
 * @author Marek Schmidt
 * 
 */
@Stateless
@Name("kiwi.service.render.renderlet.ComponentDisplayRenderlet")
@AutoCreate
public class ComponentDisplayRenderlet implements XOMRenderlet {

	@Logger
	private static Log log;
	
	@In
	private ConfigurationService configurationService;

	@In
	private ContentItemService contentItemService;

	@In
	private TaggingService taggingService;

	public void applyRecursive(KiWiResource context, Element root,
			Set<String> kiwiids) {
		XPathContext namespaces = new XPathContext();
		namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
	 {	
		Nodes components = root.query("descendant::node()[@kiwi:component]",
				namespaces);
		for (int i = 0; i < components.size(); i++) {
			Element e = (Element) components.get(i);
			String componentKiwiId = e.getAttributeValue("component",
					Constants.NS_KIWI_HTML).trim();

			ContentItem componentCi = contentItemService
					.getContentItemByKiwiId(componentKiwiId);

			
			TextContent componentTc = componentCi.getTextContent();
			if (componentTc == null) {
				log.info("textContent for #0 is null!", componentKiwiId);
				continue;
			}

			Document componentDoc = componentTc.copyXmlDocument();
			if (componentDoc == null) {
				log.info("textContent for #0 has empty Document!",
						componentKiwiId);
				continue;
			}

			List<Tag> taggings = taggingService.getTags(componentCi);
			Element taglabels = new Element("div",Constants.NS_XHTML);
			taglabels.appendChild("Tags: ");
			//String taglabels = "Tags: ";
			
			for (int j = 0; j < taggings.size(); j++) {
				Tag tag = taggings.get(j);
				Element tagLink = new Element("a",Constants.NS_XHTML);
				Attribute href = new Attribute("href",configurationService.getBaseUri()+"/search.seam?q=tag:"+tag.getLabel());				
				tagLink.addAttribute(href);
				tagLink.appendChild(tag.getLabel());
				taglabels.appendChild(tagLink);
				if (j < taggings.size()-1)
				{
				taglabels.appendChild(", ");
				}
			}
			
			e.insertChild((Node) new Element("br",Constants.NS_XHTML), 0);
			e.insertChild((Node) new Element("br",Constants.NS_XHTML), 0);
			e.insertChild(taglabels, 0);

			Element h3 = new Element("h3",Constants.NS_XHTML);
			Element a = new Element("a",Constants.NS_XHTML);
			Attribute href = new Attribute("href",configurationService.getBaseUri()+"/home.seam?kiwiid="+componentKiwiId);
			a.addAttribute(href);
			a.appendChild(componentCi.getTitle());
			h3.appendChild(a);
			e.insertChild(h3, 0);

		}}
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

		applyRecursive(context, content.getRootElement(), new HashSet<String>());
		return content;
	}

}
