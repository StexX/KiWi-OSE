package tagit2.util.route;

import java.util.List;

import tagit2.api.content.route.TrackPointFacade;

public class DistanceService {
	
	private static final double R = 6378.137; //in km
	
	/**
	 * returns the distance between two locations times 10000
	 * TODO maybe a more exact value by a more complex formula
	 * @param s location 1
	 * @param d location 2
	 * @return distance
	 */	
	private static double distanceBetween(double s_lon, double s_lat, double d_lon, double d_lat) {
        return Math.round(( Math.acos(Math.sin(Math.toRadians(s_lon)) * Math.sin(Math.toRadians(d_lon)) + Math.cos(Math.toRadians(s_lon)) * Math.cos(Math.toRadians(d_lon)) * (Math.cos(Math.toRadians(d_lat - s_lat)))) * R ) * 1000.0) / 1000.0;
    }
	
	/**
	 * returns the total distance by adding single distances. this method
	 * also considers the altitude difference between locations
	 * @param tps
	 * @return
	 */
	public static double getTotalDistance(List<TrackPointFacade> tps) {
		double dist = 0;
		tps.get(0).setDistance(dist);
		for( int i = 1; i < tps.size(); i++ ) {
			//dist between 2 points in km
			TrackPointFacade s = tps.get(i-1);
			TrackPointFacade d = tps.get(i);
			double cur_dist = distanceBetween( s.getLongitude(), s.getLatitude(), d.getLongitude(), d.getLatitude() ) ;
			
			//elevation between two points in km
			//double elev = (tps.get(i-1).getAltitude() - tps.get(i).getAltitude())/1000;
			
			//elev != 0 -> distance is bigger (pythagoras)
			//d += Math.sqrt( dist*dist + elev*elev );
			dist += cur_dist;
			tps.get(i).setDistance(dist);
		}
		
		return dist;
	}
	
	//returns a positive value or 0
//	private static double getUpDistance(double h1, double h2) {
//		if( h2 > h1 ) {
//			return h2-h1;
//		} else {
//			return 0;
//		}
//	}

}
