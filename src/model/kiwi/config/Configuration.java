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

package kiwi.config;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

/**
 * Hold a single configuration item, which might either have a list value or a single string value
 * 
 * @author Sebastian Schaffert
 *
 */
@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@BatchSize(size = 20)
@NamedQueries({
	@NamedQuery( name="configuration.byKey", 
			     query="select c from kiwi.config.Configuration c left outer join fetch c.listValue where c.kiwikey = :key")
})
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("S")
public class Configuration {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)    
	private Long id;
	
	/**
	 * The key of this configuration item. Must be unique and cannot be null.
	 */
	@Column(unique=true, nullable=false)
	@Index(name="idx_configuration_key")
	private String kiwikey;
	
	/**
	 * The list of values for this configuration key.
	 * 
	 * Fetched in EAGER mode, because it will be relevant in almost all situations
	 */
	@CollectionOfElements(fetch=FetchType.EAGER)
	private List<String> listValue;
	
	@Version
	private Long version;
	
	public Configuration(){
		
	}
	
	public Configuration(String kiwikey) {
		super();
		this.kiwikey = kiwikey;
		this.listValue = new LinkedList<String>();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the key
	 */
	public String getKiwikey() {
		return kiwikey;
	}

	/**
	 * @param key the key to set
	 */
	public void setKiwikey(String kiwikey) {
		this.kiwikey = kiwikey;
	}

	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		if(listValue != null && listValue.size() > 0) {
			return listValue.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @param stringValue the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		listValue.clear();
		listValue.add(stringValue);
	}

	/**
	 * Return the string value of this configuration as boolean.
	 * 
	 * @return the boolean value of this configuration, parsed using Boolean.getBoolean()
	 */
	public boolean getBooleanValue() {
		String s = getStringValue();
		if(s != null) {
			return Boolean.valueOf(s);
		} else {
			return false;
		}
	}
	
	public void setBooleanValue(boolean boolValue) {
		setStringValue(String.valueOf(boolValue));
	}
	
	/**
	 * Return the string value of this configuration as integer.
	 * 
	 * @return the int value of this configuration, parsed using Integer.parseInt()
	 */
	public int getIntValue() {
		String s = getStringValue();
		if(s != null) {
			return Integer.parseInt(s);
		} else {
			return 0;
		}
	}
	
	/**
	 * Set the value of this configuration to an integer.
	 * 
	 * @param i
	 */
	public void setIntValue(int i) {
		setStringValue(i+"");		
	}
	
	public double getDoubleValue() {
		String s = getStringValue();
		if(s != null) {
			return Double.parseDouble(s);
		} else {
			return 0;
		}
		
	}
	
	public void setDoubleValue(double d) {
		setStringValue(String.valueOf(d));		
	}
	
	/**
	 * @return the listValue
	 */
	public List<String> getListValue() {
		if(listValue == null) {
			this.listValue = new LinkedList<String>();
		}
		return listValue;

	}

	/**
	 * @param listValue the listValue to set
	 */
	public void setListValue(List<String> listValue) {
		this.listValue = listValue;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}
	
}
