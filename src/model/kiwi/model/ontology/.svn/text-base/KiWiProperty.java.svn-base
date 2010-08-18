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
 * 
 * 
 */
package kiwi.model.ontology;

import java.util.HashSet;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFInverse;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;

/**
 * Representation of a OWL or RDFS property as a KiWi facade. Provides convenience
 * methods for querying certain property-related aspects of a property.
 * 
 * @author Sebastian Schaffert
 *
 */
@KiWiFacade
@RDFType(Constants.NS_RDF + "Property")
public interface KiWiProperty extends ContentItemI {

	/**
	 * Return the list of superproperties of this property. Queries the rdfs:subPropertyOf
	 * property of this resource.
	 * 
	 * @return the list of superproperties of this resource
	 */
	@RDF(Constants.NS_RDFS + "subPropertyOf")
	public HashSet<KiWiProperty> getSuperProperties();
	
	public void setSuperProperties(HashSet<KiWiProperty> superproperties);
	
	/**
	 * Return the list of subproperties of this property. Inversely queries the rdfs:subPropertyOf
	 * property of this resource.
	 * 
	 * @return the list of subproperties of this resource
	 */
	@RDFInverse(Constants.NS_RDFS + "subPropertyOf")
	public HashSet<KiWiProperty> getSubProperties();
	
	public void setSubProperties(HashSet<KiWiProperty> subproperties);
	
	
	/**
	 * Return the classes that are in the domain of this property. Queries the rdfs:domain
	 * property of this resource.
	 * 
	 * @return the classes that are in the domain of this property
	 */
	@RDF(Constants.NS_RDFS + "domain")
	public HashSet<KiWiClass> getDomain();
	
	public void setDomain(HashSet<KiWiClass> domain);
	
	/**
	 * Return the classes that are in the range of this property. Queries the rdfs:range
	 * property of this resource.
	 * 
	 * @return the classes that are in the range of this property
	 */
	@RDF(Constants.NS_RDFS + "range")
	public HashSet<KiWiClass> getRange();
	
	public void setRange(HashSet<KiWiClass> range);
	
	/**
	 * Return the inverse properties of this property. Queries the owl:inverseOf property
	 * of this resource.
	 * 
	 * TODO: we need to decide whether this is a set or only a single result - both are 
	 *       sensible
	 */
	@RDF(Constants.NS_OWL + "inverseOf")
	public HashSet<KiWiProperty> getInverse();
	
	public void setInverse(HashSet<KiWiProperty> inverse);
	
	
}
