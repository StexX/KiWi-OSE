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
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.model.activity.Activity;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;

/**
 * The StreamOfActivitiesAction is a component for supporting the listing of the stream of 
 * activities for the current user.
 * 
 * At the moment, this component simply lists all activities around content items that are on a 
 * user's watchlist. In a later stage, the stream of activities should consist of more complex
 * queries, allow aggregation, grouping, ...
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.dashboard.streamOfActivitiesAction")
@Scope(ScopeType.PAGE)
public class StreamOfActivitiesAction implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	private User currentUser;

	@In
	protected Map<String, String> messages;
	
	@In
	private EntityManager entityManager;
	
	private List<Activity> activities;
	
	private List<Activity> tweetActivities;
	
	
	/**
	 * It lists activities by users including his friend activities ordered by date
	 * @return
	 */
	public List<Activity> getActivities() {
		if(activities == null) {
			Query q = entityManager.createNamedQuery("activities.listActivitiesByUser");
			q.setMaxResults(20);
			q.setParameter("user", currentUser);
			q.setHint("org.hibernate.cacheable", true);
			activities = q.getResultList();
		}
		
		return activities;
	}
	
	/**
	 * @return
	 */
	public List<Activity> getTweetActivities() {
		if(tweetActivities == null) {
			Query q = entityManager.createNamedQuery("activities.listTweetActivitiesByUser");
			q.setMaxResults(100);
			q.setParameter("user", currentUser);
			q.setHint("org.hibernate.cacheable", true);
			tweetActivities = q.getResultList();
		}
		
		return tweetActivities;
	}
	
	@Observer(value="updateActivities", create=false)
	public void clear() {
		activities = null;
		tweetActivities = null;
	}
	
	public String getMessageIdentifier(Activity activity) {
		if(activity.getMessageIdentifier() != null) {
//			String msgProp = SeamResourceBundle.getBundle().getString(activity.getMessageIdentifier());
			String msgProp = messages.get(activity.getMessageIdentifier());
			return msgProp;
		} else {
			return "";
		}
	}
}
