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

package kiwi.service.render.renderlet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.service.render.savelet.RdfaSavelet;
import kiwi.util.MD5;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * 
 * Renders the metadata to the content item HTML view as RDFa
 * 
 * - Lists all properties as <span property="..." content=""/>
 * - Understands <div about="..." rel="..." typeof="..."/> as a component CI related to the CI "above".
 * - All the outgoing triples 
 * 	 - if the object is linked to, add the "rel" attribute to the link
 *   - if the object is not linked to, add the "rel" and "resource" to an empty span
 * - All the incoming triples as "rev" and "resource" attributes to an empty span
 *   
 *   TODO: tags (CommonTag), fragments
 * 
 * @author Marek Schmidt
 * 
 */
@Stateless
@Name("kiwi.service.render.renderlet.HtmlRdfaRenderlet")
@AutoCreate
public class HtmlRdfaRenderlet implements XOMRenderlet {

	@In
	TripleStore tripleStore;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private ConfigurationService configurationService;

	@Logger
	Log log;
	
	private String generateCurie (KiWiResource property, Map<String, String> prefix2namespace) {
		String namespacePrefix = property.getNamespacePrefix();
		String curie;
		
		if (property.isUriResource()) {
			if (namespacePrefix == null) {
				/*TODO: convert URI to some form of a CURIE with temporary prefix*/
				log.info("URI with no defined prefix not yet supported. Property #0", ((KiWiUriResource)property).getUri());
				/* putting URI instead of CURIE is probably better then nothing for now... ? */
				curie = ((KiWiUriResource)property).getUri();
			}
			else {
				String localName = ((KiWiUriResource)property).getLocalName();
				String namespaceUri = tripleStore.getNamespaceUri(namespacePrefix);
				
				/* There is a "kiwi"-namespace-prefix-eating monster in RenderJSFWebService
				 * (due to some hack regarding KIWI-540 which I don't understand... 
				 * you'd probably want to remove this hack once you fix the original problem correctly) */ 
				
				if ("kiwi".equals(namespacePrefix)) {
					namespacePrefix = "otherkiwi";
				}
				
				curie = namespacePrefix + ":" + localName;
				
				if (namespaceUri == null) {
					/* The specified namespace prefix is not known in the triplestore. */
					log.debug("namespace prefix #0 not found in the triple store!", namespacePrefix);
					return curie;
				}
				
				prefix2namespace.put(namespacePrefix, namespaceUri);
			}
		}
		else {
			/* TODO: Not sure if this is the proper form for a CURIE for non-uri resource */
			curie = "[" + property.getKiwiIdentifier() + "]";
		}
		
		return curie;
	}
	
	/* System properties, that should not be displayed. This list originally copied from MetadataAction */
	static Set<String> disabledProperties = new HashSet<String> (Arrays.asList(new String[] {
			Constants.NS_KIWI_CORE+"hasTextContent",
			Constants.NS_KIWI_CORE+"id"}));
	
	public void applyRecursive (KiWiResource origContext, KiWiResource context, Node n, Map<String, String> prefix2namespace, Map<String, Element> kiwiid2a, Set<String> triples) {
		
		if (n instanceof Element) {
			Element e = (Element)n;
			
			/* Context for all the sub-elements. (may be set by an 'about' element */
			KiWiResource nextContext = context;
			
			/* Internal links. Just store references to them and process them later (to match the target with properties) */
			if ("a".equals(e.getLocalName()) && e.getAttributeValue("kind", Constants.NS_KIWI_HTML) != null) {
				String targetKiwiId = e.getAttributeValue("target", Constants.NS_KIWI_HTML).trim();
				kiwiid2a.put(context.getKiwiIdentifier() + " " + targetKiwiId, e);
			}
			
			/* Object predicates (aka RDFa links) */
			if ("span".equals(e.getLocalName()) && e.getAttributeValue("resource") != null) {
				// translate span to a
				Element a = new Element("a", Constants.NS_XHTML);
				e.getParent().replaceChild(e, a);
				
				while(e.getChildCount() > 0) {
					Node child = e.getChild(0);
					child.detach();
					a.appendChild(child);
				}
				
				// support incomplete RDFa link (with no rel)
				String relKiwiId = "";
				if (e.getAttributeValue("rel") != null) {
					relKiwiId = e.getAttributeValue("rel").trim();
				}
				
				String resourceKiwiId = e.getAttributeValue("resource").trim();
				
				ContentItem relCi = contentItemService.getContentItemByKiwiId(relKiwiId);
				ContentItem ci = contentItemService.getContentItemByKiwiId(resourceKiwiId);
				
				if (ci != null) {	
					if (relCi != null) {
						a.addAttribute(new Attribute("rel", generateCurie(relCi.getResource(), prefix2namespace)));
					}
					else {
						// client checks existence of the attributes to determine the kind of annotation...
						a.addAttribute(new Attribute("rel", ""));
					}
					
					a.addAttribute(new Attribute("resource", generateCurie(ci.getResource(), prefix2namespace)));
					
					if(ci != null && ci.getResource() != null) {
						if(ci.getResource().isUriResource()) {
							String uri = ((KiWiUriResource)ci.getResource()).getUri();
							a.addAttribute(new Attribute("href", configurationService.getBaseUri()+"/home.seam?uri="+uri));
						} else {
							String kiwiid = ci.getResource().getKiwiIdentifier();
							a.addAttribute(new Attribute("href", configurationService.getBaseUri()+"/home.seam?kiwiid="+kiwiid));
						}
						
						// check if other content item is still empty and mark link as new if so
						if(ci.getTextContent() == null) {
							a.addAttribute(new Attribute("class", "new"));						
						}
						
					} else {
						a.addAttribute(new Attribute("href", configurationService.getBaseUri()+"/home.seam?title="+resourceKiwiId));
						a.addAttribute(new Attribute("class", "new"));
					}
						
					if (relCi != null) {
						triples.add(nextContext.getKiwiIdentifier() + " " + relCi.getKiwiIdentifier() + " " + ci.getKiwiIdentifier());
					}
					
					String typeKiwiId = e.getAttributeValue("typeof");
					if (typeKiwiId != null && !"".equals(typeKiwiId)) {
						ContentItem typeCi = contentItemService.getContentItemByKiwiId(typeKiwiId);
						if (typeCi != null) {
							a.addAttribute(new Attribute("typeof", generateCurie(typeCi.getResource(), prefix2namespace)));
						}
					}
					else {
						// client checks existence of the attributes to determine the kind of annotation...
						a.addAttribute(new Attribute("typeof", ""));
					}
				}
				
				// replacement of span to a element is finished.
				e = a;
			}
			
			/* Properties */
			if (e.getAttributeValue("property") != null) {
				String propertyKiwiId = e.getAttributeValue("property").trim();
				ContentItem propertyCi = contentItemService.getContentItemByKiwiId(propertyKiwiId);
				if (propertyCi == null) {
					log.debug("applyRecursive property #0 does not exist!", propertyKiwiId);
				}
				else {
					e.addAttribute(new Attribute("property", generateCurie(propertyCi.getResource(), prefix2namespace)));
					
					String propertyContent = RdfaSavelet.extractPropertyContent (e);					
					triples.add(nextContext.getKiwiIdentifier() + " " + propertyCi.getKiwiIdentifier() + " " + MD5.md5sum(propertyContent));
				}
			}
			
			/* About element. Translate the kiwiid to actual resource, replace the kiwiid with a CURIE */
			ContentItem aboutCi = null;
			if (e.getAttributeValue("about") != null) {
				String aboutKiwiId = e.getAttributeValue("about").trim();
				aboutCi = contentItemService.getContentItemByKiwiId(aboutKiwiId);
				if (aboutCi == null) {
					log.debug("applyRecursive about property #0 does not exist!", aboutKiwiId);
				}
				else {
					if (aboutCi.getResource().isUriResource()) {
						e.addAttribute(new Attribute("about", ((KiWiUriResource)aboutCi.getResource()).getUri()));
					}
					else {
						e.addAttribute(new Attribute("about", generateCurie(aboutCi.getResource(), prefix2namespace)));
					}
					
					/* The about element means all the sub-elements are now about this resource, so we change the context. */
					nextContext = aboutCi.getResource();
					
					/* Add a CSS class attribute, so we can visually distinguish nested content items... */
					if (nextContext != origContext) {
						// only if it is nested, do not mark the top-level content as nested.
						e.addAttribute(new Attribute("class", "nestedContentItem"));
					}
				}
				
				/*
				 * The typeof attribute is an optional attribute, that may be used
				 * with conjunction with an "about" attribute to specify the type of
				 * the component. This type information is redundant (since it is
				 * stored in the triplestore as a property on the component content
				 * item), but it may be used in the UI to designate the 'primary'
				 * type that is important in this context. We preserve this
				 * information in the RDFa view, so here we just translate the
				 * kiwiid to CURIE
				 */
				if (e.getAttributeValue("typeof") != null) {
					String typeofKiwiId = e.getAttributeValue("typeof").trim();
					ContentItem typeofCi = contentItemService.getContentItemByKiwiId(typeofKiwiId);
					if (typeofCi == null) {
						log.debug("applyRecursive typeof type #0 does not exist!", typeofKiwiId);
					}
					else {
						e.addAttribute(new Attribute("typeof", generateCurie(typeofCi.getResource(), prefix2namespace)));
						
						/* Remember the triple, so we don't add it again.  */ 
						triples.add(nextContext.getKiwiIdentifier() + " " + "uri::" + Constants.NS_RDF + "type" + " " + typeofCi.getKiwiIdentifier());
					}
				}
				
				/* The rel specifies a predicate between the context and the component. */
				if (e.getAttribute("rel") != null) {
					String relKiwiId = e.getAttributeValue("rel");
					ContentItem relCi = contentItemService.getContentItemByKiwiId(relKiwiId);
					
					if (relCi == null) {
						log.debug("applyRecursive rel property #0 does not exist!", relKiwiId);
					}
					else {
						e.addAttribute(new Attribute("rel", generateCurie(relCi.getResource(), prefix2namespace)));
						
						/* Remember the triple, so we don't add it again.  */ 
						triples.add(context.getKiwiIdentifier() + " " + relCi.getKiwiIdentifier() + " " + nextContext.getKiwiIdentifier());
					}
				}
			}

			/* Recurse */
			for (int i = 0; i < e.getChildCount(); ++i) {
				Node child = e.getChild(i);
				applyRecursive(origContext, nextContext, child, prefix2namespace, kiwiid2a, triples);
			}
			
			/* After we have gone through all the descendants of the about element, add all the other triples as empty span elements */
			if (aboutCi != null) {
				for(KiWiTriple triple : nextContext.listOutgoing()) {
					if (disabledProperties.contains(triple.getProperty().getUri())) {
						continue;
					}
					
					if(triple.getObject().isLiteral()) {
						KiWiUriResource property = triple.getProperty();
						KiWiLiteral object = (KiWiLiteral)triple.getObject();
						
						String objectContentMd5 = object.getContentMd5();

						/* Check if we already have rendered this information directly in the text..*/
						if (triples.contains(nextContext.getKiwiIdentifier() + " " + property.getKiwiIdentifier() + " " + objectContentMd5)){
							continue;
						}
						
						String propertyCurie = generateCurie(property, prefix2namespace);

						Element span = new Element("span", Constants.NS_XHTML);
						span.addAttribute(new Attribute("property", propertyCurie));
						span.addAttribute(new Attribute("content", object.getContent()));
						e.appendChild(span);
					}
					else if (triple.getObject().isUriResource()) {
						KiWiUriResource property = triple.getProperty();
						KiWiUriResource object = (KiWiUriResource) triple.getObject();
						
						/* Check if we already have rendered this information directly in the text..*/
						if (triples.contains(nextContext.getKiwiIdentifier() + " " + property.getKiwiIdentifier() + " " + object.getKiwiIdentifier())) {
							continue;
						}
						
						String curie = generateCurie(property, prefix2namespace);
						
						/* If we do link to the object, add the rel to the link */
						Element a = kiwiid2a.get(object.getKiwiIdentifier());
						if (a != null) {
							/* Add the CURIE to the "rel" attribute */
							Attribute rel = a.getAttribute("rel");
							if (rel != null) {
								rel.setValue(rel.getValue() + " " + curie);
							}
							else {
								a.addAttribute(new Attribute("rel", curie));
								a.addAttribute(new Attribute("resource", object.getUri()));
							}
						}
						else {
							Element span = new Element("span", Constants.NS_XHTML);
							span.addAttribute(new Attribute("rel", curie));
							span.addAttribute(new Attribute("resource", object.getUri()));
							e.appendChild(span);
						}
					}
				}
				
				for (KiWiTriple triple : nextContext.listIncoming()) {
					if (triple.getSubject().isUriResource()) {
						KiWiUriResource subject = (KiWiUriResource)triple.getSubject();
						KiWiUriResource property = triple.getProperty();
						
						/* Check if we already have rendered this information directly in the text..*/
						if (triples.contains(subject.getKiwiIdentifier() + " " + property.getKiwiIdentifier() + " " + nextContext.getKiwiIdentifier())) {
							continue;
						}
						
						String curie = generateCurie(property, prefix2namespace);
						Element span = new Element("span", Constants.NS_XHTML);
						span.addAttribute(new Attribute("rev", curie));
						span.addAttribute(new Attribute("resource", subject.getUri()));
						e.appendChild(span);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kiwi.service.render.renderlet.Renderlet#apply(kiwi.model.kbase.KiWiResource
	 * , java.lang.Object)
	 */
	@Override
	public Document apply(KiWiResource context, Document content) {
		
		/* We can only do that if it is a URI resource (or can't we?) */
		if (context.isUriResource()) {
			/* Let us first add the "about" element representing the context.  */
			
			Element aboutDiv = new Element("div", Constants.NS_XHTML);
			aboutDiv.addAttribute(new Attribute("about", context.getKiwiIdentifier()));
			
			Element root = content.getRootElement();
			
			/* Stores all the used namespace prefixes which we need to render in the end */
			Map<String, String> prefix2namespace = new HashMap<String, String> ();
			
			/* for goiing through hrefs and remembering the targets...  */
			Map<String, Element> kiwiid2a = new HashMap<String, Element>();
			
			/* for not rendering same triple more than once */
			Set<String> triples = new HashSet<String> ();
			
			// TODO: I doubt this is the most efficient way to do it
			while(root.getChildCount() > 0) {
				Node child = root.getChild(0);
				child.detach();
				aboutDiv.appendChild(child);
			}
			root.appendChild(aboutDiv);
			
			/* Now do the actual transformation (kiwiid -> CURIE, render all hidden properties, etc... )*/
			applyRecursive (context, null, aboutDiv, prefix2namespace, kiwiid2a, triples);
			
			/* Finally, render the namespace declarations for all the CURIEs used inside */
			for (Map.Entry<String, String> namespace : prefix2namespace.entrySet()) {
				log.debug("adding namespace declaration key:#0 value:#1", namespace.getKey(), namespace.getValue());
				aboutDiv.addNamespaceDeclaration(namespace.getKey(), namespace.getValue());
			}
		}
		
		/* Better keep it here for a while... */
		log.debug("output: #0", content.toXML());
		
		return content;
	}
}
