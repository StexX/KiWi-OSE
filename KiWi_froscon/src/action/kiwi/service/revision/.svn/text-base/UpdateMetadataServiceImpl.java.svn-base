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
import java.util.Collection;
import java.util.LinkedList;

import javax.ejb.Stateless;
import javax.mail.MethodNotSupportedException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.revision.UpdateMetadataServiceLocal;
import kiwi.api.revision.UpdateMetadataServiceRemote;
import kiwi.api.transaction.TransactionService;
import kiwi.exception.ContentItemDoesNotExistException;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.MetadataUpdate;

import org.hibernate.hql.ast.QuerySyntaxException;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * Creates MetadataUpdates which point to added and 
 * removed triples.
 * Provides the functionality commit, rollback and undo.
 * 
 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
 *
 */
@Name("updateMetadataService")
@AutoCreate
@Stateless
@Scope(ScopeType.STATELESS)
public class UpdateMetadataServiceImpl implements Serializable,
		UpdateMetadataServiceLocal, UpdateMetadataServiceRemote {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4286205071465311348L;

	/**
	 * Entity Manager, persists data and so forth
	 */
	@In
	private EntityManager entityManager;
	
	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateMetadataService#updateMetadata(java.util.Set, java.util.Set, java.util.Set, java.util.Set)
	 */
	public MetadataUpdate updateMetadata( 
			Collection<KiWiTriple> removedTriples, 
			Collection<KiWiTriple> addedTriples) {
		MetadataUpdate	mdu = new MetadataUpdate();
		
				
		// fill the metadata object with information about
		// removed and added triples/namespaces
		mdu.setAddedTriples(addedTriples);
//		mdu.setAddedNamespaces(addedNS);
		mdu.setRemovedTriples(removedTriples);
//		mdu.setRemovedNamespaces(removedNS);
		
		return mdu;
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateMetadataService#undo
	 * (kiwi.model.revision.MetadataUpdate)
	 */
	public void undo(CIVersion version) throws ContentItemDoesNotExistException {

		Log log = Logging.getLog(this.getClass());
		
		MetadataUpdate mdu = version.getMetadataUpdate();
		if(mdu != null) {
			// get transaction service
			TransactionService ts = (TransactionService) Component.getInstance("transactionService");
			
			LinkedList<Long> undeleted_ids = new LinkedList<Long>();
			for(KiWiTriple t : mdu.getRemovedTriples()) {
				// add triple to current transaction data
				ts.getTransactionCIVersionData(t.getSubject().getContentItem())
					.addTransactionAddedTriples(t);
				t.setDeleted(false);
				
				undeleted_ids.add(t.getId());
	
			}
			// update immutable entity ...
			if(undeleted_ids.size() != 0) {
				Query q1 = entityManager.createNamedQuery("tripleStore.undeleteTriples");
				q1.setParameter("ids", undeleted_ids);
				try {
					q1.executeUpdate();
				} catch(QuerySyntaxException e) {
					log.error("The Query could not be executed. " +
							"The following triples should have been " +
							"marked as undeleted #0 ", undeleted_ids);
				}
			}
			
			LinkedList<Long> deleted_ids = new LinkedList<Long>();
			for(KiWiTriple t : mdu.getAddedTriples()) {
				// add triple to current transaction data
				ts.getTransactionCIVersionData(t.getSubject().getContentItem())
					.addTransactionRemovedTriples(t);
				t.setDeleted(true);
				
				deleted_ids.add(t.getId());
			}
			// update immutable entity ...
			if(deleted_ids.size() != 0) {
				Query q2 = entityManager.createNamedQuery("tripleStore.deleteTriples");
				q2.setParameter("ids", deleted_ids);
				try {
					q2.executeUpdate();
				} catch(QuerySyntaxException e) {
					log.error("The Query could not be executed. " +
							"The following triples should have been " +
							"marked as deleted #0 ", deleted_ids);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateMetadataService#commitUpdate(kiwi.model.revision.MetadataUpdate)
	 */
	public void commitUpdate(CIVersion version) {
		if(version.getMetadataUpdate() != null) {
			Log log = Logging.getLog(UpdateMetadataServiceImpl.class);
			log.debug("Persisting metadata update");
			entityManager.persist(version.getMetadataUpdate());
		}
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateMetadataService#rollbackUpdate(kiwi.model.revision.MetadataUpdate)
	 */
	public void rollbackUpdate(CIVersion version) {
		version.setMetadataUpdate(null);
	}

	@Override
	public void restore(CIVersion verion)
			throws ContentItemDoesNotExistException {
		try {
			throw new MethodNotSupportedException("UpdateMetadataService.restore(CIVersion version) is not supported");
		} catch (MethodNotSupportedException e) {
			e.printStackTrace();
		}
	}    
 }
