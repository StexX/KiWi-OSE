package interedu.service.recommend;

import interedu.api.dataimport.InterEduArtikelFacade;
import interedu.api.recommend.SimpleRecommenderService;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.tagging.TaggingService;
import kiwi.model.content.ContentItem;
import kiwi.model.tagging.Tag;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Stateless
@Scope(ScopeType.STATELESS)
@Name("interedu.simpleRecommendationService")
public class SimpleRecommenderServiceImpl implements SimpleRecommenderService {

	@Logger
	private Log log;
	
	@In
	private SolrService solrService;
	
	@In
	private TaggingService taggingService;
	
    @In
    private KiWiEntityManager kiwiEntityManager;
	
	@Override
	public List<InterEduArtikelFacade> getRecommenations( InterEduArtikelFacade article) {
		List<InterEduArtikelFacade> result = new LinkedList<InterEduArtikelFacade>();
		
		//all tags
		List<SimpleRecommendation> l1 = new LinkedList<SimpleRecommendation>();
		for( Tag tag : taggingService.getTags(article.getDelegate()) ) {
			KiWiSearchCriteria c1 = solrService.parseSearchString("tag:"+tag.getTaggingResource().getTitle()+" type:interedu:Artikel");
			for( SearchResult r : solrService.search( c1 ).getResults() ) {
				if( r.getItem().getId() != article.getId()  ) {
					SimpleRecommendation t = new SimpleRecommendation(r.getItem(),1);
					if( !l1.contains(t) ) {
						l1.add( t );
						log.info("add article #0 by tag",r.getItem().getTitle());
					} else {
						l1.remove(t);
						t.nb = t.nb+1;
						l1.add(t);
						log.info("lift article #0 by tag",r.getItem().getTitle());
					}
				}
			}
		}
		
		//all categories
		for( Long catId : article.getCategoryIds() ) {
			KiWiSearchCriteria c2 = solrService.parseSearchString("interedu:categoryid:"+catId+"  type:interedu:Artikel");
			for( SearchResult r : solrService.search( c2 ).getResults() ) {
				if( r.getItem().getId() != article.getId()  ) {
					SimpleRecommendation t = new SimpleRecommendation(r.getItem(),1);
					if( !l1.contains(t) ) {
						l1.add( t );
						log.info("add article #0 by cat",r.getItem().getTitle());
					} else {
						l1.remove(t);
						t.nb = t.nb+1;
						l1.add(t);
						log.info("lift article #0 by cat",r.getItem().getTitle());
					}
				}
			}
		}

		Collections.sort(l1);
		
		for( int i = 0; i < l1.size(); i++ ) {
			if( i < 15 ) {
				result.add( kiwiEntityManager.createFacade(l1.get(i).article, InterEduArtikelFacade.class) );
			} else break;
		}
		
		return result;
	}
	
	class SimpleRecommendation implements Comparable<SimpleRecommendation>{
		int nb;
		ContentItem article;
		public SimpleRecommendation(ContentItem ci,int nb) {
			this.article = ci;
			this.nb = nb;
		}
		public boolean equals( Object o ) {
			try {
				return ((SimpleRecommendation)o).article.getId() == article.getId();
			} catch(Exception e) {
				return false;
			}
		}
		@Override
		public int compareTo(SimpleRecommendation o) {
			return Integer.valueOf(o.nb).compareTo(this.nb);
		}

	}

	@Override
	public List<String> getArticlesBeginsWidth(String start) {
		//replace space
		//TODO
		//start = start.replace(" ", "%22");
		
		LinkedList<String> recs = new LinkedList<String>();
		
		KiWiSearchCriteria c = new KiWiSearchCriteria();
		c.setSolrSearchString("type:\"uri::http://interedu.salzburgresearch.at/Artikel\" title:"+start+"*");
		c.setLimit(-1);
		for( SearchResult r : solrService.search( c ).getResults() ) {
			log.info("result: #0", r.getItem().getTitle());
			recs.add(r.getItem().getTitle());
		}
		return recs;
	}

}
