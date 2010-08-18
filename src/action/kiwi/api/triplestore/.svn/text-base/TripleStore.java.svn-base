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
 * Contributor(s):
 * 
 * 
 */
package kiwi.api.triplestore;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import kiwi.model.kbase.KiWiAnonResource;
import kiwi.model.kbase.KiWiDataFormat;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;

import org.openrdf.repository.Repository;

// ESCA-JAVA0136:
/**
 * @author Sebastian Schaffert
 *
 */
public interface TripleStore {
	
	
	/**
	 * Adds KiWiEntity to the transaction data. Before the transaction
	 * completes, persistReal() will be called, so that every entity 
	 * is persisted into the triplestore onley once per transaction.
	 * technique: - this method is
	 * always called when an object is persisted (or when it's explicitly
	 * called) - if the object is an instance of KiWiEntity and has fields that
	 * are annotated with
	 * 
	 * @RDF(...), the Field value is stored in the triplestore - this works for
	 *            fields of the type: String, Date, Locale, classes that
	 *            implement KiWiEntity, Document and classes that implement
	 *            Collection - when an object is inherited, the superclass is
	 *            also searched for
	 * @RDF annotations
	 * @author sstroka
	 * @param <C>
	 * @param entity
	 * @return
	 */
	public <C extends KiWiEntity> C persist(C entity);
	
	/**
	 * stores RDF relevant data in the triplestore technique: - this method is
	 * always called when an object is persisted (or when it's explicitly
	 * called) - if the object is an instance of KiWiEntity and has fields that
	 * are annotated with
	 * 
	 * @RDF(...), the Field value is stored in the triplestore - this works for
	 *            fields of the type: String, Date, Locale, classes that
	 *            implement KiWiEntity, Document and classes that implement
	 *            Collection - when an object is inherited, the superclass is
	 *            also searched for
	 * @RDF annotations
	 * @author sstroka
	 * @param <C>
	 * @param entity
	 * @return
	 */
	public <C extends KiWiEntity> C persist(C entity, boolean persistNow);


	/**
	 * remove the RDF relevant data from the triple store.
	 * @param <C>
	 * @param entity
	 */
	public <C extends KiWiEntity> void remove(C entity);

	
    /**
	 * This method checks the annotated fields of a class for 'transient' (not
	 * persisted by JPA) and 'RDF' or just for 'RDF' if the class is not
	 * annotated with 'entity'. If there exists such a field the setter method
	 * of the object will be called with the data hold in the tripleStore. This
	 * allows the mapping of data hold in the tripleStore to POJOs.
	 * 
	 * It is important to know that KiWiEntities that have to be loaded to the
	 * object (e.g. Set<User> friends) will be called by the 'getUserByUri()'
	 * method. That will result in a complete-search and that is not very
	 * practicle. (Think about a KiWiPlatform with 1000 registered users where
	 * every user will load detailed information about the friends and the
	 * friends of the friends and the friends of the friends of the friends ...)
	 * That`s why we add a level of detail, let`s say '1' for <load only details
	 * for YOUR friends> and '2' for <load the details of your friends and the
	 * friends of them>, etc..
	 * 
	 * @param <C>
	 * @param entity
	 */
	public <C extends KiWiEntity> void refresh(C entity, boolean override); 
		
	
	public <C extends KiWiEntity> C merge(C entity);
	
		
	public long size();

	/**
	 * List all triples contained in this Knowledge Space.
	 * @return
	 */
	public Iterable<KiWiTriple> listTriples(boolean inferred);

    /**
     * List only inferred triples contained in this Knowledge Space.
     * @return
     */
    public Iterable<KiWiTriple> listInferredTriples();
	
    /**
     * Check whether the triple formed by the parameters exists.
     * @param subject
     * @param property
     * @param object
     * @return
     */
    public boolean hasTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object);


	/**
	 * Create or retrieve a KiWi triple in this knowledge space using the provided subject, predicate, and 
	 * object. 
	 * <p>
	 * If a triple with the same subject/property/object already exists in the knowledge base,
	 * it is returned. 
	 * <p>
	 * If a triple with the same subject/property/object does not yet exist, this method will automatically 
	 * generate a new triple id for the triple, and then store the triple in
	 * the knowledge base. The rationale behind this is that many methods of ExtendedTriple directly work on
	 * the knowledge base and require both, a triple id and the triple to be persisted.
	 * 
	 * @param subject
	 * @param property
	 * @param object
	 */
	public KiWiTriple createTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object);

	/**
	 * Create or retrieve a KiWi triple in this knowledge space using the provided subject, predicate, and 
	 * object. 
	 * <p>
	 * If a triple with the same subject/property/object already exists in the knowledge base,
	 * it is returned. 
	 * <p>
	 * If a triple with the same subject/property/object does not yet exist, this method will automatically 
	 * generate a new triple id for the triple, and then store the triple in
	 * the knowledge base. The rationale behind this is that many methods of ExtendedTriple directly work on
	 * the knowledge base and require both, a triple id and the triple to be persisted.
	 * 
	 * @param subject
	 * @param property
	 * @param object
	 * @param context
	 */
	public KiWiTriple createTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object, KiWiUriResource context);
	
	
	/**
     * Convenience method that allows passing a property by string; also avoids unnecessary JNDI
     * lookups, because the property triple does not need to be created separately.
     * 
     * @param subject a KiWiResource instance representing the subject of the triple to be added
     * @param property_uri a String containing the URI of the property to be added
     * @param object a KiWiNode instance representing the object of the triple
     * @return the KiWiTriple instance representing the relation between subject, property, and object
     */
	public KiWiTriple createTriple(KiWiResource subject, String property_uri, KiWiNode object);
	
	/**
	 * Create the triples and associated nodes passed as collection. All triples and nodes in the collection
	 * will be checked for existance using the createTriple, createUriResource, ... methods, so the triples
	 * passed as argument need not origin from this TripleStore instance.
	 * <p>
	 * The purpose of this method is to allow for efficient batch insertions of data.
	 * 
	 * @param triples
	 */
	public void storeTriplesUnchecked(Collection<KiWiTriple> triples);
	
	
	/**
	 * Create a new KiWiUriResource in this knowledge space using the provided uri. Note that resources are not
	 * stored unless they are part of a triple.
	 * @param uri
	 * @return
	 */
	public KiWiUriResource createUriResource(String uri);

	
    /**
     * Create a new KiWiUriResource in this knowledge space using the provided namespace prefix and
     * local name. Note that resources are not stored unless they are part of a triple.
     * @param nsprefix the configured namespace prefix, e.g. "rdf"
     * @param name the local name of the resource, e.g. "type"
     * @return
     */
    public KiWiUriResource createUriResourceByNamespaceTitle(String nsprefix, String name);

    /**
	 * Create a KiWiUriResource in this knowledge space by parsing the provided identifier in SPARQL/SeRQL short or long form.
	 * - long form: <URI> enclosed in pointy braces
	 * - short form: ns-prefix : local name, separated by colon
	 * @param id
	 * @return
	 */
	public KiWiUriResource createUriResourceBySPARQLId(String id);

	/**
	 * Create a new KiWiAnonResource using a new unique identifier.  Note that resources are not stored unless 
	 * they are part of a triple.
	 * 
	 * @return
	 */
	public KiWiAnonResource createAnonResource();

	/**
	 * Create a new KiWiAnonResource using the provided identifier. If the id is already used, will return the
	 * object associated with this id. Note that resources are not stored unless they are part of a triple.
	 * 
	 * @return
	 */
	public KiWiAnonResource createAnonResource(String id);

	/**
	 * Create a new KiWiLiteral of the provided type.
	 * @param <T>
	 * @param value
	 * @param language
	 * @param type
	 * @return
	 */
	public <T> KiWiLiteral createLiteral(T value, Locale language, KiWiResource type);

	/**
	 * Create a new KiWiLiteral. The type is inferred by Java reflection.
	 * @param <T>
	 * @param value
	 * @return
	 */
	public <T> KiWiLiteral createLiteral(T value);

    /**
     * Create or retrieve the default knowledge space of this system. The default knowledge space has a URI that is
     * constructed from the base URI of this KiWi installation, followed by the postfix /kspace/default, e.g.
     * http://localhost/KiWi/kspace/default
     *
     * @return
     */
    public KiWiUriResource createDefaultKnowledgeSpace();

    /**
     * Create or retrieve the knowledge space with the given uri and return it. 
     * @param uri the URI of the knowledge space
     * @return
     */
    public KiWiUriResource createKnowledgeSpace(String uri);


	/**
	 * Remove a triple from the repository. This method only adds the triple to the
     * current transaction data; the triple will be persisted to the database and repository
     * on transaction commit. 
     * 
	 * @param triple
	 */
	public void removeTriple(KiWiTriple triple);

	/**
	 * Remove a triple from the repository with the given subject, property, and object
	 * @param subject
	 * @param property
	 * @param object
	 */
	public void removeTriple(KiWiResource subject, KiWiUriResource property, KiWiNode object);

    /**
     * Convenience method that allows passing the property uri as string rather than as resource.
     * Useful in situations where it is not necessary to first create a URI resource for a property.
     * 
     * @param subject the KiWiResource that is the subject of the triple
     * @param property_uri a String containing the URI of the property to be removed
     * @param object the KiWiNode representing the object of the triple
     */
    public void removeTriple(KiWiResource subject, String property_uri, KiWiNode object);
	
    /** Convenience method to load triples of the form (subject, property, ?).
     * 
     * @param subject the KiWiResource that is the subject of the retrieved triples
     * @param property the KiWiUriResource that is the property of the retrieved triples
     * @return Triples of the form (subject, property, ?)
     */
	public List<KiWiTriple> getTriplesBySP(KiWiResource subject, KiWiUriResource property);

 	/** Convenience method to load all triples of the form (?, property, ?).
 	 * 
 	 * @param property the KiWiUriResource that is the property of the retrieved triples
 	 */
	public List<KiWiTriple> getTriplesByP(KiWiUriResource property);

 	/** Convenience method to load all triples of the form (?, property_uri, ?).
 	 * 
 	 * @param propertyUri the uri of the property of the retrieved triples
 	 */
	public List<KiWiTriple> getTriplesByP(String propertyUri);

	
 	/** Convenience method to load all triples of the form (?, property_uri, object_uri).
 	 * 
 	 * @param propertyUri the uri of the property of the retrieved triples
 	 * @param objectUri the uri of the object of the retrieved triples
 	 */
	public List<KiWiTriple> getTriplesByPO(String propertyUri, String objectUri);	
	
	/**
     * Add or update a triple to/in the triple store. This method only adds the triple to the
     * current transaction data; the triple will be persisted to the database and repository
     * on transaction commit. 
     * 
     * @param triple
     */
	public void storeTriple(KiWiTriple triple);
	
	// the following two methods will eventually be removed and should be replaced by
	// RDFExporterImpl exports
	@Deprecated
	public void exportData(OutputStream out, KiWiDataFormat format);

	@Deprecated
	public void exportData(OutputStream out, KiWiResource subject, KiWiDataFormat format);
	

	public String getNamespaceUri(String prefix);

	public String getNamespacePrefix(String uri);
	
	public KiWiNamespace getNamespace(String prefix);

	public void setNamespace(String prefix, String uri);

	/**
     * Add or update a namespace to/in the triple store. This method only adds the namespace to the
     * current transaction data; the namespace will be persisted to the database and repository
     * on transaction commit. 
     * 
     * @param ns the KiWi namespace object to update
     */
	public void setNamespace(KiWiNamespace ns);
	
	

	
	public void removeNamespace(String prefix);

	/**
	 * Removes a namespace from triplestore
	 * @param ns
	 */
	public void removeNamespace(KiWiNamespace ns);
	
	
	/**
	 * List all namespaces defined for the knowledge space
	 * @return
	 */
	public Iterable<KiWiNamespace> listNamespaces();

	/**
	 * Return the appropriate XSD type for RDF literals for the provided Java class.
	 * @param javaClass
	 * @return
	 */
	public KiWiResource getXSDType(Class javaClass);
	

	/**
	 * Return the sesame repository used by the triple store. This method is needed for the moment
	 * to transform to and from Sesame data types.
	 * @return
	 */
	@Deprecated
	public Repository getRepository();
	
	/**
	 * Recreate the Sesame index out of the database.
	 */
	public void reIndex();
	
	
	
	public void clear();
	
	public void clearCaches();
}
