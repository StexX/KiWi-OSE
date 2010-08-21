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
package tagit.test.service;

import javax.persistence.EntityManager;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.content.ContentItem;
import kiwi.model.facades.PointOfInterestFacade;
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
public class ExplorerTest extends KiWiTest {

	
	/**
	 * Test the range query facility in the explorer action
	 */
	public void testRangeQuery() throws Exception {
    	/*
    	 * Check whether we can create the point of interest correctly
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
             	EntityManager        em = (EntityManager) Component.getInstance("entityManager");
             	ContentItemService   cs = (ContentItemService) Component.getInstance("contentItemService");
				KiWiEntityManager    km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");

            	ContentItem c = cs.createContentItem();
            	cs.updateTitle(c, "Salzburg");
            	PointOfInterestFacade poi = km.createFacade(c, PointOfInterestFacade.class);
            	
            	poi.setLatitude(47.8);
            	poi.setLongitude(13.03);
            	
            	km.persist(poi);
           	
            }
    	}.run();

    	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);
            	
             	EntityManager        em = (EntityManager) Component.getInstance("entityManager");
             	ContentItemService   cs = (ContentItemService) Component.getInstance("contentItemService");
				KiWiEntityManager    km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");

            	ContentItem c = cs.getContentItemByTitle("Salzburg");
            	Assert.assertEquals(c.getTitle(), "Salzburg");
            	
            	PointOfInterestFacade poi = km.createFacade(c, PointOfInterestFacade.class);
            	Assert.assertEquals(poi.getLatitude(), 47.8, 0.01);
            	Assert.assertEquals(poi.getLongitude(), 13.03, 0.01);
            	
            	
            }
    	}.run();
    	
    	
	}
}
