package tagit2.service.importer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.geo.Location;
import kiwi.api.importexport.ImportService;
import kiwi.api.importexport.importer.ImporterLocal;
import kiwi.api.importexport.importer.ImporterRemote;
import kiwi.context.CurrentContentItemFactory;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import nu.xom.Text;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;

import tagit2.api.content.route.RouteFacade;
import tagit2.api.content.route.TrackPointFacade;
import tagit2.api.importer.TrackCleaningService;
import tagit2.util.parser.GPXParser;
import tagit2.util.parser.gpx.Track;
import tagit2.util.parser.gpx.Trackpoint;
import tagit2.util.route.AltitudeService;
import tagit2.util.route.DistanceService;
import tagit2.util.route.ProfileService;

@Name("kiwi.service.importer.gpx")
@Scope(ScopeType.STATELESS)
public class GPXImporterImpl implements ImporterLocal, ImporterRemote {

	@Logger
	private Log log;
    
    @In
    private KiWiEntityManager kiwiEntityManager;
    
    @In(create = true)
    private ContentItemService contentItemService;
    
    @In(create = true)
    private User currentUser;
    
    @In(create=true)
    private ConfigurationService configurationService;
    
    @In(create=true)
    private CurrentContentItemFactory currentContentItemFactory;
    
    @In(create=true,value="routeit.trackPointCleaner")
    private TrackCleaningService trackCleaner;
	
	private static String[] mime_types = {
		"application/gpx+xml"
	};
	
	private final String NO_NAME = "Route";
	
	@Observer(KiWiEvents.SEAM_POSTINIT) 
	@BypassInterceptors
	public void initialise() {
		log.info("registering GPX importer ...");
		
		ImportService ies = (ImportService) Component.getInstance("kiwi.core.importService");
		
		ies.registerImporter(this.getName(),"kiwi.service.importer.gpx",this);
	}
	
	@Override
	public Set<String> getAcceptTypes() {
		return new HashSet<String>(Arrays.asList(mime_types));
	}

	@Override
	public String getDescription() {
		return "Importer for parsing the GPX format";
	}

	@Override
	public String getName() {
		return "GPX";
	}

	@Override
	public int importData(URL url, String format, Set<KiWiUriResource> types,
			Set<ContentItem> tags, User user, Collection<ContentItem> output) {
		try {
			log.info("importing gpx from URL #0", url);
			trackname = url.getPath().substring(url.getPath().lastIndexOf('/')+1,url.getPath().length()-4);
			trackname = trackname.replaceAll("%20", " ");
			return importData(url.openStream(), format, types, tags, user, output);			
		} catch(IOException e) {
			log.error("I/O error while importing data from URL #0: #1",url, e.getMessage());
			return 0;
		}
	}

	@Override
	public int importData(InputStream is, String format,
			Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) {
		GPXParser parser = new GPXParser();
		try {
			return importData(parser.parse(is), types, tags, user, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int importData(Reader reader, String format,
			Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) {
		
		//TODO implement
		log.error("not yet implemented");
		return 0;
	}
	
	private String trackname;
	
	private int importData( Track t,
			Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output ) {
		try {
		log.info("track was parsed");
		//set trackname
		if( t.getName() == null ) {
			if( trackname != null ) {
				t.setName(trackname);
				trackname = null;
			} else {
				t.setName(this.NO_NAME);
			}
		}
		
		//set max trackpoints
		int maxTrackpoints = configurationService.getConfiguration("tagit.route.maxTrackpoints","100").getIntValue();
		
		//TODO should be more configurable
		if( t.getTracklist().size() > maxTrackpoints ) {
			trackCleaner.reduceTracklistByNumber(t, maxTrackpoints);
		}
		log.info("trackpoints reduced, track now contains #0 points", t.getTracklist().size() );
		
		//create Route
    	RouteFacade currentRoute = kiwiEntityManager.createFacade(contentItemService.createContentItem("/tagit/route/" + UUID.randomUUID().toString()), RouteFacade.class);
    	currentRoute.setAuthor(currentUser);
    	
    	//set startPoint
    	Location startTP = t.getTracklist().get(0).getLocation();
    	currentRoute.setLatitude(startTP.getLatitude());
    	currentRoute.setLongitude(startTP.getLongitude());
    	
    	//persist route
    	kiwiEntityManager.persist(currentRoute);
    	
    	//create TrackpointList
    	LinkedList<TrackPointFacade> trackpoints = new LinkedList<TrackPointFacade>();
    	for( Trackpoint tp : t.getTracklist() ) {
    		TrackPointFacade tpf = kiwiEntityManager.createFacade( contentItemService.createContentItem("/tagit/trackPoint/" + UUID.randomUUID().toString()), TrackPointFacade.class);
	    	tpf.setAuthor(currentUser);
	    	tpf.setLatitude(tp.getLocation().getLatitude());
	    	tpf.setLongitude(tp.getLocation().getLongitude());
	    	if( tp.getAltitude() != 0 ) {
	    		tpf.setAltitude(tp.getAltitude());
	    	} else {
	    		tpf.setAltitude(0);
	    	}
			trackpoints.add(tpf);
    	}
    	
    	//set altitude and distance
    	//test if altService is active
		boolean altitudeServiceIsActive = true;
		if( AltitudeService.getAltitude(0, 0) == 0 ) {
			log.info("AltitudeSerivce is not active");
			altitudeServiceIsActive = false;
		}
		
		if( altitudeServiceIsActive ) {
			//get elevations
			//TODO if first value is not defined!!!
			double lastValidElevation = 0;
			for( TrackPointFacade tp : trackpoints ) {
				if( tp.getAltitude() == 0 ) {
					tp.setAltitude( AltitudeService.getAltitude(tp.getLatitude(),tp.getLongitude()));
					if( tp.getAltitude() == -32768 ) {
						tp.setAltitude(lastValidElevation);
					}
				}
				lastValidElevation = tp.getAltitude();
			}
			log.info("Altitudes were setted");
		}
		
		if( !trackpoints.isEmpty() ) {
			double dist = DistanceService.getTotalDistance( trackpoints);
			log.info("orig dist: #0", dist);
			dist = Math.round( dist * 100.0 ) / 100.0;
			log.info(dist);
	        currentRoute.setDistance(  dist );
	        currentRoute.setVerticalClimb( AltitudeService.getVerticalClimb( trackpoints ) );
		}
		
		//get profile
    	byte [] profile = getImageBytes( ProfileService.getProfile(trackpoints,currentRoute).createBufferedImage(540, 180) );
    	//create multimedia (profile)
    	//some string ops for fileName
    	String name = t.getName();
    	
    	contentItemService.updateMediaContentItem(currentRoute.getDelegate(), profile, "image/png", name);
    	
		//persist trackpoints
		int ordinal = 0;
		for( TrackPointFacade tp : trackpoints ) {

			//set ordinal
	    	tp.setOrdinal(ordinal);
	    	contentItemService.updateTitle(tp, "R:"+currentRoute.getId()+"_TP:"+ordinal);
	    	++ordinal;
			
			//persist
			kiwiEntityManager.persist(tp);

		}
		
    	//set Title and TODO description
    	contentItemService.updateTitle(currentRoute, t.getName());
		currentRoute.setTrackPoints(trackpoints);
		
//		kiwiEntityManager.flush();

		// save again to process updates
    	kiwiEntityManager.refresh(currentRoute);
    	
    	//set route as currentContentItem
    	currentContentItemFactory.setCurrentItemId(currentRoute.getId());
    	currentContentItemFactory.refresh();
    	
		} catch (Exception e) {
			log.error("persist route #0 failed", t.getName());
			e.printStackTrace();
		}
		
		return 1;
		
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
