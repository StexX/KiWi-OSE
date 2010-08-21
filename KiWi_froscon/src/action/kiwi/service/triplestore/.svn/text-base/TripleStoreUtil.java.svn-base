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
package kiwi.service.triplestore;

import java.util.Locale;

import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.kbase.KiWiAnonResource;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;

import org.hibernate.proxy.HibernateProxy;
import org.jboss.seam.Component;
import org.jboss.seam.cache.CacheProvider;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.QueryLanguage;

/**
 * @author Sebastian Schaffert
 *
 */
public class TripleStoreUtil  {

	
	/**
     * Transform a KiWi node into a Sesame value; differentiates between the different types of nodes
     * and returns appropriate Sesame values:
     * <ul>
     * 	<li>a KiWiUriResource is transformed into a Sesame URI</li>
     *  <li>a KiWiAnonResource is transformed into a Sesame BNode</li>
     *  <li>a KiWiLiteral is transformed into a Sesame Literal</li>
     * </ul>
     * 
     * @param tripleStore the triple store in which the node is represented
     * @param kiwi the KiWiNode to transform
     * @return the Sesame node object
     */
	public static final Value transformKiWiToSesame(TripleStore tripleStore,KiWiNode kiwi) {
        ValueFactory f = tripleStore.getRepository().getValueFactory();
        
        return transformKiWiToSesame(f,kiwi);
	}

	/**
     * Transform a KiWi node into a Sesame value; differentiates between the different types of nodes
     * and returns appropriate Sesame values:
     * <ul>
     * 	<li>a KiWiUriResource is transformed into a Sesame URI</li>
     *  <li>a KiWiAnonResource is transformed into a Sesame BNode</li>
     *  <li>a KiWiLiteral is transformed into a Sesame Literal</li>
     * </ul>
     * This method may be used to create a Sesame Value outside the original triple store.
     * 
     * @param f the value factory to use for creating the node
     * @param kiwi the KiWiNode to transform
     * @return the Sesame node object
     */
	public static final Value transformKiWiToSesame(ValueFactory f,KiWiNode kiwi) {
		
		if(kiwi == null) {
			return null;
		}
		
		// work around lazy Hibernate proxies; I hate the visitor pattern, but Hibernate enforces
		// it - this piece of code works around it...
		if (kiwi instanceof HibernateProxy) {  
			return transformKiWiToSesame(f,(KiWiNode)((HibernateProxy)kiwi).getHibernateLazyInitializer().getImplementation());  
		}  
		
        if(kiwi.isUriResource()) {
            return f.createURI(((KiWiUriResource)kiwi).getUri());
        } else if(kiwi.isAnonymousResource()) {
            return f.createBNode(((KiWiAnonResource)kiwi).getAnonId());
        } else if(kiwi.isLiteral()) {
            KiWiLiteral l = (KiWiLiteral)kiwi;
            if(l.getType().equals(Constants.NS_XSD+"integer")) {
                return f.createLiteral(Integer.parseInt(l.getContent()));                  
            } else if(l.getType().equals(Constants.NS_XSD+"long")) {
                return f.createLiteral(Long.parseLong(l.getContent()));                  
            } else if(l.getType().equals(Constants.NS_XSD+"boolean")) {
                return f.createLiteral(Boolean.parseBoolean(l.getContent()));                  
            } else if(l.getType().equals(Constants.NS_XSD+"double")) {
                return f.createLiteral(Double.parseDouble(l.getContent()));                  
            } else if(l.getType().equals(Constants.NS_XSD+"float")) {
                return f.createLiteral(Float.parseFloat(l.getContent()));                  
            } else {
                return f.createLiteral(l.getContent().toString(), l.getLanguage()!=null?l.getLanguage().getLanguage():null);  
            }
            
        } else {
            return null;
        }
 	}

    /**
     * Transform a KiWi triple into a Sesame statement.
     * 
     * @param tripleStore the triple store in which the triple is represented 
     * @param triple the KiWiTriple to convert
     * @return
     */
	public static final Statement transformKiWiToSesame(TripleStore tripleStore, KiWiTriple triple) {
        ValueFactory f = tripleStore.getRepository().getValueFactory();
        return transformKiWiToSesame(f,triple);
	}
	
	
    /**
     * Transform a KiWi triple into a Sesame statement. This method may be used to create a 
     * Sesame Statement outside the original triple store.
     * 
     * @param f the value factory to use for creating the sesame statement
     * @param triple triple the KiWiTriple to convert
     * @return
     */
	public static final Statement transformKiWiToSesame(ValueFactory f, KiWiTriple triple) {
		return f.createStatement(   
				(Resource) transformKiWiToSesame(f,triple.getSubject()),
                (URI)      transformKiWiToSesame(f,triple.getProperty()), 
                           transformKiWiToSesame(f,triple.getObject()),  
                (Resource) transformKiWiToSesame(f,triple.getContext()) );		
	}

	/* (non-Javadoc)
	 * @see kiwi.api.triplestore.SesameService#transformKiWiToSesame(kiwi.model.kbase.KiWiQueryLanguage)
	 */
	public static final QueryLanguage transformKiWiToSesame(TripleStore tripleStore, KiWiQueryLanguage lang) {
        switch(lang) {
        case SERQL:
            return QueryLanguage.SERQL;
        case SPARQL:
            return QueryLanguage.SPARQL;
        default:
            return QueryLanguage.SPARQL;
        }
	}

	/**
     * This method transforms a Sesame 2 value into a KiWi node:
     * <ul>
     * 	<li>a Sesame Literal is transformed to a KiWiLiteral by calling tripleStore.createLiteral()</li>
     *  <li>a Sesame URI is transformed to a KiWiUriResource by calling tripleStore.createUriResource()</li>
     *  <li>a Sesame BNode is transformed to a KiWiAnonResource by calling tripleStore.createAnonResourc()</li>
     * </ul>
     * @param v
     * @return
     */
	public static final KiWiNode transformSesameToKiWi(TripleStore tripleStore, Value v) {
		if(v == null) return null;
    	
    	CacheProvider cacheProvider = (CacheProvider)Component.getInstance("org.jboss.seam.cache.cacheProvider");
    	
		KiWiNode result = (KiWiNode) cacheProvider.get("nodes",v.stringValue());
        if(result == null) {

            if(v instanceof URI) {
                URI uri = (URI)v;
                result = tripleStore.createUriResource(uri.getNamespace()+uri.getLocalName());            
            } else if(v instanceof BNode) {
                BNode bnode = (BNode)v;
                result = tripleStore.createAnonResource(bnode.getID());
            } else if(v instanceof Literal) {
                Literal literal = (Literal)v;
                
                Locale language;
                if(literal.getLanguage() != null) {
                    language = new Locale(literal.getLanguage());
                } else {
                    language = null;
                }
                
                
                // currently, type is set to String
                if(literal.getDatatype() == null) {
                    KiWiResource type = tripleStore.createUriResource(Constants.NS_XSD+"string");
                    result = tripleStore.createLiteral(literal.getLabel(), language, type);
                } else if(literal.getDatatype().getLocalName().equals("string")) {
                    KiWiResource type = tripleStore.createUriResource(Constants.NS_XSD+"string");
                    result = tripleStore.createLiteral(literal.getLabel(), language, type);
                } else if(literal.getDatatype().getLocalName().equals("integer")) {
                    KiWiResource type = tripleStore.createUriResource(Constants.NS_XSD+"integer");
                    result = tripleStore.createLiteral(literal.stringValue(), language, type);
                } else if(literal.getDatatype().getLocalName().equals("double")) {
                    KiWiResource type = tripleStore.createUriResource(Constants.NS_XSD+"double");
                    result = tripleStore.createLiteral(literal.stringValue(), language, type);
                } else if(literal.getDatatype().getLocalName().equals("float")) {
                    KiWiResource type = tripleStore.createUriResource(Constants.NS_XSD+"float");
                    result = tripleStore.createLiteral(literal.stringValue(), language, type);
                } else if(literal.getDatatype().getLocalName().equals("long")) {
                    KiWiResource type = tripleStore.createUriResource(Constants.NS_XSD+"long");
                    result = tripleStore.createLiteral(literal.stringValue(), language, type);
                } else if(literal.getDatatype().getLocalName().equals("boolean")) {
                    KiWiResource type = tripleStore.createUriResource(Constants.NS_XSD+"boolean");
                    result = tripleStore.createLiteral(literal.stringValue(), language, type);
                } else {
                    result = tripleStore.createLiteral(
                    		literal.getLabel(), 
                    		language, 
                    		(KiWiResource)transformSesameToKiWi(tripleStore,literal.getDatatype()));
                    
                }
            }
            cacheProvider.put("nodes",v.stringValue(),result);
            return result;
        } else {
        	//EntityManager entityManager = (EntityManager) Component.getInstance("entityManager");
        	
        	return result;
//        	if(result.getId() != null && !entityManager.contains(result)) {
//        		return entityManager.merge(result);
//        	} else {
//        		return result;
//        	}
        }
	}

	/**
     * This method transforms a Sesame 2 statement into a KiWi extended triple. The transformation
     * calls tripleStore.createTriple().
     * 
     * @param stmt the Sesame statement to transform
     * @return a persisted KiWiTriple
     */
	public static final KiWiTriple transformSesameToKiWi(TripleStore tripleStore,Statement stmt) {
        // in KiWi's data model, every statement is uniquely identified by its context, so we can use the 
        // context URI as unique identifier in the triple pool
        KiWiTriple result = null;
 
        if(stmt.getContext() != null) {
            KiWiResource    subject  = (KiWiResource)    transformSesameToKiWi(tripleStore,stmt.getSubject());
            KiWiUriResource property = (KiWiUriResource) transformSesameToKiWi(tripleStore,stmt.getPredicate());
            KiWiNode        object   =                   transformSesameToKiWi(tripleStore,stmt.getObject());
            KiWiUriResource context  = (KiWiUriResource) transformSesameToKiWi(tripleStore,stmt.getContext());
            
            result = tripleStore.createTriple(subject, property, object, context);
            
        } else { 
            KiWiResource    subject  = (KiWiResource)    transformSesameToKiWi(tripleStore,stmt.getSubject());
            KiWiUriResource property = (KiWiUriResource) transformSesameToKiWi(tripleStore,stmt.getPredicate());
            KiWiNode        object   =                   transformSesameToKiWi(tripleStore,stmt.getObject());
            result = tripleStore.createTriple(subject, property, object);
        }
        
        
        return result;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.triplestore.SesameService#transformSesameToUriResource(org.openrdf.model.URI)
	 */
	public static final KiWiUriResource transformSesameToUriResource(TripleStore tripleStore,URI uri) {
        return (KiWiUriResource)transformSesameToKiWi(tripleStore,uri);
	}

	
	/**
     * This method transforms a Sesame 2 statement into a KiWi extended triple. The transformation
     * is unchecked - it returns a newly allocated triple which may yield duplicates in the
     * triple store (use with care!)
     * 
     * @param stmt the Sesame statement to transform
     * @return a persisted KiWiTriple
     */
	public static final KiWiTriple uncheckedSesameToKiWi(Statement stmt) {
        // in KiWi's data model, every statement is uniquely identified by its context, so we can use the 
        // context URI as unique identifier in the triple pool
        KiWiTriple result = null;
 
        if(stmt.getContext() != null) {
            KiWiResource    subject  = (KiWiResource)    uncheckedSesameToKiWi(stmt.getSubject());
            KiWiUriResource property = (KiWiUriResource) uncheckedSesameToKiWi(stmt.getPredicate());
            KiWiNode        object   =                   uncheckedSesameToKiWi(stmt.getObject());
            KiWiUriResource context  = (KiWiUriResource) uncheckedSesameToKiWi(stmt.getContext());
            
            result = new KiWiTriple(subject, property, object, context);
            
        } else { 
            KiWiResource    subject  = (KiWiResource)    uncheckedSesameToKiWi(stmt.getSubject());
            KiWiUriResource property = (KiWiUriResource) uncheckedSesameToKiWi(stmt.getPredicate());
            KiWiNode        object   =                   uncheckedSesameToKiWi(stmt.getObject());
            result = new KiWiTriple(subject, property, object);
        }
        
        
        return result;
	}

	
	/**
     * This method transforms a Sesame 2 value into a KiWi node:
     * The transformation is unchecked - it returns a newly allocated triple which may yield duplicates in the
     * triple store (use with care!)
     * <ul>
     * 	<li>a Sesame Literal is transformed to a KiWiLiteral </li>
     *  <li>a Sesame URI is transformed to a KiWiUriResource </li>
     *  <li>a Sesame BNode is transformed to a KiWiAnonResource </li>
     * </ul>
     * @param v
     * @return
     */
	public static final KiWiNode uncheckedSesameToKiWi(Value v) {
		if(v == null) return null;
    	
		KiWiNode result = null;

        if(v instanceof URI) {
            URI uri = (URI)v;
            result = new KiWiUriResource(uri.getNamespace()+uri.getLocalName());            
        } else if(v instanceof BNode) {
            BNode bnode = (BNode)v;
            result = new KiWiAnonResource(bnode.getID());
        } else if(v instanceof Literal) {
            Literal literal = (Literal)v;
            
            Locale language;
            if(literal.getLanguage() != null) {
                language = new Locale(literal.getLanguage());
            } else {
                language = null;
            }
            
            
            KiWiResource type = null;
            
            // currently, type is set to String
            if(literal.getDatatype() == null) {
                type = new KiWiUriResource(Constants.NS_XSD+"string");
             } else if(literal.getDatatype().getLocalName().equals("string")) {
                type = new KiWiUriResource(Constants.NS_XSD+"string");
            } else if(literal.getDatatype().getLocalName().equals("integer")) {
                type = new KiWiUriResource(Constants.NS_XSD+"integer");
            } else if(literal.getDatatype().getLocalName().equals("double")) {
                type = new KiWiUriResource(Constants.NS_XSD+"double");
            } else if(literal.getDatatype().getLocalName().equals("float")) {
                type = new KiWiUriResource(Constants.NS_XSD+"float");
            } else if(literal.getDatatype().getLocalName().equals("long")) {
                type = new KiWiUriResource(Constants.NS_XSD+"long");
            } else if(literal.getDatatype().getLocalName().equals("boolean")) {
                type = new KiWiUriResource(Constants.NS_XSD+"boolean");
            } else {
            	type = (KiWiResource)uncheckedSesameToKiWi(literal.getDatatype());
            }
            result = new KiWiLiteral(literal.stringValue(), language, type);
        }
        return result;
 	}

	
	
	public static final String createCacheKey(KiWiTriple triple) {
		KiWiResource subject  = triple.getSubject();
		KiWiResource property = triple.getProperty();
		KiWiNode     object   = triple.getObject();
		KiWiResource context  = triple.getContext();
		
		return createCacheKey(subject,property,object,context);
 	}
	
	public static final String createCacheKey(KiWiResource subject, KiWiResource property, KiWiNode object, KiWiResource context) {
    	if(context != null) {
    		return "{"+subject.toString()+" "+property.toString()+" "+object.toString()+"}@"+context.toString();   
    	} else {
    		return "{"+subject.toString()+" "+property.toString()+" "+object.toString()+"}@GLOBAL";
    	}
	}

	
	public static final String createCacheKey(KiWiNamespace ns) {
		return ns.getPrefix();
	}
}
