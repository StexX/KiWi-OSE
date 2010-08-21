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
package kiwi.service.role;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.role.RoleServiceLocal;
import kiwi.api.role.RoleServiceRemote;
import kiwi.model.user.Group;
import kiwi.model.user.Role;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.management.IdentityManager;

/**
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Stateless
@Name("roleService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class RoleServiceImpl implements RoleServiceRemote, RoleServiceLocal, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8273652830220967043L;

	@In
	private EntityManager entityManager;
	
	@Override
	public Role createRole(String name) {
		entityManager.setFlushMode(FlushModeType.AUTO);
		if(!IdentityManager.instance().getIdentityStore().roleExists(name)) {
			IdentityManager.instance().getIdentityStore().createRole(name);
		}
		entityManager.flush();
		return getRoleByName(name);
	}
	
	/**
	 * Returns a list of all roles
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAllRoles() {
		Query q = entityManager.createNamedQuery("roleService.allRoles");
		return q.getResultList();
	}
	
	/**
	 * Returns a list of all roles
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllRolenames() {
		Query q = entityManager.createNamedQuery("roleService.allRolenames");
		return q.getResultList();
	}
	
	/**
	 * Returns a list of all roles
	 * @return
	 */
	@Override
	public Role getRoleByName(String name) {
		Query q = entityManager.createNamedQuery("roleService.getRoleByName");
		q.setParameter("name", name);
		try {
			return (Role) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Returns a list of all users in a role
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsersInRole(String name) {
		Query q = entityManager.createNamedQuery("roleService.getUsersInRole");
		q.setParameter("name", name);
		return q.getResultList();
	}
	
	/**
	 * Returns a list of all groups in a role
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getGroupsInRole(String name) {
		Query q = entityManager.createNamedQuery("roleService.getGroupsInRole");
		q.setParameter("name", name);
		return q.getResultList();
	}
}
