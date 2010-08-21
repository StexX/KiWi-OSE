package tagit2.util.route;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import tagit2.api.content.route.TrackPointFacade;

public class AltitudeService {
	
	/**
	 * this method returns the elevation to a given location. it
	 * uses the geonames elevation service
	 * (http://www.geonames.org/export/web-services.html)
	 * @param location
	 * @return elevation in meters
	 */
	public static int getAltitude(double latitude, double longitude) {
		final String url_string = "http://ws.geonames.org/srtm3?lat="+latitude+"&lng="+longitude;
		final int timeoutMs = 1000;
		int res = 0;
		try {
		URL url = new URL(url_string);
		URLConnection conn = url.openConnection();
		// setting these timeouts ensures the client does not deadlock indefinitely
		// when the server has problems.
		conn.setConnectTimeout(timeoutMs);
		conn.setReadTimeout(timeoutMs);
		final InputStream inputStream = conn.getInputStream();

        
        final BufferedReader in =
            new BufferedReader(new InputStreamReader(inputStream));
        String line;
        
        while ((line = in.readLine()) != null) {
        	res = Integer.parseInt(line);
        }
		} catch (Exception e) {
			System.err.println("something went wrong while getting elevation");
			e.printStackTrace();
		}

		return res;
	}
	
	/**
	 * this method returns the vertical climb of a track by adding
	 * all single climbs (positive difference between two elevations)
	 * @param tps a list of trackpoints
	 * @return all over climb
	 */
	public static double getVerticalClimb( List<TrackPointFacade> tps) {
		double up = 0;
		for( int i = 1; i < tps.size(); i++ ) {
			if( tps.get(i-1).getAltitude() < tps.get(i).getAltitude() ) {
				up += tps.get(i).getAltitude() - tps.get(i-1).getAltitude();
			}
		}
		return up;
 	}
	
}
