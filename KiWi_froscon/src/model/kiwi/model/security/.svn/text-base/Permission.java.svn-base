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
package kiwi.model.security;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.user.Group;
import kiwi.model.user.Role;
import kiwi.model.user.User;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

/**
 * TODO: implement
 * 
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@BatchSize(size = 100)
@NamedQueries({
	@NamedQuery(name  = "permissionService.getPermissionByTarget",
		        query = "from Permission p where p.target = :contentItem"),
    @NamedQuery(name  = "permissionService.getPermissionByUser",
		        query = "from Permission p where p.user = :user"),
    @NamedQuery(name  = "permissionService.getPermissionOwnerByTarget",
		        query = "select p.user from Permission p where p.target = :contentItem"),
    @NamedQuery(name  = "permissionService.getAllPermissions",
	        	query = "from Permission p"),
    @NamedQuery(name  = "permissionService.getTypePermissions", // TODO: where p.target.resource has rdf:type owl:Class...
        		query = "select p from Permission p " +
        					"where p.target.resource.uri IN " +
        					"(select t.subject.uri from KiWiTriple t where " +
        						"t.property.uri = '" + Constants.NS_RDF + "type' " +
        						"and ( t.object.uri = '" + Constants.NS_RDFS + "Class' " +
        							"or t.object.uri = '" + Constants.NS_OWL + "Class' ) " +
        						" and t.deleted = false " +
        						" and t.subject.contentItem.title is not null )" +
        						" and p.typeInheritance is not null"),
    @NamedQuery(name  = "permissionService.getUserPermissions",
    			query = "from Permission p where p.user is not null"),
    @NamedQuery(name  = "permissionService.getRolePermissions",
    			query = "from Permission p where p.role is not null"),
    @NamedQuery(name  = "permissionService.getGroupPermissions",
				query = "from Permission p where p.group is not null"),
	@NamedQuery(name  = "permissionService.getPermissionByUserAndAction",
	        	query = "from Permission p where p.user = :user and p.action = :action"),
	@NamedQuery(name  = "permissionService.getPermissionByGroupAndAction",
        		query = "from Permission p where p.group = :group and p.action = :action"),
   	@NamedQuery(name  = "permissionService.getPermissionByUserAndTarget",
   	        	query = "from Permission p where p.user = :user and p.target = :contentItem"),
	@NamedQuery(name  = "permissionService.getPermissionByGroupAndTarget",
				query = "from Permission p where p.group = :group and p.target = :contentItem"),
    @NamedQuery(name  = "permissionService.getPermissionByUserAndTargetAndAction",
	        	query = "select p from Permission p " +
	        							"where " +
	        							"p.target = :contentItem and " +
	        							"p.action = :action and " +
	        							"p.user = :user"),
	@NamedQuery(name  = "permissionService.getPermissionByRoleAndTargetAndAction",
				query = "from Permission p where p.role = :role and " +
	        							"p.target = :contentItem and " +
	        							"p.action = :action"),
	@NamedQuery(name  = "permissionService.getPermissionByGroupAndTargetAndAction",
				query = "from Permission p where p.group = :group and " +
	        							"p.target = :contentItem and " +
	        							"p.action = :action")
})
public class Permission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3840450870231297551L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	/* User holding permission */
	@ManyToOne
	private User user;
	
	/* Role holding permission */
	@ManyToOne
	private Role role;
	
	@ManyToOne
	private Group group;
	
	/* ContentItem */
	@ManyToOne
	@Index(name="idx_permission_target")
	private ContentItem target;
	
	private Boolean typeInheritance;
	
	/* e.g. write, read, create, delete... */
	@Index(name="idx_permission_action")
	private String action;
	
	public Permission() {
	}
	
	public Permission(ContentItem target, String action, User user) {
		this.target = target;
		this.action = action;
		this.user = user;
	}
	
	public Permission(ContentItem target, String action, Group group) {
		this.target = target;
		this.action = action;
		this.group = group;
	}
	
	public Permission(ContentItem target, String action, Role role) {
		this.target = target;
		this.action = action;
		this.role = role;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the recipient
	 */
//	@PermissionUser 
//	@PermissionRole
	public User getUser() {
		return user;
	}

	/**
	 * @param recipient the recipient to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * @return the target
	 */
//	@PermissionTarget
	public ContentItem getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(ContentItem target) {
		this.target = target;
	}

	/**
	 * @return the action
	 */
//	@PermissionAction
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	public void setTypeInheritance(Boolean typeInheritance) {
		this.typeInheritance = typeInheritance;
	}

	public Boolean getTypeInheritance() {
		return typeInheritance;
	}
	
	@Override
	public String toString() {
		if(user != null) {
			return (this.user.getLogin() + ":" + this.action + ":" + this.getTarget().getKiwiIdentifier());
		} else if(group != null) {
			return (this.group.getName() + ":" + this.action + ":" + this.getTarget().getKiwiIdentifier());
		} else if(role != null) {
			return (this.role.getName() + ":" + this.action + ":" + this.getTarget().getKiwiIdentifier());
		}
		return "";
	}

}
