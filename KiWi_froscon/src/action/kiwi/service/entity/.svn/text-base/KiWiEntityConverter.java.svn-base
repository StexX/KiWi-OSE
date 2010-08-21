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
package kiwi.service.entity;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;
import kiwi.model.kbase.KiWiEntity;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * A converter that allows to map KiWi entities that are managed by the KiWiEntityManager.
 * <p>
 * This is needed because the ordinary Seam entityConverters do not work nicely with entities
 * loaded by KiWiEntityManager
 * <p>
 * For a given kiwi entity of type T and with database id n, the format of the string representation
 * used by this converter is "T:N". For example, a ContentItem with id 32 would be represented as
 * "kiwi.model.content.ContentItem:32"
 * 
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("kiwiEntityConverter")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Converter
public class KiWiEntityConverter implements Converter {

	/**
	 * Convert the string representation used by KiWiEntityConverter back into the kiwi entity
	 * that was used to build the string representation.
	 * <p>
	 * This method splits the string at the colon ":". It takes the first part as a class name
	 * that is resolved using Class.forName and used by KiWiEntityManager to return the entity
	 * in the proper type. The second part is converted into a long id that is used to look up
	 * the entity in the database.
	 * 
	 * @author
	 * @param arg0 the faces context
	 * @param arg1 the user interface component that asked for the conversion
	 * @param arg2 the string representation of the entity to be converted
	 * @return the entity corresponding to the string representation
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
//	@Transactional
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		
		Log log              = Logging.getLog(KiWiEntityConverter.class);
		log.info("testx "+arg2);
		
		// split string at colon, first part is facade name, second part is long id
		String components[] = arg2.split(":");
		
		try {
			Class type = Class.forName(components[0]);
			Long  id   = Long.parseLong(components[1]);
			
			KiWiEntityManager km = (KiWiEntityManager) Component.getInstance("kiwiEntityManager");
			EntityManager     em = (EntityManager)     Component.getInstance("entityManager");
			
			em.joinTransaction();
						
			log.debug("retrieving KiWi entity of type #0 with id #1",type.getCanonicalName(),id);
			
			if(type.isInterface() && type != ContentItem.class) {
				if(type.isAnnotationPresent(KiWiFacade.class)) {
					ContentItem entity = em.find(ContentItem.class, id);
					
					log.debug("found KiWi facaded ContentItem #0", entity.toString());
					
					return km.createFacade(entity, type);
				} else {
					throw new IllegalArgumentException("only interfaces defined as KiWiFacade are supported in createQuery");
				}
			}
			else{
				Object o = em.find(type, id);
				log.debug("found KiWi entity object #0",o.toString());
				return o;
			}
		} catch (ClassNotFoundException e) {
			// the classname passed in arg2 is invalid, issue an error message
			e.printStackTrace();
		}
		
		
		
		return null;
	}

	/**
	 * Convert a kiwi entity into a string form suitable for passing to the user interface and
	 * later converting back to the same kiwi entity. For a kiwi entity of type T and with id n,
	 * the string returned will be "T:n", e.g. for the ContentItem with id 32 
	 * "kiwi.model.content.ContentItem:32".
	 * 
	 * @author
	 * @param arg0 the faces context
	 * @param arg1 the ui component requesting a conversion of the entity
	 * @param arg2 the entity to be converted
	 * @return a string representation suitable for converting back in subsequent calls
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		Log log              = Logging.getLog(KiWiEntityConverter.class);
		
		if (arg2 instanceof ContentItem){
			String entityName = "kiwi.model.content.ContentItem";			
			//log.debug("getAsString(): KiWi entity has entity name #0 and id #1", entityName, ((KiWiEntity)arg2).getId());
			return entityName+":"+((KiWiEntity)arg2).getId();
		}
		else if((arg2 instanceof ContentItemI)) {
			// the class of arg2 is a proxy generated by Proxy.getProxyClass or Proxy.newProxyInstance;
			// these are generated by kiwiEntityManager.createFacade...
			
			Class[] interfaces = arg2.getClass().getInterfaces();
			
			if(interfaces.length > 1) {
				log.warn("kiwi entity #0 had more than one interface, taking the first one...", arg2);
			}
			Class iface = interfaces[0];
						
			String entityName = iface.getCanonicalName();
			
			//log.debug("getAsString(): KiWi entity has entity name #0 and id #1", entityName, ((KiWiEntity)arg2).getId());
			
			return entityName+":"+((KiWiEntity)arg2).getId();
		} else {
			// if the entity is loaded by hibernate, it is shadowed by the javasisst proxy and we cannot 
			// properly resolve the original classname; in this case we therefore take the part before
			// the "_" in the canonical name of the class
			
			String proxyName = arg2.getClass().getCanonicalName();
			String entityName = proxyName.contains("_javassist")?proxyName.split("_")[0]:proxyName;
	
			//log.debug("getAsString(): KiWi entity has proxy name #0, entity name #1, and id #2",proxyName, entityName,((KiWiEntity)arg2).getId());
			
			
			return entityName+":"+((KiWiEntity)arg2).getId();
		}
	}

}
