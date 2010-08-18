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
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
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
 */

package kiwi.test.model;

import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.security.Identity;
import org.testng.annotations.Test;

/**
 *
 * @author sschaffe
 */
@Test
public class TaggingTest extends SeamTest {

    @Test
    public void testTagging() throws Exception {
    	
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

	        	Identity.setSecurityEnabled(false);
	        	
	        	// TODO
	        	
	        	/*
		    	KnowledgeSpace kspace = (KnowledgeSpace) getInstance("kiwi.backend.kspace");
		    	
		    	User user;
		    	if( !kspace.userExists("rodolfo") ) {
		    		user = kspace.createUser("rodolfo","pass123");
		    		user.setPassword("pass123");
		    		user.setFirstName("Rolf");
		    		user.setLastName("Sint");
		    		user.setEmail("rodolfo@gmx.at");
		    		kspace.storeUser(user);
		    	} else {
		    		user = kspace.getUserByLogin("rodolfo");
		    	}
		    	
		    	User tagger;
		    	if( !kspace.userExists("stexx") ) {
		    		tagger = kspace.createUser("stexx","pass123");
		    		tagger.setPassword("pass124");
		    		tagger.setFirstName("Steffi");
		    		tagger.setLastName("Stroka");
		    		tagger.setEmail("steffi@stroka.com");
		    		kspace.storeUser(tagger);
		    	} else {
		    		tagger = kspace.getUserByLogin("stexx");
		    	}
		    	
		    	ContentItem ci;
		    	TextContent tc = null;
		    	if( !kspace.contentItemExists("softwaretest") ) {
		    		ci = kspace.createContentItem("softwaretest");
		    		tc = kspace.createTextContent(ci, "<doc><a>Softwaretests are about testing software</a></doc>", user);
		    	} else {
		    		ci = kspace.getContentItemByTitle("softwaretest");
		    	}
		    	
		    	ContentItem ci2;
		    	TextContent tc2 = null;
		    	if( !kspace.contentItemExists("examination") ) {
		    		ci2 = kspace.createContentItem("examination");
		    		tc2 = kspace.createTextContent(ci2, "<doc><a>Examinations are about testing knowledge of students</a></doc>", user);
		    	} else {
		    		ci2 = kspace.getContentItemByTitle("examination");
		    	}
		    	
		    	ContentItem ci3;
		    	TextContent tc3 = null;
		    	if( !kspace.contentItemExists("textabouttest") ) {
		    		ci3 = kspace.createContentItem("textabouttest");
		    		tc3 = kspace.createTextContent(ci3, "<doc><a>This is a text about tests in SW-development</a></doc>", user);
		    	} else {
		    		ci3 = kspace.getContentItemByTitle("textabouttest");
		    	}
		
		    	Tag t = null;
		    	Tag t2 = null;
		    	if( !kspace.tagExists("Test", ci)) {
		    		t = kspace.createTag(user, "Test", ci);
		    		kspace.storeTag(t);
		    	} else {
		    		t = kspace.getTagByMeaning("Test", ci);
		    	}
		        if( !kspace.tagExists("Test", ci2)) {
		        	t2 = kspace.createTag(user, "Test", ci2);
			        kspace.storeTag(t2);
		        } else {
		        	t2 = kspace.getTagByMeaning("Test", ci2);
		        }
		        
		        if( !ci3.getTags().contains(t) ) {
		        	kspace.tagContentItem(ci3, t);
		        }
		        
		        Set<ContentItem> availableMeanings = kspace.getMeaningsByTagLabel("Test");
		        Assert.assertTrue(availableMeanings.contains(ci));
		        Assert.assertTrue(availableMeanings.contains(ci2));
		        
				Assert.assertEquals(kspace.getTagByMeaning("Test", ci).getId(),t.getId());
				Assert.assertEquals(kspace.getTagByMeaning("Test", ci2).getId(),t2.getId());
				
				Set<Tag> tags = kspace.getTagsByLabel("Test");
				
				Assert.assertTrue(tags.contains(t));
				Assert.assertTrue(tags.contains(t2));
				
				Assert.assertTrue(kspace.getTagByMeaning("Test", ci).getTaggedResources().contains(ci3));
				
				kspace.getTripleStore().exportData(System.err, KiWiDataFormat.RDFXML);
				*/
			}
    	}.run();
    }
}
