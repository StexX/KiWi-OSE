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

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.revision.UpdateConfigurationServiceLocal;
import kiwi.api.revision.UpdateConfigurationServiceRemote;
import kiwi.api.transaction.TransactionService;
import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.revision.ConfigurationUpdate;
import kiwi.model.revision.Revision;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * The ConfigurationUpdateService creates, commits and reverts the current status
 * of the system configuration.
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Name("updateConfigurationService")
@AutoCreate
@Stateless
@Scope(ScopeType.STATELESS)
public class UpdateConfigurationServiceImpl implements
		UpdateConfigurationServiceLocal, UpdateConfigurationServiceRemote {


	/**
	 * Entity Manager, persists data and so forth
	 */
	@In
	private EntityManager entityManager;
	
	@Override
	public ConfigurationUpdate updateConfiguration(
			Collection<KiWiNamespace> removedNS, Collection<KiWiNamespace> newNS) {
		ConfigurationUpdate cu = new ConfigurationUpdate();
		cu.setAddedNamespaces(newNS);
		cu.setRemovedNamespaces(removedNS);
		return cu;
	}
	
	@Override
	public void commitUpdate(Revision rev) {
		if(rev.getConfigurationUpdate() != null) {
			entityManager.persist(rev.getConfigurationUpdate());
		}
	}

	@Override
	public void rollbackUpdate(ConfigurationUpdate cu) {

	}

	@Override
	public void undo(ConfigurationUpdate cu) {
		// get transaction service
		TransactionService ts = (TransactionService) Component.getInstance("transactionService");
		
		for(KiWiNamespace ns : cu.getRemovedNamespaces()) {
			// add triple to current transaction data
			ts.getCurrentTransactionData()
				.addTransactionAddedNamespaces(ns);
			ns.setDeleted(false);
			
			// update immutable entity ...
			Query q = entityManager.createNamedQuery("tripleStore.undeleteNamespace");
			q.setParameter("id", ns.getId());
			int count = q.executeUpdate();
		}
		for(KiWiNamespace ns : cu.getAddedNamespaces()) {
			// add triple to current transaction data
			ts.getCurrentTransactionData()
				.addTransactionRemovedNamespaces(ns);
			ns.setDeleted(true);
			
			// update immutable entity ...
			Query q = entityManager.createNamedQuery("tripleStore.deleteNamespace");
			q.setParameter("id", ns.getId());
			int count = q.executeUpdate();
		}
	}

}
