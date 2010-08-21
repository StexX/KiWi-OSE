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

import javax.ejb.Stateless;
import javax.mail.MethodNotSupportedException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.revision.RenameContentItemServiceLocal;
import kiwi.api.revision.RenameContentItemServiceRemote;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.ContentItemDoesNotExistException;
import kiwi.exception.ContentItemMissingException;
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.RenamingUpdate;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
 *
 */
@Name("renameContentItemService")
@AutoCreate
@Stateless
@Scope(ScopeType.STATELESS)
public class RenameContentItemServiceImpl implements Serializable,
		RenameContentItemServiceLocal, RenameContentItemServiceRemote {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * transaction service, needed to add the update to a transaction resource
	 */
	@In
    private TransactionService transactionService;
	
	@In
	private TripleStore tripleStore;

	@In
	private EntityManager entityManager;
	
	@Logger
	private Log log;
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.RenameContentItemService#renameContentItem(kiwi.model.content.ContentItemI, java.lang.String)
	 */
	public RenamingUpdate renameContentItem(ContentItemI itemI, String title) {
		Log log = Logging.getLog(this.getClass());
		ContentItem item = itemI.getDelegate();
		
		RenamingUpdate ru = new RenamingUpdate();
		ru.setTitle(title);
		item.setTitle(title);
		ru.setContentItem(item);
		try {
			transactionService.getTransactionCIVersionData(item).setRenamingUpdate(ru);
		} catch (ContentItemMissingException e1) {
			e1.printStackTrace();
		}
		tripleStore.persist(item);
		return ru;
	}
	
	


	/* (non-Javadoc)
	 * @see kiwi.api.revision.RenameContentItemService#commitUpdate(kiwi.model.revision.RenamingUpdate)
	 */
	public void commitUpdate(CIVersion version) {
		if(version.getRenamingUpdate() != null) {
			EntityManager em = (EntityManager) Component.getInstance("entityManager");
			RenamingUpdate ru = version.getRenamingUpdate();
			
			em.persist(ru);
		}
	}


	/* (non-Javadoc)
	 * @see kiwi.api.revision.RenameContentItemService#rollbackUpdate(kiwi.model.revision.RenamingUpdate)
	 */
	public void rollbackUpdate(CIVersion version) {
		RenamingUpdate ru = version.getRenamingUpdate();
		
		ContentItem item = ru.getContentItem();
		Query q = entityManager.createNamedQuery("version.lastContentItemTitle");
		q.setMaxResults(1);
		try {
			String title = (String) q.getSingleResult();
			item.setTitle(title);
		} catch (NoResultException e) {
			e.printStackTrace();
		}
		// update the triplestore
		tripleStore.persist(item);
		ru = null;
	}


	/* (non-Javadoc)
	 * @see kiwi.api.revision.RenameContentItemService#undo(kiwi.model.revision.RenamingUpdate)
	 */
	public void restore(CIVersion version) {
		if(version.getRenamingUpdate() != null) {
			RenamingUpdate ru = version.getRenamingUpdate();
			
			String title = ru.getTitle();
			ContentItem item = ru.getContentItem();
			
			renameContentItem(item, title);
		} else {
			Query q = entityManager.createNamedQuery("version.lastContentItemTitle");
			q.setParameter("ci",version.getRevisedContentItem());
			q.setParameter("vid",version.getVersionId());
			q.setMaxResults(1);
			try {
				String title = (String) q.getSingleResult();
				renameContentItem(version.getRevisedContentItem(), title);
			} catch (NoResultException e) {
//				e.printStackTrace();
				log.error("no renamingUpdate found");
			}
		}
	}




	@Override
	public void undo(CIVersion version) throws ContentItemDoesNotExistException {
		try {
			throw new MethodNotSupportedException("UpdateTaggingService.restore(CIVersion version) is not supported");
		} catch (MethodNotSupportedException e) {
			e.printStackTrace();
		}
	}
}
