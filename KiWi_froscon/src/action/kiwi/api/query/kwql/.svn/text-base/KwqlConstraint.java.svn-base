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
package kiwi.api.query.kwql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import choco.Choco;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.integer.IntegerVariable;

@SuppressWarnings("unchecked")
public class KwqlConstraint {
	Constraint chocoConstraint;
	
	Map<String, VariableSet<KwqlVariable>> kwqlVariables = new HashMap<String, VariableSet<KwqlVariable>>();
	Map<Enum, VariableSet<IntegerVariable>> resourceVariables = new HashMap<Enum, VariableSet<IntegerVariable>>();

	public KwqlConstraint(Constraint chocoConstraint) {
		this.chocoConstraint = chocoConstraint;
	}
	
	public KwqlConstraint(Constraint constraint, String name, KwqlVariable variable) {
		chocoConstraint = constraint;
		
		kwqlVariables.put(name, new VariableSet<KwqlVariable>(variable));
	}
	
	public KwqlConstraint not() {
		chocoConstraint = Choco.not(chocoConstraint);
		
		return this;
	}
	
	public KwqlConstraint addConstraints(KwqlConstraint constraint) {
		chocoConstraint = Choco.and(chocoConstraint, constraint.chocoConstraint);

		if (kwqlVariables.size() > 0) {
			throw new IllegalArgumentException();
		}
		
		kwqlVariables.putAll(constraint.kwqlVariables);
		
		return this;
	}
	
	private <T, S> void crossProduct(Map<S, VariableSet<T>> map1, Map<S, VariableSet<T>> map2) {
		for (Map.Entry<S, VariableSet<T>> entry : map1.entrySet()) {									//find all matching keys
			if (map2.containsKey(entry.getKey())) {
				entry.getValue().crossProduct(map2.get(entry.getKey()));
			}
		}
		
		for (Map.Entry<S, VariableSet<T>> entry : map2.entrySet()) {									//find keys in constraint.variables that were not contained in variables 
			if (! map1.containsKey(entry.getKey())) {
				map1.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private <T, S> void union(Map<S, VariableSet<T>> map1, Map<S, VariableSet<T>> map2) {
		for (Map.Entry<S, VariableSet<T>> entry : map1.entrySet()) {								//find all matching keys
			if (map2.containsKey(entry.getKey())) {
				entry.getValue().union(map2.get(entry.getKey()));
			}
		}
		
		for (Map.Entry<S, VariableSet<T>> entry : map2.entrySet()) {								//find keys in constraint.variables that were not contained in variables 
			if (! map1.containsKey(entry.getKey())) {
				map1.put(entry.getKey(), entry.getValue());
			}
		}
	}

	
	public KwqlConstraint and(KwqlConstraint constraint) {
		chocoConstraint = Choco.and(chocoConstraint, constraint.chocoConstraint);

		crossProduct(kwqlVariables, constraint.kwqlVariables);
		crossProduct(resourceVariables, constraint.resourceVariables);
		
		return this;
	}

	public KwqlConstraint or(KwqlConstraint constraint) {
		chocoConstraint = Choco.or(chocoConstraint, constraint.chocoConstraint);

		union(kwqlVariables, constraint.kwqlVariables);
		union(resourceVariables, constraint.resourceVariables);

		return this;
	}
	
	public List<Constraint> generateInjectivityConstraints() {
		ArrayList<Constraint> constraints = new ArrayList<Constraint>();
		
		for (Map.Entry<Enum, VariableSet<IntegerVariable>> entry : resourceVariables.entrySet()) {
			for (Set<IntegerVariable> vars : entry.getValue()) {
				int size = vars.size();

				if (size > 1) {
					List<Constraint> neq = new ArrayList<Constraint>(size);
					
					for (IntegerVariable var : vars) {
						neq.add(Choco.neq(var, -1));
					}

					Constraint allBound =  Choco.and(neq.toArray(new Constraint[size]));
					Constraint allDifferent = Choco.allDifferent(vars.toArray(new IntegerVariable[size]));
					
					constraints.add(Choco.implies(allBound, allDifferent));
				} 
			}
		}
		
		return constraints;
	}
	
	public Constraint generateVariableConstraints() {
		List<Constraint> constraints = new ArrayList<Constraint>();
		
		//assure that all variables which should be equal are actually equal
		for (Map.Entry<String, VariableSet<KwqlVariable>> entry : kwqlVariables.entrySet()) {
			for (Set<KwqlVariable> vars : entry.getValue()) {
				int size = vars.size();
				
				if (size > 1) {
					List<Constraint> neq = new ArrayList<Constraint>(size);
					List<Constraint> eq = new ArrayList<Constraint>(size-1);
					
					for (KwqlVariable var : vars) {
						neq.add(Choco.neq(var, -1));
					}
					
					Iterator<KwqlVariable> it = vars.iterator();

					KwqlVariable next;
					KwqlVariable var = it.next();
					
					while (it.hasNext()) {
						next = it.next();

						eq.add(Choco.eq(var, next));
						
						var = next;
					}
					
					Constraint allBound = Choco.and(neq.toArray(new Constraint[size]));
					Constraint allEqual = Choco.and(eq.toArray(new Constraint[size-1]));
					
					constraints.add(Choco.and(allBound, allEqual));
				}
			}
		}

		//assure that in the case of equally named variables in combination with disjunctions only one variable is bound at a time
		for (Map.Entry<String, VariableSet<KwqlVariable>> entry : kwqlVariables.entrySet()) {
			int i = 0;
			int size = entry.getValue().size();
			
			if (size > 1) {
				KwqlVariable[] proxies = new KwqlVariable[size];
				
				for (Set<KwqlVariable> vars : entry.getValue()) {
					proxies[i++] = vars.iterator().next();
				}
	
				constraints.add(Choco.occurrence(-1, Choco.constant(size-1), proxies));
			}
		}
		
		return Choco.and(constraints.toArray(new Constraint[constraints.size()]));
	}
	
	public Map<String, KwqlTargetVariable> getTargetVariables() {
		Map<String, KwqlTargetVariable> variables = new HashMap<String, KwqlTargetVariable>();
		
		for (Map.Entry<String, VariableSet<KwqlVariable>> entry : kwqlVariables.entrySet()) {
			variables.put(entry.getKey(), new KwqlTargetVariable(entry.getKey(), entry.getValue()));
		}
		
		return variables;
	}

	public Set<String> getInterdependentVariables() {
		Set<String> variables = new HashSet<String>();
		
		variable: for (Map.Entry<String, VariableSet<KwqlVariable>> entry : kwqlVariables.entrySet()) {
			for (Set<KwqlVariable> vars : entry.getValue()) {
				if (vars.size() > 1) {
					variables.add(entry.getKey());
					
					continue variable;
				}
			}
		}
		
		return variables;
	}
	
	public Constraint getChocoConstraint() {
		return chocoConstraint;
	}
}

class KwqlResourceConstraint extends KwqlConstraint {
	private IntegerVariable resource;
	
	public KwqlResourceConstraint(KwqlVariable resource, Constraint chocoConstraint) {
		super(chocoConstraint);
		
		this.resource = resource;
		
		resourceVariables.put(resource.getDomain(), new VariableSet<IntegerVariable>(resource));
	}
	
	public IntegerVariable getResource() {
		return resource;
	}
}

class VariableSet<T> extends HashSet<HashSet<T>> {
	public VariableSet() { }
	
	public VariableSet(T var) {
		HashSet<T> set = new HashSet<T>();
		
		set.add(var);
		add(set);
	}
	
	public void union(VariableSet<T> variableSet) {
		addAll(variableSet);
	}
	
	public void crossProduct(VariableSet<T> variableSet) {
		VariableSet<T> result = new VariableSet<T>();
		
		for (HashSet<T> outer : this) {
			for (HashSet<T> inner : variableSet) {
				HashSet<T> element = new HashSet<T>(outer);
				
				element.addAll(inner);
				result.add(element);
			}
		}
		
		clear();
		addAll(result);
	}
}