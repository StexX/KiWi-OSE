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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import kiwi.api.triplestore.TripleStore;
import kiwi.service.reasoning.ast.BNode;
import kiwi.service.reasoning.ast.Conjunction;
import kiwi.service.reasoning.ast.Formula;
import kiwi.service.reasoning.ast.Literal;
import kiwi.service.reasoning.ast.Rule;
import kiwi.service.reasoning.ast.Term;
import kiwi.service.reasoning.ast.TriplePattern;
import kiwi.service.reasoning.ast.Uri;
import kiwi.service.reasoning.ast.Variable;
import kiwi.service.reasoning.parser.ParseException;
import kiwi.service.reasoning.parser.SimpleKWRLProgramParser;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/** Rule compiler takes parsed rules and compiles them into CompiledRules.
 * 
 * It works by translating rules into HQL queries.
 * 
 * @author Jakub Kotowski
 *
 */
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Name("ruleCompiler")
public class RuleCompiler {
	//Most of the compilation code now assumes that rule bodies are always conjunctions
	//temporary solution before implementing more general bodies
	public static final String SIMPLE_BODIES="code that needs to be changed to support more than simple bodies refers to this constant.";
	private static final String AND = " and ";
	private static final String TRIPLE_PREFIX = "t";

	@In
	private TripleStore tripleStore;
	
	@Logger
	private Log log;
			
	
	/** A helper method to determine all the places at which a variable occurs. The map is then used to construct the select part and the where clause of a query.
	 * 
	 * For rule "($1 http://foaf/firstName $2), ($1 $3 $2) -> ($1 http://some/property http://some/thing)"
	 * 
	 * it constructs the following map:
	 * 		String		ArrayList<String>
	 * 		"$1"		"t0.subject", "t1.subject" 
	 * 		"$2"		"t0.object", "t2.object"
	 * 		"$3"		"t2.property"
	 * 
	 * @param rule A rule returned by the parser.
	 * @return Returns the map described above.
	 */
	private static VariablePositions getVariablePositions(Rule rule) {
		Conjunction body = (Conjunction) rule.getBody().getFormula();
		VariablePositions vars = new VariablePositions();
		
		//ArrayList<String> vars; 
		int i = 1;
		for (Formula formula : body.getFormulas()) {
			TriplePattern tp = (TriplePattern) formula;
			
			if (tp.getSubject().isVariable()) {
				VariablePosition pos = new VariablePosition(i, TriplePattern.TriplePatternPosition.SUBJECT);
				vars.addPosition((Variable) tp.getSubject(), pos);
			}
			
			if (tp.getProperty().isVariable()) {
				VariablePosition pos = new VariablePosition(i, TriplePattern.TriplePatternPosition.PROPERTY);
				vars.addPosition((Variable) tp.getProperty(), pos);
			}
			
			if (tp.getObject().isVariable()) {
				VariablePosition pos = new VariablePosition(i, TriplePattern.TriplePatternPosition.OBJECT);
				vars.addPosition((Variable) tp.getObject(), pos);
			}		
			
			i++;
		}
		
		return vars;
	}
		
	
	//TODO typed literals
	/** Compiles rule head.
	 * 
	 * Assumes that the body has already been compiled: expects rule.variableMap to be already constructed and filled.
	 * 
	 * We allow b-nodes in subject and object because of instantiated rules.
	 * 
	 * @param rule The partially compiled rule.
	 */
	private void compileRuleHead(CompiledRule rule) {	
		if (rule.getRule().getHead().isInconsistency()) {
			if (rule.getRule().getHead().getFormula() == null)
				throw new ReasoningException("Inconsistency symbol in the rule head must be translated to triples before rule compilation!");
		}
		
		Conjunction head = rule.getHead();
		
		for (Formula formula : head.getFormulas()) {
			TriplePattern tp = (TriplePattern) formula;
			
			switch (tp.getSubject().getTermType()) {
				case BNODE:
					BNode bnode = (BNode) tp.getSubject();
					tp.setKiwiSubject(tripleStore.createAnonResource( bnode.getName() ));
					break;
				case URI:
					Uri uri = (Uri) tp.getSubject();
					tp.setKiwiSubject(tripleStore.createUriResource( uri.getUri() ));
					break;
				case VARIABLE:
				case CONSTRUCTION_VARIABLE:
					Variable variable = (Variable) tp.getSubject();
					if (! rule.getBody().getVariables().contains(variable))
						rule.addConstructionVariable(variable);
					break;
				default:
					throw new ReasoningException("Unsupported term type in head triple subject position: "+tp.getSubject().getTermType());
			}
			
			switch (tp.getProperty().getTermType()) {
				case URI:
					Uri uri = (Uri) tp.getProperty();
					tp.setKiwiProperty( tripleStore.createUriResource(uri.getUri()) );
					break;
				case VARIABLE:
				case CONSTRUCTION_VARIABLE:
					Variable variable = (Variable) tp.getProperty();
					if (! rule.getBody().getVariables().contains(variable))
						rule.addConstructionVariable(variable);
					break;
				default:
					throw new ReasoningException("Unsupported term type in head triple property position: "+tp.getProperty().getTermType());
			}
			
			switch (tp.getObject().getTermType()) {
				case BNODE:
					BNode bnode = (BNode) tp.getObject();
					tp.setKiwiObject(tripleStore.createAnonResource( bnode.getName() ));
					break;
				case URI:
					Uri uri = (Uri) tp.getObject();
					tp.setKiwiObject(tripleStore.createUriResource( uri.getUri() ));
					break;
				case LITERAL:
				case TYPED_LITERAL:
					Literal literal = (Literal) tp.getObject();
					tp.setKiwiObject( tripleStore.createLiteral( literal.getLiteral() ) );
					break;
				case VARIABLE:
				case CONSTRUCTION_VARIABLE:
					Variable variable = (Variable) tp.getObject();
					if (! rule.getBody().getVariables().contains(variable))
						rule.addConstructionVariable(variable);
					break;
				default:
					throw new ReasoningException("Unsupported term type in head triple object position: "+tp.getObject().getTermType());				
			}
		}
	}
	
	private static String toHQL(VariablePosition varpos) {
		switch (varpos.getTriplePatternPosition()) {
		case SUBJECT:
			return TRIPLE_PREFIX+varpos.getAtomNumber()+".subject ";
		case PROPERTY:
			return TRIPLE_PREFIX+varpos.getAtomNumber()+".property ";
		case OBJECT:
			return TRIPLE_PREFIX+varpos.getAtomNumber()+".object ";
			default:
				throw new IllegalArgumentException("Triple pattern position "+varpos.getTriplePatternPosition()+" is not supported.");
		}
	}
	
	/** Constructs an HQL where clause.
	 * 
	 * @param instantiatingPosition if greater than 1 then instead of matching a triple pattern at the position, this function adds 
	 * a set of triple ids matching expression to be used for rule instantiation. The HQL parameter name is tripleIds.
	 * 
	 * If compiling a normal rule (that isn't supposed to be instantiated) then instantiatingPosition must be 0 or less.
	 * 
	 * @return
	 */
	private static String constructWhereClause(VariablePositions variablePositions, Conjunction conjunction, int instantiatingPosition, int round, CompiledRule crule) {
		String newTriplesMatchingRule = "select t.id from KiWiTriple t, RuleBody rb "+
		"WHERE t.reasoningRound = "+round+" AND rb.ruleName = :ruleName AND rb.position = "+ instantiatingPosition +" AND "+
		"(rb.triplePattern.subject.variable = true OR rb.triplePattern.subject.node = t.subject) AND "+
		"(rb.triplePattern.property.variable = true OR rb.triplePattern.property.node = t.property) AND "+
		"(rb.triplePattern.object.variable = true OR rb.triplePattern.object.node = t.object)";
		crule.subquery = newTriplesMatchingRule;
		
		StringBuffer whereClause = new StringBuffer();
		
		for (Variable variable : variablePositions.getVariables()) {
			List<VariablePosition> positions = variablePositions.getVariablePositions(variable);
			if (positions.size() < 2)
				continue;
						
			for (int i = 0; i < positions.size()-1; i++) {
				whereClause.append(AND);

				whereClause.append(toHQL(positions.get(i)));
				whereClause.append(" = ");
				whereClause.append(toHQL(positions.get(i+1)));
			}			
		}
		
		int i = 1;//we count atoms from 1, see also VariablePositions
		for (Formula formula : conjunction.getFormulas()) {
			
			if (i == instantiatingPosition) {
				whereClause.append(AND);
//				whereClause.append(TRIPLE_PREFIX).append(i).append(".id").append(" IN (:tripleIds) ");
//				whereClause.append(TRIPLE_PREFIX).append(i).append(".id").append(" IN (").append(newTriplesMatchingRule).append(") ");
				whereClause.append(TRIPLE_PREFIX).append(i).append(".reasoningRound").append(" = ").append(round);
				//i++;
				//continue;
			}
			
			TriplePattern tp = (TriplePattern) formula;
			
			Term term;

			term = tp.getSubject();
			if (!term.isVariable()) {
				whereClause.append(AND);
				switch (term.getTermType()) {
				case URI:
					whereClause.append(TRIPLE_PREFIX).append(i).append(".subject.uri = '");
					whereClause.append(((Uri)term).getUri()).append("'");
					break;
				case BNODE:
					whereClause.append(TRIPLE_PREFIX).append(i).append(".subject.anonId = '");
					whereClause.append(((BNode)term).getName()).append("'");
					break;
					default:
						throw new IllegalArgumentException("Term of type "+term.getTermType()+" is not allowed in subject position.");
				}
			}
			
			term = tp.getProperty();
			if (!term.isVariable()) {
				whereClause.append(AND);
				switch (term.getTermType()) {
				case URI:
					whereClause.append(TRIPLE_PREFIX).append(i).append(".property.uri = '");
					whereClause.append(((Uri)term).getUri()).append("'");
					break;
				default:
					throw new IllegalArgumentException("Term of type "+term.getTermType()+" is not allowed in property position.");
				}
			}
			
			term = tp.getObject();
			if (!term.isVariable()) {
				whereClause.append(AND);
				switch (term.getTermType()) {
				case URI:
					whereClause.append(TRIPLE_PREFIX).append(i).append(".object.uri = '");
					whereClause.append(((Uri)term).getUri()).append("'");
					break;
				case BNODE:
					whereClause.append(TRIPLE_PREFIX).append(i).append(".object.anonId = '");
					whereClause.append(((BNode)term).getName()).append("'");
					break;
				case LITERAL:
				case TYPED_LITERAL:
					whereClause.append(TRIPLE_PREFIX).append(i).append(".object.content = '");
					whereClause.append(((Literal)term).getLiteral()).append("'");
					break;
				default:
					throw new IllegalArgumentException("Term of type "+term.getTermType()+" is not allowed in object position.");
				}
			}
			
			whereClause.append(AND).append(TRIPLE_PREFIX).append(i).append(".deleted = false ");
			
			i++;
		}// for each formula in conjunction
		
		whereClause = whereClause.replace(0, AND.length(), ""); //remove the leading AND

		return whereClause.toString();
	}
	
	//TODO typed literals
	//TODO rules with no variables shared between body and head?
	//TODO rules without variables?
	/**
	 * We allow b-nodes in subject and object because of instantiated rules.
	 * 
	 */
	private static CompiledRule compileRuleBody(Rule rule, int instantiatingPosition, int round) {
		Formula formula = rule.getBody().getFormula();
		if (!formula.isConjunction())
			throw new IllegalArgumentException("Rule "+rule.getName()+" uses not implemented features: "+formula.getFormulaType());
		
		Conjunction conjunction;
		
		conjunction = (Conjunction) formula;
		
		for (Formula f : conjunction.getFormulas()) 
			if (!f.isAtomic())
				throw new IllegalArgumentException("Rule "+rule.getName()+" uses not implemented features: "+f.getFormulaType());
		
		
		CompiledRule crule;
		if (instantiatingPosition < 1)
			crule = new CompiledRule(rule);
		else
			crule = new CompiledRuleInstance(rule);
		
		VariablePositions variablePositions = getVariablePositions(rule);
		//log.debug("VariablePositions for "+rule.getName()+": "+variablePositions.toString());
		
		String whereClause = constructWhereClause(variablePositions, conjunction, instantiatingPosition, round, crule);
		
		Set<Variable> bodyVariables = rule.getBody().getVariables();
		Set<Variable> headVariables = rule.getHead().getVariables();
		
		/** Variables to be selected in the query are those that are both in the rule body and in the rule head.  */
		List<Variable> selectVariables = new ArrayList<Variable>();
		for (Variable var : bodyVariables)
			if (headVariables.contains(var))
				selectVariables.add(var);
				
		//if (selectVariables.size() == 0)
		//	throw new ReasoningException("Rules have to contain at least one variable shared between the rule body and the rule head.");
		
		if (selectVariables.size() == 0) {
			// there is no variable that is shared between the rule body and the rule head
			// therefore we'll select all body variables
			
			crule.setHasNoSharedVariables(true);
			
			selectVariables.addAll(bodyVariables);
		}
		
		StringBuffer selectClause = new StringBuffer(); String COMMA = ","; 
		for (Variable var : selectVariables) {
			VariablePosition pos = variablePositions.getVariablePositions(var).get(0);
			selectClause.append(COMMA).append(toHQL(pos)); //TODO: verify
			crule.addSelectVariable(var);
		}
		
		//add triple id selection (so that we can compute ids for new resources)	
		for (int i = 1; i <= conjunction.getFormulas().size(); i++) 
			selectClause.append(COMMA).append(TRIPLE_PREFIX).append(i).append(".id");
		
		//remove the leading comma
		selectClause = selectClause.replace(0, COMMA.length(), "");
		//This means that the selectClause contains variableMap.size variable selections + body.getNumConjuncts triple id selections
		
		StringBuffer fromClause = new StringBuffer();
		for (int i=1; i <= conjunction.getFormulas().size(); i++) 
			fromClause.append(COMMA).append("KiWiTriple ").append(TRIPLE_PREFIX).append(i);
		
		fromClause = fromClause.replace(0, COMMA.length(), "");
		
		crule.setQuery("select "+selectClause.toString()+" from "+fromClause.toString()+" where "+whereClause.toString());
				
		return crule;
	}
	
	public CompiledRule compileRule(Rule rule) {
		return compileRule(rule, -1, -1);
	}	
	
	/** Compiles rules into HQL queries and prepares creation of new triples according to rule heads.
	 * 
	 * @param rule The rule to be compiled.
	 * @param instantiatingPosition 0 or lower for normal rules and the atom position (greater than or equal to 1) at which 
	 * the rule will be instantiated - i.e. instead of matching the atom(triple pattern), triple ids will be matched (of triples 
	 * by which the rule will be instantiated).
	 * @return the compiled rule.
	 */
	public CompiledRule compileRule(Rule rule, int instantiatingPosition, int round) {		
		CompiledRule compiledRule;
		
		try {
			if (!rule.getBody().isGround())
				compiledRule = compileRuleBody(rule, instantiatingPosition, round);
			else
				compiledRule = new CompiledRule(rule);
			compileRuleHead(compiledRule);
		} catch(ReasoningException ex) {
			log.error("Could not compile rule \""+rule.getName()+"\". Error was: #0", ex, ex.getMessage());
			return null;		
		}

		return compiledRule;
	}
	
	public ArrayList<CompiledRule> compileRules(List<Rule> parsedRules) {
		ArrayList<CompiledRule> compiledRules = new ArrayList<CompiledRule>();
		
		//log.info("Compiling rules.");
		Rule rule;
		for (int i=0; i < parsedRules.size(); i++) {
			rule = parsedRules.get(i);
			//log.info("Compiling rule "+rule.getRuleString());
				
			CompiledRule crule = compileRule(rule);
			
			if (crule != null)
				compiledRules.add(crule);
			else
				continue;
			
			//log.info("Created query #0.", crule.query);
		}
		//log.info("Rules compiled.");
		
		return compiledRules;
	}
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws UnsupportedEncodingException, ParseException {
		SimpleKWRLProgramParser parser;
		
		InputStream is = new ByteArrayInputStream("($1 http://xmlns.com/foaf/0.1/firstName $2) -> (http://dummy.com/person/dummy http://xmlns.com/foaf/0.1/firstName $2)".getBytes("UTF-8"));
		parser = new SimpleKWRLProgramParser(is);
		
		//processTest(parser.rule());
		
		String query = compileRuleBody(parser.Rule(), -1, -1).getQuery();
		
		System.out.println(query);
	}
	
}










