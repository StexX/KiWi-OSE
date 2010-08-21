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

package kiwi.service.informationextraction;

import gate.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;

import org.jboss.seam.Component;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.NaiveBayes;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.types.Alphabet;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Label;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.Labeling;
import cc.mallet.types.Multinomial;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;

import kiwi.api.informationextraction.FeatureStoreService;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.ClassifierEntity;
import kiwi.model.informationextraction.Example;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.NaiveBayesClassifier;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiResource;

/**
 * An abstract class implementing common functionality for Machine Learning Extractlets
 * 
 * @author Marek Schmidt
 *
 */
public abstract class AbstractMLExtractlet extends AbstractExtractlet {
		
	public AbstractMLExtractlet(String name) {
		super(name);
	}
	
	protected Suggestion generateSuggestion(InstanceEntity entity, ClassifierEntity classifier) {
		Suggestion ret = new Suggestion();
		
		ret.setClassifier(classifier);
		ret.setExtractletName(this.name);
		ret.setInstance(entity);
		
		return ret;
	}
	
	@Override
	public void initClassifier(ClassifierEntity classifier) {
	}
	
	@Override
	public Collection<Suggestion> extract(Collection<InstanceEntity> instances) {
		Collection<Suggestion> ret = new LinkedList<Suggestion> ();
						
		Query q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listClassifiersByExtractletName");
		q.setParameter("name", this.name);
				
		Collection<ClassifierEntity> classifiers = (Collection<ClassifierEntity>) q.getResultList();
		
		for (ClassifierEntity classifier : classifiers) {
			Collection<Suggestion> suggestions = classifyInstances(classifier, instances);
			ret.addAll(suggestions);
		}
		
		/*for (InstanceEntity instance : instances) {
			for (ClassifierEntity classifier : classifiers) {
				
				 Suggestion suggestion = classifyInstance(classifier, instance);
				 if (suggestion != null) {
					 ret.add(suggestion);
				 }
			}
		}*/
		
		return ret;
	}
	
	protected Collection<String> getInstanceFeatures(InstanceEntity instance) {
		Collection<String> features = instance.getFeatures();
		
		if (features == null) {
			FeatureStoreService featureStore = (FeatureStoreService)Component.getInstance("kiwi.informationextraction.featureStoreService");
			features = featureStore.get(instance.getExtractletName(), instance.getId());
			instance.setFeatures(features);
		}
		
		return features;
	}
	
	@Override
	public abstract Collection<InstanceEntity> extractInstances(KiWiResource context, TextContent content, Document gateDoc, Locale language);
	
	@Override
	public void updateSuggestions(ClassifierEntity classifier, Collection<Suggestion> suggestions) {
		
		Logger log = Logger.getLogger("AbstractMLExtractlet");
		log.info("updateSuggestions");
		
		// EntityManager entityManager = (EntityManager)Component.getInstance("entityManager");
		entityManager.setFlushMode(FlushModeType.COMMIT);

		if (classifier.getMalletClassifier() == null) {
			log.info("mallet classifier == null!");
			return;
		}
		
		NaiveBayesClassifier nbc = classifier.getNaiveBayesClassifier();
		Alphabet alphabet = classifier.getMalletAlphabet();
		LabelAlphabet labelAlphabet = classifier.getMalletLabelAlphabet();
		Classifier malletClassifier = classifier.getMalletClassifier();
		Pipe instancePipe = malletClassifier.getInstancePipe();
				
		assert instancePipe != null;
		assert alphabet != null;
		assert labelAlphabet != null;
		assert malletClassifier != null;
		/*
		ArrayList<Pipe> featurePipeList = new ArrayList<Pipe>();
		featurePipeList.add(new TokenSequence2FeatureSequence(alphabet));
		featurePipeList.add(new FeatureSequence2FeatureVector());
		 
		Pipe featurePipe = new SerialPipes(featurePipeList);
		featurePipe.setTargetProcessing(false);*/
		
		Label posLabel = labelAlphabet.lookupLabel("+");
		
		for (Suggestion suggestion : suggestions) {
					
			InstanceEntity instance = suggestion.getInstance();
			
			Collection<String> features = getInstanceFeatures(instance);
			
			TokenSequence tokens = new TokenSequence();
			for (String feature : features) {
				assert feature != null;
				tokens.add(new Token(feature));
			}
			
			//Instance malletInstance = new Instance(tokens, null, null, null);
			//malletInstance = featurePipe.instanceFrom(malletInstance);
			//malletInstance = instancePipe.instanceFrom(malletInstance);
			
			float nbcScore = (float)nbc.classify(features, 0);
			Classification malletClassification = malletClassifier.classify(tokens);
			Labeling malletLabeling = malletClassification.getLabeling();
			float malletScore = (float)malletLabeling.value(posLabel);
		
			log.info("updateSuggestion: " + name + " " + suggestion.getInstance().getSourceResource().getContentItem().getTitle() + " " + malletScore + " " + nbcScore);
		
			suggestion.setScore(malletScore);
		}
		
		log.info("updateSuggestions close");
//		entityManager.flush();
	}
	
	@Override
	public Collection<Suggestion> classifyInstances(ClassifierEntity classifier, Collection<InstanceEntity> instances) {
		
		Collection<Suggestion> ret = new LinkedList<Suggestion> ();
		for (InstanceEntity instance : instances) {
			Suggestion suggestion = generateSuggestion (instance, classifier);
			if (suggestion != null) {
				ret.add(suggestion);
			}
		}
		
		updateSuggestions(classifier, ret);
		
		return ret;
		
		// printClassifier(classifier);
		
		// class 0 is always the "true" one.
		/*float score = (float)classifier.getNaiveBayesClassifier().classify(getInstanceFeatures(instance), 0);
		
		Suggestion suggestion = generateSuggestion (instance, classifier);
		suggestion.setScore(score);
		
		return suggestion;*/
	}
	
	private void printClassifier(ClassifierEntity classifier) {
		Logger log = Logger.getLogger("AbstractMLExtractlet");
		log.info("classifier:");
		log.info("  naiveBayesClassifier:");
		if (classifier.getNaiveBayesClassifier() != null) {
			
			
			for (Integer klass : classifier.getNaiveBayesClassifier().features.keySet()) {
				log.info("    klass " + klass);
				log.info("      logprior " + classifier.getNaiveBayesClassifier().logpriors.get(klass));
				log.info("      features:");
				for (String feature : classifier.getNaiveBayesClassifier().features.get(klass)) {
					log.info("        " + feature);
				}
				log.info("      loglikelihoods:");
				for (Map.Entry<String, Double> entry : classifier.getNaiveBayesClassifier().loglikelihoods.get(klass).entrySet()) {
					log.info("        " + entry.getKey() + " " + entry.getValue());
				}
			}			
		}
		else {
			log.info("    null");
		}
	}
	
	@Override
	public void trainClassifier(ClassifierEntity classifier) {
		Query q;
		
		Logger log = Logger.getLogger("AbstractMLExtractlet");
		log.info("trainClassifier");
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		Alphabet instanceAlphabet = new Alphabet();
		
		LabelAlphabet labelAlphabet = new LabelAlphabet();
		labelAlphabet.lookupLabel("+", true);
		labelAlphabet.lookupLabel("-", true);
					
		//ArrayList<Pipe> labelPipeList = new ArrayList<Pipe>();
		//labelPipeList.add(new Target2Label(labelAlphabet));
		
		//Pipe labelPipe = new SerialPipes(labelPipeList);
		
		ArrayList<Pipe> featurePipeList = new ArrayList<Pipe>();
		//Pattern tokenPattern =
        //    Pattern.compile("\\S+");
		// featurePipeList.add(new CharSequence2TokenSequence(tokenPattern));
		featurePipeList.add(new TokenSequence2FeatureSequence(instanceAlphabet));
		featurePipeList.add(new FeatureSequence2FeatureVector());
		featurePipeList.add(new Target2Label(labelAlphabet));
		
		
		Pipe featurePipe = new SerialPipes(featurePipeList);
		featurePipe.setTargetProcessing(false);
			
		InstanceList ilist = new InstanceList(featurePipe);
		
		q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listExamplesByClassifier");
		q.setParameter("classifier", classifier);
		
		FeatureStoreService featureStore = (FeatureStoreService)Component.getInstance("kiwi.informationextraction.featureStoreService");
		
		Collection<Example> examples = q.getResultList();
		for (Example x : examples) {
			
			

			// Only examples of same instance type as the classifier can be considered... 
			//if (x.getInstance().getInstanceType().equals(classifier.getInstanceType())) {
			
			InstanceEntity instanceEntity = x.getSuggestion().getInstance();
			Collection<String> features = featureStore.get(instanceEntity.getExtractletName(), instanceEntity.getId());
			
			
			
			TokenSequence tokens = new TokenSequence();
			for (String feature : features) {
				tokens.add(new Token(feature));
			}
			
			Instance malletInstance = new Instance(tokens, null, null, null);
			//malletInstance = featurePipe.instanceFrom(malletInstance);
			
			if (x.getType() == Example.POSITIVE) {
				malletInstance.setTarget("+");
				
				log.info("example + " + features.toString());
			}
			else if (x.getType() == Example.NEGATIVE) {
				malletInstance.setTarget("-");
				
				log.info("example - " + features.toString());
			}
			else {
				continue;
			}
				
			ilist.addThruPipe(malletInstance);
		}
			
		ClassifierTrainer<NaiveBayes> trainer = new NaiveBayesTrainer();
		NaiveBayes malletClassifier = trainer.train(ilist);

		classifier.setMalletClassifier(malletClassifier);
		classifier.setMalletAlphabet(instanceAlphabet);
		classifier.setMalletLabelAlphabet(labelAlphabet);
		
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();
				
		for (int i = 0; i <= 1; ++i) {
			List<String> features = new LinkedList<String>();
			Map<String, Double> loglikelihoods = new HashMap<String, Double> ();
			double logprior = malletClassifier.getPriors().logProbability(i);
			
			Multinomial.Logged multinomial = malletClassifier.getMultinomials()[i];
			
			for (int j = 0; j < multinomial.size(); ++j) {
				int location = multinomial.location(j);
				double value = multinomial.value(j);
				
				String t = (String)instanceAlphabet.lookupObject(location);
				loglikelihoods.put(t, value);
				features.add(t);
			}
			
			/*if (multinomial.getIndices() != null) {
				for (int index : multinomial.getIndices()) {
					double logProb = multinomial.logProbability(index);
					Token t = (Token)instanceAlphabet.lookupObject(index);
					loglikelihoods.put(t.getText(), logProb);
					features.add(t.getText());
				}
			}*/
			
			nbc.features.put(i, features);
			nbc.loglikelihoods.put(i, loglikelihoods);
			nbc.logpriors.put(i, logprior);
		}
		
		classifier.setNaiveBayesClassifier(nbc);
		
		log.info("trainClassifier end");
		
//		entityManager.flush();
		// printClassifier(classifier);
	}
}