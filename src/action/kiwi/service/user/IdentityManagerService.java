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

package kiwi.service.user;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.security.auth.login.LoginException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import kiwi.api.config.ConfigurationService;
import kiwi.api.event.KiWiEvents;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.config.Configuration;
import kiwi.model.Constants;
import kiwi.model.user.User;
import kiwi.service.config.ConfigurationServiceImpl;
import kiwi.service.transaction.KiWiSynchronizationImpl;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions.MethodExpression;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.management.IdentityManagementException;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.security.management.JpaIdentityStore;
import org.jboss.seam.transaction.Transaction;

/**
 * The IdentitiyManagerService is used to initialise the systems pre-requisites and 
 * to login and logout the current user.
 * This class should be used instead of directly accessing the Identity.instance() methods.
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Scope(ScopeType.STATELESS)
@Name("identityManagerService")
@AutoCreate
public class IdentityManagerService {

	@Logger
	private Log log;
	
	@In
	private EntityManager entityManager;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private TransactionService transactionService;

	@In
	private TripleStore tripleStore;

	
	
	/**
	 * Initialises the systems identity pre-requisites, e.g. the admin user.
	 * 
	 * @param param
	 */
//	@Transactional(TransactionPropagationType.REQUIRED)
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Observer(KiWiEvents.CONFIGURATIONSERVICE_CREATE_ADMIN)
	public void init(String param) {
		try {
			if(!Transaction.instance().isActive()) {
				Transaction.instance().begin();
			}
		} catch (NotSupportedException e1) {
			e1.printStackTrace();
		} catch (SystemException e1) {
			e1.printStackTrace();
		}
//		transactionService.registerSynchronization(KiWiSynchronizationImpl.getInstance(), 
//				transactionService.getUserTransaction());
		Configuration config_adminName = configurationService.getConfiguration("kiwi.identity.admin.name");
		Configuration config_adminPW = configurationService.getConfiguration("kiwi.identity.admin.password");
		
		String adminName = null;
		String adminPW = null;
		
		if(config_adminName == null || (adminName = config_adminName.getStringValue()) == null) {
			configurationService.setConfiguration("kiwi.identity.admin.name", "admin");
			adminName = new String("admin");
		}
		if(config_adminPW == null || (adminPW = config_adminPW.getStringValue()) == null) {
			configurationService.setConfiguration("kiwi.identity.admin.password", "4dm1n");
			adminPW = new String("4dm1n");
		}
		
		try {
			IdentityManager.instance().createUser(adminName, adminPW);
		} catch (IdentityManagementException e) {
			log.warn("Admin user already exists");
		}
		
		entityManager.flush();
		
		try {
			Transaction.instance().commit();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (RollbackException e) {
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Called by IdentityManager.instance().createUser() before the user will be created
	 * @param user
	 */
	@Observer(JpaIdentityStore.EVENT_PRE_PERSIST_USER)
	public void prePersistUser(User user) {
		if(!ConfigurationServiceImpl.configurationInProgress) {
			user.setResource(tripleStore.createUriResource(
					configurationService.getBaseUri()+"/user/"+user.getLogin()));
			user.setType(tripleStore.createUriResource(Constants.NS_KIWI_CORE + "User"));
		}
	}

	/**
	 * Called by IdentityManager.instance().createUser() after the user has been created.
	 * @param user
	 * @return
	 */
	@Observer(JpaIdentityStore.EVENT_USER_CREATED)
	public String afterUserCreatedAction(User user) {
		if(!ConfigurationServiceImpl.configurationInProgress) {
			entityManager.flush();
			return "";
		}
		return "";
	}
	
	public boolean authenticate() {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void login(String username, String password, 
			MethodExpression customAuthentication) throws LoginException {
		if(!Identity.instance().isLoggedIn()) {
			
			Identity identity = Identity.instance();
			//identity.unAuthenticate();
			identity.setAuthenticateMethod(customAuthentication);
			identity.getCredentials().setUsername(username);
			if(password == null)
				password = new String("wtf");
			identity.getCredentials().setPassword(password);
			
			if(identity.login() == null) {
				log.error("User #0 could not log in", username);
				throw new LoginException("User "+username+" could not log in");
			}
			if(identity.isLoggedIn()) { 
				log.info("User #0 successfully logged in", username);
//				CurrentUserFactory currentUserFactory = (CurrentUserFactory) Component.getInstance("currentUserFactory");
//				currentUserFactory.forceRefresh();
			}
		} else {
			log.error("A user is already logged in :(");
		}
	}
	
	/**
	 * Login the user after setting his/her credentials
	 * @param username
	 * @param password
	 */
	public synchronized void login(String username, String password) throws LoginException {
//		Identity.instance().unAuthenticate();
		if(!Identity.instance().isLoggedIn()) {
			Identity.instance().getCredentials().setUsername(username);
			Identity.instance().getCredentials().setPassword(password);
			if(Identity.instance().login() == null) {
				log.error("User #0 could not log in", username);
				throw new LoginException("User "+username+" could not log in");
			}
		} else {
			log.error("A user is already logged in :(");
		}
	}
	
	/**
	 * logout the user and login the anonymous user instead
	 */
	public synchronized void logout() {
//		if(!Identity.instance().getCredentials().equals("anonymous")) {
//			UserService userService = (UserService) Component.getInstance("userService");
//			try {
//				if(!userService.userExists("anonymous")) {
//					userService.createUser("anonymous", "noNeed4Pass");
//				}
//			} catch (UserExistsException e) {
//				// it's ok if the anonymous user already exists
//			}
			Identity.instance().logout();
//			Identity.instance().getCredentials().setUsername("anonymous");
//			Identity.instance().getCredentials().setPassword("noNeed4Pass");
//			Identity.instance().login();
//		}
	}
}
