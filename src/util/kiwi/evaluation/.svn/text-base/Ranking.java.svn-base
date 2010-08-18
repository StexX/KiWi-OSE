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
package kiwi.evaluation;



import kiwi.model.user.User;

/**
 * @author Fred Durao
 *
 */
public class Ranking {

		public Ranking(){	
			
		}
		
		/**
		 * @param score
		 * @param max
		 * @param retrieved
		 * @param query
		 * @param user
		 */
		public Ranking(float score, float max, float retrieved, String query,
				User user) {
			super();
			this.score = score;
			this.max = max;
			this.retrieved = retrieved;
			this.query = query;
			this.user = user;
		}
		
		/**
		 * @param score
		 * @param query
		 * @param user
		 */
		public Ranking(float score, String query,
				User user) {
			super();
			this.score = score;
			this.query = query;
			this.user = user;
		}

	   private float avePrecision;
	   
	   private float aveRecall;
	   
	   private float aveScore;
	   
	   private float precision;
	   
	   private boolean promoteScore;
	   
	   private int promotions;
	   
	   private float recall;
	   
	   private float score;
	   
	   private float max;
	   
	   private float retrieved;
	   
	   private String query;
	   
	   private User user;
		   
	   public float getScore() {
			return score;
		}
		public void setScore(float score) {
			this.score = score;
		}
		public float getMax() {
			return max;
		}
		public void setMax(float max) {
			this.max = max;
		}
		public float getRetrieved() {
			return retrieved;
		}
		public void setRetrieved(float retrieved) {
			this.retrieved = retrieved;
		}
		public String getQuery() {
			return query;
		}
		public void setQuery(String query) {
			this.query = query;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
	   public float getPrecision() {
			return precision;
		}

		public void setPrecision(float precision) {
			this.precision = precision;
		}

		public float getRecall() {
			return recall;
		}

		public void setRecall(float recall) {
			this.recall = recall;
		}
	   public float getAvePrecision() {
			return avePrecision;
		}

		public void setAvePrecision(float avePrecision) {
			this.avePrecision = avePrecision;
		}

		public float getAveRecall() {
			return aveRecall;
		}

		public void setAveRecall(float aveRecall) {
			this.aveRecall = aveRecall;
		}

		public float getAveScore() {
			return aveScore;
		}

		public void setAveScore(float aveScore) {
			this.aveScore = aveScore;
		}
		
	   public boolean isPromoteScore() {
			return promoteScore;
		}

		public void setPromoteScore(boolean promoteScore) {
			 this.promotions=getPromotions()+1;
			this.promoteScore = promoteScore;
		}

		public int getPromotions() {
			return promotions;
		}

		public void setPromotions(int promotions) {
			this.promotions = promotions;
		}		
	   
	}
	
