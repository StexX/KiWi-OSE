/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder
 */
package artaround.action.artwork;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;

import org.apache.commons.io.FilenameUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import artaround.action.artwork.techniqueTree.TechniqueAction;
import artaround.action.utils.FileService;
import artaround.action.utils.ImageScaleService;
import artaround.action.utils.PropertiesReader;
import artaround.datamodel.artwork.ArtWorkFacade;
import artaround.service.ArtWorkService;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@Name("artWorkEditAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class ArtWorkEditAction {
	
	@In
	private ArtWorkFacade selectedArtWork;
	
	@In
	private ContentItemService contentItemService;
	
	@In(create = true)
	private ArtWorkBean artWorkBean;
	
	@In(required = false)
	private TechniqueAction techniqueAction;
	
	@In
    private KiWiEntityManager kiwiEntityManager;
	
	@In
	private ArtWorkService artWorkService;
	
	@Logger
	private static Log log;
	
	private int uploadsAvailable = 5;
	
	
	public int getUploadsAvailable() {
		return uploadsAvailable;
	}

	public void setUploadsAvailable(int uploadsAvailable) {
		this.uploadsAvailable = uploadsAvailable;
	}
	
	

	//	private String sortCriteria;
//
//	@org.jboss.seam.annotations.Create
//	public void init() {
//		if (selectedArtWork != null) {
//			artaroundMediaList = selectedArtWork.getArtAroundMediaList();
//		}
//
//		else {
//			artaroundMediaList = new LinkedList<MultimediaFacade>();
//		}
//
//	}
//
//	@End
//	public void delete() {
//		tripleStore.remove(selectedMultimedia);
//		artaroundMediaList.remove(selectedMultimedia);
//
//	}
//
//	
	public void deletePictureOfArtwork(MediaTmp pic) {		
		artWorkBean.getArtAroundMediaList().remove(pic);
		artWorkService.deleteMultimediaFromArtWork(selectedArtWork, pic);
	}
	
	public void listener(UploadEvent event) {

		UploadItem item = event.getUploadItem();

		log.info("File: '#0' with type '#1' was uploaded", item.getFileName(),
				item.getContentType());

		String name = FilenameUtils.getName(item.getFileName());
		String type = item.getContentType();
		// byte[] data = item.getData();
		// byte[] data = null;

		if (item.isTempFile()) {
			try {
				// data = FileUtils.readFileToByteArray(item.getFile());

				File file = item.getFile();

				String repos = PropertiesReader
						.getProperty(PropertiesReader.FILE_REPOSITORY);
				String cache = PropertiesReader.getProperty(PropertiesReader.FILE_CACHE);

				// store the orig in file repos:
				try {
					// create unique filename with timestamp+filename:
					name = "" + new Date().getTime() + "_" + name;
					FileService.copyFile(file, new File(repos + "/" + name));
				} catch (Exception e) {
					log.error("error copy file ... " + e.getMessage());
					e.printStackTrace();
				}

				// create the mini thumbnail image:
				//TODO: Werte aus property file auslesen
				Integer width = new Integer(100);
				Integer height = new Integer(75);
				File mini = new File(cache + "/" + width + "x" + height + "_"
						+ name);
				ImageScaleService.createScaledFile(file, mini, width, height, true);
				
				// create the thumbnail image:
				width = new Integer(PropertiesReader
						.getProperty(PropertiesReader.THUMB_SIZE_X));
				height = new Integer(PropertiesReader
						.getProperty(PropertiesReader.THUMB_SIZE_Y));
				File thumb = new File(cache + "/" + width + "x" + height + "_"
						+ name);
				ImageScaleService.createScaledFile(file, thumb, width, height, true);
				
				// create the mini preview image:
				width = new Integer(280);
				height = new Integer(150);
				File miniprev = new File(cache + "/" + width + "x" + height + "_"
						+ name);
				ImageScaleService.createScaledFile(file, miniprev, width, height, true);

				// create the preview image:
				width = new Integer(PropertiesReader
						.getProperty(PropertiesReader.PREVIEW_SIZE_X));
				height = new Integer(PropertiesReader
						.getProperty(PropertiesReader.PREVIEW_SIZE_Y));
				File prev = new File(cache + "/" + width + "x" + height + "_"
						+ name);
				ImageScaleService.createScaledFile(file, prev, width, height, true);

				// create the detail image:
				width = new Integer(PropertiesReader
						.getProperty(PropertiesReader.DETAIL_SIZE_X));
				height = new Integer(PropertiesReader
						.getProperty(PropertiesReader.DETAIL_SIZE_Y));
				File detail = new File(cache + "/" + width + "x" + height + "_"
						+ name);
				ImageScaleService.createScaledFile(file, detail, width, height, true);

			} catch (IOException e) {
				log.error("error reading file #0", item.getFile()
						.getAbsolutePath());
			}
		}
		
		MediaTmp mt = new MediaTmp();
		mt.setMimeType(type);
		mt.setFileName(name);
		
		LinkedList<MediaTmp> artWorkList = artWorkBean.getArtAroundMediaList();
		if(artWorkList == null){
			artWorkList = new LinkedList<MediaTmp>();
		}
		
		artWorkList.add(mt);
		artWorkBean.setArtAroundMediaList(artWorkList);
	}
	
	public String changeArtWork() {
		
		selectedArtWork.setTechniques(techniqueAction.getChosenConcepts());
				
//		contentItemService.updateTitle(selectedArtWork, artWorkBean.getArtWorkName());
//		
//		//selectedArtWork.setArtAroundMediaList(artWorkBean.getArtAroundMediaList());
//		selectedArtWork.setArtWorkTextList(artWorkBean.getArtWorkTextList());		
//		selectedArtWork.setSellingState(artWorkBean.getSellingState());
//		selectedArtWork.setAuthorName(artWorkBean.getAuthorName());
//		selectedArtWork.setDescription(artWorkBean.getDescription());
//		selectedArtWork.setPrice(artWorkBean.getPrice());
//		selectedArtWork.setCurrency(artWorkBean.getCurrency());		
//		selectedArtWork.setTechniques(techniqueAction.getChosenConcepts());
		
//		Date d = new Date();
//		selectedArtWork.setModified(d);
		
		artWorkService.updateArtWork(selectedArtWork, artWorkBean);
		
//		kiwiEntityManager.persist(selectedArtWork);
		return "/artaround/pages/profil/meinProfil.xhtml";
	}	
}
