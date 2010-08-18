/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 * 
 * Contributor(s):
 * 
 * 
 */

package kiwi.test.service;

import java.util.HashSet;
import java.util.List;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.ontology.OntologyService;
import kiwi.api.ontology.SKOSService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.ontology.KiWiClass;
import kiwi.model.ontology.KiWiProperty;
import kiwi.model.ontology.SKOSConcept;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Sebastian Schaffert
 *
 */
@Test
public class OntologyServiceTest extends KiWiTest {

	private static final String NS_TEST = "http://www.kiwi-project.eu/kiwi/test/";
	
	@Test
	public void testListClassesAndProperties() throws Exception {
		String[] ontologies = { "test/ontologyservice_test.owl" };
		setupDatabase(ontologies);
		
		System.out.println("Setup completed");
		/*
		 * Test the kiwiEntityManager persist interface by storing a new PointOfInterestFacade
		 * with a comment.
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	TripleStore       ts   = (TripleStore) Component.getInstance("tripleStore");
            	OntologyService   os   = (OntologyService) Component.getInstance("ontologyService");
            	
            	List<KiWiClass> classes = os.listClasses();
            	
            	Assert.assertEquals(classes.size(), 5);
            	
            	List<KiWiProperty> properties = os.listProperties();
            	
            	Assert.assertEquals(properties.size(), 3);
            	
            	KiWiResource i1 = ts.createUriResource(NS_TEST + "instance1");
            	KiWiResource i2 = ts.createUriResource(NS_TEST + "instance2");
            	
            	// check whether we get the right number of applicable properties, first test should be 1, second 0
            	List<KiWiProperty> app_properties = os.listApplicableProperties(i1, i2);
            	
            	Assert.assertEquals(app_properties.size(), 1);
            	
            	List<KiWiProperty> nonapp_properties = os.listApplicableProperties(i2, i1);
            	
            	Assert.assertEquals(nonapp_properties.size(), 0);
            	
            	// check whether listApplicableDataTypeProperties gives results.
            	Assert.assertTrue(os.listApplicableDataTypeProperties(i1).size()>0);
            }
    	}.run();

    	shutdownDatabase();
	}

	@Test
	public void testKiWiClassAndProperty() throws Exception {
		String[] ontologies = { "test/ontologyservice_test.owl" };
		setupDatabase(ontologies);
		
		/*
		 * Test the kiwiEntityManager persist interface by storing a new PointOfInterestFacade
		 * with a comment.
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	KiWiEntityManager km   = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	TripleStore       ts   = (TripleStore) Component.getInstance("tripleStore");
            	OntologyService   os   = (OntologyService) Component.getInstance("ontologyService");
            	
            	
            	KiWiResource _p1 = ts.createUriResource(NS_TEST + "c1c2");
            	KiWiResource _p2 = ts.createUriResource(NS_TEST + "prop1");
            	
            	KiWiResource _c1 = ts.createUriResource(NS_TEST + "D2");
            	KiWiResource _c2 = ts.createUriResource(NS_TEST + "C1");
            	
            	
            	// check whether getDomain and getRange work
            	KiWiProperty p1 = km.createFacade(_p1.getContentItem(), KiWiProperty.class);
            	KiWiProperty p2 = km.createFacade(_p2.getContentItem(), KiWiProperty.class);
            	
            	// TODO: can someone explain why the sizes of 
            	// p1 and p2 domains are expected to be 1, 
            	// and the sizes of p1 and p2 ranges are 
            	// expected to be 1 and 0?
            	Assert.assertEquals(p1.getDomain().size(), 1);
            	Assert.assertEquals(p1.getRange().size(), 1);
            	
            	Assert.assertEquals(p2.getDomain().size(), 1);
               	Assert.assertEquals(p2.getRange().size(), 0);
               	
               	// check whether getSuperClasses works
               	KiWiClass c1 = km.createFacade(_c1.getContentItem(), KiWiClass.class);
               	
               	Assert.assertEquals(c1.getSuperClasses().size(), 1);
               	
               	KiWiClass sc = c1.getSuperClasses().iterator().next();
               	Assert.assertEquals(sc.getResource().getKiwiIdentifier(), "uri::"+NS_TEST+"D1");
               	
               	// check whether getSubClasses works (@RDFInverse annotation on facades)
               	KiWiClass c2 = km.createFacade(_c2.getContentItem(), KiWiClass.class);
               	Assert.assertEquals(c2.getSubClasses().size(), 2);
             }
    	}.run();

    	shutdownDatabase();
	}

	@Test
	public void testSKOSConcepts() throws Exception {
		String[] ontologies = { "test/skos_test.owl" };
		setupDatabase(ontologies);
		
		System.out.println("Setup completed");
		/*
		 * Test whether we get back all top concepts from the skosService
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	KiWiEntityManager km   = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	TripleStore       ts   = (TripleStore) Component.getInstance("tripleStore");
            	SKOSService       skos = (SKOSService) Component.getInstance("skosService");
            	
            	
            	List<SKOSConcept> concepts = skos.getTopConcepts();
            	Assert.assertEquals(concepts.size(), 1);
            	
            	HashSet<SKOSConcept> narrower = concepts.get(0).getNarrower();
             	Assert.assertEquals(narrower.size(), 2);
             	
             	Assert.assertEquals(concepts.get(0).getPreferredLabel(), "Concept 1");
             	Assert.assertEquals(concepts.get(0).getAlternativeLabels().size(), 1);
             	Assert.assertEquals(concepts.get(0).getAlternativeLabels().iterator().next(), "Konzept 1");
              }
    	}.run();

    	shutdownDatabase();
		
	}
}
