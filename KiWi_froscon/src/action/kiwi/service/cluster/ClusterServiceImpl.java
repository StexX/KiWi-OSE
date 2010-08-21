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


package kiwi.service.cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.cluster.ClusterServiceLocal;
import kiwi.api.cluster.ClusterServiceRemote;
import kiwi.api.tagging.TaggingService;
import kiwi.model.Constants;
import kiwi.model.cluster.SpectralClusterBean;
import kiwi.model.cluster.SpectralClusteringModel;
import kiwi.model.cluster.TagCluster;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 * @author Fred Durao
 *
 */
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("clusterService")
public class ClusterServiceImpl implements ClusterServiceLocal, ClusterServiceRemote {
	
	@Logger
	private Log log;

	@In
	private EntityManager entityManager;

	@In
	private TaggingService taggingService;
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

	
	@SuppressWarnings("unchecked")
	private List<ContentItem> getPagesCreatedByUsers() {
		String s = "select ci from ContentItem ci, kiwi.model.tagging.Tag tag" +
				   " join fetch ci.resource " +
	  			   " where ci.resource is not null and " +
	  			   " ci.author is not null and " +
	  			   //" ci.author.login <> 'admin' and " +
	  			   " ci.author.login <> 'anonymous' and " +
	  			   " ci.id <> tag.taggingResource.id and "+
	  			   " ci.title <> tag.taggingResource.title and "+
		  			 " ci.title = 'page1' or "+
		  			" ci.title = 'page2' or "+
		  			" ci.title = 'page3' or "+
		  			" ci.title = 'page4' or "+
		    	  "  EXISTS (from KiWiTriple t " +
		    	  "              where ci.resource.id = t.subject.id " +
		    	  "                and t.property.uri = '"+Constants.NS_RDF+"type' " +
		    	  "                and t.object.uri in ('"+Constants.NS_KIWI_CORE+"ContentItem')) and " +	  			   
		    	  "  NOT EXISTS (from KiWiTriple t " +
		    	  "              where ci.resource.id = t.subject.id " +
		    	  "                and t.property.uri = '"+Constants.NS_RDF+"type' " +
		    	  "                and t.object.uri in ('"+Constants.NS_KIWI_CORE+"Tagging' )) and " +	  
		    	  
		    	  "  NOT EXISTS (from KiWiTriple t " +
		    	  "              where ci.resource.id = t.subject.id " +
		    	  "                and t.property.uri = '"+Constants.NS_RDF+"type' " +
		    	  "                and t.object.uri in ('"+Constants.NS_KIWI_SPECIAL+"KiWiGroup')) and " +	
		    	  
		    	  "  NOT EXISTS (from KiWiTriple t " +
		    	  "              where ci.resource.id = t.subject.id " +
		    	  "                and t.property.uri = '"+Constants.NS_RDF+"type' " +
		    	  "                and t.object.uri in ('"+Constants.NS_KIWI_CORE+"User')) and " +		
   	  			  
		    	  " ci.deleted=false";		
		
		javax.persistence.Query q = entityManager.createQuery(s);
		return q.getResultList();
	}
	@SuppressWarnings("unchecked")
	private List<ContentItem> getPagesCreatedByUsers2() {
		
		String s = "select ci from ContentItem ci left outer join ci.tagLabels as tagLabel, kiwi.model.tagging.Tag tag" +
				   " join fetch ci.resource " +
	  			   " where ci.resource is not null and " +
	  			   " ci.author is not null and " +
	  			   " ci.author.login <> 'admin' and " +
	  			   " ci.author.login <> 'anonymous' and " +
	  			   " ci.id <> tag.taggingResource.id and "+
	  			   " count(tagLabel) > 0 and "+
	  			  "  EXISTS (from KiWiTriple t " +
		    	  "              where ci.resource.id = t.subject.id " +
		    	  "                and t.property.uri = '"+Constants.NS_RDF+"type' " +
		    	  "                and t.object.uri in ('"+Constants.NS_KIWI_CORE+"ContentItem')) and " +	  			   
		    	  
		    	  "  NOT EXISTS (from KiWiTriple t " +
		    	  "              where ci.resource.id = t.subject.id " +
		    	  "                and t.property.uri = '"+Constants.NS_RDF+"type' " +
		    	  "                and t.object.uri in ('"+Constants.NS_KIWI_CORE+"Tagging' )) and " +	  
		    	  
		    	  "  NOT EXISTS (from KiWiTriple t " +
		    	  "              where ci.resource.id = t.subject.id " +
		    	  "                and t.property.uri = '"+Constants.NS_RDF+"type' " +
		    	  "                and t.object.uri in ('"+Constants.NS_KIWI_SPECIAL+"KiWiGroup')) and " +	
		    	  
		    	  "  NOT EXISTS (from KiWiTriple t " +
		    	  "              where ci.resource.id = t.subject.id " +
		    	  "                and t.property.uri = '"+Constants.NS_RDF+"type' " +
		    	  "                and t.object.uri in ('"+Constants.NS_KIWI_CORE+"User')) and " +		
   	  			  
		    	  " ci.deleted=false";		
		
		javax.persistence.Query q = entityManager.createQuery(s);
		return q.getResultList();
	}	
	
	
	/**
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ContentItem getPagesById(Long id) {
		String s = "select ci from ContentItem ci where ci.id = :id and " +
		    	  " ci.deleted=false";		

		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("id", id);		
		return (ContentItem)q.getSingleResult();
	}	
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,List<ContentItem>> getTagDocCluster() {

		//List<User> users = userService.getAllCreatedUsers();
		List<Tag> tagResources = taggingService.getAllDistinctTags();
		Set<String> tagLabels = new HashSet<String>();
		List<ContentItem> pagesContent = getPagesCreatedByUsers();
		
//		for (ContentItem xx : pagesContent) {
//				System.out.println(xx.getTitle());
//				System.out.println(xx.getKiwiIdentifier());
//		}		
		
		Set<ContentItem> pagesX = new HashSet<ContentItem>();
		for (ContentItem contentItem2 : pagesContent) {
			if (contentItem2.getTitle().equals("page1") || contentItem2.getTitle().equals("page2") || 
					contentItem2.getTitle().equals("page3") || contentItem2.getTitle().equals("page4")) {
						pagesX.add(contentItem2);	
			}
		}
		
		for (Tag tag : tagResources) {
			if (!tagLabels.contains(tag.getTaggingResource().getTitle())) {
				tagLabels.add(tag.getTaggingResource().getTitle());
			}
		}

		List<ContentItem> pages = new ArrayList<ContentItem>(pagesX); 

		SpectralClusterBean [][] spectralClusterBeans = new SpectralClusterBean[tagLabels.size()][pages.size()];
		
		List<String> tagCollection = new ArrayList(tagLabels);

		for (int i = 0; i < tagCollection.size(); i++) {
			for (int j = 0; j < pages.size(); j++) {
				float tagFrequency = this.getTagFrequencyByContentItem(((String)tagCollection.get(i)).toLowerCase() , pages.get(j), new Float(pages.size()));
		//		System.out.println(" tag "+((String)tagCollection.get(i)).toLowerCase() +" has freq " + tagFrequency+" for page "+pages.get(j).getTitle());
				spectralClusterBeans[i][j] = new SpectralClusterBean(pages.get(j).getId(),(String)tagCollection.get(i),i,j,tagFrequency);
			}			
		}		

		double [][] userTags = new double[tagCollection.size()][pages.size()];
		for (int i = 0; i < tagCollection.size(); i++) {
			for (int j = 0; j < pages.size(); j++) {
				userTags[i][j] = (double)spectralClusterBeans[i][j].getScore();					
			}			
		}
		SpectralClusteringModel spectralClusteringRecommendation = new SpectralClusteringModel();
		Matrix frequencyMatrix = new Matrix(userTags);
		
		SingularValueDecomposition singularValueDecomposition=null;
		
		try {
			singularValueDecomposition = spectralClusteringRecommendation.computeLeftSigularValueDecompositionMatrix(frequencyMatrix);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Dataset[] dataSets = null;
		try {
			dataSets = spectralClusteringRecommendation.clusterSpectralClusteringMatrix(singularValueDecomposition);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<Set<Double>,Set<Double>>  mapClusters = getMappingPositions(dataSets);
		
		Map<String,List<ContentItem>> showCLuster  = new HashMap<String, List<ContentItem>>();	

		for (Set<Double> keys : mapClusters.keySet()) {
			StringBuffer tagStringBuffer = new StringBuffer();
			for (Double tagPos : keys) {
				tagStringBuffer.append((spectralClusterBeans[tagPos.intValue()][0]).getTagLabel());
				tagStringBuffer.append("+");
			}
			Set<ContentItem> cis = new HashSet<ContentItem>(); 
			
			for (Double tagPos : keys) {
				for (int i = 0; i < pages.size(); i++) {
					if (spectralClusterBeans[tagPos.intValue()][i].getScore()>0) {
						Long ciId = spectralClusterBeans[tagPos.intValue()][i].getContentItemId();
						ContentItem contentItem = getPagesById(ciId);
						cis.add(contentItem);						
					}
				}
			}		
			showCLuster.put(tagStringBuffer.substring(0,tagStringBuffer.lastIndexOf("+")), new ArrayList(cis));
		}
		
		
		return showCLuster;
	}

	/**
	 * @param dataSets
	 * @return
	 */
	private Map<Set<Double>,Set<Double>> getMappingPositions(Dataset[] dataSets) {
		
		Map<Set<Double>,Set<Double>> mapClusters  = new HashMap<Set<Double>, Set<Double>>();
		for (int i = 0; i < dataSets.length; i++) {
			Dataset dataSet = dataSets[i];
			Set<Double> tagsCluster = new HashSet<Double>();
			Set<Double> cissCluster = new HashSet<Double>();			
			for (int j = 0; j < dataSet.size(); j++) {
				Instance instance = dataSet.get(j);
//				System.out.println(instance.toString());
				if (instance.classValue().toString().contains("tag")) {
					Double dv = new Double(instance.classValue().toString().replace("tag_pos_", ""));
					tagsCluster.add(dv);
				}
				if (instance.classValue().toString().contains("page")) {
					Double dv = new Double(instance.classValue().toString().replace("page_pos_", ""));
					cissCluster.add(dv);
				}
				if (!mapClusters.keySet().containsAll(tagsCluster) && tagsCluster.size()>1 && cissCluster.size()>1) {
					mapClusters.put(tagsCluster, cissCluster);
				}
				
			}
		}
		
//		for (Set<Double> keys : mapClusters.keySet()) {
//			System.out.println("for "+keys+ " we have "+mapClusters.get(keys));
//		}
		return mapClusters;
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
	
	
	
	/**
	 * @param tagName
	 * @param user
	 * @return
	 */
	private float getTagFrequencyByUser(String tagName, User user) {
		Long amountOfTags;
		Long amountOfIndividualTag;
		Float tagFrequency = 0f;
		
		String s = "select count (t) from kiwi.model.tagging.Tag t " +
		   " where t.taggedBy =:user " +
		   " and t.taggingResource.deleted = false";		

		javax.persistence.Query q = entityManager.createQuery(s);
		q.setParameter("user", user);
		try {
			amountOfTags = (Long)q.getSingleResult();
		} catch (NoResultException ex) {
			amountOfTags = null;
		}
			
		
		String s2 = "select count (t) from kiwi.model.tagging.Tag t " +
		   "where t.taggingResource.title = :tagName and t.taggedBy =:user" +
		   " and t.taggingResource.deleted = false";		
		q = entityManager.createQuery(s2);
		q.setParameter("tagName", tagName);
		q.setParameter("user", user);
		try {
			amountOfIndividualTag = (Long)q.getSingleResult();
		} catch (NoResultException ex) {
			amountOfIndividualTag = 0l;
		} 
		
	    if ( Double.isNaN(amountOfIndividualTag)|| Double.isNaN(amountOfIndividualTag) || Double.isInfinite(amountOfIndividualTag)|| Double.isInfinite(amountOfIndividualTag)){
	    	tagFrequency = 0f;
	    }else{
	    	tagFrequency = amountOfIndividualTag.floatValue() / amountOfTags.floatValue(); 
	    	if (tagFrequency.equals(Float.NaN) ){
		    	tagFrequency = 0f;
	    	  }
	    }
		return tagFrequency.floatValue();
	}

	/**
	 * @param tagName
	 * @param contentItem
	 * @param amountOfTags
	 * @return
	 */
	private float getTagFrequencyByContentItem(String tagName, ContentItem contentItem, Float amountOfTags) {
		Long amountOfIndividualTag;
		Float tagFrequency = 0f;
		String s2 = "select count (t) from kiwi.model.tagging.Tag t " +
		   "where t.taggingResource.title = :tagName and t.taggedResource =:contentItem" +
		   " and t.taggingResource.deleted = false";
		javax.persistence.Query q = entityManager.createQuery(s2);		
		q = entityManager.createQuery(s2);
		q.setParameter("tagName", tagName);
		q.setParameter("contentItem", contentItem);
		try {
			amountOfIndividualTag = (Long)q.getSingleResult();
		} catch (NoResultException ex) {
			amountOfIndividualTag = 0l;
		} 
		
	    if ( Double.isNaN(amountOfIndividualTag)|| Double.isNaN(amountOfIndividualTag) || Double.isInfinite(amountOfIndividualTag)|| Double.isInfinite(amountOfIndividualTag)){
	    	tagFrequency = 0f;
	    }else{
	    	tagFrequency = amountOfIndividualTag.floatValue() / amountOfTags.floatValue(); 
	    	if (tagFrequency.equals(Float.NaN) ){
		    	tagFrequency = 0f;
	    	  }
	    }
		return tagFrequency.floatValue();
	}


	/* (non-Javadoc)
	 * @see kiwi.api.cluster.ClusterService#calculateSpectralCluster()
	 */
	@Override
	public void calculateSpectralCluster() {
		if (deleteSpectralCluster()) {
			Map<String,List<ContentItem>> tagClusters = getTagDocCluster();
			for (String tagKey : tagClusters.keySet()) {
				TagCluster tagCluster = new TagCluster();
				tagCluster.setCluster(tagKey);
				tagCluster.setContentItems(tagClusters.get(tagKey));
				entityManager.persist(tagCluster);
//				entityManager.flush();
			}
		}
	}
	
	/**
	 * It gets the result by skill
	 */
	public boolean deleteSpectralCluster(){
		List<TagCluster> tagClusters = this.listTagCluster();
		for (TagCluster tagCluster : tagClusters) {
			entityManager.remove(tagCluster);
//			entityManager.flush();
		}
		return listTagCluster().isEmpty();
	} 	


	/* (non-Javadoc)
	 * @see kiwi.api.cluster.ClusterService#listTagCluster()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TagCluster> listTagCluster() {
			log.info("listing  tag clusters...");
			Query q = entityManager.createNamedQuery("cluster.listTagCluster");
			q.setHint("org.hibernate.cacheable", true);
			return (List<TagCluster>)q.getResultList();
		}
}
