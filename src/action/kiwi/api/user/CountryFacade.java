package kiwi.api.user;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;

/**
 * A KiWi facade for representing basic information about a country. May later be extended to cover
 * additional information, e.g. from the GeoNames ontology.
 * @author Sebastian Schaffert
 *
 */
@KiWiFacade
@RDFType({ Constants.NS_KIWI_CORE + "Country", Constants.NS_GEO + "SpatialThing" })
public interface CountryFacade extends ContentItemI {

	/**
	 * The name of the country. Mapped to kiwi:countryName.
	 * @return
	 */
	@RDF(Constants.NS_KIWI_CORE + "countryName")
	public String getName();
	
	public void setName(String name);
	
	/**
	 * The ISO code of the country. Mapped to kiwi:countryISOCode
	 * @return
	 */
	@RDF(Constants.NS_KIWI_CORE + "countryISOCode")
	public String getISOCode();
	
	public void setISOCode(String code);
}
