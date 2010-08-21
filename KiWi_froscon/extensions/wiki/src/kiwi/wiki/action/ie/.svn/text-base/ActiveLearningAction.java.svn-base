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

package kiwi.wiki.action.ie;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.informationextraction.InformationExtractionService;
import kiwi.api.informationextraction.InformationExtractionService.Task;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.ClassifierEntity;
import kiwi.model.informationextraction.Example;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.user.User;
import kiwi.util.KiWiXomUtils;

import nu.xom.Text;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

/**
 * 
 * Manages a information extraction suggestions for the current resource. 
 * 
 * Displays a list of suggestions and enabling/disabling suggestions for 
 * the specified resource.
 *  
 * @author Marek Schmidt
 *
 */
@Name("ie.activeLearning")
@Scope(ScopeType.PAGE)
//@Transactional
public class ActiveLearningAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create=true)
	ContentItem currentContentItem;
	
	@In
 	private EntityManager entityManager;
	
	@In
	private TripleStore tripleStore;
	
	@In(create=true)
	private User currentUser;
	
	@In(value="kiwi.informationextraction.informationExtractionService")
	private InformationExtractionService informationExtractionService;
	
	@Logger
	private Log log;
	
	private Collection<SuggestionUI> suggestions;
	private Collection<ExampleUI> positiveExamples;
	private Collection<ExampleUI> negativeExamples;

	public static class SuggestionUI {
		
		public static final int NONE = 0;
		public static final int ACCEPTED  = 1;
		public static final int REJECTED = 2;
		
		private Suggestion suggestion;
		private String snippet;
		private int state = 0;
		public void setSuggestion(Suggestion suggestion) {
			this.suggestion = suggestion;
		}
		public Suggestion getSuggestion() {
			return suggestion;
		}
		public void setSnippet(String snippet) {
			this.snippet = snippet;
		}
		public String getSnippet() {
			return snippet;
		}
		public InstanceEntity getInstance() {
			return suggestion.getInstance();
		}
		
		public float getScore() {
			return suggestion.getScore();
		}
		public void setState(int state) {
			this.state = state;
		}
		public int getState() {
			return state;
		}
	}
	
	public static class ExampleUI {
		private Example example;
		private String snippet;
		public void setExample(Example example) {
			this.example = example;
		}
		public Example getExample() {
			return example;
		}
		public void setSnippet(String snippet) {
			this.snippet = snippet;
		}
		public String getSnippet() {
			return snippet;
		}		
		public Suggestion getSuggestion() {
			return example.getSuggestion();
		}
	}
	
	
	/**
	 * Produce a simple text snippet of a text content. 
	 */
	// TODO: move this to the rendering service...
	private String snippet(TextContent content) {
		final int chars = 120;
		StringBuilder snippet = new StringBuilder();

		KiWiXomUtils.NodePosIterator iter = new KiWiXomUtils.NodePosIterator (content.getXmlDocument());
	nodes:	
		while (iter.hasNext()) {
			KiWiXomUtils.NodePos np = iter.next();
			String prefix = np.getPrefix().replace('\n', ' ');
			
			if (snippet.length() + prefix.length() > chars) {
				break;
			}
			
			snippet.append(prefix);
			
			if (np.getNode() instanceof Text) {
				String text = ((Text)np.getNode()).getValue();
				if (text.length() + snippet.length() > chars) {
					String[] split = text.split(" ");
					for (String word : split) {
						if (snippet.length() + word.length() > chars) {
							snippet.append("...");
							break nodes;
						}
						else {
							snippet.append(word);
							snippet.append(' ');
						}
					}
				}
				else {
					snippet.append(text);
				}
			}
		}
		
		return snippet.toString();
	}
	
	
	public Collection<SuggestionUI> getSuggestions () {
		if (suggestions == null) {
			suggestions = wrapSuggestionUi(informationExtractionService.getSuggestionsByClassifierResource(currentContentItem.getResource()));
		}
		return suggestions;
	}
	
	/**
	 * map(lambda x:SuggestionUI(x, snippet(x)), suggestions)
	 */
	private Collection<SuggestionUI> wrapSuggestionUi (Collection<Suggestion> suggestions) {
		Collection<SuggestionUI> uis = new LinkedList<SuggestionUI> ();
		
		for (Suggestion suggestion : suggestions) {
			SuggestionUI ui = new SuggestionUI();
			ui.setSuggestion(suggestion);
			
			// set a snippet only for non-fragment suggestions. Those have context, no need for a snippet.
			if (!suggestion.getInstance().getContext().getIsFragment()) {
				if (suggestion.getInstance().getSourceTextContent() != null) {
					ui.setSnippet(snippet(suggestion.getInstance().getSourceTextContent()));		
				}
			}
			
			uis.add(ui);
		}
		
		return uis;
	}
	
	/**
	 * map(lambda x:ExampleUI(x, snippet(x)), suggestions)
	 */
	private Collection<ExampleUI> wrapExampleUi (Collection<Example> examples) {
		Collection<ExampleUI> uis = new LinkedList<ExampleUI> ();
		
		for (Example example : examples) {
			ExampleUI ui = new ExampleUI ();
			ui.setExample(example);
			
			// set a snippet only for non-fragment suggestions. Those have context, no need for a snippet.
			if (!example.getSuggestion().getInstance().getContext().getIsFragment()) {
				if (example.getSuggestion().getInstance().getSourceTextContent() != null) {
					ui.setSnippet(snippet(example.getSuggestion().getInstance().getSourceTextContent()));
					
				}
			}
			
			uis.add(ui);
		}
		
		return uis;
	}
	
	public Collection<ExampleUI> getPositiveExamples () {
		if (positiveExamples == null) {
			positiveExamples = wrapExampleUi(informationExtractionService.getPositiveExamplesByClassifierResource(currentContentItem.getResource()));
		}
		return positiveExamples;
	}
	
	public Collection<ExampleUI> getNegativeExamples () {
		if (negativeExamples == null) {
			negativeExamples = wrapExampleUi(informationExtractionService.getNegativeExamplesByClassifierResource(currentContentItem.getResource()));
		}
		return negativeExamples;
	}
	
	public void init() {
		informationExtractionService.initExamplesForClassifierResource(currentContentItem.getResource());
		refresh();
	}
	
	public void compute() throws InterruptedException {
		
		for (SuggestionUI ui : suggestions) {
			if (ui.getState() == SuggestionUI.ACCEPTED) {
				informationExtractionService.acceptSuggestion(ui.getSuggestion(), currentUser);
				informationExtractionService.realizeSuggestion(ui.getSuggestion(), currentUser);
			}
			else if (ui.getState() == SuggestionUI.REJECTED) {
				informationExtractionService.rejectSuggestion(ui.getSuggestion(), currentUser);
			}
		}
		
		Task task = informationExtractionService.trainAndSuggestForClassifierResource(currentContentItem.getResource());
	
		task.waitForCompletition();
		
		refresh();
	}
	
	public void acceptAll() throws InterruptedException {
		for (SuggestionUI ui : suggestions) {
			if (ui.getState() == SuggestionUI.NONE) {
				ui.setState(SuggestionUI.ACCEPTED);
			}
		}
		
		compute();
	}
	
	public void rejectAll() throws InterruptedException {
		for (SuggestionUI ui : suggestions) {
			if (ui.getState() == SuggestionUI.NONE) {
				ui.setState(SuggestionUI.REJECTED);
			}
		}
		
		compute();
	}
	
	public void acceptSuggestion(SuggestionUI s) {
		
		s.setState(SuggestionUI.ACCEPTED);
		
/*		informationExtractionService.acceptSuggestion(s.getSuggestion(), currentUser);
		informationExtractionService.realizeSuggestion(s.getSuggestion(), currentUser);
		
		refresh();*/
	}
	
	public void rejectSuggestion(SuggestionUI s) {
		
		s.setState(SuggestionUI.REJECTED);
		
		/*informationExtractionService.rejectSuggestion(s.getSuggestion(), currentUser);
		
		refresh();*/
	}
	
	public void enable() throws InterruptedException {
		
		// TODO: only admins should be allowed to do that
		
		log.info("Enabling informatino extraction for tag #0", currentContentItem);
		
		if (currentContentItem == null) return;
		
		Task task = informationExtractionService.createAndInitClassifier(currentContentItem.getResource());
		
		task.waitForCompletition();
		
		refresh();
	}
	
	public void disable() {
		
		// TODO: only admins should be allowed to do that
		if (currentContentItem == null) return;
		
		for (ClassifierEntity classifier : informationExtractionService.getClassifiersForResource(currentContentItem.getResource())) {
			informationExtractionService.deleteClassifier(classifier);
		}
		
		refresh();
	}
	
	/**
	 * Are suggestions enabled for this resource? (= is there a classifier 
	 * for this resource?)
	 * @return
	 */
	public boolean isEnabled() {
		if (currentContentItem == null) return false;
		return !informationExtractionService.getClassifiersForResource(currentContentItem.getResource()).isEmpty();
	}
	
	public void refresh() {
		positiveExamples = null;
		negativeExamples = null;
		suggestions = null;
	}
	
	public void removeExample(ExampleUI example) {
		
		informationExtractionService.unrealizeSuggestion(example.getSuggestion(), currentUser);
		informationExtractionService.removeExample(example.example);
		
		// TODO: instead of writing this todo, I could write a simple conditional statement and call only the relevant remove... 
		positiveExamples.remove(example);
		negativeExamples.remove(example);
		
		//refresh();
	}
}
