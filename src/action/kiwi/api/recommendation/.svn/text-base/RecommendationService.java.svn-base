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

package kiwi.api.recommendation;

import java.util.List;
import java.util.Map;

import kiwi.api.search.SemanticIndexingService;
import kiwi.model.content.ContentItem;
import kiwi.model.recommendation.ContactRecommendation;
import kiwi.model.recommendation.MySimilarRecommendation;
import kiwi.model.recommendation.SocialCapitalRecommendation;
import kiwi.model.user.Group;
import kiwi.model.user.User;


/**
 * @author Peter Dolog, Fred Durao
 *
 */
public interface RecommendationService {

	/**
	 * Generate personalized recommendations to a give user taking into account the similarity of his/her tags and the tags from existing content items in the system.
	 * @param currentUser
	 * @return
	 */
	public List<ContentItem> getPersonalRecommendations(User currentUser);
	
	/**
	 * Generate related recommendation to the currently viewed page taking into account the similarity of tags. 
	 * @param current
	 * @param currentUser
	 * @return
	 */
	public List<ContentItem> getRecommendations(ContentItem current, User currentUser);
	
	/**
	 * Generate personalized recommendation based on four distinct factors: cosine similarity, tag representativeness, tag popularity and affinity between user and tags.
	 * @param current
	 * @param currentUser
	 * @return
	 */
	public List<ContentItem> getMultiFactorRecommendations(ContentItem current, User currentUser);
	
	/**
	 * Generate tag-based recommendations grouped by the tags of the currently viewed page.
	 * 
	 * @param currentItem
	 * @param currentUser
	 * @return
	 */
	public List<Map.Entry<ContentItem,List<ContentItem>>> getTagGroupedRecommendations(ContentItem currentItem, User currentUser);
	
	/**
	 * Generate related recommendation for a given group considering the tags assigned to it.
	 * @param currentUser
	 * @return
	 */
	public List<Map.Entry<Group,List<ContentItem>>> getGroupRecommendations(User currentUser) ;
	
	/**
	 * Group and recommend users which share similar tags in different content items.
	 * @param currentItem
	 * @param currentUser
	 * @return
	 */
	public List<User> getUsersByGroupTags(ContentItem currentItem, User currentUser);
	
	/**
	 * List users which have assigned tags to a given content item.
	 * @param contentItem
	 * @param currentUser
	 * @return
	 */
	public List<User> getUsersByContentItem(ContentItem contentItem, User currentUser);
	
	/**
	 * Return the list of all recommendable users. Might be slow when many users exist in the system.
	 */
	public List<User> getRecommendableUsers(User currentUser);
	
	/**
	 * Allow users to determine which items are similar based on their own judgment.
	 * @param currentUser
	 * @param recommendedItem
	 * @return
	 */
	public MySimilarRecommendation createMySimilarRecommendation(User currentUser, ContentItem recommendedItem);
	
	/**
	 * Loads recommendation defined as similar already exist in the system.
	 * @param currentUser
	 * @return
	 */
	public List<MySimilarRecommendation> getMySimilarRecommendations(User currentUser);
	
	/**
	 * Check whether a recommendation defined as similar already exist in the system.
	 * @param currentUser
	 * @param recommendedItem
	 * @return
	 */
	public boolean existMySimilarRecommendation(User currentUser, ContentItem recommendedItem);
	
	/**
	 *  Remove recommendations defined as similar by users.
	 * @param currentUser
	 * @param mySimilarRecommendation
	 */
	public void removeMySimilarRecommendation(User currentUser, MySimilarRecommendation mySimilarRecommendation);	

	//public List<ContentItem> getTaggedContentItemByUser(User currentUser);

	/**
	 * Allow users to remove the contact recommendations.
	 * It remove contact recommendations.
	 * @param contactRecommendation
	 * @param currentUser 
	 */
	public void removeContactRecommendation(User currentUser, ContactRecommendation contactRecommendation);
	
	/**
	 * Load the content items recommended by a friend in the contact list.
	 * @param currentUser
	 * @return
	 */
	public List<ContactRecommendation> getContactRecommendations(User currentUser);
	
	/**
	 * Allows users to recommend items to friends in their contact list.
	 * @param currentUser
	 * @param recommendedItem
	 * @param receiverLogin
	 */
	public void recommendItem(User currentUser, ContentItem recommendedItem, String receiverLogin);
	
	/**
	 * Allows users to recommend items to his groups.
	 * @param currentUser
	 * @param recommendedItem
	 * @param groupName
	 */
	public void recommendItemToGroup(User currentUser, ContentItem recommendedItem, String groupName);	
	/**
	 * Calculates a list of recommendations using the semantic vectors index created in SemanticIndexingService.
	 * 
	 * @see SemanticIndexingService
	 * @param current the content item for which to calculate recommendations
	 * @return a list of content items, ordered by relevancy (most significant first)
	 */
	public List<ContentItem> getSemanticVectorRecommendations(ContentItem current);
	
	/**
	 * Improve the tag-based recommendations with the collaborative work behind of it.
	 * 
	 * @param currentItem
	 * @param currentUser
	 * @param topic
	 * @return
	 */
	public List<SocialCapitalRecommendation> getSocialCapitalizedRecommendations(ContentItem currentItem, User currentUser);
	
	
	/**
	 * Recommendations of content items which are tagged by a specific tag
	 * @param tagLabel
	 * @param currentUser
	 * @return
	 */
	public List<ContentItem> getRecommendationsByTag(String tagLabel, User currentUser);
	
	/**
	 *  Recommendations of users which are tagged by a specific tag
	 *  
	 * @param currentUser
	 * @return
	 */
	public List<User> getContactListRecommendations(User currentUser);
	
	/**
	 * @param user
	 * @return
	 */
	public List<User> getFOAFRecommendations(User user);
	
	/**Recommendations generated by rules defined in the user model.
	 * @param user
	 * @return
	 */
	public List<ContentItem> getRuleBasedRecommendations(User user);

}
