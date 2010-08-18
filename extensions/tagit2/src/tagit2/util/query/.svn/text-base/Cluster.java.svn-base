package tagit2.util.query;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.jfree.util.Log;

import kiwi.api.geo.Location;
import tagit2.util.exchange.MapMarker;
import tagit2.util.geo.Area;

/**
 * this class represents a cluster
 * @author tkurz
 *
 */
public class Cluster {
	
	private long id;
	private LinkedList<Point> points;
	private double lat,lng;
	private Area area;
	
	private boolean firstCall = true;

	public Cluster( long id, LinkedList<Point> points, Area area ) {
		this.id = id;
		this.points = points;
		//set medium as location
		int i = Math.round( points.size() / 2 );
		Point p = points.get(i);
		this.lat = p.getLat();
		this.lng = p.getLng();
		this.area = area;
		this.area.setCenter( new Location(lat,lng));
	}
	
	/**
	 * this method returns a MapMarker Object representing this cluster
	 * @return
	 */
	public MapMarker getMapMarker() {
		MapMarker m = new MapMarker( lat, lng, id, "cluster" );
		m.setIcon("image?size="+points.size());
		m.setTitle("Cluster enthält " + points.size() + " Elemente");
		return m;
	}
	
	/**
	 * returns the id of the cluster
	 * @return
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * returns an xml representation of the cluster. it follows this sample:
	 * <?xml version='1.0' encoding='UTF-8'?>
	 * <cluster>
	 * 		<point id='1'>title one</point>
	 * 		<point id='2'>title two</point>
	 * </cluster>
	 * @return
	 */
	public String toXML() {
		StringBuffer b = new StringBuffer();
		b.append("<?xml version='1.0' encoding='UTF-8'?>");
		b.append("<cluster>");
		
		for( Point p : points ) {
			b.append(p.toXML());
		}
		
		b.append("</cluster>");
		return b.toString();
	}
	
	public String toJSON(int start,int size) {
		if( firstCall ) {
			orderPoints();
			firstCall = false;
		}
		StringBuffer b = new StringBuffer();
		b.append("{'points':[");
		int s = Math.min(points.size(), start+size);
		for( int i = start; i < s; i++ ) {
			b.append(points.get(i).toJSON());
			if( i != points.size()-1 ) {
				b.append(",");
			}
		}
		b.append("],'page':");
		b.append(start/size + 1);
		b.append(",'size':");
		b.append((points.size()-1)/size + 1);
		b.append("}");
		return b.toString();
	}
	
	public String toJSON(int start,int size,String c) {
		if( firstCall ) {
			orderPoints();
			firstCall = false;
		}
		LinkedList<Point> ps = filter(c);
		StringBuffer b = new StringBuffer();
		b.append("{'points':[");
		int s = Math.min(ps.size(), start+size);
		for( int i = start; i < s; i++ ) {
			b.append(ps.get(i).toJSON());
			if( i != ps.size()-1 ) {
				b.append(",");
			}
		}
		b.append("],'page':");
		b.append(start/size + 1);
		b.append(",'size':");
		b.append((ps.size()-1)/size + 1);
		b.append("}");
		return b.toString();
	}
	
	public Area getArea() {
		return this.area;
	}
	
	public int getSize() {
		return points.size();
	}
	
	private LinkedList<Point> filter(String c) {
		LinkedList<Point> ps = new LinkedList<Point>();
		for( Point p : points ) {
			if( p.getTitle().substring(0,1).equalsIgnoreCase(c) ) ps.add(p);
		}
		return ps;
	}
	
	private void orderPoints() {
		Collections.sort(points,new Comparator<Point>(){

			@Override
			public int compare(Point o1, Point o2) {
				return o2.getModified().compareTo(o1.getModified());
			}
			
		});
	}
	
}
