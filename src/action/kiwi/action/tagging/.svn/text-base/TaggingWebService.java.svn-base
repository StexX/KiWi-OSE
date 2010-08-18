package kiwi.action.tagging;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import kiwi.action.tagging.pojo.JSONTagListResults;
import kiwi.api.content.ContentItemService;
import kiwi.api.equity.EquityService;
import kiwi.api.tagging.ExtendedTagCloudEntry;
import kiwi.api.tagging.TagCloudService;
import kiwi.api.tagging.TaggingService;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * WebService endpoint for tagging
 * Path to the methods:
 * [TaggingWS] = http://localhost:8080/KiWi/seam/resource/services/widgets/tagging
 * 
 * [TaggingWS]/listTags.json?docUri=http://
 * query.json?q=StartPage&jsonpCallback=cb123
 * @author Szaby Grünwald
 *
 */
@Name("kiwi.webservice.TaggingWebService")
@Scope(ScopeType.STATELESS)
@Path("/widgets/tagging")
public class TaggingWebService {

	@Logger
	private static Log log;

	@In(create = true)
	TaggingService taggingService;
	
	@In(create = true)
	private User currentUser;

	@In(create=true)
	private EquityService equityService;
	
	
	public enum OrderTypes {
		ALPHA, USAGE, EQUITY
	}

	/**
	 * GET webservice for listing tags for a specific resource.
	 * @param docUri WS URI parameter "resource=" resource uri
	 * @param order WS parameter "order=" sorting order, can be alpha, usage or equity 
	 * Default is usage.
	 * @param reverse WS parameter "reverse=" true or false. Default is false.
	 * @return
	 */
	@GET
	@Path("/listTags.json")
	@Produces("application/json")
	public Response getTags(
			@QueryParam("resource") 						String docUri,
			@QueryParam("order")	@DefaultValue("usage") 	String order,
			@QueryParam("reverse") 	@DefaultValue("false")	boolean reverse
			) {
		try {
			ContentItemService ciService = (ContentItemService)Component.getInstance("contentItemService");
			ContentItem item = ciService.getContentItemByUri(docUri);
			
			if(item == null){
				throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).build()
				);
			}
			
			OrderTypes orderType = OrderTypes.ALPHA; 
			if("alpha".equals(order)){
				orderType = OrderTypes.ALPHA;
			} else { 
				if("usage".equals(order)){
					orderType = OrderTypes.USAGE;
				} else { 
					if("equity".equals(order)) {
						orderType = OrderTypes.EQUITY;
					} else {
						// 400 "order type " + order + " is undefined.") 
						return Response.status(Status.BAD_REQUEST).entity("400 - order type " + order + " is undefined.").build();
					}
				}
			}
			
			List<Tag> tags = taggingService.getTags(item);
			JSONTagListResults jsonRes = new JSONTagListResults();
			
			insertTagsToJsonResult(tags, jsonRes, item);
			jsonRes.sortTags(orderType, reverse);
			jsonRes.setResource(docUri);
			jsonRes.setCurrentLogin(currentUser.getLogin());
			jsonRes.setOrder(order);
			jsonRes.setReverse(String.valueOf(reverse));
			
			return Response.ok(jsonRes.toString()).build();
			
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	private void insertTagsToJsonResult(List<Tag> tags,
			JSONTagListResults jsonRes, ContentItem item) {
		log.info("JSON packing #0 tags", tags.size());
		TagCloudService tagCloudService = (TagCloudService)Component.getInstance("tagCloudService");
		LinkedList<ExtendedTagCloudEntry> tagCloud = new LinkedList<ExtendedTagCloudEntry>();
		tagCloud.addAll(tagCloudService.aggregateTags(tags));
		
		for(ExtendedTagCloudEntry tagCloudEntry:tagCloud){
			
			String uri = ((KiWiUriResource)tagCloudEntry.getTag().getResource()).getUri();
			Long usage = taggingService.getTagUsage(tagCloudEntry.getTag());
			
			List<Tag> tagCloudEntryTags = taggingService.getTagsByTaggedTaggingIds(item.getId(),tagCloudEntry.getTag().getId());
			double tq = equityService.getTagEquity(tagCloudEntryTags.get(0));
			
			String label = tagCloudEntry.getTagTitle();
			boolean controlled = taggingService.isControlled(tagCloudEntry.getTag().getResource());
			boolean isOwnTag = tagCloudEntry.isOwnTag();
			log.info("tag label: #0, uri: #1, tq: #2", label, uri, tq);
			jsonRes.addTag(uri, usage, label, tq, controlled, isOwnTag);
		}
	}

	/**
	 * Webservice for creating tags for a resource 
	 * @param docUri WS URI parameter "resource=" resource uri
	 * @param tags
	 * @return
	 */
	@GET
	@Path("/addTags")
	@Produces("application/json")
	public Response addTags(@QueryParam("resource") String docUri,
			@QueryParam("tags") String tags){
		
		ContentItemService ciService = (ContentItemService)Component.getInstance("contentItemService");
		ContentItem item = ciService.getContentItemByUri(docUri);
		
		if(item==null)
			return Response.status(Status.NOT_FOUND).build();
		if(tags == null || "".equals(tags.trim()))
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).
//					header(HttpHeaders.WWW_AUTHENTICATE, "what comes here?").
					entity("400 - Required parameter tags was empty.").
					build()
			);
		
		String[] tagLabels = tags.split(",");
		
		taggingService.addTags(item, tagLabels);
		return Response.ok("{}").build();
	}
	
	/**
	 * Webservice for removing existing tags belonging to a resource.
	 * @param docUri WS URI parameter "resource=" resource uri
	 * @param tags comma-separated list of tag URI's to remove
	 * @param mode can be private of shared
	 * @return
	 */
	@GET
	@Path("/removeTags")
	@Produces("application/json")
	public Response removeTags(@QueryParam("resource") String docUri,
			@QueryParam("tags") String tags,
			@QueryParam("mode") @DefaultValue("private")String mode){
		
		ContentItemService ciService = (ContentItemService)Component.getInstance("contentItemService");
		ContentItem item = ciService.getContentItemByUri(docUri);
		
		if(item==null)
			return Response.status(Status.NOT_FOUND).build();

		if(tags == null || "".equals(tags.trim()))
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).
//					header(HttpHeaders.WWW_AUTHENTICATE, "what comes here?").
					entity("Required parameter tags was empty.").
					build()
			);
		
		String[] tagUris = tags.split(",");
		taggingService.removeTaggings(item, tagUris, (mode.toLowerCase()!="private"? true: false));
		
		return Response.ok("{}").build();
	}
}
