package kiwi.test.service.query;

import kiwi.service.query.kwql.parser.KWQL;
import kiwi.service.query.kwql.parser.KWQLexer;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.Parser;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeParser;
import org.norecess.antlr.IANTLRFrontEnd;

public class IANTLRFrontEntImpl implements IANTLRFrontEnd {

	public Lexer createLexer(String input) {
		ANTLRStringStream text = new ANTLRStringStream(input);
		KWQLexer lexer = new KWQLexer(text);
		return lexer;
	}

	@Override
	public Parser createParser(TokenStream stream) {
		KWQL parser = new KWQL(stream);
		return parser;
	}

	@Override
	public TreeParser createTreeParser(Tree tree) {
		return null;
	}

}
