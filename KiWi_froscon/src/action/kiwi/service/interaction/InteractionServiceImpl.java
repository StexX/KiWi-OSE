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

package kiwi.service.interaction;

import static kiwi.model.kbase.KiWiQueryLanguage.SPARQL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.interaction.InteractionServiceLocal;
import kiwi.api.interaction.InteractionServiceRemote;
import kiwi.api.rating.RatingDataFacade;
import kiwi.api.rating.RatingFacade;
import kiwi.api.user.UserService;
import kiwi.model.Constants;
import kiwi.model.activity.CommentActivity;
import kiwi.model.content.ContentItem;
import kiwi.model.interaction.UserInteraction;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Fred Durao
 * 
 */
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("interactionService")
public class InteractionServiceImpl implements InteractionServiceLocal,
		InteractionServiceRemote {

	@Logger
	private Log log;

	@In
	private EntityManager entityManager;

	@In(create = true)
	private KiWiEntityManager kiwiEntityManager;

	@In
	private UserService userService;	

	/*
	 * (non-Javadoc)
	 * 
	 * @see kiwi.api.interaction.InteractionService#getUserInteractivity(kiwi.model.user.User,
	 *      kiwi.model.user.User)
	 */
	public float computeUserInteractivity(User user1, User user2) {
		float userInteractivityLevel = 0f;
		if (user1 != null && user2 != null && user1!=user2) {
			userInteractivityLevel =   getUserCommentInteractivity(user1, user2).size()+
									 
				                       getUserTagInteractivity(user1,user2).size()+
									 
				  				       getUserEditingInteractivity(user1,user2).size() +
									 
									   getUserRatingInteractivity(user1,user2).size();

			userInteractivityLevel = userInteractivityLevel/4;
		}
		return userInteractivityLevel;
	}

	/**
	 * @return
	 */
	public boolean deleteUserInteractivity(){
		List<UserInteraction> uss = this.listUserInteractions();
		for (UserInteraction us : uss) {
			entityManager.remove(us);
//			entityManager.flush();
		}
		return listUserInteractions().isEmpty();
	} 	
	
	
	/**
	 * computeUserSkills
	 */
	public void computeUserInteractivity(){
		if (deleteUserInteractivity()) {
			List<String> comparisions = new ArrayList<String>();
			List<User> newWsers = new ArrayList<User>();
			List<User> users = userService.getAllCreatedUsers();
			newWsers = users;
			for (User user : newWsers) {
				for (User user2 : newWsers) {
					if (user!=user2  && !comparisions.contains(user.getLogin()+user2.getLogin())) {
						//System.out.println("computeUserInteractivity...USER 1 "+user.getFirstName()+" USER 2 "+ user2.getFirstName());
						float userInterctivityValue = computeUserInteractivity(user,user2);
						if (userInterctivityValue>0f) {
							UserInteraction userInteraction = new UserInteraction(user,user2,userInterctivityValue);
							kiwiEntityManager.persist(userInteraction);
//							kiwiEntityManager.flush();
							comparisions.add(user.getLogin()+user2.getLogin());
						}
					}
				}
			}
		}		
	}


	/* (non-Javadoc)
	 * @see kiwi.api.interaction.InteractionService#getUserInteractionByUser(kiwi.model.user.User)
	 */
	@SuppressWarnings("unchecked")
	public List<UserInteraction> getUserInteractionByUser(User user){
		List<UserInteraction> result = null;	
		String s = "select s from kiwi.model.interaction.UserInteraction s where s.user = :user";
			javax.persistence.Query q = entityManager.createQuery(s);
			q.setParameter("user", user);
			try {
				result = (List<UserInteraction>)q.getResultList();
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				log.warn("error while listing user skills: query failed");
			}
			if (result == null) {
				return Collections.EMPTY_LIST;
			} else {
				return result;
			}
	}
	
	/**
	 * @param user
	 * @param user2
	 * @return
	 */
	public UserInteraction getUserInteractionByUsers(User user, User user2){
		UserInteraction result = null;	
		String s = "select s from kiwi.model.interaction.UserInteraction s where s.user = :user and s.user2 =:user2";
			javax.persistence.Query q = entityManager.createQuery(s);
			q.setParameter("user", user);
			q.setParameter("user2", user2);
			try {
				result = (UserInteraction)q.getSingleResult();
			} catch (NoResultException ex) {
				log.warn("error while listing user interactions by two users");
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				log.warn("error while listing user skills: query failed");
			}
			return result;
	}	
	
	/* (non-Javadoc)
	 * @see kiwi.api.interaction.InteractionService#listUserInteractions()
	 */
	@SuppressWarnings("unchecked")
	public List<UserInteraction> listUserInteractions(){
		List<UserInteraction> result = null;	
		String s = "from kiwi.model.interaction.UserInteraction u";
			javax.persistence.Query q = entityManager.createQuery(s);
			try {
				result = (List<UserInteraction>)q.getResultList();
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				log.warn("error while listing user skills: query failed");
			}
			if(result == null ) {
				return Collections.EMPTY_LIST;
			} else {
				return result;
			}
	}	


	/**
	 * @param user1
	 * @param user2
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unchecked", "unchecked", "unused" })
	private List<ContentItem> getUserTagInteractivity(User user1, User user2) {
		List<ContentItem> result = new LinkedList<ContentItem>();
		String s = "select distinct ci "
				+ "from kiwi.model.content.ContentItem ci left join fetch ci.resource, kiwi.model.tagging.Tag tag"
				+ " where tag.taggedResource.id = ci.id and ci.author=:user1 and tag.taggingResource.author =:user2";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("user1", user1);
		q.setParameter("user2", user2);
		q.setHint("org.hibernate.cacheable", true);
		try {
			result = (List<ContentItem>) q.getResultList();
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log
					.warn("error while listing user tag interactivity: query failed");
		}
		if (result == null) {
			return Collections.EMPTY_LIST;
		} else {
			return result;
		}
	}

	/**
	 * @param user1
	 * @param user2
	 * @return content items authored by user1 rated by user2
	 */
	@SuppressWarnings({ "unused", "unchecked", "unchecked" })
	private List<ContentItem> getUserRatingInteractivity(User user1, User user2) {
		List<ContentItem> result = new LinkedList<ContentItem>();

		String s = "SELECT ?ci WHERE { " +
					" ?s  <" + Constants.NS_RDF       + "type>          <"+ Constants.NS_KIWI_CORE + "RatingData> . " +
					" ?s  <" + Constants.NS_KIWI_CORE + "author>        <"+ user2.getResource()    + "> . " + 
					" ?s  <" + Constants.NS_KIWI_CORE + "hasRatingFacade> ?ci . "+
					" ?ci <" + Constants.NS_KIWI_CORE + "author>        <"+ user1.getResource()    +"> . }";
			javax.persistence.Query q = kiwiEntityManager.createQuery(s, SPARQL,
					ContentItem.class);  
		q.setHint("org.hibernate.cacheable", true);
		try {
			result = (List<ContentItem>) q.getResultList();
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log
					.warn("error while listing user Rating Interactivity:  query failed");
		}

		if (result == null) {
			return Collections.EMPTY_LIST;
		} else {
			return result;
		}
	}
	
	/**
	 * @param item
	 * @param user
	 * @return
	 */
	private boolean isRatedByUser(ContentItem item, User user) {
		boolean isRated = false;
		final RatingFacade ratingF = kiwiEntityManager.createFacade(
				item, RatingFacade.class);
		for(RatingDataFacade ratingData:ratingF.getRatingDataFacades()){
			if(user.equals(ratingData.getAuthor())){
				isRated = true;
			}
		}
		return isRated;
	}

	/**
	 * @param user1
	 * @param user2
	 * @return
	 */
	@SuppressWarnings({ "unused", "unchecked", "unchecked" })	
	private List<CommentActivity> getUserCommentInteractivity(User user1, User user2) {
		List<CommentActivity> result = new LinkedList<CommentActivity>();

		String s = "select a " +
		"from CommentActivity a inner join fetch a.comment left outer join a.contentItem as cia " +
		"where cia.author = :user1 and a.comment.author = :user2";
		
		Query q = entityManager
				.createQuery(s);
		q.setParameter("user1", user1);
		q.setParameter("user2", user2);
		q.setHint("org.hibernate.cacheable", true);
		try {
			result = (List<CommentActivity>) q.getResultList();
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log.warn("error while listing user: query failed");
		}

		if (result == null) {
			return Collections.EMPTY_LIST;
		} else {
			return result;
		}
	}

	/**
	 * @param user1
	 * @param user2
	 * @return
	 */
	@SuppressWarnings({ "unused", "unchecked", "unchecked" })
	private List<ContentItem> getUserEditingInteractivity(User user1, User user2) {
		List<ContentItem> result = new LinkedList<ContentItem>();

		String s = " select ci from ContentItem ci join fetch ci.resource left outer join fetch ci.textContent tc "+
				   " where ci.author.login =:user1 and tc.contentItem.author.login =:user2";

		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("user1", user1.getLogin());
		q.setParameter("user2", user2.getLogin());
		q.setHint("org.hibernate.cacheable", true);
		try {
			result = (List<ContentItem>) q.getResultList();
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log.warn("error while listing user: query failed");
		}

		if (result == null) {
			return Collections.EMPTY_LIST;
		} else {
			return result;
		}
	}
}