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

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.user.UserService;
import kiwi.exception.NamespaceResolvingException;
import kiwi.exception.NonUniqueRelationException;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.content.Content;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.util.KiWiFormatUtils;

import org.jboss.seam.Component;

/**
 * @author Sebastian Schaffert
 *
 */
public class FacadeUtils {

	/**
	 * Check whether a type is a KiWi Facade, i.e. the type or one of its superclasses has
	 * the @KiWiFacade annotation.
	 *
	 * @param <C>
	 * @param clazz
	 * @return
	 */
	public static <C> boolean isKiWiFacade(Class<C> clazz) {
		if (clazz.isAnnotationPresent(KiWiFacade.class)) {
			return true;
		} else {
			for(final Class iface : clazz.getInterfaces()) {
				if(iface.isAnnotationPresent(KiWiFacade.class)) {
					return true;
				}
			}
			if (clazz.getSuperclass() != null) {
				return isKiWiFacade(clazz.getSuperclass());
			}
			return false;
		}
	}

	/**
	 * Check whether a type is a KiWi Facade, i.e. the type or one of its superclasses has
	 * the @KiWiFacade annotation.
	 *
	 * @param <C>
	 * @param clazz
	 * @return
	 */
	public static <C> boolean isFacadeAnnotationPresent(Class<C> clazz, Class<? extends Annotation> annotation) {
		if (clazz.isAnnotationPresent(annotation)) {
			return true;
		} else {
			for(final Class iface : clazz.getInterfaces()) {
				if(iface.isAnnotationPresent(annotation)) {
					return true;
				}
			}
			if (clazz.getSuperclass() != null) {
				return isFacadeAnnotationPresent(clazz.getSuperclass(),annotation);
			}
			return false;
		}
	}


	public static <C extends Annotation,D> C getFacadeAnnotation(Class<D> clazz, Class<C> annotation) {
		if (clazz.isAnnotationPresent(annotation)) {
			return clazz.getAnnotation(annotation);
		} else {
			for(final Class iface : clazz.getInterfaces()) {
				if(iface.isAnnotationPresent(annotation)) {
					return (C) iface.getAnnotation(annotation);
				}
			}
			if (clazz.getSuperclass() != null) {
				return getFacadeAnnotation(clazz.getSuperclass(),annotation);
			}
			return null;
		}

	}

    /**
     * Returns true if the <code>clazz</code> argument is a
     * KiWiEntity, otherwise it returns false.
     *
     * @param <C> the type of the class modeled by the
     *            <code>clazz</code> argument Class object. For
     *            example, the type of String.class is
     *            Class<String>
     * @param clazz the argument to test.
     * @return true if the <code>clazz</code> argument is a KiWi
     *         entity.
     */
    public static <C> boolean isKiWiEntity(Class<C> clazz) {

        if (clazz == null) {
            return false;
        }

        final Class<?>[] interfaces = clazz.getInterfaces();
        if (Arrays.asList(interfaces).contains(KiWiEntity.class)) {
            return true;
        }

        if (clazz.getSuperclass() != null && isKiWiEntity(clazz.getSuperclass())) {
        	return true;
        }  
        	
        if(interfaces.length > 0) {
        	// hierarchy of interfaces, check each superinterface individually
        	for(Class<?> c : interfaces) {
        		if(isKiWiEntity(c)) {
        			return true;
        		}
        	}
        	
        }

        return false;
    }

    /**
     * Returns true if the <code>clazz</code> argument is a
     * KiWiEntity, otherwise it returns false.
     *
     * @param in the argument to test.
     * @return true if the <code>clazz</code> argument is a KiWi
     *         entity.
     */
    public static boolean isKiWiEntity(Object in) {

        if (in == null) {
            return false;
        }

        final Class<?> clazz = in.getClass();
        final boolean result = isKiWiEntity(clazz);

        return result;
    }

    /**
     * Check whether a type is a collection (List, Set, ...).
     *
     * @param <C> the type of the class modeled by the
     *            <code>clazz</code> argument Class object. For
     *            example, the type of String.class is Class
     *            &lt;String&gt;
     * @param clazz the type to test.
     * @return true if the type to test is a a type is a
     *         collection (List, Set, ...).
     */
    public static <C> boolean isCollection(Class<C> clazz) {

        if (clazz == null) {
            return false;
        }

        final Class<?>[] interfaces = clazz.getInterfaces();

        if (Arrays.asList(interfaces).contains(Collection.class)) {
            return true;
        }

        if (clazz.getSuperclass() != null) {
            return isCollection(clazz.getSuperclass());
        }

        return false;
    }

    /**
     * Check whether a type is a KiWi Content.
     *
     * @param <C> the type of the class modeled by the
     *            <code>clazz</code> argument Class object. For
     *            example, the type of String.class is Class
     *            &lt;String&gt.
     * @param clazz the type to test.
     * @return true if the type to test is a KiWi Content.
     */
    public static <C> boolean isContent(Class<C> clazz) {

        if (Arrays.asList(clazz.getInterfaces()).contains(Content.class)) {
            return true;
        }

        if (clazz.getSuperclass() != null) {
            return isContent(clazz.getSuperclass());
        }

        return false;
    }

    /**
     * Returns true if the <code>clazz</code> argument is a:
     * <ul>
     * <li>a primitive
     * <li>a primitive wrapper
     * <li>a java.lang.Localle class
     * <li>a java.lang.Date class
     * <li>a java.lang.String class
     * </ul>
     * otherwise it returns false.
     *
     * @param <C> the type of the class modeled by the
     *            <code>clazz</code> argument Class object. For
     *            example, the type of String.class is Class
     *            &lt;String&gt.
     * @param clazz the argument to test.
     * @return true if the <code>clazz</code> argument is a
     *         primitive, primitive wrapper, locale, date or
     *         String.
     */
    public static <C> boolean isBaseType(Class<C> clazz) {

        if (clazz == null) {
            return false;
        }

        final boolean isPrimitive = clazz.isPrimitive();
        if (isPrimitive) {
            return true;
        }

        // if I compare the Locale.class with the clazz argument
        // I can avoid the infamous case when the clazz is null,
        // the Locale.class.equals(null) is false, always - at
        // least this sustains the theory. The same logic for
        // the other equals realtions.
        final boolean isLocale = Locale.class.equals(clazz);
        if (isLocale) {
            return true;
        }

        final boolean isDate = Date.class.equals(clazz);
        if (isDate) {
            return true;
        }

        final boolean isString = String.class.equals(clazz);
        if (isString) {
            return true;
        }

        final boolean isBoolean = Boolean.class.equals(clazz);
        if (isBoolean) {
            return true;
        }

        final Class<? super C> superClass = clazz.getSuperclass();
        final boolean isNumber = Number.class.equals(superClass);
        if (isNumber) {
            return true;
        }

        // even if the char is a primitive is not a number
        final boolean isCharacter = Character.class.equals(clazz);
        if (isCharacter) {
            return true;
        }

        return false;
    }

    /**
     * Returns true if the <code>clazz</code> argument is a:
     * <ul>
     * <li>a primitive
     * <li>a primitive wrapper
     * </ul>
     * otherwise it returns false.
     *
     * @param <C> the type of the class modeled by the
     *            <code>clazz</code> argument Class object. For
     *            example, the type of String.class is Class
     *            &lt;String&gt.
     * @param clazz the argument to test.
     * @return true if the <code>clazz</code> argument is a
     *         primitive or primitive wrapper.
     */
    public static <C> boolean isPrimitive(Class<C> clazz) {

        if (clazz == null) {
            return false;
        }

        final boolean isPrimitive = clazz.isPrimitive();
        if (isPrimitive) {
            return true;
        }

        // even if the char is a primitive is not a number
        final boolean isCharacter = Character.class.equals(clazz);
        if (isCharacter) {
            return true;
        }

        final Class<? super C> superClass = clazz.getSuperclass();
        final boolean isNumber = Number.class.equals(superClass);
        if (isNumber) {
            return true;
        }

        return false;
    }

    /**
     * Returns true if the <code>in</code> argument is a:
     * <ul>
     * <li>a primitive
     * <li>a primitive wrapper
     * </ul>
     * otherwise it returns false.
     *
     * @param in the argument to test.
     * @return true if the <code>clazz</code> argument is a
     *         primitive or primitive wrapper.
     */
    public static boolean isPrimitive(Object in) {
        if (in == null) {
            return false;
        }

        final Class<?> clazz = in.getClass();
        return isPrimitive(clazz);
    }


	/**
	 * Transform a value passed as string to the base type (i.e. non-complex type) given as argument
	 *
	 * @param <T>
	 * @param value
	 * @param returnType
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static <T> T transformToBaseType(String value, Class<T> returnType) throws IllegalArgumentException {
		// transformation to appropriate primitive type
		if(Integer.class.equals(returnType) || int.class.equals(returnType)) {
			if(value == null) {
                return (T)(Integer)0;
            }
			return (T)(Integer)Integer.parseInt(value);
		} else if(Long.class.equals(returnType) || long.class.equals(returnType)) {
			if(value == null) {
                return (T)(Long)0L;
            }
			return (T)(Long)Long.parseLong(value);
		} else if(Double.class.equals(returnType) || double.class.equals(returnType)) {
			if(value == null) {
                return (T)(Double)0.0;
            }
			return (T)(Double)Double.parseDouble(value);
		} else if(Float.class.equals(returnType) || float.class.equals(returnType)) {
			if(value == null) {
                return (T)(Float)0.0F;
            }
			return(T)(Float)Float.parseFloat(value);
		} else if(Byte.class.equals(returnType) || byte.class.equals(returnType)) {
			if(value == null) {
                return (T)(Byte)(byte)0;
            }
			return (T)(Byte)Byte.parseByte(value);
		} else if(Boolean.class.equals(returnType) || boolean.class.equals(returnType)) {
			return (T)(Boolean)Boolean.parseBoolean(value);
		} else if(Character.class.equals(returnType) || char.class.equals(returnType)) {
			if(value == null) {
				return null;
			} else if(value.length() > 0) {
				return (T)(Character)value.charAt(0);
			} else {
				return null;
			}
		} else if (Locale.class.equals(returnType)) {
			return (T) new Locale(value);
		} else if (Date.class.equals(returnType)) {
			if(value == null) {
				return null;
			} else {
				try {
					return (T) KiWiFormatUtils.ISO8601FORMAT.parse(value);
				} catch (final ParseException e) {
					e.printStackTrace();
					return null;
				}
			}
		} else if(String.class.equals(returnType)) {
			return (T)value;
		} else {
			throw new IllegalArgumentException("primitive type "+returnType.getName()+" not supported by transformation");
		}
	}

	/**
	 * Transform a value passed as string to the base type (i.e. non-complex type) given as argument
	 *
	 * @param <T>
	 * @param value
	 * @param returnType
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static <T> String transformFromBaseType(T value) throws IllegalArgumentException {
		// transformation to appropriate primitive type
		if(value == null) {
			return null;
		} else if(Integer.class.equals(value.getClass()) || int.class.equals(value.getClass())) {
			return value.toString();
		} else if(Long.class.equals(value.getClass()) || long.class.equals(value.getClass())) {
			return value.toString();
		} else if(Double.class.equals(value.getClass()) || double.class.equals(value.getClass())) {
			return value.toString();
		} else if(Float.class.equals(value.getClass()) || float.class.equals(value.getClass())) {
			return value.toString();
		} else if(Byte.class.equals(value.getClass()) || byte.class.equals(value.getClass())) {
			return value.toString();
		} else if(Boolean.class.equals(value.getClass()) || boolean.class.equals(value.getClass())) {
			return value.toString();
		} else if(Character.class.equals(value.getClass()) || char.class.equals(value.getClass())) {
			return value.toString();
		} else if (Locale.class.equals(value.getClass())) {
			return value.toString();
		} else if (Date.class.equals(value.getClass())) {
			return KiWiFormatUtils.ISO8601FORMAT.format(value);
		} else if(String.class.equals(value.getClass())) {
			return value.toString();
		} else {
			throw new IllegalArgumentException("primitive type "+value.getClass().getName()+" not supported by transformation");
		}
	}
	
	
    /**
     * Helper method to transform the object reachable via
     * rdf_property from r to the given returnType; if the
     * returnType is a collection, it is also necessary to
     * provide the generic type. The KiWiEntityManager is used
     * for further querying.<br>
     * Please note that if the <code>returnType</code>is a
     * collection you <b>must</b> use a concrete class (e.g.
     * <code>java.util.ArrayList</code>) not an abstract class or
     * interface.
     *
     * @param <C>
     * @param returnType
     * @param typeOfGeneric
     * @param r
     * @param rdf_property
     * @param km
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NonUniqueRelationException
     */
	@SuppressWarnings("unchecked")
	public static <C,D extends KiWiEntity> C transform(Class<C> returnType, Type typeOfGeneric, KiWiEntity entity, String rdf_property, boolean inverse, KiWiEntityManager km) throws InstantiationException, IllegalAccessException, NonUniqueRelationException {
		// should not happen actually
		if(entity == null || entity.getResource() == null) {
			return null;
		}
		
		if( isBaseType(returnType) && !inverse) {
			// if the return type is string or primitive, get the literal value of the property and
			// transform it appropriately
			final String value = entity.getResource().getProperty("<"+rdf_property+">");

			try {
				// transformation to appropriate primitive type
				final C result = transformToBaseType(value, returnType);

				return result;
			} catch(final IllegalArgumentException ex) {
				return null;
			}
		} else if(isKiWiEntity(returnType) && !inverse) {
			// for KiWi entities, we retrieve the resource that is targeted by this property (by using getObject)
			// and create a query on the triple store using createQuery() and the resource's uri that returns the
			// result in the appropriate type (can e.g. be again a proxy using this invocation handler!)


			// TODO: implement createNamedQuery in KiWiEntityManager
			EntityManager em = (EntityManager)Component.getInstance("entityManager");
			final Query q = em.createNamedQuery("tripleStore.objectBySubjectProperty");
			q.setParameter("subj", entity.getResource());
			q.setParameter("prop_uri", rdf_property);
			q.setHint("org.hibernate.cacheable", true);
			q.setMaxResults(1);
			

			try {
				final ContentItem result = (ContentItem)q.getSingleResult();
				if(returnType.equals(ContentItem.class)) {
					return (C)result;
				} else if(User.class.equals(returnType) ) {
					// fetch user by the URI of the object
					UserService us = (UserService)Component.getInstance("userService");
					
					return (C)us.getUserByUri(((KiWiUriResource)result.getResource()).getUri());
				} else {
					// facade
					return (C)km.createFacade(result, (Class<D>)returnType);
				}
			} catch(NoResultException ex) {
				return null;
			}

		} else if(isKiWiEntity(returnType) && inverse) {
			// for KiWi entities, we retrieve the resource that is targeted by this property (by using getObject)
			// and create a query on the triple store using createQuery() and the resource's uri that returns the
			// result in the appropriate type (can e.g. be again a proxy using this invocation handler!)

	        final String query = "SELECT ?s WHERE { ?s :prop :obj }";

	        final Query q = km.createQuery(query, KiWiQueryLanguage.SPARQL, (Class<? extends KiWiEntity>)returnType);
	        q.setParameter("obj",entity.getResource().getSeRQLID());
	        q.setParameter("prop", "<"+rdf_property+">");
	        q.setMaxResults(1);

	        final C result = (C)q.getSingleResult();
			return result;

		} else if(TextContent.class.equals(returnType) && entity instanceof ContentItem && !inverse) {
			// for KiWi TextContent objects, we read the literal value as string and then call setXmlString
			// as a converter
			final ContentItem item = (ContentItem) entity;
			final TextContent tc = new TextContent(item);
			item.setTextContent(tc);
			final String value = entity.getResource().getProperty("<" + rdf_property + ">");
			//final KiWiLiteral l = entity.getResource().getLiteral("<" + rdf_property + ">");
			tc.setXmlString(value);
			return (C)tc;

		} else if(isCollection(returnType)) {
			// if we have a collection, we try to infer the generic type of its contents and
			// use this to generate values; if the generic type is a kiwi entity, we issue
			// a createQuery to the tripleStore to retrieve the corresponding values; if the
			// generic type is a base type, we transform the results to the base type and query for
			// literals
			if (typeOfGeneric instanceof ParameterizedType) {
				final ParameterizedType t = (ParameterizedType) typeOfGeneric;
				final Class tCls = (Class) t.getActualTypeArguments()[0];
				if (isKiWiEntity(tCls) && !inverse) {

					
					// TODO: implement createNamedQuery in KiWiEntityManager
					EntityManager em = (EntityManager)Component.getInstance("entityManager");
					final Query q = em.createNamedQuery("tripleStore.objectBySubjectProperty");
					q.setParameter("subj", entity.getResource());
					q.setParameter("prop_uri", rdf_property);
	    			q.setHint("org.hibernate.cacheable", true);

			        final Collection result = (Collection) returnType.newInstance();
    				List<ContentItem> myResult = q.getResultList();
    				Set<ContentItem> dupSet = new LinkedHashSet<ContentItem>(myResult);
    				if(tCls.equals(ContentItem.class)) {
    					result.addAll(dupSet);
    				} else if(User.class.equals(returnType) ) {
    					// fetch user by the URI of the object
    					UserService us = (UserService)Component.getInstance("userService");
    					
    					for(ContentItem c : dupSet) {
    						result.add((C)us.getUserByUri(((KiWiUriResource)c.getResource()).getUri()));
    					}
    				} else {
    					result.addAll(km.createFacadeList(dupSet, tCls));
    				}
			        return (C) result;
	    			
				} else if (isKiWiEntity(tCls) && inverse) {

						// TODO: change to HQL query (performance)
				        final String query = "SELECT ?s WHERE { ?s :prop :obj }";

				        final Query q = km.createQuery(query, KiWiQueryLanguage.SPARQL, tCls);
				        q.setParameter("obj",entity.getResource().getSeRQLID());
				        q.setParameter("prop", "<"+rdf_property+">");

				        final Collection result = (Collection) returnType.newInstance();
				        result.addAll(q.getResultList());
				        return (C) result;
				} else if(inverse) {
					throw new IllegalArgumentException("@RDFInverse not supported for mappings of type "+rdf_property);
				} else if( isBaseType(tCls) ) {
                    // FIXME: you can not use interfaces,
                    // you must only use concrete classes.
                    final Collection result =
                            (Collection) returnType.newInstance();
                    final KiWiResource resource = entity.getResource();
                    if (resource == null) {
                        // the KiWiEntity must have a
                        // KiWiResource!
                        final String msg = "The specified KiWiEntity has no KiWiResource.";
                        throw new NullPointerException(msg);
                    }

                    Iterable<String> properties;
					try {
						properties = resource.getProperties("<" + rdf_property + ">");
					} catch (NamespaceResolvingException e) {
						e.printStackTrace();
						properties = Collections.emptySet();
					}
                    for (final String s : properties) {
                        result.add(transformToBaseType(s, tCls));
                    }

                    return (C) result;
                } else {
					throw new IllegalArgumentException("return type is using generic type "+tCls.getName()+", which is not supported in RDF-based collections; please use either Java primitive types or KiWi Entities in KiWiFacades");
				}
			} else {
				throw new IllegalArgumentException("return type is unparametrized collection type "+returnType.getName()+", which is not supported; please use an explicit type parameter in KiWiFacades");
			}
		} else if(inverse) {
			throw new IllegalArgumentException("@RDFInverse not supported for mappings of type "+rdf_property);			
		} else {
			throw new IllegalArgumentException("unsupported return type "+returnType.getName());
		}

	}
}
