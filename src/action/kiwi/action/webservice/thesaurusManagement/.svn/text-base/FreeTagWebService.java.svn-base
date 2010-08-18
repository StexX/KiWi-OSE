package kiwi.action.webservice.thesaurusManagement;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import kiwi.api.search.SolrService;
import kiwi.api.tagging.TaggingService;
import kiwi.model.content.ContentItem;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;



@Path("/freeTagWebService")
@Name("freeTagWebService")
public class FreeTagWebService {
	
	@Out(required = false)
	private WS ws;
	
    @In
    private SolrService solrService;
    
    @In
    private TaggingService taggingService;
	
	@GET
	@Produces("application/rdf+xml")
	@Path("all")
	public Response getUpdates(){
	
		ws = WS.ArrayCIs;
		
		List<ContentItem> freeTags = taggingService.getFreeTags();
		
		return Response.ok(freeTags).build();
	}	
}
