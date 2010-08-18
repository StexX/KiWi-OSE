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
package kiwi.service.entity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFInverse;
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiLiteral;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiUriResource;

import org.jboss.seam.Component;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;


/**
 * This class implements an invocation handler to be used for
 * proxy classes that delegate to a content item and to data in
 * the triple store. It has to be constructed using the triple
 * store implementation as parameter. Interfaces that make use of
 * this invocation handler need to be annotated with the
 * <code>KiWiFacade</code> annotation.
 *
 * @author Sebastian Schaffert
 */
public class KiWiInvocationHandler implements InvocationHandler {

    private final TripleStore tripleStore;

    private ContentItem item;

    private final HashMap<String,Object> fieldCache;

    /**
     * Indicates if the cache is done not, by defaul is true.
     */
    private boolean useCache;

    public KiWiInvocationHandler(ContentItem item, TripleStore tripleStore) {
        this.item        = item;
        this.tripleStore = tripleStore;
        fieldCache  = new HashMap<String,Object>();
        useCache = true;
    }

    /**
     * Indicates if the cache is allow or not.
     * 
     * @return the useCache true if the cache is done.
     */
    public boolean isUseCache() {
        return useCache;
    }

    /**
     * Used to enable/disable the cache mechanism.
     * 
     * @param useCache true foe enable cache, false - no cache.
     */
    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    /**
     * @return the item
     */
    public ContentItem getItem() {
        return item;
    }



    /**
     * @param item the item to set
     */
    public void setItem(ContentItem item) {
        this.item = item;
    }

    /**
     * Invoke the invocation handler for the given proxy object,
     * method, and arguments. In order to execute the passed
     * method, this method does the following: - if the method
     * has a <code>RDF</code> annotation or if it is a setter and
     * the corresponding getter has a <code>RDF</code>
     * annotation, we try to retrieve the appropriate value by
     * querying the triple store and converting the triple store
     * data to the return type of the method; if the return type
     * is again an interface
     *
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[])
     * @see RDF
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // caching
        final String fieldName = method.getName().startsWith("get") || method.getName().startsWith("set") ?
                method.getName().substring(3) : method.getName();
        // TODO: It will be possible to use it when all ContentItems run over Proxy
//		if(useCache && method.getName().equals("setTextContent")) {
//			if(proxy instanceof ContentItem && args.length == 1 && args[0] instanceof TextContent) {
//				ContentItem item = (ContentItem) proxy;
//				TextContent content = (TextContent) args[0];
//				if(item == null || !content.getXmlString()
//						.equals(item.getTextContent().getXmlString())) {
//					UpdateTextContentService utcs = (UpdateTextContentService)
//							Component.getInstance("updateTextContentService");
//					if(content.getXmlString() != null) {
//						utcs.updateTextContent(item, content.getXmlString());
//				    	item.setModified(content.getCreated());
//				        item.addToAllTextContents(content);
//					}
//				}
//			}
//		}

        if(useCache && method.getName().startsWith("get")) {
            if(fieldCache.get(fieldName) != null) {
                return fieldCache.get(fieldName);
            }
        }

        if(fieldName.equals("hashCode")) {
            return item.hashCode();
        } else if(fieldName.equals("equals")) {
            final Object other = args[0];
            if(other !=null && other.getClass().equals(proxy.getClass()) && other.hashCode() == proxy.hashCode()) {
                return true;
            } else {
                return false;
            }
        } else if(fieldName.equals("toString")) {
            return "facade with delegate to "+item.toString();
        } else if(fieldName.equals("Delegate")) {
            return item;
        }

        String[] rdf_property = null;
        boolean inverse = false;

        // look for RDF annotation and extract the property from it; if not on the getter, look
        // for the corresponding setter and check whether it has a @RDF annotation; if neither has,
        // throw an IllegalArgumentException
        if(method.getAnnotation(RDF.class) != null) {
            rdf_property = method.getAnnotation(RDF.class).value();
        } else if(method.getAnnotation(RDFInverse.class) != null) {
            rdf_property = method.getAnnotation(RDFInverse.class).value();
            inverse = true;
        } else {
            try {
                final Method dualMethod = getCorrespondingGetterOrSetter(method);

                if(dualMethod.getAnnotation(RDF.class) != null) {
                    rdf_property = dualMethod.getAnnotation(RDF.class).value();
                } else if(dualMethod.getAnnotation(RDFInverse.class) != null) {
                    rdf_property = dualMethod.getAnnotation(RDFInverse.class).value();
                    inverse = true;
                }
            } catch(final NoSuchMethodException ex) {

            }

            // *** Delegation to Content Item ***

            if(rdf_property == null) {
                // no RDF property; in this case we check whether the declaring interface extends ContentItemI and
                // whether ContentItemI has a method we can delegate to

                final Class declaringInterface = method.getDeclaringClass();
                final Class contentItemI = ContentItemI.class;
                if(contentItemI.isAssignableFrom(declaringInterface)) {
                    // declaring interface is a ContentItemI, check whether ContentItemI has a method with the same
                    // signature
                    try {
                        final Method ci_method = contentItemI.getMethod(method.getName(), method.getParameterTypes());

                        // method found (no exception thrown), invoke it on the delegate item
                        return ci_method.invoke(item,args);
                    } catch(final NoSuchMethodException ex) {
                        throw new NoSuchMethodException("the called method "+method.getName()+" is neither a getter/setter annotated with @RDF nor does it delegate to a ContentItem method of the same name");
                    }
                } else {
                    throw new NoSuchMethodException("the called method "+method.getName()+" is neither a getter/setter annotated with @RDF nor does the interface implement ContentItemI");
                }
            }

        }



        // distinguish getters and setters
        if(method.getName().startsWith("get") && method.getParameterTypes().length == 0) {

            // *** get the value of a certain RDF property ***

            final Class<?> returnType   = method.getReturnType();
            final Type typeOfGeneric = method.getGenericReturnType();

            // we believe that the result is universal for each property 
            // and therefore just return the result for the firstly defined property
            final Object result = FacadeUtils.transform(returnType, typeOfGeneric, item, rdf_property[0], inverse, (KiWiEntityManager)Component.getInstance("kiwiEntityManager"));

            if(useCache) {
                fieldCache.put(fieldName,result);
            }
            return result;


        } else if(method.getName().startsWith("set") && method.getParameterTypes().length == 1) {

            // add to cache
            if(useCache) {
                fieldCache.put(fieldName,args[0]);
            }

            // *** set the value of a certain RDF property

            final Class paramType = method.getParameterTypes()[0];

            
            if( FacadeUtils.isBaseType(paramType) && !inverse) {
            	for(String v : rdf_property) {
	            	if(args[0] == null || "".equals(args[0])) {
	               	 	item.getResource().setProperty("<"+v+">", null);
	            	} else {
	            		item.getResource().setProperty("<"+v+">", 
	            										FacadeUtils.transformFromBaseType(args[0]));
	            	}
            	}
            } else if (args[0] == null ) {
            	// if argument is not a base type, and value is null, simply remove the relation
            	for(String v : rdf_property) {
		            final KiWiUriResource prop = tripleStore.createUriResource(v);
		            
		            // first remove all properties prop that have this subject;
		            for(final KiWiNode n : item.getResource().listOutgoingNodes("<" + v + ">")) {
		                tripleStore.removeTriple(item.getResource(), prop, n);
		            }
            	}
            } else if (FacadeUtils.isKiWiEntity(paramType) && !inverse) {
            	for(String v : rdf_property) {
	                final KiWiUriResource prop = tripleStore.createUriResource(v);
	                // first remove all properties prop that have this subject;
	                for(final KiWiNode n : item.getResource().listOutgoingNodes("<" + v + ">")) {
	                    tripleStore.removeTriple(item.getResource(), prop, n);
	                }
	                // then create a new triple for this property, subject, and object
	                tripleStore.createTriple(item.getResource(), prop, ((KiWiEntity)args[0]).getResource());
            	}
            } else if (FacadeUtils.isKiWiEntity(paramType) && inverse) {
            	for(String v : rdf_property) {
	                final KiWiUriResource prop = tripleStore.createUriResource(v);
	                // first remove all properties prop that have this subject;
	                for(final KiWiNode n : item.getResource().listIncomingNodes("<" + v + ">")) {
	                    tripleStore.removeTriple(item.getResource(), prop, n);
	                }
	                // then create a new triple for this property, subject, and object
	                tripleStore.createTriple(((KiWiEntity)args[0]).getResource(), prop, item.getResource());
            	}
            } else if (FacadeUtils.isCollection(paramType)) {
            	for(String v : rdf_property) {
	                final Collection c = (Collection)args[0];
	                final KiWiUriResource prop = tripleStore.createUriResource(v);
	
	                // first remove all properties prop that have this subject;
	                for(final KiWiNode n : item.getResource().listOutgoingNodes("<" + v + ">")) {
	                    tripleStore.removeTriple(item.getResource(), prop, n);
	                }
	
	                // then add each of the elements in the collection as new triple with prop
	                for (final Object o : c) {
	                    if ( (FacadeUtils.isPrimitive(o)
	                            || String.class.equals(o.getClass())) && !inverse) {
	                        final KiWiLiteral l = tripleStore.createLiteral(o);
	                        tripleStore.createTriple(item.getResource(), prop, l);
	                    } else if (FacadeUtils.isKiWiEntity(o.getClass()) && !inverse) {
	                        tripleStore.createTriple(item.getResource(), prop, ((KiWiEntity) o).getResource());
	                    } else if (FacadeUtils.isKiWiEntity(o.getClass()) && inverse) {
	                        tripleStore.createTriple(((KiWiEntity) o).getResource(), prop,item.getResource());
	                    } else if(inverse) {
	                        throw new IllegalArgumentException("method "+method.getName()+": @RDFInverse not supported for parameter type "+paramType.getName());
	                    } else {
	                        final Log log = Logging.getLog(KiWiInvocationHandler.class);
	                        log.error("the type " + o.getClass().getName() + " is not supported in collections");
	                        log.error("listing supertypes of "+ o.getClass().getName());
	                        log.error("superclass: #0",o.getClass().getSuperclass().getCanonicalName());
	                        for(final Class iface : o.getClass().getInterfaces()) {
	                            log.error("interface: #0",iface.getCanonicalName());
	                        }
	
	                        throw new IllegalArgumentException("the type "
	                                + o.getClass().getName()
	                                + " is not supported in collections");
	                    }
	                }
            	}
            } else if(inverse){
                throw new IllegalArgumentException("method "+method.getName()+": @RDFInverse not supported for parameter type "+paramType.getName());
            } else {
                throw new IllegalArgumentException("method "+method.getName()+": unsupported parameter type "+paramType.getName());
            }

            return null;

        } else {
            throw new IllegalArgumentException("only getter and setter methods are supported by the TripleStoreInvocationHandler");
        }

    }

    /**
     * Return the dually corresponding getter/setter to the passed setter/getter
     *
     * @param method
     * @return
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     */
    private Method getCorrespondingGetterOrSetter(Method method) throws NoSuchMethodException {
        final Class cls = method.getDeclaringClass();

        String  methodName = null;
        Class[] paramTypes = null;
        if(method.getName().startsWith("get")) {
            methodName = "set" + method.getName().substring(3);
            paramTypes = new Class[1];
            paramTypes[0] = method.getReturnType();
        } else if(method.getName().startsWith("set")) {
            methodName = "get" + method.getName().substring(3);
            paramTypes = new Class[0];
        } else {
            throw new NoSuchMethodException("the passed method was not a getter or setter");
        }
        return cls.getMethod(methodName, paramTypes);
    }
}
