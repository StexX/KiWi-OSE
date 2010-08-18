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

import java.util.ArrayList;
import java.util.List;

import kiwi.api.reasoning.ProgramLoader;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/** Loads sKWRL programs persisted by {@link TriplestoreProgramPersister} from the triple store.
 * 
 * @author Jakub Kotowski
 *
 */
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("triplestoreProgramLoader")
public class TriplestoreProgramLoader extends ProgramLoader {
	@Logger
	private Log log;
	@In
	private TripleStore tripleStore;

 	/** Removes all triples of the form (program, kiwi:hasRule, *)*/
 	private List<String> loadRulesOf(KiWiUriResource program) {
 		KiWiUriResource hasRule = tripleStore.createUriResource(ReasoningConstants.KIWI_PROGRAM_HAS_RULE);

 		List<KiWiTriple> triples = tripleStore.getTriplesBySP(program, hasRule);
 		
 		List<String> rules = new ArrayList<String>();
 		
 		for (KiWiTriple triple : triples) {
 			if (!triple.getObject().isLiteral())
 				throw new ReasoningException("Unexpected node type "+triple.getObject().getClass()+" at object position of triple of form (program, hasRule, *). Triple id "+triple.getId());
 			KiWiLiteral literal = (KiWiLiteral) triple.getObject();
 			rules.add(literal.getContent());
 		}
 		
 		return rules;
 	}
 	
 	/** Removes all triples of the form (program, kiwi:hasNamespace, *)*/
 	private List<String> loadNamespacesOf(KiWiUriResource program) {
 		KiWiUriResource hasNamespace = tripleStore.createUriResource(ReasoningConstants.KIWI_PROGRAM_HAS_NAMESPACE);

 		List<KiWiTriple> triples = tripleStore.getTriplesBySP(program, hasNamespace);

 		List<String> namespaces = new ArrayList<String>();
 		
 		for (KiWiTriple triple : triples) {
 			if (!triple.getObject().isLiteral())
 				throw new ReasoningException("Unexpected node type "+triple.getObject().getClass()+" at object position of triple of form (program, hasNamespace, *). Triple id "+triple.getId());
 			KiWiLiteral literal = (KiWiLiteral) triple.getObject();
 			namespaces.add(literal.getContent());
 		}
 		
 		return namespaces;
 	}
	
 	/** Loads sKWRL program of the name name.
 	 * 
 	 * Returns an empty String if the triple store does not contain triple (name, rdf:type, kiwi:sKWRLProgram).
 	 * Otherwise returns a String representation of the program loaded from triples describing the program.
 	 */
	public String getProgramContent(String name) {
		if (name.equals(ReasoningConstants.KIWI_DEFAULT_SKWRL_PROGRAM))
			name = ReasoningConstants.KIWI_RDFS_SKWRL_PROGRAM;
			
		log.info("Loading program #0", name);
		KiWiUriResource program = tripleStore.createUriResource(name);
		KiWiUriResource property = tripleStore.createUriResource(ReasoningConstants.RDF_TYPE);
		KiWiUriResource object = tripleStore.createUriResource(ReasoningConstants.KIWI_SKWRL_PROGRAM);
		
		boolean tripleExists = tripleStore.hasTriple(program, property, object);

		if (!tripleExists) {
			log.warn("Not loading program #3. Triplestore did not contain triple #0 #1 #2", program, property, object, name);
			return "";			
		}
		
		List<String> namespaces = loadNamespacesOf(program);
		List<String> rules = loadRulesOf(program);
		
		StringBuffer sb = new StringBuffer();
		
		for (String s : namespaces)
			sb.append(s).append('\n');
		
		if (sb.length() > 0)
			sb.append('\n');
		
		for (String s : rules)
			sb.append(s).append('\n');
		
		return sb.toString();
	}
}


