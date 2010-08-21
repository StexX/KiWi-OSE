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
package kiwi.api.informationextraction;

import gate.Document;

import java.util.Collection;
import java.util.Locale;

import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.Example;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.informationextraction.ClassifierEntity;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.user.User;

/**
 * 
 * @author Marek Schmidt
 *
 */
public interface Extractlet {
	
	/**
	 * This method should extract suggestions. 
	 * 
	 * @param context A content item, may be null.
	 * @param content 
	 * @param gateDoc content processed by GATE pipeline for the language of the content.
	 * @param language the langauge of the content.
	 * @return
	 */
	
	Collection<Suggestion> extract(KiWiResource context, TextContent content, Document gateDoc, Locale language);
	
	Collection<Suggestion> extract(Collection<InstanceEntity> instances);
	Collection<InstanceEntity> extractInstances(KiWiResource context, TextContent content, Document gateDoc, Locale language);
	
	void accept(Suggestion suggestion, User user);
	void reject(Suggestion suggestion, User user);
	
	String getName();
	
	
	/**
	 * 
	 * @param classifier
	 * @return
	 */
	void initClassifier(ClassifierEntity classifier);
	
	void trainClassifier(ClassifierEntity classifier);
		
	/**
	 * Updates an existing suggestion (e.g. reevaluate score after updating the models)
	 * @param suggestion
	 */
	void updateSuggestions(ClassifierEntity classifier, Collection<Suggestion> suggestions);
	Collection<Suggestion> classifyInstances(ClassifierEntity classifier,  Collection<InstanceEntity> instance); 
}
