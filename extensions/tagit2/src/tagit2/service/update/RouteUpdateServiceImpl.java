package tagit2.service.update;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import tagit2.api.content.route.RouteFacade;
import tagit2.api.content.route.TrackPointFacade;
import tagit2.api.update.RouteUpdateService;
import tagit2.util.route.AltitudeService;
import tagit2.util.route.DistanceService;
import tagit2.util.route.ProfileService;
import tagit2.util.update.LCSLocation;

@Scope(ScopeType.STATELESS)
@Name("tagit2.routeUpdateService")
public class RouteUpdateServiceImpl implements RouteUpdateService {

	@In
    private KiWiEntityManager kiwiEntityManager;
	
    @In(create = true)
    private ContentItemService contentItemService;
    
    @In
    private EntityManager entityManager;
    
    @In(create = true)
    private User currentUser;
	
	@Logger
	Log log;
	
	public void updateRoute(RouteFacade route, String routeString) {
		//get routeString to array
		routeString = routeString.substring(1,routeString.length()-1);
		String[] points = routeString.split("\\);\\(");
		//log.info("update route #0", route.getTitle());
		
		LinkedList<LCSLocation> x = new LinkedList<LCSLocation>();
		LinkedList<LCSLocation> y = new LinkedList<LCSLocation>();
		
		//create list of new track
		for( String s : points ) {
			String[] loc = s.split(", ");
			//log.info("point: lat:#0, lng:#1", loc[0],loc[1]);
			y.add(new LCSLocation( Double.valueOf( loc[0]),Double.valueOf( loc[1]) ));
		}
		
		//create list of old track and order it
		LinkedList<TrackPointFacade> tps = route.getTrackPoints();
		Collections.sort( tps,new Comparator<TrackPointFacade>(){

			@Override
			public int compare(TrackPointFacade o1, TrackPointFacade o2) {
				return o1.getOrdinal() - o2.getOrdinal();
			}
			
		});
		
		for( TrackPointFacade tp : tps ) {
			x.add(new LCSLocation( tp ));
		}
		
		//this implements the algorithm longest common subsequenz
		//results
        LinkedList<LCSLocation> r = new LinkedList<LCSLocation>();
        LinkedList<LCSLocation> d = new LinkedList<LCSLocation>();
        
        int M = x.size();
        int N = y.size();

        // opt[i][j] = length of LCS of x[i..M] and y[j..N]
        int[][] opt = new int[M+1][N+1];

        // compute length of LCS and all subproblems via dynamic programming
        for (int i = M-1; i >= 0; i--) {
            for (int j = N-1; j >= 0; j--) {
                if (x.get(i).equals( y.get(j) ) )
                    opt[i][j] = opt[i+1][j+1] + 1;
                else 
                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
            }
        }

        // recover LCS itself and print it to standard output
        int i = 0, j = 0;
        while(i < M && j < N) {
            if (x.get(i).equals( y.get(j) )) {
                r.add(x.get(i));
                i++;
                j++;
            }
            else if (opt[i+1][j] >= opt[i][j+1]) {
            	d.add(x.get(i));
            	i++;
            }
            else {
            	r.add(y.get(j));
            	j++;
            }
        }
        
        while( j < N )  {
        	r.add(y.get(j));
        	j++;
        }
        
        //delete trackpoints, that are not in use anymore
        log.info("#0 trackpoints to delete", d.size());
        for( LCSLocation l : d ) {
        	contentItemService.removeContentItem(l.getTrackpoint().getDelegate());
        }
        
        //persist new trackpoints and/or set new order
        //log.info("new list contains #0 trackpoints", r.size());
        
        //check altitude service
		boolean altitudeServiceIsActive = true;
		if( AltitudeService.getAltitude(0, 0) == 0 ) {
			log.info("AltitudeSerivce is not active");
			altitudeServiceIsActive = false;
		}
        
        LinkedList<TrackPointFacade> newTPS = new LinkedList<TrackPointFacade>();
        
        int a = 0;
        double lastAltitude = 0;
        for( LCSLocation l : r ) {
        	if( l.isPersisted() ) {
        		if( l.getTrackpoint().getAltitude() == 0 ) {
        			if( altitudeServiceIsActive ) {
    					l.getTrackpoint().setAltitude( AltitudeService.getAltitude(l.getLat(),l.getLng()));
    					if( l.getTrackpoint().getAltitude() == -32768 ) {
    						l.getTrackpoint().setAltitude(lastAltitude);
    					}
    					lastAltitude = l.getTrackpoint().getAltitude();
        	    	} else {
        	    		l.getTrackpoint().setAltitude(lastAltitude);
        	    	}
        		} else {
        			lastAltitude = l.getTrackpoint().getAltitude();
        		}
        		//new ordinal must be set
        		l.getTrackpoint().setOrdinal(a);
        		kiwiEntityManager.persist(l.getTrackpoint());
        		newTPS.add(l.getTrackpoint());
        	} else {
        		//new Trackpoint must be persisted
        		TrackPointFacade tpf = kiwiEntityManager.createFacade( contentItemService.createContentItem("/tagit/trackPoint/" + UUID.randomUUID().toString()), TrackPointFacade.class);
    	    	tpf.setAuthor(currentUser);
    	    	tpf.setLatitude(l.getLat());
    	    	tpf.setLongitude(l.getLng());
    	    	tpf.setOrdinal(a);
    	    	
    	    	//set altitude
    	    	if( altitudeServiceIsActive ) {
					tpf.setAltitude( AltitudeService.getAltitude(l.getLat(),l.getLng()));
					if( tpf.getAltitude() == -32768 ) {
						tpf.setAltitude(lastAltitude);
					}
					lastAltitude = tpf.getAltitude();
    	    	} else {
    	    		tpf.setAltitude(lastAltitude);
    	    	}
    	    	
    	    	contentItemService.updateTitle(tpf, "R:"+route.getId());
    	    	
    	    	kiwiEntityManager.persist(tpf);
    	    	
    	    	newTPS.add(tpf);      		
        	}
        	a++;
        }
        
        //set distance, altitude and profile img
    	//get distance and altitude
    	double dist = DistanceService.getTotalDistance( newTPS);
		dist = Math.round( dist * 100.0 ) / 100.0;
        route.setDistance(  dist );
        route.setVerticalClimb( AltitudeService.getVerticalClimb( newTPS ) );
        
        //get new profile
        byte [] profile = getImageBytes( ProfileService.getProfile(newTPS,route).createBufferedImage(540, 180) );
    	String name = route.getTitle();
        contentItemService.updateMediaContentItem(route.getDelegate(), profile, "image/png", name);
        
        route.setLatitude(newTPS.getFirst().getLatitude());
        route.setLongitude(newTPS.getFirst().getLongitude());
        route.setTrackPoints(newTPS);
        
        kiwiEntityManager.persist(route);
        
	}
	
	private byte[] getImageBytes(BufferedImage image) {
		
		byte[] resultImageAsRawBytes = new byte[0];
			
			//write
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				ImageIO.write( image, "png", baos );
				//close
				baos.flush();
				resultImageAsRawBytes = baos.toByteArray();
				
				baos.flush();
				baos.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		return resultImageAsRawBytes;
	}

}
