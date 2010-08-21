package froscon.model;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;

@KiWiFacade
@RDFType({ Constants.NS_FROSCON+"Bug" })
public interface FrosconBugFacade  extends ContentItemI {

	public void setStatus(String status);
	
	@RDF(Constants.NS_FROSCON+"bugStatus")
	public String getStatus();
}
