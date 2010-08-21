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

package kiwi.service.render.savelet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.KiWiProperty;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Nodes;
import nu.xom.ParentNode;
import nu.xom.Text;
import nu.xom.XPathContext;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * Initial implementation of the RDFa savelet. The savelet should extract the
 * RDF metadata from the RDFa annotations produced by the editor.
 *
 * It does not understand full RDFa, only the subset relevant to the editor. 
 *
 * Instead of a CURIE in a property attribute, a kiwiId of the property is
 * stored instead.
 * 
 * Supported features: 
 * 	RDFa properties (&lt;span property="..."&rt;value&lt;span&rt; creates a property with a value.
 * 	&lt;div about="..." &rt; defines nested content items (aka components), produces kiwi:component attribute, to be processed by the ComponentSavelet. The "about" attribute defines a context (usually subject) for all the other RDFa attributes inside of them. 
 * 
 * @author Marek Schmidt
 * 
 */
@Stateless
@AutoCreate
@Name("kiwi.service.render.savelet.RdfaSavelet")
public class RdfaSavelet implements TextContentSavelet {
	
	public static interface ContentItemProvider {
		ContentItem get(String kiwiid);
	}
	
	private ContentItemProvider contentItemProvider;
	
	public RdfaSavelet() {
		this.contentItemProvider = new ContentItemProvider() {
			@Override
			public ContentItem get(String kiwiid) {
				return contentItemService.getContentItemByKiwiId(kiwiid);
			}
		};
	}
	
	/**
	 * This is a hack to enable using this savelet from InformationExtractionService. (or any other service 
	 * that need to work with content items that are not persisted yet... )
	 * 
	 * @param contentItemProvider
	 * @param tripleStore
	 */
	public RdfaSavelet(ContentItemProvider contentItemProvider, TripleStore tripleStore, Log log) {
		this.contentItemProvider = contentItemProvider;
		this.tripleStore = tripleStore;
		this.log = log;
	}

	@Logger
	private Log log;

	@In
	private EntityManager entityManager;
	
	@In
	private TripleStore tripleStore;

	@In
	private ContentItemService contentItemService;

	@In
	private KiWiEntityManager kiwiEntityManager;
	
	/**
	 * Get the current "about" context. The nearest about attribute on the way
	 * up the tree.
	 * 
	 * @param e
	 * @return
	 */
	private KiWiResource getContext(KiWiResource rootContext, Element e) {
		ParentNode p = e.getParent();
		while (p != null) {
			if (p instanceof Element) {
				e = (Element) p;
				String about = e.getAttributeValue("about");
				if (about != null) {
					ContentItem ci = contentItemProvider.get(about);
					if (ci == null) {
						log.info("getContext about attribute references non-existing CI #0", about);
					} else {
						return ci.getResource();
					}
				}
			}

			p = p.getParent();
		}

		return rootContext;
	}

	/**
 	 * Get the current "rel" context. The nearest rel attribute on the way 
 	 * up the tree, unless "killed" by another "about" attribute
 	 */
	private KiWiResource getContextRel(Element e) {
		
		/* special case if rel and about are on the same element, such rel is for this about*/
		String rel = e.getAttributeValue("rel");
		String about = e.getAttributeValue("about");
		if (about != null && rel != null) {
			ContentItem ci = contentItemProvider.get(rel);
			if (ci == null) {
				log.info("getContextRel rel attribute references non-existing CI #0", rel);
			} else {
				return ci.getResource();
			}
		}		

		ParentNode p = e.getParent();	
		while (p != null) {
			if (p instanceof Element) {
				e = (Element) p;
				rel = e.getAttributeValue("rel");

				/* about "kills" rel */
				about = e.getAttributeValue("about");
				if (about != null) return null;

				if (rel != null) {
					ContentItem ci = contentItemProvider.get(rel);
					if (ci == null) {
						log.info("getContextRel rel attribute references non-existing CI #0", rel);
					} else {
						return ci.getResource();
					}
				}
			}

			p = p.getParent();
		}

		return null;
	}
	
	public static String extractPropertyContent (Element e) {
		/* The text below this element are the property value we want. */
		StringBuilder sb = new StringBuilder();
		Nodes texts = e.query("descendant::text()");
		for (int j = 0; j < texts.size(); ++j) {
			/*
			 * TODO: we should probably add some whitespace for each block
			 * element, but we don't expect to have lot of those here...
			 */
			sb.append(((Text) texts.get(j)).getValue());
		}
		
		return sb.toString();
	}
	
	/**
	 * Delete all the content based triples whose subject is either the context, or some component contained there.
	 * 
	 * (note that relation between a container and a component is managed by the container, so we don't delete any triples about 
	 * a relation between a component and a different container (a component may be contained in more than one container)
	 * 
	 * ... all that is possible only because we have 1:1 relationship between a resource and content, so what triples are generated from 
	 * content is not depending on where the component is contained in...
	 * 
	 * @param context
	 * @param content
	 */
	private void removeContentBasedTriples (KiWiResource context, TextContent content) {
		Collection <KiWiTriple> toDelete = new LinkedList<KiWiTriple> ();
				
		/* All the content-based triples where the context is the subject */
		for (KiWiTriple triple : context.listOutgoing()) {
			if (triple == null){
				log.info("triple is null, weird...");
				continue;
			}
			if (triple.isContentBasedOutgoing() != null && triple.isContentBasedOutgoing()) {
				toDelete.add(triple);
			}
		}
		
		/* All the content-based triples where a component is a subject */
		Nodes nodes = content.getXmlDocument().query("//node()[@about]");
		for (int i = 0; i < nodes.size(); ++i) {
			Element aboutDiv = (Element)nodes.get(i);
			String aboutKiwiId = aboutDiv.getAttributeValue("about");
			
			ContentItem ci = contentItemProvider.get(aboutKiwiId);
			for (KiWiTriple triple : ci.getResource().listOutgoing()) {
				if (triple.isContentBasedOutgoing() != null && triple.isContentBasedOutgoing()) {
					toDelete.add(triple);
				}
			}
		}		

		for (KiWiTriple triple : toDelete) {
			tripleStore.removeTriple(triple);
		}
	}
	
	private KiWiLiteral parseTypedLiteral(String value, KiWiUriResource type) {
		if (type.getKiwiIdentifier().equals(tripleStore.getXSDType(Integer.class).getKiwiIdentifier())) {
			// parse integer
			// for now, simple transformation from usual number formats.
			// TODO: implement it properly and as a service somewhere perhaps?
			
			String normalized = value.replaceAll(",", "");
			
			try{
				int i = Integer.parseInt(normalized);
				return tripleStore.createLiteral(Integer.toString(i), null, type);
			}
			catch(NumberFormatException x) {
				// ignore, return an untyped literal 
			}
		}
		
		return tripleStore.createLiteral(value);
	}
	
	private void createContentBasedProperties (KiWiResource context, TextContent content) {
		
		/* Subject resource -> property KiwiId -> list of literal values */
		Map<KiWiResource, Map<String, List<String>>> resource2properties = new HashMap<KiWiResource, Map<String, List<String>>>();

		Nodes nodes = content.getXmlDocument().query("//node()[@property]");
		for (int i = 0; i < nodes.size(); ++i) {
			/*
			 * Either we know it is always an element which has the property
			 * attribute, or I don't know XPath...
			 */
			Element e = (Element) nodes.get(i);
			String propertyKiwiId = e.getAttributeValue("property");

			String propertyContent = extractPropertyContent (e);

			/* Get the "about" context of this property */
			KiWiResource eContext = getContext(context, e);

			/* Add this value to the list of properties. */
			Map<String, List<String>> properties;
			if (!resource2properties.containsKey(eContext)) {
				properties = new HashMap<String, List<String>>();
				resource2properties.put(eContext, properties);
			} else {
				properties = resource2properties.get(eContext);
			}

			if (!properties.containsKey(propertyKiwiId)) {
				List<String> propertyValues = new LinkedList<String>();
				properties.put(propertyKiwiId, propertyValues);
			}

			properties.get(propertyKiwiId).add(propertyContent);
		}

		/* Now simply recreate all those properties. */
		for (Map.Entry<KiWiResource, Map<String, List<String>>> resource2property : resource2properties
				.entrySet()) {
			KiWiResource resource = resource2property.getKey();
			for (Map.Entry<String, List<String>> entry : resource2property
					.getValue().entrySet()) {
				
				String propertyKiwiId = entry.getKey();
				List<String> propertyValues = entry.getValue();

				ContentItem propertyCi = contentItemProvider.get(propertyKiwiId);

				/* Property has to exist... */
				if (propertyCi == null) {
					log.info("No CI for propertyKiwiId #0!", propertyKiwiId);
					continue;
				}
				/* ... and has to be a URI resource */
				if (!propertyCi.getResource().isUriResource()) {
					log.info("property #0 is not a URI resource!",
							propertyKiwiId);
					continue;
				}
				
				KiWiUriResource propertyResource = (KiWiUriResource) propertyCi.getResource();

				/* If we already have triples with the subject and property the same, remove them.
				 * This means: multiple values for one property is not supported.
				 * */
				Collection<KiWiTriple> outs;
				try {
					outs = resource.listOutgoing(propertyResource.getSeRQLID());
				} catch (NamespaceResolvingException e) {
					e.printStackTrace();
					outs = Collections.emptySet();
				}
		    	
				for (KiWiTriple triple : outs) {
					tripleStore.removeTriple (triple);
				}
				
				/*KiWiProperty property = kiwiEntityManager.createFacade(propertyResource.getContentItem(), KiWiProperty.class);
				if (property)*/
				
				KiWiUriResource type = null;
				try {
					// TODO: this is simplified semantics, only one datatype supported
					for (KiWiNode n : propertyResource.listOutgoingNodes("rdfs:range")) {
						if (n.isUriResource()) {
							type = (KiWiUriResource)n;
						}
					}
				} catch (NamespaceResolvingException e) {
					log.error(e);
				}

				
				
				/* Create the content-based triples for this resource and property*/
				for (String propertyValue : propertyValues) {
					KiWiLiteral literal = null;
					
					if (type != null) {
						//= tripleStore.createLiteral(propertyValue);
						literal = parseTypedLiteral(propertyValue, type);
					}
					else {
						literal = tripleStore.createLiteral(propertyValue);
					}
					KiWiTriple triple = tripleStore.createTriple(resource, propertyResource, literal);
					triple.setContentBasedOutgoing(true);	
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kiwi.service.render.savelet.Savelet#apply(kiwi.model.kbase.KiWiResource,
	 * java.lang.Object)
	 */
	@Override
	public TextContent apply(KiWiResource context, TextContent content) {
		
		if (context == null) {
			log.info("context is null");
			return content;
		}
		
		removeContentBasedTriples(context, content);
		
		createContentBasedProperties(context, content);
		
		createContentBasedComponentTypesAndRelations(context, content);
		
		createContentBasedLinkRelations (context, content);

		updateIteratedIncludesRelations (context, content);
	
		/* Turn the about element into components (to be processed by the ComponentSavelet */
		Nodes nodes = content.getXmlDocument().query("//node()[@about]");
		for (int i = 0; i < nodes.size(); ++i) {
			Element aboutDiv = (Element)nodes.get(i);
			String about = aboutDiv.getAttributeValue("about");
			
			aboutDiv.addAttribute(new Attribute("kiwi:component", Constants.NS_KIWI_HTML, about));
		}
		return content;
	}

	/**
	 * Create triples for RDFa links, such as <span rel="..." typeof="..." resource="..." /> 
	 * If there already exist a triple with the same subject and predicate, remove it.
	 * 
	 * The type is expected to be already one of types of the referenced CI.
	 * 
	 * @param context
	 * @param content
	 */
	private void createContentBasedLinkRelations(KiWiResource context,
			TextContent content) {

		Nodes nodes = content.getXmlDocument().query("//node()[@resource and @rel]");
		for (int i = 0; i < nodes.size(); ++i) {
			Element link = (Element)nodes.get(i);
			String rel = link.getAttributeValue("rel");
			String resource = link.getAttributeValue("resource");
			
			KiWiResource eContext = getContext(context, link);
			
			// log.info("relation: #0 #1 #2", eContext.getKiwiIdentifier(), rel, resource);
			
			ContentItem resourceCi = contentItemProvider.get(resource);
			if (resourceCi == null) {
				continue;
			}
			
			ContentItem relCi = contentItemProvider.get(rel);
			if (relCi == null) {
				continue;
			}
			
			if (!relCi.getResource().isUriResource()) {
				log.info("relation resource #0 is not a URI resource!", relCi.getKiwiIdentifier());
				continue;
			}

			Collection<KiWiTriple> outs;
			try {
				outs = eContext.listOutgoing(relCi.getResource().getSeRQLID());
			} catch (NamespaceResolvingException e) {
				e.printStackTrace();
				outs = Collections.emptySet();
			}
			// If we have exactly one such triple, replace it... (TODO: do the replacement in the editor directly, so we replace the old triple with a new triple and thus allow having more links with the same predicate on one CI)
		    // this does not work: 
			//if (outs.size() == 1) {
			//	KiWiTriple triple = outs.iterator().next();
			//	tripleStore.removeTriple (triple);
			//}

			KiWiTriple triple = tripleStore.createTriple(eContext, ((KiWiUriResource)relCi.getResource()), resourceCi.getResource());
			triple.setContentBasedOutgoing(true);
		}
	}

	/**
 	 * Creates RDF from RDFa about components, which in the editor are represented as elements with "about" attribute" 
 	 * They may be related to the containing CI by the predicate specified by a "rel" attribute either directly on the same element, or some "iteration" 
 	 * element above...
	 * @param context
	 * @param content
	 */
	private void createContentBasedComponentTypesAndRelations(
			KiWiResource context, TextContent content) {

		Nodes nodes = content.getXmlDocument().query("//node()[@about]");
		for (int i = 0; i < nodes.size(); ++i) {
			Element component = (Element)nodes.get(i);
			
			String typeof = component.getAttributeValue("typeof");
			String about = component.getAttributeValue("about");
			
			/* context, the nearest "about" RDFa attribute on the way up the tree... */
			KiWiResource eContext = getContext(context, component);

			/* the nearest "rel" RDFa attribute on the way up the tree... */
			KiWiResource eContextRel = getContextRel (component);
	
			ContentItem aboutCi = contentItemProvider.get(about);
			if (aboutCi == null) {
				log.info("ContentItem #0 does not exist.", about);
				continue;
			}
			
			/* Component may optionally have a "typeof" attribute specifying component type. */
			ContentItem typeofCi = contentItemProvider.get(typeof);
			if (typeofCi != null) {
				KiWiTriple triple = tripleStore.createTriple(aboutCi.getResource(), Constants.NS_RDF + "type", typeofCi.getResource());
				triple.setContentBasedOutgoing(true);
			}
		
				
			if (eContextRel != null) {
				if (!eContextRel.isUriResource()) {
					log.info("#0 is not a URI resource!", eContextRel);
				}
				else {
					KiWiTriple triple = tripleStore.createTriple(eContext, ((KiWiUriResource)eContextRel), aboutCi.getResource());
					triple.setContentBasedOutgoing(true);
				}
			}
		}
	}

	private void updateIteratedIncludesRelations(KiWiResource context, TextContent content) {
		XPathContext namespaces = new XPathContext();
        namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
		Nodes nodes = content.getXmlDocument().query("//node()[@kiwi:iteratedinclude]", namespaces);
		for (int i = 0; i < nodes.size(); ++i) {
			Element iteratedinclude = (Element)nodes.get(i);
			String relKiwiId = iteratedinclude.getAttributeValue("iteratedinclude", Constants.NS_KIWI_HTML);

			ContentItem relCi = contentItemProvider.get(relKiwiId);
			if (relCi == null) {
				log.info("iteratedinclude attribute value #0 does not represent an existing kiwiid", relKiwiId);
				continue;
			}

			KiWiResource eContext = getContext(context, iteratedinclude);

			// Get the collection of all the included components in this iteration...
			Set<KiWiResource> includes = new HashSet<KiWiResource>();
			Elements children = iteratedinclude.getChildElements();
			for (int j = 0; j < children.size(); ++j) {
				Element child = children.get(j);
				String includeKiwiId = child.getAttributeValue("about");
				if (includeKiwiId != null) {
					ContentItem includeCi = contentItemProvider.get(includeKiwiId);
					if (includeCi != null) {
						includes.add (includeCi.getResource());
					}
				}
			}

			// delete the triples that are not included (those were deleted), all the new ones are created by createContentBasedComponentTypesAndRelations
			try {
				for (KiWiTriple triple : eContext.listOutgoing(relCi.getResource().getSeRQLID())) {
					if (!includes.contains(triple.getObject())) {
						tripleStore.removeTriple(triple);
					}
				}
			} catch (NamespaceResolvingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
