package tagit2.service.category;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

import tagit2.api.category.CategoryService;

/**
 * this class builds methods that returns all tagit topConcepts /
 * all subconcepts for a given concept. The list are ordered by title
 * @author tkurz
 *
 */
@Scope(ScopeType.STATELESS)
@Name("tagit2.categoryService")
//@Transactional
@AutoCreate
public class CategoryServiceImpl implements CategoryService {

    @In
    private KiWiEntityManager kiwiEntityManager;
	
    @In
	private TripleStore tripleStore;
    
	@In
	private ContentItemService contentItemService;
	
    /**
     * returns all tagit topConcepts
     */
	public List<SKOSConcept> listCategories() {
		
		KiWiUriResource r_tagit_topic = tripleStore.createUriResource(Constants.NS_TAGIT+"LocationTopic");
		
		SKOSConcept c_tagit_topic = kiwiEntityManager.createFacade(r_tagit_topic.getContentItem(), SKOSConcept.class);

		LinkedList<SKOSConcept> categories = new LinkedList<SKOSConcept>();
		
		categories.addAll(c_tagit_topic.getNarrower());
		
		//categories = skosService.getTopConcepts();
		
    	Collections.sort(categories, new Comparator<SKOSConcept>() {
    		@Override
    		public int compare(SKOSConcept o1, SKOSConcept o2) {
    			return Collator.getInstance().compare(o1.getTitle(), o2.getTitle());
    		}
    	});
    	
		return categories;
	}

	/**
	 * returns all subcategory of a given category, if category is null, an emty list is returned
	 */
	public List<SKOSConcept> listSubcategories(SKOSConcept category) {
    	
		LinkedList<SKOSConcept> subcategories = new LinkedList<SKOSConcept>();
    	
		if( category == null )
			return subcategories;
		
    	subcategories.addAll(category.getNarrower());
    	
    	Collections.sort(subcategories, new Comparator<SKOSConcept>() {
    		@Override
    		public int compare(SKOSConcept o1, SKOSConcept o2) {
    			return Collator.getInstance().compare(o1.getTitle(), o2.getTitle());
    		}
    	});
    	
    	return subcategories;
    	
	}
	
	/**
	 * returns all subcategory of a given category, if category is null, an emty list is returned
	 */
	public List<String> listSubcategoryStrings(SKOSConcept category) {
    	
		LinkedList<String> subcategories = new LinkedList<String>();
    	
		if( category == null )
			return subcategories;
		
    	for( SKOSConcept c : category.getNarrower() ) {
    		subcategories.add(c.getTitle());
    	}
    	
    	Collections.sort(subcategories, new Comparator<String>() {
    		@Override
    		public int compare(String o1, String o2) {
    			return Collator.getInstance().compare(o1,o2);
    		}
    	});
    	
    	return subcategories;
    	
	}
	
    /**
     * returns all tagit topConcepts as Strings
     */
	public List<String> listCategoryStrings() {
		
		KiWiUriResource r_tagit_topic = tripleStore.createUriResource(Constants.NS_TAGIT+"LocationTopic");
		
		SKOSConcept c_tagit_topic = kiwiEntityManager.createFacade(r_tagit_topic.getContentItem(), SKOSConcept.class);

		LinkedList<String> categories = new LinkedList<String>();
		
		for( SKOSConcept c : c_tagit_topic.getNarrower() ) {
			categories.add(c.getTitle());
		}
		
    	Collections.sort(categories, new Comparator<String>() {
    		@Override
    		public int compare(String o1, String o2) {
    			return Collator.getInstance().compare(o1, o2);
    		}
    	});
    	
		return categories;
	}
	
	/**
	 * this method looks, if a Subconcept of concept with title String already exists.
	 * if not, a new Subcategory is created
	 * @return
	 */
	public SKOSConcept getSubcategory(String scTitle, SKOSConcept category) {
		
		SKOSConcept cat = null;
		
		//test if subcategory already exists
		for( SKOSConcept c: listSubcategories(category) ) {
			if( scTitle.equals(c.getTitle()) ) {
				cat = c;
				break;
			}
		}
		
		if( cat == null ) {
	    	cat = kiwiEntityManager.createFacade(
	    			contentItemService.createContentItem(), 
	    			SKOSConcept.class);
	    	contentItemService.updateTitle(cat, scTitle);
	    	cat.setPreferredLabel(scTitle);
	    	
	    	kiwiEntityManager.persist(cat);
	    	
	    	cat.setBroader(category);
	    	HashSet<SKOSConcept> cs = category.getNarrower();
	    	cs.add(cat);
	    	category.setNarrower(cs);
	    	
//	    	kiwiEntityManager.flush();
		}
		
		return cat;
	}
	
	/**
	 * if no category with this title exists, null is returned
	 * @param title
	 * @return
	 */
	public SKOSConcept getCategory( String title ) {
		for( SKOSConcept c : this.listCategories() ) {
			if( c.getTitle().equals(title) ) {
				return c;
			}
		}
		return null;
	}

}
