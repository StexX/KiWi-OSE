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
package kiwi.service.search;

import java.io.File;
import java.io.IOException;

import javax.ejb.Stateless;
import javax.transaction.SystemException;

import kiwi.api.config.ConfigurationService;
import kiwi.api.search.SemanticIndexingServiceLocal;
import kiwi.api.search.SemanticIndexingServiceRemote;
import kiwi.api.system.StatusService;
import kiwi.model.status.SystemStatus;

import org.apache.commons.io.FileUtils;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;

import pitt.search.semanticvectors.DocVectors;
import pitt.search.semanticvectors.Flags;
import pitt.search.semanticvectors.IncrementalDocVectors;
import pitt.search.semanticvectors.TermVectorsFromLucene;
import pitt.search.semanticvectors.VectorStore;
import pitt.search.semanticvectors.VectorStoreWriter;

/**
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwi.core.semanticIndexingService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class SemanticIndexingServiceImpl implements SemanticIndexingServiceLocal, SemanticIndexingServiceRemote {


	static int seedLength = 20;
	static int minFreq = 10;
	static int nonAlphabet = 0;
	static int trainingCycles = 1;
	static boolean docsIncremental = false;

	@Logger
	Log log;
	
	@In
	ConfigurationService configurationService;
	
	/**
	 * Recreate the semantic index from the SOLR-created lucene index. Makes use of the lucene 
	 * field semvector defined in solr-home/conf/schema.xml.
	 * <p>
	 * The lucene index is located using System.getProperty("solr.data.dir") under the subdirectory
	 * "index/". The resulting term and document vectors should be written to the path 
	 * configurationService.getWorkDir() + File.SEPARATOR + "semvector".
	 */
	@Override
	@Asynchronous
//	@Transactional	
	public void reIndex() {
		
		try {
			Transaction.instance().setTransactionTimeout(60000);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StatusService statusService = (StatusService) Component.getInstance("kiwi.core.statusService");

		
		SystemStatus status = new SystemStatus("semantic indexer");
		statusService.addSystemStatus(status);
		status.setMessage("rebuilding semantic index");
		
		
		String dataDir = configurationService.getConfiguration("kiwi.solr.home").getStringValue() + File.separator + "data" + File.separator;
		
		String semVectorDir = configurationService.getConfiguration("kiwi.semanticvectors").getStringValue() + File.separator + "data" + File.separator;
		
		String solrIndex = dataDir + "index" + File.separator;
		
		String svIndex   = dataDir + "semvector_temp" + File.separator;
		
		File luceneDir = new File(solrIndex);
		
		try {
			// test if index exists
			if(luceneDir.isDirectory() && luceneDir.listFiles().length > 0) {
		
				status.setProgress(0);
				
				// we copy the SOLR index from the original directory over to a temporary directory to
				// avoid locking problems so that SOLR does not get troubles...
				
				// create/delete temporary index directory
				File svDir = new File(svIndex);
				if(!svDir.exists()) {
					svDir.mkdir();
				}
				for(File file : svDir.listFiles()) {
					file.delete();
				}
				
				// copy original index directory to temporary directory
				FileUtils.copyDirectory(luceneDir, svDir);
				
				// delete Linux lock file we copied with the directory copy
				for(File file : svDir.listFiles()) {
					if(file.getName().equalsIgnoreCase("write.lock")) {
						file.delete();
					}
				}
				
				status.setProgress(33);
				
				// TODO(widdows): Make fieldToIndex configurable.
				String[] fieldsToIndex = { "semvector" };
				log.info("Seedlength = " + Flags.seedlength);
				log.info("Dimension = " + Flags.dimension);
				log.info("Minimum frequency = " + Flags.minfrequency);
				log.info("Number non-alphabet characters = "
						+ Flags.maxnonalphabetchars);
				String termFile = semVectorDir + "semanticvectors"+File.separator+"termvectors.bin";
				String docFile = semVectorDir + "semanticvectors"+File.separator+"docvectors.bin";
	
				TermVectorsFromLucene vecStore = new TermVectorsFromLucene(
						svIndex, Flags.seedlength, Flags.minfrequency,
						Flags.maxnonalphabetchars, null, fieldsToIndex);
	
				status.setProgress(66);
				
				// Create doc vectors and write vectors to disk.
				if (docsIncremental) {
					VectorStoreWriter vecWriter = new VectorStoreWriter();
					log.info("Writing term vectors to " + termFile);
					vecWriter.WriteVectors(termFile, vecStore);
					IncrementalDocVectors idocVectors = new IncrementalDocVectors(
							vecStore, svIndex, fieldsToIndex,  docFile );
				} else {
					DocVectors docVectors = new DocVectors(vecStore);
					for (int i = 1; i < trainingCycles; ++i) {
						log.info("\nRetraining with learned document vectors ...");
						vecStore = new TermVectorsFromLucene(svIndex,
								Flags.seedlength, Flags.minfrequency,
								Flags.maxnonalphabetchars, docVectors,
								fieldsToIndex);
						docVectors = new DocVectors(vecStore);
					}
					// At end of training, convert document vectors from ID keys to
					// pathname keys.
					VectorStore writeableDocVectors = docVectors
							.makeWriteableVectorStore();
	
					if (trainingCycles > 1) {
						termFile = "termvectors" + trainingCycles + ".bin";
						docFile = "docvectors" + trainingCycles + ".bin";
					}
					VectorStoreWriter vecWriter = new VectorStoreWriter();
					log.info("Writing term vectors to " + termFile);
					vecWriter.WriteVectors(termFile, vecStore);
					log.info("Writing doc vectors to " + docFile);
					vecWriter.WriteVectors(docFile, writeableDocVectors);
				}
			}
		} catch (IOException e) {
			log.error("An I/O error occurred while building the semantic index.",e);
		} catch(Exception ex) {
			log.error("An unknown error occurred while building the semantic index.",ex);
		} finally {
			status.setProgress(100);
			statusService.removeSystemStatus(status);
		}
	}

}
