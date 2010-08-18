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
package kiwi.model.recommendation;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import kiwi.model.user.User;

/**
 *
 * @author Fred Durao
 */
public class SocialCapital implements Serializable, Comparable<SocialCapital> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public SocialCapital(float socialCapitalScore, float interactionLevel,
			float earnedKnowledgeLevel, List<User> directFriends,
			List<User> indirectFriends) {
		super();
		this.socialCapitalScore = socialCapitalScore;
		this.interactionLevel = interactionLevel;
		this.earnedKnowledgeLevel = earnedKnowledgeLevel;
		this.directFriends = directFriends;
		this.indirectFriends = indirectFriends;
		
	}


	public SocialCapital() {
	}

	float socialCapitalScore;
	
	float interactionLevel;
	
	float earnedKnowledgeLevel;
	
	String topic;
	
	List<User> directFriends;
	
	List<User> indirectFriends;
	
	List<String> needs;	

	   	
		
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * Method used for ranking contentItem by contentScores necessary when
	 * calculating multi factor recommendations
	 */
	public int compareTo(SocialCapital socialCapital) {
        if (this.socialCapitalScore == socialCapital.getSocialCapitalScore())
            return 0;
        else if (this.socialCapitalScore < socialCapital.getSocialCapitalScore())
            return 1;
        else
            return -1;
    }


	public float getSocialCapitalScore() {
		return socialCapitalScore;
	}


	public void setSocialCapitalScore(float socialCapitalScore) {
		this.socialCapitalScore = socialCapitalScore;
	}


	public float getInteractionLevel() {
		return interactionLevel;
	}


	public void setInteractionLevel(float interactionLevel) {
		this.interactionLevel = interactionLevel;
	}


	public float getEarnedKnowledgeLevel() {
		return earnedKnowledgeLevel;
	}


	public void setEarnedKnowledgeLevel(float earnedKnowledgeLevel) {
		this.earnedKnowledgeLevel = earnedKnowledgeLevel;
	}


	public List<User> getDirectFriends() {
		return directFriends;
	}


	public void setDirectFriends(List<User> directFriends) {
		this.directFriends = directFriends;
	}


	public List<User> getIndirectFriends() {
		return indirectFriends;
	}


	public void setIndirectFriends(List<User> indirectFriends) {
		this.indirectFriends = indirectFriends;
	}


	public String getTopic() {
		return topic;
	}


	public void setTopic(String topic) {
		this.topic = topic;
	}


	public List<String> getNeeds() {
		return needs;
	}


	public void setNeeds(List<String> needs) {
		this.needs = needs;
	}

	@SuppressWarnings("unused")
	private void calculateDirectAndIndirectFriends(User author){
		User contentItemAuthor = author;
//		System.out.println("FRIENDS of "+contentItemAuthor.getLogin());	
		Set<User> friends = contentItemAuthor.getFriends();
		for (User friend : friends) {
//			System.out.println("DIRECT FRIENDS  "+friend.getLogin());
			Set<User> indirectFriends = friend.getFriends();
				indirectFriends.removeAll(friends);
				indirectFriends.remove(contentItemAuthor);
//				for (User indirectFriend : indirectFriends) {
//					System.out.println("INDIRECT FRIENDS  "+indirectFriend.getLogin());
//				}	
		}
	}
}
