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
package kiwi.model.personalization;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import kiwi.model.user.User;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OptimisticLockType;

/**
 *
 * @author freddurao
 */
@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@Table(name = "UserPreference")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(name="userPreferenceByUser",
				query="select u " +
					  "from UserPreference u " +
					  "where u.user =:user")})
public class UserPreference implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The database identifier of this activity. */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;	

	@OneToOne
    private User user;

    @Column(columnDefinition="bool default true")
	private boolean showStandardRecommendation = true;
    
    @Column(columnDefinition="bool default false")
	private boolean showGroupRecommendation = false;
    
    @Column(columnDefinition="bool default false")
    private boolean showSocialCapitalRecommendation = false;
    
    @Column(columnDefinition="bool default false")
    private boolean showSemanticVectorRecommendation = false;    

    @Column(columnDefinition="bool default false")
    private boolean showMultiFactorRecommendation = false;
    
    @Column(columnDefinition="bool default false")
    private boolean showTagGroupedRecommendation= false;    
    
    @Column(columnDefinition="bool default true")
    private boolean showRecommendation = true;

    @Column(columnDefinition="bool default true")
    private boolean showContactRecommendation = true;  
    
	@Column(columnDefinition="bool default true")
    private boolean showPersonalRecommendation = true;

    @Column(columnDefinition="bool default false") 
    private boolean showFriendActivity = false;
    
	@Column(columnDefinition="bool default true")
    private boolean redirectToLastPage = true;
	
	@Column(columnDefinition="bool default true")
    private boolean showMyLastVisitedPages = true;
	
	@Column(columnDefinition="bool default false")
    private boolean showTagClusterMenu = false;		

    private int recommendationSize = 3;    

/*    @Version
    private Long version;*/		
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public boolean isShowStandardRecommendation() {
		return showStandardRecommendation;
	}

	public void setShowStandardRecommendation(boolean showStandardRecommendation) {
		this.showStandardRecommendation = showStandardRecommendation;
	}

	public boolean isShowGroupRecommendation() {
		return showGroupRecommendation;
	}

	public void setShowGroupRecommendation(boolean showGroupRecommendation) {
		this.showGroupRecommendation = showGroupRecommendation;
	}

	public boolean isShowSocialCapitalRecommendation() {
		return showSocialCapitalRecommendation;
	}

	public void setShowSocialCapitalRecommendation(
			boolean showSocialCapitalRecommendation) {
		this.showSocialCapitalRecommendation = showSocialCapitalRecommendation;
	}

	public boolean isShowMultiFactorRecommendation() {
		return showMultiFactorRecommendation;
	}

	public void setShowMultiFactorRecommendation(
			boolean showMultiFactorRecommendation) {
		this.showMultiFactorRecommendation = showMultiFactorRecommendation;
	}

	public boolean isShowTagGroupedRecommendation() {
		return showTagGroupedRecommendation;
	}

	public void setShowTagGroupedRecommendation(boolean showTagGroupedRecommendation) {
		this.showTagGroupedRecommendation = showTagGroupedRecommendation;
	}

	public boolean isShowRecommendation() {
		return showRecommendation;
	}

	public void setShowRecommendation(boolean showRecommendation) {
		this.showRecommendation = showRecommendation;
	}

	public int getRecommendationSize() {
		return recommendationSize;
	}

	public void setRecommendationSize(int recommendationSize) {
		this.recommendationSize = recommendationSize;
	}

	public boolean isShowContactRecommendation() {
		return showContactRecommendation;
	}

	public void setShowContactRecommendation(boolean showContactRecommendation) {
		this.showContactRecommendation = showContactRecommendation;
	}

	public boolean isShowPersonalRecommendation() {
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
