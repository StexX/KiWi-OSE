package ideator.action;

import ideator.datamodel.IdeaFacade;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

@Name("ideator.utils")
@Scope(ScopeType.SESSION)
//@Transactional
public class IdeatorUtils {
	
	@In
    private KiWiEntityManager kiwiEntityManager;
	
	@In
	private ContentItemService contentItemService;
	
	public boolean isAnonymous(ContentItem ci){
		IdeaFacade ideaFacade = kiwiEntityManager.createFacade(ci, IdeaFacade.class);
		return ideaFacade.getAnonymous();
	}
//	
//	public boolean isInEvaluator(ContentItem ci){
//		IdeaFacade ideaFacade = kiwiEntityManager.createFacade(ci, IdeaFacade.class);
//		return ideaFacade.getStatus() == IdeaFacade.EVALUATION;
//	}
	
}
