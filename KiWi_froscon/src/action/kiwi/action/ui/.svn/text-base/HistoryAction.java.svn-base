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
package kiwi.action.ui;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import kiwi.api.history.HistoryService;
import kiwi.model.activity.VisitActivity;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * A component to keep track of the page history for displaying in the breadcrumps.
 * Listens to the "contentItemChanged" event raised by CurrentContentItemFactory.
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.ui.historyAction")
@Scope(ScopeType.SESSION)
@AutoCreate
public class HistoryAction implements Serializable {
	
	@In(create=true)
	private HistoryService historyService;
	
	@In(create=true)
	private User currentUser;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int MAX_HISTORY_SIZE = 5;
	
	private static int MAX_TITLE_LENGTH = 15;
	
	@Logger
	private Log log;
	
	private Queue<ContentItem> lastItems;
	
	@Observer("contentItemChanged")
	public void updateHistory(ContentItem newItem) {
		log.info("content item changed; updating history");
		if(lastItems == null) {
			lastItems = new LinkedList<ContentItem>();
		}
		if(lastItems.contains(newItem)) {
			lastItems.remove(newItem);
		}
		while(lastItems.size() >= MAX_HISTORY_SIZE) {
			lastItems.poll();
		}
		lastItems.add(newItem);
	}
	



	public Queue<ContentItem> getLastItems() {
		return lastItems;
	}
	
	
	
	/**
	 * Return last visits form last login
	 * @return
	 */
	@Observer(org.jboss.seam.security.Identity.EVENT_LOGIN_SUCCESSFUL)
	public Queue<ContentItem> getLastItemsFromLastLogin() {
		log.info("updating history with last items from last login");
		if(lastItems == null) {
			lastItems = new LinkedList<ContentItem>();
		}else{
			lastItems.clear();
		}		
		if (currentUser.getUserPreference()!=null && currentUser.getUserPreference().isShowMyLastVisitedPages()) {
			List<VisitActivity> lastVisits = historyService.listLastDistinctsVisitsByUser(currentUser);
			if(lastVisits == null) {
				lastVisits = new LinkedList<VisitActivity>();
			}	
			uniqueHistory = new HashSet<String>();
			for (VisitActivity visitActivity : lastVisits) {
				if (!uniqueHistory.contains(visitActivity.getContentItem().getTitle())) {
					lastItems.add(visitActivity.getContentItem());
					uniqueHistory.add(visitActivity.getContentItem().getTitle());
				}
				
			}
		}
		return lastItems;
	}
	
	private Set<String> uniqueHistory;
	
	public String ellipseString(String s) {
		if(s.length() > MAX_TITLE_LENGTH) {
			return s.substring(0, MAX_TITLE_LENGTH) + "...";
		} else {
			return s;
		}
	}
}
