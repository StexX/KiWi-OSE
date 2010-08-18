package tagit2.webservice.cluster;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import tagit2.action.explorer.ClusterAction;


@Name("tagit2.webservice.cluster")
@Scope(ScopeType.STATELESS)
@Path("/tagit2/cluster")
public class ClusterWebService {
	
	@Logger
	Log log;
	
	@In(value="tagit2.clusterAction",create=true)
	ClusterAction clusterAction;
	
	@GET
	@Produces("application/xml")
	public String getCluster() {
		return clusterAction.getCurrentClusterXML();
	}
	
	@GET
	@Produces("application/json")
	@Wrapped(element="points")
	public String getClusterJSON(@QueryParam("start") int start,
									@QueryParam("size") int size,
									@QueryParam("c") String c) {
		if( c==null ) {
			log.info("get whole cluster list");
			return clusterAction.getCurrentClusterJSON(start,size);
		} else {
			log.info("get list with char #0",c);
			return clusterAction.getCurrentClusterJSON(start,size,c);
		}
		
	}
	
	@GET
	@Path("/image")
	@Produces("image/png")
	public byte[] getClusterImage(@QueryParam("size") int origsize) {
		
		int size = getSize(origsize);
		log.info("size-converter: #0 -> #1",origsize,size);
		
		BufferedImage image = 
			new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);

		int xy = (int) Math.round((60.0-size)/2.0);
		Ellipse2D circle = new Ellipse2D.Double( xy, xy, size, size );	
		Graphics2D graphic = image.createGraphics();
		
		graphic.setPaint(new Color(255,127,0,160));
		graphic.fill(circle);
		
		graphic.setStroke( new BasicStroke());
		graphic.setPaint(Color.BLACK);
		graphic.draw(circle);
		
		graphic.setFont( new Font("Arial", Font.BOLD, 14));
		if( origsize > 9 ) {
			if( origsize > 99 ) {
				if( origsize > 999 ) {
					graphic.drawString(String.valueOf(origsize), 14, 35);
				} else {
					graphic.drawString(String.valueOf(origsize), 18, 35);
				}
			} else {
				graphic.drawString(String.valueOf(origsize), 23, 35);
			}
		} else {
			graphic.drawString(String.valueOf(origsize), 27, 35);
		}
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return os.toByteArray();
		
	}
	
	private int minCount = 2;
	private int maxCount = 5000;
	
	private int getSize(int osize) {
		double a = 30*Math.log(osize - minCount + 2) / Math.log(maxCount - minCount + 2) - 1;
		return Math.round( 30 + Math.round( a ) ) + 1;
	}

}
