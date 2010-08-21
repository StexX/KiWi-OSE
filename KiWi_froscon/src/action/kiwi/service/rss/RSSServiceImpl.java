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
package kiwi.service.rss;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.config.ConfigurationService;
import kiwi.api.rss.RSSServiceLocal;
import kiwi.api.rss.RSSServiceRemote;
import kiwi.model.activity.Activity;
import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("rssService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class RSSServiceImpl implements RSSServiceLocal, RSSServiceRemote {

	@In
	private EntityManager entityManager;
	
	@In
	private ConfigurationService configurationService;
	
	private int getMaxResults() {
		return configurationService.getConfiguration("rss.maxentries", "20").getIntValue();
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.rss.RSSService#getCommentsFeed(java.lang.String)
	 */
	@Override
	public List<ContentItem> getCommentsFeed(String kiwiid) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.rss.RSSService#getContentActivitiesFeed(java.lang.String)
	 */
	@Override
	public List<Activity> getContentActivitiesFeed(String kiwiid) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.rss.RSSService#getRecentChangesFeed()
	 */
	@Override
	public List<ContentItem> getRecentChangesFeed() {
		Query q = entityManager.createNamedQuery("contentItemService.listByDate");
		q.setMaxResults(getMaxResults());
		q.setHint("org.hibernate.cacheable", true);
		
		return q.getResultList();
	}

	/* (non-Javadoc)
	 * @see kiwi.api.rss.RSSService#getSearchFeed(java.lang.String)
	 */
	@Override
	public List<ContentItem> getSearchFeed(String searchString) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.rss.RSSService#getUserActivitiesFeed(java.lang.String)
	 */
	@Override
	public List<Activity> getUserActivitiesFeed(String login) {
		// TODO Auto-generated method stub
		return null;
	}

}