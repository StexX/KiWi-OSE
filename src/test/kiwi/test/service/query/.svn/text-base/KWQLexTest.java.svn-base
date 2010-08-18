package kiwi.test.service.query;

import static org.norecess.antlr.Assert.assertToken;
import static org.norecess.antlr.Assert.refuteToken;
import kiwi.service.query.kwql.parser.KWQLexer;

import org.junit.Before;
import org.junit.Test;
import org.norecess.antlr.ANTLRTester;

/**
 * The Class KWQLexTest.
 */

public class KWQLexTest {

	private ANTLRTester myTester;

	@Before
	public void setUp() {
		myTester = new ANTLRTester(new IANTLRFrontEntImpl());
	}

	/**
	 * Test recognition of keywords.
	 */
	@Test
	public void testKeywords() {
		assertToken(KWQLexer.KW, "r", myTester.scanInput("r"));
		assertToken(KWQLexer.KW, "a8", myTester.scanInput("a8"));
		assertToken(KWQLexer.KW, "\"bla:wusch:::blawusch\"", myTester
				.scanInput("\"bla:wusch:::blawusch\""));
		refuteToken(KWQLexer.KW, myTester.scanInput("@"));
		refuteToken(KWQLexer.KW, myTester.scanInput("Two words"));
		refuteToken(KWQLexer.KW, myTester.scanInput("$variable"));
	}

	/**
	 * Test recognition of integers.
	 */
	@Test
	public void testIntegers() {
		assertToken(KWQLexer.INT, "4", myTester.scanInput("4"));

	}

	/**
	 * Test recognition of variables.
	 */
	@Test
	public void testVariables() {
		assertToken(KWQLexer.VARIABLE, "f", myTester.scanInput("$f"));
		assertToken(KWQLexer.VARIABLE, "7", myTester.scanInput("$7"));
	}

	/**
	 * Test recognition of qualifiers.
	 */
	@Test
	public void testQualifiers() {
		assertToken(KWQLexer.RESQUAL, "child", myTester.scanInput("child:"));
	}

	/**
	 * Test recognition of arrow.
	 */
	@Test
	public void testArrow() {
		assertToken(KWQLexer.ARROW, "->", myTester.scanInput("->"));
	}

	/**
	 * Test recognition of URI.
	 */
	@Test
	public void testURI() {
		assertToken(KWQLexer.URI, "http://www.bla.com", myTester
				.scanInput("http://www.bla.com"));
		assertToken(KWQLexer.URI, "http://www.bla.com:8080/wusch", myTester
				.scanInput("http://www.bla.com:8080/wusch"));
	}

	@Test
	public void testURI2() {
		assertToken(
				KWQLexer.URI,
				"uri::http://localhost:8080/KiWi/content/bla/e96275db-4b09-43ee-96b2-505664ceb029",
				myTester
						.scanInput("uri::http://localhost:8080/KiWi/content/bla/e96275db-4b09-43ee-96b2-505664ceb029"));
	}

	/**
	 * Test recognition of dates.
	 */
	@Test
	public void testDate() {
		assertToken(KWQLexer.DA, "2009-09-23T14:42:11Z", myTester
				.scanInput("2009-09-23T14:42:11Z"));
	}

	/**
	 * Test recognition of dates.
	 */
	@Test
	public void testEscape() {
		assertToken(KWQLexer.KW, "fragment", myTester.scanInput("\\fragment"));
	}

}
