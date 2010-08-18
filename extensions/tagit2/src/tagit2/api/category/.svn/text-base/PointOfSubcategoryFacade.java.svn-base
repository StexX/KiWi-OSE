package tagit2.api.category;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;
import kiwi.model.ontology.SKOSConcept;

@KiWiFacade
@RDFType({Constants.NS_GEO+"Point" })
public interface PointOfSubcategoryFacade extends ContentItemI {
	/**
	 * The SKOS Concept used as category of this point of interest.
	 */
	@RDF(Constants.NS_TAGIT+"hasSubCategory")
	public SKOSConcept getSubCategory();
	
	public void setSubCategory(SKOSConcept c);
}