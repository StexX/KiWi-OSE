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

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import junit.framework.Assert;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.group.GroupService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.GroupExistsException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.facades.PointOfInterestFacade;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.user.Group;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Identity;
import org.testng.annotations.Test;




/**
 * @author Sebastian Schaffert
 *
 */
@Test
public class KiWiEntityManagerTest extends KiWiTest {
	
	@Test
	public void testKiWiEntityManager() throws Exception {
		Logger  log_hib = Logger.getLogger("org.hibernate");
		Logger  log_ses = Logger.getLogger("org.openrdf");

		log_hib.setLevel(Level.INFO);
		log_ses.setLevel(Level.INFO);
		 
    	
    	String[] ontologies = {
    			"ontology_kiwi.owl",
    			"tagit/tagit.owl",
    			"imports/foaf.owl",
    			"imports/geo.rdf",
    			"imports/sioc.owl",
    			"imports/hgtags.owl",
    			"imports/skos-core.rdf",
    	};
    	setupDatabase(ontologies);
		
		/*
		 * Check whether everything has been loaded successfully by looking for a few ContentItems
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Log log = Logging.getLog(this.getClass());
            	Identity.setSecurityEnabled(false);
            	
            	KiWiEntityManager km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	EntityManager     em = (EntityManager) Component.getInstance("entityManager");
            	TripleStore       ts = (TripleStore)       Component.getInstance("tripleStore");
            	
            	// check whether DB count of triples is same as TripleStore count
            	Query qCountTriples = em.createQuery("select count(t) from KiWiTriple t where t.deleted = false"); 
            	log.info("count is #0 ", qCountTriples.getSingleResult());
            	Assert.assertEquals(ts.size(), qCountTriples.getSingleResult());
            	
            	Query q = km.createQuery("SELECT ?S WHERE { ?S <"+Constants.NS_RDF+"type> <"+Constants.NS_SKOS+"Concept> }", KiWiQueryLanguage.SPARQL, ContentItem.class);
            	q.setMaxResults(10);
            	List<ContentItem> cis = q.getResultList();
            	for(ContentItem ci : cis) {
            		log.info("found content item with resource "+ci.getResource().toString());
            		for(KiWiResource type : ci.getResource().getTypes()) {
            			log.info("DBG: Type "+type.toString());
            		}
            		Assert.assertTrue(ci.getResource().hasType(ts.createUriResource(Constants.NS_SKOS+"Concept")));
            	}
            }
    	}.run();
		
		/*
		 * Test the kiwiEntityManager persist interface by storing a new PointOfInterestFacade
		 * with a comment.
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Log log = Logging.getLog(this.getClass());
            	log.debug(">>>>>>>>>>>>>>> testKiWiEntityManager 2");
            	Identity.setSecurityEnabled(false);
            	
            	KiWiEntityManager km   = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	TripleStore       ts   = (TripleStore) Component.getInstance("tripleStore");
            	User              user = (User) Component.getInstance("currentUser");
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	
            	PointOfInterestFacade poi = km.createFacade(cis.createContentItem(), PointOfInterestFacade.class);

            	poi.setAuthor(user);
            	cis.updateTitle(poi, "PointOfInterest");

            	cis.updateTextContentItem(poi, "<p>Lorem ipsum dolor sit amet.</p>");
            	
            	Assert.assertEquals(poi.getTextContent().getXmlString(), "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\"http://www.kiwi-project.eu/kiwi/html/\" kiwi:type=\"page\"><p>Lorem ipsum dolor sit amet.</p></div>");

            	poi.setLatitude(47.8);
            	poi.setLongitude(13.03);

            	ContentItem comment1 = cis.createContentItem();
            	comment1.setAuthor(user);
            	
            	cis.updateTextContentItem(comment1, "<p>comment 1</p>");
            	cis.updateTitle(comment1, "Comment");
            	
            	km.persist(comment1);
            	
            	LinkedList<ContentItem> comments = new LinkedList<ContentItem>();
            	comments.add(comment1);
            	poi.setComments(comments);

            	km.persist(poi);

            }
    	}.run();
		
    	/*
		 * Test whether the kiwiEntityManager persist a normal entity, which is noch
		 * of the type "kiwiEntity"
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>>>>>>> testKiWiEntityManager 2.5");
            	Identity.setSecurityEnabled(false);
            	
            	KiWiEntityManager km   = (KiWiEntityManager) Component.
            		getInstance("kiwiEntityManager");
            	EntityManager em   = (EntityManager) Component.getInstance("entityManager");
            	GroupService gs = (GroupService) Component.getInstance("groupService");
            	ContentItemService cis = 
					(ContentItemService) Component.getInstance("contentItemService");
            	
            	Group g = null;
				try {
					g = gs.createGroup("testGroup");
					km.persist(g);
					km.flush();
				} catch (GroupExistsException e) {
					e.printStackTrace();
				}

				Assert.assertNotNull(g);
				Query q = em.createNamedQuery("groupService.getGroupByName");
				q.setParameter("name", "testGroup");
				try {
					Group g_assert = (Group) q.getSingleResult();
					Assert.assertEquals(g.getName(), g_assert.getName());
				} catch (NoResultException e) {
					e.printStackTrace();
				}
				
				log.info(">>>>>>>>>>>>>>> testKiWiEntityManager 2.5 end");
            }
    	}.run();
    	
		
		/*
		 * Test the kiwiEntityManager query interface by looking for a point of interest
		 * first as a pure ContentItem, then as a PointOfInterestFacade.
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Log log = Logging.getLog(this.getClass());
            	log.debug(">>>>>>>>>>>>>>> testKiWiEntityManager 3");
            	Identity.setSecurityEnabled(false);
            	
            	KiWiEntityManager km   = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");

            	// get a content item from the kiwiEntityManager
            	Query q1 = km.createQuery("SELECT ?S WHERE { ?S <"+Constants.NS_RDF+"type> <"+Constants.NS_TAGIT+"PointOfInterest> }", KiWiQueryLanguage.SPARQL, ContentItem.class);
            	
            	List<ContentItem> cis = q1.getResultList();
            	
            	Assert.assertEquals(1, cis.size());
            	
            	ContentItem ci = cis.get(0);
            	
            	Assert.assertEquals("PointOfInterest", ci.getTitle());
            	Assert.assertEquals(ci.getTextContent().getXmlString(), "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\"http://www.kiwi-project.eu/kiwi/html/\" kiwi:type=\"page\"><p>Lorem ipsum dolor sit amet.</p></div>");
            	
            	Query q2 = km.createQuery("SELECT ?S WHERE { ?S <"+Constants.NS_RDF+"type> <"+Constants.NS_TAGIT+"PointOfInterest> }", KiWiQueryLanguage.SPARQL, PointOfInterestFacade.class);
            	
            	List<PointOfInterestFacade> pois = q2.getResultList();
            	
            	Assert.assertEquals(1, pois.size());
            	
            	PointOfInterestFacade poi = pois.get(0);
            	
            	Assert.assertEquals("PointOfInterest", poi.getTitle());
            	Assert.assertEquals(poi.getTextContent().getXmlString(), "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\"http://www.kiwi-project.eu/kiwi/html/\" kiwi:type=\"page\"><p>Lorem ipsum dolor sit amet.</p></div>");
            	Assert.assertEquals(47.8, poi.getLatitude(), 0.01);
            	Assert.assertEquals(13.03, poi.getLongitude(), 0.01);
            	Assert.assertEquals(1, poi.getComments().size());
            	Assert.assertEquals(ci.getResource(), poi.getResource());
            	
            	ContentItem comment = poi.getComments().get(0);
            	
            	Assert.assertEquals("Comment",comment.getTitle());
            	Assert.assertEquals(comment.getTextContent().getXmlString(), "<div xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:kiwi=\"http://www.kiwi-project.eu/kiwi/html/\" kiwi:type=\"page\"><p>comment 1</p></div>");
            }
    	}.run();
		
		
    	/*
    	 * Now we verify the functioning of the fulltext search functionality by searching for "lorem" and
    	 * checking whether this again returns the right item.
    	 */
    	
    	
    	/*
    	 * TODO: need to check transactions, refresh, merge, ... 
    	 */
		shutdownDatabase();
	}
	
	
	

}
