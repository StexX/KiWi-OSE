/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder
 */

package artaround.action.artwork.wizard;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import artaround.action.artwork.ArtWorkBean;
import artaround.action.artwork.techniqueTree.TechniqueAction;


/**
 * @author Rolf Sint
 * @version 1.0
 * @since 14.March 2010
 */

/*
 * This class returns all <b>artworks</b>
 * contains functionalities for
 * <ol>
 * <li>tag an artwork
 * <li>buy an artwork
 * <li>delete an artwork
 * <li>get artworks
 * <li>calculate the dates to an artwork
 * </ol>
 */

@Name("artWorkAction")
@Scope(ScopeType.CONVERSATION)
public class ArtWorkAction implements Serializable {

	private static final long serialVersionUID = 7482822342778616612L;

	
	@In(required = false)
	private TechniqueAction techniqueAction;
	
	@In(create = true)
	private ArtWorkBean artWorkBean;
	
        
    @Logger
    private Log log;
    
//	@In(required = false)
//	@Out(required = false)
//	private ArtWorkFacade selectedArtWork;
//	

			
		
//	@Begin(join = true)
//	public String selectArtWork(ArtWorkFacade artWork){
//		selectedArtWork = artWork;	
//		return "/artaround/pages/frontend/artWorkDetails.xhtml";
//	}
//	
//	public String selectArtWorkAuthor(ArtWorkFacade artWork){		
//		selectedArtWork = artWork;	
//		return "/artaround/pages/frontend/authorDetails.xhtml";
//	}
	
    public String neuesKunstwerk(){
    	//clear all values which are curently in the ArtWorkbean
    	artWorkBean.clear();
    	return "neuesKunstwerk";
    }
	
	public String buyArtWork(){
		return "/artaround/pages/frontend/buyArtwork.xhtml";
	}
	
	public String showArtWorksLastUploaded(){
		return "/artaround/pages/frontend/artWorks.xhtml";
	}

	
	public String goToHome(){
		return "/artaround/home.seam";
		
	}
	
	@Begin(join=true)
	public String next() {
		log.info("Going to the next Step ...");
		return "/artaround/pages/artworks/uploadMedia";
	}
	
//	public String buyArtWork(){
//		
//		selectedArtWork.setCustomerFirstName(customerFirstName);
//		selectedArtWork.setCustomerLastName(customerLastName);
//		selectedArtWork.setCustomerStreet(customerStreet);
//		selectedArtWork.setCustomerZip(customerZip);
//		selectedArtWork.setCustomerCity(customerCity);
//		selectedArtWork.setCustomerState(customerState);
//		selectedArtWork.setCustomerEmail(customerEmail);
//		selectedArtWork.setCustomerPhone(customerPhone);
//		selectedArtWork.setCustomerIsMember(customerIsMember);
//		
//		Date d = new Date();
//		selectedArtWork.setSellingDate(d);
//		
//		kiwiEntityManager.persist(selectedArtWork);
//		return "/artaround/pages/frontend/confirmDeal.xhtml";
//		
//	}
	
}
