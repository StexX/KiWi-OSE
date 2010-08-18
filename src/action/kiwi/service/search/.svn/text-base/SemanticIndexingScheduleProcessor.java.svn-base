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
package kiwi.service.search;

import kiwi.api.search.SemanticIndexingService;
import kiwi.api.system.StatusService;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.IntervalCron;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.jboss.seam.log.Log;

/**
 * Process a recurring indexing task asynchronously. By default, this component is called every
 * 30 minutes. It is controlled by SemanticIndexingScheduleController
 *
 * @author Sebastian Schaffert
 * @see SemanticIndexingScheduleController
 */
@Name("kiwi.core.semanticIndexingScheduleProcessor")
@AutoCreate
@Scope(ScopeType.APPLICATION)
public class SemanticIndexingScheduleProcessor {

	@Logger
	private Log log;
	
	@In("kiwi.core.semanticIndexingService")
	private SemanticIndexingService indexingService;
	
	@In("kiwi.core.statusService")
	private StatusService statusService;

	private static int numRuns = 0;
	
	/**
	 * Run the creation of the semantic index in regular intervals.
	 * 
	 * This method is scheduled from SemanticIndexingScheduleController and by default runs every 30 minutes.
	 * 
	 * @param interval
	 * @return
	 */
	@Asynchronous
	public QuartzTriggerHandle scheduleIndexing(@IntervalCron String cron) {
		// skip first run of system start to avoid locking problems with SOLR
		if(numRuns > 0) {
			
			indexingService.reIndex();
			
		}
		numRuns++;
		return null;
	}

}
