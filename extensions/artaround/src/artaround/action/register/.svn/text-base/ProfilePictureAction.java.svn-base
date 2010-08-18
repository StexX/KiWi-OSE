package artaround.action.register;

import java.io.IOException;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.multimedia.MultimediaService;
import kiwi.model.content.MediaContent;
import kiwi.model.user.User;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import artaround.datamodel.ArtAroundUserFacade;

@Name("artaround.profilePictureAction")
@Scope(ScopeType.CONVERSATION)
//@Transactional
@AutoCreate
public class ProfilePictureAction {

	
    @Logger
    private Log log;
    
	@In
	private MultimediaService multimediaService;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
    
	@In(required = false)
	private User currentUser;

	
	public kiwi.model.content.MediaContent upload(UploadEvent event) {

		UploadItem item = event.getUploadItem();
		
		
		log.info(item.getFileName() == null?"no data":"data exists");
		log.info(item.getData() == null?"no data":"data exists");
		
		
		log.info("File: '#0' with type '#1' was uploaded", item.getFileName(),
				item.getContentType());
		
		byte [] data = item.getData();
		if (item.isTempFile()) {
			try {
				data = FileUtils.readFileToByteArray(item.getFile());
			} catch (IOException e) {
				log.error("error reading file #0", item.getFile()
						.getAbsolutePath());
			}
		}
		
		String name = FilenameUtils.getName(item.getFileName());
		String type = item.getContentType();
		
		type = multimediaService.getMimeType(name, data);
		

		log.info(data == null?"no data":"data exists"+data.length);
		
		
		MediaContent profilePicture = new kiwi.model.content.MediaContent();
		profilePicture.setData(data);		
		profilePicture.setFileName(name);
		profilePicture.setMimeType(type);
		return profilePicture;		
	}
	
	public void listener(UploadEvent event) {		
		MediaContent profilePicture = upload(event);
		kiwiEntityManager.persist(profilePicture);
		
		final ArtAroundUserFacade userFacade = kiwiEntityManager.createFacade(currentUser.getContentItem(), ArtAroundUserFacade.class);
		userFacade.setMediaContent(profilePicture);
		kiwiEntityManager.persist(userFacade);
	}
}
