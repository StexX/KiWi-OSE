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
package interedu.service.collector;

import interedu.action.tagging.InterEduTaggingAction;
import interedu.api.dataimport.InterEduArtikelFacade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

@Name("interedu.artikelCollector")
@Scope(ScopeType.SESSION)
//@Transactional
@AutoCreate
public class ArtikelCollectorService implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private static Log log;
	
	private long currentId;
	
	private Map<Long, InterEduArtikelFacade> articleCollection;
	
	@In
	private KiWiEntityManager kiwiEntityManager;

	@In
	private EntityManager entityManager;
	
    @In(value="interedu.InterEduTaggingAction", create=true)
    private InterEduTaggingAction interEduTaggingAction;
	
	@Create
	public void create() {
		articleCollection = new HashMap<Long, InterEduArtikelFacade>();
	}
	
	public long getCurrentId() {
		return currentId;
	}

	public void setCurrentId(long currentId) {
		this.currentId = currentId;
	}

	public List<InterEduArtikelFacade> getArticleCollection() {
		List<InterEduArtikelFacade> l = new LinkedList<InterEduArtikelFacade>();
		Iterator<InterEduArtikelFacade> i = articleCollection.values().iterator();
		while( i.hasNext() ) {
			l.add(i.next());
		}
		return l;
	}
	
	public boolean isArticleCollectionEmpty() {
		return articleCollection.isEmpty();
	}
	
	public boolean isSelected(long id) {
		return articleCollection.containsKey(id);
	}
	
	public void select() {
		log.info("select #0",currentId);
		InterEduArtikelFacade a = kiwiEntityManager.createFacade( entityManager.find( ContentItem.class, currentId) , InterEduArtikelFacade.class);
		if( !articleCollection.containsKey(a.getId()) ) {
			articleCollection.put(a.getId(), a);
		}
		log.info("list contains now #0 elements", articleCollection.size());
	}
	
	public void unselect(InterEduArtikelFacade a) {
		articleCollection.remove(a.getId());
	}
	
	//**** actions
	public void clearAll() {
		articleCollection = new HashMap<Long, InterEduArtikelFacade>();
	}
	
	private String tags;
	
	public void tagAll() {
		interEduTaggingAction.setTagLabel( tags );
		interEduTaggingAction.addTagTo(getArticleCollection());
		log.info("added multitags: #0", tags);
		tags = "";
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTags() {
		return tags;
	}

}
