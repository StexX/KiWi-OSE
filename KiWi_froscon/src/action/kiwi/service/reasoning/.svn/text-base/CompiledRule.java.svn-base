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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kiwi.model.kbase.KiWiTriple;
import kiwi.service.reasoning.ast.Conjunction;
import kiwi.service.reasoning.ast.ConstructionVariable;
import kiwi.service.reasoning.ast.Formula;
import kiwi.service.reasoning.ast.Rule;
import kiwi.service.reasoning.ast.TriplePattern;
import kiwi.service.reasoning.ast.Variable;

/** Represents compiled rules.
 * 
 * 
 * @author Jakub Kotowski
 *
 */
public class CompiledRule {
	private Conjunction body;
	private Conjunction head;
	private Rule rule;
	private Set<Variable> constructionVariables = new HashSet<Variable>();
	private boolean instance = false;
	private boolean hasNoSharedVariables = false;
	public String subquery = null;

	private String query = null;
		
	private List<Variable> selectVariables = new ArrayList<Variable>();
	
	public CompiledRule(Rule rule) {
		setRule(rule);
	}
	
	public String toString() {
		return rule.getName();
	}
	
	/** Adds a new selection variable to the list of selection variables.
	 * It maintains the order in which variables were added.
	 */
	public void addSelectVariable(Variable variable) {
		selectVariables.add(variable);
	}
	
	public List<Variable> getSelectVariables() {
		return selectVariables;
	}
				
	public void setSelectVariables(List<Variable> selectVariables) {
		this.selectVariables = selectVariables;
	}

	/** Adds a construction variable to the list of construction variables.
	 * Construction variable is such a variable that is in the rule body but not in the rule head.
	 * (i.e. it can be either a {@link Variable} or a {@link ConstructionVariable})
	 * 
	 */
	public void addConstructionVariable(Variable variable) {
		constructionVariables.add(variable);
	}
	
	public boolean isConstructionVariable(Variable variable) {
		return constructionVariables.contains(variable);
	}
			
	public Conjunction getHead() {
		return head;
	}

	public void setHead(Conjunction head) {
		this.head = head;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
		setHead((Conjunction) rule.getHead().getFormula());
		setBody((Conjunction) rule.getBody().getFormula());
		
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public boolean matchesBody(KiWiTriple triple) {		
		for (Formula formula : body.getFormulas()) { 
			if (((TriplePattern)formula).matches(triple))
				return true;
		}
		
		return false;
	}
	
	public Set<Integer> getMatchPositions(KiWiTriple triple) {
		Set<Integer> set = new HashSet<Integer>();
		
		int i = 1;
		for (Formula formula : body.getFormulas()) {
			TriplePattern tp = (TriplePattern) formula;
			if (tp.matches(triple))
				set.add(i);
			i++;
		}
		
		return set;
	}
	
	/**
	 *  @see kiwi.service.reasoning.ast.Formula#isGround()
	 */
	public boolean isGround() {
		return body.isGround();
	}

	public boolean constructsNewResources() {
		return ! constructionVariables.isEmpty();
	}

	public Conjunction getBody() {
		return body;
	}

	public void setBody(Conjunction body) {
		this.body = body;
	}	
	
	public String getName() {
		return rule.getName();
	}

	public boolean isInstance() {
		return instance;
	}

	public void setInstance(boolean instance) {
		this.instance = instance;
	}
	
	public boolean isConstraintRule() {
		return getRule().getHead().isInconsistency();
	}

	public boolean hasNoSharedVariables() {
		return hasNoSharedVariables;
	}

	public void setHasNoSharedVariables(boolean hasNoSharedVariables) {
		this.hasNoSharedVariables = hasNoSharedVariables;
	}	
	
	public boolean isSatisfiableBy(TBox tbox) {		
		for (Formula formula : body.getFormulas()) {
			TriplePattern tp = (TriplePattern) formula;
			
			if (TBox.isTerminological(tp) && !tbox.isSatisfiable(tp))
				return false;				
		}
		
		return true;
	}
}


