package interedu.action.search;

import kiwi.model.ontology.SKOSConcept;

public class SKOSConceptCover {
	
	private SKOSConcept concept;
	private String label;
	public SKOSConceptCover(SKOSConcept concept, String label) {
		this.concept = concept;
		this.label = label;
	}
	public SKOSConcept getConcept() {
		return concept;
	}
	public void setConcept(SKOSConcept concept) {
		this.concept = concept;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	

}
