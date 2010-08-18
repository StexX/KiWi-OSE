package kiwi.test.service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.Assert;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.testng.annotations.Test;

import kiwi.api.content.ContentItemService;
import kiwi.model.content.ContentItem;
import kiwi.test.base.KiWiTest;

@Test
public class UpdateTitleTest extends KiWiTest {

	private Long sizeOfRenamingUpdates = new Long(0);
	
	@Test
	public void initCurrentContentItem() throws Exception {
		
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTitleTest.class);
				EntityManager     em = (EntityManager) Component.getInstance("entityManager");
				
				Query qCountUpdates = em.createQuery("select count(r) from RenamingUpdate r");            	
				sizeOfRenamingUpdates = (Long) qCountUpdates.getSingleResult();
				log.info("Count of renamingupdates is #0 ", sizeOfRenamingUpdates);
			}
		}.run();
		
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTitleTest.class);
				
				ContentItemService contentItemService = (ContentItemService)
					Component.getInstance("contentItemService");
				log.info("Creating new ContentItem");
				ContentItem ci = contentItemService.createContentItem();
				log.info("Updating title to 'test1'");
				contentItemService.updateTitle(ci, "test1");
				sizeOfRenamingUpdates++;
				log.info("Count of renamingupdates is #0 ", sizeOfRenamingUpdates);
			}
		}.run();
		
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTitleTest.class);
				EntityManager     em = (EntityManager) Component.getInstance("entityManager");
				
				Query qCountUpdates = em.createQuery("select count(r) from RenamingUpdate r");            	
				Long countR = (Long) qCountUpdates.getSingleResult();
				
				Assert.assertEquals(sizeOfRenamingUpdates, countR);
				log.info("Count of renamingupdates is #0 ", sizeOfRenamingUpdates);
			}
		}.run();
		
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
Log log = Logging.getLog(UpdateTitleTest.class);
				
				ContentItemService contentItemService = (ContentItemService)
					Component.getInstance("contentItemService");
				log.info("Creating new ContentItem");
				ContentItem ci = contentItemService.createContentItem();
				log.info("Updating title to 'test2'");
				contentItemService.updateTitle(ci, "test2");
				sizeOfRenamingUpdates++;
				log.info("Count of renamingupdates is #0 ", sizeOfRenamingUpdates);
			}
		}.run();
		
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTitleTest.class);
				EntityManager     em = (EntityManager) Component.getInstance("entityManager");
				
				Query qCountUpdates = em.createQuery("select count(r) from RenamingUpdate r");            	
				Long countR = (Long) qCountUpdates.getSingleResult();
				
				Assert.assertEquals(sizeOfRenamingUpdates, countR);
				log.info("Count of renamingupdates is #0 ", sizeOfRenamingUpdates);
			}
		}.run();
	}
}
