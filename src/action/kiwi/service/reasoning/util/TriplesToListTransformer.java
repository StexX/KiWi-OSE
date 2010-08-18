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
package kiwi.service.reasoning.util;

import java.util.HashSet;
import java.util.Set;

import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.service.reasoning.RDFList;
import kiwi.service.reasoning.ReasoningConstants;
import kiwi.service.reasoning.ReasoningException;
import kiwi.service.reasoning.util.graph.Edge;
import kiwi.service.reasoning.util.graph.Graph;
import kiwi.service.reasoning.util.graph.Node;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

public class TriplesToListTransformer {
	private Log log = Logging.getLog(this.getClass());
	TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
	KiWiUriResource nil = tripleStore.createUriResource(ReasoningConstants.RDF_NIL);
	KiWiUriResource first = tripleStore.createUriResource(ReasoningConstants.RDF_FIRST);
	KiWiUriResource rest = tripleStore.createUriResource(ReasoningConstants.RDF_REST);
	Graph graph;
	
	public TriplesToListTransformer(Set<KiWiTriple> triples) {
		graph = new Graph(triples);
		log.debug("Created new graph: #0", graph);
	}
	
	public Set<RDFList> getLists() {
		Set<RDFList> lists = new HashSet<RDFList>();

		Node nilNode = graph.getNode(nil);
		
		if (nilNode == null)
			return lists;
		
		for (Edge edge : nilNode.getIngoing(rest)) {
			Node node = getFirstNodeInRestChain(edge);
			if (node == null) {
				log.debug("Some problem while walking #0 back to the beginning of the collection. Skipping.", edge.getValue());
				continue;
			}
			RDFList list = getList(node);
			lists.add(list);
		}
		
		return lists;
	}
	
	private Node getFirstNodeInRestChain(Edge edge) {
		Set<Node> visitedNodes = new HashSet<Node>(); //to detect cycles
		
		if (!rest.equals(edge.getType()))
			throw new ReasoningException("Wrong edge type: "+edge.getType());
		
		Node beginning = edge.getBeginning();
		visitedNodes.add(beginning);
		
		Set<Edge> edges = beginning.getIngoing(rest);
		while (!edges.isEmpty()) {
			if (edges.size() > 1)
				log.error("Error in data: a branching RDF list detected #0 rdf:rest triples point to #1.", edges.size(), edge.getValue());
			
			beginning = edges.iterator().next().getBeginning();
			if (visitedNodes.contains(beginning)) {
				log.error("Error in data: a cycle in a collection detected. #0 was visited twice.", beginning.getValue());
				return null;
			}
			visitedNodes.add(beginning);
			edges = beginning.getIngoing(rest);
		}
		
		return beginning;
	}
	
	private RDFList getList(Node node) {
		RDFList list = new RDFList();
		
		Set<Edge> restEdges = node.getOutgoing(rest);
		if (restEdges.size() > 1)
			log.error("Error in data: a branching RDF list detected #0 rdf:rest triples point from #1.", restEdges.size(), node.getValue());
		
		Set<Edge> firstEdges = node.getOutgoing(first);
		if (firstEdges.size() != 1 && !node.getValue().equals(nil))
			log.error("Error in data: #0 rdf:first triples point from #1.", firstEdges.size(), node.getValue());
		
		if (! firstEdges.isEmpty()) {
			Edge firstEdge = firstEdges.iterator().next();
			list.setFirst( firstEdge.getEnd().getValue() );
			list.setFirstTriple( firstEdge.getValue() );
		}
		
		if (!restEdges.isEmpty()) {
			Edge restEdge = restEdges.iterator().next();
		
			RDFList restList = getList(restEdge.getEnd());
			list.setRest(restList);
			list.setRestTriple(restEdge.getValue());
			if (restList != null)
				restList.setPrevious(list);
		} else {
			if (!node.getValue().equals(nil))
				log.error("Error in data: #0 is not nil and there's no outgoing rdf:rest triple.", node.getValue());
			return null;
		}
		
		return list;
	}

}









