package tagit2.webservice.widgets;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import kiwi.model.kbase.KiWiUriResource;

@XmlRootElement(name = "point")
@XmlAccessorType(XmlAccessType.FIELD)
public class SNWidgetPoint {
	
	@XmlAttribute
	private long id;
	@XmlAttribute
	private String title;
	@XmlAttribute
	private double latitude;
	@XmlAttribute
	private double longitude;
	@XmlAttribute
	private String iconUrl;
	@XmlAttribute
	private String uri;
	
	public SNWidgetPoint(){
		
	}
	
	public SNWidgetPoint(SNWidgetPointFacade facade, String iconUrl) {
		this.id = facade.getId();
		this.title = facade.getTitle();
		this.iconUrl = iconUrl;
		this.latitude = facade.getLatitude();
		this.longitude = facade.getLongitude();
		this.uri = ((KiWiUriResource)facade.getResource()).getUri();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	

}
