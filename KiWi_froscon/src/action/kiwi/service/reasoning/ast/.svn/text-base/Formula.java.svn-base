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

import java.util.Set;

/**
 * @author Jakub Kotowski
 *
 */
public abstract class Formula {
	/** All the different types of formulas that are implemented. 
	 * The FormulaType is one of:
	 * <ul>
	 * 		<li><b>ATOM</b> Atomic formula. Now it has only one implementer - the {@link TriplePattern}.</li>
	 * 		<li><b>NEGATION</b> Negation of a formula. </li>
	 * 		<li><b>CONJUNCTION</b> Conjunction of formulas. </li>
	 * 		<li><b>DISJUNCTION</b> Disjunction of formulas. </li>
	 * </ul> 
	 */
	public enum FormulaType {ATOM, NEGATION, CONJUNCTION, DISJUNCTION};
	
	/** Returns true if this formula is a conjunction of formulas.
	 * 
	 * @return
	 */
	public boolean isConjunction() {
		return FormulaType.CONJUNCTION.equals(getFormulaType());
	}

	/** Returns true if this formula is a negation of a formula.
	 * 
	 * @return
	 */
	public boolean isNegation() {
		return FormulaType.NEGATION.equals(getFormulaType());
	}

	/** Returns true if this formula is a disjunction of formulas.
	 * 
	 * @return
	 */
	public boolean isDisjunction() {
		return FormulaType.DISJUNCTION.equals(getFormulaType());
	}
	
	/** True if this instance is of type Atom which now can only be a TriplePattern.
	 * 
	 * @return
	 */
	public boolean isAtomic() {
		return FormulaType.ATOM.equals(getFormulaType());
	}
	
	/** Returns the set of all variables contained anywhere in the formula.
	 * 
	 * @return
	 */
	public abstract Set<Variable> getVariables();
	
	/** Returns true if there are no variables in this formula.
	 * 
	 * @return
	 */
	public abstract boolean isGround();
	
	/** Returns the FormulaType of this formula.
	 * 
	 * The FormulaType is one of:
	 * <ul>
	 * 		<li>ATOM</li>
	 * 		<li>NEGATION</li>
	 * 		<li>CONJUNCTION</li>
	 * 		<li>DISJUNCTION</li>
	 * </ul>
	 * 
	 * @see FormulaType
	 * @return
	 */
	public abstract FormulaType getFormulaType();
}
