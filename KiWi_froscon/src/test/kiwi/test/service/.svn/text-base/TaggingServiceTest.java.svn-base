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

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.exception.UserExistsException;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;

import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class TaggingServiceTest extends KiWiTest {

	@Test
	public void testTaggingService() throws Exception {
		clearDatabase();
		
		/*
		 * Check creation of new tagging
		 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	try {
	            	KiWiEntityManager km   = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
	            	TripleStore       ts   = (TripleStore) Component.getInstance("tripleStore");
	            	ContentItemService ciService        = (ContentItemService) Component.getInstance("contentItemService");
	            	TaggingService     taggingService   = (TaggingService) Component.getInstance("taggingService");
	            	UserService        userService      = (UserService) Component.getInstance("userService");
	 
	            	// create two ContentItems for use in tagging
	            	ContentItem ci1 = ciService.createContentItem();
	            	ciService.updateTitle(ci1, "ci1");
	            	
	            	ContentItem ci2 = ciService.createContentItem();
	            	ciService.updateTitle(ci2, "ci2");
	            	
	            	User user       = userService.createUser("mustermann", "Hans", "Mustermann", "securePW");
	            	
	            	km.persist(ci1);
	            	km.persist(ci2);
	            	km.persist(user);
	            	
	            	// create a new tag and check its properties
	            	Tag t = taggingService.createTagging("mytag", ci1, ci2, user);
	            	
	             	// check whether listApplicableDataTypeProperties gives results.
	            	Assert.assertEquals(t.getTaggedResource(), ci1);
	            	Assert.assertEquals(t.getTaggingResource(), ci2);
	            	Assert.assertEquals(t.getTaggedBy(), user);
	            	
	            	// since we are still in the same transaction, this request will fail...
	            	//Assert.assertEquals(taggingService.getTags(ci1).size(),1);
            	} catch(UserExistsException ex) {
            		Assert.fail("user already exists");
            	}
            	
            }
    	}.run();
		
    	/*
    	 * check whether new tagging is now in the database
    	 */
       	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
            	KiWiEntityManager km   = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
            	TripleStore       ts   = (TripleStore) Component.getInstance("tripleStore");
            	ContentItemService ciService        = (ContentItemService) Component.getInstance("contentItemService");
            	TaggingService     taggingService   = (TaggingService) Component.getInstance("taggingService");
            	UserService        userService      = (UserService) Component.getInstance("userService");
 
            	ContentItem ci1 = ciService.getContentItemByTitle("ci1");
            	ContentItem ci2 = ciService.getContentItemByTitle("ci2");
            	User user       = userService.getUserByLogin("mustermann");
            	
            	Assert.assertEquals(taggingService.getTags(ci1).size(),1);
            	
            	// create a new tag and check its properties
            	Tag t = taggingService.getTags(ci1).iterator().next();
            	
            	
             	// check whether listApplicableDataTypeProperties gives results.
            	Assert.assertEquals(t.getTaggedResource(), ci1);
            	Assert.assertEquals(t.getTaggingResource(), ci2);
            	Assert.assertEquals(t.getTaggedBy().getLogin(), user.getLogin());
            	
         	
            }
    	}.run();
	}
	
	@Test
	public void testTagCloudService() {
		
	}
}
