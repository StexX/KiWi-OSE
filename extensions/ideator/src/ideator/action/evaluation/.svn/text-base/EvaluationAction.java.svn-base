package ideator.action.evaluation;

import ideator.datamodel.IdeaFacade;

import java.util.LinkedList;
import java.util.List;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.group.GroupService;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.user.UserService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.user.Group;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Scope(ScopeType.PAGE)
@Name("ideator.evaluationAction")
public class EvaluationAction {
	
	private Evaluation evaluation;
	
	private IdeaFacade idea;
	
	private List<String> users;
	
	private boolean ideaManager;
	
	@In(create=true)
	private User currentUser;
	
	@In
	private ContentItem currentContentItem;
	
    @In
    private SolrService solrService;

    @In
    private UserService userService;
    
    @In
    private GroupService groupService;
	
	@In(create=true,value="ideator.xmlEvaluationService")
	private XMLEvaluationService xmlEvaluationService;
	
	@In
	KiWiEntityManager kiwiEntityManager;
	
	@Logger
	Log log;
	
//	@Begin(join=true)
	@Create
	public void begin() {
		try {
			idea = kiwiEntityManager.createFacade(currentContentItem,IdeaFacade.class);
			
			ideaManager = checkIdeaManager(currentUser);
			
			if( ideaManager ) evaluation = xmlEvaluationService.getEvaluation(idea);
			else evaluation = xmlEvaluationService.getEvaluation(idea,currentUser);
			
			//get all Users
			users = new LinkedList<String>();
			KiWiSearchCriteria c = new KiWiSearchCriteria();
			c.setSolrSearchString("type:\"uri::" + Constants.NS_KIWI_CORE
				+ "User\"");
			c.setLimit(-1);
			for (SearchResult r : solrService.search(c).getResults()) {
			    User u = userService.getUserByProfile(r.getItem());
			    users.add(u.getLogin());
			}
		} catch (Exception e) {
			log.info("some failure");
			e.printStackTrace();
		}
	}

	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}
	
//	@End
	public String save() {
		xmlEvaluationService.setEvaluation(idea, evaluation);
		kiwiEntityManager.persist(idea);
		return "/ideator/idea.xhtml";
	}
	
//	@End
	public String cancel() {
		return "/ideator/idea.xhtml";
	}
	
	public String reset() {
		for( Paragraph p : evaluation.getParagraphs() ) {
			for( Criteria c : p.getCriteria() ) {
				c.setSelected(-1);
			}
		}
		xmlEvaluationService.setEvaluation(idea, evaluation);
		kiwiEntityManager.persist(idea);
		return "/ideator/evaluation.xhtml";
	}
	
	public List<String> getAllUsers() {
		return users;
	}
	
    private boolean checkIdeaManager(User user) {
    	List<Group> sr = groupService.getGroupsByUser(user);
    	for (Group g : sr) {
    	    log.info("AdminAction" + g.getName());
    	    if (g.getName().equals("Ideamanager")) {
    	    	return true;
    	    }
    	}
    	return false;
    }
    
    public boolean isIdeaManager() {
    	return ideaManager;
    }
	
}
