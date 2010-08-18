/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.datamodel.artwork;

import java.util.Date;
import java.util.LinkedList;

import org.hibernate.validator.Range;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;
import kiwi.model.ontology.SKOSConcept;

/**
 * @author Rolf Sint
 * 
 * Das ist die Facade eines Kunstwerks. Allgemeine Eigenschaften werden vom ContentItemI
 * geerbt, und müssen  nicht mehr explizit angegeben werden. Dazu gehören beispielsweise der Titel, 
 * der Author, die Tags, die Language uvm. Derzeit unterstützen die Facades noch nicht alle Datentypen, wie. z.B. numerics, ..
 */

@KiWiFacade
@RDFType(Constants.ART_AROUND_CORE + "ArtWork")
public interface ArtWorkFacade extends ContentItemI{
	
	@RDF(Constants.ART_AROUND_CORE + "authorName")
	public String getAuthorName();
	
	public String setAuthorName(String authorName);
	
	@RDF(Constants.ART_AROUND_CORE + "techniques")
	public LinkedList<SKOSConcept> getTechniques();
	
	public String setTechniques(LinkedList<SKOSConcept> technique);
		
	@RDF(Constants.ART_AROUND_CORE + "isPublic")
	public boolean getPublicAccess();

	public void setPublicAccess(boolean publicAccess);
	
	@RDF(Constants.ART_AROUND_CORE + "description")
	public String getDescription();
	
	public String setDescription(String description);
	
	@RDF(Constants.ART_AROUND_CORE + "sellingState")
	public boolean getSellingState();
	
	public String setSellingState(boolean sellingState);
	
	@RDF(Constants.ART_AROUND_CORE + "location")
	public String getLocation();
	
	public String setLocation(String location);
	
	@RDF(Constants.ART_AROUND_CORE + "price")
	public String getPrice();
	
	public String setPrice(String price);
	
	@RDF(Constants.ART_AROUND_CORE + "currency")
	public String getCurrency();
	
	public String setCurrency(String currency);
	
	//Alle Daten eines ArtAround Kunden
	
	@RDF(Constants.ART_AROUND_CORE + "customerFirstName")
	public String getCustomerFirstName();
	
	public String setCustomerFirstName(String customerFirstName);
	
	@RDF(Constants.ART_AROUND_CORE + "customerLastName")
	public String getCustomerLastName();
	
	public String setCustomerLastName(String customerLastName);
	
	@RDF(Constants.ART_AROUND_CORE + "customerStreet")
	public String getCustomerStreet();
	
	public String setCustomerStreet(String customerStreet);
	
	@RDF(Constants.ART_AROUND_CORE + "customerZip")
	public String getCustomerZip();
	
	public String setCustomerZip(String customerZip);
	
	@RDF(Constants.ART_AROUND_CORE + "customerCity")
	public String getCustomerCity();
	
	public String setCustomerCity(String customerCity);
	
	@RDF(Constants.ART_AROUND_CORE + "customerState")
	public String getCustomerState();
	
	public String setCustomerState(String customerState);
	
	@RDF(Constants.ART_AROUND_CORE + "customerEmail")
	public String getCustomerEmail();
	
	public String setCustomerEmail(String customerEmail);
	
	@RDF(Constants.ART_AROUND_CORE + "customerPhone")
	public String getCustomerPhone();
	
	public String setCustomerPhone(String customerPhone);
	
	@RDF(Constants.ART_AROUND_CORE + "customerIsMember")
	public boolean getCustomerIsMember();
	
	public void setCustomerIsMember(boolean customerIsMember);
	
	//Alle Bilder und Videos die zu einem bestimmten Kunstwerk geh�ren
	@RDF(Constants.ART_AROUND_CORE + "artAroundMediaList")
	LinkedList<MultimediaFacade> getArtAroundMediaList();	
	void setArtAroundMediaList(LinkedList<MultimediaFacade> multimediaFacades);

	/**
	 * @return the viewCounter
	 */
	@RDF(Constants.ART_AROUND_CORE + "viewCounter")	
	public Integer getViewCounter();
	/**
	 * @param viewCounter the viewCounter to set
	 */
	public void setViewCounter(Integer viewCounter);
	
	//TODO Donation
	
	/** translations of the artwork */
	@RDF(Constants.ART_AROUND_CORE + "artWorkTextList")
	LinkedList<ArtWorkTextFacade> getArtWorkTextList();	
	void setArtWorkTextList(LinkedList<ArtWorkTextFacade> multimediaFacades);
	
	/** the selling date */
	@RDF(Constants.ART_AROUND_CORE + "sellingDate")	
	public Date getSellingDate();
	public void setSellingDate(Date soldDate);
	
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

}