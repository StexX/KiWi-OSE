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
package kiwi.commons.userProfile;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.render.RenderingService;
import kiwi.api.user.KiWiProfileFacade;
import kiwi.api.user.ProfileService;
import kiwi.api.user.UserService;
import kiwi.context.CurrentContentItemFactory;
import kiwi.context.CurrentUserFactory;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * TODO:
 * - Facebook integration: offer to fetch information from Facebook
 * - Twitter integration: subscribe to twitter feed of user
 * - Blog integration: subscribe to RSS feed of blog
 * - Skype integration: online status
 * - City lookup using GeoNames
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.dashboard.profileAction")
@Scope(ScopeType.PAGE)
//@Transactional
public class ProfileAction implements Serializable {
	
	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;

	@Logger 
	protected Log log;
	
	@In 
	protected FacesMessages facesMessages;
    
	@In(scope=ScopeType.SESSION)
	protected User currentUser;

	@In
	protected KiWiEntityManager kiwiEntityManager;

	@In
	protected CurrentContentItemFactory currentContentItemFactory;
	
	@In
	protected ProfileService profileService;
	
	@In
	protected ContentItemService contentItemService;

	@In
	protected RenderingService renderingPipeline;
    
	@In
	protected CurrentUserFactory currentUserFactory;

	
	protected ContentItem profilePhoto;
	
//	protected KiWiProfileFacade currentProfile;
	
	
	protected String firstName, lastName, email, phone, mobile, street, gender;
	
	protected Date birthday;
	
	protected String facebookAccount, twitterAccount, skypeAccount;
	
	protected String homepage, weblog;
	
	protected String cityCountry, cityPostalCode, cityName;
	
	protected String interests, description;
	
	protected String rsaPubKey;
	
	protected double latitude, longitude;
	
	@Create
	public void begin() {
		log.info("setting currentContentItem to the active users profile");

//		currentUser = entityManager.merge(currentUser);
		currentContentItemFactory.setCurrentItemId(currentUser.getContentItem().getId());
		log.info("currentContentItem id set to #0, title: #1", currentUser.getContentItem().getId(),
				currentUser.getContentItem().getTitle());
		currentContentItemFactory.refresh();
		Conversation.instance().setDescription("myProfile: " + currentUser.getContentItem().getTitle());
		
		if(profilePhoto ==null){
			profilePhoto = currentUser.getProfilePhoto();
		}
		
		if(profilePhoto ==null){
			profilePhoto = contentItemService.createContentItem();
			log.info("creating new contentItem for profilePhoto with the id #0", profilePhoto.getId());
		}
			
		KiWiProfileFacade currentProfile = profileService.getProfile(currentUser);
		
		
		firstName      = currentProfile.getFirstName();
		lastName       = currentProfile.getLastName();
		gender         = currentProfile.getGender();
		birthday       = currentProfile.getBirthday();
		email          = currentProfile.getEmail();
		phone          = currentProfile.getPhone();
		mobile         = currentProfile.getMobile();
		street         = currentProfile.getStreet();
		
		if(currentProfile.getCity() != null) {
			if(currentProfile.getCity().getCountry() != null) {
				cityCountry    = currentProfile.getCity().getCountry().getISOCode();
			}
			cityPostalCode = currentProfile.getCity().getPostalCode();
			cityName       = currentProfile.getCity().getName();
		}
		
		facebookAccount = currentProfile.getFacebookAccount();
		twitterAccount  = currentProfile.getTwitterAccount();
		skypeAccount    = currentProfile.getSkype();
		
		// TODO: should be clickable!
		interests = "";
		for(ContentItem i : currentProfile.getInterests()) {
			interests += i.getTitle() + ", ";
		}
		
		latitude       = currentProfile.getLatitude();
		longitude      = currentProfile.getLongitude();
		
		if(currentProfile.getDelegate().getTextContent() != null) {
			description    = currentProfile.getDelegate().getTextContent().getPlainString();
		}
//		description    = renderingPipeline.renderEditor(currentProfile.getDelegate(), currentUser);
		
		specificBehavior();
		
	}
	
	//is overwritten by any subclass
	//implements specific behavior in the init method, because it is not allowed to implement 2 create methods
	protected void specificBehavior(){
	    
	}
	
	public String save() {
		boolean error = checkValues();
		// ... TODO
		
		KiWiProfileFacade currentProfile = profileService.getProfile(currentUser);
		
		// save values
		if(!error) {
		    if(firstName != null){
			currentProfile.setFirstName(firstName);}
		    if(lastName != null){
			currentProfile.setLastName(lastName);}
		    if(gender != null){
			currentProfile.setGender(gender);}
		    if(birthday != null){
			currentProfile.setBirthday(birthday);}
		    if(email != null){
			currentProfile.setEmail(email);}
		    if(phone != null){
			currentProfile.setPhone(phone);}
		    if(mobile != null){
			currentProfile.setMobile(mobile);}
		    if(street != null){
			currentProfile.setStreet(street);}
		    if((cityName != null)){
			currentProfile.setCity(profileService.getCity(cityPostalCode, cityName, cityCountry));}
		    if(facebookAccount != null){				
			currentProfile.setFacebookAccount(facebookAccount);}
		    if(twitterAccount != null){
			currentProfile.setTwitterAccount(twitterAccount);}
		    if(skypeAccount != null){
			currentProfile.setSkype(skypeAccount);}

		    currentProfile.setLongitude(longitude);
		    
		    currentProfile.setLatitude(latitude);

		    contentItemService.updateTextContentItem(currentProfile, description);
			
		    currentProfile.setInterests(profileService.getInterests(interests));
			
		    kiwiEntityManager.persist(currentProfile);
		}
		
		postSave();
		return "profile_edited";
	}

	/**
	 * @return
	 */
	protected boolean checkValues() {
	    boolean error = false;
	    
	    // validate values
	    Pattern p_name = Pattern.compile("[A-Z][a-z]+");
	    if(!p_name.matcher(firstName).matches()) {
	    	facesMessages.add("#{messages['profile.validate.firstname']}");
	    	error = true;
	    }
	    if(!p_name.matcher(lastName).matches()) {
	    	facesMessages.add("#{messages['profile.validate.lastname']}");
	    	error = true;
	    }
	    return error;
	}
	
	protected void postSave(){
	    
	}
	
	public User getCurrentUser() {
		// currentUser = kiwiEntityManager.merge(currentUser);
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	@Observer("userProfilePhotoSave")
	public void onSaveUserProfilePhoto() throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException{
		log.info("userProfilePhotoSave called, doing user-profile-photo specific persistence");
		
    	// remove ContentItem from old media content; we get exceptions later on otherwise
    	if(currentUser.getProfilePhoto() == null) {
    		currentUser.setProfilePhoto(profilePhoto);
    	}
    	UserService us = (UserService)Component.getInstance("userService");
//    	us.saveUser(currentUser);
    	log.info("profilePhotoId:", profilePhoto.getId());
		
		currentUserFactory.forceRefresh();
	}
	
	@Observer("userProfilePhotoRemove")
	public void onRemoveUserProfilePhoto(){
		log.info("userProfilePhotoRemove called, doing user-profile-photo specific persistence");

		currentUser.setProfilePhoto(null);
		kiwiEntityManager.persist(currentUser);
		UserService us = (UserService)Component.getInstance("userService");
//		us.saveUser(currentUser);

		profilePhoto=null;
		begin();
	}

	public ContentItem getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(ContentItem profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getFacebookAccount() {
		return facebookAccount;
	}

	public void setFacebookAccount(String facebookAccount) {
		this.facebookAccount = facebookAccount;
	}

	public String getTwitterAccount() {
		return twitterAccount;
	}

	public void setTwitterAccount(String twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public String getSkypeAccount() {
		return skypeAccount;
	}

	public void setSkypeAccount(String skypeAccount) {
		this.skypeAccount = skypeAccount;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getWeblog() {
		return weblog;
	}

	public void setWeblog(String weblog) {
		this.weblog = weblog;
	}

	public String getCityCountry() {
		return cityCountry;
	}

	public void setCityCountry(String cityCountry) {
		this.cityCountry = cityCountry;
	}

	public String getCityPostalCode() {
		return cityPostalCode;
	}

	public void setCityPostalCode(String cityPostalCode) {
		this.cityPostalCode = cityPostalCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public void setRsaPubKey(String rsaPubKey) {
		this.rsaPubKey = rsaPubKey;
	}


	public String getRsaPubKey() {
		return rsaPubKey;
	}
}
