package kiwi.action.webservice.thesaurusManagement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kiwi.api.content.ContentItemService;
import kiwi.model.content.ContentItem;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * 
 * @author Rolf Sint
 *
 */
@Path("/webResource")
@Name("webResource")
public class AllResource {

	@Logger
	private static Log log;
	
	@In
	private ContentItemService contentItemService;
	
	@GET
	@Produces("application/rdf+xml")
	public ContentItem getConcept(@QueryParam("url") String uri){
				
		log.info(uri);
		uri = uri.replace("$","#");
		
		ContentItem ci = contentItemService.getContentItemByUri(uri);
		//ContentItem ci = entityManager.find(ContentItem.class, id);
		
		log.info(ci.getTitle());
	
		return ci.getDelegate();		
	}	
}
