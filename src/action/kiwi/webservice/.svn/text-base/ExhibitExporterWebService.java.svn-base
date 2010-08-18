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
 * Szaby Gruenwald
 * 
 */
package kiwi.webservice;

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
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.cache.CacheProvider;
import org.jboss.seam.log.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Exports the results of a search query for showing in an 
 * exhibit-like tool. About exhibit see
 * http://simile.mit.edu/wiki/Exhibit
 * @author Szaby Gruenwald
 *
 */

@Name("kiwi.webservice.ExhibitExporterWebService")
@Scope(ScopeType.STATELESS)
@Path("/widgets/exhibit")
public class ExhibitExporterWebService {
	
	@Logger
	private Log log;

	@In
	private SolrService solrService;
	
	@In
	private CacheProvider<Object> cacheProvider;
	
	private final static HashMap<String, String> keySubstitutions= new HashMap<String, String>();
	private final static HashMap<String, String> valueSubstitutions= new HashMap<String, String>();
	static {
		// valueSubstitutions.put("\"", "\\\"");
		keySubstitutions.put(" ", "-");
		keySubstitutions.put("rdf:type", "type");
		keySubstitutions.put("rdfs:label", "label");
	}

	/**
	 * example: http://localhost:8080/KiWi/seam/resource/services/ws/exhibit/
	 * 		query.json?q=type:waysknow:Expert&
	 * 			typemask=(waysknow:.*%7Csioc:.*)
	 * 
	 *  Makes a KiWi query for type:waysknow:Expert
	 *  The returned json objects will only have the types that matches 
	 *  the regular expression "(waysknow:.*|sioc:.*)"
	 * @param query
	 * @param limit
	 * @param typemask
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Path("/query.json")
	@Produces("application/json;UTF-8")
	public byte[] query(
			@QueryParam("q") @DefaultValue("type:kiwi:ContentItem") String query,
			@QueryParam("limit") @DefaultValue("100") int limit,
			@QueryParam("typemask") @DefaultValue(".*") String typemask
			) throws JSONException {
		List<String> keys;
		String cacheKey = query + "_" + typemask + "_limit" + String.valueOf(limit);
		cacheProvider.setDefaultRegion("exhibit.json");
		if(cacheProvider.get("keys")==null)
			keys = new LinkedList<String>();
		else 
			keys = (List<String>)cacheProvider.get("keys");
			
		String res = (String)cacheProvider.get(cacheKey);
		if(res!=null){
			log.info("query: cached query #0", cacheKey);
			try {
				return res.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// shouldn't ever happen, because Java promises to 
				// always supports UTF-8
				e.printStackTrace();
			}
		}
		
		JSONObject result = new JSONObject();
		long startTime = System.currentTimeMillis();
		List<Map<String,Object>> items = new LinkedList<Map<String,Object>>();
		String uri = "";
		log.info("query: for #0", query);
		KiWiSearchCriteria criteria = solrService.parseSearchString(query);
		criteria.setLimit(limit);
		KiWiSearchResults results = solrService.search(criteria);
		log.info("query: collecting data for #0 items", results.getResultCount());
		for(SearchResult searchRes:results.getResults()){
			Map<String,Object> r = new HashMap<String, Object>();
			List<String> types = new LinkedList<String>();
			for(KiWiTriple triple:searchRes.getItem().getResource().listOutgoing()){
				KiWiUriResource propRes = triple.getProperty();
				String prop = propRes.getNamespacePrefix() + ":" + propRes.getLabel();
				prop = keySubstitute(prop);
				KiWiNode propObject = triple.getObject();
				uri = ((KiWiUriResource)triple.getSubject()).getUri();
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

					if("type".equals(prop)){
						if(value!="kiwi:ContentItem"){
							if(extValue.matches(typemask)){
								types.add(valSubstitute(value));
								r.put(prop, valSubstitute(value));
							}
						}
					}else{
					
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
			String label = (String)r.get("label");
			
			// For multiple types Exhibit requires extra JSON objects with the same label or id.
			for(String type:types){
				Map<String,Object> typeEl = new HashMap<String, Object>();
				
				typeEl.put("label", label);
				typeEl.put("type", type);
				r.put("type", type);
				if(label!=null)
					items.add(typeEl);
			}
			if(label!=null)
				items.add(r);
		}
		result.put("items", items);
		res = result.toString();
		res = res.replace("},", "},\n");
		
		log.info("query: returning #0 after #1 ms.", res, System.currentTimeMillis()-startTime);
		keys.add(cacheKey);
		cacheProvider.put(cacheKey, res);
		cacheProvider.put("keys",keys);
		try {
			return res.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return res.getBytes();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Observer(create=true,value={
				KiWiEvents.TRIPLE_CREATED,
				KiWiEvents.TRIPLE_REMOVED,
				KiWiEvents.TRIPLESTORE_UPDATED})
	public void wipeExhibitCache(){
		cacheProvider.setDefaultRegion("exhibit.json");
		List<String> keys = (List<String>)cacheProvider.get("keys");
		if(keys!=null){
			log.debug("wipeExhibitCache: wiping exhibit cache");
			for(String key:keys){
				cacheProvider.remove(key);
			}
			cacheProvider.remove("keys");
		}
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
