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
package kiwi.widgets.explanation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kiwi.api.explanation.ExplanationService;
import kiwi.model.kbase.KiWiTriple;
import kiwi.service.reasoning.reasonmaintenance.Justification;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/** Explanation web service provides access to justification data maintained by reason maintenance.
 * 
 * It uses the explanation service to get "explanation subtrees" for triples and returns them as JSON.
 * 
 * @author Jakub Kotowski
 *
 */
@Name("kiwi.widgets.explanationWidget")
@Scope(ScopeType.STATELESS)
@Path("/widgets/explanation")
public class ExplanationWidgetWebService {
	@Logger private Log log;
	@In("explanationService") ExplanationService explanationService;
	@In EntityManager entityManager;

	/**
	 * Return a JSON object representing a graph of nodes which explain the triple with the 
	 * id passed as argument. The web service is called using 
	 * /KiWi/seam/resource/services/widgets/explanation/explain?id=<KIWI_TRIPLE_ID>
	 * 
	 * @param id KiWiTriple id of the triple to explain
	 * @return
	 */
	@GET
	@Path("/explain")
	@Produces("application/json")
	@Wrapped(element="nodes")
	public List<NodeJSON> getSubtree(@QueryParam("id") Long id, @QueryParam("rootid") String rootid) {
		log.info("getNodes(#0)", id);
		
		List<Justification> justifications = explanationService.explainTriple(id);
		
		log.info("Explanation returned #0", justifications);
		
		return toNodes(justifications, rootid);
	}
	
	private String describeTriple(Long tripleId) {
		KiWiTriple triple = entityManager.find(KiWiTriple.class, tripleId);
		return explanationService.describeTriple(triple, false);
	}

	private String describeTriple(KiWiTriple triple) {
		return explanationService.describeTriple(triple, false);
	}
	
	private List<NodeJSON> toNodes(List<Justification> justifications, String rootid) {
		List<NodeJSON> nodes = new ArrayList<NodeJSON>(); //TODO size				
		
		for (Justification j : justifications) {
			NodeJSON node;
			List<String> neighbours;
			List<PairJSON> data;
			
			//first add all nodes to avoid the JIT adjacency bug
			/*for (String id : getAllNodeIds(j)) {
				node = new NodeJSON(id, id, new ArrayList<String>());
				nodes.add(node);
			}*/
			
			// NODE FOR THE JUSTIFIED TRIPLE
			neighbours = new ArrayList<String>(1);
			neighbours.add(rootid+j.toString());
			data = new ArrayList<PairJSON>(2); data.add(new PairJSON("kiwiid", j.getFactId().toString())); data.add(new PairJSON("type", "triple"));
			data.add(new PairJSON("kiwiidj", j.getNeoId().toString())); //id of the justif this node belongs to
			data.add(new PairJSON("id",rootid));//connect the subtree we are constructing to the right node (the node for which this tree is loaded) 
			data.add(new PairJSON("tooltip",describeTriple(j.getFact())));
			node = new NodeJSON(toJITid(rootid), toJITid(j.getFactId()), neighbours, data);
			nodes.add(node); 
			//log.info("Adding node #0", node.toString());
			
			neighbours = getNeighbours(j, rootid);

			// NODE FOR THE JUSTIFICATION
			data = new ArrayList<PairJSON>(2); data.add(new PairJSON("id", rootid + j.toString())); data.add(new PairJSON("type", "justification"));
			data.add(new PairJSON("kiwiid", j.getNeoId().toString())); 
			data.add(new PairJSON("tooltip","the node above is derived using the rule and triples below"));
			node = new NodeJSON(rootid + j.toString(),"support", neighbours, data); 
			nodes.add(node);
			//log.info("Adding node #0", node.toString());
			
			// NODES FOR THE IN TRIPLES
			for (Long id : j.getInTripleIds()) { 
				data = new ArrayList<PairJSON>(2); data.add(new PairJSON("kiwiid", id.toString())); data.add(new PairJSON("type", "triple"));
				data.add(new PairJSON("kiwiidj", j.getNeoId().toString())); //id of the justif this node belongs to
				data.add(new PairJSON("id",rootid+j.toString()+id.toString()));//create a unique id for each justification so that nodes are never shared --> we're creating a tree, never a more general graph
				data.add(new PairJSON("leaf","true")); data.add(new PairJSON("tooltip",describeTriple(id)));
				node = new NodeJSON(toJITid(rootid+j.toString()+id.toString()), toJITid(id), new ArrayList<String>(), data);
				nodes.add(node); 
				//log.info("Adding node #0", node.toString());
			}

			data = new ArrayList<PairJSON>(2); data.add(new PairJSON("kiwiid", j.getRuleId())); data.add(new PairJSON("type", "rule"));
			data.add(new PairJSON("id", rootid+j.toString()+j.getRuleId()));//we don't wan't rules have only one node in the graph
			data.add(new PairJSON("tooltip","Rule "+j.getRuleId()));
			node = new NodeJSON(toJITid(rootid+j.toString()+j.getRuleId()), j.getRuleId(), new ArrayList<String>(), data);
			nodes.add(node);
			//log.info("Adding node #0", node.toString());
		}
		
		return nodes;
	}
	
	private List<String> getNeighbours(Justification j, String rootid) {
		List<String> ns = new ArrayList<String>();
		
		//ns.add(toJITid(j.getFactId()));
		
		for (Long id : j.getInTripleIds()) 
			ns.add(toJITid(rootid+j.toString()+id));
		
		ns.add(toJITid(rootid+j.toString()+j.getRuleId()));
		
		return ns;
	}
	
	private List<String> getAllNodeIds(Justification j) {
		List<String> ns = new ArrayList<String>();
		
		ns.add(toJITid(j.getFactId()));
		
		for (Long id : j.getInTripleIds()) 
			ns.add(toJITid(id));
		
		ns.add(toJITid(j.getRuleId()));
		
		ns.add(j.toString());
		return ns;
	}
	
	/**
	 * JIT requires ids to be strings.
	 * @param id
	 * @return
	 */
	private String toJITid(Long id) {
		return "id"+id;
	}
	
	/**
	 * JIT requires ids to be strings.
	 * @param id
	 * @return
	 */
	private String toJITid(String id) {
		return "id"+id;
	}
}









