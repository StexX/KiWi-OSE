// FIXME this is a quarry taken from the old IkeWiki-based SWiM.  Take out everything that's useful and delete it

package info.kwarc.swim.service.transform;

/**
 * A collection of various extension functions called from XSLT style sheets.
 * 
 * @author clange
 */
public class XSLTFunctions {
//	/**
//	 * @param uri the URI of an article (= knowledge item)
//	 * @return
//	 */
//	public static NodeList getProblemSolutionGuessedFromDiscussion(String uri) throws ParserConfigurationException {
//		Document result = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//		Element elIssues = result.createElementNS(Namespace.NS_ARGUMENTATION_MATH, "issues");
//		// Element elIssues = result.createElement("issues");
//		result.appendChild(elIssues);
//		try {
//			StorageBackend sb = getStorageBackend();
//			
//			/* find all open issues (i.e. without decision) */
//			Collection<WikiResource> issues = sb.executeSparqlQuery("SELECT ?Issue WHERE {" +
//					"  <" + uri + "> ikewiki:hasDiscussion ?Forum ." +
//					"  ?Issue sioc:has_container ?Forum ;" +
//					"         a arguonto:Issue ." +
//					"  OPTIONAL {" +
//					"    ?Decision arguonto:decides ?Issue ;" +
//					"              a arguonto:Decision ." +
//					"  } ." +
//					"  FILTER (!bound(?Decision))" +
//					"}", "Issue");
//			// FIXME use simple for loop syntax if we don't need to delete
//			for (Iterator<WikiResource> i = issues.iterator(); i.hasNext(); ) {
//				WikiResource issue = i.next();
//				/* get the disagreements with this issue */
//				Collection<WikiResource> disagrees = sb.executeSparqlQuery("SELECT ?Disagree WHERE {" +
//						"  ?Disagree arguonto:disagrees_with <" + issue.getUri() + "> ;" +
//						"            a arguonto:Position ." +
//						"}", "Disagree");
//				if (!disagrees.isEmpty()) {
//					/* get the agreements with this issue */
//					Collection<WikiResource> agrees = sb.executeSparqlQuery("SELECT ?Agree WHERE {" +
//							"  ?Agree arguonto:agrees_with <" + issue.getUri() + "> ;" +
//							"         a arguonto:Position ." +
//							"}", "Agree");
//					/* if the issue is considered illegitimate */
//					if (disagrees.size() >= agrees.size()) {
//						// or remove the issue from the collection
//						continue;
//					}
//				}
//
//				WikiResource bestIdea = null;
//				double bestScore = 0;
//				
//				Collection<WikiResource> ideas = sb.executeSparqlQuery("SELECT ?Idea WHERE {" +
//						"  ?Idea arguonto:proposes_solution_for <" + issue.getUri() + "> ;" +
//						"        a arguonto:Idea ." +
//						"}", "Idea");
//				for (WikiResource idea : ideas) {
//					/* get the agreements with this idea */
//					Collection<WikiResource> agreesWithIdea = sb.executeSparqlQuery("SELECT ?Agree WHERE {" +
//							"  ?Agree arguonto:agrees_with <" + idea.getUri() + "> ;" +
//							"         a arguonto:Position ." +
//							"}", "Agree");
//					if (!agreesWithIdea.isEmpty()) {
//						/* get the disagreements with this idea */
//						Collection<WikiResource> disagreesWithIdea = sb.executeSparqlQuery("SELECT ?Disagree WHERE {" +
//								"  ?Disagree arguonto:disagrees_with <" + idea.getUri() + "> ;" +
//								"            a arguonto:Position ." +
//								"}", "Disagree");
//						double score = agreesWithIdea.size() / (disagreesWithIdea.size() + 1);
//						// FIXME this is not yet ordered by date if there are ideas with equal scores!
//						if (score > bestScore) {
//							bestIdea = idea;
//							bestScore = score;
//						}
//					}
//				}
//				
//				if (bestScore > 0) {
//					/* get the most specific types for this issue and for the best idea */
//					String issueType = getDiscussionPostTypeAsResource(issue.getUri()).getTitle();
//					String ideaType = getDiscussionPostTypeAsResource(bestIdea.getUri()).getTitle();
//					
//					Element elIssue = result.createElementNS(Namespace.NS_ARGUMENTATION_MATH, "issue");
//					// Element elIssue = result.createElement("issue");
//					elIssue.setAttribute("type", issueType);
//					elIssue.setAttribute("solution", ideaType);
//					elIssues.appendChild(elIssue);
//				}
//			}
//		}
//		catch (WikiException exc) {
//			/* 
//			 * Don't do anything yet. This exception is unlikely to occur, as the SPARQL query is hard-coded. 
//			 */ 
//			exc.printStackTrace(System.err);
//		}
//		return result.getChildNodes();
//		/*
//		 * OLD CODE
//		try {
//			final Main main = Main.getInstance(null);
//			StorageBackend sb = main.getStorageBackend();
//			
//			// 1. find out whether we have problem guessing available for the type of the current page
//			if (sb.executeSparqlAsk(String.format("ASK { { <%1$s> a odo:Assertion } UNION { <%1$s> a odo:Definition } }", uri))) {
//				// 2. find out whether there are at least 3 complaints
//				// TODO make this number more flexible, less arbitrary
//				Collection<WikiResource> disapprovals = sb.executeSparqlQuery("SELECT ?Post WHERE { ?Post ikewiki:disagreesWith <" + uri +"> . }", "Post");
//				if (disapprovals.size() >= 3) {
//					// 3. find out whether the problem still exists (different query depending on the type)
//					if (sb.executeSparqlAsk("ASK { <" + uri + "> a odo:Assertion }")) {
//						if (!sb.executeSparqlAsk("ASK { ?Ex a odo:Example ; odo:exemplifies <" + uri + "> . }")) {
//							return "Create an example";
//						}
//					}
//					else if (sb.executeSparqlAsk("ASK { <" + uri + "> a odo:Definition }")) {
//						return "?";
//					}
//					// other actions for notation definitions (do not yet exist in the ontology)
//				}
//				
//			}
//			
//		}
//		catch (Exception exc) {
//			exc.printStackTrace(System.err);
//		}
//		*/
//	}
//	
//	/**
//	 * This function, called by transform-html-omdoc.xsl during the transformation of editor input
//	 * to OMDoc, uses an {@link MObjParser} to parse
//	 * an ASCII representation of a formula (close to the OpenMath abstract syntax) to an 
//	 * OpenMath XML object. 
//	 * 
//	 * @param value the formula in ASCII
//	 * @return the corresponding OpenMath XML object
//	 * @throws ParserConfigurationException
//	 * @throws ParseException
//	 */
//	public static NodeList str2mobj(String value) throws ParserConfigurationException {
//		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//		try {
//			new MObjParser(new StringReader(value)).parse(doc);
//		}
//		catch (ParseException exc) {
//			// Create an OpenMath object for the error message
//			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//			Node n = doc.appendChild(doc.createElementNS(Namespace.NS_OPENMATH, "OMOBJ"));
//            Node error = n.appendChild(doc.createElementNS(Namespace.NS_OPENMATH, "OME"));
//            Element s = (Element) error.appendChild(doc.createElementNS(Namespace.NS_OPENMATH, "OMS"));
//            s.setAttribute("cd", "swim");
//            s.setAttribute("name", "invalid_mobj");
//            Node msg = error.appendChild(doc.createElementNS(Namespace.NS_OPENMATH, "OMSTR"));
//            msg.appendChild(doc.createTextNode("Syntax error: " + exc.getMessage()));
//		}
//		return doc.getChildNodes();
//	}
//
//	/**
//	 * Renders an OpenMath object to presentation MathML
//	 *
//	 * @see #render(Node, Node)
//	 */
//	public static Node render(Node element) {
//		return render(element, null);
//	}
//
//	private static org.w3c.dom.Document cloneDocument(org.w3c.dom.Document d) {
//		DOMResult dr = new DOMResult();
//		try {
//			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(d), dr);
//		} catch (TransformerException exc) {
//			// should not happen, as we're using the system's default factory and transformer
//			exc.printStackTrace();
//		}
//		return (org.w3c.dom.Document) dr.getNode();
//	}
//	
//	/**
//	 * This function, called by transform-mcd-html.xsl (or the {@link
//	 * render(Node)} method, which is called by transform-math-html.xsl)
//	 * during the transformation of OMDoc/OpenMath to user-readable HTML
//	 * with presentation MathML, uses the JOMDoc {@link Renderer} to have
//	 * an OpenMath object rendered in presentation MathML.  
//	 *
//	 * @param element the formula to be rendered
//	 * @param notationDefinition the notation definition to be used for rendering; only use if the formula is an example showing how to use exactly this notation definition, otherwise <code>null</code>
//	 */
//	public static Node render(Node element, Node notationDefinition) {
//		Node result = null;
//		if (element != null) {
//			nu.xom.Element xomElement = null;
//			switch (element.getNodeType()) {
//			case Node.DOCUMENT_NODE:
//				/* work around XOM's DOMConverter bugs: clone the input using another XSLT */
//				element = cloneDocument((org.w3c.dom.Document) element).getDocumentElement();
//				/* fall through */
//			case Node.ELEMENT_NODE:
//				xomElement = DOMConverter.convert((org.w3c.dom.Element) element);
//				break;
//			default:
//				/* FIXME raise some exception */
//			}
//			nu.xom.Node xomRendered; 
//			
//			Renderer r;
//			if (notationDefinition != null && 
//					(notationDefinition.getNodeType() == Node.ELEMENT_NODE || notationDefinition.getNodeType() == Node.DOCUMENT_NODE)) {
//				nu.xom.Document d = null;
//
//				if (notationDefinition.getNodeType() == Node.DOCUMENT_NODE) {
//					/* work around XOM's DOMConverter bugs: clone the input using another XSLT */
//					d = DOMConverter.convert(cloneDocument((org.w3c.dom.Document) notationDefinition));
//				} else {
//					/* ELEMENT_NODE */
//					d = new nu.xom.Document(DOMConverter.convert((org.w3c.dom.Element) notationDefinition));
//				}
//			
//				/* create a new renderer with exactly the given notation definition */
//				/* Credits to Christine Müller (https://trac.kwarc.info/jomdoc/ticket/73#comment:3) */
//				RendererFactory rf = RendererFactory.newInstance();
//				rf.setDynamic(false);
//				rf.setParallel(true);
//				
//				NotationCollector nc = new NotationCollector();
//				nc.addNotationSource(new F(d));
//				rf.setNotationCollector(nc);
//				
//				r = rf.newRenderer();
//			} else {
//				/* use the system-wide renderer with all notation definitions from the database */
//				Main main = null;
//				try {
//					main = Main.getInstance(null);
//				} catch (DBException exc) {
//					// this does not occur, as the system will already be running when the renderer is invoked
//					exc.printStackTrace();
//				}
//				r = main.getRenderingPipeline().getFormulaRenderer();
//			}
//			xomRendered = r.render(xomElement);
//			result = xomRendered != null ? XMLUtil.convert(Collections.singletonList(xomRendered)).item(0) : element;
//		} else {
//			/* element == null */
//			// FIXME do something
//		}
//		return result;
//	}
}
