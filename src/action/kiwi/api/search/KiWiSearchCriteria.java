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
package kiwi.api.search;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery.ORDER;

/**
 * This class defines a search over content items indexed in the KiWi system. It allows to define
 * a variety of different criteria for the various aspects associated with content items:
 * <ul>
 * <li>full-text search over title and content of a content item using Lucene syntax</li>
 * <li>range search over the modification date of a content item using SOLR syntax</li>
 * <li>tag search over the tags associated with a content item</li>
 * <li>type search over the types (given by KiWi identifier) associated with a content item</li>
 * <li>person search, currently over the authors of a content item</li>
 * <li>rdf literal search, allowing to perform a full-text search over the literal content of a
 *     certain property</li>
 * <li>rdf numerical search for integer and double, allowing to perform range queries over the 
 *     literal content of a certain property</li>
 * </ul>
 * 
 * In addition, the KiWiSearchCriteria allow to specify over which RDF properties the search
 * result should build facets and the limit and offset of results to return in the query.
 * <p>
 * Note that not all search implementations support all features. The search currently offered by
 * the KiWiEntityManager mostly allows full-text and tag-based search. The full feature set is
 * offered by the SolrService.
 * 
 * @author Sebastian Schaffert
 *
 */
public class KiWiSearchCriteria {

	/** full-text search string (title and content) */
	private String keywords;
	
	/** SOLR search string, will be prepended to the generated SOLR search as-is. */
	private String solrSearchString;
	
	/** range query over modification date */
	private Date toDate,fromDate;
	
	/** collection of tag labels to search for */
	private Set<String> tags;
	
	/** collection of purposes labels to search for */
	private Set<String> purposes;	
	
	/** collection of kiwi identifiers of types to search for */
	private Set<String> types;
	
	/** author to search for (login name) */
	private String person;
	
	/**
	 * Map containing the names of fields where keyword searches should query, mapped to the
	 * relative weight of the respective field for the ranking of results.
	 */
	private Map<String,Integer> keywordSearchFields;
	
	
	/**
	 *  map of rdf literal properties to search for; key is property URI, value is a valid SOLR query 
	 *  (text pattern)
	 */
	private Map<String,Set<String>> rdfLiteralProperties;
	
	/**
	 *  map of rdf integer properties to search for; key is property URI, value is a valid numerical
	 * SOLR query, i.e. either an integer or a range query over integers
	 */
	private Map<String,Set<String>> rdfIntegerProperties;
	
	/**
	 *  map of rdf double properties to search for; key is property URI, value is a valid numerical
	 *  SOLR query, i.e. either a double value or a range query over doubles
	 */
	private Map<String,Set<String>> rdfDoubleProperties;
	
	/**
	 * map of rdf object properties to search for; key is a property URI, value is the KiWi 
	 * identifier of a KiWi resource that is used as object of the object property in all returned 
	 * search results.
	 * 
	 * @see KiWiResource.getKiwiIdentifier()
	 */
	private Map<String,Set<String>> rdfObjectProperties;
	
	/**
	 * collection of datatype/annotation property uris for which to build facets in the result 
	 * (tags, types, and authors are already facetted by default)
	 */
	private Set<String> rdfLiteralFacets;
	
	/**
	 * collection of object property uris for which to build facets in the result
	 */
	private Set<String> rdfObjectFacets;
	
	@Deprecated
	private String rdfQuery;
	
	private String sortField;
	private ORDER sortOrder;
	
	private int offset = 0, limit = -1;
	
	public KiWiSearchCriteria() {
		tags  = new HashSet<String>();
		purposes  = new HashSet<String>();
		types = new HashSet<String>();
		rdfLiteralFacets = new HashSet<String>();
		rdfObjectFacets  = new HashSet<String>();
		rdfLiteralProperties = new HashMap<String,Set<String>>();
		rdfIntegerProperties = new HashMap<String,Set<String>>();
		rdfDoubleProperties  = new HashMap<String,Set<String>>();
		rdfObjectProperties  = new HashMap<String,Set<String>>();
		keywordSearchFields  = new HashMap<String,Integer>();
		sortField            = "score";
		sortOrder            = ORDER.desc;
	}

	/**
	 * @return the searchString
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param searchString the searchString to set
	 */
	public void setKeywords(String searchString) {
		this.keywords = searchString;
	}

	
	
	public String getSolrSearchString() {
		return solrSearchString;
	}

	public void setSolrSearchString(String solrSearchString) {
		this.solrSearchString = solrSearchString;
	}

	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the tags
	 */
	public Set<String> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the author
	 */
	public String getPerson() {
		return person;
	}

	/**
	 * @param author the author to set
	 */
	public void setPerson(String author) {
		this.person = author;
	}

	
	
	public Set<String> getTypes() {
		return types;
	}

	public void setTypes(Set<String> types) {
		this.types = types;
	}

	
	
	/**
	 * @return the rdfLiteralProperties
	 */
	public Map<String, Set<String>> getRdfLiteralProperties() {
		return rdfLiteralProperties;
	}

	/**
	 * @param rdfLiteralProperties the rdfLiteralProperties to set
	 */
	public void setRdfLiteralProperties(Map<String, Set<String>> rdfLiteralProperties) {
		this.rdfLiteralProperties = rdfLiteralProperties;
	}

	/**
	 * @return the rdfIntegerProperties
	 */
	public Map<String, Set<String>> getRdfIntegerProperties() {
		return rdfIntegerProperties;
	}

	/**
	 * @param rdfIntegerProperties the rdfIntegerProperties to set
	 */
	public void setRdfIntegerProperties(Map<String, Set<String>> rdfIntegerProperties) {
		this.rdfIntegerProperties = rdfIntegerProperties;
	}

	/**
	 * @return the rdfDoubleProperties
	 */
	public Map<String, Set<String>> getRdfDoubleProperties() {
		return rdfDoubleProperties;
	}

	/**
	 * @param rdfDoubleProperties the rdfDoubleProperties to set
	 */
	public void setRdfDoubleProperties(Map<String, Set<String>> rdfDoubleProperties) {
		this.rdfDoubleProperties = rdfDoubleProperties;
	}

	
	
	
	public Map<String, Set<String>> getRdfObjectProperties() {
		return rdfObjectProperties;
	}

	public void setRdfObjectProperties(Map<String, Set<String>> rdfObjectProperties) {
		this.rdfObjectProperties = rdfObjectProperties;
	}

	public Map<String, Integer> getKeywordSearchFields() {
		return keywordSearchFields;
	}

	public void setKeywordSearchFields(Map<String, Integer> keywordSearchFields) {
		this.keywordSearchFields = keywordSearchFields;
	}

	/**
	 * @return the rdfFacets
	 */
	public Set<String> getRdfLiteralFacets() {
		return rdfLiteralFacets;
	}

	/**
	 * @param rdfFacets the rdfFacets to set
	 */
	public void setRdfLiteralFacets(Set<String> rdfFacets) {
		this.rdfLiteralFacets = rdfFacets;
	}

	
	
	public Set<String> getRdfObjectFacets() {
		return rdfObjectFacets;
	}

	public void setRdfObjectFacets(Set<String> rdfObjectFacets) {
		this.rdfObjectFacets = rdfObjectFacets;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the rdfQuery
	 */
	public String getRdfQuery() {
		return rdfQuery;
	}

	/**
	 * @param rdfQuery the rdfQuery to set
	 */
	public void setRdfQuery(String rdfQuery) {
		this.rdfQuery = rdfQuery;
	}
	
	public boolean isHqlCriterionSet() {
		return person != null || toDate != null || fromDate != null || tags.size() > 0 ;
	}
	
	public boolean isRdfCriterionSet() {
		return rdfQuery != null;
	}

	public Set<String> getPurposes() {
		return purposes;
	}

	public void setPurposes(Set<String> purposes) {
		this.purposes = purposes;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	
	public void setSortField(String sortField, ORDER sortOrder) {
		setSortField(sortField);
		setSortOrder(sortOrder);
	}

	public ORDER getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(ORDER sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	
}
