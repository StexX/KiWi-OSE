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
package kiwi.service.reasoning;

public abstract class ReasoningTask {
	public enum ReasoningTaskType {ADD_TRIPLES, REMOVE_TRIPLES, PROGRAM_UPDATE, RUN_REASONING, ENABLE_FEATURE, DISABLE_FEATURE}
	
	private int number;
	
	public boolean isAddTriples() {
		return ReasoningTaskType.ADD_TRIPLES.equals(getReasoningTaskType());
	}
	
	public boolean isRemoveTriples() {
		return ReasoningTaskType.REMOVE_TRIPLES.equals(getReasoningTaskType());		
	}
	
	public boolean isProgramUpdate() {
		return ReasoningTaskType.PROGRAM_UPDATE.equals(getReasoningTaskType());		
	}	
	
	public boolean isRunReasoning() {
		return ReasoningTaskType.RUN_REASONING.equals(getReasoningTaskType());		
	}	
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	/** Produces a copy of the reasoning task.
	 * 
	 * It may be just a shallow copy - with new collection classes that contain the actual payload but without copying the payload itself.
	 */
	public abstract ReasoningTask clone();
	/** Returns the type of this reasoning task.
	 */
	public abstract ReasoningTaskType getReasoningTaskType();
	/** Returns a user-friendly description of this task.
	 * 
	 */
	public abstract String toString();
}
