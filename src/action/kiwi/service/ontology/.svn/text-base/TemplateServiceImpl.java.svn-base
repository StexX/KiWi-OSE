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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.ontology.TemplateServiceLocal;
import kiwi.api.ontology.TemplateServiceRemote;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.XPathContext;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * Retrieves templates, template fields and template instances that have 
 * been generated on top of ContentItems.
 * A template is a ContentItem that contains TextContent with RDFa annotated fields.
 * 
 * @author Stephanie Stroka (stephanie.stroka@salzburgresearch.at)
 *
 */
@Name("templateService")
@Scope(ScopeType.STATELESS)
@Stateless
public class TemplateServiceImpl implements TemplateServiceLocal, TemplateServiceRemote {

	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@Logger
	private Log log;
	
	/** 
	 * returns all available templates (= ContentItems of rdf-type Template) 
	 */
	public LinkedList<ContentItem> getTemplates() {
		LinkedList<ContentItem> templates = null;
		String q = "SELECT ?s " +
				"WHERE { " +
				"?s " +
				"<" + Constants.NS_RDF + "type> " +
				"<" + Constants.NS_KIWI_CORE + "Template> ." +
				" }";
		Query query = 
			kiwiEntityManager.createQuery(q, KiWiQueryLanguage.SPARQL, ContentItem.class);
		List<ContentItem> res = query.getResultList();
		if(res != null && res.size() > 0) {
			templates = new LinkedList<ContentItem>(res); 
		}
		return templates;
	}
	
	/**
	 * TODO: We should somehow check that composed 
	 * 	     contents/templates do not contain any cycles!
	 * @param n
	 * @param properties
	 * @param triples
	 * @param kiwiResource
	 * @return
	 */
	private Set<KiWiTriple> recKiWiComponentSearch(Node n, Map<Element,Set<Element>> properties, Set<KiWiTriple> triples, KiWiResource kiwiResource) {
		
		EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
		TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
		ContentItemService contentItemService = (ContentItemService) Component.getInstance("contentItemService");
		try {
			n.detach();
		} catch (nu.xom.WellformednessException e) {
			// do nothing, just ignore it
		}
		
		/* get all inside elements with attribute <i>target</i> */	
		// TODO What is this for?!
		XPathContext context = new XPathContext("kiwi", Constants.NS_KIWI_HTML);
		Nodes kiwiComponents = n.query("//*[@kiwi:component]", context);
		log.info("Found Nodes for [@kiwi:component]: " + kiwiComponents.size());
		for(int i=0; i<kiwiComponents.size(); i++) {
			if(!kiwiComponents.get(i).equals(n)) {
				Element kiwiCompElement = (Element)kiwiComponents.get(i);
				
				ContentItem kiwiComponentCI = contentItemService.getContentItemByKiwiId(
						kiwiCompElement.getAttributeValue("component",Constants.NS_KIWI_HTML));
				
				Query q = entityManager.createNamedQuery("tripleStore.tripleBySP2");
				q.setParameter("subject", kiwiResource);
				q.setParameter("property_uri", kiwiCompElement.getAttributeValue("rel").split("::")[1]);
				List<KiWiTriple> itTripl = q.getResultList();
				
				if(itTripl.size() != 1) {
					log.error("Triple does not exist or exists more then once");
				} else {
					KiWiTriple t = itTripl.get(0);
					triples.add(t);
				}
				
				Document doc = kiwiComponentCI.getTextContent().getXmlDocument();
				recKiWiComponentSearch(doc.getRootElement(), properties, triples, kiwiComponentCI.getResource());
			}
		}
		
		/* get all inside elements with attribute <i>property</i> */
		Nodes propNodes = n.query("//*[@property]");
		log.info("Found Nodes for [@property]: " + propNodes.size());
		for(int j=0; j<propNodes.size(); j++) {
			/* the property element */
			Element p = (Element) propNodes.get(j);
			
			Query q = entityManager.createNamedQuery("tripleStore.tripleBySP2");
			q.setParameter("subject", kiwiResource);
			q.setParameter("property_uri", p.getAttributeValue("property").split("::")[1]);
			List<KiWiTriple> itTripl = q.getResultList();
			
			if(itTripl.size() != 1) {
				log.error("Triple does not exist or exists more then once");
			} else {
				KiWiTriple t = itTripl.get(0);
				triples.add(t);
			}
		}
		
		/* get the elements with attribute <i>rel</i> (resource-relations) */
		Nodes relNodes = n.query("//*[@rel]");
		log.info("Found Nodes for [@rel]: " + relNodes.size());
		for (int k = 0; k < relNodes.size(); k++) {
			/* the relation element */
			Element p = (Element) relNodes.get(k);
			
			Query q = entityManager.createNamedQuery("tripleStore.tripleBySP2");
			q.setParameter("subject", kiwiResource);
			q.setParameter("property_uri", p.getAttributeValue("rel").split("::")[1]);
			List<KiWiTriple> itTripl = q.getResultList();
			
			if(itTripl.size() != 1) {
				log.error("Triple does not exist or exists more then once");
			} else {
				KiWiTriple t = itTripl.get(0);
				triples.add(t);
			}
		}
		
		return triples;
	}
	
	/**
	 * Returns fields of template instances in form of KiWiTriples.
	 * Therefore, this method calls a recursive function that 
	 * checks the nodes of the XHTML-TextContent for the RDFa 
	 * fields 'property', 'rel' and 'kiwi:component'.
	 */
	public ArrayList<KiWiTriple> getTemplateFields(String url) {
		Set<KiWiTriple> fields = new HashSet<KiWiTriple>();
		ContentItemService cis = (ContentItemService) 
			Component.getInstance("contentItemService");
		ContentItem template = cis.getContentItemByUri(url);
		Document doc = template.getTextContent().getXmlDocument();
		
		fields = recKiWiComponentSearch(
				doc.getRootElement(), 
				new HashMap<Element,Set<Element>>(), 
				new HashSet<KiWiTriple>(),
				template.getResource());
		return new ArrayList<KiWiTriple>(fields);
	}
	
	private Set<KiWiTriple> recKiWiIterationSearch(Node n, 
			Map<Element,Set<Element>> properties, 
			Set<KiWiTriple> triples, KiWiResource kiwiResource) {
		EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
		TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
		ContentItemService contentItemService = 
			(ContentItemService) Component.getInstance("contentItemService");
		try {
			n.detach();
		} catch (nu.xom.WellformednessException e) {
			// do nothing, just ignore it
		}
		
		/* get all inside elements with attribute <i>target</i> */
		XPathContext context = new XPathContext("kiwi", Constants.NS_KIWI_CORE);
		Nodes kiwiComponents = n.query("//*[@iteratedInclude]", context);
		for(int i=0; i<kiwiComponents.size(); i++) {
			if(!kiwiComponents.get(i).equals(n)) {
				Element kiwiCompElement = (Element)kiwiComponents.get(i);
				
				ContentItem kiwiComponentCI = contentItemService.getContentItemByUri(
						kiwiCompElement.getAttributeValue("iteratedInclude",Constants.NS_KIWI_CORE));
				Document doc = kiwiComponentCI.getTextContent().getXmlDocument();
				recKiWiComponentSearch(doc.getRootElement(), properties, triples, kiwiComponentCI.getResource());
			}
		}
		
		Element e = (Element) n;

		/* get all inside elements with attribute <i>property</i> */
		Nodes propNodes = n.query("//*[@property]");
		for(int j=0; j<propNodes.size(); j++) {
			/* the property element */
			Element p = (Element) propNodes.get(j);
			
			/* build a query string that returns the triple for the 
			 * <i>target</i> or <i>kiwiResource</i> as subject, the 
			 * attribute value for the <i>property</i> attribute as 
			 * a property, and the inside value as a literal or, if 
			 * there exists a <i>target</i> relationship, an object */
				
				KiWiResource subject = null;
								
				if(e.getAttribute("iteratedInclude",Constants.NS_KIWI_CORE) != null) {
					
					Query q = entityManager.createNamedQuery("tripleStore.tripleBySP2");
					q.setParameter("subject", kiwiResource.getKiwiIdentifier().split("::")[1]);
					q.setParameter("property_uri", e.getAttributeValue("rel").split("::")[1]);
					List<KiWiTriple> itTripl = q.getResultList();
					
					if(itTripl.size() != 1) {
						log.error("Triple does not exist or exists more then once");
					} else {
						KiWiTriple t = itTripl.get(0);
						triples.add(t);
					}
					
					subject = tripleStore.createUriResource(e.getAttributeValue("iteratedInclude",Constants.NS_KIWI_CORE));
				} else {
					subject = kiwiResource;
				}
				
				Query q = entityManager.createNamedQuery("tripleStore.tripleBySP2");
				q.setParameter("subject", subject);
				q.setParameter("property_uri", p.getAttributeValue("property").split("::")[1]);
				List<KiWiTriple> itTripl = q.getResultList();
				
				if(itTripl.size() != 1) {
					log.error("Triple does not exist or exists more then once");
				} else {
					KiWiTriple t = itTripl.get(0);
					triples.add(t);
				}
			}
		return triples;
	}
	
	/**
	 * returns all template instances of a specific template
	 */
	@Override
	public LinkedList<ContentItem> getTemplateInstances(String title) {
		LinkedList<ContentItem> instances = null;
		String q = "SELECT ?s " +
			"WHERE { " +
			"?s <" + Constants.NS_RDF + "type> <" + Constants.NS_KIWI_CORE + "FromTemplate> . " +
			"?s <" + Constants.NS_KIWI_CORE + "instancesTemplate> ?template . " +
			"?template <" + Constants.NS_RDFS + "label> " + "\"" + title + "\" . }";
		Query query = 
			kiwiEntityManager.createQuery(q, KiWiQueryLanguage.SPARQL, ContentItem.class);
		List<ContentItem> res = query.getResultList();
		if(res != null && res.size() > 0) {
			instances = new LinkedList<ContentItem>(res); 
		}
		return instances;
	}

	@Override
	public void applyTemplate(ContentItem instance, ContentItem template) {
		TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
		ContentItemService contentItemService = 
			(ContentItemService) Component.getInstance("contentItemService");
		
		contentItemService.updateTextContentItem(instance, template.getTextContent().copyXmlDocument());
		
		instance.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE + "FromTemplate"));
		try {
			instance.getResource().addOutgoingNode("<" + Constants.NS_KIWI_CORE + "instancesTemplate>", template.getResource());
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
		}
			
		contentItemService.saveContentItem(instance);
	}

	@Override
	public void applyTemplate(ContentItem instance) {				
		log.info("instance: #0", instance.getResource().getKiwiIdentifier());
		ContentItem template = null;
		
		String q = "SELECT ?t " +
		"WHERE { " +
		"?t <" + Constants.NS_RDF + "type> <" + Constants.NS_KIWI_CORE + "Template> . " +
		"?t <" + Constants.NS_RDF + "type> ?type . " +
		instance.getResource().getSeRQLID() + " <" + Constants.NS_RDF + "type> ?type . " +
		" FILTER (str(?type)!= \"" + Constants.NS_KIWI_CORE + "ContentItem\" ) } ";
		Query query = 
			kiwiEntityManager.createQuery(q, KiWiQueryLanguage.SPARQL, ContentItem.class);
		
		List<ContentItem> res = query.getResultList();
		if(res != null && res.size() > 0) {
			log.info("#0 possible templates", res.size());
			template = res.get(0);
		}
		
		if (template != null) {			
			log.info("using template #0", template.getKiwiIdentifier());
		
			applyTemplate (instance, template);
		}
		else {
			log.info("no suitable template for #0", instance.getKiwiIdentifier());
		}
	}
}
