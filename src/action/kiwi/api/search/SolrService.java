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

import java.util.Collection;

import kiwi.model.kbase.KiWiEntity;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * A service for interfacing Apache SOLR for searching and indexing.
 * 
 * @author Sebastian Schaffert
 *
 */
public interface SolrService {

	public void enqueue(KiWiEntity entity);
	
	public void runQueue();
	
	/**
	 * Persist a KiWi entity in the SOLR search index for searching.
	 * 
	 * @param entity
	 */
	public void persist(KiWiEntity entity);
	
	/**
	 * Remove a KiWi entity from the SOLR search index.
	 * 
	 * @param entity
	 */
	public void remove(KiWiEntity entity);
	
	/**
	 * Reindex all content items passed as arguments. Provided as a batch method for faster
	 * indexing.
	 * 
	 * @param entities the entities to update
	 */
	public void persistAll(Collection<? extends KiWiEntity> entities);
	
	/**
	 * Remove all kiwi entities passed as arguments. Provided as a batch method for faster 
	 * removal.
	 * 
	 * @param entities the entities to remove
	 */
	public void removeAll(Collection<? extends KiWiEntity> entities);
	
	/**
	 * Search for content items matching the query string passed as argument. The query string is 
	 * parsed and converted into SOLR query syntax appropriate as result
	 * 
	 * @param query
	 * @return
	 */
	public KiWiSearchResults search(KiWiSearchCriteria query);
	
	
	/**
	 * Run a direct SOLR query by calling query() on the server with a SolrQuery as argument. 
	 * Returns the QueryResponse returned by the server.
	 * <p>
	 * This method may be used if there is need for specialised search that requires direct SOLR
	 * access.
	 * 
	 * @param query
	 * @return
	 */
	public QueryResponse search(SolrQuery query);

	
	/**
	 * Optimize the index by sending the appropriate command to the SOLR server.
	 */
	public void optimize();
	
	
	/*
	 * Methods for building KiWiSearchCriteria from string, building SolrQuery from 
	 * KiWiSearchCriteria, etc. Useful if one needs more fine-grained control over how search is
	 * executed.
	 */
	
	/**
	 * Parse a KiWi search string into a KiWiSearchCriteria object, and return this object for
	 * further modification.
	 */
	public KiWiSearchCriteria parseSearchString(String searchString);
	
	/**
	 * Build a SolrQuery from KiWiSearchCriteria. Creates an appropriate Solr search string from
	 * the criteria's fields. The SolrQuery is returned for further modification (setting additional
	 * facets, setting which fields to retrieve, ...).
	 * 
	 * @param criteria
	 * @return
	 */
	public SolrQuery buildSolrQuery(KiWiSearchCriteria criteria);
	
	/**
	 * Rebuild the search index from the content items contained in the database. Might run a long
	 * time.
	 */
	public void rebuildIndex();

	/**
	 * Refresh CEQ values from the content items contained in the database. Might run a long
	 * time.
	 */
	public void updateCEQValues();

}
