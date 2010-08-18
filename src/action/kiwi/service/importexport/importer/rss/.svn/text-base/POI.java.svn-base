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

package kiwi.service.importexport.importer.rss;

import java.util.LinkedList;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFFilter;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;
import kiwi.model.ontology.SKOSConcept;

import org.hibernate.validator.Range;

/**
 * @author Sebastian Schaffert
 *
 */
@KiWiFacade
@RDFType({ Constants.NS_TAGIT+"PointOfInterest", Constants.NS_GEO+"Point" })
@RDFFilter( Constants.NS_GEO+"Point" )
public interface POI extends ContentItemI {

	/**
	 * The longitude of this point of interest. Maps to geo:long of the geo ontology
	 * in the triple store.
	 * 
	 * @return
	 */
	@Range(min=-180, max=180)
	@RDF(Constants.NS_GEO+"long")
	public double getLongitude();

	public void setLongitude(double longitude);

	/**
	 * The latitude of this point of interest. Maps to geo:lat of the geo ontology 
	 * in the triple store.
	 * @return
	 */
	@Range(min=-90, max=90)
	@RDF(Constants.NS_GEO+"lat")
	public double getLatitude();

	public void setLatitude(double latitude);

	/**
	 * The content items that are comments for this point of interest. Maps to sioc:has_reply 
	 * of the SIOC ontology in the triple store.
	 * 
	 * @return
	 */
    @RDF(Constants.NS_SIOC+"has_reply")
	public LinkedList<ContentItem> getComments();

	public void setComments(LinkedList<ContentItem> comments);

	/**
	 * The content items representing multimedia content that is associated with this point of
	 * interest. Maps to sioc:attachment.
	 * @return
	 */
    @RDF(Constants.NS_SIOC+"attachment")
	public LinkedList<ContentItem> getMultimedia();

	public void setMultimedia(LinkedList<ContentItem> multimedia);

	/**
	 * The SKOS Concept used as category of this point of interest.
	 */
	@RDF(Constants.NS_TAGIT+"hasCategory")
	public SKOSConcept getCategory();
	
	public void setCategory(SKOSConcept c);
	
	/**
	 * The SKOS Concept used as subcategory of this point of interest.
	 */
	@RDF(Constants.NS_TAGIT+"hasSubCategory")
	public SKOSConcept getSubCategory();
	
	public void setSubCategory(SKOSConcept c);
	
}