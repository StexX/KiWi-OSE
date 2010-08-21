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

package kiwi.wiki.action.ie;

import java.io.Serializable;
import java.util.Collection;

import kiwi.api.informationextraction.InformationExtractionService;
import kiwi.model.content.ContentItem;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * 
 * Manages a list of fragment and content suggestions for the current content item
 * 
 * @author Marek Schmidt
 *
 */
@Name("ie.suggestions")
@Scope(ScopeType.CONVERSATION)
public class SuggestionsAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create=true)
	ContentItem currentContentItem;
	
	@In(create=true)
	private User currentUser;
	
	@Logger
	private Log log;
	
	public Collection<Suggestion> getSuggestions () {
		InformationExtractionService ie = (InformationExtractionService)Component.getInstance("kiwi.informationextraction.informationExtractionService");	
		return ie.getSuggestionsByContentItem(currentContentItem);
	}
	
	public void init() {
		InformationExtractionService ie = (InformationExtractionService)Component.getInstance("kiwi.informationextraction.informationExtractionService");	
		//ie.initAndClassifyInstances(currentContentItem);
		ie.extractInformation(currentContentItem);
	}
	
	public void compute() {
		InformationExtractionService ie = (InformationExtractionService)Component.getInstance("kiwi.informationextraction.informationExtractionService");	
		ie.trainAndSuggestForItem(currentContentItem);
	}
	
	public void acceptSuggestion(Suggestion s) {
		InformationExtractionService ie = (InformationExtractionService)Component.getInstance("kiwi.informationextraction.informationExtractionService");
		ie.acceptSuggestion(s, currentUser);
		ie.realizeSuggestion(s, currentUser);
	}
	
	public void rejectSuggestion(Suggestion s) {
		InformationExtractionService ie = (InformationExtractionService)Component.getInstance("kiwi.informationextraction.informationExtractionService");
		ie.rejectSuggestion(s, currentUser);
	}
}
