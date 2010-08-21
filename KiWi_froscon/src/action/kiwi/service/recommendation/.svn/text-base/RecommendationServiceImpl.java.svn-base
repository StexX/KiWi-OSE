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


package kiwi.service.recommendation;

import static kiwi.model.kbase.KiWiQueryLanguage.SPARQL;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.group.GroupService;
import kiwi.api.recommendation.RecommendationServiceLocal;
import kiwi.api.recommendation.RecommendationServiceRemote;
import kiwi.api.search.SemanticIndexingService;
import kiwi.api.socialcapital.SocialCapitalService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.recommendation.ContactRecommendation;
import kiwi.model.recommendation.MySimilarRecommendation;
import kiwi.model.recommendation.SocialCapital;
import kiwi.model.recommendation.SocialCapitalRecommendation;
import kiwi.model.tagging.Tag;
import kiwi.model.user.Group;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import pitt.search.semanticvectors.ObjectVector;
import pitt.search.semanticvectors.Search;
import pitt.search.semanticvectors.SearchResult;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;

/**
 * @author Peter Dolog, Fred Durao
 *
 */
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("recommendationService")
//recommends content items which are tagged by tags of different persons but the same as tagged by current user on the current content item
@NamedQueries({
@NamedQuery(name="RecommendationService.searchContentItembyTag",
			    query="select ci " +
			    	  "from kiwi.model.content.ContentItem ci left outer join ci.tags as tag " +
					  "where tag.taggingResource.title in (:tags) " +
					  "and ci.author.login <> :cu and tag.taggedBy.login <> :cu " +
					  "and ci.deleted = false " +
					  "and ci.title != (:title) " +
					  "order by ci.title")
					  })

public class RecommendationServiceImpl implements RecommendationServiceLocal, RecommendationServiceRemote {

	@In(create=true)
	private User currentUser;	

	@Logger
	private Log log;

	@In
	private EntityManager entityManager;

	
	@In(create=true)
	private KiWiEntityManager kiwiEntityManager;	
	
	@In(create=true)
	private GroupService groupService;

	private int MAX_ITEMS = 50;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private TaggingService taggingService;
	
	@In
	private UserService userService;
	
	@In
	private SocialCapitalService socialCapitalService;
	
	@In
	private TripleStore tripleStore;	

	
	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.RecommendationService#getRecommendations(kiwi.model.content.ContentItem, kiwi.model.user.User)
	 */
	@SuppressWarnings("unchecked")
	public List<ContentItem> getRecommendations(ContentItem currentItem, User currentUser) {
		log.info("Calculating similar recommendations");

		Collection<Tag> tags = taggingService.getTags(currentItem);

		
		List<ContentItem> result = new LinkedList<ContentItem>();

		Collection<String> ltags = new HashSet<String>();

		if (tags.isEmpty()) {

		} else {
			for (Tag t : tags) {
				// just tags which are of the current user (TODO: this is probably a bug!)
				if (t.getTaggedBy() == currentUser)
					log.debug("Tags: " + t.getTaggingResource().getTitle());
				ltags.add(t.getTaggingResource().getTitle());
				
			}

			// basic select statement for content items based on tags
			// recommends content items which are tagged by tags of different
			// persons but the same as tagged by current user on the current
			// content item
			String s = "select ci "
				+ "from kiwi.model.content.ContentItem ci left join fetch ci.resource, kiwi.model.tagging.Tag tag "
				+ "where tag.taggedResource.id = ci.id and tag.taggingResource.title in (:tags) ";

			

			// if author is current user. This ensures that just those content
			// items are recommended which are of a different author
			if (currentUser.getLogin() != null) {
				s += "and ci.author.login <> :cu and tag.taggedBy.login <> :cu ";
			}
			// this filters out content items which are the same as the current
			// content item displayed
			if (currentItem.getTitle() != null) {
				s += "and ci.title <> :title ";
			}

			s += "and ci.deleted = false"; // group by ci.id ";
			//s += "and ci.deleted = false group by ci.id ";

			javax.persistence.Query q = entityManager.createQuery(s);
			q.setParameter("tags", ltags);
			//log.info("Calculating recommendations, current user #0",currentUser.getLogin());
			if (currentUser.getLogin() != null) {
				q.setParameter("cu", currentUser.getLogin());
			}
			//log.info("Calculating recommendations, current content item #0",currentItem.getTitle());
			if (currentItem.getTitle() != null) {
				q.setParameter("title", currentItem.getTitle());
			}
			
			// limit to at most 10 recommendations
			q.setMaxResults(10);
			
			q.setHint("org.hibernate.cacheable", true);

			try {
				result = (List<ContentItem>)q.getResultList();
			} catch (PersistenceException ex) {
				log.warn("error while listing recommendations: query failed");
			}
		}
		result = removeDuplicates(result);
		return result;
	}
	


	@SuppressWarnings("unchecked")
	public List<ContentItem> getRuleBasedRecommendations(User currentUser) {
		log.info("Retrieving rule based recommendations");
		List<ContentItem> result = new LinkedList<ContentItem>();
		List<KiWiTriple> triples = tripleStore.getTriplesByPO(Constants.NS_KIWI_CORE+"recommendTo", Constants.NS_KIWI_CORE+currentUser.getLogin());
		for (KiWiTriple kiwTriple : triples) {
			result.add(kiwTriple.getSubject().getContentItem());
		}
		result = removeDuplicates(result);
		return result;
	}	
	
	
	/**
	 * Method created for removing duplicate content item entries
	 * @param result
	 * @return
	 */
	private List<ContentItem> removeDuplicates(List<ContentItem> result){
		Set<ContentItem> contentItems = new HashSet<ContentItem>();
		for (ContentItem contentItem : result) {
				contentItems.add(contentItem);
		}
		result = new ArrayList<ContentItem>(contentItems);
		result = retrieveSubSet(result);
		return result;
	}
	
	/**
	 *  Method created for removing duplicate user entries
	 * @param result
	 * @return
	 */
	private List<User> removeDuplicatesUser(List<User> result){
		Set<User> contentItems = new HashSet<User>();
		for (User contentItem : result) {
				contentItems.add(contentItem);
		}
		result = new ArrayList<User>(contentItems);
		result = retrieveSubSetUser(result);
		return result;
	}	

	
	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.RecommendationService#getMultiFactorRecommendations(kiwi.model.content.ContentItem, kiwi.model.user.User)
	 */
	public List<ContentItem> getMultiFactorRecommendations(ContentItem currentItem, User currentUser) {
		log.info("Calculating multi factor recommendations");
		List<ContentItem> result = new LinkedList<ContentItem>();
		result = this.getRecommendations(currentItem, currentUser);
		if (result!=null && result.size()>1) {
			calculateContentItemScore(result, currentItem, currentUser);	
		}
		Collections.sort(result);
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ContentItem> getRecommendationsByTag(String tagLabel, User currentUser) {
		log.info("Calculating recommendations by tag");

		List<ContentItem> result = new LinkedList<ContentItem>();

		String s = "select ci "
			+ "from kiwi.model.content.ContentItem ci left join fetch ci.resource, kiwi.model.tagging.Tag tag "
			+ "where tag.taggedResource.id = ci.id and tag.taggingResource.title =:tagLabel ";		
	
		// if author is current user. This ensures that just those content
		// items are recommended which are of a different author
		if (currentUser.getLogin() != null) {
			s += "and ci.author.login <> :cu and tag.taggedBy.login <> :cu ";
		}

		s = s + " and ci.deleted = false";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("tagLabel", tagLabel);
			
		if(currentUser.getLogin() != null){
			q.setParameter("cu", currentUser.getLogin());
		}

		// limit to at most 10 recommendations
		q.setMaxResults(10);
		
		q.setHint("org.hibernate.cacheable", true);

		try {
			result = (List<ContentItem>)q.getResultList();
		} catch (PersistenceException ex) {
			log.warn("error while listing recommendations: query failed");
		}

		return result;
	}	
	
		
	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.RecommendationService#getTagGroupedRecommendations(kiwi.model.content.ContentItem, kiwi.model.user.User)
	 */
	@SuppressWarnings("unchecked")
	public List<Map.Entry<ContentItem,List<ContentItem>>> getTagGroupedRecommendations(ContentItem currentItem, User currentUser) {
		log.info("Calculating grouped recommendations");

		Collection<Tag> tags = taggingService.getTags(currentItem);

		List<Tag> qresult = new LinkedList<Tag>();

		Collection<String> ltags = new HashSet<String>();

		HashMap<ContentItem, List<ContentItem>> groupings = new HashMap<ContentItem, List<ContentItem>>();
		List<Map.Entry<ContentItem,List<ContentItem>>> result = new LinkedList<Map.Entry<ContentItem,List<ContentItem>>>();

		if(tags.isEmpty())
		{

		}
		else {
		for (Tag t : tags){
			//just tags which are of the current user
			if(t.getTaggedBy() == currentUser)
				log.debug("Tags: " + t.getTaggingResource().getTitle());
				ltags.add(t.getTaggingResource().getTitle());
			}

		//basic select statement for content items based on tags
		//recommends content items which are tagged by tags of different persons but the same as tagged by current user on the current content item
		String s = "select t " +
			    	  "from kiwi.model.tagging.Tag t " +
					  "where t.taggingResource.title in (:tags) and t.taggedResource <> :currentItem ";

		//if author is current user. This ensures that just those content items are recommended which are of a different author
		if(currentUser.getLogin() != null){
			s = s + "and t.taggedBy.login <> :cu ";
		}

		s = s + "and t.taggingResource.deleted = false order by t.taggingResource.title";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("tags", ltags);
		q.setParameter("currentItem", currentItem);
		
		if(currentUser.getLogin() != null){
			q.setParameter("cu", currentUser.getLogin());
		}

			qresult = (List<Tag>)q.getResultList();
		}

		for (Tag t: qresult)
		{
			if(groupings.get(t.getTaggingResource())==null){
				groupings.put(t.getTaggingResource(), new LinkedList<ContentItem>());
			}
			if(!groupings.get(t.getTaggingResource()).contains(t.getTaggedResource())){
				//groupings.get(t.getTaggingResource()).add(t.getTaggedResource());
				if (groupings.get(t.getTaggingResource()).size()<MAX_ITEMS) {
					groupings.get(t.getTaggingResource()).add(t.getTaggedResource());
				}
				
			}
		}
		
		result.addAll(groupings.entrySet());

		return result;
	}
	
	
	
	/**
	 * TODO:Eliminate this. Temporary method.
	 * Method created for removing duplicate user entries
	 * 
	 * @param result
	 * @return
	 */
	private List<ContentItem> retrieveSubSet(List<ContentItem> result){
		//fred
		if (currentUser!=null && currentUser.getUserPreference()!=null) {
			MAX_ITEMS =  currentUser.getUserPreference().getRecommendationSize();			
		}
		if (result.size()>MAX_ITEMS) {
			result = result.subList(0, MAX_ITEMS);	
		}
		return result;
	}
	
	/**
	 * TODO:Eliminate this. Temporary method.
	 * Method created for removing duplicate user entries
	 * 
	 * @param result
	 * @return
	 */
	private List<User> retrieveSubSetUser(List<User> result){
		if (result.size()>MAX_ITEMS) {
			result = result.subList(0, MAX_ITEMS);	
		}
		return result;
	}

	/**
	 * TODO: Check whether this method already exist and remove this.
	 * @param currentUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<String> getAllTagsByUser(User currentUser) {
		String s = "select distinct t.taggingResource.title " +
			    	  "from kiwi.model.tagging.Tag t " +
					  "where t.taggedBy = :cu" +
					  " and t.taggingResource.deleted = false";
		
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("cu", currentUser);
		return q.getResultList();
	}

	/**
	 * TODO: Check whether this method already exist and remove this.
	 * @param group
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<String> getAllTagsByGroupUser(Group group) {
		String s = "select distinct t.taggingResource.title " +
		"from kiwi.model.tagging.Tag t " +
		"where t.taggedResource = :groupContentItem" +
		" and t.taggingResource.deleted = false";
		
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("groupContentItem", group.getResource().getContentItem());
		return q.getResultList();
	}

	/**
	 * TODO: Check whether this method already exist and remove this.
	 * @param contentItem
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<String> getTagLabesByContentItem(ContentItem contentItem) {
		String s = "select tag.taggingResource.title "			
			+ " from kiwi.model.content.ContentItem ci, kiwi.model.tagging.Tag tag "
		+ " where tag.taggedResource.id = ci.id"
		+ " and ci.id =:contentItemId and ci.deleted = false";
		javax.persistence.Query q = entityManager.createQuery(s);
	    q.setParameter("contentItemId", contentItem.getId());
		return q.getResultList();
	}
	
	
	/**
	 * TODO: Check whether this method already exist and remove this.
	 * @param contentItem
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<String> getTagLabesByGroup(ContentItem contentItem) {
		String s = "select distinct tag.taggingResource.title "			
			+ " from kiwi.model.content.ContentItem ci, kiwi.model.tagging.Tag tag , kiwi.model.user.Group g"
			+ " where tag.taggedResource.id = ci.id"
			+ " and g.resource.contentItem.id = :contentItemId"
			+ " and ci.id =:contentItemId and ci.deleted = false";
			javax.persistence.Query q = entityManager.createQuery(s);
		    q.setParameter("contentItemId", contentItem.getId());
		return q.getResultList();
	}	
	
	/**
	 * TODO: Improve semantics of this method
	 * @param tagLabel
	 * @param user
	 * @return
	 */
	private Integer getTagByUser(String tagLabel, User user) {
		Long qresult = null;
			String s  = "select count(t) from kiwi.model.tagging.Tag t "
			+ " where t.taggedBy =:user" 
			+ " and t.taggingResource.title =:tagLable";
		javax.persistence.Query q = entityManager.createQuery(s);
	    q.setParameter("user", user);
	    q.setParameter("tagLable", tagLabel);
		try {
			qresult = (Long)q.getSingleResult();
					
			} catch (NoResultException ex) {
				log.warn("error while listing recommendations: no recommendations found in the system");
			}
		return qresult.intValue();
	}	
	
	/**
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<String> getTagLabelsByUser(User user) {
		List<String> qresult = null;
			String s  = "select t.taggingResource.title from kiwi.model.tagging.Tag t "
			+ " where t.taggedBy =:user";
		javax.persistence.Query q = entityManager.createQuery(s);
	    q.setParameter("user", user);

		try {
			qresult = (List<String>)q.getResultList();
					
			} catch (NoResultException ex) {
				log.warn("error while listing getTagLabelsByUser");
			}
		return qresult;
	}
	
	/**
	 * It calculates the tag representativeness
	 * @param tagLabel
	 * @return
	 */
	private long getTagWeight(String tagLabel) {

		Long tagAmount = null;
		
		Long totalTag = null;
		
		long tagWeight = 0l;
		
		String tagLable = tagLabel;

		String s = "select count (t) from kiwi.model.tagging.Tag t " +
				   "where t.taggingResource.title = :tagLabel " +
				   " and t.taggingResource.deleted = false";

		javax.persistence.Query q = entityManager.createQuery(s);

		q.setParameter("tagLabel", tagLable);
		try {
			tagAmount = (Long)q.getSingleResult();
		} catch (NoResultException ex) {
			log.warn("error while counting a particular tag");
		}

		String sAll = "select count (t) from kiwi.model.tagging.Tag t " +
		   "where t.taggingResource.deleted = false";
	
		javax.persistence.Query qAll = entityManager.createQuery(sAll);
	
		try {	
			totalTag = (Long)qAll.getSingleResult();
		} catch (NoResultException ex) {
			log.warn("error while listing counting all tags");
		}			
			
		if (tagAmount!=null && tagAmount>0) {
			tagWeight = tagAmount.longValue() / totalTag.longValue();	
		}

		return tagWeight;
	}	

	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.RecommendationService#getPersonalRecommendations(kiwi.model.user.User)
	 */
	@SuppressWarnings("unchecked")
	public List<ContentItem> getPersonalRecommendations(User currentUser) {
		log.info("Calculating personal recommendations");

		List<ContentItem> result = new LinkedList<ContentItem>();

		
		Query q = entityManager.createNamedQuery("recommendationService.personalRecommendations");
		q.setParameter("cu", currentUser.getLogin());
		//q.setMaxResults(10);
		q.setHint("org.hibernate.cacheable", true);

		try {
			result = (List<ContentItem>)q.getResultList();
		} catch (PersistenceException ex) {
			log.warn("error while listing recommendations: query failed");
		}
		result = removeDuplicates(result);
		//getTagLabesByContentItem(currentItem);
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.RecommendationService#getUsersByGroupTags(kiwi.model.content.ContentItem, kiwi.model.user.User)
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUsersByGroupTags(ContentItem contentItem, User currentUser) {
		log.info("Calculating user recommendations");
		
		Collection<String> ltags = new ArrayList<String>();

		ltags = getTagLabesByGroup(contentItem);

		List<User> result = new LinkedList<User>();

		if (ltags.isEmpty()) {

		} else {

			String s = "select distinct u "
					+ "from kiwi.model.user.User u left outer join u.tags as tag "
					
					+ " , kiwi.model.user.Group g  left outer join g.users as usersOfGroup "
					
					+ "where g.resource.id = :groupId and tag.taggingResource.title in (:groupTags) and u not in elements(g.users)";

			s += "and u.deleted = false";

			javax.persistence.Query q = entityManager.createQuery(s);
			q.setParameter("groupTags", ltags);
			q.setParameter("groupId", contentItem.getResource().getId());
			q.setHint("org.hibernate.cacheable", true);

			try {
				result = (List<User>)q.getResultList();
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				log.warn("error while listing recommendations: query failed");
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getContactListRecommendations(User currentUser) {
		log.info("Calculating getContactListRecommendations recommendations");
		
		Collection<String> ltags = new ArrayList<String>();

		ltags = getTagLabelsByUser(currentUser);

		List<User> result = new LinkedList<User>();
		
		Set<User> friends = new HashSet<User>();
		
		friends = currentUser.getFriends();
		friends.add(currentUser);

		if (ltags.isEmpty()) {

		} else {

			String s = "select distinct u "
					+ "from kiwi.model.user.User u left outer join u.tags as tag "
					+ " where tag.taggingResource.title in (:myTags) and u <> :currentUser and u not in (:contacts)";

			s += " and u.deleted = false";

			javax.persistence.Query q = entityManager.createQuery(s);
			q.setParameter("myTags", ltags);
			q.setParameter("currentUser", currentUser);
			q.setParameter("contacts", friends);
			q.setHint("org.hibernate.cacheable", true);

			try {
				result = (List<User>)q.getResultList();
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				log.warn("error while listing recommendations: query failed");
			}
		}
		return result;
	}	
	
	

	
	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.RecommendationService#getUsersByContentItem(kiwi.model.content.ContentItem, kiwi.model.user.User)
	 */
	@SuppressWarnings("unchecked")
	public List<User> getUsersByContentItem(ContentItem contentItem, User currentUser) {
		List<User> result = new LinkedList<User>();
		String s = "select u "
				+ "from kiwi.model.user.User u left outer join u.tags as tag "
				+ "where tag.taggedResource = :ci ";
		s += "and u.deleted = false"; // group by ci.id ";

		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("ci", contentItem);

		q.setMaxResults(10);

		q.setHint("org.hibernate.cacheable", true);

		try {
			result = (List<User>)q.getResultList();
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log.warn("error while listing recommendations: query failed");
		}
	
		result = removeDuplicatesUser(result);
		for (User user : result) {
			System.out.println(user.getFirstName());
		}
		return result;
	}	
	

	/**
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "unused", "unchecked", "unchecked" })
	public List<User> getFOAFRecommendations(User user) {
		List<User> results = new LinkedList<User>();
		List<User> foafResults = new LinkedList<User>();
			String s = "SELECT ?u WHERE { " +
			" ?u  <" + Constants.NS_RDF       + "type>          <"+ Constants.NS_KIWI_CORE + "User> . " +
			" ?u <" + Constants.NS_FOAF + "knows> ?w . }";
			javax.persistence.Query q = kiwiEntityManager.createQuery(s, SPARQL,User.class);
			//q.setParameter("user", user);
		q.setHint("org.hibernate.cacheable", true);
		try {
			results = (List<User>) q.getResultList();
		} catch (PersistenceException ex) {
			//ex.printStackTrace();
			log.warn("error while listing items rated:  query failed");
			results = null;
		}
		
		
		if (results == null) {
			return Collections.EMPTY_LIST;
		} else {
//			for (User user2 : results) {
//				foafResults.remove(user2);
//			}
			return results;
		}
	}	
	
	
	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.RecommendationService#getGroupRecommendations(kiwi.model.user.User)
	 */
	@SuppressWarnings("unchecked")
	public List<Map.Entry<Group,List<ContentItem>>>  getGroupRecommendations(User currentUser) {
		log.info("Calculating group recommendations");

		List<ContentItem> result = new LinkedList<ContentItem>();
		
		List<Group> groups = new LinkedList<Group>();
		
		groups = groupService.getGroupsByUser(currentUser); //this.currentUser.getGroups();
		
		Collection<String> ltags = new ArrayList<String>();  

		HashMap<Group, List<ContentItem>> groupItem = new HashMap<Group, List<ContentItem>>();
		
		List<Map.Entry<Group,List<ContentItem>>> groupList = new LinkedList<Map.Entry<Group,List<ContentItem>>>();
		
		for (Group group : groups) {

			ltags = getAllTagsByGroupUser(group);			
			
			if (ltags.isEmpty()) {

			} else {

				// basic select statement for content items based on tags
				// recommends content items which are tagged by tags of different
				// persons but the same as tagged by current user on the current
				// content item
				String s = "select ci "
					+ "from kiwi.model.content.ContentItem ci left join fetch ci.resource, kiwi.model.tagging.Tag tag "
					+ "where tag.taggedResource.id = ci.id  "						

						+ "and " 
				    	 + "  NOT EXISTS (from KiWiTriple t " 
				    	 + "              where ci.resource.id = t.subject.id " 
				    	 + "                and t.property.uri = '"+Constants.NS_RDF+"type' " 
				    	 + "                and t.object.uri in ('"+Constants.NS_KIWI_SPECIAL+"KiWiGroup')) "
				    	 
				    	 + " and tag.taggingResource.title in (:tags) ";
				
				// if author is current user. This ensures that just those content
				// items are recommended which are of a different author
				if (currentUser.getLogin() != null) {
					s += "and ci.author.login <> :cu and tag.taggedBy.login <> :cu ";
				}


				s += "and ci.deleted = false"; // group by ci.id ";

				javax.persistence.Query q = entityManager.createQuery(s);
				q.setParameter("tags", ltags);
				//log.info("Calculating recommendations, current user #0",currentUser.getLogin());
				if (currentUser.getLogin() != null) {
					q.setParameter("cu", currentUser.getLogin());
				}
				
				// limit to at most 10 recommendations
				q.setMaxResults(10);

				q.setHint("org.hibernate.cacheable", true);

				try {
					result = (List<ContentItem>)q.getResultList();
				} catch (PersistenceException ex) {
					log.warn("error while listing recommendations: query failed");
				}
			}
			
			
			List<ContactRecommendation> contactRecommendations = getContactRecommendationsByGroup(group);
			for (ContactRecommendation contactRecommendation : contactRecommendations) {
				if (contactRecommendation.getGroupReceiver()!=null && contactRecommendation.getRecommendedItem()!=null && !result.contains(contactRecommendation.getRecommendedItem())) {
					result.add(contactRecommendation.getRecommendedItem());
				}
			}
			result = removeDuplicates(result);
			
			for (Group g : groups) {
				for (ContentItem t: result)
				{
					if(groupItem.get(g)==null){
						groupItem.put(g, new LinkedList<ContentItem>());
					}
					if(!groupItem.get(g).contains(t) && t!=null){
							groupItem.get(g).add(t);
					}
				}
			}
			groupList.addAll(groupItem.entrySet());

			if (groupList.size()>MAX_ITEMS) {
				groupList = groupList.subList(0, MAX_ITEMS);	
			}	
					
		}  
		return groupList;
	}


	/**
	 * It calculates the contentItem score
	 * @param result
	 * @param currentItem
	 * @param currentUser
	 */
	private void calculateContentItemScore(List<ContentItem> result, ContentItem currentItem, User currentUser) {
		long ds = 0l;
		long userAffinity = 0l;
		Set<String> tagSet = new HashSet<String>();
		long tagWeight = 0l;
		for (ContentItem resultItem : result) {
			ds = calculateCosineSimilarity(resultItem, currentItem);
			for (Tag tag : taggingService.getTags(currentItem)) {
					String tagLabel = tag.getTaggingResource().getTitle();
					if (!tagSet.contains(tagLabel)) {
						userAffinity = calculateTagUserAffinity(tagLabel, currentUser);
						tagWeight = getTagWeight(tagLabel);
						ds = ds + userAffinity + tagWeight;
						tagSet.add(tagLabel);
					}
			}
			DecimalFormat twoDigits = new DecimalFormat("##.##");
			resultItem.setContentItemScore(Long.valueOf(twoDigits.format(ds)));
		}
	}

	/**
	 * @param currentUser
	 * @param tagLabel
	 * @return
	 */
	private long calculateTagUserAffinity(String tagLabel, User currentUser) {
		long tagUserAffinity = 0l;
		List<String> tagsOfUser = getAllTagsByUser(currentUser);
		Integer amountTagByUser = getTagByUser(tagLabel, currentUser);
		if (!tagsOfUser.isEmpty() && amountTagByUser>0) {
			tagUserAffinity = (amountTagByUser.longValue() / new Integer(tagsOfUser.size()).longValue());
		}
		return tagUserAffinity;
	}


	/**
	 * It calculates the cosine similarity
	 * @param resultItem
	 * @param contentItem
	 * @return
	 */
	public long calculateCosineSimilarity(ContentItem resultItem, ContentItem contentItem){
		AbstractStringMetric cosineSimilarity = new CosineSimilarity();
		return (long)(cosineSimilarity.getSimilarity(getTagString(resultItem), getTagString(contentItem))*100);
	}

	/** 
	 * It prepares the tag string for later cosine similarity calculus.
	 * @param contentItem
	 * @return
	 */
	private String getTagString(ContentItem contentItem){
		List<String> tagLabels = getTagLabesByContentItem(contentItem);
		StringBuilder tagString = new StringBuilder();
		if (tagLabels!=null) {
			for (String tag : tagLabels) {
				tagString.append(tag);
				tagString.append(" ");
			}
		}
		if (tagLabels.size()>0 && (tagString.lastIndexOf(",")==tagString.length()-1)){
			tagString = new StringBuilder(tagString.substring(0, tagString.lastIndexOf(",")));
		}
		return tagString.toString().trim();
	}

	


	/**
	 * Calculates a list of recommendations using the semantic vectors index created in SemanticIndexingService.
	 * 
	 * @see SemanticIndexingService
	 * @see kiwi.api.recommendation.RecommendationService#getSemanticVectorRecommendations(kiwi.model.content.ContentItem)
	 * @param current the content item for which to calculate recommendations
	 * @return a list of content items, ordered by relevancy (most significant first)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ContentItem> getSemanticVectorRecommendations(ContentItem current) {
		int defaultNumResults = configurationService.getConfiguration("kiwi.recommendations.max","10").getIntValue();

		try {
			String dataDir = configurationService.getConfiguration("kiwi.semanticvectors").getStringValue() + File.separator + "data" + File.separator;


			// TODO: kiwi configuration service
			String docvectors  = dataDir + "semanticvectors"+File.separator+"docvectors.bin";
			String termvectors = dataDir + "semanticvectors"+File.separator+"termvectors.bin";
			String matchcase =  current.getKiwiIdentifier();
			String[] args = { "-queryvectorfile", docvectors, "-searchvectorfile", docvectors, "-matchcase", matchcase };

			log.debug("looking up document vectors in #0",docvectors);
			log.debug("looking up term vectors in #0",termvectors);

			LinkedList<ContentItem> recommendations = new LinkedList<ContentItem>(); 
			LinkedList<SearchResult> results = Search.RunSearch(args, defaultNumResults);


			// Print out results.
			if (results != null && results.size() > 0) {
				for (SearchResult result: results) {
					String label = ((ObjectVector)result.getObject()).getObject().toString();
					ContentItem item = contentItemService.getContentItemByKiwiId(label);
					log.debug("recommending: #0", item.getTitle());
					recommendations.add(item);
				}
			} else {
				log.debug("No search output.");
			}

			log.debug("calculated #0 recommendations", recommendations.size());
			
			return recommendations;
		} catch(Exception ex) {
			log.error("an exception occurred while computing recommendations");
			return Collections.EMPTY_LIST;
		}
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.RecommendationService#getContactRecommendations(kiwi.model.user.User)
	 */
	@Override
	public List<ContactRecommendation> getContactRecommendations(User currentUser) {
		List<ContactRecommendation> contactRecommendations = new ArrayList<ContactRecommendation>();
		if (getContactRecommendationsByUser(currentUser)!=null) {
			contactRecommendations = getContactRecommendationsByUser(currentUser);
		} 
		return contactRecommendations;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.RecommendationService#rezcommendItem(kiwi.model.user.User, kiwi.model.content.ContentItem, java.lang.String)
	 */
	@Override
	public void recommendItem(User currentUser, ContentItem recommendedItem, String receiverLogin) {
		User recommendationReceiver =  userService.getUserByLogin(receiverLogin);
		if ((currentUser!=null) && (getContactRecommendationsBySenderReceiverAndItem(currentUser,recommendedItem,recommendationReceiver)==null)){
			ContactRecommendation contactRecommendation = new ContactRecommendation(currentUser,recommendedItem,recommendationReceiver);
			entityManager.persist(contactRecommendation);
//			entityManager.flush();
		}
	}
	
	@Override
	public void recommendItemToGroup(User currentUser, ContentItem recommendedItem, String groupName) {
		Group groupReceiver =  groupService.getGroupByName(groupName);
		if ((currentUser!=null) && (getContactRecommendationsBySenderReceiverAndGroup(currentUser,recommendedItem,groupReceiver)==null)){
			ContactRecommendation contactRecommendation = new ContactRecommendation(currentUser,recommendedItem,groupReceiver);
			entityManager.persist(contactRecommendation);
//			entityManager.flush();
		}
	}	
	
	/**
	 * Remove a contact recommendation
	 * @param contactRecommendation
	 */
	public void removeContactRecommendation(User currentUser, ContactRecommendation contactRecommendation) {
		currentUser = loadContactRecommendations(currentUser);
		if (contactRecommendation!=null && currentUser!=null && currentUser.getContactRecommendations()!=null) {
			currentUser.removeContactRecommendation(contactRecommendation);
			//entityManager.persist(currentUser);
			entityManager.remove(contactRecommendation);
//			entityManager.flush();
		}
	}	
	
	
	/**
	 * @param contentItem
	 * @param currentUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ContactRecommendation> getContactRecommendationsByUser(User recommendationReceiver) {
		List<ContactRecommendation> result = new ArrayList<ContactRecommendation>();
		String s = "select c "
			+ "from kiwi.model.recommendation.ContactRecommendation c where c.recommendationReceiver = :recommendationReceiver ";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("recommendationReceiver", recommendationReceiver);
		try {
			result = (List<ContactRecommendation>)q.getResultList();
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log.warn("error while listing recommendations: query failed");
		}
		if(result == null ) {
			return Collections.EMPTY_LIST;
		} else {
			return result;
		}
	}
	
	
	/**
	 * @param contentItem
	 * @param currentUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ContactRecommendation> getContactRecommendationsByGroup(Group groupReceiver) {
		List<ContactRecommendation> result = new ArrayList<ContactRecommendation>();
		String s = "select c "
			+ "from kiwi.model.recommendation.ContactRecommendation c where c.groupReceiver = :groupReceiver ";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("groupReceiver", groupReceiver);
		try {
			result = (List<ContactRecommendation>)q.getResultList();
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log.warn("error while listing recommendations: query failed");
		}
		if(result == null ) {
			return Collections.EMPTY_LIST;
		} else {
			return result;
		}
	}	
	/**
	 * @param contentItem
	 * @param currentUser
	 * @return
	 */
	
	private ContactRecommendation getContactRecommendationsBySenderReceiverAndItem(User recommendationSender,ContentItem recommendedItem, User recommendationReceiver) {
		ContactRecommendation result = null;
		String s = "select c "
			+ "from kiwi.model.recommendation.ContactRecommendation c where c.recommendationSender = :recommendationSender " +
					" and c.recommendationReceiver = :recommendationReceiver and c.recommendedItem = :recommendedItem";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("recommendationSender", recommendationSender);
		q.setParameter("recommendationReceiver", recommendationReceiver);
		q.setParameter("recommendedItem", recommendedItem);
		try {
			result = (ContactRecommendation)q.getSingleResult();
		} catch (NoResultException ex) {
			log.warn("error while listing contactRecommendation");
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log.warn("error while listing recommendations: query failed");
		}
		return result;
	}
	
	private ContactRecommendation getContactRecommendationsBySenderReceiverAndGroup(User recommendationSender,ContentItem recommendedItem, Group groupReceiver) {
		ContactRecommendation result = null;
		String s = "select c "
			+ "from kiwi.model.recommendation.ContactRecommendation c where c.recommendationSender = :recommendationSender " +
					" and c.groupReceiver = :groupReceiver and c.recommendedItem = :recommendedItem";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("recommendationSender", recommendationSender);
		q.setParameter("groupReceiver", groupReceiver);
		q.setParameter("recommendedItem", recommendedItem);
		try {
			result = (ContactRecommendation)q.getSingleResult();
		} catch (NoResultException ex) {
			log.warn("error while listing group contactRecommendation");
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log.warn("error while listing recommendations: query failed");
		}
		return result;
	}	
	
	/**
	 * @param contentItem
	 * @param currentUser
	 * @return
	 */
	private User loadContactRecommendations(User currentUser) {
		User result = null;
		String s = "select u "
				+ "from kiwi.model.user.User u left outer join fetch u.contactRecommendations where u = :currentUser";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("currentUser", currentUser);
		try {
			result = (User)q.getSingleResult();
		} catch (NoResultException ex) {
			log.warn("error while listing contactRecommendation");
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log.warn("error while listing recommendations: query failed");
		}
		return result;
	}


	/**
	 * Return the list of all users. Might be slow when many users exist in the system.
	 * 
	 * @return the list of all users active in the system, ordered by login

	 */
	@SuppressWarnings("unchecked")
	public List<User> getRecommendableUsers(User currentUser) {
		javax.persistence.Query q = entityManager
			.createQuery("select u from kiwi.model.user.User u where u <> :currentUser and u.login <> :anon and u.login <> :admin order by u.login");
		q.setParameter("currentUser", currentUser);
		q.setParameter("anon", "anonymous");
		q.setParameter("admin", "admin");
		return q.getResultList();
	}	

	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.RecommendationService#getContactRecommendations(kiwi.model.user.User)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MySimilarRecommendation> getMySimilarRecommendations(User currentUser) {
		List<MySimilarRecommendation> mySimilarRecommendations = getMySimilarRecommendationByUser(currentUser);
		if(mySimilarRecommendations == null ) {
			return Collections.EMPTY_LIST;
		} else {
			return mySimilarRecommendations;
		}		
	}

	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.RecommendationService#rezcommendItem(kiwi.model.user.User, kiwi.model.content.ContentItem, java.lang.String)
	 */
	@Override
	public MySimilarRecommendation createMySimilarRecommendation(User currentUser, ContentItem recommendedItem) {
		if ((currentUser!=null) && (getMySimilarRecommendationByUserAndItem(currentUser,recommendedItem)==null)){
			MySimilarRecommendation mySimilarRecommendation = new MySimilarRecommendation(currentUser,recommendedItem);
			kiwiEntityManager.persist(mySimilarRecommendation);
			kiwiEntityManager.persist(currentUser);
		}
		return getMySimilarRecommendationByUserAndItem(currentUser, recommendedItem);
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.recommendation.RecommendationService#existMySimilarRecommendation(kiwi.model.user.User, kiwi.model.content.ContentItem)
	 */
	public boolean existMySimilarRecommendation(User currentUser, ContentItem recommendedItem){
		return (getMySimilarRecommendationByUserAndItem(currentUser, recommendedItem)!=null);
	}
	
	/**
	 * Remove a contact recommendation
	 * @param contactRecommendation
	 */
	public void removeMySimilarRecommendation(User currentUser, MySimilarRecommendation mySimilarRecommendation) {
		if (mySimilarRecommendation!=null && currentUser!=null) {
			kiwiEntityManager.persist(currentUser);
			entityManager.remove(mySimilarRecommendation);
		}
	}	

	/**
	 * @param contentItem
	 * @param currentUser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<MySimilarRecommendation> getMySimilarRecommendationByUser(User user) {
		List<MySimilarRecommendation> result = new ArrayList<MySimilarRecommendation>();
		String s = "select c "
			+ "from kiwi.model.recommendation.MySimilarRecommendation c where c.user = :user ";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("user", user);
		try {
			result = (List<MySimilarRecommendation>)q.getResultList();
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log.warn("error while listing recommendations: query failed");
		}
		if(result == null ) {
			return Collections.EMPTY_LIST;
		} else {
			return result;
		}
	}
	
	
	
	/**
	 * @param contentItem
	 * @param currentUser
	 * @return
	 */
	private MySimilarRecommendation getMySimilarRecommendationByUserAndItem(User user,ContentItem contentItem) {
		MySimilarRecommendation result = null;
		String s = "select c "
			+ "from kiwi.model.recommendation.MySimilarRecommendation c where c.user = :user and c.contentItem = :contentItem";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("user", user);
		q.setParameter("contentItem", contentItem);
		try {
			result = (MySimilarRecommendation)q.getSingleResult();
		} catch (NoResultException ex) {
			log.warn("error while listing contactRecommendation");
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log.warn("error while listing recommendations: query failed");
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private List<ContentItem> getRecommendationsByTags(Collection<String> tagLabels, User currentUser, ContentItem contentItem) {
		log.info("Calculating recommendations by tag");

		List<ContentItem> result = new LinkedList<ContentItem>();

		String s = "select ci "
			+ "from kiwi.model.content.ContentItem ci left join fetch ci.resource, kiwi.model.tagging.Tag tag "
			+ "where tag.taggedResource.id = ci.id and ci != :cci and tag.taggingResource.title in (:tagLabels)";		
	
		// if author is current user. This ensures that just those content
		// items are recommended which are of a different author
		if (currentUser.getLogin() != null) {
//			s += "and ci.author.login <> :cu and tag.taggedBy.login <> :cu ";
		//	s += "and ci.author.login <> :cu ";
			s += "and ci.author.id !=1  and ci.author.login <> :cu ";
		}

		s = s + " and ci.deleted = false";
		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("tagLabels", tagLabels);
		q.setParameter("cci", contentItem);
			
		if(currentUser.getLogin() != null){
			q.setParameter("cu", currentUser.getLogin());
		}

		q.setHint("org.hibernate.cacheable", true);

		try {
			result = (List<ContentItem>)q.getResultList();
		} catch (PersistenceException ex) {
			log.warn("error while listing recommendations: query failed");
		}


		return result;
	}		

	public static float FIRST_LEVEL_FRIENDSHIP = 1;
	
	public static float SECOND_LEVEL_FRIENDSHIP = 2;

	@SuppressWarnings("unchecked")
	public List<SocialCapitalRecommendation> getSocialCapitalizedRecommendations(ContentItem currentItem, User currentUser) {
		log.info("Calculating social capital recommendations");
		List<String> needs = getTagLabesByContentItem(currentItem);
				
		Map<String,SocialCapital> personHash = new HashMap<String,SocialCapital>();

		List<SocialCapitalRecommendation> sociaList = new ArrayList<SocialCapitalRecommendation>();
		
		List<ContentItem> result = new ArrayList<ContentItem>();
		
		String topic = "news";
		
		if (currentUser.getId()>1 && !needs.isEmpty() && !currentItem.getAuthor().getLogin().equals("admin")) {
			result = getRecommendationsByTags(needs, currentUser, currentItem);	
		}

		for (ContentItem contentItem : result) {
			
			SocialCapital socialCapital = new SocialCapital();
			socialCapital.setTopic(topic);
			socialCapital.setNeeds(needs);

			SocialCapitalRecommendation socialCapitalRecommendation = new SocialCapitalRecommendation();
			socialCapitalRecommendation.setRecommendedItem(contentItem);
			User contentItemAuthor =  contentItem.getAuthor();
			//System.out.println(contentItemAuthor.getLogin());
			socialCapitalService.calculateSocialCapital(socialCapital,contentItemAuthor, 0, currentUser);
			//System.out.println("Social capital score from author "+socialCapital.getSocialCapitalScore() + " with author " + contentItemAuthor.getLogin());			
			if(!personHash.keySet().contains(contentItemAuthor.getLogin())){
				Set<User> friends = contentItemAuthor.getFriends();//getFriends with knowledge in a given topic
				socialCapital.setDirectFriends(new ArrayList(friends));
				socialCapitalService.calculateSocialCapital(socialCapital,contentItemAuthor, FIRST_LEVEL_FRIENDSHIP, currentUser); 
				//System.out.println("Social capital score from DIRECT friends "+socialCapital.getSocialCapitalScore() + " with author " + contentItemAuthor.getLogin());
				for (User friend : friends) {
					Set<User> indirectFriends = friend.getFriends();
					indirectFriends.removeAll(friends);
					indirectFriends.remove(contentItemAuthor);					
					socialCapital.setIndirectFriends(new ArrayList(indirectFriends));
					socialCapitalService.calculateSocialCapital(socialCapital,contentItemAuthor, SECOND_LEVEL_FRIENDSHIP, currentUser); 
					//System.out.println("Social capital score from INDIRECT friends "+socialCapital.getSocialCapitalScore() + " with author " + contentItemAuthor.getLogin());
				}
			}else{
				socialCapital.setDirectFriends(((SocialCapital)personHash.get(contentItemAuthor.getLogin())).getDirectFriends());
				socialCapital.setIndirectFriends(((SocialCapital)personHash.get(contentItemAuthor.getLogin())).getIndirectFriends());
				socialCapital.setSocialCapitalScore(((SocialCapital)personHash.get(contentItemAuthor.getLogin())).getSocialCapitalScore());
			}
			
			if(socialCapital.getSocialCapitalScore()>0f ){
				if(!personHash.keySet().contains(contentItemAuthor.getLogin())){
					//DecimalFormat twoDigits = new DecimalFormat("###.###");
					//socialCapitalRecommendation.setEarnedSocialCapitalValue(Float.valueOf(twoDigits.format(socialCapital.getSocialCapitalScore())));
					System.out.println("value   "+socialCapital.getSocialCapitalScore());
					socialCapitalRecommendation.setEarnedSocialCapitalValue(Float.valueOf(socialCapital.getSocialCapitalScore()));
					sociaList.add(socialCapitalRecommendation);
					personHash.put(contentItemAuthor.getLogin(),socialCapital);
				}
			}
		}
				
		Collections.sort(sociaList);
		sociaList = removeDuplicatesSocialRecommendations(sociaList);
		
		return sociaList;
	}
	
	/**
	 *  Method created for removing duplicate user entries
	 * @param result
	 * @return
	 */
	private List<SocialCapitalRecommendation> removeDuplicatesSocialRecommendations(List<SocialCapitalRecommendation> socialCapitalRecommendationsCol){
		if (socialCapitalRecommendationsCol.size()>MAX_ITEMS) {
			socialCapitalRecommendationsCol = socialCapitalRecommendationsCol.subList(0, MAX_ITEMS);	
		}
		return socialCapitalRecommendationsCol;
	}

}
