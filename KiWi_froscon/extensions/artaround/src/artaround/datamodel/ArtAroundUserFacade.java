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
package artaround.datamodel;

import java.util.Date;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@KiWiFacade
@RDFType({Constants.ART_AROUND_CORE+ "ArtAroundUser",Constants.NS_FOAF+"Person"})
public interface ArtAroundUserFacade extends ContentItemI{

	@RDF(Constants.ART_AROUND_CORE + "gebDate")
	public Date getGebDate();

	public void setGebDate(Date gebDate);

	@RDF(Constants.ART_AROUND_CORE + "closed")
	public boolean getClosed();

	public void setClosed(boolean closed);
	
	@RDF(Constants.ART_AROUND_CORE + "member")
	public int getMember();

	public void setMember(int member);

	@RDF(Constants.ART_AROUND_CORE + "userType")
	public int getUserType();

	public void setUserType(int userType);
	
	@RDF(Constants.ART_AROUND_CORE + "placeOfBirth")
	public String getPlaceOfBirth();
	
	public void setPlaceOfBirth(String placeOfBirth);
	
	@RDF(Constants.ART_AROUND_CORE + "gender")
	public String getGender();
	
	public void setGender(String gender);
	
	@RDF(Constants.ART_AROUND_CORE + "postalAdressStreetAndNumber")
	public String getPostalAdressStreetAndNumber();
	
	public void setPostalAdressStreetAndNumber(String postalAdressStreetAndNumber);
	
	@RDF(Constants.ART_AROUND_CORE + "postalAdressZipCode")
	public String getPostalAdressZipCode();
	
	public void setPostalAdressZipCode(String postalAdressZipCode);
	
	@RDF(Constants.ART_AROUND_CORE + "getPostalAdressCountryCode")
	public String getPostalAdressCountryCode();
	
	public void setPostalAdressCountryCode(String postalAdressCountryCode);
	
	@RDF(Constants.ART_AROUND_CORE + "mobilePhoneNumber")
	public String getMobilePhoneNumber();
	
	public void setMobilePhoneNumber(String mobilePhoneNumber);
	
	@RDF(Constants.ART_AROUND_CORE + "privatePhoneNumber")
	public String getPrivatePhoneNumber();
	
	public void setPrivatePhoneNumber(String privatePhoneNumber);
	
	@RDF(Constants.ART_AROUND_CORE + "officePhoneNumber")
	public String getOfficePhoneNumber();
	
	public void setOfficePhoneNumber(String officePhoneNumber);
	
	@RDF(Constants.ART_AROUND_CORE + "skypePhoneNumber")
	public String getSkypePhoneNumber();
	
	public void setSkypePhoneNumber(String skypePhoneNumber);
	
	@RDF(Constants.ART_AROUND_CORE + "facebook")
	public String getFacebook();
	
	public void setFacebook(String facebook);
	
	@RDF(Constants.ART_AROUND_CORE + "twitter")
	public String getTwitter();
	
	public void setTwitter(String twitter);
	
	@RDF(Constants.ART_AROUND_CORE + "about")
	public String getAbout();
	
	public void setAbout(String about);
	
	@RDF(Constants.ART_AROUND_CORE + "city")
	public String getPostalAdressCity();
	
	public void setPostalAdressCity(String postalAdressCity);
	
	
	public static final int BASIC_MEMBER = 1;
	
	public static final int ADVANCED_MEMBER = 1;
	
	
	
}
