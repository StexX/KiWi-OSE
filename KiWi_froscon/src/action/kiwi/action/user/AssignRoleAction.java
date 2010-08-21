/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 *
 */
package kiwi.action.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;

import kiwi.api.group.GroupService;
import kiwi.api.role.RoleService;
import kiwi.api.user.UserService;
import kiwi.model.user.Group;
import kiwi.model.user.User;

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
 * Assigns global permissions or roles to users and groups.
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Name("assignRoleAction")
@Scope(ScopeType.PAGE)
//@Transactional
public class AssignRoleAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;

	/* all users that exist and do not yet have roles assigned */
	private List<User> allUsers;
	
	/* the user from allUsers who has been selected to be 
	 * added to the usersWithRoles */
	private User selectedUser;
	
	/* all groups that exist */
	private List<Group> allGroups;
	
	/* the group from allGroups who has been selected to be 
	 * added to the groupsWithRoles */
	private Group selectedGroup;

	/* users who already have roles assigned */
	private List<UsersWithRoles> usersWithRoles;
	
	/* groups who already have roles assigned */
	private List<GroupsWithRoles> groupsWithRoles;
	
	@In
	private UserService userService;
	
	@In
	private GroupService groupService;
	
	@In
	private RoleService roleService;
	
	@In
	private EntityManager entityManager;
	
	@In
	private FacesMessages facesMessages;
	
	/**
	 * called if the page is rendered for the first time
	 */
	@Create
	public void init() {
		usersWithRoles = new ArrayList<UsersWithRoles>();
		groupsWithRoles = new ArrayList<GroupsWithRoles>();
		
		initUsersWithRoles();
		initGroupsWithRoles();

		allUsers = userService.getUsers();
		for(UsersWithRoles uwr : usersWithRoles) {
			allUsers.remove(uwr.getUser());
		}
		allGroups = groupService.getGroups();
		for(GroupsWithRoles gwr : groupsWithRoles) {
			allGroups.remove(gwr.getGroup());
		}
	}
	
	private void initGroupsWithRoles() {
		List<Group> admin;
		List<Group> read;
		List<Group> write;
		List<Group> createKSpace;
		
		Set<Group> groups = new HashSet<Group>();
		groups.addAll(admin = roleService.getGroupsInRole("admin"));
		groups.addAll(read = roleService.getGroupsInRole("read"));
		groups.addAll(write = roleService.getGroupsInRole("write"));
		groups.addAll(createKSpace = roleService.getGroupsInRole("createKSpace"));
		
		for(Group g : groups) {
			GroupsWithRoles gwr = new GroupsWithRoles();
			gwr.setGroup(g);
			
			gwr.setDisplayname(g.getName());
			gwr.setAdmin(admin.contains(g));
			gwr.setRead(read.contains(g));
			gwr.setWrite(write.contains(g));
			gwr.setCreateKnowledgeSpace(createKSpace.contains(g));
			
			groupsWithRoles.add(gwr);
		}
	}
	
	/**
	 * initialises the arranged users list and sets the 
	 * access rights of each user in the front end
	 */
	private void initUsersWithRoles() {
		List<User> admin;
		List<User> read;
		List<User> write;
		List<User> createKSpace;
		Set<User> users = new HashSet<User>();
		users.addAll(admin = roleService.getUsersInRole("admin"));
		users.addAll(read = roleService.getUsersInRole("read"));
		users.addAll(write = roleService.getUsersInRole("write"));
		users.addAll(createKSpace = roleService.getUsersInRole("createKSpace"));
		users.add(userService.getAnonymousUser());
		
		for(User u : users) {
			UsersWithRoles au = new UsersWithRoles();
			au.setUser(u);
			au.setDisplayname(u.getFirstName() + " " + u.getLastName());
			au.setAdmin(admin.contains(u));
			au.setRead(read.contains(u));
			au.setWrite(write.contains(u));
			au.setCreateKnowledgeSpace(createKSpace.contains(u));
			
			usersWithRoles.add(au);
		}
	}
	
	/**
	 * stores and adds roles to the users
	 */
	public void store() {
		
		entityManager.setFlushMode(FlushModeType.AUTO);
		
		Set<User> excludedUsers = new HashSet<User>();

		log.info("Storing users #0 ", usersWithRoles);
		for(UsersWithRoles au : usersWithRoles) {
			User u = au.getUser();
			
			if(!entityManager.contains(u) && u.getId() != null) {
				u = entityManager.merge(u);
			}
			
			if(au.isAdmin()) {
				if(!userService.hasRole(u, "admin"))
					if(!userService.addRole(u, "admin"))
						log.error("Problem while storing role 'admin' for user #0 ", u.getLogin());
			} else {
				if(userService.hasRole(u, "admin"))
					userService.removeRole(u, "admin");
			}
			if(au.isWrite()) {
				if(!userService.hasRole(u, "write"))
					if(!userService.addRole(u, "write"))
						log.error("Problem while storing role 'write' for user #0 ", u.getLogin());
			} else {
				if(userService.hasRole(u, "write"))
					userService.removeRole(u, "write");
			}
			if(au.isRead()) {
				if(!userService.hasRole(u, "read"))
					if(!userService.addRole(u, "read"))
						log.error("Problem while storing role 'read' for user #0 ", u.getLogin());
			} else {
				if(userService.hasRole(u, "read"))
					userService.removeRole(u, "read");
			} 
			if(au.isCreateKnowledgeSpace()) {
				if(!userService.hasRole(u, "createKSpace"))
					if(!userService.addRole(u, "createKSpace"))
						log.error("Problem while storing role 'createKSpace' for user #0 ", u.getLogin());
			} else {
				if(userService.hasRole(u, "createKSpace"))
					userService.removeRole(u, "createKSpace");
			}
			if(u.getId() == null) {
				entityManager.persist(u);
			}
//			entityManager.flush();
			excludedUsers.add(u);
		}
		
		log.info("Storing groups #0 ", groupsWithRoles);
		for(GroupsWithRoles gwr : groupsWithRoles) {
			Group g = gwr.getGroup();
			
			if(!entityManager.contains(g) && g.getId() != null) {
				g = entityManager.merge(g);
			}
			
			if(gwr.isAdmin()) {
				if(!groupService.hasRole(g, "admin")) {
					groupService.addRoleExcludeUsers(g, "admin", excludedUsers);
				}
			} else {
				if(groupService.hasRole(g, "admin")) {
					groupService.removeRoleExcludeUsers(g, "admin", excludedUsers);
				}
			}
			if(gwr.isWrite()) {
				if(!groupService.hasRole(g, "write")) {
					groupService.addRoleExcludeUsers(g, "write", excludedUsers);
				}
			} else {
				if(groupService.hasRole(g, "write")) {
					groupService.removeRoleExcludeUsers(g, "write", excludedUsers);
				}
			}
			if(gwr.isRead()) {
				if(!groupService.hasRole(g, "read")) {
					groupService.addRoleExcludeUsers(g, "read", excludedUsers);
				}
			} else {
				if(groupService.hasRole(g, "read")) {
					groupService.removeRoleExcludeUsers(g, "read", excludedUsers);
				}
			} 
			if(gwr.isCreateKnowledgeSpace()) {
				if(!groupService.hasRole(g, "createKSpace")) {
					groupService.addRoleExcludeUsers(g, "createKSpace", excludedUsers);
				}
			} else { 
				if(groupService.hasRole(g, "createKSpace")) {
					groupService.removeRoleExcludeUsers(g, "createKSpace", excludedUsers);
				}
			}
			
			if(g.getId() == null) {
				entityManager.persist(g);
			}
		}
		
//		entityManager.flush();
		
		facesMessages.add("Global permissions successfully stored");
	}
	
	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}
	
	public List<User> getAllUsers() {
		return allUsers;
	}
	
	public void setAllGroups(List<Group> allGroups) {
		this.allGroups = allGroups;
	}

	public List<Group> getAllGroups() {
		return allGroups;
	}

	public List<UsersWithRoles> getUsersWithRoles() {
		return usersWithRoles;
	}
	
	/**
	 * adds a new user to the list of usersWithRoles
	 */
	public void addToUsersWithRoles() {
		log.info("called addToArrangedUsers(). selectedUser is #0 ", selectedUser);
		if(selectedUser != null) {
			UsersWithRoles userBean = new UsersWithRoles();
			String name = selectedUser.getFirstName() + " " + selectedUser.getLastName();
			userBean.setUser(selectedUser);
			userBean.setDisplayname(name);
			userBean.setAdmin(false);
			userBean.setRead(true);
			userBean.setWrite(false);
			userBean.setCreateKnowledgeSpace(false);
			usersWithRoles.add(userBean);
		}
	}
	
	public String delUserWithRoles(UsersWithRoles uwr) {
		
		User u = uwr.getUser();
		log.info("Removing user #0 ", u);
		
		if(userService.hasRole(u, "admin"))
			userService.removeRole(u, "admin");
		if(userService.hasRole(u, "write"))
			userService.removeRole(u, "write");
		if(userService.hasRole(u, "read"))
			userService.removeRole(u, "read");
		if(userService.hasRole(u, "createKSpace"))
			userService.removeRole(u, "createKSpace");
		entityManager.persist(u);
//		entityManager.flush();
		
		return "/admin/permissions.xhtml";
	}
	
	public String delGroupWithRoles(GroupsWithRoles gwr) {
		Set<User> excludedUsers = new HashSet<User>();
		Group g = gwr.getGroup();
		log.info("Removing group #0 ", g);
		for(UsersWithRoles au : usersWithRoles) {
			User u = au.getUser();
			excludedUsers.add(u);
		}
		
		if(groupService.hasRole(g, "admin")) {
			groupService.removeRoleExcludeUsers(g, "admin", excludedUsers);
		}
		if(groupService.hasRole(g, "write")) {
			groupService.removeRoleExcludeUsers(g, "write", excludedUsers);
		}
		if(groupService.hasRole(g, "read")) {
			groupService.removeRoleExcludeUsers(g, "read", excludedUsers);
		}
		if(groupService.hasRole(g, "createKSpace")) {
			groupService.removeRoleExcludeUsers(g, "createKSpace", excludedUsers);
		}
		
		entityManager.persist(g);
//		entityManager.flush();
		
		return "/admin/permissions.xhtml";
	}

	public List<GroupsWithRoles> getGroupsWithRoles() {
		return groupsWithRoles;
	}
	
	/**
	 * adds a new user to the list of usersWithRoles
	 */
	public void addToGroupsWithRoles() {
		log.info("called addToGroupsWithRoles(). selectedGroup is #0 ", selectedGroup);
		if(selectedGroup != null) {
			GroupsWithRoles groupBean = new GroupsWithRoles();
			String name = selectedGroup.getName();
			groupBean.setGroup(selectedGroup);
			groupBean.setDisplayname(name);
			groupBean.setAdmin(false);
			groupBean.setRead(true);
			groupBean.setWrite(false);
			groupBean.setCreateKnowledgeSpace(false);
			groupsWithRoles.add(groupBean);
		}
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public Group getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(Group selectedGroup) {
		this.selectedGroup = selectedGroup;
	}
	
}
