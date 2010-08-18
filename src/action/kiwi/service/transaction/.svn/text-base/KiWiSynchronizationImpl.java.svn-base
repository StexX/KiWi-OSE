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
package kiwi.service.transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.transaction.Status;

import kiwi.api.event.KiWiEvents;
import kiwi.api.revision.CIVersionService;
import kiwi.api.revision.RevisionService;
import kiwi.api.revision.UpdateMetadataService;
import kiwi.api.revision.UpdateTaggingService;
import kiwi.api.search.SolrService;
import kiwi.api.transaction.CIVersionBean;
import kiwi.api.transaction.KiWiSynchronization;
import kiwi.api.transaction.TransactionService;
import kiwi.api.transaction.UpdateTransactionBean;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.triplestore.TripleStorePersister;
import kiwi.exception.ContentItemMissingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.MetadataUpdate;
import kiwi.model.revision.Revision;
import kiwi.model.revision.TaggingUpdate;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Identity;
import org.jboss.seam.transaction.Transaction;

/**
 * A synchronization for performing the KiWi-specific functionality at the end of a transaction.
 * The beforeCompletion method of this synchronization takes care of KiWi versioning and of properly
 * persisting all entities and triples into the various data stores. The afterCompletion method
 * cleans up the transaction and notifies its success/failure.
 * <p>
 * The synchronization is registered with the transaction in TransactionService.
 * <p>
 * Implemented as a singleton (since rev 3253) because it does not need to hold state.
 * 
 * @author 	Stephanie Stroka
 * 			(stephanie.stroka@salzburgresearch.at)
 *          Sebastian Schaffert
 *
 */
public class KiWiSynchronizationImpl implements KiWiSynchronization, Serializable {

	private static final long serialVersionUID = 7386382550531804338L;
	
	private static KiWiSynchronizationImpl instance = null;
	
	public static KiWiSynchronizationImpl getInstance() {
		if(instance == null) {
			instance = new KiWiSynchronizationImpl();
		}
		return instance;
	}
	
	
	private KiWiSynchronizationImpl() {
		
	}
	
	/* (non-Javadoc)
	 * @see javax.transaction.Synchronization#afterCompletion(int)
	 */
	public void afterCompletion(int arg0) {
		try {
			Log log = Logging.getLog(KiWiSynchronizationImpl.class);
			log.info(">>>>>>>>>>> transaction reached afterCompletion(): (Thread: #0)", Thread.currentThread().toString());
//			log.debug("transaction reached afterCompletion()");
			
			UpdateTransactionBean utb = UpdateTransactionBean.getInstance();
				
			// if transaction completed successfully its status is 'committed'
			switch(arg0) {
			case Status.STATUS_COMMITTED:
				log.debug("transaction committed successfully");

				// raise the asynchronous transaction success event, passing the transaction data as argument
				// for further processing, e.g. by reasoner
				if(utb != null) {
					Events.instance().raiseAsynchronousEvent(KiWiEvents.TRANSACTION_SUCCESS+"Async",utb);
				}
			break;
			case Status.STATUS_COMMITTING:
			case Status.STATUS_MARKED_ROLLBACK:
			case Status.STATUS_NO_TRANSACTION:
			case Status.STATUS_PREPARED:
			case Status.STATUS_PREPARING:
			case Status.STATUS_ROLLEDBACK:
			case Status.STATUS_ROLLING_BACK:
			case Status.STATUS_UNKNOWN:
			default:
				log.error("transaction aborted, rolling back; transaction status: #0", arg0);
				Events.instance().raiseEvent(KiWiEvents.TRANSACTION_ABORT);
				break;
			}
				
		} finally {
			UpdateTransactionBean.clearInstance();
		}
		
	}
	
	private void persisting(UpdateTransactionBean utb) {
		
		Log log = Logging.getLog(KiWiSynchronizationImpl.class);
		TripleStore tripleStore = 
			(TripleStore) Component.getInstance("tripleStore");
		TripleStorePersister tps =
			(TripleStorePersister) Component.getInstance("kiwi.core.tripleStorePersister");
		EntityManager em =
			(EntityManager) Component.getInstance("entityManager");
		SolrService solr =
			(SolrService) Component.getInstance("solrService");
		
		// merge or persist the entities
		Set<KiWiEntity> entities 		= utb.getEntities();
		for(KiWiEntity e : entities) {
			tripleStore.persist(e, true);
		}

		Set<KiWiNamespace> removedNS 		= utb.getTransactionRemovedNamespaces();
		Set<KiWiNamespace> newNS 			= utb.getTransactionAddedNamespaces();
		
		if(removedNS.size() > 0 && newNS.size() > 0) {
			// remove namespaces which are added and removed again or vice versa
			for(KiWiNamespace ns : new HashSet<KiWiNamespace>(newNS)) {
				if(utb.removeTransactionRemovedNamespaces(ns)) {
					utb.removeTransactionAddedNamespaces(ns);
				}
			}
		}
		
		if(removedNS.size() > 0 || newNS.size() > 0) {
//			UpdateConfigurationService ucs = (UpdateConfigurationService) 
//				Component.getInstance("updateConfigurationService");
//			ucs.updateConfiguration(removedNS, newNS);
			// TODO add configuationupdate to the revision
		}
		
		// really store namespaces
		for(KiWiNamespace ns : newNS) {
			tps.storeNamespace(ns);
		}
		// really remove namespaces
		for(KiWiNamespace ns : removedNS) {
			tps.removeNamespace(ns);
		}
		
		log.debug("1) #0 content item versions have been produced during this transaction", 
				utb.getContentItemVersionMap().keySet().size());
		for(ContentItem versionedCI : utb.getContentItemVersionMap().keySet()) {
			CIVersionBean ciVersionBean = utb.getContentItemVersionMap().get(versionedCI);
			
			// get namespaces and triples that were added/removed during the transaction
			Set<KiWiTriple> removedTriples 	    = ciVersionBean.getTransactionRemovedTriples();
			Set<KiWiTriple> newTriples 		    = ciVersionBean.getTransactionAddedTriples();

			long current   = System.currentTimeMillis();
			log.debug("BEFORE CLEANUP: adding #0 triples, removing #1 triples", newTriples.size(), removedTriples.size());

			if(!removedTriples.isEmpty() && !newTriples.isEmpty()) {
				// clean up, if a triple is removed and then added again we can save the work	

				// case 1: a triple is newly added and then removed again: what should happen is that it 
				// is never persisted at all, i.e. it can savely be removed from both lists
				// case 2: a triple is old, then removed and added again: what should happen is: nothing, 
				// triple stays as it is in DB, i.e. removing the triple from the lists is fine
				//
				// in both cases, removing from the added and removed lists is the correct behaviour
				for(KiWiTriple t : new HashSet<KiWiTriple>(newTriples)) {
					// removes the first occurrence of the same triple
					if(ciVersionBean.removeTransactionRemovedTriple(t)) {
						ciVersionBean.removeTransactionAddedTriple(t);
						log.debug("not updating triple #0",t.toString());
					}
				}
//				newTriples = ciVersionBean.getTransactionAddedTriples();
//				removedTriples = ciVersionBean.getTransactionRemovedTriples();
			}
			log.debug("AFTER CLEANUP: adding #0 triples, removing #1 triples", newTriples.size(), removedTriples.size());

			log.debug("triple cleanup took #0ms", System.currentTimeMillis()-current);
			current = System.currentTimeMillis();

			// add subjects of added and removed triples to SOLR indexing queue
			for(KiWiTriple t : newTriples) {
				solr.enqueue(t.getSubject().getContentItem());
			}
			for(KiWiTriple t : removedTriples) {
				solr.enqueue(t.getSubject().getContentItem());
			}
			
			
			// notify tripleStoreUpdated after transaction completion 
			if(newTriples.size() + removedTriples.size() > 0) {
				Events.instance().raiseTransactionSuccessEvent(KiWiEvents.TRIPLESTORE_UPDATED);
			}

			if(removedTriples.size() > 0 || newTriples.size() > 0) {
				UpdateMetadataService ums = (UpdateMetadataService) Component.getInstance("updateMetadataService");
				// if updateMetadataService isn't null: create a MetadataUpdate
				if(ums != null) {
					//Inferred triples shouldn't be versioned so filter them out first
					//TODO: An alternative is to track inferred triples separately - could be more efficient, would avoid this filtering/copying

					ArrayList<KiWiTriple> addedBaseTriples = new ArrayList<KiWiTriple>();
					for (KiWiTriple t : newTriples)
						if (!t.isInferred())
							addedBaseTriples.add(t);

					ArrayList<KiWiTriple> removedBaseTriples = new ArrayList<KiWiTriple>();
					for (KiWiTriple t : removedTriples)
						if (!t.isInferred())
							removedBaseTriples.add(t);

					MetadataUpdate mdu = ums.updateMetadata(removedBaseTriples, addedBaseTriples);
					if(mdu != null) {
						try {
							ciVersionBean.setMetadataUpdate(mdu);
						} catch (ContentItemMissingException e1) {
							log.error("CIVersionBean misses a content item");
							e1.printStackTrace();
						}
					} else {
						log.error("updateMetadataService.updateMetadata(...) returned null. Cannot add Update to UpdateTransactionBean");
					}
				} else {
					log.error("updateMetadataService was null");
				}
			}

			// really store triples (batch method)
			tps.storeTriples(newTriples);
			tps.removeTriples(removedTriples);
			
			List<Tag> removedTags				= ciVersionBean.getTransactionRemovedTags();
			List<Tag> addedTags 				= ciVersionBean.getTransactionAddedTags();

			if(removedTags != null && removedTags.size() > 0 || 
					addedTags != null && addedTags.size() > 0) {
				log.info("Added tags: #0 ", addedTags);
				log.info("Removed tags: #0 ", removedTags);
				// add TaggingUpdate to transaction data
				UpdateTaggingService uts = (UpdateTaggingService) 
					Component.getInstance("updateTaggingService");
				TaggingUpdate tu = uts.updateTagging(removedTags, addedTags);
				try {
					ciVersionBean.setTaggingUpdate(tu);
				} catch (ContentItemMissingException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
//	@Transactional
	private void versioning(UpdateTransactionBean utb) {
		if(Constants.CREATE_UPDATES) {
			Log log = Logging.getLog(KiWiSynchronizationImpl.class);
			// commit updates and create revision
			try {
				log.debug(">>>>>>>>>>>>>> versioning() called");
				
				long start = System.currentTimeMillis();
				
				RevisionService   revisionService = (RevisionService) Component.getInstance("revisionService");
				CIVersionService ciVersionService = (CIVersionService) Component.getInstance("ciVersionService"); 
				
				//utb.getContentItemVersionMap().keySet();
				if(revisionService != null) {
//					Set<CIVersion> versions = new HashSet<CIVersion>();
//					
					// create revision
					Revision rev = revisionService.createRevision();
					if(rev != null) {
						// commit revision
						revisionService.commit(rev);
						
						for(KiWiEntity ci : utb.getContentItemVersionMap().keySet()) {
							CIVersionBean civBean = utb.getContentItemVersionMap().get(ci);
							
							CIVersion version = civBean.getCIVersion();
							
							if(version != null) {
								if(version.getMetadataUpdate() != null) {
									log.debug(">>> MetaDataUpdate is not null and should therefore be persisted");
								}
								version.setRevision(rev);
								ciVersionService.commit(version);
							
								long current = System.currentTimeMillis();
								log.debug("creating revision and committing updateService took #0ms", current-start);
							} else {
								/* this can only happen if the reasoner added triples 
								   after the creation of the metadataUpdate in beforeCompletion().
								   Since triples produced by the reasoner should not be versioned, 
								   we just do nothing here. TODO: Figure out a more elegant way 
								   to prevent, that reasoning-triples get added to the transaction data */
							}
						}
					}			
				} else {
					log.warn("revisionService was null");
				}
			} catch (Throwable e) {
				log.error("Problem while comitting updates and creating revision: #0", e);
				e.printStackTrace();
			}
		}
	}
	
	private void flushing() throws Throwable {
		Log log = Logging.getLog(KiWiSynchronizationImpl.class);
		TripleStorePersister tps =
			(TripleStorePersister) Component.getInstance("kiwi.core.tripleStorePersister");
		EntityManager em =
			(EntityManager) Component.getInstance("entityManager");
		
		log.debug(">>>>>>>>>>>>>> flushing entityManager and triplestore-persister");
		em.flush();
		tps.flush();
	}

	/* (non-Javadoc)
	 * @see javax.transaction.Synchronization#beforeCompletion()
	 */
	public void beforeCompletion() {
		Log log = Logging.getLog(KiWiSynchronizationImpl.class);
		log.debug(">>>>>>>>>>>>>> transaction reached beforeCompletion()");
		
		/* we need to ensure that a currentUser exists before we create MetadataUpdates
		 * Does anyone have a better idea than just injecting the currentUser? */
		User currentUser = (User) Component.getInstance("currentUser");
		
		log.debug("THREAD: "+Thread.currentThread());
		try {
			long start   = System.currentTimeMillis();
			long current   = System.currentTimeMillis();
			
			TransactionService transactionService = 
				(TransactionService) Component.getInstance("transactionService");
			TripleStore tripleStore = 
				(TripleStore) Component.getInstance("tripleStore");
			TripleStorePersister tps =
				(TripleStorePersister) Component.getInstance("kiwi.core.tripleStorePersister");
			EntityManager em =
				(EntityManager) Component.getInstance("entityManager");
			SolrService solr =
				(SolrService) Component.getInstance("solrService");

			// get the transaction data
			UpdateTransactionBean utb 		= transactionService.getCurrentTransactionData();
			if(utb != null) {
				persisting(utb);
				current   = System.currentTimeMillis();
				log.debug("persisting of metadata updates took #0ms", System.currentTimeMillis()-current);
				versioning(utb);
				current   = System.currentTimeMillis();
				log.debug("versioning of metadata updates took #0ms", System.currentTimeMillis()-current);
			}

			try {
				flushing();
//				transactionService.removeRegisteredSync(instance);
				
				current = System.currentTimeMillis();
				log.info("committing transaction took #0ms overall", current-start);
				
				Events.instance().raiseEvent(KiWiEvents.TRANSACTION_SUCCESS);
			} catch(OptimisticLockException ex) {
				// this exception is thrown if there were concurrent accesses to the same resource...
				log.error("catched optimisticlock exception",ex);
				
				tps.rollback();
				
				transactionService.retryFailedTx(utb, currentUser, Identity.instance());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} catch(RuntimeException ex) {
			
			log.error("runtime exception in beforeCompletion of thread #0", Thread.currentThread());
			ex.printStackTrace();
			try {
				Transaction.instance().setRollbackOnly();
			} catch(Exception ex2) {
				log.error("unrecoverable error in beforeCompletion",ex2);
			}
		}
	}
}
