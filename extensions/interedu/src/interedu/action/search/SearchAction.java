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
package interedu.action.search;

import interedu.api.configuration.Constants;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import kiwi.action.search.KiWiSearchEngine;
import kiwi.action.webservice.thesaurusManagement.SKOSConceptUtils;
import kiwi.api.ontology.SKOSService;
import kiwi.api.search.KiWiSearchResults.KiWiFacet;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

@Scope(ScopeType.SESSION)
@Name("interedu.searchAction")
//@Transactional
public class SearchAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	Log log;
	
	private static final String SEARCH_ALL = "ganzem Portal";
	private static final String SEARCH_CATEGORY = "aktueller Kategorie";
	private static final String[] searchAreas = { SEARCH_ALL,SEARCH_CATEGORY };
	
	private static final String STATUS_ALL = "Alle Artikel";
	private static final String STATUS_0 = "Nur unbearbeitete Artikel";
	private static final String STATUS_1 = "Nur bearbeitete Artikel";
	private static final String STATUS_2 = "Nur freigegebene Artikel";
	private static final String[] searchStates = {STATUS_ALL ,STATUS_0 ,STATUS_1, STATUS_2};
	
	private static final String BASIC_SEARCHSTRING = "Suchbegriff";
	
	private LinkedList<SKOSConceptCover> breadCrumb;
	private String searchString = BASIC_SEARCHSTRING;
	private String internalSearchString;
	private String searchArea = SEARCH_ALL;
	private String searchStatus = STATUS_ALL;
	
	@In(create=true)
	private SKOSService skosService;
	
    @In(value="kiwiSearchEngine",create=true)
    private KiWiSearchEngine searchEngine;
	
	//***************************** set search options **********************
	
	/**
	 * returns the whole breadcrumb list. creates a new one if necessary
	 * @return
	 */
	public List<SKOSConceptCover> getBreadCrumb() {
		if( breadCrumb == null ) {
			breadCrumb = new LinkedList<SKOSConceptCover>();
			SKOSConcept c = skosService.getConcept( Constants.ROOT_CONCEPT_NAME );
			String label = SKOSConceptUtils.getConceptLabelInLanguage(c, new Locale("en"));
			breadCrumb.add(new SKOSConceptCover(c,label));
		}
		return breadCrumb;
	}
	
	/**
	 * return subconcepts of last element in breadcrumb
	 * @return
	 */
	public List<SKOSConceptCover> getSubConcept() {
		//if breadCrumb not exists
		if( breadCrumb == null ) {
			breadCrumb = new LinkedList<SKOSConceptCover>();
			SKOSConcept c = skosService.getConcept( Constants.ROOT_CONCEPT_NAME );
			String label = SKOSConceptUtils.getConceptLabelInLanguage(c, new Locale("en"));
			breadCrumb.add(new SKOSConceptCover(c,label));
		}
		LinkedList<SKOSConceptCover> l = new LinkedList<SKOSConceptCover>();
		
		//get narrower of current concept
		HashSet<SKOSConcept> h = breadCrumb.getLast().getConcept().getNarrower();
		for( SKOSConcept c : h ) {
			String label = SKOSConceptUtils.getConceptLabelInLanguage(c, new Locale("en"));
			l.add(new SKOSConceptCover(c,label));
		}
		
		//order list
		Collections.sort(l, new Comparator<SKOSConceptCover>(){
			@Override
			public int compare(SKOSConceptCover o1, SKOSConceptCover o2) {
				return o1.getLabel().compareTo( o2.getLabel() );
			}
		});
		
		return l;
	}
	
	/**
	 * select a subconcept of last elemnt in breadcrumb
	 * @param concept
	 */
	public String appendConcept(SKOSConcept concept) {
		String label = SKOSConceptUtils.getConceptLabelInLanguage(concept, new Locale("en"));
		breadCrumb.add(new SKOSConceptCover(concept,label));
		return doConceptSearch();
	}
	
	/**
	 * select a concept in breadcrumb
	 * @param concept
	 */
	public String selectBreadCrumbConcept(SKOSConcept concept) {
		LinkedList<SKOSConceptCover> l = new LinkedList<SKOSConceptCover>();
		for( SKOSConceptCover c : breadCrumb ) {
			l.add(c);
			if( c.getConcept().getId() == concept.getId() ) {
				break;
			}
		}
		breadCrumb = l;
		return doConceptSearch();
	}
	
	/**
	 * select a concept and set the breadcrumb path
	 * @param concept
	 */
	public String selectConcept( SKOSConcept concept ) {
		breadCrumb = new LinkedList<SKOSConceptCover>();
		SKOSConcept current = concept;
		while( current != null ) {
			String label = SKOSConceptUtils.getConceptLabelInLanguage(current, new Locale("en"));
			breadCrumb.addFirst(new SKOSConceptCover(current,label));
			
			if( current.getPreferredLabel().equals( Constants.ROOT_CONCEPT_NAME ) ) {
				break;
			} else {
				//set a broader concept
				current = current.getBroader();
			}
		}
		return doConceptSearch();
	}
	
	public String selectTag(String tag) {
		internalSearchString = "";
		searchStatus = STATUS_ALL;
		searchString = BASIC_SEARCHSTRING;
		return addSearchTag(tag);
	}

	/**
	 * keyword based search
	 * @param searchString
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchString() {
		return searchString;
	}
	
	public void resetSearchString() {
		this.searchString = "";
	}
	
	public static String[] getSearchAreas() {
		return searchAreas;
	}

	public void setSearchArea( String searchArea) {
		this.searchArea = searchArea;
		log.info("set searchArea to #0",searchArea);
	}
	
	public String getSearchArea() {
		return searchArea;
	}
	
	public static String[] getSearchStates() {
		return searchStates;
	}

	public String getSearchStatus() {
		return searchStatus;
	}
	
	//*********************** search *****************************
	
	public String setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
		return doSearch( internalSearchString );
	}
	
	/**
	 * searches with set parameters
	 */
	public String doKeywordSearch() {
		
		searchStatus = STATUS_ALL;
		
		if( searchArea.equals(SEARCH_ALL) ) {
			internalSearchString = searchString;
		} else {
			internalSearchString = searchString + " interedu:categoryid:\""+breadCrumb.getLast().getConcept().getId()+"\"";
		}
		
		return doSearch( internalSearchString );
	}
	
	public String doConceptSearch() {
		
		searchStatus = STATUS_ALL;
		
		internalSearchString = "interedu:categoryid:\""+breadCrumb.getLast().getConcept().getId()+"\"";
		searchString = BASIC_SEARCHSTRING;
		
		return doSearch( internalSearchString );
	}
	
	private String doSearch( String fullSearchString ) {
		
		if( fullSearchString != "" ) {
		
		//only articles should be returned
		fullSearchString += " type:interedu:Artikel";
		fullSearchString += getStateSearchStringAppendix();
		
		log.info( "search: searchString(#0)", fullSearchString );
		searchEngine.begin();
		searchEngine.setSearchQuery(fullSearchString);
		searchEngine.setPage(1);
		searchEngine.runSearch();
		
		log.info("open page search.xhtml");
		}
		return "search";
	}
	
	public String renderedSearchPageParts(){
		return "search_complete,content_middle_complete,content_right_complete";
	}

    public String getSearchView() {
    	return searchEngine.getSearchView();
	}

	public KiWiSearchEngine getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(KiWiSearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}

	public String getSearchQuery() {
		return searchEngine.getSearchQuery();
	}
	
	//**************** facets *************************
	public String addSearchTag(String tag) {
		//searchString += " tag:\""+tag+"\"";
		internalSearchString += " tag:\""+tag+"\"";
		return doSearch( internalSearchString );
	}
	
	public List<KiWiFacet<ContentItem>> getRelevantTags() {
		List<KiWiFacet<ContentItem>> results = searchEngine.getRelevantTags();
		
		Collections.sort(results, new Comparator<KiWiFacet<ContentItem>>() {

			@Override
			public int compare(KiWiFacet<ContentItem> o1, KiWiFacet<ContentItem> o2) {
				return new Long(o2.getResultCount()).compareTo( o1.getResultCount());
			}
			
		});
		int upperBound = 5;
		if( results.size() < 5 ) {
			upperBound = results.size();
		}
		return results.subList(0, upperBound);
	}
	
	public List<KiWiFacet<String>> getRdfFacetValues(KiWiUriResource property) {
		List<KiWiFacet<String>> results = searchEngine.getRdfFacetValues(property);
		
		Collections.sort(results, new Comparator<KiWiFacet<String>>() {

			@Override
			public int compare(KiWiFacet<String> o1, KiWiFacet<String> o2) {
				return new Long(o2.getResultCount()).compareTo( o1.getResultCount());
			}
			
		});
		int upperBound = 5;
		if( results.size() < 5 ) {
			upperBound = results.size();
		}
		return results.subList(0, upperBound);
	}
	
	public boolean existsRdfFacetValue(KiWiUriResource property) {
		return !searchEngine.getRdfFacetValues(property).isEmpty();
	}
	
	public String addRdfSearchProperty(KiWiUriResource prop, String value) {
		log.debug("addRdfSearchProperty: prop=#0, value=#1", prop.getUri(), value);
		String label = prop.getNamespacePrefix() + ":" + prop.getLocalName();
		
		//searchString += " " + label+":\""+value+"\"";
		internalSearchString += " " + label+":\""+value+"\"";
		
		return doSearch( internalSearchString );
		
	}
	
	//********************* search by status *********************
	
	private String getStateSearchStringAppendix() {
		if( searchStatus.equals( STATUS_0 ) ) {
			return " interedu:state:0";
		}
		
		if( searchStatus.equals( STATUS_1 ) ) {
			return " interedu:state:1";
		}
		
		if( searchStatus.equals( STATUS_2 ) ) {
			return " interedu:state:2";
		}
		
		return "";
	}
	
}
