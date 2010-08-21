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
package kiwi.dashboard.action;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.recommendation.RuleBasedRecommendation;
import kiwi.model.user.User;
import kiwi.service.reasoning.ReasoningConstants;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

/**
 * A component supporting the management of personal rules. Used by the 
 * Dashboard rules.xhtml view.
 * 
 * @author freddurao
 *
 */
@Name("kiwi.dashboard.ruleAction")
@Scope(ScopeType.PAGE)
public class RuleBasedAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	@Out
	private User currentUser;
	
	@In
	private EntityManager entityManager;
	
	private String sentence;
	
	@In 
    FacesMessages facesMessages;
	
	@In
	private TripleStore tripleStore;	
	
	private List<RuleBasedRecommendation> ruleBasedRecommendations;
	
	public List<RuleBasedRecommendation> getRuleBasedRecommendations() {
		if(ruleBasedRecommendations == null) {
			ruleBasedRecommendations = new LinkedList<RuleBasedRecommendation>();
		}
		ruleBasedRecommendations = loadRuleBasedRecommendations();
		return ruleBasedRecommendations;
		
	}

	public void createRule() {
			RuleBasedRecommendation ruleBasedRecommendation = new RuleBasedRecommendation();
			ruleBasedRecommendation.setSentence(sentence);
			ruleBasedRecommendation.setUser(currentUser);
			entityManager.persist(ruleBasedRecommendation);
			entityManager.flush();
			FacesMessages.instance().add("Rule created successfully.");
	}

	public void generateRule(RuleBasedRecommendation ruleBasedRecommendation) {
			KiWiUriResource programUri = tripleStore.createUriResource(ReasoningConstants.KIWI_RDFS_SKWRL_PROGRAM);//the default program, see TriplestoreProgramLoader.getContent()
			KiWiUriResource property = tripleStore.createUriResource(ReasoningConstants.KIWI_PROGRAM_HAS_RULE);
			
			
			String rule2 = "tag-1: ($tagging holygoat:taggedResource $ci), " +
					              "($tagging holygoat:associatedTag $tag), (" +
					              "$tagging holygoat:taggedBy $user), " +
					              "($tagging holygoat:taggedOn $date), " +
					              "($tag kiwi:title \"ahoj\") ->" +
					              " ($newtagging holygoat:taggedResource $ci)," +
					              " ($newtagging holygoat:associatedTag $newtag)," +
					              " ($newtagging holygoat:taggedBy $user), " +
					              "($newtagging holygoat:taggedOn $date), " +
					              "($newtag kiwi:title \"inferred\")";
		
			 // @prefix holygoat: <http://www.holygoat.co.uk/owl/redwood/0.1/tags/>			
			
			
			//String rule="rdf-type: ($1 rdf:type $2), ($2 rdfs:subClassOf $3) ->	($1 rdf:type $3)";
			String rule = "tag-"+currentUser.getLogin().concat("-").concat(""+ruleBasedRecommendation.getId())+": (($x rdf:type "+Constants.NS_KIWI_CORE+"ContentItem)," +
			  "($x "+Constants.NS_HGTAGS+"taggedWithTag \""+ruleBasedRecommendation.getSentence()+"\")) -> " +
			  "(($x "+Constants.NS_KIWI_CORE+"recommendTo "+Constants.NS_KIWI_CORE+currentUser.getLogin()+"))";
			
			System.out.println(rule);
			KiWiLiteral literal = tripleStore.createLiteral(rule);
			tripleStore.createTriple(programUri, property, literal);
	}	

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<RuleBasedRecommendation> loadRuleBasedRecommendations() {
		List<RuleBasedRecommendation> ruleBasedRecommendations  = null;
		String s = "select u from RuleBasedRecommendation u where u.user =:user ";
		javax.persistence.Query q = entityManager.createQuery(s);
        q.setParameter("user", currentUser);
        try {
        	ruleBasedRecommendations = (List<RuleBasedRecommendation>)q.getResultList();
 		} catch (NoResultException ex) {
 			//
		}
		return ruleBasedRecommendations;
	}
	
	public void removeRule(RuleBasedRecommendation rule) {
			entityManager.remove(rule);
			entityManager.flush();
			FacesMessages.instance().add("Rule deleted successfully.");
	}	

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	
	
}
