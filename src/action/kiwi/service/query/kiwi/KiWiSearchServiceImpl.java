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
 * sschaffe
 * 
 */
package kiwi.service.query.kiwi;

import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ejb.Stateless;

import kiwi.api.query.KiWiQueryResult;
import kiwi.api.query.kiwi.KiWiSearchServiceLocal;
import kiwi.api.query.kiwi.KiWiSearchServiceRemote;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.exception.NonUniqueRelationException;
import kiwi.model.content.ContentItem;
import kiwi.service.query.kiwi.parser.KiWiQueryParser;
import kiwi.service.query.kiwi.parser.ParseException;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * KiWiSearchServiceImpl
 *
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.query.kiwiSearchService")
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
public class KiWiSearchServiceImpl implements KiWiSearchServiceLocal, KiWiSearchServiceRemote {

	@Logger
	private Log log;
	
	@In
	private FacesMessages facesMessages;
	
	
	/**
	 * The KiWi Search syntax is based on the search function in the KiWi system and takes the following
	 * form:
	 * <pre>
	 * SELECT ? ?ex:prop1 ?ex:prop2 WHERE {search string} FORMAT format_descriptor
	 * </pre>
	 * where ? denotes the content item selected by the search, ?ex:prop1 and ?ex:prop2 are rdf 
	 * properties of the selected content item that are projected into the result list, search string 
	 * is a search string (SOLR syntax + KiWi extensions) as could be entered in the KiWi search box,
	 * and format_descriptor is a String identifying the format to use as output. It will be passed as
	 * parameter to the QueryResultList for further processing by the JSF.
	 * 
	 * @see kiwi.api.query.kiwi.KiWiSearchService#query(java.lang.String)
	 * @param query
	 * @return
	 */
	@Override
	public KiWiQueryResult query(String query) {
		KiWiQueryResult result = null;
		KiWiQueryParser p = new KiWiQueryParser(new StringReader(query));
		try {
			p.parseQuery();

			SolrService solrService = (SolrService) Component.getInstance("solrService");

			KiWiSearchCriteria crit = solrService.parseSearchString(p.getSearchString());

			// at the moment, we set unlimited
			crit.setLimit(Integer.MAX_VALUE);
			crit.setOffset(0);

			KiWiSearchResults sResults = solrService.search(crit);

			result = new KiWiQueryResult();
			result.setResultFormat(p.getFormat());
			result.setColumnTitles(p.getProjectedVars());

			for(SearchResult sr : sResults.getResults()) {
				ContentItem item = sr.getItem();

				Map<String,Object> row = new LinkedHashMap<String,Object>();

				for(String var : p.getProjectedVars()) {
					if("".equals(var)) {
						row.put("",item);
					} else {
						// we need to query for some property ...
						try {
							row.put(var, item.getResource().getProperty(var));
						} catch (NonUniqueRelationException e) {
							log.error("error while querying property",e);
						}
					}
				}

				result.add(row);
			}

			log.info("evaluated query; projected variables=#0, number of results=#1",p.getProjectedVars(),result.size());

		} catch (ParseException e) {
			log.error("could not parse KiWi query \"#0\", error was: #1", query,e.getMessage());
			facesMessages.add("could not parse KiWi query \"#0\", error was: #1", query,e.getMessage());
		}

		return result;
	}


}
