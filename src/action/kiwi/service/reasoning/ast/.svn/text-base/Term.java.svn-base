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

import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiUriResource;

/**
 * @author Jakub Kotowski
 *
 */
public abstract class Term {
	/** The types of terms that are implemented.
	 * Currently the possible values are:
	 * <ul>
	 * 		<li><b>VARIABLE</b> - represents a variable.</li>
	 * 		<li><b>CONSTRUCTION_VARIABLE</b> - represents a variable with a construction clause (a uri).</li>
	 * 		<li><b>URI</b> - represents a URI.</li>
	 * 		<li><b>LITERAL</b> - represents a literal.</li>
	 * 		<li><b>TYPED_LITERAL</b> - represents a literal with a type.</li>
	 * 		<li><b>BNODE</b> - represents an RDF b-node.</li>
	 * </ul>
	 */
	public enum TermType { VARIABLE, CONSTRUCTION_VARIABLE, URI, LITERAL, TYPED_LITERAL, BNODE};
	
	/** Returns true if this instance is of type {@link Variable} or {@link ConstructionVariable}.
	 */
	public boolean isVariable() { return TermType.VARIABLE.equals(getTermType()) || isConstructionVariable(); }
	/** Returns true if this instance is of type {@link ConstructionVariable} (which is a subclass of {@link Variable}).
	 */
	public boolean isConstructionVariable() { return TermType.CONSTRUCTION_VARIABLE.equals(getTermType()); }
	/** Returns true if this instance is of type {@link Uri}.
	 */
	public boolean isUri() { return TermType.URI.equals(getTermType()); }
	/** Returns true if this instance is of type {@link Literal} or {@link TypedLiteral}.
	 */
	public boolean isLiteral() { return TermType.LITERAL.equals(getTermType()) || isTypedLiteral(); }
	/** Returns true if this instance is of type {@link Literal} (which is a subclass of {@link TypedLiteral})
	 */
	public boolean isTypedLiteral() { return TermType.TYPED_LITERAL.equals(getTermType()); }
	/** Returns true if this instance is of type {@link BNode}.
	 */
	public boolean isBNode() { return TermType.BNODE.equals(getTermType()); }
	
	public boolean isValidRDFSubject() { return !isLiteral(); }
	public boolean isValidRDFProperty() { return !isLiteral(); }
	public boolean isValidRDFObject() { return true; }
	
	/** Returns the type of term of this instance.
	 * 
	 * @see TermType
	 */
	public abstract TermType getTermType();
	
	//TODO: typed literals
	public boolean matches(KiWiNode node) {
		switch (getTermType()) {
		case CONSTRUCTION_VARIABLE:
			throw new IllegalArgumentException("Matching against terms of type "+getTermType()+" is not implemented.");
		case VARIABLE:
			return true;
		case URI:
			if (node.isUriResource()) {
				String termUri = ((Uri)this).getUri();
				String nodeUri = ((KiWiUriResource)node).getUri();
				return termUri.equals(nodeUri);
			} else
				return false;
		case TYPED_LITERAL:
		case LITERAL:
			if (node.isLiteral()) {
				KiWiLiteral kiwiLit = (KiWiLiteral) node;
				Literal lit = (Literal) this;
				return kiwiLit.getContent().equals(lit.getLiteral());
			} else
				return false;
			default:
				return false;
		}
	}		
}
