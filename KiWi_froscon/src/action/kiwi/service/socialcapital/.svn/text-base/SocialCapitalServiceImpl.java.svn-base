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



package kiwi.service.socialcapital;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import kiwi.api.interaction.InteractionService;
import kiwi.api.skill.SkillService;
import kiwi.api.socialcapital.SocialCapitalServiceLocal;
import kiwi.api.socialcapital.SocialCapitalServiceRemote;
import kiwi.model.recommendation.SocialCapital;
import kiwi.model.skill.UserSkill;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * @author Fred Durao
 *
 */
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("socialCapitalService")
public class SocialCapitalServiceImpl implements SocialCapitalServiceLocal, SocialCapitalServiceRemote {

	@In
	private InteractionService interactionService;
	
	@In
	private SkillService skillService;

	/**
	 * @param contentItemAuthor
	 * @param socialCapital
	 * @param friendShipLevel
	 * @param topic
	 */
	public void calculateSocialCapital(SocialCapital socialCapital, User contentItemAuthor, float friendShipLevel, User currentUser){
			
		float socialCapitalScore = 0.1f;		
		List<User> friends = new ArrayList<User>();
		if (friendShipLevel == 0) {
			socialCapitalScore =  socialCapital.getSocialCapitalScore() + 
						    calculateEarnedKnowledge (socialCapital,socialCapital.getEarnedKnowledgeLevel(),1,currentUser) +
							calculateInteractionLevel(socialCapital,socialCapital.getInteractionLevel()    ,1,contentItemAuthor,currentUser);
			
			
			//DecimalFormat twoDigits = new DecimalFormat("##.##");
			//DecimalFormat twoDigits = new DecimalFormat("###.###");
			//socialCapital.setSocialCapitalScore(Float.valueOf(twoDigits.format(socialCapitalScore)));
			socialCapital.setSocialCapitalScore(Float.valueOf(socialCapitalScore));
			System.out.println("contentItemAuthor  "+contentItemAuthor.getLogin()+"   currentUser  "+currentUser.getLogin()+"  socialCapitalScore  "+socialCapital.getSocialCapitalScore());
		}else if (friendShipLevel == 1) {
			friends = socialCapital.getDirectFriends();
			printFriends(contentItemAuthor, friends);
		}else if (friendShipLevel == 2) {
			friends = socialCapital.getIndirectFriends();
		}
		for (User friend : friends) {
			socialCapitalScore = socialCapital.getSocialCapitalScore() + 
			(
				getPersonalEquityByUser(friend) +
			    calculateEarnedKnowledge (socialCapital,socialCapital.getEarnedKnowledgeLevel(),friendShipLevel,friend) +
				calculateInteractionLevel(socialCapital,socialCapital.getInteractionLevel()    ,friendShipLevel,contentItemAuthor,friend)
			 );
		}
		
		//DecimalFormat twoDigits = new DecimalFormat("###.###");
		//socialCapital.setSocialCapitalScore(Float.valueOf(twoDigits.format(socialCapitalScore)));
		socialCapital.setSocialCapitalScore(Float.valueOf(socialCapitalScore));
	}

	private void printFriends(User user, List<User> friends){
			for (User friend : friends) {
				System.out.println("friend  "+friend.getLogin());
			}
	}
	
	/**
	 * @param interactionLevelInit
	 * @param friendShipLevel
	 * @param user
	 * @param friend
	 * @return
	 */
	private float calculateInteractionLevel(SocialCapital socialCapital, float interactionLevelInit, float friendShipLevel, User user, User friend){
		float interactionLevelFinal = interactionLevelInit + (getInteractionLevel(user, friend)/friendShipLevel);
		socialCapital.setInteractionLevel(interactionLevelFinal);
		return interactionLevelFinal;
	}	
	
	/**
	 * @param earnedKnowledgeInit
	 * @param friendShipLevel
	 * @param friend
	 * @param topic
	 * @return
	 */
	private float calculateEarnedKnowledge(SocialCapital socialCapital, float earnedKnowledgeInit, float friendShipLevel, User friend){
		float earnedKnowledgeFinal = earnedKnowledgeInit + (getKnowledgeValueByUser(socialCapital,friend)/friendShipLevel);
		socialCapital.setEarnedKnowledgeLevel(earnedKnowledgeFinal);
		return earnedKnowledgeFinal;
	}

	/**
	 * @param user
	 * @return
	 */
	private float getKnowledgeValueByUser(SocialCapital socialCapital, User user){
		float topicKnowledgeLevel = 0.1f;
		UserSkill userSkill = skillService.getSkillsByUser(user);	
		for (String need : socialCapital.getNeeds()) {
			if (userSkill!=null && userSkill.getSkills().containsKey(need)) {
				topicKnowledgeLevel = topicKnowledgeLevel + userSkill.getSkills().get(need);	
			}
		}
		return topicKnowledgeLevel;
	}	
	
	/**
	 * @param user
	 * @param friend
	 * @return
	 */
	private float getInteractionLevel(User user, User friend){
		float interactionLevel = 0.1f;
		if (interactionService.getUserInteractionByUsers(user,friend)!=null) {	
			interactionLevel = interactionService.getUserInteractionByUsers(user,friend).getInteractivity();
		}
		return interactionLevel;
	}	
	
	
	/**
	 * @param user
	 * @return
	 */
	private float getPersonalEquityByUser(User user){
		float personalEquity = 0.1f;
		return personalEquity;
	}	
}
