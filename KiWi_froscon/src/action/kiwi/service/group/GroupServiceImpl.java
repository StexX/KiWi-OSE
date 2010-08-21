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

package kiwi.service.group;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.group.GroupServiceLocal;
import kiwi.api.group.GroupServiceRemote;
import kiwi.api.role.RoleService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.exception.GroupExistsException;
import kiwi.model.Constants;
import kiwi.model.user.Group;
import kiwi.model.user.Role;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Rolf Sint
 * 
 */
@Stateless
@Name("groupService")
@AutoCreate
@Scope(ScopeType.STATELESS)
public class GroupServiceImpl implements GroupServiceLocal, GroupServiceRemote {

	@Logger
	private static Log log;
	
	@In(create=true)
	private User currentUser;	

	// the entity manager used by this KiWi system
	@In
	private EntityManager entityManager;
	
	@In
	private TripleStore tripleStore;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private UserService userService;

	@In
	private RoleService roleService;
	
	public Group createGroup(String name) throws GroupExistsException {
		if(groupExists(name)) {
			throw new GroupExistsException("Group '"+name+"' already exists");
		}
		Group group = new Group(name);
		createResource(group);
		group.setOwner(currentUser);
		
		contentItemService.updateTitle(group.getResource().getContentItem(), group.getName());
		group.getResource().getContentItem().setAuthor(currentUser);
		
		entityManager.persist(group);
		return group;
	}

	/** 
	 * Groups are content items and they are never deleted from the database, however, they are set as deleted.
	 * Users cannot create groups with the same name if they are not deleted.
	 * group.setId(testGroup.getId()); because testGroup is a transient object which cannot be persisted.
	 * (non-Javadoc)
	 * @see kiwi.api.group.GroupService#store(kiwi.model.user.Group)
	 * 
	 */
	public void store(Group group)  {
//		group.getResource().getContentItem().setTitle(group.getName());	
		entityManager.persist(group);
	}

	private void createResource(Group group){
		group.setResource(tripleStore.createUriResource(configurationService.getBaseUri()+"/group/"+group.getName()));
		group.setType(tripleStore.createUriResource(Constants.NS_KIWI_SPECIAL + "KiWiGroup"));
	}
	
	/**
	 * removes the group
	 * 
	 * if a group is never physically removed, 
	 * we should definitely add them to the revision management...
	 * otherwise it would make no sense to have an entity that 
	 * has a deleted flag and can never be revived again...
	 * anyway I'm not sure about whether it is necessary to 
	 * NOT physically delete groups
	 */
	public void remove(Group group){ 
		group = entityManager.find(Group.class, group.getId());
		cleanGroup(group);
		entityManager.remove(group);
//		entityManager.flush();
	}
	
	private void cleanGroup(Group group){ 
		group.setUsers(new HashSet<User>());
		group.setOwner(null);
	}	
	
	public void addUserToGroup(Group group, User user){
		log.info(group.getName() + user.getLogin());
		if(user != null && group != null) {
			//add user to group
			//entityManager.setFlushMode(FlushModeType.AUTO);
			group.getUsers().add(user);
			
			if(user.getGroups() == null){
					user.setGroups(new LinkedList<Group>());}
			
			
			//add roles from group to user
			Set<Role> ur = group.getRoles();
			if(ur != null){			
				for(Role r: ur){
					log.info(user.getLogin() + r.getName());
					userService.addRole(user.getLogin(), r.getName());
				}
			}
			
			user.getGroups().add(group);
			entityManager.persist(user);			
//			entityManager.flush();
		}		
	}	

	/**
	 * returns all groups
	 */
	@SuppressWarnings("unchecked")
	public List<Group> getGroups() {
		//fixBooleanNullFields();
		javax.persistence.Query q = entityManager.createNamedQuery("groupService.getAllGroups");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Group> getAllMyGroups(User user) {
		javax.persistence.Query q = entityManager.createNamedQuery("groupService.getAllMyGroups");
		q.setParameter("user", user);
		q.setParameter("owner", user);
		return q.getResultList();
	}
	
	public Group getGroupByName(String name) {
		Group result;
		javax.persistence.Query q = entityManager.createNamedQuery("groupService.getGroupByName");
		q.setParameter("name", name);
		try {
		result = (Group)q.getSingleResult();
		} catch (NoResultException ex) {
			result = null;
		}
		return result;
	}
	
	/**
	 * Proves if a group with the given name already exists
	 * @param name
	 * @return true, if a group with the given name exists 
	 * 		and false, if a group with this name does not exist
	 */
	public boolean groupExists(String name) {
		javax.persistence.Query q = entityManager.createNamedQuery("groupService.getGroupByName");
		q.setParameter("name", name);
		return q.getResultList().size() == 1;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Group> getGroupsByUser(User user) {
		if (user == null)
			return Collections.EMPTY_LIST;
		
		javax.persistence.Query q = entityManager
				.createNamedQuery("groupService.getGroupsByUser");
		q.setParameter("user", user);
		return q.getResultList();
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Group> getGroupsByOwner(User user) {
		if (user == null)
			return Collections.EMPTY_LIST;
		
		javax.persistence.Query q = entityManager
				.createNamedQuery("groupService.getGroupsByOwner");
		q.setParameter("owner", user);
		return q.getResultList();
	}	
	
	/**
	 * This method will, if necessary, create a 
	 * role and grant that role to a certain group. 
	 * 
	 * @param group
	 * @param role: the rolename
	 */
	@Override
	//@Restrict("#{s:hasRole('admin')}")
	public synchronized void addRole(final Group group, final String role) {
		addRoleExcludeUsers(group, role, null);
	}
	
	/**
	 * This method will, if necessary, create a 
	 * role and grant that role to a certain group. 
	 * 
	 * @param name: the groupname
	 * @param role: the rolename
	 */
	@Override
	//@Restrict("#{s:hasRole('admin')}")
	public synchronized void addRole(final String name, final String role) {
		addRole(getGroupByName(name), role);
	}
	
	/**
	 * Method that adds role to group and it's users, except for
	 * a set of users, who should be excluded (e.g. in the case
	 * when they are assigned to special roles).
	 * @param name
	 * @param role
	 * @param excludedUsers
	 */
	@Override
	public synchronized void addRoleExcludeUsers(final String name, 
			final String role, final Set<User> excludedUsers) {
		addRoleExcludeUsers(getGroupByName(name), role, excludedUsers);
	}
	
	/**
	 * Method that adds role to group and it's users, except for
	 * a set of users, who should be excluded (e.g. in the case
	 * when they are assigned to special roles).
	 * @param name
	 * @param role
	 * @param excludedUsers
	 */
	@Override
	public synchronized void addRoleExcludeUsers(final Group group, 
			final String role, final Set<User> excludedUsers) {
		log.debug("g "+group +" r "+ role);
		Set<User> users = group.getUsers();
		if(excludedUsers != null && excludedUsers.size() > 0) {
			if(users.removeAll(excludedUsers))
				log.info("Excluded users #0 ", excludedUsers);
		}
		for(User u : users) {
			userService.addRole(u, role);
		}
		Role r = roleService.getRoleByName(role);
		
		Set<Group> roleGroups = null;
		if((roleGroups = r.getGroups()) == null) {
			roleGroups = new HashSet<Group>();
		}
		roleGroups.add(group);
		r.setGroups(roleGroups);
	}
	
	@Override
	public boolean hasRole(final String name, final String role) {
		return hasRole(getGroupByName(name), role);
	}
	
	@Override
	public boolean hasRole(final Group group, final String role) {
		return roleService.getGroupsInRole(role).contains(group);
	}
	
	@Override
	public synchronized void removeRole(final Group group, final String rolename) {
		removeRole(group, roleService.getRoleByName(rolename));
	}
	
	@Override
	public void removeRole(final Group group, final Role role) {
		removeRoleExcludeUsers(group, role, null);
	}
	
	@Override
	public void removeRoleExcludeUsers(final Group group, final String rolename, Set<User> excludedUsers) {
		removeRoleExcludeUsers(group, roleService.getRoleByName(rolename), excludedUsers);
	}
	
	@Override
	public void removeGroupFromUser(User u, Group g){
	    log.info(u.getGroups().size() + " "+g.getUsers().size());
	    u.getGroups().remove(g);
	    g.getUsers().remove(u);
	    log.info(u.getGroups().size() + " "+g.getUsers().size());
	   
	    entityManager.persist(u);
//	    entityManager.flush();
	}
	
	@Override
	public void removeRoleExcludeUsers(final Group group, final Role role, Set<User> excludedUsers) {
		Set<User> users = group.getUsers();
		if(excludedUsers != null && excludedUsers.size() > 0) {
			users.removeAll(excludedUsers);
		}
		for(User u : users) {
			userService.removeRole(u, role);
		}
		group.getRoles().remove(role);
		role.getGroups().remove(group);
	}
}
