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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.informationextraction.Extractlet;
import kiwi.api.informationextraction.LabelService;
import kiwi.api.informationextraction.TermRecognitionService;
import kiwi.api.informationextraction.TermRecognitionService.Term;
import kiwi.api.informationextraction.TermRecognitionService.TermGroupScore;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.model.Constants;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.Context;
import kiwi.model.informationextraction.Example;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Name("kiwi.informationextraction.englishGateEntityExtractlet")
@Scope(ScopeType.STATELESS)
public class EnglishGateEntityExtractlet extends AbstractExtractlet implements Extractlet {
	
	public EnglishGateEntityExtractlet() {
		super("kiwi.informationextraction.englishGateEntityExtractlet");
	}
	
	@Logger
	private Log log;
	
	@In
	private ConfigurationService configurationService;
	
	@In(create=true)
	TripleStore tripleStore;
	
	@In
	KiWiEntityManager kiwiEntityManager;
	
	@In(create=true)
	ContentItemService contentItemService;
	
	@In(create=true, value="kiwi.informationextraction.labelService")
	LabelService labelService;
	
	@In(value="kiwi.informationextraction.termRecognitionService", create=true)
	TermRecognitionService termRecognitionService;
	
	/**
	 * Types configured to be relevant types. All other types are to be ignored.
	 * (load every time we extract, we are stateless)
	 */
	Set<KiWiUriResource> entityTypes;
	
	// Map of GATE annotation type to a type URI
	private Map<String, KiWiResource> gateTypeResource;
	
	private Map<String, KiWiResource> getGateTypeResource() {
		if (gateTypeResource == null) {
			gateTypeResource = new HashMap<String, KiWiResource> ();
			gateTypeResource.put("Location", tripleStore.createUriResource("http://umbel.org/umbel/sc/Place"));
			gateTypeResource.put("Person", tripleStore.createUriResource(Constants.NS_FOAF + "Person"));
			gateTypeResource.put("Organization", tripleStore.createUriResource("http://umbel.org/umbel/sc/Organization"));
		}

		return gateTypeResource;
	}
	
	private Context createContext(Document gateDoc, Annotation annot) {
	
		String plain = (String)gateDoc.getFeatures().get(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME);
		
		Context ret = new Context();
		ret.setContextBegin(annot.getStartNode().getOffset().intValue());
		ret.setContextEnd(annot.getEndNode().getOffset().intValue());
		ret.setInBegin(ret.getContextBegin());
		ret.setInEnd(ret.getContextEnd());
		ret.setInContext(plain.substring(ret.getContextBegin(), ret.getContextEnd()));
		ret.setIsFragment(true);
		
		return ret;
	}
	
	private Context createContext(Document gateDoc, long begin, long end) {
		String plain = (String)gateDoc.getFeatures().get(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME);
		
		Context ret = new Context();
		ret.setContextBegin((int)begin);
		ret.setContextEnd((int)end);
		ret.setInBegin(ret.getContextBegin());
		ret.setInEnd(ret.getContextEnd());
		ret.setInContext(plain.substring(ret.getContextBegin(), ret.getContextEnd()));
		ret.setIsFragment(true);
		
		return ret;
	}
	
	private Context createContext(String label) {
		 Context ret = new Context();
		 ret.setIsFragment(false);
		 ret.setInContext(label);
		 
		 return ret;
	}
	
	private float computeScore(Suggestion suggestion) {
		int pos = 0;
		int neg = 0;
		
		// log.info("computing score for suggestion #0<#1>#2", suggestion.getInstance().getContext().getLeftContext(), suggestion.getInstance().getContext().getInContext(), suggestion.getInstance().getContext().getRightContext());
		
		for (Example example : getExamples(suggestion)) {
			if (example.getType() == Example.POSITIVE) {
				pos++;
			}
			else {
				neg++;
			}
		}
		
		// log.info("score: #0", ((float)pos + 1) / (float)(pos + neg + 1));
		
		// compute the score as a probability (fraction of positive vs. all) with add 1 smoothing
		return ((float)pos + 1) / (float)(pos + neg + 1);
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
	
	private String annotationToString (gate.Annotation a, gate.AnnotationSet tokenAS) {
		List<gate.Annotation> tokens = sortAS(tokenAS.getContained(a.getStartNode().getOffset(), a.getEndNode().getOffset()));
		StringBuilder sb = new StringBuilder();
		for (gate.Annotation token : tokens) {
			String s = (String)token.getFeatures().get("string");
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
	 * Sets resource and types properties of a suggestion by matching an existing
	 * resource with a specified label.
	 * 
	 * @param suggestion
	 * @param string
	 */
	private void matchEntity (Suggestion suggestion, String string) {
		for (KiWiResource resource : labelService.matchResources(string)) {
		
			suggestion.getResources().add(resource);
			for (KiWiResource type : resource.getTypes()) {
				if (entityTypes.contains(type) && !suggestion.getTypes().contains(type)) {
					suggestion.getTypes().add(type);
				}
			}
		}
	}

	/**
	 * Handle person names specially (try to match them with existing KiWi users.
	 * @return
	 */
	private Collection<Suggestion> extractPersons (KiWiResource context, TextContent tc, gate.Document gateDoc) {
		List<Suggestion> suggestions = new LinkedList<Suggestion> (); 
		
		gate.AnnotationSet tokenAS = gateDoc.getAnnotations().get("Token");
		List<Annotation> persons = sortAS (gateDoc.getAnnotations().get("Person"));

		for (gate.Annotation person : persons) {
			String title = annotationToString (person, tokenAS);

			Suggestion es = new Suggestion();
			
			List<KiWiResource> matchedUsers = matchPerson(title);
			
			es.setKind(Suggestion.ENTITY);
			
			//es.setCategory("Person");
			
			List<KiWiResource> types = new LinkedList<KiWiResource> ();
			KiWiResource personType = getGateTypeResource().get("Person");
			log.info("person type: #0 #1", personType.getKiwiIdentifier(), personType.getId());
			if (entityTypes.contains(personType)) {
				types.add(personType);
			}

			es.setTypes(types);
			
			
			if (matchedUsers.size() == 1) {
				es.setLabel(matchedUsers.iterator().next().getContentItem().getTitle());
			}
			else {
				es.setLabel(title);
			}	
			
			es.setResources(matchedUsers);
			
			InstanceEntity inst = new InstanceEntity();
			inst.setExtractletName(name);
			inst.setSourceResource(context);
			inst.setSourceTextContent(tc);
			
			inst.setContext(createContext(gateDoc, person));
			
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
		List<Annotation> entities = sortAS (gateDoc.getAnnotations().get(category));
		
		for (gate.Annotation entity : entities) {
			String title = annotationToString (entity, tokenAS);

			Suggestion es = new Suggestion();
			es.setKind(Suggestion.ENTITY);
			es.setLabel(title);
			//es.setCategory(category);
			/*es.setTypes(new LinkedList<KiWiResource>());
			if (getGateTypeResource().get(category) != null) {
				es.getTypes().add(getGateTypeResource().get(category));
			}*/
			
			List<KiWiResource> types = new LinkedList<KiWiResource> ();
			KiWiResource categoryType = getGateTypeResource().get(category);
			if (categoryType != null) {
				if (entityTypes.contains(categoryType)) {
					types.add(categoryType);
				}
			}
			es.setTypes(types);
			
			matchEntity(es, title);
			// es.setResources(matchEntities (title));
			
			InstanceEntity inst = new InstanceEntity();
			inst.setExtractletName(name);
			inst.setSourceResource(context);
			inst.setSourceTextContent(tc);
			
			inst.setContext(createContext(gateDoc, entity));
			
			es.setInstance(inst);
						
			es.setScore(1.0f);
			
			suggestions.add(es);
		}
		
		return suggestions;
	}
	
	private Collection<Suggestion> extractContentItemsTitles (KiWiResource context, TextContent tc, gate.Document gateDoc) {
		// ContentItemService contentItemService = (ContentItemService)Component.getInstance("contentItemService");
		gate.AnnotationSet tokenAS = gateDoc.getAnnotations().get("Token");
		List<Annotation> lookups = sortAS (gateDoc.getAnnotations().get("Lookup"));
		List<Suggestion> suggestions = new LinkedList<Suggestion> (); 
		
		for (gate.Annotation lookup : lookups) {
			
			gate.FeatureMap f = lookup.getFeatures();
			String majorType = (String)f.get("majorType");
			String minorType = (String)f.get("minorType");
			
			if ("ontology".equals(majorType)) {
				
				
				String title = annotationToString (lookup, tokenAS);
				Long resourceId = Long.valueOf(minorType, 16);
				
				KiWiResource resource = entityManager.find(KiWiResource.class, resourceId);
				//ContentItem ciByUri = contentItemService.getContentItemByUri(minorType);
				//log.debug("matching title #0 #1 #2", title, minorType, ciByUri);
				
				if (resource != null) {
					
					Suggestion es = new Suggestion();
					es.setKind(Suggestion.ENTITY);
					es.setLabel(title);
					
					matchEntity(es, title);
					// es.setResources(matchEntities (title));
					
					InstanceEntity inst = new InstanceEntity();
					inst.setExtractletName(name);
					inst.setSourceResource(context);
					inst.setSourceTextContent(tc);
					
					inst.setContext(createContext(gateDoc, lookup));
					
					es.setInstance(inst);
				
					es.setScore(1.0f);
					
					suggestions.add (es);
				}
			}
		}
		
		return suggestions;
	}

	private Collection<Suggestion> getEntitySuggestions(KiWiResource context, TextContent tc, Document gateDoc) {

		Collection<Suggestion> ret = new LinkedList<Suggestion>();
		
		ret.addAll (extractPersons (context, tc, gateDoc));
		ret.addAll (extractEntities (context, tc, gateDoc, "Organization"));
		ret.addAll (extractEntities (context, tc, gateDoc, "Location"));
		ret.addAll (extractContentItemsTitles (context, tc, gateDoc));
		
		return ret;
	}
	
	private Collection<Suggestion> getTermSuggestions(KiWiResource context, TextContent tc, Document gateDoc) { 
		
		List<Suggestion> ret = new LinkedList<Suggestion>();
		if (gateDoc == null) {
			return ret;
		}
		
		AnnotationSet as = gateDoc.getAnnotations();

		AnnotationSet tokens = as.get("Token");
		
		List<Annotation> termAnnotations = new LinkedList<Annotation>();
		termAnnotations.addAll(as.get("Term"));
		
		Comparator<Annotation> annotationComparator =  new Comparator<Annotation>() {

			@Override
			public int compare(Annotation o1, Annotation o2) {
				// TODO Auto-generated method stub
				return o1.getStartNode().getOffset().compareTo(o2.getStartNode().getOffset());
			}};
			
		class TermOffsets {
			public TermOffsets(String label, long begin, long end) {
				this.label = label;
				this.begin = begin;
				this.end = end;
			}
			public String label;
			public long begin;
			public long end;
		}
			
		Collection<TermOffsets> terms = new LinkedList<TermOffsets>();
			
		for (Annotation term : termAnnotations) {
			AnnotationSet tokensInTerm = tokens.getContained(term.getStartNode().getOffset(), term.getEndNode().getOffset());
			List<Annotation> tokensInTermList = new LinkedList<Annotation>();
			tokensInTermList.addAll(tokensInTerm);
			Collections.sort(tokensInTermList, annotationComparator);
			
			if (tokensInTermList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (Annotation token : tokensInTermList) {
					if (sb.length() > 0) {
						sb.append(' ');
					}
				
					sb.append(token.getFeatures().get("string"));
				}
		
				terms.add(new TermOffsets(sb.toString(), tokensInTermList.get(0).getStartNode().getOffset(), tokensInTermList.get(tokensInTermList.size() - 1).getEndNode().getOffset()));
			
				log.info("Term: #0", sb.toString());
			}
		}	
	
		for (TermOffsets term : terms) {
			
			String label = term.label;
			
			Suggestion es = new Suggestion();
			es.setKind(Suggestion.TAG);
			es.setLabel(label);
			
			// TODO: filter tags only.
			//es.setResources(matchEntities (label));
			matchEntity(es, label);
			
			InstanceEntity inst = new InstanceEntity();
			inst.setExtractletName(name);
			inst.setSourceResource(context);
			inst.setSourceTextContent(tc);
			
			inst.setContext(createContext(gateDoc, term.begin, term.end));
			
			es.setInstance(inst);
		
			// set real score after term merging...
			es.setScore(1.0f);
			
			ret.add (es);
		}
		
		return ret;
	}
	
	private Collection<Suggestion> mergeTermSuggestions (Collection<Suggestion> suggestions) {
		Map<String, Suggestion> label2suggestion = new HashMap<String, Suggestion> ();
		for (Suggestion suggestion : suggestions) {
			Suggestion merged = label2suggestion.get(suggestion.getLabel());
			if (merged == null) {
				Suggestion newSuggestion = new Suggestion();
				newSuggestion.setKind(Suggestion.TAG);
				newSuggestion.setExtractletName(name);
				newSuggestion.setInstance(suggestion.getInstance());
				newSuggestion.setScore(1.0f);
				newSuggestion.getResources().addAll(suggestion.getResources());
				newSuggestion.getTypes().addAll(suggestion.getTypes());
				newSuggestion.setLabel(suggestion.getLabel());
				label2suggestion.put(newSuggestion.getLabel(), newSuggestion);
			}
			else {
				merged.getResources().addAll(suggestion.getResources());
				merged.getTypes().addAll(suggestion.getTypes());
				merged.setScore(merged.getScore() + 1.0f);
			}
		}
		
		return label2suggestion.values();
	}

	@Override
	public Collection<Suggestion> extract(KiWiResource context,
			TextContent content, Document gateDoc, Locale language) {
	
		Collection<Suggestion> ret = new LinkedList<Suggestion>();
		
		// use English by default
		if (language == null) {
			language = Locale.ENGLISH;
		}
		
		if (!language.getLanguage().equals(new Locale("en").getLanguage())) {
			log.info("Languages other than english not yet supported. (locale: #0)", language);
			return ret;
		}
	
		// load entity types from the config
		entityTypes = new HashSet<KiWiUriResource>();
		for(String entity_type : configurationService.getConfiguration("kiwi.informationextraction.entityTypes").getListValue()) {
			entityTypes.add(tripleStore.createUriResource(entity_type));
		}
		
		Collection<Suggestion> entitySuggestions = getEntitySuggestions(context, content, gateDoc);
		Collection<Suggestion> termSuggestions = getTermSuggestions(context, content, gateDoc);
		
		// filter terms that are inside entity suggestions. Such terms will be suggested as tags with entity type...
		// TODO: this is suboptimal solution.
		Iterator<Suggestion> iterator = termSuggestions.iterator();
		while(iterator.hasNext()) {
			Suggestion termSuggestion = iterator.next();
			int begin = termSuggestion.getInstance().getContext().getInBegin();
			int end = termSuggestion.getInstance().getContext().getInEnd();
			
			for (Suggestion entity : entitySuggestions) {
				int eBegin = entity.getInstance().getContext().getInBegin();
				int eEnd = entity.getInstance().getContext().getInEnd();
				
				if ((begin <= eBegin && eBegin <= end) ||
					(begin <= eEnd && eEnd <= end) ||
					(eBegin <= begin && begin <= eEnd) ||
					(eBegin <= end && end <= eBegin)) {
					iterator.remove();
					break;
				}
			}
		}
	
		class TermSuggestion implements kiwi.api.informationextraction.TermRecognitionService.Term {
			
			private Suggestion suggestion;
			
			public TermSuggestion(Suggestion suggestion) {
				this.suggestion = suggestion;
			}
			
			public Suggestion getSuggestion () {
				return suggestion;
			}

			@Override
			public String getLabel() {
				return suggestion.getLabel();
			}
		}
		
		
		Collection<TermSuggestion> allSuggestions = new LinkedList<TermSuggestion> ();
		for (Suggestion suggestion: termSuggestions) {
			allSuggestions.add(new TermSuggestion(suggestion));
		}
		for (Suggestion suggestion: entitySuggestions) {
			allSuggestions.add(new TermSuggestion(suggestion));
		}
		
		// We will merge the term suggestions, creating a distinct list of "tag suggestions"
		//Collection<Suggestion> ret = new LinkedList<Suggestion> ();
		
		for (TermGroupScore tgs : termRecognitionService.scoreTerms(allSuggestions)) {
			
			// create a tag suggestion
			Suggestion tagSuggestion = new Suggestion();
			tagSuggestion.setExtractletName(this.name);
			tagSuggestion.setKind(Suggestion.TAG);
			tagSuggestion.setLabel(tgs.getLabel());
			
			InstanceEntity inst = new InstanceEntity();
			inst.setExtractletName(name);
			inst.setSourceResource(context);
			inst.setSourceTextContent(content);
			
			// we use the normalized label as the "context", so that any other term normalized to the same label will be used for 
			// the feedback score calculation
			inst.setContext(createContext(tgs.getNormalizedLabel()));
			
			tagSuggestion.setInstance(inst);
			
			// the feedback score, based on the context (which is the label)
			float feedbackScore = computeScore(tagSuggestion);
			
			// relevance score was computed by the termRecognitionService
			float relevanceScore = tgs.getScore();
		
			// the final score is a simple combination of the two scores...
			float score = feedbackScore * relevanceScore;
			
			tagSuggestion.setScore(score);
			
			// go through all the grouped suggestions and:
			// 1. set the computed score to all the ENTITY suggestions
			// 2. get the type and resource suggestions into the tag suggestion
			for (Term suggestionTerm : tgs.getTerms()) {
				Suggestion suggestion = ((TermSuggestion)suggestionTerm).getSuggestion();
				
				if (suggestion.getKind() == Suggestion.ENTITY) {
					suggestion.setScore(score);
					ret.add(suggestion);
				}
				
				for (KiWiResource resource : suggestion.getResources()) {
					if (!tagSuggestion.getResources().contains(resource)) {
						tagSuggestion.getResources().add(resource);
					}
				}
				
				for (KiWiResource type : suggestion.getTypes()) {
					if (!tagSuggestion.getTypes().contains(type)) {
						tagSuggestion.getTypes().add(type);
					}
				}
			}
			
			ret.add(tagSuggestion);
		}
		
		return ret;
	}
	
	// @Transactional
	public void reject(Suggestion suggestion, User user) {
		log.info("rejectingSuggestion #0<#1>#2", suggestion.getInstance().getContext().getLeftContext(),  suggestion.getInstance().getContext().getInContext(),  suggestion.getInstance().getContext().getRightContext());
		super.reject(suggestion, user);
	}
	
	// @Transactional
	public void accept(Suggestion suggestion, User user) {
		log.info("acceptingSuggestion #0<#1>#2", suggestion.getInstance().getContext().getLeftContext(),  suggestion.getInstance().getContext().getInContext(),  suggestion.getInstance().getContext().getRightContext());
		super.accept(suggestion, user);
	}
}
