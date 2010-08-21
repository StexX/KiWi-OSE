/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */
package artaround.service.impl;

import java.io.File;
import java.io.IOException;

import javax.ejb.Stateless;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;


import artaround.action.utils.ImageScaleService;
import artaround.action.utils.PropertiesReader;
import artaround.datamodel.artwork.MultimediaFacade;
import artaround.service.ImageService;


/**
 * @author coster
 * @see artaround.service.ImageService
 */
@Name("imageService")
@Stateless
@Scope(ScopeType.STATELESS)
@AutoCreate
public class ImageServiceImpl implements ImageService {
	
	@Logger
	private Log log;

	/** 
	 * @see artaround.service.ImageService#createImageURL(artaround.datamodel.artwork.MultimediaFacade, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public String createImageURL(String filename,
			Integer width, Integer height) {
		
		//check if the image exists in the cache:
		
		if (filename == null){
			//logger error ...
			log.error("No filename stored in multimedia Facade!");
			return null;
		}
		
		//log.info(filename);
		
		//cache path:
		String cache = PropertiesReader.getProperty(PropertiesReader.FILE_CACHE);
		String urlServer =  PropertiesReader.getProperty(PropertiesReader.SERVER_URL);
		String folderServer = PropertiesReader.getProperty(PropertiesReader.IMAGES_DIRECTORY_ON_SERVER);
		
		//filename in cache = widthxheight_filename
		String cacheFilename = width+"x"+height+"_"+filename;
		String cacheURL = cache+"/"+cacheFilename;
		File cfile = new File(cacheURL);
		if (cfile.exists()){
			return urlServer+folderServer+cacheFilename;
		}
		
		//file not exists in cache -> create it:
		//repository path:
		String repos = PropertiesReader.getProperty(PropertiesReader.FILE_REPOSITORY);
		String reposURL = repos+filename;
		File rfile = new File(reposURL);
		if (!rfile.exists()){
			log.error("No file exists in file repository!", reposURL);
			return null;
		}
		
		try {
			ImageScaleService.createScaledFile(rfile, cfile, width, height, true);
		} catch (IOException e) {
			// logger
			log.error("Image scale error!", rfile);
			e.printStackTrace();
		}
		
		return urlServer+folderServer+cacheFilename;
	}

}
