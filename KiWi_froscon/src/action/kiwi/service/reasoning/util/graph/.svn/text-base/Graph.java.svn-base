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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiTriple;

/** Graph represents a simple RDF graph - allows to navigate a set of triples like a connected graph.
 * 
 * @author Jakub Kotowski
 */
public class Graph {
	private Map<KiWiNode, Node> valueNodeMap = new HashMap<KiWiNode, Node>();
	private Map<KiWiTriple, Edge> valueEdgeMap = new HashMap<KiWiTriple, Edge>();
	private Set<Node> nodes = new HashSet<Node>();
	private Set<Edge> edges = new HashSet<Edge>();
	
	public Graph(Set<KiWiTriple> triples) {
		for (KiWiTriple triple : triples) 
			createEdge(triple);
	}
	
	/** Creates a node for the given value.
	 * 
	 * Does nothing if a node for the value already existed.
	 * @return Node for the value.
	 */
	public Node createNode(KiWiNode value) {
		if (!valueNodeMap.containsKey(value)) {
			Node node = new Node(value);
			valueNodeMap.put(value, node);
			nodes.add(node);
		}		
		
		return valueNodeMap.get(value);
	}
	
	/** Creates an edge for the given triple.
	 * 
	 * Creates nodes for the triple's subject and object if they didn't exist yet.
	 * Does nothing if an edge for the value already existed.
	 * 
	 * @return Edge for the value.
	 */
	public Edge createEdge(KiWiTriple triple) {
		if (!valueEdgeMap.containsKey(triple)) {
			Node subject = createNode(triple.getSubject());
			Node object = createNode(triple.getObject());

			Edge edge = new Edge(triple);
			edge.setBeginning(subject);
			edge.setEnd(object);
			edges.add(edge);
			valueEdgeMap.put(triple, edge);
			
			subject.addOutgoingEdge(edge);
			object.addIngoingEdge(edge);
		}

		return valueEdgeMap.get(triple);
	}
	
	/** Returns a node for the given value or null if such a node does not exist.
	 */
	public Node getNode(KiWiNode value) {
		return valueNodeMap.get(value);
	}
	
	/** Returns an edge for the given value or null if such an edge does not exist.
	 */
	public Edge getEdge(KiWiTriple value) {
		return valueEdgeMap.get(value);
	}
	
	public String toString() {
		return "Graph: "+nodes.size()+" nodes, "+edges.size()+" edges.";
	}
}


