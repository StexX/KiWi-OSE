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
package kiwi.api.triplestore;

import java.util.Collection;

import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.kbase.KiWiTriple;

/**
 * The TripleStorePersister is a helper component for the TripleStore and the KiWiSynchronization.
 * <p>
 * The TripleStorePersister really stores the triple data in the database and Sesame repository, 
 * while the TripleStore itself performs all operations only in memory, to be persisted on
 * transaction commit by KiWiSynchronization.beforeCompletion()
 * <p>
 * TripleStorePersister should only be used by developers that know what they are doing. All triple
 * operations should be accessed solely through TripleStore itself.
 *
 * @author Sebastian Schaffert
 *
 */
public interface TripleStorePersister {

	/**
	 * Store a triple in the database and Sesame repository by calling entityManager.persist and
	 * RepositoryConnection.add; should only be called on transaction commit.
	 * 
	 * @param triple
	 */
	public void storeTriple(KiWiTriple triple);
	
	/**
	 * Batch operation to store a collection of triples in the database and Sesame repository by
	 * calling entityManager.persist and RepositoryConnection.add; should only be called on
	 * transaction commit.
	 * 
	 * @param triples
	 */
	public void storeTriples(Collection<KiWiTriple> triples);
	
	/**
	 * Mark a triple as deleted in the database and remove it from the Sesame repository. Should
	 * only be called on transaction commit.
	 * 
	 * @param triple
	 */
	public void removeTriple(KiWiTriple triple);
	
	/**
	 * Batch operation to mark a collection of triples as deleted in the database and remove them 
	 * from the Sesame repository. Should only be called on transaction commit.
	 * @param triples
	 */
	public void removeTriples(Collection<KiWiTriple> triples);
	
	/**
	 * Store a namespace in the database and Sesame repository by calling entityManager.persist and
	 * RepositoryConnection.setNamespace. Should only be called on transaction commit.
	 * @param ns
	 */
	public void storeNamespace(KiWiNamespace ns);
	
	/**
	 * Mark a namespace as deleted in the database and remove it from the Sesame repository by
	 * calling RepositoryConnection.removeNamespace. Should only be called on transaction commit.
	 * @param ns
	 */
	public void removeNamespace(KiWiNamespace ns);
	
		
	/**
	 * Flush the triple store by writing all unsaved changes to the file system.
	 */
	public void flush();
	
	public void rollback();

}
