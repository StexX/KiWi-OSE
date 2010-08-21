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

package kiwi.test.action;

import junit.framework.Assert;
import kiwi.api.content.ContentItemService;
import kiwi.context.CurrentContentItemFactory;
import kiwi.model.content.ContentItem;

import org.jboss.seam.Component;
import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

/**
 * @author 	Stephanie Stroka
 * 			(stephanie.stroka@salzburgresearch.at)
 *
 */
public class EditContentTest extends SeamTest {

	@Test
	public void testLogin() throws Exception
	{
	
		new FacesRequest("/edit.xhtml") {
		
			@Override
			protected void processValidations() throws Exception
			{
				CurrentContentItemFactory ciFactory = (CurrentContentItemFactory) 
						Component.getInstance("currentContentItemFactory");
				ciFactory.loadContentItem();
				
				validateValue("#{editorAction.content}", 
						"Lorem ipsum dolor sit amet, consetetur sadipscing " +
						"elitr, sed diam nonumy eirmod tempor invidunt ut " +
						"labore et dolore magna aliquyam erat, sed diam voluptua. " +
						"At vero eos et accusam et justo duo dolores et ea rebum. " +
						"Stet clita kasd gubergren, no sea takimata sanctus est " +
						"Lorem ipsum dolor sit amet.");
				validateValue("#{editorAction.title}", "Lorem Ipsum");
				
				assert !isValidationFailure();
			}
			
			@Override
			protected void updateModelValues() throws Exception
			{
				CurrentContentItemFactory ciFactory = (CurrentContentItemFactory) 
						Component.getInstance("currentContentItemFactory");
				ciFactory.loadContentItem();
				
				setValue("#{editorAction.content}", 
						"Lorem ipsum dolor sit amet, consetetur sadipscing " +
						"elitr, sed diam nonumy eirmod tempor invidunt ut " +
						"labore et dolore magna aliquyam erat, sed diam voluptua. " +
						"At vero eos et accusam et justo duo dolores et ea rebum. " +
						"Stet clita kasd gubergren, no sea takimata sanctus est " +
						"Lorem ipsum dolor sit amet.");
				setValue("#{editorAction.title}", "Lorem Ipsum");
			}
			
			@Override
			protected void invokeApplication() throws Exception {
		
				invokeMethod("#{editorAction.storeContentItem}");

	             ContentItemService cis = (ContentItemService)
	             		Component.getInstance("contentItemService");
	             
	             ContentItem ci1 = cis.getContentItemByTitle("Lorem Ipsum");
	             Assert.assertNotNull(ci1);
	             Assert.assertNotNull(ci1.getResource());
	             Assert.assertNotNull(ci1.getResource().getKiwiIdentifier());
	             String kiwiId = ci1.getResource().getKiwiIdentifier();
	             
	             Assert.assertTrue(kiwiId.length() > 0);
	             
	             ContentItem ci2 = cis.getContentItemByKiwiId(kiwiId);
	             Assert.assertNotNull(ci2);
	             Assert.assertEquals(ci1, ci2);
	             
	             Assert.assertNotNull(ci1.getTextContent());
	             
	             String content = ci1.getTextContent().getXmlString();
	             
	             Assert.assertTrue(content.length() > 0);
	             
	             Assert.assertEquals("Lorem ipsum dolor sit amet, consetetur sadipscing " +
						"elitr, sed diam nonumy eirmod tempor invidunt ut " +
						"labore et dolore magna aliquyam erat, sed diam voluptua. " +
						"At vero eos et accusam et justo duo dolores et ea rebum. " +
						"Stet clita kasd gubergren, no sea takimata sanctus est " +
						"Lorem ipsum dolor sit amet.", content);
			}
			
		}.run();
	}
}
