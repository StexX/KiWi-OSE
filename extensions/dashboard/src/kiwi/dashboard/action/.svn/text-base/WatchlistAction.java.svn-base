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

import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.log.Log;

@Name("kiwi.dashboard.watchlistAction")
@Scope(ScopeType.CONVERSATION)
public class WatchlistAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	private User currentUser;

//	@In
//	private EntityManager entityManager;
	
	@DataModel
	private List<ContentItem> watchlist;
	
	/**
	 * Inject a Seam logger for logging purposes inside this component.
	 */
	@Logger
	private static Log log;
	
	/**
	 * Tells if the current user has the given ContentItem on his/her watchlist.
	 * @param ci The contentitem to look for.
	 * @return true if ci is watched
	 */
	public boolean isWatched(ContentItem ci){
		// currentUser = entityManager.merge(currentUser); // not needed, fetched eagerly
		boolean res = currentUser.getWatchedContent().contains(ci);
		return res;
	}
	
	/**
	 * set the watched status between currentuser and ci to watch.
	 * @param ci the contentitem to watch or unwatch.
	 * @param watch true turns on watching, false turnes it off.
	 * @return
	 */
	public void setWatch(ContentItem ci, boolean watch){
		log.info("set watch for #0 to #1", ci.getTitle(), watch);
		// currentUser = entityManager.merge(currentUser); // not needed, fetched eagerly
		if(watch){
			currentUser.getWatchedContent().add(ci);
		}else {
			currentUser.getWatchedContent().remove(ci);
		}
	}

	/**
	 * returns the watchlist of currentuser.
	 * @return
	 */
	@Factory(value="watchlist")
	public void initWatchlist(){
		watchlist = new LinkedList<ContentItem>();
		// currentUser = entityManager.merge(currentUser); // not needed, fetched eagerly
		watchlist.addAll(currentUser.getWatchedContent());
		log.info("initWatchList: return #0 elements", watchlist.size());
	}
	
}
