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
package kiwi.api.query;


/**
 * The QueryService offers generic functionality for running queries against the data in the
 * KiWi system. It supports different query languages (SPARQL, search) and delegates to the
 * respective querying subsystem.
 * <p>
 * The main method of the query service is "query" which takes as argument a string containing
 * the query and a KiWiQueryLanguage describing which query language to use to evaluate the
 * query. It returns a QueryResultList (list of QueryResults) that can then be further formatted.
 * <p>
 * Currently, the QueryService offers three syntaxes: KiWi Search, KWQL and SPARQL. For each of the
 * syntaxes, it is possible to apply different formatters. Information and properties for them 
 * is included in the QueryResultList.
 * <p>
 * The KiWi Search syntax is based on the search function in the KiWi system and takes the following
 * form:
 * <pre>
 * SELECT ? ?ex:prop1 ?ex:prop2 WHERE search string FORMAT format_descriptor
 * </pre>
 * where ? denotes the content item selected by the search, ?ex:prop1 and ?ex:prop2 are rdf 
 * properties of the selected content item that are projected into the result list, search string 
 * is a search string (SOLR syntax + KiWi extensions) as could be entered in the KiWi search box,
 * and format_descriptor is a String identifying the format to use as output. It will be passed as
 * parameter to the QueryResultList for further processing by the JSF.
 * 
 * 
 * @author Sebastian Schaffert
 *
 */
public interface QueryService {

	
	public KiWiQueryResult query(String query, String language);
}
