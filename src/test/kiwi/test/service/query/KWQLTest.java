package kiwi.test.service.query;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import kiwi.service.query.kwql.KWQLConstraintViolationException;
import kiwi.service.query.kwql.KWQLQuery;

import org.antlr.runtime.RecognitionException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The Class KWQLConstraintTest.
 */
public class KWQLTest {

	private KWQLQuery optQuery;
	private KWQLQuery valQuery;
	private KWQLQuery qualQuery;
	private KWQLQuery varQuery;
	private KWQLQuery varvalQuery;

	@Before
	public void setUp() throws Exception {
		optQuery = new KWQLQuery(
				"ci(text:Java author:$X OPTIONAL(none) tag(name:$Y))");
		valQuery = new KWQLQuery("a");
		qualQuery = new KWQLQuery("author:Mary XML");
		varQuery = new KWQLQuery("author:$x");
		varvalQuery = new KWQLQuery("author:a -> $x");
	}

	/**
	 * Test parsing a query.
	 * 
	 * @throws KWQLConstraintViolationException
	 * @throws RecognitionException
	 */
	@Test
	public void testParseQuery() throws RecognitionException,
			KWQLConstraintViolationException {
		assertEquals(
				optQuery.toTree().toStringTree(),
				"(RULE HEAD (CI_QUERY (CI_BODY (RESOURCE (TYPE ci) (AND (AND (QUALIFIER (LABEL text) (STRING Java))" +
				" (QUALIFIER (LABEL author) (VAR X))) (OPTIONAL none (RESOURCE (TYPE tag) (QUALIFIER (LABEL name) " +
				"(VAR Y)))))))))");

	}

	/**
	 * Test query validation.
	 * 
	 * @throws KWQLConstraintViolationException
	 * @throws RecognitionException
	 */
	@Test
	public void testValidate() throws RecognitionException,
			KWQLConstraintViolationException {
		assertTrue(optQuery.getValid());
		assertTrue(valQuery.getValid());
		assertTrue(qualQuery.getValid());
		assertTrue(varQuery.getValid());
		assertTrue(varvalQuery.getValid());
	}

	/**
	 * Test keyword type validation.
	 * 
	 * @throws KWQLConstraintViolationException
	 * @throws RecognitionException
	 */
	@Test
	public void testFailVarTypeValidate2() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery fQuery = new KWQLQuery("ci(URI:8)");
		assertTrue(fQuery.getValid());
	}

	/**
	 * Test qualifier count validation.
	 * @throws KWQLConstraintViolationException 
	 * @throws RecognitionException 
	 * 
	 * @throws KWQLConstraintViolationException
	 * @throws RecognitionException
	 */
	@Test(expected=KWQLConstraintViolationException.class) 
	public void testFailQualifierCountValidate() throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery fQuery;
			fQuery = new KWQLQuery("ci(title:a title:b)");
	}

	/**
	 * Test qualifier count validation.
	 * @throws KWQLConstraintViolationException 
	 * @throws RecognitionException 
	 * 
	 * @throws KWQLConstraintViolationException
	 * @throws RecognitionException
	 */
	@Test(expected=KWQLConstraintViolationException.class) 
	public void testFailQualifierCountValidate2() throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery fQuery;
			fQuery = new KWQLQuery("title:a title:b");
	}

	/**
	 * Test resource-qualifier matching validation.
	 * @throws KWQLConstraintViolationException 
	 * @throws RecognitionException 
	 * 
	 * @throws KWQLConstraintViolationException
	 * @throws RecognitionException
	 */
	@Test(expected=KWQLConstraintViolationException.class) 
	public void testFailResQualValidate() throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery fQuery;
			fQuery = new KWQLQuery("ci(name:a)");
	}

	/**
	 * Test resource-qualifier matching validation.
	 * @throws KWQLConstraintViolationException 
	 * @throws RecognitionException 
	 * 
	 * @throws KWQLConstraintViolationException
	 * @throws RecognitionException
	 */
	@Test(expected=KWQLConstraintViolationException.class) 
	public void testFailResQualValidate2() throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery fQuery;
			fQuery = new KWQLQuery("ci(title:a child:tag(title:b))");
	}

	/**
	 * Test resource nesting validation.
	 * @throws KWQLConstraintViolationException 
	 * @throws RecognitionException 
	 * 
	 * @throws KWQLConstraintViolationException
	 * @throws RecognitionException
	 */
	@Test(expected=KWQLConstraintViolationException.class) 
	public void testFailResNestingValidate() throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery fQuery;
			fQuery = new KWQLQuery("tag(fragment(a))");
	}

	/**
	 * Test resource nesting validation.
	 * @throws KWQLConstraintViolationException 
	 * @throws RecognitionException 
	 * 
	 * @throws KWQLConstraintViolationException
	 * @throws RecognitionException
	 */
	@Test(expected=KWQLConstraintViolationException.class) 
	public void testFailResNestingValidate2() throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery fQuery;
			fQuery = new KWQLQuery("ci(child:tag(link(a)))");
	}

	/*
	 * Test variable matching validation.
	 * 
	 * @throws KWQLConstraintViolationException
	 * 
	 * @throws RecognitionException
	 */
	@Ignore
	@Test(expected=KWQLConstraintViolationException.class) 
	public void testFailVariablesValidate2() throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery fQuery;
			fQuery = new KWQLQuery("$a@title:$t");
	}

	/**
	 * Test conversion to string.
	 */
	@Test
	public void testToString() {
		assertEquals(optQuery.toString(),
				"ci(text:Java author:$X OPTIONAL(none) tag(name:$Y))");
	}

	@Test
	public void resourceQualifiers() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery fQuery = new KWQLQuery(
				"ci(title:bla link(target:ci(title:bla)))");
		assertTrue(fQuery.getValid());
	}

	@Test
	public void resourceQualifiers2() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery fQuery = new KWQLQuery("ci(title:bla child:ci(title:blub))");
		assertTrue(fQuery.getValid());
	}

	@Test(expected=KWQLConstraintViolationException.class) 
	public void resourceQualifiers3() throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery fQuery;
			fQuery = new KWQLQuery(
					"ci(title:bla child:fragment(text:blub) title:bla)");
	}

	@Test
	public void test11() throws KWQLConstraintViolationException,
			RecognitionException {
		KWQLQuery q = new KWQLQuery(
				"ci((tag(author:admin name:bla) link(target:ci(title:Liste tag(name:nirgendwo)))) OR title:wusch)");
		assertTrue(q.getValid());
	}

	@Test
	public void test12() throws KWQLConstraintViolationException,
			RecognitionException {
		KWQLQuery q = new KWQLQuery(
				"ci(title:StartPage link(target:(ci(title:Orphan) OR ci(title:Liste))))");
		assertTrue(q.getValid());
	}

	@Test
	public void test13() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery(
				"ci(tag(NOT(name:(wusch OR nirgendwo))) OR StartPage)");
		assertTrue(q.getValid());
	}

	@Test
	public void test14() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("ci(title:'test')");
		assertTrue(q.getValid());
	}

	@Test
	public void test15() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("ci(title:(test -> $t))");
		assertTrue(q.getValid());
	}

	@Ignore
	@Test
	public void test16() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("ci(child:ci(title:test) -> $c)");
		assertTrue(q.getValid());
	}

	@Ignore
	@Test
	public void test17() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("ci(child:(ci(title:test) -> $c))");
		assertTrue(q.getValid());
	}

	@Test
	public void test18mm() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("ci(title:neu) @ ci(text:$t)");
		assertTrue(q.getValid());
	}

	@Test
	public void test18() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("ci(title:$t AND text:$t) @ ci(text:$t)");
		assertTrue(q.getValid());
	}

	@Test
	public void test19() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery(
				"ci((title:bla) OR (title:muh AND author:blub))");
		assertTrue(q.getValid());
	}

	@Test(expected=KWQLConstraintViolationException.class) 
	public void test20() throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery q;
			q = new KWQLQuery(
					"ci((title:bla) AND (title:muh AND author:blub))");
	}

	@Test
	public void test21() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("ci((title:bla) OR (muh AND title:blub))");
		assertTrue(q.getValid());
	}

	@Test
	public void test22() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery(
				"ci((title:bla) OR (tag(muh) AND title:blub))");
		assertTrue(q.getValid());
	}

	@Test
	public void test23() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery(
				"ci((title:bla) OR (title:blub AND tag(muh)))");
		assertTrue(q.getValid());
	}

	@Test(expected=KWQLConstraintViolationException.class) 
	public void test24() throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery q;
			q = new KWQLQuery(
					"ci((title:bla) OR (muh AND title:blub) AND title:meh)");
	}

	@Test
	public void test25() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery(
				"ci((title:bla) OR ((tag(name:e)) AND title:meh))");
		assertTrue(q.getValid());
	}

	@Test
	public void test26() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("ci(NOT(tag(a)))");
		assertTrue(q.getValid());
	}

	@Test
	public void test27() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("NOT(ci(a))");
		assertTrue(q.getValid());
	}

	@Test
	public void test29() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("NOT (ci(a) OR ci(b))");
		assertTrue(q.getValid());
	}

	@Test
	public void test30() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("NOT(NOT ci(NOT(title:NOT b)) OR ci(b))");
		assertTrue(q.getValid());
	}

	@Test
	public void test31() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("NOT(NOT(ci(NOT(title:NOT b))) OR b)");
		assertTrue(q.getValid());
	}

	@Test
	public void test32() throws RecognitionException,
			KWQLConstraintViolationException {
		KWQLQuery q = new KWQLQuery("ci(NOT(tag(bla)))");
		assertTrue(q.getValid());
	}

	@Test(expected=KWQLConstraintViolationException.class) 
	public void test32a() throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery q;
			q = new KWQLQuery("ci(NOT(tag:bla)))");
	}

	@Test(expected=KWQLConstraintViolationException.class) 
	public void test33() throws RecognitionException, KWQLConstraintViolationException {
		KWQLQuery q;
			q = new KWQLQuery("ci(title:bla))");
			assertTrue(q.getValid());
	}

	@Test
	public void test34() throws RecognitionException,
			KWQLConstraintViolationException {
		// fragment nicht fragEment
		KWQLQuery q = new KWQLQuery(
				"ci(title:StartPage fragement(author:bla))");
		assertTrue(q.getValid());
	}

	@Test
	public void test35() throws Exception {
		KWQLQuery q = new KWQLQuery(
				"ci((title:StartPage OR title:untagged) AND URI:$u)");
		assertTrue(q.getValid());
	}

	@Test
	public void test36b() throws Exception {
		KWQLQuery q = new KWQLQuery(
				"ci(title:(StartPage OR nested) NOT(tag(name:$t) tag(name:$t)))");
		assertTrue(q.getValid());
	}

	@Test
	public void test36a() throws Exception {
		KWQLQuery q = new KWQLQuery("ci(NOT(tag(name:$t) tag(name:$t)))");
		assertTrue(q.getValid());
	}

@Test(expected=KWQLConstraintViolationException.class) 
public void test37() throws RecognitionException, KWQLConstraintViolationException {
	KWQLQuery q;
		q = new KWQLQuery("ci(name:$d) BOOLEAN(ci(title:$s))");

}

@Test(expected=KWQLConstraintViolationException.class) 
public void test38() throws RecognitionException, KWQLConstraintViolationException {
	KWQLQuery q;
		q = new KWQLQuery("ci(title:$d)@ci(title:$d) BOOLEAN(ci(name:$s))");

}

@Test(expected=KWQLConstraintViolationException.class) 
public void test39() throws RecognitionException, KWQLConstraintViolationException {
	KWQLQuery q;
		q = new KWQLQuery("ci(name:a)@ci(title:$d) BOOLEAN(ci(title:$s))");

}

@Test
public void test40() throws RecognitionException, KWQLConstraintViolationException {
	KWQLQuery q;
		q = new KWQLQuery("ci(tag(name:a))@ci(title:$d) BOOLEAN(ci(title:$s))");
		assertTrue(q.getValid());
}

@Test(expected=KWQLConstraintViolationException.class) 
public void test41() throws RecognitionException, KWQLConstraintViolationException {
	KWQLQuery q;
		q = new KWQLQuery("tag(ci(name:a))@ci(title:$d) BOOLEAN(ci(title:$s))");

}

@Test(expected=KWQLConstraintViolationException.class) 
public void test42() throws RecognitionException, KWQLConstraintViolationException {
	KWQLQuery q;
		q = new KWQLQuery("tag(ci(title:a))@ci(title:$d) BOOLEAN(ci(title:$s))");

}

}
