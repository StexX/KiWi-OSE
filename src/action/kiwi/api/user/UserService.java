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
package kiwi.api.user;

import java.util.List;

import kiwi.exception.FoafProfileAssociationException;
import kiwi.exception.UserExistsException;
import kiwi.model.content.ContentItem;
import kiwi.model.user.Role;
import kiwi.model.user.User;

/**
 * @author Sebastian Schaffert
 *
 */
public interface UserService {

	/**
	 * Create a new user with the provided login. The method first 
	 * checks of a user with this login already exists; if yes, an exception is thrown. The
	 * method does not persist the user; this needs to be done by the caller.
	 * 
	 * @param login login of the user to create
	 * @return the newly created user.
	 */
	public User createUser(String login, String password) throws UserExistsException;
	
	/**
	 * Create a new user with the provided login, first name and last name. The method first 
	 * checks of a user with this login already exists; if yes, an exception is thrown. The
	 * method does not persist the user; this needs to be done by the caller.
	 * 
	 * @param login login of the user to create
	 * @param firstName first name of the user to create
	 * @param lastName last name of the user to create
	 * @return the newly created user.
	 */
	public User createUser(String login, String firstName, 
			String lastName, String password) throws UserExistsException;
	
	/**
	 * Queries and returns a user with the given webID
	 * @param webID
	 * @return
	 * @throws FoafProfileAssociationException 
	 */
	public User queryUserByFoafProfile(String foafprofileURI) throws FoafProfileAssociationException;
	
	/**
	 * Return a user by login. The user is looked up in the database and returned. In case
	 * no user with this login is found, this method returns null.
	 * 
	 * @param login the login to look for
	 * @return the user with the given login, or null if no such user exists
	 * @see kiwi.api.user.UserService#getUserByLogin(java.lang.String)
	 */
	public User getUserByLogin(String login);
	
	/**
	 * Return the list of all users. Might be slow when many users exist in the system.
	 * 
	 * @return the list of all users active in the system, ordered by login
	 * @see kiwi.api.user.UserService#getUsers()
	 */
	public List<User> getUsers();

	/**
	 * Get users by first and last name. The user is looked up in the database and returned.
	 * In case no user with this combination of first name and last name is found, this
	 * method returns null.
	 * 
	 * @param firstName the first name of the user to look for
	 * @param lastName the last name of the user to look for
	 * @return the list of users with the given first name and last name if exists, or empty list otherwise
	 * @see kiwi.api.user.UserService#getUserByName(java.lang.String, java.lang.String)
	 */
	public List<User> getUsersByName(String firstName, String lastName);
	
	/**
	 * Returns the user that holds a certain fbId. 
	 * @param fbId
	 * @return
	 */
	public User getUserByFbId(String fbId);
	
	/**
	 * Returns the user that holds a certain webId. 
	 * @param webId
	 * @return
	 */
	public User getUserByWebId(String webId);
	
	/**
	 * Get a user by the uri identifying him. Loads the user with the given URI
	 * from the database. If no such user exists, returns null.
	 * 
	 * @param uri the uri to look for
	 * @return the user with this uri, or null if there is no such user
	 * @see kiwi.api.user.UserService#getUserByUri(java.lang.String)
	 */
	public User getUserByUri(String uri);
	
	/**
	 * Gives back the user by his/her profile ContentItem.
	 * @param contentItem
	 * @return the user or null if contentItem is no user profile.
	 */
	public User getUserByProfile(ContentItem contentItem);
		
	public User getAnonymousUser();
	
	public boolean userExists(String login);
	
	/**
	 * Store all information about the user in the KiWi database. Makes sure that user as well
	 * as associated content item information is properly persisted.
	 * 
	 * @param user the user to save
	 */
//	public void saveUser(User user);

	/**
	 * Users can define what recommendations they think really are similar 
	 * @param user
	 * @param contentItem
	 */
	public void addMySimilarContentItems(User user, ContentItem contentItem);
	
	/**
	 * Users can remove their similar recommendations
	 * @param user
	 * @param contentItem
	 */
	public void removeMySimilarContentItems(User user, ContentItem contentItem);
	
	/**
	 * It lists the user's similar recommendations
	 * @param user
	 * @param contentItem
	 */
	public List<ContentItem> getMySimilarContentItems(User user);
	
	
	/**
	 * List all user generated users except the anonymous user.
	 * @return
	 */
	public List<User> getAllCreatedUsers();

	public boolean addRole(String login, String role);

	public boolean addRole(User user, String role);

	/**
	 * returns the List of login names of all users
	 */
	public List<String> getAllUsernames();

	public boolean hasRole(User user, Role role);

	public boolean hasRole(User user, String rolename);

	public void removeRole(User user, Role role);
	
	public void removeRole(User user, String rolename);
}
