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

package kiwi.context;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.config.ConfigurationService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

/**
 * This component provides a factory for loading/returning the current user of
 * the KiWi system. It is one of the components providing the context of the
 * KiWi system, which consists of the current user, the current content item,
 * and the current perspective.
 * <p>
 * The CurrentUserFactory is session-scoped, because the current user is
 * attached to the session.
 * <p>
 * This class provides methods for retrieving the current user in long lasting
 * seam contexts. cf.
 * http://wiki.jboss.org/wiki/SeamEntityHomeForLongRunningContexts
 * 
 * @author Sebastian Schaffert
 * 
 */
@Name("currentUserFactory")
@Scope(ScopeType.SESSION)
@AutoCreate
@Synchronized
//@Transactional
// avoid concurrent access to factory loader
public class CurrentUserFactory implements Serializable {

	/** the serial number for serialisation. */
	private static final long serialVersionUID = -3578093739211371048L;

	/**
	 * Inject a Seam logger for logging purposes inside this component.
	 */
	@Logger
	private static Log log;

	/**
	 * The currently active identity, injected by the Seam system.
	 */
	@In(required = false)
	private Identity identity;

	
	/**
	 * The entity manager used by this KiWi system. We could use
	 * kiwiEntityManager, put for the purpose of just loading users, the
	 * ordinary entityManager is sufficient and more efficient.
	 */
	@In
	private EntityManager entityManager;

	/**
	 * The triple store used by this KiWi system. Used for loading additional
	 * user data that is not persisted in the database.
	 */
	@In
	private TripleStore tripleStore;

	/**
	 * The configuration service of the KiWi system. Used for retrieving the
	 * base URI of the system for constructing new users.
	 */
	@In(value = "configurationService")
	ConfigurationService configurationService;

	@In(create=true)
	private UserService userService;

	/**
	 * The currently active user. Outjected to the session so that other
	 * components can access it easily. The factory method initUser below takes
	 * care of initialising this value.
	 */
	@Out(required = false)
	private User currentUser;

	/**
	 * A factory method for initialising the current user.
	 * 
	 * @return
	 */
	@Factory(value = "currentUser")
	public void initUser() {
		
		if (currentUser == null) {
				if (identity != null && identity.isLoggedIn() ) {
					// when the user is logged in, load him using the user
					// principal of the identity
					Query q = entityManager.createNamedQuery("currentUserFactory.getUserByLogin");
					/* changed from getPrincipal() to getCredential() to fix a bug. I hope that doesn't break anything else */
					q.setParameter("login", identity.getCredentials().getUsername());

					try {
						currentUser = (User) q.getSingleResult();
						tripleStore.refresh(currentUser, false);

						log.info("loaded user #0 from database", identity
								.getCredentials().getUsername());
					} catch (NoResultException ex) {
						// if user does not exist, return anonymous user
						// TODO: this case should really not happen
						log.error("user #0 was logged in but could not be loaded from the database",identity.getCredentials().getUsername());
//						new RunAsOperation(){
//							@Override
//							public void execute() {
								currentUser = userService.getAnonymousUser();
//							}
//						}.addRole("admin").run();
					}
				} else {
					// when the user is not logged in, return the anonymous user
//					new RunAsOperation(){
//						@Override
//						public void execute() {
						currentUser = userService.getAnonymousUser();
						log.info("No user logged in. Loading anonymous user.");
							currentUser = userService.getAnonymousUser();
//						}
//					}.addRole("admin").run();
				}
		}
	}
	
	/**
	 * Sets the currentUser to the given user. 
	 * @param user
	 */
	public void setCurrentUser(User user) {
		currentUser = user;
	}

	/**
	 * Refresh the currently active user by setting "currentUser" to null so
	 * that a reload is forced.
	 */
	public void refresh() {
		currentUser = null;
	}

	/**
	 * An event listener that refreshes the currentUser when a user logs in
	 * The event observer is defined in components.xml:
	 * 
	 * <event type="org.jboss.seam.security.loginSuccessful">
   	 * 		<action execute="#{currentUserFactory.refreshOnLogin()}" />
   	 * 		<action execute="#{activityLoggingService.userLogin(currentUser)}" />
     * 		<!--  <action execute="#{redirect.returnToCapturedView}"/> -->
   	 * </event>
   	 * 
	 */
	public void refreshOnLogin() {
		forceRefresh();
	}
	
	public void forceRefresh(){
		refresh();
		initUser();
	}
}
