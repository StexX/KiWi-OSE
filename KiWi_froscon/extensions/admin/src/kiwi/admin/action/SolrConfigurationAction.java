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
package kiwi.admin.action;

import java.io.Serializable;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.ontology.OntologyService;
import kiwi.api.search.SemanticIndexingService;
import kiwi.api.search.SolrService;
import kiwi.api.triplestore.TripleStore;
import kiwi.config.Configuration;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.KiWiProperty;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * A component for performing various SOLR-related maintenance activities.
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.admin.solrConfigurationAction")
@Scope(ScopeType.PAGE)
public class SolrConfigurationAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private SolrService solrService;
	
	@In("kiwi.core.semanticIndexingService")
	private SemanticIndexingService semanticIndexingService;
	
	@In
	private OntologyService ontologyService;
	
	@In
	private TripleStore tripleStore;
	
	@In
	private FacesMessages facesMessages;
	
	private String solrServer;
	
	private String solrHome;
	
	private KiWiProperty selectedFacet;
	
	private List<KiWiUriResource> rdfFacets;
	
	
	public void save() {
		configurationService.setConfiguration("kiwi.solr.server", solrServer);
		configurationService.setConfiguration("kiwi.solr.home", solrHome);
		
		facesMessages.add("Configuration saved successfully. Changes will only take effect on restart.");
	}
	
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void rebuildSearchIndex() {
		solrService.rebuildIndex();
		
		facesMessages.add("Scheduled background rebuilding of search index; search may not function properly while indexing is running");
//		try {
//			Transaction.instance().setTransactionTimeout(60000);
//		} catch (SystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		long start = System.currentTimeMillis();
//		Collection<ContentItem> items = contentItemService.getContentItems();
//		solrService.persistAll(items);
//		facesMessages.add("Successfully rebuilt search index from database (#0 documents indexed in #1 seconds).",items.size(),(System.currentTimeMillis()-start)/1000);
	}
	
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void rebuildSemanticVectors() {
		//long start = System.currentTimeMillis();
		
		semanticIndexingService.reIndex();
		
		facesMessages.add("Scheduled background rebuilding of semantic index; recommendations may not function properly while indexing is running");
		//facesMessages.add("Successfully rebuilt semantic vectors from search index (#0 seconds).",(System.currentTimeMillis()-start)/1000);
	}
	
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)	
	public void updateCEQValues() {
		solrService.updateCEQValues();
		facesMessages.add("Scheduled background refreshing of CEQ values; recommendations may not function properly while indexing is running");
		//facesMessages.add("Successfully rebuilt semantic vectors from search index (#0 seconds).",(System.currentTimeMillis()-start)/1000);
	}
	
	public void optimize() {
		long start = System.currentTimeMillis();
		solrService.optimize();
		facesMessages.add("Successfully optimized search index (#0 ms).",System.currentTimeMillis()-start);
	}

	public String getSolrServer() {
		if(solrServer==null || solrServer.equals("")) {
			solrServer = configurationService.getConfiguration("kiwi.solr.server","http://localhost:8080/SOLR").getStringValue();
		}
		return solrServer;
	}

	public void setSolrServer(String solrServer) {
		this.solrServer = solrServer;
	}

	public String getSolrHome() {
		if(solrHome==null || solrHome.equals("")) {
			solrHome = configurationService.getConfiguration("kiwi.solr.home").getStringValue();
		}
		return solrHome;
	}

	public void setSolrHome(String solrHome) {
		this.solrHome = solrHome;
	}

	public List<KiWiUriResource> getRdfFacets() {
		if(rdfFacets == null) {
			rdfFacets = new LinkedList<KiWiUriResource>();
			for(String prop_uri : configurationService.getConfiguration("kiwi.solr.facets").getListValue()) {
				rdfFacets.add(tripleStore.createUriResource(prop_uri));
			}
		}
		return rdfFacets;
	}

	public void setRdfFacets(List<KiWiUriResource> rdfFacets) {
		this.rdfFacets = rdfFacets;
	}
	
	List<KiWiProperty> availableFacets = null;
	
	public List<KiWiProperty> getAvailableFacets() {
		if(availableFacets == null) {
			availableFacets = new LinkedList<KiWiProperty>();
			
			availableFacets.addAll(ontologyService.listDatatypeProperties());
			
			Collections.sort(availableFacets, new Comparator<KiWiProperty>() {
				@Override
				public int compare(KiWiProperty o1, KiWiProperty o2) {
					return Collator.getInstance().compare(o1.getResource().getLabel(), o2.getResource().getLabel());
				}
				
			});
		}
		return availableFacets;
	}
	
	
	public KiWiProperty getSelectedFacet() {
		return selectedFacet;
	}

	public void setSelectedFacet(KiWiProperty selectedFacet) {
		this.selectedFacet = selectedFacet;
	}

	public void addFacet() {
		if(this.selectedFacet != null && !rdfFacets.contains(this.selectedFacet.getResource())) {
			log.info("adding RDF search facet #0", this.selectedFacet.getResource().getLabel());
			rdfFacets.add((KiWiUriResource)this.selectedFacet.getResource());
		}
	}
	
	public void removeFacet(KiWiUriResource facet) {
		log.info("removing RDF search facet #0", facet.getLabel());
		rdfFacets.remove(facet);
	}
	
	
	public void saveFacets() {
		log.info("saving facet configuration");
		LinkedList<String> results = new LinkedList<String>();
		for(KiWiUriResource r : rdfFacets) {
			results.add(r.getUri());
		}
		Configuration conf = configurationService.getConfiguration("kiwi.solr.facets");
		
		conf.setListValue(results);
		
		configurationService.setConfiguration(conf);

		facesMessages.add("RDF Facet configuration saved successfully.");
	
	}
}
