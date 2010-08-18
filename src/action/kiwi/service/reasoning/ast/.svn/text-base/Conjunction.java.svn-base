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
package kiwi.service.reasoning.ast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/** Abstract syntax tree node of a conjunction of formulas. 
 * @author Jakub Kotowski
 *
 */
public class Conjunction extends Formula {
	private List<Formula> formulas;
	
	public Conjunction() {
		formulas = new ArrayList<Formula>();
	}
	
	public List<Formula> getFormulas() {
		return formulas;
	}

	public void setFormulas(List<Formula> formulas) {
		if (formulas == null)
			throw new IllegalArgumentException("Formulas in a conjunction can't be set to null.");
		
		this.formulas = formulas;
	}
	
	public void addFormula(Formula formula) {
		formulas.add(formula);
	}

	/* (non-Javadoc)
	 * @see kiwi.service.reasoning.ast.Formula#getFormulaType()
	 */
	@Override
	public FormulaType getFormulaType() {
		return FormulaType.CONJUNCTION;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("(");
		
		Iterator<Formula> it = formulas.iterator();
		while (it.hasNext()) {
			buf.append(it.next().toString());
			if (it.hasNext())
				buf.append(" , ");
		}
		
		buf.append(')');
		
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see kiwi.service.reasoning.ast.Formula#getVariables()
	 */
	@Override
	public Set<Variable> getVariables() {
		Set<Variable> variables = new HashSet<Variable>();
		
		for (Formula formula : formulas)
			variables.addAll(formula.getVariables());
		
		return variables;
	}	
	
	/**
	 * @see kiwi.service.reasoning.ast.Formula#isGround()
	 */
	@Override
	public boolean isGround() {
		for (Formula formula : formulas)
			if (!formula.isGround())
				return false;
		
		return true;
	}	
}
