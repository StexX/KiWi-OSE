/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.action.search;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.ontology.OntologyService;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.KiWiSearchResults.KiWiFacet;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.search.SolrService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.KiWiProperty;

import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

import artaround.action.artwork.ArtWorkBean;
import artaround.datamodel.artwork.ArtWorkFacade;
import artaround.exception.ArtAroundSearchException;

/**
 * @author devadmin
 *
 */
@Scope(ScopeType.SESSION)
@Name("artSearchAction")
//@Transactional
public class ArtSearchAction implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Logger
	private Log log;
	
	private String fullquery;
	
	private int page;
	
	private String sort;
	
	private String order;
	
	private static final int PAGE_SIZE = 10;

	private KiWiUriResource techniqueResource;
	
	private List<KiWiFacet<KiWiResource>> techniqueFacets;
	
	private List<KiWiFacet<KiWiResource>> relevantTypes;
	
	@In
	private TripleStore tripleStore;
	
	@In
    private KiWiEntityManager kiwiEntityManager;
	
	private KiWiSearchResults searchResults;
	
	@In(required = false)
	@Out(required = false)
	private ArtWorkBean artWorkBean;

	@In(create=true)
	private SolrService solrService;
	
	@In
	private OntologyService ontologyService;
	
	private Map<String, Set<String>> m; 
	
	private Set<KiWiUriResource> selectedTechniques;
		
	@Create
	public String reset() {
		clear();
		fullquery = "";
		sort = "score";
		order = "desc";
		searchResults = new KiWiSearchResults();
		
		List<KiWiProperty> res = ontologyService.listDatatypePropertiesByName("techniques");
		log.info(res == null?"facet is null":"facet is not null");
		
		if( res.isEmpty() ) techniqueResource = null;
		else techniqueResource = ((KiWiUriResource)res.get(0).getResource());
		
		log.info(techniqueResource == null?"techniqueResource is null":"techniqueResource is not null");
		log.info(res == null || res.isEmpty()?"res is null":"res is not null");
		
		techniqueFacets = new LinkedList<KiWiFacet<KiWiResource>>();
		relevantTypes = new LinkedList<KiWiFacet<KiWiResource>>();
		
		m = new HashMap<String, Set<String>>();
		selectedTechniques = new HashSet<KiWiUriResource>();
		
		return "search";
	}
	
	
	public String search() {
		log.info(fullquery);
		clear();
		return run(null,null);
	}
	
	
	
	//return type facets if there are some
	private List<KiWiFacet<KiWiResource>> buildRelevantTypes() {
		List<KiWiFacet<KiWiResource>> results = new LinkedList<KiWiFacet<KiWiResource>>();
		
		if(searchResults != null) {
			
			for (Iterator iterator = searchResults.getTypeFacets().iterator(); iterator.hasNext();) {
				KiWiFacet<KiWiResource> kiWiFacet = (KiWiFacet<KiWiResource>) iterator.next();
				log.info(kiWiFacet.getContent().getLabel());
				if(kiWiFacet.getContent().getLabel().contains("ArtWork") || kiWiFacet.getContent().getLabel().contains("ArtAroundUser")){
					results.add(kiWiFacet);
				}
			}
			
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
	
		
	public List<KiWiFacet<KiWiResource>> getRelevantTypes() {
		return relevantTypes;
	}


	private String run(Map<String, Set<String>> m, KiWiResource type) {
		try {			
			KiWiSearchCriteria ksc = getCriteria();
			
			if(m != null)
				ksc.setRdfObjectProperties(m);
			if(type != null){
				String kiwiIdentifier = type.getKiwiIdentifier();
							
				String crit = ksc.getSolrSearchString();
				log.info(crit);
				crit = crit+" and type:\""+kiwiIdentifier+"\"";
				log.info(crit);
				ksc.setSolrSearchString(crit);
				
			}
			
			
			searchResults = solrService.search(ksc);
			
			log.info(searchResults.getResults().size());
			
			//set status facets
			if(searchResults != null) {
				techniqueFacets = buildTechniqueFacet();
				relevantTypes = buildRelevantTypes();
			}
			else Collections.emptyList();
			
		} catch(ArtAroundSearchException e) {
			searchResults = new KiWiSearchResults();
		}
		return "search";
	}
	


	private KiWiSearchCriteria getCriteria() throws ArtAroundSearchException {
		KiWiSearchCriteria criteria = new KiWiSearchCriteria();
		//set full search query string
		if(fullquery != null && !fullquery.equals("")) {
			if("*".equals(fullquery)){
				//criteria = solrService.parseSearchString("");
				criteria.setSolrSearchString("type:\"uri::http://www.artaround.at/ArtWork\" OR type:\"uri::http://www.artaround.at/ArtAroundUser\"");
				//criteria = solrService.parseSearchString("type:artaround:ArtWork");
				//criteria.getTypes().add(tripleStore.createUriResource(Constants.ART_AROUND_CORE + "ArtWork").getKiwiIdentifier());
				//criteria.getTypes().add(tripleStore.createUriResource(Constants.NS_FOAF+"Person").getKiwiIdentifier());
			}else{
				criteria = solrService.parseSearchString(fullquery);
				criteria.setSolrSearchString("(type:\"uri::http://www.artaround.at/ArtWork\" OR type:\"uri::http://www.artaround.at/ArtAroundUser\")");
				//criteria = solrService.parseSearchString(fullquery + " type:artaround:ArtWork");
				}
		} else {
			throw new ArtAroundSearchException("no query defined");
		}
		
		
		log.info("order #0",order);
		log.info("sort #0",sort);
		
		//set pages,limit etc.
		criteria.setOffset(page*PAGE_SIZE);
		criteria.setLimit(PAGE_SIZE);
		criteria.setSortField(sort);
		
		Set<String> s = new HashSet<String>();
		s.add("http://www.artaround.at/techniques");
		criteria.setRdfObjectFacets(s);
			
		
		if( order.equals("asc") ) criteria.setSortOrder(ORDER.asc);
		else criteria.setSortOrder(ORDER.desc);
		
		
		return criteria;
	}
	
	private void clear() {
		//reset the search (pages etc.)
		m = new HashMap<String, Set<String>>();
		selectedTechniques = new HashSet<KiWiUriResource>();
		page = 0;
	}
	
	
    public String getSearchView() {
    	search();
    	return  "/artaround/artaroundSearch.xhtml";
	}

	public String getFullquery() {
		return fullquery;
	}

	public void setFullquery(String fullquery) {
		this.fullquery = fullquery;
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
	
	// facets for facetted search
	
	public List<KiWiFacet<KiWiResource>> getStatusFacet() {
		if( searchResults.getResultCount() == 0 ) return Collections.emptyList();
		else return techniqueFacets;
	}
	
	private List<KiWiFacet<KiWiResource>> buildTechniqueFacet() {
						
		if( techniqueResource == null ) return Collections.emptyList();
		
		List<KiWiFacet<KiWiResource>> result = new LinkedList<KiWiFacet<KiWiResource>>();
		log.info(searchResults.getObjectPropertyFacets().size());
		
		
		Map<KiWiUriResource, Set<KiWiFacet<KiWiResource>>> m = searchResults.getObjectPropertyFacets();
		Collection<Set<KiWiFacet<KiWiResource>>> hh = m.values();
		
		for(Set<KiWiFacet<KiWiResource>> kur : hh){
			for(KiWiFacet<KiWiResource> f: kur){
				result.add(f);
				log.info(f.getContent().getContentItem().getTitle());
			}
	
		}
		
			
		log.info(searchResults.getObjectPropertyFacets().get(techniqueResource) == null?"opf are null":"opf are not null");
		
		
		return result;
	}
	
	
	
	public List<KiWiFacet<KiWiResource>> getTechniqueFacets() {
		if( searchResults.getResultCount() == 0 ) return Collections.emptyList();
		else return techniqueFacets;
	}
	
	// ------ Paging
	
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
		return run(null,null);
	}
	
	public String nextPage() {
		this.page++;
		return run(null,null);
	}
	
	public String prevPage() {
		this.page--;
		return run(null,null);
	}
	
	public boolean hasNextPage() {
		return getLastContentNumber() < searchResults.getResultCount();
	}
	
	public boolean hasPrevPage() {
		return page > 0;
	}
	
	//+++++++++++++++++++++ Search Results ++++++++++++++++++++++
	/**
	 * @return a list of searchResults depending on query String
	 */
	public List<SearchResult> getSearchResults() {
		return searchResults.getResults();
	}
	
	// technique
	public KiWiUriResource getTechniqueResource() {
		return techniqueResource;
	}

	//convert set to list
	public List<KiWiUriResource> getSelectedTechniques() {
		LinkedList<KiWiUriResource> l = new LinkedList<KiWiUriResource>();
		for(KiWiUriResource s: selectedTechniques){
			l.add(s);
		}		
		return l;
	}
	
	public String setType(KiWiResource type){
		return run(null,type);
	}

	public String setTechniqueResource(KiWiUriResource techniqueResource) {
						
		selectedTechniques.add(techniqueResource);
		
		log.info(techniqueResource.getUri());
		
		if(m == null){ 
			 m = new HashMap<String, Set<String>>();
		}
		
		m.put("http://www.artaround.at/techniques", kiwiResource2Uri(selectedTechniques));

		return run(m,null);
	}
	
	public String removeTechnique(KiWiUriResource techniqueResource){
		selectedTechniques.remove(techniqueResource);
		if(selectedTechniques.size() >0){
			m.put("http://www.artaround.at/techniques", kiwiResource2Uri(selectedTechniques));
		}
		else
		{
			m = null;
		}
		return run(m,null);
	}
	
	
	private Set<String> kiwiResource2Uri(Set<KiWiUriResource> ks){
		Set<String> ls = new HashSet<String>();
		for(KiWiUriResource s: ks){
			ls.add(s.getUri());
		}
		return ls;
	}
	
	public String detailPage(ContentItem ci){
		ArtWorkFacade artWork = kiwiEntityManager.createFacade(ci, ArtWorkFacade.class);	
		artWorkBean.init(artWork);
		return "/artaround/pages/frontend/artWorkDetails.com";
	}
}
