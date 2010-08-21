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

import java.util.List;

import kiwi.model.content.ContentItem;
import kiwi.model.user.Group;
import kiwi.model.user.User;

/**
 * This class represents a backing bean that is used to
 * create and view permissions on resource types on the 
 * user interface.
 * 
 * @author Stephanie Stroka
 * 			(stephanie.stroka@salzburgresearch.at)
 *
 */
public class TypesWithPermissions {
	
	/* users who already hold permissions on the resource type */
	private List<UsersWithPermissions> usersWithPermissions;
	
	/* groups who already hold permissions on the currentContentItem */
	private List<GroupsWithPermissions> groupsWithPermissions;
	
	/* the type that we're dealing with */
	private ContentItem type;
	
	private User selectedUser;
	
	private Group selectedGroup;
	
	/* Declares if a permission for a super-type, 
	 * that already has been defined, should be inherited */
	private boolean inheritPermission;
	
	private boolean globalRead;
	private boolean globalWrite;

	public boolean isGlobalWrite() {
		return globalWrite;
	}

	public void setGlobalWrite(boolean globalWrite) {
		this.globalWrite = globalWrite;
	}

	public void setUsersWithPermissions(List<UsersWithPermissions> usersWithPermissions) {
		this.usersWithPermissions = usersWithPermissions;
	}

	public List<UsersWithPermissions> getUsersWithPermissions() {
		return usersWithPermissions;
	}

	public void setType(ContentItem type) {
		this.type = type;
	}

	public ContentItem getType() {
		return type;
	}

	public void setInheritPermission(boolean inheritPermission) {
		this.inheritPermission = inheritPermission;
	}

	public boolean isInheritPermission() {
		return inheritPermission;
	}
	
	public void setGlobalRead(boolean globalRead) {
		this.globalRead = globalRead;
	}

	public boolean isGlobalRead() {
		return globalRead;
	}

	public void setGroupsWithPermissions(List<GroupsWithPermissions> groupsWithPermissions) {
		this.groupsWithPermissions = groupsWithPermissions;
	}

	public List<GroupsWithPermissions> getGroupsWithPermissions() {
		return groupsWithPermissions;
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
}
