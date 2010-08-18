/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.action.utils;
/**
 * 
 */


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author coster
 * read properties from WEB-INF/webapp.properties
 */
public class PropertiesReader {
	
	private static final String PROPERTY_FILE = "artaround.properties";
	
	/** the file repository */
	public static final String FILE_REPOSITORY = "artaround.file_repository";
	/** cache in jboss web apps */
	public static final String FILE_CACHE = "artaround.file_cache";
	public static final String SERVER_URL = "artaround.server_url";
	/** image sizes */
	public static final String THUMB_SIZE_X = "artaround.image.thumbSize.x";
	public static final String THUMB_SIZE_Y = "artaround.image.thumbSize.y";
	public static final String PREVIEW_SIZE_X = "artaround.image.previewSize.x";
	public static final String PREVIEW_SIZE_Y = "artaround.image.previewSize.y";
	public static final String DETAIL_SIZE_X = "artaround.image.detailSize.x";
	public static final String DETAIL_SIZE_Y = "artaround.image.detailSize.y";
	/** image directory on server **/
	public static final String IMAGES_DIRECTORY_ON_SERVER = "artaround.img_file_dir";
	
	/**
	 * read the property value from artaround property file
	 * @param prop the property key
	 * @return the property value
	 */
	public static String getProperty(String prop){
		
		Logger logger = Logger.getLogger(PropertiesReader.class);
		
		Properties properties = new Properties() ;
		try {
			InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTY_FILE);
			properties.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
		String p = properties.getProperty(prop);
		//add slash if not exits:
		if (prop.equals(FILE_REPOSITORY) ||
				prop.equals(FILE_CACHE) ||
				prop.equals(SERVER_URL) ||
				prop.equals(IMAGES_DIRECTORY_ON_SERVER)){
			if (!p.endsWith("/") && !p.endsWith("\\")){
				p += "/";
			}
		}
		//create folders if not exists -> for file repository:
		if (prop.equals(FILE_REPOSITORY)){
			File f = new File(p);
			if (!f.exists()){
				if (!f.mkdir()){
					logger.error("Error creating folder: "+p);
				}
			}
		}
		//create folders if not exists -> for file cache:
		if (prop.equals(FILE_CACHE)){
			File f = new File(p);
			if (!f.exists()){
				if (!f.mkdir()){
					logger.error("Error creating folder: "+p);
				}
			}
		}
		return p.trim();
	}
	
}
