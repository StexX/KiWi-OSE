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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import choco.Choco;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.VariableType;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;

@SuppressWarnings("unchecked")
public class KwqlTargetVariable extends IntegerVariable {
	private Constraint constraint;
	
	private Solver solver;
	private Dictionary dictionary;

	private Set<Enum> labels = new HashSet<Enum>();
	
	public KwqlTargetVariable(String name, VariableSet<KwqlVariable> variableSet) {
		super(name, VariableType.INTEGER, Choco.MIN_LOWER_BOUND, Choco.MAX_LOWER_BOUND);
		
        for (String option : options) {
            addOption(option);
        }

		//generate constraints which assure that this variable is bound to one of the variables in variableSet
		List<Constraint> constraints = new ArrayList<Constraint>();
		
		for (Set<KwqlVariable> vars : variableSet) {
			KwqlVariable proxy = vars.iterator().next();
			
			labels.add(proxy.getDomain());
			constraints.add(Choco.eq(this, proxy));
		}

		Constraint bindings = Choco.or(constraints.toArray(new Constraint[constraints.size()]));
		
		constraint = Choco.and(bindings, Choco.neq(this, -1));
	}
	
	public void setValues(Collection<Integer> values) {
		int i = 0;
		int size = values.size();

		this.values = new int[size];

		for (Integer value : values) {
			this.values[i++] = value;
		}
		
		this.setLowB(this.values[0]);
		this.setUppB(this.values[size-1]);
	}
	
	public void setConversionObjects(Dictionary dictionary, Solver solver) {
		this.solver = solver;
		this.dictionary = dictionary;
	}

	public Constraint getConstraint() {
		return constraint;
	}

	public Set<Enum> getLabels() {
		return labels;
	}
	
	public String getValue() {
		return dictionary.translate(solver.getVar(this).getVal());
	}
}
