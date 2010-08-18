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
package kiwi.service.recommendation.evaluation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;

import kiwi.api.history.HistoryService;
import kiwi.api.rating.RatingService;
import kiwi.api.recommendation.RecommendationService;
import kiwi.api.recommendation.evaluation.RecommendationEvaluationService;
import kiwi.api.skill.SkillService;
import kiwi.api.user.UserService;
import kiwi.evaluation.Performance;
import kiwi.evaluation.Ranking;
import kiwi.model.activity.VisitActivity;
import kiwi.model.content.ContentItem;
import kiwi.model.recommendation.SocialCapitalRecommendation;
import kiwi.model.skill.UserSkill;
import kiwi.model.user.User;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;



/**
 * Fred Durao
 *
 */
@Stateless
@Name("recommendationEvaluationService")
public class RecommendationEvaluationServiceImpl implements RecommendationEvaluationService {

	@In(create = true)
	private RatingService ratingService;
	
	@In
	private UserService userService;
	
	@In
	private SkillService skillService;	
	
	@In(create = true)
	private RecommendationService recommendationService;
	
	@In
	private HistoryService historyService;	

	/**
	 * @param user
	 * @return
	 */
	private Set<ContentItem> listVisitsByUser(User user) {
		Set<ContentItem> resultList= new HashSet<ContentItem>();
		for (VisitActivity visitActivity : historyService.listVisitsByUser(user)) {
			resultList.add(visitActivity.getContentItem());
		}
		return resultList;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.evaluation.RecommendationEvaluationService#recommendationPerformance(java.util.List)
	 */
	public void recommendationPerformance(List<ContentItem> contentItems) {
		int count = 0;
			for (ContentItem contentItem : contentItems) {
					if (ratingService.getRatingAverage(contentItem)>=3d) {
						count++;
					}
			}
		}	

	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.evaluation.RecommendationEvaluationService#evaluateRecommendation()
	 */
	public void evaluateRecommendation() {

		List<Ranking> rankings = new ArrayList<Ranking>();
		
		List<User> users = userService.getAllCreatedUsers();
		
		Map<User,List<Ranking>>  resultMaps = new HashMap<User,List<Ranking>>();

		for (User user : users) {
			/*
			if (user.getLogin().equals("user12")||
			    user.getLogin().equals("user11")||		
			    user.getLogin().equals("user10")||
				continue;
			}
			 */
			List<ContentItem> userHistorys = new ArrayList<ContentItem>(listVisitsByUser(user));
			
			List<ContentItem> userContributionsRelevantItems = skillService.listUserContributions(user);
			
			rankings = new ArrayList<Ranking>();

			for (ContentItem lastVisitedPage : userHistorys) {
			
				Ranking ranking = new Ranking();
				
				//List<ContentItem> recommendedItems = recommendationService.getRecommendations(lastVisitedPage, user);

				List<SocialCapitalRecommendation> socialRecommendedItems = recommendationService.getSocialCapitalizedRecommendations(lastVisitedPage, user);
				List<ContentItem> recommendedItems = new ArrayList<ContentItem>(); 
				for (SocialCapitalRecommendation socialCapitalRecommendation : socialRecommendedItems) {
					recommendedItems.add(socialCapitalRecommendation.getRecommendedItem());
				}
				
				ranking.setQuery(""+lastVisitedPage.getId());
				
				ranking.setUser(user);

				rankings.add(recommendationPerformance(recommendedItems,userContributionsRelevantItems,ranking));
				
			}
			
			resultMaps.put(user, rankings);

		}
		
		printResults(resultMaps);
	}

	/**
	 * @param recommendedItems
	 * @param relevantItems
	 * @param ranking
	 * @return
	 */
	public Ranking recommendationPerformance(List<ContentItem> recommendedItems, List<ContentItem> relevantItems,Ranking ranking) {
			List<ContentItem> results = recommendedItems;
			List<ContentItem> retrievedItems = new ArrayList<ContentItem>();
			int count = 0;
			ranking.setMax(results.size());
			for (ContentItem contentItem : results) {
					if (ratingService.getRatingAverage(contentItem)>=3d) {
						retrievedItems.add(contentItem);
						count++;
					}
			}
			
			try {
				Performance.processContentItemMetrics(retrievedItems, relevantItems,ranking);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ranking.setRetrieved(count);
			ranking.setScore(ranking.getRetrieved()/ranking.getMax());
			return ranking;
		}

	/**
	 * @param rankingMaps
	 */
	private void printResults(Map<User,List<Ranking>> rankingMaps){
		float aveScore=0f;
		float avePrec=0f;
		float aveRecall=0f;
		int promo=0;
		float maxSize=0f;
		Map<User,Ranking>  resultFinalMaps = new HashMap<User,Ranking>();
		for (User user : rankingMaps.keySet()) {
			maxSize=(float)rankingMaps.get(user).size();
			for (Ranking ranking : rankingMaps.get(user)) {
				aveScore = aveScore+ranking.getScore();
				avePrec = avePrec+ranking.getPrecision();
				aveRecall = aveRecall+ranking.getRecall();
				promo = promo+ranking.getPromotions();
				//System.out.println("User	"+user.getLogin()+"	query	"+searchRanking.getQuery()+"	score	"+searchRanking.getScore()+"	precision	"+searchRanking.getPrecision()+"	recall	"+searchRanking.getRecall());
			}
			Ranking rankingFinal = new Ranking();
			rankingFinal.setAveScore(aveScore/maxSize);
			rankingFinal.setAveRecall(aveRecall/maxSize);
			rankingFinal.setAvePrecision(avePrec/maxSize);
			rankingFinal.setPromotions(promo);
			resultFinalMaps.put(user,rankingFinal);
			aveScore=0f;
			avePrec=0f;
			aveRecall=0f;
			maxSize=0f;	
			promo=0;
		}
		
		StringBuffer sb = new StringBuffer();
		for (User user : resultFinalMaps.keySet()) {
			Ranking rankingFinal = resultFinalMaps.get(user);
			//System.out.println("User	"+user.getLogin()+"	avg. score	"+searchRankingFinal.getAveScore()+"	avg. precision	"+searchRankingFinal.getAvePrecision()+"	avg .recall	"+searchRankingFinal.getAveRecall());
			System.out.println("User	"+user.getLogin()+"	avg. score	"+rankingFinal.getAveScore()+"	avg. precision	"+rankingFinal.getAvePrecision()+"	avg .recall	"+rankingFinal.getAveRecall()+"	total promotions	"+rankingFinal.getPromotions());
			String text = "User	"+user.getLogin()+"	avg. score	"+rankingFinal.getAveScore()+"	avg. precision	"+rankingFinal.getAvePrecision()+"	avg .recall	"+rankingFinal.getAveRecall()+"	total promotions	"+rankingFinal.getPromotions();
			sb.append(text);
			sb.append("\n");
		}
		
		writeResults(sb.toString());
		
	}
	
	/**
	 * @param text
	 */
	private void writeResults(String text){
		Calendar now = Calendar.getInstance();
		String date = now.get(Calendar.DATE)+"_"+now.get(Calendar.HOUR)+"_"+now.get(Calendar.MINUTE)+"_"+now.get(Calendar.SECOND);
		FileWriter fstream;
		try {
			if (!text.trim().equals("")) {
				fstream = new FileWriter("C:/Users/Fred/Desktop/rec/recResult_"+date+".txt");
				//fstream = new FileWriter("C:/Users/Fred/Desktop/sr/searchResult.txt");
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(text);
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		
	}

	/**
	 * @return
	 */
	public List<UserSkill> getUserSkills() {
		List<UserSkill>  userSkills = skillService.listUserSkills();
		if (userSkills == null) {
			userSkills = skillService.listUserSkills();
		}
		return userSkills;
	}
	
	
	/**
	 * @return
	 */
	private static List<String> getTestQuery(){
		List<String> query = new ArrayList<String>();
		query.add("standup");
		query.add("stocking");
		query.add("windpower");
		query.add("threat");
		query.add("briefing");
		query.add("personality");
		query.add("storm");
		query.add("kid");
		query.add("missing");
		return query;
	}
}
