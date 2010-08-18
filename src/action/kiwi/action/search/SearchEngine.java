package kiwi.action.search;

import kiwi.api.search.KiWiSearchResults;

public interface SearchEngine {

	/**
	 * Execute the search
	 */
	public void runSearch();

	/**
	 * @param searchQuery the searchQuery to set
	 */
	public void setSearchQuery(String searchQuery);

	/**
	 * @param page the page to set
	 */
	public void setPage(int page);

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize);
	
	public void setOrder(String order);
	public void setOrderBy(String orderBy);

	/**
	 * @return the searchResults
	 */
	public KiWiSearchResults getSearchResults();

}