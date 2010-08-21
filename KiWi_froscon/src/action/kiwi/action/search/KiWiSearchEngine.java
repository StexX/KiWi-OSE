/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 *
 */
package kiwi.action.search;

import java.io.Serializable;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import kiwi.api.event.KiWiEvents;
import kiwi.api.render.RenderingService;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.PersonalizedSearchService;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.KiWiFacet;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import kiwi.service.search.SolrServiceImpl;

import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

@Name("kiwiSearchEngine")
@Scope(ScopeType.CONVERSATION)
public class KiWiSearchEngine implements Serializable, SearchEngine {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3008556187463229039L;

	@Logger
	private static Log log;
	
    @In FacesMessages facesMessages;
	
	/**
	 * input current user; might affect the loading of the content item.
	 */
	@In(create=true)
	private User currentUser;

		
	/**
	 * The JPA entity manager used by this KiWi system
	 */
	@In
	private EntityManager entityManager;
	
	/**
	 * initiate the personalized search
	 */
	@In(create = true)
	PersonalizedSearchService personalizedSearchService;
	
	
	/**
	 * the search service used by this KiWi system
	 */
	@In(create=true)
	private SolrService solrService;
	
	@In
    private RenderingService renderingPipeline;

	@In
	private TaggingService taggingService;
	
	private boolean personalSearch;
	
	/**
	 * sortBy can be
	 */
	private String[] orderBys = {"modified","score","ceq"};
	private String orderBy;
	
	public ORDER order;
	public List<String> orders;

	/**
	 * the query string displayed in the search input field. Contains the full query, including
	 * tags, persons and types.
	 */
	private String searchQuery;
	
	
	/**
	 * the page of the search results currently displayed.
	 */
	private int page = 1;
	
	/**
	 * the number of search results displayed per page.
	 */
	private int pageSize = 10;
	
	/**
	 * a list of integers indicating the possible pages.
	 */
	private List<Integer> pages;
	
	/**
	 * the list of search results for the current page.
	 */
	
	private KiWiSearchResults searchResults;
	
	
	private List<KiWiUriResource> rdfFacets;
	
	private List<KiWiUriResource> objectPropertyFacets;
	
	private final static Pattern pat_searchSpecial = Pattern.compile("(tag|purpose|person|type):\\s*(\"([^\"\\\\]|\\\\.)*\"|\\w*)", Pattern.DOTALL);

	/**
	 * Initialise the SearchAction and start a conversation. 
	 * Triggered as page action when search.xhtml is displayed. 
	 */
	@Begin(join=true)
	public void begin() {
	}
	
	/**
	 * End the SearchAction and terminate the conversation.
	 */
	@End
	public void end() {
		
	}
	
	/* (non-Javadoc)
	 * @see kiwi.action.search.SearchEngine#runSearch()
	 */
	public void runSearch() {
		log.info("runSearch. query: #0", searchQuery);
		log.info("order by #0 #1", orderBy,order);
		KiWiSearchCriteria criteria = new KiWiSearchCriteria();
		
		if(solrService==null){
			// This is a strange thing, but called from a stateless webservice it's null.
			log.debug("runSearch: solrService was null");
			solrService = (SolrService)Component.getInstance("solrService");
		}
		
		if(searchQuery != null && !searchQuery.equals("")) {
			if("*".equals(searchQuery))
				criteria = solrService.parseSearchString("type:kiwi:ContentItem");
			else
				criteria = solrService.parseSearchString(searchQuery);
		} else {
			searchResults = new KiWiSearchResults();
			return;
		}

		//set ordering
		criteria.setSortOrder(order);
		criteria.setSortField(orderBy);
		
		criteria.setOffset((page-1)*pageSize);
		criteria.setLimit(pageSize);
		searchResults = solrService.search(criteria);
		
		//searchResults = kiwiEntityManager.search(criteria, ContentItem.class);
		
		if(getPersonalSearch() && (currentUser.getId()!=1L)){
			searchResults = personalizedSearchService.runPersonilazedSearch(searchResults);
		}
		
		pages = new LinkedList<Integer>();
		if(searchResults!=null){
			for(int i = 0; i < 1.0 * searchResults.getResultCount() / pageSize; i++) {
				pages.add(i+1);
			}
		}
		
		if(currentUser!=null && Conversation.instance().getViewId()!=null && Conversation.instance().getViewId().contains("search") &&
				searchQuery!="")
			Events.instance().raiseEvent(KiWiEvents.ACTIVITY_SEARCH, currentUser, searchQuery);
	}
	
	/**
	 * @return the searchQuery
	 */
	public String getSearchQuery() {
		return searchQuery;
	}

	/* (non-Javadoc)
	 * @see kiwi.action.search.SearchEngine#setSearchQuery(java.lang.String)
	 */
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	/**
	 * @return the searchResults
	 */
	public KiWiSearchResults getSearchResults() {
		return searchResults;
	}

	public String renderPreview(ContentItem ci){
		if(ci.getTextContent()==null)return "";
		return renderingPipeline.renderPreview(ci);
	}
	
	/**
	 * Add the tag with the given label to the search ...
	 * 
	 * @param tag
	 */
	public void addSearchTag(String tag) {

		// if tag is not only characters, enclose in ""
		if(!tag.matches("^\\w+$")) {
			searchQuery = searchQuery + " tag:\""+tag+"\"";
		} else {
			searchQuery = searchQuery + " tag:"+tag;
		}
		
		page = 1;
		runSearch();
	}
	
	
	/**
	 * Add the purpose with the given label to the search ...
	 * 
	 * @param tag
	 */
	public void addSearchPurpose(String purpose) {
		if(!purpose.matches("^\\w+$")) {
			searchQuery = searchQuery + " purpose:\""+purpose+"\"";
		} else {
			searchQuery = searchQuery + " purpose:"+purpose;
		}
		page = 1;
		runSearch();
	}


	/**
	 * Remove the tag with the given label from the search
	 * @param tag
	 */
	public void removeSearchTag(String tag){
		// search all occurrences of the tag and remove them
		Matcher m = pat_searchSpecial.matcher(searchQuery);
		StringBuffer buf = new StringBuffer();
		while(m.find()) {
			String kind = m.group(1);
			String query = m.group(2);
			if(query.startsWith("\")")) {
				query = m.group(3);
			}
			if("tag".equals(kind) && tag.equals(query)) {
				m.appendReplacement(buf, "");
			} else {
				m.appendReplacement(buf, m.group());
			}
			
		}
		m.appendTail(buf);
		searchQuery = buf.toString();
		
		runSearch();
	}
	
	/**
	 * Add the tag with the given label to the search ...
	 * 
	 * @param tag
	 */
	public void addSearchType(KiWiResource type) {
		String kiwiIdentifier = type.getKiwiIdentifier();
		TripleStore ts = (TripleStore)Component.getInstance("tripleStore");
		
		String prefix = type.getNamespacePrefix();
		log.info("addSearchType: namespacePrefix: #0", prefix);
		KiWiNamespace namespace = ts.getNamespace(prefix);
		String uri = namespace.getUri();
		
		String label = kiwiIdentifier.replaceAll("uri::" + uri, prefix + ":");
		searchQuery = searchQuery + " type:"+label+"";
		
		page = 1;
		runSearch();
	}

	public int min(int a, int b){
		return Math.min(a, b);
	}
	
	/**
	 * Add the person with the given label to the search ...
	 * 
	 * @param tag
	 */
	public void addSearchPerson(String person) {
		// if tag is not only characters, enclose in ""
		if(!person.matches("^\\w+$")) {
			searchQuery = searchQuery + " person:\""+person+"\"";
		} else {
			searchQuery = searchQuery + " person:"+person;
		}
		
		page = 1;
		runSearch();
	}
	
	/**
	 * Remove the person with the given label from the search
	 * @param tag
	 */
	public void removeSearchPerson(String person){
		// search all occurrences of the tag and remove them
		Matcher m = pat_searchSpecial.matcher(searchQuery);
		StringBuffer buf = new StringBuffer();
		while(m.find()) {
			String kind = m.group(1);
			String query = m.group(2);
			if(query.startsWith("\")")) {
				query = m.group(3);
			}
			if("person".equals(kind) && person.equals(query)) {
				m.appendReplacement(buf, "");
			} else {
				m.appendReplacement(buf, m.group());
			}
			
		}
		m.appendTail(buf);
		searchQuery = buf.toString();
		
		runSearch();
	}

	
	public List<Tag> getCITags(ContentItem ci){
		return taggingService.getTags(ci);
	}

	
	// further input parameters could be: time, location, type, SPARQL, ...
	
	/**
	 * List all the Content Items used as tags in the list of search results. For displaying in a tag cloud.
	 * 
	 * TODO: should be provided by KiWiEntityManager QueryResult
	 */
	public List<KiWiFacet<ContentItem>> getRelevantTags() {
		
		List<KiWiFacet<ContentItem>> results = new LinkedList<KiWiFacet<ContentItem>>();
		
		if(searchResults != null) {
				
			results.addAll(searchResults.getTagFacets());
			
//			Collections.sort(results, new Comparator<KiWiFacet<ContentItem>>() {
//
//				@Override
//				public int compare(KiWiFacet<ContentItem> o1, KiWiFacet<ContentItem> o2) {
//					//TODO something wrong here
//					return Collator.getInstance().compare(o1.getContent().getTitle(), o2.getContent().getTitle());
//				}
//				
//			});
			
		} else {
			log.warn("called getRelevantTags() without a search result");
		}
		
		log.debug("search results have #0 taggings overall.",results.size());
		
		return results;
	}
	
	/**
	 * List all the Content Items used as tags in the list of search results. For displaying in a tag cloud.
	 * 
	 * TODO: should be provided by KiWiEntityManager QueryResult
	 */
	public List<KiWiFacet<String>> getRelevantPurposes() {
		
		List<KiWiFacet<String>> results = new LinkedList<KiWiFacet<String>>();
		
		if(searchResults != null) {
			results.addAll(searchResults.getPurposeFacets());
			
			Collections.sort(results, new Comparator<KiWiFacet<String>>() {
				
				@Override
				public int compare(KiWiFacet<String> o1, KiWiFacet<String> o2) {
					return Collator.getInstance().compare(o1.getContent(), o2.getContent());
				}
				
			});
			
		} else {
			log.warn("called getRelevantPurposes() without a search result");
		}
		
		log.debug("getRelevantPurposes: search results have #0 elements overall.", results.size());
		
		return results;
	}

	/**
	 * List the types that occur in the search results for displaying in facetted search.
	 * 
	 * TODO: should be provided by KiWiEntityManager QueryResult
	 * @return
	 */
	public List<KiWiFacet<KiWiResource>> getRelevantTypes() {
		List<KiWiFacet<KiWiResource>> results = new LinkedList<KiWiFacet<KiWiResource>>();
		
		if(searchResults != null) {
			results.addAll(searchResults.getTypeFacets());
			
			Collections.sort(results, new Comparator<KiWiFacet<KiWiResource>>() {

				@Override
				public int compare(KiWiFacet<KiWiResource> o1, KiWiFacet<KiWiResource> o2) {				
					return Collator.getInstance().compare(o1.getContent().getLabel(), o2.getContent().getLabel());
				}
				
			});
			
		} else {
			log.warn("called getRelevantTypes without a search result");
		}
		log.debug("getRelevantTypes: return #0 elements", results.size());
		return results;
	}
	
	/**
	 * List the users that occur in the search results for displaying in facetted search.
	 * @return
	 */
	public List<KiWiFacet<User>> getRelevantPersons() {
		List<KiWiFacet<User>> results = new LinkedList<KiWiFacet<User>>();
		
		if(searchResults != null) {
			results.addAll(searchResults.getPersonFacets());
			
			Collections.sort(results, new Comparator<KiWiFacet<User>>() {

				@Override
				public int compare(KiWiFacet<User> o1, KiWiFacet<User> o2) {
					return Collator.getInstance().compare(o1.getContent().getFirstName()+" "+o1.getContent().getLastName(), o2.getContent().getFirstName()+" "+o2.getContent().getLastName());
				}
				
			});
			
		} else {
			log.warn("called getRelevantTypes without a search result");
		}
		log.debug("getRelevantPersons: return #0 elements", results.size());
		return results;
	}
	
	
	
	/**
	 * @return getPersonalSearch
	 */
	public boolean getPersonalSearch() {
		return personalSearch;
	}

	/**
	 * @param personalSearch
	 */
	public void setPersonalSearch(boolean personalSearch) {
		this.personalSearch = personalSearch;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/* (non-Javadoc)
	 * @see kiwi.action.search.SearchEngine#setPage(int)
	 */
	public void setPage(int page) {
		this.page = page;
		runSearch();
	}
	
	public void nextPage() {
		this.page++;
		runSearch();
	}

	public void prevPage() {
		if(this.page > 1) {
			this.page--;
			runSearch();
		}
	}
	
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/* (non-Javadoc)
	 * @see kiwi.action.search.SearchEngine#setPageSize(int)
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the pages
	 */
	public List<Integer> getPages() {
		return pages;
	}

	/**
	 * @param pages the pages to set
	 */
	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}
	
	public void changeSortBy(){
		facesMessages.add("Result sorting is not implemented yet.");
	}
	
	/**
	 * Workaround method to enforce a queued AJAX request and avoid concurrent conversation.
	 * Called by search button.
	 * 
	 * @return
	 */
	public String getSearchView() {
		return "/search.xhtml";
	}
	
	public List<String> autocomplete(Object param) {
		javax.persistence.Query q = 
			entityManager.createNamedQuery("searchAction.autocomplete");
        q.setParameter("n", param.toString().toLowerCase()+"%");
        q.setHint("org.hibernate.cacheable", true);
        return q.getResultList();
	}

	/**
	 * An aggregator for displaying users together with the number of times they occur in the 
	 * result and possibly further statistics.
	 * @author Sebastian Schaffert
	 *
	 */
	public static class UserAggregator {
		private User user;
		private int count;
		
		private UserAggregator(User user) {
			this.user = user;
		}
		
		public void incCount() {
			count++;
		}

		public User getUser() {
			return user;
		}

		public int getCount() {
			return count;
		}
	}
	
	/**
	 * An aggregator for displaying types together with the number of times they occur in the 
	 * result and possibly further statistics.
	 * @author Sebastian Schaffert
	 *
	 */
	public static class TypeAggregator {
		private KiWiResource type;
		private int count;
		
		private TypeAggregator(KiWiResource type) {
			this.type = type;
		}
		
		public void incCount() {
			count++;
		}

		public KiWiResource getType() {
			return type;
		}

		public int getCount() {
			return count;
		}
		
		
	}
	
	public List<KiWiUriResource> getObjectPropertyFacets() {
		if(objectPropertyFacets == null || objectPropertyFacets.size() == 0) {
			objectPropertyFacets = new LinkedList<KiWiUriResource>();
			
			if(searchResults==null){
				begin();
				runSearch();
			}
			
			for(KiWiUriResource prop : searchResults.getObjectPropertyFacets().keySet()) {
				objectPropertyFacets.add(prop);
			}
			log.debug("getObjectPropertyFacets: generating #0 facets", rdfFacets.size());
		}else{
			log.debug("getObjectPropertyFacets: returning #0 facets from cache", rdfFacets.size());
		}
		return objectPropertyFacets;
		
	}
	
	public List<KiWiFacet<KiWiResource>> getObjectPropertyFacetValues(KiWiUriResource property) {
		List<KiWiFacet<KiWiResource>> result = new LinkedList<KiWiFacet<KiWiResource>>();
		if(searchResults.getObjectPropertyFacets() != null && searchResults.getObjectPropertyFacets().get(property) != null) {
			result.addAll(searchResults.getObjectPropertyFacets().get(property));
		}
		Collections.sort(result, new Comparator<KiWiFacet<KiWiResource>>() {

			@Override
			public int compare(KiWiFacet<KiWiResource> o1, KiWiFacet<KiWiResource> o2) {
				return Collator.getInstance().compare(o1.getContent().getLabel(), o2.getContent().getLabel());
			}
			
		});
		
		return result;
	}
	
	public void addObjectPropertySearchProperty(KiWiUriResource prop, KiWiResource value) {
		String label = prop.getNamespacePrefix() + ":" + prop.getLocalName();
		
		searchQuery = searchQuery + " " + label+":\""+value.getKiwiIdentifier()+"\"";
		
		page = 1;
		runSearch();
		
	}


	
	
	public List<KiWiUriResource> getRdfFacets() {
		if(rdfFacets == null || rdfFacets.size()==0) {
			rdfFacets = new LinkedList<KiWiUriResource>();
			
			if(searchResults==null && searchQuery!=null & searchQuery!=""){
				begin();
				runSearch();
			}
			
			if (searchResults!=null){
				for(KiWiUriResource prop : searchResults.getPropertyFacets().keySet()) {
					rdfFacets.add(prop);
				}
			}
			log.debug("getRdfFacets: generating #0 facets", rdfFacets.size());
		}else {
			log.debug("getRdfFacets: returning #0 facets from cache", rdfFacets.size());
		}
		return rdfFacets;
	}
	
	
	public List<KiWiFacet<String>> getRdfFacetValues(KiWiUriResource property) {
		List<KiWiFacet<String>> result = new LinkedList<KiWiFacet<String>>();
		log.info((searchResults == null)?"result is null":"result is not null");
		if(searchResults.getPropertyFacets() != null && searchResults.getPropertyFacets().get(property) != null) {
			result.addAll(searchResults.getPropertyFacets().get(property));
		}
		Collections.sort(result, new Comparator<KiWiFacet<String>>() {

			@Override
			public int compare(KiWiFacet<String> o1, KiWiFacet<String> o2) {
				return Collator.getInstance().compare(o1.getContent(), o2.getContent());
			}
			
		});
		
		return result;
	}
	
	public void addRdfSearchProperty(KiWiUriResource prop, String value) {
		log.debug("addRdfSearchProperty: prop=#0, value=#1", prop.getUri(), value);
		String label = prop.getNamespacePrefix() + ":" + prop.getLocalName();
		
		searchQuery = searchQuery + " " + label+":\""+value+"\"";
		log.debug("addRdfSearchProperty: New search query: #0", searchQuery);
		facesMessages.add("new query: #0", searchQuery);
		page = 1;
		runSearch();
		
	}
	
	//**** SORTING ***//
	public String getOrderBy() {
		if (orderBy == null)
			orderBy = "modified";
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public String[] getOrderBys() {
		return this.orderBys;
	}
	
	public String getOrder() {
		if (order == null)
			order = ORDER.desc;
		return order.name();
	}

	public void setOrder(String order) {
		this.order = Enum.valueOf(ORDER.class, order);
	}
	
	public List<String> getOrders() {
		if( orders == null ) {
			orders = new LinkedList<String>();
			for( ORDER s : ORDER.values() ) {
				orders.add(s.name());
			}
		}
		return orders;
	}

}
