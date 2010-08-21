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
package kiwi.api.explanation;

import java.util.List;

import kiwi.model.kbase.KiWiTriple;
import kiwi.service.explanation.Story;
import kiwi.service.reasoning.reasonmaintenance.Justification;

/**
 *  Explanation service transforms information produced by the reasoner and reason maintenance into the form of explanations.
 * 
 * @author Jakub Kotowski
 *
 */
public interface ExplanationService {
	public List<Justification> explainTriple(Long id);
	public List<Story> explainTripleTextually(Long id, boolean html);
	
	/** Tries to translate property uris to something more readable.
	 * 
	 * For example for "http://www.w3.org/2000/01/rdf-schema#subClassOf" it returns "is a kind of".
	 * 
	 * @param propertyUri The URI of the property.
	 * @return The natural language translation.
	 */
	public String translateProperty(String propertyUri);	
	
	/** Returns a humand readable description of the kiwi triple.
	 * 
	 * If html is true then enriches the description with additional html.
	 * 
	 * @param triple
	 * @param html
	 * @return
	 */
	public String describeTriple(KiWiTriple triple, boolean html);
}
