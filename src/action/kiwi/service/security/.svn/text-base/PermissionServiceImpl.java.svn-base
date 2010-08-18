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
package kiwi.service.security;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.SystemException;

import kiwi.api.security.PermissionServiceLocal;
import kiwi.api.security.PermissionServiceRemote;
import kiwi.context.KnowledgeSpaceBean;
import kiwi.exception.PermissionException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.KiWiClass;
import kiwi.model.security.Permission;
import kiwi.model.user.Group;
import kiwi.model.user.Role;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.cache.CacheProvider;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;

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
@Name("permissionService")
@Stateless
@AutoCreate
@Scope(ScopeType.STATELESS)
//@Transactional
public class PermissionServiceImpl implements PermissionServiceLocal, PermissionServiceRemote {

	@In
	private EntityManager entityManager;
	
	@Logger
	private Log log;
	
	/**
 	 * The cache provider defined in Seam. Used extensively for caching triples and nodes in order to 
 	 * avoid unnecessary database access.
 	 */
 	@In
 	private CacheProvider cacheProvider;
	
	/**
	 * Grants action-permission for single ContentItems to 
	 * individual users
	 * 
	 * @param user
	 * @param target
	 * @param action
	 * @throws PermissionException 
	 */
	@Override
	public void grantPermission(User user, ContentItem target, String action) throws PermissionException {
		if(user == null || target == null || action == null) {
			throw new PermissionException("Cannot assign permission " +
					"to null object: (user: " + user + ", target: " + 
					target + ", action: " + action + ")");
		}
		if(!hasPermission(user, target, action)) {
			Permission p = new Permission(target, action, user);
			log.debug("Persisting permission (#0 - #1 - #2)", user.getLogin(), target.getTitle(), action);
			cacheProvider.put("permission", p.toString(), p);
			entityManager.persist(p);
		}
	}
	
	/**
	 * Grants action-permission for single ContentItems to 
	 * individual users
	 * 
	 * @param user
	 * @param target
	 * @param action
	 * @throws PermissionException 
	 */
	@Override
	public void grantPermission(User user, ContentItem type, String action, Boolean inheritsPermissions) throws PermissionException {
		if(user == null || type == null || action == null) {
			throw new PermissionException("Cannot assign permission " +
					"to null object: (user: " + user + ", type: " + 
					type + ", action: " + action + ")");
		}
		if(!hasPermission(user, type, action)) {
			Permission p = new Permission(type.getDelegate(), action, user);
			p.setTypeInheritance(inheritsPermissions);
			log.debug("Persisting permission (#0 - #1 - #2)", user.getLogin(), ((KiWiUriResource) type.getResource()).getUri(), action);
			cacheProvider.put("permission", p.toString(), p);
			entityManager.persist(p);
		} else {
			Permission p = getPermission(user, type, action);
			p.setTypeInheritance(inheritsPermissions);
		}
	}
	
	/**
	 * Grants action-permission for single ContentItems to 
	 * groups
	 * 
	 * @param user
	 * @param target
	 * @param action
	 * @throws PermissionException 
	 */
	// TODO: grant permissions to ContentItems that hold the type
	@Override
	public void grantPermission(Group group, ContentItem target, String action) throws PermissionException {
		if(group == null || target == null || action == null) {
			throw new PermissionException("Cannot assign permission " +
					"to null object: (group: " + group + ", target: " + 
					target + ", action: " + action + ")");
		}
		if(!hasPermission(group, target, action)) {
			Permission p = new Permission(target, action, group);
			log.debug("Persisting permission (#0 - #1 - #2)", group.getName(), ((KiWiUriResource) target.getResource()).getUri(), action);
			cacheProvider.put(Constants.CACHE_PERMISSIONS, p.toString(), p);
			entityManager.persist(p);
		}
	}

	/**
	 * Grants action-permission for single ContentItems to 
	 * individual users
	 * 
	 * @param user
	 * @param target
	 * @param action
	 * @throws PermissionException 
	 */
	// TODO: grant permissions to ContentItems that hold the type
	@Override
	public void grantPermission(Group group, ContentItem type, String action, Boolean inheritsPermissions) throws PermissionException {
		if(group == null || type == null || action == null) {
			throw new PermissionException("Cannot assign permission " +
					"to null object: (group: " + group + ", type: " + 
					type + ", action: " + action + ")");
		}
		if(!hasPermission(group, type, action)) {
			Permission p = new Permission(type, action, group);
			p.setTypeInheritance(inheritsPermissions);
			log.debug("Persisting permission (#0 - #1 - #2)", group.getName(), 
					((KiWiUriResource) type.getResource()).getUri(), action);
			cacheProvider.put(Constants.CACHE_PERMISSIONS, p.toString(), p);
			entityManager.persist(p);
		} else {
			Permission p = getPermission(group, type, action);
			p.setTypeInheritance(inheritsPermissions);
		}
	}

	/**
	 * Grants action-permission for single ContentItems to 
	 * a role
	 * 
	 * @param role
	 * @param target
	 * @param action
	 * @throws PermissionException 
	 */
	@Override
	public void grantPermission(Role role, ContentItem target, String action) throws PermissionException {
		if(role == null || target == null || action == null) {
			throw new PermissionException("Cannot assign permission " +
					"to null object: (role: " + role + ", target: " + 
					target + ", action: " + action + ")");
		}
		if(!hasPermission(role, target, action)) {
			Permission p = new Permission(target, action, role);
			log.debug("Persisting permission (#0 - #1 - #2)", role.getName(), target.getTitle(), action);
			cacheProvider.put(Constants.CACHE_PERMISSIONS, p.toString(), p);
			entityManager.persist(p);
		}
	}
	
	/**
	 * Grants action-permission for single ContentItems to 
	 * a role
	 * 
	 * @param role
	 * @param target
	 * @param action
	 * @throws PermissionException 
	 */
	// TODO: grant permissions to ContentItems that hold the type
	@Override
	public void grantPermission(Role role, ContentItem type, String action, 
			Boolean inheritsPermissions) 
		throws PermissionException {
		if(role == null || type == null || action == null) {
			throw new PermissionException("Cannot assign permission " +
					"to null object: (role: " + role + ", type: " + 
					type + ", action: " + action + ")");
		}
		if(!hasPermission(role, type, action)) {
			Permission p = new Permission(type.getDelegate(), action, role);
			p.setTypeInheritance(inheritsPermissions);
			log.debug("Persisting permission (#0 - #1 - #2)", role.getName(), ((KiWiUriResource) type.getResource()).getUri(), action);
			cacheProvider.put(Constants.CACHE_PERMISSIONS, p.toString(), p);
			entityManager.persist(p);
		} else {
			Permission p = getPermission(role, type, action);
			p.setTypeInheritance(inheritsPermissions);
		}
	}
	
	/**
	 * Grants action-permission for all ContentItems in a 
	 * KnowledgeSpace to individual users
	 * 
	 * @param group
	 * @param target
	 * @param action
	 */
	@Override
	public void grantPermission(User user, KnowledgeSpaceBean target, String action) {
		
		// for all CIS that belong to the KnowledgeSpace: grant permissions
//		target.g
//		Permission p = new Permission(
//				target, action, 
//				new SimplePrincipal(owner.getName()));
//		return PermissionManager.instance().grantPermission(p);
	}
	
	/**
	 * Removes permissions from a user
	 * @param user
	 * @param target
	 * @param action
	 */
	@Override
	public void removePermission(User user, ContentItem target, String action) {
		if(hasPermission(user, target, action)) {
			Permission toRemove = getPermission(user, target, action);
			entityManager.remove(toRemove);
			cacheProvider.remove(Constants.CACHE_PERMISSIONS,toRemove.toString());
		}
	}
	
	/**
	 * 
	 */
	// TODO: grant permissions to ContentItems that hold the type
	public void removePermission(Role r, KiWiClass type, String action) {
		if(hasPermission(r, type.getDelegate(), action)) {
			Permission toRemove = getPermission(r, type.getDelegate(), action);
			entityManager.remove(toRemove);
			cacheProvider.remove(Constants.CACHE_PERMISSIONS,toRemove.toString());
		}
	}
	
	/**
	 * Removes permissions from a user
	 * @param user
	 * @param target
	 * @param action
	 */
	// TODO: grant permissions to ContentItems that hold the type
	@Override
	public void removePermission(User user, KiWiClass type, String action) {
		if(hasPermission(user, type.getDelegate(), action)) {
			Permission toRemove = getPermission(user, type.getDelegate(), action);
			entityManager.remove(toRemove);
			cacheProvider.remove(Constants.CACHE_PERMISSIONS,toRemove.toString());
		}
	}
	
	/**
	 * Removes permissions from a role
	 * @param user
	 * @param target
	 * @param action
	 */
	@Override
	public void removePermission(Role role, ContentItem target, String action) {
		if(hasPermission(role, target, action)) {
			Permission toRemove = getPermission(role, target, action);
			entityManager.remove(toRemove);
			cacheProvider.remove(Constants.CACHE_PERMISSIONS,toRemove.toString());
		}
	}
	
	/**
	 * Removes permissions from a role
	 * @param user
	 * @param target
	 * @param action
	 */
	@Override
	public void removePermission(Group group, ContentItem target, String action) {
		if(hasPermission(group, target, action)) {
			Permission toRemove = getPermission(group, target, action);
			entityManager.remove(toRemove);
			cacheProvider.remove(Constants.CACHE_PERMISSIONS,toRemove.toString());
		}
	}
	
	/**
	 * Queries whether a user has the permission to perform an
	 * action on a certain target (=contentItem)
	 * 
	 * @param owner
	 * @param ci
	 * @param action
	 * @return true if the permission exists and false otherwise
	 */
	@Override
//	@Transactional(TransactionPropagationType.REQUIRED)
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean hasPermission(User user, ContentItem ci, String action) {
		try {
			if(!Transaction.instance().isActive()) {
				log.error("Transaction is not active :(");
				return false;
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		Boolean p = (Boolean) cacheProvider.get(Constants.CACHE_PERMISSIONS,user.getLogin() + ":" + action + ":" + ci.getKiwiIdentifier());
		if(p == null) {
			Query q = entityManager.createNamedQuery("permissionService.getPermissionByUserAndTargetAndAction");
			q.setParameter("user", user);
			q.setParameter("action", action);
			q.setParameter("contentItem", ci);
			q.setMaxResults(1);
			
			if(q.getResultList().size() != 0) {
				cacheProvider.put(Constants.CACHE_PERMISSIONS, user.getLogin() + ":" + action + ":" + ci.getKiwiIdentifier(), Boolean.TRUE);
				return true;
			} else {
				return false;
			}
		} else {
			return p;
		}
	}
	
	/**
	 * Queries whether a user has the permission to perform an
	 * action on a certain type (=rdf:type)
	 * 
	 * @param owner
	 * @param ci
	 * @param action
	 * @return true if the permission exists and false otherwise
	 */
//	@Override
//	public boolean hasPermission(User user, KiWiClass type, String action) {
//		Query q = entityManager.createNamedQuery("permissionService.getPermissionByUserAndTypeAndAction");
//		q.setParameter("user", user);
//		q.setParameter("action", action);
//		q.setParameter("type", type.getDelegate());
//		
//		return q.getResultList().size() != 0;
//	}
	
	/**
	 * Queries whether a user has the permission to perform an
	 * action on a certain target (=contentItem)
	 * 
	 * @param owner
	 * @param ci
	 * @param action
	 * @return true if the permission exists and false otherwise
	 */
	@Override
	public boolean hasPermission(Role role, ContentItem ci, String action) {
		if(cacheProvider.get(Constants.CACHE_PERMISSIONS,role.getName() + ":" + action + ":" + ci.getKiwiIdentifier()) == null) {
			Query q = entityManager.createNamedQuery("permissionService.getPermissionByRoleAndTargetAndAction");
			q.setParameter("role", role);
			q.setParameter("action", action);
			q.setParameter("contentItem", ci);
			q.setMaxResults(1);
			
			if(q.getResultList().size() != 0) {
				cacheProvider.put(Constants.CACHE_PERMISSIONS, role.getName() + ":" + action + ":" + ci.getKiwiIdentifier(), Boolean.TRUE);
				return true;
			} else {
				return false;
			}
			
		} else {
			return true;
		}
	}
	
	@Override
	public boolean hasPermission(Group group, ContentItem ci, String action) {
		if(cacheProvider.get(Constants.CACHE_PERMISSIONS, group.getName() + ":" + action + ":" + ci.getKiwiIdentifier()) == null) {
			Query q = entityManager
				.createNamedQuery("permissionService.getPermissionByGroupAndTargetAndAction");
			q.setParameter("group", group);
			q.setParameter("action", action);
			q.setParameter("contentItem", ci);
			q.setMaxResults(1);
			
			if(q.getResultList().size() != 0) {
				cacheProvider.put(Constants.CACHE_PERMISSIONS, group.getName() + ":" + action + ":" + ci.getKiwiIdentifier(), Boolean.TRUE);
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	
	/**
	 * Queries whether a user has the permission to perform an
	 * action on a certain type
	 * 
	 * @param owner
	 * @param type
	 * @param action
	 * @return true if the permission exists and false otherwise
	 */
//	@Override
//	public boolean hasPermission(Role role, KiWiClass type, String action) {
//		Query q = entityManager.createNamedQuery("permissionService.getPermissionByRoleAndTypeAndAction");
//		q.setParameter("role", role);
//		q.setParameter("action", action);
//		q.setParameter("type", type);
//		
//		return q.getResultList().size() != 0;
//	}
	
	/**
	 * Returns all permissions assigned for one contentItem
	 * 
	 * @param item
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Permission> getPermissionsByTarget(ContentItem item) {
		Query q = entityManager
			.createNamedQuery("permissionService.getPermissionByTarget");
		q.setParameter("contentItem", item);
		
		return q.getResultList();
	}
	
	/**
	 * Returns all permissions assigned for one contentItem
	 * 
	 * @param item
	 * @return
	 */
//	@Override
//	@SuppressWarnings("unchecked")
//	public List<Permission> getPermissionsByType(KiWiClass type) {
//		Query q = entityManager
//			.createNamedQuery("permissionService.getPermissionByType");
//		q.setParameter("type", type);
//		
//		return q.getResultList();
//	}
	
	/**
	 * Returns all permissions assigned for one contentItem
	 * 
	 * @param item
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Permission> getTypePermissions() {
		Query q = entityManager
			.createNamedQuery("permissionService.getTypePermissions");
		
		log.info("type permissions: #0 ", q.getResultList());
		
		return q.getResultList();
	}
	
	/**
	 * Returns all permissions assigned to one user
	 * 
	 * @param user
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Permission> getPermissionsByRecipient(User user) {
		Query q = entityManager
			.createNamedQuery("permissionService.getPermissionByUser");
		q.setParameter("user", user);
		
		return q.getResultList();
	}
	
	/**
	 * Returns all permissions assigned to one user for one contentItem
	 * 
	 * @param user
	 * @param item
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Permission> getPermissionsByRecipientAndTarget(User user, ContentItem item) {
		Query q = entityManager
			.createNamedQuery("permissionService.getPermissionByUserAndTarget");
		q.setParameter("user", user);
		q.setParameter("contentItem", item);
		
		return q.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Permission> getPermissionsByGroupAndTarget(Group group,
			ContentItem item) {
		Query q = entityManager
		.createNamedQuery("permissionService.getPermissionByGroupAndTarget");
	q.setParameter("group", group);
	q.setParameter("contentItem", item);
	
	return q.getResultList();
	}
	
	/**
	 * Returns all permissions assigned to one user for one contentItem
	 * 
	 * @param user
	 * @param item
	 * @return
	 */
//	@Override
//	@SuppressWarnings("unchecked")
//	public List<Permission> getPermissionsByRecipientAndType(User user, KiWiClass type) {
//		Query q = entityManager
//			.createNamedQuery("permissionService.getPermissionByUserAndType");
//		q.setParameter("user", user);
//		q.setParameter("type", type);
//		
//		return q.getResultList();
//	}
	
	/**
	 * returns the users that have permissions on a rdf:type
	 * 
	 * @param ci
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> getPermissionOwnerByType(KiWiClass type) {
		Query q = entityManager.createNamedQuery("permissionService.getPermissionOwnerByType");
		q.setParameter("type", type);
		
		return q.getResultList();
	}
	
	/**
	 * returns the users that have permissions on a contentItem
	 * 
	 * @param ci
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> getPermissionOwnerByTarget(ContentItem ci) {
		Query q = entityManager.createNamedQuery("permissionService.getPermissionOwnerByTarget");
		q.setParameter("contentItem", ci);
		
		return q.getResultList();
	}
	
	/**
	 * returns the groups that have permissions on a contentItem
	 * 
	 * @param ci
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> getPermissionGroupByTarget(ContentItem ci) {
		Query q = entityManager.createNamedQuery("permissionService.getPermissionGroupByTarget");
		q.setParameter("contentItem", ci);
		
		return q.getResultList();
	}
	
	/**
	 * returns the permission object for a user on a 
	 * contentItem for a certain action
	 * 
	 * @param user
	 * @param ci
	 * @param action
	 * @return
	 */
	@Override
	public Permission getPermission(User user, ContentItem ci, String action) {
		Query q = entityManager.
			createNamedQuery("permissionService.getPermissionByUserAndTargetAndAction");
		q.setParameter("user", user);
		q.setParameter("action", action);
		q.setParameter("contentItem", ci);
		
		q.setMaxResults(1);
		
		try {
			return (Permission) q.getSingleResult();
		} catch(NoResultException ex) {
			return null;
		}
	}
	
	/**
	 * returns the permission object for a role on a 
	 * contentItem for a certain action
	 * 
	 * @param role
	 * @param ci
	 * @param action
	 * @return
	 */
	@Override
	public Permission getPermission(Role role, ContentItem ci, String action) {
		Query q = entityManager.
			createNamedQuery("permissionService.getPermissionByRoleAndTargetAndAction");
		q.setParameter("contentItem", ci);
		q.setParameter("role", role);
		q.setParameter("action", action);
		
		q.setMaxResults(1);
		
		try {
			return (Permission) q.getSingleResult();
		} catch(NoResultException ex) {
			return null;
		}
	}
	
	@Override
	public Permission getPermission(Group group, ContentItem ci,
			String action) {
		Query q = entityManager.
		createNamedQuery("permissionService.getPermissionByGroupAndTargetAndAction");
		q.setParameter("contentItem", ci);
		q.setParameter("group", group);
		q.setParameter("action", action);
		
		q.setMaxResults(1);
		
		try {
			return (Permission) q.getSingleResult();
		} catch(NoResultException ex) {
			return null;
		}
	}
}
