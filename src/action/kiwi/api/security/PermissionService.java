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
package kiwi.api.security;

import java.util.List;

import kiwi.context.KnowledgeSpaceBean;
import kiwi.exception.PermissionException;
import kiwi.model.content.ContentItem;
import kiwi.model.ontology.KiWiClass;
import kiwi.model.security.Permission;
import kiwi.model.user.Group;
import kiwi.model.user.Role;
import kiwi.model.user.User;

/**
 * The PermissionService is responsible for granting and removing permissions
 * for actions to special contentItems and users. It is the main part of the 
 * kiwi permission management and is used by action classes which allow the 
 * user to assign permissions and which allow other classes to permit access
 * to resources for users, who don't have permissions.
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
public interface PermissionService {

	/**
	 * Grants action-permission for single ContentItems to 
	 * individual users
	 * 
	 * @param user
	 * @param target
	 * @param action
	 * @throws PermissionException 
	 */
	public void grantPermission(User user, ContentItem target, String action) throws PermissionException;
	
	/**
	 * Grants action-permission for single ContentItems to 
	 * a role
	 * 
	 * @param role
	 * @param target
	 * @param action
	 * @throws PermissionException 
	 */
	void grantPermission(Role role, ContentItem target, String action)
			throws PermissionException;
	
	/**
	 * Grants action-permission for all ContentItems in a 
	 * KnowledgeSpace to individual users
	 * 
	 * @param group
	 * @param target
	 * @param action
	 */
	public void grantPermission(User user, KnowledgeSpaceBean target, String action);

	/**
	 * Returns all permissions assigned for one contentItem
	 * 
	 * @param item
	 * @return
	 */
	public List<Permission> getPermissionsByTarget(ContentItem item);

	/**
	 * Returns all permissions assigned for one type
	 * 
	 * @param type
	 * @return
	 */
//	public List<Permission> getPermissionsByType(KiWiClass type);
	
	/**
	 * Returns all permissions assigned to one user
	 * 
	 * @param user
	 * @return
	 */
	public List<Permission> getPermissionsByRecipient(User user);

	/**
	 * Queries whether a user has the permission to perform an
	 * action on a certain target (=contentItem)
	 * 
	 * @param user
	 * @param ci
	 * @param action
	 * @return true if the permission exists and false otherwise
	 */
	public boolean hasPermission(User user, ContentItem ci, String action);

	/**
	 * Queries whether a user has the permission to perform an
	 * action on a certain type of contentItem
	 * 
	 * @param user
	 * @param type
	 * @param action
	 * @return true if the permission exists and false otherwise
	 */
//	public boolean hasPermission(User user, KiWiClass type, String action);
	
	/**
	 * Queries whether a role has the permission to perform an
	 * action on a certain target (=contentItem)
	 * 
	 * @param user
	 * @param ci
	 * @param action
	 * @return true if the permission exists and false otherwise
	 */
	public boolean hasPermission(Role role, ContentItem ci, String action);

	/**
	 * returns the users that have permissions on a contentItem
	 * 
	 * @param ci
	 * @return
	 */
	public List<User> getPermissionOwnerByTarget(ContentItem ci);

	/**
	 * Returns all permissions assigned to one user for one contentItem
	 * 
	 * @param user
	 * @param item
	 * @return
	 */
	public List<Permission> getPermissionsByRecipientAndTarget(User user,
			ContentItem item);

	/**
	 * returns the permission object for a user on a 
	 * contentItem for a certain action
	 * 
	 * @param user
	 * @param ci
	 * @param action
	 * @return
	 */
	public Permission getPermission(User user, ContentItem ci, String action);

	public void removePermission(User user, ContentItem target, String action);
	
	/**
	 * Removes permissions from a user
	 * @param r
	 * @param type
	 * @param action
	 */
	public void removePermission(Role r, KiWiClass type, String action);

	/**
	 * Removes permissions from a role
	 * @param user
	 * @param target
	 * @param action
	 */
	public void removePermission(Role role, ContentItem target, String action);
	
	/**
	 * returns the permission object for a role on a 
	 * contentItem for a certain action
	 * 
	 * @param role
	 * @param ci
	 * @param action
	 * @return
	 */
	public Permission getPermission(Role role, ContentItem ci, String action);

	/**
	 * returns the group that has been assigned a permission for
	 * the given contentItem
	 * 
	 * @param ci
	 * @return
	 */
	public List<User> getPermissionGroupByTarget(ContentItem ci);

	/**
	 * returns the permission owners for a certain type of ContentItems
	 * @param type
	 * @return
	 */
	public List<User> getPermissionOwnerByType(KiWiClass type);

	public void grantPermission(User user, ContentItem type, String action,
			Boolean inheritsPermissions) throws PermissionException;

	public void grantPermission(Role role, ContentItem type, String action,
			Boolean inheritsPermissions) throws PermissionException;
	
	public void removePermission(User user, KiWiClass type, String action);

	public List<Permission> getTypePermissions();

	public boolean hasPermission(Group group, ContentItem target, String action);

	public void grantPermission(Group group, ContentItem target, String action)
			throws PermissionException;

	public void grantPermission(Group group, ContentItem type, String action,
			Boolean inheritsPermissions) throws PermissionException;

	public Permission getPermission(Group group, ContentItem ci, String action);

	public void removePermission(Group group, ContentItem target, String action);

	public List<Permission> getPermissionsByGroupAndTarget(Group group,
			ContentItem target);

//	public List<Permission> getPermissionsByRecipientAndType(User user,
//			KiWiClass type);


}
