package tagit2.util.geo;

import kiwi.api.geo.Location;

public class GPSUtils {
	
	private static String REGEX = "'|\"|\\s";
	
	public static Location toLocation(String latDegrees, String lngDegrees) {
		
		double lat = 0;
		double lng = 0;
		
		try {
			//parse NSDegrees
			String[] nsd = latDegrees.split(REGEX);
			lat = toDecimal(nsd[0],nsd[1],nsd[2],nsd[3]);
			
			String[] ewd = lngDegrees.split(REGEX);
			lng = toDecimal(ewd[0],ewd[1],ewd[2],ewd[3]);
			
			return new Location(lat,lng);
		} catch(Exception e) {
			throw new NumberFormatException("GeoStrings "+latDegrees+", "+lngDegrees+" can not be parsed");
		}
	}
	
	private static double toDecimal(String degree, String minutes, String seconds , String senw ) {
		double d = Double.parseDouble(degree);
		double m = Double.parseDouble(minutes);
		double s = Double.parseDouble(seconds);
		char x = senw.charAt(0);
		double value = ((s/60)+m)/60+d;
		switch( x ) {
		case 'S':
		case 'W':
			return -value;
		default:
			return value;
		}
	}

}
