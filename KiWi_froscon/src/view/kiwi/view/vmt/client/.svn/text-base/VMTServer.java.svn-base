package kiwi.view.vmt.client;

import java.util.List;
import kiwi.transport.client.TransportSkosConcept;
import kiwi.transport.client.TransportSkosDetails;
import kiwi.transport.client.TransportTag;

import com.google.gwt.user.client.rpc.RemoteService;

public interface VMTServer extends RemoteService {
	/**
	 * Returns the list of child concepts belonging to the ContentItem with
	 * the database id aParent. It may be either a skos:Concept or
	 * skos:ConceptScheme.
	 */
	public List<TransportSkosConcept> getChildConcepts(long aParent);
	/**
	 * Convert the ContentItem given by the database id aTag to a skos:Concept
	 * by adding and place it inside the parent given by the database id
	 * aParent. The parent may be either a skos:Concept or skos:ConceptScheme.
	 */
	public Boolean convertToConcept(long aTag, long aParent);
	/**
	 * Moves the concept given by the database id aConcept from its current
	 * parent given by aOldParent to a new parent given by aParent.
	 * both parents can be either of type skos:Concept or skos:ConceptScheme.
	 */
	public Boolean moveConcept(long aConcept, long aOldParent, long aParent);
	/**
	 * Adds the concept given by the database id aConcept as a new child to
	 * the ContentItem given by aParent. The parent may be either a
	 * skos:Concept or skos:ConceptScheme.
	 * The concept is not actually duplicated, only a new parent-child
	 * relationship is created
	 */
	public Boolean copyConcept(long aConcept, long aParent);
	/**
	 * A semantic relation is created between the two concepts given by the
	 * database ids aConcept1 and aConcept2 using bi-directional skos:related
	 * properties.
	 */
	public Boolean setRelated(long aConcept1, long aConcept2);
	/**
	 * The semantic relation designated by bi-directional skos:related
	 * properties is removed between the concept given by the database id
	 * aConcept and every one of the concepts inside aConcepts.
	 */
	public Boolean unsetRelated(long aConcept, List<Long> aConcepts);
	/**
	 * This fetches the complete language and relationship details of a concept
	 * given by the database id aConcept. @see TransportSkosDetails.
	 */
	public TransportSkosDetails getDetails(long aConcept);
	/**
	 * This saves the language details presented inside aConcept into the
	 * triplestore. The relationship information inside TransportSkosDetails
	 * is not used and can be omitted.
	 */
	public Boolean saveDetails(TransportSkosDetails aConcept);
	/**
	 * Returns a list of ContentItems of type kiwi-core:Tag that do not have
	 * the type skos:Concept and thus are not contained in a skos thesaurus yet.
	 */
	public List<TransportTag> getFreeTags();
	/**
	 * Creates a new ContentItem with type skos:ConceptScheme with using the
	 * title given in aTitle, returning its new database id.
	 */
	public Long newConceptScheme(String aTitle);
	/**
	 * Creates a new ContentItem with type skos:Concept and places it inside
	 * the parent given by the database id aParent. The new concept is given
	 * a preferred label in the locale "en" using aTitle.
	 * Returns the newly created database id.
	 */
	public Long newConcept(long aParent, String aTitle);
	/**
	 * This takes a List of concepts in aConcepts using the syntax
	 * "parentId-childId". This removes the given parent-child relationship
	 * and may also remove the ContentItem completely when no more parent-child
	 * exist. parentId and childId are both database ids.
	 */
	public Boolean removeConcepts(List<String> aConcepts);
	/**
	 * The concepts given in aConcepts (database ids) are connected to each
	 * other using bi-directional owl:sameAs properties
	 */
	public Boolean setSame(List<Long> aConcepts);
	/**
	 * The concepts given in aConcepts are merged into the concept given by
	 * aConcept. This is achieved by intelligently merging the skos properties
	 * and then adding every other property that exists in aConcepts to
	 * aConcept. The concepts given in aConcepts are removed upon merging.
	 */
	public Boolean mergeInto(long aConcept, List<Long> aConcepts);
}
