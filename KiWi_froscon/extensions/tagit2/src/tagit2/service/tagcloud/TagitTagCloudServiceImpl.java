package tagit2.service.tagcloud;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.solr.client.solrj.response.FacetField.Count;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;

import tagit2.api.tagcloud.TagitTagCloudService;
import tagit2.util.tagcloud.TagCloudItem;

//@Transactional
@Name("tagit2.tagCloudService")
@Stateless
@AutoCreate
public class TagitTagCloudServiceImpl implements TagitTagCloudService {

	private static final int maxFontSize = 6;
	private static final int minFontSize = 1;
	
	private static final String fontClass = "tagCloudItem";
	
	public List<TagCloudItem> getTagCloud(List<Count> facets) {
		List<TagCloudItem> cloud = new LinkedList<TagCloudItem>();
		
		//a logarithmic tag cloud
		long treshold = 1;
		long maxCount = 5000;
		long minCount = Long.MAX_VALUE;
		long tresholds = maxFontSize-minFontSize;
		
		for( Count f : facets ) {
			if( f.getCount() < minCount )
				minCount = f.getCount();
			if( f.getCount() > maxCount )
				maxCount = f.getCount();
		}
		
		//seen at http://blogoforum.com/tag/blogoforum+math+suggestion+tagcloud+tagging/tag-cloud-makeup-continued-3294.html
		
		for( Count f : facets ) {
			double a = tresholds*Math.log(f.getCount() - minCount + 2) / Math.log(maxCount - minCount + 2) - 1;
			int fontSize = Math.round( minFontSize + Math.round( a ) * treshold ) + 1;
			cloud.add( new TagCloudItem(f.getName(), fontClass+fontSize, f.getCount()) );
		}
		
		return cloud;
	}

}
