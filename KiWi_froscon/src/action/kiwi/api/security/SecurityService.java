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
package kiwi.api.security;

import org.jboss.seam.security.permission.PermissionResolver;
import org.jboss.seam.security.permission.ResolverChain;

/**
 * The security service allows to check whether a user has a certain permission on a resource. It 
 * provides the methods
 * <ul>
 * <li>hasPermission(ContentItem, User, Action), checking whether the user has the "Action"-Permission
 *     on ContentItem</li>
 * <li>hasPermission(Action), a shortcut using currentContentItem and currentUser</li> 
 * </ul>
 * Both methods return true when the user has the permission, false otherwise. Action is one of
 * <ul>
 * <li>"view" - indicating that the user is allowed to view the content item</li>
 * <li>"edit" - indicating that the user is allowed to edit the content item</li>
 * <li>"metadata" - indicating that the user is allowed to modify the metadata associated with the 
 *     content item</li>
 * <li>"delete" - indicating that the user is allowed to delete the content item</li>
 * </ul>
 * Currently, a permission/restriction is stored in the database as a tuple 
 * (resource, user, kind, precedence), where resource is a kiwi identifier or pattern, user is
 * the id of the user in the database, kind is one of the actions shown above, and 
 * 
 * @author Sebastian Schaffert
 *
 */
public interface SecurityService extends PermissionResolver {

	public void initialise();
	
	public void shutdown();
	
	public void register(ResolverChain resolverChain);
	
	/**
	 * Check whether the user has the permission named "action" on the contentitem "item". Returns
	 * true if this is the case, false otherwise.
	 * 
	 * @param item the content item on which to grant the permission
	 * @param action the action to check for ("view","edit","metadata","delete")
	 * @return true if the permission is granted
	 */
	public boolean hasPermission(Object item, String action);
	
	/**
	 * Check whether currentUser has the permission named "action" on currentContentItem. Returns
	 * true if this is the case, false otherwise.
	 * 
	 * @param action the action to check for ("view","edit","metadata","delete")
	 * @return true if the permission is granted
	 */
	public boolean hasPermission(String action);
}
