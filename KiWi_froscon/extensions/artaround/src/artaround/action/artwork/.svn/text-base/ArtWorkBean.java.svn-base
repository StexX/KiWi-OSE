/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder
 */

package artaround.action.artwork;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

import artaround.action.artwork.techniqueTree.TechniqueAction;
import artaround.datamodel.artwork.ArtWorkFacade;
import artaround.datamodel.artwork.ArtWorkTextFacade;
import artaround.datamodel.artwork.MultimediaFacade;

/**
 * @author Rolf Sint
 * 
 */
@Name("artWorkBean")
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class ArtWorkBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Begin(join=true)
	@Create
	public void begin(){
	
	}
	
	@In(create= true)
	@Out(required = false)
	private TechniqueAction techniqueAction;
	
	@Logger
	private static Log log;
		
	private String artWorkName, authorName, description,
			price, currency, customerFirstName, customerLastName, customerStreet, 
			customerZip, customerCity, customerState, customerEmail, customerPhone;
	
    private double longitude;

    private double latitude;
	
	private LinkedList<SKOSConcept> techniques;
	
	private LinkedList<ArtWorkTextFacade> artWorkTextList;
	
	private Date sellingDate;
	private boolean publicAccess, sellingState, customerIsMember;
	private LinkedList<MediaTmp> artAroundMediaList;
	
	//called each time you create a new artwork, e.g. if you click on "neues Kunstwerk"
	public void clear(){
		artWorkName = "";
		authorName  = "";
		techniques  = null;
		techniqueAction = null;
		description  = "";
		price  = "";
		currency = "";
		customerFirstName  = "";
		customerLastName  = "";
		customerStreet  = "";
		customerZip  = "";
		customerCity  = "";
		customerState = "";
		customerEmail = "";
		customerPhone = "";
		longitude = 0;
		latitude = 0;
		artAroundMediaList = null;
	}
	
	// initializes the ArtWorkBean according to a given ArtWorkFacade
	// used e.g. for the ArtWorkEditAction, the ArtWorkFacade is the current and selected Facade
	public void init(ArtWorkFacade selectedArtWork){
		this.artWorkName = selectedArtWork.getTitle();
		
		//convert MultimediaFacadeList to MultiMediaTmpList
		LinkedList<MultimediaFacade> ll = selectedArtWork.getArtAroundMediaList();
		artAroundMediaList = new LinkedList<MediaTmp>();
		for (MultimediaFacade multimediaFacade : ll) {
			artAroundMediaList.add(new MediaTmp(multimediaFacade.getMimeType(), multimediaFacade.getFilename()));
		}
		
		this.artWorkTextList = selectedArtWork.getArtWorkTextList();

		this.sellingDate = selectedArtWork.getSellingDate();
		this.sellingState = selectedArtWork.getSellingState();
		this.publicAccess = selectedArtWork.getPublicAccess();

		this.authorName = selectedArtWork.getAuthorName();		
		this.techniques = selectedArtWork.getTechniques();
		
		//Set techiques in TechniquAction
		if(this.techniques != null)
		{
			techniqueAction.setChosenConcepts(this.techniques);
		}
		else
		{
			techniqueAction = null;
		}
		
		
		this.description = selectedArtWork.getDescription();
		this.price = selectedArtWork.getPrice();
		this.currency = selectedArtWork.getCurrency();
		
//		this.customerFirstName = selectedArtWork.getCustomerFirstName();
//		this.customerLastName = selectedArtWork.getCustomerLastName();
//		this.customerStreet = selectedArtWork.getCustomerStreet();
//		this.customerZip = selectedArtWork.getCustomerZip();
//		this.customerCity = selectedArtWork.getCustomerCity();
//		this.customerState = selectedArtWork.getCustomerState();
//		this.customerEmail = selectedArtWork.getCustomerEmail();
//		this.customerPhone = selectedArtWork.getCustomerPhone();
//		this.customerIsMember = selectedArtWork.getCustomerIsMember();		
	}
	
		
	public void check(){
		log.info(latitude);
		log.info(longitude);
	}
	
	public String getArtWorkName() {
		return artWorkName;
	}

	public void setArtWorkName(String artWorkName) {
		this.artWorkName = artWorkName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}


	public LinkedList<SKOSConcept> getTechniques() {
		return techniques;
	}

	public void setTechniques(LinkedList<SKOSConcept> techniques) {
		this.techniques = techniques;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}
	
	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}	
	
	public String getCustomerStreet() {
		return customerStreet;
	}

	public void setCustomerStreet(String customerStreet) {
		this.customerStreet = customerStreet;
	}
	
	public String getCustomerZip() {
		return customerZip;
	}

	public void setCustomerZip(String customerZip) {
		this.customerZip = customerZip;
	}
	
	public String getCustomerCity() {
		return customerCity;
	}

	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}
	
	public String getCustomerState() {
		return customerState;
	}

	public void setCustomerState(String customerState) {
		this.customerState = customerState;
	}
	
	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	
	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public LinkedList<ArtWorkTextFacade> getArtWorkTextList() {
		return artWorkTextList;
	}

	public void setArtWorkTextList(LinkedList<ArtWorkTextFacade> artWorkTextList) {
		this.artWorkTextList = artWorkTextList;
	}

	public Date getSellingDate() {
		return sellingDate;
	}

	public void setSellingDate(Date sellingDate) {
		this.sellingDate = sellingDate;
	}

	public boolean getPublicAccess() {
		return publicAccess;
	}

	public void setPublicAccess(boolean publicAccess) {
		this.publicAccess = publicAccess;
	}
	
	public boolean getCustomerIsMember() {
		return customerIsMember;
	}

	public void setCustomerIsMember(boolean customerIsMember) {
		this.customerIsMember = customerIsMember;
	}

	public boolean getSellingState() {
		return sellingState;
	}

	public void setSellingState(boolean sellingState) {
		this.sellingState = sellingState;
	}

	public LinkedList<MediaTmp> getArtAroundMediaList() {
		return artAroundMediaList;
	}

	public void setArtAroundMediaList(
			LinkedList<MediaTmp> artAroundMediaList) {
		this.artAroundMediaList = artAroundMediaList;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
