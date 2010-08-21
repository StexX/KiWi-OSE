package kiwi.service.informationextraction;

import gate.Document;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;

import kiwi.api.informationextraction.Extractlet;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.ClassifierEntity;
import kiwi.model.informationextraction.Context;
import kiwi.model.informationextraction.Example;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.user.User;

public abstract class AbstractExtractlet implements Extractlet {
	protected String name;
	
	@In
	EntityManager entityManager;
	
	public AbstractExtractlet(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
//	@Transactional(TransactionPropagationType.REQUIRED)
	public void accept(Suggestion suggestion, User user) {
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		//if(!entityManager.contains(suggestion)) {
		//suggestion = entityManager.find(Suggestion.class, suggestion.getId());
		
		entityManager.refresh(suggestion);
		
		Example example = new Example();
		example.setSuggestion(suggestion);
		example.setUser(user);
		example.setType(Example.POSITIVE);
		
		
		entityManager.persist(example);
		// em.flush();
	}
	
	@Override
//	@Transactional(TransactionPropagationType.REQUIRED)
	public void reject(Suggestion suggestion, User user) {
		
		entityManager.setFlushMode(FlushModeType.COMMIT);
		
		//if(!entityManager.contains(suggestion)) {
		// suggestion = entityManager.find(Suggestion.class, suggestion.getId());
		// }
		
		entityManager.refresh(suggestion);
		
		Example example = new Example();
		example.setSuggestion(suggestion);
		example.setUser(user);
		example.setType(Example.NEGATIVE);
		
		
		entityManager.persist(example);
		// em.flush();
	}
	
	/**
	 * Returns the examples that matches the specified suggestion (e.g. for listing all the examples with the same context)
	 * @param s
	 * @return
	 */
	protected Collection<Example> getExamples(Suggestion s) {
		
		Collection<Example> ret = new LinkedList<Example>();
		
		EntityManager em = (EntityManager)Component.getInstance("entityManager");
		
		Query q = em.createNamedQuery("kiwi.informationextraction.informationExtractionService.listExamplesByContextHashAndExtractletName");
		q.setParameter("hash", s.getInstance().getContextHash());
		q.setParameter("name", this.name);
		List<Example> examples = (List<Example>)q.getResultList();
		for (Example example : examples) {
			Context suggestionContext = s.getInstance().getContext();
			Context exampleContext = example.getSuggestion().getInstance().getContext();
			
			if (suggestionContext.getInContext() != null) {
				if (!suggestionContext.getInContext().equals(exampleContext.getInContext())) {
					continue;
				}
			}
			else {
				if (exampleContext.getInContext() != null) {
					continue;
				}
			}
			
			if (suggestionContext.getLeftContext() != null) {
				if (!suggestionContext.getLeftContext().equals(exampleContext.getLeftContext())) {
					continue;
				}
			}
			else {
				if (exampleContext.getLeftContext() != null) {
					continue;
				}
			}
			
			if (suggestionContext.getRightContext() != null) {
				if (!suggestionContext.getRightContext().equals(exampleContext.getRightContext())) {
					continue;
				}
			}
			else {
				if (exampleContext.getRightContext() != null) {
					continue;
				}
			}
			
			ret.add(example);
		}
		
		return ret;
	}
	
	@Override
	public Collection<Suggestion> extract(KiWiResource context, TextContent content, Document gateDoc, Locale language) {
		return new LinkedList<Suggestion> ();
	}
	
	
	@Override
	public Collection<Suggestion> extract(Collection<InstanceEntity> instances) {
		return new LinkedList<Suggestion> ();
	}
	
	@Override
	public void initClassifier(ClassifierEntity entity) {
	}
	
	@Override
	public Collection<InstanceEntity> extractInstances(KiWiResource context, TextContent content, Document gateDoc, Locale language) {
		return new LinkedList<InstanceEntity> ();
	}
	
	@Override
	public void updateSuggestions(ClassifierEntity classifier, Collection<Suggestion> suggestions) {
		
	}
	
	@Override
	public Collection<Suggestion> classifyInstances(ClassifierEntity classifier, Collection<InstanceEntity> instances) {
		return null;
	}
	
	@Override
	public void trainClassifier(ClassifierEntity entity) {
		
	}
}
