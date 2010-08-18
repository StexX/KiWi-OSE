package ideator.action.preEvaluation;

import ideator.datamodel.IdeaFacade;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Scope(ScopeType.CONVERSATION)
@Name("ideator.preEvaluationAction")
public class PreEvaluationAction {
	
	@Logger
	private Log log;
	
	private List<PreCriteria> preCriterias;
	
	@In
	private ContentItem currentContentItem;
	
	private IdeaFacade idea;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	
	@Create
	public void begin() {
		try {
			idea = kiwiEntityManager.createFacade(currentContentItem,IdeaFacade.class);
		}
		 catch (Exception e) {
			 log.info("some failure");
			 e.printStackTrace();
		 }
	}
		
	public String accept(Long status){	
		if(status == preCriterias.size()){
			idea.setStatus(IdeaFacade.EVALUATION);
			idea.setPreEvaluationStatus(status.intValue());
			kiwiEntityManager.persist(idea);			
			log.info("Pre evaluation successfull");
			return "/ideator/accepted.xhtml";
		}
		else{
			log.info("Criteria Accepted");
			return null;
		}		
	}
	
	public String reject(Long status){
		idea.setStatus(IdeaFacade.ARCHIVE);
		idea.setPreEvaluationStatus(status.intValue());
		kiwiEntityManager.persist(idea);
		FacesMessages.instance().add("Idea rejected");
		log.info("idea rejected "+status);
		return "/ideator/idea.xhtml";
	}
	
	

	public List<PreCriteria> getCriterias() throws Exception {
		try {
			InputStream in = getClass().getClassLoader().getResourceAsStream("data/preEval.xml");
			// parse and normalize
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(in);
			doc.getDocumentElement().normalize();
			// read paragraphs
			NodeList criterias = doc.getElementsByTagName("criteria");

			preCriterias = new LinkedList<PreCriteria>();
			
//			values = new boolean[criterias.getLength()];
			
			for (int i = 0; i < criterias.getLength(); i++) {
				// get question and preEvalStatus
				Element criteria = (Element) criterias.item(i);
				String c_value = criteria.getAttribute("question");
				int c_preEvalStatus = Integer.valueOf(criteria
						.getAttribute("preEvalStatus"));

				PreCriteria pc = new PreCriteria();
				pc.setQuestion(c_value);
				pc.setPreEvalStatus(c_preEvalStatus);

				preCriterias.add(pc);

				
			}
			in.close();

			return preCriterias;
		}
		catch (Exception e) {
			throw new Exception("");
		}
	}
	
	//returns true if an idea has the status in evaluation or higher
	public boolean inEvaluation(){
		if(idea.getStatus() >= IdeaFacade.EVALUATION){
			return true;
		}
		else{
			return false;
		}
	}
}
