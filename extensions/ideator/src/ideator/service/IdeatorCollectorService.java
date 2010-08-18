package ideator.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

@Name("ideator.collector")
@Scope(ScopeType.SESSION)
//@Transactional
@AutoCreate
public class IdeatorCollectorService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Logger
	private static Log log;
	
	private long id;
	
	@In
	private EntityManager entityManager;
	
	private Map<Long, ContentItem> articleCollection;
	
	@Create
	public void create() {
		articleCollection = new HashMap<Long, ContentItem>();
	}
	
	public List<ContentItem> getArticleCollection() {
		List<ContentItem> l = new LinkedList<ContentItem>();
		Iterator<ContentItem> i = articleCollection.values().iterator();
		while( i.hasNext() ) {
			l.add(i.next());
		}
		return l;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	public boolean isArticleCollectionEmpty() {
		return articleCollection.isEmpty();
	}
	
	public boolean isSelected(long id) {
		return articleCollection.containsKey(id);
	}
	
	public void select() {
		log.info("select #0",id);
		ContentItem a = entityManager.find( ContentItem.class, id);
		if( !articleCollection.containsKey(a.getId()) ) {
			articleCollection.put(a.getId(), a);
		}
		log.info("list contains now #0 elements", articleCollection.size());
	}
	
	public void unselect(ContentItem a) {
		articleCollection.remove(a.getId());
	}
	
	//**** actions
	public void clearAll() {
		articleCollection = new HashMap<Long, ContentItem>();
	}
	
}
