/*
 * Copyright (c) 2008-2010, The KiWi Project (http://www.kiwi-project.eu)
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
 */
package kiwi.view.dashboard.profile.server;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

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
import kiwi.view.dashboard.main.client.UIFormFieldValue;
import kiwi.view.dashboard.profile.client.ProfileGWTAction;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.jboss.seam.contexts.Context;
import org.jboss.seam.core.Contexts;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.web.Session;

/**
 * TODO:
 * - Facebook integration: offer to fetch information from Facebook
 * - Twitter integration: subscribe to twitter feed of user
 * - Blog integration: subscribe to RSS feed of blog
 * - Skype integration: online status
 * - City lookup using GeoNames
 * 
 * @author Szaby Gruenwald
 *
 */
@Name("kiwi.view.dashboard.profile.client.ProfileGWTAction")
//@Transactional
@Scope(ScopeType.SESSION)
public class ProfileGWTActionImpl implements ProfileGWTAction, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger 
	private Log log;
	
    @In 
    private FacesMessages facesMessages;
    
	@In
	private User currentUser;

	@In
	private KiWiEntityManager kiwiEntityManager;

	@In
	private CurrentContentItemFactory currentContentItemFactory;
	
	@In
	private ProfileService profileService;
	
	@In
	private UserService userService;
	
	@In
	private ContentItemService contentItemService;

    @In
    private RenderingService renderingPipeline;
    
	@In
	private CurrentUserFactory currentUserFactory;

	private ContentItem profilePhoto;
	
	// Store the state
	private KiWiProfileFacade currentProfile;
	
	
	private String login, firstName, lastName, email, phone, mobile, street, gender;
	
	private Date birthday;
	
	private String facebookAccount, twitterAccount, skypeAccount;
	
	private String homepage, weblog;
	
	private String cityCountry, cityPostalCode, cityName;
	
	private String interests, description;
	
	private double latitude, longitude;
	
	
	@Create
	public void begin() {
		log.info("begin: currentUser.login: #0", currentUser.getLogin());
        login = currentUser.getLogin();
//		currentUser = entityManager.merge(currentUser);

//		currentContentItemFactory.
//                setCurrentItemId(currentUser.getContentItem().getId());
//        log.info("currentContentItem id set to #0, title: #1", currentUser.getContentItem().getId(),
//                currentUser.getContentItem().getTitle());
//		currentContentItemFactory.refresh();

//		Conversation.instance().setDescription("myProfile: " + currentUser.getContentItem().getTitle());
		
		if(profilePhoto ==null){
			profilePhoto = currentUser.getProfilePhoto();
		}
		
		if(profilePhoto ==null){
			profilePhoto = contentItemService.createContentItem();
			log.info("creating new contentItem for profilePhoto with the id #0", profilePhoto.getId());
		}
			
		currentProfile = profileService.getProfile(currentUser);
		
		
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
		
		
		description    = renderingPipeline.renderEditor(currentProfile.getDelegate(),currentUser);
	}

	@Override
	@WebRemote
	/**
	 * Serves the gwt UI with all the fields a profile has. 
	 * Also the unset fields. Could be refactored so the field definitions would be separate
	 * 
	 */
	public LinkedHashMap<String, UIFormFieldValue> getProfileMap() {
        if(login != currentUser.getLogin()){
            begin();
        }

		LinkedHashMap<String, UIFormFieldValue> res = new LinkedHashMap<String, UIFormFieldValue>();

        log.info("getProfileMap: currentUser.login: #0", currentUser.getLogin());

		UIFormFieldValue firstNameFieldValue = new UIFormFieldValue("First name");
		firstNameFieldValue.setStringValue(firstName);
		res.put("firstName", firstNameFieldValue);
		
		UIFormFieldValue lastNameFieldValue = new UIFormFieldValue("Last name");
		lastNameFieldValue.setStringValue(lastName);
		res.put("lastName", lastNameFieldValue);

		UIFormFieldValue genderFieldValue = new UIFormFieldValue("Gender");
		genderFieldValue.setStringValue(gender);
		res.put("gender", genderFieldValue);

		UIFormFieldValue birthdayFieldValue = new UIFormFieldValue("Birthday");
		birthdayFieldValue.setDateValue(birthday);
		res.put("birthday", birthdayFieldValue);

        UIFormFieldValue emailFieldValue = new UIFormFieldValue("Email");
        emailFieldValue.setStringValue(email);
        res.put("email", emailFieldValue);

        UIFormFieldValue profilePhotoFieldValue = new UIFormFieldValue("Email");
        String profilePhotoUrl = profileService.getProfilePhotoDownloadUrl(currentUser);
        profilePhotoFieldValue.setStringValue(profilePhotoUrl);
        res.put("profilePhoto", profilePhotoFieldValue);

		UIFormFieldValue facebookAccountFieldValue = new UIFormFieldValue("Facebook account");
		facebookAccountFieldValue.setStringValue(facebookAccount);
		res.put("facebookAccount", facebookAccountFieldValue);

		UIFormFieldValue homepageFieldValue = new UIFormFieldValue("Homepage");
		homepageFieldValue.setStringValue(homepage);
		res.put("homepage", homepageFieldValue);

/*
		UIFormFieldValue XXXFieldValue = new UIFormFieldValue("XXX");
		XXXFieldValue.setStringValue(XXX);
		res.put("XXX", XXXFieldValue);
*/
//		res.setId(currentProfile.getId());
//		res.setLanguage(null);
//		res.setLatitude(latitude);
//		res.setLongitude(longitude);
//		res.setMediaContent(null);
//		res.setMobile(mobile);
//		res.setModified(currentProfile.getModified());
//		res.setPhone(phone);
//		res.setProfilePhoto(getProfilePhotoTO());
//		res.setSkype(skypeAccount);
//		res.setStreet(street);
//		res.setTagLabels(null);
//		res.setTextContent(null);
//		res.setTitle(null);
//		res.setTwitterAccount(null);

		UIFormFieldValue editableFieldValue = new UIFormFieldValue("editable");
		editableFieldValue.setBooleanValue(new Boolean(true));
		res.put("editable", editableFieldValue);

		return res;
	}
	
	@Override
	@WebRemote
	public void saveChanges(LinkedHashMap<String, UIFormFieldValue> changeMap) {
		boolean error = false;
		
		// validate values
		Pattern p_name = Pattern.compile("[A-Z][a-z]+");
		if(changeMap.containsKey("firstName")){
			if(changeMap.get("firstName") != null){
				firstName = changeMap.get("firstName").getStringValue();
				if(!p_name.matcher(firstName).matches()) {
					facesMessages.add("#{messages['profile.validate.firstname']}");
					error = true;
				}
			}
		}
		if(changeMap.containsKey("lastName")){
			lastName = changeMap.get("lastName").getStringValue();
			if(!p_name.matcher(lastName).matches()) {
				facesMessages.add("#{messages['profile.validate.firstname']}");
				error = true;
			}
		}
		if(changeMap.containsKey("birthday")){
			if(changeMap.get("birthday") != null){

				birthday = changeMap.get("birthday").getDateValue();
				if(birthday.after(new Date())) {
					facesMessages.add("#{messages['profile.validate.birthday']}");
					error = true;
				}
			}

			
		}
		
		// ... TODO
		
		// save values
		if(!error) {
			
			if(changeMap.containsKey("firstName")){
				if(changeMap.get("firstName") == null){
					currentProfile.setFirstName(null);
				} else {
					firstName = changeMap.get("firstName").getStringValue();
					currentProfile.setFirstName(firstName);
				}
			}
			if(changeMap.containsKey("lastName")){
				lastName = changeMap.get("lastName").getStringValue();
				currentProfile.setLastName(lastName);
			}
			if(changeMap.containsKey("birthday")){
				if(changeMap.get("birthday") == null){
					currentProfile.setBirthday(null);
				} else {
					birthday = changeMap.get("birthday").getDateValue();
					currentProfile.setBirthday(birthday);
				}
			}
			if(changeMap.containsKey("gender")){
				gender = changeMap.get("gender").getStringValue();
				currentProfile.setGender(gender);
			}
			if(changeMap.containsKey("email")){
				email = changeMap.get("email").getStringValue();
				currentProfile.setEmail(email);
			}
			if(changeMap.containsKey("phone")){
				phone = changeMap.get("phone").getStringValue();
				currentProfile.setPhone(phone);
			}
			if(changeMap.containsKey("mobile")){
				mobile = changeMap.get("mobile").getStringValue();
				currentProfile.setMobile(mobile);
			}
			if(changeMap.containsKey("street")){
				street = changeMap.get("street").getStringValue();
				currentProfile.setStreet(street);
			}
			
//			currentProfile.setCity(profileService.getCity(cityPostalCode, cityName, cityCountry));
						
			
			if(changeMap.containsKey("facebookAccount")){
				facebookAccount = changeMap.get("facebookAccount").getStringValue();
				currentProfile.setFacebookAccount(facebookAccount);
			}
			if(changeMap.containsKey("twitterAccount")){
				twitterAccount = changeMap.get("twitterAccount").getStringValue();
				currentProfile.setTwitterAccount(twitterAccount);
			}
			if(changeMap.containsKey("skypeAccount")){
				skypeAccount = changeMap.get("skypeAccount").getStringValue();
				currentProfile.setSkype(skypeAccount);
			}
			
			if(changeMap.containsKey("longitude")){
				longitude = changeMap.get("longitude").getDoubleValue();
				currentProfile.setLongitude(longitude);
			}
			if(changeMap.containsKey("latitude")){
				latitude = changeMap.get("latitude").getDoubleValue();
				currentProfile.setLatitude(latitude);
			}
			
			
			if(changeMap.containsKey("description")){
				description = changeMap.get("description").getStringValue();
				contentItemService.updateTextContentItem(currentProfile, description);
			}
			
//			currentProfile.setInterests(profileService.getInterests(interests));
			
			kiwiEntityManager.persist(currentProfile);
		}
		
		for(String key:changeMap.keySet()){
			UIFormFieldValue value = changeMap.get(key);
			log.info("Change to store: #0: #1 (#2)", key, (value==null?"null":value.getValue()), (value==null?"":value.getType()));
		}
		
	}

}
