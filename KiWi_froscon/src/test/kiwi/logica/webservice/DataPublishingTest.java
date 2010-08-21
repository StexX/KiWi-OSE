package kiwi.logica.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.ontology.TemplateService;
import kiwi.api.render.RenderingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.webservice.DataAccessService;
import kiwi.exception.NamespaceResolvingException;
import kiwi.exception.TextContentNotChangedException;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.service.triplestore.TripleStoreUtil;
import kiwi.test.base.KiWiTest;
import kiwi.util.KiWiCollections;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.jboss.seam.Component;
import org.openrdf.model.Statement;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.memory.MemoryStore;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class DataPublishingTest extends KiWiTest {

	@Test
	public void testpublishingData4Templates() throws Exception {
		
		String[] ontologies = {
    			"ontology_kiwi.owl",
    			"imports/foaf.owl"
    	};
		setupDatabase(ontologies);
		
		/**
		 * instantiating template for a person (containing name, email address and gender)
		 */
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				Builder build = new Builder();
				Document xom1;
				InputStream is = null;
				try {
					is = this.getClass().getResourceAsStream("templates/template1.xml");
					xom1 = build.build(is);
					TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
					ContentItemService contentItemService = (ContentItemService) Component.getInstance("contentItemService");
					ContentItem personTemplate = contentItemService.createContentItem("Template_Person");
					contentItemService.updateTitle(personTemplate, "Template_Person");
					personTemplate.addType(tripleStore.createUriResource("http://www.kiwi-project.eu/Template"));
					personTemplate.addType(tripleStore.createUriResource("http://xmlns.com/foaf/0.1/Person"));
					
					contentItemService.updateTextContentItem(personTemplate, xom1.toXML());
				} catch (ValidityException e) {
					e.printStackTrace();
				} catch (ParsingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.run();
		
		/**
		 * 
		 */
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				ConfigurationService configurationService = (ConfigurationService) 
					Component.getInstance("configurationService");
				DataAccessService dataAccessService = 
					(DataAccessService) Component.getInstance("webservice.dataAccess");
				InputStream is = this.getClass().getResourceAsStream("templates/data4template1.rdf");
				
				
				
				StringBuilder sb = new StringBuilder();
				int c;
				try {
					byte[] b = new byte[4096];
					while((c = is.read(b)) != -1) {
						sb.append(new String(b,0,c));
					}
					dataAccessService.publishRDF(sb.toString(), 
							"application/rdf+xml", 
							"http://www.logica.dk/core/someTemplateInstance", 
							configurationService.getBaseUri()+"/Template_Person");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.run();
		
		/**
		 * 
		 */
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				ContentItemService contentItemService = (ContentItemService) 
					Component.getInstance("contentItemService");
				TemplateService templateService = (TemplateService) 
					Component.getInstance("templateService");
				RenderingService renderingService = (RenderingService)
					Component.getInstance("renderingPipeline");
				
				
				ContentItem templateInstance = contentItemService.getContentItemByUri("http://www.logica.dk/core/someTemplateInstance");
				String templateInstanceText = renderingService.renderHTML(templateInstance);
				System.out.println(templateInstanceText);
				
				List<KiWiTriple> triplesKiWi = templateService
					.getTemplateFields("http://www.logica.dk/core/someTemplateInstance");
				try {
					List<KiWiTriple> triplesLogica = kiwi.util.KiWiCollections.toList(transformInputStream2Triples(
							this.getClass()
							.getResourceAsStream("templates/data4template1.rdf")));
				
					System.out.println("Comparing triples " + triplesKiWi + 
							" (KiWi) with " + triplesLogica + " (Logica)");
					for(int i = 0; i < triplesLogica.size(); i++) {
						boolean exists = false;
						for(int j = 0; j < triplesKiWi.size(); j++) {
							KiWiTriple logTr = triplesLogica.get(i);
							KiWiTriple kiwTr = triplesKiWi.get(j);
							if(logTr.toRDFXML().equals(kiwTr.toRDFXML())) {
								exists = true;
							}
						}
						Assert.assertTrue(exists);
					}
				} catch (RepositoryException e) {
					e.printStackTrace();
				}
			}
			
			/**
			 * transforming an inputstream to Iterable<KiWiTriple>
			 * @param is
			 * @return
			 * @throws RepositoryException
			 */
			private final Iterable<KiWiTriple> transformInputStream2Triples(InputStream is) throws RepositoryException {
				ConfigurationService configurationService = (ConfigurationService) 
					Component.getInstance("configurationService");
				Repository myRepository = new SailRepository(new MemoryStore());
				Set<KiWiTriple> triples = new HashSet<KiWiTriple>();
				
				myRepository.initialize();
				RepositoryConnection myCon = myRepository.getConnection();

				ValueFactory f = myRepository.getValueFactory();

				try {
					myCon.add(
							is, 
							configurationService.getBaseUri(), 
							RDFFormat.RDFXML);
					
					RepositoryResult<Statement> result = myCon.getStatements(null, null, null, false);
					while(result.hasNext()) {
						triples.add(TripleStoreUtil.uncheckedSesameToKiWi(result.next()));
					}
					
					myCon.close();
				} catch (RDFParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				return triples;
			}
		}.run();
		
		/**
		 * 
		 */
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				ContentItemService contentItemService = (ContentItemService) 
					Component.getInstance("contentItemService");
				KiWiEntityManager kiwiEntityManager = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
				TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
				
				ContentItem template = contentItemService.getContentItemByTitle("Template_Person");
				ContentItem templateInstance = contentItemService.getContentItemByUri("http://www.logica.dk/core/someTemplateInstance");
				
				Set<KiWiTriple> triples;
				try {
					triples = KiWiCollections.toSet(template.getResource().listOutgoing("<http://www.kiwi-project.eu/kiwi/core/hasTemplateInstance>"));
				} catch (NamespaceResolvingException e) {
					e.printStackTrace();
					triples = Collections.emptySet();
				}
				Assert.assertTrue(triples.size() == 1);
				for(KiWiTriple t : triples) {
					Assert.assertEquals(((KiWiResource)t.getObject()).getContentItem(),templateInstance);
				}
			}
		}.run();
	}
	
	@Test
	public void testpublishingLUCData4Templates() throws Exception {
		
		String[] ontologies = {
    			"ontology_kiwi.owl",
    			"logica/logica.owl"
    	};
		setupDatabase(ontologies);
		
		/**
		 * instantiating template for a person (containing name, email address and gender)
		 */
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				Builder build = new Builder();
				Document xom1;
				InputStream is = null;
				try {
					is = this.getClass().getResourceAsStream("templates/template2.xml");
					xom1 = build.build(is);
					TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
					ContentItemService contentItemService = (ContentItemService) Component.getInstance("contentItemService");
					ContentItem personTemplate = contentItemService.createContentItem("Template_Employee");
					contentItemService.updateTitle(personTemplate, "Template_Employee");
					personTemplate.addType(tripleStore.createUriResource("http://www.kiwi-project.eu/Template2"));
					
					contentItemService.updateTextContentItem(personTemplate, xom1.toXML());
				} catch (ValidityException e) {
					e.printStackTrace();
				} catch (ParsingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.run();
		
		/**
		 * 
		 */
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				ConfigurationService configurationService = (ConfigurationService) 
					Component.getInstance("configurationService");
				DataAccessService dataAccessService = 
					(DataAccessService) Component.getInstance("webservice.dataAccess");
				InputStream is = this.getClass().getResourceAsStream("templates/data4template2.rdf");
				
				
				
				StringBuilder sb = new StringBuilder();
				int c;
				try {
					byte[] b = new byte[4096];
					while((c = is.read(b)) != -1) {
						sb.append(new String(b,0,c));
					}
					dataAccessService.publishRDF(sb.toString(), 
							"application/rdf+xml", 
							"http://www.logica.dk/DxA-employee-5", 
							configurationService.getBaseUri()+"/Template_Employee");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.run();
		
		/**
		 * 
		 */
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				ContentItemService contentItemService = (ContentItemService) 
					Component.getInstance("contentItemService");
				TemplateService templateService = (TemplateService) 
					Component.getInstance("templateService");
				RenderingService renderingService = (RenderingService)
					Component.getInstance("renderingPipeline");
				
				
				ContentItem templateInstance = contentItemService.getContentItemByUri("http://www.logica.dk/DxA-employee-5");
				String templateInstanceText = renderingService.renderHTML(templateInstance);
				System.out.println(templateInstanceText);
				
				List<KiWiTriple> triplesKiWi = templateService
					.getTemplateFields("http://www.logica.dk/DxA-employee-5");
				try {
					List<KiWiTriple> triplesLogica = kiwi.util.KiWiCollections.toList(transformInputStream2Triples(
							this.getClass()
							.getResourceAsStream("templates/data4template2.rdf")));
				
					System.out.println("Comparing triples " + triplesKiWi + 
							" (KiWi) with " + triplesLogica + " (Logica)");
					for(int i = 0; i < triplesLogica.size(); i++) {
						boolean exists = false;
						for(int j = 0; j < triplesKiWi.size(); j++) {
							KiWiTriple logTr = triplesLogica.get(i);
							KiWiTriple kiwTr = triplesKiWi.get(j);
							if(logTr.toRDFXML().equals(kiwTr.toRDFXML())) {
								exists = true;
							}
						}
						Assert.assertTrue(exists);
					}
				} catch (RepositoryException e) {
					e.printStackTrace();
				}
			}
			
			/**
			 * transforming an inputstream to Iterable<KiWiTriple>
			 * @param is
			 * @return
			 * @throws RepositoryException
			 */
			private final Iterable<KiWiTriple> transformInputStream2Triples(InputStream is) throws RepositoryException {
				ConfigurationService configurationService = (ConfigurationService) 
					Component.getInstance("configurationService");
				Repository myRepository = new SailRepository(new MemoryStore());
				Set<KiWiTriple> triples = new HashSet<KiWiTriple>();
				
				myRepository.initialize();
				RepositoryConnection myCon = myRepository.getConnection();

				ValueFactory f = myRepository.getValueFactory();

				try {
					myCon.add(
							is, 
							configurationService.getBaseUri(), 
							RDFFormat.RDFXML);
					
					RepositoryResult<Statement> result = myCon.getStatements(null, null, null, false);
					while(result.hasNext()) {
						triples.add(TripleStoreUtil.uncheckedSesameToKiWi(result.next()));
					}
					
					myCon.close();
				} catch (RDFParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				return triples;
			}
		}.run();
	}
}
