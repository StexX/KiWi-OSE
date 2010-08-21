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

import java.util.List;

import kiwi.service.reasoning.ast.Atom;
import kiwi.service.reasoning.ast.Conjunction;
import kiwi.service.reasoning.ast.ConstructionVariable;
import kiwi.service.reasoning.ast.Disjunction;
import kiwi.service.reasoning.ast.Formula;
import kiwi.service.reasoning.ast.Namespace;
import kiwi.service.reasoning.ast.Negation;
import kiwi.service.reasoning.ast.Program;
import kiwi.service.reasoning.ast.Rule;
import kiwi.service.reasoning.ast.Term;
import kiwi.service.reasoning.ast.TriplePattern;
import kiwi.service.reasoning.ast.Uri;
import kiwi.service.reasoning.ast.Variable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/** ProgramCompiler is responsible for siple rule program compilation.
 * 
 * Expands namespaces, translates constraint rules, compiles rules.
 * 
 * @author Jakub Kotowski
 *
 */
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Name("programCompiler")
public class ProgramCompiler {
	private static final String DEFAULT_RDF_GRAPH_CI = "http://www.kiwi-project.eu/kiwi/core/RDFContentItem/default";
	private static final String KIWI_HAS_ANNOTATION = "http://www.kiwi-project.eu/kiwi/core/hasAnnotation";
	private static final String RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static final String KIWI_INCONSISTENCY = "http://www.kiwi-project.eu/kiwi/core/inconsistency";
	
	@Logger
	private Log log;
	
	@In
	private RuleCompiler ruleCompiler;
	
	/** Translate rules of the form ... -> inconsistency to rules of the form ... -> (xyz kiwi:hasAnnotation a), (a rdf:type kiwi:inconsistency).
	 * 
	 * ... where a is a construction variable - variable not present in the rule body and for which therefore a new resource will be created.
	 * @param rules
	 */
	private void translateConstraintRules(List<Rule> rules) {
		for (Rule rule : rules) {
			if (! rule.getHead().isInconsistency())
				continue;
			
			Conjunction conjunction = new Conjunction();
			TriplePattern triplePattern = new TriplePattern();
			
			Term subject = new Uri(DEFAULT_RDF_GRAPH_CI);
			Term property = new Uri(KIWI_HAS_ANNOTATION);
			
			Variable freshVar = rule.createFreshVariable();
			Term object = new ConstructionVariable(freshVar, KIWI_INCONSISTENCY+"/");
			
			triplePattern.setSubject(subject);
			triplePattern.setProperty(property);
			triplePattern.setObject(object);
			
			conjunction.addFormula(triplePattern);
			
			
			triplePattern = new TriplePattern();
			subject = new ConstructionVariable(freshVar, KIWI_INCONSISTENCY+"/");
			property = new Uri(RDF_TYPE);
			object = new Uri(KIWI_INCONSISTENCY);
			
			triplePattern.setSubject(subject);
			triplePattern.setProperty(property);
			triplePattern.setObject(object);
			
			conjunction.addFormula(triplePattern);

			rule.getHead().setFormula(conjunction); 
		}
	}
	
	public static Program expandNamespaces(Program program) {
		List<Rule> parsedRules = program.getRules();		
		
		for (Rule rule : parsedRules) 
			expandNamespaces(rule, program);

		return program;
	}
	
	/** Compiles Program into CompiledProgram, as a side-effect namespaces are expanded for Program.
	 * 
	 * @param program
	 * @return
	 */
	public CompiledProgram compileProgram(Program program) {
		if(program == null) {
			return null;
		}
		List<Rule> parsedRules = program.getRules();		

		expandNamespaces(program);
		
		translateConstraintRules(parsedRules);
		
		//print(parsedRules);
		
		List<CompiledRule> compiledRules = ruleCompiler.compileRules(parsedRules);
		
		CompiledProgram compiledProgram = new CompiledProgram();
		
		compiledProgram.setRules(compiledRules);
		compiledProgram.setNamespaces(program.getNamespaces());
		
		return compiledProgram;
	}

	private void print(List<Rule> parsedRules) {
		for (Rule rule : parsedRules) {
			log.debug("Rule "+rule);
		}
	}

	private static void expandNamespace(Term term, Program program) {
		if (term.isLiteral())
			return;
		
		if (term.isVariable())
			return;
				
		Uri uri = (Uri) term;
		
		if (!uri.isPrefixed())
			return;
		
		Namespace ns = program.getNamespaceForPrefix(uri.getPrefix());
		uri.setUri(ns.getUri() + uri.getLocalName());
	}
	
	private static void expandNamespaces(Formula formula, Program program) {
		switch (formula.getFormulaType()) {
			case ATOM:
				Atom atom = (Atom) formula;
				switch(atom.getAtomType()) {
					case TRIPLE_PATTERN:
						TriplePattern triple = (TriplePattern) atom;
						expandNamespace(triple.getSubject(), program);
						expandNamespace(triple.getProperty(), program);
						expandNamespace(triple.getObject(), program);
				}
				break;
			case CONJUNCTION:
				Conjunction conjunction = (Conjunction) formula;
				for (Formula subformula : conjunction.getFormulas())
					expandNamespaces(subformula, program);
				break;
			case DISJUNCTION:
				Disjunction disjunction = (Disjunction) formula;
				for (Formula subformula : disjunction.getFormulas())
					expandNamespaces(subformula, program);
				break;
			case NEGATION:
				Negation negation = (Negation) formula;
				expandNamespaces(negation.getFormula(), program);
				break;
			default:
				throw new IllegalArgumentException("Formula type "+formula.getFormulaType()+" is not implemented yet!");
		}
	}
	
	private static void expandNamespaces(Rule rule, Program program) {
		expandNamespaces(rule.getBody().getFormula(), program);
		
		if ( ! rule.getHead().isInconsistency() ) 
			expandNamespaces(rule.getHead().getFormula(), program);
	}	
}
