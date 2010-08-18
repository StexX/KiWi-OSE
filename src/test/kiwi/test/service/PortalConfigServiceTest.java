/*
 * File : PortalConfigServiceTest.java.java
 * Date : Mar 17, 2010
 * 
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

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import junit.framework.Assert;
import kiwi.api.config.PortalConfigService;
import kiwi.api.user.UserService;
import kiwi.exception.UserExistsException;
import kiwi.model.user.User;
import kiwi.test.base.KiWiTest;
import kiwi.transport.client.WidgetConfig;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.security.Identity;
import org.testng.annotations.Test;

/**
 * @author Szaby Grünwald
 * @version 0.7
 * @since 0.7
 *
 */
@Test
public class PortalConfigServiceTest extends KiWiTest {
	
	@Test
	public void testPortalConfiguration() throws Exception {
    	
		clearDatabase();
    	/*
    	 * Create a user and set a widgetConfiguration
    	 */
		new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>> PortalConfigTest (1)");
            	PortalConfigService pcs = (PortalConfigService) Component.getInstance("kiwi.core.portalConfigService");
            	EntityManager        em = (EntityManager) Component.getInstance("entityManager");

            	UserService        us = (UserService) Component.getInstance("userService");
            	
            	User user=null;
				try {
					user = us.createUser("testuser_1", "Hans", "Mustermann", "hansPW");
				} catch (UserExistsException e) {
					log.error("testuser_1 exists already");
				}
            	
            	if(user==null)user = us.getUserByLogin("testuser_1");
            	
            	if(user==null)Assert.fail("user shouldn't be null!");
				
            	List<WidgetConfig> portalConfig = generateTestPortalConfig();
            	
            	pcs.setPortalConfiguration(user, "AppA", portalConfig);
            }

			private List<WidgetConfig> generateTestPortalConfig() {
				List<WidgetConfig> res = new LinkedList<WidgetConfig>();
            	WidgetConfig conf1 = new WidgetConfig();
            	conf1.setWidgetId("testWidget");
            	conf1.setColumn(0);
            	conf1.setRow(0);
            	conf1.setProperties("parameterA = 123");
            	res.add(conf1);
            	return res;
			}
    	}.run();

    	/*
    	 * test if the configuration is persisted and has the correct elements inside.
    	 */
    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	Identity.setSecurityEnabled(false);

            	Log log = Logging.getLog(this.getClass());
            	log.info(">>>>>>>>> PortalConfigTest (2)");
            	PortalConfigService pcs = (PortalConfigService) Component.getInstance("kiwi.core.portalConfigService");
            	EntityManager        em = (EntityManager) Component.getInstance("entityManager");

            	UserService        us = (UserService) Component.getInstance("userService");
            	User user = us.getUserByLogin("testuser_1");
            	
            	String applicationId = "AppA";
            	
            	List<WidgetConfig> portalConfig = pcs.getPortalConfiguration(user, applicationId);
            	
            	Assert.assertNotNull(user);
            	
            	Assert.assertEquals(1, pcs.getNumberOfPortalColumns(user, applicationId));
            	Assert.assertEquals(1, portalConfig.size());
            	Assert.assertEquals(0, portalConfig.get(0).getColumn());
            	Assert.assertEquals(0, portalConfig.get(0).getRow());
            	Assert.assertEquals("testWidget", portalConfig.get(0).getWidgetId());
            	Assert.assertEquals("parameterA = 123", portalConfig.get(0).getProperties());
            }
    	}.run();
	}
	
}
