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

package kiwi.service.informationextraction;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.informationextraction.LabelService;
import kiwi.api.transaction.TransactionService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.informationextraction.Label;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.ontology.SKOSConcept;
import kiwi.service.transaction.KiWiSynchronizationImpl;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;

@Stateless
@Name("kiwi.informationextraction.labelService")
@Scope(ScopeType.STATELESS)
@TransactionManagement(TransactionManagementType.BEAN)
public class LabelServiceImpl implements LabelService {

	@Logger
	private Log log;
	
	@In
	EntityManager entityManager;
	
	@In
	private TransactionService transactionService;
	
	@In
	KiWiEntityManager kiwiEntityManager;
	
	@In(create=true)
	ContentItemService contentItemService;
	
	public void deleteLabels(ContentItem ci) {
		try {
			if(Transaction.instance().isActive()) {
				Transaction.instance().commit();
			}
			Transaction.instance().begin();
			transactionService.registerSynchronization(
            		KiWiSynchronizationImpl.getInstance(), 
            		transactionService.getUserTransaction() );
			Transaction.instance().enlist(entityManager);

//			entityManager.joinTransaction();
			Query q = entityManager.createNamedQuery("kiwi.informationextraction.labelService.deleteLabelsByResource");
			q.setParameter("resource", ci.getResource());
			q.executeUpdate();
			
			Transaction.instance().commit();
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
		} catch (NotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void createLabels(ContentItem ci) {
		List<Label> ret = new LinkedList<Label>();
		try {
			if(Transaction.instance().isActive()) {
				Transaction.instance().commit();
			}
			Transaction.instance().begin();
			transactionService.registerSynchronization(
            		KiWiSynchronizationImpl.getInstance(), 
            		transactionService.getUserTransaction() );
			Transaction.instance().enlist(entityManager);
			entityManager.joinTransaction();

			for (Label label : getLabels(ci)) {
				entityManager.persist(label);
			}
			
			Transaction.instance().commit();
			
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
		} catch (NotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initLabels() {
		Query q;
		
		q = entityManager.createNamedQuery("kiwi.informationextraction.labelService.deleteLabels");
		q.executeUpdate();
		
		for (ContentItem ci : contentItemService.getContentItems()) {
			for (Label label : getLabels(ci)) {
				entityManager.persist(label);
			}
		}
		
		entityManager.flush();
	}

	@Override
	public List<Object[]> getLabels() {
		Query q = entityManager.createNamedQuery("kiwi.informationextraction.labelService.listResourceLabels");
		return (List<Object[]>)q.getResultList();
	}

	@Override
	public List<KiWiResource> matchResources(String label) {
		// TODO Auto-generated method stub
		Query q = entityManager.createNamedQuery("kiwi.informationextraction.labelService.listResourcesByString");
		q.setParameter("string", label.toLowerCase());
		return (List<KiWiResource>)q.getResultList();
	}

	@Observer(value=KiWiEvents.TITLE_UPDATED)
	@Override
	public void onTitleUpdated(ContentItem ci) {
		Events.instance().raiseAsynchronousEvent(KiWiEvents.TITLE_UPDATED+"Async", ci);
	}
	
	@Asynchronous
	@Observer(value=KiWiEvents.TITLE_UPDATED+"Async")
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void onTitleEvent(ContentItem item) {
		deleteLabels(item);
		createLabels(item);			
	}

	@Override
	public List<Label> getLabels(ContentItem ci) {
		List<Label> ret = new LinkedList<Label>();
		
		Label label;
		if (ci.getTitle() != null) {
			label = new Label();
			label.setString(ci.getTitle().toLowerCase());
			label.setResource(ci.getResource());
			label.setType(Label.TYPE_TITLE);

			ret.add(label);
		}

		// retrieve SKOS concepts alternative labels
		if (ci.getResource().hasType(Constants.NS_SKOS + "Concept")) {
			SKOSConcept skosConcept = kiwiEntityManager.createFacade(ci, SKOSConcept.class);
			if (skosConcept != null) {
				for (String altlabel : skosConcept.getAlternativeLabels()) {
					label = new Label();
					label.setString(altlabel.toLowerCase());
					label.setResource(ci.getResource());
					label.setType(Label.TYPE_ALTLABEL);

					ret.add(label);
				}
			}
		}
		
		return ret;
	}
}
