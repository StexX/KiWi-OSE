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
package kiwi.service.tagging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.tagging.TaggingService;
import kiwi.api.tagging.TaggingServiceLocal;
import kiwi.api.tagging.TaggingServiceRemote;
import kiwi.api.transaction.CIVersionBean;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.exception.NonUniqueRelationException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;

/**
 * A DAO service for managing tags. Provides convenience methods for creating new tags, modifying
 * tags, and removing tags.
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("taggingService")
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
public class TaggingServiceImpl implements TaggingServiceLocal, TaggingServiceRemote {

	@Logger
	private Log log;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
		
	@In
	private EntityManager entityManager;
	
	@In
	private TripleStore tripleStore;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private SolrService solrService;
	
	@In(create=true)
	private User currentUser;
	
	/**
	 * Create a new tagging of taggedItem with taggingItem based on the given label for taggingUser.
	 * This method creates a new tag instance with a generated, unique uri resource. The tag is then
	 * persisted in the entityManager and tripleStore, and returned to the caller.
	 * 
	 * @param label a textual label of the tagging; this is usually the string entered by the user
	 *              which has then been used to look up taggingItem
	 * @param taggedItem the tagged content item
	 * @param taggingItem the content item used as tag
	 * @param taggingUser the user who performed the tagging
	 * @return the newly created and persisted tag
	 */
	public Tag createTagging(String label, ContentItem taggedItem, ContentItem taggingItem, User taggingUser) {
		assert(label != null);
		assert(taggedItem != null);
		assert(taggingItem != null);
		assert(taggingUser != null);
		
		Tag tag = new Tag();
		tag.setResource(tripleStore.createUriResource(
				configurationService.getBaseUri() + 
				"/tags/"+UUID.randomUUID().toString() + 
				"/"+label.toLowerCase().replace(" ","_")));
		tag.setTaggedBy(taggingUser);
		tag.setTaggingResource(taggingItem);
		tag.setTaggedResource(taggedItem);
		
		ContentItem ci = contentItemService.getContentItemByTitle(tag.getLabel());
		
		if(ci == null || !isSKOSConcept(ci)){
				KiWiUriResource kuri = tripleStore.createUriResource(Constants.NS_KIWI_CORE+"FreeTag");
				tag.getResource().addType(kuri);
		}
		
		contentItemService.updateTitle(tag.getResource().getContentItem(), "tag: "+label.toLowerCase());	

		tag.setDeleted(false);
		
		kiwiEntityManager.persist(tag);
		
		log.debug("created new tag #0",label.toLowerCase());
		
		return addTagging(tag);
	}
	
	private boolean isSKOSConcept(ContentItem ci){
			KiWiUriResource kuri = (KiWiUriResource) ci.getResource();
			for(KiWiResource kr : kuri.getTypes()){
				KiWiUriResource kur = (KiWiUriResource) kr;
				if(kur.getKiwiIdentifier().contains("http://www.w3.org/2004/02/skos/core#Concept")){
					log.info("Concept"+kur.getKiwiIdentifier());
					return true;
				}
			}
		return false;
	}
	
	/**
	 * Verify whether the given label has been associated with the content item.
	 * 
	 * @param taggedItem the content item that has been tagged
	 * @param label the label used for tagging
	 * @return true if the label was used for tagging the item.
	 */
	public boolean hasTag(ContentItem taggedItem, String label) {
		Query q = entityManager.createNamedQuery("taggingAction.getTagByLabelItem");
		q.setParameter("itemid", taggedItem.getId());
		q.setParameter("label", label);
		q.setMaxResults(1);
		q.setHint("org.hibernate.cacheable", true);
		
		return q.getResultList().size() > 0;
		
	}
	
	/**
	 * Adds a tag to the database/triplestore and to the transaction data, 
	 * where it will be put under version-control
	 * @param tag, the tag that will be added
	 * @return tag, the tag that has been added
	 */
	public Tag addTagging(Tag tag) {
		assert(tag != null);
		TransactionService ts = (TransactionService) Component.getInstance("transactionService");
		tag.setDeleted(false);
		
		ContentItem taggedItem  = tag.getTaggedResource();
		User taggingUser = tag.getTaggedBy();
		ContentItem taggingItem = tag.getTaggingResource();
		CIVersionBean civ = ts.getTransactionCIVersionData(taggedItem);
		log.debug("TaggingServiceImpl.addTagging() called ts.getTransactionCIVersionData()");
		if(civ != null) {
			
			// add kiwi:Tag type to taggingItem, and manually add relation hgtags:taggedWithTag
			KiWiUriResource t_tag = tripleStore.createUriResource(Constants.NS_KIWI_CORE + "Tag");
			if(!taggingItem.getResource().hasType(t_tag)) {
				taggingItem.getResource().addType(t_tag);
			}
			try {
				taggedItem.getResource().addOutgoingNode("<"+Constants.NS_HGTAGS+"taggedWithTag>", taggingItem.getResource());
			} catch (NamespaceResolvingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// persist data
			kiwiEntityManager.persist(tag);
//			taggedItem.getTags().add(tag);
//			kiwiEntityManager.persist(taggedItem);
			
			// add the tag to transaction
			civ.getTransactionAddedTags().add(tag);
			
			// notify the activity logging service that the user added a new tag
			Events.instance().raiseEvent(KiWiEvents.ACTIVITY_ADDTAG, taggingUser,tag);
	
			return tag;
		} else {
			return null;
		}
	}

	/**
	 * Remove an existing tagging from the database.
	 * 
	 * @param tag the tagging to remove
	 */
	public void removeTagging(Tag tag) {
		ContentItem taggedItem  = tag.getTaggedResource();
		ContentItem taggingItem = tag.getTaggingResource();
		//User        taggingUser = tag.getTaggedBy();
		
		try {
			taggedItem.getResource().removeOutgoingNode("<"+Constants.NS_HGTAGS+"taggedWithTag>", taggingItem.getResource());
		} catch (NamespaceResolvingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tag.setDeleted(true);
		
		// fred: why did you introduce this?
		// taggingItem.setDeleted(true);
		kiwiEntityManager.remove(tag);

		// add the tag to transaction
		TransactionService ts = (TransactionService) Component.getInstance("transactionService");
		// add triple to current transaction data
		ts.getTransactionCIVersionData(tag.getTaggedResource()).getTransactionRemovedTags().add(tag);
		log.info("TaggingServiceImpl.removeTagging() called ts.getTransactionCIVersionData()");
		
		log.debug("removing tagging #0",tag);
		
		Events.instance().raiseEvent(KiWiEvents.ACTIVITY_REMOVETAG,currentUser,tag);
	}

	/**
	 * Provide autocompletion of content items that are of type "kiwi:Tag" or "skos:Concept" and 
	 * whose title starts with "prefix". Autocompletion is delegated to the SOLR search service.
	 * @param prefix
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> autocomplete(String prefix) {
		if(prefix.length() >= 2) {
		
		
			KiWiSearchCriteria crit = new KiWiSearchCriteria();
			
			StringBuilder qString = new StringBuilder();
			
			// add prefix to query string
			qString.append("title:"+prefix+"*");
			qString.append(" ");
			
			// add (type:kiwi:Tag OR type:skos:Concept)
			qString.append("(");
			qString.append("type:\"uri::"+Constants.NS_KIWI_CORE+"Tag\"");
			qString.append(" OR ");
			qString.append("type:\"uri::"+Constants.NS_SKOS+"Concept\"");
			qString.append(")");
			
			crit.setSolrSearchString(qString.toString());
			
			SolrQuery query = solrService.buildSolrQuery(crit);
			query.setStart(0);
			query.setRows(Integer.MAX_VALUE);
			query.setSortField("title_id", ORDER.asc);
			
			query.setFields("title");
			
			QueryResponse rsp = solrService.search(query);
			
			List<String> result = new LinkedList<String>();
			
			if(rsp != null) {
				SolrDocumentList docs = rsp.getResults();
			
			
			
				for(SolrDocument doc : docs) {
					result.add(doc.getFieldValue("title").toString());
				}
			}
			return result;
		} else {
			return Collections.EMPTY_LIST;
		}
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Tag> getTags(ContentItem item) {
		Query q = entityManager.createNamedQuery("taggingService.listTagsByContentItem");
		q.setParameter("ci", item);
		q.setHint("org.hibernate.cacheable", true);
		return q.getResultList();
	}
	

	@SuppressWarnings("unchecked")
	public List<Tag> getTagsByUser(User currentUser) {
		log.info("getTagByUser");
		Query q = entityManager.createNamedQuery("taggingService.listTagsByUser");
		
		q.setParameter("cu", currentUser);
		q.setHint("org.hibernate.cacheable", true);
		return q.getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	public List<Tag> getTagsByLabel(String tagLabel) {
		Query q = entityManager.createNamedQuery("taggingService.listTagsByLabel");
		q.setParameter("label", tagLabel);
		return q.getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	public List<Tag> getAllTags() {
		Query q = entityManager.createNamedQuery("taggingService.listAllTags");
		return q.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Tag> getAllDistinctTags() {
		Query q = entityManager.createNamedQuery("taggingService.listAllDistinctTags");
		return q.getResultList();
	}
	
	public Tag getTagById(long tagId) {
		Tag result;
		javax.persistence.Query q = entityManager
				.createQuery("select distinct t from kiwi.model.tagging.Tag t where t.id =:tagId  and t.taggingResource.deleted = false");
		q.setParameter("tagId", tagId);
		try {
		result = (Tag)q.getSingleResult();
		} catch (NoResultException ex) {
			result = null;
		}
		return result;
	}

	@Override
	public Long getTagUsage(ContentItem tag) {
		Query q = entityManager.createQuery("select count(t.taggedResource.id) as count " +
					  "from kiwi.model.tagging.Tag t " +
					  "where t.taggingResource.title = :title " +
					  "  and t.deleted = false "+
					  "group by t.taggedResource.id");

		q.setParameter("title", tag.getTitle());
		log.info("query built for tag #0: #1", tag.getTitle(), q.toString());
		log.info("query result: #0", q.getSingleResult().toString());
		return (Long)q.getSingleResult();
	}

	@Override
	public void addTags(ContentItem item, String[] labels) {
		for(String label:labels){
			label = label.trim();
			ContentItem taggingItem = contentItemService.getContentItemByTitle(label);
			if(taggingItem == null) {
				// create new Content Item of type "tag" if the tag does not yet exist
				taggingItem = contentItemService.createContentItem("content/"+label.toLowerCase().replace(" ","_")+"/"+UUID.randomUUID().toString());
				taggingItem.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag"));
				contentItemService.updateTitle(taggingItem, label);
				kiwiEntityManager.persist(taggingItem);
				log.info("created new content item for non-existant tag");
			}
			
			createTagging(label, item, taggingItem, currentUser);
		}					
		entityManager.refresh(item);

		Events.instance().raiseEvent("tagUpdate");
	}

	@Override
	public boolean isControlled(KiWiResource taggingResource) {
		return taggingResource.hasType(Constants.NS_SKOS + "Concept");	
	}

	@Override
	public void removeTaggings(ContentItem taggedItem, String[] tagUris,
			boolean shared) {
		for(String tagUri:tagUris){
			tagUri = tagUri.trim();
			ContentItem taggingItem = contentItemService.getContentItemByUri(tagUri);
				removeTagging(taggedItem, taggingItem, shared);
		}
		
	}

	/**
	 * Removes tagging made by any user.
	 * @param taggedItem
	 * @param taggingItem
	 */
	@SuppressWarnings("unchecked")
	private void removeTagging(ContentItem taggedItem,
			ContentItem taggingItem, boolean shared) {
		Query q;
		if(shared){
			q= entityManager.createQuery("select t " +
					  "from kiwi.model.tagging.Tag t " +
					  "where t.taggingResource.id = :tagId " +
					  "  and t.taggedResource.id = :itemId "+
					  "  and t.deleted = false ");

			q.setParameter("tagId", taggingItem.getId());
			q.setParameter("itemId", taggedItem.getId());
		}else {
			q= entityManager.createQuery("select t " +
					  "from kiwi.model.tagging.Tag t " +
					  "where t.taggingResource.id = :tagId " +
					  "  and t.taggedResource.id = :itemId "+
					  "  and t.taggedBy.login = :login " +
					  "  and t.deleted = false ");

			q.setParameter("tagId", taggingItem.getId());
			q.setParameter("itemId", taggedItem.getId());
			q.setParameter("login", currentUser.getLogin());
		}
		List<Tag> tagsToDelete = q.getResultList();
		for(Tag tag:tagsToDelete){
			removeTagging(tag);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tag> getTagsByTaggedTaggingIds(Long taggedResId, Long taggingResId) {
		Query q = entityManager.createQuery("select t " +
				  "from kiwi.model.tagging.Tag t " +
				  "where t.taggingResource.id = :taggingResId " +
				  " and t.taggedResource.id = :taggedResId " +
				  " and t.deleted = false");

		q.setParameter("taggedResId", taggedResId);
		q.setParameter("taggingResId", taggingResId);
		
	return (List<Tag>)q.getResultList();
	}
	
	//return all freetags -> all contentItems which have thy type freeTag
	public List<ContentItem> getFreeTags(){
		ArrayList<ContentItem> freeTags = new ArrayList<ContentItem>();
		KiWiSearchCriteria c = new KiWiSearchCriteria();
		c.setSolrSearchString("type:\"uri::" + Constants.NS_KIWI_CORE+ "FreeTag\"");
		c.setLimit(-1);
		for (SearchResult r : solrService.search(c).getResults()) {
			ContentItem ci = r.getItem();

			log.info(ci.getTitle());
			String tag = ci.getTitle().replace("tag:", "").trim();
			log.info(tag);
			List<Tag> tl = getTagsByLabel(tag);
			//there should exist only one tag with the same name
			if(tl.size() > 0){
				Tag t = tl.get(0);
				ContentItem freeTag = t.getTaggingResource();
				log.info("freeTag #0 ",freeTag.getTitle());
				if(freeTag != null){
					freeTags.add(freeTag);
				}
			}
		}
		return freeTags;
	}
	
}
