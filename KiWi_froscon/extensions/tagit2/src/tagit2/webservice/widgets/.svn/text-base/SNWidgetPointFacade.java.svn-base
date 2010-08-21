package tagit2.webservice.widgets;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;

import org.hibernate.validator.Range;

/**
 * this is a facade, that allows to get longitude and latitude of a given newsItem
 * @author tkurz
 *
 */
@KiWiFacade
@RDFType({ Constants.NS_TAGIT+"NewsItem" })
public interface SNWidgetPointFacade extends ContentItemI {
	/**
	 * The longitude of this newsItem. Maps to geo:long of the geo ontology
	 * in the triple store.
	 * 
	 * @return
	 */
	@Range(min=-180, max=180)
	@RDF(Constants.NS_GEO+"long")
	public double getLongitude();

	public void setLongitude(double longitude);

	/**
	 * The latitude of this newItem. Maps to geo:lat of the geo ontology 
	 * in the triple store.
	 * @return
	 */
	@Range(min=-90, max=90)
	@RDF(Constants.NS_GEO+"lat")
	public double getLatitude();

	public void setLatitude(double latitude);
}
