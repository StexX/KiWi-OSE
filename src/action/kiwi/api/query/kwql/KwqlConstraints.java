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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import kiwi.service.query.kwql.parser.KWQL;

import org.antlr.runtime.tree.Tree;

import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;

/**
 * Generates a model for a constraint solver that determines variable bindings
 * and checks injectivity constraints on a query result.
 * 
 * @author Steffen Hausmann
 * 
 */
@SuppressWarnings("unchecked")
public class KwqlConstraints {
	Model model;
	Solver solver;
	
	/** contains the names of variables that occur multiple times and have dependencies between each other */
	Collection<String> interdependentVariables;
	
	/** variables that contain the value of the variables used in a kwql query */
	Map<String, KwqlTargetVariable> targetVariables;
	
	/** contains a mapping between Strings and numeric ids */
	Dictionary dictionary = new Dictionary();
	
	/** relations (containment, value) which reflect the properties of a content item */
	Map<Enum, List<int[]>> namedRelations = new HashMap<Enum, List<int[]>>();
	
	/** the domains for all kind of variables */
	Map<Enum, TreeSet<Integer>> variableDomain = new HashMap<Enum, TreeSet<Integer>>();
	
	/** the constraints that are generated from the structure of a kwql query */
	Collection<Constraint> chocoConstraints = new ArrayList<Constraint>();
	
	/** constraints which ensure that either variables have the demanded values or are unbound */
	Collection<Constraint> chocoUnboundVariableConstraints = new ArrayList<Constraint>();
	
	/** constraints which ensure the kwql injectivity criteria */
	Collection<Constraint> chocoInjectiveConstraints = new ArrayList<Constraint>();
	
	/** the actually used variables during the constraint solving (they are referenced by kwqlVariables) */ 
	Set<KwqlVariable> chocoVariables = new HashSet<KwqlVariable>();
	
	public KwqlConstraints(Tree body) {
		IntegerVariable container = createVariable(KwqlResource.ci, "ROOT");

		KwqlConstraint constraint = generateResourceConstraints(body, KwqlAxis.none, container);
		
		chocoInjectiveConstraints.addAll(constraint.generateInjectivityConstraints());
		chocoUnboundVariableConstraints.add(Choco.neq(container, -1));													//ensure that the root is always bound

		chocoConstraints.add(constraint.getChocoConstraint());
		chocoConstraints.add(constraint.generateVariableConstraints());
		
		targetVariables = constraint.getTargetVariables();
		
		for (KwqlTargetVariable var : targetVariables.values()) {
			chocoConstraints.add(var.getConstraint());
		}
		
		interdependentVariables = constraint.getInterdependentVariables();
	}
	
	private List<int[]> getRelation(Enum label) {
		List<int[]> relation = namedRelations.get(label);
		
		if (relation == null) {
			relation = new ArrayList<int[]>();
			
			namedRelations.put(label, relation);
		}
		
		return relation;
	}
	
	private KwqlVariable createVariable(Enum label, String name) {
		KwqlVariable var = new KwqlVariable(name, label);
		
		chocoVariables.add(var);
		
		return var;
	}

	private void addToDomain(Enum label, int value) {
		label = normalizeResource(label);
		
		TreeSet<Integer> values = variableDomain.get(label);
		
		if (values == null) {
			values = new TreeSet<Integer>();
			
			values.add(-1);
			
			variableDomain.put(label, values); 
		}
		
		values.add(value);
	}
	
	private void initializeVariableDomains() {
		for (KwqlVariable var : chocoVariables) {
			
			var.setValues(variableDomain.get(normalizeResource(var.getDomain())));
		}
		
		for (KwqlTargetVariable var : targetVariables.values()) {
			TreeSet<Integer> domain = new TreeSet<Integer>();
			
			for (Enum label: var.getLabels()) {
				domain.addAll(variableDomain.get(normalizeResource(label)));
			}
			
			var.setValues(domain);
		}
	}
	
	private void initializeModel() {
		for (IntegerVariable var : chocoVariables) {
			model.addVariable(var);
		}
		
		for (KwqlTargetVariable var : targetVariables.values()) {
			var.setConversionObjects(dictionary, solver);
			
			model.addVariable(var);
		}
		
		for (Constraint constraint : chocoConstraints) {
			model.addConstraint(constraint);
		}
		
		for (Constraint constraint : chocoUnboundVariableConstraints) {
			model.addConstraint(constraint);
		}
		
		if (chocoInjectiveConstraints.size() > 0) {
			model.addConstraints(chocoInjectiveConstraints.toArray(new Constraint[0]));
		}
	}
	
	private Enum normalizeResource(Enum label) {
		if (label instanceof KwqlContentItemQualifier && ((KwqlContentItemQualifier)label).isAxis()) {
			return KwqlResource.ci;
		} else if (label instanceof KwqlFragmentQualifier && ((KwqlFragmentQualifier)label).isAxis()) {
			return KwqlResource.fragment;
		} else if (label instanceof KwqlLinkQualifier && ((KwqlLinkQualifier)label).isAxis()) {
			return KwqlResource.ci;
		} else {
			return label;
		}
	}
	
	/**********************************/
	
	private KwqlResourceConstraint generateContainmentConstraints(Enum relation, String name, IntegerVariable container) {
		KwqlVariable resource = createVariable(relation, name);
		
		List<int[]> domain = getRelation(relation);
		
		Constraint containmentConstraint = Choco.feasPairAC(container, resource, domain);
		
//		System.err.println("added relation constraint: (" + container.toString() + ", " + resource.toString() + ") \\in R_" + relation.name());
		
		return new KwqlResourceConstraint(resource, containmentConstraint);
	}
	
	private KwqlConstraint addValueConstraint(Enum label, Tree node, IntegerVariable container) {
		String name = node.getParent().toStringTree();
		Integer value = dictionary.addImmutable(node.getChild(0).getText());
		
		List<int[]> relation = getRelation(label);
		
		IntegerVariable var = createVariable(label, name);
		
		chocoUnboundVariableConstraints.add(Choco.implies(Choco.eq(container, -1), Choco.eq(var, -1)));
		chocoUnboundVariableConstraints.add(Choco.implies(Choco.neq(var, -1), Choco.and(Choco.feasPairAC(container, var, relation), Choco.eq(var, value))));
		
		return new KwqlConstraint(Choco.and(Choco.feasPairAC(container, var, relation), Choco.eq(var, value)));
	}
	
	private KwqlConstraint addVariableConstraint(Enum label, Tree node, IntegerVariable container) {
		String name = node.getChild(0).getText();
		
		List<int[]> relation = getRelation(label);
		
		KwqlVariable var = createVariable(label, node.toStringTree());
		
		chocoUnboundVariableConstraints.add(Choco.implies(Choco.eq(container, -1), Choco.eq(var, -1)));
		
		return new KwqlConstraint(Choco.feasPairAC(container, var, relation), name, var);
	}
	
	private KwqlConstraint generateResourceConstraints(Tree node, KwqlAxis axis, IntegerVariable container) {
		return generateResourceConstraints(node.getChild(1), node.getChild(0).getChild(0), axis, container);
	}
	
	private KwqlConstraint generateResourceConstraints(Tree node, Tree type, KwqlAxis axis, IntegerVariable container) {
		if (type.getType() == KWQL.DISJUNCTION) {
			return generateResourceConstraints(node, type.getChild(0), axis, container).or(generateResourceConstraints(node, type.getChild(1), axis, container));
		} else if (type.getType() == KWQL.CI) {
			KwqlConstraint qualifierConstraint;
			KwqlResourceConstraint containmentConstraints;
			
			IntegerVariable resource;
			switch (axis) {
				case none:
				//	IntegerVariable resource = createVariable(KwqlResource.ci, "ROOT");
					resource=container;
					
					qualifierConstraint = generateContentItemConstraints(node, resource);

					return qualifierConstraint;
					
				case child:
					containmentConstraints = generateContainmentConstraints(KwqlContentItemQualifier.child, node.getParent().toStringTree(), container);
					qualifierConstraint = generateContentItemConstraints(node, containmentConstraints.getResource());
					
					break;
					
				case descendant:
					containmentConstraints = generateContainmentConstraints(KwqlContentItemQualifier.descendant, node.getParent().toStringTree(), container);
					qualifierConstraint = generateContentItemConstraints(node, containmentConstraints.getResource());

					break;
				
				case target:
					containmentConstraints = generateContainmentConstraints(KwqlLinkQualifier.target, node.getParent().toStringTree(), container);
					qualifierConstraint = generateContentItemConstraints(node, containmentConstraints.getResource());

					break;
				default:
					throw new IllegalArgumentException();
			}
			
			chocoInjectiveConstraints.addAll(qualifierConstraint.generateInjectivityConstraints());
			chocoUnboundVariableConstraints.add(Choco.implies(Choco.neq(containmentConstraints.getResource(), -1), qualifierConstraint.getChocoConstraint()));			//ensure that the resource is either unbound or fulfills the requested properties
			
			return containmentConstraints.addConstraints(qualifierConstraint);

		} else if (type.getType() == KWQL.FRAG) {
			KwqlConstraint qualifierConstraint;
			KwqlResourceConstraint containmentConstraints;
			
			switch (axis) {
				case none:
					containmentConstraints = generateContainmentConstraints(KwqlResource.fragment, node.getParent().toStringTree(), container);
					qualifierConstraint = generateFragmentConstraints(node, containmentConstraints.getResource());
					
					break;
					
				case child:
					containmentConstraints = generateContainmentConstraints(KwqlFragmentQualifier.child, node.getParent().toStringTree(), container);
					qualifierConstraint = generateFragmentConstraints(node, containmentConstraints.getResource());
					
					break;

				case descendant:
					containmentConstraints = generateContainmentConstraints(KwqlFragmentQualifier.descendant, node.getParent().toStringTree(), container);
					qualifierConstraint = generateFragmentConstraints(node, containmentConstraints.getResource());
					
					break;

				default:
					throw new IllegalArgumentException();
			}
			
			chocoInjectiveConstraints.addAll(qualifierConstraint.generateInjectivityConstraints());
			chocoUnboundVariableConstraints.add(Choco.implies(Choco.neq(containmentConstraints.getResource(), -1), qualifierConstraint.getChocoConstraint()));			//ensure that the resource is either unbound or fulfills the requested properties
			
			return containmentConstraints.addConstraints(qualifierConstraint);

		} else if (type.getType() == KWQL.TAG) {
			KwqlResourceConstraint containmentConstraints = generateContainmentConstraints(KwqlResource.tag, node.getParent().toStringTree(), container);
			KwqlConstraint qualifierConstraint = generateTagConstraints(node, containmentConstraints.getResource());
			
			chocoInjectiveConstraints.addAll(qualifierConstraint.generateInjectivityConstraints());
			chocoUnboundVariableConstraints.add(Choco.implies(Choco.neq(containmentConstraints.getResource(), -1), qualifierConstraint.getChocoConstraint()));			//ensure that the resource is either unbound or fulfills the requested properties
			
			return containmentConstraints.addConstraints(qualifierConstraint);

		} else if (type.getType() == KWQL.LINK) {
			return generateLinkConstraints(node, container);
		} else {
			throw new UnexpectedNodeException();
		}
	}
	
	private KwqlConstraint generateContentItemConstraints(Tree node, IntegerVariable container) {
		if (node.getType() == KWQL.NEGATION) {
			return generateContentItemConstraints(node.getChild(0), container).not();
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION) {
			return generateContentItemConstraints(node.getChild(0), container).and(generateContentItemConstraints(node.getChild(1), container));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return generateContentItemConstraints(node.getChild(0), container).or(generateContentItemConstraints(node.getChild(1), container));
		} else if (node.getType() == KWQL.RESOURCE) {
			return generateResourceConstraints(node, KwqlAxis.none, container);
		} else if (node.getType() == KWQL.QUALIFIER) {
			Tree label = node.getChild(0).getChild(0);
			
			//no qualifier given -> try them all
			if (label == null) {
				KwqlConstraint result = new KwqlConstraint(Choco.FALSE);
				
				for (KwqlContentItemQualifier qualifier : KwqlContentItemQualifier.values()) {
					if (qualifier.isValue()) {
						result = result.or(generateContentItemLabelConstraints(node.getChild(1), qualifier, container));
					}
				}

				return result;
			} else {
				return generateContentItemLabelConstraints(node.getChild(1), KwqlContentItemQualifier.valueOf(label.getText()), container);
			}
		} else {
			throw new UnexpectedNodeException();
		}
	}
	
	private KwqlConstraint generateTagConstraints(Tree node, IntegerVariable container) {
		if (node.getType() == KWQL.NEGATION) {
			return generateTagConstraints(node.getChild(0), container).not();
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION) {
			return generateTagConstraints(node.getChild(0), container).and(generateTagConstraints(node.getChild(1), container));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return generateTagConstraints(node.getChild(0), container).or(generateTagConstraints(node.getChild(1), container));
		} else if (node.getType() == KWQL.QUALIFIER) {
			Tree label = node.getChild(0).getChild(0);
			
			//no qualifier given -> try them all
			if (label == null) {
				KwqlConstraint result = new KwqlConstraint(Choco.FALSE);
				
				for (KwqlTagQualifier qualifier : KwqlTagQualifier.values()) {
					if (qualifier.isValue()) {
						result = result.or(generateTagLabelConstraints(node.getChild(1), qualifier, container));
					}
				}

				return result;
			} else {
				return generateTagLabelConstraints(node.getChild(1), KwqlTagQualifier.valueOf(label.getText()), container);
			}
		} else {
			throw new UnexpectedNodeException();
		}
	}
	
	private KwqlConstraint generateFragmentConstraints(Tree node, IntegerVariable container) {
		if (node.getType() == KWQL.NEGATION) {
			return generateFragmentConstraints(node.getChild(0), container).not();
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION) {
			return generateFragmentConstraints(node.getChild(0), container).and(generateFragmentConstraints(node.getChild(1), container));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return generateFragmentConstraints(node.getChild(0), container).or(generateFragmentConstraints(node.getChild(1), container));
		} else if (node.getType() == KWQL.RESOURCE) {
			return generateResourceConstraints(node, KwqlAxis.none, container);
		} else if (node.getType() == KWQL.QUALIFIER) {
			Tree label = node.getChild(0).getChild(0);
			
			//no qualifier given -> try them all
			if (label == null) {
				KwqlConstraint result = new KwqlConstraint(Choco.FALSE);
				
				for (KwqlFragmentQualifier qualifier : KwqlFragmentQualifier.values()) {
					if (qualifier.isValue()) {
						result = result.or(generateFragmentLabelConstraints(node.getChild(1), qualifier, container));
					}
				}

				return result;
			} else {
				return generateFragmentLabelConstraints(node.getChild(1), KwqlFragmentQualifier.valueOf(label.getText()), container);
			}
		} else {
			throw new UnexpectedNodeException();
		}
	}

	private KwqlConstraint generateLinkConstraints(Tree node, IntegerVariable container) {
		if (node.getType() == KWQL.NEGATION) {
			return generateLinkConstraints(node.getChild(0), container).not();
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION) {
			return generateLinkConstraints(node.getChild(0), container).and(generateLinkConstraints(node.getChild(1), container));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return generateLinkConstraints(node.getChild(0), container).or(generateLinkConstraints(node.getChild(1), container));
		} else if (node.getType() == KWQL.QUALIFIER) {
			Tree label = node.getChild(0).getChild(0);
			
			//no qualifier given -> try them all
			if (label == null) {
				KwqlConstraint result = new KwqlConstraint(Choco.FALSE);
				
				for (KwqlLinkQualifier qualifier : KwqlLinkQualifier.values()) {
					if (qualifier.isValue()) {
						result = result.or(generateLinkLabelConstraints(node.getChild(1), qualifier, container));
					}
				}

				return result;
			} else {
				return generateLinkLabelConstraints(node.getChild(1), KwqlLinkQualifier.valueOf(label.getText()), container);
			}
		} else if (node.getType() == KWQL.RESOURCE) {
			int resourceType = node.getChild(0).getChild(0).getType();
			
			if (resourceType == KWQL.TAG) {
				return new KwqlConstraint(Choco.FALSE);							//links are currently no real resources 
			} else {
				throw new UnexpectedNodeException();
			}
		} else {
			throw new UnexpectedNodeException();
		}
	}
	
	private KwqlConstraint generateContentItemLabelConstraints(Tree node, KwqlContentItemQualifier label, IntegerVariable container) {
		if (node.getType() == KWQL.NEGATION) {
			return generateContentItemLabelConstraints(node.getChild(0), label, container).not();
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION) {
			return generateContentItemLabelConstraints(node.getChild(0), label, container).and(generateContentItemLabelConstraints(node.getChild(1), label, container));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return generateContentItemLabelConstraints(node.getChild(0), label, container).or(generateContentItemLabelConstraints(node.getChild(1), label, container));
		} else if (node.getType() == KWQL.RESOURCE) {
			switch (label) {
				case child:
					return generateResourceConstraints(node, KwqlAxis.child, container);

				case descendant:
					return generateResourceConstraints(node, KwqlAxis.descendant, container);
					
				default:
					throw new IllegalArgumentException();
			}
		} else if (node.getType() == KWQL.STRING || node.getType() == KWQL.INTEGER || node.getType() == KWQL.UR) {
			return addValueConstraint(label, node, container);
		} else if (node.getType() == KWQL.VAR) {
			return addVariableConstraint(label, node, container);
		} else {
			throw new UnexpectedNodeException();
		}
	}
	
	private KwqlConstraint generateFragmentLabelConstraints(Tree node, KwqlFragmentQualifier label, IntegerVariable container) {
		if (node.getType() == KWQL.NEGATION) {
			return generateFragmentLabelConstraints(node.getChild(0), label, container).not();
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION) {
			return generateFragmentLabelConstraints(node.getChild(0), label, container).and(generateFragmentLabelConstraints(node.getChild(1), label, container));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return generateFragmentLabelConstraints(node.getChild(0), label, container).or(generateFragmentLabelConstraints(node.getChild(1), label, container));
		} else if (node.getType() == KWQL.RESOURCE) {
			switch (label) {
				case child:
					return generateResourceConstraints(node, KwqlAxis.child, container);

				case descendant:
					return generateResourceConstraints(node, KwqlAxis.descendant, container);
					
				default:
					throw new IllegalArgumentException();
			}
		} else if (node.getType() == KWQL.STRING || node.getType() == KWQL.INTEGER || node.getType() == KWQL.UR) {
			return addValueConstraint(label, node, container);
		} else if (node.getType() == KWQL.VAR) {
			return addVariableConstraint(label, node, container);
		} else {
			throw new UnexpectedNodeException();
		}
	}
	
	private KwqlConstraint generateLinkLabelConstraints(Tree node, KwqlLinkQualifier label, IntegerVariable container) {
		if (node.getType() == KWQL.NEGATION) {
			return generateLinkLabelConstraints(node.getChild(0), label, container).not();
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION) {
			return generateLinkLabelConstraints(node.getChild(0), label, container).and(generateLinkLabelConstraints(node.getChild(1), label, container));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return generateLinkLabelConstraints(node.getChild(0), label, container).or(generateLinkLabelConstraints(node.getChild(1), label, container));
		} else if (node.getType() == KWQL.RESOURCE) {
			return generateResourceConstraints(node, KwqlAxis.target, container);
		} else if (node.getType() == KWQL.STRING || node.getType() == KWQL.INTEGER || node.getType() == KWQL.UR) {
			return addValueConstraint(label, node, container);
		} else if (node.getType() == KWQL.VAR) {
			return addVariableConstraint(label, node, container);
		} else {
			throw new UnexpectedNodeException();
		}
	}
	
	private KwqlConstraint generateTagLabelConstraints(Tree node, Enum label, IntegerVariable container) {
		if (node.getType() == KWQL.NEGATION) {
			return generateTagLabelConstraints(node.getChild(0), label, container).not();
		} else if (node.getType() == KWQL.AND || node.getType() == KWQL.CONJUNCTION) {
			return generateTagLabelConstraints(node.getChild(0), label, container).and(generateTagLabelConstraints(node.getChild(1), label, container));
		} else if (node.getType() == KWQL.DISJUNCTION) {
			return generateTagLabelConstraints(node.getChild(0), label, container).or(generateTagLabelConstraints(node.getChild(1), label, container));
		} else if (node.getType() == KWQL.STRING || node.getType() == KWQL.INTEGER || node.getType() == KWQL.UR) {
			return addValueConstraint(label, node, container);
		} else if (node.getType() == KWQL.VAR) {
			return addVariableConstraint(label, node, container);
		} else {
			throw new UnexpectedNodeException();
		}
	}

	/**********************************************/
	
	/**
	 * Add an id of a resource to the set of possible ids for a resource.
	 * 
	 * This information is later used to set the scope of the variables.
	 * 
	 * @param relation either a label name (child, descendant) or a resource type (tag, link, fragment)
	 * @param id the id of the resource
	 */
	private void addResourceId(Enum relation, int id) {
		addToDomain(relation, id);
	}
	
	public void setRootId(int id) {
		if (variableDomain.containsKey(KwqlResource.ci)) {
			throw new IllegalArgumentException();
		}
		
		addToDomain(KwqlResource.ci, id);
	}
	
	public void addToValues(Enum label, int id, String[] values) {
		List<int[]> relation = getRelation(label);
		
//		System.err.println(String.format("(%d, %s) added to R_%s (labelValues)", id, Arrays.toString(values), label.name()));

		if (values != null) {
			for (String value : values) {
				Integer ordinal = dictionary.addVolatile(value);
				
				addToDomain(label, ordinal);
				relation.add(new int[] {id, ordinal});
			}
		} else {
			addToDomain(label, -1);
			relation.add(new int[] {id, -1});
		}
	}
	
	public void addToRelation(Enum name, int container, int resource) {
		List<int[]> relation = namedRelations.get(name);
		
		addResourceId(name, resource);
		
		if (relation == null) {
			throw new IllegalArgumentException();
		}
		
		for (int[] entry : relation) {
			if (entry[0] == container && entry[1] == resource) {
//				System.err.println(String.format("(%d, %d) already contained in R_%s (labelDomains)", container, resource, name.name()));
				
				return;
			}
		}
		
//		System.err.println(String.format("(%d, %d) added to R_%s (labelDomains)", container, resource, name.name()));
		
		relation.add(new int[] { container, resource });
	}
	
	public void addConstraint(Constraint constraint) {
		chocoConstraints.add(constraint);
	}
	
	public void clearValues() {
		dictionary.clear();
		variableDomain.clear();
		
		for (Map.Entry<Enum, List<int[]>> entry : namedRelations.entrySet()) {
			entry.getValue().clear();
		}
	}
	
	public boolean solve() {
		solver = new CPSolver();
		model = new CPModel();
		
		initializeVariableDomains();
		initializeModel();
		
		solver.read(model);
		
		return solver.solve();
	}
	
	public boolean nextSolution() {
		return solver.nextSolution();
	}
	
	public boolean containsNontrivialConstrains() {
		return !targetVariables.isEmpty() || !chocoInjectiveConstraints.isEmpty();
	}
	
	public String variableBindingsToString() {
		StringBuilder sb = new StringBuilder();
		
		for (KwqlTargetVariable var : targetVariables.values()) {
			sb.append("$");
			sb.append(var.getName());
			sb.append(" = ");
			sb.append(var.getValue());
			sb.append(", ");
		}

		for (KwqlVariable var : chocoVariables) {
			sb.append(var.getName() + ": " + solver.getVar(var).getVal() + ", ");
		}

		sb.setLength(sb.length()-2);
		
		return sb.toString();
	}
	
	public Collection<String> getInterdependentVariables() {
		return interdependentVariables;
	}
	
	public Map<String, KwqlTargetVariable> getTargetVariables() {
		return targetVariables;
	}
}

class Dictionary {
	private Integer nextId = 0;
	
	private Map<String, Integer> immutableMapping = new TreeMap<String, Integer>();
	
	private Map<Integer, String> inverseImmutableMapping = new TreeMap<Integer, String>();
	
	private Map<String, Integer> volatileMapping = new TreeMap<String, Integer>();
	
	private Map<Integer, String> inverseVolatileMapping = new TreeMap<Integer, String>();
	
	private Integer add(String value, Map<String, Integer> mapping, Map<Integer, String> inverseMapping) {
		value = value.toLowerCase();
		
		if (mapping.containsKey(value)) {
			return mapping.get(value);
		} else {
			Integer id = nextId++;
			
			mapping.put(value, id);
			inverseMapping.put(id, value);
			
			return id;
		}
	}
	
	public String translate(Integer id) {
		if (inverseVolatileMapping.containsKey(id)) {
			return inverseVolatileMapping.get(id);
		} else if (inverseImmutableMapping.containsKey(id)) {
			return inverseImmutableMapping.get(id);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public Integer addVolatile(String value) {
		value = value.toLowerCase();
		
		if (immutableMapping.containsKey(value)) {
			return immutableMapping.get(value);
		} else {
			return add(value, volatileMapping, inverseVolatileMapping);
		}
	}
	
	public Integer addImmutable(String value) {
		return add(value, immutableMapping, inverseImmutableMapping);
	}
	
	public void clear() {
		nextId = immutableMapping.size();
		
		volatileMapping.clear();
		inverseVolatileMapping.clear();
	}
}