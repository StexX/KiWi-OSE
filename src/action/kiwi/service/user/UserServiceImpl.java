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

package kiwi.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.transaction.SystemException;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.query.sparql.SparqlService;
import kiwi.api.role.RoleService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserServiceLocal;
import kiwi.api.user.UserServiceRemote;
import kiwi.exception.FoafProfileAssociationException;
import kiwi.exception.UserExistsException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.Role;
import kiwi.model.user.User;
import kiwi.util.KiWiCollections;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.management.IdentityManagementException;
import org.jboss.seam.security.management.IdentityManager;
import org.jboss.seam.transaction.Transaction;

/**
 * A service component that provides various access methods for retrieving users
 * from the database.
 * 
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("userService")
@AutoCreate
@Scope(ScopeType.STATELESS)
//@Transactional
public class UserServiceImpl implements UserServiceLocal, UserServiceRemote {

	/**
	 * Inject a Seam logger for logging purposes inside this component.
	 */
	@Logger
	private static Log log;

	/** 
	 * The entity manager used by this KiWi system. We could use kiwiEntityManager,
	 * put for the purpose of just loading users, the ordinary entityManager is
	 * sufficient and more efficient.
	 */
	@In
	private EntityManager entityManager;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private TripleStore tripleStore;
	
	@In("kiwi.query.sparqlService")
	private SparqlService sparqlService;	
	
	@In
	private RoleService roleService;
	
	/**
	 * Retrieve a list of users via fulltext search over login, firstname and lastname. The 
	 * keyword given as parameter is used as a Lucene search string.
	 * 
	 * @param keyword the keyword to search for (lucene query)
	 * @return the list of users that match the fulltext search
	 */
	public List<User> getUsersByKeyword(String keyword) {
		List<User> result;
		// perform lucene keyword search over users
		QueryParser parser = new MultiFieldQueryParser(
				new String[] { "user.login", "user.firstName", "user.lastName" }, new StopAnalyzer());

		try {
			org.apache.lucene.search.Query luceneQuery = parser.parse(keyword);


			FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search
					.createFullTextEntityManager(entityManager);
			FullTextQuery fullTextQuery = (FullTextQuery) fullTextEntityManager
					.createFullTextQuery(luceneQuery, User.class);

			// eliminate duplicates and sort by using a TreeSet
			result = fullTextQuery.getResultList();
			for(User u : result) {
				//tripleStore.merge(u,false);
			}

		} catch (Exception ex) {
			log.error("error while executing full text query", ex);
			result = new ArrayList<User>();
		}
		return result;
	}
	
	/**
	 * Create a new user with the provided login. The method first 
	 * checks of a user with this login already exists; if yes, an exception is thrown. The
	 * method does not persist the user; this needs to be done by the caller.
	 * 
	 * @param login login of the user to create
	 * @return the newly created user.
	 */
	@Override
	//@Restrict("#{s:hasRole('admin')}")
	public User createUser(String login, String password) throws UserExistsException {
		return createUser(login, "", login, password);
	}
	
	

	/**
	 * Create a new user with the provided login, first name and last name. The method first 
	 * checks of a user with this login already exists; if yes, an exception is thrown. The
	 * method does not persist the user; this needs to be done by the caller.
	 * 
	 * @param login login of the user to create
	 * @param firstName first name of the user to create
	 * @param lastName last name of the user to create
	 * @return the newly created user.
	 */
	@Override
	public User createUser(final String login, final String firstName, 
			final String lastName, final String password) throws UserExistsException {
		
		entityManager.setFlushMode(FlushModeType.AUTO);
		try {
			IdentityManager idMgr = IdentityManager.instance();
			if(idMgr.userExists(login)){
				log.error("could not create user #0", login);
				throw new UserExistsException("user with login "+login+" already exists");
			}
			idMgr.createUser(login, password,firstName, lastName);
		} catch(IdentityManagementException ex) {
			log.error("error creating user with login #0", login);
			ex.printStackTrace();
		}

		Query q = entityManager.createNamedQuery("currentUserFactory.getUserByLogin");
		q.setParameter("login", login);
		
		try {
			q.setMaxResults(1);
			User u = (User) q.getSingleResult();
			u.setPassword(password);
			tripleStore.persist(u);
			return u;
		} catch(NoResultException ex) {
			log.error("Could not persist user #0 ", login);
			return null;
		}
	}
	
	/**
	 * This method will, if necessary, create a 
	 * role and grant that role to a certain user. 
	 * 
	 * @param login: the username
	 * @param role: the rolename
	 */
	@Override
	//@Restrict("#{s:hasRole('admin')}")
	public boolean addRole(final User user, final String role) {
		return addRole(user.getLogin(), role);
	}
	
	/**
	 * This method will, if necessary, create a 
	 * role and grant that role to a certain user. 
	 * 
	 * @param login: the username
	 * @param role: the rolename
	 */
	@Override
	//@Restrict("#{s:hasRole('admin')}")
	public synchronized boolean addRole(final String login, final String role) {
		Role r = null;
		if((r = roleService.getRoleByName(role)) == null) {
			r = roleService.createRole(role);
		}
		
//		if(IdentityManager.instance().getIdentityStore().grantRole(login, role)) {
//			return true;
//		} else {
			if(!IdentityManager.instance().getIdentityStore().grantRole(login, role)) {
				User u = getUserByLogin(login);
//			if(!hasRole((u = getUserByLogin(login)), role)) {
				u.getRoles().add(r);
				r.getUser().add(u);
			}
			return true;
				
//			} else {
//				return false;
//			}
//		}
//		return IdentityManager.instance().getIdentityStore().grantRole(login, role);
//		entityManager.flush();
	}
	
	@Override
	public boolean hasRole(final User user, final String rolename) {
		try {
			if(!Transaction.instance().isActive()) {
				log.error("No active transaction :(");
				return false;
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		List<User> usersInRole = roleService.getUsersInRole(rolename);
		return usersInRole.contains(user);
	}

	@Override
	public boolean hasRole(final User user, final Role role) {
		List<User> usersInRole = roleService.getUsersInRole(role.getName());
		return usersInRole.contains(user);
	}
	
	@Override
	public synchronized void removeRole(final User user, final Role role) {
		user.getRoles().remove(role);
		role.getUser().remove(user);
	}
	
	@Override
	public synchronized void removeRole(final User user, final String rolename) {
		removeRole(user, roleService.getRoleByName(rolename));
	}
	
	/**
	 * Return a user by login. The user is looked up in the database and returned. In case
	 * no user with this login is found, this method returns null.
	 * 
	 * @param login the login to look for
	 * @return the user with the given login, or null if no such user exists
	 * @see kiwi.api.user.UserService#getUserByLogin(java.lang.String)
	 */
	public User getUserByLogin(String login) {
		User u = null;

        Query q = entityManager.createNamedQuery("currentUserFactory.getUserByLogin");
        q.setParameter("login", login);

        try {
        	q.setMaxResults(1);
            u = (User) q.getSingleResult();
        } catch(NonUniqueResultException ex) {
                log.error("More than one user with login #0 found", login);
                ex.printStackTrace();
        } catch(NoResultException ex) {
                log.error("User #0 not found", login);
        }
        // TODO: enable when working (...rly necessary?)
        //tripleStore.merge(u, false);
        return u;
	}
	
	/**
	 * Return a user by his/her facebook account. The user is looked up in the database and returned. 
	 * In case no user with this fbId is found, this method returns null.
	 * 
	 * @param fbId the facebook id to look for
	 * @return the user with the given fbId, or null if no such user exists
	 * @see kiwi.api.user.UserService#getUserByFbId(java.lang.String)
	 */
	public User getUserByFbId(String fbId) {
		User result;
		javax.persistence.Query q = entityManager
			.createNamedQuery("userService.getUserByFacebookAcc");
		q.setParameter("fbId", fbId);
		q.setHint("org.hibernate.cacheable", true);
		try {
			result = (User) q.getSingleResult();
			
			// TODO: enable when working
			//tripleStore.merge(result, false);
			
		} catch (NoResultException ex) {
			result = null;
		}
		return result;
	}
	
	/**
	 * Return a user by his/her web id. The user is looked up in the database and returned. 
	 * In case no user with this webId is found, this method returns null.
	 * 
	 * @param webId the web id to look for
	 * @return the user with the given fbId, or null if no such user exists
	 * @see kiwi.api.user.UserService#getUserByFbId(java.lang.String)
	 */
	public User getUserByWebId(String webId) {
		User result;
		javax.persistence.Query q = entityManager
			.createNamedQuery("userService.getUserByWebId");
		q.setParameter("webId", webId);
		q.setHint("org.hibernate.cacheable", true);
		try {
			result = (User) q.getSingleResult();
			
			// TODO: enable when working
			//tripleStore.merge(result, false);
			
		} catch (NoResultException ex) {
			result = null;
		}
		return result;
	}
	
	/**
	 * Return the list of all users. Might be slow when many users exist in the system.
	 * 
	 * @return the list of all users active in the system, ordered by login
	 * @see kiwi.api.user.UserService#getUsers()
	 */
	public List<User> getUsers() {
		javax.persistence.Query q = entityManager
			.createQuery("select t from kiwi.model.user.User t order by t.login");
		return q.getResultList();
	}

	/**
	 * Return the list of the login names of all users.
	 * 
	 * @return the list of all users active in the system, ordered by login
	 * @see kiwi.api.user.UserService#getUsers()
	 */
	@Override
	public List<String> getAllUsernames() {
		javax.persistence.Query q = entityManager
			.createQuery("select u.login from kiwi.model.user.User u order by u.login");
		return q.getResultList();
	}


	/* (non-Javadoc)
	 * @see kiwi.api.user.UserService#getAllCreatedUsers()
	 */
	public List<User> getAllCreatedUsers() {
		javax.persistence.Query q = entityManager
			.createQuery("select t from kiwi.model.user.User t where t.id!=1 order by t.login");
		return q.getResultList();
	}
	
	/**
	 * Get users by first and last name. The user is looked up in the database and returned.
	 * In case no user with this combination of first name and last name is found, this
	 * method returns null.
	 * 
	 * @param firstName the first name of the user to look for
	 * @param lastName the last name of the user to look for
	 * @return the list of users with the given first name and last name if exists, or empty list otherwise
	 * @see kiwi.api.user.UserService#getUserByName(java.lang.String, java.lang.String)
	 */
	public List<User> getUsersByName(String firstName, String lastName) {
		javax.persistence.Query q = entityManager
				.createNamedQuery("userService.getUserByName");
		q.setParameter("fn", firstName);
		q.setParameter("ln", lastName);
		
		return q.getResultList();
	}
	
	/**
	 * Get user by email address. The user is looked up in the database and returned.
	 * The email address should be unique, thus just one user will be returned.
	 * In case no user with this email address is found, this
	 * method returns null.
	 * 
	 * @param firstName the first name of the user to look for
	 * @param lastName the last name of the user to look for
	 * @return the list of users with the given first name and last name if exists, or empty list otherwise
	 * @see kiwi.api.user.UserService#getUserByName(java.lang.String, java.lang.String)
	 */
	public User getUserByEmail(String email) {
		try {
			javax.persistence.Query q = entityManager
					.createNamedQuery("userService.getUserByEmail");
			q.setParameter("email", email);
			
			q.setMaxResults(1);
			return (User) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Get a user by the uri identifying him. Loads the user with the given URI
	 * from the database. If no such user exists, returns null.
	 * 
	 * @param uri the uri to look for
	 * @return the user with this uri, or null if there is no such user
	 * @see kiwi.api.user.UserService#getUserByUri(java.lang.String)
	 */
	public User getUserByUri(String uri) {
		javax.persistence.Query q = entityManager
				.createNamedQuery("userService.getUserByUri");
		q.setParameter("uri", uri);
		q.setHint("org.hibernate.cacheable", true);
		
		try {
			User res = (User) q.getSingleResult();
			// TODO: enable when working
			//tripleStore.merge(res, false);
			log.debug("loaded user with uri #0",uri);
			return res;
		} catch(NoResultException ex) {
			log.warn("could not find user with uri #0",uri);
			return null;
		}
	}
	
	/**
	 * Returns a user via the openId
	 * @param openId
	 * @return
	 * @throws FoafProfileAssociationException 
	 */
	public User queryUserByFoafProfile(String foafprofileURI) throws FoafProfileAssociationException {
		Set<KiWiNode> nodes = KiWiCollections.toSet(sparqlService.queryNode("SELECT ?U WHERE { ?U <"+Constants.NS_KIWI_CORE+"foafprofile> <"+ foafprofileURI +"> }", 
				KiWiQueryLanguage.SPARQL));
		KiWiUriResource userResource = null;
		if(nodes.size() == 1) {
			userResource = (KiWiUriResource) nodes.iterator().next();
		} else if(nodes.size() > 1) {
			throw new FoafProfileAssociationException("More than one user associated with the same foafprofile");
		}
		User u = null;
		if(userResource != null) {
			u = (User) getUserByUri(userResource.getUri());
		}
		return u;
	}

	/**
	 * Gives back the user by his/her profile ContentItem.
	 * @param contentItem
	 * @return the user or null if contentItem is no user profile.
	 */
	@Override
	public User getUserByProfile(ContentItem contentItem) {
		
		return getUserByUri(((KiWiUriResource)contentItem.getResource()).getUri());
		
	}

	/**
	 * Check whether a user with the given login already exists. If so,
	 * returns true. Otherwise returns false.
	 * 
	 * @param login the login to look for
	 * @return true if a user with this login already exists, false otherwise
	 * @see kiwi.api.user.UserService#userExists(java.lang.String)
	 */
	public boolean userExists(String login) {
		javax.persistence.Query q = entityManager.createNamedQuery("currentUserFactory.getUserByLogin");
		q.setParameter("login", login);
		q.setHint("org.hibernate.cacheable", true);
		return q.getResultList().size() == 1;
	}

	
	
	/**
	 * Store all information about the user in the KiWi database. Makes sure that user as well
	 * as associated content item information is properly persisted.
	 * 
	 * @param user the user to save
	 */
//	@Override
//	public void saveUser(User user) {
//		kiwiEntityManager.persist(user);
//		kiwiEntityManager.persist(user.getResource().getContentItem());
//		if(user.getProfilePhoto()!=null)
//			kiwiEntityManager.persist(user.getProfilePhoto());
//			
//	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.user.UserService#addMySimilarContentItems(kiwi.model.user.User, kiwi.model.content.ContentItem)
	 */
	public void addMySimilarContentItems(User user, ContentItem contentItem) {
		User newUser = existSimilarItem(user,contentItem);
		if (newUser!=null) {
			user.addSimilarItem(contentItem);
			kiwiEntityManager.persist(user);	
		}
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.user.UserService#removeMySimilarContentItems(kiwi.model.user.User, kiwi.model.content.ContentItem)
	 */
	public void removeMySimilarContentItems(User user, ContentItem contentItem) {
		User detached = getUserByLogin(user.getLogin());
		//user = kiwiEntityManager.merge(user);
		getMySimilarContentItems(detached);
		detached.getSimilarItems().remove(contentItem);
		kiwiEntityManager.persist(detached);
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.user.UserService#getMySimilarContentItems(kiwi.model.user.User)
	 */
	public List<ContentItem> getMySimilarContentItems(User user) {
		List<ContentItem> similarItems = new ArrayList<ContentItem>();
		User qresult =  this.kiwiEntityManager.merge(user);
		if (qresult.getSimilarItems()==null || qresult.getSimilarItems().isEmpty()) {
			String s = "select u from kiwi.model.user.User u where u.id =:userId";
			javax.persistence.Query q = entityManager.createQuery(s);
			q.setParameter("userId", user.getId());
			try {
				qresult = (User)q.getSingleResult();
			} catch (NoResultException ex) {
				log.warn("error while calculating existSimilarItem");
			}
		}
		similarItems.addAll(new ArrayList<ContentItem>(qresult.getSimilarItems()));		
		return similarItems;
	
	}

	
	/**
	 * @param user
	 * @param contentItem
	 * @return
	 */
	private User existSimilarItem(User user,ContentItem contentItem) {
		User qresult = null;
		String s = "select u from kiwi.model.user.User u left outer join fetch u.similarItems where u.id =:userId and :contentItem not in elements(u.similarItems)";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("userId", user.getId());
		q.setParameter("contentItem", contentItem);
		try {
			qresult = (User)q.getSingleResult();
		} catch (NoResultException ex) {
			log.warn("error while calculating existSimilarItem");
		}
		return qresult;
	}
	
	/**
	 * Return the anonymous user of this KiWi system.
	 * <p>
	 * If the anonymous user is already stored in the database, this method
	 * tries to load him. Otherwise, he is newly created and persisted in the
	 * entityManager.
	 * 
	 * @return the anonymous user of this KiWi system
	 * @throws UserExistsException
	 */
//	@Restrict("#{s:hasRole('admin')}")
	public User getAnonymousUser() {
		
		User anonymousUser = getUserByLogin("anonymous");
		
		if(anonymousUser != null) {
			tripleStore.refresh(anonymousUser, false);
			log.info("loaded anonymous user from database");
			return anonymousUser;
		} else {
			try {
				anonymousUser = createUser("anonymous","Anonymous","User", "noNeed4Pass");
				addRole("anonymous", "read");
			} catch (UserExistsException e) {
				// should never happen, since we asked right before
				e.printStackTrace();
			}
			log.info("created new anonymous user");
			return anonymousUser;
		}

	}
}
