package kiwi.test.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.revision.UpdateTextContentService;
import kiwi.exception.ContentItemDoesNotExistException;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.TextContentUpdate;
import kiwi.model.revision.TextContentUpdateDiff;
import kiwi.test.base.KiWiTest;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(sequential=true)
public class UpdateTextContentTest extends KiWiTest {

	private Long sizeOfTextContentUpdates = new Long(0);
	private ContentItem currentContentItem;
	
	@Test
	public void initCurrentContentItem() throws Exception {
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				currentContentItem = (ContentItem) Component.getInstance(
					"currentContentItem");
			}
		}.run();
	}
	
	@Test(dependsOnMethods={ "initCurrentContentItem" })
	public void initQuery() throws Exception {
		clearDatabase();
		
		new FacesRequest() {	
			@Override
			protected void invokeApplication() {

				Log log = Logging.getLog(UpdateTextContentTest.class);
				
				log.info("Init UpdateTextContentTest");
				
				EntityManager     em = (EntityManager) Component.getInstance("entityManager");
				
				Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");            	
				sizeOfTextContentUpdates = (Long) qCountUpdates.getSingleResult();
				
				log.info(">>>>>>  Initial size of TextContentUpdates is #0 ", sizeOfTextContentUpdates);
			}
			
		}.run();
	}
	
	@Test(dependsOnMethods={ "initQuery" })
	public void init() throws Exception {
		
		/**
    	 * set back the textcontent to an empty xml document: <div></div>
    	 */
    	new FacesRequest() {			
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>>>> >>>>>> Set back the TextContent to an empty xml document");
            	
				Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text0.xml");
				ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
				EntityManager     em = (EntityManager) Component.getInstance("entityManager");
				TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom);
            	
            	if(!em.contains(currentContentItem)) {
            		currentContentItem = em.merge(currentContentItem);
            	}
            	
				log.info(">>>>>> before update text content");
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
				
				log.info(">>>>>> after update text content");
			}
			
		}.run();
	}
	
	@Test(dependsOnMethods = { "init" })
	public void checkInit() throws Exception {
		/**
    	 * set back the textcontent to an empty xml document: <div></div>
    	 */
    	new FacesRequest() {			
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTextContentTest.class);
				
				log.info(">>>>>> Init UpdateTextContentTest");
				EntityManager     em = (EntityManager) Component.getInstance("entityManager");
				
				log.info(">>>>>> Before query");
				// the update is just added when the transaction completes successfully -> 0 updates
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	// if the last textcontent change produced an update, we increment sizeOfTextContentUpdates
            	if(qCountUpdates.getSingleResult().equals(sizeOfTextContentUpdates + 1L)) {
            		sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	}
            	log.info(">>>>>> after query");
			}
			
		}.run();
	}
	
	@Test(dependsOnMethods = { "checkInit" })
	public void testInitialTextContentUpdates() throws Exception {
		
		clearDatabase();
		
		/*
		 * Tests whether the updateTextContent() method succeeds with an initial text
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	
            	log.info(">>>> Test initial TextContentUpdate");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");

            	Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text1.xml");

            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	// the update is just added when the transaction completes successfully -> 0 updates
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	log.info(">>>> End Test initial TextContentUpdate");
            }
    	}.run();
		
    	/*
		 * Tests whether the update was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	Identity.setSecurityEnabled(false);
        		Log log = Logging.getLog(UpdateTextContentTest.class);
        		
        		log.info(">>>> Test amount of TextContentUpdates after initial update");
        		
            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");
            	
            	// the update is just added when the transaction completes successfully -> 0 updates
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	// if the last textcontent change produced an update, we increment sizeOfTextContentUpdates
            	if(qCountUpdates.getSingleResult().equals(sizeOfTextContentUpdates + 1L)) {
            		sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	}
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	log.info(">>>> End Test amount of TextContentUpdates after initial update");
            }
    	}.run();
    	
    	/*
		 * Tests whether the correct content was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Test initial TextContentUpdate content [testinitial]");

            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");
            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");
            	em.refresh(currentContentItem);
            	
            	Assert.assertTrue(currentContentItem.getVersions().size() > 0);
            	log.info("Version: #0",currentContentItem.getVersions().size()-1);
            	TextContentUpdate tcu = currentContentItem.getVersions().get(
            			currentContentItem.getVersions().size()-1).getTextContentUpdate();
            	
            	log.info("TextContent: #0",tcu.getTextContent().getXmlString());
            	Assert.assertNotNull(tcu.getChanges());
            	List<TextContentUpdateDiff> diffs = tcu.getChanges();
            	
            	log.info("diff get changed words: #0", diffs.get(0).getChangedWords());
            	// The TextContentUpdate should have 1 different node: /div[1]/p[1]
            	Assert.assertEquals(diffs.size(), 1);
            	Assert.assertEquals(diffs.get(0).getXPath(),"/*[local-name()='div'][1]/*[local-name()='p'][1]");
            	
            	// div[1]/p[1] contains 3 changed elements:
            	// the opening tag:
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap()
            			.containsKey("000000.000000"));
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap()
            			.get("000000.000000"), "");
            	// the content:
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap()
            			.containsKey("000001.000000"));
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap()
            			.get("000001.000000"), "");
            	// and the closing tag:
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap()
            			.containsKey("999999.000000"));
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap()
            			.get("999999.000000"), "");
            	
            	log.info(">>>> End Test initial TextContentUpdate content [testinitial]");
            }
    	}.run();
    	
    	/*
		 * Tests whether the updateTextContent() method succeeds with an initial text
		 */
//    	new FacesRequest() {
//
//            @Override
//            protected void invokeApplication() {
//            	EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
//            	ContentItem ci = entityManager.find(ContentItem.class, 5L);
//				System.out.println("---------> "+ ci.getVersions());
//			}
//		}.run();
		
    	/**
    	 * set back the textcontent to an empty xml document: <div></div>
    	 */
    	new FacesRequest() {			
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>>> Set back the TextContent to an empty xml document");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");
            	
				Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text0.xml");

				ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	
            	log.info(">>>>> End Set back the TextContent to an empty xml document");
			}
			
		}.run();
	}
	
	/**
	 * This Test adds words to a ContentItem and tests it for the correct output
	 * @throws Exception
	 */
	@Test(dependsOnMethods = { "checkInit" })
	public void testAddingWords() throws Exception {
		
		/*
		 * Tests whether the updateTextContent() method succeeds with an initial text
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Load initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");
	            	
	           	EntityManager     em = (EntityManager) Component.getInstance("entityManager");
            	
            	Document xom1 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text1.xml");
            	
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom1);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	log.info(">>>> End Load initial TextContent");
            }
    	}.run();
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Add word to a node in the initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");
            	
            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");
            	
            	Document xom2 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text2.xml");
				
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom2);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	// if the last textcontent change produced an update, we increment sizeOfTextContentUpdates
            	if(qCountUpdates.getSingleResult().equals(sizeOfTextContentUpdates + 1L)) {
            		sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	}
            	
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	
            	log.info(">>>> End Add word to a node in the initial TextContent");
            }
    	}.run();
    	
    	/*
		 * Tests whether the update was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
        		
        		Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Test for the correct amount of TextContentUpdates");
        		
            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	log.info(">>>> End Test for the correct amount of TextContentUpdates");
            }
    	}.run();
    	
    	/*
		 * Tests whether the correct content was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> [testAddingWords] Test for the correct content of TextContentUpdates");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	log.info(">>>> [testAddingWords] 1");
            	log.info("Version: #0",currentContentItem.getVersions().size()-1);
            	Assert.assertTrue(currentContentItem.getVersions().size() > 0);
            	TextContentUpdate tcu = currentContentItem.getVersions().get(
            			currentContentItem.getVersions().size()-1).getTextContentUpdate();
            	log.info("TextContent: #0",tcu.getTextContent().getXmlString());
            	log.info(">>>> [testAddingWords] 2");
            	Assert.assertNotNull(tcu.getChanges());
            	List<TextContentUpdateDiff> diffs = tcu.getChanges();
            	log.info("diff get changed words: #0", diffs.get(0).getChangedWords());
            	log.info(">>>> [testAddingWords] 3");
            	// The TextContentUpdate should have 1 different nodes: /div[1]/p[1]
            	Assert.assertEquals(diffs.size(), 1);
            	log.info(">>>> [testAddingWords] 4");
            	Assert.assertEquals(diffs.get(0).getXPath(),"/*[local-name()='div'][1]/*[local-name()='p'][1]");
            	log.info(">>>> [testAddingWords] #0", diffs.get(0).getChangedWordsMap());
            	// the content:
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap()
            			.containsKey("000002.000000"));
            	log.info(">>>> [testAddingWords] 6");
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap()
            			.get("000002.000000"), "");
            	log.info(">>>> [testAddingWords] 7");
            	
            	log.info(">>>> [testAddingWords] End Test for the correct content of TextContentUpdates");
            }
    	}.run();
    	
    	new FacesRequest() {			
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Set back the TextContent to an empty xml document");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text0.xml");

				ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	
            	log.info(">>>> End Set back the TextContent to an empty xml document");
			}
			
		}.run();
	}
	
	@Test(dependsOnMethods = { "checkInit" })
	public void testDeletingWords() throws Exception {
				
		/*
		 * Tests whether the updateTextContent() method succeeds with an initial text
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Load initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	Document xom1 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text3.xml");
            	
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom1);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	log.info(">>>> End Load initial TextContent");
            }
    	}.run();
    	
    	/*
		 * Tests whether the updateTextContent() method succeeds with an initial text
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>> Add word to a node in the initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");

            	Document xom2 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text4.xml");
				
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom2);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	// if the last textcontent change produced an update, we increment sizeOfTextContentUpdates
            	if(qCountUpdates.getSingleResult().equals(sizeOfTextContentUpdates + 1L)) {
            		sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	}
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	
            	log.info(">>> End add word to a node in the initial TextContent");
            	
            	
            }
    	}.run();
    	
    	/*
		 * Tests whether the update was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
        		
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>> Test for the correct amount of TextContentUpdates");
        		
            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	log.info(">>> End Test for the correct amount of TextContentUpdates");
            }
    	}.run();
    	
    	/*
		 * Tests whether the correct content was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Test for the correct content of TextContentUpdates [testDeletingWords]");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	Assert.assertTrue(currentContentItem.getVersions().size() > 0);
            	log.info("Version: #0",currentContentItem.getVersions().size()-1);
            	TextContentUpdate tcu = currentContentItem.getVersions().get(
            			currentContentItem.getVersions().size()-1).getTextContentUpdate();
            	log.info("TextContent: #0",tcu.getTextContent().getXmlString());
            	Assert.assertNotNull(tcu.getChanges());
            	List<TextContentUpdateDiff> diffs = tcu.getChanges();
            	log.info("diff get changed words: #0", diffs.get(0).getChangedWords());
            	// The TextContentUpdate should have 1 different nodes: /*[local-name()='div'][1]//*[local-name()='p'][1]
            	Assert.assertEquals(diffs.size(), 1);
            	Assert.assertEquals(diffs.get(0).getXPath(),"/*[local-name()='div'][1]/*[local-name()='p'][1]");
            	
            	log.debug("testDeletingWords: ChangedWordsMap #0 ", 
            			diffs.get(0).getChangedWordsMap());
            	
            	Map<String,String> changedWords = diffs.get(0).getChangedWordsMap();
            	Assert.assertEquals(changedWords.size(), 4);
            	// the content:
            	Assert.assertTrue(changedWords
            			.containsValue("willbedeleted1"));
            	Assert.assertTrue(changedWords
            			.containsValue("willbedeleted2"));
            	Assert.assertTrue(changedWords
            			.containsValue("willbedeleted3"));
            	Assert.assertTrue(changedWords
            			.containsValue("willbedeleted4"));
            	
            	List<String> list = new LinkedList<String>();
            	for(String s: changedWords.keySet()) {
            		list.add(changedWords.get(s));
            	}
            	Collections.sort(list);
            	Iterator<String> it = list.iterator();
            	Assert.assertEquals(it.next(), "willbedeleted1");
            	Assert.assertEquals(it.next(), "willbedeleted2");
            	Assert.assertEquals(it.next(), "willbedeleted3");
            	Assert.assertEquals(it.next(), "willbedeleted4");
            	
            	log.info(">>>> End Test for the correct content of TextContentUpdates");
            }
    	}.run();
    	
    	new FacesRequest() {			
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Set back the TextContent to an empty xml document");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text0.xml");

				ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	
            	log.info(">>>> End Set back the TextContent to an empty xml document");
			}
			
		}.run();
	}
	
	
	/**
	 * This Test adds xml nodes to a TextContent xml 
	 * document and checks whether it is stored correctly
	 * @throws Exception
	 */
	@Test(dependsOnMethods = { "checkInit" })
	public void testAddingNodes() throws Exception {
		
		/*
		 * Tests whether the updateTextContent() method succeeds with an initial text
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>> Load initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	Document xom1 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text1.xml");
            	
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom1);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	log.info(">>> End Load initial TextContent");
            }
    	}.run();
    	
    	/*
		 * Tests whether the updateTextContent() method succeeds with an initial text
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Add word to a node in the initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");

            	Document xom2 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text5.xml");
				
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom2);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	// if the last textcontent change produced an update, we increment sizeOfTextContentUpdates
            	if(qCountUpdates.getSingleResult().equals(sizeOfTextContentUpdates + 1L)) {
            		sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	}
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	
            	log.info(">>>> End Add word to a node in the initial TextContent");
            }
    	}.run();
    	
    	/*
		 * Tests whether the update was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
        		
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Test for the correct amount of TextContentUpdates");
        		
            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	log.info(">>>> End Test for the correct amount of TextContentUpdates");
            }
    	}.run();
    	
    	/*
		 * Tests whether the correct content was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Test for the correct content of TextContentUpdates");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	Assert.assertTrue(currentContentItem.getVersions().size() > 0);
            	log.info("Version: #0",currentContentItem.getVersions().size()-1);
            	TextContentUpdate tcu = currentContentItem.getVersions().get(
            			currentContentItem.getVersions().size()-1).getTextContentUpdate();
            	
            	log.info("TextContent: #0",tcu.getTextContent().getXmlString());
            	Assert.assertNotNull(tcu.getChanges());
            	List<TextContentUpdateDiff> diffs = tcu.getChanges();
            	log.info("diff: #0",diffs);
            	// The TextContentUpdate should have 3 different nodes
            	Assert.assertEquals(diffs.size(), 3);
            	
            	for(int i=0; i<3; i++) {
            		String xpath = diffs.get(i).getXPath();
            		if(xpath.equals("/*[local-name()='div'][1]/*[local-name()='p'][2]/*[local-name()='b'][1]")) {
            			diffs.get(i).getChangedWordsMap().containsKey("000000.000000");
            			diffs.get(i).getChangedWordsMap().containsKey("000001.000000");
            			diffs.get(i).getChangedWordsMap().containsKey("999999.000000");
            		} else if(xpath.equals("/*[local-name()='div'][1]/*[local-name()='p'][3]")) {
            			diffs.get(i).getChangedWordsMap().containsKey("000000.000000");
            			diffs.get(i).getChangedWordsMap().containsKey("000001.000000");
            			diffs.get(i).getChangedWordsMap().containsKey("999999.000000");
            		} else if(xpath.equals("/*[local-name()='div'][1]/*[local-name()='p'][1]")) {
            			diffs.get(i).getChangedWordsMap().containsKey("000000.000000");
            			diffs.get(i).getChangedWordsMap().containsKey("000001.000000");
            			diffs.get(i).getChangedWordsMap().containsKey("999999.000000");
            		} else {
            			Assert.fail("diff returned wrong xpath: " + xpath);
            		}
            		log.info(">>>> End Test for the correct content of TextContentUpdates");
            	}
            }
    	}.run();
    	
    	/* undo last textcontent update */
    	new FacesRequest() {			
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Test for the undo functionality of TextContentUpdates");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	CIVersion version = currentContentItem.getVersions().get(
            			currentContentItem.getVersions().size()-2);
            	
            	UpdateTextContentService utcs = (UpdateTextContentService) 
            			Component.getInstance("updateTextContentService");
            	
            	// restores the previous textcontent
            	try {
					utcs.restore(version);
				} catch (ContentItemDoesNotExistException e) {
					e.printStackTrace();
				}
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	
            	log.info(">>>> End Test for the undo functionality of TextContentUpdates");
            }
    	}.run();
    	
    	/*
		 * Tests whether the update was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
        		
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>> Test for the correct amount of TextContentUpdates");
        		
            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	log.info(">>> End Test for the correct amount of TextContentUpdates");
            }
    	}.run();
    	
    	/* undo last textcontent update */
    	new FacesRequest() {			
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Test for the correct content of the last TextContentUpdate");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	TextContentUpdate tcu = currentContentItem.getVersions().get(
            			currentContentItem.getVersions().size()-1).getTextContentUpdate();
            	
            	Document xom1 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text1.xml");
            	TextContent tc_tmp = new TextContent();
            	tc_tmp.setXmlDocument(xom1);
            	
            	List<String> currentText = new ArrayList<String>();
            	List<String> comparisonText = new ArrayList<String>();
            	for(String s : tcu.getTextContent().getXmlDocument().toXML().split(" ")) {
            		for(String s1: s.split("\\n")) {
            			currentText.addAll(Arrays.asList(s1.split("\\t")));
            		}
            	}
            	for(String s : tc_tmp.getXmlDocument().toXML().split(" ")) {
            		for(String s1: s.split("\\n")) {
            			comparisonText.addAll(Arrays.asList(s1.split("\\t")));
            		}
            	}
            	while( currentText.remove("") );
            	while( comparisonText.remove("") );
            	Assert.assertTrue(currentText.size() == comparisonText.size());
            	for(int i=0; i<currentText.size(); i++) {
            		Assert.assertEquals(currentText.get(i),comparisonText.get(i));
            	}
            	
            	log.info(">>>> End Test for the correct content of the last TextContentUpdate");
            }
    	}.run();
    	
    	/* Set back the TextContent to an empty xml document */
    	new FacesRequest() {			
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>>> Set back the TextContent to an empty xml document");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text0.xml");

				ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	
            	log.info(">>>> End Set back the TextContent to an empty xml document");
			}
			
		}.run();
	}
	
	/**
	 * This Test adds links to a TextContent xml 
	 * document and checks whether it is stored correctly
	 * @throws Exception
	 */
	@Test(dependsOnMethods = { "checkInit" })
	public void testAddingLinks() throws Exception {
		
		/*
		 * Tests whether the updateTextContent() method succeeds with an initial text
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info(">>> Load initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	Document xom1 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text1.xml");
            	
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom1);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	log.info(">>>> end Load initial TextContent");
            }
    	}.run();
    	
    	/*
		 * Tests whether the updateTextContent() method succeeds with an initial text
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Add word to a node in the initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");

            	Document xom2 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text_link.xml");
				
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom2);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	// if the last textcontent change produced an update, we increment sizeOfTextContentUpdates
            	if(qCountUpdates.getSingleResult().equals(sizeOfTextContentUpdates + 1L)) {
            		sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	}
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            }
    	}.run();
    	
    	/*
		 * Tests whether the update was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
        		
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Test for the correct amount of TextContentUpdates");
        		
            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            }
    	}.run();
    	
    	/*
		 * Tests whether the correct content was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Test for the correct content of TextContentUpdates");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	Assert.assertTrue(currentContentItem.getVersions().size() > 0);
            	TextContentUpdate tcu = currentContentItem.getVersions().get(
            			currentContentItem.getVersions().size()-1).getTextContentUpdate();
            	
            	Assert.assertNotNull(tcu.getChanges());
            	List<TextContentUpdateDiff> diffs = tcu.getChanges();
            	
            	// The TextContentUpdate should have 1 different nodes
            	Assert.assertEquals(diffs.size(), 1);

        		if(diffs.get(0).getXPath().equals("/*[local-name()='div'][1]/*[local-name()='p'][1]/*[local-name()='a'][1]")) {
        			diffs.get(0).getChangedWordsMap().containsKey("000000.000000");
        			diffs.get(0).getChangedWordsMap().containsKey("000001.000000");
        			diffs.get(0).getChangedWordsMap().containsKey("999999.000000");
        		}
            }
    	}.run();
    	
    	/* Set back the TextContent to an empty xml document */
    	new FacesRequest() {			
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Set back the TextContent to an empty xml document");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text0.xml");

				ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
			}
			
		}.run();
	}
	
	@Test(dependsOnMethods = { "checkInit" })
	public void testDeletingNodes() throws Exception {
		/*
		 * Tests whether the updateTextContent() method succeeds with an initial text
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Load initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	Document xom1 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text5.xml");
            	
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom1);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            }
    	}.run();
    	
    	/*
		 * Tests whether the updateTextContent() method succeeds with an initial text
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Add word to a node in the initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");

            	Document xom2 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text1.xml");
				
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom2);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	if(qCountUpdates.getSingleResult().equals(sizeOfTextContentUpdates + 1L)) {
            		sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	}
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            }
    	}.run();
    	
    	/*
		 * Tests whether the update was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
        		
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Test for the correct amount of TextContentUpdates");
        		
            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            }
    	}.run();
    	
    	/*
		 * Tests whether the correct content was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Test for the correct content of TextContentUpdates");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	Assert.assertTrue(currentContentItem.getVersions().size() > 0);
            	TextContentUpdate tcu = currentContentItem.getVersions().get(
            			currentContentItem.getVersions().size()-1).getTextContentUpdate();
            	
            	Assert.assertNotNull(tcu.getChanges());
            	List<TextContentUpdateDiff> diffs = tcu.getChanges();
            	
            	// The TextContentUpdate should have 3 different nodes
            	Assert.assertEquals(diffs.size(), 1);
            	
            	Assert.assertEquals(diffs.get(0).getXPath(),"/*[local-name()='div'][1]/*[local-name()='p'][1]");
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap().containsKey("000000.000001"));
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap().get("000000.000001"),"<p>");
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap().containsKey("000000.999992"));
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap().get("000000.999992"),"testAddedParagraphBefore");
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap().containsKey("000000.999993"));
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap().get("000000.999993"),"</p>");
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap().containsKey("000001.999994"));
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap().get("000001.999994"),"<b>");
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap().containsKey("000001.999995"));
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap().get("000001.999995"),"someBoldTestInHere");
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap().containsKey("000001.999996"));
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap().get("000001.999996"),"</b>");
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap().containsKey("999999.999997"));
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap().get("999999.999997"),"<p>");
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap().containsKey("999999.999998"));
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap().get("999999.999998"),"testAddedParagraphBehind");
            	Assert.assertTrue(diffs.get(0).getChangedWordsMap().containsKey("999999.999999"));
            	Assert.assertEquals(diffs.get(0).getChangedWordsMap().get("999999.999999"),"</p>");
            	// the content: /*[local-name()='div'][1]/*[local-name()='p'][2]/*[local-name()='b'][1]
            	
//            	
//            	Assert.assertEquals(diffs.get(0).getChangedWordsMap()
//            			.get("0"), "");
//            	Assert.assertEquals(diffs.get(0).getChangedWordsMap()
//            			.get("1"), "");
//            	Assert.assertEquals(diffs.get(0).getChangedWordsMap()
//            			.get("2"), "");
//            	
//            	// the content: div[1]/p[3]
//            	Assert.assertTrue(diffs.get(1).getChangedWordsMap()
//            			.containsKey("0"));
//            	Assert.assertTrue(diffs.get(1).getChangedWordsMap()
//            			.containsKey("1"));
//            	Assert.assertTrue(diffs.get(1).getChangedWordsMap()
//            			.containsKey("2"));
//            	
//            	Assert.assertEquals(diffs.get(1).getChangedWordsMap()
//            			.get("0"), "");
//            	Assert.assertEquals(diffs.get(1).getChangedWordsMap()
//            			.get("1"), "");
//            	Assert.assertEquals(diffs.get(1).getChangedWordsMap()
//            			.get("1"), "");
//            	
//            	// the content: div[1]/p[1]
//            	Assert.assertTrue(diffs.get(2).getChangedWordsMap()
//            			.containsKey("0"));
//            	Assert.assertTrue(diffs.get(2).getChangedWordsMap()
//            			.containsKey("1"));
//            	Assert.assertTrue(diffs.get(2).getChangedWordsMap()
//            			.containsKey("2"));
//            	
//            	Assert.assertEquals(diffs.get(2).getChangedWordsMap()
//            			.get("0"), "");
//            	Assert.assertEquals(diffs.get(2).getChangedWordsMap()
//            			.get("1"), "");
//            	Assert.assertEquals(diffs.get(2).getChangedWordsMap()
//            			.get("1"), "");
            		
            }
    	}.run();
    	
    	/* Set back the TextContent to an empty xml document */
    	new FacesRequest() {			
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Set back the TextContent to an empty xml document");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text0.xml");

				ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
			}
			
		}.run();
	}
	
	@Test(dependsOnMethods = { "checkInit" })
	public void testUndoOfAddedWords() throws Exception {
		
	}
	
	@Test(dependsOnMethods = { "checkInit" })
	public void testUndoOfDeletedWords() throws Exception {
		
	}
	
	@Test(dependsOnMethods = { "checkInit" })
	public void testUndoOfAddedNodes() throws Exception {
		
	}
	
	@Test(dependsOnMethods = { "checkInit" })
	public void testUndoOfDeletedNodes() throws Exception {
		
	}
	
	/**
	 * This Test a concrete bug as experienced by Marek
	 * @throws Exception
	 */
	@Test(dependsOnMethods = { "checkInit" })
	public void testLoremBug() throws Exception {
		
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Load initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	Document xom1 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/preTextLorem500.xml");
            	
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom1);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            }
    	}.run();
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Add word to a node in the initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");

            	Document xom2 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/postTextLorem500.xml");
				
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom2);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	// if the last textcontent change produced an update, we increment sizeOfTextContentUpdates
            	if(qCountUpdates.getSingleResult().equals(sizeOfTextContentUpdates + 1L)) {
            		sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	}
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            }
    	}.run();
    	
    	/*
		 * Tests whether the update was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
        		
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Test for the correct amount of TextContentUpdates");
        		
            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            }
    	}.run();
    	
    	/*
		 * Tests whether the correct content was stored in the DB
		 */
    	new FacesRequest() {
    		
            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Test for the correct content of TextContentUpdates");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	Assert.assertTrue(currentContentItem.getVersions().size() > 0);
            	TextContentUpdate tcu = currentContentItem.getVersions().get(
            			currentContentItem.getVersions().size()-1).getTextContentUpdate();
            	
            	Assert.assertNotNull(tcu.getChanges());
            	List<TextContentUpdateDiff> diffs = tcu.getChanges();
            	
            	// The TextContentUpdate should have 1 different nodes: /div[1]/p[1]
            	Assert.assertEquals(diffs.size(), 2);
            	
            	for(int i=0; i<2; i++) {
            		String xpath = diffs.get(i).getXPath();
            		if(xpath.equals("/*[local-name()='div'][1]/*[local-name()='p'][1]")) {
            			diffs.get(i).getChangedWordsMap().containsKey("000000.000000");
            			diffs.get(i).getChangedWordsMap().containsKey("999999.000000");
            		} else if(xpath.equals("/*[local-name()='div'][1]/*[local-name()='p'][2]")) {
            			diffs.get(i).getChangedWordsMap().containsKey("000001.000000");
            			diffs.get(i).getChangedWordsMap().containsKey("000002.000000");
            		} else {
            			Assert.fail("diff returned wrong xpath: " + xpath);
            		}
            	}
            }
    	}.run();
    	
    	new FacesRequest() {			
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Set back the TextContent to an empty xml document");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text0.xml");

				ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
			}
			
		}.run();
	}
	
	/**
	 * This Test a concrete bug as experienced by Marek
	 * @throws Exception
	 */
	@Test(dependsOnMethods = { "checkInit" })
	public void testMSBug() throws Exception {
		
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Load initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	Document xom1 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text_ms0_pre.xml");
            	
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom1);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            }
    	}.run();
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Identity.setSecurityEnabled(false);
            	
            	Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Add word to a node in the initial TextContent");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");

            	Document xom2 = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text_ms0_post.xml");
				
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom2);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	Query qCountUpdates = em.createQuery("select count(t) from TextContentUpdate t");
            	// if the last textcontent change produced an update, we increment sizeOfTextContentUpdates
            	if(qCountUpdates.getSingleResult().equals(sizeOfTextContentUpdates + 1L)) {
            		sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            	}
            	Assert.assertEquals(qCountUpdates.getSingleResult(),sizeOfTextContentUpdates);
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
            }
    	}.run();
    	
    	new FacesRequest() {			
			@Override
			protected void invokeApplication() {
				Log log = Logging.getLog(UpdateTextContentTest.class);
            	log.info("Set back the TextContent to an empty xml document");
            	
            	currentContentItem = (ContentItem) Component.getInstance("currentContentItem");

            	Document xom = UpdateTextContentSupport.getDocumentFromFile("xml_texts/text0.xml");

				ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	TextContent tc = new TextContent(currentContentItem);
            	tc.setXmlDocument(xom);
            	
            	cis.updateTextContentItem(currentContentItem, tc.getXmlString());
            	
            	sizeOfTextContentUpdates = sizeOfTextContentUpdates + 1L;
			}
			
		}.run();
	}
	
	/*protected void deleteTextContentUpdates() throws Exception {
		new FacesRequest() {
			@Override
			protected void invokeApplication() throws Exception {
				EntityManager em = (EntityManager) Component
						.getInstance("entityManager");
				em.clear();
				// delete all textcontentupdates
				Query q2 = em.createQuery("delete from TextContentUpdate c");
				q2.executeUpdate();
				// delete all textcontentupdatediffs
				Query q3 = em.createQuery("delete from TextContentUpdateDiff d");
				q3.executeUpdate();
			}
		}.run();
	}*/
	
	public static class UpdateTextContentSupport {
		public static Document getDocumentFromFile(String pathToFile) {
			Log log = Logging.getLog(UpdateTextContentTest.class);
			
			ConfigurationService configurationService = 
	    		(ConfigurationService) Component
				.getInstance("configurationService");
			
			InputStream in = null;
			in = new BufferedInputStream(
					UpdateTextContentTest.class
						.getResourceAsStream(pathToFile)
					);
			
			Assert.assertNotNull(in);
			Builder builder = new Builder();
			Document xom = null;
			try {
				xom = builder.build(in, configurationService.getBaseUri());
			} catch (ValidityException e) {
				log.error("Building document from InputStream failed (ValidityException): #0 ", e.getMessage());
				e.printStackTrace();
			} catch (ParsingException e) {
				log.error("Building document from InputStream failed (ParsingException): #0 ", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				log.error("Building document from InputStream failed (IOException): #0 ", e.getMessage());
				e.printStackTrace();
			} finally {
		    	try {
					in.close();
				} catch (IOException e) {
					log.error("Closing InputStream failed: #0 ", e.getMessage());
					e.printStackTrace();
				}
			}
			Assert.assertNotNull(xom);
			
			log.debug("Xom object created: #0 ", xom.toXML());
			
			return xom;
		}
	}
}
