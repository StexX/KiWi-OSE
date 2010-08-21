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
 */
package kiwi.service.reasoning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import kiwi.model.kbase.KiWiTriple;
import kiwi.service.reasoning.ast.Rule;

/** Takes care of instantiating rules given a set of triples.
 * 
 * Tries to match each of the triples to each of the rules and creates a set of
 * rule instances for these matches.
 * 
 * @author Jakub Kotowski
 *
 */
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("ruleInstantiator")
public class RuleInstantiator {
	@In
	private RuleCompiler ruleCompiler;

	@SuppressWarnings("unused")
	@Logger
	private Log log;

	/** A wrapper class for information about how to instantiate a rule.
	 * 
	 * @author Jakub Kotowski
	 *
	 */
	class RuleInstanceInfo {
		private Map<Integer, Set<Long>> positionTriplesMap = new HashMap<Integer, Set<Long>>();
		private Rule rule;
		
		public RuleInstanceInfo(Rule rule) {
			this.rule = rule;
		}
		
		public void addPosition(Integer position) {
			if (!positionTriplesMap.containsKey(position))
				positionTriplesMap.put(position, new HashSet<Long>());
		}
		
		public void addPositions(Set<Integer> positions) {
			for (Integer position : positions)
				addPosition(position);
		}
		
		public void addTripleForPosition(Long tripleId, Integer position) {
			Set<Long> tripleIds = positionTriplesMap.get(position);
			if (tripleIds == null)
				throw new ReasoningException("Assertion failed, attempting to add a tripleId for a position that hasn't been created yet. This is a bug.");
			
			tripleIds.add(tripleId);
		}
		
		public Set<Integer> getPositions() {
			return positionTriplesMap.keySet();
		}
		
		public Set<Long> getTripleIdsForPosition(Integer position) {
			return positionTriplesMap.get(position);
		}
		
		public Rule getRule() {
			return rule;
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			
			for (Integer pos : positionTriplesMap.keySet()) {
				sb.append(",");
				sb.append(pos).append(":").append(positionTriplesMap.get(pos));
			}
			sb.replace(0, 1, "{").append("}");
			
			return rule.getName()+":"+sb.toString();
		}
	}
	
	/** Compiles a list of information about which triples match which rules and where.
	 * It is necessary for building rule instances.
	 * 
	 */
	private List<RuleInstanceInfo> compileInfoList(List<CompiledRule> rules, Set<KiWiTriple> triples) {
		List<RuleInstanceInfo> infoList = new ArrayList<RuleInstanceInfo>(rules.size());
		
		/* We have to distinguish between different matching positions
		 * 
		 * Rule:     ($1 p $2) (x p $3) -> head
		 * Matches:  (x  p  y) (x p y)
		 *           (a  p  b)
		 *           
		 * (x p y) matches two positions in the rule, (a p b) only one
		 * 
		 */	
		for (CompiledRule rule : rules) {
			RuleInstanceInfo info = new RuleInstanceInfo(rule.getRule());
			
			for (KiWiTriple triple : triples) {
				Set<Integer> matchPositions = rule.getMatchPositions(triple);
				
				if (matchPositions.isEmpty())
					continue;
				
				info.addPositions(matchPositions);
				
				for (Integer position : matchPositions) 
					info.addTripleForPosition(triple.getId(), position);				
			}

			infoList.add(info);
		}	
		return infoList;
	}
	
	/** Instantiates rules given a set of triples.
	 * 
	 * Tries to match each of the triples to each of the rules and returns a set of
	 * rule instances for these matches.
	 */	
	public List<CompiledRule> instantiate(List<CompiledRule> rules, Set<KiWiTriple> triples, int round) {
		List<RuleInstanceInfo> infoList = compileInfoList(rules, triples);
		
		List<CompiledRule> compiledInstances = new ArrayList<CompiledRule>();
		
		for (RuleInstanceInfo info : infoList) {

			for (Integer position : info.getPositions())  {
				CompiledRule newRule = ruleCompiler.compileRule(info.getRule(), position, round);

				if (!newRule.isInstance())
					throw new ReasoningException("Assertion failed. Rule "+info.getRule().getName()+
							" was compiled as an instance but the result of compilation wasn't an instance.");

				CompiledRuleInstance newRuleInstance = (CompiledRuleInstance) newRule;

				newRuleInstance.setInstantiatingTripleIds(info.getTripleIdsForPosition(position));
				compiledInstances.add(newRuleInstance);				
			}
		}
		
		return compiledInstances;
	}
}
