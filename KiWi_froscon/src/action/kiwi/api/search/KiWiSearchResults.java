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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.apache.solr.common.SolrDocument;

/**
 * This class represents the search results of a search performed in the KiWi system. 
 * 
 * @author Sebastian Schaffert
 *
 */
public class KiWiSearchResults {

	/**
	 * The list of content items returned by the search, limited by the search offset and limit.
	 */
	private List<SearchResult> results;
	
	/**
	 * The total number of results returned by the query, ignoring offset and limit.
	 */
	private long resultCount;
	
	/**
	 * The facets for tags associated with the results: for each tag that occurs in the total 
	 * results (ignoring offset and limit), a KiWiFacet is added to this set. The content of the
	 * KiWiFacet is the tagging content item.
	 */
	private Set<KiWiFacet<ContentItem>> tagFacets;
	
	
	/**
	 * The facets for purpose associated with the results: for each tag that occurs in the total 
	 * results (ignoring offset and limit), a KiWiFacet is added to this set. The content of the
	 * KiWiFacet is the tagging content item.
	 */
	private Set<KiWiFacet<String>> purposeFacets;	
	
	/**
	 * The facets for types associated with the results: for each type that occurs in the total
	 * results (ignoring offset and limit), a KiWiFacet is added to this set. The content of the 
	 * KiWiFacet is the KiWiResource used as type.
	 */
	private Set<KiWiFacet<KiWiResource>> typeFacets;
	
	/**
	 * The facets for persons associated with the results: for each person occuring in the total
	 * results (ignoring offset and limit), a KiWiFacet is added to this set. The content of
	 * the KiWiFacet is the user object related to the person.
	 */
	private Set<KiWiFacet<User>> personFacets;
	
	/**
	 * A map from properties (uri resources) to facets associated with the property. For each 
	 * literal value occurring for the property, a KiWiFacet is added to the set mapped to. The
	 * content of the KiWiFacet is the string value of the literal.
	 */
	private Map<KiWiUriResource,Set<KiWiFacet<String>>> propertyFacets;
	
	
	/**
	 * A map from properties (uri resources) to facets associated with the object property. For each 
	 * resource value occurring for the property, a KiWiFacet is added to the set mapped to. The
	 * content of the KiWiFacet is the KiWiResource.
	 */
	private Map<KiWiUriResource,Set<KiWiFacet<KiWiResource>>> objectPropertyFacets;
	
	public KiWiSearchResults() {
		results        = new LinkedList<SearchResult>();
		tagFacets      = new HashSet<KiWiFacet<ContentItem>>();
		purposeFacets      = new HashSet<KiWiFacet<String>>();
		typeFacets     = new HashSet<KiWiFacet<KiWiResource>>();
		personFacets   = new HashSet<KiWiFacet<User>>();
		propertyFacets = new HashMap<KiWiUriResource, Set<KiWiFacet<String>>>();
		objectPropertyFacets = new HashMap<KiWiUriResource, Set<KiWiFacet<KiWiResource>>>();
	}
	
	/**
	 * @return the results
	 */
	public List<SearchResult> getResults() {
		return results;
	}




	/**
	 * @param results the results to set
	 */
	public void setResults(List<SearchResult> results) {
		this.results = results;
	}




	/**
	 * @return the resultCount
	 */
	public long getResultCount() {
		return resultCount;
	}




	/**
	 * @param resultCount the resultCount to set
	 */
	public void setResultCount(long resultCount) {
		this.resultCount = resultCount;
	}




	/**
	 * @return the tagFacets
	 */
	public Set<KiWiFacet<ContentItem>> getTagFacets() {
		return tagFacets;
	}




	/**
	 * @param tagFacets the tagFacets to set
	 */
	public void setTagFacets(Set<KiWiFacet<ContentItem>> tagFacets) {
		this.tagFacets = tagFacets;
	}




	/**
	 * @return the typeFacets
	 */
	public Set<KiWiFacet<KiWiResource>> getTypeFacets() {
		return typeFacets;
	}




	/**
	 * @param typeFacets the typeFacets to set
	 */
	public void setTypeFacets(Set<KiWiFacet<KiWiResource>> typeFacets) {
		this.typeFacets = typeFacets;
	}




	/**
	 * @return the personFacets
	 */
	public Set<KiWiFacet<User>> getPersonFacets() {
		return personFacets;
	}




	/**
	 * @param personFacets the personFacets to set
	 */
	public void setPersonFacets(Set<KiWiFacet<User>> personFacets) {
		this.personFacets = personFacets;
	}


	public Set<KiWiFacet<String>> getPurposeFacets() {
		return purposeFacets;
	}

	public void setPurposeFacets(Set<KiWiFacet<String>> purposeFacets) {
		this.purposeFacets = purposeFacets;
	};


	/**
	 * @return the propertyFacets
	 */
	public Map<KiWiUriResource, Set<KiWiFacet<String>>> getPropertyFacets() {
		return propertyFacets;
	}




	/**
	 * @param propertyFacets the propertyFacets to set
	 */
	public void setPropertyFacets(Map<KiWiUriResource, Set<KiWiFacet<String>>> propertyFacets) {
		this.propertyFacets = propertyFacets;
	}

	
	
	public Map<KiWiUriResource, Set<KiWiFacet<KiWiResource>>> getObjectPropertyFacets() {
		return objectPropertyFacets;
	}

	public void setObjectPropertyFacets(
			Map<KiWiUriResource, Set<KiWiFacet<KiWiResource>>> objectPropertyFacets) {
		this.objectPropertyFacets = objectPropertyFacets;
	}



	/**
	 * A class to represent a single search result (currently a combination of item and the 
	 * associated ranking score).
	 * 
	 * @author Sebastian Schaffert
	 *
	 */
	public static class SearchResult implements Comparable<SearchResult>{
		
		private ContentItem item;
		
		private SolrDocument solrDoc;
		
		private float score;
		
		private double ceq;
		
		private String highlightPreview;

		public SearchResult(ContentItem item, SolrDocument doc, float score, double ceq) {
			super();
			this.item = item;
			this.score = score;
			this.solrDoc   = doc;
			this.ceq = ceq;
		}
		
		public SearchResult(ContentItem item, SolrDocument doc, float score) {
			this(item, doc, score,0.0);
		}

		public ContentItem getItem() {
			return item;
		}

		public void setItem(ContentItem item) {
			this.item = item;
		}

		public float getScore() {
			return score;
		}

		public void setScore(float score) {
			this.score = score;
		}

		public double getCeq() {
			return ceq;
		}

		public void setCeq(double ceq) {
			this.ceq = ceq;
		}

		public String getHighlightPreview() {
			return highlightPreview;
		}

		public void setHighlightPreview(String highlightPreview) {
			this.highlightPreview = highlightPreview;
		}

		public SolrDocument getSolrDoc() {
			return solrDoc;
		}

		public void setSolrDoc(SolrDocument doc) {
			this.solrDoc = doc;
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 * Method used for ranking searchResult by score necessary when
		 * calculating personalized search
		 */
		public int compareTo(SearchResult searchResult) {
	        if (this.score == searchResult.getScore())
	            return 0;
	        else if (this.score < searchResult.getScore())
	            return 1;
	        else
	            return -1;
	    }
	}

	/**
	 * Representation of facet information as offered by the search result. Consists of the
	 * facet name/identifier, the count of items in this facet, and an appropriate search string
	 * for querying this facet.
	 *
	 * @author Sebastian Schaffert
	 */
	public static class KiWiFacet<T> {
		
		private T content;
		
		private long resultCount;
		
		private String query;

		public KiWiFacet(T content, long resultCount, String query) {
			super();
			this.content = content;
			this.resultCount = resultCount;
			this.query = query;
		}

		/**
		 * @return the name
		 */
		public T getContent() {
			return content;
		}

		/**
		 * @param name the name to set
		 */
		public void setContent(T content) {
			this.content = content;
		}

		/**
		 * @return the resultCount
		 */
		public long getResultCount() {
			return resultCount;
		}

		/**
		 * @param resultCount the resultCount to set
		 */
		public void setResultCount(long resultCount) {
			this.resultCount = resultCount;
		}

		/**
		 * @return the query
		 */
		public String getQuery() {
			return query;
		}

		/**
		 * @param query the query to set
		 */
		public void setQuery(String query) {
			this.query = query;
		}
		

		
		
	}

}
