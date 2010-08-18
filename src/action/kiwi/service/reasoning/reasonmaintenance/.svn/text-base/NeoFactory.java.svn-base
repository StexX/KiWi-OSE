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

import java.io.File;

import kiwi.api.config.ConfigurationService;

import org.jboss.seam.Component;
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
import org.neo4j.kernel.EmbeddedGraphDatabase;

/** Seam factory to create NeoService as necessary.
 * 
 * @author Jakub Kotowski
 *
 */
@Name("RMS.neo")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class NeoFactory {
	private static String NEO_DIR_KEY = "kiwi.neo4j.dir";
	@Logger private static Log log;
	private GraphDatabaseService neo;
	
	@In(value="reasonMaintenanceRunning",create=false,required=false)
	private Boolean reasonMaintenanceRunning = false;
	
	@Unwrap
	public synchronized GraphDatabaseService createNeo() {
		if (neo == null) {
			log.info("Initializing Neo4j graph database ...");
            ConfigurationService configurationService = (ConfigurationService) Component.getInstance("configurationService");
            
            String neoPath = configurationService.getConfiguration(NEO_DIR_KEY).getStringValue();
            if(neoPath == null) {
            	neoPath = configurationService.getWorkDir()+File.separator+"neo4j";
            	configurationService.setConfiguration(NEO_DIR_KEY, neoPath);
            }

            neo = new EmbeddedGraphDatabase( neoPath );
			neo.enableRemoteShell();
			
			log.info("Neo4j initialized on path #0", neoPath);
		}
		
		return neo;
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
					log.error("Interrupted from waiting for reason maintenance to finish. ", e);
				}
			}
		else
			log.warn("reasonMaintenanceRunning was null");
		
		log.info("Shutting down neo4j ...");
		if (neo != null)
			neo.shutdown();
		else
			log.warn("Neo was null, can't shut it down.");
	}	
}



