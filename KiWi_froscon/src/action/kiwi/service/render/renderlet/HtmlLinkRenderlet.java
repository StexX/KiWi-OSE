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
import kiwi.model.kbase.KiWiUriResource;
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
 * A renderlet that renders links to internal pages. It searches for all HTML "a" elements with
 * attribute kiwi:kind="intlink" and rewrites their href in such a way that it points to /home.seam
 * with the link target (from the link's "target" attribute) passed as kiwiid or uri parameter so 
 * that the correct content item is displayed in the currently active KiWi application when the link 
 * is clicked. 
 * <p>
 * Links to non-existant content items create a new page. The link is rendered with the class="new"
 * attribute to allow different styling of links to non-existant items like in other wikis. 
 * <p>
 * A minimal correct internal link in the KiWi representation thus looks as follows:
 * <p>
 * <code>
 * &lt;a kiwi:kind="intlink" kiwi:target="http://localhost:8080/KiWi/content/FrontPage"&gt;FrontPage&lt;/a&gt;
 * </code>
 * <p>
 * It will be transformed into the following correct XHTML link:
 * <p>
 * <code>
 * &lt;a href="http://localhost:8080/KiWi/home.seam?uri=http://localhost:8080/KiWi/content/FrontPage"&gt;FrontPage&lt;/a&gt;
 * </code>
 * 
 * @author sschaffe
 *
 */
@Stateless
@Name("kiwi.service.render.renderlet.HtmlLinkRenderlet")
@AutoCreate
public class HtmlLinkRenderlet implements XOMRenderlet {

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
		// TODO: need to rewrite targetId -> target attribute so as to get links that are nice for
		// users while still staying reasonably consistent
		
		XPathContext namespaces = new XPathContext();
		namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
		namespaces.addNamespace("html", Constants.NS_XHTML);
		
		Nodes links = content.query("//html:a[@kiwi:kind='intlink']", namespaces);
		

		for(int i=0; i<links.size(); i++) {
			Element link = (Element)links.get(i);

			// find target kiwiid or title from kiwi:target attribute
			String ci_kiwiid = link.getAttributeValue("target", Constants.NS_KIWI_HTML).trim();
			
			// if title found, look for content item with that title and try to reference it as href attribute of the link
			// if no content item found, use the title for linking; ContentItemAction creates a new empty content item
			if(ci_kiwiid != null) {
				ContentItem ci = contentItemService.getContentItemByKiwiId(ci_kiwiid);
				
				
				if(ci != null && ci.getResource() != null) {
					if(ci.getResource().isUriResource()) {
						String uri = ((KiWiUriResource)ci.getResource()).getUri();
						link.addAttribute(new Attribute("href", configurationService.getBaseUri()+"/home.seam?uri="+uri));
					} else {
						String kiwiid = ci.getResource().getKiwiIdentifier();
						link.addAttribute(new Attribute("href", configurationService.getBaseUri()+"/home.seam?kiwiid="+kiwiid));
					}
					
					// check if other content item is still empty and mark link as new if so
					if(ci.getTextContent() == null) {
						link.addAttribute(new Attribute("class", "new"));						
					}
					
				} else {
					link.addAttribute(new Attribute("href", configurationService.getBaseUri()+"/home.seam?title="+ci_kiwiid));
					link.addAttribute(new Attribute("class", "new"));
				}
				
			}
			
		}
		
		
		return content;
	}

}
