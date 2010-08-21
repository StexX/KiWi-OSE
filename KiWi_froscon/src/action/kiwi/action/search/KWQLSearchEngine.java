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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.query.kwql.KwqlService;
import kiwi.api.render.RenderingService;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.PersonalizedSearchService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.apache.solr.client.solrj.SolrQuery.ORDER;
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

/**
 * @author Klara Weiand
 * 
 */
@Name("kwqlSearchEngine")
@Scope(ScopeType.SESSION)
public class KWQLSearchEngine implements Serializable, SearchEngine {

	private static final long serialVersionUID = -2769583345325298488L;

	@Logger
	private static Log log;

	@In
	FacesMessages facesMessages;

	/**
	 * input current user; might affect the loading of the content item.
	 */
	@In(create = true)
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

	@In("kiwi.query.kwqlService")
	private KwqlService kwqlService;

	@In
	private RenderingService renderingPipeline;

	private boolean personalSearch;

	/**
	 * sortBy can be
	 */
	private String[] orderBys = { "modified", "score", "ceq" };
	private String orderBy = "modified";

	public ORDER order = ORDER.desc;
	public List<String> orders;

	/**
	 * the query string displayed in the search input field. Contains the full
	 * query
	 */
	private String kwqlQuery;

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

	private KiWiSearchResults currentResults;

	/**
	 * Initialise the KWQLAction and start a conversation. Triggered as page
	 * action when kwql.xhtml is displayed.
	 */
	@Begin(join = true)
	public void begin() {
	}

	/**
	 * End the KWQLAction and terminate the conversation.
	 */
	@End
	public void end() {

	}

	/**
	 * Execute the search
	 * 
	 */
	public void runSearch() {
		log.info("runSearch.s query: #0", kwqlQuery);

		if (kwqlQuery != null && !kwqlQuery.equals("")) {
			searchResults = kwqlService.search(kwqlQuery);
			this.page = 1;
		} else {
			searchResults = new KiWiSearchResults();
			return;
		}

		if (getPersonalSearch() && (currentUser.getId() != 1L)) {
			searchResults = personalizedSearchService
					.runPersonilazedSearch(searchResults);
		}

		pages = new LinkedList<Integer>();
		if (searchResults != null) {
			for (int i = 0; i < 1.0 * searchResults.getResultCount() / pageSize; i++) {
				pages.add(i + 1);
			}
		}

		getResults(true);

		if (currentUser != null && Conversation.instance().getViewId() != null
				&& Conversation.instance().getViewId().contains("search")
				&& kwqlQuery != "")
			Events.instance().raiseEvent(KiWiEvents.ACTIVITY_SEARCH,
					currentUser, kwqlQuery);
	}

	/**
	 * 
	 */
	public void getResults(Boolean reSort) {
		if (reSort) {
			sortResults();
		}
		
		int start = ((page - 1) * pageSize);
		int stop = min((page * pageSize), (int) searchResults
				.getResultCount());

		currentResults = new KiWiSearchResults();
		currentResults.setResults(searchResults.getResults().subList(start, stop));
	}

	/**
	 * @return the KWQLQuery
	 */
	public String getKWQLQuery() {
		return kwqlQuery;
	}

	/**
	 * @param kwqlQuery
	 *            the KWQLQuery to set
	 */
	public void setKWQLQuery(String kwqlQuery) {
		this.kwqlQuery = kwqlQuery;
	}

	/**
	 * @return the searchResults
	 */
	public KiWiSearchResults getSearchResults() {
		return searchResults;
	}

	/**
	 * @return the searchResults
	 */
	public KiWiSearchResults getCurrentResults() {
		return currentResults;
	}

	public String renderPreview(ContentItem ci) {
		if (ci.getTextContent() == null)
			return "";
		return renderingPipeline.renderPreview(ci);
	}

	public String getPreview(ContentItem item) {
		String newString = "";
		try {
			String text = item.getTextContent().getPlainString();
			String[] tokens = text.split(" ");
			if (text.length() > 0) {
				int i = 0;
				while (i<tokens.length && i <= 20) {
					newString = newString + " " + tokens[i];
					i++;
				}
			}
		} catch (Exception e) {
			return newString;
		}
		return newString;
	}

	public int min(int a, int b) {
		return Math.min(a, b);
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

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(int page) {
		this.page = page;
		getResults(false);
	}

	public void nextPage() {
		this.page++;
		getResults(false);
	}

	public void prevPage() {
		if (this.page > 1) {
			this.page--;
			getResults(false);
		}
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
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
	 * @param pages
	 *            the pages to set
	 */
	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

	/**
	 * Workaround method to enforce a queued AJAX request and avoid concurrent
	 * conversation. Called by search button.
	 * 
	 * @return
	 */
	public String getSearchView() {
		return "/kwql.xhtml";
	}

	@Override
	public void setSearchQuery(String searchQuery) {
		setKWQLQuery(searchQuery);
	}

	// **** SORTING ***//
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
		if (orders == null) {
			orders = new LinkedList<String>();
			for (ORDER s : ORDER.values()) {
				orders.add(s.name());
			}
		}
		return orders;
	}

	private void sortResults() {
		if (searchResults != null && searchResults.getResultCount() > 1) {
			if (orderBy.equals("score")) {
				Collections.sort(searchResults.getResults(),
						new scoreComparator());
			} else if (orderBy.equals("ceq")) {
				Collections.sort(searchResults.getResults(),
						new ceqComparator());
			} else {
				Collections.sort(searchResults.getResults(),
						new modifiedComparator());
			}
			if (order == ORDER.desc) {
				Collections.reverse(searchResults.getResults());
			}
		}
	}

	class modifiedComparator implements Comparator<Object> {

		public int compare(Object sr1, Object sr2) {
			Date sr1modified = ((SearchResult) sr1).getItem().getModified();
			Date sr2modified = ((SearchResult) sr2).getItem().getModified();

			return ((Date) sr1modified).compareTo(((Date) sr2modified));
		}

	}

	class ceqComparator implements Comparator<Object> {

		public int compare(Object sr1, Object sr2) {
			double sr1ceq = ((SearchResult) sr1).getCeq();
			double sr2ceq = ((SearchResult) sr2).getCeq();

			return ((Double) sr1ceq).compareTo(((Double) sr2ceq));
		}

	}

	class scoreComparator implements Comparator<Object> {

		public int compare(Object sr1, Object sr2) {
			double sr1score = ((SearchResult) sr1).getScore();
			double sr2score = ((SearchResult) sr2).getScore();

			return ((Double) sr1score).compareTo(((Double) sr2score));
		}

	}

}