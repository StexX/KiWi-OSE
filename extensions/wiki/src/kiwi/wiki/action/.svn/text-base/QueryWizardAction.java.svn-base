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
package kiwi.wiki.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import kiwi.action.search.KiWiSearchEngine;
import kiwi.action.search.SearchAction;
import kiwi.api.render.RenderingService;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.log.Log;
import org.richfaces.component.html.HtmlTogglePanel;

/**
 * @author Szaby Gruenwald
 *
 */
@Path("/query")
@Name("queryWizardAction")
@Scope(ScopeType.SESSION)
//@Transactional
public class QueryWizardAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2745961466454668272L;

	@Logger
	private static Log log;
	
	@In
	private RenderingService renderingPipeline;

	private String format, query, lang;

	final String defaultFormat = "table";
	final String defaultLang = "kiwi";
	
	public void setQuery(String format, String query, String lang){
		log.info("format: #0, query: #1", format, query);
	}

	@In(value="kiwiSearchEngine",create=true)
	private KiWiSearchEngine searchEngine;
	
//	@In(create=true)
//	private QueryParametersBridge queryParametersBridge;
	
	@In(value="queryWizardAction.queryParameters", scope=ScopeType.APPLICATION, required=false)
	@Out(value="queryWizardAction.queryParameters", scope=ScopeType.APPLICATION, required=false)
	private Map<Long,String[]> queryParameters;
	
	public Map<Long,String[]> getQueryParameters(){
		if(queryParameters == null) {
			queryParameters = new HashMap<Long,String[]>();
		}
		return queryParameters;
	}
	
	public void setMyQueryParameters(){
		if(searchEngine != null)query = searchEngine.getSearchQuery();
		log.info("query found #0", query);
		query = "SELECT ? WHERE {" + query + "}";
		String[] res = new String[]{format, query, lang};
		getQueryParameters().put(getUserId(), res);
	}

	public Long getUserId(){
		return ((User)Component.getInstance("currentUser")).getId();
	}
    public List<String> getFormats(){
		List<String> res = new ArrayList<String>();
		res.addAll(Arrays.asList("table","map","timeline"));
		return res;
	}

	public String getFormat() {
		return format;
	}


	public void setFormat(String format) {
		if(format=="") {
			this.format = defaultFormat;
		} else
			this.format = format;
		setMyQueryParameters();
	}


	public String getQuery() {
		log.info("getQuery: #0", query);
		return query;
	}

	public void setQuery(String query) {
		log.info("setQuery: #0", query);
		SearchAction sa = (SearchAction)Component.getInstance("searchAction");
		this.query = query.replaceAll("^.*?\\{(.*?)\\}.*?$", "$1");  
		sa.setSearchQuery(this.query);
		setMyQueryParameters();
	}


	public String getLang() {
		return lang;
	}

	public String getPreviewToggleState(){
		return ((HtmlTogglePanel)javax.faces.context.FacesContext.getCurrentInstance().getViewRoot().findComponent("querywizardForm:queryWizardPreviewToggle")).getValue().toString();
	}

	public void setLang(String lang) {
		if(lang=="") {
			this.lang = defaultLang;
		} else
			this.lang = lang;
		setMyQueryParameters();
	}
	
	@GET
	@Path("/renderpreview/{userId}")
	@Produces("text/xml; charset=utf-8")
	public byte[] renderPreview(@PathParam("userId") long userId) {
		if(!getUserId().equals(userId))return "<span/>".getBytes();
		
		ContentItem currentContentItem = (ContentItem) Component.getInstance("currentContentItem");
//		String test = "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\"http://www.kiwi-project.eu/kiwi/html/\" kiwi:format=\"table\" kiwi:kind=\"query\" kiwi:lang=\"kiwi\" kiwi:query=\"SELECT ? WHERE {type:kiwi:Image}\">The stored QUERY</div>";
//		String previewHtml = getPreviewHtml();
		String previewHtml = getPreviewHtml(userId);
		if(previewHtml==null || previewHtml=="")
			return "<span/>".getBytes();
		
		String res = renderingPipeline.renderHTML(previewHtml, currentContentItem);
//		String res = renderingPipeline.renderHTML(test, currentContentItem);
		log.info("cid: #0, rendering html: #1", Conversation.instance().getId(), res);
		return res.getBytes();
	}

	/**
	 * 
	 * @return a 3-tuple with the query parameters for the currentUser
	 */
	public String[] getMyQueryParameters(){
		return getQueryParameters().get(getUserId());
	}
	
	public String getPreviewHtml(Long userId){
		String[] myQueryParameters = getQueryParameters().get(userId);
		if(myQueryParameters==null)return "";
		return buildQueryWikiTag(myQueryParameters[0], myQueryParameters[1], myQueryParameters[2]);
	}
	private String buildQueryWikiTag(String format, String query, String lang){ 
		String res = "";
		res+="<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\"http://www.kiwi-project.eu/kiwi/html/\"";
		res+=" kiwi:kind=\"query\"";
		res+=" kiwi:query=\"" + query + "\""; 
		res+=" kiwi:lang= \"" + lang  + "\"";
		res+=" kiwi:format= \"" + format  + "\"";
		res+=" >The stored QUERY</div>";
		return res;
	}
	
	@Create
	public void init(){
		if(format==null || format == "")format="table";
	}
	
	public void submit(){
		
	}

	public String renderedSearchPageParts(){
		return "searchField,searchresults,nrOfResults,articlemeta,resultNavigation";//,search-keyword";
	}
	
	public KiWiSearchEngine getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(KiWiSearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}

}
