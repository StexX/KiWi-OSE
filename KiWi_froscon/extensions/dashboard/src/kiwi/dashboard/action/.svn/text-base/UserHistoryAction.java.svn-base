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
package kiwi.dashboard.action;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kiwi.api.history.HistoryService;
import kiwi.model.activity.AddTagActivity;
import kiwi.model.activity.CommentActivity;
import kiwi.model.activity.EditActivity;
import kiwi.model.activity.SearchActivity;
import kiwi.model.activity.VisitActivity;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * The user history action offers backing functionality for the history widget. It provides
 * methods for listing last page visits, last page edits, last taggings, last comments, and
 * last searches.
 * 
 * @author Sebastian Schaffert
 *
 */
/**
 * @author admin
 *
 */
@Name("kiwi.dashboard.userHistoryAction")
@Scope(ScopeType.PAGE)
public class UserHistoryAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create=true)
	private HistoryService historyService;

	@In
	private User currentUser;
	
	private Queue<SearchActivity> lastSearches;
	
	private Queue<String> unique = new LinkedList<String>();
	
	public Queue<SearchActivity> getLastSearches() {
		if (lastSearches == null) {
			lastSearches = new LinkedList<SearchActivity>();
		}
		List<SearchActivity> seList = listLastDistinctSearches();
		for (SearchActivity searchActivity : seList) {
				if (!unique.contains(searchActivity.getSearchString()) && !searchActivity.getSearchString().equals("")) {
					unique.add(searchActivity.getSearchString());
					lastSearches.add(searchActivity);
					
				}
		}
		return lastSearches;
	}
	
	/**
	 * Return the last visits
	 * @return
	 */
	public List<VisitActivity> listLastVisits() {
		return historyService.listLastVisitsByUser(currentUser);
	}
	
	/**
	 * Return the last edits
	 * @return
	 */
	public List<EditActivity> listLastEdits() {	
		return historyService.listLastEditsByUser(currentUser);
	}
	
	/**
	 * Return the last comments
	 * @return
	 */
	public List<CommentActivity> listLastComments() {
		return historyService.listLastCommentsByUser(currentUser);		
	}
	
	/**
	 * Return the last searches
	 * @return
	 */
	public List<SearchActivity> listLastSearches() {
		return historyService.listLastSearchesByUser(currentUser);
	}
	
	/**
	 * Return the last searches
	 * @return
	 */
	public List<SearchActivity> listLastDistinctSearches() {
		return historyService.listLastDistinctSearchesByUser(currentUser);
	}	
	
	/**
	 * Return the last tags
	 * @return
	 */
	public List<AddTagActivity> listLastTags() {
		return historyService.listLastTagsByUser(currentUser);
	}

	/**
	 * @param s
	 * @param max
	 * @return
	 */
	public String ellipse(String s, int max) {
		if(s.length() > max) {
			return s.substring(0, max)+"...";
		} else {
			return s;
		}
	}
	
}
