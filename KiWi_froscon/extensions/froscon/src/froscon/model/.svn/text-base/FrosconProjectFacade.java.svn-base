package froscon.model;

import java.util.HashSet;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;

@KiWiFacade
@RDFType({ Constants.NS_FOAF+"Project" })
public interface FrosconProjectFacade extends ContentItemI {

	public void setName(String name);
	
	@RDF(Constants.NS_FROSCON+"projectName")
	public String getName();
	
	public void setStage(String stage);
	
	@RDF(Constants.NS_FROSCON+"projectStage")
	public String getStage();

	public void setBugs(HashSet<ContentItem> bugs);
	
	@RDF(Constants.NS_FROSCON + "hasBugs")
	public HashSet<ContentItem> getBugs();
}
