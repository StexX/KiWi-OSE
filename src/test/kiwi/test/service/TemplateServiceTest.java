package kiwi.test.service;

import java.util.ArrayList;

import junit.framework.Assert;
import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.ontology.TemplateService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.test.base.KiWiTest;
import nu.xom.Builder;
import nu.xom.Document;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.testng.annotations.Test;

/**
 * This is a test for the TemplateServiceImpl.java. 
 * It checks whether the correct triples are returned 
 * by extracting the xhtml content of a ContentItem.
 * 
 * @author Stephanie Stroka (stephanie.stroka@salzburg.ac.at)
 *
 */
@Test
public class TemplateServiceTest extends KiWiTest {
	
	/**
	 * initializes the environment to test templates
	 * @throws Exception
	 */
	@Test
	public void setupTemplateEnvironment() throws Exception {
		
		String[] ontologies = {
    			"ontology_kiwi.owl",
    			"imports/foaf.owl"
    	};
		setupDatabase(ontologies);
		
		/**
		 * Creates two ContentItems, one linking to the other one.
		 * The two ContentItems contian RDFa properties and are 
		 * linked via RDFa rel and target attributes.
		 */
		new FacesRequest() {
			
			@Override
			protected void invokeApplication() throws Exception {
				Builder build = new Builder();
				Document xom2 = build.build(this.getClass().getResourceAsStream("xml_texts/templateTest2.xml"));
				TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
				ContentItemService contentItemService = (ContentItemService) Component.getInstance("contentItemService");
				
				ContentItem anotherPersonsTemplate = contentItemService.createContentItem("TrentReznor");
				contentItemService.updateTitle(anotherPersonsTemplate, "TrentReznor");
				anotherPersonsTemplate.addType(tripleStore.createUriResource("http://www.kiwi-project.eu/Template"));
				anotherPersonsTemplate.addType(tripleStore.createUriResource("http://xmlns.com/foaf/0.1/Person"));
				
				contentItemService.updateTextContentItem(anotherPersonsTemplate, xom2.toXML());
			}
		}.run();
		
		/**
		 * Creates two ContentItems, one linking to the other one.
		 * The two ContentItems contian RDFa properties and are 
		 * linked via RDFa rel and target attributes.
		 */
		new FacesRequest() {
			
			@Override
			protected void invokeApplication() throws Exception {
				Builder build = new Builder();
				Document xom1 = build.build(this.getClass().getResourceAsStream("xml_texts/templateTest.xml"));
				
				TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
				ContentItemService contentItemService = (ContentItemService) Component.getInstance("contentItemService");
				ContentItem personTemplate = contentItemService.createContentItem("JamieZawinksi");
				contentItemService.updateTitle(personTemplate, "JamieZawinksi");
				personTemplate.addType(tripleStore.createUriResource("http://www.kiwi-project.eu/Template"));
				personTemplate.addType(tripleStore.createUriResource("http://xmlns.com/foaf/0.1/Person"));
				
				contentItemService.updateTextContentItem(personTemplate, xom1.toXML());
			}
		}.run();
		
	}
	
	/**
	 * contains the tests for retrieving the template fields
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTempateFieldQuery() throws Exception {
		
		
		/**
		 * The TempateService is called to get a list of kiWiTriples, 
		 * which link to the RDFa properties and relations inside 
		 * of the ContentItem. They are checked for correctness.
		 */
		new FacesRequest() {
			
			@Override
			protected void invokeApplication() throws Exception {
				ConfigurationService configurationService = (ConfigurationService) 
					Component.getInstance("configurationService");
				Log log = Logging.getLog(this.getClass());
				
				TemplateService templateService = (TemplateService) 
					Component.getInstance("templateService");
				/* get all template fields */
				ArrayList<KiWiTriple> triples = templateService.getTemplateFields(
						configurationService.getBaseUri()+"/JamieZawinksi");
				
				for(KiWiTriple t : triples) {
					/* get the triples` subject */
					String sub = t.getSubject().getKiwiIdentifier();
					
					/* the subject is either the front page */
					if(sub.endsWith("JamieZawinksi")) {
						String prop = t.getProperty().getKiwiIdentifier();
						KiWiNode objNode = t.getObject();
						if(objNode.isLiteral()) {
							KiWiLiteral objLiteral = (KiWiLiteral) objNode;
							String literal = objLiteral.getContent();
							if(prop.equals("uri::http://xmlns.com/foaf/0.1/name")) {
								Assert.assertEquals("Jamie Zawinski", literal);
							} else if(prop.equals("uri::http://xmlns.com/foaf/0.1/mbox")) {
								Assert.assertEquals("jwz@jwz.org", literal);
							} else if(prop.equals("uri::http://xmlns.com/foaf/0.1/gender")) {
								Assert.assertEquals("male", literal);
							}
						} else if(objNode.isUriResource()) {
							KiWiUriResource objRes = (KiWiUriResource) objNode;
							if(prop.equals("uri::http://xmlns.com/foaf/0.1/knows")) {
								Assert.assertTrue(objRes.getUri().endsWith("TrentReznor"));
							}
						}
					}
					/* or the template for organdational unit */
					else if (sub.endsWith("TrentReznor")) {
						/* should just contain one property 'orgName' */
						String prop = t.getProperty().getKiwiIdentifier();
						KiWiNode objNode = t.getObject();
						if(objNode.isLiteral()) {
							if(prop.equals("uri::http://xmlns.com/foaf/0.1/name")) {
								Assert.assertEquals("Trent Reznor", ((KiWiLiteral) objNode).getContent());
							} else if(prop.equals("uri::http://xmlns.com/foaf/0.1/birthday")) {
								Assert.assertEquals("17. Mai 1965", ((KiWiLiteral) objNode).getContent());
							} else {
								// something went wrong
								log.error("Found an unexpected triple #0 ", t);
							}
						} else {
							// something went wrong
							log.error("Found an unexpected triple #0 ", t);
						}
					} else {
						// something went wrong
						log.error("Found an unexpected triple #0 ", t);
					}
				}
			}
		}.run();
	}
}
