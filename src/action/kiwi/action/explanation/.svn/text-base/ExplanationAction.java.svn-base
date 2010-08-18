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
package kiwi.action.explanation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kiwi.api.explanation.ExplanationService;
import kiwi.service.explanation.Story;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.jboss.seam.log.Log;

@Name("explanationAction")
@Scope(ScopeType.SESSION)
public class ExplanationAction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Logger
	private static Log log;
	@In private ExplanationService explanationService;
	
	@In(required=false) @Out(required=false)
	Long tripleIdToExplain = 0l;
		
	List<Long> idsToExplain = new ArrayList<Long>();
	
	@Out(value="explanationAction.stories")
	List<Story> stories = new ArrayList<Story>();
	
	public ExplanationAction() {
		log.debug("Creating new EXPLANATION ACTION. Stories = #0. id to explain = #1", stories.size(), tripleIdToExplain);
	}

	public Long getTripleIdToExplain() {
		log.info("Returning triple id to explain #0",tripleIdToExplain);
		if (tripleIdToExplain == null)
			return tripleIdToExplain = 0l;
		
		return tripleIdToExplain;
	}

	public void setTripleIdToExplain(Long tripleIdToExplain) {
		log.info("Setting triple id to explain to #0",tripleIdToExplain);
		if (tripleIdToExplain == null) {
			this.tripleIdToExplain = 0l;
			return;
		}
		this.tripleIdToExplain = tripleIdToExplain;
		idsToExplain.clear();
		stories.clear();
		addTripleId(tripleIdToExplain);
	}	
	
	public String explainTriple(Long id) {
		setTripleIdToExplain(id);
		
		log.info("explainTriple() is about to return with id #0",id);
		return "explain";
	}
	
	public synchronized List<Story> getStories() {
		log.info("Returing a textual explanation with #0 stories."+this, stories.size());
		return stories;
	}
	
	@WebRemote
	public synchronized void addTripleId(Long id) {
		if (!idsToExplain.contains(id)) {
			log.debug("Adding id #0 Now there are #1 stories. "+this, id, stories.size());
			idsToExplain.add(id);
			List<Story> explanation = explanationService.explainTripleTextually(id, true);
			stories.addAll(explanation);
		}else {
			log.debug("Not adding id #0, it was already there.",id);
		}
		log.debug("Done adding. There are #0 stories in total."+this, stories.size());
	}
}
