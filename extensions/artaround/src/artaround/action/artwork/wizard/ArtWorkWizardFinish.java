/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder
 */
package artaround.action.artwork.wizard;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@Name("artWorkWizardFinish")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class ArtWorkWizardFinish {
	

	public String finish() {
				
		
		
//		if(artaroundMediaList != null){
//		// persistiere
//		for (MultimediaFacade m : artaroundMediaList) {
//			kiwiEntityManager.persist(m);
//		}}
//	
//		// merge() is EVIL!!!
//		log.info((artWork == null)?"ArtWork is null":"ArtWork is not null");
//		artWork.setAuthor(currentUser);
//		kiwiEntityManager.persist(artWork);
//
//		artWork.setArtAroundMediaList(artaroundMediaList);
//		//kiwiEntityManager.refresh(artWork);


		Events.instance().raiseEvent("onSaveEvent");

		return "/artaround/pages/profil/meinProfil.xhtml";
	}
	
//	final ContentItem artaroundMediaTmp = contentItemService
//	.createContentItem();
//MultimediaFacade artaroundMedia = kiwiEntityManager.createFacade(
//	artaroundMediaTmp, MultimediaFacade.class);
//
//artaroundMedia.setFilename(name);
//artaroundMedia.setMimeType(type);
	
}
