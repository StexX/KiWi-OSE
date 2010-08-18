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
package kiwi.service.reasoning;

import java.util.List;

/**
 * 
 * @author Jakub Kotowski
 *
 * @param <T>
 */
public abstract class AbstractSummaryStatistics<T extends AbstractStatistics> extends AbstractStatistics {
	protected List<T> stats;
	
	@Override
	public int getGeneratedTriplesCount() {
		int result = 0;
		for (T stat : stats)
			result += stat.getGeneratedTriplesCount();
		
		return result;
	}

	@Override
	public int getMarkedTriplesCount() {
		int result = 0;
		for (T stat : stats)
			result += stat.getMarkedTriplesCount();
		
		return result;
	}

	@Override
	public int getNewTriplesCount() {
		int result = 0;
		for (T stat : stats)
			result += stat.getNewTriplesCount();
		
		return result;
	}

	@Override
	public int getRulesCount() {
		int result = 0;
		for (T stat : stats)
			result += stat.getRulesCount();
		
		return result;
	}

	/** Returns the amount of time in milliseconds that the processing took.
	 * 
	 *  If not both start and stop methods were called then it computes the time as the sum of times of contained stats
	 *  otherwise returns the time difference between the calls in ms.
	 */	
	@Override
	public Long getTime() {
		if (reasoningBegin != null && reasoningEnd != null)
			return reasoningEnd - reasoningBegin;		
		
		Long result = 0l;
		for (T stat : stats)
			result += stat.getTime();
		
		return result;
	}

	/** Returns the amount of time in milliseconds that the justification processing took.
	 * 
	 *  Computes the time as the sum of times of contained stats plus the time recorded using this containing class.
	 */	
	@Override
	public long getJustificationAddingTime() {		
		long result = reasonmaintenanceTime;
		for (T stat : stats)
			result += stat.getJustificationAddingTime();
		
		return result;
	}
	
	/** Returns the amount of time in milliseconds that the persistence processing took.
	 *  Computes the time as the sum of times of contained stats plus the time recorded using this containing class.
	 */	
	@Override
	public long getPersistenceTime() {
		long result = persistenceTime;
		for (T stat : stats)
			result += stat.getPersistenceTime();
		
		return result;		
	}
}

