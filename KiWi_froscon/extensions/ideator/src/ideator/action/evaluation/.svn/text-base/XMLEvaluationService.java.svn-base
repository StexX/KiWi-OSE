package ideator.action.evaluation;

import ideator.datamodel.IdeaFacade;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Name("ideator.xmlEvaluationService")
@Scope(ScopeType.STATELESS)
public class XMLEvaluationService {

	private static String FILENAME = "eval.xml";
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	public void setEvaluation(IdeaFacade idea, Evaluation evaluation) {
		preprocess(evaluation);
		if( idea.getEvaluation() == null ) {
			ContentItem ci = contentItemService.createContentItem();
			contentItemService.updateMediaContentItem(ci, evaluation.toXMLString().getBytes(), "application/xml", FILENAME);
			idea.setEvaluation(ci);
		} else {
			contentItemService.updateMediaContentItem(idea.getEvaluation(), evaluation.toXMLString().getBytes(), "application/xml", FILENAME);
		}
		
		//set evaluators
		LinkedList<String> evaluators = new LinkedList<String>();
		for( Paragraph p : evaluation.getParagraphs() ) {
			if( p.getUser() != null && !p.getUser().equals("") ) evaluators.add(p.getUser());
		}
		idea.setEvaluators(evaluators);
		
		//is full evaluated
		if( evaluation.isComplete() ) idea.setStatus( IdeaFacade.EVALUATED );
		else idea.setStatus( IdeaFacade.EVALUATION );
	}

	public Evaluation getEvaluation(IdeaFacade idea) throws Exception {
		return getEvaluation(idea,null);
	}
	
	public Evaluation getEvaluation(IdeaFacade idea, User user) throws Exception {
		InputStream in;
		if( idea.getEvaluation() == null ) {
			in = getClass().getClassLoader().getResourceAsStream("data/eval.xml");
		} else {
			in = new ByteArrayInputStream(idea.getEvaluation().getMediaContent().getData());
		}
		Evaluation eval = this.getEvaluation(in, user);
		in.close();
		
		return eval;
	}
	
	public List<EvaluationResource> getEvaluationResources(List<ContentItem> list) {
		List<EvaluationResource> eval = new LinkedList<EvaluationResource>();
		for( ContentItem ci : list ) {
			try {
				IdeaFacade i = kiwiEntityManager.createFacade(ci, IdeaFacade.class);
				if( i.getStatus() > i.EVALUATION ) {
					eval.add( new EvaluationResource(getEvaluation(i),i) );
				}
			} catch(Exception e) {
				//should not happen
				e.printStackTrace();
			}
		}
		return eval;
	}
	
	private Evaluation getEvaluation(InputStream inputStream, User user) throws Exception {
		try {
			Evaluation eval = new Evaluation();

			//parse and normalize
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inputStream);
			doc.getDocumentElement().normalize();
		  
			//read paragraphs
			NodeList paragraphs = doc.getElementsByTagName("paragraph");
			for (int i = 0; i < paragraphs.getLength(); i++) {
				//get title and factor
				Element paragraph = (Element) paragraphs.item(i);
				String p_title = paragraph.getAttribute("title");
				
				Paragraph para = new Paragraph(p_title);
				
				if( paragraph.getAttribute("user") != null ) {
					String p_user = paragraph.getAttribute("user");
					para.setUser(p_user);
					
					//filter
					if( user != null && para.getUser().equals(user.getLogin()) ) {
						para.setRendered(true);
					}
				}
				
				if( user == null ) {
					para.setRendered(true);
				}
				
				//read criterias
				NodeList criterias = paragraph.getElementsByTagName("criteria");
				for (int j = 0; j < criterias.getLength(); j++) {
					//get title and factor
					Element criteria = (Element) criterias.item(j);
					String c_title = criteria.getAttribute("title");
					Criteria q = new Criteria(c_title);

					//get rating and comment
					Element rating = (Element)criteria.getElementsByTagName("rating").item(0);
					if( criteria.getElementsByTagName("comment").getLength() != 0 ) {
						String comment = criteria.getElementsByTagName("comment").item(0).getTextContent();
						q.setComment(comment);
					}
					
					//read choices
					NodeList choices = rating.getElementsByTagName("choice");
					for( int z = 0; z < choices.getLength(); z++ ) {
						//get value and selected
						Element choice = (Element) choices.item(z);
						int a_value = Integer.parseInt(choice.getAttribute("value"));
						String a_title = choices.item(z).getChildNodes().item(0).getNodeValue().trim();
						
						Rating a = new Rating(a_title, a_value);
						
						boolean a_selected = Boolean.parseBoolean(choice.getAttribute("selected"));
						if( a_selected ) {
							q.setSelected(z);
						}
						
						q.add(a);
					}
					
					para.add(q);
					
				}
				
				eval.add(para);
				
			}
			return eval;
			
		} catch (Exception e) {
			throw new Exception("some failure while parsing xml file");
		}
	}
	
	
	private void preprocess(Evaluation evaluation) {
		// notify users
		// ... TODO
		
	}
	
}
