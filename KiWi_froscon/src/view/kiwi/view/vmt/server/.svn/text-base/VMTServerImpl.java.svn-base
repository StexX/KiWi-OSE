package kiwi.view.vmt.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.ontology.SKOSConcept;
import kiwi.model.ontology.SKOSConceptScheme;
import kiwi.transport.client.TransportSkosConcept;
import kiwi.transport.client.TransportSkosDetails;
import kiwi.transport.client.TransportTag;
import kiwi.transport.client.TransportSkosDetails.TransportLanguageDetails;
import kiwi.view.vmt.client.VMTServer;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.remoting.WebRemote;
//import org.jboss.seam.annotations.Logger;
//import org.jboss.seam.log.Log;

@Name("kiwi.view.vmt.client.VMTServer")
public class VMTServerImpl implements VMTServer {
	@In
	private EntityManager entityManager;
	@In
	private KiWiEntityManager kiwiEntityManager;
	@In
	private TripleStore tripleStore;
	@In(create = true)
	private ContentItemService contentItemService;
	
	//@Logger
	//private Log log;

	@SuppressWarnings("unchecked")
	@WebRemote
	public List<TransportSkosConcept> getChildConcepts(long aParent) {
		if(aParent == 0)
		{
			Query q = entityManager.createNamedQuery("contentItemService.byType");
			q.setParameter("type", tripleStore
				.createUriResource(Constants.NS_SKOS + "ConceptScheme"));
			q.setHint("org.hibernate.cacheable", true);
			List<SKOSConceptScheme> l = kiwiEntityManager.createFacadeList(q.getResultList(), SKOSConceptScheme.class, false);
			List<TransportSkosConcept> res = new ArrayList<TransportSkosConcept>(l.size());
			
			for(SKOSConceptScheme s : l)
				res.add(new TransportSkosConcept(s.getId(), s.getTitle(), s.getTopConcepts().size() != 0, true));
			return res;
		}
		// else
		
		ContentItem r = byId(aParent);
		HashSet<SKOSConcept> l = null;
		
		if(isScheme(r))
		{
			SKOSConceptScheme s = kiwiEntityManager.createFacade(r, SKOSConceptScheme.class);
			l = s.getTopConcepts();
		}
		else // then it must be a concept
		{
			SKOSConcept c = kiwiEntityManager.createFacade(r, SKOSConcept.class);
			l = c.getNarrower();
		}

		List<TransportSkosConcept> res = new ArrayList<TransportSkosConcept>(l.size());
		for(SKOSConcept c : l)
			res.add(new TransportSkosConcept(c.getId(), c.getPreferredLabel(), c.getNarrower().size() != 0, false));
		return res;
	}

	@SuppressWarnings("unchecked")
	@WebRemote
	public List<TransportTag> getFreeTags() {
		Query q = entityManager.createNamedQuery("contentItemService.byType");
		q.setParameter("type", tripleStore
				.createUriResource(Constants.NS_KIWI_CORE + "Tag"));
		q.setHint("org.hibernate.cacheable", true);

		List<ContentItem> cis = q.getResultList();
		List<TransportTag> tags = new ArrayList<TransportTag>(cis.size()/2);
		for(ContentItem ci : cis)
		{
			if(!isConcept(ci))
				tags.add(new TransportTag(ci.getId(), ci.getTitle()));
		}
		return tags;
	}

	private ContentItem byId(long aId)
	{
		Query q = entityManager.createNamedQuery("contentItemService.byId");
		q.setParameter("id", aId);
		q.setHint("org.hibernate.cacheable", true);
		// this may throw a NoResultException which should really not happen
		// as the database ids are unique and may only reference existing
		// records.
		return (ContentItem) q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	private List<KiWiTriple> getOutgoing(KiWiResource aRes, String aProp)
	{
		Query q = entityManager.createNamedQuery("tripleStore.tripleBySP2");
		q.setParameter("subject", aRes);
		q.setParameter("property_uri", aProp);
		return q.getResultList();
	}

	private boolean isScheme(ContentItem aItem)
	{
		return aItem.getResource().hasType(tripleStore.createUriResource(Constants.NS_SKOS + "ConceptScheme"));
	}

	private boolean isConcept(ContentItem aItem)
	{
		return aItem.getResource().hasType(tripleStore.createUriResource(Constants.NS_SKOS + "Concept"));
	}
	
	@WebRemote
	public Boolean convertToConcept(long aTag, long aParent) {
		ContentItem tag = byId(aTag);
		tag.addType(tripleStore.createUriResource(Constants.NS_SKOS + "Concept"));
		KiWiResource childRes = tag.getResource();

		KiWiResource strType = tripleStore.createUriResource(Constants.NS_XSD+"string");
		Locale loc = new Locale("en");
		KiWiLiteral object = tripleStore.createLiteral(tag.getTitle(), loc, strType);
		tripleStore.createTriple(childRes, Constants.NS_SKOS + "prefLabel", object);
		
		ContentItem parent = byId(aParent);
		KiWiResource parentRes = parent.getResource();
		
		if(isScheme(parent))
		{
			tripleStore.createTriple(parentRes, Constants.NS_SKOS + "hasTopConcept", childRes);
			tripleStore.createTriple(childRes, Constants.NS_SKOS + "topConceptOf", parentRes);
		}
		else
		{
			tripleStore.createTriple(parentRes, Constants.NS_SKOS + "narrower", childRes);
			tripleStore.createTriple(childRes, Constants.NS_SKOS + "broader", parentRes);
		}
		return Boolean.TRUE;
	}

	@WebRemote
	public Boolean moveConcept(long aConcept, long aOldParent, long aParent) {
		SKOSConcept c = kiwiEntityManager.createFacade(byId(aConcept), SKOSConcept.class);
		ContentItem newParent = byId(aParent);
		ContentItem oldParent = byId(aOldParent);
		
		KiWiResource parentRes = newParent.getResource();
		KiWiResource oldParentRes = oldParent.getResource();
		KiWiResource childRes = c.getResource();

		if(isScheme(oldParent))
		{
			tripleStore.removeTriple(oldParentRes, Constants.NS_SKOS + "hasTopConcept", childRes);
			tripleStore.removeTriple(childRes, Constants.NS_SKOS + "topConceptOf", oldParentRes);
		}
		else // the old parent is a Concept
		{
			tripleStore.removeTriple(oldParentRes, Constants.NS_SKOS + "narrower", childRes);
			tripleStore.removeTriple(childRes, Constants.NS_SKOS + "broader", oldParentRes);
		}
		
		if(isScheme(newParent))
		{
			tripleStore.createTriple(parentRes, Constants.NS_SKOS + "hasTopConcept", childRes);
			tripleStore.createTriple(childRes, Constants.NS_SKOS + "topConceptOf", parentRes);
		}
		else // the new parent is a Concept
		{
			tripleStore.createTriple(parentRes, Constants.NS_SKOS + "narrower", childRes);
			tripleStore.createTriple(childRes, Constants.NS_SKOS + "broader", parentRes);
		}
		
		return Boolean.TRUE;
	}
	
	@WebRemote
	public Boolean copyConcept(long aConcept, long aParent)
	{
		ContentItem newParent = byId(aParent);
		KiWiResource parentRes = newParent.getResource();
		KiWiResource childRes = byId(aConcept).getResource();
		
		if(isScheme(newParent))
		{
			tripleStore.createTriple(parentRes, Constants.NS_SKOS + "hasTopConcept", childRes);
			tripleStore.createTriple(childRes, Constants.NS_SKOS + "topConceptOf", parentRes);
		}
		else // the new parent is a Concept
		{
			tripleStore.createTriple(parentRes, Constants.NS_SKOS + "narrower", childRes);
			tripleStore.createTriple(childRes, Constants.NS_SKOS + "broader", parentRes);
		}		
		return Boolean.TRUE;
	}

	@WebRemote
	public Boolean setRelated(long aConcept1, long aConcept2) {
		KiWiResource obj1 = byId(aConcept1).getResource();
		KiWiResource obj2 = byId(aConcept2).getResource();
		tripleStore.createTriple(obj1, Constants.NS_SKOS + "related", obj2);
		tripleStore.createTriple(obj2, Constants.NS_SKOS + "related", obj1);

		return Boolean.TRUE;
	}

	@WebRemote
	public Boolean unsetRelated(long aConcept, List<Long> aConcepts) {
		KiWiResource obj1 = byId(aConcept).getResource();
		for(Long id : aConcepts)
		{
			KiWiResource obj2 = byId(id).getResource();
			tripleStore.removeTriple(obj1, Constants.NS_SKOS + "related", obj2);
			tripleStore.removeTriple(obj2, Constants.NS_SKOS + "related", obj1);
		}

		return Boolean.TRUE;
	}

	@SuppressWarnings("unchecked")
	private List<KiWiLiteral> getAllProps(KiWiResource aRes, String aProp)
	{
		Query q = entityManager.createNamedQuery("tripleStore.literal2BySubjectProperty");
		q.setHint("org.hibernate.cacheable", true);
		q.setParameter("subject", aRes);
		q.setParameter("property_uri", aProp);
		return (List<KiWiLiteral>) q.getResultList();
	}
	
	@WebRemote
	public TransportSkosDetails getDetails(long aConcept)
	{
		SKOSConcept c = kiwiEntityManager.createFacade(byId(aConcept), SKOSConcept.class);
		TransportSkosDetails d = new TransportSkosDetails(aConcept, c.getPreferredLabel());
		KiWiResource cr = c.getResource();

		List<KiWiLiteral> pl = getAllProps(cr, Constants.NS_SKOS + "prefLabel");
		for(KiWiLiteral l : pl)
		{
			if(l.getContent().isEmpty())
				continue;
			// whow, how did this happen? a label without a language?
			// Just assume the language as "en"
			if(l.getLanguage() == null)
				l.setLanguage(new Locale("en"));
			String lang = l.getLanguage().getLanguage(); // yeah, funny
			if(!d.languages.containsKey(lang))
				d.languages.put(lang, new TransportLanguageDetails(true));
			d.languages.get(lang).prefLabel = l.getContent();
		}
		pl = getAllProps(cr, Constants.NS_SKOS + "definition");
		for(KiWiLiteral l : pl)
		{
			if(l.getContent().isEmpty())
				continue;
			if(l.getLanguage() == null)
				l.setLanguage(new Locale("en"));
			String lang = l.getLanguage().getLanguage();
			if(!d.languages.containsKey(lang))
				d.languages.put(lang, new TransportLanguageDetails(true));
			d.languages.get(lang).definition = l.getContent();
		}
		pl = getAllProps(cr, Constants.NS_SKOS + "altLabel");
		for(KiWiLiteral l : pl)
		{
			if(l.getContent().isEmpty())
				continue;
			if(l.getLanguage() == null)
				l.setLanguage(new Locale("en"));
			String lang = l.getLanguage().getLanguage();
			if(!d.languages.containsKey(lang))
				d.languages.put(lang, new TransportLanguageDetails(true));
			d.languages.get(lang).altLabels.add(l.getContent());
		}
		pl = getAllProps(cr, Constants.NS_SKOS + "hiddenLabel");
		for(KiWiLiteral l : pl)
		{
			if(l.getContent().isEmpty())
				continue;
			if(l.getLanguage() == null)
				l.setLanguage(new Locale("en"));
			String lang = l.getLanguage().getLanguage();
			if(!d.languages.containsKey(lang))
				d.languages.put(lang, new TransportLanguageDetails(true));
			d.languages.get(lang).hiddenLabels.add(l.getContent());
		}
		
		HashSet<SKOSConcept> l = c.getRelated();
		d.related = new ArrayList<TransportSkosConcept>(l.size());
		for(SKOSConcept r : l)
			d.related.add(new TransportSkosConcept(r.getId(), r.getPreferredLabel(), false /* don't care in this situation */, false));
		return d;
	}
	
	private void removeAllProps(KiWiResource aRes, String aProp)
	{
		removeAllProps(aRes, aProp, null);
	}

	@SuppressWarnings("unchecked")
	private void removeAllProps(KiWiResource aRes, String aProp, Locale aLocale)
	{
		Query q = entityManager.createNamedQuery("tripleStore.literalTripleBySubjectProperty"+(aLocale != null ? "Locale" : ""));
		q.setHint("org.hibernate.cacheable", true);
		q.setParameter("subject", aRes);
		q.setParameter("property_uri", aProp);
		if(aLocale != null)
			q.setParameter("locale", aLocale);
		List<KiWiTriple> l = q.getResultList();
		for(KiWiTriple t : l)
			tripleStore.removeTriple(t);
	}

	@WebRemote
	public Boolean saveDetails(TransportSkosDetails aConcept)
	{
		/*
		 * this may seem like a dumb strategy on first sight...
		 * first removing all the properties and then adding the updated ones
		 * but the triplestore does the heavy lifting in the background,
		 * ensuring that everything is batched into one transaction and
		 * duplicate activity is removed
		 */
		SKOSConcept c = kiwiEntityManager.createFacade(byId(aConcept.id), SKOSConcept.class);
		KiWiResource cr = c.getResource();
		
		removeAllProps(cr, Constants.NS_SKOS + "prefLabel");
		removeAllProps(cr, Constants.NS_SKOS + "definition");
		removeAllProps(cr, Constants.NS_SKOS + "altLabel");
		removeAllProps(cr, Constants.NS_SKOS + "hiddenLabel");
		
		KiWiResource strType = tripleStore.createUriResource(Constants.NS_XSD+"string");
		
		for(Entry<String, TransportLanguageDetails> e : aConcept.languages.entrySet())
		{
			Locale loc = new Locale(e.getKey());
			TransportLanguageDetails details = e.getValue();
			KiWiLiteral object;
			if(!details.prefLabel.isEmpty())
			{
				object = tripleStore.createLiteral(details.prefLabel, loc, strType);
				tripleStore.createTriple(cr, Constants.NS_SKOS + "prefLabel", object);
			}

			if(!details.definition.isEmpty())
			{
				object = tripleStore.createLiteral(details.definition, loc, strType);
				tripleStore.createTriple(cr, Constants.NS_SKOS + "definition", object);
			}

			for(String l : details.altLabels)
			{
				if(l.isEmpty())
					continue;
				object = tripleStore.createLiteral(l, loc, strType);
				tripleStore.createTriple(cr, Constants.NS_SKOS + "altLabel", object);
			}

			for(String l : details.hiddenLabels)
			{
				if(l.isEmpty())
					continue;
				object = tripleStore.createLiteral(l, loc, strType);
				tripleStore.createTriple(cr, Constants.NS_SKOS + "hiddenLabel", object);
			}
		}
		return Boolean.TRUE;
	}
	
	@WebRemote
	public Long newConceptScheme(String aTitle) {
		ContentItem c = contentItemService.createContentItem();
		c.addType(tripleStore.createUriResource(Constants.NS_SKOS + "ConceptScheme"));
		return c.getId();
	}
	
	@WebRemote
	public Long newConcept(long aParent, String aTitle)
	{
		ContentItem c = contentItemService.createContentItem();
		contentItemService.updateTitle(c, aTitle);
		c.addType(tripleStore.createUriResource(Constants.NS_SKOS + "Concept"));
		
		KiWiResource strType = tripleStore.createUriResource(Constants.NS_XSD+"string");
		
		KiWiLiteral object = tripleStore.createLiteral(aTitle, new Locale("en"), strType);
		tripleStore.createTriple(c.getResource(), Constants.NS_SKOS + "prefLabel", object);
		
		ContentItem parent = byId(aParent);
		KiWiResource parentRes = parent.getResource();
		KiWiResource childRes = c.getResource();
		if(isScheme(parent))
		{
			tripleStore.createTriple(parentRes, Constants.NS_SKOS + "hasTopConcept", childRes);
			tripleStore.createTriple(childRes, Constants.NS_SKOS + "topConceptOf", parentRes);
		}
		else // the new parent is a Concept
		{
			tripleStore.createTriple(parentRes, Constants.NS_SKOS + "narrower", childRes);
			tripleStore.createTriple(childRes, Constants.NS_SKOS + "broader", parentRes);
		}
		
		return c.getId();
	}

	@WebRemote
	public Boolean removeConcepts(List<String> aConcepts)
	{
		for(String s : aConcepts)
		{
			String[] ids = s.split("-");
			int parentId = Integer.parseInt(ids[0]);
			int childId = Integer.parseInt(ids[1]);
			
			ContentItem child = byId(childId);
			KiWiResource childRes = child.getResource();

			// if the concept has more than one parent, just remove the
			// connection to its parent and not the whole content item
			if(getOutgoing(childRes, Constants.NS_SKOS + "topConceptOf").size() + 
					getOutgoing(childRes, Constants.NS_SKOS + "broader").size() > 1)
			{
				ContentItem parent = byId(parentId);
				KiWiResource parentRes = parent.getResource();
				if(isScheme(parent))
				{
					tripleStore.removeTriple(parentRes, Constants.NS_SKOS + "hasTopConcept", childRes);
					tripleStore.removeTriple(childRes, Constants.NS_SKOS + "topConceptOf", parentRes);
				}
				else // the old parent is a Concept
				{
					tripleStore.removeTriple(parentRes, Constants.NS_SKOS + "narrower", childRes);
					tripleStore.removeTriple(childRes, Constants.NS_SKOS + "broader", parentRes);
				}
			}
			else
			{
				// also make sure to remove every child concept.
				List<String> subRemove = new ArrayList<String>(8);
				SKOSConcept c = kiwiEntityManager.createFacade(child, SKOSConcept.class);
				HashSet<SKOSConcept> l = c.getNarrower();
				for(SKOSConcept cc : l)
				{
					subRemove.add(childId+"-"+cc.getId());
				}
				removeConcepts(subRemove);
				
				contentItemService.removeContentItem(child);
			}
		}
		
		return Boolean.TRUE;
	}
	
	@WebRemote
	public Boolean setSame(List<Long> aConcepts)
	{
		for(Long l1 : aConcepts)
		{
			for(Long l2: aConcepts)
			{
				if(l1.equals(l2))
					continue;
				KiWiResource r1 = byId(l1).getResource();
				KiWiResource r2 = byId(l2).getResource();
				tripleStore.createTriple(r1, Constants.NS_OWL + "sameAs", r2);
				tripleStore.createTriple(r2, Constants.NS_OWL + "sameAs", r1);
			}
		}
		return Boolean.TRUE;
	}
	
	@WebRemote
	public Boolean mergeInto(long aConcept, List<Long> aConcepts)
	{
		/*
		 * Merge prefLabel and definition separately, as there can be only one
		 * per language.
		 * Then merge every other triple. It may be possible to create some
		 * duplicates or invalid triples but we do not care about them yet.
		 */
		KiWiResource res = byId(aConcept).getResource();
		Map<String, TransportLanguageDetails> lang = getDetails(aConcept).languages;

		KiWiResource strType = tripleStore.createUriResource(Constants.NS_XSD+"string");

		for(Long id : aConcepts)
		{
			KiWiResource merge = byId(id).getResource();
			TransportSkosDetails langMerge = getDetails(id);
			
			// merge the language details
			for(Entry<String, TransportLanguageDetails> e : langMerge.languages.entrySet())
			{
				Locale loc = new Locale(e.getKey());
				
				if(!lang.containsKey(e.getKey()))
					lang.put(e.getKey(), new TransportLanguageDetails(true));
				
				TransportLanguageDetails l = lang.get(e.getKey());
				TransportLanguageDetails toMerge = e.getValue();
				
				if(!toMerge.prefLabel.isEmpty())
				{
					KiWiLiteral object = tripleStore.createLiteral(toMerge.prefLabel, loc, strType);
					// set the new prefLabel if there was none before
					if(l.prefLabel.isEmpty())
					{
						l.prefLabel = toMerge.prefLabel;
						tripleStore.createTriple(res, Constants.NS_SKOS + "prefLabel", object);
					}
					// or set it as altlabel
					else
					{
						tripleStore.createTriple(res, Constants.NS_SKOS + "altLabel", object);
					}
				}
				// append the definition
				if(!toMerge.definition.isEmpty())
				{
					KiWiLiteral object = tripleStore.createLiteral(l.definition+"\n"+toMerge.definition, loc, strType);
					// remove the old definition
					removeAllProps(res, Constants.NS_SKOS + "definition", loc);
					// and add the appended one
					tripleStore.createTriple(res, Constants.NS_SKOS + "definition", object);
				}
				// altLabels and hiddenLabels are copies together with the other properties
				// but make sure to delete the already merged prefLabel and definition
				// to not add them twice
				removeAllProps(merge, Constants.NS_SKOS + "prefLabel", loc);
				removeAllProps(merge, Constants.NS_SKOS + "definition", loc);
			}
			
			// copy over all triples
			
			for(KiWiTriple t : merge.listOutgoing())
			{
				tripleStore.createTriple(res, t.getProperty(), t.getObject());
			}

			for(KiWiTriple t : merge.listIncoming())
			{
				tripleStore.createTriple(t.getSubject(), t.getProperty(), res);
			}
			contentItemService.removeContentItem(byId(id));
		}
		
		return Boolean.TRUE;
	}
}
