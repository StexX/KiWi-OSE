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
package kiwi.action.debug;

import static kiwi.model.kbase.KiWiQueryLanguage.SPARQL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.exception.KiWiException;
import kiwi.exception.NonUniqueRelationException;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Name("sparqlQueryAction")
@Scope(ScopeType.CONVERSATION)
@Deprecated
public class QueryAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String queryText;
	List<ContentItem> results;
	
	/** Inject the KiWiEntityManager for querying. */
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@Logger
	private static Log log;
	
	public String runQuery(){
		
		log.info("sparql query: ' #0'", queryText);
		Query query = kiwiEntityManager.createQuery(
				queryText, 
				SPARQL, ContentItem.class);
		results = query.getResultList();
		log.info("found #0 items", results.size());
		Iterator<ContentItem> it = results.iterator();
		while(it.hasNext()){
			log.info("found #0", it.next().getResource().getKiwiIdentifier());
		}

		return "";
	}

	public List<KiWiResource> getTypes(ContentItem ci) throws NonUniqueRelationException, KiWiException{
		Iterable<KiWiResource> typeResources = ci.getTypes();
		List<KiWiResource> types = new ArrayList<KiWiResource>();
		// List<ContentItem> typeCIs = new ArrayList<ContentItem>();
		Iterator<KiWiResource> it=typeResources.iterator();
		while(it.hasNext()){
			KiWiResource type =it.next();
			if (type.getLabel(null)!=null && type.getLabel(null)!="" &&
					!type.getKiwiIdentifier().equals("uri::http://www.kiwi-project.eu/kiwi/core/ContentItem"))
				types.add(type);
			if(type.getContentItem()==null)
				log.debug("KiWiResource #0 has a ContentItem null. ", type.getKiwiIdentifier());
		}
		return types;
	}
	

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public List<ContentItem> getResults() {
		return results;
	}

	public void setResults(List<ContentItem> results) {
		this.results = results;
	}
}
