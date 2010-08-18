package kiwi.test.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.reasoning.ReasoningService;
import kiwi.api.revision.RevisionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.MetadataUpdate;
import kiwi.model.revision.Revision;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdateMetadataTest extends KiWiTest {
	
	private Long countExistingTriples = new Long(0);
	private Long countExistingNamespaces = new Long(0);
	private Long countMetadataUpdates = new Long(0);
	private Long countRevisions = new Long(0);
	
	private ContentItem contentItem;
	
	/**
	 * initializes currentContentItem and 
	 * counts for several entities
	 * @throws Exception
	 */
	@Test
	public void init() throws Exception {
		
		clearDatabase();
		
		/**
		 * initialises the currentContentItem
		 */
		new FacesRequest() {
			
			@Override
			protected void invokeApplication() throws Exception {
				
				ReasoningService reasoningService = (ReasoningService) Component.getInstance("kiwi.core.reasoningService");
                reasoningService.disableReasoning();
				
				// necessary????
				contentItem = (ContentItem) Component.getInstance("currentContentItem");
				
			}
			
		}.run();
		/**
		 * initializes the variables: 
		 * countExistingTriples,
		 * countExistingNamespaces,
		 * countMetadataUpdates
		 * countRevisions
		 */
		new FacesRequest() {
			
			@Override
			protected void invokeApplication() throws Exception {
				
				EntityManager em = (EntityManager) Component.getInstance("entityManager");
				Query q1 = em.createQuery("select count(t) from KiWiTriple t where t.deleted=false");
				q1.setMaxResults(1);
				countExistingTriples = (Long) q1.getSingleResult();
				
				Query q2 = em.createQuery("select count(n) from KiWiNamespace n where n.deleted=false");
				q2.setMaxResults(1);
				countExistingNamespaces = (Long) q2.getSingleResult();
				
				Query q3 = em.createQuery("select count(mdu) from MetadataUpdate mdu");
		    	q3.setMaxResults(1);
		    	countMetadataUpdates = (Long) q3.getSingleResult();
		    	
		    	Query q4 = em.createQuery("select count(rev) from Revision rev");
		    	q4.setMaxResults(1);
		    	countRevisions = (Long) q4.getSingleResult();
			}
			
		}.run();
	}
	
	@Test(dependsOnMethods = {"testRevision"})
	public void endOfTest() throws Exception {
		/**
		 * initialises the currentContentItem
		 */
		new FacesRequest() {
			
			@Override
			protected void invokeApplication() throws Exception {
				ReasoningService reasoningService = (ReasoningService) Component.getInstance("kiwi.core.reasoningService");
				reasoningService.enableReasoning();
			}
		}.run();
	}
	
	@Test(dependsOnMethods = { "init" })
    public void testRevision() throws Exception {
    	
		/**
		 * adds two triples and two namespaces
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	
            	Log log = Logging.getLog(UpdateMetadataTest.class);
            	log.info(">>>>>>>>>> Start of method testRevision() (1) ");
            	TripleStore ts = (TripleStore) Component.getInstance("tripleStore");
            	EntityManager em = (EntityManager) 
            			Component.getInstance("entityManager");
            	
            	log.info(">>>>>>>>>> testRevision() begin creating UriResources");
            	KiWiUriResource subject1 = ts.createUriResource("http://www.example.org/subject");
            	KiWiUriResource predicate1 = ts.createUriResource("http://www.example.org/predicate");
            	KiWiUriResource object1 = ts.createUriResource("http://www.example.org/object");
            	
            	KiWiUriResource subject2 = ts.createUriResource("http://www.example.org/subject2");
            	KiWiUriResource predicate2 = ts.createUriResource("http://www.example.org/predicate2");
            	KiWiUriResource object2 = ts.createUriResource("http://www.example.org/object2");
            	
            	log.info(">>>>>>>>>> testRevision() begin creating Triples");
            	ts.createTriple(subject1, predicate1, object1);
            	ts.createTriple(subject2, predicate2, object2);
            	ts.setNamespace("ex", "http://www.example.org");
            	ts.setNamespace("ex2", "http://www.example2.org");
            	
            	log.info(">>>>>>>>>> testRevision() checking number of triples/namespaces with em queries");
            	Query q1 = em.createQuery("select count(t) from KiWiTriple t where t.deleted=false");
            	Query q2 = em.createQuery("select count(n) from KiWiNamespace n where n.deleted=false");
            	
            	q1.setMaxResults(1);
            	q2.setMaxResults(1);
            	
            	// triples and namespaces should just be added after the transaction
            	Assert.assertEquals(q1.getSingleResult(),countExistingTriples+0L);
            	Assert.assertEquals(q2.getSingleResult(),countExistingNamespaces+0L);
            }
    	}.run();
    	
    	/**
    	 * checks amount of triples and namespaces
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	EntityManager em = (EntityManager) 
    					Component.getInstance("entityManager");
            	
            	Log log = Logging.getLog(UpdateMetadataTest.class);
            	log.info(">>>>>>>>>> testRevision() new transaction ended: " +
            			"checking number of triples/namespaces (2)");
            	Query q1 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate' and t.deleted=false");
            	Query q2 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate2' and t.deleted=false");
            	Query q3 = em.createQuery("select count(n) from KiWiNamespace n where n.uri = 'http://www.example.org' and n.deleted=false");
            	Query q4 = em.createQuery("select count(n) from KiWiNamespace n where n.uri = 'http://www.example2.org' and n.deleted=false");
            	
            	q1.setMaxResults(1);
            	q2.setMaxResults(1);
            	q3.setMaxResults(1);
            	q4.setMaxResults(1);
            	
            	// triples and namespaces should just be added after the transaction
            	Assert.assertEquals(q1.getSingleResult(),1L);
            	Assert.assertEquals(q2.getSingleResult(),1L);
            	Assert.assertEquals(q3.getSingleResult(),1L);
            	Assert.assertEquals(q4.getSingleResult(),1L);
            	
            	/*countMetadataUpdates must be increased by 2, because now we got 2 MetadataUpdates per Revision 
            	 * (-> 2 diverse ContentItem Versions)*/
            	countMetadataUpdates = countMetadataUpdates+2;
            	countRevisions = countRevisions+1;
            	
            	log.info(">>>>>>>>>> testRevision() end of 'checking number of triples/namespaces'");
            }
    	}.run();
    	
    	
    	/**
		 * adds two other triples and namespaces and deleted one triple
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	TripleStore ts = (TripleStore) Component.getInstance("tripleStore");
            	EntityManager em = (EntityManager) 
            			Component.getInstance("entityManager");
            	
            	Log log = Logging.getLog(UpdateMetadataTest.class);
            	log.info(">>>>>>>>>> testRevision() new transaction ended: " +
            			"creating more triples/namespaces (3)");
            	KiWiUriResource subject1 = ts.createUriResource("http://www.example.org/subject3");
            	KiWiUriResource predicate1 = ts.createUriResource("http://www.example.org/predicate3");
            	KiWiUriResource object1 = ts.createUriResource("http://www.example.org/object3");
            	
            	Query q1 = em.createQuery("select t from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate2' and t.deleted=false");
            	q1.setMaxResults(1);
            	
            	ts.removeTriple((KiWiTriple) q1.getSingleResult());
            	KiWiUriResource subject2 = ts.createUriResource("http://www.example.org/subject2");
            	KiWiUriResource predicate2 = ts.createUriResource("http://www.example.org/predicate2");
            	KiWiUriResource object2 = ts.createUriResource("http://www.example.org/object4");
            	
            	ts.createTriple(subject1, predicate1, object1);
            	ts.createTriple(subject2, predicate2, object2);
            	ts.setNamespace("ex3", "http://www.example3.org");
            	ts.setNamespace("ex4", "http://www.example4.org");
            	
            	log.info(">>>>>>>>>> testRevision() end of creating more triples/namespaces");
            }
    	}.run();
    	
    	/**
    	 * checks amount of triples and namespaces
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	EntityManager em = (EntityManager) 
    					Component.getInstance("entityManager");
            	
            	Log log = Logging.getLog(UpdateMetadataTest.class);
            	log.info(">>>>>>>>>> testRevision() () new transaction ended: " +
            			"counting triples/namespaces (4)");
            	Query q1 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate' and t.deleted=false");
            	Query q2 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate3' and t.deleted=false");
            	Query q3 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate2' and t.deleted=false");
            	Query q4 = em.createQuery("select count(t) from KiWiTriple t where t.object.uri = 'http://www.example.org/object4' and t.deleted=false");
            	Query q5 = em.createQuery("select count(n) from KiWiNamespace n where n.uri = 'http://www.example.org' and n.deleted=false");
            	Query q6 = em.createQuery("select count(n) from KiWiNamespace n where n.uri = 'http://www.example2.org' and n.deleted=false");
            	Query q7 = em.createQuery("select count(n) from KiWiNamespace n where n.uri = 'http://www.example3.org' and n.deleted=false");
            	Query q8 = em.createQuery("select count(n) from KiWiNamespace n where n.uri = 'http://www.example4.org' and n.deleted=false");
            	
            	q1.setMaxResults(1);
            	q2.setMaxResults(1);
            	q3.setMaxResults(1);
            	q4.setMaxResults(1);
            	q5.setMaxResults(1);
            	q6.setMaxResults(1);
            	q7.setMaxResults(1);
            	q8.setMaxResults(1);
            	
            	// triples and namespaces should just be added after the transaction
            	Assert.assertEquals(q1.getSingleResult(),1L);
            	Assert.assertEquals(q2.getSingleResult(),1L);
            	Assert.assertEquals(q3.getSingleResult(),1L);
            	Assert.assertEquals(q4.getSingleResult(),1L);
            	Assert.assertEquals(q5.getSingleResult(),1L);
            	Assert.assertEquals(q6.getSingleResult(),1L);
            	Assert.assertEquals(q7.getSingleResult(),1L);
            	Assert.assertEquals(q8.getSingleResult(),1L);
            	
            	/* again increase by 2, because two diverse CI Versions (each holding a MDU) were created */
            	countMetadataUpdates = countMetadataUpdates + 2;
            	countRevisions = countRevisions+1;
            	
            	log.info(">>>>>>>>>> testRevision() end of counting triples/namespaces");
            	
            }
    	}.run();
    	
    	/**
    	 * checks amount of current MetadataUpdate triples and calls restore() on first Revision
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateMetadataTest.class);
            	log.info(">>>>>>>>>> testRevision() () (5)");
            	EntityManager em = (EntityManager) 
						Component.getInstance("entityManager");
            	log.info("###### entitymanager created #0 ", em);
            	RevisionService revS = (RevisionService) 
    					Component.getInstance("revisionService");
            	log.info("###### revisionservice created #0 ", revS);
            	
            	Query q = em.createQuery("select t from KiWiTriple t " +
            			"where t.property.uri = " +
            			"'http://www.example.org/predicate' and t.deleted=false");
            	log.info(">>>>>>>>>> testRevision() () (5) query");
            	KiWiTriple t = null;
            	try {
					t = (KiWiTriple) q.getSingleResult();
				} catch (NoResultException e) {
					e.printStackTrace();
				}
            	
				Assert.assertNotNull(t);
				log.info(">>>>>>>>>> testRevision() () (5) t not null");
				List<CIVersion> versions = t.getSubject().getContentItem().getVersions();
				log.info(">>>>>>>>>> testRevision() () (5) t get civersions");
				int versionCount = versions.size();
				CIVersion getLastVersion = versions.get(versionCount-1);
				log.info(">>>>>>>>>> testRevision() () (5) t get last version");
				Revision rev = getLastVersion.getRevision();
				log.info(">>>>>>>>>> testRevision() () (5) t get rev");
				
//		    	Revision rev = em.find(Revision.class, (countRevisions-1));
            	
		    	log.info("###### Revision queried #0 ", rev);
		    	Assert.assertNotNull(rev);
		    	
		    	log.info("###### Revision query ci versions");
		    	Assert.assertNotNull(rev.getContentItemVersions());
		    	log.info("###### Revision query ci versions size");
		    	Assert.assertEquals(2L, rev.getContentItemVersions().size());
		    	boolean checked_add_rem = false;
		    	boolean checked_add = false;
		    	
		    	for(CIVersion civ : rev.getContentItemVersions()) {
		    		log.info("###### Ensure that metadataupdate != null");
		    		Assert.assertNotNull(civ.getMetadataUpdate());
		    		
		    		MetadataUpdate mdu = civ.getMetadataUpdate();
		    		if(mdu.getRemovedTriples() != null && mdu.getRemovedTriples().size() != 0) {
				    	checked_add_rem = true;
		    		} else {
		    			log.info("###### Metadataupdate - added #0 triples", mdu.getAddedTriples().size());
				    	Assert.assertEquals(mdu.getAddedTriples().size(),1L);
				    	checked_add = true;
		    		}			    	
		    	}
		    	
		    	Assert.assertTrue(checked_add);
		    	log.info("###### Added triples have been checked ");
		    	Assert.assertTrue(!checked_add_rem);
		    	
		    	log.info("###### Before restoring revision with id #0 ", (countRevisions-1));
//		    	Query q2 = em.createQuery("select r from Revision r where r.id='"+(countRevisions-1)+"'");
//		    	q2.setMaxResults(1);
//		    	log.info("before query");
//		    	Revision r = (Revision) q2.getSingleResult();
//		    	log.info("after query, before restore");
		    	revS.restore(rev);
		    	log.info("after restore");
            }
    	}.run();
    	
    	/**
    	 * checks amount of triples and namespaces
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateMetadataTest.class);
            	log.info(">>>>>>>>>> testRevision() () (6)");
            	EntityManager em = (EntityManager) 
    					Component.getInstance("entityManager");
            	
            	Query q1 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate' and t.deleted=false");
            	Query q2 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate2' and t.deleted=false");
            	Query q3 = em.createQuery("select count(n) from KiWiNamespace n where n.uri = 'http://www.example.org' and n.deleted=false");
            	Query q4 = em.createQuery("select count(n) from KiWiNamespace n where n.uri = 'http://www.example2.org' and n.deleted=false");
            	
            	q1.setMaxResults(1);
            	q2.setMaxResults(1);
            	q3.setMaxResults(1);
            	q4.setMaxResults(1);
            	
            	// triples and namespaces should just be added after the transaction
            	Assert.assertEquals(q1.getSingleResult(),1L);
            	Assert.assertEquals(q2.getSingleResult(),1L);
            	Assert.assertEquals(q3.getSingleResult(),1L);
            	Assert.assertEquals(q4.getSingleResult(),1L);
            	
            	countRevisions = countRevisions + 1;
            	countMetadataUpdates = countMetadataUpdates + 2;
            }
    	}.run();
    	
    	/**
		 * adds two other triples and namespaces and deleted one triple
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateMetadataTest.class);
            	log.info(">>>>>>>>>> testRevision() () (7)");
            	TripleStore ts = (TripleStore) Component.getInstance("tripleStore");
            	EntityManager em = (EntityManager) 
            			Component.getInstance("entityManager");
            	
            	KiWiUriResource subject1 = ts.createUriResource("http://www.example.org/subject5");
            	KiWiUriResource predicate1 = ts.createUriResource("http://www.example.org/predicate5");
            	KiWiUriResource object1 = ts.createUriResource("http://www.example.org/object5");
            	
            	Query q1 = em.createQuery("select t from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate2' and t.deleted=false");
            	q1.setMaxResults(1);
            	ts.removeTriple((KiWiTriple) q1.getSingleResult());
            	
            	KiWiUriResource subject2 = ts.createUriResource("http://www.example.org/subject6");
            	KiWiUriResource predicate2 = ts.createUriResource("http://www.example.org/predicate6");
            	KiWiUriResource object2 = ts.createUriResource("http://www.example.org/object6");
            	
            	ts.createTriple(subject1, predicate1, object1);
            	ts.createTriple(subject2, predicate2, object2);
            	
            	ts.removeNamespace("ex2");
            	
            	ts.setNamespace("ex5", "http://www.example5.org");
            	ts.setNamespace("ex6", "http://www.example6.org");
            }
    	}.run();
    	
    	/**
    	 * checks amount of triples and namespaces
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateMetadataTest.class);
            	log.info(">>>>>>>>>> testRevision() () (8)");
            	EntityManager em = (EntityManager) 
    					Component.getInstance("entityManager");
            	
            	Query q1 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate' and t.deleted=false");
            	Query q2 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate2' and t.deleted=false");
            	Query q3 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate5' and t.deleted=false");
            	Query q4 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate6' and t.deleted=false");
            	Query q5 = em.createQuery("select count(n) from KiWiNamespace n where n.uri = 'http://www.example.org' and n.deleted=false");
            	Query q6 = em.createQuery("select count(n) from KiWiNamespace n where n.uri = 'http://www.example2.org' and n.deleted=false");
            	Query q7 = em.createQuery("select count(n) from KiWiNamespace n where n.uri = 'http://www.example5.org' and n.deleted=false");
            	Query q8 = em.createQuery("select count(n) from KiWiNamespace n where n.uri = 'http://www.example6.org' and n.deleted=false");
            	
            	q1.setMaxResults(1);
            	q2.setMaxResults(1);
            	q3.setMaxResults(1);
            	q4.setMaxResults(1);
            	q5.setMaxResults(1);
            	q6.setMaxResults(1);
            	q7.setMaxResults(1);
            	q8.setMaxResults(1);
            	
            	// triples and namespaces should just be added after the transaction
            	Assert.assertEquals(q1.getSingleResult(),1L);
            	Assert.assertEquals(q2.getSingleResult(),0L);
            	Assert.assertEquals(q3.getSingleResult(),1L);
            	Assert.assertEquals(q4.getSingleResult(),1L);
            	Assert.assertEquals(q5.getSingleResult(),1L);
            	Assert.assertEquals(q6.getSingleResult(),0L);
            	Assert.assertEquals(q7.getSingleResult(),1L);
            	Assert.assertEquals(q8.getSingleResult(),1L);
            	
            	countRevisions = countRevisions + 1;
            	countMetadataUpdates = countMetadataUpdates + 3;
            }
    	}.run();
    	
    	/**
    	 * checks amount of current MetadataUpdate triples and calls restore() on first MetadataUpdate
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateMetadataTest.class);
            	log.info(">>>>>>>>>> testRevision() () (9)");
            	EntityManager em = (EntityManager) 
						Component.getInstance("entityManager");
            	log.info(">>>>>>>>>> testRevision() (9) initialised EM");
            	RevisionService revS = (RevisionService) 
    					Component.getInstance("revisionService");
            	log.info(">>>>>>>>>> testRevision() (9) initialised RevisionService");
            	
            	Query q = em.createQuery("select t from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate' and t.deleted=false");
            	KiWiTriple t = null;
            	try {
					t = (KiWiTriple) q.getSingleResult();
				} catch (NoResultException e) {
					e.printStackTrace();
				}
            	
				Assert.assertNotNull(t);
				List<CIVersion> versions = t.getSubject().getContentItem().getVersions();
				int versionCount = versions.size();
				CIVersion getLastVersion = versions.get(versionCount-1);
				Revision rev = getLastVersion.getRevision();
				
//            	Revision rev = em.find(Revision.class, countRevisions-3);
            	
		    	log.info("###### Revision queried #0 ", rev);
		    	Assert.assertNotNull(rev);
		    	
		    	log.info("###### Revision query ci versions");
		    	Assert.assertNotNull(rev.getContentItemVersions());
		    	log.info("###### Revision query ci versions size");
		    	Assert.assertEquals(2L, rev.getContentItemVersions().size());
		    	
		    	boolean checked_add_rem = false;
		    	boolean checked_add = false;
		    	
		    	for(CIVersion civ : rev.getContentItemVersions()) {
		    		log.info("###### Ensure that metadataupdate != null");
		    		Assert.assertNotNull(civ.getMetadataUpdate());
		    		
		    		MetadataUpdate mdu = civ.getMetadataUpdate();
		    		if(mdu.getRemovedTriples() != null && mdu.getRemovedTriples().size() != 0) {
				    	checked_add_rem = true;
		    		} else {
		    			log.info("###### Metadataupdate - added #0 triples", mdu.getAddedTriples().size());
				    	Assert.assertEquals(mdu.getAddedTriples().size(),1L);
				    	checked_add = true;
		    		}			    	
		    	}
		    	
		    	Assert.assertTrue(checked_add);
		    	Assert.assertTrue(!checked_add_rem);
		    	
		    	revS.restore(rev);
		    	
		    	log.info(">>>>>>>>>> testRevision() END OF (9)");
            }
    	}.run();
    	
    	/**
    	 * checks amount of triples and namespaces
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {
            	Log log = Logging.getLog(UpdateMetadataTest.class);
            	log.info(">>>>>>>>>> testRevision() () (10)");
            	EntityManager em = (EntityManager) 
    					Component.getInstance("entityManager");
            	
            	Query q1 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate' and t.deleted=false");
            	Query q2 = em.createQuery("select count(t) from KiWiTriple t where t.property.uri = 'http://www.example.org/predicate2' and t.deleted=false");
            	
            	q1.setMaxResults(1);
            	q2.setMaxResults(1);
            	
            	// triples and namespaces should just be added after the transaction
            	Assert.assertEquals(q1.getSingleResult(),1L);
            	Assert.assertEquals(q2.getSingleResult(),1L);
            	
            	countMetadataUpdates = countMetadataUpdates + 3;
            	
            	log.info(">>>>>>>>>> testRevision() END OF (10)");
            }
    	}.run();
    }
}
