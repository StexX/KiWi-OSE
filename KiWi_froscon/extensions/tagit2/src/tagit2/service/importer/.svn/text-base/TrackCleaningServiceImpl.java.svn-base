package tagit2.service.importer;

import java.util.LinkedList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import tagit2.api.importer.TrackCleaningService;
import tagit2.util.parser.gpx.Track;
import tagit2.util.parser.gpx.Trackpoint;

/**
 * this service reduces the number of trackpoints
 * @author tkurz
 *
 */
@Scope(ScopeType.STATELESS)
@Name("routeit.trackPointCleaner")
public class TrackCleaningServiceImpl implements TrackCleaningService {

	/**
	 * simplest reducer: take every (nbOfPoints / maxTrackpoints) point
	 */
	public Track reduceTracklistByNumber(Track track, int maxTrackpoint) {
		List<Trackpoint> old_list = track.getTracklist();
		LinkedList<Trackpoint> new_list = new LinkedList<Trackpoint>();
		int range = old_list.size() / maxTrackpoint;

		for( int i = 0; i < old_list.size(); i++) {
			if( i%range == 0 ) {
				new_list.add(old_list.get(i));
			}
		}
		
		//add last point if necessary
		Trackpoint p = old_list.get(old_list.size()-1);
		if( ! p.equals(new_list.getLast()) ) {
			new_list.addLast(p);
		}
		
		track.setTracklist(new_list);
		
		return track;
	}

}
