/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2008-2009, The KiWi Project (http://www.kiwi-project.eu)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * - Neither the name of the KiWi Project nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Contributor(s):
 * sschaffe
 *
 */
package kiwi.test.service.search;

import java.util.Collections;

import kiwi.api.content.ContentItemService;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.SolrService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.apache.solr.client.solrj.SolrQuery;
import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * SolrServiceTest
 *
 * @author Sebastian Schaffert
 *
 */
@Test
public class SolrServiceTest extends KiWiTest {

	@Test
	public void testSearchType() throws Exception {

		/*
		 * Create two content items, one with type http://www.example.com/type1, one with http://www.example.com/type2
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	final TripleStore        ts = (TripleStore)        Component.getInstance("tripleStore");

            	final ContentItem c1 = cs.createTextContentItem("<p>Hello World!</p>");
            	cs.updateTitle(c1, "A ContentItem 1");
            	cs.saveContentItem(c1);
            	c1.addType(ts.createUriResource("http://www.example.com/type1"));

				final ContentItem c2 = cs.createTextContentItem("<p>Hallo Welt!</p>");
            	cs.updateTitle(c2, "A ContentItem 2");
            	cs.saveContentItem(c2);
            	c2.addType(ts.createUriResource("http://www.example.com/type2"));
              }
    	}.run();

		/*
		 * Check that the items have been created successfully and that they have the correct types
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	final TripleStore        ts = (TripleStore)        Component.getInstance("tripleStore");

            	final ContentItem c1 = cs.getContentItemByTitle("A ContentItem 1");
            	final ContentItem c2 = cs.getContentItemByTitle("A ContentItem 2");

            	Assert.assertNotNull(c1);
            	Assert.assertTrue(c1.getResource().hasType(ts.createUriResource("http://www.example.com/type1")));

               	Assert.assertNotNull(c2);
            	Assert.assertTrue(c2.getResource().hasType(ts.createUriResource("http://www.example.com/type2")));
            }
    	}.run();


		/*
		 * Check that the search with criterion "type" returns the correct results
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	Component.getInstance("contentItemService");
            	final SolrService        ss = (SolrService) Component.getInstance("solrService");
            	final TripleStore        ts = (TripleStore)        Component.getInstance("tripleStore");

            	final KiWiSearchCriteria crit1 = new KiWiSearchCriteria();
            	crit1.getTypes().add(ts.createUriResource("http://www.example.com/type1").getKiwiIdentifier());

            	final KiWiSearchResults result1 = ss.search(crit1);

            	Assert.assertEquals(result1.getResultCount(), 1);

            	final ContentItem c1 = result1.getResults().get(0).getItem();

            	Assert.assertEquals(c1.getTitle(), "A ContentItem 1");

             }
    	}.run();


	}

	@Test
	public void testSearchRDFLiteral() throws Exception {

		/*
		 * Create two content items, and set property http://www.example.com/p1 to VALUE1 and VALUE2
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	Component.getInstance("tripleStore");

            	final ContentItem c1 = cs.createTextContentItem("<p>Hello World!</p>");
            	cs.updateTitle(c1, "B ContentItem 1");
            	cs.saveContentItem(c1);
            	c1.getResource().setProperty("<http://www.example.com/p1>", "VALUE1");

				final ContentItem c2 = cs.createTextContentItem("<p>Hallo Welt!</p>");
            	cs.updateTitle(c2, "B ContentItem 2");
            	cs.saveContentItem(c2);
            	c2.getResource().setProperty("<http://www.example.com/p1>", "VALUE2");
              }
    	}.run();

		/*
		 * Check that the items have been created successfully and that they have the correct types
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	Component.getInstance("tripleStore");

            	final ContentItem c1 = cs.getContentItemByTitle("B ContentItem 1");
            	final ContentItem c2 = cs.getContentItemByTitle("B ContentItem 2");

            	Assert.assertNotNull(c1);
            	Assert.assertEquals(c1.getResource().getProperty("<http://www.example.com/p1>"),"VALUE1");

               	Assert.assertNotNull(c2);
               	Assert.assertEquals(c2.getResource().getProperty("<http://www.example.com/p1>"),"VALUE2");
            }
    	}.run();


		/*
		 * Check that the search with criterion "type" returns the correct results
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	Component.getInstance("contentItemService");
            	final SolrService        ss = (SolrService) Component.getInstance("solrService");
            	Component.getInstance("tripleStore");

            	final KiWiSearchCriteria crit1 = new KiWiSearchCriteria();
            	crit1.getRdfLiteralProperties().put("http://www.example.com/p1", Collections.singleton("VALUE1"));

            	final KiWiSearchResults result1 = ss.search(crit1);

            	Assert.assertEquals(result1.getResultCount(), 1);

            	final ContentItem c1 = result1.getResults().get(0).getItem();

            	Assert.assertEquals(c1.getTitle(), "B ContentItem 1");

             }
    	}.run();


	}


	@Test
	public void testSearchRDFInteger() throws Exception {

		/*
		 * Create two content items, and set property http://www.example.com/p1 to "1" and "2"
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	Component.getInstance("tripleStore");

            	final ContentItem c1 = cs.createTextContentItem("<p>Hello World!</p>");
            	cs.updateTitle(c1, "C ContentItem 1");
            	cs.saveContentItem(c1);
            	c1.getResource().setProperty("<http://www.example.com/p1>", "1");

				final ContentItem c2 = cs.createTextContentItem("<p>Hallo Welt!</p>");
            	cs.updateTitle(c2, "C ContentItem 2");
            	cs.saveContentItem(c2);
            	c2.getResource().setProperty("<http://www.example.com/p1>", "3");
              }
    	}.run();

		/*
		 * Check that the items have been created successfully and that they have the correct types
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	Component.getInstance("tripleStore");

            	final ContentItem c1 = cs.getContentItemByTitle("C ContentItem 1");
            	final ContentItem c2 = cs.getContentItemByTitle("C ContentItem 2");

            	Assert.assertNotNull(c1);
            	Assert.assertEquals(c1.getResource().getProperty("<http://www.example.com/p1>"),"1");

               	Assert.assertNotNull(c2);
               	Assert.assertEquals(c2.getResource().getProperty("<http://www.example.com/p1>"),"3");
            }
    	}.run();


		/*
		 * Check that the search with criterion "type" returns the correct results for point query
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	Component.getInstance("contentItemService");
            	final SolrService        ss = (SolrService) Component.getInstance("solrService");
            	Component.getInstance("tripleStore");

            	final KiWiSearchCriteria crit1 = new KiWiSearchCriteria();
            	crit1.getRdfIntegerProperties().put("http://www.example.com/p1", Collections.singleton("1"));

            	final KiWiSearchResults result1 = ss.search(crit1);

            	Assert.assertEquals(result1.getResultCount(), 1);

            	final ContentItem c1 = result1.getResults().get(0).getItem();

            	Assert.assertEquals(c1.getTitle(), "C ContentItem 1");

             }
    	}.run();


		/*
		 * Check that the search with criterion "type" returns the correct results for a range query
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	Component.getInstance("contentItemService");
            	final SolrService        ss = (SolrService) Component.getInstance("solrService");
            	Component.getInstance("tripleStore");

            	final KiWiSearchCriteria crit1 = new KiWiSearchCriteria();
            	crit1.getRdfIntegerProperties().put("http://www.example.com/p1", Collections.singleton("[2 TO 4]"));

            	final KiWiSearchResults result1 = ss.search(crit1);

            	Assert.assertEquals(result1.getResultCount(), 1);

            	final ContentItem c1 = result1.getResults().get(0).getItem();

            	Assert.assertEquals(c1.getTitle(), "C ContentItem 2");

             }
    	}.run();
	}

	@Test
	public void testSearchRDFDouble() throws Exception {

		/*
		 * Create two content items, and set property http://www.example.com/p1 to "1" and "2"
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	Component.getInstance("tripleStore");

            	final ContentItem c1 = cs.createTextContentItem("<p>Hello World!</p>");
            	cs.updateTitle(c1, "D ContentItem 1");
            	cs.saveContentItem(c1);
            	c1.getResource().setProperty("<http://www.example.com/p2>", "1.1");

				final ContentItem c2 = cs.createTextContentItem("<p>Hallo Welt!</p>");
            	cs.updateTitle(c2, "D ContentItem 2");
            	cs.saveContentItem(c2);
            	c2.getResource().setProperty("<http://www.example.com/p2>", "3.3");
              }
    	}.run();

		/*
		 * Check that the items have been created successfully and that they have the correct types
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	Component.getInstance("tripleStore");

            	final ContentItem c1 = cs.getContentItemByTitle("D ContentItem 1");
            	final ContentItem c2 = cs.getContentItemByTitle("D ContentItem 2");

            	Assert.assertNotNull(c1);
            	Assert.assertEquals(c1.getResource().getProperty("<http://www.example.com/p2>"),"1.1");

               	Assert.assertNotNull(c2);
               	Assert.assertEquals(c2.getResource().getProperty("<http://www.example.com/p2>"),"3.3");
            }
    	}.run();


		/*
		 * Check that the search with criterion "type" returns the correct results for point query
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	Component.getInstance("contentItemService");
            	final SolrService        ss = (SolrService) Component.getInstance("solrService");
            	Component.getInstance("tripleStore");

            	final KiWiSearchCriteria crit1 = new KiWiSearchCriteria();
            	crit1.getRdfDoubleProperties().put("http://www.example.com/p2", Collections.singleton("1.1"));

            	final KiWiSearchResults result1 = ss.search(crit1);

            	Assert.assertEquals(result1.getResultCount(), 1);

            	final ContentItem c1 = result1.getResults().get(0).getItem();

            	Assert.assertEquals(c1.getTitle(), "D ContentItem 1");

             }
    	}.run();


		/*
		 * Check that the search with criterion "type" returns the correct results for a range query
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	Component.getInstance("contentItemService");
            	final SolrService        ss = (SolrService) Component.getInstance("solrService");
            	Component.getInstance("tripleStore");

            	final KiWiSearchCriteria crit1 = new KiWiSearchCriteria();
            	crit1.getRdfDoubleProperties().put("http://www.example.com/p2", Collections.singleton("[2 TO 4]"));

            	final KiWiSearchResults result1 = ss.search(crit1);

            	Assert.assertEquals(result1.getResultCount(), 1);

            	final ContentItem c1 = result1.getResults().get(0).getItem();

            	Assert.assertEquals(c1.getTitle(), "D ContentItem 2");

             }
    	}.run();
	}

	@Test
	public void testSearchKeyword() throws Exception {
		/*
		 * Create a ContentItem using the ContentItemService and save it so that it gets indexed by solr
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	final TaggingService     ts = (TaggingService) Component.getInstance("taggingService");

            	final ContentItem c1 = cs.createContentItem();

            	// check that ContentItem contains minimal information
            	Assert.assertNotNull(c1);
            	Assert.assertNotNull(c1.getResource());

            	cs.updateTitle(c1, "MyContentItem");

            	cs.saveContentItem(c1);

            	cs.updateTextContentItem(c1, "<p>Hello World!</p>");


            	Assert.assertNotNull(c1.getTextContent());
            	Assert.assertEquals(c1.getTextContent().getHtmlContent(), "<p xmlns=\"http://www.w3.org/1999/xhtml\">Hello World!</p>");

            	// create another content item
            	final ContentItem c2 = cs.createContentItem();

            	cs.updateTitle(c2, "MyOtherContentItem");

            	cs.saveContentItem(c2);

            	cs.updateTextContentItem(c2, "<p>Blah Blub!</p>");

            	Assert.assertNotNull(c2.getTextContent());
            	Assert.assertEquals(c2.getTextContent().getHtmlContent(), "<p xmlns=\"http://www.w3.org/1999/xhtml\">Blah Blub!</p>");

            	final ContentItem tag1 = cs.createContentItem();
            	cs.updateTitle(tag1, "TagOne");

            	ts.createTagging("TagOne", c1, tag1, (User)Component.getInstance("currentUser"));
            }
    	}.run();

		/*
		 * Check that the items have been created successfully and that they have the correct types
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");
            	Component.getInstance("tripleStore");
            	final TaggingService     tg = (TaggingService)     Component.getInstance("taggingService");

            	final ContentItem c1 = cs.getContentItemByTitle("MyContentItem");
            	final ContentItem c2 = cs.getContentItemByTitle("MyOtherContentItem");

            	Assert.assertNotNull(c1);
               	Assert.assertNotNull(c2);

               	Assert.assertTrue(tg.hasTag(c1, "TagOne"));
             }
    	}.run();

		/*
		 * Search for the content item by running a full-text search for "blub"
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final SolrService solr = (SolrService) Component.getInstance("solrService");

            	final KiWiSearchCriteria c = new KiWiSearchCriteria();
            	c.setKeywords("blub");

            	final KiWiSearchResults r = solr.search(c);
            	Assert.assertTrue(r.getResultCount() >= 1L);

            	final ContentItem result = r.getResults().get(0).getItem();
            	Assert.assertEquals(result.getTitle(), "MyOtherContentItem");
             }
    	}.run();


		/*
		 * Search for the content item by running a full-text search for "MyOtherContentItem" (testing title search)
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final SolrService solr = (SolrService) Component.getInstance("solrService");

            	final KiWiSearchCriteria c = new KiWiSearchCriteria();
            	c.setKeywords("MyOtherContentItem");

            	final KiWiSearchResults r = solr.search(c);
            	Assert.assertTrue(r.getResultCount() >= 1L);

            	final ContentItem result = r.getResults().get(0).getItem();
            	Assert.assertEquals(result.getTitle(), "MyOtherContentItem");
             }
    	}.run();

		/*
		 * Search for the content item by running a full-text search for "MyOtherContentItem" (testing title search case insensitive)
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final SolrService solr = (SolrService) Component.getInstance("solrService");

            	final KiWiSearchCriteria c = new KiWiSearchCriteria();
            	c.setKeywords("myothercontentitem");

            	final KiWiSearchResults r = solr.search(c);
            	Assert.assertTrue(r.getResultCount() >= 1L);

            	final ContentItem result = r.getResults().get(0).getItem();
            	Assert.assertEquals(result.getTitle(), "MyOtherContentItem");
             }
    	}.run();

		/*
		 * Search for the content item by running a full-text search for "Tag1" (testing tag search)
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final SolrService solr = (SolrService) Component.getInstance("solrService");

            	final KiWiSearchCriteria c = new KiWiSearchCriteria();
            	c.setKeywords("TagOne");

            	final KiWiSearchResults r = solr.search(c);
            	Assert.assertTrue(r.getResultCount() >= 1L);

            	// items tagged with Tag1 should be first

            	boolean found = false; int i;
            	for(i = 0; i< r.getResultCount(); i++) {
            		final ContentItem result = r.getResults().get(i).getItem();
            		if(result.getTitle().equals("MyContentItem")) {
            			found = true;
            			break;
            		}
            	}
            	Assert.assertTrue(found);

            	// TODO: result ranking is not correct
            	//Assert.assertEquals(i, 0);
             }
    	}.run();
	}

	@Test
	public void testBuildSolrQueryKeywordEmpty() throws Exception {
		/*
		 * Create search string for a full-text search for "keyword1"
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final SolrService solr = (SolrService) Component.getInstance("solrService");

            	final KiWiSearchCriteria c = new KiWiSearchCriteria();
            	c.setKeywords("");

            	final SolrQuery q = solr.buildSolrQuery(c);

            	Assert.assertEquals(q.getQuery(),"");
             }
    	}.run();



	}

	@Test
	public void testBuildSolrQueryKeyword() throws Exception {
		/*
		 * Create search string for a full-text search for "keyword1"
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final SolrService solr = (SolrService) Component.getInstance("solrService");

            	final KiWiSearchCriteria c = new KiWiSearchCriteria();
            	c.setKeywords("keyword1");

            	final SolrQuery q = solr.buildSolrQuery(c);

            	Assert.assertEquals(q.getQuery(),
//	            "(content:keyword1 OR author:keyword1^2 OR title:keyword1 OR author_login:keyword1^2 OR tag:keyword1^8 OR purpose:keyword1)");
            	"(+l_34c58b3b44badd92481ed7368629d428:keyword1 OR tag:keyword1^8 OR +l_3e3b10d47a3c0d5e38c3c43e6e206de4:keyword1 OR purpose:keyword1 OR +l_8c1a059ce3017802fa3930af2044949f:keyword1 OR content:keyword1 OR author:keyword1^2 OR title:keyword1 OR +l_7858186c3a78c025317325c4c60c1f7c:keyword1 OR author_login:keyword1^2 OR +l_812ec1cc4f48c219250986752705def9:keyword1 OR +l_42d8e6d4759109ca4d2eb433480adac1:keyword1 OR +l_7fe44f311783c64eedb03944ca30df52:keyword1)");
             }
    	}.run();



	}

	@Test
	public void testParseSearchString() throws Exception {

		/*
		 * simple keyword search
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final SolrService solr = (SolrService) Component.getInstance("solrService");

            	final KiWiSearchCriteria c1 = solr.parseSearchString("keyword1");
            	Assert.assertEquals(c1.getKeywords(), "keyword1");

            	final KiWiSearchCriteria c2 = solr.parseSearchString("keyword1 keyword2");
            	Assert.assertEquals(c2.getKeywords(), "keyword1 keyword2");
             }
    	}.run();

		/*
		 * keyword and tag search
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final SolrService solr = (SolrService) Component.getInstance("solrService");

            	final KiWiSearchCriteria c1 = solr.parseSearchString("keyword1 tag:tag1");
            	Assert.assertEquals(c1.getKeywords(), "keyword1");
            	Assert.assertTrue(c1.getTags().contains("tag1"));

            	final KiWiSearchCriteria c2 = solr.parseSearchString("keyword1 tag:tag1 tag:tag2");
            	Assert.assertEquals(c2.getKeywords(), "keyword1");
            	Assert.assertTrue(c2.getTags().contains("tag1"));
            	Assert.assertTrue(c2.getTags().contains("tag2"));
             }
    	}.run();

		/*
		 * rdf search
		 */

    	// first create a namespace
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final TripleStore ts = (TripleStore) Component.getInstance("tripleStore");

            	ts.setNamespace("ex", "http://www.example.com/");
             }
    	}.run();

    	// then use it in search
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final SolrService solr = (SolrService) Component.getInstance("solrService");

            	final KiWiSearchCriteria c1 = solr.parseSearchString("keyword1 ex:p1:val1");
            	Assert.assertEquals(c1.getKeywords(), "keyword1");
            	Assert.assertTrue(c1.getRdfLiteralProperties().get("http://www.example.com/p1").contains("val1"));

            	final KiWiSearchCriteria c2 = solr.parseSearchString("keyword1 ex:p1:val1 ex:p2:1 ex:p3:3.3");
            	Assert.assertEquals(c2.getKeywords(), "keyword1");
            	Assert.assertTrue(c2.getRdfLiteralProperties().get("http://www.example.com/p1").contains("val1"));
            	Assert.assertTrue(c2.getRdfLiteralProperties().get("http://www.example.com/p2").contains("1"));
            	//Assert.assertTrue(c2.getRdfIntegerProperties().get("http://www.example.com/p2").contains("1"));
            	Assert.assertTrue(c2.getRdfLiteralProperties().get("http://www.example.com/p3").contains("3.3"));
            	//Assert.assertTrue(c2.getRdfDoubleProperties().get("http://www.example.com/p3").contains("3.3"));
             }
    	}.run();

	}


}
