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

package kiwi.test.service.render.savelet;

import static org.junit.Assert.*;

import kiwi.service.render.savelet.ExtractLinksSavelet;

import org.testng.annotations.Test;

/**
 * @author sschaffe
 *
 */
@Test
public class ExtractLinksSaveletTest {

	/**
	 * Test method for {@link kiwi.service.render.savelet.ExtractLinksSavelet#apply(java.lang.String)}.
	 */
	@Test
	public void testApply() {
		ExtractLinksSavelet savelet = new ExtractLinksSavelet();
		
		String content01 = "[[Test]]";
		String result = savelet.apply(null,content01);
		
		assertEquals("<a kiwi:kind=\"intlink\" kiwi:target=\"Test\" href=\"\">Test</a> ",result);
		
		String content02 = "[[Test | Test Label]]";
		result = savelet.apply(null,content02);
		
		assertEquals("<a kiwi:kind=\"intlink\" kiwi:target=\"Test\" href=\"\">Test Label</a> ",result);
		
		String content03 = "[[rdf:type :: Test]]";
		result = savelet.apply(null,content03);
		
		assertEquals("<a kiwi:kind=\"intlink\" kiwi:target=\"Test\" kiwi:rel=\"rdf:type\" href=\"\">Test</a> ",result);
		
		String content04 = "[[rdf:type :: Test | Test Label]]";
		result = savelet.apply(null,content04);
		
		assertEquals("<a kiwi:kind=\"intlink\" kiwi:target=\"Test\" kiwi:rel=\"rdf:type\" href=\"\">Test Label</a> ",result);
	}

}
