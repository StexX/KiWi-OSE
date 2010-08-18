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
package kiwi.service.reasoning.ast;

import java.util.HashSet;
import java.util.Set;

/** Abstract syntax tree top node of a rule. 
 * 
 * @author Jakub Kotowski
 *
 */
public class Rule {
	public static String RULE_NAME_PREFIX = "RULE";
	private static String FRESH_VARIABLE_PREFIX = "$VAR";

	Body body;
	Head head;
	String name;

	public Rule(Body body, Head head) {
		this.body = body;
		this.head = head;
	}
	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Variable> getVariables() {
		Set<Variable> variables = new HashSet<Variable>();
		variables.addAll(getBody().getVariables());
		variables.addAll(getHead().getVariables());
		return variables;
	}

	public Variable createFreshVariable() {
		Set<Variable> variables = getVariables();
		
		int i = 1;		
		Variable var = new Variable(FRESH_VARIABLE_PREFIX + i);
		
		while (variables.contains(var)) 
			var = new Variable(FRESH_VARIABLE_PREFIX + ++i); 
		
		return var;
	}	
	
	public String toString() {
		return name+": "+body.toString()+" -> "+head.toString();
	}
	
	public boolean isGround() {
		return body.isGround() && head.isGround();
	}
	
	public boolean equals(Object object) {
		if (!(object instanceof Rule))
			return false;
		
		Rule rule = (Rule) object;
		return rule.toString().equals(this.toString());
	}
	
	public int hashCode() {
		return this.toString().hashCode();
	}
}
