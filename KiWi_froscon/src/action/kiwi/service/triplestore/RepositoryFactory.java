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
package kiwi.service.triplestore;

import java.io.File;

import kiwi.api.config.ConfigurationService;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.Unwrap;
import org.jboss.seam.log.Log;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.nativerdf.NativeStore;

/**
 * RepositoryFactory
 *
 * @author Sebastian Schaffert
 *
 */
@Name("tripleStore.repository")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Synchronized(timeout=1000000L)
public class RepositoryFactory {

	/**
	 * Get the seam logger for issuing logging statements.
	 */
	@Logger
	private static Log log;

	/**
	 * The Sesame 2 repository. We are using a native repository for efficient storage.
	 */
    private Repository repository;

    /**
     * Factory for initialising a repository ...
     * 
     * Upon creation of this component, initialise the Sesame 2 native repository.
     * @author Sebastian Schaffert
     * @see kiwi.api.triplestore.TripleStore#initialise()
     *
     */
    @Unwrap
    public Repository createRepository() {
    	if(repository == null) {
	        try {
	            long start = System.currentTimeMillis();
	 
	            ConfigurationService configurationService = (ConfigurationService) Component.getInstance("configurationService");
	            
	            log.info("KiWi: initialising Sesame 2 repository ...");
	            // initialise Sesame
	            //File sesameDataDir = new File(configurationService.getWorkDir()+File.separator+"triples");
	            String sesamePath = configurationService.getConfiguration("kiwi.triplestore.dir").getStringValue();
	            File sesameDataDir;
	            File sesameLockDir;
	            if(sesamePath != null) {
	            	sesameDataDir = new File(sesamePath);
	            	sesameLockDir = new File(sesamePath+File.separator+"lock");
	            } else {
	            	sesameDataDir = new File(configurationService.getWorkDir()+File.separator+"triples");
	            	sesameLockDir = new File(configurationService.getWorkDir()+File.separator+"triples"+File.separator+"lock");
	            }
	            // forcefully remove any existing lock directories, because sesame does not behave 
	            // correctly here (or maybe it does, but we can't help it)
	            if(sesameLockDir.exists()) {
	            	for(File f : sesameLockDir.listFiles()) {
	            		f.delete();
	            	}
	            	sesameLockDir.delete();
	            }
	            String indexes = "spoc,posc,cosp,opsc";
	            log.info("creating new sail repository, as no repository has been set up yet ...");
	            
	            NativeStore store = new NativeStore(sesameDataDir, indexes);
	            repository = new SailRepository(store);
	
	
	            repository.initialize();
	            
		        log.info("KiWi: Sesame 2 initialised successfully ( #0 ms)",System.currentTimeMillis()-start);
	 		
			} catch (RepositoryException e) {
				log.error("error while initialising Sesame repository", e);
			} 	
    	} 
    	return repository;
    	
    }

    
    @Destroy
    public void shutdown() {
		try {
			if(repository != null) {
				repository.shutDown();
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
