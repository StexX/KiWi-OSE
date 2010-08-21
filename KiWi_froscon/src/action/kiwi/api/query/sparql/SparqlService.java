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
package kiwi.api.query.sparql;

import java.util.Map;

import kiwi.api.query.KiWiQueryResult;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.service.query.sparql.QueryBinding;

/**
 * SparqlService implements functionality for evaluating SPARQL queries on top of KiWi's triple
 * store.
 *
 * @author Sebastian Schaffert
 *
 */
public interface SparqlService {

	/**
	 * Evaluate the given query using the SPARQL language. Returns a KiWiQueryResult where the
	 * column names are the variable names used in the SPARQL select.
	 * 
	 * @param queryString a query string in SPARQL format; KiWi namespaces are added automatically
	 * @return a KiWiQueryResult where the column titles are the variable names
	 */
	public KiWiQueryResult query(String queryString);
	
	
	/**
	 * Query the TripleStore using the provided queryString and query language. Queries are executed
	 * using the underlying repository, currently Sesame 2.
	 * @param queryString
	 * @param qLang
	 * @return
	 */
	public Iterable<QueryBinding> query(String queryString, KiWiQueryLanguage qLang);

	/**
	 * Query for triples formed by the first three variables in the SELECT projection. This currently mostly
	 * makes sense with SPARQL queries, as SeRQL is not able to query the properties.
	 * 
	 * @param qString
	 * @param qLang
	 * @param preBinding predefined variable bindings to be used in the query
	 * @return
	 */
	public Iterable<KiWiTriple> queryTriple(String qString,
			KiWiQueryLanguage qLang, Map<String, KiWiNode> preBinding);

	/**
	 * Query for a resource using the provided query in the provided language. The method will return an iterator
	 * that iterates over the first variable in the query string.
	 * 
	 * @param qString
	 * @param qLang
	 * @return
	 */
	public abstract <C extends KiWiResource> Iterable<C> queryResource(String qString, KiWiQueryLanguage qLang);

	/**
	 * Query for a literal using the provided query in the provided language. The method will return an iterator
	 * that iterates over the first variable in the query string.
	 * 
	 * @param qString
	 * @param qLang
	 * @return
	 */
	public Iterable<KiWiLiteral> queryLiteral(String qString, KiWiQueryLanguage qLang);

	/**
	 * Return an iterable iterating over the string content of literal values in a query. Useful
	 * for many getter methods in KiWiResource and other classes. Note that this method ignores
	 * language information and should only be used when the language is clear.
	 * 
	 * @param qString
	 * @param qLang
	 * @return
	 */
	public Iterable<String> queryLiteralValue(String qString, KiWiQueryLanguage qLang);

	/**
	 * Generic method to query generically for a KiWiNode provided query in the provided language. The method will 
	 * return an iterator that iterates over the first variable in the query string.
	 * @param <C>
	 * @param qString
	 * @param qLang
	 * @return
	 */
	public <C extends KiWiNode> Iterable<C> queryNode(String qString, KiWiQueryLanguage qLang);

}
