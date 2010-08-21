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

package kiwi.service.informationextraction;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.informationextraction.Extractlet;
import kiwi.api.informationextraction.KiWiGATEService;
import kiwi.api.informationextraction.LabelService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.Context;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Name("kiwi.informationextraction.deutschGateEntityExtractlet")
@Scope(ScopeType.STATELESS)
public class DeutschGateEntityExtractlet extends AbstractExtractlet {
	
	public DeutschGateEntityExtractlet() {
		super("kiwi.informationextraction.deutschGateEntityExtractlet");
	}

	@Logger
	private Log log;
	
	@In(value="kiwi.informationextraction.gateService", required = false)
	KiWiGATEService kiwiGateService;
	
	@In(create=true)
	TripleStore tripleStore;
	
	@In(create=true, value="kiwi.informationextraction.labelService")
	LabelService labelService;
	
	@In
	KiWiEntityManager kiwiEntityManager;
	
	@In(create=true)
	ContentItemService contentItemService;
	
	// Map of GATE annotation type to a type URI
	private Map<String, KiWiResource> gateTypeResource;
	
	private Map<String, KiWiResource> getGateTypeResource() {
		if (gateTypeResource == null) {
			gateTypeResource = new HashMap<String, KiWiResource> ();
			gateTypeResource.put("LOC", tripleStore.createUriResource("http://umbel.org/umbel/sc/Place"));
			gateTypeResource.put("PER", tripleStore.createUriResource(Constants.NS_FOAF + "Person"));
			gateTypeResource.put("ORG", tripleStore.createUriResource("http://umbel.org/umbel/sc/Organization"));
		}

		return gateTypeResource;
	}
	
	private static Comparator<gate.Annotation> _annotationComparator =  new Comparator<gate.Annotation>() {
		@Override
		public int compare(gate.Annotation o1, gate.Annotation o2) {
			return o1.getStartNode().getOffset().compareTo(o2.getStartNode().getOffset());
	}};
	
	private List<gate.Annotation> sortAS (gate.AnnotationSet as) {
		LinkedList<gate.Annotation> ret = new LinkedList<gate.Annotation>();
		ret.addAll(as);
		Collections.sort(ret, _annotationComparator);
		return ret;
	}

	private Context createContext(Annotation annot) {
		Context ret = new Context();
		ret.setContextBegin(annot.getStartNode().getOffset().intValue());
		ret.setContextEnd(annot.getEndNode().getOffset().intValue());
		ret.setInBegin(ret.getContextBegin());
		ret.setInEnd(ret.getContextEnd());
		ret.setIsFragment(true);
		
		return ret;
	}
	
	private String annotationToString (gate.Annotation a, gate.AnnotationSet tokenAS, gate.AnnotationSet lemmaAS) {
		List<gate.Annotation> tokens = sortAS(tokenAS.getContained(a.getStartNode().getOffset(), a.getEndNode().getOffset()));
		StringBuilder sb = new StringBuilder();
		for (gate.Annotation token : tokens) {
			String s = null;
			
			// if there is Lemma annotation, use that instead
			AnnotationSet lemmas = lemmaAS.get(token.getStartNode().getOffset(), token.getEndNode().getOffset());
			if (lemmas.size() == 1) {
				Annotation lemma = lemmas.iterator().next();
				s = (String)lemma.getFeatures().get("Lemma");
			}
			else {
				// try to use TreeTagger lemma
				s = (String)token.getFeatures().get("lemma");
				if ("<unknown>".equals(s)) {
					s = null;
				}
			}
			
			if (s == null) {
				s = (String)token.getFeatures().get("string");
			}
			if (sb.length() > 0) sb.append(' ');
			sb.append(s);
		}
		return sb.toString();
	}
	
	private List<KiWiResource> matchPerson(String string) {
		UserService userService = (UserService)Component.getInstance("userService");
		
		String[] names = string.split(" ");
		// try to match users, baseline heuristic
		Collection<User> users = userService.getUsers();
		List<KiWiResource> matchedUsers = new LinkedList<KiWiResource> ();
				
		for (User user : users) {
			if (names.length == 1) {
				if (names[0].equalsIgnoreCase(user.getFirstName())) {
					matchedUsers.add(user.getResource());
				}
			}
			else if (names.length >= 2) {
				char inicial = names[0].charAt(0);
				String name_surname = names[names.length - 1];
				
				if (name_surname.equalsIgnoreCase(user.getLastName()) && user.getFirstName().charAt(0) == inicial) {
					matchedUsers.add(user.getResource());
				}
			}
		}
		
		return matchedUsers;
	}
	
	/** 
	 * Find entities that matches the string
	 * @param title
	 * @return
	 */
	private List<KiWiResource> matchEntities(String string) {		
		return labelService.matchResources(string);
	}

	/**
	 * Handle person names specially (try to match them with existing KiWi users.
	 * @return
	 */
	private Collection<Suggestion> extractPersons (KiWiResource context, TextContent tc, gate.Document gateDoc) {
		List<Suggestion> suggestions = new LinkedList<Suggestion> (); 
		
		gate.AnnotationSet tokenAS = gateDoc.getAnnotations().get("Token");
		gate.AnnotationSet lemmaAS = gateDoc.getAnnotations().get("Lemma");
		List<Annotation> persons = sortAS (gateDoc.getAnnotations().get("PER"));

		for (gate.Annotation person : persons) {
			String title = annotationToString (person, tokenAS, lemmaAS);

			Suggestion es = new Suggestion();
			
			List<KiWiResource> matchedUsers = matchPerson(title);
			
			es.setKind(Suggestion.ENTITY);
			
			//es.setCategory("Person");
			
			List<KiWiResource> types = new LinkedList<KiWiResource> ();
			KiWiResource personType = getGateTypeResource().get("PER");
			log.info("person type: #0 #1", personType.getKiwiIdentifier(), personType.getId());
			types.add(personType);
			es.setTypes(types);
			
			
			if (matchedUsers.size() == 1) {
				es.setLabel(matchedUsers.iterator().next().getContentItem().getTitle());
			}
			else {
				es.setLabel(title);
			}	
			
			es.setResources(matchedUsers);
			
			InstanceEntity inst = new InstanceEntity();
			inst.setSourceResource(context);
			inst.setSourceTextContent(tc);
			
			inst.setContext(createContext(person));
			
			es.setInstance(inst);
						
			es.setScore(1.0f);
			
			suggestions.add(es);
			
		}
		
		return suggestions;
	}
	
	/**
	 * Extract GATE annotations classes as entities. (such as Location, Organization)
	 * @param gateDoc
	 * @return
	 */
	private Collection<Suggestion> extractEntities (KiWiResource context, TextContent tc, gate.Document gateDoc, String category) {
		List<Suggestion> suggestions = new LinkedList<Suggestion> ();
		gate.AnnotationSet tokenAS = gateDoc.getAnnotations().get("Token");
		gate.AnnotationSet lemmaAS = gateDoc.getAnnotations().get("Lemma");
		List<Annotation> entities = sortAS (gateDoc.getAnnotations().get(category));
		
		for (gate.Annotation entity : entities) {
			String title = annotationToString (entity, tokenAS, lemmaAS);

			Suggestion es = new Suggestion();
			es.setKind(Suggestion.ENTITY);
			es.setLabel(title);
			
			List<KiWiResource> types = new LinkedList<KiWiResource> ();
			KiWiResource categoryType = getGateTypeResource().get(category);
			if (categoryType != null) {
				log.info("#0 type: #1 #2", category, categoryType.getKiwiIdentifier(), categoryType.getId());
				types.add(categoryType);
			}
			es.setTypes(types);
			
			es.setResources(matchEntities (title));
			
			InstanceEntity inst = new InstanceEntity();
			inst.setSourceResource(context);
			inst.setSourceTextContent(tc);
			
			inst.setContext(createContext(entity));
			
			es.setInstance(inst);
						
			es.setScore(1.0f);
			
			suggestions.add(es);
		}
		
		return suggestions;
	}
	
	private Collection<Suggestion> extractContentItemsTitles (KiWiResource context, TextContent tc, gate.Document gateDoc) {
		ContentItemService contentItemService = (ContentItemService)Component.getInstance("contentItemService");
		gate.AnnotationSet tokenAS = gateDoc.getAnnotations().get("Token");
		List<Annotation> lookups = sortAS (gateDoc.getAnnotations().get("Lookup"));
		gate.AnnotationSet lemmaAS = gateDoc.getAnnotations().get("Lemma");
		List<Suggestion> suggestions = new LinkedList<Suggestion> ();
		
		for (gate.Annotation lookup : lookups) {
			
			gate.FeatureMap f = lookup.getFeatures();
			String majorType = (String)f.get("majorType");
			String minorType = (String)f.get("minorType");
			
			if ("ontology".equals(majorType)) {
				
				String title = annotationToString (lookup, tokenAS, lemmaAS);
				
				// resources are stored by hex id string in the "minorType" of the Lookup object 
				Long resourceId = Long.valueOf(minorType, 16);
				KiWiResource resource = entityManager.find(KiWiResource.class, resourceId);
				
				if (resource != null) {
					Suggestion es = new Suggestion();
					es.setKind(Suggestion.ENTITY);
					es.setLabel(title);
					
					es.setResources(matchEntities (title));
					
					InstanceEntity inst = new InstanceEntity();
					inst.setSourceResource(context);
					inst.setSourceTextContent(tc);
					
					inst.setContext(createContext(lookup));
					
					es.setInstance(inst);
				
					es.setScore(1.0f);
					
					suggestions.add (es);
				}
			}
		}
		
		return suggestions;
	}
	
	private Collection<Suggestion> getTermSuggestions(KiWiResource context, TextContent tc, Document doc) { 
		
		List<Suggestion> ret = new LinkedList<Suggestion>();
		if (doc == null) {
			return ret;
		}
		
		AnnotationSet as = doc.getAnnotations();

		AnnotationSet tokens = as.get("Token");
		AnnotationSet lemmaAS = as.get("Lemma");
		
		List<Annotation> nounChunks = new LinkedList<Annotation>();
		nounChunks.addAll(as.get("NP"));
		
		Comparator<Annotation> annotationComparator =  new Comparator<Annotation>() {

			@Override
			public int compare(Annotation o1, Annotation o2) {
				// TODO Auto-generated method stub
				return o1.getStartNode().getOffset().compareTo(o2.getStartNode().getOffset());
			}};
		
		Collections.sort(nounChunks, annotationComparator);
		
		for (Annotation chunk : nounChunks) {
			AnnotationSet tokensInChunk = tokens.getContained(chunk.getStartNode().getOffset(), chunk.getEndNode().getOffset());
			List<Annotation> tokensInChunkList = new LinkedList<Annotation>();
			tokensInChunkList.addAll(tokensInChunk);
			Collections.sort(tokensInChunkList, annotationComparator);
			
			StringBuilder sb = new StringBuilder();
			for (Annotation token : tokensInChunkList) {
				
				// don't include the initial determiner
				if (sb.length() == 0) {
					String category = (String)token.getFeatures().get("category");
					if ("ART".equals(category)) {
						continue;
					}
				}
				
				String s = null;
				
				// if there is Lemma annotation, use that instead
				AnnotationSet lemmas = lemmaAS.get(token.getStartNode().getOffset(), token.getEndNode().getOffset());
				if (lemmas.size() == 1) {
					Annotation lemma = lemmas.iterator().next();
					s = (String)lemma.getFeatures().get("Lemma");
				}
				else {
					// try to use TreeTagger lemma
					s = (String)token.getFeatures().get("lemma");
					if ("<unknown>".equals(s)) {
						s = null;
					}
				}
				
				if (s == null) {
					s = (String)token.getFeatures().get("string");
				}
				
				if (sb.length() > 0) {
					sb.append(' ');
				}
				
				sb.append(s);
			}
			
			String term = sb.toString();
			
			Suggestion es = new Suggestion();
			es.setKind(Suggestion.TAG);
			es.setLabel(term);
			
			// TODO: filter tags only.
			es.setResources(matchEntities (term));
			
			InstanceEntity inst = new InstanceEntity();
			inst.setSourceResource(context);
			inst.setSourceTextContent(tc);
			
			inst.setContext(createContext(chunk));
			
			es.setInstance(inst);
		
			es.setScore(1.0f);
			
			ret.add (es);
		}
		
		return ret;
	}
	
	private Collection<Suggestion> getEntitySuggestions(KiWiResource context, TextContent tc, Document gateDoc) {
		Collection<Suggestion> ret = new LinkedList<Suggestion>();
		
		ret.addAll (extractPersons (context, tc, gateDoc));
		ret.addAll (extractEntities (context, tc, gateDoc, "ORG"));
		ret.addAll (extractEntities (context, tc, gateDoc, "LOC"));
		ret.addAll (extractContentItemsTitles (context, tc, gateDoc));
		
		return ret;
	}

	@Override
	public void accept(Suggestion suggestion, User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Suggestion> extract(KiWiResource context,
			TextContent content, Document gateDoc, Locale language) {
Collection<Suggestion> ret = new LinkedList<Suggestion>();
		
		if (!language.getLanguage().equals(new Locale("de").getLanguage())) {
			log.info("Languages other than deutsch not yet supported. (locale: #0)", language);
			return ret;
		}

		ret.addAll (getTermSuggestions(context, content, gateDoc));
		ret.addAll (getEntitySuggestions(context, content, gateDoc));
		
		return ret;
	}

	@Override
	public void reject(Suggestion suggestion, User user) {
		// TODO Auto-generated method stub
		
	}
}
