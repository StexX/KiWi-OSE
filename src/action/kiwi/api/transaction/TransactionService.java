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
package kiwi.api.transaction;

import javax.faces.event.PhaseEvent;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;

import kiwi.exception.TransactionNotInitializedException;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.security.Identity;

/**
 * @author Sebastian Schaffert
 *
 */
public interface TransactionService {
	
//	public void resetPhaseFlag(PhaseEvent phase);
	
	public void phaseListener(PhaseEvent phase);
	
	/**
	 * Returns the current transaction data for a certain ContentItem 
	 * that stores KiWiUpdates.
	 * These updates are collected to form a revision when the
	 * transaction completes successfully. 
	 * @return
	 */
	public CIVersionBean getTransactionCIVersionData(ContentItem item);
	
	/**
	 * Returns the current transaction data for all ContentItems. 
	 * This includes a link to the ContentItem Versions, entities 
	 * that have been updated and the ContentItem that is the 
	 * origin of the updates
	 * @return
	 * @throws TransactionNotInitializedException
	 */
	public UpdateTransactionBean getCurrentTransactionData();
	
	/**
	 * Returns the transaction manager
	 * @return
	 */
	public javax.transaction.TransactionManager getTransactionManager();
	
	/**
	 * registers a synchronization for the transaction
	 * @param sync
	 * @param userTransaction
	 */
	public void registerSynchronization(KiWiSynchronization sync, javax.transaction.UserTransaction transaction);
	
	/**
	 * TODO: update
	 * This method returns the JTA user transaction; stolen from Seam as we need direct access to
	 * the JTA transaction and Seam wraps it into a new object on each call.
	 * 
	 * @return
	 * @throws NamingException
	 */
	public javax.transaction.UserTransaction getUserTransaction();
	
	/**
	 * Tries to run the updates again when the transaction failed before
	 * @param utb
	 */
	public void retryFailedTx(UpdateTransactionBean utb, User currentUser, Identity identity);
	
	/**
	 * Called when the transaction process initialises.
	 */
    public void initialise();
	
    /**
     * Called when the transaction process shuts down.
     */
    public void shutdown();
    
    /**
     * Called when the transaction process is destroyed.
     */
    public void destroy();

    /**
	 * Starts a transaction programmatically
	 * @throws NotSupportedException
	 * @throws SystemException
	 */
	public void beginTransaction() throws NotSupportedException, SystemException;

	/**
	 * Commits a transaction programmatically
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws RollbackException
	 * @throws HeuristicMixedException
	 * @throws HeuristicRollbackException
	 * @throws SystemException
	 */
	public void commitTransaction() throws SecurityException, IllegalStateException,
			RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException;
    
}
