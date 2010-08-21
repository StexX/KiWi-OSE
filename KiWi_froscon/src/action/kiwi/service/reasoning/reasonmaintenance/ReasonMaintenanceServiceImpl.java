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
package kiwi.service.reasoning.reasonmaintenance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import kiwi.api.reasoning.reasonmaintenance.ReasonMaintenanceServiceLocal;
import kiwi.api.reasoning.reasonmaintenance.ReasonMaintenanceServiceRemote;
import kiwi.api.transaction.TransactionService;
import kiwi.model.kbase.KiWiTriple;
import kiwi.service.reasoning.ReasoningTaskStatistics;
import kiwi.service.transaction.KiWiSynchronizationImpl;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.index.IndexService;

/** Reason maintenance service processes updates made to the triple store.
 * 
 * It maintains a justification dependency graph and uses it to remove facts (triples) that are no longer justified.
 * 
 * 
 * @author Jakub Kotowski
 *
 */
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("reasonMaintenanceService")
@TransactionManagement(TransactionManagementType.BEAN)
public class ReasonMaintenanceServiceImpl implements ReasonMaintenanceServiceLocal,ReasonMaintenanceServiceRemote {	
	public enum EdgeTypes implements RelationshipType {
		IN, OUT, RULE, JUSTIFICATION, REF
	}
	
	private static final String FACT_NODE_KEY = "fact";
	private static final String FACT_NODE_KIWI_ID = "fact_kiwi_id";
	private static final String RULE_NODE_KEY = "rule";
	private static final String RULE_NODE_KIWI_ID = "rule_kiwi_id";
	private static final String JUSTIFICATION_NODE_KEY = "justification";
	private static final String CURRENT_SUPPORT = "current_support";
	private static final String MARK = "mark";
	private static final String BASE = "base";
		
	private Set<Node> markedNodes = new HashSet<Node>();
	private Set<Node> unmarkedNodes = new HashSet<Node>();
	
	@In("RMS.neo") private GraphDatabaseService neo;
	@In("RMS.neoIndexService") private IndexService indexService;
	private @Logger Log log;
	private @In EntityManager entityManager;

	@In
	private TransactionService transactionService;
	
	@Out(value="reasonMaintenanceRunning", scope = ScopeType.APPLICATION, required = false)
	private Boolean running = false;
	
	public void addJustifications(Set<Justification> justifs) {
		setRunning(true);
		Transaction tx = neo.beginTx();
		try {
		
			for (Justification justif : justifs) 
				addJustification(justif);
			
			tx.success();
		
		} finally {
			tx.finish();
			setRunning(false);
		}
	}
	
	/** Determines whether a fact has a justification.
	 * 
	 * @param tripleId Id of the fact of interest.
	 * @return true if the fact with id tripleId has a justification.
	 * @return false otherwise.
	 */
	private boolean hasJustification(Long tripleId) {
		String factNodeId = kiwiTripleIdToNeoId(tripleId);
		Node factNode = getFactNode(factNodeId);
		
		if (factNode == null)
			return false;
		
		Iterable<Relationship> justifs = factNode.getRelationships(EdgeTypes.JUSTIFICATION);
		
		return justifs.iterator().hasNext();
	}
		
	private void addJustification(Justification justif) {	
		//log.debug("Adding justification #0 "+Thread.currentThread(), justif.toString());
		
			Node justifNode = indexService.getSingleNode(JUSTIFICATION_NODE_KEY, justif.toString());
			
			if (justifNode != null) {
				//log.debug("Justification \"#0\" was already stored. Skipping.", justif.toString());
				return;
			}
			if(justif.getFactId() == null) {
				log.warn("While storing justifications: fact ID was null. Fact was #0. Did the previous transaction commit successfully?",justif.getFact());
				return;
			}			
			// retrieve or create all necessary nodes

			justifNode = createAndIndexJustification(justif.toString());
			
			//mark the justif as CURRENT_SUPPORT if it is the first justification of the supported fact
			//base triples have a thought virtual justification that is their current support
			if (! hasJustification(justif.getFactId()) && ! justif.getFact().isBase()) {
				justifNode.setProperty(CURRENT_SUPPORT, true);
			} else 
				if (! justifNode.hasProperty(CURRENT_SUPPORT))
					justifNode.setProperty(CURRENT_SUPPORT, false);				
			
			
			String factNodeId = kiwiTripleIdToNeoId(justif.getFactId());
			String ruleNodeId = kiwiRuleIdToNeoId(justif.getRuleId());
			
			Node factNode = getFactNode(factNodeId);
			Node ruleNode = getRuleNode(ruleNodeId);
			
			if (factNode == null) {
				factNode = createAndIndexFact(factNodeId, justif.getSupportedFact());
			}
			
			// if the fact node was created as an IN node of a justification then it doesn't have the base property set
			if (! factNode.hasProperty(BASE)) 
				factNode.setProperty(BASE, justif.getFact().isBase());
			
			
			if (ruleNode == null)
				ruleNode = createAndIndexRule(ruleNodeId, justif.getRuleId());
			
			Node[] inNodes = getOrCreateFactsForTriples( justif.getInTripleIds() );
			
			Node referenceNode = neo.getReferenceNode();
			
			// create all edges to represent the justification
			
			justifNode.createRelationshipTo(factNode, EdgeTypes.JUSTIFICATION);
			justifNode.createRelationshipTo(ruleNode, EdgeTypes.RULE);
			
			for (Node node : inNodes) 
				justifNode.createRelationshipTo(node, EdgeTypes.IN);
			
			//add a relationship to the reference node for easier browsing using the NeoShell
			referenceNode.createRelationshipTo(justifNode, EdgeTypes.REF);
						
			//log.debug("Successfuly added new justification: #0, node id: #1", justif.toString(),justifNode.getId());		
	}
	
	public List<Justification> getJustificationFor(Long tripleId) {
		setRunning(true);
		//log.debug("getJustificationFor(#0)", tripleId);
		ArrayList<Justification> js = new ArrayList<Justification>();

		Transaction tx = neo.beginTx();
		try {
			
			Node node = getFactNode(tripleId);
			if (node == null) {
				log.error("There's a bug somewhere. Node #0 is not in the neo4j database.", tripleId);
				return js;
			}
			//log.debug("Neo returned #0, id #1", node, node.getId());
			Iterable<Relationship> justifRels = node.getRelationships(EdgeTypes.JUSTIFICATION);
			
			for (Relationship rel : justifRels) {
				Node jusNode = rel.getStartNode();
				Justification justif = toJustification(jusNode);
				js.add(justif);
				//log.debug("It has justification: #0",justif.toString());
			}
			
			tx.success();
		} finally {
			tx.finish();
			setRunning(false);
		}
		
		//log.debug("Returning #0 justifications.", js.size());
		return js;
	}
	
	private Justification toJustification(Node n) {
		Justification jus;
		
		if (n == null)
			throw new IllegalArgumentException("toJustification() can't take null as input.");
		
		if (!n.hasProperty(JUSTIFICATION_NODE_KEY))
			throw new IllegalArgumentException("toJustification() takes only justification nodes as input! The input node was: "+ n+", id: "+n.getId());

		Relationship r = n.getSingleRelationship(EdgeTypes.JUSTIFICATION, Direction.BOTH);
		Node factNode = r.getEndNode();
		
		Iterable<Relationship> rels = n.getRelationships(EdgeTypes.IN);
		ArrayList<Long> inTriples = new ArrayList<Long>();
		for (Relationship rel : rels) {
			Node node = rel.getEndNode();
			inTriples.add((Long)node.getProperty(FACT_NODE_KIWI_ID));
		}
		
		r = n.getSingleRelationship(EdgeTypes.RULE, Direction.BOTH);
		Node ruleNode = r.getEndNode();
		
		KiWiTriple triple = entityManager.find(KiWiTriple.class, factNode.getProperty(FACT_NODE_KIWI_ID));
		
		jus = new Justification(triple,inTriples.toArray(new Long[inTriples.size()]), (String)ruleNode.getProperty(RULE_NODE_KIWI_ID));
		
		jus.setNeoId(n.getId());
		
		return jus;
	}
	
	private void removeJustification(Node justif) {
		Iterable<Relationship> rels = justif.getRelationships();
		
		for (Relationship rel : rels)
			rel.delete();
		
		log.debug("Deleting justification #0, node id #1", justif.getProperty(JUSTIFICATION_NODE_KEY), justif.getId());
		
		String id = (String) justif.getProperty(JUSTIFICATION_NODE_KEY);
		indexService.removeIndex(justif, JUSTIFICATION_NODE_KEY, id);
		
		justif.delete();
	}
	
	private boolean isCurrentSupport(Node justif) {
		Boolean isCurrentSupport = (Boolean) justif.getProperty(CURRENT_SUPPORT);
		
		return isCurrentSupport;
	}
	
	/** The marking procedure of the current support strategy algorithm.
	 * 
	 * @param node Starting node.
	 * @param markedNodes The set of so far marked nodes.
	 */
	private void mark(Node fact) {
		setMark(fact);
		
		//get all justifications in which the fact participates
		Iterable<Relationship> inRels = fact.getRelationships(EdgeTypes.IN);
		for (Relationship inRel : inRels) {
			Node justif = inRel.getStartNode();
			
			if (isMarked(justif))
				continue; //there's a cycle and this justification has already been processed
			
			setMark(justif);
			
			if (! isCurrentSupport(justif))
				continue; // continue marking only if this justification is current support

			//get all facts that the justification supports and mark them
			Node supportedNode = getSupportedNode(justif);
			
			//doesn't really have to be here as all the nodes will be marked by the first call of this function
			//for (Node node : supportedNodes)
			//	setMark(node);
		
			//remove the justification because it is no longer valid (one of its IN nodes was removed)
			//it is necessary to first remove the justification and only then to recurse to avoid endless loops which
			//may occur in case that a justification is added for a base fact (the triple is both base and derived)
			//removeJustification(justif);
			
			//recursively mark the rest of the tree
			mark(supportedNode);
		}		
	}
	
	private boolean hasSupport(Node node) {
		return node.hasRelationship(EdgeTypes.JUSTIFICATION);
	}
	
	/** For a node, returns a justification that is not marked if it exists. 
	 * 
	 * @param node The fact node the justification of which should be returned.
	 * @return The unmarked justification if it exists. Null otherwise.
	 */
	private Node getUnmarkedJustification(Node node) {
		if (! node.hasProperty(FACT_NODE_KEY))
			throw new IllegalArgumentException("Can't get a justification of a node that is not a fact node: "+node+" id("+node.getId()+").");
		
		Iterable<Relationship> justifRels = node.getRelationships(EdgeTypes.JUSTIFICATION);
		
		for (Relationship rel : justifRels) {
			Node justif = rel.getStartNode();
			if (! isMarked(justif))
				return justif;
		}
		
		return null;
	}
	
	
	//TODO rule nodes without relationships
	private void removeNodes(Set<Node> nodes) {
		Set<Long> toDelete = new HashSet<Long>();
		
		for (Node n : nodes) {
			if (n.hasRelationship()) {
				Iterable<Relationship> rels = n.getRelationships();
				
				for (Relationship rel : rels)
					rel.delete();			
			}
			
			if (n.hasProperty(FACT_NODE_KEY)) {
				String id = (String) n.getProperty(FACT_NODE_KEY);
				indexService.removeIndex(n, FACT_NODE_KEY, id);
				Long tripleId = (Long) n.getProperty(FACT_NODE_KIWI_ID, null);
				
				if (tripleId == null)
					log.error("Fact node representing a KiWiTriple did not contain the triple id! Node id: #0", n.getId());
				else
					toDelete.add(tripleId);
			}
			
			if (n.hasProperty(RULE_NODE_KEY)) {
				log.error("A rule node exists between nodes marked for deletion! #0 id(#1)", n, n.getId());
				//String id = (String) n.getProperty(RULE_NODE_KEY);
				//indexService.removeIndex(n, RULE_NODE_KEY, id);
			}
			
			if (n.hasProperty(JUSTIFICATION_NODE_KEY)) {
				String id = (String) n.getProperty(JUSTIFICATION_NODE_KEY);
				indexService.removeIndex(n, JUSTIFICATION_NODE_KEY, id);
			}
			
			n.delete();
		}
		
		deleteTriples(toDelete);
	}
	
	//TODO add a check that only inferred triples are removed (create new named query tripleStore.deleteTripleIfInferred)
	//TODO inferred triples should be deleted not marked as deleted, right?
	/*private void removeTriple(Long id) {
		try {
			org.jboss.seam.transaction.Transaction.instance().begin();
			entityManager.joinTransaction();
			
			try {
				log.debug("Removing triple id #0", id);
				Query q = entityManager.createNamedQuery("tripleStore.deleteTripleIfNotBase");
				q.setParameter("id", id);
				int result = q.executeUpdate();
				if (result > 0)
					log.debug("\t removed.");
				else
					log.debug("\t NOT removed. It was a base triple.");
					
				
				org.jboss.seam.transaction.Transaction.instance().commit();
			} catch (IllegalStateException e) {
				org.jboss.seam.transaction.Transaction.instance().rollback();
				e.printStackTrace();
			} catch (TransactionRequiredException e) {
				org.jboss.seam.transaction.Transaction.instance().rollback();
				e.printStackTrace();
			} catch(PersistenceException e) {
				org.jboss.seam.transaction.Transaction.instance().rollback();
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (NotSupportedException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		} catch (RollbackException e) {
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}*/
	
	//TODO inferred triples should be deleted not marked as deleted, right?
	private void deleteTriples(Set<Long> ids) {
		//log.debug("Removing triples #0", ids);
		
		try {
			int i = 0;
			Set<Long> ids2update = new HashSet<Long>();
			for(Long id : ids) {
				try {
					ids2update.add(id);
					if(i % 20 == 0) {
						org.jboss.seam.transaction.Transaction.instance().begin();
						transactionService.registerSynchronization(
		                		KiWiSynchronizationImpl.getInstance(), 
		                		transactionService.getUserTransaction() );
						entityManager.joinTransaction();
						Query q = entityManager.createNamedQuery("tripleStore.deleteTriplesIfNotBase");
						q.setParameter("ids", ids2update);
						q.executeUpdate();
						
						org.jboss.seam.transaction.Transaction.instance().commit();
						ids2update = new HashSet<Long>();
					}
				} catch (IllegalStateException e) {
					org.jboss.seam.transaction.Transaction.instance().rollback();
					e.printStackTrace();
				} catch (TransactionRequiredException e) {
					org.jboss.seam.transaction.Transaction.instance().rollback();
					e.printStackTrace();
				} catch(PersistenceException e) {
					org.jboss.seam.transaction.Transaction.instance().rollback();
					e.printStackTrace();
				}
				i++;
			}
			if(!ids2update.isEmpty()) {
				try {
					org.jboss.seam.transaction.Transaction.instance().begin();
					transactionService.registerSynchronization(
	                		KiWiSynchronizationImpl.getInstance(), 
	                		transactionService.getUserTransaction() );
					entityManager.joinTransaction();
					Query q = entityManager.createNamedQuery("tripleStore.deleteTriplesIfNotBase");
					q.setParameter("ids", ids2update);
					q.executeUpdate();
					org.jboss.seam.transaction.Transaction.instance().commit();
				} catch (IllegalStateException e) {
					org.jboss.seam.transaction.Transaction.instance().rollback();
					e.printStackTrace();
				} catch (TransactionRequiredException e) {
					org.jboss.seam.transaction.Transaction.instance().rollback();
					e.printStackTrace();
				} catch(PersistenceException e) {
					org.jboss.seam.transaction.Transaction.instance().rollback();
					e.printStackTrace();
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (NotSupportedException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		} catch (RollbackException e) {
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			e.printStackTrace();
		}
	}	
	
	private Set<Node> getInNodes(Node node) {
		if (! node.hasProperty(JUSTIFICATION_NODE_KEY))
			throw new IllegalArgumentException("getInNodes() accepts only justification nodes but received a non-fact node: "+node+" id("+node.getId()+")");
		
		Set<Node> ins = new HashSet<Node>();
		
		Iterable<Relationship> rels = node.getRelationships(EdgeTypes.IN);
		for (Relationship rel : rels)
			ins.add(rel.getStartNode());
		
		return ins;
	}
	
	private Node getSupportedNode(Node node) {
		if (! node.hasProperty(JUSTIFICATION_NODE_KEY))
			throw new IllegalArgumentException("getSupportedNodes() accepts only justification nodes but received a non-fact node: "+node+" id("+node.getId()+")");
				
		Relationship rel = node.getSingleRelationship(EdgeTypes.JUSTIFICATION, Direction.OUTGOING);
		
		return rel.getEndNode();
	}
	
	private boolean allUnmarked(Set<Node> nodes) {
		for (Node n : nodes)
			if (isMarked( n ))
				return false;
		
		return true;
	}
	
	private void setMark(Node node) {
		node.setProperty(MARK, MARK);
		markedNodes.add(node);
	}
	
	private void unsetMark(Node node) {
		node.removeProperty(MARK);
		unmarkedNodes.add(node);
	}
	
	private boolean isMarked(Node node) {
		return node.hasProperty(MARK);
	}
	
	private void unmark(Node node) {
		if (! node.hasProperty(FACT_NODE_KEY))
			throw new IllegalArgumentException("unmark() accepts only fact nodes but received a non-fact node: "+node+" id("+node.getId()+")");
		
		assert isMarked(node);
		
		unsetMark(node);
		
		Iterable<Relationship> inRels = node.getRelationships(EdgeTypes.IN);
		for (Relationship rel : inRels) {
			Node justif = rel.getStartNode();
			if (! isMarked(justif))
				continue;
			
			Set<Node> ins = getInNodes(justif);
			
			if (allUnmarked(ins)) {
				unsetMark(justif);
				
				Node supported = getSupportedNode(justif);
				unmark(supported);
			}
		}
	}
	
	private void checkForReSupport() {		
		for (Node node : markedNodes) {
			if (! node.hasProperty(FACT_NODE_KEY))
				continue; //we want to check only fact nodes
			
			if (! isMarked(node))
				continue;//it may have been unmarked in the meantime. markedNodes contains all marked notes, it can't be updated while we are iterating through it, so only the mark is removed from each node but the node is left in markedNodes
			
			Node alternativeSupport = getUnmarkedJustification(node);
			
			if (alternativeSupport == null)
				continue;
			
			alternativeSupport.setProperty(CURRENT_SUPPORT, true);
			
			unmark(node);
		}
	}
/*	
	if ( ! hasSupport(node) ) {
		//the justification tree depending on the node to check has already been removed
		//so all that's left is to remove the node if it has no support

		if (node.hasRelationship()) {
			log.warn("Removing node id #0 that has no support but still has some relationships attached.", node.getId());
			Iterable<Relationship> rels = node.getRelationships();
			
			for (Relationship rel : rels)
				log.warn("\t #0 --- #1 --- #2", rel.getStartNode().getId(), rel.getType(), rel.getEndNode().getId());			
		}
		
		Long tripleId = (Long) node.getProperty(FACT_NODE_KIWI_ID, null);
		
		if (tripleId == null)
			log.error("Fact node representing a KiWiTriple did not contain the triple id! Node id: #0", node.getId());
		else
			toDelete.add(tripleId);
		
		removeNode(node);
	}
}

deleteTriples(toDelete);
	*/
	
	/** Implements the current support strategy algorithm for incremental updates in presence of cycles in the DDN.
	 * 
	 */
	//TODO what if we are asked to remove an inferred triple?
	private void removeTriple(KiWiTriple triple) {
		if (!triple.isBase()) {
			log.error("Asked to remove a not base triple with id #0: #1. Doing nothing.", triple.getId(), triple);
			return;
		}
		
		log.debug("Removing triple #0 #1.", triple.getId(), triple.toString());
				
		Node fact = getFactNode(kiwiTripleIdToNeoId(triple.getId()));
		
		if (fact == null)
			// the fact isn't part of any justification
			return;
		
		markedNodes.clear();
		unmarkedNodes.clear();
		
		
		// ---- current support strategy algorithm ---- 
		mark(fact);
		
		log.debug("#0 nodes have been marked.", markedNodes.size());
				
		checkForReSupport();
		
		log.debug("Alternative support found for #0 nodes", unmarkedNodes.size());
		
		assert markedNodes.containsAll( unmarkedNodes );
		
		markedNodes.removeAll(unmarkedNodes);

		removeNodes(markedNodes);
	}
	
	public ReasoningTaskStatistics removeTriples(Set<KiWiTriple> triples) {
		if(triples != null && triples.size() > 0) {
			ReasoningTaskStatistics stats = new ReasoningTaskStatistics();
			stats.start();
			setRunning(true);
			
			Transaction tx = neo.beginTx();
			try {
			
				for (KiWiTriple triple : triples) 
					removeTriple(triple);
				
				tx.success();
		
			} finally {
				tx.finish();
				setRunning(false);
			}			
			stats.stop();
			return stats;
		} else {
			return null;
		}
	}
	
	private Node[] getOrCreateFactsForTriples(Long[] tripleIds) {
		Node[] nodes = new Node[tripleIds.length];
		
		for (int i = 0; i < tripleIds.length; i++) {
			String factNodeId = kiwiTripleIdToNeoId(tripleIds[i]);
		
			nodes[i] = getFactNode(factNodeId);
			
			if (nodes[i] == null)
				nodes[i] = createAndIndexFact(factNodeId,tripleIds[i]);
		}
		
		return nodes;
	}
	
	private Node getFactNode(Long id) {
		return getFactNode(kiwiTripleIdToNeoId(id));
	}
	
	private Node getFactNode(String factNodeId) {
		return indexService.getSingleNode(FACT_NODE_KEY, factNodeId);		
	}
	
	private Node getRuleNode(String ruleNodeId) {
		return indexService.getSingleNode(RULE_NODE_KEY, ruleNodeId);
	}
	
	private String kiwiTripleIdToNeoId(Long id) {
		return "triple("+id+")";
	}
	
	private String kiwiRuleIdToNeoId(String id) {
		return "rule("+id+")";
	}
	
	private Node createAndIndexFact(String sid, KiWiTriple fact) {
		Node node = neo.createNode();
		node.setProperty(FACT_NODE_KEY, sid);
		node.setProperty(FACT_NODE_KIWI_ID, fact.getId());
		node.setProperty(BASE, fact.isBase());
		indexService.index(node, FACT_NODE_KEY, sid);
		
		return node;
	}

	private Node createAndIndexFact(String sid, Long factId) {
		Node node = neo.createNode();
		node.setProperty(FACT_NODE_KEY, sid);
		node.setProperty(FACT_NODE_KIWI_ID, factId);
		indexService.index(node, FACT_NODE_KEY, sid);
		
		return node;
	}
	
	private Node createAndIndexRule(String sid, String id) {
		Node node = neo.createNode();
		node.setProperty(RULE_NODE_KEY, sid);
		node.setProperty(RULE_NODE_KIWI_ID, id);
		indexService.index(node, RULE_NODE_KEY, sid);
		
		return node;
	}	
	
	private Node createAndIndexJustification(String id) {
		//log.debug("Creating new justification node: #0", id);
		
		Node node = neo.createNode();
		node.setProperty(JUSTIFICATION_NODE_KEY, id);
		indexService.index(node, JUSTIFICATION_NODE_KEY, id);
		
		return node;
	}

	public synchronized Boolean isRunning() {
		return running;
	}

	private synchronized void setRunning(Boolean running) {
		//need to outject manually because we set the flag after entering a method and before leaving it, so interceptors won't work		
		Contexts.getApplicationContext().set("reasonMaintenanceRunning", running);
		this.running = running;
		//log.info("Reason maintenance running set to #0", running);
	}		
	
}





