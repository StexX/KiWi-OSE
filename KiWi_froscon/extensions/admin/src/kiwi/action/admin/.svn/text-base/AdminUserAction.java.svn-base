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
package kiwi.action.admin;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kiwi.api.user.UserService;
import kiwi.model.user.Group;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.security.management.IdentityManager;

/**
 * @author Rolf Sint
 * 
 */

@Scope(ScopeType.CONVERSATION)
@Name("adminUser")
public class AdminUserAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	UserService userService;

	@DataModel
	private List<String> users;

	@DataModelSelection
	@Out(required = false)
	private User selectedUser;
	
	@Factory("users")
	public void initUsers(){
		

				users = IdentityManager.instance().listUsers();

		
		
	}

	public String editIt() {
		return "/admin/edituser";
	}


	public List<String> getUsers() {
		return users;
	}


	public void setUsers(List<String> users) {
		this.users = users;
	}
	
	public Set<Group> getGroupsOfUser() {
	//	UserAccount ua =  selectedUser.getUserAccount();
	//	Set<Group> lg =  ua.getGroups();
		
		Set<Group> results = new HashSet<Group>();
//		for (Iterator iterator = lg.iterator(); iterator.hasNext();) {
//			Group name = (Group) iterator.next();
//			results.add(name.getName());
//		}
		Group h = new Group();
		h.setName("abc");
		results.add(h);
		
		return results;
	}
}
