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

package kiwi.test.base;

import kiwi.api.importexport.ImportService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.user.User;
import kiwi.service.config.ConfigurationServiceImpl;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.mock.SeamTest;
import org.jboss.seam.security.Identity;
import org.testng.annotations.BeforeClass;

/**
 * @author Sebastian Schaffert
 *
 */
public class KiWiTest extends SeamTest {

    private static final Log LOG = Logging.getLog(KiWiTest.class);

	public KiWiTest() {
		super();
		ConfigurationServiceImpl.testing = true;
	}

//	protected void initUser() throws Exception {
//		new FacesRequest() {
//
//            @Override
//            protected void invokeApplication() {
//
//            	Identity.setSecurityEnabled(false);
//
//            	UserService userService = (UserService) Component.getInstance("userService");
//            	IdentityManagerService identityManagerService =
//            		(IdentityManagerService) Component.getInstance("identityManagerService");
//            	identityManagerService.init("");
//            	userService.getAnonymousUser();
//            }
//		}.run();
//	}

	protected void clearDatabase() throws Exception {
	   	new FacesRequest() {

            @Override
            protected void invokeApplication() {

            	Identity.setSecurityEnabled(false);

            	final TripleStore ts = (TripleStore) Component.getInstance("tripleStore");

            	ts.clear();

             }
    	}.run();

	}

	protected void setupDatabase() throws Exception {
    	final String[] ontologies = {
    			"ontology_kiwi.owl",
    			"tagit/tagit.owl",
    			"imports/foaf.owl",
    			"imports/geo.rdf",
    			"imports/sioc.owl",
    			"imports/hgtags.owl",
    			"imports/skos-core.rdf",
    			"thesaurus/thesaurus.rdf",
    	};
    	setupDatabase(ontologies);
    	H2StoredProcedureLoader.loadH2SoredProcedures();
	}


	/**
	 * Setup the database before testing.
	 */
	protected void setupDatabase(final String[] ontologies) throws Exception {
		clearDatabase();

    	new FacesRequest() {

            @Override
            protected void invokeApplication() throws Exception {

            	final Log log = Logging.getLog(this.getClass());
            	Identity.setSecurityEnabled(false);

            	final ImportService im = (ImportService)Component.getInstance("kiwi.core.importService");
            	final User currentUser = (User) Component.getInstance("currentUser");

            	for(final String ont : ontologies) {
            		log.info("loading ontology #0 ", ont);
            		im.importData(Thread.currentThread().getContextClassLoader().getResourceAsStream("ontologies/"+ont), "application/rdf+xml", null, null, currentUser, null);
            		//im.importData(Thread.currentThread().getContextClassLoader().getResourceAsStream("ontologies/"+ont),KiWiDataFormat.RDFXML);
            	}

            }
    	}.run();
	}

	protected void shutdownDatabase() throws Exception {
		clearDatabase();
	}

//	@AfterClass
//	@Override
//	public void cleanupClass() throws Exception
//	{
//		super.cleanupClass();
//		super.stopSeam();
//		Bootstrap bootstrap = Bootstrap.getInstance();
//		bootstrap.shutdown();
//	}
//
//	@BeforeClass
//	@Override
//	public void setupClass() throws Exception
//	{
//		super.startSeam();
//		super.setupClass();
//	}
//
//	@BeforeMethod
//	@Override
//	public void begin()
//	{
//		super.begin();
//	}
//
//	@AfterMethod
//	@Override
//	public void end()
//	{
//		super.end();
//	}
//
//	@Override
//	protected void startJbossEmbeddedIfNecessary() throws Exception {
//		new EmbeddedBootstrap().startAndDeployResources();
//	}

    /**
     * Runs only once when this class is loaded. More preciselly
     * this class loads all stored procedures.
     */
    @BeforeClass
    public void setUp() {
//        H2StoredProcedureLoader.loadH2SoredProcedures();
//        LOG.info("All H2 stored procedures are loaded.");
    }
}
