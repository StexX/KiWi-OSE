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

package kiwi.action.recommendation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.group.GroupService;
import kiwi.api.rating.RatingService;
import kiwi.api.recommendation.RecommendationService;
import kiwi.api.recommendation.evaluation.RecommendationEvaluationService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.recommendation.ContactRecommendation;
import kiwi.model.recommendation.MySimilarRecommendation;
import kiwi.model.recommendation.SocialCapitalRecommendation;
import kiwi.model.user.Group;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.richfaces.event.DropEvent;
import org.richfaces.event.DropListener;

/**
 * A backing action for the recommendation widgets; calls the recommendationService component
 * methods and caches their results in the current conversation
 * 
 * @author Sebastian Schaffert, Fred Durao
 *
 */
@Name("recommendationAction")
@Scope(ScopeType.CONVERSATION)
public class RecommendationAction implements DropListener, Serializable  {

	private static final long serialVersionUID = 1L;

	@In
	private User currentUser;

	@In(create=true)
	private ContentItem currentContentItem;
	
	@In
	private RecommendationService recommendationService;
	
	@In(create=true)
	private GroupService groupService;	
	
	@Out(value="recommendationActionScope.userSimilarItems", required=false)
	private List<MySimilarRecommendation> userSimilarItems = new ArrayList<MySimilarRecommendation>();	
	
	@Out(value="recommendationActionScope.contactRecommendations", required=false)
	private List<ContactRecommendation> contactRecommendations;
	
	@Out(value="recommendationActionScope.recommendableUsersAndGroups", required=false)
	private List<String> recommendableUsersAndGroups;

	private String recommendationTarget;
	
	private String topic;
	
	@In(create=true)
	private KiWiEntityManager kiwiEntityManager;
	
	@In 
    FacesMessages facesMessages;	

	/* (non-Javadoc)
     * @see org.richfaces.event.DropListener#processDrop(org.richfaces.event.DropEvent)
     */
    public void processDrop(DropEvent dropEvent) {
		Object dragValue = dropEvent.getDragValue();
		ContentItem recommendedContentItem = null;
		if((dragValue instanceof ContentItem) || (dragValue instanceof ContactRecommendation)){
			if(dragValue instanceof ContentItem){
				recommendedContentItem = (ContentItem)dragValue;
			} else if(dragValue instanceof ContactRecommendation){
				recommendedContentItem = ((ContactRecommendation)dragValue).getRecommendedItem();			
			}else if(dragValue instanceof SocialCapitalRecommendation){
				recommendedContentItem = ((SocialCapitalRecommendation)dragValue).getRecommendedItem();			
			}	
			if (recommendedContentItem !=null) {				
				recommendationService.createMySimilarRecommendation(currentUser,recommendedContentItem);
//				kiwiEntityManager.flush();	
				userSimilarItems.clear();
				userSimilarItems.addAll(recommendationService.getMySimilarRecommendations(currentUser));
			}
		}
	}
    
    /**It removes similar recommendation from a user
     * @param contentItem
     */
    public void removeSimilarRecommendation(MySimilarRecommendation mySimilarRecommendation) {
    	recommendationService.removeMySimilarRecommendation(currentUser, mySimilarRecommendation);
   		userSimilarItems.remove(mySimilarRecommendation);
    }

    
    /**
     * It loads similar recommendation from a user
     */
    @Factory("recommendationActionScope.userSimilarItems")
	public void loadUserSimilarItems() {
			userSimilarItems = new ArrayList<MySimilarRecommendation>();
			userSimilarItems.clear();
			userSimilarItems.addAll(recommendationService.getMySimilarRecommendations(currentUser));
	}    
	
	/**
	 * the list of personalrecommendations valid for the current content item; 
	 * outjected as recommendationActionScope.recommendations
	 */
	@Out(value="recommendationActionScope.personalRecommendations", required=false)
	private List<ContentItem> personalRecommendations;	
	
	/**
	 * the list of recommendations valid for the current content item; 
	 * outjected as recommendationActionScope.recommendations
	 */
	
	@Out(value="recommendationActionScope.recommendations", required=false)
	private List<ContentItem> recommendations;
	
	/**
	 * the list of recommendations valid for the current content item; 
	 * outjected as recommendationActionScope.multiFactorRecommendations
	 */
	@Out(value="recommendationActionScope.multiFactorRecommendations", required=false)
	private List<ContentItem> multiFactorRecommendations;
	
	/**
	 * 
	 */
	@Out(value="recommendationActionScope.ruleBasedRecommendations", required=false)
	private List<ContentItem> ruleBasedRecommendations;
	
	/**
	 * the list of recommendations for the current content item, 
	 * grouped by tag
	 * outjected as recommendationActionScope.groupedRecommendations
	 */
	@Out(value="recommendationActionScope.groupedRecommendations", required=false)
	private List<Map.Entry<ContentItem,List<ContentItem>>> groupedRecommendations;
	
	/**
	 * the list of user recommendations for a group
	 */
	@Out(value="recommendationActionScope.userRecommendations", required=false)
	private List<User> userRecommendations;	
	
	/**
	 * the list of user recommendations for a content item
	 */
	@Out(value="recommendationActionScope.userContentItemRecommendations", required=false)
	private List<User> userContentItemRecommendations;	
	
	
	/**
	 * the list of recommendations for a group
	 */
	@Out(value="recommendationActionScope.groupRecommendations", required=false)
	private List<Map.Entry<Group,List<ContentItem>>> groupRecommendations;
	
	/**
	 * the list of recommendations based on social capital
	 */
	@Out(value="recommendationActionScope.socialCapitalizedRecommendations", required=false)
	private List<SocialCapitalRecommendation> socialCapitalizedRecommendations;	
	
	/**
	 * the list of contactList recommendations
	 */
	@Out(value="recommendationActionScope.contactListRecommendations", required=false)
	private List<User> contactListRecommendations;
	
	/**
	 * the list of contactList recommendations
	 */
	@Out(value="recommendationActionScope.foafRecommendations", required=false)
	private List<User> foafRecommendations;	

	/**
	 * A factory method for creating the FOAF recommendations;
	 */
	@Factory("recommendationActionScope.foafRecommendations")
	public void getFoafRecommendations() {
		if(foafRecommendations == null) {
			foafRecommendations = recommendationService.getFOAFRecommendations(currentUser);
		}
	}	

	/**
	 * A factory method for creating the recommendations list; only executed when
	 * recommendations for this conversation is still non-null
	 */
	@Factory("recommendationActionScope.recommendations")
	public void calculateRecommendations() {
		if(recommendations == null) {
			recommendations = recommendationService.getRecommendations(currentContentItem, currentUser);
		}
	}
	
	/**
	 * A factory method for getting personal recommendations list; only executed when
	 * recommendations for this conversation is still non-null
	 */
	@Factory("recommendationActionScope.personalRecommendations")
	public void calculatePersonalRecommendations() {
		if(personalRecommendations == null) {
			personalRecommendations = recommendationService.getPersonalRecommendations(currentUser);
		}
	}	
	
	/**
	 * A factory method for getting rule based recommendations list; only executed when
	 * recommendations for this conversation is still non-null
	 */
	@Factory("recommendationActionScope.ruleBasedRecommendations")
	public void calculateRuleBasedRecommendations() {
		if(ruleBasedRecommendations == null) {
			ruleBasedRecommendations = recommendationService.getRuleBasedRecommendations(currentUser);
		}
	}	
	
	/**
	 * A factory method for creating the multi factor recommendations list; only executed when
	 * recommendations for this conversation is still non-null
	 */
	@Factory("recommendationActionScope.multiFactorRecommendations")
	public void calculateMultiFactorRecommendations() {
		if(multiFactorRecommendations == null) {
			multiFactorRecommendations = recommendationService.getMultiFactorRecommendations(currentContentItem, currentUser);
		}
	}
	
	/**
	 * A factory method for calculating the grouped recommendations list;
	 * only executed when groupedRecommendations is still non-null
	 */
	@Factory("recommendationActionScope.groupedRecommendations")
	public void calculateGroupedRecommendations() {
		if(groupedRecommendations == null) {
			groupedRecommendations = recommendationService.getTagGroupedRecommendations(currentContentItem, currentUser);
		}
	}
	
	/**
	 * A factory method for calculating the group recommendations list;
	 * only executed when groupedRecommendations is still non-null
	 */
	@Factory("recommendationActionScope.groupRecommendations")
	public void calculateGroupRecommendations() {
		if(groupRecommendations == null) {
			groupRecommendations = recommendationService.getGroupRecommendations(currentUser);
		}
	}
	
	/**
	 * A factory method for calculating the user recommendations list;
	 * only executed when calculateUserRecommendations is still non-null
	 */
	@Factory("recommendationActionScope.userRecommendations")
	public void calculateUserRecommendations() {
		if(userRecommendations == null) {
			userRecommendations = recommendationService.getUsersByGroupTags(currentContentItem, currentUser);
		}
	}	
	
	
	/**
	 * A factory method for calculating the user recommendations list;
	 * only executed when calculateUserContentItemRecommendations is still non-null
	 */
	@Factory("recommendationActionScope.userContentItemRecommendations")
	public void calculateUserContentItemRecommendations() {
		if(userContentItemRecommendations == null) {
			userContentItemRecommendations = recommendationService.getUsersByContentItem(currentContentItem, currentUser);
		}
	}
	
	/**
	 * A factory method for calculating the user recommendations list;
	 * only executed when calculateUserContentItemRecommendations is still non-null
	 */
	@Factory("recommendationActionScope.contactRecommendations")
	public void getContactRecommendations() {
		if(contactRecommendations == null ||contactRecommendations.isEmpty()) {
			contactRecommendations = new ArrayList<ContactRecommendation>();
			contactRecommendations.addAll(recommendationService.getContactRecommendations(currentUser));
		}		
	}
	
	@Factory("recommendationActionScope.recommendableUsersAndGroups")
	public void getRecommendableUsersAndGroups() {
		
		if(recommendableUsersAndGroups == null) {
			List<User> users = recommendationService.getRecommendableUsers(currentUser);
			recommendableUsersAndGroups = new LinkedList<String>();
			for(User user : users) {
				recommendableUsersAndGroups.add("User: "+user.getLogin());
			}
			
			List<Group> groups = groupService.getAllMyGroups(currentUser);
			for(Group group : groups) {
				recommendableUsersAndGroups.add("Group: "+group.getName());
			}
		}
	}	

	/**
	 * A method for social recommendations 
	 * @param recommendedItem
	 */
	public void recommendItem(ContentItem recommendedItem){
		if(recommendedItem != null && recommendationTarget!=null && currentUser.getId()!=1 && (!currentUser.getLogin().equals(recommendationTarget))) {
			if (recommendationTarget.startsWith("User:")) {
				recommendationService.recommendItem(currentUser, recommendedItem, recommendationTarget.replace("User:","").trim());
			}else if (recommendationTarget.startsWith("Group:")) {
				recommendationService.recommendItemToGroup(currentUser, recommendedItem, recommendationTarget.replace("Group:","").trim());	
			}
			facesMessages.add("Item recommended.");
		}
	}
	
    /**It removes similar recommendation from a user
     * @param contentItem
     */
    public void removeContactRecommendation(ContactRecommendation contactRecommendation) {
    	if(contactRecommendation != null) {    	
    		recommendationService.removeContactRecommendation(currentUser, contactRecommendation);
    		contactRecommendations.remove(contactRecommendation);
    	}
    	
    }	
    
   
	@In(create=true)
    private UserService userService;
   
	@In(create=true)
	private ContentItemService contentItemService;
   
	@In(create=true)
	private RatingService ratingService;

    public void rate(String userLogin,int rating,String title) {
		User user = userService.getUserByLogin(userLogin);
		ContentItem item = contentItemService.getContentItemByTitle(title);
		
		if(rating == 0){
			ratingService.cancelRating(item, user);
		} else {
			if(rating > 5){
				rating = 5;
				ratingService.setRating(item, user, rating);
			}else{
				ratingService.setRating(item, user, rating);
			}
		} 
	
    }
    
    
	@In(create=true)
	private TripleStore tripleStore;
	
	@In(create=true)
	private TaggingService taggingService;	
	
	public void addTag(String label,String title,User user) {
		
		// TODO: query by uri instead of by title
		ContentItem taggingItem = contentItemService.getContentItemByTitle(label);

		if(taggingItem == null) {
			// create new Content Item of type "tag" if the tag does not yet exist
			taggingItem = contentItemService.createContentItem("content/"+label.toLowerCase().replace(" ","_")+"/"+UUID.randomUUID().toString());
			taggingItem.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag"));
			taggingItem.setTitle(label);
			kiwiEntityManager.persist(taggingItem);
			//log.info("created new content item for non-existant tag");
		}

		ContentItem item = contentItemService.getContentItemByTitle(title);
		
		taggingService.createTagging(label, item, taggingItem, user);

		Events.instance().raiseEvent("tagUpdate");
	}    

    public String getRecommendationTarget() {
		return recommendationTarget;
	}

	public void setRecommendationTarget(String recommendationTarget) {
		this.recommendationTarget = recommendationTarget;
	}
	
	
	/**
	 * A factory method for calculating socialCapitalizedRecommendations;
	 * only executed when calculateUserRecommendations is still non-null
	 */
	@Factory("recommendationActionScope.socialCapitalizedRecommendations")
	public void calculateSocialCapitalizedRecommendations() {
		if(socialCapitalizedRecommendations == null) {
			socialCapitalizedRecommendations = recommendationService.getSocialCapitalizedRecommendations(currentContentItem, currentUser);
		}
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}	

	@In(create=true)
	private RecommendationEvaluationService recommendationEvaluationService;
	
	
	public void evaluateRecommendation(){
		recommendationEvaluationService.evaluateRecommendation();
	}
}
