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

package kiwi.service.informationextraction.classification;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kiwi.model.informationextraction.NaiveBayesClassifier;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;

public class LuceneClassification {
	
	private IndexReader reader;
	private NaiveBayesClassifier classifier;
	private String featureField;
	private Map<String, Double> loglikelihoods;
	private List<String> features;
	private double logprior;
	
	public LuceneClassification(IndexReader reader, NaiveBayesClassifier classifier, String featureField, int klass) {
		 this.reader = reader;
		 this.classifier = classifier;
		 this.featureField = featureField;
		 
		 loglikelihoods = classifier.loglikelihoods.get(klass);
		 logprior = classifier.logpriors.get(klass);
		 features = classifier.features.get(klass);
	}
	
	public boolean hasNext(Iteration prev) {
		return prev == null ? classifier.features.size() > 0 : classifier.features.size() > prev.iteration + 1;
	}
		
	public Iteration step(Iteration prev) throws IOException {
		
		if (!hasNext(prev)) return prev;
		
		int currentTermIndex = prev == null ? 0 : prev.iteration + 1;
		String currentTerm = features.get(currentTermIndex);
		
		Map<Integer, Double> prevId2logscore = prev == null ? new HashMap<Integer, Double>() : prev.id2logscore;
		Map<Integer, Double> id2logscore = new HashMap<Integer, Double>();
		
		Term term = new Term(this.featureField, features.get(currentTermIndex));
		
		TermDocs tds = reader.termDocs(term);
		while(tds.next()) {
			int docid = tds.doc();
			
			Double prevValueObj = prevId2logscore.get(docid);
			double prevValue;
			if (prevValueObj == null) {
				prevValue = logprior;
			}
			else {
				prevValue = prevValueObj;
			}

			double logLikelihood = loglikelihoods.get(currentTerm);
			id2logscore.put(docid, prevValue + logLikelihood);
		}
		
		Iteration next = new Iteration();
		next.iteration = prev.iteration + 1;
		next.id2logscore = id2logscore;
		
		return next;
	}
}
