package tagit2.api.content.route;

import java.io.Serializable;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItemI;

@KiWiFacade
@RDFType( {Constants.NS_GEO+"LocatedImage"})
public interface GeoLocatedImageFacade extends ContentItemI, Serializable {

	@RDF(Constants.NS_EXIF + "gpsLongitude")
	String getExifLongitude();
	void setExifLongitude(String s);
	
	@RDF(Constants.NS_EXIF + "gpsLongitudeRef")
	String getExifLongitudeRef();
	void setExifLongitudeRef(String s);
	
	@RDF(Constants.NS_EXIF + "gpsLatitude")
	String getExifLatitude();
	void setExifLatitude(String s);
	
	@RDF(Constants.NS_EXIF + "gpsLatitudeRef")
	String getExifLatitudeRef();
	void setExifLatitudeRef(String s);
	
}
