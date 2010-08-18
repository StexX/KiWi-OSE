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
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.store.FSDirectory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import kiwi.api.config.ConfigurationService;
import kiwi.api.informationextraction.FeatureStoreServiceLocal;

/**
 * Implement feature store as a lucene index.
 * 
 * @author Marek Schmidt
 *
 */
@Name("kiwi.informationextraction.featureStoreService")
@Scope(ScopeType.APPLICATION)
public class FeatureStoreServiceImpl implements FeatureStoreServiceLocal {

	private Map<String, IndexWriter> indexes = new HashMap<String, IndexWriter>();
	
	@Logger
	private Log log;
	
	@In
	private ConfigurationService configurationService;
	
	@Override
	public Collection<String> get(String index, Long id) {
		
		Collection<String> ret = new LinkedList<String>();
		
		IndexWriter writer = getIndexWriter(index);
		if (writer != null) {
			Term term = new Term("id", id.toString());
			IndexReader reader;
			try {
				reader = writer.getReader();
				TermDocs docs = reader.termDocs(term);
				
				// should be exactly one document
				while(docs.next()) {
					Document doc = reader.document(docs.doc());
					
					
					
					for (String f : doc.get("features").split(" ")) {
						ret.add(f);
					}
				}
				
			} catch (IOException e) {
				log.error("error reading feature store", e);
			}
			
		}
		
		return ret;
	}

	@Override
	public void put(String index, Long id, Collection<String> features) {
		IndexWriter writer = getIndexWriter(index);
		if (writer != null) {
					
			Document doc = new Document();
			doc.add(new Field("id", id.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			// TODO: Now, joining the collection just to split it again by lucene tokenizer is stupid...
			StringBuilder sb = new StringBuilder();
			for (String f : features) {
				if (sb.length() > 0) {
					sb.append(' ');
				}
				sb.append(f);
			}
			
			doc.add(new Field("features", sb.toString(), Field.Store.YES, Field.Index.ANALYZED));
			
			try {
				writer.deleteDocuments(new Term("id", id.toString()));
				writer.addDocument(doc);
			} catch (Exception e) {
				log.error("error indexing features", e);
			}
		}
	}
	
	@Destroy
	public void destroy() {
		for (IndexWriter writer : indexes.values()) {
			try {
				writer.commit();
				writer.close(true);
			} catch (Exception x) {
				log.error("Error closing index", x);
			}
		}
	}

	@Override
	public void remove(String index, Long id) {
		IndexWriter writer = getIndexWriter(index);
		if (writer != null) {
			try {
				writer.deleteDocuments(new Term("id", id.toString()));
			} catch (Exception x) {
				log.error("Error deleting document #0", x, id);
			}
		}
	}

	@Override
	public Collection<Long> getIds(String index, String feature) {
		// TODO: implement as a generator
		Collection<Long> ret = new LinkedList<Long>();
		
		IndexWriter writer = indexes.get(index);
		if (writer != null) {
			Term term = new Term("features", feature);
			try {
				IndexReader reader = writer.getReader();
				
				TermDocs docs = reader.termDocs(term);
				while(docs.next()) {
					int docId = docs.doc();
					ret.add(Long.parseLong(reader.document(docId).get("id")));
				}
			} catch (Exception e) {
				log.error("Error reading index", e);
			}
		}
		
		return ret;
	}

	private IndexWriter getIndexWriter(String index) {
		IndexWriter ret = indexes.get(index);
		if (ret == null) {
			// TODO: this does not have really anything to do with gate, so it
			// should be some other configuration option. (or gate.home renamed
			// to "ie.home" or something... 
			
			String gateHome = configurationService.getConfiguration(
			"gate.home").getStringValue();
		
			if (gateHome == null) {
				log.info("gate.home not set");
				return null;
			}
			
			File indexesHome = new File(gateHome, "featureStore");
			File indexDir = new File(indexesHome, index);
			
			boolean create = false;
			if (!indexDir.exists()) {
				create = true;
				indexDir.mkdirs();
			}
			
			try {
				ret = new IndexWriter(FSDirectory.getDirectory(indexDir), new WhitespaceAnalyzer(), create, IndexWriter.MaxFieldLength.LIMITED);
				indexes.put(index, ret);
			} catch (Exception e) {
				log.error("Error opening index #0", e, indexDir.toString());
				return null;
			}
		}
		
		return ret;
	}

	@Override
	public void flush(String index) {
		IndexWriter writer = getIndexWriter(index);
		if (writer != null) {
			try {
				writer.commit();
			} catch (Exception e) {
				log.error("Error during commit", e);
			}
		}
	}
}
