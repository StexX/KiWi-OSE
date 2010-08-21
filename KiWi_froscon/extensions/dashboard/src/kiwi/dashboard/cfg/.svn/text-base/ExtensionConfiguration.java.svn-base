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
package kiwi.dashboard.cfg;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import kiwi.api.extension.ExtensionService;
import kiwi.api.extension.KiWiApplication;
import kiwi.transport.client.WidgetConfig;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.dashboard.cfg")
@Scope(ScopeType.STATELESS)
public class ExtensionConfiguration {

	@Logger
	private Log log;
	
	@In
	private ExtensionService extensionService;
	
	@Observer("org.jboss.seam.postInitialization") 
	public void initializeExtension() {
		
		log.info("KiWi Dashboard Extension initialising ...");
		
		extensionService.registerApplication(new KiWiApplication() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 3511390316130764221L;

			@Override
			public String getIdentifier() {
				return "dashboard";
			}

			@Override
			public String getName() {
				return "Dashboard";
			}

			@Override
			public String getDescription() {
				return "My Universe";
			}

			@Override
			public Set<String> getPermissibleRoles() {
				return null;
			}

			/* (non-Javadoc)
			 * @see kiwi.api.extension.KiWiApplication#getDefaultPortalConfig()
			 */
			@Override
			public List<WidgetConfig> getDefaultPortalConfig() {
				LinkedList<WidgetConfig> res = new LinkedList<WidgetConfig>();
				String properties = "arg123 = XYZ";
				res.add(WidgetConfig.generate("profile", "Profile widget", 0, 0, properties));
				
//				properties = "width = 100";
//				res.add(WidgetConfig.generate("profilephoto", "Profile Photo widget", 0, 1, properties));
				
//				properties = "arg456 = ABCD";
//				res.add(WidgetConfig.generate("profile", "Profile widget", 1, 0, properties));

				properties = "dummy = blah";
				res.add(WidgetConfig.generate("dummy", "Dummy widget", 1, 1, properties));
				
				return res;
			}
			
			
			
		});
			
	}
}
