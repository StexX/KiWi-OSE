package tagit2.util.update;

import tagit2.api.content.route.TrackPointFacade;

public class LCSLocation {
	
	private TrackPointFacade tp;
	private double lat,lng;
	
	public LCSLocation( double lat, double lng ) {
		this.setLat(lat);
		this.setLng(lng);
	}
	
	public LCSLocation( TrackPointFacade tp ) {
		this.tp = tp;
		this.setLat(tp.getLatitude());
		this.setLng(tp.getLongitude());
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLng() {
		return lng;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLat() {
		return lat;
	}
	
	public boolean equals( Object o ) {
		try {
			LCSLocation l = (LCSLocation)o;
			return l.getLat() == getLat() && l.getLng() == getLng();
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean isPersisted() {
		return tp != null;
	}
	
	public TrackPointFacade getTrackpoint() {
		return tp;
	}

}
