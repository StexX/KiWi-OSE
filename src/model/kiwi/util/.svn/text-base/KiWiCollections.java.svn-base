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

package kiwi.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This class contains static helper methods for supporting java collections
 * in Java 5.
 *
 * @author Sebastian Schaffert
 */
public class KiWiCollections {

    /**
     * Convert any iterable into a list
     * @param <T>
     * @param iterable
     * @return
     */
    public static <T> List<T> toList(Iterable<T> iterable) {
        return (List<T>) toCollection(LinkedList.class,iterable);
    }
    
    /**
     * Convert any iterable into a set
     * @param <T>
     * @param iterable
     * @return
     */
    public static <T> Set<T> toSet(Iterable<T> iterable) {
        return (Set<T>) toCollection(HashSet.class,iterable);
    }
    
    private static <C extends Collection<T>,T> C toCollection(Class<C> cls, Iterable<T> iterable) {
        try {
            C result = cls.newInstance();

            for(T item : iterable) {
                result.add(item);
            }
             
            return result;
        } catch(InstantiationException ex) {
            return null;
        } catch(IllegalAccessException ex) {
            return null;
        }
    }
    
    public static <T> T first(Iterable<T> iterable) {
        return iterable.iterator().next();
    }
}
