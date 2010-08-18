package kiwi.service.informationextraction;

import gate.Annotation;
import gate.Document;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.tagging.TaggingService;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.ClassifierEntity;
import kiwi.model.informationextraction.Context;
import kiwi.model.informationextraction.Example;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

@Name("kiwi.informationextraction.documentTagExtractlet")
@Scope(ScopeType.STATELESS)
//@Transactional
public class DocumentTagExtractlet extends AbstractMLExtractlet {

	@Logger
	private Log log;
	
	public DocumentTagExtractlet() {
		super("kiwi.informationextraction.documentTagExtractlet");
	}
	
	@Override
	public Suggestion generateSuggestion(InstanceEntity entity, ClassifierEntity classifier) {
		Suggestion ret = super.generateSuggestion(entity, classifier);
		ret.setKind(Suggestion.TAG);
		ret.getResources().add(classifier.getResource());
		ret.setLabel(classifier.getResource().getContentItem().getTitle());
		
		return ret;
	}
	
	@Override
	public void initClassifier(ClassifierEntity classifier) {
		
		EntityManager entityManager = (EntityManager)Component.getInstance("entityManager");
		
		Long taggingResourceContentItemId = classifier.getResource().getContentItem().getId();
		
		// Get the list of text contentids of content items positively tagged with the tag.
		Query q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listTaggedItemsByTaggingItem");
    	q.setParameter("id", taggingResourceContentItemId);
    		
    	Set<Long> textContentIds = new HashSet<Long>();
    	
		Collection<ContentItem> taggedes = q.getResultList();
			
		for (ContentItem tagged : taggedes) {
			if (tagged.getTextContent() != null) {
				textContentIds.add(tagged.getTextContent().getId());
			}
		}
		
		q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listInstancesByExtractletName");
		q.setParameter("name", name);
			
		Collection<InstanceEntity> instances = q.getResultList();
		for (InstanceEntity instance : instances) {
			
			Suggestion suggestion = new Suggestion();
			suggestion.setKind(Suggestion.TAG);
			suggestion.getResources().add(classifier.getResource());
			suggestion.setLabel(classifier.getResource().getContentItem().getTitle());
			suggestion.setExtractletName(name);
			suggestion.setInstance(instance);
			suggestion.setClassifier(classifier);
			// initial score to zero.
			suggestion.setScore(0);
			
			entityManager.persist(suggestion);
			
			log.info("created suggestion #0", suggestion.getId());
				
			if (instance.getSourceTextContent() != null && textContentIds.contains(instance.getSourceTextContent().getId())) {
				Example example = new Example();
				example.setSuggestion(suggestion);
				example.setType(Example.POSITIVE);
				
				TaggingService taggingService = (TaggingService)Component.getInstance("taggingService");
				
				User user = null;
				List<Tag> tags = taggingService.getTags(instance.getSourceResource().getContentItem());
				for (Tag tag : tags) {
					if (tag.getTaggingResource().getId().equals(taggingResourceContentItemId)) {
						user = tag.getTaggedBy();
					}
				}
				
				if (user == null) {
					log.warn("No author found for tagging for instance #0", instance.getId());
				}
				
				example.setUser(user);
				
				entityManager.persist(example);
				
				log.info("created example #0", example.getId());
			}
			else {
				// do nothing.
			}
		}
	}

	@Override
	public Collection<InstanceEntity> extractInstances(KiWiResource context,
			TextContent content, Document gateDoc, Locale language) {
Collection<InstanceEntity> ret = new LinkedList<InstanceEntity> ();
		
		if (content == null) {
			return ret;
		}
		
		gate.AnnotationSet tokens = gateDoc.getAnnotations().get("Token");
		
		Collection<String> features = new LinkedList<String>();
		for (Annotation token : tokens) {
			gate.FeatureMap f = token.getFeatures();
			String stem = (String)f.get("stem");
			
			features.add(stem);
		}
		
		InstanceEntity inst = new InstanceEntity();
		inst.setSourceResource(context);
		inst.setSourceTextContent(content);
		inst.setContext(new Context());
		inst.setExtractletName(name);
			
		inst.setFeatures(features);
			
		ret.add(inst);
		
		return ret;
	}

}
