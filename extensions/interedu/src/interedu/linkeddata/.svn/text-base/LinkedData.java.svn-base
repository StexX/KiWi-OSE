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
package interedu.linkeddata;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import nu.xom.XPathContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.log.Log;

/**
 * @author Christoph Wieser
 * 
 */

@Scope(ScopeType.CONVERSATION)
@Name("linkeddata")
public class LinkedData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String search = "";
	@DataModel
	private ArrayList<String> result = null;

	@Logger
	Log log;

	public void find() {
		log.info("Searching for '" + search + "' in the Linked Data Cloud!");

		ArrayList<String> concepts = getConcepts(search);

		result = getLabels(concepts);

	}

	// Get labels for concepts
	private ArrayList<String> getLabels(ArrayList<String> concepts) {
		ArrayList<String> labels = new ArrayList<String>();
		for (String concept : concepts) {
			String sparqlLabels = "select distinct ?RESULT where  { { <"
					+ concept + "> skos:prefLabel ?RESULT } UNION { <" + concept
					+ "> skos:altLabel ?RESULT } UNION { <" + concept
					+ "> skos:hiddenLabel ?RESULT } }";
			labels.addAll(getResults(getDocument(sparqlLabels), "//sr:literal"));
		}
		return labels;
	}

	// Find appropriate concepts for search
	private ArrayList<String> getConcepts(String search) {
		String sparqlQueryConcpets = "select distinct ?SUBJECT, ?QUERY where {"
				+ "{ ?SUBJECT skos:prefLabel ?QUERY FILTER regex(?QUERY, '^"
				+ search
				+ "$', 'i' ) ."
				+ "} UNION { ?SUBJECT skos:altLabel ?QUERY FILTER regex(?QUERY, '^"
				+ search
				+ "$', 'i' ) ."
				+ "} UNION { ?SUBJECT skos:hiddenLabel ?QUERY FILTER regex(?QUERY, '^"
				+ search + "$', 'i' ) .} }";
		ArrayList<String> mainConcepts = getResults(
				getDocument(sparqlQueryConcpets), "//sr:uri");

		// Container for all resulting concepts
		// (mainConcepts are added later to avoid circular evaluation.)
		ArrayList<String> concepts = new ArrayList<String>();

		// Get narrower concepts
		for (String mainConcept : mainConcepts) {
			log.info("Searching for narrower concepts for '" + mainConcept
					+ "' in the Linked Data Cloud!");
			String sparqlNarrowerConcepts = "select distinct ?RESULT where { <"
					+ mainConcept + "> skos:narrower ?RESULT}";
			concepts.addAll(getResults(getDocument(sparqlNarrowerConcepts),
					"//sr:uri"));
		}

		// Get broader concepts
		for (String mainConcept : mainConcepts) {
			log.info("Searching for broader concepts for '" + mainConcept
					+ "' in the Linked Data Cloud!");
			String sparqlBroaderConcepts = "select distinct ?RESULT where { <"
					+ mainConcept + "> skos:broader ?RESULT}";
			concepts.addAll(getResults(getDocument(sparqlBroaderConcepts),
					"//sr:uri"));
		}

		// Add mainConcepts to resulting concepts
		concepts.addAll(mainConcepts);
		
		return concepts;
	}

	// Extract List from XML-Document via XPath
	private ArrayList<String> getResults(Document document, String xPath) {
		if (document != null) {
			XPathContext xPathContext = new XPathContext();
			xPathContext.addNamespace("sr",
					"http://www.w3.org/2005/sparql-results#");

			Nodes nodes = document.query(xPath, xPathContext);

			result = new ArrayList<String>();
			for (int i = 0; i < nodes.size(); i++) {
				Node node = nodes.get(i);
				result.add(node.getValue());
			}
			return result;
		}
		return null;
	}

	private Document getDocument(String sparqlQuery) {
		URL url = getURL(sparqlQuery);

		// Get Document and parse it
		Document document = null;
		try {
			document = new Builder().build(url.openStream());
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	private URL getURL(String sparqlQuery) {
		URL url = null;
		try {
			String searchParam = "";
			searchParam += "default-graph-uri="
					+ URLEncoder.encode("http://dbpedia.org", "UTF8");
			searchParam += "&should-sponge=";
			searchParam += "&query=" + URLEncoder.encode(sparqlQuery, "UTF8");
			searchParam += "&format="
					+ URLEncoder.encode("application/sparql-results+xml",
							"UTF8");
			searchParam += "&debug=on";
			url = new URL("http://dbpedia.org/sparql/service.wsdl?"
					+ searchParam);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public ArrayList<String> getResult() {
		return result;
	}

	public void setResult(ArrayList<String> result) {
		this.result = result;
	}

}
