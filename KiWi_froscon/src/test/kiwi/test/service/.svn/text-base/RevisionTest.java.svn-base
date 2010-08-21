package kiwi.test.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.Assert;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.revision.RevisionService;
import kiwi.model.content.ContentItem;
import kiwi.model.revision.CIVersion;
import kiwi.test.service.UpdateTextContentTest.UpdateTextContentSupport;
import nu.xom.Document;

import org.jboss.seam.Component;
import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

@Test
public class RevisionTest extends SeamTest {

    @Test
    public void testRevisionCreation() throws Exception {
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
        		ContentItemService contentItemService = (ContentItemService) 
        			Component.getInstance("contentItemService");
        		ContentItem ci = contentItemService.createContentItem("revisionTest");
        		
        		Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text0.xml");
        		contentItemService.updateTextContentItem(ci, xom);
        		
        		BufferedImage bimage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB); 
        		
        		contentItemService.updateMediaContentItem(ci, getImageBytes(bimage), "image/png", "file");
        		
        		contentItemService.saveContentItem(ci);
            }
    	}.run();
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	ConfigurationService configurationService = (ConfigurationService) 
            		Component.getInstance("configurationService");
        		ContentItemService contentItemService = (ContentItemService) 
        			Component.getInstance("contentItemService");
        		ContentItem ci = contentItemService.getContentItemByUri(configurationService.getBaseUri()+"/"+ "revisionTest");
        		
        		Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text2.xml");
        		contentItemService.updateTextContentItem(ci, xom);
        		
        		contentItemService.saveContentItem(ci);
            }
    	}.run();
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
	            ConfigurationService configurationService = (ConfigurationService) 
	        		Component.getInstance("configurationService");
	    		ContentItemService contentItemService = (ContentItemService) 
	    			Component.getInstance("contentItemService");
	    		RevisionService revisionService = (RevisionService) 
	    			Component.getInstance("revisionService");
	    		ContentItem ci = contentItemService.getContentItemByUri(configurationService.getBaseUri()+"/"+ "revisionTest");
	    		
	    		Assert.assertEquals(2, ci.getVersions().size());
	    		Assert.assertEquals(2, revisionService.getAllRevisions(ci).size());
            }
    	}.run();
    }
    
    
    @Test
    public void testRevisionQueries() throws Exception {
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
        		ContentItemService contentItemService = (ContentItemService) 
        			Component.getInstance("contentItemService");
        		ContentItem ci = contentItemService.createContentItem("revisionTest");
        		
        		Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text0.xml");
        		contentItemService.updateTextContentItem(ci, xom);
        		
        		BufferedImage bimage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB); 
        		
        		contentItemService.updateMediaContentItem(ci, getImageBytes(bimage), "image/png", "file");
        		
        		contentItemService.saveContentItem(ci);
            }
    	}.run();
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	ConfigurationService configurationService = (ConfigurationService) 
            		Component.getInstance("configurationService");
        		ContentItemService contentItemService = (ContentItemService) 
        			Component.getInstance("contentItemService");
        		ContentItem ci = contentItemService.getContentItemByUri(configurationService.getBaseUri()+"/"+ "revisionTest");
        		
        		Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text2.xml");
        		contentItemService.updateTextContentItem(ci, xom);
        		
        		contentItemService.saveContentItem(ci);
            }
    	}.run();
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	ConfigurationService configurationService = (ConfigurationService) 
	        		Component.getInstance("configurationService");
	    		ContentItemService contentItemService = (ContentItemService) 
	    			Component.getInstance("contentItemService");
	    		ContentItem ci = contentItemService.getContentItemByUri(configurationService.getBaseUri()+"/"+ "revisionTest");
	    		
	    		for(CIVersion v : ci.getVersions()) {
		    		Long id = v.getRevision().getId();
		    		
		        	EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
		        	Query q = entityManager.createQuery("update Revision r set r.creationDate = :backTo1994 where r.id = " + id.toString());
		        	
		        	q.setParameter("backTo1994", new GregorianCalendar(1994, 02, 14, 14, 00).getTime());
	    		}
            }
    	}.run();
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
	            ConfigurationService configurationService = (ConfigurationService) 
	        		Component.getInstance("configurationService");
	    		ContentItemService contentItemService = (ContentItemService) 
	    			Component.getInstance("contentItemService");
	    		RevisionService revisionService = (RevisionService) 
	    			Component.getInstance("revisionService");
	    		ContentItem ci = contentItemService.getContentItemByUri(configurationService.getBaseUri()+"/"+ "revisionTest");
	    		
	    		Assert.assertEquals(0, revisionService.getRevisions(new GregorianCalendar(2009, 02, 14, 14, 00).getTime(), ci));
            }
    	}.run();
    }
    
    
    
    private byte[] getImageBytes(BufferedImage image) {
		
		byte[] resultImageAsRawBytes = new byte[0];
			
			//write
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				ImageIO.write( image, "png", baos );
				//close
				baos.flush();
				resultImageAsRawBytes = baos.toByteArray();
				
				baos.flush();
				baos.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		return resultImageAsRawBytes;
	}
}
