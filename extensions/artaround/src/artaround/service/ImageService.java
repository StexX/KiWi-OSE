/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.service;

import artaround.datamodel.artwork.MultimediaFacade;

/**
 * @author coster
 * Service to handle images
 */
public interface ImageService {
	
	/**
	 * creates the URL of an image
	 * @param multimediaFacade
	 * @return the URL of an image
	 */
	public String createImageURL(String fileName, Integer width, Integer height);

}
