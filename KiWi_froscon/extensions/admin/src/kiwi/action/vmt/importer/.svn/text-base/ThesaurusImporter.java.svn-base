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
package kiwi.action.vmt.importer;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.importexport.importer.RDFImporter;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.memory.MemoryStore;

/**
 * @author Rolf Sint
 *
 */

@AutoCreate
@Name("thesaurusImporter")
@Scope(ScopeType.CONVERSATION)
public class ThesaurusImporter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;

	private RepositoryConnection con;
	
	private String url_thesaurus;
	private String url_concept;
	private String concept_schema;
	private String topconcept;
	private String format;
	//private String language;
	
	final static int MAXDEPTH = 5;

	
//	@Create
//	public void init(){
//	    	url_thesaurus = "http://pptry.punkt.at/cubalibre/project/urn:kiwi/thesaurus";
//		url_concept = "http://pptry.punkt.at/cubalibre/project/urn:kiwi/concept";
//		concept_schema = "/urn:uuid:1D6439AB-915E-0001-B1FE-1CD015A716D0/";
//		topconcept = "topConcept";
//		format = "n3";
//		language = "en";
//	}
	
	public void importThesaurus() throws Exception {
		
	    	url_thesaurus = (url_thesaurus == null)?"":url_thesaurus;
	    	url_concept = (url_concept == null)?"":url_concept;
	    	concept_schema = (concept_schema == null)?"":concept_schema;
	    	topconcept = (topconcept == null)?"":topconcept;
	    
	    	format = "&format=" + format;
	   // 	language = "&language="+language;
	    	
		//final String url = url_thesaurus + concept_schema + topconcept + format;
		final String url = url_thesaurus + topconcept + "?" + "thesaurusURI="+concept_schema+format;
		log.debug(url);

		Repository myRepository = new SailRepository(new MemoryStore());
		myRepository.initialize();
		con = myRepository.getConnection();
		
		//conceptsImorted is all
		//conceptsToExpand is prestage from conceptsToImports
		List<SConcept> conceptsImorted =  new LinkedList<SConcept>();
		List<SConcept> conceptsToExpand = getTopConcepts(url);
		log.debug("Topconcept size: #0",conceptsToExpand.size());
		
		conceptsImorted = conceptsToExpand;
	
		List <SConcept> conceptsToImport = new LinkedList<SConcept>();
		
		int d=1;
		while (d <= MAXDEPTH){
		log.debug("-");
			
			for (SConcept concept : conceptsToExpand) {
				log.debug("y");
				List<SConcept> n = getNarrowers(concept.uri);
				
				conceptsToImport.addAll(n);
			}
			conceptsToExpand = conceptsToImport;
			
			conceptsImorted.addAll(conceptsToExpand);
			
			conceptsToImport.clear();
			d++;
		}
				
		for (SConcept ci : conceptsImorted) {
			buildConcept(ci.uri.replace("\"", ""));
		}
		
	}
	

	public void buildConcept(String uri) throws Exception{
		
		// collect subjects in a HashSet
		//HashSet<Resource> subjects = new HashSet<Resource>();
		
	    	//String y = url_concept + File.separatorChar + uri.trim() + File.separatorChar + format + language;
	    	String y = url_concept +"?conceptURI="+uri+format;
	    //String y = url_concept + File.separatorChar + uri.trim() + File.separatorChar + "?format=n3";// + language;
		log.debug(y);
		URL url1 = new URL(y);
		con.clear();
		con.add(url1, url1.toString(), RDFFormat.TURTLE);
		
		RDFImporter    ddfImporter = (RDFImporter)    Component.getInstance("kiwi.service.importer.rdf");
		ddfImporter.importDataSesame(con, null, null, null, null);
	
	}
	
	
	public List<SConcept> getTopConcepts(String thesaurusURI){
		List<SConcept> topConcepts = new LinkedList<SConcept>();
		try {
			URL u = new URL(thesaurusURI);
			con.add(u, u.toString(), RDFFormat.TURTLE);

			// query all skos concepts
			GraphQueryResult graphResult = con
					.prepareGraphQuery(
							QueryLanguage.SERQL,
							"CONSTRUCT * FROM {x} rdf:type {skos:Concept} USING NAMESPACE rdf = <http://www.w3.org/1999/02/22-rdf-syntax-ns#> , skos = <http://www.w3.org/2004/02/skos/core#> ")
					.evaluate();

			// iterate over all concepts
			while (graphResult.hasNext()) {				
				Statement st = graphResult.next();
				topConcepts.add(new SConcept(0,st.getSubject().toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Release the connection.
			// method.releaseConnection();
		}
		return topConcepts;

	}
	
	
	public List<SConcept> getNarrowers(String uri) throws Exception{
		//String y = url_concept + File.separatorChar + uri.trim() + File.separatorChar + format + language;
	    	String y = url_concept +"?conceptURI="+uri+format;
	
		log.debug(y);
		URL url1 = new URL(y);
		con.clear();
		con.add(url1, url1.toString(), RDFFormat.TURTLE);
	
		org.openrdf.model.URI narrower = new URIImpl("http://www.w3.org/2004/02/skos/core#narrower");
		RepositoryResult<Statement> narStatements = con.getStatements(null, narrower, null, true);

		List<SConcept> l = new LinkedList<SConcept>();
		// für narrowers weiter printConcept aufrufen
		while (narStatements.hasNext()) {
			Statement st1 = narStatements.next();
			l.add(new SConcept(0,st1.getObject().toString().trim()));
		}
		return l;
	}


	public String getUrl_thesaurus() {
	    return url_thesaurus;
	}


	public void setUrl_thesaurus(String urlThesaurus) {
	    url_thesaurus = urlThesaurus;
	}


	public String getUrl_concept() {
	    return url_concept;
	}


	public void setUrl_concept(String urlConcept) {
	    url_concept = urlConcept;
	}


	public String getConcept_schema() {
	    return concept_schema;
	}


	public void setConcept_schema(String conceptSchema) {
	    concept_schema = conceptSchema;
	}


	public String getTopconcept() {
	    return topconcept;
	}


	public void setTopconcept(String topconcept) {
	    this.topconcept = topconcept;
	}


	public String getFormat() {
	    return format;
	}


	public void setFormat(String format) {
	    this.format = format;
	}


//	public String getLanguage() {
//	    return language;
//	}


//	public void setLanguage(String language) {
//	    this.language = language;
//	}
}

class SConcept{
	
	String uri;

	SConcept(int deep, String uri){
		this.uri = uri;
		
	}	
}
