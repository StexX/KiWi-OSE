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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import kiwi.api.group.GroupService;
import kiwi.api.ontology.OntologyService;
import kiwi.api.role.RoleService;
import kiwi.api.security.PermissionService;
import kiwi.api.user.UserService;
import kiwi.exception.PermissionException;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiAnonResource;
import kiwi.model.ontology.KiWiClass;
import kiwi.model.security.Permission;
import kiwi.model.user.Group;
import kiwi.model.user.Role;
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

@Name("grantTypePermissionAction")
@Scope(ScopeType.PAGE)
//@Transactional
public class GrantTypePermissionAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	private List<ContentItem> allTypes;
	
	private ContentItem selectedType;
	
	private List<User> allUsers;
	
	private List<Group> allGroups;
	
	/* types who already defined permissions */
	private List<TypesWithPermissions> typesWithPermissions;
	
	@In
	private RoleService roleService;
	
	@In
	private UserService userService;
	
	@In
	private GroupService groupService;
	
	@In
	private OntologyService ontologyService;
	
	@In
	private PermissionService permissionService;
	
	@In
	private FacesMessages facesMessages;
	
	/* TODO: remove asap */
	@In
	private EntityManager entityManager;
	
	/**
	 * called if the page is rendered for the first time
	 */
	@Create
	public void init() {
		typesWithPermissions = new ArrayList<TypesWithPermissions>();
		
		initTypesWithPermissions();
		
		allTypes = new LinkedList<ContentItem>();
		// TODO when ontologyService.listClasses() will deliver anonymous 
		// nodes, we'll have to deal here with how to display them... 
		// (see KIWI-159 in Jira)
		for(KiWiClass cls : ontologyService.listClasses()) {
			if(cls.getTitle() != null) {
				allTypes.add(cls.getDelegate());
			}
			else{
				log.info("not added Type, (anonymous?) #0", cls.toString());
				if(cls instanceof KiWiAnonResource){
					log.info("anon resource #0 has anonId #1", cls.toString(), ((KiWiAnonResource) cls).getAnonId());
				}
			}
		}
		
		allUsers = userService.getUsers();
		allGroups = groupService.getGroups();
	}
	
	/**
	 * initialises the typesWithPermissions list and triggers
	 * the initialisation of each user and group for a certain
	 * resource type
	 */
	private void initTypesWithPermissions() {
		HashMap<ContentItem,TypesWithPermissions> typeCollection = new HashMap<ContentItem, TypesWithPermissions>();
		
		List<Permission> typePermissions = permissionService.getTypePermissions();
		for(Permission tp : typePermissions) {
			Boolean inheritance = tp.getTypeInheritance();
			
			// just add permission if it is used on types
			if(inheritance != null) {
				if(!typeCollection.keySet().contains(tp.getTarget())) {
					TypesWithPermissions twp = new TypesWithPermissions();
					typeCollection.put(tp.getTarget(), twp);
					twp = typeCollection.get(tp.getTarget());
					
					twp.setType(tp.getTarget());
				
					HashMap<User,UsersWithPermissions> userCollection = new HashMap<User, UsersWithPermissions>();
					HashMap<Group,GroupsWithPermissions> groupCollection = new HashMap<Group, GroupsWithPermissions>();
					List<Permission> permissions = permissionService.getPermissionsByTarget(tp.getTarget());
					List<UsersWithPermissions> usersWithPermissions = new ArrayList<UsersWithPermissions>();
					List<GroupsWithPermissions> groupsWithPermissions = new ArrayList<GroupsWithPermissions>();
					
					for(Permission p : permissions) {
						// just add permission if it is used on types
						Boolean inheritance2 = p.getTypeInheritance();
						if(inheritance2 != null) {
							if(p.getUser() != null) {
								if(!userCollection.keySet().contains(p.getUser())) {
									UsersWithPermissions uwp = new UsersWithPermissions();
									userCollection.put(p.getUser(), uwp);
									uwp.setUser(p.getUser());
									uwp.setDisplayname(p.getUser().getFirstName() + " " + 
														p.getUser().getLastName());
									List<Permission> final_permissions = permissionService.
										getPermissionsByRecipientAndTarget(p.getUser(),p.getTarget());
									for(Permission fp : final_permissions) {
										if(fp.getTypeInheritance() != null) {
											if(fp.getAction().equals("read")) {
												uwp.setRead(true);
											} else if(fp.getAction().equals("write")) {
												uwp.setWrite(true);
											} else if(fp.getAction().equals("editPermissions")) {
												uwp.setEditPermissions(true);
											} else if(fp.getAction().equals("annotate")) {
												uwp.setAnnotate(true);
											} else if(fp.getAction().equals("delete")) {
												uwp.setDelete(true);
											} else if(fp.getAction().equals("tag")) {
												uwp.setTag(true);
											} else if(fp.getAction().equals("comment")) {
												uwp.setComment(true);
											}
										}
									}
									usersWithPermissions.add(uwp);
								}
							} else if(p.getGroup() != null) {
								if(!groupCollection.keySet().contains(p.getGroup())) {
									GroupsWithPermissions gwp = new GroupsWithPermissions();
									groupCollection.put(p.getGroup(), gwp);
									gwp.setGroup(p.getGroup());
									gwp.setDisplayname(p.getGroup().getName());
									
									List<Permission> final_permissions = permissionService.
										getPermissionsByGroupAndTarget(p.getGroup(),p.getTarget());
									for(Permission fp : final_permissions) {
										if(fp.getTypeInheritance() != null) {
											if(fp.getAction().equals("read")) {
												gwp.setRead(true);
											} else if(fp.getAction().equals("write")) {
												gwp.setWrite(true);
											} else if(fp.getAction().equals("editPermissions")) {
												gwp.setEditPermissions(true);
											} else if(fp.getAction().equals("annotate")) {
												gwp.setAnnotate(true);
											} else if(fp.getAction().equals("delete")) {
												gwp.setDelete(true);
											} else if(fp.getAction().equals("tag")) {
												gwp.setTag(true);
											} else if(fp.getAction().equals("comment")) {
												gwp.setComment(true);
											}
										}
									}
									groupsWithPermissions.add(gwp);
								}
							} else if(p.getRole() != null) {
								if(p.getRole().getName().equals("read")) {
									twp.setGlobalRead(true);
								}
								if(p.getRole().getName().equals("write")) {
									twp.setGlobalWrite(true);
								}
							}
						}
					}
					twp.setInheritPermission(inheritance);
					twp.setUsersWithPermissions(usersWithPermissions);
					twp.setGroupsWithPermissions(groupsWithPermissions);
					typesWithPermissions.add(twp);
				}
			}
		}
	}
	
	/**
	 * get list of all users
	 * @param allGroups
	 */
	public List<User> getAllUsers() {
		return allUsers;
	}
	
	/**
	 * set list of all users
	 * @param allGroups
	 */
	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}
	
	/**
	 * get list of all groups
	 * @param allGroups
	 */
	public List<Group> getAllGroups() {
		return allGroups;
	}
	
	/**
	 * set list of all groups
	 * @param allGroups
	 */
	public void setAllGroups(List<Group> allGroups) {
		this.allGroups = allGroups;
	}

	/**
	 * grants or removes permissions according to the action flags
	 * set in the user interface
	 */
	public void store() {
		log.info("Storing types #0 ", typesWithPermissions);
		
		// iterate through type pemissions
		for(TypesWithPermissions twp : typesWithPermissions) {
			ContentItem type = twp.getType();
			
			// iterate through users
			if(twp.getUsersWithPermissions() != null) {
				for(UsersWithPermissions uwp : twp.getUsersWithPermissions()) {
					User u = uwp.getUser();
					try {
						if (uwp.isRead())
							permissionService.grantPermission(u, type, "read", twp.isInheritPermission());
						else
							permissionService.removePermission(u, type, "read");
						if (uwp.isWrite())
							permissionService.grantPermission(u, type, "write", twp.isInheritPermission());
						else
							permissionService.removePermission(u, type, "write");
						if (uwp.isDelete())
							permissionService.grantPermission(u, type, "delete", twp.isInheritPermission());
						else
							permissionService.removePermission(u, type, "delete");
						if (uwp.isEditPermissions())
							permissionService.grantPermission(u, type, "editPermissions", twp.isInheritPermission());
						else
							permissionService.removePermission(u, type, "editPermissions");
						if (uwp.isAnnotate())
							permissionService.grantPermission(u, type, "annotate", twp.isInheritPermission());
						else
							permissionService.removePermission(u, type, "annotate");
						if (uwp.isComment())
							permissionService.grantPermission(u, type, "comment", twp.isInheritPermission());
						else
							permissionService.removePermission(u, type, "comment");
						if (uwp.isTag())
							permissionService.grantPermission(u, type, "tag", twp.isInheritPermission());
						else
							permissionService.removePermission(u, type, "tag");
					} catch (PermissionException e) {
						e.printStackTrace();
					}
				}
			}
			
			// iterate through groups
			if(twp.getGroupsWithPermissions() != null) {
				for(GroupsWithPermissions gwp : twp.getGroupsWithPermissions()) {
					Group g = gwp.getGroup();
					try {
						if (gwp.isRead())
							permissionService.grantPermission(g, type, "read", twp.isInheritPermission());
						else
							permissionService.removePermission(g, type, "read");
						if (gwp.isWrite())
							permissionService.grantPermission(g, type, "write", twp.isInheritPermission());
						else
							permissionService.removePermission(g, type, "write");
						if (gwp.isDelete())
							permissionService.grantPermission(g, type, "delete", twp.isInheritPermission());
						else
							permissionService.removePermission(g, type, "delete");
						if (gwp.isEditPermissions())
							permissionService.grantPermission(g, type, "editPermissions", twp.isInheritPermission());
						else
							permissionService.removePermission(g, type, "editPermissions");
						if (gwp.isAnnotate())
							permissionService.grantPermission(g, type, "annotate", twp.isInheritPermission());
						else
							permissionService.removePermission(g, type, "annotate");
						if (gwp.isComment())
							permissionService.grantPermission(g, type, "comment", twp.isInheritPermission());
						else
							permissionService.removePermission(g, type, "comment");
						if (gwp.isTag())
							permissionService.grantPermission(g, type, "tag", twp.isInheritPermission());
						else
							permissionService.removePermission(g, type, "tag");
					} catch (PermissionException e) {
						e.printStackTrace();
					}
				}
			}
			
			try {
				Role r = roleService.getRoleByName("read");
				if(twp.isGlobalRead()) {
					permissionService.grantPermission(r, type, "read", twp.isInheritPermission());
				} else {
					permissionService.removePermission(r, type, "read");
				}
			} catch (PermissionException e1) {
				e1.printStackTrace();
			}

			try {
				Role r = roleService.getRoleByName("write");
				if(twp.isGlobalWrite()) {
					permissionService.grantPermission(r, type, "write", twp.isInheritPermission());
				} else {
					permissionService.removePermission(r, type, "write");
				}
			} catch (PermissionException e1) {
				e1.printStackTrace();
			}
			
		}		
//		entityManager.flush();
		facesMessages.add("Successfully stored");
	}
	
	/**
	 * adds a new user to the list of arranged users
	 */
	public void addToTypesWithPermissions() {
		if(selectedType != null) {
			
			TypesWithPermissions typesBean = new TypesWithPermissions();
			typesBean.setType(selectedType);
		
			typesWithPermissions.add(typesBean);
		}
	}
	
	/**
	 * adds a user to the list of type permissions
	 * @param typesWithPermissions
	 */
	public void addToUsersWithTypePermissions(TypesWithPermissions typesWithPermissions) {
		if(typesWithPermissions.getSelectedUser() != null) {
			UsersWithPermissions userBean = new UsersWithPermissions();
			userBean.setUser(typesWithPermissions.getSelectedUser());
			userBean.setDisplayname(typesWithPermissions.getSelectedUser().getFirstName() + " " + typesWithPermissions.getSelectedUser().getLastName());
			
			if(typesWithPermissions.getUsersWithPermissions() == null) {
				typesWithPermissions.setUsersWithPermissions(new ArrayList<UsersWithPermissions>());
			}
			typesWithPermissions.getUsersWithPermissions().add(userBean);
		}
	}
	
	/**
	 * adds a group to the list of type permissions
	 * @param typesWithPermissions
	 */
	public void addToGroupsWithTypePermissions(TypesWithPermissions typesWithPermissions) {
		if(typesWithPermissions.getSelectedGroup() != null) {
			GroupsWithPermissions groupBean = new GroupsWithPermissions();
			groupBean.setGroup(typesWithPermissions.getSelectedGroup());
			groupBean.setDisplayname(typesWithPermissions.getSelectedGroup().getName());
			
			if(typesWithPermissions.getGroupsWithPermissions() == null) {
				typesWithPermissions.setGroupsWithPermissions(new ArrayList<GroupsWithPermissions>());
			}
			typesWithPermissions.getGroupsWithPermissions().add(groupBean);
		}
	}
	
	/**
	 * removes a user from the list of specific type permissions
	 * @param typesWithPermissions
	 * @param uwp
	 */
	public String delUserWithTypePermissions(TypesWithPermissions typesWithPermissions, UsersWithPermissions uwp) {
		User u = uwp.getUser();
		ContentItem type = typesWithPermissions.getType();
		
		if(u != null && type != null) {
			permissionService.removePermission(u, type, "read");
			permissionService.removePermission(u, type, "write");
			permissionService.removePermission(u, type, "editPermissions");
			permissionService.removePermission(u, type, "annotate");
			permissionService.removePermission(u, type, "tag");
			permissionService.removePermission(u, type, "comment");
			permissionService.removePermission(u, type, "delete");
			
//			entityManager.flush();
		}
		
		return "/admin/permissions.xhtml";
	}
	
	/**
	 * removes a group from the list of specific type permissions
	 * @param typesWithPermissions
	 * @param uwp
	 */
	public String delGroupWithTypePermissions(TypesWithPermissions typesWithPermissions, GroupsWithPermissions gwp) {
		
		Group g = gwp.getGroup();
		ContentItem type = typesWithPermissions.getType();
		
		if(g != null && type != null) {
			permissionService.removePermission(g, type, "read");
			permissionService.removePermission(g, type, "write");
			permissionService.removePermission(g, type, "editPermissions");
			permissionService.removePermission(g, type, "annotate");
			permissionService.removePermission(g, type, "tag");
			permissionService.removePermission(g, type, "comment");
			permissionService.removePermission(g, type, "delete");
			
//			entityManager.flush();
		}
		
		return "/admin/permissions.xhtml";
	}

	public void setAllTypes(List<ContentItem> allTypes) {
		this.allTypes = allTypes;
	}

	public List<ContentItem> getAllTypes() {
		return allTypes;
	}

	public ContentItem getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(ContentItem selectedType) {
		this.selectedType = selectedType;
	}

	public List<TypesWithPermissions> getTypesWithPermissions() {
		return typesWithPermissions;
	}

	public void setTypesWithPermissions(
			List<TypesWithPermissions> typesWithPermissions) {
		this.typesWithPermissions = typesWithPermissions;
	}
	
	
}
