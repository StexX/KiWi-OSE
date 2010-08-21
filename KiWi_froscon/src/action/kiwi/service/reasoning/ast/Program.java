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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kiwi.service.reasoning.ReasoningException;

public class Program {
	private String name;
	private List<Namespace> namespaces;
	private Map<String, Namespace> prefixToNsMap;
	private Map<String, Namespace> uriToNsMap;
	private Map<String, Rule> ruleNameToRuleMap;
	private List<Rule> rules; 
	
	public Program() {
		namespaces = new ArrayList<Namespace>();
		prefixToNsMap = new HashMap<String, Namespace>();
		uriToNsMap = new HashMap<String, Namespace>();
		ruleNameToRuleMap = new HashMap<String, Rule>();
		rules = new ArrayList<Rule>();
	}
	
	public List<Namespace> getNamespaces() {
		return namespaces;
	}
	
	public void addNamespace(Namespace ns) {
		namespaces.add(ns);
		prefixToNsMap.put(ns.getPrefix(), ns);
		uriToNsMap.put(ns.getUri(), ns);
	}
	
	public void addNamespace(String prefix, String uri) {
		addNamespace(new Namespace(prefix, uri));
	}
	
	public Namespace getNamespaceForPrefix(String prefix) {
		return prefixToNsMap.get(prefix);
	}
	
	public Namespace getNamespaceForUri(String uri) {
		return uriToNsMap.get(uri);
	}
	
	public void addRule(Rule rule) {
		if (ruleNameToRuleMap.containsKey(rule.getName())) {
			throw new ReasoningException("Rule names must be unique: "+rule.getName());
		}
		
		rules.add(rule);
		ruleNameToRuleMap.put(rule.getName(), rule);
	}
	
	public Rule getRule(String ruleName) {
		return ruleNameToRuleMap.get(ruleName);
	}
	
	public boolean containsRule(String ruleName) {
		return ruleNameToRuleMap.containsKey(ruleName);
	}
	
	public boolean contains(Rule rule) {
		return rules.contains(rule);
	}
	
	public List<Rule> getRules() {
		return rules;
	}
	
	public String getName() {
		if (name == null)
			throw new ReasoningException("Program name has to be set first!");
		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		for (Namespace ns : namespaces) {
			buf.append(ns.toString()).append('\n');
		}
		
		buf.append('\n');
		
		for (Rule rule : rules) {
			buf.append(rule.toString()).append('\n');
		}
		
		return buf.toString();
	}
}



