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
package kiwi.service.query;

import javax.ejb.Stateless;

import kiwi.api.query.KiWiQueryResult;
import kiwi.api.query.QueryServiceLocal;
import kiwi.api.query.QueryServiceRemote;
import kiwi.api.query.kiwi.KiWiSearchService;
import kiwi.api.query.kwql.KwqlService;
import kiwi.api.query.sparql.SparqlService;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.cache.CacheProvider;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;


/**
 * @author Sebastian Schaffert
 *
 */
@Name("queryService")
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
public class QueryServiceImplCore implements QueryServiceLocal, QueryServiceRemote {

	/**
	 * maximum time to cache query results (default: 60 seconds)
	 */
	private static final long QUERY_TTL = 60000;
	
	@Logger
	private Log log;
	
	@In
	private FacesMessages facesMessages;
	
	@In
	private CacheProvider<Object> cacheProvider;
	
	@In("kiwi.query.kwqlService")
	private KwqlService kwqlService;
	
	@In("kiwi.query.kiwiSearchService")
	private KiWiSearchService kiwiSearchService;
	
	@In("kiwi.query.sparqlService")
	private SparqlService sparqlService;

	
	/* (non-Javadoc)
	 * @see kiwi.api.query.QueryService#query(java.lang.String, java.lang.String)
	 */
	@Override
	public KiWiQueryResult query(String query, String language) {
		KiWiQueryResult result = (KiWiQueryResult) cacheProvider.get("QUERY-"+language.toUpperCase(), query);
			if(language==null){
			log.error("query language is null!");
			facesMessages.add("query language shouldn't be null.");			
		}
		if(result == null || result.getLastModified() + QUERY_TTL < System.currentTimeMillis()) {
			if("KIWI".equals(language.toUpperCase())) {
				result = kiwiSearchService.query(query);
			} else if ("KWQL".equals(language.toUpperCase())) {
				result = kwqlService.query(query);
			} else if ("SPARQL".equals(language.toUpperCase())) {
				result = sparqlService.query(query);
			} else {
				log.error("query language #0 not yet supported",language);
				facesMessages.add("query language #0 not yet supported",language);
			}
			if(result != null) {
				cacheProvider.put("QUERY-"+language.toUpperCase(), query, result);
			}
		}
		return result;
	}

}
