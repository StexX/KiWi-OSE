package kiwi.service.query.kwql;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import kiwi.service.query.kwql.parser.KWQL;
import kiwi.service.query.kwql.parser.KWQLexer;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.DOTTreeGenerator;
import org.antlr.runtime.tree.Tree;
import org.antlr.stringtemplate.StringTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class KWQLQuery {

	private String text;
	private CommonTree parseTree;
	private Boolean valid = false;
	private ArrayList<KWQLComponent> bodies;
	private HashSet<String> headVariables;
	private HashSet<ArrayList<String>> bodyVariables;
	private HashMap<String, ArrayList<String>> resourceQualifiersC;
	private HashMap<String, String> qualifierValueTypesC;
	private HashMap<String, Integer> resourceNestingsC;
	private ArrayList<String> qualifierInstancesC;
	private HashSet<String> valueTypesC;

	/**
	 * Instantiates a new KWQL query.
	 * 
	 * @param text
	 *            the text of the query
	 * @throws KWQLConstraintViolationException
	 * @throws RecognitionException
	 */
	public KWQLQuery(String text) throws RecognitionException,
			KWQLConstraintViolationException {
		this.text = text.trim();
		bodies = new ArrayList<KWQLComponent>();
		bodyVariables = new HashSet<ArrayList<String>>();
		headVariables = new HashSet<String>();
		parseQuery();
		findBodies();
		
		validate();


	}

	public ArrayList<KWQLComponent> getBodies() {
		return bodies;
	}

	public Tree getHead() {
		Tree head = parseTree.getChild(0).getChildCount() > 0 ? parseTree
				.getChild(0) : null;
		return head;
	}

	public Boolean getValid() {
		return valid;
	}

	public boolean hasConstruction() {
		if (parseTree != null && parseTree.getChild(0).getChildCount() > 0) {
			return true;
		} else
			return false;
	}

//	public boolean isGlobal() {
//		boolean global;
//		global = parseTree.getChild(1).getText().equals("GBODY") ? true : false;
//		return global;
//	}

	public void printDOT(String outputName) {
		if (!toTree().isNil()) {
			DOTTreeGenerator gen = new DOTTreeGenerator();
			StringTemplate st = gen.toDOT(toTree());
			FileWriter outputStream;
			try {
				outputStream = new FileWriter(outputName + ".dot");
				outputStream.write(st.toString());
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else
			System.out.println("No valid parse tree");
	}

	/**
	 * Prints the tree.
	 */
	public void printTree() {
		if (!toTree().isNil())
			System.out.println(toTree().toStringTree());
		else
			System.out.println("No valid parse tree");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return text;
	}

	/**
	 * To tree.
	 * 
	 * @return the tree
	 */
	public Tree toTree() {
		try {
			return parseTree;
		} catch (Exception e) {
			return new CommonTree();
		}
	}

	@SuppressWarnings("unchecked")
	private int ancestorDistance(CommonTree t, String type) {
		List<Tree> ancestors = t.getAncestors();
		for (int i = ancestors.size() - 1; i >= 0; i--) {
			if (ancestors.get(i).getText().equals(type)) {
				return ancestors.size() - i;
			}
		}
		return -1;
	}

	private void extractQueryBodies(Tree root) {
		String label = root.getText();
		if (label.equals("BOOL_BODY")) {
			bodies.add(new KWQLComponent(root));
		} else if (label.equals("CI_BODY")) {
			bodies.add(new KWQLComponent(root));
			for (int i = 0; i < root.getChildCount(); i++) {
				extractQueryBodies(root.getChild(i));

			}
		} else {
			for (int i = 0; i < root.getChildCount(); i++) {
				extractQueryBodies(root.getChild(i));
			}

		}
	}

	private void findBodies() {
		if (parseTree != null) {
			extractQueryBodies(parseTree.getChild(1));
		}
	}

	private HashSet<ArrayList<String>> getBodyVariables(Tree root) {
		if (root.getText().equals("AND")) {
			return pwUnion(getBodyVariables(root.getChild(0)),
					getBodyVariables(root.getChild(1)));
		} else if (root.getText().equals("OR")) {
			return union(getBodyVariables(root.getChild(0)),
					getBodyVariables(root.getChild(1)));
		} else if (root.getText().equals("VAR")) {
			HashSet<ArrayList<String>> s = new HashSet<ArrayList<String>>();
			ArrayList<String> s2 = new ArrayList<String>();
			if (hasAncestor((CommonTree) root, "NOT")) {
				s2.add("");
			} else {
				s2.add(root.getChild(0).getText());
			}
			s.add(s2);
			return s;
		} else if (root.getText().equals("STRING") || root.getText().equals("BOOL_BODY"))  {
			HashSet<ArrayList<String>> s = new HashSet<ArrayList<String>>();
			ArrayList<String> s2 = new ArrayList<String>();
			s2.add("");
			s.add(s2);
			return s;
		} else if (root.getChildCount() == 1 || root.getText().equals("CI_BODY")) {
			return getBodyVariables(root.getChild(0));
		} else {
			return getBodyVariables(root.getChild(1));
		}
	}

	private void getHeadVariables(Tree root) {
		for (int i = 0; i < root.getChildCount(); i++) {
			String label = root.getChild(i).getText();
			if (label.equals("VAR")) {
				headVariables.add(root.getChild(i).getChild(0).getText());
			} else {
				getHeadVariables(root.getChild(i));
			}

		}

	}

	private Boolean hasAncestor(CommonTree t, String type) {
		return ancestorDistance(t, type) != -1 ? true : false;
	}

	/**
	 * Parses the query using the ANTLR generated classes.
	 * 
	 * @throws RecognitionException
	 */
	private void parseQuery() throws RecognitionException,
			KWQLConstraintViolationException {
		validateBracketing();

		ANTLRStringStream input = new ANTLRStringStream(text);
		KWQLexer lexer = new KWQLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		KWQL parser = new KWQL(tokens);
		KWQL.kwqlrule_return r = parser.kwqlrule();
		parseTree = (CommonTree) r.getTree();

	}

	private HashSet<ArrayList<String>> pwUnion(HashSet<ArrayList<String>> set1,
			HashSet<ArrayList<String>> set2) {
		HashSet<ArrayList<String>> result = new HashSet<ArrayList<String>>();
		for (ArrayList<String> s1 : set1) {
			for (ArrayList<String> s2 : set2) {
				ArrayList<String> tmp = new ArrayList<String>();
				tmp.addAll(s1);
				tmp.addAll(s2);
				result.add(tmp);
			}
		}
		return result;
	}

	private HashSet<ArrayList<String>> union(HashSet<ArrayList<String>> a,
			HashSet<ArrayList<String>> b) {
		a.addAll(b);
		return a;
	}

	/**
	 * Given a valid parse, walks the resulting tree and applies the
	 * constraints.
	 * 
	 * @throws KWQLConstraintViolationException
	 * 
	 * @throws KWQLConstraintViolationException
	 */
	private void validate() throws KWQLConstraintViolationException {
		initializeConstraints();
		if (hasConstruction()) {
			bodyVariables = getBodyVariables(parseTree.getChild(1));
			getHeadVariables(parseTree.getChild(0));
			validateVariables();
		}
		ArrayList<KWQLComponent> bodies = getBodies();
		for (KWQLComponent body : bodies) {
			valid = true;
			body.validate(resourceQualifiersC, qualifierValueTypesC, resourceNestingsC, qualifierInstancesC,valueTypesC);
			}
		Tree head = getHead();
		KWQLComponent headn = new KWQLComponent(head);
		headn.validate(resourceQualifiersC, qualifierValueTypesC, resourceNestingsC, qualifierInstancesC, valueTypesC);
		
	}

	/**
	 * Validate that the bracketing of the query is correct.
	 * 
	 * @throws KWQLConstraintViolationException
	 *             the KWQL constraint violation exception
	 */
	private void validateBracketing() throws KWQLConstraintViolationException {
		Stack<Character> brackets = new Stack<Character>();
		char[] query = text.toCharArray();
		for (char character : query) {
			switch (character) {
			case '(':
				brackets.push(character);
				break;
			case ')':
				if (!brackets.empty() && (brackets.peek()).equals('('))
					brackets.pop();
				else {
					ArrayList<String> cause = new ArrayList<String>();
					cause.add(String.valueOf(character));
					throw new KWQLConstraintViolationException('b', cause);
				}
			}
		}
		if (!brackets.empty()) {
			ArrayList<String> cause = new ArrayList<String>(Arrays
					.asList(new String[] { ((Character) text.charAt(text
							.length() - 1)).toString() }));
			throw new KWQLConstraintViolationException('b', cause);

		}
	}

	/**
	 * Check whether all variables used in the head are used in the body.
	 * 
	 * @throws KWQLConstraintViolationException
	 *             the KWQL constraint violation exception
	 */
	private void validateVariables() throws KWQLConstraintViolationException {
		System.out.println("Head variables: "+ headVariables);
		System.out.println("Body variables: "+ bodyVariables);
		if (!headVariables.isEmpty()) {
			for (String headVar : headVariables) {
				for (ArrayList<String> varC : bodyVariables) {
					if (!varC.contains(headVar)) {
						valid = false;
						ArrayList<String> cause = new ArrayList<String>();
						cause.add(headVar);
						throw new KWQLConstraintViolationException('v', cause);
					}
				}
			}
		}
	}
	
	/**
	 * Initialize constraints.
	 */
	private void initializeConstraints() {
		resourceNestingsC = new HashMap<String, Integer>();
		resourceQualifiersC = new HashMap<String, ArrayList<String>>();
		qualifierValueTypesC = new HashMap<String, String>();
		qualifierInstancesC = new ArrayList<String>();
		valueTypesC = new HashSet<String>();

		Document document = loadConfig();
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();

		try {
			NodeList resourceElements = (NodeList) xPath.evaluate(
					"/KWQLConfiguration/resource", document,
					XPathConstants.NODESET);
			for (int i = 0; i < resourceElements.getLength(); i++) {
				Node nestingValue = (Node) xPath.evaluate(
						"./nestingLevel/text()", resourceElements.item(i),
						XPathConstants.NODE);
				Node name = (Node) xPath.evaluate("attribute::name",
						resourceElements.item(i), XPathConstants.NODE);
				NodeList qualifierTypes = (NodeList) xPath.evaluate(
						"./qualifierTypes/name/text()", resourceElements
								.item(i), XPathConstants.NODESET);
				ArrayList<String> quals = new ArrayList<String>();
				for (int j = 0; j < qualifierTypes.getLength(); j++) {
					quals.add(qualifierTypes.item(j).getNodeValue());
				}
				resourceNestingsC.put(name.getNodeValue(), Integer
						.parseInt(nestingValue.getNodeValue()));
				resourceQualifiersC.put(name.getNodeValue(), quals);
			}

			NodeList qualifierElements = (NodeList) xPath.evaluate(
					"/KWQLConfiguration/qualifier", document,
					XPathConstants.NODESET);
			for (int i1 = 0; i1 < qualifierElements.getLength(); i1++) {
				String name = xPath.evaluate("attribute::name",
						qualifierElements.item(i1));
				String values = xPath.evaluate("valueType", qualifierElements
						.item(i1));
				String singular = xPath.evaluate("singular", qualifierElements
						.item(i1));
				if (singular.equals("true")) {
					qualifierInstancesC.add(name);
				}
				qualifierValueTypesC.put(name, values);
				valueTypesC.add(values.toLowerCase());
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	private Document loadConfig() {
		Document document = null;
		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			document = parser.parse(new File(KWQLComponent.class.getResource(
					"kwql.xml").toURI().getPath()));
			/*
			 * SchemaFactory factory = SchemaFactory
			 * .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); Source
			 * schemaFile = new StreamSource(new File(KWQLQuery.class
			 * .getResource("kwql.xsd").getPath())); Schema schema =
			 * factory.newSchema(schemaFile); Validator validator =
			 * schema.newValidator(); validator.validate(new
			 * DOMSource(document));
			 */} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}
	
}
