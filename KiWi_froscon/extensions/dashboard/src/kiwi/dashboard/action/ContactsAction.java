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
package kiwi.dashboard.action;

import java.io.Serializable;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import kiwi.api.user.UserService;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * A component supporting the management of contacts in the user profile of a user. Used by the 
 * Dashboard contacts.xhtml view.
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.dashboard.contactsAction")
@Scope(ScopeType.PAGE)
public class ContactsAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	@Out
	private User currentUser;
	
	@In
	private EntityManager entityManager;
	
	@In
	private UserService userService;
	
	@Logger
	private Log log;
	
	private String selectedUser;
	
	
	private List<String> allUsers;
	
	@In 
    FacesMessages facesMessages;
	
	private List<User> friends;
	
	public List<User> getFriends() {
		if(friends == null) {
			if(!entityManager.contains(currentUser)) {
				currentUser = entityManager.find(User.class, currentUser.getId());
			}
			friends = new LinkedList<User>(currentUser.getFriends());
			Collections.sort(friends, 
				new Comparator<User>() {
					@Override
					public int compare(User o1, User o2) {
						return Collator.getInstance().compare(o1.getFirstName()+" "+o1.getLastName(), o2.getFirstName()+" "+o2.getLastName());
					}
					
				}
			);
		}
		
		return friends;
		
	}
	
	/**
	 * Retrieve a list of all users that are not yet contacts of currentUser. In the future, 
	 * this should not list all users but only those matching the search criteria.
	 * 
	 * @return
	 */
	public List<String> getAllUsers() {
		if(allUsers == null) {
			if(!entityManager.contains(currentUser)) {
				currentUser = entityManager.find(User.class, currentUser.getId());
			}
			List<User> users = userService.getUsers();
			users.removeAll(currentUser.getFriends());
			
			allUsers = new LinkedList<String>();
			for(User user : users) {
				allUsers.add(user.getLogin());
			}
		}
		log.info(allUsers.size());
		return allUsers;
	}
	
	
	
	/**
	 * Add a friend to the list of friends of the current user
	 */
	public void addFriend() {
		User friend = userService.getUserByLogin(selectedUser);
		log.info("friendlogin: "+friend.getLogin());
		log.info("currentuserLogin: "+currentUser.getLogin());
		becomeFriend(friend.getLogin());
	}
	
	
	/**
	 * Add a friend to the list of friends of the current user
	 */
	public void becomeFriend(String login) {
		User friend = userService.getUserByLogin(login);
		if(!entityManager.contains(currentUser)) {
			currentUser = entityManager.find(User.class, currentUser.getId());
			entityManager.refresh(currentUser);
		}
		if(friend != null && currentUser.getId()!=friend.getId()) {
			currentUser.getFriends().add(friend);
			entityManager.persist(currentUser);
			entityManager.flush();			
			allUsers = null;
			friends  = null;
			selectedUser=null;
			Events.instance().raiseEvent("activity.addFriend", currentUser, friend);
			facesMessages.add("You have a new kiwi friend!");
		}
	}
	
	/**
	 * Add a friend to the list of friends of the current user
	 */
	public boolean isFriend(String login) {
		User friend = userService.getUserByLogin(login);
		return isFriend(friend);
	}	
	
	/**
	 * Add a friend to the list of friends of the current user
	 */
	public boolean isFriend(User user) {
		boolean isFriend = false;
		
		if(!entityManager.contains(user)) {
			user = entityManager.find(User.class, user.getId());
		}

		if(user != null) {
			if(user!=currentUser) {
				currentUser = entityManager.find(User.class, currentUser.getId());
				isFriend = currentUser.getFriends().contains(user);
			}
		}
		
		return isFriend;
	}	
	
	
	/**
	 * Remove a friend from the list of friends of the current user
	 */
	public void removeFriend(User friend) {
		if(friend != null) {
			currentUser.getFriends().remove(friend);
			allUsers = null;
			friends  = null;
			selectedUser=null;
			Events.instance().raiseEvent("activity.removeFriend", currentUser, friend);
		}
		
	}

	public String getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(String selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	
}
