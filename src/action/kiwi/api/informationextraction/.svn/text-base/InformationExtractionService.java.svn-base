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

import java.util.Collection;
import java.util.Locale;

import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.ClassifierEntity;
import kiwi.model.informationextraction.Example;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

public interface InformationExtractionService {
	
	public static interface Task {
		public void waitForCompletition() throws InterruptedException;
	}
	
	void init();
	void remove();
	
	Collection<Suggestion> extractTags(ContentItem ci);
	Collection<Suggestion> extractTags(KiWiResource context, TextContent content, Locale language);
	
	Collection<Suggestion> extractEntities(ContentItem ci);
	Collection<Suggestion> extractEntities(KiWiResource context, TextContent content, Locale language);
	
	Collection<String> getExtractletNames();
	
	Task initExamples(ClassifierEntity classifier);
	void initExamplesForClassifierResource(KiWiResource resource);
	
	/**
	 * Called after a tagging event, should create example if the tagging
	 * resource is mapped to a classifier.
	 * @param tagging
	 */
	void createPositiveExampleForTagging(Tag tagging);
	void createNegativeExampleForTagging(Tag tagging);
	
	
	void initInstances ();
	
	ClassifierEntity createClassifier(KiWiResource resource, String extractletName);
	
	/**
	 * Creates a classifier based on the type of the resource and the system configuration
	 * @param resource
	 * @return
	 */
	ClassifierEntity createClassifier(KiWiResource resource);
	void deleteClassifier(ClassifierEntity classifier);
	
	void trainClassifier(ClassifierEntity classifier);
	void computeSuggestions(ClassifierEntity classifier);
	
	Task createAndInitClassifier(KiWiResource resource, String extractletName);	
	Task createAndInitClassifier(KiWiResource resource);	
	
	/**
	 * All classifiers that have something to do with the resource (e.g. it is either tag or type or something...)
	 * @param resource
	 * @return
	 */
	Collection<ClassifierEntity> getClassifiersForResource(KiWiResource resource);
	Collection<ClassifierEntity> getClassifiers();
	
	void trainAndSuggest(ClassifierEntity classifier);
	void trainAndSuggestForItem(ContentItem currentContentItem);
	Task trainAndSuggestForClassifierResource(KiWiResource resource);
	
	void acceptSuggestion(kiwi.model.informationextraction.Suggestion s, User user);
	void rejectSuggestion(kiwi.model.informationextraction.Suggestion s, User user);
	
	void realizeSuggestion(kiwi.model.informationextraction.Suggestion s, User user);
	void unrealizeSuggestion(Suggestion s, User user);
	
	Collection<Example> getPositiveExamples(ClassifierEntity classifier);
	Collection<Example> getNegativeExamples(ClassifierEntity classifier);
	
	Collection<Example> getPositiveExamplesByClassifierResource(KiWiResource resource);
	Collection<Example> getNegativeExamplesByClassifierResource(KiWiResource resource);
	
	void removeExample(Example example);
	
	Collection<kiwi.model.informationextraction.Suggestion> getSuggestionsByClassifier(ClassifierEntity classifier);
	Collection<kiwi.model.informationextraction.Suggestion> getSuggestionsByContentItem(ContentItem item);
	Collection<kiwi.model.informationextraction.Suggestion> getSuggestionsByTextContent(TextContent textcontent);
	Collection<kiwi.model.informationextraction.Suggestion> getSuggestionsByClassifierResource(KiWiResource resource);
	
	//public void initAndClassifyInstances(ContentItem item);
	public void extractInformation(ContentItem item);
	public void extractInformationAsync(ContentItem item);
	
	public void runTasks(Long interval) throws Exception;
	
	void setEnabled(boolean enabled);
	boolean getEnabled();
	
	/**
	 * Should IE automatically run when content changes?
	 */
	void setOnline(boolean online);
	boolean getOnline();
	
}
