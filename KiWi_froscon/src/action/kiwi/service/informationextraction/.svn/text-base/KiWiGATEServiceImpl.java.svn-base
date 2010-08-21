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

import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.ProcessingResource;
import gate.creole.ANNIEConstants;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.creole.gazetteer.Lookup;
import gate.util.GateException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.ejb.Remove;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.config.ConfigurationService;
import kiwi.api.event.KiWiEvents;
import kiwi.api.informationextraction.KiWiGATEServiceLocal;
import kiwi.api.informationextraction.LabelService;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.Label;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.util.KiWiXomUtils;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;

/**
 * @author Marek Schmidt
 * 
 */
@Name("kiwi.informationextraction.gateService")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class KiWiGATEServiceImpl implements KiWiGATEServiceLocal {

	@Logger
	private Log log;

	@In
	private ConfigurationService configurationService;

	private SerialAnalyserController controller;
	private SerialAnalyserController deController;
	
	private gate.creole.gazetteer.Gazetteer contentItemTitlesGazetteer;
	
	// Make this service robust to GATE not existing. Let the service just do nothing.
	private boolean enabled = false;
	
	@In
	private EntityManager entityManager;
	
	@In(create=true, value="kiwi.informationextraction.labelService")
	private LabelService labelService;
	
	private boolean isTreeTaggerInstalled = false;
	private boolean isDurmInstalled = false;
	private boolean isMunpexInstalled = false;
	
	private void initDe() {
		if (!isTreeTaggerInstalled) {
			log.info("Not attempting to initialize Deutsch, TreeTagger not installed.");
			return;
		}
		
		if (!isMunpexInstalled) {
			log.info("Not attempting to initialize Deutsch, MuNPEx-0.2 not installed.");
			return;
		}
		
		if (!isDurmInstalled) {
			log.info("Not attempting to initialize Deutsch, DurmLemmatizer not installed.");
			return;
		}
		
		try {
			deController = (SerialAnalyserController) Factory.createResource(
					"gate.creole.SerialAnalyserController", Factory
							.newFeatureMap(), Factory.newFeatureMap(),
					"Controller_" + Gate.genSym());
			
			FeatureMap params;
			// FeatureMap features;
			ProcessingResource pr;
			
			params = Factory.newFeatureMap();
			pr = (ProcessingResource) Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			pr = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.SimpleTokeniser", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			params.put("grammarURL", new File(new File(new File(new File(Gate.getPluginsHome(), "german"), "resources"), "tokeniser"), "postprocessGerman.jape").toURI().toURL());
			pr = (ProcessingResource) Factory.createResource("gate.creole.Transducer", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			pr = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			params.put("encoding", "ISO-8859-1");
			params.put("failOnUnmappableChar", false);
			params.put("treeTaggerBinary", new File(new File(new File(Gate.getPluginsHome(), "TreeTagger"), "resources"), "tree-tagger-german-gate").toURI().toURL());
			pr = (ProcessingResource) Factory.createResource("gate.treetagger.TreeTagger", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			params.put("encoding", "UTF-8");
			params.put("caseSensitive", true);
			params.put("listsURL", new File(new File(new File(new File(Gate.getPluginsHome(), "german"), "resources"), "gazetteer"), "lists.def").toURI().toURL());
			pr = (ProcessingResource) Factory.createResource("gate.creole.gazetteer.DefaultGazetteer", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			params.put("encoding", "UTF-8");
			params.put("caseSensitive", true);
			params.put("listsURL", new File(new File(new File(new File(Gate.getPluginsHome(), "german"), "resources"), "gazetteer"), "lists_token_internal.def").toURI().toURL());
			pr = (ProcessingResource) Factory.createResource("gate.creole.gazetteer.DefaultGazetteer", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			params.put("grammarURL", new File(new File(new File(new File(Gate.getPluginsHome(), "german"), "resources"), "grammar"), "main.jape").toURI().toURL());
			pr = (ProcessingResource) Factory.createResource("gate.creole.Transducer", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			pr = (ProcessingResource) Factory.createResource("gate.creole.orthomatcher.OrthoMatcher", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			params.put("grammarURL", new File(new File(Gate.getPluginsHome(), "MuNPEx-0.2"), "de-np_main.jape").toURI().toURL());
			pr = (ProcessingResource) Factory.createResource("gate.creole.Transducer", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			params.put("grammarURL", new File(new File(new File(Gate.getPluginsHome(), "DurmLemmatizer-1.0"), "DeLem"), "de_morph_main.jape").toURI().toURL());
			pr = (ProcessingResource) Factory.createResource("gate.creole.Transducer", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			params.put("probabilityFiles", new File(new File(Gate.getPluginsHome(), "DurmLemmatizer-1.0"), "CaseProbs").toURI().toURL());
			pr = (ProcessingResource) Factory.createResource("ipd.creole.casetagger.CaseTagger", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			pr = (ProcessingResource) Factory.createResource("ipd.creole.number.Number", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			pr = (ProcessingResource) Factory.createResource("ipd.creole.demorphanalyzer.GermanMorphologicalAnalyzer", params);
			deController.add(pr);
			
			params = Factory.newFeatureMap();
			params.put("lexiconPath", new File(new File(new File(Gate.getPluginsHome(), "DurmLemmatizer-1.0"), "DE-Lexicon"), "delexicon.txt").toURI().toURL());
			pr = (ProcessingResource) Factory.createResource("ipd.creole.germanlemmatizer.GermanLemmatizer", params);
			deController.add(pr);
			
		} catch (ResourceInstantiationException e) {
			log.info("Failed to initialize GATE", e);
		} catch (MalformedURLException e) {
			log.info("Failed to initialize GATE", e);
		}		
	}
	
	@Create
	public void create() {
		
	}
	
	@Override
	public void init() {
		try {
			
			enabled = false;
			
			if (configurationService.getConfiguration(
					"gate.home").getStringValue() == null) {
				log.info("gate.home not set");
				return;
			}
			
			if (Gate.getGateHome() != null) {
				log.info("gate home != null, assuming GATE initialized");
			}
			else {
				log.info("gate home is null, assuming GATE not yet initialized");
			
				Gate.setGateHome(new File(configurationService.getConfiguration(
					"gate.home").getStringValue()));
				Gate.setPluginsHome(new File(configurationService.getConfiguration(
					"gate.home").getStringValue(), "plugins"));

				Gate.init();
				Gate.getCreoleRegister().registerDirectories(
					new File(Gate.getPluginsHome(), "ANNIE").toURI().toURL());
			
				Gate.getCreoleRegister().registerDirectories(
					new File(Gate.getPluginsHome(), "NP_Chunking").toURI().toURL());
			
				Gate.getCreoleRegister().registerDirectories(
					new File(Gate.getPluginsHome(), "Stemmer").toURI().toURL());
				
				File treeTaggerBin = new File(new File(Gate.getPluginsHome(), "TreeTagger"), "TreeTaggerBin");
				if (treeTaggerBin.isDirectory()) {
					Gate.getCreoleRegister().registerDirectories(
						new File(Gate.getPluginsHome(), "TreeTagger").toURI().toURL());
					
					isTreeTaggerInstalled = true;
				}
					
				File durmPlugin = new File(Gate.getPluginsHome(), "DurmLemmatizer-1.0");
				if (durmPlugin.isDirectory()) {
					Gate.getCreoleRegister().registerDirectories(
							new File(new File(durmPlugin, "CaseTagger"), "build").toURI().toURL());
					
					Gate.getCreoleRegister().registerDirectories(
							new File(new File(durmPlugin, "GermanLemmatizer"), "build").toURI().toURL());
				
					Gate.getCreoleRegister().registerDirectories(
							new File(new File(durmPlugin, "GermanMorphologicalAnalyzer"), "build").toURI().toURL());
				
					Gate.getCreoleRegister().registerDirectories(
							new File(new File(durmPlugin, "Number"), "build").toURI().toURL());
				
					isDurmInstalled = true;
				}
				
				File munpexPlugin = new File(Gate.getPluginsHome(), "MuNPEx-0.2");
				if (munpexPlugin.isDirectory()) {
					isMunpexInstalled = true;
					// munpex is not a CREOLE plugin, just a bunch of JAPE rules, so not registering creole.
				}
			}

			
			// Init the baseline processing pipeline
			// TODO: store it as GATE .application file and load it at runtime
			
			controller = (SerialAnalyserController) Factory.createResource(
					"gate.creole.SerialAnalyserController", Factory
							.newFeatureMap(), Factory.newFeatureMap(),
					"Controller_" + Gate.genSym());
			
			for (String element : ANNIEConstants.PR_NAMES) {
				FeatureMap params = Factory.newFeatureMap(); ProcessingResource pr =
					 (ProcessingResource) Factory.createResource(element, params);
				controller.add(pr);
			}
			 
			FeatureMap params = Factory.newFeatureMap(); 
			ProcessingResource pr = (ProcessingResource) Factory.createResource("mark.chunking.GATEWrapper", params);
			controller.add(pr);
			
			params = Factory.newFeatureMap(); 
			pr = (ProcessingResource) Factory.createResource("stemmer.SnowballStemmer", params);
			controller.add(pr);
			
			params.put(gate.creole.gazetteer.DefaultGazetteer.DEF_GAZ_CASE_SENSITIVE_PARAMETER_NAME, false);
			contentItemTitlesGazetteer = (gate.creole.gazetteer.Gazetteer) Factory.createResource("gate.creole.gazetteer.DefaultGazetteer", params);

			// Sebastian: implemented a more efficient listing of content item title and uri than 
			// contentItemService.getContentItems(); see ContentItem for named query
			/*Query q = entityManager.createNamedQuery("kiwiGATEService.listContentItems");
			for (Object[] item : (List<Object[]>)q.getResultList()) {
				
				if(item[0] != null && item[1] != null) {
					String uri = (String)item[1];
					Lookup l = new Lookup("terms", "ontology", uri, "english");
					
					// log.info("adding lookup #0 #1", ((String)item[0]).toLowerCase(), uri);
	
					contentItemTitlesGazetteer.add(((String)item[0]).toLowerCase(), l);
				} else {
					log.debug("content item with uri #1 and title #0 was not added to gazetteer",item[0],item[1]);
				}
			}*/
			
			for (Object[] item : labelService.getLabels()) {
				Long resourceId = (Long)item[1];
				// We represent resources by their ids, shorter and faster to retrieve then kiwiids or uris.
				Lookup l = new Lookup("terms", "ontology", Long.toHexString(resourceId), "english");
				contentItemTitlesGazetteer.add((String)item[0], l);
			}

			controller.add(contentItemTitlesGazetteer);
			
			// Custom JAPE rules for term recognition
			params = Factory.newFeatureMap(); 
			params.put("grammarURL", (new File(Gate.getGateHome(), "atr_en.jape")).toURI().toURL());
			pr = (ProcessingResource) Factory.createResource("gate.creole.Transducer", params);
			controller.add(pr);
			
			
			initDe();
				
			enabled = true;
			 
		} catch (GateException e) {
			log.info("Failed to initialize GATE", e);
		} catch (IOException e) {
			log.info("Failed to initialize GATE", e);
		} catch(gate.util.GateRuntimeException e ) {
			log.info("Failed to initialize GATE", e);
		}
		log.info("GATE successfully initialized");
	}

	@Remove
	public void shutdown() {
	}

	@Destroy
	public void destroy() {
	}
	
	public Document stringToGateDocument (String plaintext) {
		
		if (!enabled) return null;
		
		try {			
			
			log.debug("stringToGateDocument, plaintext: #0", plaintext);
			
			FeatureMap params = Factory.newFeatureMap();
			params.put(Document.DOCUMENT_PRESERVE_CONTENT_PARAMETER_NAME, true);
			params.put(Document.DOCUMENT_REPOSITIONING_PARAMETER_NAME, true);
			params.put(Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/plain");
			params.put(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, plaintext);
			params.put(Document.DOCUMENT_MARKUP_AWARE_PARAMETER_NAME, true);
			
			Document doc = (Document) Factory.createResource(
					"gate.corpora.DocumentImpl", params);
			
			// have a easy way to access the plain text from extractlets... 
			doc.getFeatures().put(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, plaintext);

			try {
				doc.setSourceUrl(new URL("http://example.com"));
			} catch (MalformedURLException e) {
			}
			
			return doc;
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			log.info("error creating GATE document", e);
			return null;
		}
	}

	public Document textContentToGateDocument(KiWiResource context,
			TextContent tc) {		
		
		String plaintext = "";
		if (tc != null) {
			nu.xom.Document xom = tc.copyXmlDocument();
			plaintext = KiWiXomUtils.xom2plain(xom);
		}
		
		Document doc = stringToGateDocument(plaintext);
		if (doc != null) {
			doc.getFeatures().put("kiwiResource", context);
		}

		return doc;
	}

	// TODO:
	public TextContent gateDocumentToTextContent(KiWiResource context,
			Document doc) {
		return null;
	}
	
	private void run(SerialAnalyserController controller, Document doc) {
		
		try {
			Corpus corpus;
			corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");

			corpus.add(doc);

			// tell the pipeline about the corpus and run it
			controller.setCorpus(corpus);
			controller.execute();
		} catch(gate.creole.ExecutionException e) {
			// workaround a GATE bug of unwilling to accept that empty set is a set too...
			if ("No sentences or tokens to process!\nPlease run a sentence splitter and tokeniser first!".equals(e.getMessage())) {
				log.info("likely processing an empty document");
			}
			else {
				log.info("error in GATE execution", e);
			}
		} catch (ResourceInstantiationException e) {
			log.info("error in creating GATE corpus", e);
		} catch(Exception e) {
			log.info("error in GATE execution", e);
		}
		
		/*AnnotationSet as = doc.getAnnotations();
		for (Annotation a : as) {
			log.info("annotation: #0", a.toString());
		}*/
	}

	@Deprecated
	public void run(Document doc) {
		
		if (!enabled) return;

		try {
			Corpus corpus;
			corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");

			corpus.add(doc);

			// tell the pipeline about the corpus and run it
			controller.setCorpus(corpus);
			controller.execute();
		} catch (ResourceInstantiationException e) {
			log.info("error in creating GATE corpus", e);
		} catch (ExecutionException e) {
			log.info("error in GATE execution", e);
		}
	}
	
	public void run(Document doc, String lang) {
		
		if (!enabled) return;
		
		if ("en".equals(lang)) {
			run(controller, doc);
		}
		if ("de".equals(lang) && deController != null) {
			run(deController, doc);
		}
	}
	
	
	@Observer(value=KiWiEvents.TITLE_UPDATED)
	public void onTitleUpdated(ContentItem ci) {
		Events.instance().raiseAsynchronousEvent(KiWiEvents.TITLE_UPDATED+"Async", ci);
	}
	
	@Asynchronous
	@Observer(value=KiWiEvents.TITLE_UPDATED+"Async")
	public void addEntity(ContentItem ci) {
		
		if (!enabled) return;
		
		for(Label label : labelService.getLabels(ci)) {
			
			Long id = ci.getResource().getId();
			Lookup l = new Lookup("terms", "ontology", Long.toHexString(id), "english");
			
			contentItemTitlesGazetteer.add(label.getString(), l);
		}
	}
}
