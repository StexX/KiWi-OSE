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
import java.util.List;

import kiwi.service.reasoning.ast.Rule;

/** Represents a result of a parsed program comparison using ProgramComparer.
 * 
 * @author Jakub Kotowski
 *
 */
public class ProgramUpdateTask extends ReasoningTask {
	private List<Rule> removedRules = new ArrayList<Rule>();
	private List<Rule> addedRules = new ArrayList<Rule>();
	private String programName;
	
	public ProgramUpdateTask() {}
	
	public ProgramUpdateTask(String programName, List<Rule> addedRules, List<Rule> removedRules) {
		this.programName = programName;
		this.addedRules = addedRules;
		this.removedRules = removedRules;
	}
	
	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public void addRemovedRule(Rule rule) {
		removedRules.add(rule);
	}
	
	public void addAddedRule(Rule rule) {
		addedRules.add(rule);
	}
	
	public List<Rule> getRemovedRules() {
		return removedRules;
	}
	
	public List<Rule> getAddedRules() {
		return addedRules;
	}
	
	public boolean isEmpty() {
		return removedRules.isEmpty() && addedRules.isEmpty();
	}

	@Override
	public ReasoningTaskType getReasoningTaskType() {
		return ReasoningTaskType.PROGRAM_UPDATE;
	}

	@Override
	public String toString() {
		return "Update program "+programName+": remove "+removedRules.size()+" and add "+addedRules.size();
	}

	/** Produces just a shallow copy of the instance, it doesn't clone contained rules.
	 * 
	 */
	@Override
	public ReasoningTask clone() {
		return new ProgramUpdateTask(programName, new ArrayList<Rule>(addedRules), new ArrayList<Rule>(removedRules));
	}
}
