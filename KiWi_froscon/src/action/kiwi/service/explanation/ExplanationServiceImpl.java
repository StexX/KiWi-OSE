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
package kiwi.service.explanation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import kiwi.api.explanation.ExplanationServiceLocal;
import kiwi.api.explanation.ExplanationServiceRemote;
import kiwi.api.reasoning.reasonmaintenance.ReasonMaintenanceService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.service.reasoning.reasonmaintenance.Justification;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * Explanation service transforms information produced by the reasoner and reason maintenance into the form of explanations.
 * 
 * @author Jakub Kotowski
 *
 */
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
@Name("explanationService")
public class ExplanationServiceImpl implements ExplanationServiceLocal, ExplanationServiceRemote {
	@Logger private Log log;
	@In("reasonMaintenanceService") ReasonMaintenanceService reasonMaintenance;
	private @In TripleStore tripleStore;
	private @In EntityManager entityManager;
	private Map<String, String> prefixDictionary;
	
	private static final String PREFIX_FILE = "kiwi/service/explanation/prefix.cc.all.txt";
	private static final Map<String, String> dictionary = new HashMap<String, String>();
	static {
		dictionary.put("http://www.w3.org/1999/02/22-rdf-syntax-ns#type","is");
		dictionary.put("http://www.w3.org/2000/01/rdf-schema#subClassOf","is a kind of");
	}
	
	private void loadPrefixes() {
		prefixDictionary = new HashMap<String, String>();
		java.net.URL url = Thread.currentThread().getContextClassLoader().getResource(PREFIX_FILE); //ClassLoader.getSystemResource(RULES_FILE);
		
		try {
			File f = new File(url.toURI());
		    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		    String line;
		    while ((line = reader.readLine()) != null) {
		    	line = line.trim();
		    	if (!line.equals("")) { 
		    		String[] parts = line.split("\t");
		    		prefixDictionary.put(parts[1], parts[0]);
		    	}
		    }
		} catch (URISyntaxException e) {
			log.error("Error while loading prefixes from file "+PREFIX_FILE+". The error was: "+e.getMessage());
			return;
		} catch (FileNotFoundException e) {
			log.error("File "+PREFIX_FILE+" not found. The error was: "+e.getMessage());
			return;			
		} catch (IOException e) {
			log.error("Error while reading from file "+PREFIX_FILE+". The error was: "+e.getMessage());
			return;
		}			
	}
	
	public ExplanationServiceImpl() {
		loadPrefixes();
	}
	
	private String getShortName(String uri) throws URISyntaxException {
		if (uri == null)
			throw new IllegalArgumentException("getShortName() does not accept a null argument.");
		
		String name = new URI(uri).getFragment(); //URI-reference = [ absoluteURI | relativeURI ] [ "#" fragment ] http://www.ietf.org/rfc/rfc2396.txt
		if (name == null || name.length() == 0) {
			if ((uri.lastIndexOf('/')+1) == uri.length())
				uri = uri.substring(0,uri.lastIndexOf('/'));
			
			name = uri.substring(uri.lastIndexOf('/')+1);
		}
		return name;
	}
	
	/** Tries shorten the uri using a table of known namespace prefixes.
	 * 
	 * If there is no known prefix for the uri it returns the same result as getShortName().
	 * 
	 * @param uri
	 * @return
	 * @throws URISyntaxException
	 */
	private String getPrefixedName(String uri) throws URISyntaxException {
		String name = getShortName(uri);
		String prefix_uri = uri.substring(0, uri.length() - name.length()); 
		
		if (prefixDictionary.containsKey(prefix_uri)) {
			return prefixDictionary.get(prefix_uri)+":"+name;
		}
		
		String prefix = tripleStore.getNamespacePrefix(prefix_uri);
		if (prefix != null)
			return prefix + ":" + name;
		
		return name;
	}
	
	/** Tries to tokenize strings of characters to words.
	 * 
	 * Works only for camel case now.
	 * 
	 * @param string
	 * @return
	 */
	private String tryToTokenize(String string) {
		if (string == null)
			return null;
		
		if (string.isEmpty())
			return string;
		
		StringBuffer result = new StringBuffer();
    	result.append(Character.toLowerCase(string.charAt(0)));
		
    	for (int i = 1; i < string.length(); i++) {
    		char c = string.charAt(i);
    		if (Character.toUpperCase(c) == c) {
    			result.append(" ").append(Character.toLowerCase(c));
    			continue;
    		}
    		result.append(c);
    	}
    	
    	return result.toString();
	}
	
	/** Tries to translate property uris to something more readable.
	 * 
	 * For example for "http://www.w3.org/2000/01/rdf-schema#subClassOf" it returns "is a kind of".
	 * 
	 * @param propertyUri The URI of the property.
	 * @return The natural language translation.
	 */
	public String translateProperty(String propertyUri) {
		if (dictionary.containsKey(propertyUri))
			return dictionary.get(propertyUri);
		
		String name;
	
		try {
			name = getShortName(propertyUri);
			
			return tryToTokenize(name);
			
		} catch (URISyntaxException e) {
			return propertyUri;
		}		
	}
	
	public List<Justification> explainTriple(Long id) {
		log.info("explainTriple(#0)", id);
		List<Justification> jus = reasonMaintenance.getJustificationFor(id);

		return jus;
	}		
	
	public List<Story> explainTripleTextually(Long id, boolean html) {
		log.info("explainTripleTextually(#0)", id);
		List<Justification> justifs = reasonMaintenance.getJustificationFor(id);
		
		List<Story> stories = new ArrayList<Story>(justifs.size());
		for (Justification justif : justifs) {
			Story story = narrateJustification(justif, html);
			stories.add(story);
			//log.debug("Triple id #0 story: #1", id, story);
		}
		
		return stories;
	}
	
	private Story narrateJustification(Justification justif, boolean html) {
		Story result = new Story();
		
		String justifId = "kiwiidj"+justif.getNeoId();
		result.setJustificationId(justifId);
		
		KiWiTriple fact = justif.getFact();
		StringBuffer buffer = new StringBuffer();
		
		buffer = narrateTriple(fact, html, justifId);		
		result.addLine(buffer.toString(), "because");
		
		for (int i = 0; i < justif.getInTripleIds().length; i++) {
			Long inId = justif.getInTripleIds()[i];
			
			KiWiTriple inTriple = entityManager.find(KiWiTriple.class, inId);
			if (inTriple == null) {
				log.error("Couldn't find triple with id #0. This can be caused by old data in the neo4j database."+
						" When you delete the relational database you have to delete /tmp/kiwi/neo4j too.", inId);
				continue;
			}

			buffer = narrateTriple(inTriple, html, justifId);
			//if (i != justif.getInTripleIds().length - 1)
				result.addLine(buffer.toString(),"and");
			//else
			//	result.addLine(buffer.toString(),"");				
		}
		
		result.addLine("because of rule "+justif.getRuleId(),"");
				
		return result;
	}
	
	public String describeTriple(KiWiTriple triple, boolean html) {
		return narrateTriple(triple, html, "").toString();
	}
	
	/**
	 * 
	 * @param justifId id of the containing justification. Serves to generate unique ids for the triple's span element.
	 * @return
	 */
	private StringBuffer narrateTriple(KiWiTriple triple, boolean html, String justifId) {
		StringBuffer buffer = new StringBuffer();
		
		String subject, property, object;

		subject = html ? 
				"<span title=\""+getPrefixedName(triple.getSubject())+"\">"+getShortName(triple.getSubject())+"</span>"
				:
				getShortName(triple.getSubject());
		
		String propUri = triple.getProperty().getUri();
		if (dictionary.containsKey(propUri)) {
			property = dictionary.get(propUri);
		} else
			property = getShortName(triple.getProperty());
		
		if (html) property = "<span title=\""+getPrefixedName(triple.getProperty())+"\">"+property+"</span>";
		
		object = html ? "<span title=\""+getPrefixedName(triple.getObject())+"\">"+ getShortName(triple.getObject())+"</span>"
				: getShortName(triple.getObject());

		if (html)
			buffer.append("<span id=\"").append(justifId+"kiwiid"+triple.getId()).append("\">");
		
		buffer.append(subject).append(" ").append(property).append(" ").append(object);

		if (html)
			buffer.append("</span>");
		
		return buffer;
	}
	
	private String getPrefixedName(KiWiNode node) {
		if (node.isUriResource()) {
			KiWiUriResource res = (KiWiUriResource) node;
			try {
				return getPrefixedName(res.getUri());
			} catch (Exception e) {
				return getShortName(node);
			}
		}
		return getShortName(node);
	}
	
	private String getShortName(KiWiNode node) {
		String name;
		
		try {
			name = node.getShortName();
		} catch (URISyntaxException e) {
			log.warn("Couldn't get a short name for node id #0: #1", node.getId(), e.getMessage());
			name = node.toString();
		}		
		
		return name;
	}
}



