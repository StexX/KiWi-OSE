package kiwi.test.service.query;

import static org.norecess.antlr.Assert.assertTree;
import kiwi.service.query.kwql.parser.KWQL;

import org.junit.Before;
import org.junit.Test;
import org.norecess.antlr.ANTLRTester;

/**
 * The Class KWQLParseTest.
 */
public class KWQLParseTest {

	private ANTLRTester myTester;

	@Before
	public void setUp() {
		myTester = new ANTLRTester(new IANTLRFrontEntImpl());
	}

	/**
	 * Test value queries.
	 */
	@Test
	public void testValues() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))))",
				myTester.scanInput("a").parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(OR(STRING(a))(STRING(b))))))))",
				myTester.scanInput("a OR b").parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(AND(STRING(a))(STRING(b))))))))",
				myTester.scanInput("a AND b").parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(AND(STRING(a))(STRING(b))))))))",
				myTester.scanInput("(a AND b)").parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(NOT(STRING(a))))))))",
				myTester.scanInput("NOT a").parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(NOT(STRING(a))))))))",
				myTester.scanInput("NOT (a)").parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(NOT(OR(STRING(a))(STRING(b)))))))))",
				myTester.scanInput("NOT (a OR b)").parseAs("kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)" +
				"(OR(STRING(a))(AND(STRING(b))(STRING(c)))))))))", myTester
						.scanInput("a OR (b AND c)").parseAs("kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(OR(STRING(a))" +
				"(AND(STRING(b))(STRING(c)))))))))", myTester
						.scanInput("(a OR (b AND c))").parseAs("kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(OR(STRING(a))" +
				"(AND(STRING(b))(NOT(STRING(c))))))))))", myTester
						.scanInput("(a OR (b AND NOT c))").parseAs("kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)" +
				"(NOT(OR(STRING(a))(AND(STRING(b))(NOT(STRING(c)))))))))))", myTester
						.scanInput("(NOT (a OR (b AND NOT c)))")
						.parseAs("kwqlrule"));
	}

	/**
	 * Test complex queries.
	 */
	@Test
	public void testQueries() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(author))(STRING(Mary)))))))",
				myTester.scanInput("ci(author:Mary)").parseAs("kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(author))(NOT(STRING(name))))))))", myTester.scanInput(
						"ci(author:NOT name)").parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(RESOURCE(TYPE(link))(AND(QUALIFIER(LABEL(target))" +
				"(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(title))(STRING(bla)))))(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(name))(STRING(bla))))))))))",
				myTester.scanInput(
						"ci(link(target:ci(title:bla) tag(name:bla)))")
						.parseAs("kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(RESOURCE(TYPE(fragment))" +
				"(QUALIFIER(LABEL(text))(STRING(Java))))))))",
				myTester.scanInput("ci(fragment(text:Java))").parseAs("kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(author))(STRING(\"Mary\"))))))))",
				myTester.scanInput("ci(tag(author:\"Mary\"))").parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(name))(STRING(Java))))(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(name))(STRING(XML)))))))))",
				myTester.scanInput("ci(tag(name:Java) tag(name:XML))").parseAs(
						"kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(name))(STRING(Java))))(QUALIFIER(LABEL(child))(RESOURCE(TYPE(ci))" +
				"(QUALIFIER(LABEL(author))(STRING(XML))))))))))",
				myTester.scanInput("ci(tag(name:Java) child:ci(author:XML))")
						.parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(OR(QUALIFIER(LABEL(author))(STRING(Mary)))" +
				"(RESOURCE(TYPE(tag))(QUALIFIER(LABEL(name))(STRING(Java)))))))))",
				myTester.scanInput("author:Mary OR tag(name:Java)").parseAs(
						"kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(text))(AND(STRING(Java))" +
				"(NOT(STRING(XML)))))))))", myTester
						.scanInput("ci(text:(Java AND NOT XML))").parseAs(
								"kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(name))(STRING(AND))))))))", myTester
						.scanInput("ci(tag(name:\\AND))").parseAs("kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(QUALIFIER(LABEL(author))" +
				"(VAR(A)))(QUALIFIER(LABEL(title))(VAR(T))))))))",
				myTester.scanInput("ci(author:$A title:$T)").parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(name))(STRING(a))(VAR(s)))))))",
				myTester.scanInput("ci(name:a -> $s)").parseAs("kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(name))" +
				"(OR(STRING(a))(STRING(b)))(VAR(s)))))))", myTester
						.scanInput("ci(name:a OR b -> $s)").parseAs("kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(name))" +
				"(OR(STRING(a))(STRING(b)))(VAR(s)))))))", myTester
						.scanInput("ci(name:(a OR b) -> $s)").parseAs("kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL)(NOT(OR(STRING(a))(STRING(b)))))))))"
				, myTester.scanInput(
						"ci(NOT (a OR b))").parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(AND(QUALIFIER(LABEL(text))" +
				"(STRING(Java)))(QUALIFIER(LABEL(author))(VAR(X))))(OPTIONAL(aa)(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(name))(VAR(Y))))))))))",
				myTester.scanInput(
						"ci(text:Java author:$X OPTIONAL(aa) tag(name:$Y))")
						.parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(QUALIFIER(LABEL(author))(VAR(x)))" +
				"(QUALIFIER(LABEL)(OR(STRING(bla))(STRING(blub)))))))))",
				myTester.scanInput("ci(author:$x AND bla OR blub)").parseAs(
						"kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(OR(AND(QUALIFIER(LABEL(author))(VAR(x)))" +
				"(QUALIFIER(LABEL)(STRING(bla))))(QUALIFIER(LABEL)(STRING(blub))))))))",
				myTester.scanInput("ci((author:$x AND bla) OR blub) ").parseAs(
						"kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL(author))(STRING(\"a b\")))))))",
				myTester.scanInput("author:\"a b\"").parseAs("kwqlrule"));
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(AND(QUALIFIER(LABEL(author))(STRING(a)))" +
				"(QUALIFIER(LABEL)(STRING(b))))))))",
				myTester.scanInput("author:a b").parseAs("kwqlrule"));
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL(author))(AND(STRING(a))(STRING(b))))))))",
				myTester.scanInput("author:(a b)").parseAs("kwqlrule"));
	}


	@Test
	public void formerlyFaultyQuery() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(title))(STRING(a)))))" +
				"(SEL_QUERY(AND(CI_BODY(RESOURCE(TYPE(ci))(AND(QUALIFIER(LABEL(author))" +
				"(NOT(STRING(name))))(QUALIFIER(LABEL)(STRING(bla))))))(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)" +
				"(STRING(bla))))))))",
				myTester.scanInput("ci(title:a)@ci(author:NOT name bla) bla").parseAs("kwqlrule"));
	}

	@Test
	public void tagUri() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(URI))(UR(http://www.bla.com))))))))",
				myTester.scanInput("ci(tag(URI:http://www.bla.com))").parseAs(
						"kwqlrule"));
	}

	@Test
	public void test7() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(URI))(UR(http://www.bla.com:8080/wusch))))))))",
				myTester
						.scanInput("ci(tag(URI:http://www.bla.com:8080/wusch))")
						.parseAs("kwqlrule"));
	}

	@Test(expected = AssertionError.class)
	public void test5() {
		assertTree(KWQL.RULE, "(RULE(HEAD)(CI_BODY(RESOURCE(TYPE(ci)))))",
				myTester.scanInput("ci()").parseAs("kwqlrule"));
	}

	@Test
	public void test6() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(QUALIFIER(LABEL(author))" +
				"(STRING(a)))(QUALIFIER(LABEL)(STRING(b))))))))",
				myTester.scanInput("ci(author:a b)").parseAs("kwqlrule"));
	}

	@Test
	public void test8() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(NOT(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(name))(STRING(bla)))))))))",
				myTester.scanInput("ci(NOT(tag(name:bla)))").parseAs("kwqlrule"));
	}

	@Test
	public void test9() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(numberEd))(INTEGER(4)))))))",
				myTester.scanInput("ci(numberEd:4)").parseAs("kwqlrule"));
	}

	@Test
	public void test10() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(QUALIFIER(LABEL(title))" +
				"(STRING(StartPage)))(RESOURCE(TYPE(link))(QUALIFIER(LABEL(target))(RESOURCE(TYPE(ci))" +
				"(QUALIFIER(LABEL(title))(STRING(Liste)))))))))))",
				myTester.scanInput(
						"ci(title:StartPage link(target:ci(title:Liste)))")
						.parseAs("kwqlrule"));
	}

	@Test
	public void test11() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(AND(QUALIFIER(LABEL(title))" +
				"(STRING(bla)))(QUALIFIER(LABEL(child))(RESOURCE(TYPE(fragment))(QUALIFIER(LABEL(text))" +
				"(STRING(blub))))))(QUALIFIER(LABEL(title))(STRING(bla))))))))",
				myTester.scanInput(
						"ci(title:bla child:fragment(text:blub) title:bla)")
						.parseAs("kwqlrule"));
	}

	@Test
	public void test12() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(AND(QUALIFIER(LABEL(title))" +
				"(STRING(bla)))(QUALIFIER(LABEL(child))(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(text))(STRING(blub))))))" +
				"(QUALIFIER(LABEL(title))(STRING(bla))))))))",
				myTester.scanInput(
						"ci(title:bla child:ci(text:blub) title:bla)").parseAs(
						"kwqlrule"));
	}

	@Test
	public void test13() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(QUALIFIER(LABEL(title))" +
				"(STRING(bla)))(QUALIFIER(LABEL(child))(RESOURCE(TYPE(ci))(AND(QUALIFIER(LABEL(text))" +
				"(STRING(blub)))(QUALIFIER(LABEL(title))(STRING(bla)))))))))))",
				myTester.scanInput(
						"ci(title:bla child:ci(text:blub title:bla))").parseAs(
						"kwqlrule"));
	}

	@Test
	public void test14() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(QUALIFIER(LABEL(title))" +
				"(STRING(bla)))(QUALIFIER(LABEL(child))(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a))))))))))",
				myTester.scanInput("ci(title:bla child:a)").parseAs("kwqlrule"));
	}

	@Test
	public void test15() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(AND(QUALIFIER(LABEL(title))" +
				"(STRING(bla)))(QUALIFIER(LABEL(child))(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a))))))" +
				"(QUALIFIER(LABEL)(STRING(b))))))))", myTester
						.scanInput("ci(title:bla child:a b)").parseAs("kwqlrule"));
	}

	@Test
	public void test16() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(AND(QUALIFIER(LABEL(title))(STRING(bla)))" +
				"(QUALIFIER(LABEL(child))(RESOURCE(TYPE)(QUALIFIER(LABEL)(AND(STRING(a))(STRING(b)))))))" +
				"(QUALIFIER(LABEL)(STRING(c))))))))",
				myTester.scanInput("ci(title:bla child:(a b) c)").parseAs(
						"kwqlrule"));
	}

	@Test
	public void test17() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(QUALIFIER(LABEL(title))" +
				"(STRING(bla)))(QUALIFIER(LABEL(child))(RESOURCE(TYPE)(QUALIFIER(LABEL(text))(STRING(b))))))))))",
				myTester.scanInput("ci(title:bla child:text:b)")
						.parseAs("kwqlrule"));
	}

	@Test
	public void test18() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(QUALIFIER(LABEL(title))(STRING(StartPage)))" +
				"(RESOURCE(TYPE(link))(QUALIFIER(LABEL(target))(OR(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(title))" +
				"(STRING(Orphan))))(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(title))(STRING(Liste))))))))))))",
				myTester
						.scanInput(
								"ci(title:StartPage link(target:(ci(title:Orphan) OR ci(title:Liste))))")
						.parseAs("kwqlrule"));
	}

	@Test
	public void test19() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL)(STRING(\"bla:wusch:::blawusch\")))))))",
				myTester.scanInput("ci(\"bla:wusch:::blawusch\")").parseAs(
						"kwqlrule"));
	}

	@Test
	public void test20() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))" +
				"(QUALIFIER(LABEL(created))(DATE(2009-09-23T14:42:11Z)))))))",
				myTester.scanInput("ci(created:2009-09-23T14:42:11Z)").parseAs(
						"kwqlrule"));
	}

	@Test
	public void test21() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(title))(STRING('bla')))))))",
				myTester.scanInput("ci(title:'bla')").parseAs("kwqlrule"));
	}

	@Test
	public void test22() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(OR(RESOURCE(TYPE(ci))(QUALIFIER(LABEL)(STRING(a))))" +
				"(RESOURCE(TYPE(ci))(QUALIFIER(LABEL)(STRING(s))))))))", myTester
						.scanInput("ci(a) OR ci(s)").parseAs("kwqlrule"));
	}

	@Test
	public void test23() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(OR(RESOURCE(TYPE(ci))(QUALIFIER(LABEL)(STRING(a))))" +
				"(RESOURCE(TYPE)(RESOURCE(TYPE(tag))(QUALIFIER(LABEL)(STRING(s)))))))))",
				myTester.scanInput("ci(a) OR tag(s)").parseAs("kwqlrule"));
	}

	@Test
	public void test24() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(AND(RESOURCE(TYPE(tag))(QUALIFIER(LABEL)(STRING(a))))(RESOURCE(TYPE(tag))(QUALIFIER(LABEL)(STRING(s)))))))))",
				myTester.scanInput("tag(a) AND tag(s)").parseAs("kwqlrule"));
	}

	@Test
	public void test25() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(OR(RESOURCE(TYPE(tag))(QUALIFIER(LABEL)(STRING(a))))" +
				"(RESOURCE(TYPE(tag))(QUALIFIER(LABEL)(STRING(s)))))))))",
				myTester.scanInput("tag(a) OR tag(s)").parseAs("kwqlrule"));
	}

	@Test
	public void test26() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(OR(RESOURCE(TYPE(ci))(QUALIFIER(LABEL)(STRING(a))))(RESOURCE(TYPE(ci))" +
				"(QUALIFIER(LABEL)(STRING(s))))))))",
				myTester.scanInput("ci(a) OR ci(s)").parseAs("kwqlrule"));
	}

	@Test()
	public void test23a() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(title))(STRING(a)))))" +
				"(SEL_QUERY(AND(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL)(STRING(a)))))" +
				"(CI_BODY(RESOURCE(TYPE)(RESOURCE(TYPE(tag))(QUALIFIER(LABEL)(STRING(s)))))))))",
				myTester.scanInput("ci(title:a)@ci(a) AND tag(s)").parseAs("kwqlrule"));
	}

	@Test()
	public void test24a() {
		
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(title))(STRING(a)))))" +
				"(SEL_QUERY(AND(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL)(STRING(a)))))" +
				"(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL)(STRING(s))))))))",
				myTester.scanInput("ci(title:a)@ci(a) AND ci(s)").parseAs("kwqlrule"));
	}

	@Test()
	public void test25a() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(title))(STRING(a)))))" +
				"(SEL_QUERY(AND(CI_BODY(RESOURCE(TYPE)(RESOURCE(TYPE(tag))(QUALIFIER(LABEL)(STRING(a))))))" +
				"(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL)(STRING(s))))))))",
				myTester.scanInput("ci(title:a)@tag(a) AND ci(s)").parseAs("kwqlrule"));
	}

	@Test
	public void test27() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(NOT(STRING(a))))))))",
				myTester.scanInput("NOT(a)").parseAs("kwqlrule"));
	}

	@Test
	public void test28() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(NOT(QUALIFIER(LABEL(title))(STRING(a))))))))",
				myTester.scanInput("NOT(title:a)").parseAs("kwqlrule"));
	}

	@Test
	public void test29() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(NOT(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(title))(STRING(a))))))))",
				myTester.scanInput("NOT(ci(title:a))").parseAs("kwqlrule"));
	}

	@Test
	public void test30() {
		assertTree(KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(NOT(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(title))(STRING(a)))))))))", myTester
						.scanInput("NOT(tag(title:a))").parseAs("kwqlrule"));
	}

	@Test
	public void test31() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(NOT(OR(RESOURCE(TYPE(tag))(QUALIFIER(LABEL(name))" +
				"(STRING(a))))(QUALIFIER(LABEL(title))(STRING(a)))))))))", myTester
						.scanInput("NOT(tag(name:a) OR title:a)").parseAs(
								"kwqlrule"));
	}

	@Test
	public void test32() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(NOT(AND(RESOURCE(TYPE(tag))(QUALIFIER(LABEL(name))" +
				"(STRING(a))))(RESOURCE(TYPE(tag))(QUALIFIER(LABEL(name))(STRING(b))))))))))", myTester
						.scanInput("NOT(tag(name:a) AND tag(name:b))").parseAs(
								"kwqlrule"));
	}

	@Test
	public void test33() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(NOT(AND(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL)(STRING(a))))(NOT(AND(RESOURCE(TYPE(tag))(QUALIFIER(LABEL)(STRING(b))))" +
				"(RESOURCE(TYPE(tag))(QUALIFIER(LABEL)(STRING(c))))))))))))", myTester
						.scanInput("NOT(tag(a) AND NOT(tag(b) AND tag(c)))")
						.parseAs("kwqlrule"));
	}
	
	@Test
	public void test39() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(title))(STRING(a)))))" +
				"(SEL_QUERY(AND(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(author))(STRING(Mary)))))" +
				"(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(XML))))))))", myTester
						.scanInput("ci(title:a)@ci(author:Mary) AND XML")
						.parseAs("kwqlrule"));
	}

	/**
	 * Faulty query3.
	 */
	@Test(expected = AssertionError.class)
	public void faultyQuery3() {
		myTester.scanInput("NOT(NOT(ci(a)))").parseAs("kwqlrule");
	}

	/**
	 * Faulty query4.
	 */
	@Test(expected = AssertionError.class)
	public void faultyQuery4() {
		myTester.scanInput("ci(title:ci(a))").parseAs("kwqlrule");
	}

	@Test
	public void test34() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE)(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(name))(STRING(fragment))))))))",
				myTester.scanInput("tag(name:\\fragment)").parseAs("kwqlrule"));
	}

	@Test
	public void test35() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(AND(AND(QUALIFIER(LABEL(text))" +
				"(STRING(Java)))(QUALIFIER(LABEL(author))(VAR(X))))(OPTIONAL(s)(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(name))(VAR(Y))))))))))",
				myTester.scanInput(
						"ci(text:Java author:$X OPTIONAL(s) tag(name:$Y))")
						.parseAs("kwqlrule"));
	}
	
	@Test
	public void test36() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_BODY(RESOURCE(TYPE(ci))(NOT(AND(QUALIFIER(LABEL(title))" +
				"(STRING(bla)))(RESOURCE(TYPE(tag))(QUALIFIER(LABEL(name))(STRING(blub))))))))))",
				myTester.scanInput("ci(NOT(title:bla AND tag(name:blub)))").parseAs("kwqlrule"));
	}
	
	@Test
	public void test37() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE(ci))(NOT(AND(QUALIFIER(LABEL(title))" +
				"(STRING(bla)))(RESOURCE(TYPE(tag))(QUALIFIER(LABEL(name))(STRING(blub))))))))" +
				"(BOOL_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL)(STRING(b))))))))",
				myTester.scanInput("ci(NOT(title:bla AND tag(name:blub))) BOOLEAN(ci(b))").parseAs("kwqlrule"));
	}
	
	@Test
	public void test38() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE(ci))(NOT(AND(QUALIFIER(LABEL(title))(STRING(bla)))" +
				"(RESOURCE(TYPE(tag))(QUALIFIER(LABEL(name))(STRING(blub))))))))(BOOL_BODY(RESOURCE(TYPE(ci))" +
				"(QUALIFIER(LABEL)(STRING(b))))))))",
				myTester.scanInput("(ci(NOT(title:bla AND tag(name:blub))) BOOLEAN(ci(b)))").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest1() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(b))))))))",
				myTester.scanInput("a AND BOOLEAN(b)").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest2() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)" +
				"(STRING(a)))))(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(NOT(STRING(b)))))))))",
				myTester.scanInput("a AND BOOLEAN(NOT b)").parseAs("kwqlrule"));
	}

	@Test
	public void globalTest3() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(NOT(RESOURCE(TYPE)(QUALIFIER(LABEL)(NOT(STRING(b))))))))))",
				myTester.scanInput("a AND NOT BOOLEAN(NOT b)").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest4() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(OR(NOT(RESOURCE(TYPE)(QUALIFIER(LABEL)(NOT(STRING(b))))))(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(c)))))))))",
				myTester.scanInput("a AND NOT BOOLEAN(NOT b) OR BOOLEAN(c)").parseAs("kwqlrule"));
	}	
	
	@Test
	public void globalTest5() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(AND(NOT(RESOURCE(TYPE)(QUALIFIER(LABEL)(NOT(STRING(b))))))(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(c)))))))))",
				myTester.scanInput("a AND NOT BOOLEAN(NOT b) BOOLEAN(c)").parseAs("kwqlrule"));
	}	
	
	@Test
	public void globalTest6() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(NOT(OR(RESOURCE(TYPE)(QUALIFIER(LABEL)(NOT(STRING(b)))))(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(c))))))))))",
				myTester.scanInput("a AND NOT (BOOLEAN(NOT b) OR BOOLEAN(c))").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest7() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(AND(NOT(OR(RESOURCE(TYPE)(QUALIFIER(LABEL)(NOT(STRING(b)))))" +
				"(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(c))))))(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(f)))))))))",
				myTester.scanInput("a AND NOT (BOOLEAN(NOT b) OR BOOLEAN(c)) AND BOOLEAN(f)").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest8() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(NOT(RESOURCE(TYPE)(QUALIFIER(LABEL)(NOT(STRING(b))))))(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(b))))))))",
				myTester.scanInput("NOT BOOLEAN(NOT b) a AND BOOLEAN(b)").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest9() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(c))))(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(r))))))))",
				myTester.scanInput("BOOLEAN(c) AND a AND BOOLEAN(r)").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest10() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(NOT(RESOURCE(TYPE)(QUALIFIER(LABEL)(NOT(STRING(b))))))(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(r))))))))",
				myTester.scanInput("NOT BOOLEAN(NOT b) AND a AND BOOLEAN(r)").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest11() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(AND(OR(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(b))))))(CI_COMP(CI_BODY(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(b)))))(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))))" +
				"(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(d))))))))",
				myTester.scanInput("(a BOOLEAN(b) OR b BOOLEAN(a)) BOOLEAN(d)").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest12() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(OR(OR(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(b))))))(CI_COMP(CI_BODY(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(d)))))(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(c)))))))" +
				"(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(h)))))(BOOL_BODY(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(f)))))))))",
				myTester.scanInput("a BOOLEAN(b) OR d BOOLEAN(c) OR (BOOLEAN(f) h)").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest13() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(OR(AND(OR(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(b))))))(CI_COMP(CI_BODY(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(b)))))(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(c)))))))" +
				"(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(d))))))(CI_BODY(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(d))))))))",
				myTester.scanInput("(a BOOLEAN(b) OR b BOOLEAN(c)) BOOLEAN(d) OR d").parseAs("kwqlrule"));
	}

	@Test
	public void globalTest14() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(AND(OR(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(b))))))(CI_COMP(CI_BODY(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(b)))))(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(c)))))))" +
				"(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(dd))))))))",
				myTester.scanInput("BOOLEAN(dd)(a BOOLEAN(b) OR b BOOLEAN(c))").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest15() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE(ci))(QUALIFIER(LABEL(text))(VAR(n)))))" +
				"(BOOL_BODY(RESOURCE(TYPE(ci))(AND(QUALIFIER(LABEL(title))(VAR(n)))(RESOURCE(TYPE(tag))" +
				"(QUALIFIER(LABEL(name))(STRING(KiWi))))))))))",
				myTester.scanInput("ci(text:$n) BOOLEAN(ci(title:$n tag(name:KiWi)))").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest16() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(AND(OR(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(b))))))(CI_COMP(CI_BODY(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(b)))))(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(c)))))))" +
				"(BOOL_BODY(AND(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(dsd))))(RESOURCE(TYPE)(QUALIFIER(LABEL)" +
				"(STRING(dd)))))))))",
				myTester.scanInput("BOOLEAN(dsd) BOOLEAN(dd)(a BOOLEAN(b) OR b BOOLEAN(c))").parseAs("kwqlrule"));
	}

	@Test
	public void globalTest17() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(OR(STRING(a))" +
				"(STRING(c))))))(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(g))))))))",
				myTester.scanInput("(a OR c) AND BOOLEAN(g)").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest18() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(OR(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(a)))))" +
				"(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(c)))))(BOOL_BODY(RESOURCE(TYPE)" +
				"(QUALIFIER(LABEL)(STRING(g)))))))))",
				myTester.scanInput("a OR (c AND BOOLEAN(g))").parseAs("kwqlrule"));
	}
	
	@Test
	public void globalTest19() {
		assertTree(
				KWQL.RULE,
				"(RULE(HEAD)(CI_QUERY(CI_COMP(CI_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(OR(STRING(a))" +
				"(STRING(c))))))(BOOL_BODY(RESOURCE(TYPE)(QUALIFIER(LABEL)(STRING(g))))))))",
				myTester.scanInput("a OR c AND BOOLEAN(g)").parseAs("kwqlrule"));
	}
}