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
package artaround.action.register;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.content.MediaContent;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

import artaround.datamodel.ArtAroundUserFacade;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@Name("artaround.profileAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class ProfileAction {
	
	
	private ProfileBean profileBean;
	
//	@In
//	private ArtAroundUserFacade artAroundUserFacade;
	
	@In
	private User currentUser;
	

	@Logger
	private Log log;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	private ArtAroundUserFacade userFacade;
	
	@Create
	public void begin(){
			    
		userFacade = kiwiEntityManager.createFacade(currentUser.getContentItem(), ArtAroundUserFacade.class);
		
		log.info(userFacade.getGebDate());
		log.info(userFacade.getGender());
		log.info(userFacade.getPostalAdressStreetAndNumber());
		log.info(userFacade.getPlaceOfBirth());
		log.info(userFacade.getPostalAdressCity());
		log.info(userFacade.getPostalAdressCountryCode());
		log.info(userFacade.getMobilePhoneNumber());
		log.info(userFacade.getFacebook());
		log.info(userFacade.getTwitter());
		
		
		profileBean = new ProfileBean();
		profileBean.setFirstName(currentUser.getFirstName());
		log.info(currentUser.getFirstName());
		profileBean.setLastName(currentUser.getLastName());
		log.info(currentUser.getLastName());
		profileBean.setEmailAdress(currentUser.getEmail());
		log.info(currentUser.getEmail());
		//-- Facade infrmation --//
		profileBean.setGebDate(userFacade.getGebDate());
		profileBean.setGender(userFacade.getGender());
		profileBean.setPostalAdressStreetAndNumber(userFacade.getPostalAdressStreetAndNumber());
		profileBean.setPlaceOfBirth(userFacade.getPlaceOfBirth());
		profileBean.setPostalAdressCity(userFacade.getPostalAdressCity());
		profileBean.setPostalAdressCountryCode(userFacade.getPostalAdressCountryCode());
		profileBean.setMobilePhoneNumber(userFacade.getMobilePhoneNumber());
		profileBean.setFacebook(userFacade.getFacebook());
		profileBean.setTwitter(userFacade.getTwitter());
	}
	
	public void store(){
		
		currentUser.setFirstName(profileBean.getFirstName());
		currentUser.setLastName(profileBean.getLastName());
		currentUser.setEmail(profileBean.getEmailAdress());
		userFacade.setGebDate(profileBean.getGebDate());
		userFacade.setGender(profileBean.getGender());
		userFacade.setPostalAdressStreetAndNumber(profileBean.getPostalAdressStreetAndNumber());
		userFacade.setPostalAdressCity(profileBean.getPostalAdressCity());
		userFacade.setPostalAdressCountryCode(profileBean.getPostalAdressCountryCode());
		userFacade.setPlaceOfBirth(profileBean.getPlaceOfBirth());
		userFacade.setMobilePhoneNumber(profileBean.getMobilePhoneNumber());
		userFacade.setFacebook(profileBean.getFacebook());
		userFacade.setTwitter(profileBean.getTwitter());
		
		kiwiEntityManager.persist(userFacade.getDelegate());
	}
	
	public ProfileBean getProfileBean() {
		return profileBean;
	}

	public void setProfileBean(ProfileBean profileBean) {
		this.profileBean = profileBean;
	}
	
	 public User getCurrentUser() {
		    return currentUser;
	 }

	public void setCurrentUser(User currentUser) {
		    this.currentUser = currentUser;
	}
	

	
}
