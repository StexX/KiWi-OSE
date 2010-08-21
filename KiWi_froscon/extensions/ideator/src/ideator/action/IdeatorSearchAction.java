package ideator.action;

import ideator.datamodel.IdeaFacade;
import ideator.exception.IdeatorSearchException;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kiwi.api.group.GroupService;
import kiwi.api.ontology.OntologyService;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.KiWiSearchResults.KiWiFacet;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.search.SolrService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.KiWiProperty;
import kiwi.model.user.Group;
import kiwi.model.user.User;
import kiwi.util.MD5;

import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

@Name("ideator.searchAction")
@Scope(ScopeType.SESSION)
//@Transactional
public class IdeatorSearchAction {
	
	private static final int PAGE_SIZE = 10;
	
	@Logger
	Log log;
	
	private String query;
	private List<String> tags;
	private int status;
	private String sort;
	private String order;
	private int page;
	
	private KiWiUriResource statusResource;
	
	private KiWiSearchResults searchResults;
	
	private List<KiWiFacet<String>> statusFactes;
	
	@In(create=true)
	private SolrService solrService;
	
	@In
	public Map<String, String> messages;
	
    @In
    private GroupService groupService;
	
	@In
	private OntologyService ontologyService;
	
	@In(create=true)
	private User currentUser;
		

	public String resetAndSearch() {
		//save search query from input field and reset search
		String q = query;
		reset();
		query = q;
		
		//search
		return search();
	}
	
	public String search() {
		clear();
		return run();
	}
	
	public boolean hasResults() {
		return searchResults.getResultCount() > 0;
	
	}
	
	@Create
	public String reset() {
		//get resource for ideatorStatus
		List<KiWiProperty> res = ontologyService.listDatatypePropertiesByName("status");
		if( res.isEmpty() ) statusResource = null;
		else statusResource = ((KiWiUriResource)res.get(0).getResource());
		clear();
		query = "";
		tags = new LinkedList<String>();
		searchResults = new KiWiSearchResults();
		statusFactes = new LinkedList<KiWiFacet<String>>();
		status = -1;
		sort = "score";
		order = "desc";
		return "search";
	}
	
	private String run() {
		try {
			searchResults = solrService.search(getCriteria());
			
			//set status facets
			if(searchResults != null) statusFactes = buildStatusFacet();
			else Collections.emptyList();
			
		} catch(IdeatorSearchException e) {
			searchResults = new KiWiSearchResults();
		}
		return "search";
	}
	
	private void clear() {
		//reset the search (pages etc.)
		page = 0;
	}
	
	private KiWiSearchCriteria getCriteria() throws IdeatorSearchException {
		KiWiSearchCriteria criteria = new KiWiSearchCriteria();

		String fullquery = parseQuery();
		
		//set full search query string
		if(fullquery != null && !query.equals("")) {
			if("*".equals(fullquery))
				criteria = solrService.parseSearchString("type:ideator:Idea");
			else
				criteria = solrService.parseSearchString(fullquery + " type:ideator:Idea");
		} else {
			throw new IdeatorSearchException("no query defined");
		}
		
		Set<String> rdfFacets = new HashSet<String>();
		
		if( statusResource != null ) {
			rdfFacets.add(statusResource.getUri());
			criteria.setRdfLiteralFacets(rdfFacets);
		}
		
		//set pages,limit etc.
		criteria.setOffset(page*PAGE_SIZE);
		criteria.setLimit(PAGE_SIZE);
		criteria.setSortField(sort);
		if( order.equals("asc") ) criteria.setSortOrder(ORDER.asc);
		else criteria.setSortOrder(ORDER.desc);
		
		//check if user is ideamanager
		criteria.setSolrSearchString( getUserSpecificSearchString() );
		
		return criteria;
	}
	
	private String parseQuery() {
		if( query == null || query.equals("")  || query.equals("*") ) {
			if( tags.isEmpty() &&  status < 0) {
				return "*";
			} else {
				return getTagString() + getStatusString();
			}
		} else {
			return query.toLowerCase() + getTagString() + getStatusString();
		}
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}
	
//+++++++++++++++++++++ Search Results ++++++++++++++++++++++
	/**
	 * @return a list of searchResults depending on query String
	 */
	public List<SearchResult> getSearchResults() {
		return searchResults.getResults();
	}
	
	//return tag facets if there are some
	public List<KiWiFacet<ContentItem>> getTagFacetList() {
		if(searchResults != null) {
			return new ArrayList<KiWiFacet<ContentItem>>(searchResults.getTagFacets());	
		}
		return Collections.emptyList();
	}
	
	private List<KiWiFacet<String>> buildStatusFacet() {
		if( statusResource == null ) return Collections.emptyList();
		
		List<KiWiFacet<String>> result = new LinkedList<KiWiFacet<String>>();
		if(searchResults.getPropertyFacets() != null && searchResults.getPropertyFacets().get(statusResource) != null) {
			result.addAll(searchResults.getPropertyFacets().get(statusResource));
		}
		Collections.sort(result, new Comparator<KiWiFacet<String>>() {

			@Override
			public int compare(KiWiFacet<String> o1, KiWiFacet<String> o2) {
				return Collator.getInstance().compare(o1.getContent(), o2.getContent());
			}
			
		});
		
		return result;
	}
	
	public List<KiWiFacet<String>> getStatusFacet() {
		if( searchResults.getResultCount() == 0 ) return Collections.emptyList();
		else return statusFactes;
	}
	
	public String getStatusFacetString(int i) {
		return messages.get("ideator.search.status."+i);
	}
	
//++++++++++++++++++++++ Tag things ++++++++++++++++++++++++
	private String getTagString() {
		String tagString = "";
		for( int i = 0; i < tags.size(); i++ ) {
			String tag = tags.get(i);
			// if tag is not only characters, enclose in ""
			if(!tag.matches("^\\w+$")) {
				tagString += " tag:\""+tag+"\"";
			} else {
				tagString += " tag:"+tag;
			}
		}
		return tagString;
	}
	
	
	public String addTag(String tag) {
		if( !tags.contains(tag) ) {
			tags.add(tag);
			clear();
		}
		return run();
	}
	
	public String removeTag(String tag) {
		if( tags.contains(tag) ) {
			tags.remove(tag);
			clear();
		}
		return run();
	}
	
	public List<String> getSearchTags() {
		return tags;
	}
	
	//+++++++++++++ status things ++++++++++++
	public String getStatusString() {
		if( status == -1 ) return "";
		else return " ideator:status:"+status;
	}

	
	public int getStatus() {
		return status;
	}
	
	public String setStatus(int status) {
		this.status = status;
		clear();
		return run();
	}
	
	//+++++++++++++++++ sort and ordering +++++++++++++
	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSort() {
		return sort;
	}
	
	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrder() {
		return order;
	}

	//++++++++++++++++++ paging ++++++++++++++
	public int getCurrentPage() {
		return page+1;
	}
	
	public boolean[] getPageArray() {
		boolean[] a = new boolean[(int)Math.ceil((double)searchResults.getResultCount()/PAGE_SIZE)];
		if( a.length > 0 ) a[page] = true;
		return a;
	}
	
	public int getFirstContentNumber() {
		return (page*PAGE_SIZE)+1;
	}
	
	public long getLastContentNumber() {
		return Math.min((long)((page*PAGE_SIZE)+PAGE_SIZE), searchResults.getResultCount());
	}
	
	public long getResultCount() {
		return searchResults.getResultCount();
	}
	
	public String setPage(int i) {
		this.page = i;
		return run();
	}
	
	public String nextPage() {
		this.page++;
		return run();
	}
	
	public String prevPage() {
		this.page--;
		return run();
	}
	
	public boolean hasNextPage() {
		return getLastContentNumber() < searchResults.getResultCount();
	}
	
	public boolean hasPrevPage() {
		return page > 0;
	}
	
	//******************** idea manager checker **************
    private boolean checkIdeaManager(User user) {
    	List<Group> sr = groupService.getGroupsByUser(user);
    	for (Group g : sr) {
    	    log.info("AdminAction" + g.getName());
    	    if (g.getName().equals("Ideamanager")) {
    	    	return true;
    	    }
    	}
    	return false;
    }
    
    private String getUserSpecificSearchString() {
    	if( !checkIdeaManager(currentUser) ) {
    		
    		final TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
    		final KiWiUriResource prop1 = tripleStore.createUriResourceByNamespaceTitle("ideator", "status");
    		final KiWiUriResource prop2 = tripleStore.createUriResourceByNamespaceTitle("ideator", "evaluator");
    		final KiWiUriResource prop3 = tripleStore.createUriResourceByNamespaceTitle("ideator", "coAuthors");

    		
    		// (author_login:"rolf" OR i_ status:(1) OR l_ evaluator:"rolf" OR l_ coaruthos:"rsint")
    		StringBuilder qString = new StringBuilder();
    		//show all ideas where the author is the currentuser
    		qString.append("(author_login:\"");
			qString.append(currentUser.getLogin());
			//or the idea is declared as free
			qString.append("\" OR ");
			qString.append("i_");
			qString.append(MD5.md5sum(prop1.getUri()));
			qString.append(":(");
			qString.append(IdeaFacade.FREE);
			//or the currentuser is evaluator
			qString.append(") OR ");
			qString.append("l_");
			qString.append(MD5.md5sum(prop2.getUri()));
			qString.append(":\"");
			qString.append(currentUser.getLogin());
			qString.append("\" ");
			//or the currentuser is coautor
			qString.append("OR ");
			qString.append("l_");
			qString.append(MD5.md5sum(prop3.getUri()));
			qString.append(":\"");
			qString.append(currentUser.getLogin());
			
			qString.append("\")");
			
			log.info("qString: #0", qString.toString());
			
			return qString.toString();
			
    	} return null;
    }	
}
