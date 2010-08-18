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

import kiwi.service.reasoning.ast.Atom;
import kiwi.service.reasoning.ast.TriplePattern;

/**
 * Holds triples of the form {atomNumber, triplePatternPosition, atomType}.
 * 
 * For example: {1, SUBJECT, TRIPLE_PATTERN}
 * 
 * @author Jakub Kotowski
 *
 */
public class VariablePosition {
	private int atomNumber;
	private Atom.AtomType atomType;
	private TriplePattern.TriplePatternPosition triplePatternPosition;
	
	/**
	 * 
	 * @param atomNumber The number of the atom from the beginning of the rule. Begins with 1.
	 * @param triplePatternPosition
	 */
	public VariablePosition(int atomNumber, TriplePattern.TriplePatternPosition triplePatternPosition) {
		if (atomNumber < 1)
			throw new IllegalArgumentException("Atom number must be greater than or equal to 1.");
		
		this.atomNumber = atomNumber;
		this.triplePatternPosition = triplePatternPosition;
		this.atomType = Atom.AtomType.TRIPLE_PATTERN;
	}
	public int getAtomNumber() {
		return atomNumber;
	}
	public void setAtomNumber(int atomNumber) {
		this.atomNumber = atomNumber;
	}
	public Atom.AtomType getAtomType() {
		return atomType;
	}
	public void setAtomType(Atom.AtomType atomType) {
		this.atomType = atomType;
	}
	
	/** Returns the position in a triple pattern at which the variable occurred.
	 * @see TriplePattern.TriplePatternPosition
	 */
	public TriplePattern.TriplePatternPosition getTriplePatternPosition() {
		return triplePatternPosition;
	}
	public void setTriplePatternPosition(
			TriplePattern.TriplePatternPosition triplePatternPosition) {
		this.triplePatternPosition = triplePatternPosition;
	}
	
	public String toString() {
		return "{"+atomNumber+","+triplePatternPosition+","+atomType+"}";
	}
}
