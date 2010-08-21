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

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.tagging.ExtendedTagCloudEntry;
import kiwi.api.tagging.TagCloudEntry;
import kiwi.api.tagging.TagCloudService;
import kiwi.api.tagging.TagCloudServiceLocal;
import kiwi.api.tagging.TagCloudServiceRemote;
import kiwi.api.user.UserService;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Sebastian Schaffert
 * 
 */
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("tagCloudService")

public class TagCloudServiceImpl implements TagCloudServiceLocal,
		TagCloudServiceRemote {

	@Logger
	private Log log;

	@In(create=true)
	private User currentUser;
	
	@In
	private EntityManager entityManager;
	
	public List<TagCloudEntry> userTagCloud(User u) {
		log.info("aggregating tags for user #0", u.getLogin());

		List<TagCloudEntry> result = new LinkedList<TagCloudEntry>();
		int countMax = 1;
		Date firstTaggingDate = null;
		Date lastTaggingDate = null;
		
		
		Query q = entityManager.createNamedQuery("tagCloudService.getTagCloudByAuthor");
		q.setParameter("login", u.getLogin());
		q.setHint("org.hibernate.cacheable", true);
		
		List<Object[]> qres = q.getResultList();
		for(Object[] o : qres) {
			// can get very expensive!
			//ContentItem tag = entityManager.find(ContentItem.class,(Long)o[0]);
			String title = (String)o[1];
			int count = ((Long)o[2]).intValue();
			Date firstModified = (Date)o[3];
			Date lastModified = (Date)o[4];
			
			if(count > countMax) {
				countMax = count;
			}
			
			if(firstTaggingDate == null || firstTaggingDate.getTime() > firstModified.getTime()) {
				firstTaggingDate = firstModified;
			}
			
			if(lastTaggingDate == null || lastTaggingDate.getTime() < lastModified.getTime()) {
				lastTaggingDate = lastModified;
			}
			
			TagCloudEntry bean = new TagCloudEntry(count,title);
			bean.setLastTaggingDate(lastTaggingDate);

			// font size
			if (countMax > 1) {
				bean.setSize(100 + (count - 1) * 100 / (countMax - 1));
			} else {
				bean.setSize(100);
			}
			
			// color coding; we use 100 for the first tagging time and 200 for
			// the last and calculate the
			// value for this tag relative to this interval
			int colorCode = (int) (100 + (lastModified.getTime() - firstTaggingDate
					.getTime())
					* 100.0
					/ (lastTaggingDate.getTime() - firstTaggingDate.getTime()));
			bean.setColorCode(colorCode);

			result.add(bean);
		}
		return result;
	}
	
	/**
	 * Do a more efficient, database-driven aggregation of the tags of a content item.
	 * Otherwise yields the same results as aggregateTags(Collection<Tag>)
	 * @param item
	 * @return
	 */
	public List<TagCloudEntry> aggregateTagsByCI(ContentItem item) {
		Query q = entityManager.createNamedQuery("tagCloudService.aggregateTagsByContentItem");
		q.setParameter("ci", item);
		q.setHint("org.hibernate.cacheable", true);
		
		List<Object[]> qresult = q.getResultList();
		List<TagCloudEntry> result = new LinkedList<TagCloudEntry>();

		for(Object[] r : qresult) {
			String title = (String)r[0];
			Long count = (Long)r[1];
			Date lastModified = (Date)r[2];
			TagCloudEntry b = new TagCloudEntry(count.intValue(), title);
			b.setLastTaggingDate(lastModified);
			
			result.add(b);
		}
		return result;
	}
	
	/**
	 * Do a more efficient, database-driven aggregation of the tags of a content item.
	 * Otherwise yields the same results as aggregateTags(Collection<Tag>)
	 * @param item
	 * @return
	 */
	public List<TagCloudEntry> aggregateTagsByCI(ContentItem item, boolean includeInferred) {
		Query q = entityManager.createNamedQuery("tagCloudService.aggregateTagsByContentItem");
		q.setParameter("ci", item);
		q.setHint("org.hibernate.cacheable", true);
		
		List<Object[]> qresult = q.getResultList();
		//List<TagCloudEntry> result = new LinkedList<TagCloudEntry>();
		HashMap<String, TagCloudEntry> result = new HashMap<String,TagCloudEntry>();

		for(Object[] r : qresult) {
			String title = (String)r[0];
			Long count = (Long)r[1];
			Date lastModified = (Date)r[2];
			TagCloudEntry b = new TagCloudEntry(count.intValue(), title);
			b.setLastTaggingDate(lastModified);
			
			result.put(title, b);
		}

		if (includeInferred) {
			KiWiResource resource = item.getResource();
			if (resource instanceof KiWiUriResource) {
				q = entityManager.createNamedQuery("tagCloudService.aggregateInferredTagsByContentItem");
				q.setParameter("uri", ((KiWiUriResource)resource).getUri());
				//q.setHint("org.hibernate.cacheable", true);		
				qresult = q.getResultList();
				
				for (Object[] r : qresult) {
					String title = (String)r[0];
					Long count = (Long)r[1];
					Date lastModified = new Date(211, 6, 6);
					TagCloudEntry b;
					b = result.get(title);
					if (b != null) {
						b.setCount(b.getCount()+ count.intValue());
					} else 
						b = new TagCloudEntry(count.intValue(), title);
					
					b.setLastTaggingDate(lastModified);											
				}
			} 
		}
		
		return new LinkedList(result.values());
	}
	
	/**
	 * TODO: could also provide as argument the maximum number of tags in the
	 * result and then filter out those with the lowest count
	 */
	public List<ExtendedTagCloudEntry> aggregateTags(Collection<Tag> tags) {

		log.info("aggregating #0 tags", tags.size());

		int countMax = 1;
		Date firstTaggingDate = null;
		Date lastTaggingDate = null;
		List<ExtendedTagCloudEntry> result = new LinkedList<ExtendedTagCloudEntry>();

		// group tags by tagging resource
		HashMap<ContentItem, List<Tag>> groupings = new HashMap<ContentItem, List<Tag>>();
		for (Tag t : tags) {
			if (groupings.get(t.getTaggingResource()) == null) {
				groupings.put(t.getTaggingResource(), new LinkedList<Tag>());
			}
			groupings.get(t.getTaggingResource()).add(t);
			if (groupings.get(t.getTaggingResource()).size() > countMax) {
				countMax = groupings.get(t.getTaggingResource()).size();
			}
			if (firstTaggingDate == null
					|| t.getCreationTime().compareTo(firstTaggingDate) < 0) {
				firstTaggingDate = t.getCreationTime();
			}
			if (lastTaggingDate == null
					|| t.getCreationTime().compareTo(lastTaggingDate) > 0) {
				lastTaggingDate = t.getCreationTime();
			}
		}

		// store tags in aggregator beans for the result
		for (List<Tag> l : groupings.values()) {
			ExtendedTagCloudEntry b = new ExtendedTagCloudEntry(l.size(), l.get(0)
					.getTaggingResource());

			// font size
			if (countMax > 1) {
				b.setSize(100 + (l.size() - 1) * 100 / (countMax - 1));
			} else {
				b.setSize(100);
			}

			// latest modification
			Date lastModified = null;
			boolean own_tag   = false;
			for (Tag t : l) {
				if (lastModified == null
						|| t.getCreationTime().compareTo(lastModified) > 0) {
					lastModified = t.getCreationTime();
				}
				if(t.getTaggedBy().equals(currentUser)) {
					own_tag = true;
				}
			}
			b.setLastTaggingDate(lastModified);
			b.setOwnTag(own_tag);
			
			// color coding; we use 100 for the first tagging time and 200 for
			// the last and calculate the
			// value for this tag relative to this interval
			int colorCode = (int) (100 + (lastModified.getTime() - firstTaggingDate
					.getTime())
					* 100.0
					/ (lastTaggingDate.getTime() - firstTaggingDate.getTime()));
			b.setColorCode(colorCode);

			result.add(b);
		}
		Collections.sort(result, TagCloudEntry.COMP_TITLE);

		return result;
	}

	@Override
	public List<ExtendedTagCloudEntry> portalTagCloud() {
		log.info("aggregating tags for all users");

		List<ExtendedTagCloudEntry> result = new LinkedList<ExtendedTagCloudEntry>();
		int countMax = 1;
		Date firstTaggingDate = null;
		Date lastTaggingDate = null;
		
		
		Query q = entityManager.createNamedQuery("tagCloudService.getFullTagCloud");
		q.setHint("org.hibernate.cacheable", true);
		
		List<Object[]> qres = q.getResultList();
		for(Object[] o : qres) {
			ContentItem tag = entityManager.find(ContentItem.class,(Long)o[0]);
			int count = ((Long)o[1]).intValue();
			Date firstModified = (Date)o[2];
			Date lastModified = (Date)o[3];
			
			if(count > countMax) {
				countMax = count;
			}
			
			if(firstTaggingDate == null || firstTaggingDate.getTime() > firstModified.getTime()) {
				firstTaggingDate = firstModified;
			}
			
			if(lastTaggingDate == null || lastTaggingDate.getTime() < lastModified.getTime()) {
				lastTaggingDate = lastModified;
			}
			
			ExtendedTagCloudEntry bean = new ExtendedTagCloudEntry(count,tag);
			bean.setLastTaggingDate(lastTaggingDate);

			// font size
			if (countMax > 1) {
				bean.setSize(100 + (count - 1) * 100 / (countMax - 1));
			} else {
				bean.setSize(100);
			}
			
			// color coding; we use 100 for the first tagging time and 200 for
			// the last and calculate the
			// value for this tag relative to this interval
			int colorCode = (int) (100 + (lastModified.getTime() - firstTaggingDate
					.getTime())
					* 100.0
					/ (lastTaggingDate.getTime() - firstTaggingDate.getTime()));
			bean.setColorCode(colorCode);

			result.add(bean);
		}
		return result;
	}
	
	/**
	 * @see TagCloudService#listTaggingUsers(String)
	 * @author Szaby Grünwald
	 */
	@Override
	public List<Map<String,Object>> listTaggingUsers(String tagLabel) {
		List<Map<String, Object>> res = new LinkedList<Map<String,Object>>();
		
		// call query
		Query q = entityManager.createNamedQuery("tagCloudService.getUsersUsingTag");
		q.setParameter("tag", tagLabel);
		q.setHint("org.hibernate.cacheable", true);
		
		UserService userService = (UserService)Component.getInstance("userService");
		
		List<Object[]> qres = q.getResultList();
		
		Map<String, Object> firstTagger = new HashMap<String, Object>();
		Date firstTaggingDate = new Date();
		for(Object[] r : qres) {
			Map<String, Object> taggingUser = new HashMap<String, Object>();
			
			taggingUser.put("user",         userService.getUserByLogin((String)r[0]));
			taggingUser.put("count",        r[1]);
			taggingUser.put("firstTagging", dateFormat((Date)r[2]));
			taggingUser.put("lastTagging",  dateFormat((Date)r[3]));
			taggingUser.put("first",        false);
			if( ((Date)r[2]).before(firstTaggingDate) ){
				firstTaggingDate = (Date)r[2];
				firstTagger = taggingUser;
			}
			res.add(taggingUser);
		}
		firstTagger.put("first", true);
		return res;
	}
	private String dateFormat (Date d){
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM MM yyyy HH:mm:ss Z");
		return dateFormatter.format(d);
	}
	
}
