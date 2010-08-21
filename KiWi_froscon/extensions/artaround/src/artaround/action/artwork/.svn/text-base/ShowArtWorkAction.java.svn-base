/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder
 */
package artaround.action.artwork;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.equity.EquityService;
import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import artaround.action.utils.ImageScaleService;
import artaround.action.utils.PropertiesReader;
import artaround.datamodel.artwork.ArtWorkFacade;
import artaround.datamodel.artwork.MultimediaFacade;
import artaround.service.ArtWorkService;
import artaround.service.ImageService;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 * 
 */
@Name("showArtWorkAction")
@Scope(ScopeType.CONVERSATION)
public class ShowArtWorkAction {

	/** injection of the image service to get the URL of images */
	@In(create = true)
	private ImageService imageService;

	@In(required = false)
	private ArtWorkService artWorkService;
	
	@In
	private EquityService equityService;
	
	@Logger
	private Log log;
	
	//ArtWorks From User
	private List<ArtWorkFacade> artWorks;

	//All ArtWorks
	private List<ArtWorkFacade> allArtWorks;
	
	@In(create = true)
	private ArtWorkBean artWorkBean;
	
	@In
    private KiWiEntityManager kiwiEntityManager;
	
	@Out(required = false)
	private ArtWorkFacade selectedArtWork;
	
	/**
	 * @param artWork
	 * @return Html Link
	 */
	//Setting here the current ArtWork (selectedArtWor) to be modified in the EditAction
	@Begin(join=true)
	public String view(ArtWorkFacade artWork){
		 log.info("Artworktitel: #0",artWork.getTitle());
		 artWorkBean.init(artWork);	
		 selectedArtWork = artWork;
		 return "/artaround/pages/artworks/imagesOfArtwork.xhtml";
	}
	
	public String selectArtWork(ArtWorkFacade artWork){
		 log.info("Artworktitel: #0",artWork.getTitle());
		 artWorkBean.init(artWork);	
		 selectedArtWork = artWork;
		 return "/artaround/pages/frontend/artWorkDetails.xhtml";
	}
	
	public String buyArtWork(){
		 
		 return "/artaround/pages/frontend/buyArtwork.xhtml";
	}
	
	public void deleteArtWork(ArtWorkFacade artWork){
		kiwiEntityManager.remove(artWork.getDelegate());
		artWorks.remove(artWork);	
	}
	
	
	/**
	 * get thumbnail URL of all images in database
	 * 
	 * @return thumbnail URLs of all images in database
	 */
	public List<String> getArtWorkThumbURLs() {

		List<String> filenames = new Vector<String>();

		// FIXME: entferne iterationen �ber alle bilder eines kunstwerks!
		List<ArtWorkFacade> artworks = getArtWorks();
		for (ArtWorkFacade artwork : artworks) {
			List<MultimediaFacade> multimedias = artwork
					.getArtAroundMediaList();
			for (MultimediaFacade multimedia : multimedias) {
				// FIXME: if (multimedia.isMaster()){
				String url = imageService.createImageURL(multimedia.getFilename(),
						new Integer(PropertiesReader
								.getProperty(PropertiesReader.THUMB_SIZE_X)),
						new Integer(PropertiesReader
								.getProperty(PropertiesReader.THUMB_SIZE_Y)));
				if (url != null) {
					filenames.add(url);
				}
				// }
			}
		}

		return filenames;

	}

	/**
	 * return the height of the wrapper div of a single artwork thumbnail
	 * 
	 * @return the height of the wrapper div of a single artwork thumbnail
	 */
	public String getArtWorkWrapHeight() {
		Integer y = new Integer(PropertiesReader
				.getProperty(PropertiesReader.THUMB_SIZE_Y));
		y = (y * 120) / 100;
		return "" + y;
	}

	/**
	 * return the width of the wrapper div of a single artwork thumbnail
	 * 
	 * @return the width of the wrapper div of a single artwork thumbnail
	 */
	public String getArtWorkWrapWidth() {
		Integer x = new Integer(PropertiesReader
				.getProperty(PropertiesReader.THUMB_SIZE_X));
		x = (x * 120) / 100;
		return "" + x;
	}

	/**
	 * return the height of the wrapper div of a single artwork thumbnail
	 * 
	 * @return the height of the wrapper div of a single artwork thumbnail
	 */
	public String getArtWorkPrevHeight() {
		Integer y = new Integer(PropertiesReader
				.getProperty(PropertiesReader.PREVIEW_SIZE_Y));
		return "" + y;
	}

	/**
	 * return the width of the wrapper div of a single artwork thumbnail
	 * 
	 * @return the width of the wrapper div of a single artwork thumbnail
	 */
	public String getArtWorkPrevWidth() {
		Integer x = new Integer(PropertiesReader
				.getProperty(PropertiesReader.PREVIEW_SIZE_X));
		return "" + x;
	}

	// TODO: implement query for the master image
	// //@End
	public String getMasterFileNameFromArtwork(ArtWorkFacade a) {
		LinkedList<MultimediaFacade> ll = new LinkedList<MultimediaFacade>();
		ll = a.getArtAroundMediaList();

		if (ll.size() > 0) {
			MultimediaFacade mf = ll.getFirst();
			MediaTmp mTmp = new MediaTmp(mf.getMimeType(), mf.getFilename());
			return getMultimediaFilename(mTmp);
		}
		return null;
	}

	public String getPreviewMasterFileNameFromArtwork(ArtWorkFacade a) {
		LinkedList<MultimediaFacade> ll = new LinkedList<MultimediaFacade>();
		ll = a.getArtAroundMediaList();

		if (ll.size() > 0) {
			MultimediaFacade mf = ll.getFirst();
			MediaTmp mTmp = new MediaTmp(mf.getMimeType(), mf.getFilename());	
			return getPreviewMultimediaFilename(mTmp);
		}
		return null;

	}

	public String getThumbMasterFileNameFromArtwork(ArtWorkFacade a) {

		log.info(a == null ? "null value" : "ok");

		LinkedList<MultimediaFacade> ll = new LinkedList<MultimediaFacade>();
		ll = a.getArtAroundMediaList();
		
		
		
		if (ll.size() > 0) {
			MultimediaFacade mf = ll.getFirst();
			MediaTmp mTmp = new MediaTmp(mf.getMimeType(), mf.getFilename());	
			return getThumbMultimediaFilename(mTmp);
		}
		return null;

	}
	
	public String getThumbMasterFileNameFromArtwork(ContentItem ci) {
		ArtWorkFacade a = kiwiEntityManager.createFacade(ci,ArtWorkFacade.class);
		
		LinkedList<MultimediaFacade> ll = new LinkedList<MultimediaFacade>();
		ll = a.getArtAroundMediaList();
		
		if (ll.size() > 0) {
			MultimediaFacade mf = ll.getFirst();
			MediaTmp mTmp = new MediaTmp(mf.getMimeType(), mf.getFilename());	
			return getThumbMultimediaFilename(mTmp);
		}
		return null;

	}


	
	//
	public String getMultimediaFilename(MediaTmp multimedia) {
		Integer width = new Integer(PropertiesReader
				.getProperty(PropertiesReader.DETAIL_SIZE_X));
		Integer height = new Integer(PropertiesReader
				.getProperty(PropertiesReader.DETAIL_SIZE_Y));
		return imageService.createImageURL(multimedia.getFileName(), width, height);
	}
	
	//
	public String getPreviewMultimediaFilename(MediaTmp multimedia) {
		Integer width = new Integer(PropertiesReader
				.getProperty(PropertiesReader.PREVIEW_SIZE_X));
		Integer height = new Integer(PropertiesReader
				.getProperty(PropertiesReader.PREVIEW_SIZE_Y));
		return imageService.createImageURL(multimedia.getFileName(), width, height);
	}
	//
	public String getThumbMultimediaFilename(MediaTmp multimedia) {
		Integer width = new Integer(PropertiesReader
				.getProperty(PropertiesReader.THUMB_SIZE_X));
		Integer height = new Integer(PropertiesReader
				.getProperty(PropertiesReader.THUMB_SIZE_Y));
		return imageService.createImageURL(multimedia.getFileName(), width, height);
	}

	public String getMiniMultimediaFilename(MediaTmp multimedia) {
		// TODO Werte von Property File auslesen
		Integer width = new Integer(100);
		Integer height = new Integer(75);
		return imageService.createImageURL(multimedia.getFileName(), width, height);
	}

	public String getMiniPreviewMultimediaFilename(MediaTmp multimedia) {
		// TODO Werte von Property File auslesen
		Integer width = new Integer(280);
		Integer height = new Integer(150);
		return imageService.createImageURL(multimedia.getFileName(), width, height);
	}

	public List<ArtWorkFacade> getAllArtWorks() {
		allArtWorks = artWorkService.getAllArtWorks();
		return allArtWorks;
	}

	public List<ArtWorkFacade> getArtWorks() {
		artWorks = artWorkService.getArtWorks();
		return artWorks;
	}
	
	public double getEquityValue(ContentItem ci){
		return equityService.getContentItemEquity(ci);
	}
}
