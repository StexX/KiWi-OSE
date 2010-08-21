package kiwi.test.service.reasoning;

import java.util.List;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import kiwi.api.reasoning.ReasoningService;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.service.reasoning.CompiledProgram;
import kiwi.service.reasoning.CompiledRule;
import kiwi.service.reasoning.ProgramCompiler;
import kiwi.service.reasoning.ProgramParser;
import kiwi.service.reasoning.RuleBodyProcessor;
import kiwi.service.reasoning.RuleBodyResult;
import kiwi.service.reasoning.StringProgramLoader;
import kiwi.service.reasoning.ast.Conjunction;
import kiwi.service.reasoning.ast.Formula;
import kiwi.service.reasoning.ast.Program;
import kiwi.service.reasoning.ast.Rule;
import kiwi.service.reasoning.ast.Variable;
import kiwi.service.transaction.KiWiSynchronizationImpl;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.transaction.Transaction;
import org.testng.Assert;
import org.testng.annotations.Test;


/** 
 * 
 * Important note: don't use real namespaces (rdf, rdfs, ...) for testing because it could interfere with
 * triples that kiwi creates automatically (for example it creates the triple anonymousUser rdf:type kiwiUser)
 * 
 * @author Jakub Kotowski
 *
 */
@Test
public class ProgramTest extends KiWiTest {
	Log log = Logging.getLog(this.getClass());
	
	private void forcePersistence() {

		// disabled, because it causes troubles when several entity managers are
		// active at the same time

		// entityManager.flush();

		try {
			Transaction.instance().commit();
			Transaction.instance().begin();
			TransactionService transactionService = 
				(TransactionService) Component.getInstance("transactionService");
			transactionService.registerSynchronization(
            		KiWiSynchronizationImpl.getInstance(), 
            		transactionService.getUserTransaction() );
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	@Test
	public void testProgramParsing() throws Exception {
		/*
		 * 
		 */
    	new FacesRequest() {
            @Override
            protected void invokeApplication() {
            	log.info("ProgramTest.testProgramParsing()");
            	ReasoningService reasoningService = (ReasoningService) Component.getInstance("kiwi.core.reasoningService");
            	//we have to switch online reasoning off already here because this test is in the same test suite with ReasoningTest and before ReasoningTest starts running
            	//some tasks may already be scheduled (triples added for the new anonymous user)
            	reasoningService.disableReasoning();
            	TransactionService ts = (TransactionService) Component.getInstance("transactionService");
//            	ts.removeRegisteredSync(KiWiSynchronizationImpl.getInstance());
            	
            	StringProgramLoader programLoader = (StringProgramLoader) Component.getInstance("stringProgramLoader");
            	programLoader.addLine("@prefix rdf : <http://kiwi/rdf#>");
            	programLoader.addLine("@prefix rdfs: <http://kiwi/rdfsss#>");
            	programLoader.addLine("($1 http://kiwi/rdf#type $2) -> ($1 http://kiwi/rdf#type http://kiwi/rdf#Resource)");
            	programLoader.addLine("rdf-subclass: ($1 rdfs:subClassOf $2), ($2 rdfs:subClassOf $3) -> ($1 rdfs:subClassOf $3)");            	
            	programLoader.addLine("constraint: ($1 rdfs:subClassOf $2), ($2 rdf:type http://kiwi/nothing) -> inconsistency");            	
            	log.info(" rules loaded");

            	Program parsedProgram = programLoader.loadProgram("");

            	Assert.assertEquals(parsedProgram.getNamespaces().size(), 2,"ERROR");
            	Assert.assertEquals(parsedProgram.getRules().size(), 3,"ERROR");
            	
            	Rule rule = parsedProgram.getRules().get(0);
            	Assert.assertEquals(rule.getBody().getFormula().getFormulaType(), Formula.FormulaType.CONJUNCTION);
            	Assert.assertEquals(rule.getBody().getVariables().size(), 2,"ERROR");
            	Assert.assertEquals(rule.getHead().getVariables().size(), 1,"ERROR");
            	
            	rule = parsedProgram.getRules().get(1);
            	Assert.assertEquals(rule.getBody().getFormula().getFormulaType(), Formula.FormulaType.CONJUNCTION);
            	Assert.assertEquals(((Conjunction)rule.getBody().getFormula()).getFormulas().size(), 2,"ERROR");
            	Assert.assertEquals(rule.getBody().getVariables().size(), 3,"ERROR");
            	Assert.assertEquals(rule.getHead().getVariables().size(), 2,"ERROR");

            	rule = parsedProgram.getRules().get(2);
            	Assert.assertEquals(rule.getBody().getFormula().getFormulaType(), Formula.FormulaType.CONJUNCTION);
            	Assert.assertEquals(((Conjunction)rule.getBody().getFormula()).getFormulas().size(), 2,"ERROR");
            	Assert.assertEquals(rule.getBody().getVariables().size(), 2,"ERROR");
            	Assert.assertEquals(rule.getHead().getVariables().size(), 0,"ERROR");
            	Assert.assertTrue(rule.getHead().isInconsistency(),"ERROR");
            }
    	}.run();    	
	}
	
	@Test
	public void testProgramCompilation() throws Exception {
		/*
		 * 
		 */
    	new FacesRequest() {
            @Override
            protected void invokeApplication() {
            	System.out.println("ProgramTest.testProgramCompilation()");
            	ReasoningService reasoningService = (ReasoningService) Component.getInstance("kiwi.core.reasoningService");
            	//we have to switch online reasoning off already here because this test is in the same test suite with ReasoningTest and before ReasoningTest starts running
            	//some tasks may already be scheduled (triples added for the new anonymous user)
            	reasoningService.disableReasoning();
            	
            	StringProgramLoader programLoader = (StringProgramLoader) Component.getInstance("stringProgramLoader");
            	programLoader.addLine("@prefix rdf : <http://kiwi/rdf#>");
            	programLoader.addLine("@prefix rdfs: <http://kiwi/rdfsss#>");
            	programLoader.addLine("($1 http://kiwi/rdf#type $2) -> ($1 http://kiwi/rdf#type http://kiwi/rdf#Resource)");
            	programLoader.addLine("rdf-subclass: ($1 rdfs:subClassOf $2), ($2 rdfs:subClassOf $3) -> ($1 rdfs:subClassOf $3)");            	
            	programLoader.addLine("constraint: ($1 rdfs:subClassOf $2), ($2 rdf:type http://kiwi/nothing) -> inconsistency");            	
            	System.out.println(" rules loaded");

            	Program parsedProgram = programLoader.loadProgram("");

            	ProgramCompiler programCompiler = (ProgramCompiler) Component.getInstance("programCompiler");
            	
            	CompiledProgram compiledProgram = programCompiler.compileProgram(parsedProgram);

            	Assert.assertNotNull(compiledProgram);
            	
        		List<CompiledRule> compiledRules = compiledProgram.getRules();
        		
        		System.out.println(" rules compiled");
            	
            	Assert.assertEquals(compiledRules.size(), 3);
            	
            	CompiledRule r1 = compiledRules.get(0);
            	System.out.println("r1 = "+r1.getRule().toString());
            	CompiledRule r2 = compiledRules.get(1);
            	System.out.println("r2 = "+r2.getRule().toString());
            	CompiledRule r3 = compiledRules.get(2);
            	System.out.println("r3 = "+r3.getRule().toString());
            	
            	Assert.assertFalse(r1.isGround());
            	Assert.assertFalse(r1.isInstance());
            	Assert.assertFalse(r1.isConstraintRule());
            	Assert.assertEquals(r1.getSelectVariables().size(), 1);
            	Assert.assertFalse(r1.hasNoSharedVariables());
 
            	Assert.assertFalse(r2.isGround());
            	Assert.assertFalse(r2.isInstance());
            	Assert.assertFalse(r2.isConstraintRule());
            	Assert.assertEquals(r2.getSelectVariables().size(), 2);
            	Assert.assertFalse(r2.hasNoSharedVariables());
            	
            	Assert.assertFalse(r3.isGround());
            	Assert.assertFalse(r3.isInstance());
            	Assert.assertTrue(r3.isConstraintRule());
            	Assert.assertEquals(r3.getSelectVariables().size(), 2);
            	Assert.assertTrue(r3.hasNoSharedVariables());//that's why there are 2 select variables and not 0

            	System.out.println(" rules tested");
            }
    	}.run();    	
	}
	
	@Test
	public void testRuleBodyExecution() throws Exception {
		/*
		 * 
		 */
    	new FacesRequest() {
            @Override
            protected void invokeApplication() {
            	System.out.println("ProgramTest.testRuleBodyExecution()");
            	ReasoningService reasoningService = (ReasoningService) Component.getInstance("kiwi.core.reasoningService");
            	//we have to switch online reasoning off already here because this test is in the same test suite with ReasoningTest and before ReasoningTest starts running
            	//some tasks may already be scheduled (triples added for the new anonymous user)
            	reasoningService.disableReasoning();
            	
            	TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
            	tripleStore.clear();
            	tripleStore.clearCaches();
            	tripleStore = (TripleStore) Component.getInstance("tripleStore");
            	
            	KiWiUriResource user1 = tripleStore.createUriResource("http://kiwi/test/user1");
            	KiWiUriResource user2 = tripleStore.createUriResource("http://kiwi/test/user2");
            	KiWiUriResource rdftype = tripleStore.createUriResource("http://kiwi/rdf#type");
            	KiWiUriResource rdfssubclass = tripleStore.createUriResource("http://kiwi/rdfsss#subClassOf");
            	KiWiUriResource kiwiUser = tripleStore.createUriResource("http://kiwi/test/usertype");            	
            	KiWiUriResource kiwiResource = tripleStore.createUriResource("http://kiwi/test/resource");            	

            	KiWiUriResource class1 = tripleStore.createUriResource("http://kiwi/test/class1");
            	KiWiUriResource class2 = tripleStore.createUriResource("http://kiwi/test/class2");
            	KiWiUriResource class3 = tripleStore.createUriResource("http://kiwi/test/class3");

            	Assert.assertNotNull(tripleStore.createTriple(user1, rdftype, kiwiUser));
            	Assert.assertNotNull(tripleStore.createTriple(user2, rdftype, kiwiUser));

            	Assert.assertNotNull(tripleStore.createTriple(class1, rdfssubclass, class2));
            	Assert.assertNotNull(tripleStore.createTriple(class2, rdfssubclass, class3));
            	//Assert.assertNotNull(tripleStore.createTriple(class1, rdfssubclass, class2));

            	forcePersistence();
            	
            	StringProgramLoader programLoader = (StringProgramLoader) Component.getInstance("stringProgramLoader");;
            	programLoader.addLine("@prefix rdf : <http://kiwi/rdf#>");
            	programLoader.addLine("@prefix rdfs: <http://kiwi/rdfsss#>");
            	programLoader.addLine("($1 http://kiwi/rdf#type $2) -> ($1 http://kiwi/rdf#type http://kiwi/rdf#Resource)");
            	programLoader.addLine("rdf-subclass: ($1 rdfs:subClassOf $2), ($2 rdfs:subClassOf $3) -> ($1 rdfs:subClassOf $3)");            	
            	programLoader.addLine("constraint: ($1 rdfs:subClassOf $2), ($2 rdf:type http://kiwi/nothing) -> inconsistency");            	
            	System.out.println(" rules loaded");

            	Program parsedProgram = programLoader.loadProgram("");

            	ProgramCompiler programCompiler = (ProgramCompiler) Component.getInstance("programCompiler");
            	
            	CompiledProgram compiledProgram = programCompiler.compileProgram(parsedProgram);

            	Assert.assertNotNull(compiledProgram);
            	
        		List<CompiledRule> compiledRules = compiledProgram.getRules();
        		
        		System.out.println(" rules compiled");
            	
            	CompiledRule r1 = compiledRules.get(0);
            	System.out.println(r1.getName()+": "+r1.getQuery());
            	CompiledRule r2 = compiledRules.get(1);
            	System.out.println(r2.getName()+": "+r2.getQuery());
            	CompiledRule r3 = compiledRules.get(2);
            	System.out.println(r3.getName()+": "+r3.getQuery());
            	
            	RuleBodyProcessor ruleBodyProcessor = (RuleBodyProcessor) Component.getInstance("ruleBodyProcessor");;

            	List<RuleBodyResult> r1r = ruleBodyProcessor.process(r1);
            	Assert.assertEquals(r1r.size(), 2);
            	
            	List<RuleBodyResult> r2r = ruleBodyProcessor.process(r2);
            	Assert.assertEquals(r2r.size(), 1);
            	Assert.assertEquals(r2r.get(0).getTripleIds().size(), 2);
            	Assert.assertTrue(r2r.get(0).getBinding(new Variable("$1")) instanceof KiWiUriResource);
            	KiWiUriResource uri = (KiWiUriResource) r2r.get(0).getBinding(new Variable("$1"));
            	Assert.assertEquals(uri, class1);
            	uri = (KiWiUriResource) r2r.get(0).getBinding(new Variable("$2"));
            	Assert.assertNull(uri); //null because $2 is not a selectVariable
            	Assert.assertFalse(r2.getSelectVariables().contains(new Variable("$2")));
            	uri = (KiWiUriResource) r2r.get(0).getBinding(new Variable("$3"));
            	Assert.assertEquals(uri, class3);
            	
            	List<RuleBodyResult> r3r = ruleBodyProcessor.process(r3);
            	Assert.assertEquals(r3r.size(), 0);
            }
    	}.run();    	
	}
}

