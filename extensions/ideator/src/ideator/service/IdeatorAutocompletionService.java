package ideator.service;

import java.util.LinkedList;
import java.util.List;

import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.user.UserService;
import kiwi.model.Constants;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

@Name("ideator.autocompletionService")
@Scope(ScopeType.STATELESS)
//@Transactional
@AutoCreate
public class IdeatorAutocompletionService {
	
	@Logger
	private Log log;
	
	@In
	private SolrService solrService;
	
	@In
	private UserService userService;
	
	public List<User> getUsersByKeyword(String key) {
		List<User> l = new LinkedList<User>();
		
		//prepare key
		key = key.toLowerCase();
		
		if( key.endsWith(" ") ) {
			key = key.trim();
		} else {
			key = key+"*";
		}
		
		key = key.replaceAll(" ", " AND title:");
		
		//query
		KiWiSearchCriteria c = new KiWiSearchCriteria();
		c.setSolrSearchString("type:\"uri::"+Constants.IDEATOR_CORE+ "IdeatorUser\" (title:"+key+")");
		c.setLimit(-1);
		for( SearchResult r : solrService.search( c ).getResults() ) {
			l.add( userService.getUserByProfile(r.getItem()) );
		}
		log.info("#0 autocompletion results", l.size());
		return l;
	}

}
