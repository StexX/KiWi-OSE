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
package kiwi.service.query.sparql;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;

import kiwi.api.query.KiWiQueryResult;
import kiwi.api.query.sparql.SparqlServiceLocal;
import kiwi.api.query.sparql.SparqlServiceRemote;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.service.triplestore.TripleStoreUtil;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.openrdf.OpenRDFException;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;

/**
 * SparqlServiceImpl
 *
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.query.sparqlService")
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
public class SparqlServiceImpl implements SparqlServiceLocal, SparqlServiceRemote {

	@Logger
	private Log log;
	
	@In
	private TripleStore tripleStore;
	
	/**
	 * Evaluate a query using SPARQL syntax. This method delegates to the Triple Store's SPARQL
	 * functionality for the moment.
	 * 
	 * @see kiwi.api.query.sparql.SparqlService#query(java.lang.String)
	 */
	@Override
	public KiWiQueryResult query(String queryString) {
		KiWiQueryResult result = new KiWiQueryResult();
		
		// Provide the possibility to use ?this in queries. This gets replaced 
		// by the currentContentItems URI.
		ContentItem currentContentItem = (ContentItem)Component.getInstance("currentContentItem");
		
		queryString.replace("?this", ((KiWiUriResource)currentContentItem.getResource()).getUri());
		
		for(QueryBinding b : query(queryString,KiWiQueryLanguage.SPARQL)) {
			Map<String,Object> row = new LinkedHashMap<String,Object>();
			for(String key : b.getKeys()) {
				KiWiNode n = b.getBinding(key);
				
				if(n.isLiteral()) {
					row.put(key, ((KiWiLiteral)n).getContent() );
				} else {
					row.put(key, ((KiWiResource)n).getContentItem() );
				}
			}
			result.add(row);
		}
		
		return result;
	}
	
	
	
    /**
     * Query the TripleStore using the provided queryString and query language. Queries are executed
     * using the underlying repository, currently Sesame 2.
     * @param queryString
     * @param qLang
     * @return
     */
    public synchronized Iterable<QueryBinding> query(String queryString, KiWiQueryLanguage qLang) {
        try {
        	RepositoryConnection con = tripleStore.getRepository().getConnection();
        	
            final TupleQuery tupleQuery = con.prepareTupleQuery(
            		TripleStoreUtil.transformKiWiToSesame(tripleStore,qLang), 
            		buildQuery(queryString,qLang));
            final TupleQueryResult result = tupleQuery.evaluate();
 
            Set<QueryBinding> resultSet = new HashSet<QueryBinding>();
            while(result.hasNext()) {
                BindingSet bindingSet = result.next();
                QueryBinding b = new QueryBinding(tripleStore);
                for (String key : bindingSet.getBindingNames()) {
                    b.setBinding(key, bindingSet.getValue(key));
                }
                resultSet.add(b);
            }
            result.close();
            con.close();
            
            log.debug("query yielded #0 result items",resultSet.size());
            
            return resultSet;

        } catch (OpenRDFException e) {
            throw new UnsupportedOperationException("Operation not yet supported!");
        }
    }

    /**
     * Query for triples formed by the first three variables in the SELECT projection. This currently mostly
     * makes sense with SPARQL queries, as SeRQL is not able to query the properties.
     * 
     * @param qString
     * @param qLang
     * @param preBinding predefined variable bindings to be used in the query
     * @return
     */
    public synchronized Iterable<KiWiTriple> queryTriple(String qString, KiWiQueryLanguage qLang, Map<String,KiWiNode> preBinding) {
        List<KiWiTriple> triples = new LinkedList<KiWiTriple>();
    	try {
        	RepositoryConnection con = tripleStore.getRepository().getConnection();
        	
            final TupleQuery tupleQuery = con.prepareTupleQuery(
            		TripleStoreUtil.transformKiWiToSesame(tripleStore,qLang), 
            		buildQuery(qString,qLang));
            if(preBinding != null) {
                for(String varName : preBinding.keySet()) {
                    tupleQuery.setBinding(
                    		varName, 
                    		TripleStoreUtil.transformKiWiToSesame(tripleStore,preBinding.get(varName)));
                }
            }
            final TupleQueryResult result = tupleQuery.evaluate();
            final List<String> bindingNames = result.getBindingNames();
            if(!result.hasNext()) {
            	return triples;
            }
            BindingSet bindingSet = result.next();
            final String varSubj = bindingNames.get(0), varProp = bindingNames.get(1), varObj = bindingNames.get(2);
            
            for(String s : bindingNames) {
                // statement needed to construct context
            	Statement stmt = con.getStatements((Resource) bindingSet.getValue(varSubj), (URI) bindingSet.getValue(varProp), bindingSet.getValue(varObj), true).next();

                triples.add(TripleStoreUtil.transformSesameToKiWi(tripleStore,stmt));
            }
            
            return triples;
         } catch (OpenRDFException e) {
            log.error("error while querying for triples",e);
            throw new UnsupportedOperationException("Operation not yet supported!");
        }
    }

    /**
     * Query for a resource using the provided query in the provided language. The method will return an iterator
     * that iterates over the first variable in the query string.
     * 
     * @param qString
     * @param qLang
     * @return
     */
    public <C extends KiWiResource> Iterable<C> queryResource(String qString, KiWiQueryLanguage qLang) {
        return queryNode(qString,qLang);
    }
    
    /**
     * Query for a literal using the provided query in the provided language. The method will return an iterator
     * that iterates over the first variable in the query string.
     * 
     * @param qString
     * @param qLang
     * @return
     */
    public Iterable<KiWiLiteral> queryLiteral(String qString, KiWiQueryLanguage qLang) {
        return queryNode(qString,qLang);
    }
    
    /**
     * Return an iterable iterating over the string content of literal values in a query. Useful
     * for many getter methods in KiWiResource and other classes. Note that this method ignores
     * language information and should only be used when the language is clear.
     * 
     * @param qString
     * @param qLang
     * @return
     */
    public Iterable<String> queryLiteralValue(String qString, KiWiQueryLanguage qLang) {
        final Iterator<KiWiLiteral> it = queryLiteral(qString,qLang).iterator();
        return new Iterable<String>() {

            public Iterator<String> iterator() {
                return new Iterator<String>() {

                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    public String next() {
                        return it.next().getContent().toString();
                    }

                    public void remove() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    
                };
            }
            
        };
    }
    
    /**
     * Generic method to query generically for a KiWiNode provided query in the provided language. The method will 
     * return an iterator that iterates over the first variable in the query string.
     * @param <C> the type of node to return
     * @param qString the query string in SPARQL or SeRQL
     * @param qLang the query language (either SPARQL or SeRQL)
     * @return an iterable containing the results of the query
     */
    @SuppressWarnings("unchecked")
	public synchronized <C extends KiWiNode> Iterable<C> queryNode(String qString, KiWiQueryLanguage qLang) {
       
        try {
        	RepositoryConnection con = tripleStore.getRepository().getConnection();
        	
            final TupleQuery tupleQuery = con.prepareTupleQuery(
            		TripleStoreUtil.transformKiWiToSesame(tripleStore,qLang), 
            		buildQuery(qString,qLang));
            final TupleQueryResult result = tupleQuery.evaluate();
            final List<String> bindingNames = result.getBindingNames();
            final String varSubj = bindingNames.get(0);

            BindingSet bindingSet = null;
            Set<C> resultSet = new HashSet<C>();
            while(result.hasNext()) {
                bindingSet = result.next();
                resultSet.add((C)TripleStoreUtil.transformSesameToKiWi(tripleStore,bindingSet.getValue(varSubj)));
            }
            result.close();
            
            log.debug("queryNode: query yielded #0 result items",resultSet.size());

            return resultSet;

        } catch (OpenRDFException e) {
            log.error("error while querying for resource; query was #0, error was #1",qString,e.getMessage());
            throw new UnsupportedOperationException("error while querying for resource (query string: "+qString+")");
        } catch(NullPointerException e) {
        	log.error("NullPointerException while querying for resource; query string: #0",qString,e);
        	throw e;
        }
       
    }
    
    /**
     * Build a query string for the given query language by adding namespace definitions as
     * contained in this knowledge space according to the syntax of qLang.
     * 
     * @param qString the query string in the language specified
     * @param qLang query language used for query
     * @return
     */
    private String buildQuery(String qString, KiWiQueryLanguage qLang) {
        StringBuilder builder = new StringBuilder();
        switch(qLang) {
            case SPARQL:
                // add all prefixes to the query string
                for(KiWiNamespace ns : tripleStore.listNamespaces() ) {
                        if(ns.getPrefix().equals("")) {
                                builder.append("BASE <"+ns.getUri()+">\n");
                        } else {
                                builder.append("PREFIX "+ns.getPrefix()+": <"+ns.getUri()+">\n");
                        }
                }
                builder.append(qString);
                log.debug("created query string: #0",builder.toString());
                return builder.toString();
            case SERQL:
                builder.append(qString);
                Iterator<KiWiNamespace> it = tripleStore.listNamespaces().iterator();                
                if(it.hasNext()) {
                    builder.append("\n");
                    builder.append("USING NAMESPACE\n");
                    while(it.hasNext()) {
                        KiWiNamespace ns = it.next();
                        builder.append("  ");
                        builder.append(ns.getPrefix());
                        builder.append(" = <");
                        builder.append(ns.getUri());
                        builder.append(">");
                        if(it.hasNext()) {
                            builder.append(",\n");
                        }
                    }
                }
                log.debug("created query string: #0",builder.toString());
                return builder.toString();
            default:
                return qString;
        }
    }


}
