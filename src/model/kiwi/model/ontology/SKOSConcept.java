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
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;

/**
 * Representation of a SKOS concept as a KiWi facade. Provides convenience methods for querying
 * the most relevant SKOS properties of concepts.
 * 
 * @author Sebastian Schaffert
 * @author Arpad Borsos
 *
 * Unimplemented SKOS properties:
 * skos:broadMatch
 * skos:broaderTransitive
 * skos:changeNote
 * skos:closeMatch
 * skos:editorialNote
 * skos:exactMatch
 * skos:example
 * skos:historyNote
 * skos:inScheme
 * skos:mappingRelation
 * skos:narrowMatch
 * skos:narrowerTransitive
 * skos:notation
 * skos:note
 * skos:relatedMatch
 * skos:scopeNote
 * skos:semanticRelation
 */
@KiWiFacade
@RDFType(Constants.NS_SKOS + "Concept")
public interface SKOSConcept extends ContentItemI {
	/**
	 * Retrieve SKOS concepts that are broader than this concept. Maps to the skos:broader
	 * property of SKOS core.
	 */
	// FIXME: this is a simplification of the SKOS standard. It should really support multiple
	// broaders as per SKOS standard
	@RDF(Constants.NS_SKOS + "broader")
	//FIXME: @RDFInverse(Constants.NS_SKOS + "narrower")
	public SKOSConcept getBroader();
	
	public void setBroader(SKOSConcept broader);
	
	
	/**
	 * Retrieve SKOS concepts that are narrower than this concept. Maps to the skos:narrower
	 * property of SKOS core.
	 */
	@RDF(Constants.NS_SKOS + "narrower")
	//FIXME: @RDFInverse(Constants.NS_SKOS + "broader")
	public HashSet<SKOSConcept> getNarrower();
	
	public void setNarrower(HashSet<SKOSConcept> narrower);
	
	
	/**
	 * Retrieve SKOS concepts that are related to this concept. Maps to the skos:related
	 * property of SKOS core.
	 */
	@RDF(Constants.NS_SKOS + "related")
	//FIXME: @RDFInverse(Constants.NS_SKOS + "related") // This is a symmetric property
	public HashSet<SKOSConcept> getRelated();
	
	public void setRelated(HashSet<SKOSConcept> related);
	

	@RDF(Constants.NS_SKOS + "topConceptOf")
	// FIXME: @RDFInverse(Constants.NS_SKOS + "hasTopConcept")
	public SKOSConceptScheme getScheme();
	public void setScheme(SKOSConceptScheme concepts);

	
	@RDF(Constants.NS_SKOS + "definition")
	public String getDefinition();
	
	public void setDefinition(String definition);
	
	/**
	 * Get/set the preferred label of this concept. Maps to the skos:preferredLabel property
	 * of SKOS core.
	 */
	@RDF(Constants.NS_SKOS + "prefLabel")
	public String getPreferredLabel();
	
	public void setPreferredLabel(String s);
	
	/**
	 * Get/set the set of alternative labels of this concept. Maps to the skos:alternativeLabel
	 * property of SKOS core.
	 */
	@RDF(Constants.NS_SKOS + "altLabel")
	public HashSet<String> getAlternativeLabels();
	
	public void setAlternativeLabels(HashSet<String> alternativeLabels);
	
	@RDF(Constants.NS_SKOS + "hiddenLabel")
	public HashSet<String> getHiddenLabels();
	
	public void setHiddenLabels(HashSet<String> hiddenLabels);
	
	//temporary solution for the interedu project, workshop
	@RDF(Constants.NS_SKOS + "interEduArticles")
	public HashSet<ContentItem> getArticles();
	
	public void setArticles(HashSet<ContentItem> articles);	
}
