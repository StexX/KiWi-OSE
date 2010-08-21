package kiwi.test.service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.content.ContentItemService;
import kiwi.api.revision.RevisionService;
import kiwi.api.tagging.TaggingService;
import kiwi.model.content.ContentItem;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.TaggingUpdate;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the functionality of UpdateTaggingService
 * @author Stephanie Stroka (sstroka@salzburgeresearch.at)
 *
 */
@Test
public class UpdateTaggingServiceTest extends KiWiTest {

	private Long countTagginUpdates = new Long(0);
	
	/**
	 * calculate amount of tagging update
	 * @throws Exception
	 */
	@Test
	public void init() throws Exception {
		
		/**
		 * initialises variable: countTaggingUpdates
		 */
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				
				Log log = Logging.getLog(this.getClass());
				Identity.setSecurityEnabled(false);
				EntityManager em = (EntityManager) 
						Component.getInstance("entityManager");
				Assert.assertNotNull(em);
				
				Query q1 = em.createQuery("select count(t) from TaggingUpdate t");
				q1.setMaxResults(1);
				countTagginUpdates = (Long) q1.getSingleResult();
				log.info("CountTaggingUpdate: #0 ", countTagginUpdates);
			}
			
		}.run();
	}
	
	@Test(dependsOnMethods = { "init" })
    public void testRevision() throws Exception {
    	
		/**
		 * add a tag
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(this.getClass());
            	ContentItemService cs = (ContentItemService) Component
            			.getInstance("contentItemService");
            	TaggingService ts = (TaggingService) 
            		Component.getInstance("taggingService");
            	ContentItem taggedItem = cs.createExternContentItem("http://www.example.com/tagged");
            	ContentItem taggingItem = cs.createExternContentItem("http://www.example.com/tagging");
            	User user = (User) Component.getInstance("currentUser");
            	ts.createTagging("newTag", taggedItem, taggingItem, user);
            	log.info("Tagging created");
            	
            }
    	}.run();
    	
    	/**
    	 * test for added tag and remove a tag
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(this.getClass());
            	EntityManager em = (EntityManager) Component.getInstance("entityManager");
            	ContentItemService cs = (ContentItemService) Component
    					.getInstance("contentItemService");
            	
            	log.info("Before query");
            	Query q1 = em.createQuery("select count(t) from TaggingUpdate t");
            	q1.setMaxResults(1);
            	log.info("(0.5)");
            	log.info("query result: #0 , expected: #1 ", q1.getSingleResult(), countTagginUpdates+1L);
            	Assert.assertEquals(q1.getSingleResult(),countTagginUpdates+1L);
            	countTagginUpdates = countTagginUpdates+1L;
            	
            	log.info("(1)");
            	ContentItem taggedItem = cs.getContentItemByUri("http://www.example.com/tagged");
            	ContentItem taggingItem = cs.getContentItemByUri("http://www.example.com/tagging");
            	
            	log.info("(2)");
            	Assert.assertTrue(taggedItem.getVersions().size() >= 1);
            	log.info("(3)");
            	CIVersion vers = taggedItem.getVersions().get(taggedItem.getVersions().size()-1);

            	TaggingUpdate tu = vers.getTaggingUpdate();
            	
            	log.info("(4)");
            	Assert.assertEquals(tu.getAddedTags().size(),1L);
            	log.info("(5)");
            	Assert.assertEquals(tu.getRemovedTags().size(),0L);
            	log.info("(6)");
            	
            	Tag t = tu.getAddedTags().iterator().next();
            	Assert.assertEquals(t.getTaggingResource(),taggingItem);
            	log.info("(7)");
            	
            	TaggingService ts = (TaggingService) 
        				Component.getInstance("taggingService");
            	log.info("(8)");
            	ts.removeTagging(t);
            	log.info("(9)");
            }
    	}.run();
    	
    	/**
    	 * test for removed tag and undo taggingupdate
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(this.getClass());
            	EntityManager em = (EntityManager) Component.getInstance("entityManager");
            	ContentItemService cs = (ContentItemService) Component
    					.getInstance("contentItemService");
            	
            	Query q1 = em.createQuery("select count(t) from TaggingUpdate t");
            	q1.setMaxResults(1);
            	log.info("(10)");
            	Assert.assertEquals(q1.getSingleResult(),countTagginUpdates+1L);
            	log.info("(11)");
            	countTagginUpdates = countTagginUpdates+1L;
            	
            	ContentItem taggedItem = cs.getContentItemByUri("http://www.example.com/tagged");
            	
            	log.info("(12)");
            	Assert.assertTrue(taggedItem.getVersions().size() >= 1);
            	log.info("(13)");
            	CIVersion version = taggedItem.getVersions().get(taggedItem
            			.getVersions().size()-2);
            	
            	RevisionService revs = (RevisionService) 
            			Component.getInstance("revisionService");
            	revs.restore(version.getRevision());
            	log.info("(14)");
            }
    	}.run();
    	
    	/**
    	 * test for undo results (should be 1 added tag and 0 removed tags)
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(this.getClass());
            	EntityManager em = (EntityManager) Component.getInstance("entityManager");
            	ContentItemService cs = (ContentItemService) Component
    					.getInstance("contentItemService");
            	
            	log.info("(15)");
            	Query q1 = em.createQuery("select count(t) from TaggingUpdate t");
            	q1.setMaxResults(1);
            	log.info("(16)");
            	Assert.assertEquals(q1.getSingleResult(),countTagginUpdates+1L);
            	countTagginUpdates = countTagginUpdates+1L;
            	
            	ContentItem taggedItem = cs.getContentItemByUri("http://www.example.com/tagged");
            	ContentItem taggingItem = cs.getContentItemByUri("http://www.example.com/tagging");
            	
            	log.info("(17)");
            	Assert.assertTrue(taggedItem.getVersions().size() >= 1);
            	CIVersion rev = taggedItem.getVersions().get(taggedItem.getVersions().size()-1);
            	
            	log.info("(18)");
            	TaggingUpdate tu = rev.getTaggingUpdate();
            	
            	Assert.assertEquals(tu.getAddedTags().size(),1L);
            	log.info("(19)");
            	Assert.assertEquals(tu.getRemovedTags().size(),0L);
            	
            	log.info("(20)");
            	Tag t = tu.getAddedTags().iterator().next();
            	Assert.assertEquals(t.getTaggingResource(),taggingItem);
            	log.info("(21)");
            }
    	}.run();
    }
}
