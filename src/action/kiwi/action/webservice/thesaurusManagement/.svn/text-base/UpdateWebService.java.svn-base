/**
 * 
 */
package kiwi.action.webservice.thesaurusManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import kiwi.api.content.ContentItemService;
import kiwi.model.content.ContentItem;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.log.Log;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author Rolf Sint
 * 
 */
@Path("/updateWebService")
@Name("updateWebService")
public class UpdateWebService {
	
	
//	@In(required=false) 
//	private RevisionService revisionService;
	
	@In(required=false) 
	private ContentItemService contentItemService;
	
	@Logger
	private Log log;
	
	@In(required = false)
	ContentItem currentContentItem;
	
	
	@Out(required = false)
	private Calendar since;
	
	@Out(required = false)
	private WS ws;
	
	//TODO: datum in ISO 6801, libary Jodatime
	@GET
	@Produces("application/rdf+xml")
	@Path("all")
	public Response getUpdates(@QueryParam("isoTime") String isoTime){
	
		ws = WS.ArrayCIs;
		
		log.info("IsoTime",isoTime);
		log.info((contentItemService == null)?"contentItemService = null":"contentItemService is not null");
		
		DateTimeFormatter fmt = ISODateTimeFormat.dateTimeParser();
		DateTime dt = fmt.parseDateTime(isoTime);
		since  = dt.toCalendar(Locale.GERMANY);	
		
		log.info("dateTime "+since.getTime());
		ArrayList<ContentItem> contentItems = (ArrayList<ContentItem>) contentItemService.getContentItemsByDate(since.getTime(), new Date());
		log.info("updates:"+contentItems.size());
		return Response.ok(contentItems).build();
	}	
}
