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

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;
import org.jboss.seam.log.Log;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

/**
 * ConnectionFactory - a manager component that returns a Sesame connection to the current 
 * repository (using @Unwrap). The connection is session scoped so that each user has her
 * own connection to the repository.
 *
 * @author Sebastian Schaffert
 *
 */
@Name("tripleStore.repositoryConnection")
@Scope(ScopeType.SESSION)
@AutoCreate
public class ConnectionFactory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Get the seam logger for issuing logging statements.
	 */
	@Logger
	private static Log log;

	/**
	 * The Sesame 2 repository. We are using a native repository for efficient storage.
	 */
	@In(value="tripleStore.repository", create = true)
    private Repository repository;
	
    private RepositoryConnection con;
	

	
    /**
     * Initialise a RepositoryConnection and store it in session scope.
     * @return
     */
    @Unwrap
    public RepositoryConnection getConnection() {
    	try {
        	if(con == null || !con.isOpen()) {
 				con = repository.getConnection();
		        con.setAutoCommit(false);
        	}
		} catch (RepositoryException e) {
			log.error("error while initialising Sesame repository", e);				
		} catch(IllegalStateException ex) {
			log.warn("repository was in an illegal state; trying to reinialise...");
			try {
				repository.shutDown();
				// TODO: we need to tell RepositoryFactory to reconnect to the repository...
				
			} catch(Exception e) {}
			try {
				con = repository.getConnection();
				con.setAutoCommit(false);
			} catch(Exception e) {
				log.error("failed to reinitialise repository, giving up...");
			}
		}
    	return con;
    }

    /**
     * Shutdown the connection by calling con.close() if it is still open.
     */
    @Destroy
    public void shutdown() {
		try {
			if(con != null && con.isOpen()) {
				con.close();
			}
		} catch (RepositoryException e) {
			log.error("error while closing Sesame connection (#0)",e.getMessage());
		}
    }

}
