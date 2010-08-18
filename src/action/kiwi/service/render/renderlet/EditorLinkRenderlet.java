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

import kiwi.api.content.ContentItemService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParentNode;
import nu.xom.Text;
import nu.xom.XPathContext;

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
@Name("kiwi.service.render.renderlet.EditorLinkRenderlet")
@AutoCreate
public class EditorLinkRenderlet implements XOMRenderlet {
	
	@In
	private ContentItemService contentItemService;
	
	@Logger
	private static Log log;
	
	/* (non-Javadoc)
	 * @see kiwi.backend.render.renderlet.Renderlet#apply(kiwi.model.kbase.KiWiResource, java.lang.Object)
	 */
	public Document apply(KiWiResource context, Document content) {
		XPathContext namespaces = new XPathContext();
		namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
		namespaces.addNamespace("html", Constants.NS_XHTML);
		
		Nodes bookmark_starts = content.query("//kiwi:bookmarkstart", namespaces);
		// track open bookmarks, display end bookmarks only for those that were actually opened
		Set<String> open_bookmarks = new HashSet<String> ();
		
		for(int i=0; i<bookmark_starts.size(); i++) {
			Element bookmark_start = (Element)bookmark_starts.get(i);
			String bookmark_id  = bookmark_start.getAttributeValue("id");
			
			log.debug("bookmark_uri: #0", bookmark_id);
	    
	    	String wf_bookmark = "[[BookmarkStart:" + bookmark_id + "]]";
			ParentNode parent = bookmark_start.getParent();
			parent.replaceChild(bookmark_start, new Text(wf_bookmark));
				
			open_bookmarks.add(bookmark_id);
		}
		
		Nodes bookmark_ends = content.query("//kiwi:bookmarkend", namespaces);
		
		for(int i=0; i<bookmark_ends.size(); i++) {
			Element bookmark_end = (Element)bookmark_ends.get(i);
			String bookmark_id = bookmark_end.getAttributeValue("id");
			
			// Only add ending mark if the opening mark was ever created
			if (open_bookmarks.contains(bookmark_id)) {
				String wf_bookmark = "[[BookmarkEnd:" + bookmark_id + "]]";
				ParentNode parent = bookmark_end.getParent();
				parent.replaceChild(bookmark_end, new Text(wf_bookmark));
			}
		}
		
		Nodes links = content.query(
				"//html:a[@kiwi:kind='intlink'] | " +
				"//html:a[@kiwi:kind='extlink'] | " +
				"//html:a[@kiwi:kind='imagelink'] | " +
				"//html:a[@kiwi:kind='videolink']", namespaces);
		
		for(int i=0; i < links.size(); i++) {
			Element link = (Element)links.get(i);
			
			// get the kind of the link (intlink, extlink, imagelink or videolink)
			String ci_kind = link.getAttributeValue("kind", Constants.NS_KIWI_HTML);

			// find target kiwiid from kiwi:target attribute
			String ci_target = link.getAttributeValue("target", Constants.NS_KIWI_HTML);
			
			ContentItem ci = contentItemService.getContentItemByKiwiId(ci_target);
			if(ci != null) {
				ci_target = ci.getTitle();
			}
			
			// In general, link is replaced by two text nodes and the subtree of 
			// the original link is inserted between them.
			StringBuilder prefix = new StringBuilder();
			String suffix = null;
			
			if (ci_kind.equals("intlink")) {
				// find semantic relations (Semantic MediaWiki style link)
				String ci_rel = link.getAttributeValue("rel", Constants.NS_KIWI_HTML);	
				if (ci_rel != null) {
					prefix.append("[[ ");
					prefix.append(ci_rel);
					prefix.append(" :: ");
					prefix.append(ci_target);
					prefix.append(" | ");
				}
				else {
					prefix.append("[[ ");
					prefix.append(ci_target);
					prefix.append(" | ");
				}
				
				suffix = " ]]";
			}
			else if(ci_kind.equals("extlink")) {
				prefix.append("[ ");
				prefix.append(ci_target);
				prefix.append(" | ");
				suffix = " ]";
			}
			else if(ci_kind.equals("imagelink")) {
				prefix.append("[[Image: ");
				prefix.append(ci_target);
				prefix.append(" | ");
				
				String ci_html_kind = link.getAttributeValue("kind");
				boolean isthumb = ci_html_kind != null && ci_html_kind.equals("thumb");
				if (isthumb) {
					String ic_align = link.getAttributeValue("align");
					String ic_width = link.getAttributeValue("width");
					
					prefix.append(ic_align);
					prefix.append(" | thumb | ");
					prefix.append(ic_width);
					prefix.append("px | ");
				}
				
				suffix = " ]]";
			}
			else if(ci_kind.equals("videolink")) {
				prefix.append("[[Video: ");
				prefix.append(ci_target);
				prefix.append(" | ");
				
				String ic_align = link.getAttributeValue("align");
				String ic_height = link.getAttributeValue("height");
				String ic_width = link.getAttributeValue("width");		
				
				if (ic_align != null) {
					prefix.append(ic_align);
					prefix.append(" | ");
					prefix.append(ic_width);
					prefix.append("px | ");
					prefix.append(ic_height);
					prefix.append("px | ");
				}
				
				suffix = " ]]";
			}
			
			ParentNode parent = link.getParent();
			int index = parent.indexOf(link);
			
			parent.replaceChild(link, new Text(prefix.toString()));
			while(link.getChildCount() > 0) {
				Node child = link.getChild(0);
				child.detach();
				parent.insertChild(child, ++index);
			}
			parent.insertChild(new Text(suffix), ++index);
		}
				
		log.debug("EditorLinkRenderlet output: #0", content.toXML());
		
		return content;
	}
}
