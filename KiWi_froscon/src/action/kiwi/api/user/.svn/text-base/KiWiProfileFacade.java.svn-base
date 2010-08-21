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

import java.util.Date;
import java.util.HashSet;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;

import org.hibernate.validator.Email;
import org.hibernate.validator.Range;

/**
 * A KiWi facade for managing the data related to the user profile of a user. This facade is 
 * primarily used as a backing bean for the KiWi Profile Editor interface.
 * 
 * @author Sebastian Schaffert
 *
 */
@KiWiFacade
@RDFType({ Constants.NS_KIWI_CORE+"User", Constants.NS_FOAF +"Person", Constants.NS_SIOC + "User", Constants.NS_GEO + "Point" })
public interface KiWiProfileFacade extends ContentItemI {

	/**
	 * The content item representing the current profile photo of the user. 
	 * Mapped to kiwi:hasProfilePhoto. Note that changing the profile photo in the facade currently
	 * does not update the user object.
	 * @return
	 */
    @RDF(Constants.NS_KIWI_CORE + "hasProfilePhoto")
    public ContentItem getProfilePhoto();

    public void setProfilePhoto(ContentItem photo);
    
	
	/**
	 * The nickname of the user. Since this is also the login id to the KiWi system, this value is 
	 * read-only and cannot be changed once a user has registered.
	 * 
	 * @return
	 */
    @RDF(Constants.NS_FOAF + "nick")    
    public String getNickname();
      
    /**
     * The first name of the user. Mapped to foaf:firstName. Note that changing the value using
     * this facade currently does not update the field for the User object!
     * 
     * @return
     */
    @RDF(Constants.NS_FOAF + "firstName")
    public String getFirstName();
    
    public void setFirstName(String firstName);
    
    /**
     * The last name of the user. Mapped to foaf:name. Note that changing the value using this
     * facade currently does not update the field of the User object.
     * @return
     */
    @RDF(value = {Constants.NS_FOAF + "name", Constants.NS_FOAF + "family_name", Constants.NS_FOAF + "familyName", Constants.NS_FOAF + "surname"})
    public String getLastName();

    public void setLastName(String lastName);
    
	
    /**
     * The gender of the user (either "male" or "female"). Mapped to foaf:gender.
     * 
     * TODO: facades should support enums (http://wiki.kiwi-project.eu/atlassian-jira/browse/KIWI-599).
     * @return
     */
    @RDF(Constants.NS_FOAF + "gender")
	public String getGender();
	
	public void setGender(String gender);
	
	
	/**
	 * The birthday of the user as java.util.Date. Mapped to foaf:birthday.
	 * @return
	 */
	@RDF(Constants.NS_FOAF + "birthday")
	public Date getBirthday();
	
	public void setBirthday(Date birthday);
	
	
	/**
	 * The Facebook account ID of the user. Mapped to kiwi:facebookAccount. Supposed to be used to
	 * fetch and link data the user has already specified on facebook (see Facebook Java API).
	 * @return
	 */
	@RDF(Constants.NS_KIWI_CORE + "facebookAccount")
	public String getFacebookAccount();
	
	public void setFacebookAccount(String account);
	
	
	/**
	 * The Twitter account ID of the user. Mapped to kiwi:twitterAccount. Supposed to be used to
	 * fetch and link tweets of the user at Twitter.
	 * @return
	 */
	@RDF(Constants.NS_KIWI_CORE + "twitterAccount")
	public String getTwitterAccount();
	
	public void setTwitterAccount(String twitter);
	
	/**
	 * The E-Mail address of the user. Mapped to foaf:mbox. Note that changing this value in the
	 * facade currently does not automatically update the User object.
	 * @return
	 */
    @Email
    @RDF(Constants.NS_FOAF + "mbox")
	public String getEmail();
	
	public void setEmail(String address);
	
	/**
	 * The URL of the homepage of the user. Mapped to foaf:homepage.
	 * @return
	 */
	@RDF(Constants.NS_FOAF + "homepage")
	public String getHomepage();
	
	public void setHomepage(String url);
	
	/**
	 * The URL of the Weblog of the user. Mapped to foaf:weblog. Supposed to be used for aggregating
	 * the RSS feed of the user into KiWi.
	 * 
	 * @return
	 */
	@RDF(Constants.NS_FOAF + "weblog")
	public String getWeblog();
	
	public void setWeblog(String url);
	
	/**
	 * The landline phone number of the user. Mapped to kiwi:phone.
	 * @return
	 */
	@RDF(Constants.NS_KIWI_CORE + "phone")
	public String getPhone();
	
	public void setPhone(String number);
	
	/**
	 * The mobile phone number of the user. Mapped to kiwi:mobile. Should we send SMS to the user
	 * or allow SMS publishing?
	 * @return
	 */
	@RDF(Constants.NS_KIWI_CORE + "mobile")
	public String getMobile();
	
	public void setMobile(String number);
	
	/**
	 * The skype accound ID of the user. Mapped to kiwi:skypeAccount. We could use this to integrate
	 * presence information and online status.
	 * @return
	 */
	@RDF(Constants.NS_KIWI_CORE + "skypeAccount")
	public String getSkype();
	
	public void setSkype(String address);
	
	/**
	 * The street the user lives in. Mapped to kiwi:street. We represent the address as a combination
	 * of a city resource and a string representing the street.
	 * @return
	 */
	@RDF(Constants.NS_KIWI_CORE + "street")
	public String getStreet();
	
	public void setStreet(String street);
	
	/**
	 * The city the user lives in. Mapped to kiwi:city. The city is returned as a ContentItem that
	 * potentially has a geolocation and other properties. This allows us to identify (in combination
	 * with GeoNames) the geo-context of a user (city, country, ...).
	 * 
	 * Idea: DBPedia or Geonames lookup (http://www.geonames.org/export/ajax-postalcode-autocomplete.html)
	 * 
	 * @return
	 */
	@RDF(Constants.NS_KIWI_CORE + "city")
	public CityFacade getCity();
	
	public void setCity(CityFacade city);
		
	
	/**
	 * The longitude of this point of interest. Maps to geo:long of the geo ontology
	 * in the triple store.
	 * 
	 * @return
	 */
	@Range(min=-180, max=180)
	@RDF(Constants.NS_GEO+"long")
	public double getLongitude();

	public void setLongitude(double longitude);

	/**
	 * The latitude of this point of interest. Maps to geo:lat of the geo ontology 
	 * in the triple store.
	 * @return
	 */
	@Range(min=-90, max=90)
	@RDF(Constants.NS_GEO+"lat")
	public double getLatitude();

	public void setLatitude(double latitude);
	
	/**
	 * The friends of this user. Mapped to foaf:knows. Updating the list of friends should be done
	 * through the User object.
	 * 
	 * @return
	 */
	@RDF(Constants.NS_FOAF + "knows")
	public HashSet<ContentItem> getFriends();

	public void setFriends(HashSet<ContentItem> friends);

	/**
	 * The interests of the user as a set of ContentItems representing topics in the KiWi system.
	 * Mapped to foaf:interest_topic.
	 * 
	 * @return
	 */
	@RDF(Constants.NS_FOAF + "interest_topic")
	public HashSet<ContentItem> getInterests();
	
	public void setInterests(HashSet<ContentItem> interests);

	@RDF(Constants.NS_RSA + "modulus")
	public String getModulus();
	
	public void setModulus(String modulus);
	
	@RDF(Constants.NS_RSA + "public_exponent")
	public String getPublic_exponent();
	
	public void setPublic_exponent(String public_exponent);
	
}
