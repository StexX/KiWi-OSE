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
package kiwi.action.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.LoginException;

import kiwi.api.config.ConfigurationService;
import kiwi.api.group.GroupService;
import kiwi.api.personalization.UserPreferenceService;
import kiwi.api.user.MailService;
import kiwi.api.user.UserService;
import kiwi.exception.GroupExistsException;
import kiwi.exception.RegisterException;
import kiwi.exception.UserExistsException;
import kiwi.model.user.Group;
import kiwi.model.user.User;
import kiwi.service.user.IdentityManagerService;

import org.hibernate.validator.Email;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.RunAsOperation;

/**
 * The RegisterAction is triggered by the register 
 * form and creates a user who is new to the KiWi platform
 * 
 * @author Sebastian Schaffert, Stephanie Stroka
 *								(stephanie.stroka@salzburgresearch.at)
 *
 */
@Name("registerAction")
@Scope(ScopeType.SESSION)
//@Transactional
public class RegisterAction implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	protected static Log log;

    @In
    protected UserService userService;
	
    @In
    protected GroupService groupService;
	
    @In
	private UserPreferenceService userPreferenceService;	    
    
    protected String login, firstName, lastName, password, validatePassword;
    
    @Email
    protected String email;
    
    @In(create=true)
    private MailService mailService;
    
    @In
    private ConfigurationService configurationService;
    
    @In
    private IdentityManagerService identityManagerService;
    
	@In 
    FacesMessages facesMessages;
	
	@In
	protected Map<String, String> messages;

	@Create
    @Begin(nested=true)
    public void begin() {
    }

    
	public String register() {
		if(userService.userExists(login)) {
			facesMessages.add("User #0 already exists", login);
			log.info("not creating already existing KiWi user: #0",login);
			return null;
		} else if(!password.equals(validatePassword)) {
			facesMessages.add("Passwords do not match.");
			log.info("passwords did not match for KiWi user: #0",login);
			return null;			
		} else {
			new RunAsOperation(){
				@Override
				public void execute()  {

					try {
						createUser();
					} catch (UserExistsException e) {
						facesMessages.add("User #0 already exists", login);
						log.info("not creating already existing KiWi user: #0",login);
						setSuccess(false);
					} catch (RegisterException e) {
						facesMessages.add("#0", e.getMessage());
						log.info("not creating KiWi user: #0",login);
						setSuccess(false);
					}
				}
			}.addRole("admin").run();

			if(success) {
				Set<String> s = new HashSet<String>();
				s.add(email);
				mailService.sendMail(s, "Welcome to the KiWi Platform", 
						"Welcome " + login + ", \n\nthanks for registering at " + configurationService.getBaseUri() + ". \nYour password has been set to " + password);
				log.info("created new KiWi user: #0" , login);
				
				try {
					identityManagerService.login(login,password);
				} catch(LoginException e) {
					facesMessages.add(e.getMessage());
					return null;
				}
				return "success";
			} else {
				return null;
			}
		}
	}

	private boolean success;
	protected void setSuccess(boolean success) {
		this.success = success;
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
	 * @return the validatePassword
	 */
	public String getValidatePassword() {
		return validatePassword;
	}


	/**
	 * @param validatePassword the validatePassword to set
	 */
	public void setValidatePassword(String validatePassword) {
		this.validatePassword = validatePassword;
	}


	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}


	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * @throws UserExistsException
	 * @throws RegisterException 
	 */
	protected void createUser() throws UserExistsException, RegisterException {
		User user = userService.createUser(login, firstName, lastName, password);
		user.setEmail(email);
		Group users = null;
		if((users = groupService.getGroupByName("users")) == null) {
			try {
				users = groupService.createGroup("users");
			} catch (GroupExistsException e) {
				e.printStackTrace();
			}
		} 
		groupService.addUserToGroup(users, user);
		groupService.store(users);
		user.setUserPreference(userPreferenceService.createInitialUserPreference(user));
		setSuccess(true);
	}
		
}

