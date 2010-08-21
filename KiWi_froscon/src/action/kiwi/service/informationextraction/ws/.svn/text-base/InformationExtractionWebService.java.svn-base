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
package kiwi.service.informationextraction.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import kiwi.api.informationextraction.InformationExtractionService;
import kiwi.model.Constants;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.Context;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiAnonResource;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.util.KiWiXomUtils;
import kiwi.util.KiWiXomUtils.NodePos;
import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Text;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.rdfxml.RDFXMLWriterFactory;
import org.openrdf.sail.memory.MemoryStore;

@Name("kiwi.informationextraction.ws.informationExtractionWebService")
@Scope(ScopeType.APPLICATION)
@Path("/ie")
public class InformationExtractionWebService {
	
	@Logger
	private Log log;
	
	private Document createBookmark (Document doc, String resource_id, int beginPos, int endPos) {
		Document ret = (Document)doc.copy();
		
		NodePos begin = KiWiXomUtils.getNode(ret, 0, beginPos);
		
		if (begin != null) {
			Element e_begin = new Element("kiwi:bookmarkstart", Constants.NS_KIWI_HTML);
			e_begin.addAttribute(new Attribute("resource", resource_id));
			
			KiWiXomUtils.insertNodeInsideText((Text)(begin.getNode()), e_begin, beginPos - begin.getPos());
		}
		
		NodePos end = KiWiXomUtils.getNode(ret, 0, endPos);
		
		if (end != null) {
			Element e_end = new Element("kiwi:bookmarkend", Constants.NS_KIWI_HTML);
			e_end.addAttribute(new Attribute("resource", resource_id));
			
			KiWiXomUtils.insertNodeInsideText((Text)(end.getNode()), e_end, endPos - end.getPos());
		}
		
		return ret;
	}
	
	@POST
	@Path("/er")
	@Consumes("application/xml")
	@Produces("text/xml; charset=utf-8")
	public byte[] extractEntities(byte[] xml) throws RepositoryException {
		
		InformationExtractionService informationExtractionService = 
			(InformationExtractionService)Component.getInstance("kiwi.informationextraction.informationExtractionService");
		
		Repository myRepository = new SailRepository(new MemoryStore());

		myRepository.initialize();
		RepositoryConnection myCon = myRepository.getConnection();
		ValueFactory f = myRepository.getValueFactory();
		RDFXMLWriterFactory writerFactory = new RDFXMLWriterFactory();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		RDFWriter rdfWriter = writerFactory.getWriter(out);
		
		Builder builder = new Builder();
		Document doc;
		
		Resource thisDoc = f.createBNode();
		URI typeContentItem = f.createURI(Constants.NS_KIWI_CORE, "ContentItem");
		URI fragmentOf = f.createURI(Constants.NS_KIWI_SPECIAL, "fragmentOf");
		URI predicateTaggedWithTag = f.createURI(Constants.NS_HGTAGS, "taggedWithTag");
		URI predicateHasTextContent = f.createURI(Constants.NS_KIWI_CORE, "hasTextContent");
		URI isa = f.createURI(Constants.NS_RDF, "type");
		
		int id = 0;
		
		try {
			doc = builder.build(new ByteArrayInputStream(xml));
			
			TextContent tc = new TextContent();
			tc.setXmlDocument(doc);
			
			doc = tc.getXmlDocument();
			Collection<Suggestion> ess = informationExtractionService.extractEntities(null, tc, Locale.ENGLISH);
			for (Suggestion es : ess) {
				InstanceEntity ie = es.getInstance();
				Context ctx = ie.getContext();
				String fragment_id = "fragment" + String.valueOf(++id);
				String fragment_curie = "[_:" + fragment_id + "]";
				doc = createBookmark(doc, fragment_curie, ctx.getInBegin(), ctx.getInEnd());
				
				// add some semantic to the graph about the fragment
				Resource fragment = f.createBNode(fragment_id);
				
				myCon.add(fragment, fragmentOf, thisDoc);
				
				for (KiWiResource resource : es.getResources()) {
					if (resource.isUriResource()) {
						myCon.add(fragment, predicateTaggedWithTag, f.createURI(((KiWiUriResource)resource).getUri()));
					}
					else {
						myCon.add(fragment, predicateTaggedWithTag, f.createBNode(((KiWiAnonResource)resource).getAnonId()));
					}
				}
				
				for (KiWiResource type : es.getTypes()) {
					if (type.isUriResource()) {
						myCon.add(fragment, predicateTaggedWithTag, f.createURI(((KiWiUriResource)type).getUri()));
					}
					else {
						myCon.add(fragment, predicateTaggedWithTag, f.createBNode(((KiWiAnonResource)type).getAnonId()));
					}
				}
			}
			
			myCon.add(thisDoc, isa, typeContentItem);
			myCon.add(thisDoc, predicateHasTextContent, f.createLiteral(doc.toXML()));	
			
			myCon.commit();
			
			myCon.export(rdfWriter);
			
			return out.toByteArray();
						
		} catch (Exception e) {
			log.info("can't parse document", e);
		}
		
		return new byte[0];
	}
	
	
	
}
