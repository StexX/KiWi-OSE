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
package kiwi.service.security;

import java.util.Iterator;
import java.util.Set;

import javax.ejb.Remove;
import javax.ejb.Stateless;

import kiwi.api.security.SecurityServiceLocal;
import kiwi.api.security.SecurityServiceRemote;
import kiwi.model.content.ContentItem;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.permission.PermissionResolver;
import org.jboss.seam.security.permission.ResolverChain;

/**
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("securityService")
@Scope(ScopeType.STATELESS)
public class SecurityServiceImpl implements PermissionResolver, SecurityServiceLocal, SecurityServiceRemote {

	@Logger
	private Log log;
	
	@Observer("org.jboss.seam.postInitialization")
	public void initialise() {
		log.info("KiWi Security Service starting up ...");
		
	}
	
	@Remove
	public void shutdown() {
		log.info("KiWi Security Service shutting down ...");
	}

//	@Observer(create=true,value="org.jboss.seam.security.defaultResolverChainCreated")
	public void register(ResolverChain resolverChain) {
		log.info("KiWi Security Service registering ...");
//		resolverChain.getResolvers().add(this);
	}
	
	/**
	 * 
	 * 
	 * @see kiwi.api.security.SecurityService#hasPermission(kiwi.model.content.ContentItem, kiwi.model.user.User, java.lang.String)
	 */
	@Override
	public boolean hasPermission(Object item, String action) {
//		User user = (User) Component.getInstance("currentUser");
//		
//		if(user != null && item != null) {
//			if(item instanceof KiWiResource) {
//	
//				log.debug("granting permission '#0' on resource '#1' to user '#2'", 
//						action, 
//						((KiWiResource)item).getKiwiIdentifier(), 
//						user.getLogin());
//			} else if(item instanceof ContentItem) {
//				if(((ContentItem)item).getResource() != null) {
//				log.debug("granting permission '#0' on content item '#1' to user '#2'", 
//						action, 
//						((ContentItem)item).getResource().getKiwiIdentifier(), 
//						user.getLogin());
//				} else {
//					log.error("content item resource was null (#0)",item);
//				}
//				
//			}
//		} else {
//			log.warn("while checking permissions: user (#0) or item (#1) was null", user, item);
//		}
		return true;
	}

	/**
	 * 
	 * @see kiwi.api.security.SecurityService#hasPermission(java.lang.String)
	 */
	@Override
	public boolean hasPermission(String action) {
		
		ContentItem currentContentItem = (ContentItem) Component.getInstance("currentContentItem");
		
		return hasPermission(currentContentItem, action);
	}

	
	@Override
	public void filterSetByAction(Set<Object> targets, String action) {
		for(Iterator<Object> it = targets.iterator(); it.hasNext(); ) {
			Object o = it.next();
			if(hasPermission(o,action)) {
				it.remove();
			}
		}
		
	}

	
	
}
