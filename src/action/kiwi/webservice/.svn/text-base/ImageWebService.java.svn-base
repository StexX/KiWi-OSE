package kiwi.webservice;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import kiwi.api.content.ContentItemService;
import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("kiwi.webservice.ImageWebService")
@Scope(ScopeType.STATELESS)
@Path("/images")
public class ImageWebService {

	@In
	ContentItemService contentItemService;
	
	@GET
	@Path("/thumb")
	@Produces("image/jpeg")
	public Response getThumbnail(@QueryParam("maxWidth") @DefaultValue("100") int maxWidth,
							@QueryParam("maxHeight") @DefaultValue("100") int maxHeight,
							@QueryParam("uri") String uri) {
		try {
			
			//read
			ContentItem ci = contentItemService.getContentItemByUri(uri);
			InputStream in = new ByteArrayInputStream(ci.getMediaContent().getData());
			BufferedImage img = ImageIO.read(in);
			in.close();
			
			double imgHeight = img.getHeight();
			double imgWidth = img.getWidth();
			
			double origRatio = imgHeight / imgWidth;
			double necRatio = maxHeight / maxWidth;
			
			long height = 0;
			long width = 0;
			
			if( origRatio > necRatio ) {
				//hight must be min
				if( imgHeight < maxHeight ) {
					height = Math.round( imgHeight );
				} else {
					height = Math.round( maxHeight );
				}
			} else {
				if( imgWidth < maxWidth ) {
					height = Math.round( imgHeight );
				} else {
					height = Math.round( ( maxWidth / imgWidth ) * imgHeight );
				}
			}
			width =  Math.round( height / origRatio );
			
			//scale and draw
			Image newImage = img.getScaledInstance((int)width, (int)height, BufferedImage.SCALE_SMOOTH);
			BufferedImage bi = new BufferedImage( newImage.getWidth(null),newImage.getHeight(null),BufferedImage.TYPE_INT_RGB);   
			Graphics2D g2 = bi.createGraphics();
			g2.drawImage(newImage, 0, 0, null);

			//write
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			byte[] out = baos.toByteArray();
			baos.flush();
			baos.close();
			
			//return
			return Response.ok(out).build();
		} catch(Exception e) {
			return Response.status(404).build();
		}
	}
}
