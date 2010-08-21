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

import kiwi.api.revision.DeleteContentItemServiceLocal;
import kiwi.api.revision.DeleteContentItemServiceRemote;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.ContentItemDoesNotExistException;
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.DeletionUpdate;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * DeleteContentItemServiceImpl creates, commits, 
 * rolls-back, and reverts DeletionUpdates.
 * 
 * @author Stephanie Stroka 
 * 			(sstroka@salzburgresearch.at)
 *
 */

@Name("deleteContentItemService")
@AutoCreate
@Stateless
@Scope(ScopeType.STATELESS)
public class DeleteContentItemServiceImpl implements Serializable,
		DeleteContentItemServiceLocal, DeleteContentItemServiceRemote {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In
	private TripleStore tripleStore;

	/* (non-Javadoc)
	 * @see kiwi.api.revision.DeleteContentItemService#deletionUpdateContentItem(kiwi.model.content.ContentItem, boolean)
	 */
	public DeletionUpdate deletionUpdateContentItem(ContentItemI itemI, boolean deleted) {
		ContentItem item = itemI.getDelegate();
		DeletionUpdate du = new DeletionUpdate();
		
		du.setContentItem(item);
		// set item as deleted or undeleted (depends on the status)
		item.setDeleted(deleted);
		du.setDeleted(deleted);
		
		KiWiResource resource = item.getResource();
		// TODO: MetadataUpdate instead of removing triples by hand
		for(KiWiTriple ke : resource.listIncoming()) {
			tripleStore.removeTriple(ke);
		}
		for(KiWiTriple ke : resource.listOutgoing()) {
			tripleStore.removeTriple(ke);
		}
		
		tripleStore.persist(item);
		return du;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.revision.DeleteContentItemService#commitUpdate(kiwi.model.revision.DeletionUpdate)
	 */
	public void commitUpdate(CIVersion version) {
		if(version.getDeletionUpdate() != null) {
			EntityManager em = (EntityManager) 
					Component.getInstance("entityManager");
			DeletionUpdate du = version.getDeletionUpdate();
			
			em.persist(du);
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.api.revision.DeleteContentItemService#rollbackUpdate(kiwi.model.revision.DeletionUpdate)
	 */
	public void rollbackUpdate(CIVersion version) {
		DeletionUpdate du = version.getDeletionUpdate();
		
		if(du.isDeleted()) {
			ContentItem item = du.getContentItem();
			item.setDeleted(!du.isDeleted());
			// update the triplestore
			tripleStore.persist(item);
		}
		du = null;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.revision.DeleteContentItemService#undo(kiwi.model.revision.DeletionUpdate)
	 */
	public void restore(CIVersion version) {
		if(version.getDeletionUpdate() != null) {
			DeletionUpdate du = version.getDeletionUpdate();
			
			deletionUpdateContentItem(du.getContentItem(), du.isDeleted());
		}
	}

	/**
	 * This method will do nothing, because the DeletionUpdate is a discrete update.
	 */
	@Override
	public void undo(CIVersion version) throws ContentItemDoesNotExistException {
		try {
			throw new MethodNotSupportedException("DeleteContentItemService.restore(CIVersion version) is not supported");
		} catch (MethodNotSupportedException e) {
			e.printStackTrace();
		}
	}
}
