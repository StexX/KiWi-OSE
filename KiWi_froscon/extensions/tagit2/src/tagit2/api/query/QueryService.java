package tagit2.api.query;

import tagit2.util.geo.Area;
import tagit2.util.geo.Ratio;
import tagit2.util.query.TagitSearchResult;

public interface QueryService extends QueryServiceLocal, QueryServiceRemote {
	
	public TagitSearchResult getClusteredMarkers(Area area, Ratio ratio, String searchString, String layerSearchString);
	public TagitSearchResult getMarkers(Area area, String searchString, String layerSearchString);
	public Area getBounds( String searchString, String layerSearchString);

}
