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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import kiwi.model.informationextraction.NaiveBayesClassifier;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;

public class LuceneTraining {
	public static NaiveBayesClassifier learn(IndexReader reader, Map<Integer, Set<Integer>> class2ids, String featureField) throws IOException {
		NaiveBayesClassifier ret = new NaiveBayesClassifier();
		
		int total = 0;
		Map<Integer, Map<String, Integer>> pos = new HashMap<Integer, Map<String, Integer>> ();
		Map<Integer, Map<String, Integer>> neg = new HashMap<Integer, Map<String, Integer>> ();
		
		for (Integer klass : class2ids.keySet()) {
			pos.put(klass, new HashMap<String,Integer> ());
			neg.put(klass, new HashMap<String,Integer> ());
		}
		
		for (Integer klass : class2ids.keySet()) {
			total += class2ids.get(klass).size();
			
			for (Integer docid : class2ids.get(klass)) {
				Document doc = reader.document(docid);
				Field field = doc.getField(featureField);
				TokenStream stream = field.tokenStreamValue();
				
				Token token = new Token();
				while( (token = stream.next(token)) != null) {
					String text = token.termText();
					
					for (Integer klass2 : class2ids.keySet()) {
						if (klass.equals(klass2)) {
							int value = pos.get(klass2).get(text);
							pos.get(klass2).put(text, value + 1);
						}
						else {
							int value = neg.get(klass2).get(text);
							neg.get(klass2).put(text, value + 1);
						}
					}
				}
			}
		}
		
		ret.logpriors = new HashMap<Integer, Double> ();
		ret.loglikelihoods = new HashMap<Integer, Map<String, Double>>();
		ret.features = new HashMap<Integer, List<String>> ();
		for (Integer klass : class2ids.keySet()) {
			
			ret.logpriors.put(klass, Math.log(class2ids.get(klass).size()) - Math.log(total));
			
			Map<String, Double> klassloglikelihoods = new HashMap<String, Double>(); 
			
			for (Map.Entry<String, Integer> pose : pos.get(klass).entrySet()) {
				double posValue = pose.getValue();
				double negValue = neg.get(klass).get(pose.getKey());
				
				double loglikelihood = Math.log(posValue) - Math.log(posValue + negValue);
			
				klassloglikelihoods.put(pose.getKey(), loglikelihood);
			}
			
			ret.loglikelihoods.put(klass, klassloglikelihoods);
			
			List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>> (klassloglikelihoods.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
				@Override
				public int compare(Entry<String, Double> o1,
						Entry<String, Double> o2) {
					return - Double.compare(o1.getValue(), o2.getValue());
				}
			});
			
			List<String> features = new LinkedList<String> ();
			for (Map.Entry<String, Double> entry : list) {
				features.add(entry.getKey());
			}
			
			ret.features.put(klass, features);
		}
		
		return ret;
	}
}
