/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.equity.EquityService;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

import artaround.action.artwork.ArtWorkBean;
import artaround.action.artwork.MediaTmp;
import artaround.action.utils.LastSubmittedComparator;
import artaround.datamodel.artwork.ArtWorkFacade;
import artaround.datamodel.artwork.MultimediaFacade;

/**
 * @author Rolf Sint
 * @version 0.7
 * @since 0.7
 *
 */
@Scope(ScopeType.STATELESS)
@Name("artWorkService")
//@Transactional
@AutoCreate
public class ArtWorkService {
	
	@In
	private TripleStore tripleStore;
	
	@In
    private KiWiEntityManager kiwiEntityManager;
	
	
	@In
	private SolrService solrService;
	
	@In(required = false)
	private Comparator<ArtWorkFacade> comp;
	
    @In(create = true)
    private User currentUser;
    
	@In
	private ContentItemService contentItemService;
		

    @Logger
    private Log log;
	
	public List<ArtWorkFacade> getAllArtWorks(){
		KiWiSearchCriteria criteria = new KiWiSearchCriteria();
		criteria.getTypes().add(tripleStore.createUriResource(Constants.ART_AROUND_CORE + "ArtWork").getKiwiIdentifier());
		criteria.setLimit(Integer.MAX_VALUE);
		List <ArtWorkFacade> allArtWorks = getArtWoksWithCriteria(criteria);		
		return allArtWorks;
	}
	
	/**
	 * This class <b>returns all Artworks</b>
	 * @param no parameters
	 * @return returns all ArtWors
	 */
	public List<ArtWorkFacade> getArtWorks(){
		KiWiSearchCriteria criteria = new KiWiSearchCriteria();
		criteria.getTypes().add(tripleStore.createUriResource(Constants.ART_AROUND_CORE + "ArtWork").getKiwiIdentifier());
		criteria.setPerson(currentUser.getLogin());
		criteria.setLimit(Integer.MAX_VALUE);
		List <ArtWorkFacade> artWorks = getArtWoksWithCriteria(criteria);	
		return artWorks;
	}
	
	private List<ArtWorkFacade> getArtWoksWithCriteria(KiWiSearchCriteria criteria){
		List <ArtWorkFacade> allArtWorks = new LinkedList<ArtWorkFacade>();
		KiWiSearchResults results = solrService.search(criteria);
		log.info("Number of artworks: "+results.getResultCount());
		
		for(SearchResult r : results.getResults()) {
			if(r.getItem() != null) {
				ArtWorkFacade i = kiwiEntityManager.createFacade(r.getItem(),
						ArtWorkFacade.class);
				
				log.info("TimeStamp: #0", i.getCreated()+" "+i.getModified());
				allArtWorks.add(i);		
			}
		}
		
		log.info(comp == null?"comp is null":"comp is not null");
		if(comp == null){
			comp = new LastSubmittedComparator();
		}
		Collections.sort(allArtWorks,comp);
		return allArtWorks;
	}
	
	
	//updates an existing ArtWork by values hold in a given ArtWorkBean
	public void updateArtWork(ArtWorkFacade artWorkFacade, ArtWorkBean artWorkBean){
						
		Date d = new Date();
		artWorkFacade.setModified(d);

		createArtWork(artWorkBean, artWorkFacade);				
	}
	
	//creates a new ArtWork by values hold in a given ArtWorkBean
	public ArtWorkFacade createArtWork(ArtWorkBean artWorkBean){		
		final ContentItem artWorkItem = contentItemService.createContentItem();
		ArtWorkFacade artWork = kiwiEntityManager.createFacade(artWorkItem, ArtWorkFacade.class);		
		return createArtWork(artWorkBean, artWork);		
	}

	/**
	 * @param artWorkBean
	 * @param artWork
	 */
	private ArtWorkFacade createArtWork(ArtWorkBean artWorkBean, ArtWorkFacade artWork) {
		contentItemService.updateTitle(artWork, artWorkBean.getArtWorkName());
		artWork.setAuthor(currentUser);
		
		artWork.setDescription(artWorkBean.getDescription());

		artWork.setPublicAccess(artWorkBean.getPublicAccess());

		artWork.setPrice(artWorkBean.getPrice());
		artWork.setCurrency(artWorkBean.getCurrency());
		artWork.setSellingState(artWorkBean.getSellingState());
		
		artWork.setArtWorkTextList(artWorkBean.getArtWorkTextList());		
		artWork.setSellingState(artWorkBean.getSellingState());
		artWork.setAuthorName(artWorkBean.getAuthorName());
		artWork.setDescription(artWorkBean.getDescription());
		artWork.setPrice(artWorkBean.getPrice());
		artWork.setCurrency(artWorkBean.getCurrency());		
		
		
//		artWork.setCustomerFirstName(artWorkBean.getCustomerFirstName());
//		artWork.setCustomerLastName(artWorkBean.getCustomerLastName());
//		artWork.setCustomerStreet(artWorkBean.getCustomerStreet());
//		artWork.setCustomerZip(artWorkBean.getCustomerZip());
//		artWork.setCustomerCity(artWorkBean.getCustomerCity());
//		artWork.setCustomerState(artWorkBean.getCustomerState());
//		artWork.setCustomerEmail(artWorkBean.getCustomerEmail());
//		artWork.setCustomerPhone(artWorkBean.getCustomerPhone());
//		artWork.setCustomerIsMember(artWorkBean.getCustomerIsMember());
//		artWork.setSellingDate(artWorkBean.getSellingDate());
		
		artWork.setLatitude(artWorkBean.getLatitude());
		artWork.setLongitude(artWorkBean.getLongitude());
		
		//TODO Store store Multimedia, Tags and Technique
		
		LinkedList<MediaTmp> mTmp =  artWorkBean.getArtAroundMediaList();
		LinkedList<MultimediaFacade> aml = new LinkedList<MultimediaFacade>();
		if(mTmp != null){
			for (MediaTmp mediaTmp : mTmp) {
				//create all contentItems according to the values of mTmp
				final ContentItem artaroundMediaTmp = contentItemService.createContentItem();
				MultimediaFacade artaroundMedia = kiwiEntityManager.createFacade(artaroundMediaTmp, MultimediaFacade.class);
				artaroundMedia.setFilename(mediaTmp.getFileName());
				artaroundMedia.setMimeType(mediaTmp.getMimeType());

				aml.add(artaroundMedia);

			}
			//Add MultimediaFacase to ArtWorkFacade
			artWork.setArtAroundMediaList(aml);
		}
		
		kiwiEntityManager.persist(artWork);	
		return artWork;
	}
	
	
	public void deleteMultimediaFromArtWork(ArtWorkFacade artWork, MediaTmp pic){
		LinkedList<MultimediaFacade> mf = artWork.getArtAroundMediaList();
		for (MultimediaFacade multimediaFacade : mf) {
			if(multimediaFacade.getFilename().equals(pic.getFileName())){
				mf.remove(multimediaFacade);
				artWork.setArtAroundMediaList(mf);	
				kiwiEntityManager.remove(multimediaFacade.getDelegate());
				return;
			}
		}				
	}
	
//	public List<ArtWorkFacade> getAllArtWorks(){
//		
//		//KiWiUriResource kur = (KiWiUriResource) currentUser.getResource();
//		//log.info("The currentuser has the uri: #0", kur.getUri());
//		//SpparQL Query: Fragt den triplestore nach allen Kunstwerken des eingeloggten users ab, ist aber sehr langsam im Vergleich zu HQL querries auf der Datenbank oder sogar Solr Queries im Volltexindex (um ein vielfaches schneller)
//		//javax.persistence.Query q = kiwiEntityManager.createQuery("SELECT ?artwork WHERE { ?artwork rdf:type <"+Constants.ART_AROUND_CORE+"ArtWork>. ?artwork kiwi:author <"+kur.getUri()+">}",KiWiQueryLanguage.SPARQL,ArtWorkFacade.class);
//		List <ArtWorkFacade> allArtWorks = new LinkedList<ArtWorkFacade>();
//		//type:"uri::http://www.artaround.at/ArtWork" person:t Suche nach allen Kunstwerken einer bestimmten Person
//	//	String str = "type:\"uri::"+ Constants.ART_AROUND_CORE + "ArtWork\"  person:" + currentUser.getLogin();
//	//	log.info(str);
//		KiWiSearchCriteria criteria = new KiWiSearchCriteria();
//		criteria.getTypes().add(tripleStore.createUriResource(Constants.ART_AROUND_CORE + "ArtWork").getKiwiIdentifier());
//		criteria.setLimit(Integer.MAX_VALUE);
//	//	criteria.setPerson(currentUser.getLogin());
//	//	criteria = solrService.parseSearchString(str);	
//		KiWiSearchResults results = solrService.search(criteria);
//		log.info("Number of artworks: "+results.getResultCount());
//		
//		for(SearchResult r : results.getResults()) {
//			if(r.getItem() != null) {
//				ArtWorkFacade i = kiwiEntityManager.createFacade(r.getItem(),
//						ArtWorkFacade.class);
//				
//				log.info("TimeStamp: #0", i.getCreated()+" "+i.getModified());
//				allArtWorks.add(i);		
//			}
//		}
//		
//		log.info(comp == null?"comp is null":"comp is not null");
//		if(comp == null){
//			comp = new LastSubmittedComparator();
//		}
//		Collections.sort(allArtWorks,comp);
//		
//		return allArtWorks;
//	}
	
	
}
