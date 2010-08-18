/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.datamodel.events;

import java.util.Date;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;

/**
 *
 * @author coster
 * @version 0.1
 * 
 * events of a user
 */
@KiWiFacade
@RDFType(Constants.ART_AROUND_CORE + "ArtWork")
public interface EventFacade extends ContentItemI{	
	
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
	 * @return the name
	 */
	@RDF(Constants.ART_AROUND_CORE + "name")		
	public String getName();
	/**
	 * @param name the name to set
	 */
	public void setName(String name) ;
	/**
	 * @return the datetime
	 */
	@RDF(Constants.ART_AROUND_CORE + "datetime")		
	public Date getDatetime();
	/**
	 * @param datetime the datetime to set
	 */
	public void setDatetime(Date datetime);

}
