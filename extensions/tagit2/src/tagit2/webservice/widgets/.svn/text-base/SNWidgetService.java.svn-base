package tagit2.webservice.widgets;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.SolrService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.util.MD5;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

import tagit2.api.query.IconService;


@Name("tagit2.webservice.SNWidget")
@Scope(ScopeType.STATELESS)
@Path("/widgets/tagit2/points")
//@Transactional
public class SNWidgetService {
	
	@Logger
	Log log;
	
	@In
	private EntityManager entityManager;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
    @In
    private SolrService solrService;
	
    /*
	@In(value="tagit2.iconService",create=true)
	private IconService iconService;
	*/
    
	@GET
	@Path("/single")
	@Produces("application/json")
	@Wrapped(element="single")
	public SNWidgetPoint getSinglePoint(@QueryParam("id") String id ) {
		try {
			return createSNWIdgetPoint(getId(id ));
		} catch (Exception e) {
			log.error(e.toString());
		}
		return new SNWidgetPoint();
	}
	
	@GET
	@Path("/multi")
	@Produces("application/json")
	@Wrapped(element="points")
	public List<SNWidgetPoint> getMultiPoints(@QueryParam("ids") String ids) {
		try {
			StringTokenizer t = new StringTokenizer(ids, ",");
			LinkedList<SNWidgetPoint> l = new LinkedList<SNWidgetPoint>();
			
			//parse ids
			List<String> id_strings = new LinkedList<String>();
			while (t.hasMoreTokens()) {
				id_strings.add(t.nextToken());
			}
			
			List<Long> id_longs = getIds(id_strings);
			for( Long lid : id_longs ) {
				SNWidgetPoint p = createSNWIdgetPoint(lid);
				if (p.getTitle() != null)
					l.add(p);
			}
			
			if(!l.isEmpty())
				return l;
		} catch (Exception e) {
			log.error(e.toString());
		}
		return new LinkedList<SNWidgetPoint>();
	}
	
	private SNWidgetPoint createSNWIdgetPoint(long id) throws Exception {
		// get facade by id
		ContentItem c = entityManager.find(ContentItem.class, id);
		if (c != null) {
			SNWidgetPointFacade p = kiwiEntityManager.createFacade(c,
					SNWidgetPointFacade.class);
			// get icon by content item
			//TODO special icon service (get icon for category)
			String iconUrl = "nachricht_default.png";
			//test if there is a point defined
			//TODO remove comment
			//if( p.getLongitude() != 0.0 & p.getLatitude() != 0.0 ) 
				// build JSON Object
				return new SNWidgetPoint(p, iconUrl);
		}
		return new SNWidgetPoint();		
	}
	
	private List<Long> getIds(List<String> id_strings) {
		List<Long> ids = new LinkedList<Long>();
		//make a solr query for id list
		
		String uri_ecid = Constants.NS_FCP_CORE + "ecid";
		String f_ecid = "l_"+MD5.md5sum(uri_ecid);
		
		StringBuilder qString = new StringBuilder();
		qString.append(f_ecid);
		qString.append(":(");
		for( int i = 0; i < id_strings.size(); i++ ) {
			qString.append(id_strings.get(i));
			if( i != id_strings.size()-1 ) {
				qString.append(" OR ");
			}
		}
		qString.append(")");
		
		//get solr
		KiWiSearchCriteria criteria = solrService.parseSearchString("");
		criteria.setSolrSearchString(qString.toString());
		
		SolrQuery query = solrService.buildSolrQuery(criteria);
		
		query.setStart(0);
		query.setRows(Integer.MAX_VALUE);
		
		query.setFields("id");
		QueryResponse rsp = solrService.search(query);
		SolrDocumentList docs = rsp.getResults();
		
		for(SolrDocument doc : docs) {
			try {
				Long id = Long.parseLong(doc.getFieldValue("id").toString());
				ids.add(id);
			} catch(NumberFormatException e) {
				log.error("id field was not a valid integer value: #0",doc.getFieldValue("id").toString());
			}
		}
		
		return ids;
	}
	
	private long getId( String id ) {
		List<String> ls = new LinkedList<String>();
		ls.add(id);
		List<Long> l = getIds(ls);
		if( l.size() == 0 ) return -1;
		else return l.get(0);
	}

}
