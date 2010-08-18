package kiwi.api.user;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;

import org.hibernate.validator.Range;

/**
 * A KiWi facade for representing basic data about cities. May later be extended to cover additional
 * city information.
 * 
 * @author Sebastian Schaffert
 *
 */
@KiWiFacade
@RDFType({ Constants.NS_GEO + "Point", Constants.NS_KIWI_CORE + "City" })
public interface CityFacade extends ContentItemI {

	/**
	 * The postal code of the city (relative to the country). Mapped to kiwi:cityPostalCode.
	 * @return
	 */
	@RDF(Constants.NS_KIWI_CORE + "cityPostalCode")
	public String getPostalCode();
	
	public void setPostalCode(String postalCode);
	
	/**
	 * The country of the city. Mapped to kiwi:inCountry, and returns a CountryFacade.
	 * @return
	 */
	@RDF(Constants.NS_KIWI_CORE + "inCountry")
	public CountryFacade getCountry();
	
	public void setCountry(CountryFacade country);
	
	/**
	 * The name of the city. Mapped to kiwi:cityName. TODO: should be the same as the title!
	 * @return
	 */
	@RDF(Constants.NS_KIWI_CORE + "cityName")
	public String getName();
	
	public void setName(String name);
	
	
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
