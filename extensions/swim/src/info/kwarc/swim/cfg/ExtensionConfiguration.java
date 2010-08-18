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
package info.kwarc.swim.cfg;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import kiwi.api.config.ConfigurationService;
import kiwi.api.extension.ExtensionService;
import kiwi.api.extension.KiWiApplication;
import kiwi.config.Configuration;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * 
 * @author Christoph Lange
 *
 */
@Name("info.kwarc.swim.cfg")
@Scope(ScopeType.STATELESS)
public class ExtensionConfiguration {

	@Logger
	private Log log;
	
	@In
	private ExtensionService extensionService;

	@In
	private ConfigurationService configurationService;
	
	@Observer("org.jboss.seam.postInitialization") 
	public void initializeExtension() {
		
		log.info("KiWi SWiM Extension initialising ...");
		
		extensionService.registerApplication(new KiWiApplication() {

			@Override
			public String getIdentifier() {
				return "swim";
			}

			@Override
			public String getName() {
				return "SWiM";
			}

			@Override
			public String getDescription() {
				return "The KiWi SWiM Application";
			}

			@Override
			public Set<String> getPermissibleRoles() {
				return null;
			}
		});
		
		initializeSaveletsAndRenderlets();
	}

	/**
	 * Make sure that the savelets and renderlets required by this extension are
	 * executed in the right order.
	 */
	// TODO restructure declaratively SWIM-71
	private void initializeSaveletsAndRenderlets() {
		final String SWIM_CONVERT_RENDERLET = "info.kwarc.swim.service.render.renderlet.SWiMConvertRenderlet";
		
		final String SWIM_CONVERT_SAVELET = "info.kwarc.swim.service.render.savelet.SWiMConvertSavelet";
		final String OMDOC_SAVELET = "info.kwarc.swim.service.render.savelet.OMDocSavelet";
		final List<String> SWIM_SAVELETS = Arrays.asList(new String[] {
				SWIM_CONVERT_SAVELET,
				OMDOC_SAVELET
		}); 
		
		// set up the text savelets
		// TODO rename SWIM-70
		Configuration text_savelets = configurationService.getConfiguration("savelets.text");
		List<String> textSaveletList = text_savelets.getListValue();
		final int swimConvertSaveletIndex = textSaveletList.indexOf(SWIM_CONVERT_SAVELET); 
		final int omdocSaveletIndex = textSaveletList.indexOf(OMDOC_SAVELET); 
		
		if (!(swimConvertSaveletIndex > -1 &&
				omdocSaveletIndex > swimConvertSaveletIndex)) {
			log.info("Setting up text savelets required for SWiM");
			textSaveletList.removeAll(SWIM_SAVELETS);
			textSaveletList.add(SWIM_CONVERT_SAVELET);
			textSaveletList.add(OMDOC_SAVELET);
			configurationService.setConfiguration(text_savelets);
		}
		
		// set up the editor XOM renderlets
		Configuration editor_xom_renderlets = configurationService.getConfiguration("renderlets.editor.xom");
		List<String> editorXomRenderletList = editor_xom_renderlets.getListValue();
		final int swimConvertRenderletIndex = editorXomRenderletList.indexOf(SWIM_CONVERT_RENDERLET);
		
		if (!(swimConvertRenderletIndex > -1)) {
			log.info("Setting up editor renderlets required for SWiM");
			editorXomRenderletList.add(SWIM_CONVERT_RENDERLET);
			configurationService.setConfiguration(editor_xom_renderlets);
		}
		
		// set up the HTML XOM renderlets
		// FIXME replace by proper OMDoc→PMML transformation renderlet
		Configuration html_xom_renderlets = configurationService.getConfiguration("renderlets.html.xom");
		List<String> htmlXomRenderletList = html_xom_renderlets.getListValue();
		final int swimConvertRenderletIndex2 = htmlXomRenderletList.indexOf(SWIM_CONVERT_RENDERLET);
		
		if (!(swimConvertRenderletIndex2 > -1)) {
			log.info("Setting up HTML renderlets required for SWiM");
			htmlXomRenderletList.add(SWIM_CONVERT_RENDERLET);
			configurationService.setConfiguration(html_xom_renderlets);
		}
	}
}
