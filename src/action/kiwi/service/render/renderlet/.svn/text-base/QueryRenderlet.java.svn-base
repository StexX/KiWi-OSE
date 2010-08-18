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

import kiwi.model.Constants;
import kiwi.model.kbase.KiWiResource;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import nu.xom.Text;
import nu.xom.XPathContext;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwi.service.render.renderlet.QueryRenderlet")
@AutoCreate
public class QueryRenderlet implements XOMRenderlet {

	@Logger
	private Log log;
	
	/**
	 * Locate div-elements containing kiwi:query attribute in the content and replace them by the
	 * appropriate tag library tag for running the query, depending on format. div-elements
	 * may take the following form:
	 * <pre>
	 * &lt;div kiwi:kind="query" 
	 *      kiwi:query="QUERY_STRING" 
	 *      kiwi:lang="QUERY_LANG" 
	 *      kiwi:format="RESULT_FORMAT"&gt;
	 * &lt;/div&gt;
	 * </pre>
	 * <ul>
	 * <li>QUERY_STRING is the query string in KIWI or SPARQL syntax to evaluate</li>
	 * <li>QUERY_LANG is one of the query languages supported by KiWi, i.e. "KIWI" or "SPARQL"</li>
	 * <li>RESULT_FORMAT is one of the formatting templates for results, currently "TABLE", "MAP", "CALENDAR"</li>
	 * </ul>
	 */
	@Override
	public Document apply(KiWiResource context, Document content) {
		log.info("running query renderlet...");
		
		XPathContext namespaces = new XPathContext();
		namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
		namespaces.addNamespace("html", Constants.NS_XHTML);
		
		Nodes queries = content.query("//html:div[@kiwi:kind='query']", namespaces);

		for(int i = 0; i<queries.size(); i++) {
			Element q_el = (Element)queries.get(i);
			
			String query  = q_el.getAttributeValue("query",Constants.NS_KIWI_HTML);
			String lang   = q_el.getAttributeValue("lang", Constants.NS_KIWI_HTML);
			String format = q_el.getAttributeValue("format", Constants.NS_KIWI_HTML);
			
			log.info("found query: #0",query);
			
			if("map".equalsIgnoreCase(format)) {
				Element qResult = new Element("queryResultMap",Constants.NS_JSF_KIWI);
				qResult.addAttribute(new Attribute("query",query));
				if(lang != null) {
					qResult.addAttribute(new Attribute("lang",lang));
				}
				q_el.removeChildren();
				q_el.appendChild(qResult);
			} else if("calendar".equalsIgnoreCase(format)) {
				// TODO
				q_el.removeChildren();
				q_el.appendChild(new Text("format "+format+" is currently not supported"));
			} else {
				// table
				Element qResult = new Element("queryResultTable",Constants.NS_JSF_KIWI);
				qResult.addAttribute(new Attribute("query",query));
				if(lang != null) {
					qResult.addAttribute(new Attribute("lang",lang));
				}
				q_el.removeChildren();
				q_el.appendChild(qResult);
			}
		}
		
		// TODO Auto-generated method stub
		return content;
	}

}
