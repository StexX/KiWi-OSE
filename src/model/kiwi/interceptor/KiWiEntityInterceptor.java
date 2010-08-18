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
package kiwi.interceptor;

import java.lang.reflect.Method;

import kiwi.model.kbase.KiWiEntity;

/**
 * 
 * 
 * @author Sebastian Schaffert
 *
 */
public interface KiWiEntityInterceptor {

	/**
	 * The before() method of a KiWiEntityInterceptor is called immediately before a method 
	 * annotated with @KiWiInterceptor(IMPLEMENTING_CLASS) is executed. It takes as argument the 
	 * KiWi entity that is currently under interception, the java.lang.reflect.Method that has been 
	 * called, and the arguments that have been passed to the method.
	 * 
	 * @param entity
	 * @param method
	 * @param args
	 */
	public void before(KiWiEntity entity, Method method, Object[] args);
	
	/**
	 * The after() method of a KiWiEntityInterceptor is called immediately after a method annotated
	 * with @KiWiInterceptor(IMPLEMENTING_CLASS) is successfully executed. It takes as argument the 
	 * KiWi entity that is currently under interception, the java.lang.reflect.Method that has been 
	 * called, and the arguments that have been passed to the method.
	 * 
	 * @param entity
	 * @param method
	 * @param args
	 */
	public void after(KiWiEntity entity, Method method, Object[] args);
}
