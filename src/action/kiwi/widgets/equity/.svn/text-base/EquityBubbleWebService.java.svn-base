

package kiwi.widgets.equity;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import kiwi.api.content.ContentItemService;
import kiwi.api.equity.EquityService;
import kiwi.model.content.ContentItem;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Name("kiwi.widgets.equityBubbleWebService")
@Scope(ScopeType.STATELESS)
@Path("/widgets/ceq/buble")
public class EquityBubbleWebService {

    @Logger
    Log log;

    @In
    private EquityService equityService;

    @In
    private ContentItemService contentItemService;

    @GET
    @Path("/image")
    @Produces("image/png")
    public byte[] getBubble(@QueryParam("uri") String uri) {

        ContentItem ci = contentItemService.getContentItemByUri(uri);
        BufferedImage img = EquityBubble.getImageFor(equityService, ci);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return os.toByteArray();

    }
}
