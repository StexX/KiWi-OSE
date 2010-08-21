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
package kiwi.service.reasoning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

import kiwi.api.reasoning.ProgramLoader;
import kiwi.api.reasoning.ProgramProcessorLocal;
import kiwi.api.reasoning.ProgramProcessorRemote;
import kiwi.api.reasoning.reasonmaintenance.ReasonMaintenanceService;
import kiwi.api.system.StatusService;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.status.SystemStatus;
import kiwi.service.reasoning.ReasoningConfiguration.ReasoningFeature;
import kiwi.service.reasoning.ast.ConstructionVariable;
import kiwi.service.reasoning.ast.Formula;
import kiwi.service.reasoning.ast.Program;
import kiwi.service.reasoning.ast.Term;
import kiwi.service.reasoning.ast.TriplePattern;
import kiwi.service.reasoning.ast.Variable;
import kiwi.service.reasoning.reasonmaintenance.Justification;
import kiwi.service.transaction.KiWiSynchronizationImpl;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;

import com.planetj.math.rabinhash.RabinHashFunction64;

//TODO check that newly inferred triples are not reported back as new ones in transaction success (or leverage it)

/** ProgramProcessor is the main reasoning component.
 * 
 * It takes updates in form of ReasoningTasks and compiled rules and runs the rules on the data using forward-chaining.
 * ProgramProcessor also notifies ReasonMaintenance of newly derived facts.
 * 
 * @author Jakub Kotowski
 *
 */
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("programProcessor")
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ProgramProcessorImpl implements ProgramProcessorRemote, ProgramProcessorLocal {
	private static String DEFAULT_NS_CHAR = "/";
	private static String DEFAULT_NEWNODE_NS = "http://www.kiwi-project.eu/kiwi/reasoning/newnode/";

	@Logger
	private Log log;

	@In
	private TripleStore tripleStore;
	@In
	private EntityManager entityManager;
	
	@In
	private TransactionService transactionService;
	
	@In(value="triplestoreProgramLoader")
	private ProgramLoader programLoader;
	
	private ProgramLoader customProgramLoader;

	@In
	private RuleInstantiator ruleInstantiator;
	
	@In
	private ProgramCompiler programCompiler;
	
	@In
	private RuleBodyProcessor ruleBodyProcessor;

	@In("kiwi.core.statusService")
	private StatusService statusService;
	private SystemStatus status;
	
	@In("reasonMaintenanceService")
	private ReasonMaintenanceService reasonMaintenance;
	
	@In
	private ReasoningConfiguration reasoningConfiguration;

	@In("TBox")
	private TBox tbox;

	private List<CompiledRule> compiledSystemRules;

	/**
	 * If tripleIds is [9, 4, 1] and rule.toString().equals("B->H") then the
	 * resulting string is "9,4,1,B->H"
	 * 
	 * That is, for each body match and each rule there will be a new id string.
	 */
	private StringBuffer constructNewNodeIdString(List<Long> tripleIds,	CompiledRule rule) {
		StringBuffer newNodeIdToHash = new StringBuffer();

		for (Long id : tripleIds)
			newNodeIdToHash.append(id).append(",");

		newNodeIdToHash.append(rule.toString());

		return newNodeIdToHash;
	}

	private String createNewUri(String uri, String newNodeId) {
		RabinHashFunction64 rabinHash = RabinHashFunction64.DEFAULT_HASH_FUNCTION;
		String newUri = uri;

		if (uri.charAt(uri.length() - 1) != '/'
				&& uri.charAt(uri.length() - 1) != '#')
			newUri = uri + DEFAULT_NS_CHAR;

		newUri = newUri
				+ Long.toString(rabinHash.hash(newNodeId), Character.MAX_RADIX);

		//log.debug("Created new URI: " + newUri + " for string id \""
		//		+ newNodeId + "\"");
		return newUri;
	}

	/**
	 * The new resource id is a hash of body result triple ids, the rule string and the variable name.
	 * That means that for each body match and each rule and each construction variable there will be a 
	 * different resource id.
	 * 
	 * Hashing is done using the Robin's fingerprinting function so that there are theoretical probabilistic
	 * guarantees of a very low collision probability.
	 * 
	 * @param result
	 * @param rule
	 * @return
	 */
	private Map<Variable, KiWiUriResource> constructNewNodeMap(RuleBodyResult result, CompiledRule rule) {
		Map<Variable, KiWiUriResource> newnodemap = new HashMap<Variable, KiWiUriResource>();

		if (! rule.constructsNewResources())
			return newnodemap;

		StringBuffer newNodeIdToHash = constructNewNodeIdString(result.getTripleIds(), rule);

		for (Variable variable : rule.getHead().getVariables()) {
			if (! rule.isConstructionVariable(variable))
				continue;
			
			if (newnodemap.containsKey(variable))
				continue;
			
			StringBuffer stringId = new StringBuffer(newNodeIdToHash);
			stringId.append(variable.getName());
			
			String namespace;
			if (variable.getTermType() == Term.TermType.CONSTRUCTION_VARIABLE) {
				ConstructionVariable var = (ConstructionVariable) variable;
				namespace = var.getConstructionUri();
			} else
				namespace = DEFAULT_NEWNODE_NS;
			
			String uri = createNewUri(namespace, stringId.toString());
			KiWiUriResource uriResource = tripleStore.createUriResource( uri ); 
			newnodemap.put(variable, uriResource);
		}
		
		return newnodemap;
	}

	/**
	 * Constructs new triples according to the rule head and matches for the
	 * rule body.
	 * 
	 * If result is null then ReasoningException is thrown.
	 * 
	 * @param ruleBodyResult
	 *            Contains query results - first results for selected variables
	 *            in the rule body, then triple ids for each matched body
	 *            triple. This means that the results contain
	 *            rule.variableMap.size variable selections +
	 *            rule.body.getNumConjuncts triple id selections.
	 *            
	 *  @return List of inferred triples, new and old.
	 */
	private List<KiWiTriple> constructTripleForResult(RuleBodyResult ruleBodyResult, CompiledRule rule) {
		if (ruleBodyResult == null)
			throw new ReasoningException("RuleBodyResult must not be null.");
		
		KiWiResource subject; KiWiUriResource property; KiWiNode object; 
		
		List<KiWiTriple> newTriples = new ArrayList<KiWiTriple>();


		Map<Variable, KiWiUriResource> newnodemap = constructNewNodeMap(ruleBodyResult, rule);

		for (Formula formula : rule.getHead().getFormulas()) {
			TriplePattern tp = (TriplePattern) formula;
						
			switch (tp.getSubject().getTermType()) {
				case VARIABLE:
				case CONSTRUCTION_VARIABLE:
					Variable variable = (Variable) tp.getSubject();
					if (rule.isConstructionVariable(variable))
						subject = newnodemap.get(tp.getSubject());
					else {
						Object obj = ruleBodyResult.getBinding(variable);
						if (obj instanceof KiWiLiteral)
							continue; //the rule looks like (s p $1) -> ($1 x y) and $1 matched a literal so it's in fact no match
						
						subject = (KiWiResource) obj;
					}
					break;
				default: //the term isn't a variable so the resource was constructed already at compilation time.
					subject = tp.getKiwiSubject();
			}
			
			switch (tp.getProperty().getTermType()) {
				case VARIABLE:
				case CONSTRUCTION_VARIABLE:
					Variable variable = (Variable) tp.getProperty();
					property = (KiWiUriResource) ruleBodyResult.getBinding(variable);
					break;
				default:
					property = tp.getKiwiProperty();
			}
			
			switch (tp.getObject().getTermType()) {
				case VARIABLE:
				case CONSTRUCTION_VARIABLE:
					Variable variable = (Variable) tp.getObject();
					if (rule.isConstructionVariable(variable))
						object = newnodemap.get(tp.getObject());
					else {
						Object obj = ruleBodyResult.getBinding(variable);
						object = (KiWiNode) obj;
					}
					break;
				default: //the term isn't a variable so the resource was constructed already at compilation time.
					object = tp.getKiwiObject();
			}
			KiWiTriple newTriple = tripleStore.createTriple(subject, property, object);
			newTriple.setInferred(true);
			if (newTriple.isNew())
				newTriple.setBase(false); //createTriple() sets the triple as base by default
			newTriples.add(newTriple);			
		}
		//log.debug("Reasoning ADDED new triples #0 . Now the transaction should complete", newTriples);
		
		return newTriples;
	}

	/**
	 * 
	 * If addedTriples == null then it will run reasoning on the whole database.
	 * 
	 */
	public ReasoningTaskStatistics process(Set<KiWiTriple> addedTriples) {
		long start, end;
		status = statusService.getSystemStatus("reasonerStatus");

		start = System.currentTimeMillis();

		setStatusMessage("Loading program.");
		Program program;
		
		if (customProgramLoader != null)
			program = customProgramLoader.loadProgram("");
		else
			program = programLoader.loadProgram(ReasoningConstants.KIWI_DEFAULT_SKWRL_PROGRAM);
				
		CompiledProgram compiledProgram = programCompiler.compileProgram(program);
		
		if(compiledProgram != null) {
			List<CompiledRule> compiledRules = compiledProgram.getRules();
	
			end = System.currentTimeMillis();
	
			log.info("Rule program loading and compilation took #0 ms.", end - start);
	
			log.info("Starting reasoning.");
	
			start = System.currentTimeMillis();
	
			ReasoningTaskStatistics stats = processSemiNaively(compiledRules, addedTriples);
	
			// tripleStore.flush();
	
			end = System.currentTimeMillis();
	
			return stats;
		} else {
			return null;
		}
	}

	public ReasoningTaskStatistics processNaively(List<CompiledRule> compiledRules) {
		ReasoningTaskStatistics stats = new ReasoningTaskStatistics();
		int newTriplesInRound = 1;

		log.info("Starting naive forward-chaining.");

		while (newTriplesInRound > 0) {
			RoundStatistics roundStats = new RoundStatistics();
			roundStats.start();

			try {
				//log.debug("constructTripleForResult() transaction begin");
				Transaction.instance().begin();
				transactionService.registerSynchronization(
                		KiWiSynchronizationImpl.getInstance(), 
                		transactionService.getUserTransaction() );
			} catch (NotSupportedException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			for (CompiledRule rule : compiledRules) {

				roundStats.increaseRulesCount();
				
				//log.info("Processing rule " + rule);
				//log.info("Running query #0", rule.query);

				List<RuleBodyResult> ruleBodyResults = ruleBodyProcessor.process(rule);
				
				if (ruleBodyResults == null)
					continue;

				//log.info("Query returned #0 results.", ruleBodyResults.size());

				Set<Justification> justifications = new HashSet<Justification>();

				for (RuleBodyResult ruleBodyResult : ruleBodyResults) {
					List<KiWiTriple> triplesForResult = constructTripleForResult(ruleBodyResult, rule);
					roundStats.increaseGeneratedTriplesCount(triplesForResult.size());
					
					for (KiWiTriple triple : triplesForResult)
						if (triple.isNew())
							roundStats.increaseNewTriplesCount();
					
					//log.debug("Created #0 new triples for result #1.",newTriplesForResult == null ? 0 : newTriplesForResult.size(), result.toString());

					for (KiWiTriple triple : triplesForResult) // it is important that we add justifications for all triples, not only really new ones 
						justifications.add(new Justification(triple, ruleBodyResult.getTripleIds(), rule.getName()));
				}
				
				//log.info("#0 triples were really new.", newTriplesForRule);
			}// for compiled rules

			//log.info("Created #0 triples in this round.", newTriplesInRound);

			try {
				//log.debug("constructTripleForResult() transaction commit");
				roundStats.startPersistence();
				Transaction.instance().commit();
				roundStats.stopPersistence();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (RollbackException e) {
				e.printStackTrace();
			} catch (HeuristicMixedException e) {
				e.printStackTrace();
			} catch (HeuristicRollbackException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			roundStats.stop();
			stats.addRound(roundStats);
		}// while still new triples

		log.info("Reasoning stats: \n" + stats);
		return stats;
	}

	public ReasoningTaskStatistics processAddedTriples(Set<KiWiTriple> addedTriples) {
		return process(addedTriples);
	}

	private boolean matchesARule(KiWiTriple triple,
			List<CompiledRule> compiledRules) {
		for (CompiledRule rule : compiledRules) {
			if (rule.matchesBody(triple)) {
				//log.debug("Triple #0 matches rule #1.", triple.toString(), rule
				//		.toString());// TODO: remove this logging
				return true;
			}
		}

		return false;
	}

	//@Transactional(TransactionPropagationType.MANDATORY)
	public ReasoningTaskStatistics processRemovedTriples(Set<KiWiTriple> removedTriples) {
		tbox.removeAll(removedTriples);
		
		return reasonMaintenance.removeTriples(removedTriples);
	}

	private void addJustifications(Set<Justification> justifications) {
		long begin = System.currentTimeMillis();
		
		if (justifications == null) {
			log.error("addJustifications() should never be called with null parameter. Skipping.");
			return;
		}
		
		//log.info("Adding #0 justifications.", justifications.size());
		reasonMaintenance.addJustifications(justifications);
		
		long end = System.currentTimeMillis();
		log.info("Adding #0 justifications took #1 ms.", justifications.size(), end - begin);		
	}
		
	/**
	 * One round of semi-naive forward-chaining.
	 * 
	 * Goes once through all rules and marks those new triples which can
	 * generate new triples in the next round.
	 * 
	 * @param compiledRules
	 * @return Returns a pair &lt;newTriples, markedtriples&gt;
	 */
	private SemiNaiveOneRoundResult semiNaiveOneRound(List<CompiledRule> compiledRules) {
		
		SemiNaiveOneRoundResult roundResult = new SemiNaiveOneRoundResult();
		RoundStatistics stats = roundResult.getRoundStatistics();
		stats.start();
		
		for (CompiledRule rule : compiledRules) {

			if (reasoningConfiguration.isEnabled(ReasoningFeature.HYBRID_REASONING))
				if (!rule.isSatisfiableBy(tbox)) {
					log.debug("Skipping not satisfiable rule " + rule);
					continue;
				}

			log.debug("Processing rule " + rule);
				
			//log.debug("Running query #0", rule.query);

			stats.increaseRulesCount();
			
			List<RuleBodyResult> ruleBodyResults = ruleBodyProcessor.process(rule);
			//log.debug("Results for rule #0: #1", rule.getName(), ruleBodyResults);
			
			if (ruleBodyResults == null)
				continue;

			for (RuleBodyResult ruleBodyResult : ruleBodyResults) {
				List<KiWiTriple> triplesForResult = constructTripleForResult(ruleBodyResult, rule);
				stats.increaseGeneratedTriplesCount(triplesForResult.size());

				for (KiWiTriple triple : triplesForResult) // it is important that we add justifications for all triples, not only really new ones 
					roundResult.addJustification(new Justification(triple, ruleBodyResult.getTripleIds(), rule.getName()));
				
				int newCount = 0;
				for (KiWiTriple triple : triplesForResult) {
					if (!triple.isNew()) 
						continue;
					
					stats.increaseNewTriplesCount(); newCount++;
					
					if (reasoningConfiguration.isEnabled(ReasoningFeature.HYBRID_REASONING))
						tbox.add(triple);
					
					if (matchesARule(triple, compiledSystemRules)) {
						roundResult.addMarkedTriple(triple);
						stats.increaseMarkedTriplesCount();
					}
				}
				
//				log.debug("Created #0 triples (#1 new) for result #2.", triplesForResult.size(), newCount, ruleBodyResult);

			}
//			log.debug("Query returned #0 results.", ruleBodyResults.size());
//			log.debug("#0 triples were really new.", roundResult.getMarkedTriples().size());

		}// for compiled rules

//		log.debug("This round generated #0 new triples", roundResult.getRoundStatistics().getNewTriplesCount());
//		log.debug(" ------------------------ SEMINAIVE ONE ROUND MARKED "
//				+ roundResult.getMarkedTriples().size() + " TRIPLES ------------------------ ");

		stats.stop();
		return roundResult;
	}

	private void setStatusMessage(String msg) {
		if (status != null)
			status.setMessage(msg);
	}

	@SuppressWarnings("unused")
	private void setStatusProgress(int progress) {
		if (status != null)
			status.setProgress(progress);
	}
		
	private void setRound(Set<KiWiTriple> triples, int round) {

		int notpersisted = 0;
		int modified = 0;
		try {
			Set<Long> ids = new HashSet<Long>(200);
			int i = 0;
			for (KiWiTriple triple : triples) {
				if (triple.getId() != null)
					ids.add(triple.getId());
				else { 
					triple.setReasoningRound(round);
					notpersisted++;
				}
				try {
					if(i % 200 == 0) {
						log.debug("setRound() transaction begin");
						Transaction.instance().begin();
						transactionService.registerSynchronization(
		                		KiWiSynchronizationImpl.getInstance(), 
		                		transactionService.getUserTransaction() );
						entityManager.joinTransaction();

						Query q = entityManager.createNamedQuery("programProcessor.markTriplesForRoundById");
						q.setParameter("round", round);
						q.setParameter("tripleIds", ids);
						modified = modified + q.executeUpdate();
						log.debug("setRound() transaction commit");
						Transaction.instance().commit();
						ids = new HashSet<Long>(200);
					}
				} catch(IllegalStateException e) {
					log.error("setRound() transaction rollback");
					Transaction.instance().rollback();
					e.printStackTrace();
				} catch(TransactionRequiredException e) {
					log.error("setRound() transaction rollback");
					Transaction.instance().rollback();
					e.printStackTrace();
				} catch(PersistenceException e) {
					log.error("setRound() transaction rollback");
					Transaction.instance().rollback();
					e.printStackTrace();
				}
				i++;
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

		log.info("Mark triples modified #0 persisted triples and #1 not persisted triples.", modified, notpersisted);
		
/*		Query q = entityManager.createQuery("from KiWiTriple t where t.reasoningRound = "+round);
		
		List list = q.getResultList();
		for (Object obj : list) {
			KiWiTriple t = (KiWiTriple)obj;
			log.debug("Round #0 triple: #1, id:#2, s:#3, p:#4, o:#5", round, obj.toString(), t.getId(), t.getSubject().getId(), t.getProperty().getId(), t.getObject().getId());
		}
		
		q = entityManager.createQuery("from PatternNode n");
		
		list = q.getResultList();
		for (Object obj : list) {
			log.debug("PatternNode: #0", obj.toString());
		}		
		
		q = entityManager.createQuery("from TriplePattern n");
		
		list = q.getResultList();
		for (Object obj : list) {
			log.debug("TriplePattern #0 ", obj.toString());
		}		
		
		q = entityManager.createQuery("from RuleBody n");
		
		list = q.getResultList();
		for (Object obj : list) {
			log.debug("RuleBody #0 ", obj.toString());
		}*/		
	}
	
	private void unmarkTriplesMarkedForRound(int round) {
		try {
			log.debug("unmarkTriplesMarkedForRound() transaction begin");
			Transaction.instance().begin();
			transactionService.registerSynchronization(
            		KiWiSynchronizationImpl.getInstance(), 
            		transactionService.getUserTransaction() );
			
			entityManager.joinTransaction();
			try {
				Query q = entityManager.createNamedQuery("programProcessor.unmarkTriplesMarkedForRound");
				q.setParameter("round", round);
				int modified;
				modified = q.executeUpdate();
				log.debug("Unmark triples modified #0 triples", modified);
				log.debug("unmarkTriplesMarkedForRound() transaction commit");
				Transaction.instance().commit();
				
			} catch(IllegalStateException e) {
				log.debug("unmarkTriplesMarkedForRound() transaction rollback");
				Transaction.instance().rollback();
				e.printStackTrace();
			} catch(TransactionRequiredException e) {
				log.debug("unmarkTriplesMarkedForRound() transaction rollback");
				Transaction.instance().rollback();
				e.printStackTrace();
			} catch(PersistenceException e) {
				log.debug("unmarkTriplesMarkedForRound() transaction rollback");
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
	}
	
	private ReasoningTaskStatistics processSemiNaively(List<CompiledRule> compiledRules, Set<KiWiTriple> addedTriples) {
		ReasoningTaskStatistics stats = new ReasoningTaskStatistics();
		stats.start();
		Set<KiWiTriple> markedTriples;

		int round = 1;

		// semiNaiveOneRound has to mark triples according to the original
		// rules, not instances!
		compiledSystemRules = compiledRules;

		log.debug("Starting semi-naive forward-chaining.");
		
		//log.debug("Set of added triples: #0 ", addedTriples);

		round = 0;

		if (addedTriples == null) { 
			// we are asked to process whatever changed
			// in the db (we are not given the change)

			round++;
			setStatusMessage("Round " + round);
			SemiNaiveOneRoundResult roundResult = semiNaiveOneRound(compiledSystemRules);
			markedTriples = roundResult.getMarkedTriples();
			RoundStatistics roundStats = roundResult.getRoundStatistics();
			
				roundStats.startPersistence();
			
			setRound(markedTriples, round+1);
				roundStats.stopPersistence();
			
				roundStats.startAddingJustifications();
			addJustifications(roundResult.getJustifications());
				roundStats.stopAddingJustifications();
				
			stats.addRound(roundStats);
			
		} else { // we are given the change as addedTriples
			markedTriples = addedTriples;
				stats.startPersistence();
			setRound(markedTriples, round+1);
				stats.stopPersistence();

			log.debug("PROCESSING #0 ADDED TRIPLES.", markedTriples.size());
		}
		
		
		while (!markedTriples.isEmpty()) {
			round++;

			setStatusMessage("Round " + round);

			
			List<CompiledRule> compiledInstances = ruleInstantiator.instantiate(compiledSystemRules, markedTriples, round);

			try {
				//log.debug("constructTripleForResult() transaction begin");
				Transaction.instance().begin();
				transactionService.registerSynchronization(
                		KiWiSynchronizationImpl.getInstance(), 
                		transactionService.getUserTransaction() );
			} catch (NotSupportedException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			SemiNaiveOneRoundResult roundResult = semiNaiveOneRound(compiledInstances);
			
			try {
				//log.debug("constructTripleForResult() transaction commit");
				Transaction.instance().commit();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (RollbackException e) {
				e.printStackTrace();
			} catch (HeuristicMixedException e) {
				e.printStackTrace();
			} catch (HeuristicRollbackException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}
						
			markedTriples = roundResult.getMarkedTriples();
			RoundStatistics roundStats = roundResult.getRoundStatistics();
			
				roundStats.startPersistence();
			setRound(markedTriples, round+1);
						
				unmarkTriplesMarkedForRound(round);
			
			// reasoning requires that all triples have ids, new ones too
			
				roundStats.stopPersistence();
			
				roundStats.startAddingJustifications();
			addJustifications(roundResult.getJustifications());
				roundStats.stopAddingJustifications();

			//log.debug("Round #0 marked #1 triples.", round, markedTriples.size());
			stats.addRound(roundStats);

		}// while there are some marked triples

		//log.info("Reasoning stats: \n" + stats);

		stats.stop();
		return stats;
	}

	public ProgramLoader getProgramLoader() {
		return customProgramLoader;
	}

	public void setProgramLoader(ProgramLoader programLoader) {
		this.customProgramLoader = programLoader;
	}
	
}


