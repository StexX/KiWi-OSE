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
package kiwi.service.fragment;

import java.util.Collection;
import java.util.LinkedList;

import javax.ejb.Stateless;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.fragment.FragmentFacade;
import kiwi.api.fragment.FragmentServiceLocal;
import kiwi.api.fragment.FragmentServiceRemote;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItemI;
import kiwi.model.content.TextFragment;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

/**
 * @author Marek Schmidt
 *
 */
@Stateless
@Name("fragmentService")
@Scope(ScopeType.STATELESS)
@AutoCreate
//@Transactional
public class FragmentServiceImpl implements FragmentServiceLocal,
		FragmentServiceRemote {
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private TripleStore tripleStore;
	
	@In
	private ContentItemService contentItemService;
	
	@Logger
	private Log log;
	
	private KiWiUriResource createFragmentResource (String fragment_id) {
		if (fragment_id.startsWith("uri::")){
			return tripleStore.createUriResource(fragment_id.substring("uri::".length()));
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.fragment.FragmentService#createFragment(kiwi.model.content.ContentItemI, java.lang.String, java.lang.Class)
	 */
	@Override
	public <C extends FragmentFacade> C createFragment(
			ContentItemI containing_ci, String fragment_id, Class<C> type) {
				
		KiWiUriResource resource = createFragmentResource (fragment_id);
		// Create the facade
    	log.debug("Creating fragment #0", resource.getKiwiIdentifier());
		C ff = kiwiEntityManager.createFacade(resource.getContentItem(), type);
		ff.getDelegate().setDeleted(false);
    	ff.setContainingContentItem(containing_ci.getDelegate());
    	ff.addType(tripleStore.createUriResource(Constants.NS_KIWI_SPECIAL + "Fragment"));
		
    	for (KiWiTriple t : ff.getResource().listOutgoing()) {
    		log.debug("triple: #0", t.toString());
    	}
    	
		return ff;
	}
	
	@Override
	public <C extends FragmentFacade> C createFragment(
			ContentItemI containing_ci, Class<C> type) {

		C ff = kiwiEntityManager.createFacade(contentItemService.createContentItem(), type);
    	ff.setContainingContentItem(containing_ci.getDelegate());
    	ff.addType(tripleStore.createUriResource(Constants.NS_KIWI_SPECIAL + "Fragment"));
		
    	for (KiWiTriple t : ff.getResource().listOutgoing()) {
    		log.debug("triple: #0", t.toString());
    	}
    	
		return ff;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.fragment.FragmentService#getContentItemFragment(kiwi.model.content.ContentItemI, java.lang.String)
	 */
	@Override
	public <C extends FragmentFacade> C getContentItemFragment(ContentItemI containing_ci,
			String fragment_id, Class<C> type) {

		if (fragment_id.startsWith("uri::")) {
			String uri = fragment_id.substring("uri::".length());
			C ff = kiwiEntityManager.find(type, uri);
			return ff;
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.fragment.FragmentService#getContentItemFragments(kiwi.model.content.ContentItemI)
	 */
	@Override
	public <C extends FragmentFacade> Collection<C> getContentItemFragments(
			ContentItemI containing_ci, Class<C> type) {
		
		Collection<C> ret = new LinkedList<C> ();
		Collection<KiWiTriple> triples;
		try {
			triples = containing_ci.getResource().listIncoming("http://www.kiwi-project.eu/kiwi/special/fragmentOf");
		
    	
			for (KiWiTriple triple : triples) {
				KiWiResource subject = triple.getSubject();
				if (subject.isUriResource()) {
					KiWiUriResource subject_uri = (KiWiUriResource)triple.getSubject();
					C ff = kiwiEntityManager.find(type, subject_uri.getUri());
					if (ff != null) {
						ret.add(ff);
					}
				}
			}
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
		}
		
		return ret;
		
		/*Query query = kiwiEntityManager.createQuery("SELECT ?B WHERE { ?B <http://www.kiwi-project.eu/kiwi/special/fragmentOf> <" + ((KiWiUriResource)containing_ci.getResource()).getUri() + "> . }", KiWiQueryLanguage.SPARQL, type);
    	return query.getResultList();*/
	}

	/* (non-Javadoc)
	 * @see kiwi.api.fragment.FragmentService#getTextFragment(kiwi.api.fragment.FragmentFacade)
	 */
	@Override
	public TextFragment getTextFragment(FragmentFacade ff) {
		if (ff.getContainingContentItem() == null){ 
			log.debug("getTextFragment: containing content item for #0 is null!", ff);
			return null;
		}
		
		return ff.getContainingContentItem().getTextContent().getFragment((KiWiUriResource)ff.getResource());
	}

	/* (non-Javadoc)
	 * @see kiwi.api.fragment.FragmentService#saveFragment(kiwi.api.fragment.FragmentFacade)
	 */
	@Override
	public void saveFragment(FragmentFacade ff) {
		kiwiEntityManager.persist(ff.getDelegate());
	}

	/* (non-Javadoc)
	 * @see kiwi.api.fragment.FragmentService#removeFragment(kiwi.api.fragment.FragmentFacade)
	 */
	@Override
	public void removeFragment(FragmentFacade ff) {
		kiwiEntityManager.remove(ff.getDelegate());
	}

	

}
