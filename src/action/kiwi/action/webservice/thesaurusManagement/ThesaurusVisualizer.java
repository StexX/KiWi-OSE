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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import kiwi.api.ontology.SKOSService;
import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * @author Rolf Sint
 * 
 */
@Name("thesaurusVisualizer")
@Scope(ScopeType.CONVERSATION)
public class ThesaurusVisualizer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create = true)
	private SKOSService skosService;
	
	private HashMap<String, List<String>> narrowerRelationship;
	
	public void generateXml() {

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		try {
			//ugly hack ;-/		(it is possible to avoid the hack by changing the flash file that it should look up to the tmp directory where the xml file is stored)
			XMLStreamWriter writer = factory
					.createXMLStreamWriter(new FileOutputStream(
							"../server/default/deploy/KiWi.ear/kiwiext-w2k.jar/visualizeThesaurus/relationBrowser.xml"));

			writer.writeStartDocument();
			
			writer.writeStartElement("RelationViewerData");
			
				writer.writeStartElement("Settings");
				writer.writeAttribute("appTitle", "Browse Thesaurus");
				writer.writeAttribute("startID", "uri::http://www.eduhui.at/concepts#778"); //TODO
				writer.writeAttribute("defaultRadius", "150");
				writer.writeAttribute("maxRadius", "180");

					writer.writeStartElement("RelationTypes");
						writer.writeStartElement("DirectedRelation");
							writer.writeAttribute("color", "0xAAAAAA");
							writer.writeAttribute("lineSize", "4");
							writer.writeAttribute("letterSymbol", "N");
							writer.writeAttribute("labelText", "Narrower");
						writer.writeEndElement();
						
						writer.writeStartElement("MyCustomRelation");
							writer.writeAttribute("color", "0xB7C631");
							writer.writeAttribute("lineSize", "4");
							writer.writeAttribute("letterSymbol", "B");
							writer.writeAttribute("labelText", "Broader");
						writer.writeEndElement();
						
						writer.writeStartElement("UndirectedRelation");
							writer.writeAttribute("color", "0x85CDE4");
							writer.writeAttribute("lineSize", "4");
						writer.writeEndElement();
					writer.writeEndElement();

					writer.writeStartElement("NodeTypes");
						writer.writeEmptyElement("Node");
						writer.writeEmptyElement("Comment");
						writer.writeEmptyElement("Person");
						writer.writeEmptyElement("Document");
					writer.writeEndElement();
			
				writer.writeEndElement(); //End setting
			
			LinkedList<SKOSConcept> topConcepts = (LinkedList<SKOSConcept>) skosService.getTopConcepts();
			
			HashMap<String, String> topConceptRelationship = new HashMap<String, String>();
			narrowerRelationship = new HashMap<String, List<String>>();
			
			writer.writeStartElement("Nodes");
			
			SKOSConcept old = null;
			for (SKOSConcept skosConcept: topConcepts) {
				if (old != null){
					topConceptRelationship.put(skosConcept.getKiwiIdentifier(), old.getKiwiIdentifier());
				}
				writer.writeStartElement("Node");
					writer.writeAttribute("id", skosConcept.getKiwiIdentifier());
					String name = SKOSConceptUtils.getConceptLabelsInLanguages(skosConcept, new Locale("de"), new Locale("en"));					
					writer.writeAttribute("name", name);
				
					writer.writeEndElement();
				old = skosConcept;
				generateNarrowers(skosConcept, writer);
				
			}
			writer.writeEndElement(); //End Nodes

			writer.writeStartElement("Relations");
			Collection<String> topConceptRel = topConceptRelationship.keySet();

			for (String s : topConceptRel) {
				writer.writeStartElement("UndirectedRelation");
				writer.writeAttribute("fromID", "" + s);
				writer.writeAttribute("toID", topConceptRelationship.get(s));
				writer.writeEndElement();
			}
			
			Collection<String> narrowerRel = narrowerRelationship.keySet();
			//iterate over all keys
			for (String s : narrowerRel) {
				List<String> na  = narrowerRelationship.get(s);
				for (String s1 : na) {
					//narrower
					writer.writeStartElement("DirectedRelation");
					writer.writeAttribute("fromID", "" + s);
					writer.writeAttribute("toID", s1);
					writer.writeEndElement();
				}
			}
			
			writer.writeEndElement(); //end relations
			writer.writeEndElement(); //End RelationViewerData
			writer.writeEndDocument();
			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void generateNarrowers(SKOSConcept skosConcept, XMLStreamWriter writer){
		HashSet<SKOSConcept> narrower = skosConcept.getNarrower();
		List<String> temp = new LinkedList<String>();
		
		for (SKOSConcept skosConceptNar: narrower) {
			temp.add(skosConceptNar.getKiwiIdentifier());
			try {
				writer.writeStartElement("Node");
					writer.writeAttribute("id", skosConceptNar.getKiwiIdentifier());
					String label = SKOSConceptUtils.getConceptLabelsInLanguages(skosConceptNar, new Locale("de"), new Locale("en"));					
					writer.writeAttribute("name", label);
				writer.writeEndElement();
				narrowerRelationship.put(skosConcept.getKiwiIdentifier(), temp);
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
			//recursive call to get the children from the children
			generateNarrowers(skosConceptNar, writer);
		}
	}
}
