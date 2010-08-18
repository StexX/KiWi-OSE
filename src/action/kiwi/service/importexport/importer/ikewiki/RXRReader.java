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
package kiwi.service.importexport.importer.ikewiki;

import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.Text;
import nu.xom.XPathContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.Component;

public class RXRReader implements Iterable<KiWiTriple> {
	/**
	 * Iterates over all RDF triples in an RDF/RXR document and returns them as {@link WikiLink}s.
	 * Invalid triples are skipped; a warning message is logged in any such case.
	 * 
	 * @author christoph.lange
	 */
	public static class TripleIterator implements Iterator<KiWiTriple> {
	    private static Log log = LogFactory.getLog(RXRReader.TripleIterator.class);

	    private Document document;
	    private XPathContext xPathContext;
	    private TripleStore tripleStore;
	    
	    private Nodes triples;
	    /**
	     * the current index of iteration
	     */
	    private int i;
	    /**
	     * the next triple to be returned by {@link #next()}
	     */
	    private KiWiTriple next;
	    
	    public TripleIterator(Document doc, XPathContext xPathContext, TripleStore sb) {
	    	this.document = doc;
	    	this.xPathContext = xPathContext;
	    	this.tripleStore = sb;
	    	
			triples = document.query("//rxr:triple",xPathContext);
			log.info("processing "+triples.size()+" triples");
			i = 0;
			next = null;
	    }

		public boolean hasNext() {
			while (next == null) {
				/* go ahead until a valid triple is found, or until the list of nodes ends. */
				
				if (i >= triples.size()) {
					return false;
				}
				
				next = get(i);
				i++;
			}
			
			return true;
			
			/* side effects:
			 * If there is a valid next triple t (probably behind skipped invalid once), next == t holds.
			 * If there is no more valid triple, next == null holds. 
			 */
		}

		/**
		 * Extracts one component (subject, predicate or object) from an RXR triple.
		 * If there is not exactly one subject/predicate/object, a warning message is logged, and <code>null</code> returned 
		 * 
		 * @param triple the XML node representing the RDF/RXR triple
		 * @param component the desired component, as string
		 * @return the desired component as a XOM node, or <code>null</code> if there is not exactly one 
		 */
		private Element queryTripleComponent(Node triple, String component) {
			Nodes nodes = triple.query("rxr:" + component, xPathContext);
			if (nodes.size() != 1) {
				log.warn(String.format("triple %1$s contains no %2$s or more than one %2$s", triple.toXML(), component));
				return null;
			} else {
				return (Element) nodes.get(0);
			}
		}
		
		/**
		 * Gets the i-th triple, or null if that triple is invalid. This method must not be called with a triple index that is out of bounds.
		 * 
		 * @return
		 */
		private KiWiTriple get(int i) {
			Node current = triples.get(i);
			
			KiWiResource    subject = null;
			KiWiUriResource predicate = null;
			KiWiNode object = null;
			
			/* determine the subject */
			Element n_subject = queryTripleComponent(current, "subject");
			if (n_subject != null) {
				if(n_subject.getAttribute("uri") != null) {
					// a URI resource
					subject = tripleStore.createUriResource(n_subject.getAttributeValue("uri"));
				} else if(n_subject.getAttribute("blank") != null) {
					// anonymous resource
					subject = tripleStore.createAnonResource(n_subject.getAttributeValue("blank"));
				} else {
					log.warn("triple "+current.toXML()+" contains invalid subject");
				}
			}

			/* determine the predicate */
			Element n_predicate = queryTripleComponent(current, "predicate");
			if (n_predicate != null) {
				if(n_predicate.getAttribute("uri") != null) {
					// a URI resource
					predicate = tripleStore.createUriResource(n_predicate.getAttributeValue("uri"));
				} else {
					log.warn("triple "+current.toXML()+" contains invalid predicate");
				}
			}

			/* determine the object */
			Element n_object = queryTripleComponent(current, "object");
			if (n_object != null) {
				if(n_object.getAttribute("uri") != null) {
					// a URI resource
					object = tripleStore.createUriResource(n_object.getAttributeValue("uri"));
				} else if(n_object.getAttribute("blank") != null) {
					// anonymous resource
					object = tripleStore.createAnonResource(n_object.getAttributeValue("blank"));
				} else {
					// literal
					String content = "";
					for(int j = 0; j<n_object.getChildCount(); j++) {
						if(n_object.getChild(j) instanceof Text) {
							Text txt = (Text)n_object.getChild(j);
							content += txt.getValue();
						}
					}
					String lang = n_object.getAttributeValue("lang", "http://www.w3.org/XML/1998/namespace");
					String datatype = n_object.getAttributeValue("datatype");
					if(lang != null && ! lang.equals("")) {
						object = tripleStore.createLiteral(content, new Locale(lang), tripleStore.createUriResource(Constants.NS_XSD+"string"));
					} else if (datatype != null && ! datatype.equals("")) {
						object = tripleStore.createLiteral(content, new Locale(lang), tripleStore.createUriResource(datatype));
					} else {
						object = tripleStore.createLiteral(content);							
					}
				}
			}
			
			if(subject != null && predicate != null && object != null) {
				return tripleStore.createTriple(subject, predicate, object);
			} else {
				return null;
			}
		}
		
		public KiWiTriple next() {
			if (next == null) {
				/*
				 * initialize the next triple, if hasNext() has not explicitly been called before.
				 * In a good programming style this does not happen. The Java syntax for (elem: Iterable) calls
				 * hasNext() as well.
				 */
				hasNext();
			}
			
			KiWiTriple result = next;
			next = null;
			if (result != null) {
				return result;
			} else {
				throw new NoSuchElementException("no more valid triples in RXR document"); 
			}
		}
			
		public void remove() {
			throw new UnsupportedOperationException("removing wiki links is not supported");
		}
	}
	
//    private static Log log = LogFactory.getLog(RXRReader.class);

    private Document document;
    private XPathContext xPathContext;
	
	public RXRReader(Document doc) {
		this.document = doc;
		xPathContext = new XPathContext();
		xPathContext.addNamespace("rxr", "http://ilrt.org/discovery/2004/03/rxr/");
	}

	public Iterator<KiWiTriple> iterator() {
		return new TripleIterator(document, xPathContext, (TripleStore)Component.getInstance("tripleStore"));
	}
}
