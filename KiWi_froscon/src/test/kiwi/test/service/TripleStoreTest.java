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

import java.util.Iterator;
import java.util.LinkedList;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.Assert;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.exception.NonUniqueRelationException;
import kiwi.model.Constants;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.testng.annotations.Test;

/**
 * Test the functionality of the TripleStoreImpl class
 *
 * @author Sebastian Schaffert
 *
 */
@Test
public class TripleStoreTest extends KiWiTest {


	@Test
	public void testNamespaces() throws Exception {

		/*
		 * Check whether we can create namespaces and they are cached properly
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	Assert.assertNotNull(em);
            	ts.setNamespace("ex","http://www.example.com/");

            	final KiWiNamespace ns = ts.getNamespace("ex");

            	Assert.assertNotNull(ns);
            	Assert.assertEquals("http://www.example.com/", ns.getUri());


            	Assert.assertEquals(0L, em.createQuery("select count(ns) from KiWiNamespace ns where ns.deleted = false").getSingleResult());

            }
    	}.run();

		/*
		 * Check whether namespace has been persisted properly
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	em.createNamedQuery("tripleStore.listNamespaces");
            	Assert.assertNotNull(em);
            	final Query q = em.createQuery("select count(ns) from KiWiNamespace ns where ns.deleted = false");
            	Assert.assertNotNull(q);

            	Assert.assertEquals(1L, em.createQuery("select count(ns) from KiWiNamespace ns where ns.deleted = false").getSingleResult());

            	final KiWiNamespace ns = ts.getNamespace("ex");
            	Assert.assertEquals("http://www.example.com/", ns.getUri());

            }
    	}.run();

		/*
		 * Check whether we can remove namespaces and they are removed from cache
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	ts.removeNamespace("ex");

            	// transaction not yet committed, so still in database
            	Assert.assertEquals(1L, em.createQuery("select count(ns) from KiWiNamespace ns where ns.deleted = false").getSingleResult());

            }
    	}.run();

		/*
		 * Check whether namespace has been removed from database in last transaction commit
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	Assert.assertNull(ts.getNamespace("ex"));
            	Assert.assertEquals(0L, em.createQuery("select count(ns) from KiWiNamespace ns where ns.deleted = false").getSingleResult());

            }
    	}.run();

    	shutdownDatabase();
	}

	@Test
	public void testTripleStore() throws Exception {
		//setupDatabase();
		clearDatabase();

		/*
		 * Check whether we can create nodes and triples.
		 */
    	new FacesRequest() {

            // load the h2 stored procedures

            @Override
            protected void invokeApplication() {
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	final KiWiUriResource subject  = ts.createUriResource(Constants.NS_KIWI_CORE + "ContentItem");
            	final KiWiUriResource property1 = ts.createUriResource(Constants.NS_RDF + "type");
            	final KiWiUriResource object1   = ts.createUriResource(Constants.NS_OWL + "Class");
            	final KiWiUriResource property2 = ts.createUriResource(Constants.NS_RDFS + "subClassOf");
            	final KiWiUriResource object2   = ts.createUriResource(Constants.NS_RDFS + "Resource");

            	Assert.assertTrue(subject.getId() != null);
            	Assert.assertTrue(property1.getId() != null);
            	Assert.assertTrue(object1.getId() != null);
            	Assert.assertTrue(property2.getId() != null);
            	Assert.assertTrue(object2.getId() != null);

            	ts.createTriple(subject, property1, object1);
            	ts.createTriple(subject, property2, object2);

            	// check proper caching of triples; transaction has not yet been committed!
            	Assert.assertTrue(ts.hasTriple(subject, property1, object1));
            	Assert.assertTrue(ts.hasTriple(subject, property2, object2));

            	// check that the triples have not yet been persisted to database because
            	Assert.assertEquals(0L, em.createQuery("select count(t) from KiWiTriple t where t.deleted = false").getSingleResult());

            }
    	}.run();

		/*
		 * Check whether we triples have been persisted after completion of transaction
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	Assert.assertEquals(2L, em.createQuery("select count(t) from KiWiTriple t where t.deleted = false").getSingleResult());

            	final KiWiUriResource subject  = ts.createUriResource(Constants.NS_KIWI_CORE + "ContentItem");
            	final KiWiUriResource property1 = ts.createUriResource(Constants.NS_RDF + "type");
            	final KiWiUriResource object1   = ts.createUriResource(Constants.NS_OWL + "Class");
            	final KiWiUriResource property2 = ts.createUriResource(Constants.NS_RDFS + "subClassOf");
            	final KiWiUriResource object2   = ts.createUriResource(Constants.NS_RDFS + "Resource");

            	// check proper querying of triples
            	Assert.assertTrue(ts.hasTriple(subject, property1, object1));
            	Assert.assertTrue(ts.hasTriple(subject, property2, object2));

            }
    	}.run();


		/*
		 * Check whether we can remove triples again
		 * TODO: does it make sense that hasTriple still yields true after removal?
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	final KiWiUriResource subject  = ts.createUriResource(Constants.NS_KIWI_CORE + "ContentItem");
            	final KiWiUriResource property1 = ts.createUriResource(Constants.NS_RDF + "type");
            	final KiWiUriResource object1   = ts.createUriResource(Constants.NS_OWL + "Class");
            	final KiWiUriResource property2 = ts.createUriResource(Constants.NS_RDFS + "subClassOf");
            	final KiWiUriResource object2   = ts.createUriResource(Constants.NS_RDFS + "Resource");

            	ts.removeTriple(subject, property1, object1);
            	ts.removeTriple(subject, property2, object2);

            	// still in database, since transaction not yet committed; count should be 2
            	Assert.assertEquals(2L, em.createQuery("select count(t) from KiWiTriple t where t.deleted = false").getSingleResult());


            	// triple store should yield false when looking for triples
            	//Assert.assertFalse(ts.hasTriple(subject, property1, object1));
            	//Assert.assertFalse(ts.hasTriple(subject, property2, object2));
            }
    	}.run();


		/*
		 * Check whether triples have actually been removed from database
		 * TODO: should orphan nodes be deleted as well?
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	final KiWiUriResource subject  = ts.createUriResource(Constants.NS_KIWI_CORE + "ContentItem");
            	final KiWiUriResource property1 = ts.createUriResource(Constants.NS_RDF + "type");
            	final KiWiUriResource object1   = ts.createUriResource(Constants.NS_OWL + "Class");
            	final KiWiUriResource property2 = ts.createUriResource(Constants.NS_RDFS + "subClassOf");
            	final KiWiUriResource object2   = ts.createUriResource(Constants.NS_RDFS + "Resource");

            	// I do not understand this comment...it doesn't match with the Assert call below. The assert fails with 0L, but not with 2L (Steffi)
            	// still in database, since transaction not yet committed; count should be 2
            	Assert.assertEquals(0L, em.createQuery("select count(t) from KiWiTriple t where t.deleted = false").getSingleResult());


            	// triple store should yield false when looking for triples
            	Assert.assertFalse(ts.hasTriple(subject, property1, object1));
            	Assert.assertFalse(ts.hasTriple(subject, property2, object2));
            }
    	}.run();


    	final String MY_NS = "http://www.example.com/myns/";

		/*
		 * Check creating and removing triples
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final TripleStore ts = (TripleStore) Component.getInstance("tripleStore");

            	final KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");
            	final KiWiUriResource property = ts.createUriResource(MY_NS+ "predicate");
            	final KiWiUriResource object   = ts.createUriResource(MY_NS + "Object");

            	ts.createTriple(subject, property, object);

            	Assert.assertTrue(ts.hasTriple(subject, property, object));

            	ts.removeTriple(subject, property, object);

            	Assert.assertFalse(ts.hasTriple(subject, property, object));
            }
    	}.run();

    	/*
    	 * Checking whether adding/removing works between transactions as well ...
    	 */
       	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final TripleStore ts = (TripleStore) Component.getInstance("tripleStore");

            	final KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");
            	final KiWiUriResource property = ts.createUriResource(MY_NS+ "predicate");
            	final KiWiUriResource object   = ts.createUriResource(MY_NS + "Object");

            	ts.createTriple(subject, property, object);

            	Assert.assertTrue(ts.hasTriple(subject, property, object));

            }
    	}.run();

       	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final TripleStore ts = (TripleStore) Component.getInstance("tripleStore");

            	final KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");
            	final KiWiUriResource property = ts.createUriResource(MY_NS+ "predicate");
            	final KiWiUriResource object   = ts.createUriResource(MY_NS + "Object");

            	Assert.assertTrue(ts.hasTriple(subject, property, object));
            	ts.removeTriple(subject, property, object);

            }
    	}.run();

       	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final TripleStore ts = (TripleStore) Component.getInstance("tripleStore");

            	final KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");
            	final KiWiUriResource property = ts.createUriResource(MY_NS+ "predicate");
            	final KiWiUriResource object   = ts.createUriResource(MY_NS + "Object");

            	// TODO: assert fails, the triple exists
            	Assert.assertFalse(ts.hasTriple(subject, property, object));
            }
    	}.run();



		/*
		 * Check .equals() and .hashCode() on triples
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	Component.getInstance("entityManager");

            	final KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");
            	final KiWiUriResource property1 = ts.createUriResource(MY_NS + "property");
            	final KiWiLiteral object1   = ts.createLiteral("testvalue");
               	final KiWiLiteral object2   = ts.createLiteral("testvalue");

            	final KiWiTriple t1 = ts.createTriple(subject, property1, object1);
            	final KiWiTriple t2 = ts.createTriple(subject, property1, object2);

            	// check proper caching of triples; transaction has not yet been committed!
            	Assert.assertTrue(ts.hasTriple(subject, property1, object1));
            	Assert.assertTrue(ts.hasTriple(subject, property1, object2));

            	Assert.assertEquals(t1, t2);
            	Assert.assertEquals(t1.hashCode(), t2.hashCode());

            }
    	}.run();


		shutdownDatabase();
	}


	@Test
	public void testResources() throws Exception {
    	final String MY_NS = "http://www.example.com/myns/";
		clearDatabase();
		/*
		 * Check whether we can set properties and immediately query for them
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testResources() 1");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	Component.getInstance("entityManager");

            	final KiWiUriResource subject1  = ts.createUriResource(MY_NS + "Subject");
            	final KiWiUriResource subject2  = ts.createUriResource(MY_NS + "Subject");

            	Assert.assertTrue(subject1 == subject2); // caching should return same resource
            	Assert.assertEquals(subject1, subject2);

            	log.info(">>>>>>>>>> testResources() 1 end");

            }
    	}.run();

		/*
		 * Check whether we can set properties and immediately query for them
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testResources() 2");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	final KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");
            	try {
					subject.setProperty("<"+MY_NS+"test>", "test");
				} catch (final NamespaceResolvingException e1) {
					e1.printStackTrace();
				}

            	// since transaction not yet committed, count should be 0
            	Assert.assertEquals(0L, em.createQuery("select count(t) from KiWiTriple t where t.deleted = false and t.property.uri='"+MY_NS+"test'").getSingleResult());

            	// check whether caching yields right property result
            	try {
					Assert.assertEquals("test", subject.getProperty("<"+MY_NS+"test>"));
				} catch (final NonUniqueRelationException e) {
				}

				log.info(">>>>>>>>>> testResources() 2 end");
            }
    	}.run();

		/*
		 * Check whether we can query persistent properties
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testResources() 3");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	// since transaction has been committed, count should be 1 now
            	Assert.assertEquals(1L, em.createQuery("select count(t) from KiWiTriple t where t.deleted = false and t.property.uri='"+MY_NS+"test'").getSingleResult());

            	final KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");

            	try {
					Assert.assertEquals("test", subject.getProperty("<"+MY_NS+"test>"));
				} catch (final NonUniqueRelationException e) {
				}

				log.info(">>>>>>>>>> testResources() 3 end");
            }
    	}.run();

		/*
		 * Check whether we can modify properties and get the right result from cache
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testResources() 4");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	Component.getInstance("entityManager");

            	final KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");
            	try {
					subject.setProperty("<"+MY_NS+"test>", "another test");
				} catch (final NamespaceResolvingException e1) {
					e1.printStackTrace();
				}

            	try {
					Assert.assertEquals("another test", subject.getProperty("<"+MY_NS+"test>"));
				} catch (final NonUniqueRelationException e) {
				}

				log.info(">>>>>>>>>> testResources() 4 end");
            }
    	}.run();

		/*
		 * Check whether modification has been persisted properly over transactions
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testResources() 5");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	// since transaction has been committed, count should be 1 now
            	Assert.assertEquals(1L, em.createQuery("select count(t) from KiWiTriple t where t.deleted = false and t.property.uri='"+MY_NS+"test'").getSingleResult());

            	final KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");
            	try {
					Assert.assertEquals("another test", subject.getProperty("<"+MY_NS+"test>"));
				} catch (final NonUniqueRelationException e) {
				}

				log.info(">>>>>>>>>> testResources() 5 end");
            }
    	}.run();


    	/*
    	 * Check whether listOutgoing also works as expected
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testResources() 6");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	Component.getInstance("entityManager");

            	final KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");
            	final LinkedList<KiWiTriple> l = new LinkedList<KiWiTriple>();

            	for(final KiWiTriple t : subject.listOutgoing()) {
            		l.add(t);
            	}
            	Assert.assertEquals(1, l.size());

            	log.info(">>>>>>>>>> testResources() 6 end");
            }
    	}.run();

		/*
		 * Check whether we can remove properties and get the right result from cache
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testResources() 7");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");


            	final KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");
            	try {
					subject.removeProperty("<"+MY_NS+"test>");
				} catch (final NamespaceResolvingException e) {
					e.printStackTrace();
				}

//            	try {
//					Assert.assertNull(subject.getProperty("<"+MY_NS+"test>"));
//				} catch (NonUniqueRelationException e) {
//				}
//
				// since transaction has not been committed, count should still be 1
            	Assert.assertEquals(1L, em.createQuery("select count(t) from KiWiTriple t where t.deleted = false  and t.property.uri='"+MY_NS+"test'").getSingleResult());

            	log.info(">>>>>>>>>> testResources() 7 end");
            }
    	}.run();

		/*
		 * Check whether property has been properly removed from database after transaction
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testResources() 8");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	final EntityManager em = (EntityManager) Component.getInstance("entityManager");

            	// since transaction has been committed, count should be 0 now
            	Assert.assertEquals(0L, em.createQuery("select count(t) from KiWiTriple t where t.deleted = false  and t.property.uri='"+MY_NS+"test'").getSingleResult());

            	final KiWiUriResource subject  = ts.createUriResource(MY_NS + "Subject");

            	try {
					Assert.assertNull(subject.getProperty("<"+MY_NS+"test>"));
				} catch (final NonUniqueRelationException e) {
				}

				log.info(">>>>>>>>>> testResources() 8 end");
            }
    	}.run();


    	shutdownDatabase();
	}


	@Test
	public void testLiteral() throws Exception {

		/*
		 * Check .equals() and .hashCode() methods on literals
		 */
	   	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testLiteral() 1");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	Component.getInstance("entityManager");

            	final KiWiLiteral literal1       = ts.createLiteral("testvalue");
            	final KiWiLiteral literal2       = ts.createLiteral("testvalue");

            	Assert.assertTrue(literal1 == literal2);
            	Assert.assertEquals(literal1, literal2);
            	Assert.assertEquals(literal1.hashCode(), literal2.hashCode());

            	log.info(">>>>>>>>>> testLiteral() 1 end");
            }
    	}.run();


		/*
		 * Check whether we can create triples to literals and they are
		 * cached properly
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testLiteral() 2");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	Component.getInstance("entityManager");

            	final KiWiUriResource subject  = ts.createUriResource(Constants.NS_KIWI_CORE + "ContentItem");
            	final KiWiUriResource property3 = ts.createUriResource(Constants.NS_KIWI_CORE+"createdOn");
            	final KiWiLiteral literal       = ts.createLiteral("testvalue");

            	ts.createTriple(subject, property3, literal);

            	// check proper caching of triples; transaction has not yet been committed!
            	Assert.assertTrue(ts.hasTriple(subject, property3, literal));

            	// check if triple.Object is still a literal
            	final Iterable<KiWiTriple> outgoing = subject.listOutgoing();
            	final Iterator<KiWiTriple> it= outgoing.iterator();
            	while(it.hasNext()){
            		final KiWiTriple triple = it.next();
            		if(triple.getObject().getId().equals(literal.getId())){
            			Assert.assertTrue(triple.getObject() instanceof KiWiLiteral);
            		}
            	}
            	log.info(">>>>>>>>>> testLiteral() 2 end");
            }
    	}.run();

    	/*
    	 * check if the literals are still instanceOf KiWiLiteral
    	 * after closing the transition.
    	 */
    	new FacesRequest() {
            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testLiteral() 3");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	Component.getInstance("entityManager");

            	final KiWiUriResource subject  = ts.createUriResource(Constants.NS_KIWI_CORE + "ContentItem");
            	final KiWiUriResource property3 = ts.createUriResource(Constants.NS_KIWI_CORE+"createdOn");
            	final KiWiLiteral literal       = ts.createLiteral("testvalue");

            	Assert.assertTrue(ts.hasTriple(subject, property3, literal));

            	// check if triple.Object is after closing the transition still is a literal
            	final Iterable<KiWiTriple> outgoing = subject.listOutgoing();
            	final Iterator<KiWiTriple> it= outgoing.iterator();
            	while(it.hasNext()){
            		final KiWiTriple triple = it.next();
            		if(triple.getObject().getId().equals(literal.getId())){
            			System.out.println("literal type: "+triple.getObject().getClass().getCanonicalName());
            			Assert.assertTrue(triple.getObject() instanceof KiWiLiteral);
            		}
            	}
            	log.info(">>>>>>>>>> testLiteral() 3 end");
            }
    	}.run();


	}

	@Test
	public void testKiWiResource() throws Exception {

		/*
		 * Check whether KiWiResources in triples have a ContentItem.
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testKiWiResource() 1");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	Component.getInstance("entityManager");

            	final KiWiUriResource subject  = ts.createUriResource(Constants.NS_KIWI_CORE + "ContentItem123");
            	final KiWiUriResource property1 = ts.createUriResource(Constants.NS_RDF + "type");
            	final KiWiUriResource object1   = ts.createUriResource(Constants.NS_OWL + "Class");

            	ts.createTriple(subject, property1, object1);

            	Assert.assertTrue(subject.getContentItem()!=null);
            	Assert.assertTrue(property1.getContentItem()!=null);
            	Assert.assertTrue(object1.getContentItem()!=null);
            	log.info(">>>>>>>>>> testKiWiResource() 1 end");
            }
    	}.run();

    	/*
    	 * and the same in a new transaction..
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	final Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>>> testKiWiResource() 2");
            	final TripleStore   ts = (TripleStore) Component.getInstance("tripleStore");
            	Component.getInstance("entityManager");

            	final KiWiUriResource subject  = ts.createUriResource(Constants.NS_KIWI_CORE + "ContentItem123");
            	final KiWiUriResource property1 = ts.createUriResource(Constants.NS_RDF + "type");
            	final KiWiUriResource object1   = ts.createUriResource(Constants.NS_OWL + "Class");

            	Assert.assertTrue(subject.getContentItem()!=null);
            	Assert.assertTrue(property1.getContentItem()!=null);
            	Assert.assertTrue(object1.getContentItem()!=null);
            	log.info(">>>>>>>>>> testKiWiResource() 2 end");
            }
    	}.run();
}
}
