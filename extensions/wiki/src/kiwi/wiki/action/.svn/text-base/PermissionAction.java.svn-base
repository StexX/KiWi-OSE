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
package kiwi.wiki.action;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.transaction.SystemException;

import kiwi.api.security.PermissionService;
import kiwi.api.user.UserService;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.user.Group;
import kiwi.model.user.Role;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;

/**
 * Checks whether the currentUser has a certain role
 * or permission on the currentContentItem
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Scope(ScopeType.PAGE)
@Name("permissionAction")
//@Transactional
public class PermissionAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create=true)
	private User currentUser;
	
	@In(create=true)
	private ContentItem currentContentItem;
		
	@In
	private UserService userService;
		
	@In
	private PermissionService permissionService;
	
	@In
	private EntityManager entityManager;

	@Logger
	private Log log;
	
	
	/**
	 * returns true if the currentUser has the permission to
	 * perform an action on the currentContentItem 
	 * and false otherwise
	 * 
	 * @param action
	 * @return
	 */
	public boolean hasPermission(String action) {
		try {
			if(!Transaction.instance().isActive()) {
				log.error("No active transaction :(");
				return false;
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		// check for individual permissions
		if(!permissionService.hasPermission(currentUser, currentContentItem, action)) {
			if(!entityManager.contains(currentUser)) {
				currentUser = entityManager.find(User.class, currentUser.getId());
			}
			entityManager.refresh(currentUser);
			// if there are no individual permissions, 
			// check whether role permissions are assigned to the contentItem
			for(Role r : currentUser.getRoles()) {
				if(permissionService.hasPermission(r, currentContentItem, action))
					return true;
			}
			// if there are no individual or role-permissions, check whether
			// permissions have been assigned to the group
			for(Group g : currentUser.getGroups()) {
				if(permissionService.hasPermission(g, currentContentItem, action))
					return true;
			}
			// else check for type-based permissions
			for(KiWiResource r : currentContentItem.getTypes()) {
				if(permissionService.hasPermission(currentUser, r.getContentItem(), action))
					return true;
			}
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * returns true if the currentUser has a certain role
	 * and false otherwise
	 * 
	 * @param role
	 * @return
	 */
	public boolean hasRole(String role) {
		return userService.hasRole(currentUser, role);
	}
	
}
