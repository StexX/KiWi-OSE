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
package kiwi.action.webservice.thesaurusManagement;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import kiwi.api.config.ConfigurationService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.ontology.SKOSService;
import kiwi.model.content.ContentItem;
import kiwi.model.ontology.SKOSConcept;
import kiwi.util.json.JSONWriter;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

@Path("/thesaurus")
@Name("thesaurus")
public class ThesaurusResource {

	@In(create = true)
	private SKOSService skosService;
	
	@In
	private EntityManager entityManager;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private ConfigurationService configurationService;
	
	@Logger
	private static Log log;

	@GET
	@Path("{id}")
	@Produces("text/html")
	public Response getHTML(@PathParam("id") String id) {
		//set returnpath
		URI uri = null;
		try {
			uri = new URI( configurationService.getConfiguration("RELATIVE_PATH") + "/thesaurus/html/" + id);
		} catch (URISyntaxException e) {
			log.error("some failure while creating URI for redirect");
		}
		return Response.seeOther(uri).build();
	}
	
	@GET
	@Path("html/{id}")
	@Produces("application/rdf+xml")
	public Response getHTML2(@PathParam("id") String id) {
		Long id_long = Long.valueOf(id);
		ContentItem ci = entityManager.find(ContentItem.class, id_long);
		log.info("select contentItem #0", ci.getTitle());
		return Response.ok(ci).build();
	}
	
	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response getJSON(@PathParam("id") String id) {
		//set returnpath
		URI uri = null;
		try {
			uri = new URI( configurationService.getConfiguration("RELATIVE_PATH") + "/thesaurus/json");
		} catch (URISyntaxException e) {
			log.error("some failure while creating URI for redirect");
		}
		return Response.seeOther(uri).build();
	}
	
	@GET
	@Path("json/{id}")
	@Produces("application/x-javascript")
	public Response getJSON2(@PathParam("id") String id, @QueryParam("callback") String callback) {
		if( callback == null ) {
			callback = "";
		}
		Long id_long = Long.valueOf(id);
		ContentItem ci = entityManager.find(ContentItem.class, id_long);
		
		SKOSConcept i = kiwiEntityManager.createFacade(ci,
				SKOSConcept.class);
		
		StringBuffer b = new StringBuffer();
		b.append(callback);
		b.append("(");
		b.append(JSONWriter.out(i));
		b.append(")");
		return Response.ok(b.toString()).build();
	}
	
	/**
	 * this method returns a 303 HTTP redirect to the resource 'All SKOS TopConcepts' in JSON format.
	 * This is necessary to make it browsable by LinkedData browsers
	 * @return 303 HTTP redirect
	 */
	@GET
	@Path("top")
	@Produces("application/json")
	public Response getTopConceptsInJSON() {
		//set returnpath
		URI uri = null;
		try {
			uri = new URI( configurationService.getConfiguration("RELATIVE_PATH") + "/thesaurus/top/json");
		} catch (URISyntaxException e) {
			log.error("some failure while creating URI for redirect");
		}
		return Response.seeOther(uri).build();
	}
	
	/**
	 * This method returns all SKOS TopConcepts in JSON format.
	 * A callback parameter specifies the name of the JSON Object. The HTTP Content Type
	 * has to be 'application/x-javascript' to be parsable by jQuery
	 * @return resource 'All SKOS TopConcepts' in JSON format.
	 */
	@GET
	@Path("top/json")
	@Produces("application/x-javascript")
	public Response getTopConceptsInJSON2(@QueryParam("callback") String callback) {
		log.info("request for JSON with callback #0", callback);
		if( callback == null ) {
			callback = "";
		}
		LinkedList<SKOSConcept> sk = (LinkedList<SKOSConcept>) skosService.getTopConcepts();
		StringBuffer s = new StringBuffer();
		s.append(callback+"({\n\"title\" : \"Concepts\",\n\"concepts\" : [\n");
		for( SKOSConcept c : sk ) {
			s.append(JSONWriter.out(c));
			s.append(",");
		}
		s.append("]\n})");
		log.info("Message:\n#0", s.toString());
		return Response.ok(s.toString()).build();
	}
	
	/**
	 * this method returns a 303 HTTP redirect to the resource 'All SKOS TopConcepts' in HTML format.
	 * This is necessary to make it browsable by LinkedData browsers
	 * @return 303 HTTP redirect
	 */
	@GET
	@Path("top")
	@Produces("application/rdf+xml")
	public Response getTopConceptsInHTML() {				
		//set returnpath
		URI uri = null;
		try {
			uri = new URI( configurationService.getConfiguration("RELATIVE_PATH") + "/thesaurus/top/html");
		} catch (URISyntaxException e) {
			log.error("some failure while creating URI for redirect");
		}
		return Response.seeOther(uri).build();
	}
	
	/**
	 * @return resource 'All SKOS TopConcepts' in HTML format
	 */
	@GET
	@Path("top/html")
	@Produces("application/rdf+xml")
	public Response getTopConceptsInHTML2() {				
		LinkedList<SKOSConcept> sk = (LinkedList<SKOSConcept>) skosService.getTopConcepts();
		LinkedList<ContentItem> s = transform(sk);
		return Response.ok(s).build();
	}
	
	@GET
	@Produces("application/json")
	@Path("sub/{id}")
	public Response getSubConceptsInJSON(@PathParam("id") String id) {
		//set returnpath
		URI uri = null;
		try {
			uri = new URI( configurationService.getConfiguration("RELATIVE_PATH") + "/thesaurus/sub/json/" +id);
		} catch (URISyntaxException e) {
			log.error("some failure while creating URI for redirect");
		}
		return Response.seeOther(uri).build();
	}

	@GET
	@Produces("application/x-javascript")
	@Path("sub/json/{id}")
	public Response getSubConceptsInJSON2(@PathParam("id") String id, @QueryParam("callback") String callback) {		
		
		if( callback == null ) {
			callback = "";
		}
		
		Long id_long = Long.valueOf(id);
		ContentItem ci = entityManager.find(ContentItem.class, id_long);
		
		SKOSConcept i = kiwiEntityManager.createFacade(ci,
				SKOSConcept.class);
				
		HashSet<SKOSConcept> sc = i.getNarrower();
		LinkedList<SKOSConcept> scl = new LinkedList<SKOSConcept>(sc);		
		
		StringBuffer s = new StringBuffer();
		s.append(callback+"({\n\"title\" : \"Sub Concepts of "+i.getPreferredLabel()+"\",\n\"concepts\" : [\n");
		for( SKOSConcept c : scl ) {
			s.append(JSONWriter.out(c));
			s.append(",");
		}
		s.append("]\n})");
		log.info("Message:\n#0", s.toString());
		
		return Response.ok(s.toString()).build();
	}
	
	@GET
	@Produces("text/html")
	@Path("sub/{id}")
	public Response getSubConceptsInHTML(@PathParam("id") String id) {
		//set returnpath
		URI uri = null;
		try {
			uri = new URI( configurationService.getConfiguration("RELATIVE_PATH") + "/thesaurus/sub/html/" + id);
		} catch (URISyntaxException e) {
			log.error("some failure while creating URI for redirect");
		}
		return Response.seeOther(uri).build();
	}
	
	@GET
	@Produces("application/rdf+xml")
	@Path("sub/html/{id}")
	public Response getSubConceptsInHTML2(@PathParam("id") String id) {		

		Long id_long = Long.valueOf(id);
		ContentItem ci = entityManager.find(ContentItem.class, id_long);
		
		SKOSConcept i = kiwiEntityManager.createFacade(ci,
				SKOSConcept.class);
				
		HashSet<SKOSConcept> sc = i.getNarrower();
		LinkedList<SKOSConcept> scl = new LinkedList<SKOSConcept>(sc);		
		LinkedList<ContentItem> llc = transform(scl);
		
		return Response.ok(llc).build();
	}
	
	/**
	 * transforms a List of SKOSConcepts to a List of ContentItems
	 * @param sc
	 * @return
	 */
	private LinkedList<ContentItem> transform(List<SKOSConcept> sc){
		LinkedList<ContentItem> skContentItems = new LinkedList<ContentItem>();
		for(SKOSConcept s: sc){
			skContentItems.add(s.getDelegate());
		}
		return skContentItems;
	}
}