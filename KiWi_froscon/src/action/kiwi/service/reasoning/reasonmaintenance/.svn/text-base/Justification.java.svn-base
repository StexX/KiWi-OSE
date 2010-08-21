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

import java.util.List;

import kiwi.model.kbase.KiWiTriple;

/** Temporary holder of justification data. 
 * 
 * It also builds a unique justification id using triple ids.
 * Fact triple id is examined only upon the request for the unique justification id (toString()).
 * This is to give as much time as possible for the triples to get persisted. 
 * 
 * @author Jakub Kotowski
 *
 */
public class Justification {
	private Long[] inTripleIds;
	private String ruleId;
	private KiWiTriple fact;
	private Long neoId = null;
	
	public Justification(KiWiTriple fact, Long[] inTripleIds, String ruleId) {
		setFact(fact);
		setInTripleIds(inTripleIds);
		this.ruleId = ruleId;
	}
	
	public Justification(KiWiTriple fact, List<Long> inTripleIds, String ruleId) {
		this(fact, inTripleIds.toArray(new Long[inTripleIds.size()]), ruleId);
	}

	private String buildUniqueId() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("IN(");
		for (Long id : getInTripleIds()) 
			buf.append(id).append(',');
		
		buf.deleteCharAt(buf.length()-1).append(')');
		
		buf.append("RULE(").append(getRuleId()).append(')');
		
		buf.append("FACT(").append(getFactId()).append(')');
		
		return buf.toString();		
	}
	
	/**
	 * 
	 * @return Returns a sorted array of kiwi triple ids which are IN. 
	 */
	public Long[] getInTripleIds() {
		return inTripleIds;
	}

	public void setInTripleIds(Long[] inTripleIds) {
		this.inTripleIds = inTripleIds;
	}	
	
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public Long getFactId() {
		return getFact().getId();
	}	
	
	public KiWiTriple getFact() {
		return fact;
	}

	public void setFact(KiWiTriple fact) {
		this.fact = fact;
	}

	/**
	 * @return Returns a unique descriptive id of this justification. 
	 */
	public String toString() {
		return buildUniqueId();
	}

	public Long getNeoId() {
		return neoId;
	}

	public void setNeoId(Long neoId) {
		this.neoId = neoId;
	}	
	
	public KiWiTriple getSupportedFact() {
		return fact;
	}
}
