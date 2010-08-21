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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import kiwi.api.group.GroupService;
import kiwi.api.role.RoleService;
import kiwi.api.security.PermissionService;
import kiwi.api.user.UserService;
import kiwi.exception.PermissionException;
import kiwi.model.content.ContentItem;
import kiwi.model.security.Permission;
import kiwi.model.user.Group;
import kiwi.model.user.Role;
import kiwi.model.user.User;
import kiwi.util.KiWiCollections;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * Grants permissions to individual contentItems. Users who already
 * have permissions on the ContentItem (currently 'read', 'write',
 * 'editPermissions', 'comment', 'tag', 'annotate' and 'delete')
 * will be hold in the usersWithPermissions list.
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Name("grantPermissionAction")
@Scope(ScopeType.PAGE)
//@Transactional
public class GrantPermissionAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	/* all users that exist and do not yet have permissions on
	 * the currentContentItem */
	private List<User> allUsers;
	
	/* the user from allUsers who has been selected to be 
	 * added to the usersWithPermissions */
	private User selectedUser;
	
	/* all groups that exist */
	private List<Group> allGroups;
	
	/* the group from allGroups who has been selected to be 
	 * added to the groupsWithPermissions */
	private Group selectedGroup;
	
	/* users who already hold permissions on the currentContentItem */
	private List<UsersWithPermissions> usersWithPermissions;
	
	/* groups who already hold permissions on the currentContentItem */
	private List<UsersWithPermissions> groupsWithPermissions;
	
	@In
	private UserService userService;
	
	@In
	private GroupService groupService;
	
	@In
	private RoleService roleService;
	
	@In
	private PermissionService permissionService;
	
	@In
	private FacesMessages facesMessages;
	
	@In(create=true)
	private ContentItem currentContentItem;
	
	/* TODO: remove asap */
	@In
	private EntityManager entityManager;
	
	private boolean globalRead;
	private boolean globalWrite;
	
	/**
	 * called if the page is rendered for the first time
	 */
	@Create
	public void init() {
		usersWithPermissions = new ArrayList<UsersWithPermissions>();
		
		initUsersWithPermissions();
		allUsers = userService.getUsers();
		for(UsersWithPermissions uwp : usersWithPermissions) {
			allUsers.remove(uwp.getUser());
		}
		setAllGroups(groupService.getGroups());
	}
	
	/**
	 * initialises the usersWithPermissions list and sets the 
	 * access rights of each user in the front end
	 */
	private void initUsersWithPermissions() {
		
		Set<User> users = KiWiCollections.toSet(permissionService.getPermissionOwnerByTarget(currentContentItem));
		
		/* for each user who has permissions on the currentContentItem,
		 * we determine which permissions he has */
		for(User u : users) {
			UsersWithPermissions uwp = new UsersWithPermissions();
			uwp.setUser(u);
			uwp.setDisplayname(u.getFirstName() + " " + u.getLastName());
			
			List<Permission> permissions = permissionService
				.getPermissionsByRecipientAndTarget(u, currentContentItem);
			
			for(Permission p : permissions) {
				if(p.getAction().equals("read")) {
					uwp.setRead(true);
				} else if(p.getAction().equals("write")) {
					uwp.setWrite(true);
				} else if(p.getAction().equals("editPermissions")) {
					uwp.setEditPermissions(true);
				} else if(p.getAction().equals("annotate")) {
					uwp.setAnnotate(true);
				} else if(p.getAction().equals("delete")) {
					uwp.setDelete(true);
				} else if(p.getAction().equals("tag")) {
					uwp.setTag(true);
				} else if(p.getAction().equals("comment")) {
					uwp.setComment(true);
				}
			}
			usersWithPermissions.add(uwp);
		}
		
		if(permissionService.hasPermission(
				roleService.getRoleByName("read"), currentContentItem, "read"))
			globalRead = true;
		else
			globalRead = false;
		
		if(permissionService.hasPermission(
				roleService.getRoleByName("write"), currentContentItem, "write"))
			globalWrite = true;
		else
			globalWrite = false;
	}

	/**
	 * grants or removes permissions according to the action flags
	 * set in the user interface
	 */
	public void store() {
		log.info("Storing users #0 ", usersWithPermissions);
		for(UsersWithPermissions awp : usersWithPermissions) {
			User u = awp.getUser();
			try {
				if (awp.isRead())
					permissionService.grantPermission(u, currentContentItem,
							"read");
				else
					permissionService.removePermission(u, currentContentItem,
							"read");
				if (awp.isWrite())
					permissionService.grantPermission(u, currentContentItem,
							"write");
				else
					permissionService.removePermission(u, currentContentItem,
							"write");
				if (awp.isDelete())
					permissionService.grantPermission(u, currentContentItem,
							"delete");
				else
					permissionService.removePermission(u, currentContentItem,
							"delete");
				if (awp.isEditPermissions())
					permissionService.grantPermission(u, currentContentItem,
							"editPermissions");
				else
					permissionService.removePermission(u, currentContentItem,
							"editPermissions");
				if (awp.isAnnotate())
					permissionService.grantPermission(u, currentContentItem,
							"annotate");
				else
					permissionService.removePermission(u, currentContentItem,
							"annotate");
				if (awp.isComment())
					permissionService.grantPermission(u, currentContentItem,
							"comment");
				else
					permissionService.removePermission(u, currentContentItem,
							"comment");
				if (awp.isTag())
					permissionService.grantPermission(u, currentContentItem,
							"tag");
				else
					permissionService.removePermission(u, currentContentItem,
							"tag");
			} catch (PermissionException e) {
				e.printStackTrace();
			}
		}
		
		try {
			Role r = roleService.getRoleByName("read");
			if(globalRead) {
				permissionService.grantPermission(r, currentContentItem, "read");
			} else {
				permissionService.removePermission(r, currentContentItem, "read");
			}
		} catch (PermissionException e1) {
			e1.printStackTrace();
		}

		try {
			Role r = roleService.getRoleByName("write");
			if(globalWrite) {
				permissionService.grantPermission(r, currentContentItem, "write");
			} else {
				permissionService.removePermission(r, currentContentItem, "write");
			}
		} catch (PermissionException e1) {
			e1.printStackTrace();
		}
		
//		entityManager.flush();
		facesMessages.add("Successfully stored");
	}
	
	/**
	 * returns usersWithPermissions
	 * @return
	 */
	public List<UsersWithPermissions> getUsersWithPermissions() {
		return usersWithPermissions;
	}
	
	/**
	 * adds a new user to the list of arranged users
	 */
	public void addToUsersWithPermissions() {
		if(selectedUser != null) {
			UsersWithPermissions userBean = new UsersWithPermissions();
			String name = selectedUser.getFirstName() + " " + selectedUser.getLastName();
			userBean.setUser(selectedUser);
			userBean.setDisplayname(name);
			userBean.setRead(true);
			userBean.setWrite(false);
			userBean.setEditPermissions(false);
			userBean.setAnnotate(true);
			userBean.setTag(true);
			userBean.setDelete(false);
			userBean.setComment(true);
			usersWithPermissions.add(userBean);
		}
	}
	
	public void setAllGroups(List<Group> allGroups) {
		this.allGroups = allGroups;
	}

	public List<Group> getAllGroups() {
		return allGroups;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedGroup(Group selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	public Group getSelectedGroup() {
		return selectedGroup;
	}

	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}

	public List<User> getAllUsers() {
		return allUsers;
	}

	public void setGlobalRead(boolean globalRead) {
		this.globalRead = globalRead;
	}

	public boolean isGlobalRead() {
		return globalRead;
	}
	
	public void setGlobalWrite(boolean globalWrite) {
		this.globalWrite = globalWrite;
	}

	public boolean isGlobalWrite() {
		return globalWrite;
	}
}
