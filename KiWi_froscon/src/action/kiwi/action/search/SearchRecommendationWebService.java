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
package kiwi.action.search;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kiwi.api.event.KiWiEvents;
import kiwi.api.recommendation.RecommendationService;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.PersonalizedSearchService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.widgets.recommendations.RecommendationJSON;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Endpoint for search and recommendation widgets
 * examples are
 * http://localhost:8080/KiWi/seam/resource/services/widgets/search/recommend/query.json?q=StartPage&jsonpCallback=cb123
 * @author Fred Durao
 *
 */
@Name("kiwi.webservice.SearchRecommendationWebService")
@Scope(ScopeType.STATELESS)
// "/widgets/*" is necessary in order to have JSONP
@Path("/widgets/search/recommend")
public class SearchRecommendationWebService {

	@Logger
	private static Log log;
	
	/**
	 * initiate the personalized search
	 */
	@In(create = true)
	PersonalizedSearchService personalizedSearchService;
	
	@In(create = true)
	RecommendationService recommendationService;	

	/**
	 * input current user; might affect the loading of the content item.
	 */
	@In(create = true)
	private User currentUser;

	private final static HashMap<String, String> keySubstitutions= new HashMap<String, String>();
	private final static HashMap<String, String> valueSubstitutions= new HashMap<String, String>();

	private static final String FORMAT_INFO = "query.json has following parameters: " +
			"q is the query String;\n\n" +
			
			"personal tells if it's a personal search, default is false;\n\n" +
			
			"pageSize tells how many results should be on a page, default is 10;\n\n" +
			
			"page The result page to start with, default is 1;\n\n" +
			
			"typemask is a regExp tofilter the content type list in the result list. Remark: " +
			"in KiWi each ContentItem has a set of rdf:types. Some of them are explicit and some" +
			"are inherited. typemask gives you the possibility to narrow down the listed types to the" +
			"set you really need. Default is the regexp '.*' which means no filtering. An " +
			"example could be '(foaf:.*%7Csioc:.*)' which results in listing foaf and sioc types " +
			"of the results only.\n\n" +
			
			"qLang is the query language. It can be 'kwql' or 'nativekiwi', default is kwql. ";

	static {
		// valueSubstitutions.put("\"", "\\\"");
		keySubstitutions.put(" ", "-");
		keySubstitutions.put("rdf:type", "type");
		keySubstitutions.put("rdfs:label", "label");
	}
	
	/**
	 * Execute the search query
	 *   e.g. http://localhost:8080/KiWi/seam/resource/services/widgets/search/query.json?q=StartPage&jsonpCallback=blah12345
	 *  	     typemask=(foaf:.*%7Csioc:.*) 
	 *   %7C is the character |
	 * TODO: MediaContents
	 * @param query
	 * @param personalSearch 
	 * @param pageSize 
	 * @param typemask is a regexp for specifying a filter for type
	 */
	@GET
	@Path("/query.json")
	@Produces("application/json;UTF-8")
	public byte[] runSearch(
			@QueryParam("q") String query, 
			@QueryParam("personal") @DefaultValue("false")boolean personalSearch, 
			@QueryParam("pageSize") @DefaultValue("10") int pageSize,
			@QueryParam("page") @DefaultValue("1") int page,
			@QueryParam("typemask") @DefaultValue(".*") String typemask,
			@QueryParam("qLang") @DefaultValue("kwql") String queryLanguage) {
		KiWiSearchResults searchResults;
		
		List<ContentItem> personalizedRecommendations = null;		
		log.info("runSearch.s query: #0", query);
				
		if(query=="" || query == null)
			return getErrorResult(FORMAT_INFO, null);
		
		SearchEngine searchEngine;
		log.info("Query Lang #0, query string #1", queryLanguage, query);
		if("kwql".equals(queryLanguage)){
			log.info("searchengine set to kwql");
			searchEngine = (KWQLSearchEngine)Component.getInstance("kwqlSearchEngine");
		} else if("kiwinative".equals(queryLanguage)) {
			searchEngine = (KiWiSearchEngine)Component.getInstance("kiwiSearchEngine");

		} else {
			searchEngine = (KiWiSearchEngine)Component.getInstance("kiwiSearchEngine");
		}
		log.info("searchengine: #0", searchEngine.toString());
		searchEngine.setPage(page);
		searchEngine.setPageSize(pageSize);
		searchEngine.setSearchQuery(query);
		searchEngine.runSearch();
		searchResults = searchEngine.getSearchResults();
		
		if(personalSearch && (currentUser.getId()!=1L)){
			searchResults = personalizedSearchService.runPersonilazedSearch(searchResults);
		}
		
		if(currentUser!=null && Conversation.instance().getViewId()!=null && Conversation.instance().getViewId().contains("search") &&
				query!="")
			Events.instance().raiseEvent(KiWiEvents.ACTIVITY_SEARCH, currentUser, query);
		
		
		personalizedRecommendations = recommendationService.getRecommendationsByTag(query, currentUser);
		
		return kiwiSearchResultsAndRecommendations2ByteArray(searchResults, personalizedRecommendations, typemask);
	}


	/**
	 * Making a JSON object out of a KiWiSearchResults and Recommendations object
	 * @param searchResults
	 * @param typeMask 
	 * @return
	 */
	private byte[] kiwiSearchResultsAndRecommendations2ByteArray(KiWiSearchResults searchResults, List<ContentItem> personalizedRecommendations, String typeMask) {
		byte[] res;
		JSONObject searchResultsJSON;
		
		try {
			searchResultsJSON = searchResultsAndRecommendationsToJSON(searchResults,personalizedRecommendations, typeMask);
		} catch (JSONException e) {
			e.printStackTrace();
			return getErrorResult("JSONException "+e.getMessage(), e);
		}
		try {
			res = searchResultsJSON.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return getErrorResult("UnsupportedEncodingException"+e.getMessage(), e);
		}
		return res;
	}

	private byte[] getErrorResult(String errorMsg, Exception e) {
		JSONObject errJS = new JSONObject();
		try {
			Map<String, Object> errObj = new HashMap<String, Object>();
			errObj.put("message", errorMsg);
			errJS.put("error", errObj);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return errJS.toString().getBytes();
	}



	/**
	 * @param searchResults
	 * @param personalizedRecommendations
	 * @param typemask
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	private JSONObject searchResultsAndRecommendationsToJSON(KiWiSearchResults searchResults, List<ContentItem> personalizedRecommendations, String typemask) throws JSONException {
		JSONObject result = new JSONObject();
		List<Object> items = new LinkedList<Object>();
		
		for(SearchResult sRes:searchResults.getResults()){
			Map<String,Object> r = new HashMap<String, Object>();
			List<String> types = new LinkedList<String>();
			
			String uri = "";
			for(KiWiTriple triple:sRes.getItem().getResource().listOutgoing()){
				KiWiUriResource propRes = triple.getProperty();
				String prop = propRes.getNamespacePrefix() + ":" + propRes.getLabel();
				prop = keySubstitute(prop);
				KiWiNode propObject = triple.getObject();
				uri  = ((KiWiUriResource)triple.getSubject()).getUri();
				String value;
				if(propObject.isLiteral()){
					// Property is a value
					value = ((KiWiLiteral)propObject).getContent();
					log.trace("query: result property: #0, literal value #1", prop, value);
					if("kiwi:hasTextContent".equals(prop))
						r.put(prop, valSubstitute(value));
					r.put(prop, valSubstitute(value));
				}else if(propObject.isUriResource()){
					// Property is an object
					KiWiResource objectUriRes = (KiWiResource)propObject;
					String extValue = objectUriRes.getNamespacePrefix() + ":" + objectUriRes.getLabel();
					value = objectUriRes.getLabel();
					log.trace("query: result property: #0, urires value #1", prop, value);

					if("type".equals(prop) || "rdf:type".equals(prop)){
						if(value!="kiwi:ContentItem"){
							if(extValue.matches(typemask)){
								types.add(valSubstitute(value));
								r.put(prop, valSubstitute(value));
							}
						}
					} else {
					
						LinkedList<Object> list;
						if(r.get(prop)==null){
							list = new LinkedList<Object>();
						} else {
							list = (LinkedList<Object>)r.get(prop);
						}
						list.add(valSubstitute(value));
						// JSONObject generates JSON only if the whole list is added again and again.
						r.put(prop, list);
					}
				}
			}
			r.put("resourceUri", uri);
			r.put("types", types);
			String label = (String)r.get("label");
			
			if(label!=null)
				items.add(r);

//			Map<String, Object> sResMap = new HashMap<String, Object>();
//			sResMap.put("title", sRes.getItem().getTitle());
//			items.add(sResMap);
		}
		
		
		
		//creating personalized JSON recommendations 
		List<RecommendationJSON> recommendations = new LinkedList<RecommendationJSON>();
		for(ContentItem ci: personalizedRecommendations) {
			recommendations.add(new RecommendationJSON(ci));
		}

		result.put("nrOfResults", searchResults.getResultCount());
		result.put("results", items);
		result.put("personalizedRecommendations", recommendations);
		
		return result;
	}

	private String valSubstitute(String value) {
		return substitute(value, valueSubstitutions);
	}

	private String keySubstitute(String value) {
		return substitute(value, keySubstitutions);
	}

	private String substitute(String value,
			HashMap<String, String> substitutions) {
		String res = value;
		for(String key:substitutions.keySet()){
			res=res.replace(key, substitutions.get(key));
		}
		return res;
	}

}
