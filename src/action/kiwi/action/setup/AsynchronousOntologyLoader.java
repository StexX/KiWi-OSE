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
package kiwi.action.setup;

import java.io.InputStream;
import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import kiwi.api.importexport.ImportService;
import kiwi.api.reasoning.ReasoningService;
import kiwi.api.setup.OntologyBean;
import kiwi.api.system.StatusService;
import kiwi.api.transaction.TransactionService;
import kiwi.context.CurrentUserFactory;
import kiwi.model.status.SystemStatus;
import kiwi.model.user.User;
import kiwi.service.importexport.KiWiImportException;
import kiwi.service.transaction.KiWiSynchronizationImpl;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.transaction.Transaction;

/**
 * The AsynchronousOntologyLoader loads ontologies into the 
 * system via the loadOntologies() method. 
 *
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.admin.ontologyLoader")
@AutoCreate
@Scope(ScopeType.STATELESS)
public class AsynchronousOntologyLoader {

	@Logger
	private static Log log;

	@In("kiwi.core.importService")
	private ImportService importService;

	@In("kiwi.core.statusService")
	private StatusService statusService;
	
	@In
	private TransactionService transactionService;
	
	/**
	 * Imports ontologies into the system. The currentUser and the identity MUST be 
	 * given as a parameter to ensure, that the importer has the correct user, otherwise 
	 * the anonymous user would be used to import ontologies.
	 * 
	 * @param setupOntologyList
	 * @param clear
	 * @param currentUser
	 * @param identity
	 */
	@Asynchronous
	public void loadOntologies(List<OntologyBean> setupOntologyList, boolean clear, User currentUser, Identity identity) {
		
		CurrentUserFactory currentUserFactory = 
				(CurrentUserFactory) Component.getInstance("currentUserFactory");
		currentUserFactory.setCurrentUser(currentUser);
		
		SystemStatus status = new SystemStatus("ontology import");
		status.setId("ontology import");
		status.setProgress(0);
		statusService.addSystemStatus(status);
		
		ReasoningService reasoner = (ReasoningService) Component.getInstance("kiwi.core.reasoningService");
		boolean enabled = reasoner.isReasoningEnabled();
		reasoner.disableReasoning();
		
		try {
			Transaction.instance().setTransactionTimeout(60000);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(!Transaction.instance().isActive()) {
				Transaction.instance().begin();
				transactionService.registerSynchronization(
                		KiWiSynchronizationImpl.getInstance(), 
                		transactionService.getUserTransaction() );
			} else {
				log.error("Another transaction has been active.");
				return;
			}
		} catch (SystemException e1) {
			e1.printStackTrace();
		} catch (NotSupportedException e1) {
			e1.printStackTrace();
		}
		
		log.info("current transaction: #0", Transaction.instance().hashCode());

		if(clear) {
			// this is now transaction save
			importService.clearTripleStore();
		}

		int progress = 0;
		int max = 0;
		for(OntologyBean ob : setupOntologyList) {
			if(ob.isLoad())
				max++;
		}
		long time = 0;
		for(OntologyBean bean : setupOntologyList) {
			if(bean.isLoad()) {
				status.setMessage("Importing " + bean.getName());
				log.info("loading ontology '#0'", bean.getName());
				try {
					time = System.currentTimeMillis();
					if(!Transaction.instance().isActive()) {
						Transaction.instance().begin();
						transactionService.registerSynchronization(
		                		KiWiSynchronizationImpl.getInstance(), 
		                		transactionService.getUserTransaction() );
					}					
					
					InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(bean.getUri());
					if(resourceStream == null)
						throw new KiWiImportException("The uri '" + bean.getUri() + "' couldn't be opened.");
					/* using the importData method that takes the currentUser as a parameter, 
					 * because otherwise the currentUser object gets lost as a result of the asynchronous method call */
					importService.importData(resourceStream, "application/rdf+xml", null, null, currentUser, null);
//					importService.importData(resourceStream, bean.getFormat());
					
					Transaction.instance().commit();
					
					log.info("loading the ontology '#0' finished (#1 ms).", bean.getName(), System.currentTimeMillis()-time);
				} catch(Exception ex) {
					log.error("Error importing the ontology '#0': #1", bean.getName(), ex.getMessage());
				}
				progress ++;
				status.setProgress(100 * progress / max);
			} else {
				log.debug("not loading ontology '#0'", bean.getName());
			}
		}

		reasoner.setReasoningEnabled(enabled);

		statusService.removeSystemStatus(status);
		
		try {
			if(Transaction.instance().isActive()) {
				Transaction.instance().commit();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		} catch (RollbackException e) {
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			e.printStackTrace();
		}
		
		reasoner.runEnqueuedTasks();
		
		log.info("ontology import finished");
	}

}
