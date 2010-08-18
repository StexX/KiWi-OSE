package kiwi.test.service.reasoning;

import java.util.HashSet;
import java.util.Set;

import kiwi.api.reasoning.ProgramProcessor;
import kiwi.api.reasoning.ReasoningService;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.service.reasoning.ReasoningTaskStatistics;
import kiwi.service.reasoning.StringProgramLoader;
import kiwi.service.transaction.KiWiSynchronizationImpl;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ReasoningTest extends KiWiTest {
	Log log = Logging.getLog(this.getClass());
	
	@Test
	public void testReasoning1() throws Exception {
		/*
		 * 
		 */
    	new FacesRequest() {
            @Override
            protected void invokeApplication() {

            	log.info("BEGIN testReasoning1()");
            	
            	TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
            	tripleStore.clear();

            	TransactionService ts = (TransactionService) Component.getInstance("transactionService");
//            	ts.removeRegisteredSync(KiWiSynchronizationImpl.getInstance());
            	
            	ReasoningService reasoningService = (ReasoningService) Component.getInstance("kiwi.core.reasoningService");
            	reasoningService.disableReasoning();
            	log.info("Reasoning queue contained "+reasoningService.getTaskQueue().size()+" tasks.");
            	reasoningService.getTaskQueue().clear();
            	
            	KiWiUriResource userx = tripleStore.createUriResource("http://kiwi/test/userx");
            	KiWiUriResource rdftype = tripleStore.createUriResource("http://kiwi/rdf#type");
            	KiWiUriResource usertype = tripleStore.createUriResource("http://kiwi/test/usertype");            	
            	
            	KiWiTriple userTypeTriple = tripleStore.createTriple(userx, rdftype, usertype);
            	
            }
    	}.run();
    	
    	new FacesRequest() {
            @Override
            protected void invokeApplication() {
            	
            	StringProgramLoader programLoader = (StringProgramLoader) Component.getInstance("stringProgramLoader");
            	programLoader.addLine("($1 http://kiwi/rdf#type $2) -> ($1 http://kiwi/rdf#type http://kiwi/rdf#Resource)");
            	programLoader.addLine("($1 http://kiwi/rdf#subClass $2), ($2 http://kiwi/rdf#subClass $3) -> ($1 http://kiwi/rdf#subClass $3)");            	
            	log.info(" rules loaded");
            	
            	ProgramProcessor programProcessor = (ProgramProcessor) Component.getInstance("programProcessor");
            	programProcessor.setProgramLoader(programLoader);
            	
            	TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
            	
            	TransactionService ts = (TransactionService) Component.getInstance("transactionService");
//            	ts.removeRegisteredSync(KiWiSynchronizationImpl.getInstance());
            	
            	KiWiUriResource userx = tripleStore.createUriResource("http://kiwi/test/userx");
            	KiWiUriResource rdftype = tripleStore.createUriResource("http://kiwi/rdf#type");
            	KiWiUriResource usertype = tripleStore.createUriResource("http://kiwi/test/usertype");
            	
            	KiWiTriple userTypeTriple = tripleStore.createTriple(userx, rdftype, usertype);
            	
            	// we can't test via ReasoningTasks because the reasoning task may not be created yet because of the asynchronicity
            	
            	//Queue<ReasoningTask> queue = reasoningService.getTaskQueue();
            	
        		//System.out.println("Reasoning queue contained "+queue.size()+" tasks.");
            	
            	//Assert.assertEquals(queue.size(), 1);
            	
            	//ReasoningTask task = queue.peek();
            	
            	//System.out.println("Task = "+task.toString());
            	
            	//Assert.assertEquals(task.getAddedTriples().size(), 1);
            	//Assert.assertEquals(task.getRemovedTriples().size(), 0);
            	
            	//Assert.assertTrue(task.getAddedTriples().contains(userTypeTriple));
            	
            	Iterable<KiWiTriple> triples = tripleStore.listTriples(false);
            	for (KiWiTriple triple : triples)
            		log.info("KiWiTriple(#0): #1", triple.getId(), triple.toString());
            	            	
            	log.info("About to run rule processor");
            	Set<KiWiTriple> addedTriples = new HashSet<KiWiTriple>(); 
            	addedTriples.add(userTypeTriple);
            	ReasoningTaskStatistics stats = programProcessor.processAddedTriples(addedTriples);
            	Assert.assertEquals(stats.getNewTriplesCount(), 1);
            	
            	log.info("Reasoner created "+stats.getNewTriplesCount()+" new triples.");
            	
            	KiWiUriResource rdfresource = tripleStore.createUriResource("http://kiwi/rdf#Resource");            	
            	            	
            	Assert.assertTrue(tripleStore.hasTriple(userx, rdftype, rdfresource));
            	log.info("END testReasoning1()");
            }
    	}.run();    	
	}
	
	@Test
	public void testRuleChaining() throws Exception {
		/*
		 * 
		 */
    	new FacesRequest() {
            @Override
            protected void invokeApplication() {
            	log.info("BEGIN testReasoningRuleChaining()");

            	TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
            	tripleStore.clear();
            	tripleStore.clearCaches();
            	tripleStore = (TripleStore) Component.getInstance("tripleStore");

            	TransactionService ts = (TransactionService) Component.getInstance("transactionService");
//            	ts.removeRegisteredSync(KiWiSynchronizationImpl.getInstance());
            	
            	ReasoningService reasoningService = (ReasoningService) Component.getInstance("kiwi.core.reasoningService");
            	reasoningService.disableReasoning();
            	log.info("Reasoning queue contained "+reasoningService.getTaskQueue().size()+" tasks.");
            	reasoningService.getTaskQueue().clear();
            	
            	KiWiUriResource userx = tripleStore.createUriResource("http://kiwi/test/userx");
            	KiWiUriResource rdftype = tripleStore.createUriResource("http://kiwi/rdf#type");
            	KiWiUriResource usertype = tripleStore.createUriResource("http://kiwi/test/usertype");            	
            	
            	KiWiTriple userTypeTriple = tripleStore.createTriple(userx, rdftype, usertype);
            }
    	}.run();
    	
    	new FacesRequest() {
            @Override
            protected void invokeApplication() {

            	StringProgramLoader programLoader = (StringProgramLoader) Component.getInstance("stringProgramLoader");
            	programLoader.addLine("($1 http://kiwi/rdf#type $2) -> ($1 http://kiwi/rdf#type http://kiwi/rdf#Resource)");
            	programLoader.addLine("($1 http://kiwi/rdf#subClass $2), ($2 http://kiwi/rdf#subClass $3) -> ($1 http://kiwi/rdf#subClass $3)");
            	programLoader.addLine("($1 http://kiwi/rdf#type http://kiwi/rdf#Resource) -> ($1 http://kiwi/rdf#type http://kiwi/test/resource)");
            	
            	ProgramProcessor programProcessor = (ProgramProcessor) Component.getInstance("programProcessor");
            	programProcessor.setProgramLoader(programLoader);
            	
            	TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
            	
            	TransactionService ts = (TransactionService) Component.getInstance("transactionService");
//            	ts.removeRegisteredSync(KiWiSynchronizationImpl.getInstance());
            	
            	KiWiUriResource userx = tripleStore.createUriResource("http://kiwi/test/userx");
            	KiWiUriResource rdftype = tripleStore.createUriResource("http://kiwi/rdf#type");
            	KiWiUriResource usertype = tripleStore.createUriResource("http://kiwi/test/usertype");            	
            	KiWiUriResource kiwiresource = tripleStore.createUriResource("http://kiwi/test/resource");            	
            	
            	KiWiTriple userTypeTriple = tripleStore.createTriple(userx, rdftype, usertype);
            	            	
            	Iterable<KiWiTriple> triples = tripleStore.listTriples(false);
            	for (KiWiTriple triple : triples)
            		log.info("KiWiTriple(#0): #1", triple.getId(), triple.toString());
            	
				log.info("About to run rule processor");
            	Set<KiWiTriple> addedTriples = new HashSet<KiWiTriple>(); addedTriples.add(userTypeTriple);
            	ReasoningTaskStatistics stats = programProcessor.processAddedTriples(addedTriples);
            	log.info("Reasoner created "+stats.getNewTriplesCount()+" new triples.");
            	Assert.assertEquals(stats.getNewTriplesCount(), 2);
            	
            	
            	KiWiUriResource rdfresource = tripleStore.createUriResource("http://kiwi/rdf#Resource");            	
            	            	
            	Assert.assertTrue(tripleStore.hasTriple(userx, rdftype, rdfresource));
            	Assert.assertTrue(tripleStore.hasTriple(userx, rdftype, kiwiresource));
            	log.info("END testReasoningRuleChaining()");
            }
    	}.run();    	
	}	
	
}
