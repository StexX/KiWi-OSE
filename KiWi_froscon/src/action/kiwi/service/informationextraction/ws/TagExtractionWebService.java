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
package kiwi.service.informationextraction.ws;

import java.io.IOException;

import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.informationextraction.InformationExtractionService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.SKOSConcept;
import kiwi.model.tagging.Tag;

import nu.xom.Builder;
import nu.xom.Document;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A RESTful web service primarily designed for the Sun Use Case (Tagging)
 * 
 * It exposes the tag suggesting funcionality of the information extraction service.
 * 
 *  TODO:
 *  	accepting/rejecting suggestions
 * 
 * @author Marek Schmidt
 *
 */
@Name("kiwi.informationextraction.ws.tagExtractionWebService")
@Scope(ScopeType.SESSION)
@Path("/ie/tagExtraction")
public class TagExtractionWebService {
	
	@In(create=true, value="kiwi.informationextraction.informationExtractionService")
	private InformationExtractionService informationExtractionService;
	
	@Logger
	private Log log;
	
	@In(create=true)
	private ContentItemService contentItemService;
	
	@In(create=true)
	private ConfigurationService configurationService;
	
	@In(create=true)
	private TripleStore tripleStore;
	
	@In(create=true)
	private KiWiEntityManager kiwiEntityManager;
	
	@In(create=true)
	private TaggingService taggingService;
	
	private Map<String, Suggestion> suggestionMap = new HashMap<String, Suggestion>();
	
	/* @Multipart("uri")
	 @Multipart(value="textcontent", type="text/html")
	 @Multipart("algorithm")
	 @Multipart("user")*/
	
	private String getPart (Map<String, List<InputPart>> dataMap, String name) throws IOException {
		List<InputPart> parts = dataMap.get(name);
		if (parts != null) {
			for (InputPart part : parts) {
				return part.getBodyAsString();
			}
		}
	
		return null;
	}
	
	private Collection<Suggestion> extractSuggestions (Map<String, List<InputPart>> dataMap) throws Exception {
		String uri = null;
		String algorithm = null;
		String user = null;
		String textContent = null;
		String jsonpCallback = null;
				
		uri = getPart(dataMap, "uri");
		user = getPart(dataMap, "user");
		algorithm = getPart(dataMap, "algorithm");
		textContent = getPart(dataMap, "textContent");
		jsonpCallback = getPart(dataMap, "jsonpCallback");
		
		TextContent tc = null;
		
		if (textContent != null) {
			Builder builder = new Builder();
			Document xom = builder.build(new StringReader(textContent));
		
			tc = new TextContent();
			tc.setXmlDocument(xom);
		}
		else if (uri != null) {
			ContentItem ci = contentItemService.getContentItemByUri(uri);
			if (ci != null) {
				tc = ci.getTextContent();
			}
			else {
				throw new Exception("No content item found with uri " + uri);
			}
		}
		
		if (tc == null) {
			throw new Exception("No content to process!");
		}
		
		Collection<Suggestion> suggestions = informationExtractionService.extractTags(null, tc, Locale.ENGLISH);
		
		return suggestions;
	}
	
	@POST
	@Path("/extractTags")
	@Consumes("multipart/form-data")
	@Produces("application/json")
	public String extractTags(MultipartFormDataInput formData) throws Exception {
		
		log.info("extractTags");
		
		Map<String, List<InputPart>> dataMap = formData.getFormDataMap();
		
		String jsonpCallback = getPart(dataMap, "jsonpCallback");
		
		Collection<Suggestion> suggestions = extractSuggestions(dataMap);
		
		JSONArray array = new JSONArray();
		
		for (Suggestion suggestion : suggestions) {
			if (suggestion.getKind() == Suggestion.TAG) {
				String id = "tag_" + UUID.randomUUID().toString();
				suggestionMap.put(id, suggestion);
				
				JSONObject jsonSuggestion = new JSONObject();
				
				float score = suggestion.getScore();
				if (Float.isInfinite(score) || Float.isNaN(score)) {
					score = 0;
				}
				
				jsonSuggestion.put("id", id);
				jsonSuggestion.put("score", score);
				jsonSuggestion.put("label", suggestion.getLabel());
				
				if (suggestion.getResources().size() > 0) {
					// TODO: ambiguous suggestions?
					jsonSuggestion.put("uri", suggestion.getResources().get(0));
				}
				
				array.put(jsonSuggestion);
			}
		}
		
		if (jsonpCallback != null) {
			return jsonpCallback + "(" + array.toString(4) + ")";
		}
		else {
			return array.toString(4);
		}
	}
	
	@POST
	@Path("/extractTaxonomyTags")
	@Consumes("multipart/form-data")
	@Produces("application/json")
	public String extractTaxonomyTags (MultipartFormDataInput formData) throws Exception {
		
		log.info("extractTaxonomyTags");
		
		// load taxonomy concepts from the configuration
		// TODO: cache this somewhere.

		// top level -> individual concepts
		Map<KiWiUriResource, List<KiWiUriResource>> categories = new LinkedHashMap<KiWiUriResource, List<KiWiUriResource>>();
		// concept -> top level
		Map<KiWiUriResource, KiWiUriResource> up = new HashMap<KiWiUriResource, KiWiUriResource> ();
		// top level -> best concept suggestion
		//Map<KiWiUriResource, KiWiUriResource> best = new HashMap<KiWiUriResource, KiWiUriResource> ();
		Map<KiWiUriResource, Float> scores = new HashMap<KiWiUriResource, Float>(); 
		
		for(String taxonomyConcept : configurationService.getConfiguration("kiwi.informationextraction.taxonomyConcepts").getListValue()) {
			String[] split = taxonomyConcept.split(" ");
			
			KiWiUriResource concept = tripleStore.createUriResource(split[0]);
			KiWiUriResource topLevelConcept = tripleStore.createUriResource(split[1]);
			// int level = Integer.parseInt(split[2]);
			
			if (!categories.containsKey(topLevelConcept)) {
				categories.put(topLevelConcept, new LinkedList<KiWiUriResource>());
			}
		
			categories.get(topLevelConcept).add(concept);
			up.put(concept, topLevelConcept);
		}
				
		Map<String, List<InputPart>> dataMap = formData.getFormDataMap();
		
		String jsonpCallback = getPart(dataMap, "jsonpCallback");
		
		Collection<Suggestion> suggestions = extractSuggestions(dataMap);
		
		JSONArray optionalArray = new JSONArray();
		
		for (Suggestion suggestion : suggestions) {
			if (suggestion.getKind() == Suggestion.TAG) {
				String id = "tag_" + UUID.randomUUID().toString();
				suggestionMap.put(id, suggestion);
				
				JSONObject jsonSuggestion = new JSONObject();
				
				jsonSuggestion.put("id", id);
				
				float score = suggestion.getScore();
				if (Float.isInfinite(score) || Float.isNaN(score)) {
					score = 0;
				}
				
				jsonSuggestion.put("score", score);
				jsonSuggestion.put("label", suggestion.getLabel());
				
				for (KiWiResource resource : suggestion.getResources()) {
					// is this a SKOS concept?
					if (resource.isUriResource()) {
						String uri = ((KiWiUriResource)resource).getUri();
						SKOSConcept c = kiwiEntityManager.find(SKOSConcept.class, uri);
						while (c != null) {
							if (c.getResource().isUriResource()) {
								KiWiUriResource cResource = (KiWiUriResource)c.getResource();
								if (up.containsKey(cResource)) {
									// found a concept in the list!
									// from a possible ambigous suggestion, select this one.
									jsonSuggestion.put("uri", uri);

									// and also remember the found upper level concept for the required list... 
									// best.put(up.get(cResource), cResource);
									if (!scores.containsKey(cResource)) {
										scores.put(cResource, 0.0f);
									}
									
									scores.put(cResource, 1.0f + scores.get(cResource));
								}
							}
							
							c = c.getBroader();
						}
						
						// set unambiguous uri only if it hasn't been found previously...
						if (!jsonSuggestion.has("uri")) {
							jsonSuggestion.put("uri", uri);
						}
					}
				}
				
				optionalArray.put(jsonSuggestion);
			}
		}
		
		JSONArray taxonomyArray = new JSONArray();
		for (KiWiUriResource topLevel : categories.keySet()) {
			JSONObject jsonTopLevel = new JSONObject();
			
			jsonTopLevel.put("label", topLevel.getLabel());
			jsonTopLevel.put("uri", topLevel.getUri());
			
			JSONArray concepts = new JSONArray();
			for (KiWiUriResource concept : categories.get(topLevel)) {
				JSONObject jsonConcept = new JSONObject();
				jsonConcept.put("label", concept.getLabel());
				jsonConcept.put("uri", concept.getUri());
				
				float score = 0.0f;
				if (scores.containsKey(concept)) {
					score = scores.get(concept);
				}
				
				jsonConcept.put("score", score);
				concepts.put(jsonConcept);
			}
			
			jsonTopLevel.put("narrower", concepts);	
			taxonomyArray.put(jsonTopLevel);
		}
		
		JSONObject ret = new JSONObject();
		ret.put("required", taxonomyArray);
		ret.put("optional", optionalArray);
		
		if (jsonpCallback != null) {
			return jsonpCallback + "(" + ret.toString(4) + ")";
		}
		else {
			return ret.toString(4);
		}
	}
	
	@GET
	@Path("/extractTaxonomyTags")
	@Produces("application/json")
	public String extractTaxonomyTags (@QueryParam("uri") String contentUri, @QueryParam("jsonpCallback") String jsonpCallback) throws Exception {
		
		log.info("extractTaxonomyTags");
		
		// load taxonomy concepts from the configuration
		// TODO: cache this somewhere.

		// top level -> individual concepts
		Map<KiWiUriResource, List<KiWiUriResource>> categories = new LinkedHashMap<KiWiUriResource, List<KiWiUriResource>>();
		// concept -> top level
		Map<KiWiUriResource, KiWiUriResource> up = new HashMap<KiWiUriResource, KiWiUriResource> ();
		// top level -> best concept suggestion
		//Map<KiWiUriResource, KiWiUriResource> best = new HashMap<KiWiUriResource, KiWiUriResource> ();
		Map<KiWiUriResource, Float> scores = new HashMap<KiWiUriResource, Float>(); 
		
		for(String taxonomyConcept : configurationService.getConfiguration("kiwi.informationextraction.taxonomyConcepts").getListValue()) {
			String[] split = taxonomyConcept.split(" ");
			
			KiWiUriResource concept = tripleStore.createUriResource(split[0]);
			KiWiUriResource topLevelConcept = tripleStore.createUriResource(split[1]);
			// int level = Integer.parseInt(split[2]);
			
			if (!categories.containsKey(topLevelConcept)) {
				categories.put(topLevelConcept, new LinkedList<KiWiUriResource>());
			}
		
			categories.get(topLevelConcept).add(concept);
			up.put(concept, topLevelConcept);
		}
		
		TextContent tc = null;
		ContentItem ci = contentItemService.getContentItemByUri(contentUri);
		if (ci != null) {
			tc = ci.getTextContent();
		}
		else {
			throw new Exception("No content item found with uri " + contentUri);
		}
	
		if (tc == null) {
			throw new Exception("No content to process!");
		}
	
		Collection<Suggestion> suggestions = informationExtractionService.extractTags(null, tc, Locale.ENGLISH);
				
		JSONArray optionalArray = new JSONArray();
		
		for (Suggestion suggestion : suggestions) {
			if (suggestion.getKind() == Suggestion.TAG) {
				String id = "tag_" + UUID.randomUUID().toString();
				suggestionMap.put(id, suggestion);
				
				JSONObject jsonSuggestion = new JSONObject();
				
				jsonSuggestion.put("id", id);
				
				float score = suggestion.getScore();
				if (Float.isInfinite(score) || Float.isNaN(score)) {
					score = 0;
				}
				jsonSuggestion.put("score", score);
				jsonSuggestion.put("label", suggestion.getLabel());
				
				for (KiWiResource resource : suggestion.getResources()) {
					// is this a SKOS concept?
					if (resource.isUriResource()) {
						String rUri = ((KiWiUriResource)resource).getUri();
						SKOSConcept c = kiwiEntityManager.find(SKOSConcept.class, rUri);
						while (c != null) {
							if (c.getResource().isUriResource()) {
								KiWiUriResource cResource = (KiWiUriResource)c.getResource();
								if (up.containsKey(cResource)) {
									// found a concept in the list!
									// from a possible ambigous suggestion, select this one.
									jsonSuggestion.put("uri", rUri);

									// and also remember the found upper level concept for the required list... 
									// best.put(up.get(cResource), cResource);
									if (!scores.containsKey(cResource)) {
										scores.put(cResource, 0.0f);
									}
									
									scores.put(cResource, 1.0f + scores.get(cResource));
								}
							}
							
							c = c.getBroader();
						}
						
						// set unambiguous uri only if it hasn't been found previously...
						if (!jsonSuggestion.has("uri")) {
							jsonSuggestion.put("uri", rUri);
						}
					}
				}
				
				optionalArray.put(jsonSuggestion);
			}
		}
		
		JSONArray taxonomyArray = new JSONArray();
		for (KiWiUriResource topLevel : categories.keySet()) {
			JSONObject jsonTopLevel = new JSONObject();
			
			jsonTopLevel.put("label", topLevel.getLabel());
			jsonTopLevel.put("uri", topLevel.getUri());
			
			JSONArray concepts = new JSONArray();
			for (KiWiUriResource concept : categories.get(topLevel)) {
				JSONObject jsonConcept = new JSONObject();
				jsonConcept.put("label", concept.getLabel());
				jsonConcept.put("uri", concept.getUri());
				
				float score = 0.0f;
				if (scores.containsKey(concept)) {
					score = scores.get(concept);
				}
				
				jsonConcept.put("score", score);
				concepts.put(jsonConcept);
			}
			
			jsonTopLevel.put("narrower", concepts);	
			taxonomyArray.put(jsonTopLevel);
		}
		
		JSONObject ret = new JSONObject();
		ret.put("required", taxonomyArray);
		ret.put("optional", optionalArray);
		
		if (jsonpCallback != null) {
			return jsonpCallback + "(" + ret.toString(4) + ")";
		}
		else {
			return ret.toString(4);
		}
	}
	

	/**
	 * A service that returns the required categories and their members. In the "score" attribute it returns 1 if the specified content item is tagged with the particular tag 
	 * @param uri
	 * @param jsonpCallback
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/listOfRequiredTags")
	@Produces("application/json")
	public String listOfRequiredTags (@QueryParam("uri") String uri, @QueryParam("jsonpCallback") String jsonpCallback) throws Exception {
		
		log.info("listOfRequiredTags");
	
		// load taxonomy concepts from the configuration
		// TODO: cache this somewhere.

		// top level -> individual concepts
		Map<KiWiUriResource, List<KiWiUriResource>> categories = new LinkedHashMap<KiWiUriResource, List<KiWiUriResource>>();
		// concept -> top level
		Map<KiWiUriResource, KiWiUriResource> up = new HashMap<KiWiUriResource, KiWiUriResource> ();
		// top level -> best concept suggestion
		//Map<KiWiUriResource, KiWiUriResource> best = new HashMap<KiWiUriResource, KiWiUriResource> ();
		Map<KiWiUriResource, Float> scores = new HashMap<KiWiUriResource, Float>(); 
		
		for(String taxonomyConcept : configurationService.getConfiguration("kiwi.informationextraction.taxonomyConcepts").getListValue()) {
			String[] split = taxonomyConcept.split(" ");
			
			KiWiUriResource concept = tripleStore.createUriResource(split[0]);
			KiWiUriResource topLevelConcept = tripleStore.createUriResource(split[1]);
			// int level = Integer.parseInt(split[2]);
			
			if (!categories.containsKey(topLevelConcept)) {
				categories.put(topLevelConcept, new LinkedList<KiWiUriResource>());
			}
		
			categories.get(topLevelConcept).add(concept);
			up.put(concept, topLevelConcept);
		}
		
		// tagging resource uris:
		Set<String> tagUris = new HashSet<String> ();
		
		ContentItem item = contentItemService.getContentItemByUri(uri);
		if (item == null) {
			log.info("content item #0 not found.", uri);
		}
		else {
			for(Tag tag : taggingService.getTags(item)) {
				if (tag.getTaggingResource().getResource().isUriResource()) {
					tagUris.add(((KiWiUriResource)tag.getTaggingResource().getResource()).getUri());
				}
			}
		}
								
		JSONArray taxonomyArray = new JSONArray();
		for (KiWiUriResource topLevel : categories.keySet()) {
			JSONObject jsonTopLevel = new JSONObject();
			
			jsonTopLevel.put("label", topLevel.getLabel());
			jsonTopLevel.put("uri", topLevel.getUri());
			
			JSONArray concepts = new JSONArray();
			for (KiWiUriResource concept : categories.get(topLevel)) {
				JSONObject jsonConcept = new JSONObject();
				jsonConcept.put("label", concept.getLabel());
				jsonConcept.put("uri", concept.getUri());
				
				float score = 0.0f;
				
				if (tagUris.contains(concept.getUri())) {
					score = 1.0f;
				}
				jsonConcept.put("score", score);
				concepts.put(jsonConcept);
			}
			
			jsonTopLevel.put("narrower", concepts);	
			taxonomyArray.put(jsonTopLevel);
		}
		
		JSONObject ret = new JSONObject();
		ret.put("required", taxonomyArray);
		// ret.put("optional", optionalArray);
		
		if (jsonpCallback != null) {
			return jsonpCallback + "(" + ret.toString(4) + ")";
		}
		else {
			return ret.toString(4);
		}
	}
	
	@GET
	@Path("/acceptSuggestion")
	@Produces("text/plain")
	public String acceptSuggestion(@QueryParam("id") String id) {
		return "accepting " + id;
	}
	
	@GET
	@Path("/rejectSuggestion")
	@Produces("text/plain")
	public String rejectSuggestion(@QueryParam("id") String id) {
		return "rejecting " + id;
	}
}
