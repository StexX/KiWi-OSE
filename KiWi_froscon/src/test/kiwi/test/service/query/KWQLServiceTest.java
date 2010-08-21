package kiwi.test.service.query;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

import kiwi.api.content.ContentItemService;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.query.kwql.KwqlService;
import kiwi.model.content.ContentItem;
import kiwi.test.base.KiWiTest;

public class KWQLServiceTest extends KiWiTest {
	
	@Test
	public void testValueSearch() throws Exception {
		
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	ContentItem c1 = cs.createTextContentItem("<p>This is a kwql test content item</p>");
            	cs.updateTitle(c1, "ContentItem one kwql");
            	cs.saveContentItem(c1);
             	
				ContentItem c2 = cs.createTextContentItem("<p>test</p>");
            	cs.updateTitle(c2, "ContentItem two kwql");
            	cs.saveContentItem(c2);
              }
    	}.run();
    	
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	KwqlService        ks = (KwqlService) Component.getInstance("kiwi.query.kwqlService");

            	String query = "ci(title:kwql)";
            	KiWiSearchResults result = ks.search(query);
            	Assert.assertEquals(result.getResultCount(), 2);
            	
            	ContentItem c1 = result.getResults().get(0).getItem();
            	Assert.assertTrue(c1.getTitle().contains("kwql"));
            	
            	ContentItem c2 = result.getResults().get(1).getItem();
            	
            	Assert.assertTrue(c2.getTitle().contains("kwql"));
            	
            	query = "ci(title:(one AND kwql))";

            	result = ks.search(query);
            	Assert.assertEquals(result.getResultCount(), 1);
            	
            	c1 = result.getResults().get(0).getItem();
            	
            	Assert.assertEquals(c1.getTitle(), "ContentItem one kwql");
            	
             }
    	}.run();  
    	
	}
	
}