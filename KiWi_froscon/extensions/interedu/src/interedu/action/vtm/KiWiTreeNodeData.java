package interedu.action.vtm;

import java.util.Locale;

import kiwi.action.webservice.thesaurusManagement.SKOSConceptUtils;
import kiwi.model.ontology.SKOSConcept;

public class KiWiTreeNodeData {

	private SKOSConcept concept;
	
	public KiWiTreeNodeData(SKOSConcept concept) {
		this.concept = concept;
	}
	
	public String toString() {
		return SKOSConceptUtils.getConceptLabelInLanguage(concept, new Locale("en"));
	}
	
	public SKOSConcept getConcept() {
		return concept;
	}
}
