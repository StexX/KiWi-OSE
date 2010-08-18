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
package kiwi.action.template;

import java.io.Serializable;

import javax.persistence.EntityManager;

import kiwi.api.content.ContentItemService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.jboss.seam.log.Log;

/**
 * @author Rolf Sint
 *
 */
@Name("templateEditingAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
@AutoCreate
public class TemplateEditingAction implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private static Log log;
	
	@In
	private EntityManager entityManager;
	
	@In(required = false)
	ContentItemService contentItemService;
	
	@In(required = false)
	private TripleStore tripleStore;
			
	//on save the property value is stored
	@WebRemote
	public boolean save(String value,String property, long id){
		log.info("Value " + value);	
		log.info("Prpertyx " + property);	
		
		ContentItem currentContentItem = entityManager.find(
				ContentItem.class, id);
		
		if(currentContentItem != null){
			log.info("CurrentcontentItem #0", currentContentItem.getTitle());
		}
		else
		{
			log.error("CurrentcontentItem is null");
		}		
		
		log.info("Done!");
		
		ContentItem propertyCi = contentItemService.getContentItemByKiwiId(property);
		KiWiResource eContext = currentContentItem.getResource();
		KiWiUriResource propertyResource = (KiWiUriResource) propertyCi.getResource();
		KiWiLiteral literal = tripleStore.createLiteral(value);
		
		String propertySeRQLID = propertyCi.getResource().getSeRQLID();
		
		try {
			for (KiWiTriple triple : eContext.listOutgoing(propertySeRQLID)) {
				log.info("removing triple #0", triple);
				tripleStore.removeTriple(triple);
			}
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
		}
		
		KiWiTriple triple = tripleStore.createTriple(currentContentItem.getResource(), propertyResource, literal);
		log.info("t4x ");
		log.info("adding triple #0", triple);
		return true;
	}

	
	
	public String getFieldValue(String propertyKiwiId, long id) {
		
		ContentItem currentContentItem = entityManager.find(ContentItem.class, id);
		
		if(currentContentItem != null){
			log.info("CurrentcontentItem #0", currentContentItem.getTitle());
		}
		else
		{
			log.error("CurrentcontentItem is null");
		}		
		
		String value = null;
		KiWiResource eContext = currentContentItem.getResource();
		
		ContentItem propertyCi = contentItemService.getContentItemByKiwiId(propertyKiwiId);
		String propertySeRQLID = propertyCi.getResource().getSeRQLID();
		
		try {
			for (KiWiTriple triple : eContext.listOutgoing(propertySeRQLID)) {
				log.info("outgoing triples ...");
				if (triple.getObject() instanceof KiWiLiteral) {
					if (value != null) {
						log.info("multiple property values for #0, ignoring property", propertySeRQLID);
						value = "Empty";
						return value;
					}
					value =  ((KiWiLiteral) triple.getObject()).getContent();
				}
			}
		} catch (NamespaceResolvingException e) {
			e.printStackTrace();
		}

		return value;
	}	
}
