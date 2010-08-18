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
package kiwi.action.webservice.thesaurusManagement;

import java.util.Locale;

import kiwi.model.kbase.KiWiResource;
import kiwi.model.ontology.SKOSConcept;

/**
 * @author Rolf Sint
 *
 */
public class SKOSConceptUtils {
	//returns the specified label in the given language, if the label in this language 
	//does not exist, it returns the label in the default language (de,en, no spec. label)
	public static String getConceptLabelInLanguage(SKOSConcept sk, Locale l){
		KiWiResource kr = sk.getResource();
		Locale [] locs = new Locale[3];
		locs[0] = l;
		locs[1] = new Locale("de");
		locs[2] = new Locale("en");
		
		String name = "";
		for (Locale loc: locs){
			name = kr.getProperty("http://www.w3.org/2004/02/skos/core#prefLabel", loc );
			if (name != null){
				return name;
			}
		}
		
		
		
		//if still empty then take the default label
		if (name == null){
				name = sk.getPreferredLabel();
		}
		//if still empty then take the title/marked with a . for debugging purposes
		if(name == null || name.equals(""))
		    name = sk.getTitle()+".";
				
		return name; 
	}
	
	// takes 2 languages and returns the conceptname which consists of two labesl lile auto (car)
	//used in the visualizer modul. g
	public static String getConceptLabelsInLanguages(SKOSConcept sk, Locale lang1, Locale lang2){
			KiWiResource kr = sk.getResource();
			
			String name = kr.getProperty("http://www.w3.org/2004/02/skos/core#prefLabel", lang1 );
			String name1 = kr.getProperty("http://www.w3.org/2004/02/skos/core#prefLabel", lang2 );
			
			//if both exist, return both
			if (name != null && name1 != null){
				return name + " ("+ name1 + ")";
			}
			else if (name != null && name1 == null){
				return name;
			}
			else if (name == null && name1 != null){
				return name1;
			}
			
				
		return sk.getPreferredLabel();
			
	}
}
