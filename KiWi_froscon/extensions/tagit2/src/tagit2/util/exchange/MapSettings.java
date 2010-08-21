package tagit2.util.exchange;

import kiwi.api.geo.Location;

/**
 * This class is used to get data by jsFunction (to bundle data)
 * @author tkurz
 *
 */
public class MapSettings {
	
	private Location center;
	private int zoom;
	private long currentPoiId;
	
	public MapSettings(Location center, int zoom, long currentPoiId) {
		this.center = center;
		this.zoom = zoom;
		this.currentPoiId = currentPoiId;
	}
	
	//getters and setters
	public void setCenter(Location center) {
		this.center = center;
	}
	public Location getCenter() {
		return center;
	}
	public int getZoom() {
		return zoom;
	}
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	public long getCurrentPoiId() {
		return currentPoiId;
	}
	public void setCurrentPoiId(long currentPoiId) {
		this.currentPoiId = currentPoiId;
	}

}
