/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2008-2009, The KiWi Project (http://www.kiwi-project.eu)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution.
 * - Neither the name of the KiWi Project nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Contributor(s): Jakub Kotowski
 * 
 * 
 */
package kiwi.service.reasoning;

import static kiwi.service.reasoning.ReasoningConstants.OWL_NS;
import static kiwi.service.reasoning.ReasoningConstants.RDFS_NS;
import static kiwi.service.reasoning.ReasoningConstants.RDF_FIRST;
import static kiwi.service.reasoning.ReasoningConstants.RDF_NS;
import static kiwi.service.reasoning.ReasoningConstants.RDF_REST;
import static kiwi.service.reasoning.ReasoningConstants.RDF_TYPE;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.service.reasoning.ast.TriplePattern;
import kiwi.service.reasoning.ast.Uri;
import kiwi.service.reasoning.util.TriplesToListTransformer;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/** Class representing and maintaining the RDF terminology triples, the TBox.
 * 
 * @author Jakub Kotowski
 */
@Name("TBox")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class TBox {
	private Log log = Logging.getLog(this.getClass());
	private Set<KiWiTriple> terminologicalTriples = new HashSet<KiWiTriple>();
	private Set<KiWiNode> collectionNodes = new HashSet<KiWiNode>();
	private Set<KiWiTriple> terminologicalCollectionTriples = new HashSet<KiWiTriple>();
	private Set<RDFList> collections = new HashSet<RDFList>();
	private boolean collectionsChanged = false;

	/** A meta-class is a class of classes or properties; i.e., the members of a meta-class are 
	 * either classes or properties. 
	 */
	public enum MetaClass {
		Property(RDF_NS+"Property"),
		rdfs_Class(RDFS_NS+"Class"),
		ContainterMembershipProperty(RDFS_NS+"ContainerMembershipProperty"),
		AnnotationProperty(OWL_NS+"AnnotationProperty"),
		owl_Class(OWL_NS+"Class"),
		DatatypeProperty(OWL_NS+"DatatypeProperty"),
		DeprecatedClass(OWL_NS+"DeprecatedClass"),
		DeprecatedProperty(OWL_NS+"DeprecatedProperty"),
		FunctionalProperty(OWL_NS+"FunctionalProperty"),
		InverseFunctionalProperty(OWL_NS+"InverseFunctionalProperty"),
		ObjectProperty(OWL_NS+"ObjectProperty"),
		OntologyProperty(OWL_NS+"OntologyProperty"),
		Restriction(OWL_NS+"Restriction"),
		SymmetricProperty(OWL_NS+"SymmetricProperty"),
		TransitiveProperty(OWL_NS+"TransitiveProperty"),
		IrreflexiveProperty(OWL_NS+"IrreflexiveProperty"),
		AsymmetricProperty(OWL_NS+"AsymmetricProperty"),
		Nothing(OWL_NS+"Nothing");
		
		private final String uri;
		
		private static Set<String> MetaClasses = new HashSet<String>();
		static {
			for (MetaClass mc : MetaClass.values())
				MetaClasses.add(mc.getUri());
		}
		
		MetaClass(String uri) {
			this.uri = uri;
		}
		
		public String getUri() {
			return uri;
		}
		
		public boolean matches(String uri) {
			return this.uri.equals(uri);
		}
		
		public boolean matches(KiWiUriResource uri) {
			return matches(uri.getUri());
		}
		
		public static boolean isMetaClass(String uri) {
			return MetaClasses.contains(uri);
		}
		
		public static boolean isMetaClass(KiWiUriResource resource) {
			return isMetaClass(resource.getUri());
		}
	}
	


	/** A meta-property is one which has a meta-class as it's domain. Meta-properties are used
	 * to describe classes and properties
	 */
	public enum MetaProperty {
		domain(RDFS_NS+"domain"),
		range(RDFS_NS+"range"),
		subClassOf(RDFS_NS+"subClassOf"),
		subPropertyOf(RDFS_NS+"subPropertyOf"),
		allValuesFrom(OWL_NS+"allValuesFrom"),
		cardinality(OWL_NS+"cardinality"),
		complementOf(OWL_NS+"complementOf"),
		disjointWith(OWL_NS+"disjointWith"),
		equivalentClass(OWL_NS+"equivalentClass"),
		equivalentProperty(OWL_NS+"equivalentProperty"),
		hasValue(OWL_NS+"hasValue"),
		intersectionOf(OWL_NS+"intersectionOf"),
		inverseOf(OWL_NS+"inverseOf"),
		maxCardinality(OWL_NS+"maxCardinality"),
		minCardinality(OWL_NS+"minCardinality"),
		maxQualifiedCardinality(OWL_NS+"maxQualifiedCardinality"),
		oneOf(OWL_NS+"oneOf"),
		onProperty(OWL_NS+"onProperty"),
		someValuesFrom(OWL_NS+"someValuesFrom"),
		unionOf(OWL_NS+"unionOf"),
		propertyDisjointWith(OWL_NS+"propertyDisjointWith"),
		sourceIndividual(OWL_NS+"sourceIndividual"),
		assertionProperty(OWL_NS+"assertionProperty"),
		targetIndividual(OWL_NS+"targetIndividual"),
		targetValue(OWL_NS+"targetValue"),
		differentFrom(OWL_NS+"differentFrom");
		
		private final String uri;
		
		private static Set<String> MetaProperties = new HashSet<String>();
		static {
			for (MetaProperty mp : MetaProperty.values())
				MetaProperties.add(mp.getUri());
		}
		
		MetaProperty(String uri) {
			this.uri = uri;
		}
		
		public String getUri() {
			return uri;
		}		
		
		public boolean matches(String uri) {
			return this.uri.equals(uri);
		}
		
		public boolean matches(KiWiUriResource uri) {
			return matches(uri.getUri());
		}
		
		public static boolean isMetaProperty(String uri) {
			return MetaProperties.contains(uri);
		}
		
		public static boolean isMetaProperty(KiWiUriResource resource) {
			return isMetaProperty(resource.getUri());
		}
	}

	/** A membership assertion of a meta-class.
	 * 
	 * I.e. triple of the form (?, rdf:type, c) where MetaProperty.isMetaClass(c) is true.
	 * 
	 * @param triple
	 */
	public static boolean isMetaClassMembershipAssertion(KiWiTriple triple) {
		KiWiUriResource property = triple.getProperty();
		KiWiUriResource object = null;
		
		if (triple.getObject().isUriResource())
			object = (KiWiUriResource) triple.getObject();
		else
			return false;//if object is not a URI resource then it can't be a MetaClass
		
		if (RDF_TYPE.equals(property.getUri()) && MetaClass.isMetaClass(object))
			return true;
		
		return false;
	}
	
	/** A membership assertion of a meta-class.
	 * 
	 * I.e. triple pattern of the form (?, rdf:type, c) where MetaProperty.isMetaClass(c) is true.
	 * 
	 * @return True if property is rdf:type and object is a meta class.
	 * @return False otherwise. I.e. also if either property or object is a variable.
	 */
	public static boolean isMetaClassMembershipAssertion(TriplePattern triple) {
		Uri property;
		Uri object;
		
		if (triple.getProperty().isUri())
			property = (Uri) triple.getProperty();
		else
			return false;
		
		if (triple.getObject().isUri())
			object = (Uri) triple.getObject();
		else
			return false;//if object is not a URI resource then it can't be a MetaClass
		
		if (RDF_TYPE.equals(property.getUri()) && MetaClass.isMetaClass(object.getUri()))
			return true;
		
		return false;
	}
	
	/** A membership assertion of a meta-property.
	 * 
	 * I.e. triple of the form (?, p, ?) where MetaProperty.isMetaProperty(p) is true.
	 * 
	 * @param triple
	 */
	public static boolean isMetaPropertyMembershipAssertion(KiWiTriple triple) {
		KiWiUriResource property = triple.getProperty();
		
		//a membership assertion of a meta-property
		if (MetaProperty.isMetaProperty(property))
			return true;
		
		return false;
	}

	/** A membership assertion of a meta-property.
	 * 
	 * I.e. triple pattern of the form (?, p, ?) where MetaProperty.isMetaProperty(p) is true.
	 * 
	 * @return True if the property of the pattern is a MetaProperty Uri.
	 * @return False otherwise. I.e. if the property is not a Uri or not a MetaProperty.
	 */
	public static boolean isMetaPropertyMembershipAssertion(TriplePattern triple) {
		Uri property;
		
		if (triple.getProperty().isUri())
			property = (Uri)triple.getProperty();
		else
			return false;
		
		//a membership assertion of a meta-property
		if (MetaProperty.isMetaProperty(property.getUri()))
			return true;
		
		return false;
	}

	/** Determines whether a triple is a terminological collection triple.
	 * 
	 * Does not perform a check whether it also is a sound terminological collection triple.
	 */
	public static boolean isTerminologicalCollection(KiWiTriple triple) {
		KiWiUriResource property = triple.getProperty();
			
		if (RDF_FIRST.equals(property.getUri()) || RDF_REST.equals(property.getUri())) 
			return true;
		
		return false;
	}

	/** Determines whether a triple pattern is a terminological collection triple pattern.
	 * 
	 */
	public static boolean isTerminologicalCollection(TriplePattern triple) {
		Uri property;
		
		if (triple.getProperty().isUri())
			property = (Uri) triple.getProperty();
		else
			return false;
			
		if (RDF_FIRST.equals(property.getUri()) || RDF_REST.equals(property.getUri())) 
			return true;
		
		return false;
	}

	/** Verifies whether the triple is a correct terminological coolection triple, i.e. whether it is 
	 * a triple in a non-branching non-cyclic path ending with rdf:nil.
	 */
	public boolean isCorrectTerminologicalCollection(KiWiTriple triple) {
		if (!isTerminologicalCollection(triple))
			return false;
		
		if (collectionsChanged)
			buildCollections();

		for (RDFList list : collections)
			if (list.contains(triple))
				return true;
		
		return false;
	}
			
	public void buildCollections() {
		log.debug("Building RDF lists out of #0 terminological collection triples.", terminologicalCollectionTriples.size());
		TriplesToListTransformer transformer = new TriplesToListTransformer(terminologicalCollectionTriples);
		collections = transformer.getLists();
		log.debug("Built #0 lists out of #1 terminological collection triples.", collections.size(), terminologicalCollectionTriples.size());
		
		/**just for debugging**/
		Set<KiWiTriple> unusedTriples = new HashSet<KiWiTriple>();
		unusedTriples.addAll(terminologicalCollectionTriples);
		
		for (RDFList list : collections) { 
			unusedTriples.removeAll(list.getAllFirstTriples());
			unusedTriples.removeAll(list.getAllRestTriples());
		}
		
		log.debug("After building RDF lists, #0 terminological collection triples remained unused.", unusedTriples.size());
		/*if (unusedTriples.size() != 0)
			ReasoningUtils.printSetToLog(unusedTriples, log);
		
		for (RDFList list : collections) 
			log.debug("Collection: #0", list.toString());			
		*/
		
		collectionsChanged = false;
	}
	
	/** Removes all collections that are not pointed to by a terminological triple.
	 * 
	 * Also removes all triples that make up the affected collections from this TBox.
	 * 
	 * @return The number of removed collections.
	 */
	public int cleanupCollections() {
		Set<RDFList> toRemove = new HashSet<RDFList>();
		
		for (RDFList list : collections) {
			KiWiNode beginningNode = list.getFirstTriple().getSubject(); //should be equal to list.getRestTriple().getSubject()

			assert list.getFirstTriple().getSubject().equals(list.getRestTriple().getSubject());
			
			if (!collectionNodes.contains(beginningNode)) 
				toRemove.add(list);
		}
		
		for (RDFList list : toRemove) {
			terminologicalCollectionTriples.removeAll(list.getAllFirstTriples());
			terminologicalCollectionTriples.removeAll(list.getAllRestTriples());
			terminologicalTriples.removeAll(list.getAllFirstTriples());
			terminologicalTriples.removeAll(list.getAllRestTriples());
		}
		
		collections.removeAll(toRemove);
		
		log.debug("Collection cleanup removed #0 collections.", toRemove.size());
		
		return toRemove.size();
	}
	
	/** Determines whether the triple is a terminological triple.
	 * Terminological triple is either a membership assertion of a meta-property
	 * or a membership assertion of a meta-class
	 * or it is a terminological collection triple.
	 *
	 * In case of terminological collection triples this method does not verify whether the triple is correct.
	 */
	public static boolean isTerminological(KiWiTriple triple) {
		return isMetaPropertyMembershipAssertion(triple) || 
			   isMetaClassMembershipAssertion(triple) || 
			   isTerminologicalCollection(triple);
	}
	
	/** Determines whether the triple is a terminological triple.
	 * Terminological triple is either a membership assertion of a meta-property
	 * or a membership assertion of a meta-class
	 * or it is a terminological collection triple.
	 *
	 * In case of terminological collection triples this method does not verify whether the triple is correct.
	 */
	public static boolean isTerminological(TriplePattern triple) {
		return isMetaPropertyMembershipAssertion(triple) || 
			   isMetaClassMembershipAssertion(triple) || 
			   isTerminologicalCollection(triple);
	}
	
	/** Determines whether the triple is a terminological triple.
	 * Same as {@link isTerminologicalTriple()} but with correctness checking for terminological collection triples.
	 */
	public boolean isCorrectTerminological(KiWiTriple triple) {
		return isMetaPropertyMembershipAssertion(triple) || 
			   isMetaClassMembershipAssertion(triple) || 
			   isCorrectTerminologicalCollection(triple);
	}

	/** Determines whether the property of the triple is supposed to point to a collection (a resource of RDF List type).
	 * 
	 * @return True if triple.getProperty() is one of owl:intersectionOf, owl:unionOf, or owl:oneOf.
	 */
	public boolean isCollectionPointingTerminological(KiWiTriple triple) {
		return MetaProperty.intersectionOf.matches(triple.getProperty()) ||
		MetaProperty.unionOf.matches(triple.getProperty()) ||
		MetaProperty.oneOf.matches(triple.getProperty());
	}
	
	/** Returns true if this TBox contains the triple.
	 */
	public boolean contains(KiWiTriple triple) {
		return terminologicalTriples.contains(triple);
	}
	
	/** Adds the triple to this TBox.
	 * 
	 * Returns true if it was a terminological triple.
	 * Returns false and does not add the triple if it wasn't a treminological triple.
	 */
	public boolean add(KiWiTriple triple) {
		if (isTerminologicalCollection(triple)) {
			terminologicalCollectionTriples.add(triple);
			collectionsChanged = true;
		}
		
		if (isCollectionPointingTerminological(triple)) 
			collectionNodes.add(triple.getObject());
		
		if (isTerminological(triple)) {
			if (!terminologicalTriples.contains(triple)) {
				terminologicalTriples.add(triple);
				//log.debug("New terminological triple: #0", triple);
			}
			return true;
		}
		
		return false;
	}
	
	/** Adds all given triples to this TBox.
	 * 
	 * @return the number of truly added triples, which is the number of times add(triple) would return true.
	 */
	public int addAll(Collection<KiWiTriple> triples) {
		int i = 0;
		
		for (KiWiTriple triple : triples) {
			boolean added = add(triple);
			if (added) i++;
		}
		
		return i;
	}
	
	/** Remove triple from this TBox.
	 * 
	 * Returns true if it was a terminological triple.
	 * Returns false if it wasn't a treminological triple.
	 */
	public boolean remove(KiWiTriple triple) {
		if (!isTerminological(triple))
			return false;
		
		terminologicalTriples.remove(triple);
		
		if (isTerminologicalCollection(triple)) {
			terminologicalCollectionTriples.remove(triple);
			collectionsChanged = true;
		}
		
		return true;
	}
	
	/** Removes all given triples from this TBox.
	 * 
	 * @return the number of truly removed triples, which is the number of times remove(triple) would return true.
	 */
	public int removeAll(Collection<KiWiTriple> triples) {
		int i = 0;
		
		for (KiWiTriple triple : triples) {
			boolean removed = remove(triple);
			if (removed) i++;
		}
		
		return i;		
	}
	
	public boolean isSatisfiable(TriplePattern triplePattern) {
		if (!isTerminological(triplePattern))
			return false;
		
		for (KiWiTriple triple : terminologicalTriples) {
			if (triplePattern.matches(triple))
				return true;
		}
			
		return false;
	}
	
	public void clear() {
		terminologicalCollectionTriples.clear();
		terminologicalTriples.clear();
		collectionNodes.clear();
		collections.clear();
		collectionsChanged = false;
	}	
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("TBox: ");
		sb.append(terminologicalTriples.size()).append(" terminological triples, of of which ");
		sb.append(terminologicalCollectionTriples.size()).append(" are collection triples, out of which ");
		sb.append(collections.size()).append(" RDF collections were built.");
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(MetaProperty.isMetaProperty(MetaProperty.allValuesFrom.getUri()));
	}
	
}









