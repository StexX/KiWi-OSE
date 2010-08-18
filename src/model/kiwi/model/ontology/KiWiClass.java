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
import kiwi.model.annotations.RDFFilter;
import kiwi.model.annotations.RDFInverse;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;

/**
 * Representation of a OWL or RDFS class in the KiWi system as a facade of
 * a ContentItem. Provides convenience methods for working with classes.
 * 
 * @author Sebastian Schaffert
 *
 */
@KiWiFacade
@RDFType({ Constants.NS_OWL + "Class", Constants.NS_RDFS+"Class" })
@RDFFilter({ Constants.NS_OWL + "Class", Constants.NS_RDFS+"Class" })
public interface KiWiClass extends ContentItemI {

	/**
	 * Return the list of superclasses of this class. Queries the rdfs:subClassOf property
	 * of a resource.
	 * 
	 * @return a set of KiWiClass objects which make up the superclasses of this class
	 */
	@RDF(Constants.NS_RDFS+"subClassOf")
	public HashSet<KiWiClass> getSuperClasses();
	
	/**
	 * Set the list of superclasses of this class.
	 * @param classes the new list of superclasses
	 */
	public void setSuperClasses(HashSet<KiWiClass> classes);
	
	
	/**
	 * Return the list of subclasses of this class. Inversely queries the rdfs:subClassOf
	 * property of a resource
	 * 
	 * @return a set of KiWiClass objects which make up the subclasses of this class
	 */
	@RDFInverse(Constants.NS_RDFS+"subClassOf")
	public HashSet<KiWiClass> getSubClasses();
	
	/**
	 * Set the list of subclasses of this class.
	 * @param classes the new list of subclasses
	 */
	public void setSubClasses(HashSet<KiWiClass> classes);
	
	
	/**
	 * Return all classes that are equivalent to this class. Queries the owl:equivalentClass 
	 * property of a resource.
	 */
	@RDF(Constants.NS_OWL + "equivalentClass")
	public HashSet<KiWiClass> getEquivalentClasses();
	
	public void setEquivalentClasses(HashSet<KiWiClass> classes);
	
	/**
	 * Return the properties for which this class is in the range. Inversely queries the rdfs:range
	 * property.
	 */
	@RDFInverse(Constants.NS_RDFS + "range")
	public HashSet<KiWiProperty> getInRange();
	
	public void setInRange(HashSet<KiWiProperty> inrange);
	
	/**
	 * Return the properties for which this class is in the domain. Inversely queries the 
	 * rdfs:domain property.
	 */
	@RDFInverse(Constants.NS_RDFS + "domain")
	public HashSet<KiWiProperty> getInDomain();
	
	public void setInDomain(HashSet<KiWiProperty> indomain);

}
