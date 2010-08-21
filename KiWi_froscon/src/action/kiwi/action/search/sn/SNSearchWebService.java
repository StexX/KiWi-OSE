package kiwi.action.search.sn;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import kiwi.api.ontology.OntologyService;
import kiwi.api.render.RenderingService;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.KiWiSearchResults.KiWiFacet;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.search.SolrService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;

import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Name("kiwi.webservice.snSearchWebService")
@Scope(ScopeType.STATELESS)
@Path("/sn/search")
public class SNSearchWebService {
	
	private static final int MAX_DESC_SIZE = 100;
	
	private static final DateFormat df = new SimpleDateFormat( "dd.MM.yyyy" );
	
	private static final List<String> SN_TYPES = Arrays.asList(
												Constants.NS_FCP_CORE + "NewsItem"
												,Constants.NS_FCP_CORE + "BlogItem"
												//,Constants.NS_KIWI_CORE + "Image"
											);
	
	private static final String CATEGORY_URI = Constants.NS_TAGIT+"hasCategory";
	
	private static final String FACET_PATTERN = "'([^']+)':'([^']+)'";
	private static final String TAG_PATTERN = "[^\\[\\],]+";
	
	@Logger
	private static Log log;
	
	@In(create=true)
	private SolrService solrService;
	
	@In
	private RenderingService renderingPipeline;
	
	@In
	private OntologyService ontologyService;
	
	@In
	private TripleStore tripleStore;
	
	private String query;
	private int pageSize;
	private int page;
	private ORDER order;
	private String orderBy;
	private List<Facet> facets;
	private List<String> tags;
	private String type;
	
	private KiWiSearchResults results;
	private KiWiSearchCriteria criteria;

	@GET
	@Path("/query.json")
	@Produces("application/json")
	public Response runSearch(
			@QueryParam("query") String query,
			@QueryParam("pageSize") @DefaultValue("10") int pageSize,
			@QueryParam("page") @DefaultValue("1") int page,
			@QueryParam("order") @DefaultValue("desc") String order,
			@QueryParam("orderBy") @DefaultValue("modified") String orderBy,
			@QueryParam("facets") @DefaultValue("[]") String facets,
			@QueryParam("tags") @DefaultValue("[]") String tags,
			@QueryParam("type") @DefaultValue("") String type) {
		
		try {

			if (query == "" || query == null)
				return Response.noContent().build();
			
			//set fields
			this.query = query;
			this.pageSize = pageSize;
			this.page = page;
			this.order = Enum.valueOf(ORDER.class, order);
			this.orderBy = orderBy;
			this.tags = parseTags(tags);
			this.facets = parseFacetString(facets);
			this.type = type;

			//build search criteria, search by SOLR and build a JSON Object out of the result set
			
			this.criteria = buildCriteria(false);
			this.results = solrService.search(criteria);
			JSONObject result = buildJSON();

			// return
			return Response.ok(result.toString()).build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().entity(e.getCause().getLocalizedMessage()).build();
		}
	}
	
	//********************* prepare search query *****************//
	private KiWiSearchCriteria buildCriteria(boolean typebased) {
		
		KiWiSearchCriteria criteria = solrService.parseSearchString(getFullQuery());
		
		//set basics
		criteria.setOffset((page-1)*pageSize);
		criteria.setLimit(pageSize);
		criteria.setSortOrder(order);
		criteria.setSortField(orderBy);
		
		//set facets
		Set<String> rdfFacets = new HashSet<String>();
		rdfFacets.add(CATEGORY_URI);
		
		//set types
		criteria.setSolrSearchString(getSOLRQuery(typebased));
		
		return criteria;
	}
	
	private String getSOLRQuery(boolean typebased) {
		//set types
		String stypes="(";
		if(typebased && type != null && !type.equals("")) {
			stypes = "type:\""+"uri::"+type+"\"";
		} else {
			for( int i = 0; i < SN_TYPES.size(); i++ ) {
				stypes += "type:\""+"uri::"+SN_TYPES.get(i)+"\"";
				if( i != SN_TYPES.size()-1 ) {
					stypes += " OR ";
				}
			}
			stypes+=")";
		}
		
		return stypes;
	}
	
	private String getFullQuery() {
		
		//set tags
		String stags = "";
		for( String tag : tags ) {
			stags += " tag:\""+tag+"\"";
		}
		
		//set rdf facets
		String sfacets = "";
		for( Facet f : facets ) {
			sfacets += " "+f.buildQuery();
		}
		
		//set query
		String squery = query;
		if(query.equals("*")) squery = "";
		
		String fullquery = squery + stags + sfacets;
		if(fullquery.equals("")) {
			//TODO change
			fullquery = "type:kiwi:ContentItem";
		}
		
		return fullquery;
	}
	
	//******************** create JSON Result *******************//
	private JSONObject buildJSON() throws SNResultException {
		try {
			JSONObject obj = new JSONObject();
			obj.put("query",getJSONQuery());
			obj.put("results",getJSONResult());
			
			return obj;
		} catch (JSONException e) {
			throw new SNResultException("JSON ReturnObject can not be created: "+e.getMessage());
		}
	}
	
	private JSONObject getJSONQuery() throws JSONException {
		JSONObject o = new JSONObject();
		
		boolean set = true;
		if(type != null) {
			for( KiWiFacet<KiWiResource> ur : results.getTypeFacets() ) {
				KiWiUriResource kur = (KiWiUriResource)ur.getContent();
				if( kur.getUri().equals(type) ) {
					JSONObject to = new JSONObject();
					to.put("uri", type);
					to.put("title", kur.getLabel());
					o.put("type", to);
					set = false;
					break;
				}
			}
		}
		if(set) {
			type = "";
			o.put("type", "default");
		}

		o.put("string", criteria.getKeywords());
		
		JSONArray f_array = new JSONArray();
		for( Facet item : facets ) {
			f_array.put(item.buildJSON());
		}
		o.put("facets",f_array);

		o.put("order", order);
		o.put("orderBy",orderBy);
		o.put("page", page);
		return o;
	}
	
	private JSONObject getJSONResult() throws SNResultException {
		JSONObject o = new JSONObject();
		try {
			o.put("total", results.getResultCount());
			
			//property facets
			JSONArray facets = new JSONArray();
			Iterator<KiWiUriResource> fi = results.getPropertyFacets().keySet().iterator();
			while(fi.hasNext()) {
				JSONObject fo = new JSONObject();
				KiWiUriResource uri = fi.next();
				fo.put("uri", uri.getUri());
				fo.put("title", "urititle");
				
				Set<KiWiFacet<String>> tf = results.getPropertyFacets().get(uri);
				JSONArray fv = new JSONArray();
				for( KiWiFacet<String> kf : tf ) {
					JSONObject fvo = new JSONObject();
					fvo.put("uri", kf.getContent());
					fvo.put("title", kf.getContent());
					fvo.put("count", kf.getResultCount());
					fv.put(fvo);
				}
				fo.put("values", fv);
				facets.put(fo);
			}
			o.put("facets", facets);
			
			//tag facets
			JSONArray tags = new JSONArray();
			for( KiWiFacet<ContentItem> tf : results.getTagFacets() ) {
				JSONObject to = new JSONObject();
				to.put("title", tf.getContent().getTitle());
				to.put("uri", ((KiWiUriResource)tf.getContent().getResource()).getUri());
				to.put("count", tf.getResultCount());
				tags.put(to);
			}
			o.put("tags",tags);
			
			//types
			JSONArray types = new JSONArray();
			//order types
			for( KiWiFacet<KiWiResource> ur : results.getTypeFacets() ) {
				String uri = ((KiWiUriResource)ur.getContent()).getUri();
				if( SN_TYPES.contains(uri) ) {
					JSONObject to = new JSONObject();
					to.put("title", ur.getContent().getLabel());
					to.put("uri", ((KiWiUriResource)ur.getContent()).getUri());
					to.put("count", ur.getResultCount());
					types.put(to);
				}
			}
			o.put("types",types);
			
			if(type != null && !type.equals("")) {
				this.results = solrService.search(buildCriteria(true));
			}
			o.put("items", getJSONResultItems());
			
		} catch (JSONException e) {
			throw new SNResultException("Some failure building JSON result: "+e.getMessage());
		}
		
		return o;
	}
	
	/**
	 * for templating
	 * @return
	 * @throws JSONException 
	 */
	private JSONArray getJSONResultItems() throws JSONException {
		JSONArray l = new JSONArray();
		for( SearchResult res : results.getResults()) {
			ContentItem ci = res.getItem();
			JSONObject ro = new JSONObject();
			ro.put("title", ci.getTitle());
			if( res.getHighlightPreview() != "" ) {
				String des = renderingPipeline.renderPreview(ci);
				if(des.length() < MAX_DESC_SIZE) {
					ro.put("description", des);
				} else {
					des = des.substring(0,MAX_DESC_SIZE);
					int x = des.lastIndexOf(" ");
					ro.put("description", des.substring(0,x)+"...</p>");
				}
			} else {
				ro.put("description", "..."+res.getHighlightPreview()+"...");
			}
			//TODO get ecid
			ro.put("id", ci.getId());
			ro.put("modified", df.format(ci.getModified()));
			
			//set type
			ro.put("type", getType(ci));
			
			l.put(ro);
		}
		return l;
	}
	
	private String getType(ContentItem ci) {
		for( KiWiResource type : ci.getTypes() ) {
			String s = ((KiWiUriResource)type).getUri();
			if( SN_TYPES.contains(s) ) return s;
		}
		return "default";
	}
	
	//******************* SOME PARSING STUFF **********************//
	private List<String> parseTags(String s) {
		List<String> l = new LinkedList<String>();
		
		// Compile and use regular expression
		Pattern pattern = Pattern.compile(TAG_PATTERN);
		Matcher matcher = pattern.matcher(s);
		while(matcher.find()) {
			l.add(matcher.group());
		}
		
		return l;
	}
	
	/**
	 * parse facets
	 * @param s
	 * @return
	 */
	private List<Facet> parseFacetString(String s) {
		List<Facet> l = new LinkedList<Facet>();
		
		// Compile and use regular expression
		Pattern pattern = Pattern.compile(FACET_PATTERN);
		Matcher matcher = pattern.matcher(s);
		while(matcher.find()) {
			String uri = matcher.group(1);
			String value_uri = matcher.group(2);
			Facet f = new Facet(uri);
			if( l.contains(f) ) {
				l.get(l.indexOf(f)).addValue(value_uri);
			} else {
				f.addValue(value_uri);
				l.add(f);
			}
		}
		
		return l;
	}
	
	class Facet {
		private String uri;
		private String title;
		private List<Value> values;
	
		public Facet(String uri) {
			this.uri = uri;
			if(uri.startsWith("http://")) {
				this.title = "FacetTitle";
			} else {
				this.title = uri;
			}
			this.values = new LinkedList<Value>();
		}
		
		public void addValue(String uri) {
			this.addValue(uri,-1);
		}
		
		public void addValue(String uri, int count) {
			this.values.add(new Value(uri,count));
		}
		
		public boolean equals(Object o) {
			try {
				return ((Facet)o).uri.equals(uri);
			} catch(Exception e) {
				return false;
			}
		}
		
		public JSONObject buildJSON() throws JSONException {
			JSONObject o = new JSONObject();
			o.put("uri", uri);
			o.put("title", title);
			JSONArray v_array = new JSONArray();
			for( Value item : values ) {
				v_array.put(item.build());
			}
			o.put("values",v_array);
			return o;
		}
		
		public String buildQuery() {
			String s = "";
			for( Value v : values ) {
				s += " uri::"+uri+":"+v.buildQuery();
			}
			return s;
		}
		
		class Value {
			
			private String title;
			private String uri;
			private int count;
			
			public Value(String uri, int count) {
				this.uri = uri;
				if(uri.startsWith("http://")) {
					//TODO set title
					this.title = "Test";
				} else {
					this.title = uri;
				}	
				this.count = count;
			}
			
			public String buildQuery() {
				if(uri.startsWith("http://")) {
					return "uri::"+uri;
				} else {
					return "'"+title+"'";
				}
			}
			
			public JSONObject build() throws JSONException {
				JSONObject o = new JSONObject();
				o.put("uri", uri);
				o.put("title", title);
				if( count > -1 ) o.put("count", count);
				return o;
			}
			
		}
		
	}

}
