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
package kiwi.service.reasoning.reasonmaintenance;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.index.IndexService;
import org.neo4j.index.lucene.LuceneIndexService;

/** Seam factory to create neo IndexService as necessary.
 * 
 * @author Jakub Kotowski
 *
 */
@Name("RMS.neoIndexService")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class NeoIndexServiceFactory {
	@In("RMS.neo") private GraphDatabaseService neo;
	private IndexService indexService;
	private @Logger Log log;
	
	@In(value="reasonMaintenanceRunning",create=false,required=false)
	private Boolean reasonMaintenanceRunning = false;
	
	@Unwrap
	public synchronized IndexService createIndexService() {
		if (indexService == null) {
			log.info("Initializing Neo4j's Lucene index service ...");
			indexService = new LuceneIndexService( neo );
			log.info("Neo4js Lucene index service initialized."+Thread.currentThread());
		}
		
		return indexService;
	}
	
	@Destroy
	public synchronized void destroy() {
		if (reasonMaintenanceRunning != null)
			while (reasonMaintenanceRunning) {
				log.info("Waiting for reason maintenance to finish ...");
				try {
					Thread.sleep(1000);
					reasonMaintenanceRunning = (Boolean) Contexts.getApplicationContext().get("reasonMaintenanceRunning");					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		else
			log.debug("reasonMaintenanceRunning was null");
		
		log.info("Shutting down Neo4j's Lucene index service ...");
		if (indexService != null)
			indexService.shutdown();
		else
			log.warn("Index service was null. Can't shut it down.");
	}	
}












