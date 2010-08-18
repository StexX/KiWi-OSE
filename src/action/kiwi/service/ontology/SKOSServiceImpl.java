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
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.ontology.SKOSServiceLocal;
import kiwi.api.ontology.SKOSServiceRemote;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Sebastian Schaffert
 *
 */
@Name("skosService")
@Scope(ScopeType.STATELESS)
@Stateless
public class SKOSServiceImpl implements SKOSServiceLocal, SKOSServiceRemote {

	
	@In
	private EntityManager entityManager;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
    @In(create = true)
    private ContentItemService contentItemService;
    
    @In
    private SolrService solrService;
	
	@Logger
	private Log log;
	
	/* (non-Javadoc)
	 * @see kiwi.api.ontology.SKOSService#getTopConcepts()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SKOSConcept> getTopConcepts() {
		Query q = entityManager.createNamedQuery("tripleStore.objectByProperty");
		q.setParameter("prop_uri", Constants.NS_SKOS + "hasTopConcept");
		q.setHint("org.hibernate.cacheable", true);
		
		List<ContentItem> result = (List<ContentItem>)q.getResultList();
		
		if (result.size() != 0){			
			Set<ContentItem> setItems = new LinkedHashSet<ContentItem>(result);
			result.clear();
			result.addAll(setItems);
			log.info("result.size: #0, data loaded", result.size());	
			List<SKOSConcept> ls = kiwiEntityManager.createFacadeList(result, SKOSConcept.class, false);			
			log.info("test1");
			
			
			Collections.sort(ls, new ConceptComparator());
			log.info("test2");
			
			return ls;
		}
		
		else {
			log.info("No data loaded");
			return Collections.EMPTY_LIST;
		}
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.ontology.SKOSService#getConcept(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public SKOSConcept getConcept(String conceptName) {
		String qString = "select ci from ContentItem ci, KiWiTriple con" +
				" where ci.resource.id = con.subject.id" +
				" and con.property.uri = :concept" + 
				" and con.object.content = :title";
		String concept = Constants.NS_SKOS + "prefLabel";
		Query q = entityManager.createQuery(qString);
		q.setParameter("concept", concept);
		q.setParameter("title", conceptName);
		List<ContentItem> result = q.getResultList();
		if (result.size() != 0){
			return kiwiEntityManager.createFacade(result.get(0), SKOSConcept.class);
		} else return null;
	}

	
	/* (non-Javadoc)
	 * @see kiwi.api.ontology.SKOSService#getConceptByLabel(java.lang.String)
	 */
	public List<SKOSConcept> getConceptByLabel(String label){
		List<SKOSConcept> l = new ArrayList<SKOSConcept>();
		KiWiSearchCriteria c = new KiWiSearchCriteria();
		c.setSolrSearchString("type:\"uri::"+ Constants.NS_SKOS+"Concept\" "+label);
		c.setLimit(-1);
		for (SearchResult r : solrService.search(c).getResults()) {
			 SKOSConcept concept = kiwiEntityManager.createFacade(r.getItem(), SKOSConcept.class);
			 l.add(concept);
		}
		return l;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.ontology.SKOSService#createSkosConcept(java.lang.String, java.util.Locale)
	 */
	public SKOSConcept createSkosConcept(String newConceptTitle, Locale loc){
		ContentItem c = contentItemService.createContentItem();
		contentItemService.updateTitle(c, newConceptTitle);
		SKOSConcept sk = kiwiEntityManager.createFacade(c, SKOSConcept.class);
		KiWiResource skr = sk.getResource();
		try {
			skr.setProperty("http://www.w3.org/2004/02/skos/core#prefLabel",newConceptTitle,loc);
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
		}
		return sk;
	}
}
