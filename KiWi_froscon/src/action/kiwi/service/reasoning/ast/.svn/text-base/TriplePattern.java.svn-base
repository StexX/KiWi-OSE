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

import java.util.HashSet;
import java.util.Set;

import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;

/**
 * @author Jakub Kotowski
 *
 */
public class TriplePattern extends Atom {
	/** Represents names of positions of a triple pattern.
	 * Possible values are:
	 * <ul>
	 * 		<li>SUBJECT</li>
	 * 		<li>PROPERTY</li>
	 * 		<li>OBJECT</li>
	 * </ul>
	 */
	public enum TriplePatternPosition { SUBJECT, PROPERTY, OBJECT }
	
	Term subject;
	Term property;
	Term object;
	KiWiResource kiwiSubject;
	KiWiUriResource kiwiProperty;
	KiWiNode kiwiObject;
	
	public TriplePattern() {
	}
	
	public TriplePattern(Term subject, Term property, Term object) {
		this.subject = subject;
		this.property = property;
		this.object = object;
	}
		
	public Term getSubject() {
		return subject;
	}

	public void setSubject(Term subject) {
		this.subject = subject;
	}

	public Term getProperty() {
		return property;
	}

	public void setProperty(Term property) {
		this.property = property;
	}

	public Term getObject() {
		return object;
	}

	public void setObject(Term object) {
		this.object = object;
	}

	public Set<Variable> getVariables() {
		Set<Variable> variables = new HashSet<Variable>();
		
		if (subject.isVariable())
			variables.add((Variable)subject);
		
		if (property.isVariable())
			variables.add((Variable)property);
		
		if (object.isVariable())
			variables.add((Variable)object);
		
		return variables;
	}
	
	public KiWiResource getKiwiSubject() {
		return kiwiSubject;
	}

	public void setKiwiSubject(KiWiResource kiwiSubject) {
		this.kiwiSubject = kiwiSubject;
	}

	public KiWiUriResource getKiwiProperty() {
		return kiwiProperty;
	}

	public void setKiwiProperty(KiWiUriResource kiwiProperty) {
		this.kiwiProperty = kiwiProperty;
	}

	public KiWiNode getKiwiObject() {
		return kiwiObject;
	}

	public void setKiwiObject(KiWiNode kiwiObject) {
		this.kiwiObject = kiwiObject;
	}

	/* (non-Javadoc)
	 * @see kiwi.service.reasoning.ast.Atom#getAtomType()
	 */
	@Override
	public AtomType getAtomType() {
		return AtomType.TRIPLE_PATTERN;
	}
	
	public String toString() {
		return "("+subject.toString()+" "+property.toString()+" "+object.toString()+")";
	}

	/* (non-Javadoc)
	 * @see kiwi.service.reasoning.ast.Formula#isGround()
	 */
	@Override
	public boolean isGround() {
		return !subject.isVariable() && !property.isVariable() && !object.isVariable();
	}
	
	public boolean matches(KiWiTriple triple) {
		return getSubject().matches(triple.getSubject()) &&
		getProperty().matches(triple.getProperty()) &&
		getObject().matches(triple.getObject());		
	}	
}
