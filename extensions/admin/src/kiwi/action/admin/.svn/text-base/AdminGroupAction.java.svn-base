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
import java.util.List;

import kiwi.api.group.GroupService;
import kiwi.exception.GroupExistsException;
import kiwi.model.user.Group;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;

/**
 * @author Rolf Sint
 * 
 */

@Scope(ScopeType.CONVERSATION)
@Name("adminGroup")
public class AdminGroupAction implements Serializable {

//	@In(value = "kiwi.group", required = false)
//	Group group;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	@DataModel
	private List<Group> groups;

	@In
	GroupService groupService;

	
	/* (non-Javadoc)
	 * @see kiwi.action.admin.IAdminGroupAction#init()
	 */
	@Factory("groups")
	public void init(){
		groups = groupService.getGroups();
	}

	// @Out
	// allGroups is the same as groups, but no datamodel. It is used for
	// Outjection
	// -->Bijection does not work with DataModels
	// private List<Group> allGroups;

	@DataModelSelection
	@Out(required = false)
	private Group selectedGroup;

	/* (non-Javadoc)
	 * @see kiwi.action.admin.IAdminGroupAction#setGroups(java.util.List)
	 */
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	/* (non-Javadoc)
	 * @see kiwi.action.admin.IAdminGroupAction#delete()
	 */
	
	public String delete() {
		groups.remove(selectedGroup);
		groupService.remove(selectedGroup);
		return "/admin/adminGroup";
	}

	/* (non-Javadoc)
	 * @see kiwi.action.admin.IAdminGroupAction#configure()
	 */
	public String configure() {
		return "/admin/editGroup";
	}

	/* (non-Javadoc)
	 * @see kiwi.action.admin.IAdminGroupAction#store()
	 */
	//@Begin(nested = true)
	public String store() throws GroupExistsException {
		Group group = new Group();
		group.setName(name);
		
		groups.add(group);
		groupService.store(group);

		return "/admin/adminGroup";
	}

	/* (non-Javadoc)
	 * @see kiwi.action.admin.IAdminGroupAction#getGroups()
	 */
	public List<Group> getGroups() {
		return groups;
	}

	/* (non-Javadoc)
	 * @see kiwi.action.admin.IAdminGroupAction#getSelectedGroup()
	 */
	public Group getSelectedGroup() {
		return selectedGroup;
	}

	// public void setAllGroups(List<Group> allGroups) {
	// this.allGroups = allGroups;
	// }

	/* (non-Javadoc)
	 * @see kiwi.action.admin.IAdminGroupAction#destroy()
	 */
	@Destroy
	public void destroy() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
