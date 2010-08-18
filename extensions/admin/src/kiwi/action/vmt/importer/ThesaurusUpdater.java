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

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.importexport.importer.RDFImporter;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.joda.time.DateTime;
import org.openrdf.model.Statement;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.memory.MemoryStore;

import com.sun.syndication.feed.atom.Content;

import antlr.collections.List;


/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@AutoCreate
@Name("thesaurusUpdater")
@Scope(ScopeType.CONVERSATION)
public class ThesaurusUpdater {
    
    @Logger
    private Log log;
    
    private String updateUri;
    
    private Date date;
    
    private RepositoryConnection con;
    
    private String format;
    
    private String url_concept;
    
    private java.util.Set<String> formats;
    
    @Create
    public void init(){
	formats = new HashSet<String>();
	formats.add("n3");
    }

    @In
    private KiWiEntityManager kiwiEntityManager;
    
    @In
    private ContentItemService contentItemService;
    
    public void updateThesaurus() throws Exception {
	
	log.debug("updating thesaurus ...");
	log.debug("update ontology is located at "+updateUri);
	
	if(date != null){
	    DateTime dt = new DateTime(date);
	    log.debug(dt.toLocalDateTime());
	    updateUri = updateUri + "/" + dt.toLocalDateTime();
	    log.debug(updateUri);
	}
	
	Repository myRepository = new SailRepository(new MemoryStore());
	myRepository.initialize();
	con = myRepository.getConnection();
	
	updateUri += "?format="+format;
	
	
	try {
	    log.debug(updateUri);
	    URL u = new URL(updateUri);
	    con.add(u, u.toString(), RDFFormat.TURTLE);
	
	//get all concepts
	GraphQueryResult rs = con.prepareGraphQuery(QueryLanguage.SERQL,"CONSTRUCT * FROM {x} rdf:type {skos:Concept} USING NAMESPACE rdf = <http://www.w3.org/1999/02/22-rdf-syntax-ns#> , skos = <http://www.w3.org/2004/02/skos/core#> ").evaluate();
	
	LinkedList<String> concepts2Import = new LinkedList<String>();
	LinkedList<String> concepts2update = new LinkedList<String>();
	
	while (rs.hasNext()) {
	    	log.debug("--- ");
		Statement st = rs.next();
		log.debug("---"+st.getSubject());
		log.debug(st);
		RepositoryResult<Statement> rs1 = con.getStatements(st.getSubject(), null, null, true);
		
		
		boolean deprecated = false;
		boolean modified = false;
		boolean replacedBy = false;
		boolean created = false;
		
		//java.util.List<Statement> lls = Collections.EMPTY_LIST;
		
		//get all data about the concept
		while (rs1.hasNext()) {	
		    
		    Statement st1 = rs1.next();
		  //  lls.add(st1);
		    
		    if(st1.getPredicate().toString().contains("deprecated")){
			deprecated = true;
		    }
		    if(st1.getPredicate().toString().contains("modified")){
			modified = true;
		    }
		    if(st1.getPredicate().toString().contains("replaced")){
			replacedBy = true;
		    }
		    if(st1.getPredicate().toString().contains("created")){
			created = true;
		    }
  
		    log.debug("S "+st1.getSubject());
		    log.debug("P "+st1.getPredicate());
		    log.debug("O "+st1.getObject());
		    if(st1.getObject().toString().contains("Umwelt")){
			log.debug("umwelt");
		    }
		    
		}
		
		if(created == true){
		    log.debug("create new concept ...");
//		    String y = url_concept + File.separatorChar + lls.get(0).getSubject().toString() + File.separatorChar + format + language;
//		    ContentItem ci = contentItemService.createContentItem(lls.get(0).getSubject().toString());
		    concepts2Import.add(st.getSubject().toString());
		    //importRemoteConcept(st.getSubject().toString());		    
		}		
		else if(deprecated == true && replacedBy == false){
		    log.debug("delete concept ...");
		    
		    ContentItem ci = contentItemService.getContentItemByUri(st.getSubject().toString());
		    log.debug(ci != null?ci.getTitle():"ci is null");
		    if(ci != null){
			kiwiEntityManager.remove(ci);
		    }
		    // delete concept
		}
		else if((modified == true) && (deprecated == false) && (replacedBy == false)){
		    log.debug("reload concept ...");
		    concepts2update.add(st.getSubject().toString());
		    // lade das konzept neu
		}
		else if((modified == true) && (replacedBy == true)){
		    log.debug("concept replaced by ... replace tags");
		    //concept replaced by
		}
	}
	
	for(String conceptUri: concepts2Import){
	    
	    
	   String broaderURI = importRemoteConcept(conceptUri);
	   
	   ContentItem broader = contentItemService.getContentItemByUri(broaderURI);
	   ContentItem ci = contentItemService.getContentItemByUriIncludeDeleted(conceptUri);
	   
	   log.debug("broaderuri "+broaderURI+"-");
	 
	   if(broader != null){
	       
	   log.debug("broaderuri "+broaderURI);
	   log.debug(ci.getTitle());
	   log.debug(broader.getTitle());
	   
	   SKOSConcept concept = kiwiEntityManager.createFacade(ci, SKOSConcept.class);
	   SKOSConcept broaderConcept = kiwiEntityManager.createFacade(broader, SKOSConcept.class);
	   
	   HashSet<SKOSConcept> skl = broaderConcept.getNarrower();
	   skl.add(concept);
	   broaderConcept.setNarrower(skl);
	   
	   kiwiEntityManager.persist(broaderConcept);
	   

	   }
	   else{
	       log.debug("Broader element is null ...");
	       FacesMessages.instance().add("Error: Broader element can not be found ... Some elements may not be imported correctly");
	   }
	}
	for(String conceptUri: concepts2update){
	    updateRemoteConcept(conceptUri);
	}
	
	} catch (Exception e) {
	    FacesMessages.instance().add("Something wrong happened. Most likely the urls to the webservices are not valid.");
	}
    }
    
    //deletes old properties
    //imports a Concept with a given uri
    public void updateRemoteConcept(String uri) throws Exception{
		
		// collect subjects in a HashSet
		//HashSet<Resource> subjects = new HashSet<Resource>();
		String y = url_concept +"?conceptURI="+uri+"&format="+format;
		log.debug(y);
		//String y = url_concept + File.separatorChar + uri.trim() + File.separatorChar + format + language;
		try{
        		URL url1 = new URL(y);
        		con.clear();
        		con.add(url1, url1.toString(), RDFFormat.TURTLE);
        		
        		RDFImporter    ddfImporter = (RDFImporter)    Component.getInstance("kiwi.service.importer.rdf");
        		ddfImporter.importDataSesame(con, null, null, null, null);
        		}
		catch(Exception e){
		    	FacesMessages.instance().add("Something wrong happened. Most likely the urls to the webservices are not valid.");
		}
    }
    
    //imports a Concept with a given uri
    public String importRemoteConcept(String uri) throws Exception{
		
		// collect subjects in a HashSet
		//HashSet<Resource> subjects = new HashSet<Resource>();
		String y = url_concept +"?conceptURI="+uri+"&format="+format;
		log.debug(y);
		//String y = url_concept + File.separatorChar + uri.trim() + File.separatorChar + format + language;
		URL url1 = new URL(y);
		con.clear();
		con.add(url1, url1.toString(), RDFFormat.TURTLE);
		
		String broaderURI = getBroader(con);

		RDFImporter    ddfImporter = (RDFImporter)    Component.getInstance("kiwi.service.importer.rdf");
		//ddfImporter.addDataSesame(con, null, null, null, null);
		ddfImporter.importDataSesame(con, null, null, null, null);
		
		return broaderURI;
    }
    
    public String getBroader(RepositoryConnection myCon) throws Exception{
	for(RepositoryResult<Statement> r = con.getStatements(null, null, null, false) ; r.hasNext(); ) {
	    Statement stmt = r.next();
	    if(stmt.getPredicate().toString().contains("broader")){
		return stmt.getObject().toString();
	    }
	}
	return null;
    }

  

    public String getUpdateUri() {
        return updateUri;
    }

    public void setUpdateUri(String updateUri) {
        this.updateUri = updateUri;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public String getFormat() {
        return format;
    }



    public void setFormat(String format) {
        this.format = format;
    }


    public String getUrl_concept() {
        return url_concept;
    }


    public void setUrl_concept(String urlConcept) {
        url_concept = urlConcept;
    }
    
    public java.util.Set<String> getFormats() {	
        return formats;
    }
}


