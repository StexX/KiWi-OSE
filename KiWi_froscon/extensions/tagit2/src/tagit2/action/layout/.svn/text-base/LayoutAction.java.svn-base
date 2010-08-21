package tagit2.action.layout;

import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiResource;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import tagit2.action.content.LocationAction;
import tagit2.action.content.NewLocationAction;
import tagit2.action.content.NewRouteAction;
import tagit2.action.content.NewsItemAction;
import tagit2.action.content.PersonAction;
import tagit2.action.content.RouteAction;
import tagit2.action.explorer.ExplorerAction;

/**
 * this class is used to handle different layouts
 * @author tkurz
 *
 */
@Scope(ScopeType.STATELESS)
@Name("tagit2.layoutAction")
@AutoCreate
public class LayoutAction {
	
//	@In
//	private User currentUser;
	
    @In(create=true)
    private ContentItem currentContentItem;
    
    @In(create=true, value="tagit2.explorerAction")
    private ExplorerAction explorerAction;
	
    @In(create=true,value="tagit2.newsItemAction")
    private NewsItemAction newsItemAction;
    
    @In(create=true,value="tagit2.locationAction")
    private LocationAction locationAction;
    
    @In(create=true,value="tagit2.newLocationAction")
    private NewLocationAction newLocationAction;
    
    @In(create=true, value="tagit2.newRouteAction")
    private NewRouteAction newRouteAction;
    
    @In(create=true, value="tagit2.routeAction")
    private RouteAction routeAction;
    
    @In(create=true, value="tagit2.personAction")
    private PersonAction personAction;
    
    @Logger
    Log log;
	/**
	 * 
	 * @param ci
	 * @return
	 */
	public String getTagitLayoutPath() {
		
		String res = "content/none.xhtml";
		
		//if explorer is in createMode
		switch( explorerAction.getMode() ) {
		case CREATE_LOCATION:
			res = "content_new/location_new.xhtml";
			if( newLocationAction.getMode() == -1 ) {
				newLocationAction.begin();
			}
			break;
		case CREATE_ROUTE:
			res = "content_new/route_new.xhtml";
			if( newRouteAction.getMode() != 2 ) {
				newRouteAction.setMode(2);
			}
			break;
		default:
			//there must be a ranking because everything that is displayable on tagit
			//is a PointOfInetrest but maybe also another type (like a route)
			
			//until then we break, when contentItem has topType (like route)
			
			//init with none
			for( KiWiResource type : currentContentItem.getTypes() ) {
				String seRQLID = type.getSeRQLID();

				if(seRQLID.contains(Constants.NS_FCP_CORE+"Location")) {
					res = "content/location.xhtml";
					locationAction.begin();
					break;
				} else if(seRQLID.contains(Constants.NS_KIWI_CORE+"BlogPost")) {
					//TODO should be a specific Blog interface
					res = "content/blog.xhtml";
					newsItemAction.begin();
					break;
				} else if(seRQLID.contains(Constants.NS_FCP_CORE+"NewsItem")) {
					res = "content/newsItem.xhtml";
					newsItemAction.begin();
					break;
				} else if(seRQLID.contains(Constants.NS_TAGIT + "Route")) {
					//TODO CHANGE!!!
					res = "content/route.xhtml";
					//res = "content/photoroute.xhtml";
					routeAction.begin();
					break;
				} else if(seRQLID.contains(Constants.NS_KIWI_CORE + "User")) {
					//if( currentUser.getContentItem().getId() == currentContentItem.getId() ) {
					//	res = "content/user.xhtml";
					//	personAction.begin();
					//	break;
					//} else {
						res = "content/person.xhtml";
						personAction.begin();
						break;
					//}
				} else if(seRQLID.contains(Constants.NS_DEMO+"LocatedMeeting")) {
					res = "content/newsItem.xhtml";
					newsItemAction.begin();
					break;
				}
				
			}
		
//TODO some common type for all pois
		}
		log.info("set layout to #0",res);
		return res;
	}
	
}
