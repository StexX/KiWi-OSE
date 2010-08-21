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

package kiwi.service.ontology;

import static kiwi.model.kbase.KiWiQueryLanguage.SPARQL;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.ontology.OntologyServiceLocal;
import kiwi.api.ontology.OntologyServiceRemote;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.SolrService;
import kiwi.model.Constants;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.ontology.KiWiClass;
import kiwi.model.ontology.KiWiProperty;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("ontologyService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class OntologyServiceImpl implements OntologyServiceLocal, OntologyServiceRemote {

	/** Inject the Seam logger for logging. */
	@Logger
	private Log log;
	
	/** Inject the KiWiEntityManager for querying. */
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	/** For direct HQL queries */
	@In
	private EntityManager entityManager;
	
	/** for class autocompletion */
	@In
	private SolrService solrService;
	
	/**
	 * List all classes that are defined in the KiWi system.
	 * 
	 * @return a list of KiWiClass objects representing the classes in the KiWi system
	 * @see kiwi.api.ontology.OntologyService#listClasses()
	 */
	public List<KiWiClass> listClasses() {
//		Query query = kiwiEntityManager.createQuery(
//				"SELECT ?S WHERE { " +
//				"{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_OWL + "Class> } " +
//				" UNION " +
//				"{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_RDFS + "Class> } }", 
//				SPARQL, KiWiClass.class);
		List<KiWiClass> classes = new LinkedList<KiWiClass>();
		
		Query q = entityManager.createNamedQuery("ontologyService.listClasses");
		q.setHint("org.hibernate.cacheable", true);

		Set setItems = new LinkedHashSet(kiwiEntityManager.createFacadeList(q.getResultList(),KiWiClass.class,false));		
		classes.addAll(setItems);
		
		return classes;
	}

	/**
	 * List all properties that are defined in the KiWi system.
	 * 
	 * @return a list of KiWiProperty objects representing the properties in the KiWi system
	 * @see kiwi.api.ontology.OntologyService#listProperties()
	 */
	public List<KiWiProperty> listProperties() {
		Query query = kiwiEntityManager.createQuery(
				"SELECT ?S WHERE { " +
				"{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_OWL + "ObjectProperty> } " +
				" UNION " +
				"{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_OWL + "AnnotationProperty> } " +
				" UNION " +
				"{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_OWL + "DatatypeProperty> } " +
				" UNION " +
				"{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_RDFS + "Property> } }", 
				SPARQL, KiWiProperty.class);
		return query.getResultList();
	}
	
	/**
	 * List all properties that are defined in the KiWi system.
	 * 
	 * @return a list of KiWiProperty objects representing the properties in the KiWi system
	 * @see kiwi.api.ontology.OntologyService#listProperties()
	 */
	public List<KiWiProperty> listDatatypeProperties() {
		Query query = kiwiEntityManager.createQuery(
				"SELECT ?S WHERE { " +
				"{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_OWL + "AnnotationProperty> } " +
				" UNION " +
				"{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_OWL + "DatatypeProperty> } }", 
				SPARQL, KiWiProperty.class);
		return query.getResultList();
	}
	
	

	/**
	 * List the properties that currently exist between a given subject and object. 
	 * @param subject the subject of the relation
	 * @param object the object of the relation
	 * @return the list of properties that are used in relations between subject and object
	 */
	@Override
	public List<KiWiProperty> listExistingProperties(KiWiResource subject, KiWiResource object) {
		Query query = kiwiEntityManager.createQuery(
				"SELECT ?P WHERE { " + subject.getSeRQLID() + " ?P " + object.getSeRQLID() + " } ",
				SPARQL, KiWiProperty.class);
		return query.getResultList();
	}

	/**
	 * List the properties that are applicable between a given subject and object. Whether a
	 * property is applicable is decided based on the domain and range of the property: if one of 
	 * the types of the subject is in the range, and one of the types of the object is in the 
	 * domain, the property is applicable.
	 * 
	 * @param subject the subject to check
	 * @param object the object to check
	 * @return a list of applicable properties
	 * @see kiwi.api.ontology.OntologyService#listApplicableProperties(kiwi.model.content.ContentItem, kiwi.model.content.ContentItem)
	 */
	public List<KiWiProperty> listApplicableProperties(KiWiResource subject, KiWiResource object) {
		String sparqlStr = "SELECT ?S WHERE { " +
		"{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_OWL + "ObjectProperty> . " +
		"  ?S <" + Constants.NS_RDFS + "domain> ?D . " +
		"  ?S <" + Constants.NS_RDFS + "range> ?R . " +
		"  "+ subject.getSeRQLID()+" <" + Constants.NS_RDF + "type> ?D . " +
		"  "+ object.getSeRQLID()+" <" + Constants.NS_RDF + "type> ?R " +				
		"} } ";
		log.info("listApplicableProperties sparql: ' #0'", sparqlStr);
		Query query = kiwiEntityManager.createQuery(
				sparqlStr, 
				SPARQL, KiWiProperty.class);
		return query.getResultList();
	}

	/**
	 * List the datatype properties that are applicable for a given subject. Whether a property is
	 * applicable is decided based on the range of the property: if one of the types of the subject
	 * is in the range, the property is applicable. 
	 * <p>
	 * This method currently ignores the domain of the property. In the future, OntologyService
	 * could provide a method that takes into account the actual datatype.
	 * 
	 * @param subject the subject for which the properties are supposed to be listed
	 * @return a list of KiWiProperty facades that are applicable
	 */
	public List<KiWiProperty> listApplicableDataTypeProperties(KiWiResource subject) {
		Query query = kiwiEntityManager.createQuery(
				"SELECT ?S WHERE { " +
				"{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_OWL + "DatatypeProperty> . " +
				"  ?S <" + Constants.NS_RDFS + "domain> ?D . " +
				"  "+ subject.getSeRQLID()+" <" + Constants.NS_RDF + "type> ?D  " +
				"} } ", 
				SPARQL, KiWiProperty.class);
		return query.getResultList();
	}

	
	/**
	 * Provide autocompletion of content items that are of type "owl:Class" or "rdfs:Class" and 
	 * whose title starts with "prefix". Autocompletion is delegated to the SOLR search service.
	 * @param prefix
	 * @return
	 */
	public List<String> autocomplete(String prefix) {
		if(prefix.length() >= 2) {
		
			KiWiSearchCriteria crit = new KiWiSearchCriteria();
			StringBuilder qString = new StringBuilder();
			
			// add prefix to query string
			qString.append("title:"+prefix+"*");
			qString.append(" ");
			
			// add (type:kiwi:Tag OR type:skos:Concept)
			qString.append("(");
			qString.append("type:\"uri::"+Constants.NS_OWL+"Class\"");
			qString.append(" OR ");
			qString.append("type:\"uri::"+Constants.NS_RDFS+"Class\"");
			qString.append(")");
			
			crit.setSolrSearchString(qString.toString());
			
			SolrQuery query = solrService.buildSolrQuery(crit);
			query.setStart(0);
			query.setRows(Integer.MAX_VALUE);
			query.setSortField("title_id", ORDER.asc);
			
			query.setFields("title");
			
			QueryResponse rsp = solrService.search(query);
			
			List<String> result = new LinkedList<String>();
			if(rsp == null){
				log.error("autocomplete: solr delivered null for the query #0", 
						qString.toString());
				return Collections.emptyList();
			}
			SolrDocumentList docs = rsp.getResults();
			
			
			for(SolrDocument doc : docs) {
				result.add(doc.getFieldValue("title").toString());
			}
			
			return result;
		} else {
			return Collections.emptyList();
		}
	}
	
	public List<KiWiProperty> listDatatypePropertiesByName( String name ) {
		Query query = kiwiEntityManager.createQuery(
				"SELECT ?S WHERE { " +
				"?S <"+Constants.NS_KIWI_CORE+"title> \""+name+"\" . " +
				"{{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_OWL + "AnnotationProperty> } " +
				" UNION " +
				"{ ?S <" + Constants.NS_RDF + "type> <" + Constants.NS_OWL + "DatatypeProperty> }} "+
				"}", 
				SPARQL, KiWiProperty.class);
		return query.getResultList();
	}

	
}
