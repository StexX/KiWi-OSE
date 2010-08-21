package tagit2.util.geo;

import kiwi.api.geo.Location;

/**
 * 
 * @author tkurz
 *
 * This class describes a geological Area. The bound are defined by two <code>Location</code>s.
 * swLatLng describes the lower-left corner, neLatLng the upper-right one.
 */
public class Area {
	
	/**
	 * lower-left corner
	 */
	private Location swLatLng;
	
	/**
	 * upper-right corner
	 */
	private Location neLatLng;
	
	private Location center;
	
	/**
	 * 
	 * @param swLatLng = lower-left corner
	 * @param neLatLng = upper-right corner
	 */
	public Area(Location swLatLng, Location neLatLng) {
		this.swLatLng = swLatLng;
		this.neLatLng = neLatLng;
	}
	
	public Area(double swLat, double swLng, double neLat, double neLng) {
		this.swLatLng = new Location(swLat,swLng);
		this.neLatLng = new Location(neLat,neLng);
	}
	
	public Location getSwLatLng() {
		return swLatLng;
	}
	public void setSwLatLng(Location swLatLng) {
		this.swLatLng = swLatLng;
	}
	public Location getNeLatLng() {
		return neLatLng;
	}
	public void setNeLatLng(Location neLatLng) {
		this.neLatLng = neLatLng;
	}
	
	@Override
	public String toString() {
		return "sw( " + swLatLng + " ), ne( "+ neLatLng + ")";
	}
	
	public double getUpperLat() {
		return Math.max(swLatLng.getLatitude(), neLatLng.getLatitude());
	}
	
	public double getLowerLat() {
		return Math.min(swLatLng.getLatitude(), neLatLng.getLatitude());
	}
	
	public double getUpperLng() {
		return Math.max(swLatLng.getLongitude(), neLatLng.getLongitude());
	}
	
	public double getLowerLng() {
		return Math.min(swLatLng.getLongitude(), neLatLng.getLongitude());
	}
	
	public double getWidth() {
		return this.getUpperLng()-this.getLowerLng();
	}
	
	public double getHeight() {
		return this.getUpperLat()-this.getLowerLat();
	}
	
	public Location getCenter() {
		if( center == null )
		return new Location( this.getUpperLat() - this.getHeight()/2, this.getUpperLng() - this.getWidth()/2 );
		else return center;
	}
	
	public void setCenter(Location center) {
		this.center = center;
	}

}
