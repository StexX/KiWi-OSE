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
package kiwi.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that a certain KiWi facade field should be mapped inversely to a
 * property in the triple store. It is the inverse of the @RDF annotation, e.g. when using
 * @RDFInverse("rdfs:subClassOf"), the annotated method returns the subclasses, while the 
 * annotation @RDF("rdfs:subClassOf") would return the superclasses. Note that @RDFInverse only
 * works on ObjectProperties; for all other properties it behaves exactly like @RDF
 * <p>
 * The KiWiEntityManager and TripleStore check for the presence of this annotation on methods and
 * dynamically maps them to queries on the triple store, using the resource of the annotated
 * interface or class (which must implement KiWiEntity to provide a getResource() method) as 
 * object.
 * <p>
 * This is a runtime annotation and it is applicable on getter methods.<br>
 * <p>
 * TODO: currently, only KiWiFacades are supported; also, it is currently not possible to provide
 *       @RDF and @RDFInverse on the same method at the same time.
 *
 * @author Sebastian Schaffert
 * @see kiwi.model.kbase.KiWiEntity
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD})
public @interface RDFInverse {

    /**
     * Return the URI of the RDF predicate to use for the field
     * or method.
     *
     * @returns URI of the RDF predicate to use for the field or
     *          method.
     */
    String[] value();
}
