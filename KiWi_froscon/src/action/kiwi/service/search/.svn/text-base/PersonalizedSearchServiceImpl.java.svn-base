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
package kiwi.service.search;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.PersonalizedSearchService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.model.activity.SearchActivity;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * @author Nilay, Fred Durao
 *
 */
@Stateless
@Name("personalizedSearchService")
public class PersonalizedSearchServiceImpl implements PersonalizedSearchService {

	public static void main(String[] args) {
		long init =System.currentTimeMillis();
		long end = System.currentTimeMillis();
		System.out.println(end-init);
	}

	@Logger
	private static Log log;

	@In(create = true)
	private User currentUser;

	/**
	 * The JPA entity manager used by this KiWi system
	 */
	@In
	private EntityManager entityManager;
    
	/**
	 * @param kiWiSearchResults
	 * @return
	 */
	public KiWiSearchResults runPersonilazedSearch(
			KiWiSearchResults kiWiSearchResults) {
		List<SearchResult> results = kiWiSearchResults.getResults();
		List<String> tagList = getAllTagsByUser(currentUser);
		List<ContentItem> watchlist = new LinkedList<ContentItem>();
		watchlist.addAll(currentUser.getWatchedContent());
//		List<SearchActivity> lastSearches = listLastSearches();
//		List<String> lastSearchList= getLastSearches(lastSearches);
		for (SearchResult searchResult : results) {
			ContentItem contentItem = searchResult.getItem();
			List<String> contentItemTags = getTagLabesByContentItem(contentItem);
			for (String tag : contentItemTags) {
				float tagScore= getTagScore(tag,tagList);
				updateScoreBasedOnUserTags(searchResult, tag, tagList, tagScore);
				updateScoreBasesOnWatchListTag(searchResult, tag, tagScore,watchlist);
			}

		}
		Collections.sort(results);
		kiWiSearchResults.setResults(results);
		return kiWiSearchResults;
	}
	
	/**
	 * It compares the user's tag and the tags of content item which is in
	 * user's watch list and updates the score
	 * 
	 * @param searchResult ,tag
	 * 
	 */
	private void updateScoreBasesOnWatchListTag(SearchResult searchResult, String tag, float tagScore,List<ContentItem> watchlist) {
		for (String userTag : getTagsFromWatchList(watchlist)) {
			if (tag.equals(userTag)) {
				searchResult.setScore(searchResult.getScore()
						+ tagScore);
			}
		}
	}

	/**
	 * It updates the score considering last searches
	 * 
	 * @param searchResult , tag
	 */
	private void updateScoreBasedOnLastSearches(SearchResult searchResult, String tag,List<String> lastSearchList) {
		for (String lastSearch : lastSearchList) {
			if (tag.equals(lastSearch)) {
				searchResult.setScore(searchResult.getScore() + getLastSearchScore(lastSearch,lastSearchList));
			}
		}
		
	}

	/**
	 * It updates the score considering user tags
	 * 
	 * @param searchResult, tag
	 */
	private void updateScoreBasedOnUserTags(SearchResult searchResult,String tag,List<String> tagList, float tagScore) {
		for (String userTag : tagList) {
			if (tag.equals(userTag)) {
				searchResult.setScore(searchResult.getScore()
						+ tagScore);
			}
		}
	}

	/**
	 * Normalizing
	 * 
	 * @param currentTag
	 * @return
	 */
	private float getTagScore(String currentTag,List <String> tagList) {
		float tagScore = 0f;
		int max = 0;
//		List<String> tagList = getAllTagsByUser(currentUser);
		for (String tag : tagList) {
			max = Math.max(max, getTagFrequencyByUser(tag).intValue());
		}
		tagScore = (float) getTagFrequencyByUser(currentTag) / (float) max;
		return tagScore;
	}
	
	/**
	 * Calculating Last Search Score
	 * 
	 * @param currentTag
	 * @return
	 */
	private float getLastSearchScore(String lastSearch, List<String> lastSearchList) {
		float lastSearchScore = 0f;
		float max = 0;
		
		for (String s : lastSearchList) {
			max = Math.max(max, getLastSearchFrequency(s,lastSearchList));
		}
		lastSearchScore = (float) getLastSearchFrequency(lastSearch,lastSearchList) / (float) max;
		return lastSearchScore;
	}

	/**
	 * TagFruquency
	 * 
	 * @param currentTag
	 * @return
	 */
	private Long getTagFrequencyByUser(String currentTag) {
		Long tagFrequency = null;
		String s = "select count(t.taggingResource.title) "
				+ "from kiwi.model.tagging.Tag t where t.taggedBy = :cu  and t.taggingResource.title= :tag"
				+ " and t.taggingResource.deleted = false";

		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("cu", currentUser);
		q.setParameter("tag", currentTag);
		try {
			tagFrequency = (Long) q.getSingleResult();
		} catch (NoResultException ex) {
			log.warn("couldn't find any result");
		}
		return tagFrequency;
	}
	
	/**
	 * Last Search Frequency
	 * 
	 * @param currentTag
	 * @return
	 */
	private float getLastSearchFrequency(String search, List<String> lastSearchList) {
		float lastSearchFrequency = 0f;
		for (String s : lastSearchList) {
			if(s.equals(search))
			{
				lastSearchFrequency++;
			}
		}
		System.out.println(lastSearchFrequency);
		return lastSearchFrequency;
	}

	/**
	 * User's tags
	 * 
	 * @param currentUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<String> getAllTagsByUser(User currentUser) {
		String s = "select distinct t.taggingResource.title "
				+ "from kiwi.model.tagging.Tag t " + "where t.taggedBy = :cu"
				+ " and t.taggingResource.deleted = false";

		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("cu", currentUser);
		return q.getResultList();
	}

	/**
	 * @param currentUser
	 * @return
	 */
	public List<String> getTagsFromWatchList(List<ContentItem> watchlist) {
		List<String> resultList = new LinkedList<String>();

		for (ContentItem contentItem : watchlist) {
			resultList = getTagLabesByContentItem(contentItem);
		}
		return resultList;
	}

	/**
	 * @param contentItem
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<String> getTagLabesByContentItem(ContentItem contentItem) {
		String s = "select tag.taggingResource.title "
				+ " from kiwi.model.content.ContentItem ci, kiwi.model.tagging.Tag tag "
				+ " where tag.taggedResource.id = ci.id"
				+ " and ci.id =:contentItemId and ci.deleted = false";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("contentItemId", contentItem.getId());
		return q.getResultList();
	}

	/**
	 * 
	 * @param
	 * @return List<String>
	 */
	public List<String> getLastSearches(List<SearchActivity> lastSearches) {
		List<String> resultList = new LinkedList<String>();
		for (SearchActivity searchActivity : lastSearches) {
			resultList.add(searchActivity.getSearchString());
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<SearchActivity> listLastSearches() {
		Query q = entityManager
				.createNamedQuery("activities.listLastUserSearches");
		q.setParameter("login", currentUser.getLogin());
		q.setHint("org.hibernate.cacheable", true);
		q.setMaxResults(5);

		return q.getResultList();
	}
}
