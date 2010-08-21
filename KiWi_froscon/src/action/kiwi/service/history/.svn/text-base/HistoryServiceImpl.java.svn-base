package kiwi.service.history;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.config.ConfigurationService;
import kiwi.api.history.HistoryServiceLocal;
import kiwi.api.history.HistoryServiceRemote;
import kiwi.model.activity.AddTagActivity;
import kiwi.model.activity.CommentActivity;
import kiwi.model.activity.EditActivity;
import kiwi.model.activity.SearchActivity;
import kiwi.model.activity.VisitActivity;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * @author Fred Durao, Nilay Coskun
 *s
 */
@Stateless
@Scope(ScopeType.SESSION)
@AutoCreate
@Name("historyService")
public class HistoryServiceImpl implements HistoryServiceLocal, HistoryServiceRemote  {
	
	@Logger
	private Log log;
	
	@In
	private EntityManager entityManager;
	
	@In(create=true)
	private User currentUser;	
	
	@In(create=true)
	private ConfigurationService configurationService;		

	private int histSize = 5;
	
	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#listLastVisitsByUser(kiwi.model.user.User)
	 */
	public List<VisitActivity> listLastVisitsByUser(User user) {
		log.info("listing last user visits...");
		Query q = entityManager.createNamedQuery("activities.listLastUserVisits");
		q.setParameter("login", user.getLogin());
		q.setHint("org.hibernate.cacheable", true);
		q.setMaxResults(histSize);
	
		return q.getResultList();
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#listVisitsByUser(kiwi.model.user.User)
	 */
	public List<VisitActivity> listVisitsByUser(User user) {
		log.info("listing  user visits...");
		Query q = entityManager.createNamedQuery("activities.listLastUserVisits");
		q.setParameter("login", user.getLogin());
		q.setHint("org.hibernate.cacheable", true);
	
		return q.getResultList();
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#listLastVisitByUser(kiwi.model.user.User)
	 */
	public VisitActivity listLastVisitByUser(User user) {
		VisitActivity visitActivity = null;
		log.info("listing  user visits...");
		Query q = entityManager.createNamedQuery("activities.listLastUserVisits");
		q.setParameter("login", user.getLogin());
		q.setMaxResults(1);
		q.setHint("org.hibernate.cacheable", true);
		try {
			visitActivity = (VisitActivity)q.getSingleResult();
		} catch (NoResultException ex) {
				log.warn("no visity activity detected");
		}
		return visitActivity;
	}	
	
	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#listLastEditsByUser(kiwi.model.user.User)
	 */
	public List<EditActivity> listLastEditsByUser(User user) {
		log.info("listing last user edits...");
		Query q = entityManager.createNamedQuery("activities.listLastUserEdits");
		q.setParameter("login", user.getLogin());
		q.setHint("org.hibernate.cacheable", true);
		q.setMaxResults(histSize);
	
		return q.getResultList();	
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#listEditsByUser(kiwi.model.user.User)
	 */
	public List<EditActivity> listEditsByUser(User user) {
		log.info("listing user edits...");
		Query q = entityManager.createNamedQuery("activities.listLastUserEdits");
		q.setParameter("login", user.getLogin());
		q.setHint("org.hibernate.cacheable", true);
	
		return q.getResultList();
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#listLastCommentsByUser(kiwi.model.user.User)
	 */
	public List<CommentActivity> listLastCommentsByUser(User user) {
		log.info("listing last user edits...");
		Query q = entityManager.createNamedQuery("activities.listLastUserComments");
		q.setParameter("login", user.getLogin());
		q.setHint("org.hibernate.cacheable", true);
		q.setMaxResults(histSize);
	
		return q.getResultList();
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#listLastCommentsByUser(kiwi.model.user.User)
	 */
	public List<CommentActivity> listCommentsByUser(User user) {
		log.info("listing last commebts by user...");
		Query q = entityManager.createNamedQuery("activities.listLastUserComments");
		q.setParameter("login", user.getLogin());
		q.setHint("org.hibernate.cacheable", true);
		
		return q.getResultList();
	}	
	
	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#listLastSearchesByUser(kiwi.model.user.User)
	 */
	public List<SearchActivity> listLastSearchesByUser(User user) {
		log.info("listing last user searches...");
		Query q = entityManager.createNamedQuery("activities.listLastUserSearches");
		q.setParameter("login", user.getLogin());
		q.setHint("org.hibernate.cacheable", true);
		q.setMaxResults(histSize);
	
		return q.getResultList();	
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#listLastDistinctSearchesByUser(kiwi.model.user.User)
	 */
	public List<SearchActivity> listLastDistinctSearchesByUser(User user) {
		log.info("listing last distinct user searches...");
		Query q = entityManager.createNamedQuery("activities.listLastDistinctUserSearches");
		q.setParameter("login", user.getLogin());
		q.setHint("org.hibernate.cacheable", true);
		q.setMaxResults(10);
	
		return q.getResultList();	
	}	
	
	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#listLastDistinctSearchesByUser(kiwi.model.user.User)
	 */
	public List<VisitActivity> listLastDistinctsVisitsByUser(User user) {
		log.info("listing last distinct user visits...");
		Query q = entityManager.createNamedQuery("activities.listLastDistinctUserVisits");
		q.setParameter("login", user.getLogin());
		q.setHint("org.hibernate.cacheable", true);
		q.setMaxResults(10);
		return q.getResultList();	
	}		
	
	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#listSearchesByUser(kiwi.model.user.User)
	 */
	public List<SearchActivity> listSearchesByUser(User user) {
		log.info("listing user edits...");
		Query q = entityManager.createNamedQuery("activities.listLastUserSearches");
		q.setParameter("login", user.getLogin());
		q.setHint("org.hibernate.cacheable", true);

		return q.getResultList();
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#listLastTagsByUser(kiwi.model.user.User)
	 */
	public List<AddTagActivity> listLastTagsByUser(User user) {
		log.info("listing last user tags...");
		Query q = entityManager.createNamedQuery("activities.listLastUserTags");
		q.setParameter("login", user.getLogin());
		q.setHint("org.hibernate.cacheable", true);
		q.setMaxResults(histSize);
	
		return q.getResultList();
	}
	
	public List<AddTagActivity> listTagsByUser(User user) {
		log.info("listing  user tags...");
		Query q = entityManager.createNamedQuery("activities.listLastUserTags");
		q.setParameter("login", user.getLogin());
		q.setHint("org.hibernate.cacheable", true);
	
		return q.getResultList();
	}

	/* (non-Javadoc)
	 * @see kiwi.api.history.HistoryService#getLastVisitedPage()
	 */
	public String getLastVisitedPage() {
		VisitActivity visityActivity = listLastVisitByUser(currentUser);
		if (visityActivity!=null) {
			KiWiUriResource kiwiUriResource =  (KiWiUriResource)visityActivity.getContentItem().getResource();
			if (kiwiUriResource.isUriResource()) {
				return kiwiUriResource.getUri();
			}else{
				return configurationService.getStartPage();
			}
		}else{
			return configurationService.getStartPage();
		}
	}
}
