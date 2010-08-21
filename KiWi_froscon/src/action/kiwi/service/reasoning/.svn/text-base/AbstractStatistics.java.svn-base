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

/**
 * 
 * @author Jakub Kotowski
 *
 */
public abstract class AbstractStatistics {
	protected Long reasoningBegin, reasoningEnd;
	protected Long reasonmaintenanceBegin, reasonmaintenanceEnd;
	protected long reasonmaintenanceTime = 0;
	protected Long persistenceBegin, persistenceEnd;
	protected long persistenceTime = 0;
	
	public abstract int getRulesCount();
	public abstract int getNewTriplesCount();
	public abstract int getGeneratedTriplesCount();
	public abstract int getMarkedTriplesCount();
	
	/** Returns the amount of time in milliseconds that the processing took.
	 * 
	 *  Returns null if not both start and stop methods were called.
	 */	
	public Long getTime() {
		if (reasoningBegin != null && reasoningEnd != null)
			return reasoningEnd - reasoningBegin;		
		return null;
	}
	
	/** Returns the amount of time in milliseconds that reason maintenance took.
	 * 
	 *  Returns null if not both start and stop methods were called.
	 */	
	public long getJustificationAddingTime() {
		return reasonmaintenanceTime;
	}
	
	/** Returns the amount of time in milliseconds that persistence took.
	 * 
	 *  Returns 0 if not both start and stop persistence methods were called.
	 *  Persistence time is increased each time stopPersistence method was called if the startPersistence method was called before.
	 */	
	public long getPersistenceTime() {
		return persistenceTime;
	}
	
	public void start() {
		reasoningBegin = System.currentTimeMillis();
	}
	
	public void stop() {
		reasoningEnd = System.currentTimeMillis();
	}		
	
	public void startAddingJustifications() {
		reasonmaintenanceBegin = System.currentTimeMillis();
	}
	
	public void stopAddingJustifications() {
		reasonmaintenanceEnd = System.currentTimeMillis();
		if (reasonmaintenanceBegin != null)
			reasonmaintenanceTime += reasonmaintenanceEnd.longValue() - reasonmaintenanceBegin.longValue();
		reasonmaintenanceBegin = null;
		reasonmaintenanceEnd = null;
	}		
	
	public void startPersistence() {
		persistenceBegin = System.currentTimeMillis();
	}
	
	public void stopPersistence() {
		persistenceEnd = System.currentTimeMillis();
		if (persistenceBegin != null)
			persistenceTime += persistenceEnd - persistenceBegin;
		persistenceBegin = null;
		persistenceEnd = null;
	}
}


