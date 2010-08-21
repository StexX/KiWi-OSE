package tagit2.api.query;

import java.util.List;

import kiwi.model.content.ContentItem;
import kiwi.model.facades.PointOfInterestFacade;
import tagit2.util.query.Point;

public interface IconService extends IconServiceLocal, IconServiceRemote {
	
	public void setIconsOf( List<Point> points );
	public String getIconOf( ContentItem contentItem );
	
	//returns an icon for a PointOfInterestFacade of type "BlogPost"
	public String getIconStringForBlogPost(PointOfInterestFacade blog);

}
