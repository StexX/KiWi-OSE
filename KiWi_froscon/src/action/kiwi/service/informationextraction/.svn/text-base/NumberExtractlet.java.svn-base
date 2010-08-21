package kiwi.service.informationextraction;

import gate.Annotation;
import gate.Document;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.informationextraction.ClassifierEntity;
import kiwi.model.informationextraction.Context;
import kiwi.model.informationextraction.Example;
import kiwi.model.informationextraction.InstanceEntity;
import kiwi.model.informationextraction.Suggestion;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.tagging.Tag;
import kiwi.model.user.User;
import kiwi.util.KiWiXomUtils;
import kiwi.util.KiWiXomUtils.NodePos;
import kiwi.util.KiWiXomUtils.NodePosIterator;

import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

@Name("kiwi.informationextraction.numberExtractlet")
@Scope(ScopeType.STATELESS)
//@Transactional
public class NumberExtractlet extends AbstractMLExtractlet {

	@Logger
	private Log log;
	
	@In
	TripleStore tripleStore;
	
	public NumberExtractlet() {
		super("kiwi.informationextraction.numberExtractlet");
	}
	
	@Override
	public Suggestion generateSuggestion(InstanceEntity entity, ClassifierEntity classifier) {
		Suggestion ret = super.generateSuggestion(entity, classifier);
		ret.setKind(Suggestion.DATATYPE);
		ret.getResources().add(classifier.getResource());
		ret.getTypes().add(tripleStore.getXSDType(Integer.class));
		ret.setLabel(entity.getValue());
		
		return ret;
	}
	
	@Override
	public void initClassifier(ClassifierEntity classifier) {
		
		EntityManager entityManager = (EntityManager)Component.getInstance("entityManager");
		Query q;
		
		KiWiResource classifierResource = (KiWiResource)classifier.getResource();
		
		if (!classifierResource.isUriResource()) {
			log.info("classifier resource #0 is not URI!", classifierResource.getKiwiIdentifier());
			return;
		}
		
		Set<String> potentialKiwiids = new HashSet<String> ();
		for (KiWiTriple triple : tripleStore.getTriplesByP((KiWiUriResource)classifierResource)) {
			potentialKiwiids.add(triple.getSubject().getKiwiIdentifier());
			
			log.info("adding #0 to list of potentialy interesting kiwiids", triple.getSubject().getKiwiIdentifier());
		}
		
		//classifierResource.lt
		
		// Get the list of text contentids of content items positively tagged with the tag.
		/*Query q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listTaggedItemsByTaggingItem");
    	q.setParameter("id", taggingResourceContentItemId);
    		
    	Set<Long> textContentIds = new HashSet<Long>();
    	
		Collection<ContentItem> taggedes = q.getResultList();
			
		for (ContentItem tagged : taggedes) {
			if (tagged.getTextContent() != null) {
				textContentIds.add(tagged.getTextContent().getId());
			}
		}*/
		
		q = entityManager.createNamedQuery("kiwi.informationextraction.informationExtractionService.listInstancesByExtractletName");
		q.setParameter("name", name);
			
		Collection<InstanceEntity> instances = q.getResultList();
	allInstances:
		for (InstanceEntity instance : instances) {
			
			// skip old textcontents.
			if (!instance.getSourceTextContent().getId().equals(instance.getSourceResource().getContentItem().getTextContent().getId())) {
				continue;
			}
			
			Suggestion suggestion = new Suggestion();
			suggestion.setKind(Suggestion.DATATYPE);
			suggestion.getResources().add(classifierResource);
			suggestion.getTypes().add(tripleStore.getXSDType(Integer.class));
			suggestion.setLabel(instance.getValue());
			suggestion.setExtractletName(name);
			suggestion.setInstance(instance);
			suggestion.setClassifier(classifier);
			// initial score to zero.
			suggestion.setScore(0);
			
			entityManager.persist(suggestion);
			
			// log.info("created suggestion #0", suggestion.getId());
				
			if (instance.getSourceTextContent() != null) {

				if (!potentialKiwiids.contains(instance.getSourceResource().getKiwiIdentifier())) {
					// only pages that has the property as a value are potential positive examples...
					continue;
				}
				
				//log.info("potentialy interesting instance: #0", instance.getId());
				
				/*NodePos npBegin = KiWiXomUtils.getNode(instance.getSourceTextContent().getXmlDocument(), 0, instance.getContext().getInBegin());
				NodePos npEnd = KiWiXomUtils.getNode(instance.getSourceTextContent().getXmlDocument(), 0, instance.getContext().getInEnd());
				*/
				
				KiWiXomUtils.NodePosIterator npiter = new KiWiXomUtils.NodePosIterator(instance.getSourceTextContent().getXmlDocument());
				while(npiter.hasNext()) {
					NodePos np = npiter.next();
					
					if (np.getPos() == instance.getContext().getInBegin()) {
						
						if (np.getNode() instanceof Element) {
														
							Element e = (Element)np.getNode();
							if (e.getAttribute("property") != null) {
								log.info("found element with property");
								String propertyAttr = e.getAttributeValue("property");
								String[] properties = propertyAttr.trim().split(" ");
								for (String property : properties) {
									// log.info("comparing property")
									if (classifierResource.getKiwiIdentifier().equals(property)) {
										
										Example example = new Example();
										example.setSuggestion(suggestion);
										example.setType(Example.POSITIVE);
										example.setUser(instance.getSourceResource().getContentItem().getAuthor());
										
										entityManager.persist(example);
										
										//log.info("created example #0", example.getId());
										
										//log.info("np.getPos = #0, ctx inBegin: #1", np.getPos(), instance.getContext().getInBegin());
										
										continue allInstances;
									}
								}
							}
						}
					}
				}
				
				//TaggingService taggingService = (TaggingService)Component.getInstance("taggingService");
				
				/*User user = null;
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
				
				entityManager.persist(example);*/
				
				
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
		
		for (Annotation token : tokens) {
			gate.FeatureMap f = token.getFeatures();
			String category = (String)f.get("category");
			String string = (String)f.get("string");
			
			//log.info("string #0, cat: #1 ", string, category);
			
			if ("CD".equals(category)) {
				string = string.replaceAll(",", "");
				
				try{					
					InstanceEntity inst = new InstanceEntity();
					inst.setSourceResource(context);
					inst.setSourceTextContent(content);
					inst.setExtractletName(name);
					
					//we expect the exception...
					inst.setValue(Integer.toString(Integer.parseInt(string)));
					
					Context ctx = generateBlockContext(content.getXmlDocument(), 
							token.getStartNode().getOffset().intValue(),
							token.getEndNode().getOffset().intValue());
					
					inst.setContext(ctx);
					
					// TODO: modify the method to generate the feature set directly instead of using string builder
					String featstr = generateContextFeatures(gateDoc, token, ctx);
					List<String> features = new LinkedList<String>();
					for (String ff : featstr.split(" ")) {
						features.add(ff);
					}
					inst.setFeatures(features);
					
					//log.info("creating numberextractlet instance #0", inst.getContext().toString());
					
					ret.add(inst);
				}
				catch(NumberFormatException x) {
					//log.info("Could not parse format #0", string);
				}
			}
			
			//features.add(stem);
		}
		
		/*
		InstanceEntity inst = new InstanceEntity();
		inst.setSourceResource(context);
		inst.setSourceTextContent(content);
		inst.setContext(new Context());
		inst.setExtractletName(name);
			
		inst.setFeatures(features);
			
		ret.add(inst);*/
		
		return ret;
	}

private void generateFeaturesFromGateTokens (List<gate.Annotation> tokens, String prefix, StringBuilder sb) {
		
		Set<String> nouns = new HashSet<String> ();
		nouns.add("NN");
		nouns.add("NNP");
		nouns.add("NNS");
		nouns.add("NNPS");
		
		int i = 0;
		for (Annotation token : tokens) {
			gate.FeatureMap f = token.getFeatures();
			String stem = (String)f.get("stem");
			String category = (String)f.get("category");
			String orth = (String)f.get("orth");
			
			// with position 
			if (i < 4) {
				sb.append(' ');
				sb.append(prefix);
				sb.append(i);
				sb.append('s');
				sb.append(stem);
		
				sb.append(' ');
				sb.append(prefix);
				sb.append(i);
				sb.append('c');
				sb.append(category);
			
				sb.append(' ');
				sb.append(prefix);
				sb.append(i);
				sb.append('o');
				sb.append(orth);
			}
			
			// without position
			if (nouns.contains(category)) {
				sb.append(' ');
				sb.append(prefix);
				sb.append('s');
				sb.append(stem);
			}
			
			++i;
			
			if (i > 10) break;
		}
	}
	
	/**
	 * Generate a context for a fragment specified by text coordinates.
	 * The context will be based on the the smallest common XML subtree.
	 * @param doc
	 * @param begin
	 * @return
	 */
	private Context generateBlockContext(nu.xom.Document doc, int begin, int end) {
		
		NodePos npBegin = KiWiXomUtils.getNode(doc, 0, begin);
		NodePos npEnd = KiWiXomUtils.getNode(doc, 0, end);
			
		Node parent = KiWiXomUtils.getLeastCommonParent(npBegin.getNode(), npEnd.getNode());
		
		Node contextBegin = KiWiXomUtils.getFirstTextNode(parent);
		Node contextEnd = KiWiXomUtils.getLastTextNode(parent);
		
		int contextBeginPos = -1;
		int contextEndPos = -1;
		
		NodePosIterator npi = new NodePosIterator(doc);
		while(npi.hasNext()) {
			NodePos np = npi.next();
			
			if (np.getNode().equals(contextBegin)) {
				contextBeginPos = np.getPos();
			}
			if (np.getNode().equals(contextEnd)) {
				if (contextEnd instanceof Text) {
					contextEndPos = np.getPos() + ((Text)contextEnd).getValue().length();
				}
			}
		}
		
		String plain = KiWiXomUtils.xom2plain(doc);
		if (contextBeginPos < 0) contextBeginPos = 0;
		if (contextEndPos < 0 || contextEndPos > plain.length()) contextEndPos = plain.length();
		
		Context ret = new Context();
		ret.setLeftContext(plain.substring(contextBeginPos, begin));
		ret.setInContext(plain.substring(begin, end));
		ret.setRightContext(plain.substring(end, contextEndPos));
		ret.setContextBegin(contextBeginPos);
		ret.setContextEnd(contextEndPos);
		ret.setInBegin(begin);
		ret.setInEnd(end);
		ret.setIsFragment(true);
		
		return ret;
	}
	

	private String generateContextFeatures(gate.Document doc, Annotation contextAnnotation, Context ctx) {

		gate.AnnotationSet tokens = doc.getAnnotations().get("Token");
		gate.AnnotationSet preTokensAS = null;
		gate.AnnotationSet postTokensAS = null;
		gate.AnnotationSet inTokensAS = null;
			
		preTokensAS = tokens.getContained((long)ctx.getContextBegin(), contextAnnotation.getStartNode().getOffset());
		postTokensAS = tokens.getContained(contextAnnotation.getEndNode().getOffset(), (long)ctx.getContextEnd());
		inTokensAS = tokens.getContained(contextAnnotation.getStartNode().getOffset(), contextAnnotation.getEndNode().getOffset());

		LinkedList<Annotation> preTokens = new LinkedList<Annotation> ();
		LinkedList<Annotation> postTokens = new LinkedList<Annotation> ();
		LinkedList<Annotation> inTokens = new LinkedList<Annotation> ();
		
		preTokens.addAll(preTokensAS);
		postTokens.addAll(postTokensAS);
		inTokens.addAll(inTokensAS);
		
		Comparator<Annotation> annotationComparator =  new Comparator<Annotation>() {
			@Override
			public int compare(Annotation o1, Annotation o2) {
				return o1.getStartNode().getOffset().compareTo(o2.getStartNode().getOffset());
		}};
		
		Comparator<Annotation> annotationComparatorReverse =  new Comparator<Annotation>() {
			@Override
			public int compare(Annotation o1, Annotation o2) {
				return o2.getStartNode().getOffset().compareTo(o1.getStartNode().getOffset());
		}};
			
		Collections.sort(preTokens, annotationComparatorReverse);
		Collections.sort(postTokens, annotationComparator);
		Collections.sort(inTokens, annotationComparator);
		
		StringBuilder sb = new StringBuilder();
		generateFeaturesFromGateTokens (preTokens, "-", sb);
		generateFeaturesFromGateTokens (postTokens, "+", sb);
		generateFeaturesFromGateTokens (inTokens, "", sb);
				
		return sb.toString();
	}
}
