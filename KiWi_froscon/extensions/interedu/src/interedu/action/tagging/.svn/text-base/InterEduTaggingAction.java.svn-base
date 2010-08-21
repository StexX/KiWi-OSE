package interedu.action.tagging;

import interedu.api.dataimport.InterEduArtikelFacade;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

@Name("interedu.InterEduTaggingAction")
//@Transactional
@Scope(ScopeType.STATELESS)
public class InterEduTaggingAction {
	@Logger
	private static Log log;
	
    @In FacesMessages facesMessages;
	
	// input current user; might affect the loading of the content item
	@In(create = true)
	private User currentUser;
	
	// the entity manager used by this KiWi system
	@In
	private EntityManager entityManager;
	
	@In
	private TripleStore tripleStore;

	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private ContentItemService contentItemService;
	
	
	@In
	private TaggingService taggingService;
 
    // newly created tag
	private String tagLabel;

	
	/**
	 * Action called by tagging.xhtml add button when submitting a new tag for the resource
	 */
	public void addTagTo(InterEduArtikelFacade article) {
		log.info("adding new tags for input #0", tagLabel);

		// avoid detached entities
//		currentUser = entityManager.merge(currentUser);
		if(!entityManager.contains(currentUser)) {
			currentUser = entityManager.find(User.class, currentUser.getId());
		}
		
		String[] components = tagLabel.split(",");
		
		for(String component : components) {
			
			String label = component.trim();
			if( !label.equals("") ) {
				log.info("adding tag -#0-",label);
				addTag(label,article);
			}
		}
//		entityManager.flush();
		
		article.setState( InterEduArtikelFacade.IN_USE );
		article.setModified( new Date() );
		
		kiwiEntityManager.persist(article);
		tripleStore.persist(article.getDelegate());
		
//		kiwiEntityManager.flush();
		
		entityManager.refresh(currentUser);
		tripleStore.persist(currentUser);
		
		log.info("tag added successfully");
	}

	public void addTag(String label, InterEduArtikelFacade article) {
			
		// TODO: query by uri instead of by title
		ContentItem taggingItem = contentItemService.getContentItemByTitle(label);

		if(taggingItem == null) {
			// create new Content Item of type "tag" if the tag does not yet exist
			taggingItem = contentItemService.createContentItem("content/"+label.toLowerCase().replace(" ","_")+"/"+UUID.randomUUID().toString());
			taggingItem.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag"));
			contentItemService.updateTitle(taggingItem, label);
			kiwiEntityManager.persist(taggingItem);
			log.info("created new content item for non-existant tag");
		}


		taggingService.createTagging(label, article.getDelegate(), taggingItem, currentUser);
					
		kiwiEntityManager.persist(article);
//		kiwiEntityManager.flush();

		Events.instance().raiseEvent("tagUpdate");
	}
	
	
	public void addTagLine(String label) {
		if(tagLabel != null && !"".equals(tagLabel)) {
			tagLabel += ", ";
		}
		tagLabel += label;
	}
	
	public void removeTag(ContentItem taggingItem, InterEduArtikelFacade article) {
		log.info("removing tag #0 by user #1",taggingItem.getTitle(),currentUser.getLogin());
		
		// avoid detached entities
//		currentUser = entityManager.merge(currentUser);
		if(!entityManager.contains(currentUser)) {
			currentUser = entityManager.find(User.class, currentUser.getId());
		}
		//article = kiwiEntityManager.merge(article);

		Query q = entityManager.createNamedQuery("taggingAction.getTagByIdAuthor");
		q.setParameter("login", currentUser.getLogin());
		q.setParameter("id",taggingItem.getId());
		
		List<Tag> l = (List<Tag>)q.getResultList();
		for(Tag t : l) {
			taggingService.removeTagging(t);
		}
		
		article.setState(InterEduArtikelFacade.IN_USE);
		article.setModified( new Date() );
		
		kiwiEntityManager.persist(article);
//		kiwiEntityManager.flush();

		Events.instance().raiseEvent("tagUpdate");
	}
	
	
	/**
	 * @return the tagLabel
	 */
	public String getTagLabel() {
		return tagLabel;
	}

	/**
	 * @param tagLabel the tagLabel to set
	 */
	public void setTagLabel(String tagLabel) {
		this.tagLabel = tagLabel;
	}
	
	public void addTagTo(List<InterEduArtikelFacade> articleList) {
		
		for( InterEduArtikelFacade art : articleList ) {
			addTagTo(art);
		}
		
	}
	
	public List<String> autocomplete() {
		List<String> auto =  new LinkedList<String>();
		return auto;
	}
}
