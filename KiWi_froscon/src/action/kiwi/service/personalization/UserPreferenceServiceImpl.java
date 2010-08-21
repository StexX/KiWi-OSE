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


package kiwi.service.personalization;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import kiwi.api.personalization.UserPreferenceServiceLocal;
import kiwi.api.personalization.UserPreferenceServiceRemote;
import kiwi.model.personalization.UserPreference;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Fred Durao
 *
 */
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("userPreferenceService")
public class UserPreferenceServiceImpl implements UserPreferenceServiceLocal, UserPreferenceServiceRemote {

	@Logger
	private Log log;

	@In
	private EntityManager entityManager;
	
	@In
	private User currentUser;	
	
	/**
	 * 
	 */
	public void createUserPreference(UserPreference userPreference) {
		currentUser.setUserPreference(userPreference);
		entityManager.persist(userPreference);
//		entityManager.flush();
	}	

	/**
	 * @return
	 */
	/* (non-Javadoc)
	 * @see kiwi.api.personalization.UserPreferenceService#getUserPreferenceByUser(kiwi.model.user.User)
	 */
	public UserPreference getUserPreferenceByUser(User user) {
		UserPreference userPreference = null;
		javax.persistence.Query q = entityManager.createNamedQuery("userPreferenceByUser");
        q.setParameter("user", user);
        try {
        	userPreference = (UserPreference)q.getSingleResult();
 		} catch (NoResultException ex) {
 			//
		}
		return userPreference;
	}


	/* (non-Javadoc)
	 * @see kiwi.api.personalization.UserPreferenceService#createInitialUserPreference(kiwi.model.user.User)
	 */
	@Override
	public UserPreference createInitialUserPreference(User user) {
		log.info("Creating user preference for user ?", user.getFirstName());
		UserPreference userPreference = new UserPreference();
		userPreference.setUser(user);
		entityManager.persist(userPreference);
//		entityManager.flush();
		return getUserPreferenceByUser(user);
	}		

}
