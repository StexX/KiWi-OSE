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
package kiwi.webservice;

/**
 * This is a simple helper class, a java bean or pojo, representing 
 * one structured data unit's value with it's name. 
 * @author Karsten
 */
public class DataStructured {
	private String name;
	private String value;
	private boolean foreignKey = false;	// set to true, if this is a foreign key information
	
	
	/**
	 * Empty constructor
	 */
	public DataStructured() {
		super();
	}

	/**
	 * Constructor initialising the complete structured data unit.
	 * @param name
	 * @param value
	 */
	public DataStructured(String name, String value) {
		this();
		this.name = name;
		this.value = value;
	}

	
	/**
	 * Fully qualified Constructor.
	 * @param name
	 * @param value
	 * @param foreignKey
	 */
	public DataStructured(String name, String value, boolean foreignKey) {
		super();
		this.name = name;
		this.value = value;
		this.foreignKey = foreignKey;
	}

	/**
	 * This method returns the Structured Data's name.
	 * @return 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * This method sets the Structured Data's name.
	 * @param name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * This method returns the Structured Data's value.
	 * @return
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * This method sets the Structured Data's value.
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the foreignKey
	 */
	public boolean isForeignKey() {
		return foreignKey;
	}

	/**
	 * @param foreignKey the foreignKey to set
	 */
	public void setForeignKey(boolean foreignKey) {
		this.foreignKey = foreignKey;
	}
}
