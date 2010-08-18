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

package kiwi.service.revision;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.revision.CIVersionService;
import kiwi.api.revision.RevisionServiceLocal;
import kiwi.api.revision.RevisionServiceRemote;
import kiwi.model.content.ContentItem;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.Revision;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * The RevisionServiceImpl creates, commits, rolls-back and restores the Revision object for 
 * a transaction. Furthermore, it provides methods to query Revisions, e.g. Revisions since 
 * a certain date.
 * 
 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
 * @param <C>
 *
 */
@Stateless
@Name("revisionService")
@AutoCreate
@Scope(ScopeType.STATELESS)
public class RevisionServiceImpl implements RevisionServiceLocal, RevisionServiceRemote, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In(required=false)
	private User currentUser;
	
	@In(required=false)
	private EntityManager entityManager;
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#createRevision(java.util.List, 
	 * 		java.util.List, java.util.List, java.util.List, java.util.List, 
	 * 		java.util.List, kiwi.model.content.ContentItem)
	 */
	public Revision createRevision() {
		Revision rev = new Revision();
		rev.setUser(currentUser);
		return rev;
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#undo(kiwi.model.revision.Revision)
	 */	
	public boolean restore(Revision rev) {
		
		Log log = Logging.getLog(RevisionServiceImpl.class);
		log.debug("Restore revision");
		
		EntityManager entityManager = (EntityManager) 
			Component.getInstance("entityManager");
		entityManager.refresh(rev);
		
		CIVersionService ciVersionService = (CIVersionService) 
				Component.getInstance("ciVersionService");
		boolean everythingOk = true;
		// the revisions that are affected by that restore action
		List<Revision> affectedRevisions = new LinkedList<Revision>();
		
		Set<CIVersion> revVersions = rev.getContentItemVersions();
		
		// get all content item versions of every content item, 
		// which was affected in that revision
		log.debug("Revision #0 links to #1 CI-versions ", 
				rev, revVersions.size());
		
		if(revVersions.size() == 0)
			affectedRevisions.add(rev);
		else 
			for(CIVersion v : revVersions) {
				List<CIVersion> allVersionsOfCI = v.getRevisedContentItem().getVersions();
				log.debug("The list of versions of ContentItem #0 has the size #1 ", 
						v.getRevisedContentItem(), allVersionsOfCI.size());
				for(CIVersion civ : allVersionsOfCI) {
					if(!affectedRevisions.contains(civ.getRevision())) {
						affectedRevisions.add(civ.getRevision());
						log.debug("Revision #0 of #1 has been added to the list of affected revisions.", 
								civ.getRevision(), civ.getRevisedContentItem().getTitle());
					}
				}
				ciVersionService.restore(v);
			}
		
		if(affectedRevisions.size()-1 < 0) {
			throw new IndexOutOfBoundsException("The revision that should have " +
					"been restored does not contain any contentitem versions.");
		}
		for(int i=affectedRevisions.size()-1; affectedRevisions.get(i) != rev; i--) {
			Revision r = affectedRevisions.get(i);
			entityManager.refresh(r);
			for(CIVersion v : r.getContentItemVersions()) {
				if(!ciVersionService.undo(v)) {
					everythingOk = false;
				}
			}
		}
		return everythingOk;
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#commit(kiwi.model.revision.Revision)
	 */
	public boolean commit(Revision rev) {
//		UpdateConfigurationService updateConfigurationService = (UpdateConfigurationService) Component.
//				getInstance("updateConfigurationService");
//		updateConfigurationService.commitUpdate(rev);
		
		EntityManager em = (EntityManager) Component.getInstance("entityManager");
		em.persist(rev);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#commit(kiwi.model.revision.Revision)
	 */
	public boolean rollback(Revision rev) {
		CIVersionService ciVersionService = (CIVersionService) 
				Component.getInstance("ciVersionService");
		boolean everythingOk = true;
		for(CIVersion civ : rev.getContentItemVersions()) {
			if(!ciVersionService.rollback(civ)) {
				everythingOk = false;
			}
		}
		return everythingOk;
	}
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#getAllRevisions(kiwi.model.content.ContentItem)
	 */
	@Override
	public List<Revision> getAllRevisions(ContentItem ci) {
		Query q = entityManager.createNamedQuery("revisionService.getAllRevisionsByCI");
		q.setParameter("ci", ci);
		
		return q.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#getRevisions(java.util.Date)
	 */
	@Override
	public List<Revision> getRevisions(Date since) {
		return getRevisions(since, new Date());
	}
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#getRevisions(java.util.Date, kiwi.model.content.ContentItem)
	 */
	@Override
	public List<Revision> getRevisions(Date since, ContentItem ci) {
		return getRevisions(since, new Date(), ci);
	}
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#getRevisions(java.util.Date, java.util.Set)
	 */
	@Override
	public List<Revision> getRevisions(Date since, Set<ContentItem> cis) {
		return getRevisions(since, new Date(), cis);
	}
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#getRevisions(java.util.Date, java.util.Date)
	 */
	@Override
	public List<Revision> getRevisions(Date since, Date until) {
		Query q = entityManager.createNamedQuery("revisionService.getAllRevisionByDate");
		q.setParameter("since", since);
		q.setParameter("until", until);
		
		return q.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#getRevisions(java.util.Date, java.util.Date, kiwi.model.content.ContentItem)
	 */
	@Override
	public List<Revision> getRevisions(Date since, Date until, ContentItem ci) {
		Query q = entityManager.createNamedQuery("revisionService.getAllRevisionsByCIandDate");
		q.setParameter("ci", ci);
		q.setParameter("since", since);
		q.setParameter("until", until);
		
		return q.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#getRevisions(java.util.Date, java.util.Date, java.util.Set)
	 */
	@Override
	public List<Revision> getRevisions(Date since, Date until, Set<ContentItem> cis) {
		List<Revision> revisions = new LinkedList<Revision>();
		for(ContentItem ci : cis) {
			revisions.addAll(getRevisions(since, until, ci));
		}
		return revisions;
	}
}
