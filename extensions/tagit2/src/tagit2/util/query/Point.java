package tagit2.util.query;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import tagit2.util.exchange.MapMarker;

/**
 * this class represents a point search result
 * @author tkurz
 *
 */
public class Point {
	
	//properties
	private static final String ICON = "point.png";
	
	private static DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,Locale.GERMAN);
	
	private long id;
	private double lat,lng;
	private String title;
	private String icon;
	
	private Date modified;
	
	//Constructors
	/**
	 * creates a Point
	 * @param id
	 * @param lat
	 * @param lng
	 * @param title
	 */
	public Point( long id, double lat, double lng, String title, Date modified ) {
		this.id = id;
		this.lat = lat;
		this.lng = lng;
		this.title = title;
		this.modified = modified;
	}
	
	//getters and setters
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public long getId() {
		return id;
	}

	//methods
	/**
	 * returns a MapMarker representing this Point
	 * @return
	 */
	public MapMarker getMapMarker() {
		MapMarker m = new MapMarker( lat, lng, id, "point" );
		m.setTitle(title);
		
		//set icon
		if( icon == null ) {
			m.setIcon(ICON);
		} else {
			m.setIcon(icon);
		}
		return m;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String toString() {
		return "'"+title+"'";
	}
	
	public String toXML() {
		return "<point id='" + id + "' lat='" + lat + "' lng='" + lng + "'>" + title + "</point>";
	}

	public String toJSON() {
		String t = title;
		try {
			t = new String(title.getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// maybe a wrong encoding now
			e.printStackTrace();
		}
		return "{'id':'"+id+"','lat':'"+lat+"','lng':'"+lng+"','date':'" + df.format(modified) + "','title':'"+t+"'}";
	}
	
	public String getTitle() {
		return title;
	}
	
	public Date getModified() {
		return modified;
	}
}
