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
package kiwi.service.tagging;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.ejb.Stateless;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.informationextraction.InformationExtractionService;
import kiwi.api.tagging.TagRecommendation;
import kiwi.api.tagging.TagRecommendationServiceLocal;
import kiwi.api.tagging.TagRecommendationServiceRemote;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import pitt.search.semanticvectors.CloseableVectorStore;
import pitt.search.semanticvectors.ObjectVector;
import pitt.search.semanticvectors.Search;
import pitt.search.semanticvectors.SearchResult;

/**
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("tagRecommendationService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class TagRecommendationServiceImpl implements TagRecommendationServiceLocal, TagRecommendationServiceRemote {

	
	@Logger
	Log log;
	
	@In
	ConfigurationService configurationService;
	
	@In
	ContentItemService contentItemService;
	
	@In
	TripleStore tripleStore;
	
	//private static CloseableVectorStore queryVecReader;
	private static CloseableVectorStore searchVecReader;
	
	@Override
	/**
	 * Return a list of tag recommendations for the content item ci. 
	 * <p>
	 * The calculation of recommendations depends on the underlying algorithm and is currently
	 * performed using semantic vectors on a corpus generated out of the KiWi search index. The
	 * ci.getKiWiIdentifier() is used to identify the index in the document vector.
	 *   
	 * Calculate recommendations based on term and document vectors created in SemanticIndexingService
	 * 
	 * @param ci
	 * @return Tag Recommendations
	 */
	public List<TagRecommendation> getRecommendations(ContentItem ci) {
		int defaultNumResults = 10;

		try {
			String dataDir = configurationService.getConfiguration("kiwi.semanticvectors").getStringValue() + File.separator + "data" + File.separator;


			// TODO: kiwi configuration service
			String docvectors  = dataDir + "semanticvectors"+File.separator+"docvectors.bin";
			String termvectors = dataDir + "semanticvectors"+File.separator+"termvectors.bin";
			String matchcase =  ci.getKiwiIdentifier();
			String[] args = { "-queryvectorfile", docvectors, "-searchvectorfile", termvectors, "-matchcase", matchcase };

			log.debug("looking up document vectors in #0",docvectors);
			log.debug("looking up term vectors in #0",termvectors);

			LinkedList<TagRecommendation> tagRecommendationList = new LinkedList<TagRecommendation>(); 
			LinkedList<SearchResult> results = Search.RunSearch(args, defaultNumResults);

			KiWiUriResource t_concept = tripleStore.createUriResource(Constants.NS_SKOS+"Concept");

			// Print out results.
			if (results != null && results.size() > 0) {
				for (SearchResult result: results) {
					String label = ((ObjectVector)result.getObject()).getObject().toString();
					ContentItem item = contentItemService.getContentItemByTitle(label);
					TagRecommendation tagRecommendation = new TagRecommendation(
							label,
							item != null && item.getResource().hasType(t_concept),
							item != null,
							result.getScore()
					);
					tagRecommendation.setItem(item);
					tagRecommendationList.add(tagRecommendation);
				}
			} else {
				log.debug("No search output.");
			}

			return tagRecommendationList;
		} catch(NullPointerException ex) {
			log.warn("semantic vector index not yet initialised; could not compute recommendation");
			return Collections.EMPTY_LIST;
		} catch(Exception ex) {
			log.error("an exception occurred while computing recommendations",ex);
			return Collections.EMPTY_LIST;
		}
	}
	
	
	private static class TagRecommendationRatingComparator implements Comparator<TagRecommendation> {

		@Override
		public int compare(TagRecommendation o1, TagRecommendation o2) {
			int ret =  - Float.compare(o1.getRating(), o2.getRating());
			if (ret == 0) {
				return - o1.getLabel().compareTo(o2.getLabel());
			}
			return ret;
		}
		
	}
	
	@Override
	public Map<String, Collection<TagRecommendation>> getGroupedRecommendations(ContentItem ci) {

		// How many tags per category?
		// TODO: make it configurable
		int per_category_limit = 10;
		
		Map<String, Collection<TagRecommendation>> ret = new LinkedHashMap<String, Collection<TagRecommendation>> ();
		
		InformationExtractionService informationExtractionService = 
			(InformationExtractionService)Component.getInstance("kiwi.informationextraction.informationExtractionService");
		
		KiWiUriResource t_concept = tripleStore.createUriResource(Constants.NS_SKOS+"Concept");
		
		Comparator<TagRecommendation> tagRecommendationRatingComparator = new TagRecommendationRatingComparator();
		TreeSet<TagRecommendation> others = new TreeSet<TagRecommendation> (tagRecommendationRatingComparator);
		
		// Recognize entities
		for (Suggestion es : informationExtractionService.extractTags(ci)) {
			ContentItem item = null;
			
			// TODO: modify TagRecommendation to support ambiguous items (and let User choose)
			// for now, set one randomly.
			if (es.getResources().size() >= 1) {
				item = es.getResources().iterator().next().getContentItem();
				
				if (es.getResources().size() > 1) {
					log.info("ambiguous entity: #0", es.getLabel());
					for (KiWiResource resource : es.getResources()) {
						log.info("    #0 (#1)", resource.getKiwiIdentifier(), resource.getContentItem().getTitle());
					}
				}
			}

			TagRecommendation tagRecommendation = new TagRecommendation(
					es.getLabel(),
					item != null && item.getResource().hasType(t_concept),
					item != null,
					es.getScore()
			);
			tagRecommendation.setItem(item);
			
			// Special case for uncategorized entities, to be merged with general "Uncategorized"
			if (es.getTypes().size() == 0) {
				others.add(tagRecommendation);
				if (others.size() > per_category_limit) {
					others.pollLast();
				}
			}
			else {
				for (KiWiResource type : es.getTypes()) {
					TreeSet<TagRecommendation> set = (TreeSet<TagRecommendation>) ret.get(type.getLabel());
					if (set == null) {
						set = new TreeSet<TagRecommendation>(tagRecommendationRatingComparator);
						ret.put(type.getLabel(), set);
					}
					set.add(tagRecommendation);
					if (set.size() > per_category_limit) {
						set.pollLast();
					}
				}
			}
		}
		
		others.addAll(getRecommendations(ci));
		if (others.size() > 0) {
			while (others.size() > per_category_limit) {
				others.pollLast();
			}
			ret.put("Uncategorized", others);
		}
		
		return ret;
	}
}
