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
package kiwi.action.reasoning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.model.DataModel;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.reasoning.ReasoningService;
import kiwi.model.kbase.KiWiTriple;
import kiwi.service.reasoning.ReasoningStatistics;
import kiwi.service.reasoning.ReasoningTask;
import kiwi.service.reasoning.ReasoningTaskStatistics;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Jakub Kotowski
 *
 */
@Name("reasoningAction")
@Scope(ScopeType.CONVERSATION)
public class ReasoningAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private static Log log;
	
	@In("kiwi.core.reasoningService")
	private ReasoningService reasoningService;
 	@In
    private EntityManager entityManager;
	
	private Integer inferredTriplesCurrentPage = 1;
	private Integer baseTriplesCurrentPage = 1;
		
	@In(value="ra.uidataWrapper")
	private UIDataWrapper uidataWrapper;
	
	public String runReasoner() {
		log.info("ReasoningAction: about to call reasoningService.runReasoner()");
		reasoningService.runReasoner();
		return "success";
	}
	
	public String runEnqueuedTasks() {
		log.info("ReasoningAction: about to call reasoningService.runEnqueuedTasks()");
		reasoningService.runEnqueuedTasks();
		return "success";
	}	
		
	public boolean isReasoningEnabled() {
		return reasoningService.isReasoningEnabled();
	}
	
	public void setReasoningEnabled(boolean enabled) {
		reasoningService.setReasoningEnabled(enabled);
	}

	public boolean isHybridReasoningEnabled() {
		return reasoningService.isHybridReasoningEnabled();
	}
	
	public void setHybridReasoningEnabled(boolean enabled) {
		reasoningService.setHybridReasoningEnabled(enabled);
	}

	public Integer getInferredTriplesCurrentPage() {
		return inferredTriplesCurrentPage;
	}

	public void setInferredTriplesCurrentPage(Integer inferredTriplesCurrentPage) {
		this.inferredTriplesCurrentPage = inferredTriplesCurrentPage;
	}		
	
	public Integer getBaseTriplesCurrentPage() {
		return baseTriplesCurrentPage;
	}

	public void setBaseTriplesCurrentPage(Integer baseTriplesCurrentPage) {
		this.baseTriplesCurrentPage = baseTriplesCurrentPage;
	}

	@SuppressWarnings("unchecked")
	List<KiWiTriple> getPage(boolean inferred, int first, int rows, String sortBy) {
    	Query q = entityManager.createNamedQuery(inferred ? "tripleStore.inferredTriples" : "tripleStore.baseTriples");
    	q.setFirstResult(first);
    	q.setMaxResults(rows);
    	return q.getResultList();		
	}
	
	private Long getTriplesCount(boolean inferred) {
    	Query q = entityManager.createNamedQuery(inferred ? "tripleStore.inferredTriplesCount" : "tripleStore.baseTriplesCount");
    	return (Long)q.getSingleResult();				
	}
	
	private DataModel getPagedDataModel(boolean inferred, String sortBy) {
		//log.debug("get paged data model inferred: "+inferred);
		int totalListSize = getTriplesCount(inferred).intValue();
		
		//log.debug("There is #0 triples in total.",totalListSize);
		List<KiWiTriple> pagedList = getPage(inferred, uidataWrapper.getUIData().getFirst(), uidataWrapper.getUIData().getRows(), sortBy);
		PagedListDataModel dataModel = new PagedListDataModel(pagedList, totalListSize, uidataWrapper.getUIData().getRows());
		return dataModel;
	}

	public DataModel getTableModelBase() {
		return getPagedDataModel(false, "");
	}
	
	public DataModel getTableModelInferred() {
		return getPagedDataModel(true, "");
	}	
	
	public List<ReasoningTask> getTaskQueue() {
		List<ReasoningTask> tasks = new ArrayList<ReasoningTask>();
		
		for (ReasoningTask task : reasoningService.getTaskQueue())
			tasks.add(task);
		
		return tasks;
	}
	
	public ReasoningStatistics getReasoningStats() {
		return reasoningService.getReasoningStatistics();
	}
	
	public List<ReasoningTaskStatistics> getReasoningTaskStats() {
		List<ReasoningTaskStatistics> stats = new ArrayList<ReasoningTaskStatistics>( reasoningService.getReasoningStatistics().getTaskStats() ); 
		Collections.reverse(stats);
		return stats;
	}
	
	public boolean isReasonerRunning() {
		return reasoningService.isReasonerRunning();
	}
	
	public ReasoningTask getCurrentTask() {
		return reasoningService.getCurrentTask();
	}
}


