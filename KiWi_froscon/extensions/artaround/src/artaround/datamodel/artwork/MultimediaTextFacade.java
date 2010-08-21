/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.datamodel.artwork;

import kiwi.model.content.ContentItemI;
import java.util.Locale;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;

/**
 * @author coster
 * @version 0.1
 * media content descriptions in different languages
 */
@KiWiFacade
@RDFType(Constants.ART_AROUND_CORE + "MultimediaText")
public interface MultimediaTextFacade extends ContentItemI{

	/**
	 * @return the description
	 */
	@RDF(Constants.ART_AROUND_CORE + "description")	
	public String getDescription() ;
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description);
	/**
	 * @return the language
	 */
	@RDF(Constants.ART_AROUND_CORE + "language")	
	public Locale getLanguage();
	/**
	 * @param language the language to set
	 */
	public void setLanguage(Locale language) ;
	/**
	 * @return the mediaContent
	 */
	@RDF(Constants.ART_AROUND_CORE + "multimedia")	
	public MultimediaFacade getMultimedia() ;
	/**
	 * @param mediaContent the mediaContent to set
	 */
	public void setMultimedia(MultimediaFacade mediaContent) ;

}
