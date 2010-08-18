///**
// * 
// */
//package kiwi.action.vmt.importer;
//
//import java.io.File;
//import java.net.URL;
//import java.util.LinkedList;
//
//import org.openrdf.model.Statement;
//import org.openrdf.model.impl.URIImpl;
//import org.openrdf.query.GraphQueryResult;
//import org.openrdf.query.QueryLanguage;
//import org.openrdf.repository.Repository;
//import org.openrdf.repository.RepositoryConnection;
//import org.openrdf.repository.RepositoryException;
//import org.openrdf.repository.RepositoryResult;
//import org.openrdf.repository.sail.SailRepository;
//import org.openrdf.rio.RDFFormat;
//import org.openrdf.sail.memory.MemoryStore;
//
///**
// * @author Rolf Sint
// * 
// */
//public class TesterX {
//
//	static RepositoryConnection con;
//
//	final static String URL_THESAURUS = "http://pptry.punkt.at/cubalibre/project/urn:kiwi/thesaurus";
//	final static String URL_CONCEPT = "http://pptry.punkt.at/cubalibre/project/urn:kiwi/concept";
//	final static String CONCEPT_SCHEMA = "/urn:uuid:1D6439AB-915E-0001-B1FE-1CD015A716D0/";
//	final static String TOPCONCEPT = "topConcept";
//	final static String FORMAT = "?format=n3";
//	final static String LANGUAGE = "&language=en";
//
//	final static int DEEP = 5;
//	
//	
//	public static void main(String[] args) {
//
//		LinkedList<String> list = new LinkedList<String>();
//
//		// String url =
//		// "http://showcase.kiwi-project.eu/KiWi/seam/resource/services/thesaurus/top/html";
//
//		// build url for all top concepts
//		String url = URL_THESAURUS + CONCEPT_SCHEMA + TOPCONCEPT + FORMAT;
//
//		try {
//			Repository myRepository = new SailRepository(new MemoryStore());
//			myRepository.initialize();
//			con = myRepository.getConnection();
//
//			URL u = new URL(url);
//			con.add(u, u.toString(), RDFFormat.TURTLE);
//
//			// query all skos concepts
//			GraphQueryResult graphResult = con
//					.prepareGraphQuery(
//							QueryLanguage.SERQL,
//							"CONSTRUCT * FROM {x} rdf:type {skos:Concept} USING NAMESPACE rdf = <http://www.w3.org/1999/02/22-rdf-syntax-ns#> , skos = <http://www.w3.org/2004/02/skos/core#> ")
//					.evaluate();
//
//			// iterate over all concepts
//			while (graphResult.hasNext()) {
//				con.clear();
//				Statement st = graphResult.next();
//				// alle top concepts in der liste
//				list.add(st.getSubject().toString());
//				bringNarrowers(list);
//			}
//
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			// Release the connection.
//			// method.releaseConnection();
//		}
//
//	}
//
//	/**
//	 * @throws RepositoryException
//	 */
//	private static void bringNarrowers(LinkedList<String> list)
//			throws Exception {
//
//		for (String st : list) {
//			LinkedList<String> l = (st);
//		}
//	}
//	
//	
//	public static LinkedList<String> getNarrowers(String uri) throws Exception{
//		String y = URL_CONCEPT + File.separatorChar + uri.trim() + File.separatorChar + FORMAT + LANGUAGE;			
//		URL url1 = new URL(y);
//		
//		con.add(url1, url1.toString(), RDFFormat.TURTLE);
//	
//		org.openrdf.model.URI narrower = new URIImpl("http://www.w3.org/2004/02/skos/core#narrower");
//		RepositoryResult<Statement> narStatements = con.getStatements(null, narrower, null, true);
//
//		LinkedList<String> l = new LinkedList<String>();
//		// für narrowers weiter printConcept aufrufen
//		while (narStatements.hasNext()) {
//			Statement st1 = narStatements.next();
//			l.add(st1.getObject().toString().trim());
//		}
//		return l;
//	}
//}
//
//// private static void printConceptByUri(String uri) {
//// try {
//// con.clear();
////			
////			
//// //e.g. "http://pptry.punkt.at/cubalibre/project/urn:kiwi/concept/"+
//// uri.trim() + "/?format=n3&language=en";
////			
//// //Build url which represents each single concept
//// String y = URL_CONCEPT + File.separatorChar + uri.trim() + File.separatorChar
//// + FORMAT + LANGUAGE;
//// URL url1 = new URL(y);
////			
//// con.add(url1, url1.toString(), RDFFormat.TURTLE);
////			
//// //query the prefered labels
//// org.openrdf.model.URI prefLab = new URIImpl(
//// "http://www.w3.org/2004/02/skos/core#prefLabel");
//// RepositoryResult<Statement> istatements = con.getStatements(null,
//// prefLab, null, true);
////
//// // s ausgeben
//// while (istatements.hasNext()) {
//// Statement st1 = istatements.next();
//// System.out.println("preferedLabel "+st1.getObject());
//// }
////			
//// //System.out.println(uri.trim());
////			
////			
////			
////						
//// } catch (Exception e) {
//// e.printStackTrace();
//// }
//// }
//
//// }
