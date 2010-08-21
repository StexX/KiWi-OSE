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

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.schema.IndexSchema;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import kiwi.api.config.ConfigurationService;
import kiwi.api.informationextraction.TermRecognitionServiceLocal;

@Name("kiwi.informationextraction.termRecognitionService")
@Scope(ScopeType.APPLICATION)
public class TermRecognitionServiceImpl implements TermRecognitionServiceLocal {
	
	@In
	ConfigurationService configurationService;
	
	@Logger
	private Log log;
	
	SolrConfig config;
	IndexSchema schema;
	Analyzer analyzer;
	IndexReader indexReader;
	
	@Create
	public void create()  {
		
		try {
			// Get the solr index
			String solrDataDir = System.getProperty("solr.data.dir");
			if (solrDataDir == null) {
				log.info("solr.data.dir property undefined!");
				return;
			}
			
			String solrHomeDir = System.getProperty("solr.solr.home");
			if (solrHomeDir == null) {
				log.info("solr.solr.home property undefined!");
				return;
			}
			
			config = new SolrConfig("config", new FileInputStream(new File(new File(solrHomeDir, "conf"), "solrconfig.xml")));
			
			schema = new IndexSchema(config, "schema", new FileInputStream(new File(new File(solrHomeDir, "conf"), "schema.xml")));
			
			analyzer = schema.getAnalyzer();
					
			indexReader = IndexReader.open(FSDirectory.getDirectory(new File(solrDataDir, "index")), true);
		}
		catch(Exception x) {
			log.info("error initializing TermRecognitionService", x);
		}
	}
	
	@Override
	public float scoreTerm(String term, float tf) {
		
		if (analyzer == null || indexReader == null) {
			return 1.0f;
		}
		
		try {
			TokenStream stream = analyzer.tokenStream("content", new StringReader(term));
			
			// Approximate tf.idf as tf . (arithmetic mean of term idfs), + 1 smooth
			int ntokens = 1;
			float aidf = 0.0f;
			float D = (float)indexReader.numDocs();
			for(;;) {
				Token token = stream.next();
				if (token == null) break;
				
				int tendocs;
				TermEnum te = indexReader.terms(new org.apache.lucene.index.Term("content", token.term()));
				tendocs = te.docFreq();
				log.debug("term #0 df = #1", token.term(), tendocs);
				
				aidf += Math.log(D / (float)tendocs);
				
				++ ntokens;
			}
			
			float idf = aidf / (float)ntokens;
				
			log.debug("term #0 score #1", term, tf * idf);
			
			return tf * idf;
		}
		catch(Exception x) {
			log.info("Exception in computing term score.", x);
		}
		
		return 1.0f;
	}

	@Override
	public List<TermGroupScore> scoreTerms(Collection<? extends Term> terms) {
		
		List<TermGroupScore> ret = new LinkedList<TermGroupScore>();
		
		// normalize(term)^-1, that is, map normalized labels to their original terms.
		Map<String, List<Term> > normterms = new HashMap<String, List<Term> > ();
		// One particular member of normalize(term)^-1 that is suitable for representing the whole set... 
		Map<String, String> normlabel = new HashMap<String, String> ();
		
		// token document frequencies, retrieved from the solr index.
		Map<String, Integer> tokenDocFreq = new HashMap<String, Integer> (); 
		
		for (Term term : terms) {
			try {
				TokenStream stream = analyzer.tokenStream("content", new StringReader(term.getLabel()));
				Token token = new Token();
				
				// construct normalized string 
				StringBuilder norm = new StringBuilder();
				for (;;) {
					token.clear();
					token = stream.next(token);
					if (token == null) {
						break;
					}
					
					String tokenTerm = token.term();
					
					if (norm.length() > 0) {
						norm.append(' ');
					}
					
					// Retrieve the token document frequencies while we are at it... 
					if (!tokenDocFreq.containsKey(tokenTerm)) {
						TermEnum te = indexReader.terms(new org.apache.lucene.index.Term("content", tokenTerm));
						
						int tendocs = 0; 
						if (te.term().text().equals(tokenTerm)) {
							tendocs = te.docFreq();
						}
						
						tokenDocFreq.put(tokenTerm, tendocs);
					}
					
					norm.append(token.term());
				}
				
				String normString = norm.toString();
				// group the terms by their normalized strings...
				List<Term> thisnormterms;
				if (!normterms.containsKey(normString)) {
					thisnormterms = new LinkedList<Term> ();
					thisnormterms.add(term);
					normterms.put(normString, thisnormterms);
					
					normlabel.put(normString, term.getLabel());
				}
				else {
					normterms.get(normString).add(term);
					
					// simple heuristic for the label, choose the shortest one of all the alternatives...
					if (term.getLabel().length() < normlabel.get(normString).length()) {
						normlabel.put(normString, term.getLabel());
					}
				}
			}
			catch(Exception x) {
				log.info("Exception in computing term score.", x);
			}
		}
		
		// number of documents, a kind of normalizing factor for idf values... 
		float D = (float)indexReader.numDocs();
		
		for (Map.Entry<String, List<Term>> normterm : normterms.entrySet()) {
			// compute the tf * idf (interpolated idf)
			
			// term frequency is exactly that...
			float tf = normterm.getValue().size();
			
			// interpolated idf is computed as a arithmetic average of token idfs... (= geometric average of token inverse frequencies * some constant)
			final String normString = normterm.getKey();
			
			float aidf = 0;
			int ntokens = 1; // add one, so we don't deal with divide by zero on empty terms...
			for (String tokenString : normString.split(" ")) {
				float tendocs;
				
				if (tokenDocFreq.containsKey(tokenString)) {
					tendocs = tokenDocFreq.get(tokenString) + 1.0f;
				}
				else {
					tendocs = 1.0f;
				}
				
				aidf += Math.log(D / (float)tendocs);
				
				++ntokens;
			}
			
			float iidf = aidf / (float)ntokens;
			
			final float score = tf * iidf;
			final String label = normlabel.get(normString);
			final List<Term> thisterms = normterm.getValue(); 
			
			ret.add(new TermGroupScore() {
				@Override
				public String getLabel() {
					return label;
				}

				@Override
				public float getScore() {
					return score;
				}

				@Override
				public List<Term> getTerms() {
					return thisterms;
				}

				@Override
				public String getNormalizedLabel() {
					// TODO Auto-generated method stub
					return normString;
				}				
			});
		}
		
		return ret;
	}
}