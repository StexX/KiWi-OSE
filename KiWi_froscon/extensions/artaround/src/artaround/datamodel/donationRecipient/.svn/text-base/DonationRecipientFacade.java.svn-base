/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.datamodel.donationRecipient;
import java.util.LinkedList;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import artaround.datamodel.artwork.ArtWorkFacade;
import kiwi.model.content.ContentItemI;

/**
 * @author coster
 * @version 0.1
 *
 * recipient of donations
 *
 */
@KiWiFacade
@RDFType(Constants.ART_AROUND_CORE + "ArtWork")
public interface DonationRecipientFacade extends ContentItemI{	

	/**
	 * the artworks of this donationRecipient
	 * @return the artworkToDonationRecipient
	 */
	@RDF(Constants.ART_AROUND_CORE + "artworkList")	
	public LinkedList<ArtWorkFacade> getArtworks() ;
	/**
	 * @param artworkToDonationRecipient the artworkToDonationRecipient to set
	 */
	public void setArtwork(
			LinkedList<ArtWorkFacade> artworks) ;

	/**
	 * @return the contact
	 */
	@RDF(Constants.ART_AROUND_CORE + "contact")	
	public String getContact() ;
	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) ;
	/**
	 * @return the name
	 */
	@RDF(Constants.ART_AROUND_CORE + "name")	
	public String getName() ;
	/**
	 * @param name the name to set
	 */
	public void setName(String name);
	/**
	 * @return the eMail
	 */
	@RDF(Constants.ART_AROUND_CORE + "email")	
	public String geteMail();
	/**
	 * @param eMail the eMail to set
	 */
	public void seteMail(String eMail) ;
	/**
	 * @return the active
	 */
	@RDF(Constants.ART_AROUND_CORE + "isActive")	
	public Boolean getActive();
	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active);
	/**
	 * @return the street
	 */
	@RDF(Constants.ART_AROUND_CORE + "street")	
	public String getStreet();
	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) ;
	/**
	 * @return the postcode
	 */
	@RDF(Constants.ART_AROUND_CORE + "postcode")	
	public String getPostcode();
	/**
	 * @param postcode the postcode to set
	 */
	public void setPostcode(String postcode);
	/**
	 * @return the city
	 */
	@RDF(Constants.ART_AROUND_CORE + "city")	
	public String getCity();
	/**
	 * @param city the city to set
	 */
	public void setCity(String city);
	/**
	 * @return the isoCountryCode3
	 */
	@RDF(Constants.ART_AROUND_CORE + "countryCode")	
	public String getIsoCountryCode3();
	/**
	 * @param isoCountryCode3 the isoCountryCode3 to set
	 */
	public void setIsoCountryCode3(String isoCountryCode3);
	/**
	 * @return the phone
	 */
	@RDF(Constants.ART_AROUND_CORE + "phone")	
	public String getPhone();
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) ;
	/**
	 * @return the mobilePhone
	 */
	@RDF(Constants.ART_AROUND_CORE + "mobile")	
	public String getMobilePhone();
	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public void setMobilePhone(String mobilePhone);
	/**
	 * @return the fax
	 */
	@RDF(Constants.ART_AROUND_CORE + "fax")	
	public String getFax() ;
	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax);
	/**
	 * @return the eMail
	 */
	@RDF(Constants.ART_AROUND_CORE + "email")	
	public String getEMail() ;
	/**
	 * @param mail the eMail to set
	 */
	public void setEMail(String mail);
	/**
	 * @return the web
	 */
	@RDF(Constants.ART_AROUND_CORE + "web")	
	public String getWeb();
	/**
	 * @param web the web to set
	 */
	public void setWeb(String web);

}
