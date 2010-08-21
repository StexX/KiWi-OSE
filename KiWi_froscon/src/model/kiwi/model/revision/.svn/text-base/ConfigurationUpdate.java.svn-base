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
package kiwi.model.revision;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import kiwi.model.kbase.KiWiNamespace;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

/**
 * TODO: install COnfigurationUpdate correctly...
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Entity
@Immutable
public class ConfigurationUpdate implements KiWiUpdate {

	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Id
	private Long id;
	
	@GenericGenerator(name="gg3",strategy="hilo")
	@ManyToMany
	@JoinTable(name = "removedNamespace_metadataUpdate",
    		joinColumns = { @JoinColumn(name = "metadataUpdate_id") },
    		inverseJoinColumns = { @JoinColumn(name = "removedNamespaces_id") } )
    @CollectionId(
    		columns = @Column(name="removedNS_MDU_id"),
    		type = @Type(type="long"),
    		generator = "gg3")
	private Collection<KiWiNamespace> removedNamespaces;
	
	@GenericGenerator(name="gg4",strategy="hilo")
	@ManyToMany
	@JoinTable(name = "addedNamespace_metadataUpdate",
    		joinColumns = { @JoinColumn(name = "metadataUpdate_id") },
    		inverseJoinColumns = { @JoinColumn(name = "addedNamespaces_id") } )
    @CollectionId(
    		columns = @Column(name="addedNS_MDU_id"),
    		type = @Type(type="long"),
    		generator = "gg4"
    		)
	private Collection<KiWiNamespace> addedNamespaces;
	
	/**
	 * @return the removedNamespaces
	 */
	public Collection<KiWiNamespace> getRemovedNamespaces() {
		return removedNamespaces;
	}

	/**
	 * @param removedNamespaces the removedNamespaces to set
	 */
	public void setRemovedNamespaces(Collection<KiWiNamespace> removedNamespaces) {
		this.removedNamespaces = removedNamespaces;
	}

	/**
	 * @return the addedNamespaces
	 */
	public Collection<KiWiNamespace> getAddedNamespaces() {
		return addedNamespaces;
	}

	/**
	 * @param addedNamespaces the addedNamespaces to set
	 */
	public void setAddedNamespaces(Collection<KiWiNamespace> addedNamespaces) {
		this.addedNamespaces = addedNamespaces;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;	
	}
}
