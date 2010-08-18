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
 * 
 * 
 */
package kiwi.service.reasoning;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiTriple;

public class RDFList {
	//private Log log = Logging.getLog(this.getClass());
	private KiWiNode first = null;
	private RDFList rest = null;
	private RDFList previous = null;
	private KiWiTriple firstTriple;
	private KiWiTriple restTriple;
	
	public KiWiNode getFirst() {
		return first;
	}

	public void setFirst(KiWiNode first) {
		this.first = first;
	}

	public RDFList getRest() {
		return rest;
	}

	public void setRest(RDFList rest) {
		this.rest = rest;
	}

	public KiWiTriple getFirstTriple() {
		return firstTriple;
	}

	/** Sets the (?, rdf:first, ?) triple of this List.
	 * 
	 * Verifies the type of the triple and throws a ReasoningException if the property is not rdf:first.
	 * Also sets the "first node", equivalent to calling setFirst(firstTriple.getObject()).
	 */
	public void setFirstTriple(KiWiTriple firstTriple) {
		if (!ReasoningConstants.RDF_FIRST.equals(firstTriple.getProperty().getUri()))
			throw new ReasoningException("Wrong triple used to build an RDF collection. Expected property rdf:first, the triple was: "+firstTriple);
		
		this.firstTriple = firstTriple;
		setFirst(firstTriple.getObject());
	}

	public KiWiTriple getRestTriple() {
		return restTriple;
	}

	/** Sets the (?, rdf:rest, ?) triple of this List.
	 * 
	 * Verifies the type of the triple and throws a ReasoningException if the property is not rdf:rest.
	 */
	public void setRestTriple(KiWiTriple restTriple) {
		if (!ReasoningConstants.RDF_REST.equals(restTriple.getProperty().getUri()))
			throw new ReasoningException("Wrong triple used to build an RDF collection. Expected property rdf:rest, the triple was: "+restTriple);
		
		this.restTriple = restTriple;
	}
	
	public Set<KiWiTriple> getAllRestTriples() {
		Set<KiWiTriple> rests = new HashSet<KiWiTriple>();
		
		rests.add(restTriple);
		if (rest != null)
			rests.addAll(rest.getAllRestTriples());
		
		return rests;
	}
	
	public Set<KiWiTriple> getAllFirstTriples() {
		Set<KiWiTriple> firsts = new HashSet<KiWiTriple>();
		
		firsts.add(firstTriple);
		if (rest != null)
			firsts.addAll(rest.getAllFirstTriples());
		
		return firsts;
	}
	
	public Set<KiWiNode> getAllFirstNodes() {
		Set<KiWiNode> firsts = new HashSet<KiWiNode>();
		
		firsts.add(first);
		if (rest != null)
			firsts.addAll(rest.getAllFirstNodes());
		
		return firsts;
	}
	
	public boolean contains(KiWiTriple triple) {
		if (triple.equals(firstTriple))
			return true;
		
		if (rest != null)
			return rest.contains(triple);
		else
			return false;
	}
	
	public boolean contains(KiWiNode node) {
		if (node.equals(first))
			return true;
		
		if (rest != null)
			return rest.contains(node);
		else
			return false;
	}
	
	public RDFList getPrevious() {
		return previous;
	}

	public void setPrevious(RDFList previous) {
		this.previous = previous;
	}
	
	public RDFList getBeginning() {
		if (previous == null)
			return this;
		
		return previous.getBeginning();
	}

	public String toString() {
		String firstName;
		
		try {
			firstName = first.getShortName();
		} catch (URISyntaxException e) {
			firstName = first.toString();
		}
		
		String restName;
		if (rest != null)
			restName = rest.toString();
		else
			restName = "nil";
		
		return firstName+":"+restName;
	}
}




