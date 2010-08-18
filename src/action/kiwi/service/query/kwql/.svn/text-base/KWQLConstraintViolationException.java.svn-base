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
package kiwi.service.query.kwql;
import java.util.ArrayList;

/**
 * 
 * @author Klara Weiand
 *
 */
public class KWQLConstraintViolationException extends Exception {
	private static final long serialVersionUID = 1L;
	char type;
	ArrayList<String> cause;
	
	/**
	 * Instantiates a new kWQL constraint violation exception.
	 * 
	 * @param type the type of the exception
	 * @param cause the cause of the exception
	 */
	public KWQLConstraintViolationException(char type, ArrayList<String> cause)
		{
		this.type=type;
		this.cause=cause;

		}
	
	/** (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	public String getMessage()
		{
		switch (type)
			{
		case 'b': return "Bracketing incorrect near token '"+ cause.get(0)+"'";
		case 'n': return "Qualifier '"+ cause.get(0) + "' may only be defined once within one resource";
		case 'q': String qualifier=cause.get(cause.size()-1);cause.remove(cause.size()-1); return "Resource '"+ qualifier+ "' has no qualifier(s) "+cause.toString();
		case 'r': return "'"+cause.get(0)+"' cannot be nested inside '"+ cause.get(1)+"'";
		case 'v': return "Variable(s) "+cause.toString()+" occur(s) in head but may not be bound in body";
		case 'w': return "'"+cause.get(0)+"' may not have a value of type RESOURCE";

			}
		return null;
		}
}
