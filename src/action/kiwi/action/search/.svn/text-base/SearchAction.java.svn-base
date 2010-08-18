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
 * Szaby Gruenwald
 * 
 */

package kiwi.action.search;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * @author Sebastian Schaffert
 *
 */
@Name("searchAction")
@Scope(ScopeType.CONVERSATION)
public class SearchAction implements Serializable {

	private static final long serialVersionUID = 2812172771232630433L;

	@Logger
	private static Log log;
	
    @In FacesMessages facesMessages;
    
    @In(value="kiwiSearchEngine",create=true)
    private KiWiSearchEngine searchEngine;
    
    public void runSearch(){
    	searchEngine.runSearch();
    }

    public List<String> autocomplete(Object param) {
    	return searchEngine.autocomplete(param);
    }
    
	/**
	 * Initialise the SearchAction and start a conversation. 
	 * Triggered as page action when search.xhtml is displayed. 
	 */
	@Begin(join=true)
	public void begin() {
	}
	
	/**
	 * End the SearchAction and terminate the conversation.
	 */
	@End
	public void end() {
		
	}
	
	public String renderedSearchPageParts(){
		return "searchHistory,searchField,searchresults,nrOfResults,articlemeta,resultNavigation";//,search-keyword";
	}

    public String getSearchView() {
    	return searchEngine.getSearchView();
	}

	public KiWiSearchEngine getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(KiWiSearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}

	public String getSearchQuery() {
		return searchEngine.getSearchQuery();
	}

	public void setSearchQuery(String searchQuery) {
		searchEngine.setSearchQuery(searchQuery);
	}
	
	
	
}
