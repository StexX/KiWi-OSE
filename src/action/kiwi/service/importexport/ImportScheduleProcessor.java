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
package kiwi.service.importexport;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.importexport.ImportService;
import kiwi.api.system.StatusService;
import kiwi.model.importexport.ImportTask;
import kiwi.model.status.SystemStatus;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.IntervalDuration;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.jboss.seam.log.Log;

/**
 * A component that processes scheduled import tasks.
 *
 * @author Sebastian Schaffert
 * @see ImportScheduleController
 */
@Name("kiwi.core.importScheduleProcessor")
@AutoCreate
@Scope(ScopeType.APPLICATION)
//@Transactional
public class ImportScheduleProcessor {

	@Logger
	private Log log;
	
	@In("kiwi.core.importService")
	private ImportService importService;
	
	@In("kiwi.core.statusService")
	private StatusService statusService;
	
	@In
	private EntityManager entityManager;
	
	private boolean running = false;

	/**
	 * Run the recurring imports using the interval in milliseconds as interval between calls.
	 * Iterates over all ImportTasks and checks whether they need to be run.
	 * 
	 * This method is scheduled from ImportScheduleController and by default runs every minute.
	 * 
	 * @param interval
	 * @return
	 */
	@Asynchronous
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public QuartzTriggerHandle scheduleRecurringImport(
			@IntervalDuration Long interval) {
		
		if(!running) {
			running = true;
		
			SystemStatus status = new SystemStatus("scheduled import");
			status.setProgress(0);
			statusService.addSystemStatus(status);

			try {
				// hibernate currently does not allow date arithmetics :-(
				//Query q = entityManager.createNamedQuery("importTask.listScheduledTasks");
				Query q = entityManager.createNamedQuery("importTask.listAllTasks");
				List<ImportTask> mytasks = q.getResultList();



				Date now = new Date();

				int count = 0, progress = 0; 
				for(ImportTask t : mytasks) {
					if(now.getTime() - t.getLastExecuted().getTime() > t.getInterval().getTime()) {
						try {
							log.info("importing task no #0: #1",t.getId(), t.getDescription());
							count += importService.importData(new URL(t.getUrl()), t.getFormat(), t.getTypes(), t.getTags(), t.getOwner(), null);
							t.setLastExecuted(new Date());
						} catch(MalformedURLException ex) {
							log.error("malformed URL: #0",t.getUrl());
						} catch(Exception ex) {
							log.error("import task not executed; an exception occurred",ex);
						}
					}
					progress ++;
					status.setProgress(100 * progress / mytasks.size());
				}

//				entityManager.flush();
				log.info("#0 content items imported",count);

			} catch (SecurityException e) {
				log.error("error while committing transaction (security exception)",e);
			} catch (IllegalStateException e) {
				log.error("error while committing transaction (illegal state)",e);
			} finally {
				statusService.removeSystemStatus(status);
				running = false;
			}
		
		} else {
			log.debug("import already running; not starting a second run...");
		}
		return null;
	}
	


}
