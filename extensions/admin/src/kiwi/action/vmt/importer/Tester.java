///**
// * 
// */
//package kiwi.action.vmt.importer;
//
///**
// * @author devadmin
// *
// */
//public class Tester {
//	 String importFromUrl(String topconceptsURL, String conceptsBaseURL, String format, int maxDepth, boolean transformURIsforKiWi) {
//		        RDFFormat f
//		        String suffix = ''
//		        if (format.equalsIgnoreCase('n3')) {
//		            f = RDFFormat.N3
//		            suffix="?format=n3"
//		        }
//		        else if (format.equalsIgnoreCase('json')) {
//		            f = null // TODO JSON+RDF import!
//		        }
//		        else if (format.equalsIgnoreCase('xml')) {
//		            f = RDFFormat.RDFXML
//		        }
//		        else {
//		            f = RDFFormat.RDFXML
//		        }
//
//		        try {
//		         URL u = new URL(topconceptsURL+suffix);
//		            importRC.add(u, u.toString(), f);
//		        }
//		        catch (Exception e) {
//		            log.error("Exception on import! \n" +e)
//		        }
//
//		        List topConcepts = []
//		        RepositoryResult<Statement> topConceptStatements = importRC.getStatements(null, rdf_type, skos_Concept, true)
//		        while (topConceptStatements.hasNext()) {
//		            Statement st = (Statement)(topConceptStatements.next())
//		            String uri = st?.getSubject()?.toString()
//		            if (uri) {
//		                topConcepts += uri
//		            }
//		        }
//
//		        log.info("topConcepts found: $topConcepts")
//
//		        List conceptsImported = topConcepts
//		        numAll += topConcepts.size()
//		        numCompleted += topConcepts.size()
//		        List conceptsToExpand = topConcepts
//		        List conceptsToImport = []
//
//		  log.info("starting crawl down from topconcepts for at most $maxDepth layers ...")
//		  
//		  int d=1
//		  while (d<=maxDepth && (conceptsToExpand) ) {
//		 
//		            log.info("expanding concepts on layer $d ...")
//
//		         log.info("concepts to expand: $conceptsToExpand")
//		            conceptsToExpand.each { ecURI ->
//		                log.info("expanding $ecURI ...")
//		             List n =  getNarrowersOfConcept(ecURI)
//		                log.info("found narrowers $n")
//		                conceptsToImport += n
//		            }
//		            conceptsToImport = (conceptsToImport.unique()) - conceptsImported
//		            numAll += conceptsToImport.size()
//		            conceptsToExpand = conceptsToImport
//		         log.info("concepts to import: $conceptsToImport")
//		            conceptsToImport.each { iURI ->
//		             if (transformURIsforKiWi) {
//		                 iURI = iURI.replaceAll('http://','http/').replaceAll('#','%23')
//		                }
//		                URL u = new URL(conceptsBaseURL+'/'+iURI+suffix);
//		                log.info("importing $u ...")
//		                importRC.add(u, u.toString(), f);
//		                numCompleted++
//		                log.info(getProgress())
//		            }
//		            conceptsImported += conceptsToImport
//		            conceptsToImport = []
//		            
//		            d++
//		        }
//		  log.debug( "concepts: " + getAllConceptsWithPrefLabel() )
//		  getAllConceptsWithSkosDetails()
//		    }
//
//		    List getNarrowersOfConcept(String startConceptURI) {
//		        List narrowers = []
//		        RepositoryResult<Statement> narrowerStatements = importRC.getStatements(new URIImpl(startConceptURI), skos_narrower, null, true);
//		        while (narrowerStatements.hasNext()) {
//		            Statement st = (Statement)(narrowerStatements.next())
//		            String uri = st?.getObject()?.toString()
//		            if (uri) {
//		                narrowers += uri
//		            }
//		        }
//		        return narrowers
//		    }
//}
