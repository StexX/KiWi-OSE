package kiwi.service.query.kwql;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class KWQLComponent{
	private CommonTree parseTree;
	private Boolean valid = false;
	private HashMap<String, HashSet<String>> resourceQualifiers;
	private HashMap<String, ArrayList<String>> resourceQualifiersC;
	private HashMap<String, HashSet<String>> qualifierValueTypes;
	private HashMap<String, String> qualifierValueTypesC;
	private HashMap<String, HashSet<String>> resourceNestings;
	private HashMap<String, Integer> resourceNestingsC;
	private HashSet<ArrayList<String>> qualifierInstances;
	private ArrayList<String> qualifierInstancesC;
	private HashSet<String> valueTypesC;

	/**
	 * Instantiates a new KWQL query.
	 * @param valueTypesC 
	 * @param qualifierInstancesC 
	 * @param resourceNestingsC 
	 * @param qualifierValueTypesC 
	 * @param resourceQualifiersC 
	 * 
	 * @param text
	 *            the text of the query
	 * @throws KWQLConstraintViolationException
	 */
	public KWQLComponent(Tree parseTree) {
		this.parseTree = (CommonTree) parseTree;
		resourceQualifiers = new HashMap<String, HashSet<String>>();
		qualifierValueTypes = new HashMap<String, HashSet<String>>();
		resourceNestings = new HashMap<String, HashSet<String>>();
		qualifierInstances = new HashSet<ArrayList<String>>();
	}

	public Tree getBody() {
		return parseTree;
	}

	public Boolean getValid() {
		return valid;
	}

	/**
	 * Prints the tree.
	 */
	public void printTree() {
		System.out.println(parseTree.toStringTree());
	}

	public void setBody(Tree parseTree) {
		this.parseTree = (CommonTree) parseTree;
	}

	/**
	 * @param qualifierType
	 * @param label
	 */
	private void addQualifierValueType(String qualifierType, String label) {
		if (!qualifierValueTypes.containsKey(qualifierType)) {
			qualifierValueTypes.put(qualifierType, new HashSet<String>());
		}
		qualifierValueTypes.get(qualifierType).add(label);
	}

	/**
	 * @param resourceType
	 * @param oldResourceType
	 * @param child
	 */
	private void addResourceNesting(String resourceType,
			String oldResourceType, CommonTree child) {
		int distRes = ancestorDistance(child, "RESOURCE");
		int distQual = ancestorDistance(child, "QUALIFIER");
		if (!oldResourceType.equals("#")
				&& ((distRes < distQual) || (distRes == -1 || distQual == -1))) {
			if (!resourceNestings.containsKey(oldResourceType)) {
				resourceNestings.put(oldResourceType, new HashSet<String>());
			}
			resourceNestings.get(oldResourceType).add(resourceType);
		}
	}

	/**
	 * @param resourceType
	 * @param qualifierType
	 */
	private void addResourceQualifier(String resourceType, String qualifierType) {
		if (!resourceQualifiers.containsKey(resourceType)) {
			resourceQualifiers.put(resourceType, new HashSet<String>());
		}
		resourceQualifiers.get(resourceType).add(qualifierType);
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

	private HashSet<ArrayList<String>> getQualifierCombinations(Tree child) {
//		System.out.println(child.toStringTree());
		if (child.getText().equals("AND")) {
			return pwUnion(getQualifierCombinations(child.getChild(0)),
					getQualifierCombinations(child.getChild(1)));
		} else if (child.getText().equals("OR")) {
			return union(getQualifierCombinations(child.getChild(0)),
					getQualifierCombinations(child.getChild(1)));
		} else if (child.getText().equals("QUALIFIER")
				&& child.getChild(0).getChildCount() > 0) {
			HashSet<ArrayList<String>> s = new HashSet<ArrayList<String>>();
			ArrayList<String> s2 = new ArrayList<String>();
			s2.add(child.getChild(0).getChild(0).getText());
			s.add(s2);
			return s;
		} else if (child.getText().equals("NOT")) {
			return getQualifierCombinations(child.getChild(0));
		}

		return new HashSet<ArrayList<String>>();

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

	private void setValid(Boolean valid) {
		this.valid = valid;
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
	 */
	protected void validate(HashMap<String, ArrayList<String>> resourceQualifiersC, HashMap<String, String> qualifierValueTypesC, HashMap<String, Integer> resourceNestingsC, ArrayList<String> qualifierInstancesC, HashSet<String> valueTypesC) throws KWQLConstraintViolationException {
		this.parseTree = (CommonTree) parseTree;
		this.resourceQualifiersC= resourceQualifiersC;
		this.qualifierValueTypesC = qualifierValueTypesC;
		this.resourceNestingsC = resourceNestingsC;
		this.qualifierInstancesC = qualifierInstancesC;
		this.valueTypesC = valueTypesC;
		
		String resourceType = "#";
		String qualifierType = "#";
		//System.out.println(parseTree.toStringTree());
		walkTree(parseTree, resourceType, qualifierType);
//		System.out.println("resourceQualifiers: " + resourceQualifiers);
//		System.out.println("QualifierValueTypes: " + qualifierValueTypes);
//		System.out.println("ResourceNestings: " + resourceNestings);
//		System.out.println("QualifierInstances: " + qualifierInstances);

		setValid(true);
		validateResourceNesting();
		validateResourceQualifierPairs();
		validateQualifierCount();
		validateValueTypes(qualifierValueTypesC);

	}

	/**
	 * Checks whether all qualifiers occur only the allowed number of times
	 * within one resource.
	 * 
	 * @throws KWQLConstraintViolationException
	 *             the KWQL constraint violation exception
	 */
	private void validateQualifierCount()
			throws KWQLConstraintViolationException {
		for (ArrayList<String> qualifierList : qualifierInstances) {
			qualifierList.retainAll(qualifierInstancesC);
			if (!qualifierList.isEmpty()) {
				for (String qualifier : qualifierInstancesC) {
					if (qualifierList.lastIndexOf(qualifier) != qualifierList
							.indexOf(qualifier)) {
						setValid(false);
						ArrayList<String> cause = new ArrayList<String>();
						cause.add(qualifier);
						throw new KWQLConstraintViolationException('n', cause);
					}
				}
			}
		}
	}

	/**
	 * Check whether all resources nested inside another resource are permitted.
	 * 
	 * @throws KWQLConstraintViolationException
	 *             the KWQL constraint violation exception
	 */
	private void validateResourceNesting()
			throws KWQLConstraintViolationException {
		int previous, current;
		Set<String> keys = resourceNestings.keySet();
		for (String q : keys) {

			previous = resourceNestingsC.get(q);

			for (Object resource : resourceNestings.get(q)) {
				current = resourceNestingsC.get(resource);
				if (previous >= current) {
					setValid(false);
					ArrayList<String> cause = new ArrayList<String>();
					cause.add((String) resource);
					cause.add(q);

					throw new KWQLConstraintViolationException('r', cause);
				}
			}
		}
	}

	/**
	 * Checks whether all resource-qualifier pairs are appropriate.
	 * 
	 * @throws KWQLConstraintViolationException
	 *             the KWQL constraint violation exception
	 */
	private void validateResourceQualifierPairs()
			throws KWQLConstraintViolationException {
		Set<String> keys = resourceQualifiers.keySet();
		for (String q : keys) {
			for (String qual : resourceQualifiers.get(q)) {
				if (!resourceQualifiersC.get(q).contains(qual)) {
					setValid(false);
					ArrayList<String> cause = new ArrayList<String>();
					cause.add(q);
					cause.add(qual);
					throw new KWQLConstraintViolationException('q', cause);
				}
			}
		}
	}

	/**
	 * Checks whether all qualifiers occur with allowed value types.
	 * 
	 * @throws KWQLConstraintViolationException
	 *             the KWQL constraint violation exception
	 */
	private void validateValueTypes(HashMap<String, String> qualifierValueTypesC) throws KWQLConstraintViolationException {
		Set<String> keys = qualifierValueTypes.keySet();

		for (String q : keys) {
			for (String value : qualifierValueTypes.get(q)) {
				if (!value.equalsIgnoreCase("var")
						&& !qualifierValueTypesC.get(q).equalsIgnoreCase(
								"String")
						&& !qualifierValueTypesC.get(q).equalsIgnoreCase(value)) {
					setValid(false);
					ArrayList<String> cause = new ArrayList<String>(Arrays
							.asList(new String[] { q, value }));
					throw new KWQLConstraintViolationException('w', cause);
				}
			}
		}
	}

	private void walkTree(CommonTree t, String resourceType,
			String qualifierType) {
		if (t != null) {
			String oldResourceType = resourceType;
			for (int i = 0; i < t.getChildCount(); i++) {
				CommonTree child = (CommonTree) t.getChild(i);
				String label = child.getText();
				if (label.equals("RESOURCE")) {
					if (child.getChild(0).getChildCount() > 0) {
						resourceType = child.getChild(0).getChild(0).getText()
								.toLowerCase();
						addResourceNesting(resourceType, oldResourceType, child);
					} else {
						resourceType = "#";
					}
					String qualifierRootLabel = child.getChild(1).getText();
					if (qualifierRootLabel.equals("AND")
							|| qualifierRootLabel.equals("OR")
							|| qualifierRootLabel.equals("NOT")) {
						qualifierInstances
								.addAll(getQualifierCombinations(child
										.getChild(1)));
					}

				} else if (label.equals("QUALIFIER")) {
					if (child.getChild(0).getChildCount() > 0) {

						qualifierType = child.getChild(0).getChild(0).getText();
						if (!resourceType.equals("#")) {

							addResourceQualifier(resourceType, qualifierType);
						}

					} else {
						qualifierType = "#";
					}
				}
				if (valueTypesC.contains(label.toLowerCase())
						&& !qualifierType.equals("#")
						&& child.getChildCount() != 0) {
					addQualifierValueType(qualifierType, label);
				}

				walkTree(child, resourceType, qualifierType);
				resourceType = oldResourceType;
			}
		}
	}
}
