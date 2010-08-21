package tagit2.api.category;

import java.util.List;

import kiwi.model.ontology.SKOSConcept;

public interface CategoryService extends CategoryServiceLocal, CategoryServiceRemote {
	
	public List<SKOSConcept> listCategories();
	public List<SKOSConcept> listSubcategories(SKOSConcept category);
	
	public List<String> listCategoryStrings();
	public List<String> listSubcategoryStrings(SKOSConcept category);
	
	public SKOSConcept getSubcategory(String scTitle, SKOSConcept category);
	public SKOSConcept getCategory( String title );
	
}
