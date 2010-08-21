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

import interedu.api.dataimport.InterEduArtikelFacade;

import java.util.HashSet;

import junit.framework.Assert;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Identity;
import org.testng.annotations.Test;

/**
 * @author Rolf Sint
 * 
 */
@Test
public class FacadeTest extends KiWiTest {

//	@Test
//	public void testFacades() throws Exception {
//		clearDatabase();
//		
//		new FacesRequest() {
//
//			@Override
//			protected void invokeApplication() {
//
//				Identity.setSecurityEnabled(false);
//
//				KiWiEntityManager kiwiEntityManager = (KiWiEntityManager) Component
//						.getInstance("kiwiEntityManager");
//				
//				// initialize the components required for this test
//				ContentItemService contentItemService = (ContentItemService) Component.getInstance("contentItemService");
//            	TripleStore       tripleStore   = (TripleStore) Component.getInstance("tripleStore");
//            	User              user = (User) Component.getInstance("currentUser");
//            	
//            	//Create a new Content Item for the Article Facade
//            	ContentItem artikel = contentItemService.createContentItem("interEdu/"+"123456");
//            	contentItemService.updateTitle(artikel, "TestEintrag");
//				artikel.setAuthor(user);
//				artikel.addType(tripleStore.createUriResource(Constants.NS_INTEREDU_CORE+"Artikel"));
//				
//				//persists the contentItem for the Article
//				kiwiEntityManager.persist(artikel);
//				
//				//Creates a Facade, in this example an IntereduArtikelFacade 
//				InterEduArtikelFacade intArt = kiwiEntityManager.createFacade(artikel, InterEduArtikelFacade.class);
//				intArt.setArtikelAutor("rodolfo");
//				
//				//Each Artikel may have several categories, therefore some categories are created in this section
//				HashSet<String> categoriesSet = new HashSet<String>();
//				
//				categoriesSet.add("category1");
//				categoriesSet.add("category2");
//				
//				//Categorie Number 1
////                ContentItem catContItem1 =
////                	contentItemService.createContentItem("interEdu/"+"1");
////                kiwiEntityManager.persist(catContItem1);
////
////				
////				//Categorie Number 2
////                ContentItem catContItem2 =
////                    contentItemService.createContentItem("interEdu/"+"2");
////                kiwiEntityManager.persist(catContItem2);
////				
////
////				
////				//the categories are added to the HashMap,
////                categoriesSet.add(catContItem1);
////                categoriesSet.add(catContItem2);
////				
//				//The HAshMap is added to the article
//				//intArt.setCategories(categoriesSet);
//				
//				//Tests if the size of the categories is equivalent to number of categories which were added to the article
//				// does not work, because transaction has not yet been committed; maybe we need to cache in KiWiInvocationHandler?
//				//Assert.assertEquals(intArt.getCategories().size(), 2);
//				
//			}
//
//		}.run();		
//
//		new FacesRequest() {
//
//			@Override
//			protected void invokeApplication() {
//							
//				KiWiEntityManager kiwiEntityManager = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
//				ContentItemService contentItemService = (ContentItemService) Component.getInstance("contentItemService");
//
//				ContentItem artikel = contentItemService.getContentItemByTitle("TestEintrag");
//				InterEduArtikelFacade intArt = kiwiEntityManager.createFacade(artikel, InterEduArtikelFacade.class);
//
//				
//				//Tests if the size of the categories is equivalent to number of categories which were added to the article
//				//Assert.assertEquals(intArt.getCategories().size(), 2);
//				
//			}
//
//		}.run();
//	}
	
	@Test
	public class FacadeTest2 extends KiWiTest {

		@Test
		public void testRDFAnnotations() throws Exception {
			clearDatabase();
			
			new FacesRequest() {

				@Override
				protected void invokeApplication() {

					Log log = Logging.getLog(FacadeTest.class);
					log.info("started RDF annotation test");
					Identity.setSecurityEnabled(false);

					KiWiEntityManager kiwiEntityManager = (KiWiEntityManager) Component
							.getInstance("kiwiEntityManager");
					
					// initialize the components required for this test
	            	TripleStore       tripleStore   = (TripleStore) Component.getInstance("tripleStore");
	            	User              user = (User) Component.getInstance("currentUser");

	            	user.setLastName("Mueller");
	            	
	            	kiwiEntityManager.persist(user);
	            	
	            	
	            	Assert.assertEquals(1, tripleStore.getTriplesBySP(user.getResource(), tripleStore.createUriResource(Constants.NS_FOAF + "name")).size());
	            	Assert.assertEquals(1, tripleStore.getTriplesBySP(user.getResource(), tripleStore.createUriResource(Constants.NS_FOAF + "surname")).size());
	            	Assert.assertEquals(1, tripleStore.getTriplesBySP(user.getResource(), tripleStore.createUriResource(Constants.NS_FOAF + "family_name")).size());
	            	Assert.assertEquals(1, tripleStore.getTriplesBySP(user.getResource(), tripleStore.createUriResource(Constants.NS_FOAF + "familyName")).size());
	            	Assert.assertEquals(1, tripleStore.getTriplesBySP(user.getResource(), tripleStore.createUriResource(Constants.NS_FOAF + "lastName")).size());
	            	
	            	KiWiTriple name = tripleStore.getTriplesBySP(user.getResource(), tripleStore.createUriResource(Constants.NS_FOAF + "name")).get(0);
	            	KiWiTriple surname = tripleStore.getTriplesBySP(user.getResource(), tripleStore.createUriResource(Constants.NS_FOAF + "surname")).get(0);
	            	KiWiTriple family_name = tripleStore.getTriplesBySP(user.getResource(), tripleStore.createUriResource(Constants.NS_FOAF + "family_name")).get(0);
	            	KiWiTriple familyName= tripleStore.getTriplesBySP(user.getResource(), tripleStore.createUriResource(Constants.NS_FOAF + "familyName")).get(0);
	            	KiWiTriple lastname = tripleStore.getTriplesBySP(user.getResource(), tripleStore.createUriResource(Constants.NS_FOAF + "lastName")).get(0);
	            	
	            	Assert.assertTrue(name.getObject().isLiteral());
	            	Assert.assertTrue(surname.getObject().isLiteral());
	            	Assert.assertTrue(family_name.getObject().isLiteral());
	            	Assert.assertTrue(familyName.getObject().isLiteral());
	            	Assert.assertTrue(lastname.getObject().isLiteral());
	            	
	            	Assert.assertEquals("Mueller", ((KiWiLiteral)name.getObject()).getContent());
	            	Assert.assertEquals("Mueller", ((KiWiLiteral)surname.getObject()).getContent());
	            	Assert.assertEquals("Mueller", ((KiWiLiteral)family_name.getObject()).getContent());
	            	Assert.assertEquals("Mueller", ((KiWiLiteral)familyName.getObject()).getContent());
	            	Assert.assertEquals("Mueller", ((KiWiLiteral)lastname.getObject()).getContent());
	            	
	            	log.info("stopped RDF annotation test");
				}

			}.run();
		}
	}

}
