package interedu.service.layout;

import javax.persistence.EntityManager;

import interedu.api.dataimport.InterEduArtikelFacade;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.render.RenderingService;
import kiwi.model.content.ContentItem;
import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

@Scope(ScopeType.STATELESS)
@Name("interedu.articelFacadeService")
//@Transactional
public class ArticelFacadeService {
	
	@In
	KiWiEntityManager kiwiEntityManager;
	
    @In
    EntityManager entityManager;
	
	@Logger
	Log log;
	
    @In
    private RenderingService renderingPipeline;

	public InterEduArtikelFacade getArtikelFacade(ContentItem ci) {
		return kiwiEntityManager.createFacade(ci, InterEduArtikelFacade.class);
	}
	
	public String getRenderedDescription(InterEduArtikelFacade art) {
		return renderingPipeline.renderHTML(art.getDelegate());
	}
	
	public void artikelToGreen(InterEduArtikelFacade art) {
		art.setState(InterEduArtikelFacade.COMMITTIT);
		kiwiEntityManager.persist(art);
//		kiwiEntityManager.flush();
	}
	
	public void artikelToYellow(InterEduArtikelFacade art) {
		art.setState(InterEduArtikelFacade.IN_USE);
		kiwiEntityManager.persist(art);
//		kiwiEntityManager.flush();
	}
	
	public SKOSConcept getConcept(long id) {
		return kiwiEntityManager.createFacade( entityManager.find(ContentItem.class, id) , SKOSConcept.class) ;
	}
	
}
