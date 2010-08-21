package tagit2.util.parser.gpx;

import java.util.Date;

import kiwi.api.geo.Location;

public class Trackpoint {
	
	private Location location;
	private Date time;
	private double altitude;
	
	public Trackpoint(double lat, double lng) {
		location = new Location(lat,lng);
	}
	
	public Location getLocation() {
		return location;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public double getAltitude() {
		return altitude;
	}
}
