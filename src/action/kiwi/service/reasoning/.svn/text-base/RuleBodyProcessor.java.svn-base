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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.service.reasoning.ast.BNode;
import kiwi.service.reasoning.ast.Formula;
import kiwi.service.reasoning.ast.Literal;
import kiwi.service.reasoning.ast.TriplePattern;
import kiwi.service.reasoning.ast.Uri;
import kiwi.service.reasoning.ast.Variable;

/**
 * @author Jakub Kotowski
 *
 */
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("ruleBodyProcessor")
public class RuleBodyProcessor {
	@In
	private TripleStore tripleStore;

	@In
	private EntityManager entityManager;
	
	@SuppressWarnings("unused")
	@Logger
	private Log log;
	
	/**
	 * @param rule
	 * @return Returns null if the body is not satisfied by the current state of the triple store.
	 */
	private List<RuleBodyResult> executeGroundRuleBody(CompiledRule rule) {
		if (!rule.isGround())
			throw new IllegalArgumentException(
					"getTriplesMatchingBody(rule) works only on ground rules.");

		log.debug("Executing ground rule #0.", rule.getName());
		
		RuleBodyResult ruleBodyResult = new RuleBodyResult();
		
		for (Formula formula : rule.getBody().getFormulas()) {
			TriplePattern tp = (TriplePattern) formula;
			
			if (!tp.isGround())
				throw new ReasoningException("Assertion failed: supposedly ground rule body contains a not ground triple.");

			Query q = entityManager.createNamedQuery("tripleStore.tripleIdBySPO");
			KiWiResource subject, property; 
			KiWiNode object;

			switch (tp.getSubject().getTermType()) {
				case BNODE:
					BNode bnode = (BNode) tp.getSubject();
					subject = tripleStore.createAnonResource( bnode.getName() );
					break;
				case URI:
					Uri uri = (Uri) tp.getSubject();
					subject = tripleStore.createUriResource( uri.getUri() );
					break;
				default:
					throw new ReasoningException("Unexpected term type at a body triple subject position: "+tp.getSubject().getTermType());
			}
			
			switch (tp.getProperty().getTermType()) {
				case URI:
					Uri uri = (Uri) tp.getProperty();
					property = tripleStore.createUriResource( uri.getUri() );
					break;				
				default:
					throw new ReasoningException("Unexpected term type at a body triple property position: "+tp.getProperty().getTermType());
			}

			switch (tp.getObject().getTermType()) {
			case BNODE:
				BNode bnode = (BNode) tp.getObject();
				object = tripleStore.createAnonResource( bnode.getName() );
				break;
			case URI:
				Uri uri = (Uri) tp.getObject();
				object = tripleStore.createUriResource( uri.getUri() );
				break;
			case LITERAL:
			case TYPED_LITERAL:
				Literal literal = (Literal) tp.getObject();
				object = tripleStore.createLiteral( literal.getLiteral() );
				break;
			default:
				throw new ReasoningException("Unexpected term type at a body triple object position: "+tp.getObject().getTermType());
			}

			q.setHint("org.hibernate.cacheable", true);
			q.setParameter("subject", subject);
			q.setParameter("property", property);
			q.setParameter("object", object);

			q.setMaxResults(1);

			Object obj;

			try {
				obj = q.getSingleResult();
			} catch (NoResultException ex) {
				return null;
			}

			Long tripleId = (Long) obj;
			
			ruleBodyResult.addTripleId(tripleId);
		}

		List<RuleBodyResult> results = new ArrayList<RuleBodyResult>();
		results.add(ruleBodyResult);
		
		return results;
	}
	
	/**
	 * The mess with triple sets is here because of a bug in Hibernate:
	 * http://opensource.atlassian.com/projects/hibernate/browse/HHH-2166
	 * 
	 * StackOverflow in case of queries with long value sets ("where t.id in (:tripleIds:)")
	 * 
	 * @param rule
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<RuleBodyResult> executeRuleBody(CompiledRule rule) {
		List<RuleBodyResult> result = new ArrayList<RuleBodyResult>();

		log.debug("Executing rule #0. Query: #1", rule.getName(), rule.getQuery());
/*		if (rule.isInstance()) {
			log.info("Running subquery: "+rule.subquery);
			Query subq = entityManager.createQuery(rule.subquery);
			List subqueryResults = null;
			subq.setParameter("ruleName", rule.getName());
//			log.info("setting name '#0'", rule.getName());
			subqueryResults = subq.getResultList();
			Set<Long> ids = ((CompiledRuleInstance)rule).getInstantiatingTripleIds();
			log.info("Subquery for #2 returned #0 results. And originally there were #1 marked triples.",subqueryResults.size(), ids.size(), rule.getName());
			//if (((CompiledRuleInstance)rule).getInstantiatingTripleIds().size() < 6)
			//for (Long id : ((CompiledRuleInstance)rule).getInstantiatingTripleIds())
			if (subqueryResults.size() != ids.size()) {
				log.info("Original triple ids: #0", ids.toString());
				log.info("Subquery result: #0",subqueryResults.toString());
				log.info("The suspicious query is: #0", rule.subquery);
				throw new ReasoningException("Subquery returned a wrong number of results.");
			}
		}*/
		Query q = entityManager.createQuery(rule.getQuery());
		q.setHint("org.hibernate.cacheable", true);		
		
		List queryResults = null;

		/*List<Set<Long>> tripleSets = new ArrayList<Set<Long>>();
		if (rule.isInstance()) {
			Set<Long> triples = ((CompiledRuleInstance)rule).getInstantiatingTripleIds();
			if (triples.size() < 2000)
				tripleSets.add(new HashSet<Long>( triples ));
			else {
				Set<Long> set = new HashSet<Long>();
				int i = 0;
				for (Long id : triples) {
					i++;
					set.add(id);
					if (i % 2000 == 0) {
						tripleSets.add(set);
						set = new HashSet<Long>();
					}
				}
				if (!set.isEmpty())
					tripleSets.add(set);
			}
		} else tripleSets.add(new HashSet());*/
		
		//for (Set set : tripleSets) {			
			//if (rule.isInstance()) { 
				//q.setParameter("tripleIds", set);
				//q.setParameter("ruleName", rule.getName());
			//}
	
			queryResults = q.getResultList();
			
			
			for (Object object : queryResults) {
				Object[] singleQueryResult = (Object[]) object;
				RuleBodyResult ruleBodyResult = new RuleBodyResult();
				
				List<Variable> selectVariables = rule.getSelectVariables();
				for (int i = 0; i < selectVariables.size(); i++) 
					ruleBodyResult.addVariableBinding(selectVariables.get(i), singleQueryResult[i]);
				
				for (int i = selectVariables.size(); i < selectVariables.size() + rule.getBody().getFormulas().size(); i++) 
					ruleBodyResult.addTripleId( (Long) singleQueryResult[i] );
				
				result.add(ruleBodyResult);
			}
		//}
					
		return result;
	}

	public List<RuleBodyResult> process(CompiledRule rule) {
		if (rule.isGround())
			return executeGroundRuleBody(rule);
		
		return executeRuleBody(rule);
	}
	
}
