/**
 * 
 */
package kiwi.action.vmt.importer;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.openrdf.model.Namespace;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.memory.MemoryStore;

/**
 * @author Rolf Sint
 *
 */
public class ThesaurusImporterTester {

	static RepositoryConnection con;
	
	final static String URL_THESAURUS = "http://pptry.punkt.at/cubalibre/project/urn:kiwi/thesaurus";
	final static String URL_CONCEPT = "http://pptry.punkt.at/cubalibre/project/urn:kiwi/concept";
	final static String CONCEPT_SCHEMA = "/urn:uuid:1D6439AB-915E-0001-B1FE-1CD015A716D0/";
	final static String TOPCONCEPT = "topConcept";
	final static String FORMAT = "?format=n3";
	final static String LANGUAGE = "&language=en";
	
	final static int MAXDEPTH = 2;

	
	public static void main(String[] args) throws Exception {
		
		final String url = URL_THESAURUS + CONCEPT_SCHEMA + TOPCONCEPT + FORMAT;
		
		Repository myRepository = new SailRepository(new MemoryStore());
		myRepository.initialize();
		con = myRepository.getConnection();
		
		//conceptsImorted is all
		//conceptsToExpand is prestage from conceptsToImports
		List<ConceptT1> conceptsImorted =  new LinkedList<ConceptT1>();
		List<ConceptT1> conceptsToExpand = getTopConcepts(url);
		
		conceptsImorted = conceptsToExpand;
	
		List <ConceptT1> conceptsToImport = new LinkedList<ConceptT1>();
		
		int d=1;
		while (d <= MAXDEPTH){
			
			for (ConceptT1 concept : conceptsToExpand) {
				List<ConceptT1> n = new ThesaurusImporterTester().getNarrowers(concept.uri);
				
				conceptsToImport.addAll(n);
			}
			conceptsToExpand = conceptsToImport;
			
			conceptsImorted.addAll(conceptsToExpand);
			
			conceptsToImport.clear();
			d++;
		}
				
		for (ConceptT1 ci : conceptsImorted) {
			new ThesaurusImporterTester().buildConcept(ci.uri.replace("\"", ""), ci.deep);
		}
		
	}
	

	public void  buildConcept(String uri, int d) throws Exception{
		String y = URL_CONCEPT + File.separatorChar + uri.trim() + File.separatorChar + FORMAT + LANGUAGE;
		URL url1 = new URL(y);
		con.clear();
		con.add(url1, url1.toString(), RDFFormat.TURTLE);
		
//		for(RepositoryResult<Namespace> ns = con.getNamespaces(); ns.hasNext(); ) {
//            Namespace ns1 = ns.next();
//            
//          System.out.println(ns1.getPrefix());
//          System.out.println(ns1.getPrefix());
//        }
		
		
		Resource r = new URIImpl(uri);
		org.openrdf.model.URI prefLab = new URIImpl("http://www.w3.org/2004/02/skos/core#prefLabel");
		
	//	RepositoryResult<Statement> istatements = con.getStatements(null,prefLab, null, true);
		RepositoryResult<Statement> istatements = con.getStatements(r,null, null, true);

		while (istatements.hasNext()) {
				Statement st1 = istatements.next();
		}		
	}
	
	
	public static List<ConceptT1> getTopConcepts(String thesaurusURI){
		List<ConceptT1> topConcepts = new LinkedList<ConceptT1>();
		try {
			URL u = new URL(thesaurusURI);
			con.add(u, u.toString(), RDFFormat.TURTLE);

			// query all skos concepts
			GraphQueryResult graphResult = con
					.prepareGraphQuery(
							QueryLanguage.SERQL,
							"CONSTRUCT * FROM {x} rdf:type {skos:Concept} USING NAMESPACE rdf = <http://www.w3.org/1999/02/22-rdf-syntax-ns#> , skos = <http://www.w3.org/2004/02/skos/core#> ")
					.evaluate();

			// iterate over all concepts
			while (graphResult.hasNext()) {				
				Statement st = graphResult.next();
				topConcepts.add(new ConceptT1(0,st.getSubject().toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Release the connection.
			// method.releaseConnection();
		}
		return topConcepts;

	}
	
	
	public List<ConceptT1> getNarrowers(String uri) throws Exception{
		String y = URL_CONCEPT + File.separatorChar + uri.trim() + File.separatorChar + FORMAT + LANGUAGE;			
		URL url1 = new URL(y);
		con.clear();
		con.add(url1, url1.toString(), RDFFormat.TURTLE);
	
		org.openrdf.model.URI narrower = new URIImpl("http://www.w3.org/2004/02/skos/core#narrower");
		RepositoryResult<Statement> narStatements = con.getStatements(null, narrower, null, true);

		List<ConceptT1> l = new LinkedList<ConceptT1>();
		// für narrowers weiter printConcept aufrufen
		while (narStatements.hasNext()) {
			Statement st1 = narStatements.next();
			l.add(new ConceptT1(0,st1.getObject().toString().trim()));
		}
		return l;
	}
	


}

class ConceptT1{
	
	int deep;
	String uri;

	
	ConceptT1(int deep, String uri){
		this.deep = deep;
		this.uri = uri;
		
	}	
}
