package tagit2.api.tagcloud;

import java.util.List;

import org.apache.solr.client.solrj.response.FacetField.Count;

import tagit2.util.tagcloud.TagCloudItem;

public interface TagitTagCloudService extends TagitTagCloudServiceLocal, TagitTagCloudServiceRemote {
	
	public List<TagCloudItem> getTagCloud( List<Count> facets );
	
}
