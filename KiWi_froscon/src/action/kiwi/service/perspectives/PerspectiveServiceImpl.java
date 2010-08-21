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
package kiwi.service.perspectives;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.perspectives.PerspectiveServiceLocal;
import kiwi.api.perspectives.PerspectiveServiceRemote;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.perspective.Perspective;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

/**
 * PerspectiveServiceImpl
 *
 * @author Sebastian Schaffert
 *
 */
@Stateless
@AutoCreate
@Name("perspectiveService")
@Scope(ScopeType.STATELESS)
//@Transactional
public class PerspectiveServiceImpl implements PerspectiveServiceLocal, PerspectiveServiceRemote {

	@Logger
    private Log log;
	
	@In
	private EntityManager entityManager;
	
	/**
	 * Retrieve a perspective by name and return it. Returns null if the perspective does not exist.
	 * 
	 * @return
	 * @see kiwi.api.perspectives.PerspectiveService#getPerspective(java.lang.String)
	 */
	@Override
	public Perspective getPerspective(String name) {
		Query q = entityManager.createNamedQuery("perspectiveService.perspectiveByName");
		q.setParameter("name",name);
		q.setMaxResults(1);
		q.setHint("org.hibernate.cacheable", true);
		try {
			return (Perspective)q.getSingleResult();
		} catch(NoResultException ex) {
			return null;
		}
	}

	/**
	 * Add a new perspective to the system. The perspective must have a unique name.
	 * 
	 * @param perspective
	 * @see kiwi.api.perspectives.PerspectiveService#addPerspective(kiwi.model.perspective.Perspective)
	 */
	@Override
	public void addPerspective(Perspective perspective) {
		log.debug("adding perspective #0 (id=#1)",perspective.getName(),perspective.getId());
		if(perspective.getId() == null) {
			entityManager.persist(perspective);
		} 
	}

	/**
	 * Update the existing perspective given as parameter. Does nothing if perspective does not exist.
	 * <p>
	 * Note that the Perspective entity is read only; updates need to be performed using HQL
	 * 
	 * @param perspective
	 * @see kiwi.api.perspectives.PerspectiveService#updatePerspective(kiwi.model.perspective.Perspective)
	 */
	@Override
	public void updatePerspective(Perspective perspective) {
		if(perspective.getId() != null) {
			Query q = entityManager.createNamedQuery("perspectiveService.perspectiveUpdate");
			q.setParameter("id", perspective.getId());
			q.setParameter("name", perspective.getName());
			q.setParameter("description", perspective.getDescription());
			q.setParameter("path", perspective.getPath());
			q.executeUpdate();
		}

	}

	/**
	 * Remove the existing perspective given as parameter. Does nothing if perspective does not exist.
	 * <p>
	 * Note that the Perspective entity is read only; updates need to be performed using HQL
	 * 
	 * @param perspective
	 * @see kiwi.api.perspectives.PerspectiveService#removePerspective(kiwi.model.perspective.Perspective)
	 */
	@Override
	public void removePerspective(Perspective perspective) {
		if(perspective.getId() != null) {
			Query q = entityManager.createNamedQuery("perspectiveService.perspectiveDelete");
			q.setParameter("id", perspective.getId());
			q.executeUpdate();
		}

	}

	/**
	 * Return the system default perspective used as standard when creating new content items.
	 * 
	 * @see kiwi.api.perspectives.PerspectiveService#getDefaultPerspective()
	 */
	@Override
	public Perspective getDefaultPerspective() {
		return getPerspective("Default");
	}

	/**
	 * Return a list of all perspectives.
	 * 
	 * @return
	 * @see kiwi.api.perspectives.PerspectiveService#listPerspectives()
	 */
	@Override
	public List<Perspective> listPerspectives() {
		Query q = entityManager.createNamedQuery("perspectiveService.perspectiveList");
		q.setHint("org.hibernate.cacheable", true);
		return q.getResultList();
	}

	/* (non-Javadoc)
	 * @see kiwi.api.perspectives.PerspectiveService#attachPerspective(kiwi.model.content.ContentItem, kiwi.model.perspective.Perspective)
	 */
	@Override
	public void attachPerspective(ContentItem ci, Perspective perspective) {
		// workaround for setup
		if(perspective != null) {
			if(!ci.getPerspectives().contains(perspective)) {
				log.debug("adding perspective #0 to content item #1", perspective.getName(), ci.getKiwiIdentifier());
				ci.getPerspectives().add(perspective);
			} else {
				log.debug("not adding perspective #0 to content item #1, it is already present", perspective.getName(), ci.getKiwiIdentifier());
			}
		} else {
			log.warn("perspective is null, probably perspectives have not yet been initialised");
		}
	}

	
	
}
