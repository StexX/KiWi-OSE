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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import kiwi.api.group.GroupService;
import kiwi.api.user.UserService;
import kiwi.exception.GroupExistsException;
import kiwi.model.user.Group;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

/**
 * A component supporting the management of groups. Used by the 
 * Dashboard groups.xhtml view.
 * 
 * @author Fred Durao
 *
 */
@Name("kiwi.dashboard.groupAction")
@Scope(ScopeType.CONVERSATION)
public class GroupAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	private GroupService groupService;

	private List<Group> myGroups;
	
	private List<Group> myCreatedGroups;
	
	private List<Group> allMyGroups;

	private String name;
	
	private List<String> allUsers;

	@In
	@Out
	private User currentUser;	
	
	@In
	private UserService userService;

	private String selectedUser;	
	
	@In
	private EntityManager entityManager;

	@In 
    FacesMessages facesMessages;
	
	
	/**
	 * Retrieve a list of all groups.
	 * 
	 * @return
	 */
	public List<Group> getAllGroups() {
		List<Group> allGroups = groupService.getGroups();
		return allGroups;
	}
	
	
	/**
	 * Create a new group
	 * @throws GroupExistsException 
	 */
//	@Factory("kiwi.dashboard.groupAction.store")
	public void store()  {
		
		try {
			Group group = groupService.createGroup(name);
			groupService.store(group);
		} catch (GroupExistsException e) {
			e.printStackTrace();
		}
		
		//TODO create activity.addGroup
		//Events.instance().raiseEvent(KiWiEvents.ACTIVITY_ADDGROUP, currentUser, group);
	}
	
	/**
	 * Add a new group to the list of groups
	 */
	@Out(value="kiwi.dashboard.groupAction.myGroups", required=false)
	public List<Group> getMyGroups() {
		myGroups = groupService.getGroupsByUser(currentUser);
		return myGroups;
	}
	
	@Out(value="kiwi.dashboard.groupAction.myCreatedGroups", required=false)
	public List<Group> getMyCreatedGroups() {
		myCreatedGroups = groupService.getGroupsByOwner(currentUser);
		return myCreatedGroups;
	}	
	
	
	@Out(value="kiwi.dashboard.groupAction.getAllMyGroups", required=false)
	public List<Group> getAllMyGroups() {
		allMyGroups = groupService.getAllMyGroups(currentUser);
		return allMyGroups;
	}		
	
	
	/**
	 * Add a user to a group
	 */
	public boolean isUserMemberOfGroup(Group group) {
		if(!entityManager.contains(currentUser)) {
			currentUser = entityManager.find(User.class, currentUser.getId());
		}
		return currentUser.getGroups().contains(group);
	}			

	
	
	/**
	 * Remove a group
	 */
	@Factory("kiwi.dashboard.groupAction.removeGroup(group)")
	public void removeGroup(Group selectedGroup) {
		myGroups.remove(selectedGroup);
		groupService.remove(selectedGroup);
		//Events.instance().raiseEvent("activity.removeGroup", currentUser, group);
	}	
	
	
	/**
	 * Retrieve a list of all users that are not yet part of the selected of currentUser.
	 * 
	 * @return
	 */
	public List<String> getAllUsers() {
		if(allUsers == null) {
			List<User> users = userService.getUsers();
			allUsers = new LinkedList<String>();
			for(User user : users) {
				allUsers.add(user.getLogin());
			}
		}
		return allUsers;
	}
	
	/**
	 * Returns a list of all users who belong to a group
	 * @param group
	 * @return
	 */
	public List<User> addUserTo(Group group) {
		List<User> users = new LinkedList<User>(group.getUsers());
		return users;
	}

	/**
	 * Returns a list of all users who belong to a group
	 * @param group
	 * @return
	 */
	public List<User> getUsersInGroup(Group group) {
		List<User> users = new LinkedList<User>(group.getUsers());
		return users;
	}
	
	
	/**
	 * Add a user to a group
	 */
	public void addToGroup(Group selectedGroup) {
		User user = userService.getUserByLogin(selectedUser);
		if(user != null && selectedGroup != null) {
				
				if(user.getGroups().contains(selectedGroup)){
					FacesMessages.instance().add("This user is already part of this group");
				}	
				else{
					groupService.addUserToGroup(selectedGroup, user);
				}
		}		
	}	
	
	/**
	 * Add a user to a group
	 */
	public void addMeToGroup(Group selectedGroup) {
		if(currentUser.getGroups().contains(selectedGroup)){
			FacesMessages.instance().add("You are already part of this group");
		}else{
			groupService.addUserToGroup(selectedGroup, currentUser);
			FacesMessages.instance().add("You are now part of this group");
		}
	}	

	/**
	 * Remove a group
	 */
	@Factory("kiwi.dashboard.groupAction.removeUserFromGroup(user,group)")
	public String removeUserFromGroup(User user, Group group) {
		List<User> users = new ArrayList<User>();
		for (User userLocal : group.getUsers()) {
			if (user!=userLocal) {
				users.add(userLocal);
			}
			
		}
		if(!entityManager.contains(group)) {
			group = entityManager.find(Group.class, group.getId());
		}
		group.setUsers(null);
		group.getUsers().addAll(users);
		return "/dashboard/groups";
	}		
	
		

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(String selectedUser) {
		this.selectedUser = selectedUser;
	}

}
