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

import javax.ejb.Stateless;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import kiwi.api.transaction.CIVersionBean;
import kiwi.api.transaction.KiWiSynchronization;
import kiwi.api.transaction.TransactionService;
import kiwi.api.transaction.UpdateTransactionBean;
import kiwi.context.CurrentUserFactory;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.transaction.Transaction;
import org.jboss.seam.util.Naming;

/**
 * @author Sebastian Schaffert
 * edited by 	Stephanie Stroka
 *				(stephanie.stroka@salzburgresearch.at)
 */
@Stateless
@Name("transactionService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class TransactionServiceImpl implements TransactionService, Serializable {
	
	
	//maintain two lists to work around a bug in JBoss EJB3 where a new TransactionSynchronization
	//gets registered each time the bean is called
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@In(required=false)
	private EntityManager entityManager;
	
	@Observer("org.jboss.seam.beforePhase")
	public synchronized void phaseListener(PhaseEvent phase) {
		if(phase.getPhaseId() == PhaseId.INVOKE_APPLICATION) {
			log.info(">>>>>>>>>>> Called phaseListener.beforePhase");
			try {
				if(Transaction.instance().isActive()) {
					UserTransaction utx = getUserTransaction();
					registerSynchronization(KiWiSynchronizationImpl.getInstance(), utx );
				} else {
					log.error("Application was invoked without having an active transaction :(");
				}
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public CIVersionBean getTransactionCIVersionData(ContentItem item) {
		UpdateTransactionBean utb = getCurrentTransactionData();
		if(utb.getContentItemVersionMap().get(item) == null) {
			CIVersionBean civBean = new CIVersionBean();
			
			civBean.setContentItem(item);
			utb.getContentItemVersionMap().put(item, civBean);
		}
		return utb.getContentItemVersionMap().get(item);
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.transaction.TransactionService#getCurrentTransactionData()
	 */
	public UpdateTransactionBean getCurrentTransactionData() {
		if(UpdateTransactionBean.getInstance() == null) {
			// when there is no transaction data associated with the thread, create a new
			// UpdateTransactionBean and register the KiWiSynchronization
			UserTransaction utx = getUserTransaction();
			UpdateTransactionBean.setInstance(new UpdateTransactionBean());
		}
		
		return UpdateTransactionBean.getInstance();
	}
	
	
	/* (non-Javadoc)
	 * @see kiwi.api.transaction.TransactionService#registerSynchronization(kiwi.api.transaction.KiWiSynchronization, org.jboss.seam.transaction.UserTransaction)
	 */
	public synchronized void registerSynchronization(KiWiSynchronization sync, javax.transaction.UserTransaction transaction) {
		log.info(">>>>>>>>>>> register synchronization: #0 (Thread: #1)", sync.getClass().getSimpleName(), Thread.currentThread().toString());
		try {
			((org.jboss.seam.transaction.UserTransaction) 
					Component.getInstance("org.jboss.seam.transaction.transaction",ScopeType.EVENT) )
					.registerSynchronization(sync);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			log.error("Registering synchronization #0 failed because of an IllegalStateException", 
					sync.getClass().getSimpleName());
		}
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.transaction.TransactionService#getTransactionManager()
	 */
	public javax.transaction.TransactionManager getTransactionManager() {
		TransactionManager transactionManager;
		try {
			InitialContext context = Naming.getInitialContext();
			try {
				transactionManager = (javax.transaction.TransactionManager) context.lookup("java:/TransactionManager");
			} catch (NameNotFoundException nnfe) {
				try {
					transactionManager = (javax.transaction.TransactionManager) context.lookup("java:appserver/TransactionManager");
				} catch(NameNotFoundException ee) {
					try {
						//Embedded JBoss has no java:comp/TransactionManager
						transactionManager = (javax.transaction.TransactionManager) context.lookup("TransactionManager");
						transactionManager.getStatus(); //for glassfish, which can return an unusable UT
					} catch (Exception e) {
						log.error("error while retrieving JTA transaction manager: #0",nnfe.getMessage());
						return null;
					}
				}
			}
		} catch(NamingException ex) {
			log.error("error while retrieving JTA transaction manager: #0",ex.getMessage());
			return null;
		}
		return transactionManager;
	}
	
	/**
	 * Get direct access to JTA user transaction, as we use it as a key. To improve performance, we
	 * cache it in the event scope...
	 * 
	 */
	public javax.transaction.UserTransaction getUserTransaction() {
		
		// cache transaction in event scope
		UserTransaction utx = (UserTransaction) Contexts.getEventContext().get("_kiwi_transaction");


		if(utx == null) {
			try {
				InitialContext context = Naming.getInitialContext();
				try {
					utx = (javax.transaction.UserTransaction) context.lookup("UserTransaction");
				} catch (NameNotFoundException nnfe) {
					try {
						utx = (javax.transaction.UserTransaction) context.lookup("javax.transaction.UserTransaction");
					} catch(NameNotFoundException ee) {
						try {
							utx = (javax.transaction.UserTransaction) context.lookup("java:comp/UserTransaction");
						} catch (Exception e) {
							log.error("error while retrieving JTA transaction manager: #0",nnfe.getMessage());
							return null;
						}
					}
				}
			} catch(NamingException ex) {
				log.error("error while retrieving JTA transaction manager: #0",ex.getMessage());
				return null;
			}

			Contexts.getEventContext().set("_kiwi_transaction",utx);
		}
		return utx;
	}

	/**
	 * This method prepares objects that failed to persist, because of 
	 * e.g. some OptimisticLocking Exception, for a recurrence.
	 * @param utb the UpdateTransactionBean that carries the objects 
	 * 		  that have failed to persist during the last tx
	 */
	@Asynchronous
	public void retryFailedTx(UpdateTransactionBean utb, User currentUser, Identity identity) {
		CurrentUserFactory currentUserFactory = 
			(CurrentUserFactory) Component.getInstance("currentUserFactory");
		currentUserFactory.setCurrentUser(currentUser);
		
		log.info("Retry transaction");
		/* check, if we still got retries left... */
		if(utb.getRetryCount() <= UpdateTransactionBean.timeout) {
			try {
				if(!Transaction.instance().isActive()) {
					Transaction.instance().begin();
					registerSynchronization(
	                		KiWiSynchronizationImpl.getInstance(), 
	                		getUserTransaction() );
				}
				
				// update the version numbers
				for(ContentItem ci : utb.getContentItemVersionMap().keySet()) {
					
					log.info("Retry transaction on ContentItem #0 ", ci);
					EntityManager em = (EntityManager) Component.getInstance("entityManager");
					
					// update version number of ContentItem
					Query q = em.createNamedQuery("contentItemService.currentVersionId");
					q.setParameter("id", ci.getId());
					Long vid = (Long) q.getSingleResult();
					log.info("Last ContentItem version number was #0 - setting it to #1 now.", ci.getVersion(), vid);
					ci.setVersion(vid);
					
					// maybe we need to update the tags, too...hmmmmm....
					// well,..I'll notice if tests fail, I guess
				}
				
				Transaction.instance().commit();
			} catch (SystemException e1) {
				e1.printStackTrace();
				log.error("Transaction failed with message #0 ", e1.getMessage());
			} catch (NotSupportedException e1) {
				e1.printStackTrace();
				log.error("Transaction failed with message #0 ", e1.getMessage());
			} catch (RollbackException e1) {
				e1.printStackTrace();
				log.error("Transaction failed with message #0 ", e1.getMessage());
			} catch (HeuristicMixedException e1) {
				e1.printStackTrace();
				log.error("Transaction failed with message #0 ", e1.getMessage());
			} catch (HeuristicRollbackException e1) {
				e1.printStackTrace();
				log.error("Transaction failed with message #0 ", e1.getMessage());
			}
		}
	}
	
	/**
	 * Starts a transaction programmatically
	 * @throws NotSupportedException
	 * @throws SystemException
	 */
	@Override
	public void beginTransaction() throws NotSupportedException, SystemException {
		Transaction.instance().begin();
		registerSynchronization(
				KiWiSynchronizationImpl.getInstance(), 
				getUserTransaction() );
		entityManager.joinTransaction();
	}
	
	/**
	 * Commits a transaction programmatically
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws SystemException
	 */
	@Override
	public void commitTransaction() throws 
			SecurityException, IllegalStateException, 
			RollbackException, HeuristicMixedException, 
			HeuristicRollbackException, SystemException {
		Transaction.instance().commit();
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.transaction.TransactionService#initialise()
	 */
    public void initialise() {
    }
    
	/* (non-Javadoc)
	 * @see kiwi.api.transaction.TransactionService#shutdown()
	 */
    public void shutdown() {
    	log.info("transaction service is shutting down");
    }

    /* (non-Javadoc)
	 * @see kiwi.api.transaction.TransactionService#destroy()
	 */
    public void destroy() {
    }
}
