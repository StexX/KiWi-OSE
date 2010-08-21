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
package kiwi.action.setup;

import java.io.Serializable;

import javax.security.auth.login.LoginException;

import kiwi.api.config.ConfigurationService;
import kiwi.api.group.GroupService;
import kiwi.api.perspectives.PerspectiveService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.config.Configuration;
import kiwi.exception.GroupExistsException;
import kiwi.exception.UserDoesNotExistException;
import kiwi.exception.UserExistsException;
import kiwi.exception.UserUpdateException;
import kiwi.model.perspective.Perspective;
import kiwi.model.user.Group;
import kiwi.model.user.User;
import kiwi.service.config.ConfigurationServiceImpl;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.management.IdentityManager;

/**
 * SetupUserAction - manages the creation of the initial users of the KiWi system (admin and anonymous).
 *
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.setup.setupUserAction")
@Scope(ScopeType.PAGE)
public class SetupUserAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@In 
    FacesMessages facesMessages;

	@In
	private UserService userService;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private PerspectiveService perspectiveService;
	
	@In
	private GroupService groupService;
	
	@In
	private TripleStore tripleStore;
	
	private String firstName, lastName, password, verify;
	
	/**
	 * Creates the admin user by querying it's (hopefully) already existing dummy 
	 * and changing the data to the given field properties
	 * 
	 * @throws UserDoesNotExistException
	 * @throws LoginException
	 * @throws UserExistsException
	 * @throws UserUpdateException
	 */
	private void createAdminUser() throws UserDoesNotExistException, LoginException, UserExistsException, UserUpdateException {
		log.info("creating user (#0 #1) ...", firstName,lastName);
		
		Configuration config_adminName = configurationService.getConfiguration("kiwi.identity.admin.name");
		Configuration config_adminPW = configurationService.getConfiguration("kiwi.identity.admin.password");
		
		String admin = config_adminName.getStringValue();
		String adminPW = config_adminPW.getStringValue();
		
		
		Identity.instance().getCredentials().setUsername(admin);
		Identity.instance().getCredentials().setPassword(adminPW);
		log.info("Admin user login status #0 ", Identity.instance().login());
		
		User u = userService.getUserByLogin(admin);
		if(u == null) {
			facesMessages.add(
					"Problem while creating the admin user. Dummy admin does not exist.");
			throw new UserDoesNotExistException("The admin user was expected to exist, " +
					"but it doesn't. Seems to be a problem in the Configuration process");
		}
		if(!password.equals(verify)) {
			facesMessages.add("Passwords do not match.");
			log.error("passwords did not match for KiWi user: admin");
			throw new LoginException("The given passwords didn't match");
		}
		
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setPassword(password);
		
		IdentityManager.instance().changePassword(admin, password);
		u.setResource(tripleStore.createUriResource(
					configurationService.getBaseUri()+"/user/admin"));
		
		userService.addRole(u, "admin");
		userService.addRole(u, "write");
		userService.addRole(u, "read");
		userService.addRole(u, "createKSpace");
		
		Group users = initUserGroup();
		Group admins = initAdminGroup();
		
		groupService.addUserToGroup(users, u);		
		groupService.addUserToGroup(admins, u);
		
		tripleStore.persist(u);
	}
	
	/**
	 * Creates the anonymous user. This is necessary, because it might happen
	 * that an asynchronous method needs the anonymous user object, but cannot
	 * create it (because of permission problems)
	 * 
	 * @throws UserDoesNotExistException
	 * @throws LoginException
	 * @throws UserExistsException
	 * @throws UserUpdateException
	 */
	private void createAnonymousUser() {
		log.info("creating anonymous user");
		
		userService.getAnonymousUser();
	}
	
	private Group initUserGroup() {
		try {
			Group g = groupService.createGroup("users");
			groupService.addRole(g, "read");
			groupService.store(g);
			return g;
		} catch (GroupExistsException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Group initAdminGroup() {
		try {
			Group g = groupService.createGroup("admins");
			groupService.addRole(g, "read");
			groupService.addRole(g, "write");
			groupService.addRole(g, "admin");
			groupService.addRole(g, "createKSpace");
			groupService.store(g);
			return g;
		} catch (GroupExistsException e) {
			e.printStackTrace();
		}
		return null;
	}

    public void doPerspectives() {
    	Perspective p_default = new Perspective("Default", "default");
    	p_default.setDescription("Generic perspective for viewing and editing all kinds of content in a wiki style.");
    	perspectiveService.addPerspective(p_default);
    	
    	Perspective p_photo = new Perspective("Photo", "photo");
    	p_photo.setDescription("Perspective for viewing and editing photos. Displays additional information, e.g. EXIF.");
    	perspectiveService.addPerspective(p_photo);
    	
    	// FIXME (doesn't work in the view):
//    	Perspective p_metadata = new Perspective("Metadata", "metadata");
//    	p_metadata.setDescription("Perspective for viewing and editing RDF metadata of the current content item (RDF annotation and datatype properties).");
//    	perspectiveService.addPerspective(p_metadata);
    }
    
	/**
	 * TODO: try out Identity.setSecurityEnabled(false)
	 * @return
	 */
	public String submit() {
	
		if(!ConfigurationServiceImpl.setupInProgress) {
			// turn off "needsSetup" flag
			ConfigurationServiceImpl.needsSetup      = false;
			ConfigurationServiceImpl.setupInProgress = true;
			
			// set the setup flag in the configuration; maybe we should do that later?
			configurationService.setConfiguration("kiwi.setup", "true");
			
			doPerspectives();
			try {
				createAdminUser();
				createAnonymousUser();
			} catch (LoginException e) {
				e.printStackTrace();
			} catch (UserDoesNotExistException e) {
				e.printStackTrace();
			} catch (UserExistsException e) {
				e.printStackTrace();
			} catch (UserUpdateException e) {
				e.printStackTrace();
			}
			
//			login();
			return "success";
		} else {
			facesMessages.add("Setup already in progress");
			return null;
		}
	}
	
	public String skip() {
		// turn off "needsSetup" flag
		ConfigurationServiceImpl.needsSetup      = false;
		
		return "/home.seam";
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the verify
	 */
	public String getVerify() {
		return verify;
	}

	/**
	 * @param verify the verify to set
	 */
	public void setVerify(String verify) {
		this.verify = verify;
	}	
	
}
