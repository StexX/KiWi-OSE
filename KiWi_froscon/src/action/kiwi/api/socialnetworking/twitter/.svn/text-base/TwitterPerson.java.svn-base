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
 * sschaffe
 * 
 */
package kiwi.api.socialnetworking.twitter;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.unto.twitter.TwitterProtos.User;

import org.apache.shindig.social.core.model.AddressImpl;
import org.apache.shindig.social.core.model.ListFieldImpl;
import org.apache.shindig.social.opensocial.model.Account;
import org.apache.shindig.social.opensocial.model.Address;
import org.apache.shindig.social.opensocial.model.BodyType;
import org.apache.shindig.social.opensocial.model.Enum;
import org.apache.shindig.social.opensocial.model.ListField;
import org.apache.shindig.social.opensocial.model.Name;
import org.apache.shindig.social.opensocial.model.Organization;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.model.Url;
import org.apache.shindig.social.opensocial.model.Enum.Drinker;
import org.apache.shindig.social.opensocial.model.Enum.LookingFor;
import org.apache.shindig.social.opensocial.model.Enum.NetworkPresence;
import org.apache.shindig.social.opensocial.model.Enum.Smoker;

/**
 * TwitterPerson - Implementation of OpenSocial Person as wrapper around a Twitter User object.
 *
 * @author Sebastian Schaffert
 *
 */
public class TwitterPerson implements Person {

	private User user;
	
	public TwitterPerson(User user) {
		this.user = user;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getAboutMe()
	 */
	@Override
	public String getAboutMe() {
		return user.getDescription();
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getAccounts()
	 */
	@Override
	public List<Account> getAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getActivities()
	 */
	@Override
	public List<String> getActivities() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getAddresses()
	 */
	@Override
	public List<Address> getAddresses() {
		Address a = new AddressImpl();
		a.setLocality(user.getLocation());
		return Collections.singletonList(a);
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getAge()
	 */
	@Override
	public Integer getAge() {
		// Twitter has no age information
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getBirthday()
	 */
	@Override
	public Date getBirthday() {
		// Twitter has no birthday information
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getBodyType()
	 */
	@Override
	public BodyType getBodyType() {
		// Twitter has no body information
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getBooks()
	 */
	@Override
	public List<String> getBooks() {
		// Twitter has no book information
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getCars()
	 */
	@Override
	public List<String> getCars() {
		// Twitter has no car information
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getChildren()
	 */
	@Override
	public String getChildren() {
		// Twitter has no children information
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getCurrentLocation()
	 */
	@Override
	public Address getCurrentLocation() {
		Address a = new AddressImpl();
		a.setLocality(user.getLocation());
		return a;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return user.getScreenName();
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getDrinker()
	 */
	@Override
	public Enum<Drinker> getDrinker() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getEmails()
	 */
	@Override
	public List<ListField> getEmails() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getEthnicity()
	 */
	@Override
	public String getEthnicity() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getFashion()
	 */
	@Override
	public String getFashion() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getFood()
	 */
	@Override
	public List<String> getFood() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getGender()
	 */
	@Override
	public Gender getGender() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getHappiestWhen()
	 */
	@Override
	public String getHappiestWhen() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getHasApp()
	 */
	@Override
	public Boolean getHasApp() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getHeroes()
	 */
	@Override
	public List<String> getHeroes() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getHumor()
	 */
	@Override
	public String getHumor() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getId()
	 */
	@Override
	public String getId() {
		return ""+user.getId();
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getIms()
	 */
	@Override
	public List<ListField> getIms() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getInterests()
	 */
	@Override
	public List<String> getInterests() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getIsOwner()
	 */
	@Override
	public boolean getIsOwner() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getIsViewer()
	 */
	@Override
	public boolean getIsViewer() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getJobInterests()
	 */
	@Override
	public String getJobInterests() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getLanguagesSpoken()
	 */
	@Override
	public List<String> getLanguagesSpoken() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getLivingArrangement()
	 */
	@Override
	public String getLivingArrangement() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getLookingFor()
	 */
	@Override
	public List<Enum<LookingFor>> getLookingFor() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getMovies()
	 */
	@Override
	public List<String> getMovies() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getMusic()
	 */
	@Override
	public List<String> getMusic() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getName()
	 */
	@Override
	public Name getName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getNetworkPresence()
	 */
	@Override
	public Enum<NetworkPresence> getNetworkPresence() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getNickname()
	 */
	@Override
	public String getNickname() {
		return user.getName();
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getOrganizations()
	 */
	@Override
	public List<Organization> getOrganizations() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getPets()
	 */
	@Override
	public String getPets() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getPhoneNumbers()
	 */
	@Override
	public List<ListField> getPhoneNumbers() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getPhotos()
	 */
	@Override
	public List<ListField> getPhotos() {
		ListField result = new ListFieldImpl("other", user.getProfile().getImageUrl());
		return Collections.singletonList(result);
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getPoliticalViews()
	 */
	@Override
	public String getPoliticalViews() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getProfileSong()
	 */
	@Override
	public Url getProfileSong() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getProfileUrl()
	 */
	@Override
	public String getProfileUrl() {
		return user.getUrl();
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getProfileVideo()
	 */
	@Override
	public Url getProfileVideo() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getQuotes()
	 */
	@Override
	public List<String> getQuotes() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getRelationshipStatus()
	 */
	@Override
	public String getRelationshipStatus() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getReligion()
	 */
	@Override
	public String getReligion() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getRomance()
	 */
	@Override
	public String getRomance() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getScaredOf()
	 */
	@Override
	public String getScaredOf() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getSexualOrientation()
	 */
	@Override
	public String getSexualOrientation() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getSmoker()
	 */
	@Override
	public Enum<Smoker> getSmoker() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getSports()
	 */
	@Override
	public List<String> getSports() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getStatus()
	 */
	@Override
	public String getStatus() {
		return user.getStatus().getText();
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getTags()
	 */
	@Override
	public List<String> getTags() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getThumbnailUrl()
	 */
	@Override
	public String getThumbnailUrl() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getTurnOffs()
	 */
	@Override
	public List<String> getTurnOffs() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getTurnOns()
	 */
	@Override
	public List<String> getTurnOns() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getTvShows()
	 */
	@Override
	public List<String> getTvShows() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getUpdated()
	 */
	@Override
	public Date getUpdated() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getUrls()
	 */
	@Override
	public List<Url> getUrls() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#getUtcOffset()
	 */
	@Override
	public Long getUtcOffset() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setAboutMe(java.lang.String)
	 */
	@Override
	public void setAboutMe(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setAccounts(java.util.List)
	 */
	@Override
	public void setAccounts(List<Account> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setActivities(java.util.List)
	 */
	@Override
	public void setActivities(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setAddresses(java.util.List)
	 */
	@Override
	public void setAddresses(List<Address> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setAge(java.lang.Integer)
	 */
	@Override
	public void setAge(Integer arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setBirthday(java.util.Date)
	 */
	@Override
	public void setBirthday(Date arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setBodyType(org.apache.shindig.social.opensocial.model.BodyType)
	 */
	@Override
	public void setBodyType(BodyType arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setBooks(java.util.List)
	 */
	@Override
	public void setBooks(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setCars(java.util.List)
	 */
	@Override
	public void setCars(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setChildren(java.lang.String)
	 */
	@Override
	public void setChildren(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setCurrentLocation(org.apache.shindig.social.opensocial.model.Address)
	 */
	@Override
	public void setCurrentLocation(Address arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setDisplayName(java.lang.String)
	 */
	@Override
	public void setDisplayName(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setDrinker(org.apache.shindig.social.opensocial.model.Enum)
	 */
	@Override
	public void setDrinker(Enum<Drinker> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setEmails(java.util.List)
	 */
	@Override
	public void setEmails(List<ListField> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setEthnicity(java.lang.String)
	 */
	@Override
	public void setEthnicity(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setFashion(java.lang.String)
	 */
	@Override
	public void setFashion(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setFood(java.util.List)
	 */
	@Override
	public void setFood(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setGender(org.apache.shindig.social.opensocial.model.Person.Gender)
	 */
	@Override
	public void setGender(Gender arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setHappiestWhen(java.lang.String)
	 */
	@Override
	public void setHappiestWhen(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setHasApp(java.lang.Boolean)
	 */
	@Override
	public void setHasApp(Boolean arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setHeroes(java.util.List)
	 */
	@Override
	public void setHeroes(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setHumor(java.lang.String)
	 */
	@Override
	public void setHumor(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setId(java.lang.String)
	 */
	@Override
	public void setId(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setIms(java.util.List)
	 */
	@Override
	public void setIms(List<ListField> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setInterests(java.util.List)
	 */
	@Override
	public void setInterests(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setIsOwner(boolean)
	 */
	@Override
	public void setIsOwner(boolean arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setIsViewer(boolean)
	 */
	@Override
	public void setIsViewer(boolean arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setJobInterests(java.lang.String)
	 */
	@Override
	public void setJobInterests(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setLanguagesSpoken(java.util.List)
	 */
	@Override
	public void setLanguagesSpoken(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setLivingArrangement(java.lang.String)
	 */
	@Override
	public void setLivingArrangement(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setLookingFor(java.util.List)
	 */
	@Override
	public void setLookingFor(List<Enum<LookingFor>> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setMovies(java.util.List)
	 */
	@Override
	public void setMovies(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setMusic(java.util.List)
	 */
	@Override
	public void setMusic(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setName(org.apache.shindig.social.opensocial.model.Name)
	 */
	@Override
	public void setName(Name arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setNetworkPresence(org.apache.shindig.social.opensocial.model.Enum)
	 */
	@Override
	public void setNetworkPresence(Enum<NetworkPresence> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setNickname(java.lang.String)
	 */
	@Override
	public void setNickname(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setOrganizations(java.util.List)
	 */
	@Override
	public void setOrganizations(List<Organization> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setPets(java.lang.String)
	 */
	@Override
	public void setPets(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setPhoneNumbers(java.util.List)
	 */
	@Override
	public void setPhoneNumbers(List<ListField> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setPhotos(java.util.List)
	 */
	@Override
	public void setPhotos(List<ListField> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setPoliticalViews(java.lang.String)
	 */
	@Override
	public void setPoliticalViews(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setProfileSong(org.apache.shindig.social.opensocial.model.Url)
	 */
	@Override
	public void setProfileSong(Url arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setProfileUrl(java.lang.String)
	 */
	@Override
	public void setProfileUrl(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setProfileVideo(org.apache.shindig.social.opensocial.model.Url)
	 */
	@Override
	public void setProfileVideo(Url arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setQuotes(java.util.List)
	 */
	@Override
	public void setQuotes(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setRelationshipStatus(java.lang.String)
	 */
	@Override
	public void setRelationshipStatus(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setReligion(java.lang.String)
	 */
	@Override
	public void setReligion(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setRomance(java.lang.String)
	 */
	@Override
	public void setRomance(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setScaredOf(java.lang.String)
	 */
	@Override
	public void setScaredOf(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setSexualOrientation(java.lang.String)
	 */
	@Override
	public void setSexualOrientation(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setSmoker(org.apache.shindig.social.opensocial.model.Enum)
	 */
	@Override
	public void setSmoker(Enum<Smoker> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setSports(java.util.List)
	 */
	@Override
	public void setSports(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setStatus(java.lang.String)
	 */
	@Override
	public void setStatus(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setTags(java.util.List)
	 */
	@Override
	public void setTags(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setThumbnailUrl(java.lang.String)
	 */
	@Override
	public void setThumbnailUrl(String arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setTurnOffs(java.util.List)
	 */
	@Override
	public void setTurnOffs(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setTurnOns(java.util.List)
	 */
	@Override
	public void setTurnOns(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setTvShows(java.util.List)
	 */
	@Override
	public void setTvShows(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setUpdated(java.util.Date)
	 */
	@Override
	public void setUpdated(Date arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setUrls(java.util.List)
	 */
	@Override
	public void setUrls(List<Url> arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.apache.shindig.social.opensocial.model.Person#setUtcOffset(java.lang.Long)
	 */
	@Override
	public void setUtcOffset(Long arg0) {
		// TODO Auto-generated method stub

	}

}
