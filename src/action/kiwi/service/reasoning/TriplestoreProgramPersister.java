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
 * Contributor(s): Jakub Kotowski
 * 
 * 
 */
package kiwi.service.reasoning;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.service.reasoning.ast.Namespace;
import kiwi.service.reasoning.ast.Program;
import kiwi.service.reasoning.ast.Rule;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

//TODO typed literals must be persisted properly, ... (~ similar as we have to expand namespaces for uris before persisting)
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Name("programPersister")
public class TriplestoreProgramPersister {
	
	@Logger
	private Log log;
	@In
	private TripleStore tripleStore;	
 	
 	@In private EntityManager entityManager;

 	@SuppressWarnings("unchecked")
 	/** Removes all triples of the form (subject, property, *)*/
	private void deleteTriples(KiWiResource subject, KiWiUriResource property) {
 		Query q = entityManager.createNamedQuery("tripleStore.tripleBySP");
 		q.setParameter("subject", subject);
 		q.setParameter("property", property);
 		
 		List<Object> result = q.getResultList();
 		
 		for (Object obj : result)
 			tripleStore.removeTriple((KiWiTriple)obj); 		
 	}
 	
 	/** Removes all triples of the form (program, kiwi:hasRule, *)*/
 	private void deleteRulesOf(KiWiUriResource program) {
 		KiWiUriResource hasRule = tripleStore.createUriResource(ReasoningConstants.KIWI_PROGRAM_HAS_RULE);

 		deleteTriples(program, hasRule);
 	}
 	
 	/** Removes all triples of the form (program, kiwi:hasNamespace, *)*/
 	private void deleteNamespacesOf(KiWiUriResource program) {
 		KiWiUriResource hasNamespace = tripleStore.createUriResource(ReasoningConstants.KIWI_PROGRAM_HAS_NAMESPACE);

 		deleteTriples(program, hasNamespace);
 	}
 	
 	/** Stores compiled program in the form of triples to the triple store.
 	 * 
 	 * If the program was stored before it will be rewritten.
 	 * 
 	 * @param program
 	 */
	public void persist(Program program) {
		log.info("Persisting program #0",program.getName());
		//log.debug("program: #0 ",program.toString());
		
		KiWiUriResource programUri = tripleStore.createUriResource(program.getName());
		KiWiUriResource property = tripleStore.createUriResource(ReasoningConstants.RDF_TYPE);
		KiWiUriResource object = tripleStore.createUriResource(ReasoningConstants.KIWI_SKWRL_PROGRAM);
		
		KiWiTriple triple = tripleStore.createTriple(programUri, property, object);

		if (!triple.isNew()) {
			log.info("Program #0 was already stored in the triple store. Rewriting.", program.getName());
		}
		
		//make sure nothing interferes
		deleteRulesOf(programUri);
		deleteNamespacesOf(programUri);
		
		//store the program
		property = tripleStore.createUriResource(ReasoningConstants.KIWI_PROGRAM_HAS_NAMESPACE);
		for (Namespace ns : program.getNamespaces()) {
			KiWiLiteral literal = tripleStore.createLiteral(ns.toString());
			triple = tripleStore.createTriple(programUri, property, literal);
			//log.debug("Created triple: #0", triple);
		}		

		property = tripleStore.createUriResource(ReasoningConstants.KIWI_PROGRAM_HAS_RULE);
		for (Rule rule : program.getRules()) {
			KiWiLiteral literal = tripleStore.createLiteral(rule.toString());
			triple = tripleStore.createTriple(programUri, property, literal);
			//log.debug("Created triple: #0", triple);
		}		
	}
}
