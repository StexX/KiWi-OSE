/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */


package artaround.datamodel.artwork;

import java.util.Locale;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;

/**
 * @author coster
 * text of an artwork in different languages
 */
@KiWiFacade
@RDFType(Constants.ART_AROUND_CORE + "ArtWorkText")
public interface ArtWorkTextFacade extends ContentItemI{
	
	/**
	 * the name of the artwork
	 * @return the name
	 */
	@RDF(Constants.ART_AROUND_CORE + "name")	
	public String getName();
	/**
	 * @param name the name to set
	 */
	public void setName(String name);

	/**
	 * @return the artwork
	 */
	@RDF(Constants.ART_AROUND_CORE + "artwork")	
	public ArtWorkFacade getArtwork();
	/**
	 * @param artwork the artwork to set
	 */
	public void setArtwork(ArtWorkFacade artwork);
	/**
	 * @return the language
	 */
	@RDF(Constants.ART_AROUND_CORE + "language")	
	public Locale getLanguage() ;
	/**
	 * @param language the language to set
	 */
	public void setLanguage(Locale language) ;
	/**
	 * the description of the artwork
	 * @return the description
	 */
	@RDF(Constants.ART_AROUND_CORE + "description")	
	public String getDescription();
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description);

}
