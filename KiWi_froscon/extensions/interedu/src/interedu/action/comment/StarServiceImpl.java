package interedu.action.comment;

import interedu.api.comment.StarService;
import interedu.api.dataimport.InterEduComment;

import javax.ejb.Stateless;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Stateless
@Scope(ScopeType.STATELESS)
@Name("interedu.starService")
public class StarServiceImpl implements StarService {
	
	@Logger
	Log log;
	
	public String getStarDisplay(InterEduComment comment) {
		int value = comment.getRating();
		return getStars(value);
	}
	
	public String getStars(int value) {
		
		log.info("star value is #0", value);
		
		StringBuilder s = new StringBuilder();
		
		for( int i = 1; i < 6; i++ ) {
			if( i <= value) {
				s.append( "<img alt=\"Stern\" src=\"images/icon_star.png\"/>" );
			} else {
				s.append( "<img alt=\"Stern grey\" src=\"images/icon_star_grey.png\"/>" );
			}
		}
		
		return s.toString();
		
	}

}
