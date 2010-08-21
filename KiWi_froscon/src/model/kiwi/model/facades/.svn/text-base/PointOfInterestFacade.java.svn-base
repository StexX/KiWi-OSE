/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2008-2009, The KiWi Project (http://www.kiwi-project.eu)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of the KiWi Project nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contributor(s):
 * sschaffe
 * 
 */
package kiwi.model.facades;

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
 * The PointOfIntereestFacade extends and ordinary 
 * ContentItem with geographical information
 * 
 * @author Sebastian Schaffert
 *
 */
@KiWiFacade
@RDFType({ Constants.NS_TAGIT+"PointOfInterest", Constants.NS_GEO+"Point" })
@RDFFilter( Constants.NS_GEO+"Point" )
public interface PointOfInterestFacade extends ContentItemI {

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
	 * The textual address of this point of interest. Uses the tagit:address property
	 * in the triple storr
	 * @return
	 */
	@RDF(Constants.NS_TAGIT+"address")
	public String getAddress();

	public void setAddress(String address);

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
