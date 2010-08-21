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

package kiwi.api.ontology;

import java.util.List;

import kiwi.model.kbase.KiWiResource;
import kiwi.model.ontology.KiWiClass;
import kiwi.model.ontology.KiWiProperty;

/**
 * The OntologyService provides convenience operations for working with OWL and RDFS ontologies 
 * in the KiWi system, like listing classes, listing properties, etc.
 * 
 * @author Sebastian Schaffert
 *
 */
public interface OntologyService {

	/**
	 * List all classes that are defined in the KiWi system.
	 * 
	 * @return a list of KiWiClass objects representing the classes in the KiWi system
	 */
	public List<KiWiClass> listClasses();
	
	/**
	 * List all properties that are defined in the KiWi system.
	 * 
	 * @return a list of KiWiProperty objects representing the properties in the KiWi system
	 */
	public List<KiWiProperty> listProperties();
	
	/**
	 * List the properties that currently exist between a given subject and object. 
	 * @param subject the subject of the relation
	 * @param object the object of the relation
	 * @return the list of properties that are used in relations between subject and object
	 */
	public List<KiWiProperty> listExistingProperties(KiWiResource subject, KiWiResource object);
	
	
	/**
	 * List all datatype properties that are defined in the KiWi system.
	 * 
	 * @return a list of KiWiProperty objects representing the properties in the KiWi system
	 */
	public List<KiWiProperty> listDatatypeProperties();
	
	/**
	 * List the properties that are applicable between a given subject and object. Whether a
	 * property is applicable is decided based on the domain and range of the property: if one of 
	 * the types of the subject is in the range, and one of the types of the object is in the 
	 * domain, the property is applicable.
	 * 
	 * @param subject the subject to check
	 * @param object the object to check
	 * @return a list of applicable properties
	 */
	public List<KiWiProperty> listApplicableProperties(KiWiResource subject, KiWiResource object);

	/**
	 * List the datatype properties that are applicable for a given subject. Whether a property is
	 * applicable is decided based on the range of the property: if one of the types of the subject
	 * is in the range, the property is applicable. 
	 * <p>
	 * This method currently ignores the domain of the property. In the future, OntologyService
	 * could provide a method that takes into account the actual datatype.
	 * 
	 * @param subject the subject for which the properties are supposed to be listed
	 * @return a list of KiWiProperty facades that are applicable
	 */
	public List<KiWiProperty> listApplicableDataTypeProperties(KiWiResource subject);
	
	
	/**
	 * Provide autocompletion of content items that are of type "owl:Class" or "rdfs:Class" and 
	 * whose title starts with "prefix". Autocompletion is delegated to the SOLR search service.
	 * @param prefix
	 * @return
	 */
	public List<String> autocomplete(String prefix);
	
	/**
	 * List all datatype properties that are defined in the KiWi system and match title 'name'.
	 * 
	 * @return a list of KiWiProperty objects representing the properties in the KiWi system
	 */
	public List<KiWiProperty> listDatatypePropertiesByName( String name );

}
