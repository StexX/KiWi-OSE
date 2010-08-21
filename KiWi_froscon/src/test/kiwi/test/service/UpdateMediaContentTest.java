package kiwi.test.service;

import java.io.File;
import java.net.URL;

import kiwi.api.content.ContentItemService;
import kiwi.api.multimedia.MultimediaService;
import kiwi.api.revision.UpdateMediaContentService;
import kiwi.model.content.ContentItem;
import kiwi.model.revision.CIVersion;
import kiwi.test.base.KiWiTest;

import org.apache.commons.io.FileUtils;
import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(sequential=true)
public class UpdateMediaContentTest extends KiWiTest {

	
	@Test
	public void initCurrentContentItem() throws Exception {
		
		final URL datapath = this.getClass().getResource("content/data");
		final File f1 = new File(datapath.getPath()+File.separator+"kiwis_in_prague-4.jpg");
		final String fileName1 = f1.getName();
		final File f2 = new File(datapath.getPath()+File.separator+"kiwi1.png");
		final String fileName2 = f2.getName();
		
		/*
		 * Test a JPEG file
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final MultimediaService  ms = (MultimediaService) Component.getInstance("multimediaService");
            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final byte[] data     = FileUtils.readFileToByteArray(f1);
            	final String mimeType = ms.getMimeType(f1);

            	final ContentItem c = cs.createMediaContentItem(data, mimeType, fileName1);

            	Assert.assertEquals(c.getTitle(), fileName1);
            	Assert.assertNotNull(c.getMediaContent());
            	Assert.assertEquals(c.getMediaContent().getMimeType(), "image/jpeg");
            	Assert.assertEquals(c.getMediaContent().getFileName(), fileName1);
            	Assert.assertEquals(c.getMediaContent().getData(), data);
              }
    	}.run();
    	
    	/*
		 * Update MediaContent
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final byte[] data     = FileUtils.readFileToByteArray(f2);
            	
            	ContentItem c = cs.getContentItemByTitle(fileName1);
            	cs.updateMediaContentItem(c, data,  "image/png", fileName2);

            	Assert.assertEquals(c.getTitle(), fileName2);
            	Assert.assertNotNull(c.getMediaContent());
            	Assert.assertEquals(c.getMediaContent().getMimeType(), "image/png");
            	Assert.assertEquals(c.getMediaContent().getFileName(), fileName2);
            	Assert.assertEquals(c.getMediaContent().getData(), data);
              }
    	}.run();
    	
    	/*
		 * Undo Update
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	final UpdateMediaContentService updateMediaContentService = 
            			(UpdateMediaContentService) Component.getInstance("updateMediaContentService");
            	
            	ContentItem c = cs.getContentItemByTitle(fileName2);

            	Assert.assertNotNull(c);
            	CIVersion version = c.getVersions().get(
            			c.getVersions().size()-2);
            	
            	updateMediaContentService.restore(version);
            }
    	}.run();
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {
            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	
            	ContentItem item = cs.getContentItemByTitle(fileName1);
            	Assert.assertNotNull(item);
            	Assert.assertEquals(item.getTitle(), fileName1);
            	Assert.assertEquals(item.getMediaContent().getMimeType(), "image/jpeg");
            	Assert.assertEquals(item.getMediaContent().getFileName(), fileName1);
            	final byte[] data     = FileUtils.readFileToByteArray(f1);
            	Assert.assertEquals(item.getMediaContent().getData(), data);
            	
            	ContentItem item2 = cs.getContentItemByTitle(fileName2);
            	Assert.assertNull(item2);
              }
    	}.run();
	}
}
