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
package kiwi.service.reasoning.util.graph;

import java.util.HashSet;
import java.util.Set;

import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiUriResource;

public class Node {
	private KiWiNode value;
	private Set<Edge> ingoing = new HashSet<Edge>();
	private Set<Edge> outgoing = new HashSet<Edge>();
	
	public Node(KiWiNode value) {
		this.value = value;
	}
	
	public KiWiNode getValue() {
		return value;
	}
	public void setValue(KiWiNode value) {
		this.value = value;
	}
	public Set<Edge> getIngoing() {
		return ingoing;
	}
	
	private Set<Edge> getEdgesOfType(Set<Edge> edges, KiWiUriResource type) {
		Set<Edge> result = new HashSet<Edge>();
		
		for (Edge edge : edges) 
			if (edge.getType().equals(type))
				result.add(edge);
		
		return result;
	}
	
	public Set<Edge> getIngoing(KiWiUriResource type) {
		return getEdgesOfType(ingoing, type);
	}
	
	public Set<Edge> getOutgoing(KiWiUriResource type) {
		return getEdgesOfType(outgoing, type);
	}
	
	public void setIngoing(Set<Edge> ingoing) {
		this.ingoing = ingoing;
	}
	public Set<Edge> getOutgoing() {
		return outgoing;
	}
	
	
	
	public void setOutgoing(Set<Edge> outgoing) {
		this.outgoing = outgoing;
	}
	
	public void addIngoingEdge(Edge edge) {
		ingoing.add(edge);
	}
	
	public void addOutgoingEdge(Edge edge) {
		outgoing.add(edge);
	}
}
