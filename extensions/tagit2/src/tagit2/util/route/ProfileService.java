package tagit2.util.route;

import java.awt.Color;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.Log;

import tagit2.api.content.route.RouteFacade;
import tagit2.api.content.route.TrackPointFacade;

public class ProfileService {

	public static final String light_blue ="#81BEF7";
	public static final String green="#088A08";
	
	public static final double steps = 10;
	
	public static JFreeChart getProfile( List<TrackPointFacade> tps, RouteFacade route ) {
		try {
			
			XYSeries series = new XYSeries("Tour");
			double highest = -10000;
			double lowest = 10000;
			double dist = 0;
			
			//define interval
			double interval = route.getDistance()/steps;
			for( TrackPointFacade tp : tps ) {
				if( dist <= tp.getDistance()) {
					dist = dist+interval;
					double elev = tp.getAltitude();
					if( elev > highest ) {
						highest = elev;
					}
					if( lowest > elev ) {
						lowest = elev;
					}
					series.add(tp.getDistance(), tp.getAltitude());
				}
			}
			
			series.add(tps.get(tps.size()-1).getDistance(), tps.get(tps.size()-1).getAltitude());
			XYDataset xyDataset = new XYSeriesCollection(series);
			JFreeChart chart = ChartFactory.createXYAreaChart
			          (route.getTitle(), "distance / km", "height / m",
			           xyDataset, PlotOrientation.VERTICAL, false, 
			            true, false);
			//set color
			chart.setBackgroundPaint(Color.lightGray);
			chart.getXYPlot().setBackgroundPaint(Color.decode(light_blue));
			chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.decode(green));

			//limit axis
			chart.getXYPlot().getDomainAxis().setRange(0,tps.get(tps.size()-1).getDistance());
			if( highest - lowest < 560 ) {
				chart.getXYPlot().getRangeAxis().setRange(lowest-20,lowest+580);
			} else {
				chart.getXYPlot().getRangeAxis().setRange(lowest-20,highest+20);
			}
			
			return chart;
		} catch (Exception e) {
			Log.error("something went wrong while creating image");
		}
		return null;
		
	}
	
}
