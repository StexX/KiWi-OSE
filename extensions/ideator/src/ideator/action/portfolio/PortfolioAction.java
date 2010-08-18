package ideator.action.portfolio;

import ideator.action.evaluation.Criteria;
import ideator.action.evaluation.Evaluation;
import ideator.action.evaluation.EvaluationResource;
import ideator.action.evaluation.Paragraph;
import ideator.action.evaluation.XMLEvaluationService;
import ideator.service.IdeatorCollectorService;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Name("ideator.portfolio")
@Scope(ScopeType.PAGE)
public class PortfolioAction {
	
	private EvaluationResource currentResource;
	
	private HashMap<Long,String> colors;
	
	private Axis xaxis,yaxis,zaxis;
	
	private HashMap<String,Axis> axesset;
	private List<Axis> axes;
	
	private static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	
	@Logger
	private Log log;
	
	@In(value="ideator.collector")
	private IdeatorCollectorService collector;
	
	@In(value="ideator.xmlEvaluationService",create=true)
	private XMLEvaluationService evalService;
	
	private List<EvaluationResource> evaluations;
	
	@Create
	public void init() {
		//get all evaluation
		evaluations = evalService.getEvaluationResources( collector.getArticleCollection() );
		log.info("Collection has #0 evaluations", evaluations.size());
		//set axes
		setAxes();
		colors = new HashMap<Long,String>();
	}
	
	public List<PortfolioValue> getValues() {
		log.info("get #0 values for xAxis #1, yAxis #2, zAxis #3",evaluations.size(),xaxis,yaxis,zaxis);
		//check if axis are selected
		if( xaxis == null || yaxis == null || zaxis == null ) return null;
		
		List<PortfolioValue> list = new LinkedList<PortfolioValue>();
		//set values
		for( EvaluationResource eval : evaluations ) {
			list.add(getValue(eval));
		}
		return list;
	}
	
	private void setAxes() {
		axesset = new HashMap<String,Axis>();
		for( EvaluationResource e : evaluations ) {
			for( Paragraph p : e.getEvaluation().getParagraphs() ) {
				if( !axesset.containsKey(p.getTitle()) ) axesset.put(p.getTitle(),new Axis(p.getTitle()));
			}
		}
		
		axes = new LinkedList<Axis>();
		Iterator<String> it = axesset.keySet().iterator();
		while( it.hasNext() ) {
			//TODO max/min from Axis in the whole system
			axes.add(axesset.get(it.next()));
		}
		
		switch ( axes.size() ) {
		case 0: xaxis = null;yaxis = null;zaxis = null;break;
		case 1: xaxis = axes.get(0);yaxis = axes.get(0);zaxis = axes.get(0);break;
		case 2: xaxis = axes.get(0);yaxis = axes.get(1);zaxis = axes.get(1);break;
		default: xaxis = axes.get(0);yaxis = axes.get(1);zaxis = axes.get(2);
		}
	}
	
	private PortfolioValue getValue( EvaluationResource eval ){//Evaluation evaluation) {
		
		long[][] data = {{getData(xaxis,eval.getEvaluation()),getData(yaxis,eval.getEvaluation()),getData(zaxis,eval.getEvaluation())}};
		
		String color;
		Long id = eval.getIdea().getId();
		if( colors.containsKey(id) ) {
			color = colors.get(id);
		} else {
			color = getColor();
			colors.put(id, color);
		}
		
		return new PortfolioValue(color,
									eval.getIdea().getTitle(),
									id,
									true,
									data,
									eval.getIdea().getAuthor().getLogin(),
									df.format(eval.getIdea().getCreated()));
	}
	
	private long getData(Axis axis, Evaluation evaluation) {
		for( Paragraph p : evaluation.getParagraphs() ) {
			if( p.getTitle().equals(axis.getTitle()) ) {
				//TODO normalize Values
				long v = getPValue(p);
				return v;
			}
		}
		return 0;
	}
	
	private long getPValue( Paragraph p ) {
		double value = 0;
		for( Criteria c : p.getCriteria() ) {
			if( c.getSelected() > -1 ) {
				value += c.getRatings().get(c.getSelected()).getValue();
			}
		}
		
		return Math.round(value/p.getCriteria().size());
	}
	
	private String getColor() {
		String s = null;
		Random numGen = new Random();
		s = "#";
		for( int i = 0; i < 3; i++ ) {
			String x = Integer.toHexString(numGen.nextInt(256));
			if( x.length() == 1 ) s += "0" + x;
			else s += x;
		}
		return s;
	}
	
	public List<Axis> getAxes() {
		return axes;
	}
	
	public void setAxes(List<Axis> axes) {
		this.axes = axes;
	}

	public void setZaxis(String zaxis) {//TODO fix
		this.zaxis = axesset.get(zaxis);
	}

	public String getZaxis() {
		return zaxis.getTitle();
	}

	public void setXaxis(String xaxis) {
		this.xaxis = axesset.get(xaxis);
	}

	public String getXaxis() {
		return xaxis.getTitle();
	}

	public void setYaxis(String yaxis) {
		this.yaxis = axesset.get(yaxis);
	}

	public String getYaxis() {
		return yaxis.getTitle();
	}
	
	public void setCurrentResource(long id) {
		if( id == -1 ) currentResource = null;
		else {
			//TODO
		}
	}
	
}
