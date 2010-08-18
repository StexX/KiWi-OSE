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

import java.util.HashSet;
import java.util.List;

import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

/**
 * A KiWi Service for managing user profile information.
 * 
 * 
 * @author Sebastian Schaffert
 *
 */
public interface ProfileService {

	/**
	 * Return the profile for the given KiWi user.
	 * 
	 * @param user
	 * @return
	 */
	public KiWiProfileFacade getProfile(User user);
	
	

	/**
	 * Get the content item that is associated with the interest with title given as parameter
	 * "name". If such a content item already exists, it is returned. If not, this method creates a 
	 * new content item with type kiwi:Tag and kiwi:Interest.
	 * 
	 * @param name
	 * @return
	 */
	public ContentItem getInterest(String name);
	
	/**
	 * Parse the comma separated string passed as argument and return a set of content items
	 * representing the interests identified in the string. Interests that do not yet exist are
	 * created as new content items with types kiwi:Tag and kiwi:Interest.
	 * 
	 * @param interests
	 * @return
	 */
	public HashSet<ContentItem> getInterests(String interests);
	
	/**
	 * Return the city with the given postal code, name, and country. If the city does not yet exist,
	 * it is created as a new resource in the KiWi system of type kiwi:City.
	 * 
	 * @param postalCode
	 * @param name
	 * @param country
	 * @return
	 */
	public CityFacade getCity(String postalCode, String name, String country);
	
	/**
	 * Return the country with the given ISO code or name. If the country does not yet exist, a new 
	 * resource of type kiwi:Country is created using the ISO code as short name and the Java Locale 
	 * name as long name.
	 * 
	 * @param identifier a country name or (preferrable) ISO code
	 * @return
	 */
	public CountryFacade getCountry(String identifier);
	
	
	/**
	 * Return all microblogging posts of the user passed as argument. If "limit" is bigger than 0,
	 * at most "limit" results are returned. The result is created using the search functionality
	 * of the KiWi system. 
	 * <p>
	 * The returned content items all have the rdf type kiwi:MicrobloggingPost or one of its 
	 * subclasses (kiwi:TwitterPost, kiwi:FacebookPost, ...).
	 * 
	 * @param user   a KiWiProfileFacade identifying the user whose microblogging posts to retrieve
	 * @param limit  if greater than 0, the maximum number of results to return
	 * @return       a list of content items ordered by modification date, first item is newest.
	 */
	public List<ContentItem> getMicrobloggingPosts(KiWiProfileFacade user, int limit);
	
	
	/**
	 * Return all imported blog posts of the user passed as argument. If "limit" is bigger than 0,
	 * at most "limit" results are returned. The result is created using the search functionality
	 * of the KiWi system. 
	 * <p>
	 * The returned content items all have the rdf type kiwi:BlogPost or one of its 
	 * subclasses.
	 * 
	 * @param user   a KiWiProfileFacade identifying the user whose imported blog posts to retrieve
	 * @param limit  if greater than 0, the maximum number of results to return
	 * @return       a list of content items ordered by modification date, first item is newest.
	 */
	public List<ContentItem> getBlogPosts(KiWiProfileFacade user, int limit);
	
	/**
	 * Return all images, that were added by the user.The result is created using the search functionality
	 * of the KiWi system. 
	 * <p>
	 * The returned content items all have the rdf type kiwi:image
	 * @param user a KiWiProfileFacade identifying the user whose imported images to retrieve
	 * @return
	 */
	public List<ContentItem> getImages( KiWiProfileFacade user );
	
	public boolean importProfileInformation(RepositoryConnection myCon, String base, User user) throws RepositoryException;

	public String getProfilePhotoDownloadUrl(User u);
}
