package tagit2.util.exchange;

import kiwi.api.geo.Location;

/**
 * this class represents a marker for gMap, it can be displayed depending on
 * type. location, id and type <b>must not</b> be null!
 * @author tkurz
 *
 */
public class MapMarker {

	private Location location;
	private long id;
	private String title;
	private String type;
	private String icon;
	
	public MapMarker(double lat, double lng, long id, String type) {
		this.location = new Location(lat,lng);
		this.id = id;
		this.type = type;
	}
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
