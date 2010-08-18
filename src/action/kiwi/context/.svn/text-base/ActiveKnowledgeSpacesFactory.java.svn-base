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
package kiwi.context;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.log.Log;

/**
 * The activeKnowledgeSpacesFactory maintains a list of all knowledge spaces that are
 * currently used by the user. Knowledge Spaces are content items whose resources are
 * used as context identifiers of named graphs in the triple store. When querying
 * the triple store, only the activeKnowledgeSpaces are considered. When modifying
 * the triple store, all changes are performed in the "currentKnowledgeSpace". The user
 * has the ability to select both, the activeKnowledgeSpaces and the currentKnowledgeSpace,
 * provided he has the access rights to these knowledge spaces.
 * <p>
 * This factory component maintains two session-scoped elements:
 * <ul>
 * <li>the list of all active knowledge spaces for querying (activeKnowledgeSpaces); 
 *     the list is scoped to the session</li>
 * <li>the currently selected knowledge space for editing (currentKnowledgeSpace);
 *     the list is scoped to the conversation </li>
 * </ul>
 * 
 * @author Sebastian Schaffert
 * @see CurrentKnowledgeSpaceFactory
 */
@Name("activeKnowledgeSpacesFactory")
@Scope(ScopeType.SESSION)
public class ActiveKnowledgeSpacesFactory implements Serializable {

	/**
	 * The serial number of this serializable.
	 */
	private static final long serialVersionUID = -8193435650172242296L;

	
	/**
	 * Inject the seam logger for debugging and information messages.
	 */
	@Logger
	private Log log;
	
	/**
	 * The set of knowledge spaces active in this session for querying.
	 */
	@Out(value = "activeKnowledgeSpaces", required=false)
	private TreeSet<ContentItem> activeKnowledgeSpaces;

	/**
	 * The data model of all knowledge spaces.
	 */
	@DataModel("allKnowledgeSpaces")
	private List<KnowledgeSpaceBean> allKnowledgeSpaces;
	
	/**
	 * Initialise the collection of active knowledge spaces by iterating over the datamodel
	 * and selecting those content items that are marked as "active".
	 */
	@Factory("activeKnowledgeSpaces")
	public void initActiveKnowledgeSpaces() {
		initAllKnowledgeSpaces(); // ensure that allKnowledgeSpaces is initialised
		
		if(activeKnowledgeSpaces == null) {
			activeKnowledgeSpaces = new TreeSet<ContentItem>();
			
		}
		activeKnowledgeSpaces.clear();
		for(KnowledgeSpaceBean bean : allKnowledgeSpaces) {
			if(bean.isActive()) {
				activeKnowledgeSpaces.add(bean.getContentItem());
			}
		}
		log.debug("currently #0 active knowledge spaces out of #1 overall", activeKnowledgeSpaces.size(),allKnowledgeSpaces.size());
	}

	/**
	 * Initialise the list of all knowledge spaces by listing all content items that have
	 * the type "kiwi:KnowledgeSpace".
	 * 
	 * TODO: we'd rather do this by using a separate Entity called "KnowledgeSpace"
	 */
	@Factory("allKnowledgeSpaces")
	public void initAllKnowledgeSpaces() {
		if( allKnowledgeSpaces == null) {
			allKnowledgeSpaces = new LinkedList<KnowledgeSpaceBean>();
		
			/*
			Query q = entityManager.createNamedQuery("contentItemService.byType");
			q.setParameter("type", tripleStore.createUriResource(Constants.NS_KIWI_CORE+"KnowledgeSpace"));
			
			try {
				for(ContentItem ci : (List<ContentItem>)q.getResultList()) {
					allKnowledgeSpaces.add(new KnowledgeSpaceBean(ci));
				}
			} catch(NoResultException ex) {
				log.warn("no knowledge spaces available in this system");
			}
			*/
		}
	}
}
