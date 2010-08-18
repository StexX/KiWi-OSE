package kiwi.widgets.equity;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kiwi.api.content.ContentItemService;
import kiwi.api.equity.EquityService;
import kiwi.model.content.ContentItem;
import kiwi.service.equity.EquityChartService;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jfree.chart.labels.BubbleXYItemLabelGenerator;

@Name("kiwi.widgets.ceqChartWidget")
@Scope(ScopeType.STATELESS)
@Path("/widgets/ceq/chart")
public class CEQChartWebService {
    
    private static final int STEP = 100;
	
	@Logger
	Log log;
	
	@In
	private EquityService equityService;
	
	@In
	private EquityChartService equityChartService;
	
	@In
	private ContentItemService contentItemService;
	
	
	
	@GET
	@Path("/image")
	@Produces("image/png")
	public byte[] getChart(	@QueryParam("uri") String uri,
							@QueryParam("start") Long start,
							@QueryParam("end") Long end) {
		
		ContentItem ci = contentItemService.getContentItemByUri(uri);
		BufferedImage img = null;
		
		if( start != null ) {
			if( end != null ) {
				img = equityChartService.getChart(ci, new Date(start), new Date(end), STEP);
			} else {
				img = equityChartService.getChart(ci, new Date(start), new Date(), STEP);
			}
		} else if( end != null ) {
			img = equityChartService.getChart(ci, ci.getCreated(), new Date(end), STEP);
		} else {
			img = equityChartService.getChart(ci, STEP);
		}
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "png", os);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return os.toByteArray();
		
	}
	
	@GET
	@Path("/activities")
	@Produces("application/json")
	@Wrapped(element="activities")
	public List<CEQActivityJSON> getCEQActivities(
			                                     @QueryParam("uri") String uri,
			                                     @QueryParam("start") Long start,
			                                     @QueryParam("end") Long end,
			                                     @QueryParam("position") Double pos,
			                                     @QueryParam("width") Integer width) {
		ContentItem ci = contentItemService.getContentItemByUri(uri);
		List<CEQActivityJSON> list = new LinkedList<CEQActivityJSON>();
		
		if( start == null ) {
			start = ci.getCreated().getTime();
		}
		
		if( end == null ) {
			end = System.currentTimeMillis();
		}
		
		//get dates
		Date[] dates = getStartEnd( start,end,pos,width);
		
		//validate
		if( ci.getCreated().after(dates[0]) ) {
			dates[0] = ci.getCreated();
		}
		if( dates[1].after(new Date()) ) {
			dates[1] = new Date();
		}
		
		log.info("get activities from #0 to #1", dates[0], dates[1]);
		//get map and add to list
		Map<Date,String> m = equityService.getActivityNames(ci, dates[0], dates[1]);
		for( Map.Entry<Date, String> me : m.entrySet() ) {
			list.add(new CEQActivityJSON(me.getValue(),me.getKey()));
		}
		//order list
		Collections.sort(list, new Comparator<CEQActivityJSON>() {

			@Override
			public int compare(CEQActivityJSON o1, CEQActivityJSON o2) {
				return o1.compareTo(o2);
			}
			
		});
		
		return list;
	}
	
	private Date[] getStartEnd(Long start, Long end, Double position, Integer width) {
		Date[] dates = new Date[2];
		
		Long overallWidth = end-start;
		Long rangeWidth = Math.round((overallWidth / 100.0) * (width / 2.0));
		
		//get long for position
		Long pos = Math.round( overallWidth * position) + start;
		
		dates[0] = new Date( pos-rangeWidth );
		dates[1] = new Date( pos+rangeWidth );
		
		return dates;
	}

}
