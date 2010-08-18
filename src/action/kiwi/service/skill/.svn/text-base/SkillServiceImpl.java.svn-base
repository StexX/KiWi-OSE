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
package kiwi.service.skill;

import static kiwi.model.kbase.KiWiQueryLanguage.SPARQL;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.history.HistoryService;
import kiwi.api.ontology.SKOSService;
import kiwi.api.skill.SkillServiceLocal;
import kiwi.api.skill.SkillServiceRemote;
import kiwi.api.tagging.TagRecommendation;
import kiwi.api.tagging.TagRecommendationService;
import kiwi.api.user.UserService;
import kiwi.model.Constants;
import kiwi.model.activity.CommentActivity;
import kiwi.model.activity.EditActivity;
import kiwi.model.activity.VisitActivity;
import kiwi.model.content.ContentItem;
import kiwi.model.ontology.SKOSConcept;
import kiwi.model.skill.FakeSkill;
import kiwi.model.skill.UserSkill;
import kiwi.model.user.User;
import kiwi.service.skill.parser.CustomTermTokenizer;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Fred Durao, Nilay Coskun
 *
 */
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("skillService")
public class SkillServiceImpl implements SkillServiceLocal, SkillServiceRemote  {
	
	@Logger
	private Log log;	
	
	@In
	private ContentItemService contentItemService;

	@In
	private TagRecommendationService tagRecommendationService;	
	
	@In
	private HistoryService historyService;	
	
	@In
	private EntityManager entityManager;	
	
	@In(create=true)
	private KiWiEntityManager kiwiEntityManager;
	
	@In(create=true)
	private SKOSService skosService;	
	
	@In
	private UserService userService;
	
	@In
	private User currentUser;		
	
	private CustomTermTokenizer customTermTokenizer = new CustomTermTokenizer();
	
	private static float THRESHOLD  = 0.2f;
	
	/* (non-Javadoc)
	 * @see kiwi.api.skill.SkillService#getSkills(kiwi.model.user.User)
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Float> computeSkillsByUser(User currentUser){
		Map<String, Float> map = new HashMap<String, Float>();
		Map<String, Float> mapFinal = new HashMap<String, Float>();
		Set<Long> duplicatedList = new HashSet<Long>();
		Set<ContentItem> cis = new HashSet<ContentItem>();
		cis.addAll(contentItemService.getContentItemByAuthor(currentUser));
		cis.addAll(contentItemService.getContentItemTaggedByUser(currentUser));
		cis.addAll(listCommentsByUser(currentUser));
		cis.addAll(listEditsByUser(currentUser));
		cis.addAll(listVisitsByUser(currentUser));
		cis.addAll(listItemsRatedByUser(currentUser));

		
    	
    	for (ContentItem ci : cis) {
			if(!duplicatedList.contains(ci.getId())){
				duplicatedList.add(ci.getId());
				
				if (ci.getTitle() == null || ci.getTextContent()==null || ci.getTextContent().getPlainString()==null) {
					continue;
				}
				String textUnderEvaluation = ci.getTextContent().getPlainString().concat(" ").concat(ci.getTitle()).toLowerCase();
				map = customTermTokenizer.getFrequencyMap(new StringReader(textUnderEvaluation), new HashSet(getFakeSkills()));

				Collection<String> tagLabels = new ArrayList<String>();
				tagLabels = contentItemService.getTagLabelsByContentItemAndAuthor(ci,currentUser);
				//System.out.println("author "+currentUser.getFirstName()+" as tags "+tagLabels);
				for (String tagLabel : tagLabels) {
					if(map.containsKey(tagLabel)){
						float tmp = map.get(tagLabel);
						map.remove(tagLabel);
						map.put(tagLabel, tmp+1);
						tmp = 0f;
					}else {
						map.put(tagLabel, 1f);
					}
				}
			}
		}
    	
    	//map = validateTermsAsSKOSContect(map);
    	map = customTermTokenizer.getNormalizedValues(map);
    	Iterator it = map.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, Float> pairs = (Map.Entry<String, Float>)it.next();
				if (((String)pairs.getKey()).equals("kay")) {
					System.out.println(((String)pairs.getKey()));
				}
		        if ((Float)pairs.getValue()>=THRESHOLD && this.getFakeSkillsBySkillName((String)pairs.getKey())==null) {
		        	mapFinal.put(pairs.getKey(), pairs.getValue());
			        //System.out.println("   -> " +  pairs.getKey() + " = " + pairs.getValue());						
				}
		    }
		return mapFinal;
	}	
	
	
	/**
	 * @param maps
	 * @return
	 */
	public Map<String, Float> validateTermsAsSKOSContect(Map<String, Float> maps){
		Map<String, Float> validMap = new HashMap<String, Float>();
		for (String key : maps.keySet()) {
			List<SKOSConcept> skosConcepts = skosService.getConceptByLabel(key);
			if (skosConcepts!=null && !skosConcepts.isEmpty()) {
				validMap.put(key, maps.get(key));
			}
		}
		return validMap;
	}		
	

	/* (non-Javadoc)
	 * @see kiwi.api.skill.SkillService#getSkills(kiwi.model.user.User)
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Float> computeIndexedSkillsByUser(User currentUser){
		Map<String, Float> map = new HashMap<String, Float>();
		Map<String, Float> mapFinal = new HashMap<String, Float>();
		Set<Long> duplicatedList = new HashSet<Long>();
		Set<ContentItem> cis = new HashSet<ContentItem>();
		cis.addAll(contentItemService.getContentItemByAuthor(currentUser));
		cis.addAll(contentItemService.getContentItemTaggedByUser(currentUser));
		cis.addAll(listCommentsByUser(currentUser));
		cis.addAll(listEditsByUser(currentUser));
		//cis.addAll(listVisitsByUser(currentUser));
		cis.addAll(listItemsRatedByUser(currentUser));
			
    	float topSkillValue = 0f;
    	float currentSkillValue = 0f;
    	float trashshold = 0f;
		for (ContentItem ci : cis) {
			if(!duplicatedList.contains(ci.getId())){
				duplicatedList.add(ci.getId());

				Collection<TagRecommendation> res2 = tagRecommendationService.getRecommendations(ci);

				for (TagRecommendation tagRecommendation : res2) {
					if(map.containsKey(tagRecommendation.getLabel())){
						float tmp = map.get(tagRecommendation.getLabel());
						map.remove(tagRecommendation.getLabel());
						map.put(tagRecommendation.getLabel(), tmp+1);
						currentSkillValue=tmp+1;
						tmp = 0f;
					}else {
						currentSkillValue=1;
						map.put(tagRecommendation.getLabel(), 1f);
					}
					topSkillValue = Math.max(topSkillValue,currentSkillValue);
				}
				
				
				
				Collection<String> tagLabels = new ArrayList<String>();
				tagLabels = contentItemService.getTagLabelsByContentItemAndAuthor(ci,currentUser);
				//System.out.println("author "+currentUser.getFirstName()+" as tags "+tagLabels);
				
				for (String tagLabel : tagLabels) {
					if(map.containsKey(tagLabel)){
						float tmp = map.get(tagLabel);
						map.remove(tagLabel);
						map.put(tagLabel, tmp+1);
						currentSkillValue=tmp+1;
						tmp = 0f;
					}else {
						currentSkillValue=1;
						map.put(tagLabel, 1f);
					}
					topSkillValue = Math.max(topSkillValue,currentSkillValue);
				}
			}
		}
		
	
		trashshold = topSkillValue * 0.2f;

		Iterator it = map.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, Float> pairs = (Map.Entry<String, Float>)it.next();
		        if ((Float)pairs.getValue()>=trashshold) {
		        	mapFinal.put(pairs.getKey(), pairs.getValue());
			       // System.out.println("   -> " +  pairs.getKey() + " = " + pairs.getValue());						
				}

		    }
		    
		return mapFinal;
	}
	

	/**
	 * computeUserSkills
	 */
	public void computeUserSkills(){
		loadFakeSkillList();
		if (deleteUserSkills()) {
			List<User> users = userService.getAllCreatedUsers();
			for (User user : users) {
//				System.out.println("computeUserSkills... "+user.getFirstName());
				computeUserSkill(user);
									
			}
		}		
	}

	public void computeUserSkill(User user){
			Map<String, Float> map = computeSkillsByUser(user);
			if (map!=null && !map.isEmpty()) {
//				System.out.println("map for use "+user.getFirstName()+" is "+map);
				UserSkill userSkill = new UserSkill(user,map);
				kiwiEntityManager.persist(userSkill);
//				kiwiEntityManager.flush();
			}else{
//				System.out.println("map is null or empty or existssssssssssssss");
			}
			
	}		
	/**
	 * It gets the result by skill
	 */
	public boolean deleteUserSkills(){
		List<UserSkill> uss = this.listUserSkills();
		for (UserSkill us : uss) {
			entityManager.remove(us);
			//entityManager.flush();
		}
//		entityManager.flush();
		return listUserSkills().isEmpty();
	} 	

	
	/**
	 * It gets the result by skill
	 */
	public UserSkill getSkillsByUser(User user){
		UserSkill result = null;	
		String s = "select s from kiwi.model.skill.UserSkill s where s.user = :user";
			javax.persistence.Query q = entityManager.createQuery(s);
			q.setParameter("user", user);
			try {
				result = (UserSkill)q.getSingleResult();
			} catch (NoResultException ex) {
				log.warn("error while listing user skills");
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				log.warn("error while listing user skills: query failed");
			}
			return result;
	}
	
	/**
	 * It gets the result by skill
	 */
	public UserSkill getSkillsByUserAndSkills(User user, Map<String,Float> skills){
		UserSkill result = null;	
		String s = "select s from kiwi.model.skill.UserSkill s where s.user = :user and s.skills =:skills";
			javax.persistence.Query q = entityManager.createQuery(s);
			q.setParameter("user", user);
			q.setParameter("skills", skills);
			try {
				result = (UserSkill)q.getSingleResult();
			} catch (NoResultException ex) {
				log.warn("error while listing user skills");
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				log.warn("error while listing user skills: query failed");
			}
			return result;
	}
	
	/**
	 * It gets the result by skill
	 */
	@SuppressWarnings("unchecked")
	public List<UserSkill> listUserSkills(){
		List<UserSkill> result = null;
		List<UserSkill> resultFinal= null;
		
		//String s = "select s from kiwi.model.skill.UserSkill s left join s.skills b, kiwi.model.skill.FakeSkill f where f.fakeSkill not in ( index(b) )";
		String s = "select s from kiwi.model.skill.UserSkill s";		
			javax.persistence.Query q = entityManager.createQuery(s);
			try {
				result = (List<UserSkill>)q.getResultList();
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				log.warn("error while listing user skills: query failed");
			}
			
			if (result!=null && !result.isEmpty()) {
				resultFinal = new ArrayList<UserSkill>();
			}
			
			for (UserSkill userSkill : result) {
				for (String skill :userSkill.getSkills().keySet()){
					if (this.getFakeSkillsBySkillName(skill)==null && !resultFinal.contains(userSkill)) {
						resultFinal.add(userSkill);	
					} 
				}
			}
			
			if(resultFinal == null ) {
				return Collections.EMPTY_LIST;
			} else {
				return resultFinal;
			}
	}	
	
	
	/**
	 * It gets the result by skill
	 */
	@SuppressWarnings("unchecked")
	public List<FakeSkill> listFakeSkills(){
		List<FakeSkill> result = null;	
		String s = "from kiwi.model.skill.FakeSkill s";
			javax.persistence.Query q = entityManager.createQuery(s);
			try {
				result = (List<FakeSkill>)q.getResultList();
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				log.warn("error while listing fake Skills: query failed");
			}
			if(result == null ) {
				return Collections.EMPTY_LIST;
			} else {
				return result;
			}
	}	
	
	
	/**
	 * It gets the result by skill
	 */
	public FakeSkill getFakeSkillsByUser(User user){
		FakeSkill result = null;	
		String s = "select s from kiwi.model.skill.FakeSkill s where s.user = :user";
			javax.persistence.Query q = entityManager.createQuery(s);
			q.setParameter("user", user);
			try {
				result = (FakeSkill)q.getSingleResult();
			} catch (NoResultException ex) {
				log.warn("error while listing user skills");
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				log.warn("error while listing user skills: query failed");
			}
			return result;
	}	
	
	
	/**
	 * It gets the result by skill
	 */
	public FakeSkill getFakeSkillsBySkillName(String skillName){
		FakeSkill result = null;	
		String s = "select s from kiwi.model.skill.FakeSkill s where s.fakeSkill = :skillName";
			javax.persistence.Query q = entityManager.createQuery(s);
			q.setParameter("skillName", skillName);
			try {
				result = (FakeSkill)q.getSingleResult();
			} catch (NoResultException ex) {
				log.warn("error while listing user skills");
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				log.warn("error while listing user skills: query failed");
			}
			return result;
	}	
	
	
	/**
	 * @param skillName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getFakeSkills(){
		List<String> result = null;	
		String s = "select s.fakeSkill from kiwi.model.skill.FakeSkill s";
			javax.persistence.Query q = entityManager.createQuery(s);
			try {
				result = (List<String>)q.getResultList();
			} catch (NoResultException ex) {
				log.warn("error while listing user skills");
			} catch (PersistenceException ex) {
				ex.printStackTrace();
				log.warn("error while listing user skills: query failed");
			}
			return result;
	}	
	
	/**
	 * return comments of given user
	 * @param user
	 * @return
	 */
	private Set<ContentItem> listCommentsByUser(User user) {
		Set<ContentItem> resultList= new HashSet<ContentItem>();
		for (CommentActivity commentActivity : historyService.listCommentsByUser(user)) {
			resultList.add(commentActivity.getContentItem());
		}
		return resultList;
	}	
		
	/**
	 * return edits of given user
	 * @param user
	 * @return
	 */
	private Set<ContentItem> listEditsByUser(User user) {
		Set<ContentItem> resultList= new HashSet<ContentItem>();
		for (EditActivity editActivity : historyService.listEditsByUser(user)) {
			resultList.add(editActivity.getContentItem());
		}
		return resultList;
	}	
	
	/**
	 * return visits of given user
	 * @param user
	 * @return
	 */
	private Set<ContentItem> listVisitsByUser(User user) {
		Set<ContentItem> resultList= new HashSet<ContentItem>();
		for (VisitActivity visitActivity : historyService.listVisitsByUser(user)) {
			resultList.add(visitActivity.getContentItem());
		}
		return resultList;
	}
	

	/**
	 * @param user1
	 * @param user2
	 * @return content items authored by user1 rated by user2
	 */
	@SuppressWarnings({ "unused", "unchecked", "unchecked" })
	private List<ContentItem> listItemsRatedByUser(User user) {
		List<ContentItem> result = new LinkedList<ContentItem>();

		String s = "SELECT ?ci WHERE { " +
					" ?s  <" + Constants.NS_RDF       + "type>          <"+ Constants.NS_KIWI_CORE + "RatingData> . " +
					" ?s  <" + Constants.NS_KIWI_CORE + "author>        <"+ user.getResource()    + "> . " + 
					" ?s  <" + Constants.NS_KIWI_CORE + "hasRatingFacade> ?ci . }";
			javax.persistence.Query q = kiwiEntityManager.createQuery(s, SPARQL,
					ContentItem.class);  
		q.setHint("org.hibernate.cacheable", true);
		try {
			result = (List<ContentItem>) q.getResultList();
		} catch (PersistenceException ex) {
			ex.printStackTrace();
			log
					.warn("error while listing items rated:  query failed");
		}

		if (result == null) {
			return Collections.EMPTY_LIST;
		} else {
			return result;
		}
	}


	/* (non-Javadoc)
	 * @see kiwi.api.skill.SkillService#listUserContributions(kiwi.model.user.User)
	 */
	@SuppressWarnings("unchecked")
	public List<ContentItem> listUserContributions(User currentUser){
		Set<Long> duplicatedList = new HashSet<Long>();
		Set<ContentItem> cis = new HashSet<ContentItem>();
		Set<ContentItem> userContributions = new HashSet<ContentItem>();
		cis.addAll(contentItemService.getContentItemByAuthor(currentUser));
		cis.addAll(contentItemService.getContentItemTaggedByUser(currentUser));
		cis.addAll(listCommentsByUser(currentUser));
		cis.addAll(listEditsByUser(currentUser));
		cis.addAll(listItemsRatedByUser(currentUser));

		for (ContentItem ci : cis) {
			if(!duplicatedList.contains(ci.getId())){
				duplicatedList.add(ci.getId());
				userContributions.add(ci);
			}
		}
//		System.out.println("cis size "+ cis.size()+" usc size "+ userContributions.size());
		return new ArrayList(userContributions);
	}

	/**
     * 
     */
    public void loadFakeSkillList(){
    	Set<String> set = customTermTokenizer.getStopWordList();
    	for (String string : set) {
    		addFakeSkill(string);
    	}
    }
    
    /**
     * @param fakeSkillString
     */
    public void addFakeSkill(String fakeSkillString){
    	if (fakeSkillString!=null && !fakeSkillString.equals("") && getFakeSkillsBySkillName(fakeSkillString)==null) {
    		FakeSkill fakeSkill = new FakeSkill();
    		fakeSkill.setFakeSkill(fakeSkillString);
    		fakeSkill.setUser(currentUser);
    		entityManager.persist(fakeSkill);
//    		entityManager.flush();
		}
    }
	
	/* (non-Javadoc)
	 * @see kiwi.api.skill.SkillService#removeSkill(java.lang.String)
	 */
	public boolean removeSkill(String skillName) {
		addFakeSkill(skillName);
		this.computeUserSkills();
		return this.getFakeSkillsBySkillName(skillName)!=null;
	}
	
		
}
