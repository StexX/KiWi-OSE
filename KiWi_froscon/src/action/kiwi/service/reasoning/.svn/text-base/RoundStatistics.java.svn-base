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

/** Holds "statistical" information about an individual iteration of forward-chaining.
 * 
 * @author Jakub Kotowski
 *
 */
public class RoundStatistics extends AbstractStatistics {
	private int roundNumber = 0;
	private int rulesCount = 0;
	private int newTriplesCount = 0;
	private int generatedTriplesCount = 0;
	private int markedTriplesCount = 0;
	
	public RoundStatistics() {		
	}

	public int getRoundNumber() {
		return roundNumber;
	}

	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}

	public int getRulesCount() {
		return rulesCount;
	}

	public void increaseRulesCount() {
		this.rulesCount++;
	}

	public int getNewTriplesCount() {
		return newTriplesCount;
	}

	public void increaseNewTriplesCount() {
		this.newTriplesCount++;
	}

	public void increaseNewTriplesCount(int increment) {
		this.newTriplesCount += increment;
	}

	public int getGeneratedTriplesCount() {
		return generatedTriplesCount;
	}

	public void increaseGeneratedTriplesCount() {
		this.generatedTriplesCount++;
	}

	public void increaseGeneratedTriplesCount(int increment) {
		this.generatedTriplesCount += increment;
	}

	public int getMarkedTriplesCount() {
		return markedTriplesCount;
	}

	public void increaseMarkedTriplesCount() {
		this.markedTriplesCount++;
	}

	public void increaseMarkedTriplesCount(int increment) {
		this.markedTriplesCount += increment;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("Round " + roundNumber + " ran " + getRulesCount()+ " rules"+
				" generated "+ getGeneratedTriplesCount()+" triples "
				+ " out of which " + getNewTriplesCount() + " were new triples");
		s.append(" and marked " + getMarkedTriplesCount() + " triples");
		if (getTime() != null)
			s.append(" in " + getTime() + "ms.");
		return s.toString();
	}
}
