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
 * sschaffe
 * 
 */
package kiwi.dashboard.action;

import java.io.Serializable;

import kiwi.api.personalization.UserPreferenceService;
import kiwi.model.personalization.UserPreference;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

/**
 * IntegrationAction - implements user preference. At here users can set interface preferences.
 *
 * @author Fred Durao
 *
 */
@Name("kiwi.dashboard.preferenceAction")
@Scope(ScopeType.PAGE)
public class PreferenceAction implements Serializable {

	private static final long serialVersionUID = 1L;

	@In
	private UserPreferenceService userPreferenceService;	

	@In
	private User currentUser;

	private String recommendationSize = "3";

	private boolean showStandardRecommendation;
    
    private boolean showGroupRecommendation;
    
    private boolean showSocialCapitalRecommendation;
    
    private boolean showMultiFactorRecommendation;
    
    private boolean showTagGroupedRecommendation;    
    
    private boolean showRecommendation;	

	private boolean showContactRecommendation;  
    
    private boolean showPersonalRecommendation;
    
    private boolean showSemanticVectorRecommendation;
    
    private boolean showFriendActivity;

    private boolean redirectToLastPage;

    private boolean showMyLastVisitedPages;
    
    private boolean showTagClusterMenu;    

    /**
	 * 
	 */
	public void saveUserPreference() {
		UserPreference userPreference = userPreferenceService.getUserPreferenceByUser(currentUser);
		if (userPreference==null) {
			userPreference = new UserPreference();	
		}			
		userPreference.setRecommendationSize(new Integer(getRecommendationSize()));
		userPreference.setShowGroupRecommendation(showGroupRecommendation);
		userPreference.setShowMultiFactorRecommendation(showMultiFactorRecommendation);
		userPreference.setShowSocialCapitalRecommendation(showSocialCapitalRecommendation);
		userPreference.setShowStandardRecommendation(showStandardRecommendation);
		userPreference.setShowTagGroupedRecommendation(showTagGroupedRecommendation);
		userPreference.setShowPersonalRecommendation(showPersonalRecommendation);
		userPreference.setShowContactRecommendation(showContactRecommendation);
		userPreference.setShowSemanticVectorRecommendation(showSemanticVectorRecommendation);
		userPreference.setShowFriendActivity(showFriendActivity);
		userPreference.setShowMyLastVisitedPages(showMyLastVisitedPages);
		userPreference.setRedirectToLastPage(redirectToLastPage);
		userPreference.setShowTagClusterMenu(showTagClusterMenu);
		userPreference.setUser(currentUser);
		userPreferenceService.createUserPreference(userPreference);
		FacesMessages.instance().add("User preferences updated.");		
	}
	
	
	/**
	 * 
	 */
	@Create
	public void loadUserPreference() {
		UserPreference userPreference = userPreferenceService.getUserPreferenceByUser(currentUser);
		if (userPreference!=null) {
			this.setRecommendationSize(new Integer(userPreference.getRecommendationSize()).toString());
			this.setShowGroupRecommendation(userPreference.isShowGroupRecommendation());
			this.setShowMultiFactorRecommendation(userPreference.isShowMultiFactorRecommendation());
			this.setShowSocialCapitalRecommendation(userPreference.isShowSocialCapitalRecommendation());
			this.setShowStandardRecommendation(userPreference.isShowStandardRecommendation());
			this.setShowTagGroupedRecommendation(userPreference.isShowTagGroupedRecommendation());
			this.setShowPersonalRecommendation(userPreference.isShowPersonalRecommendation());
			this.setShowContactRecommendation(userPreference.isShowContactRecommendation());
			this.setShowSemanticVectorRecommendation(userPreference.isShowSemanticVectorRecommendation());
			this.setShowRecommendation(userPreference.isShowRecommendation());
			this.setShowFriendActivity(userPreference.isShowFriendActivity());
			this.setShowMyLastVisitedPages(userPreference.isShowMyLastVisitedPages());
			this.setRedirectToLastPage(userPreference.isRedirectToLastPage());
			this.setShowTagClusterMenu(userPreference.isShowTagClusterMenu());
		}else {
			userPreferenceService.createInitialUserPreference(currentUser);
		}
	}	
	
	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public String getRecommendationSize() {
		return recommendationSize;
	}

	public void setRecommendationSize(String recommendationSize) {
		this.recommendationSize = recommendationSize;
	}	

	public boolean getShowStandardRecommendation() {
		return showStandardRecommendation;
	}


	public void setShowStandardRecommendation(boolean showStandardRecommendation) {
		this.showStandardRecommendation = showStandardRecommendation;
	}


	public boolean getShowGroupRecommendation() {
		return showGroupRecommendation;
	}


	public void setShowGroupRecommendation(boolean showGroupRecommendation) {
		this.showGroupRecommendation = showGroupRecommendation;
	}


	public boolean getShowSocialCapitalRecommendation() {
		return showSocialCapitalRecommendation;
	}


	public void setShowSocialCapitalRecommendation(
			boolean showSocialCapitalRecommendation) {
		this.showSocialCapitalRecommendation = showSocialCapitalRecommendation;
	}


	public boolean getShowMultiFactorRecommendation() {
		return showMultiFactorRecommendation;
	}


	public void setShowMultiFactorRecommendation(
			boolean showMultiFactorRecommendation) {
		this.showMultiFactorRecommendation = showMultiFactorRecommendation;
	}


	public boolean getShowTagGroupedRecommendation() {
		return showTagGroupedRecommendation;
	}


	public void setShowTagGroupedRecommendation(boolean showTagGroupedRecommendation) {
		this.showTagGroupedRecommendation = showTagGroupedRecommendation;
	}


	public boolean getShowRecommendation() {
		return showRecommendation;
	}


	public void setShowRecommendation(boolean showRecommendation) {
		this.showRecommendation = showRecommendation;
	}
	
    
    public boolean getShowContactRecommendation() {
		return showContactRecommendation;
	}

	public void setShowContactRecommendation(boolean showContactRecommendation) {
		this.showContactRecommendation = showContactRecommendation;
	}

	public boolean getShowPersonalRecommendation() {
		return showPersonalRecommendation;
	}

	public void setShowPersonalRecommendation(boolean showPersonalRecommendation) {
		this.showPersonalRecommendation = showPersonalRecommendation;
	}


	public boolean isShowSemanticVectorRecommendation() {
		return showSemanticVectorRecommendation;
	}


	public void setShowSemanticVectorRecommendation(
			boolean showSemanticVectorRecommendation) {
		this.showSemanticVectorRecommendation = showSemanticVectorRecommendation;
	}


	public boolean isShowFriendActivity() {
		return showFriendActivity;
	}


	public void setShowFriendActivity(boolean showFriendActivity) {
		this.showFriendActivity = showFriendActivity;
	}


	public boolean isRedirectToLastPage() {
		return redirectToLastPage;
	}


	public void setRedirectToLastPage(boolean redirectToLastPage) {
		this.redirectToLastPage = redirectToLastPage;
	}


	public boolean isShowMyLastVisitedPages() {
		return showMyLastVisitedPages;
	}


	public void setShowMyLastVisitedPages(boolean showMyLastVisitedPages) {
		this.showMyLastVisitedPages = showMyLastVisitedPages;
	}	
	
	public boolean isShowTagClusterMenu() {
		return showTagClusterMenu;
	}

	public void setShowTagClusterMenu(boolean showTagClusterMenu) {
		this.showTagClusterMenu = showTagClusterMenu;
	}		
	
}
