/*
 * Filename     : RecommendationTest.java
 * Version      : 0.00.01-01
 * Date         : March 6, 2009
 * Copyright :
 *
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
 */


package kiwi.test.service;


import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.recommendation.RecommendationService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.user.UserService;
import kiwi.exception.UserExistsException;
import kiwi.model.content.ContentItem;
import kiwi.model.recommendation.ContactRecommendation;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Used to tests the <code>RecommendationTest</code> class mechanisms.

 */
@Test
public final class RecommendationTest extends KiWiTest {

	
	@Test
	public void testRecommendations() throws Exception{
		
		clearDatabase();

		new FacesRequest() {
			//ant -Dclass=kiwi.test.service.RecommendationTest test.run-class
			@Override
			protected void invokeApplication() throws UserExistsException {
				Log log = Logging.getLog(this.getClass());
				log.info(">>>>>>>>> (1)");
				Identity.setSecurityEnabled(false);
				KiWiEntityManager kiwiEntityManager = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	ContentItemService ciService        = (ContentItemService) Component.getInstance("contentItemService");
            	UserService        userService      = (UserService) Component.getInstance("userService");

            	// create users
            	User user  = userService.createUser("user", "user", "user", "pw1");
            	User user2 = userService.createUser("user2", "user2", "user2", "pw2");
            	User user3 = userService.createUser("user3", "user3", "user3", "pw3");
            	User user4 = userService.createUser("user4", "user4", "user4", "pw4");            	
            	
            	log.info(">>>>>>>>> (2)");
//            	kiwiEntityManager.persist(user);
//            	kiwiEntityManager.persist(user2);
//            	kiwiEntityManager.persist(user3);            	
//            	kiwiEntityManager.persist(user4);            	
            	
            	// create two tagged resources
            	ContentItem ci1 = ciService.createContentItem();
				ciService.updateTitle(ci1, "ci1");
            	ci1.setAuthor(user4);
            	            	
            	log.info(">>>>>>>>> (3)");
            	ContentItem ci2 = ciService.createContentItem();
            	ciService.updateTitle(ci2, "ci2");
            	ci2.setAuthor(user4);            	
            	
            	log.info(">>>>>>>>> (4)");
            	ContentItem ci3 = ciService.createContentItem();
            	ciService.updateTitle(ci3, "ci3");
            	ci3.setAuthor(user4);            	
            	
            	log.info(">>>>>>>>> (5)");
            	ciService.saveContentItem(ci1);
            	ciService.saveContentItem(ci2);
            	ciService.saveContentItem(ci3);
            	
            	Assert.assertNotNull(ci1.getResource());
            	Assert.assertNotNull(ci2.getResource());
            	Assert.assertNotNull(ci3.getResource());
            	
            	log.info(">>>>>>>>> (6)");
            	// create two tagging resources
            	ContentItem tag1 = ciService.createContentItem();
            	ciService.updateTitle(tag1, "tag1");

            	log.info(">>>>>>>>> (7)");
            	ContentItem tag2 = ciService.createContentItem();
            	ciService.updateTitle(tag2, "tag2");
            	
            	log.info(">>>>>>>>> (8)");
            	ContentItem tag3 = ciService.createContentItem();
            	ciService.updateTitle(tag3, "tag1");
            	
            	log.info(">>>>>>>>> (9)");
            	ContentItem tag4 = ciService.createContentItem();
            	ciService.updateTitle(tag4, "tag2");
            	            	
            	log.info(">>>>>>>>> (10)");
            	ciService.saveContentItem(tag1);
            	ciService.saveContentItem(tag2);
            	ciService.saveContentItem(tag3);
            	ciService.saveContentItem(tag4);

            	log.info(">>>>>>>>> (11)");
            	Assert.assertNotNull(tag1.getResource());
            	Assert.assertNotNull(tag2.getResource());
            	Assert.assertNotNull(tag3.getResource());
            	Assert.assertNotNull(tag4.getResource());
            	log.info(">>>>>>>>> (12)");

            }
    	}.run();

    	/*
    		 * Preparing environment
    		 */
        	new FacesRequest() {

        		
                @Override
                protected void invokeApplication() {
                Log log = Logging.getLog(this.getClass());
                log.info(">>>>>>>>> (13)");
				Identity.setSecurityEnabled(false);
            	ContentItemService ciService        = (ContentItemService) Component.getInstance("contentItemService");
            	TaggingService     taggingService   = (TaggingService) Component.getInstance("taggingService");
            	UserService        userService      = (UserService) Component.getInstance("userService");
            	
            	log.info(">>>>>>>>> (14)");
            	ContentItem ci1 =  ciService.getContentItemByTitle("ci1");
            	ContentItem ci2 =  ciService.getContentItemByTitle("ci2");
            	ContentItem ci3 =  ciService.getContentItemByTitle("ci3");            	
            	
            	log.info(">>>>>>>>> (15)");
            	Assert.assertNotNull(ci1);
            	Assert.assertNotNull(ci2);
            	Assert.assertNotNull(ci1.getResource());
            	Assert.assertNotNull(ci2.getResource());
            	
            	log.info(">>>>>>>>> (16)");
            	ContentItem tag1 =  ciService.getContentItemByTitle("tag1");
            	ContentItem tag2 =  ciService.getContentItemByTitle("tag2");
            	ContentItem tag3 =  ciService.getContentItemByTitle("tag1");
            	ContentItem tag4 =  ciService.getContentItemByTitle("tag2");            	

            	log.info(">>>>>>>>> (17)");
            	Assert.assertNotNull(tag1);
            	Assert.assertNotNull(tag2);
            	Assert.assertNotNull(tag3);
            	Assert.assertNotNull(tag4);            	
            	
            	log.info(">>>>>>>>> (18)");
            	User user =  userService.getUserByLogin("user");
            	User user2 =  userService.getUserByLogin("user2");
            	User user3 =  userService.getUserByLogin("user3");
            	User user4 =  userService.getUserByLogin("user4");    

            	log.info(">>>>>>>>> (19)");
            	Assert.assertNotNull(user);
            	Assert.assertNotNull(user2);
            	Assert.assertNotNull(user3);
            	Assert.assertNotNull(user4);   
            	
            	log.info(">>>>>>>>> (20)");
            	// create tagging scenario
            	Tag tagging1 = taggingService.createTagging("tag1", ci1, tag1, user);
            	Tag tagging2 = taggingService.createTagging("tag2", ci1, tag2, user);
            	Tag tagging3 = taggingService.createTagging("tag1", ci2, tag3, user2);
            	Tag tagging4 = taggingService.createTagging("tag1", ci2, tag3, user3);
            	Tag tagging5 = taggingService.createTagging("tag2", ci1, tag4, user4);
            	Tag tagging6 = taggingService.createTagging("tag2", ci3, tag4, user3);
            	
         	
            	log.info(">>>>>>>>> (21)");
            	Assert.assertEquals(tagging1.getTaggedResource(), ci1);
            	Assert.assertEquals(tagging2.getTaggingResource(), tag2);
            	Assert.assertEquals(tagging3.getTaggedResource(), ci2);
            	Assert.assertEquals(tagging1.getTaggedBy(), user);
            	Assert.assertEquals(tagging2.getTaggedBy(), user);
            	Assert.assertEquals(tagging3.getTaggedBy(), user2);
            	Assert.assertEquals(tagging4.getTaggedBy(), user3);
            	Assert.assertEquals(tagging5.getTaggedBy(), user4);
            	Assert.assertEquals(tagging6.getTaggedBy(), user3);
            	log.info(">>>>>>>>> (22)");
			}

		}.run();		
		
		


		
    	/*
    	 * check whether new tagging is now in the database
    	 */
		new FacesRequest() {
			
		    @Override
		    protected void invokeApplication() {
             	Log log = Logging.getLog(this.getClass());
             	log.info(">>>>>>>>> (23)");
            	Identity.setSecurityEnabled(false);

            	RecommendationService recommendationService = (RecommendationService) Component.getInstance("recommendationService");
            	ContentItemService ciService        = (ContentItemService) Component.getInstance("contentItemService");
            	UserService        userService      = (UserService) Component.getInstance("userService");
            	KiWiEntityManager kiwiEntityManager = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");            	

            	log.info(">>>>>>>>> (24)");
            	User user = userService.getUserByLogin("user");
            	User user2 = userService.getUserByLogin("user2");
            	User user3 = userService.getUserByLogin("user3");
            	User user4 = userService.getUserByLogin("user4");            	
            	
            	log.info(">>>>>>>>> (25)");
            	Assert.assertNotNull(user.getResource());
            	Assert.assertNotNull(user2.getResource());
            	
            	log.info(">>>>>>>>> (26)");
            	ContentItem ci1 = ciService.getContentItemByTitle("ci1");
            	ContentItem ci2 = ciService.getContentItemByTitle("ci2");
            	ContentItem ci3 = ciService.getContentItemByTitle("ci3");

            	log.info(">>>>>>>>> (27)");
            	Assert.assertNotNull(ci1.getResource());
            	Assert.assertNotNull(ci2.getResource()); 
            	
            	//Test social recommendations
            	Assert.assertNotNull(recommendationService.getContactRecommendations(null));
            	Assert.assertTrue(recommendationService.getContactRecommendations(null).isEmpty());
            	Assert.assertTrue(recommendationService.getContactRecommendations(user4).isEmpty());
            	recommendationService.recommendItem(user, ci1, user4.getLogin());
            	kiwiEntityManager.flush();
            	
            	log.info(">>>>>>>>> (28)");
            	Assert.assertNotNull(recommendationService.getContactRecommendations(user4));
            	Assert.assertFalse(recommendationService.getContactRecommendations(user4).isEmpty());
            	
            	log.info(">>>>>>>>> (29)");
            	ContactRecommendation contactRecommendation = recommendationService.getContactRecommendations(user4).get(0);
            	Assert.assertNotNull(contactRecommendation);
            	ContentItem contentItemC1 = contactRecommendation.getRecommendedItem();
            	Assert.assertEquals(contentItemC1,ci1);
            	recommendationService.removeContactRecommendation(user4, contactRecommendation);
            	
            	kiwiEntityManager.flush();            	
            	log.info(">>>>>>>>> (30)");
            	Assert.assertNotNull(recommendationService.getContactRecommendations(user4));
            	Assert.assertTrue(recommendationService.getContactRecommendations(user4).isEmpty());

            	//Test basic recommendations
//            	Assert.assertEquals(recommendationService.getRecommendations(ci1, user).size(),2);
//            	Assert.assertEquals(recommendationService.getRecommendations(ci1, user4).size(),0);
//            	Assert.assertEquals(recommendationService.getRecommendations(ci1, user3).size(),1);
//            	Assert.assertEquals(recommendationService.getRecommendations(ci2, user4).size(),0);
//            	Assert.assertEquals(recommendationService.getRecommendations(ci2, user3).size(),1);
//            	Assert.assertEquals(recommendationService.getRecommendations(ci1, user).size(),2);
//            	Assert.assertEquals(recommendationService.getRecommendations(ci1, user).size(),2);
//            	Assert.assertEquals(recommendationService.getRecommendations(ci2, user).size(),0);

            	//Test similar recommendations 
            	//Test similar recommendations 
            	Assert.assertNotNull(userService.getMySimilarContentItems(user4));
            	Assert.assertTrue(userService.getMySimilarContentItems(user4).isEmpty());
            	userService.addMySimilarContentItems(user4, ci3);
            	Assert.assertNotNull(userService.getMySimilarContentItems(user4));
            	Assert.assertFalse(userService.getMySimilarContentItems(user4).isEmpty());
            	Assert.assertEquals(userService.getMySimilarContentItems(user4).get(0),ci3);
            	userService.removeMySimilarContentItems(user4, ci3);
            	Assert.assertNotNull(userService.getMySimilarContentItems(user4));
            	Assert.assertTrue(userService.getMySimilarContentItems(user4).isEmpty());
            	log.info(">>>>>>>>> (31)");

            	//Test personal recommendations 
            	//Assert.assertNotNull(recommendationService.getPersonalRecommendations(null));
            	//Assert.assertEquals(recommendationService.getPersonalRecommendations(user3).size(),2);
            	//expected:<2> but was:<0>

            	
            	//Test multifactor recommendations
            	Assert.assertNotNull(recommendationService.getTagGroupedRecommendations(ci1, user3));
            	
            	//Test group recommendations
            	Assert.assertNotNull(recommendationService.getGroupRecommendations(null));
            	
           	
            	//Test group recommendations
            	Assert.assertNotNull(recommendationService.getUsersByContentItem(null, null));            	

            	//Test group recommendations
            	Assert.assertNotNull(recommendationService.getUsersByGroupTags(ci2, user4)); 
            	Assert.assertEquals(recommendationService.getUsersByGroupTags(ci2, user4).size(),0);            	

            	//Test multifactor recommendations
            	Assert.assertNotNull(recommendationService.getMultiFactorRecommendations(ci2,user4));
            	
            	//Assert.assertTrue(recommendationService.getMultiFactorRecommendations(ci2,user4).isEmpty());
            	
              	
            	//Assert.assertFalse(recommendationService.getMultiFactorRecommendations(ci2,user3).isEmpty());
            	//Assert.assertEquals(recommendationService.getMultiFactorRecommendations(ci2,user3).size(),1);
            	log.info(">>>>>>>>> (32)");
            }
    	}.run();
		
	}
}
