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

import kiwi.api.group.GroupService;
import kiwi.api.user.UserService;
import kiwi.exception.GroupExistsException;
import kiwi.model.user.Group;
import kiwi.model.user.Role;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.security.management.JpaIdentityStore;

/**
 * This action class is supposed to 
 * generate an admin user or to grant admin privileges 
 * if a user with the same identity already exists 
 * (the second is on the TODO list).
 * 
 * @author Stephanie Stroka 
 * (stephanie.stroka@salzburgresearch.at)
 *
 */
@Name("adminSetupAction")
@Scope(ScopeType.SESSION)
@AutoCreate
//@Transactional
public class AdminSetupAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private static Log log;

    @In
    private UserService userService;
	
    @In
    private GroupService groupService;
    
    // the created user
	private User user;

	private String password;
	
	// the password for validation
	private String validatePassword;
	
	@In
	private IdentityManager identityManager;

//	@In
//	private KiWiEntityManager kiwiEntityManager;
//	
//	@In
//	private EntityManager entityManager;
	
	@In 
    private FacesMessages facesMessages;
	
	@Create
	@Begin(nested=true)
	public void begin() {
		user = new User();
	}
	
	@Observer(value=JpaIdentityStore.EVENT_PRE_PERSIST_USER_ROLE, create=false)
	public void roleCreated(Role role) {
		if(role != null && role.getName() != null) {
			try {
				Group group = groupService.createGroup(role.getName());
			} catch (GroupExistsException e) {
				e.printStackTrace();
			}
    	}
	}
	
	public String register() {
		final String login = user.getLogin();
		
		if(userService.userExists(login)) {
			facesMessages.add("User #{registerAction.user.login} already exists.");
			log.info("not creating already existing KiWi user: #0",login);
			return null;
		} else if(!password.equals(validatePassword)) {
			facesMessages.add("Passwords do not match.");
			log.info("passwords did not match for KiWi user: #0",login);
			return null;			
		} else {
			if(identityManager.getIdentityStore().createUser(login, password)) {
				log.info("Created new user #0 ", login);
				identityManager.getIdentityStore().createRole("admin");
				identityManager.getIdentityStore().grantRole(login, "admin");
			}

			log.info("created new KiWi user: #0" , user.getLogin());
			return "success";
		}
	}

	@End
    public void login() {
//		Identity.instance().getCredentials().setUsername(user.getLogin());
//		Identity.instance().getCredentials().setPassword(password);
//		
//		Identity.instance().login();		    	
    }

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
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
}
