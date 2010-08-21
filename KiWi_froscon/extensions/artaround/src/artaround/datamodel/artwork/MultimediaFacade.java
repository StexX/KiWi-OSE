/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.datamodel.artwork;

import java.util.LinkedList;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;

/**
 * @author Rolf Sint
 *
 */
@KiWiFacade
@RDFType(Constants.ART_AROUND_CORE + "Multimedia")
public interface MultimediaFacade extends ContentItemI{
	
	//beschreibt ob das ausgewählte Bild das gesamte Kunstwerk repräsentieren soll
	@RDF(Constants.ART_AROUND_CORE + "isMaster")
	public boolean isMaster();
	
	/**
	 * @return the mediaContentText
	 */
	@RDF(Constants.ART_AROUND_CORE + "multimediaText")	
	public LinkedList<MultimediaTextFacade> getMultimediaText();
	/**
	 * @param mediaContentText the mediaContentText to set
	 */
	public void setMultimediaText(LinkedList<MultimediaTextFacade> mediaContentText);
	/**
	 * @return the artwork
	 */
	@RDF(Constants.ART_AROUND_CORE + "artwork")	
	public ArtWorkFacade getArtwork();
	/**
	 * @param artwork the artwork to set
	 */
	public void setArtwork(ArtWorkFacade artwork) ;
	/**
	 * the mime type of the media
	 * @return the mimeType
	 */
	@RDF(Constants.ART_AROUND_CORE + "mimeType")	
	public String getMimeType() ;
	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) ;
	/**
	 * the filename of the media
	 * @return the filename
	 */
	@RDF(Constants.ART_AROUND_CORE + "filename")	
	public String getFilename() ;
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) ;
	/**
	 * set to true if the media content has public access
	 * @return the publicAccess
	 */
	@RDF(Constants.ART_AROUND_CORE + "isPublicAccess")	
	public Boolean getPublicAccess() ;
	/**
	 * @param publicAccess the publicAccess to set
	 */
	public void setPublicAccess(Boolean publicAccess) ;
	/**
	 * the width of the picture or video
	 * @return the width
	 */
	@RDF(Constants.ART_AROUND_CORE + "width")	
	public Integer getWidth();
	/**
	 * @param width the width to set
	 */
	public void setWidth(Integer width) ;
	/**
	 * the height of the picture or video
	 * @return the height
	 */
	@RDF(Constants.ART_AROUND_CORE + "height")	
	public Integer getHeight();
	/**
	 * @param height the height to set
	 */
	public void setHeight(Integer height) ;
}
