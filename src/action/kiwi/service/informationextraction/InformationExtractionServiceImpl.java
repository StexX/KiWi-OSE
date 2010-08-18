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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.fragment.FragmentFacade;
import kiwi.api.fragment.FragmentService;
import kiwi.api.informationextraction.Extractlet;
import kiwi.api.informationextraction.FeatureStoreService;
import kiwi.api.informationextraction.InformationExtractionServiceLocal;
import kiwi.api.informationextraction.InformationExtractionServiceRemove;
import kiwi.api.informationextraction.KiWiGATEService;
import kiwi.api.system.StatusService;
import kiwi.api.tagging.TaggingService;
import kiwi.api.transaction.TransactionService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.content.TextFragment;
import kiwi.model.informationextraction.ClassifierEntity;
import kiwi.model.informationextraction.Context;
import kiwi.model.informationextraction.Example;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.InstanceExtractorEntity;
import kiwi.model.informationextraction.NaiveBayesClassifier;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.KiWiClass;
import kiwi.model.ontology.KiWiProperty;
import kiwi.model.status.SystemStatus;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import kiwi.service.transaction.KiWiSynchronizationImpl;
import kiwi.util.KiWiXomUtils.NodePos;
import kiwi.util.KiWiXomUtils.NodePosIterator;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.IntervalDuration;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;

import com.google.gwt.i18n.client.Messages;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.NaiveBayes;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.types.Alphabet;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelAlphabet;
import cc.mallet.types.Multinomial;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;

/**
 * 
 *  Information extraction service. Provides suggestions for various kinds of
 *  annotation. The extraction itself is handled by "extractlets". 
 *  An Extractlet may produce suggestions on its own, or it can
 *  be assigned a specific resource (such as a tag) and use machine learning
 *  to classify potential instances. The assignment is represented by a 
 *  "Classifier" entity.
 * 
 * TODO:
 * 	  cleanup
 *    documentation
 * 
 * @author Marek Schmidt
 *
 */
@Name("kiwi.informationextraction.informationExtractionService")
@Scope(ScopeType.APPLICATION)
public class InformationExtractionServiceImpl implements
		InformationExtractionServiceLocal, InformationExtractionServiceRemove {
	
	private abstract class InformationExtractionTask implements Task {
		abstract void run(InformationExtractionServiceImpl service);
		
		// A very simple synchronization mechanism... 
		// We can't use Lock, because we create the lock in a different thread than the one that will be unlocking it...
		Semaphore running;
		
		public InformationExtractionTask () {
			running = new Semaphore(0);
		}
		
		@Override
		public void waitForCompletition() throws InterruptedException {
			try{
				running.acquire();
			}
			finally{
				running.release();
			}
		}
		
		
		public void start(InformationExtractionServiceImpl service) {
			try {
				service.log.info("task: active: #0", Transaction.instance().isActive());
				run(service);
//				service.entityManager.flush();
			}
			catch(Exception x) {
				service.log.error("error running task ",x);
			}
			finally{
				complete();
			}
		}
		
		public void complete() {
			running.release();
		}
	}
	
	private Queue<InformationExtractionTask> taskQueue;
	private Lock runningLock;
	private boolean running = false;
	
	@Logger
	private Log log;
	
	@In(value="kiwi.informationextraction.gateService", required = false)
	KiWiGATEService kiwiGateService;
	
	@In(create=true)
	TripleStore tripleStore;
	
	@In
	EntityManager entityManager;
	
	@In
	private TransactionService transactionService;
	
	@In
	KiWiEntityManager kiwiEntityManager;
	
	@In
	FacesMessages facesMessages;
	
	@In(create=true)
	ContentItemService contentItemService;
	
	@In("kiwi.core.statusService")
	private StatusService statusService;
	
	@In
	private ConfigurationService configurationService;
	
	@In(create=true)
	private TaggingService taggingService;
	
	private boolean enabled;
	private boolean online;
	
	private Collection<String> extractlets; 
	
	@Create
	public void create() {
		taskQueue = new ConcurrentLinkedQueue<InformationExtractionTask>();
		runningLock = new ReentrantLock();
		
		enabled = configurationService.getConfiguration("kiwi.informationextraction.informationExtractionService.enabled", "false").getBooleanValue();
		online = configurationService.getConfiguration("kiwi.informationextraction.informationExtractionService.online", "false").getBooleanValue();
		
		// TODO: configurable
		extractlets = new LinkedList<String> ();
		extractlets.add("kiwi.informationextraction.englishGateEntityExtractlet");
		extractlets.add("kiwi.informationextraction.deutschGateEntityExtractlet");
		extractlets.add("kiwi.informationextraction.documentTagExtractlet");
		extractlets.add("kiwi.informationextraction.documentTypeExtractlet");
		extractlets.add("kiwi.informationextraction.numberExtractlet");
		extractlets.add("kiwi.informationextraction.actionItemExtractlet");
		
		// enqueue the init task, so we initialze in the background
		init();
	}
	
	private void _init() {
		if (kiwiGateService != null) {
			SystemStatus status = new SystemStatus("initialization of the Information Extraction Service");
			status.setProgress(0);
			statusService.addSystemStatus(status);
			
			kiwiGateService.init();
			
			statusService.removeSystemStatus(status);
		}
	}
	
	public void init() {
		enqueueTask (new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				service._init();
			}
		});
	}
	
	@Remove
	public void remove() {
		
	}
	
	@Override
	public Collection<kiwi.model.informationextraction.Suggestion> extractEntities(ContentItem ci) {
		return extractEntities(ci.getResource(), ci.getTextContent(), ci.getLanguage());
	}

	@Override
	public Collection<kiwi.model.informationextraction.Suggestion> extractTags(ContentItem ci) {
		return extractTags(ci.getResource(), ci.getTextContent(), ci.getLanguage());
	}
	
	
	private Collection<kiwi.model.informationextraction.Suggestion> extractSuggestions (KiWiResource context,
			TextContent content, Locale language) {
		Collection<kiwi.model.informationextraction.Suggestion> ret = new LinkedList<kiwi.model.informationextraction.Suggestion> ();
		gate.Document gateDoc = kiwiGateService.textContentToGateDocument(context, content);
		
		if (gateDoc == null) {
			log.info("Unable to preprocess document");
			return ret;
		}
		
		// run the pipeline for the language of the content item. (should ignore not supported languages)
		kiwiGateService.run(gateDoc, language.getLanguage());
				
		// Now, run all the extractlets.
		for (String extractletName : getExtractletNames()) {
			Extractlet extractlet = (Extractlet)Component.getInstance(extractletName);
			
			for (kiwi.model.informationextraction.Suggestion suggestion : extractlet.extract(context, content, gateDoc, language)) {
				// force the name, so we can be sure which component to blame for the suggestion.
				suggestion.setExtractletName(extractletName);
								
				ret.add(suggestion);
			}
		}
		
		return ret;
	}

	@Override
	public Collection<String> getExtractletNames() {
		return extractlets;
	}

	/**
	 * Deprecated interface for getting suggestions. It just wraps the new suggestion API.
	 */
	@Override
	public Collection<kiwi.model.informationextraction.Suggestion> extractEntities(KiWiResource context,
			TextContent content, Locale language) {
		if (kiwiGateService == null) {
			log.info("GATE service not initialized.");
			return new LinkedList<kiwi.model.informationextraction.Suggestion>();
		}
		
		Collection<kiwi.model.informationextraction.Suggestion> ret = new LinkedList<kiwi.model.informationextraction.Suggestion> ();
		
		for (kiwi.model.informationextraction.Suggestion suggestion : extractSuggestions (context, content, language)) {
			if (suggestion.getKind() == kiwi.model.informationextraction.Suggestion.ENTITY) {
				ret.add(suggestion);
			}
		}

		return ret;
	}

	/**
	 * Deprecated interface for getting suggestions. It just wraps the new suggestion API.
	 */
	@Override
	public Collection<kiwi.model.informationextraction.Suggestion> extractTags(KiWiResource context,
			TextContent content, Locale language) {
		if (kiwiGateService == null) {
			log.info("GATE service not initialized.");
			return new LinkedList<kiwi.model.informationextraction.Suggestion>();
		}
				
		Collection<kiwi.model.informationextraction.Suggestion> ret = new LinkedList<kiwi.model.informationextraction.Suggestion> ();
		
		for (kiwi.model.informationextraction.Suggestion suggestion : extractSuggestions (context, content, language)) {
			if (suggestion.getKind() == kiwi.model.informationextraction.Suggestion.TAG) {
				ret.add(suggestion);
			}
		}

		return ret;
	}
	
	/**
	 * Get the coordinates of a text fragment in the text space.
	 * @param tf
	 * @return 2 member array, begin and end offset of the text fragment.
	 */
	private Integer[] textFragment2Offsets(TextFragment tf) {
		Integer[] ret = new Integer[2];
		
		// KiWiXomUtils
		int begin = -1;
		int end = -1;
		NodePosIterator npi = new NodePosIterator(tf.getContainingContent().getXmlDocument());
		while(npi.hasNext()) {
			NodePos np = npi.next();
			
			if (np.getNode().equals(tf.getBookmarkStart())) {
				begin = np.getPos();
			}
			if (np.getNode().equals(tf.getBookmarkEnd())) {
				end = np.getPos();
			}
		}
		
		if (begin == -1 || end == -1) {
			log.info("bookmarks not found in textcontent for fragment #0", tf.getResource().getKiwiIdentifier());
		}
		
		ret[0] = begin;
		ret[1] = end;
		
		return ret;
	}
	
	@Override
	public Collection<ClassifierEntity> getClassifiersForResource(KiWiResource resource) {
		Query q;
						
		q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listClassifiersByResourceId");
		q.setParameter("resourceId", resource.getId());
				
		return (Collection<ClassifierEntity>) q.getResultList();
	}
	
	@Override
	public Collection<ClassifierEntity> getClassifiers() {
		Query q;
				
		q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listClassifiers");
				
		return (Collection<ClassifierEntity>) q.getResultList();
	}
	
	/*
	private Collection<ClassifierEntity> getClassifiersForType(InstanceExtractorEntity type) {
		Query q;
		
		// get the classifier for this tag, so we now the type of instances to look for
		q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listClassifiersByInstanceType");
		q.setParameter("type", type);
		
		return (Collection<ClassifierEntity>) q.getResultList();
	}*/
	
	/*
	private void initContentTagExample(ContentItem ci, ClassifierEntity classifier, int klass) {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		
		Query q;
		q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listInstancesBySourceItemAndInstanceType");
		q.setParameter("itemid", ci.getId());
		q.setParameter("type", classifier.getInstanceType());
		
		Collection<InstanceEntity> instances = q.getResultList();
		for (InstanceEntity instance : instances) {
			if (!instance.getContext().getIsFragment()) {
				
				// TODO: delete any previous example with the same instance and classifier
				
				Example example = new Example();
				example.setInstance(instance);
				example.setClassifier(classifier);
				example.setType(klass);
				
				// TODO: it should author of the tagging, not the content item.
				example.setUser(ci.getAuthor());
				
				em.persist(example);
			}
		}
	}*/
	
	/**
	 * Create examples from instances matching the tagged fragment facade. 
	 * This method excepts that the fragment facade is tagged with the tag the classifier is a tag for.
	 * @param ff
	 * @param classifier
	 */
	/*
	private void initFragmentTagExample(FragmentFacade ff, ClassifierEntity classifier, int klass) {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		FragmentService fragmentService = (FragmentService)Component.getInstance("fragmentService");
		
		ContentItem containingCi = ff.getContainingContentItem();
		
		if (containingCi != null) {
			TextFragment tf = fragmentService.getTextFragment(ff);
			Integer[] offsets = textFragment2Offsets (tf);
			
			int begin = offsets[0];
			int end = offsets[1];
			
			// try to match an instance... 
			Query q;
			q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listInstancesBySourceItemAndInstanceType");
			q.setParameter("itemid", containingCi.getId());
			q.setParameter("type", classifier.getInstanceType());
			
			Collection<InstanceEntity> instances = q.getResultList();
			InstanceEntity match = null;
			for (InstanceEntity instance : instances) {
				if (!instance.getContext().getIsFragment()) {
					continue;
				}

				log.info("instance entity #0 #1", instance.getSourceContentItem().getResource().getKiwiIdentifier(), instance.getContext().toString());
				Context ctx = instance.getContext();
				if ( (ctx.getInBegin() < begin && ctx.getInEnd() < end) || (ctx.getInBegin() > begin && ctx.getInEnd() > end)) {
					// no match
					log.info("no match");
				}
				else {
					// nonempty intersection, good enough.
					
					match = instance;
					
					// TODO: find the best match of all the candidates.
					break;
				}
			}
			
			if (match != null) {
				log.info("match! creating example.");
				Example example = new Example();
				example.setInstance(match);
				example.setClassifier(classifier);
				example.setType(klass);
				
				// TODO: it should author of the tagging, not the fragment.
				example.setUser(ff.getAuthor());
				
				em.persist(example);
			}
		}
		else {
			log.info("Fragment #0 is not contained anywhere!", ff.getKiwiIdentifier());
		}
	}*/
	
	/*
	private void _initTagExamples(ClassifierEntity classifier, ContentItem tag) {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		FragmentService fragmentService = (FragmentService)Component.getInstance("fragmentService");
		
		Query q;
		
		// clear all existing examples about this classifier first
		q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.deleteExamplesByClassifier");
		q.setParameter("classifier", classifier);
		q.executeUpdate();
		
		// get all the positive examples as a set of actual fragments
		q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listTaggedItemsByTaggingItem");
    	q.setParameter("id", tag.getId());
    	
		KiWiUriResource fragmentType = tripleStore.createUriResource(Constants.NS_KIWI_SPECIAL + "Fragment");
	
		Collection<ContentItem> taggedes = q.getResultList();
			
		for (ContentItem tagged : taggedes) {
			log.info("tagged item #0", tagged.getResource().getKiwiIdentifier());
			// potential tagged fragments
			if (tagged.getResource().hasType(fragmentType)) {
				FragmentFacade ff = kiwiEntityManager.createFacade(tagged, FragmentFacade.class);
				
				initFragmentTagExample(ff, classifier, Example.POSITIVE);
			}
			else {
				// potential content items.
				initContentTagExample(tagged, classifier, Example.POSITIVE);
			}
		}
			
		em.flush();
	}*/
	
	/*
	 * Creates one specific example from a tagging.
	 * @param classifier a classifier
	 * @param tag a tagging
	 */
	/*
	private void _createTagExample(ClassifierEntity classifier, Tag tagging, int klass) {
		
		KiWiUriResource fragmentType = tripleStore.createUriResource(Constants.NS_KIWI_SPECIAL + "Fragment");
		
		ContentItem tagged = tagging.getTaggedResource();
		
		if (tagged.getResource().hasType(fragmentType)) {
			FragmentFacade ff = kiwiEntityManager.createFacade(tagged, FragmentFacade.class);
			
			initFragmentTagExample(ff, classifier, klass);
		}
		else {
			// potential content items.
			initContentTagExample(tagged, classifier, klass);
		}
	}
	*/
	
	/*
	 * Initialize the fragment tag examples from actual tagged fragments 
	 * and content tag examples from tagged content items.
	 * 
	 * This method should not be necessary in the end, examples should 
	 * be gathered automatically on taggings creation and other user actions.
	 * 
	 * It expects that instances has already been produced.
	 * 
	 * @param tag
	 */
	//@Asynchronous
	/*
//	@Transactional
	private void initTagExamples(final ClassifierEntity classifier, final ContentItem tag) {
		enqueueTask (new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				service._initTagExamples(classifier, tag);
			}
		});
	}*/
	
	private void _initExamples(final ClassifierEntity classifier) {
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		SystemStatus status = new SystemStatus("initialization of classifier " + classifier);
		status.setProgress(0);
		statusService.addSystemStatus(status);
		
		log.info("starting initialization for classifier #0", classifier.getId());
		
		if (classifier.getExtractletName() != null) {
			
			Query q;

			q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.deleteExamplesByClassifier");
			q.setParameter("classifier", classifier);
			q.executeUpdate();
			
			q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listSuggestionsByClassifier");
			q.setParameter("classifier", classifier);			
			Collection<kiwi.model.informationextraction.Suggestion> suggestions = q.getResultList();
			for (kiwi.model.informationextraction.Suggestion suggestion : suggestions) {
				suggestion.getResources().clear();
				suggestion.getTypes().clear();
				suggestion.getRoles().clear();
				suggestion = entityManager.merge(suggestion);
				entityManager.remove(suggestion);
			}
			
//			entityManager.flush();
			
			Extractlet extractlet = (Extractlet)Component.getInstance(classifier.getExtractletName());
			extractlet.initClassifier(classifier);
			
			/*
			for (Example example : examples) {
				entityManager.persist(example);
			}*/
		}
		
//		entityManager.flush();
		
		log.info("ended initialization for classifier #0", classifier.getId());		
		statusService.removeSystemStatus(status);
	}
	
	@Override
	public Task initExamples(final ClassifierEntity classifier) {
		return enqueueTask (new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				service._initExamples(classifier);
				// service._initTagExamples(classifier, classifier.getResource().getContentItem());
			}
		});
		//initTagExamples(classifier, classifier.getResource().getContentItem());
	}
	
	@Override
	public void initExamplesForClassifierResource(final KiWiResource resource) {
		log.info("initExamplesForClassifierResource: #0", resource.getKiwiIdentifier());
		enqueueTask (new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				for (final ClassifierEntity classifier : service.getClassifiersForResource(resource)) {
					service._initExamples(classifier);
				}
			}
		});
	}
	
	/**
	 * Create an example from a tagging. 
	 * It tries to find an existing Suggestion matching this tagging,
	 * and will try to create a new suggestion if none is found (from existing instances)
	 * 
	 * @param classifier the classifier for the tagging resource
	 * @param tagged content item tagged (tagged resource)
	 * @param user the user responsible for the example (e.g. author of the tagging)
	 * @param klass (positive/negative)
	 */
	private void _createTagExample(ClassifierEntity classifier, ContentItem tagged, User user, int klass) {
		
		// TODO: support fragment tags as well.
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		if (tagged == null || tagged.getTextContent() == null) {
			log.info("_createTagExample: tagged item has no text content!");
			return;
		}
		
		Extractlet extractlet = (Extractlet)Component.getInstance(classifier.getExtractletName());
		
		Query q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listSuggestionsByClassifierAndSourceTextContent");
		q.setParameter("classifier", classifier);
		q.setParameter("contentid", tagged.getTextContent().getId());
		Collection<Suggestion> suggestions = (Collection<Suggestion>)q.getResultList();
		for (Suggestion suggestion : suggestions) {
			log.info("_createTagExample: creating example for suggestion #0", suggestion.getId());
			if (klass == Example.POSITIVE) {
				extractlet.accept(suggestion, user);
			}
			else {
				extractlet.reject(suggestion, user);
			}
		}
		
		if (suggestions.size() == 0) {
			log.info("_createTagExample: no suggestion found for #0 #1", classifier, tagged);
			
			q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listInstancesBySourceTextContentAndExtractletName");
			q.setParameter("name", classifier.getExtractletName());
			q.setParameter("contentid", tagged.getTextContent().getId());
			
			Collection<InstanceEntity> instances = (Collection<InstanceEntity>)q.getResultList();
			Collection<Suggestion> instanceSuggestions = extractlet.classifyInstances(classifier, instances);
			for (Suggestion suggestion : instanceSuggestions) {				
				entityManager.persist(suggestion);
				
				if (klass == Example.POSITIVE) {
					extractlet.accept(suggestion, user);
				}
				else {
					extractlet.reject(suggestion, user);
				}
			}
			
			if (instances.size() == 0) {
				log.info("_createTagExample: no instances found for #0 #1", classifier, tagged);
			}
		}
	}
	
	private void createExampleForTagging(final Tag tagging, final int klass) {
		if (tagging.getTaggingResource() == null) {
			// no tagging resource, no classifier
			return;
		}
		
		for (final ClassifierEntity classifier : this.getClassifiersForResource(tagging.getTaggingResource().getResource())) {
			enqueueTask (new InformationExtractionTask() {
				@Override
				public void run(InformationExtractionServiceImpl service) {
					service._createTagExample(classifier, tagging.getTaggedResource(), tagging.getTaggedBy(), klass);
				}
			});
		}
	}
	
	
//	@Transactional
	@Override
	public void createPositiveExampleForTagging(final Tag tagging) {
		// This operation breaks
		// createExampleForTagging(tagging, Example.POSITIVE);
	}
	
//	@Transactional
	@Override
	public void createNegativeExampleForTagging(final Tag tagging) {
		createExampleForTagging(tagging, Example.NEGATIVE);
	}
	
	/*
	private Collection<InstanceExtractorEntity> createInstanceExtractors () {
		Collection<InstanceExtractorEntity> ret = new LinkedList<InstanceExtractorEntity>();
		
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		Query q;
		
		q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.deleteInstanceExtractors");
		q.executeUpdate();
		
		InstanceExtractorEntity entity;
		
		// TODO: make configurable
		entity = new InstanceExtractorEntity();
		entity.setMalletAlphabet(new Alphabet());
		entity.setType(DocumentInstanceExtractor.class.getName());
		entity.setTitle("Document");
		em.persist(entity);
		ret.add(entity);
		
		// disable noun phrase entities for now. they are stored terribly inefficiently, produce lots of garbage in the database
		// TODO: fix it, after implementing better feature storage.

 		entity = new InstanceExtractorEntity();
		entity.setMalletAlphabet(new Alphabet());
		entity.setType(NounPhraseInstanceExtractor.class.getName());
		entity.setTitle("Noun Phrase");
		em.persist(entity);
		ret.add(entity);
		
		
		entity = new InstanceExtractorEntity();
		entity.setMalletAlphabet(new Alphabet());
		entity.setType(DateInstanceExtractor.class.getName());
		entity.setTitle("Date");
		em.persist(entity);
		ret.add(entity);
		
		return ret;
	}*/
	
	private void _initInstances() {
		SystemStatus status = new SystemStatus("initialization of instances for Information Extraction");
		status.setProgress(0);
		statusService.addSystemStatus(status);
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		FeatureStoreService featureStoreService = (FeatureStoreService)Component.getInstance("kiwi.informationextraction.featureStoreService");
		
		Query q;

		// TODO: can't this be done somehow using cascade delete? does hibernate support it in some way?
		q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.deleteExamples");
		q.executeUpdate();

		// TODO: can't this be done somehow without using native queries?
		q = entityManager.createNativeQuery("delete from Suggestion_Resource");
		q.executeUpdate();
		q = entityManager.createNativeQuery("delete from Suggestion_Type");
		q.executeUpdate();
		q = entityManager.createNativeQuery("delete from Suggestion_Role");
		q.executeUpdate();
		
		q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.deleteSuggestions");
		q.executeUpdate();
		
		// delete all existing instances first.
		q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.deleteInstances");
		q.executeUpdate();
		
		
		// prepare instance extractors
		// Collection<InstanceExtractorEntity> instanceExtractors = getInstanceExtractors();
	
		Collection<Extractlet> extractlets = getExtractlets();
		
		// clean the alphabets
		/*for (InstanceExtractorEntity extractor : instanceExtractors) {
			extractor.setMalletAlphabet(new Alphabet());
		}*/
	
		// Blacklist of types, for which we do not generate instances
		// TODO: user defined rules for what extractors use on which types?
		Set<KiWiResource> typeBlacklist = new HashSet<KiWiResource>();

		KiWiUriResource contentItemType = tripleStore.createUriResource(Constants.NS_KIWI_CORE + "ContentItem");
		typeBlacklist.add (tripleStore.createUriResource(Constants.NS_OWL + "Class"));
		typeBlacklist.add (tripleStore.createUriResource(Constants.NS_OWL + "Ontology"));
		typeBlacklist.add (tripleStore.createUriResource(Constants.NS_OWL + "ObjectProperty"));
		typeBlacklist.add (tripleStore.createUriResource(Constants.NS_OWL + "DatatypeProperty"));
		
		typeBlacklist.add (tripleStore.createUriResource(Constants.NS_RDF + "Property"));
		
		typeBlacklist.add (tripleStore.createUriResource(Constants.NS_RDFS + "Class"));
		
		typeBlacklist.add (tripleStore.createUriResource(Constants.NS_SKOS + "Concept"));
		
		Set<ContentItem> cis = contentItemService.getContentItems();
		int count = 0;
items: 
		for (ContentItem ci : cis) {
			
			status.setProgress((100 * count) / cis.size());
			++count;
			
			// it has to be a content item...
			if (!ci.getResource().hasType(contentItemType)) {
				continue items;
			}
			// ...and may not be of one of blacklist types.
			for (KiWiResource type : ci.getTypes()) {
				if (typeBlacklist.contains(type)) {
					continue items;
				}
			}
			
			// process the content by a GATE pipeline, first create a GATE representation of the text content:
			gate.Document gateDoc = kiwiGateService.textContentToGateDocument(ci.getResource(), ci.getTextContent());
			
			if (gateDoc == null) {
				log.info("Unable to preprocess document");
				continue items;
			}
			
			// run the pipeline for the language of the content item. (should ignore not supported languages)
			kiwiGateService.run(gateDoc, ci.getLanguage().getLanguage());
			
			
			// we still don't have enough confusion about what "ie" means... 
			/*for (InstanceExtractorEntity ie : instanceExtractors) {
				for (InstanceEntity i : ie.extractInstances(ci, gateDoc)) {
					em.persist(i);
					
					log.debug("created instance #0 #1, #2", i.getSourceContentItem().getTitle(), i.getId(), i.getContext().toString());
				}
			}*/
			for (Extractlet extractlet : extractlets) {
				
				for (InstanceEntity instance : extractlet.extractInstances(ci.getResource(), ci.getTextContent(), gateDoc, ci.getLanguage())) {
					entityManager.persist(instance);
										
					if (instance.getFeatures() != null) {
						featureStoreService.put(instance.getExtractletName(), instance.getId(), instance.getFeatures());
					}
					
					log.debug("created instance #0 #1, #2", instance.getSourceResource().getContentItem().getTitle(), instance.getId(), instance.getContext().toString());
				}
				
				featureStoreService.flush(extractlet.getName());
			}
			
		}

		// entityManager.flush();
		
		statusService.removeSystemStatus(status);
	}
	
	private Collection<Extractlet> getExtractlets() {
		Collection<Extractlet> ret = new LinkedList<Extractlet> ();
		for (String extractletName : getExtractletNames()) {
			ret.add((Extractlet)Component.getInstance(extractletName));
		}
		
		return ret;
	}
	
	private Extractlet getExtractlet(String extractletName) {
		return (Extractlet)Component.getInstance(extractletName);
	}

	public void initInstances() {
		enqueueTask (new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				service._initInstances();
			}
		});
	}
	
	// just create a new labe l alphabet and make sure we create it always the same way... 
	/*
	private LabelAlphabet getLabelAlphabet() {
		LabelAlphabet ret = new LabelAlphabet();
		ret.lookupLabel("+", true);
		ret.lookupLabel("-", true);
		
		return ret;
	}*/
		
	
	private void _trainClassifier (ClassifierEntity classifier) {
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		if (!entityManager.contains(classifier)) {
			classifier = entityManager.find(ClassifierEntity.class, classifier.getId());
		}
		
		Extractlet extractlet = getExtractlet(classifier.getExtractletName());
		extractlet.trainClassifier(classifier);
		
		assert classifier.getMalletAlphabet() != null;
		assert classifier.getMalletLabelAlphabet() != null;
		assert classifier.getMalletClassifier() != null;
		assert classifier.getNaiveBayesClassifier() != null;
		
		/*
		Query q;
		
		Alphabet instanceAlphabet = new Alphabet();
		
		LabelAlphabet labelAlphabet = new LabelAlphabet();
		labelAlphabet.lookupLabel("+", true);
		labelAlphabet.lookupLabel("-", true);
					
		ArrayList<Pipe> labelPipeList = new ArrayList<Pipe>();
		labelPipeList.add(new Target2Label(labelAlphabet));
		Pipe labelPipe = new SerialPipes(labelPipeList);
		
		ArrayList<Pipe> featurePipeList = new ArrayList<Pipe>();
		//Pattern tokenPattern =
        //    Pattern.compile("\\S+");
		// featurePipeList.add(new CharSequence2TokenSequence(tokenPattern));
		featurePipeList.add(new TokenSequence2FeatureSequence(instanceAlphabet));
		featurePipeList.add(new FeatureSequence2FeatureVector());
		 
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
			malletInstance = featurePipe.instanceFrom(malletInstance);
			
			if (x.getType() == Example.POSITIVE) {
				malletInstance.setTarget("+");
			}
			else if (x.getType() == Example.NEGATIVE) {
				malletInstance.setTarget("-");
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
		
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();
				
		for (int i = 0; i < 1; ++i) {
			List<String> features = new LinkedList<String>();
			Map<String, Double> loglikelihoods = new HashMap<String, Double> ();
			double logprior = malletClassifier.getPriors().logProbability(i);
			
			Multinomial.Logged multinomial = malletClassifier.getMultinomials()[i];
			for (int index : multinomial.getIndices()) {
				double logProb = multinomial.logProbability(index);
				Token t = (Token)instanceAlphabet.lookupObject(index);
				loglikelihoods.put(t.getText(), logProb);
				features.add(t.getText());
			}
			
			nbc.features.put(i, features);
			nbc.loglikelihoods.put(i, loglikelihoods);
			nbc.logpriors.put(i, logprior);
		}
		
		classifier.setNaiveBayesClassifier(nbc);*/
		
		// entityManager.flush();

		log.debug("training end");
	}
	
	@Override
	public void trainClassifier (final ClassifierEntity classifier) {
		enqueueTask (new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				service._trainClassifier(classifier);
			}
		});
	}
	
	/**
	 * Runs the classifier on an instance and produces an suggestion accordingly. 
	 * @param em
	 * @param classifier
	 * @param entity
	 */
	/*
	private kiwi.model.informationextraction.Suggestion classifySuggestion (ClassifierEntity classifier, InstanceEntity entity) {
		
		Classifier malletClassifier = classifier.getMalletClassifier();
		
		if (malletClassifier == null) {
			return null;
		}

		Instance inst = entity.getMalletInstance().shallowCopy();
		Classification cls = malletClassifier.classify(inst);
		
		log.debug("classification of #0 #1 \n #2", entity.getSourceContentItem().getKiwiIdentifier(), entity.getContext(), cls.toString());
		
		if (cls.getLabeling().getBestLabel().getEntry().equals("+")) {
			kiwi.model.informationextraction.Suggestion suggestion = new kiwi.model.informationextraction.Suggestion();
			suggestion.setInstance(entity);
			suggestion.setScore((float)cls.getLabeling().getBestValue());
			suggestion.setClassifier(classifier);
			
			// construct an actual suggestion. The specific kind of suggestion depends
			// mostly on the type of the classifier resource... (if it's a tag, or a property, or a type...)
			// TODO: we just recognize tags/fragments for now... implement other kinds... 
			suggestion.setLabel(classifier.getResource().getContentItem().getTitle());
			if (entity.getContext().getIsFragment()) {
				suggestion.setKind(kiwi.model.informationextraction.Suggestion.TAGGED_FRAGMENT);
			}
			else {
				suggestion.setKind(kiwi.model.informationextraction.Suggestion.TAG);
			}
			List<KiWiResource> resources = new LinkedList<KiWiResource>();
			
			resources.add(classifier.getResource());
			suggestion.setResources(resources);

			return suggestion;
		}
		
		return null;
	}*/

	/*
	private void _computeSuggestions(ClassifierEntity classifier) {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		Query q;
		
		if (!em.contains(classifier)) {
			classifier = em.find(ClassifierEntity.class, classifier.getId());
		}
				
		// delete all existing suggestions about this tag first.
		q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listSuggestionsByClassifier");
		q.setParameter("classifier", classifier);
		Collection<kiwi.model.informationextraction.Suggestion> suggestions = q.getResultList();
		for (kiwi.model.informationextraction.Suggestion suggestion : suggestions) {
			suggestion.getResources().clear();
			suggestion.getTypes().clear();
			suggestion.getRoles().clear();
			suggestion = em.merge(suggestion);
			em.remove(suggestion);
		}
		
		em.flush();
		
		q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listInstancesByInstanceType");
		q.setParameter("type", classifier.getInstanceType());
		Collection<InstanceEntity> instances = q.getResultList();
		for (InstanceEntity entity : instances) {
			kiwi.model.informationextraction.Suggestion suggestion = classifySuggestion(classifier, entity);
			if (suggestion != null) {
				em.persist(suggestion);
			}
		}
		
		em.flush();
	}*/
	
	private void _computeSuggestions(ClassifierEntity classifier) {
		
		SystemStatus status = new SystemStatus("Computing suggestions for classifier " + classifier);
		status.setProgress(0);
		statusService.addSystemStatus(status);
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		Query q;
		
		if (!entityManager.contains(classifier)) {
			classifier = entityManager.find(ClassifierEntity.class, classifier.getId());
		}
		
		Extractlet extractlet = getExtractlet(classifier.getExtractletName());
		
		q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listSuggestionsByClassifier");
		q.setParameter("classifier", classifier);
		
		Collection<kiwi.model.informationextraction.Suggestion> suggestions = q.getResultList();
		extractlet.updateSuggestions(classifier, suggestions);
		// entityManager.flush();
		/*int total = suggestions.size();
		int i = 0;
		for (kiwi.model.informationextraction.Suggestion suggestion : suggestions) {
			extractlet.updateSuggestion(suggestion);
			// status.setProgress((int)((100.0 * (i++)) / total));
		}*/
		
		statusService.removeSystemStatus(status);
	}
	
	@Override
	public void computeSuggestions(final ClassifierEntity classifier) {
		enqueueTask (new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				service._computeSuggestions(classifier);
			}
		});
	}

	@Override
//	@Transactional(TransactionPropagationType.REQUIRED)
	public void acceptSuggestion(kiwi.model.informationextraction.Suggestion s, User user) {
		
		log.info("acceptSuggestion");
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		Extractlet extractlet = (Extractlet)Component.getInstance(s.getExtractletName());
		if (extractlet != null) {
			log.info("acceptSuggestion extractlet #0", s.getExtractletName());
			extractlet.accept(s, user);
//			entityManager.flush();
		}
		else {
			log.error("acceptSuggestion: no extractlet with the name #0", s.getExtractletName());
		}
	}

	@Override
//	@Transactional(TransactionPropagationType.REQUIRED)
	public void rejectSuggestion(kiwi.model.informationextraction.Suggestion s, User user) {
		log.info("rejectSuggestion");
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		Extractlet extractlet = (Extractlet)Component.getInstance(s.getExtractletName());
		if (extractlet != null) {
			log.info("rejectSuggestion extractlet #0", s.getExtractletName());
			extractlet.reject(s, user);
//			entityManager.flush();
		}
		else {
			log.error("rejectSuggestion: no extractlet with the name #0", s.getExtractletName());
		}
	}

	@Override
	public void trainAndSuggest(ClassifierEntity classifier) {
		
		// SystemStatus status = new SystemStatus("recomputing tag suggestions for tag " + tag.getTitle());
		// status.setProgress(0);
		// statusService.addSystemStatus(status);
		
		this.trainClassifier(classifier);
				
		// status.setProgress(50);
		
		this.computeSuggestions(classifier);
				
		// statusService.removeSystemStatus(status);
	}
	
	/*
	private void _trainAndSuggestForItem(ContentItem item) {
		SystemStatus status = new SystemStatus("recomputing tag suggestions for item " + item.getTitle());
		status.setProgress(0);
		statusService.addSystemStatus(status);
		
		// retrain all relevant classifiers
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		Query q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listExamplesByInstanceSourceItem");
    	q.setParameter("itemid", item.getId());
		
    	Set<ClassifierEntity> classifiers = new HashSet<ClassifierEntity>();
    	List<Example> examples = (List<Example>)q.getResultList();
    	for (Example example : examples) {
    		classifiers.add(example.getClassifier());
    	}
    	
    	// TODO: compute suggestions only about item, put the rest for background processing
    	for (ClassifierEntity classifier : classifiers) {
    		this.trainClassifier(classifier);
    		this.computeSuggestions(classifier);
    	}
		
    	em.flush();
    	
		statusService.removeSystemStatus(status);
	}*/
	
	
	@Override
	public void trainAndSuggestForItem(final ContentItem item) {
		enqueueTask (new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				// TODO:
				// service._trainAndSuggestForItem(item);
			}
		});
	}
	
	private void _trainAndSuggestForClassifierResource (KiWiResource resource) {
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		SystemStatus status = new SystemStatus("recomputing tag suggestions for resource " + resource.getContentItem().getTitle());
		status.setProgress(0);
		statusService.addSystemStatus(status);
				
		for (ClassifierEntity classifier : this.getClassifiersForResource(resource)) {
			_trainClassifier(classifier);
			_computeSuggestions(classifier);
		}
		
		// entityManager.flush();
    	 
		statusService.removeSystemStatus(status);
	}
	
	@Override
	public Task trainAndSuggestForClassifierResource(final KiWiResource resource) {
		InformationExtractionTask task = new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				service._trainAndSuggestForClassifierResource(resource);
			}
		};
		
		//enqueueTask(task);
		
		task.start(this);
		
		return task;
	}

	@Override
	public Collection<Example> getPositiveExamples (ClassifierEntity classifier) {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		Query q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listPositiveExamplesByClassifier");
    	q.setParameter("classifier", classifier);
		q.setHint("org.hibernate.cacheable", true);
    	
		return q.getResultList();
	}
	
	@Override
	public Collection<Example> getNegativeExamples (ClassifierEntity classifier) {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		Query q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listNegativeExamplesByClassifier");
    	q.setParameter("classifier", classifier);
		q.setHint("org.hibernate.cacheable", true);
    	
		return q.getResultList();
	}

	@Override
	public Collection<Example> getNegativeExamplesByClassifierResource(
			KiWiResource resource) {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		Query q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listNegativeExamplesByClassifierResource");
    	q.setParameter("resource", resource);
		q.setHint("org.hibernate.cacheable", true);
    	
		return q.getResultList();
	}

	@Override
	public Collection<Example> getPositiveExamplesByClassifierResource(
			KiWiResource resource) {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		Query q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listPositiveExamplesByClassifierResource");
    	q.setParameter("resource", resource);
		q.setHint("org.hibernate.cacheable", true);
    	
		return q.getResultList();
	}
	
	@Override
	public Collection<kiwi.model.informationextraction.Suggestion> getSuggestionsByContentItem(ContentItem item) {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		Query q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listSuggestionsByResourceSortedByScore");
    	q.setParameter("resourceid", item.getResource().getId());
		q.setHint("org.hibernate.cacheable", true);
		q.setMaxResults(50);
		    	
		return q.getResultList();
	}
	
	@Override
	public Collection<kiwi.model.informationextraction.Suggestion> getSuggestionsByTextContent(TextContent textcontent) {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		Query q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listSuggestionsByInstanceSourceTextContentSortedByScore");
    	q.setParameter("contentid", textcontent.getId());
		//q.setHint("org.hibernate.cacheable", true);
		//q.setMaxResults(50);
		    	
		return q.getResultList();
	}

	@Override
	public Collection<kiwi.model.informationextraction.Suggestion> getSuggestionsByClassifier(ClassifierEntity classifier) {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		Query q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listSuggestionsByClassifierSortedByScore");
    	q.setParameter("classifier", classifier);
		q.setHint("org.hibernate.cacheable", true);
		q.setMaxResults(50);
    	
		return q.getResultList();
	}
	
	@Override
	public Collection<kiwi.model.informationextraction.Suggestion> getSuggestionsByClassifierResource(
			KiWiResource resource) {
		
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		Query q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listSuggestionsByClassifierResourceSortedByScore");
    	q.setParameter("resource", resource);
		// q.setHint("org.hibernate.cacheable", true);
		q.setMaxResults(7);
    	
		return q.getResultList();
		
	}
	
	@Override
//	@Transactional(TransactionPropagationType.REQUIRED)
	public void deleteClassifier(ClassifierEntity classifier) {
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		Query q;
		
		q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.deleteExamplesByClassifier");
		q.setParameter("classifier", classifier);
		q.executeUpdate();
		
		q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listSuggestionsByClassifier");
		q.setParameter("classifier", classifier);
		// q.setHint("javax.persistence.cache.retrieveMode", "BYPASS");
		Collection<kiwi.model.informationextraction.Suggestion> suggestions = q.getResultList();
		for (kiwi.model.informationextraction.Suggestion suggestion : suggestions) {
			suggestion.getResources().clear();
			suggestion.getTypes().clear();
			suggestion.getRoles().clear();
			if(!entityManager.contains(suggestion) && suggestion.getId() != null) {
				suggestion = entityManager.merge(suggestion);
			}
			entityManager.remove(suggestion);
		}
				
		entityManager.remove(classifier);
		//entityManager.flush();
	}

	/*
	@Override
//	@Transactional
	public void createClassifier(ContentItem tag, InstanceExtractorEntity type) {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		TripleStore tripleStore = (TripleStore)Component.getInstance("tripleStore");
		
		KiWiUriResource tagType = tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag");
		
		if (!tag.getResource().hasType(tagType)) {
			log.info("Content item #0 is not a tag", tag.getKiwiIdentifier());
			return;
		}
		
		ClassifierEntity cls = new ClassifierEntity();
		cls.setResource(tag.getResource());
		cls.setInstanceType(type);
		
		em.persist(cls);
	}*/
		
	@Override
//	@Transactional(TransactionPropagationType.REQUIRED)
	public ClassifierEntity createClassifier(KiWiResource resource, String extractletName) {
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		log.info("creating classifier #0 #1", resource, extractletName);
		
		ClassifierEntity cls = new ClassifierEntity();
		cls.setResource(resource);
		cls.setExtractletName(extractletName);
		
		entityManager.persist(cls);
		
		// entityManager.flush();
		
		return cls;
	}
	
	private void _createAndInitClassifier(KiWiResource resource, String extractletName) {
		log.info("creating classifier #0 #1", resource, extractletName);
		
		ClassifierEntity cls = new ClassifierEntity();
		cls.setResource(resource);
		cls.setExtractletName(extractletName);
		
		entityManager.persist(cls);
		
		this._initExamples(cls);
	}
	
	private void _createAndInitClassifier(KiWiResource resource) {
		KiWiUriResource typeTag = tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag");
		KiWiUriResource typeClass = tripleStore.createUriResource(Constants.NS_OWL + "Class");
		KiWiUriResource typeDatatypeProperty = tripleStore.createUriResource(Constants.NS_OWL + "DatatypeProperty");
		KiWiResource typeInteger = tripleStore.getXSDType(Integer.class);
		
		log.info("typeInteger: #0", typeInteger);
		
		if (resource.hasType(typeTag)) {
			this._createAndInitClassifier(resource, "kiwi.informationextraction.documentTagExtractlet");
			return;
		}
		else if (resource.hasType(typeClass)) {
			this._createAndInitClassifier(resource, "kiwi.informationextraction.documentTypeExtractlet");
			return;
		}
		else if (resource.hasType(typeDatatypeProperty)) {
			log.info("hasType datatype property");
			KiWiProperty property = kiwiEntityManager.createFacade(resource.getContentItem(), KiWiProperty.class);
			if (property != null) {
				log.info("property != null");
				// TODO: not conforming to OWL, only a single range is supported
				
				try {
					for (KiWiTriple triple : resource.listOutgoing("rdfs:range")) {
						KiWiResource range = (KiWiResource)triple.getObject();
						log.info("range: #0", range.getKiwiIdentifier());
						if (range.getKiwiIdentifier().equals(typeInteger.getKiwiIdentifier())) {
							log.info("range contains integer...");
							this._createAndInitClassifier(resource, "kiwi.informationextraction.numberExtractlet");
							return;
						}
					}
				} catch (NamespaceResolvingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		facesMessages.add("No extractlet found that could extract the requested type. Sorry");
	}
	
	@Override
	public Task createAndInitClassifier(final KiWiResource resource, final String extractletName) {
		return enqueueTask (new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				service._createAndInitClassifier(resource, extractletName);
			}
		});
	}
	
	@Override
	public Task createAndInitClassifier(final KiWiResource resource) {
		InformationExtractionTask task = new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				service._createAndInitClassifier(resource);
			}
		};
		
		//enqueueTask(task);
		task.start(this);
		
		return task;
	}
	
	@Override
//	@Transactional(TransactionPropagationType.REQUIRED)
	public ClassifierEntity createClassifier(KiWiResource resource) {
		// TODO: make configurable, based on resource types... 
		return createClassifier(resource, "kiwi.informationextraction.documentTagExtractlet");
	}

	/*
	@Override
	public Collection<InstanceExtractorEntity> getInstanceExtractors() {
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
				
		Query q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.getInstanceExtractors");
		q.setHint("org.hibernate.cacheable", true);
    	
		Collection<InstanceExtractorEntity> ret = q.getResultList();
		
		if (ret.size() == 0) {
			return createInstanceExtractors();
		}
		
		return ret;
	}*/
	
	
	private Task enqueueTask (InformationExtractionTask task) {
		// enqueue tasks only if we have enabled information extraction in config...
		// otherwise, simply drop them.
		if (enabled) {
			taskQueue.add(task);
		}
		
		return task;
	}

	@Override
	//@Transactional(TransactionPropagationType.REQUIRED)
	@Asynchronous
	public void runTasks(@IntervalDuration Long interval) throws Exception {
		
		//log.info("runTasks");
		
		// atomic read/write to running variable
		try{
			runningLock.lock();
			if (running) {
				return;
			}
			else {
				running = true;
			}
		}
		finally{
			runningLock.unlock();
		}
		
		// make sure we set the running to false at the end
		try{
			Transaction.instance().setTransactionTimeout(60000);
			while(!taskQueue.isEmpty()) {
				InformationExtractionTask task = taskQueue.poll();
				if (task != null) {
					log.info("Running IE task");
					
					try {
						Transaction.instance().begin();
						transactionService.registerSynchronization(
		                		KiWiSynchronizationImpl.getInstance(), 
		                		transactionService.getUserTransaction() );
						entityManager.joinTransaction();
						entityManager.setFlushMode(FlushModeType.COMMIT);
						task.run(this);
						Transaction.instance().commit();
					}
					finally {
						task.complete();
					}
				}
			}
		}
		finally{
			// atomic write to running variable
			
			try {
				runningLock.lock();
				running = false;
			}
			finally {
				runningLock.unlock();
			}
			
			//log.info("runTasks exit");
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public boolean getEnabled() {
		return enabled;
	}
	
	@Override
	public void setOnline(boolean online) {
		this.online = online;
	}
	
	@Override
	public boolean getOnline() {
		return this.online;
	}

	/**
	 * Realizes a suggestion.
	 * Creates whatever the suggestion suggests to be created.
	 * 
	 * @param s a suggestion
	 * @param user the user framed with the creation of the whatever.
	 */
	@Override
	public void realizeSuggestion(kiwi.model.informationextraction.Suggestion s, User user) {
		
		if (s.getKind() == Suggestion.DATATYPE) {
			KiWiResource subject = s.getInstance().getSourceResource();
			KiWiUriResource predicate = (KiWiUriResource)s.getClassifier().getResource();
			
			KiWiLiteral object = null;
			if (s.getTypes().size() == 1) {
				KiWiResource type = s.getTypes().get(0);
				object = tripleStore.createLiteral(s.getInstance().getValue(), s.getInstance().getSourceResource().getContentItem().getLanguage(), type);
			}
			else {
				object = tripleStore.createLiteral(s.getInstance().getValue());
			}
			// KiWiLiteral value, language, type)
			tripleStore.createTriple(subject, predicate, object);
		}
		else if (s.getKind() == Suggestion.TAG) {
			KiWiResource subject = s.getInstance().getSourceResource();
			
			log.info("createTagging: #0 #1 #2 #4", s.getLabel(), subject.getContentItem().getKiwiIdentifier(), s.getClassifier().getResource().getContentItem().getKiwiIdentifier(), user.getContentItem().getKiwiIdentifier());
			taggingService.createTagging(s.getLabel(), subject.getContentItem(), s.getClassifier().getResource().getContentItem(), user);
		}
		else if (s.getKind() == Suggestion.TYPE) {
			KiWiResource subject = s.getInstance().getSourceResource();
			KiWiUriResource property = tripleStore.createUriResource(Constants.NS_RDF + "type");
			KiWiResource object = s.getClassifier().getResource();
			tripleStore.createTriple(subject, property, object);
		}
		// s.getClassifier().getInstanceType().realizeInstance(s.getClassifier(), user, s.getInstance());
	}
	
	@Override
	public void unrealizeSuggestion(kiwi.model.informationextraction.Suggestion s, User user) {
		if (s.getKind() == Suggestion.DATATYPE) {
			KiWiResource subject = s.getInstance().getSourceResource();
			KiWiUriResource predicate = (KiWiUriResource)s.getClassifier().getResource();
			
			KiWiLiteral object = null;
			if (s.getTypes().size() == 1) {
				KiWiResource type = s.getTypes().get(0);
				object = tripleStore.createLiteral(s.getInstance().getValue(), s.getInstance().getSourceResource().getContentItem().getLanguage(), type);
			}
			else {
				object = tripleStore.createLiteral(s.getInstance().getValue());
			}
			// KiWiLiteral value, language, type)
			tripleStore.removeTriple(subject, predicate, object);
		}
		else if (s.getKind() == Suggestion.TAG) {
			KiWiResource subject = s.getInstance().getSourceResource();
			
			// taggingService.createTagging(s.getLabel(), subject.getContentItem(), s.getClassifier().getResource().getContentItem(), user);
			for (Tag tag : taggingService.getTags(subject.getContentItem())) {
				if (tag.getTaggedBy().equals(user) && tag.getTaggingResource().equals(s.getClassifier().getResource().getContentItem())) {
					taggingService.removeTagging(tag);
					break;
				}
			}
		}
		else if (s.getKind() == Suggestion.TYPE) {
			KiWiResource subject = s.getInstance().getSourceResource();
			KiWiUriResource property = tripleStore.createUriResource(Constants.NS_RDF + "type");
			KiWiResource object = s.getClassifier().getResource();
			tripleStore.removeTriple(subject, property, object);
		}
	}
	
	/**
	 * Recreates all suggestion for a content item.
	 * It deletes all existing suggestions.
	 * Then runs the instance extractors
	 * For each instance extractors a classifier is run to classify the instances.
	 * Then all information extraction plugins are run. 
	 * 
	 * Then all the examples are reconstructed from existing annotation. 
	 * @param item
	 */
	private void _extractInformation(ContentItem item) {
		SystemStatus status = new SystemStatus("initialization of instances for Information Extraction on CI " + item.getTitle());
		status.setProgress(0);
		statusService.addSystemStatus(status);
				
		try{
			Query q;
			
			// delete examples only from the current textcontent (keeping examples of older versions of the same content item)
			q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.deleteExamplesByInstanceSourceTextContent");
			q.setParameter("contentid", item.getTextContent().getId());
			q.executeUpdate();
	
//			entityManager.flush();
			
			// Delete all suggestions about this content item (including old textcontent no point of having suggestions of old versions
			// TODO: do it better, perhaps using native SQL... the problem is those ManyToMany tables...
			q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listUnusedSuggestionsByInstanceSourceResource");
			q.setParameter("resourceid", item.getResource().getId());
			Collection<kiwi.model.informationextraction.Suggestion> suggestions = q.getResultList();
			for (kiwi.model.informationextraction.Suggestion suggestion : suggestions) {
				suggestion.getResources().clear();
				suggestion.getTypes().clear();
				suggestion.getRoles().clear();
				suggestion = entityManager.merge(suggestion);
				entityManager.remove(suggestion);
			}
			
			/*q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.deleteSuggestionsByInstanceSourceItem");
			q.setParameter("itemid", item.getId());
			q.executeUpdate();*/
			
//			entityManager.flush();
			
			// delete instances only from the current textcontent (keep instances of old textcontent, which may be used by examples)
			q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.deleteUnusedInstancesBySourceResource");
			q.setParameter("resourceid", item.getResource().getId());
			q.executeUpdate();
			
//			entityManager.flush();
			// TODO: delete instances of the old content of the same content item that are not used as examples.
			
			// process the content by a GATE pipeline, first create a GATE representation of the text content:
			gate.Document gateDoc = kiwiGateService.textContentToGateDocument(item.getResource(), item.getTextContent());
			
			if (gateDoc == null) {
				log.info("Unable to preprocess document");
				return;
			}
			
			// run the pipeline for the language of the content item. (should ignore not supported languages)
			kiwiGateService.run(gateDoc, item.getLanguage().getLanguage());
			
			// run all instance extractors and all relevant classifiers...
			/*
			Collection<InstanceExtractorEntity> instanceExtractors = getInstanceExtractors ();
			
			for (InstanceExtractorEntity ie : instanceExtractors) {
				
				Collection<ClassifierEntity> classifiers = getClassifiersForType(em, ie);
				
				for (InstanceEntity i : ie.extractInstances(item, gateDoc)) {
					em.persist(i);
					
					log.info("created instance #0 #1, #2", i.getSourceContentItem().getTitle(), i.getId(), i.getContext());
					
					for (ClassifierEntity classifier : classifiers) {
						kiwi.model.informationextraction.Suggestion suggestion = classifySuggestion(classifier, i);
						if (suggestion != null) {
							em.persist(suggestion);
						}
					}
				}
			}*/
			
			FeatureStoreService featureStoreService = (FeatureStoreService)Component.getInstance("kiwi.informationextraction.featureStoreService");
			
			// Now, run all the extractlets.
			for (String extractletName : getExtractletNames()) {
				Extractlet extractlet = (Extractlet)Component.getInstance(extractletName);
				
				// There are two options for extractlet, 
				// 1. use instance entities, in that case, generate them and use the extract(instances) method...
				//   (we want to store instance entities even if no suggestions are generated, e.g. for future classifiers.)
				Collection<InstanceEntity> instances = extractlet.extractInstances(item.getResource(), item.getTextContent(), gateDoc, item.getLanguage());
				
				suggestions = extractlet.extract(instances);
				for (Suggestion suggestion : suggestions) {
					suggestion.setExtractletName(extractletName);
					entityManager.persist(suggestion);
					
					log.info("suggestion: #0 #1 #2 #3", suggestion.getId(), suggestion.getKind(), suggestion.getExtractletName(), suggestion.getScore());
				}
				
				for (InstanceEntity instance : instances) {
					entityManager.persist(instance);
					
					// index the features, use the extractlet name as the "category" in the feature store.
					assert (extractletName == instance.getExtractletName());
					
					if (instance.getFeatures() != null) {
						featureStoreService.put(extractletName, instance.getId(), instance.getFeatures());
					}
				}
				
//				entityManager.flush();
				
				// 2. it doesn't use instance entities, or it does, but creates them directly... 
				suggestions = extractlet.extract(item.getResource(), item.getTextContent(), gateDoc, item.getLanguage());
				for (kiwi.model.informationextraction.Suggestion suggestion : suggestions) {
					// force the name, so we can be sure which component to blame for the suggestion.
					suggestion.setExtractletName(extractletName);
					entityManager.persist(suggestion);
					
					if (suggestion.getInstance() != null) {
						// persist the instance, also, index its features in the feature store 
						InstanceEntity instanceEntity = suggestion.getInstance();
						entityManager.persist(instanceEntity);
						
						// index the features, use the extractlet name as the "category" in the feature store.
						assert (extractletName == instanceEntity.getExtractletName());
						
						if (instanceEntity.getFeatures() != null) {
							featureStoreService.put(extractletName, instanceEntity.getId(), instanceEntity.getFeatures());
						}
					}
				}
				
//				entityManager.flush();
				
				featureStoreService.flush(extractletName);
				
				/*for (kiwi.model.informationextraction.Example example : extractlet.initExamples())*/
				// TODO: init examples (suggestions) .
			}
			
			
			
			/*
			// recreate fragment based examples
			FragmentService fragmentService = (FragmentService)Component.getInstance("fragmentService");
			TaggingService taggingService = (TaggingService)Component.getInstance("taggingService");
			Collection<FragmentFacade> ffs = fragmentService.getContentItemFragments(item, FragmentFacade.class);
			
			for (FragmentFacade ff : ffs) {
				Collection<Tag> tags = taggingService.getTags(ff.getDelegate());
				for (Tag t : tags) {
					Collection<ClassifierEntity> classifiers = getClassifiersForResource(t.getTaggingResource().getResource());
					for (ClassifierEntity classifier : classifiers) {
						initFragmentTagExample(ff, classifier, Example.POSITIVE);
					}
				}
			}
			
			// recreate content based examples
			Collection<Tag> tags = taggingService.getTags(item);
			for (Tag t : tags) {
				Collection<ClassifierEntity> classifiers = getClassifiersForResource(t.getTaggingResource().getResource());
				for (ClassifierEntity classifier : classifiers) {
					initContentTagExample(item, classifier, Example.POSITIVE);
				}
			}*/
			
			// entityManager.flush();
			
		}
		finally {
			statusService.removeSystemStatus(status);
		}
	}

	@Override
	public void extractInformationAsync(final ContentItem item) {
		enqueueTask (new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				service._extractInformation(item);
			}
		});
	}
	
	@Override
	public void extractInformation(final ContentItem item) {
		InformationExtractionTask task = (new InformationExtractionTask() {
			@Override
			public void run(InformationExtractionServiceImpl service) {
				service._extractInformation(item);
			}
		});
		
		task.start(this);
	}
	
	
	
	@Override
	public void removeExample(Example example) {
		entityManager.remove(example);
//		entityManager.flush();
	}
}
