/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 *
 */
package kiwi.test.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.content.ContentItemService;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiTriple;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Sebastian Schaffert
 *
 */
@Test
public class ContentItemServiceTest extends KiWiTest {

	@Test
	public void testCreateContentItem() throws Exception {
		/*
		 * Test the creation of a ContentItem using the ContentItemService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.createContentItem();

            	// check that ContentItem contains minimal information
            	Assert.assertNotNull(c);
            	Assert.assertNotNull(c.getResource());

            	cs.updateTitle(c, "MyContentItem");

            	cs.saveContentItem(c);

            	cs.updateTextContentItem(c, "<p>Hello World!</p>");

            	Assert.assertNotNull(c.getTextContent());
            	Assert.assertEquals(c.getTextContent().getHtmlContent(), "<p xmlns=\"http://www.w3.org/1999/xhtml\">Hello World!</p>");
             }
    	}.run();

		/*
		 * check whether number of content items equals number of resources (i.e. no dangling items)
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final EntityManager     em = (EntityManager) Component.getInstance("entityManager");

            	final Query qCountResources = em.createQuery("select count(r) from KiWiResource r");
            	final Query qCountItems = em.createQuery("select count(ci) from ContentItem ci");

             	Assert.assertEquals(qCountResources.getSingleResult(), qCountItems.getSingleResult());
             }
    	}.run();


		/*
		 * Test the querying of content items
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.getContentItemByTitle("MyContentItem");

            	// check that ContentItem contains all information
            	Assert.assertNotNull(c);
            	Assert.assertNotNull(c.getResource());
             	Assert.assertEquals(c.getTitle(), "MyContentItem");
             	Assert.assertNotNull(c.getTextContent());
            	Assert.assertEquals(c.getTextContent().getHtmlContent(), "<p xmlns=\"http://www.w3.org/1999/xhtml\">Hello World!</p>");
             }
    	}.run();

		/*
		 * Test the searching of content items
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	final Log log = Logging.getLog(ContentItemServiceTest.class);
            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final List<ContentItem> l = cs.searchContentItems("Hello", null, null);

            	for(final ContentItem i : l) {
            		log.info("Title: "+i.getTitle());
            	}


            	Assert.assertEquals(l.size(), 1);


            	final ContentItem c = l.get(0);

            	// check that ContentItem contains minimal information
            	Assert.assertNotNull(c);
            	Assert.assertNotNull(c.getResource());
             	Assert.assertEquals(c.getTitle(), "MyContentItem");
             	Assert.assertNotNull(c.getTextContent());
            	Assert.assertEquals(c.getTextContent().getHtmlContent(), "<p xmlns=\"http://www.w3.org/1999/xhtml\">Hello World!</p>");
             }
    	}.run();

		/*
		 * Test the removal of content items
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Logging.getLog(ContentItemServiceTest.class);
            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.getContentItemByTitle("MyContentItem");

            	cs.removeContentItem(c);
             }
    	}.run();

		/*
		 * Test the removal of content items
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Logging.getLog(ContentItemServiceTest.class);
            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.getContentItemByTitle("MyContentItem");

            	Assert.assertNull(c);
             }
    	}.run();

	}

	/**
	 * Test multiple editing of a ContentItem, adding links and removing them again (to test whether
	 * they are correctly identified and stored as triples).
	 * @throws Exception
	 */
	@Test
	public void testEditContentItem() throws Exception {

		/*
		 * First, create a Content Item
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.createContentItem();

            	// check that ContentItem contains minimal information
            	Assert.assertNotNull(c);
            	Assert.assertNotNull(c.getResource());

            	cs.updateTitle(c, "MyOtherContentItem");

            	cs.saveContentItem(c);

            	cs.updateTextContentItem(c, "<p>Lorem Ipsum!</p>");

            	Assert.assertNotNull(c.getTextContent());
            	Assert.assertEquals(c.getTextContent().getHtmlContent(), "<p xmlns=\"http://www.w3.org/1999/xhtml\">Lorem Ipsum!</p>");
             }
    	}.run();

		/*
		 * Update the content item and add a link
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.getContentItemByTitle("MyOtherContentItem");

            	cs.updateTextContentItem(c, "<p>[[Lorem]] Ipsum!</p>");
             }
    	}.run();

    	/*
    	 * There should now be exactly one relation of type kiwi:internalLink ...
    	 */
       	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.getContentItemByTitle("MyOtherContentItem");

            	Collection<KiWiTriple> l;
				try {
					l = c.getResource().listOutgoing(Constants.NS_KIWI_CORE+"internalLink");
				} catch (final NamespaceResolvingException e) {
					e.printStackTrace();
					l = Collections.emptySet();
				}

            	Assert.assertEquals(l.size(),1);
             }
    	}.run();

		/*
		 * Update the content item again and add another link
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.getContentItemByTitle("MyOtherContentItem");

            	cs.updateTextContentItem(c, "<p>[[Lorem]] [[Ipsum]]!</p>");
             }
    	}.run();

    	/*
    	 * There should now be exactly two relations of type kiwi:internalLink ...
    	 */
       	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	final Log log = Logging.getLog(this.getClass());

            	final ContentItemService cs = (ContentItemService) Component.getInstance("contentItemService");

            	final ContentItem c = cs.getContentItemByTitle("MyOtherContentItem");

            	Collection<KiWiTriple> l;
				try {
					l = c.getResource().listOutgoing(Constants.NS_KIWI_CORE+"internalLink");
					log.info("internal link: #0 " ,l);
				} catch (final NamespaceResolvingException e) {
					e.printStackTrace();
					l = Collections.emptySet();
				}

            	Assert.assertEquals(l.size(),2);
             }
    	}.run();

	}
}
