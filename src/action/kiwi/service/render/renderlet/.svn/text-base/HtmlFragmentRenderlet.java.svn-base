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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;

import kiwi.model.Constants;
import kiwi.model.kbase.KiWiResource;
import kiwi.util.KiWiXomUtils;
import kiwi.util.KiWiXomUtils.NodePos;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParentNode;
import nu.xom.Text;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

@Stateless
@Name("kiwi.service.render.renderlet.HtmlFragmentRenderlet")
@AutoCreate
public class HtmlFragmentRenderlet implements XOMRenderlet {
	
	@Logger
	Log log;

	@Override
	public Document apply(KiWiResource context, Document content) {
		
		Document ret = content;
		
		Map<Text, String> abspans = new HashMap<Text, String>();
		
		Set<String> open = new HashSet<String>();
		
		Set<String> opening = new HashSet<String>();
		Set<String> closing = new HashSet<String>();
		
		Iterator<NodePos> iter = new KiWiXomUtils.NodePosIterator(ret);
		while (iter.hasNext()) {
			NodePos np = iter.next();
			if (np.getNode() instanceof Element) {
				Element e = (Element)np.getNode();
				
				// log.info("element #0", e.getLocalName());
				
				if ("bookmarkstart".equals(e.getLocalName())) {
					String id = e.getAttributeValue("id");
					if (id != null) {
						opening.add(id);
					}
				}
				else if ("bookmarkend".equals(e.getLocalName())) {
					String id = e.getAttributeValue("id");
					if (id != null) {
						closing.add(id);
					}
				}
			}
			else if (np.getNode() instanceof Text) {
	
				// we do opening/closing after actually approaching the text node, 
				// because of empty bookmarks, which may appear in any order...
				open.addAll(opening);
				open.removeAll(closing);
				
				opening.clear();
				closing.clear();
				
				Text text = (Text) np.getNode();
				if (!open.isEmpty()) {
					StringBuilder sb = new StringBuilder();
					for (String id : open) {
						if (sb.length() != 0) {
							sb.append(' ');
						}
						sb.append(id);
					}
					
					abspans.put(text, sb.toString());
				}
			}
		}
		
		for (Map.Entry<Text, String> entry : abspans.entrySet()) {
			Text text = entry.getKey();
			
			Element abspan = new Element("span", Constants.NS_XHTML);
			abspan.addAttribute(new Attribute("fragment_ids", entry.getValue()));
			ParentNode pn = text.getParent();
			
			pn.replaceChild(text, abspan);
			abspan.appendChild(text);
		}
		
		return ret;
	}
}
