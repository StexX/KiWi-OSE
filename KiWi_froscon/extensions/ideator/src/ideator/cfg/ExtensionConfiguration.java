/*
 * File : ExtensionConfiguration.java
 * Date : Oct 8, 2009
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
 */package ideator.cfg;

import java.util.Set;

import kiwi.api.event.KiWiEvents;
import kiwi.api.extension.ExtensionService;
import kiwi.api.extension.KiWiApplication;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author mradules
 *
 */
@Name("ideator.cfg")
@Scope(ScopeType.STATELESS)
public class ExtensionConfiguration {
    @Logger
    private Log log;

    @In
    private ExtensionService extensionService;

    @Observer(KiWiEvents.EXTENSIONSERVICE_INIT)
    public void initializeExtension() {

        log.info("KiWi Ideator Extension initialising ...");

        extensionService.registerApplication(new KiWiApplication() {

            @Override
            public String getIdentifier() {
                return "ideator";
            }

            @Override
            public String getName() {
                return "Ideator";
            }

            @Override
            public String getDescription() {
                return "The KiWi Ideator integration.";
            }

			@Override
			public Set<String> getPermissibleRoles() {
				return null;
			}
			
			@Override
			public String getTheme() {
				return "ideator";
			}
			
        });

    }
}
