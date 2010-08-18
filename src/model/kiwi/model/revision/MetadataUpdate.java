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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import kiwi.model.kbase.KiWiTriple;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * MetadataUpdate, holds the KiWiTriples and KiWiNamespaces that have been 
 * added to or removed from the application
 * 
 * @author 	Stephanie Stroka
 * 			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Entity
@Immutable
@BatchSize(size = 20)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class MetadataUpdate implements KiWiUpdate, Serializable {

	/**
	 * generated serialization id
	 */
	private static final long serialVersionUID = 7139902859089376167L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	@GenericGenerator(name="gg1",strategy="hilo")
	@ManyToMany
	@JoinTable(name = "removedTriple_metadataUpdate",
    		joinColumns = { @JoinColumn(name = "metadataUpdate_id") },
    		inverseJoinColumns = { @JoinColumn(name = "removedTriples_id") } )
    @CollectionId(
    		columns = @Column(name="removedT_MDU_id"),
    		type = @Type(type="long"),
    		generator = "gg1")
    @BatchSize(size = 100)
	private Collection<KiWiTriple> removedTriples;
	
	@GenericGenerator(name="gg2",strategy="hilo")
	@ManyToMany
	@JoinTable(name = "addedTriple_metadataUpdate",
    		joinColumns = { @JoinColumn(name = "metadataUpdate_id") },
    		inverseJoinColumns = { @JoinColumn(name = "addedTriples_id") } )
    @CollectionId(
    		columns = @Column(name="addedT_MDU_id"),
    		type = @Type(type="long"),
    		generator = "gg2")
    @BatchSize(size = 100)
	private Collection<KiWiTriple> addedTriples;
	
	/* (changed by steffi) To be able to attach every update action to a ContentItem, 
 	we need to forbear to keep track of changes in namespaces (they can be regenerated, anyways) */
//	@GenericGenerator(name="gg3",strategy="hilo")
//	@ManyToMany
//	@JoinTable(name = "removedNamespace_metadataUpdate",
//    		joinColumns = { @JoinColumn(name = "metadataUpdate_id") },
//    		inverseJoinColumns = { @JoinColumn(name = "removedNamespaces_id") } )
//    @CollectionId(
//    		columns = @Column(name="removedNS_MDU_id"),
//    		type = @Type(type="long"),
//    		generator = "gg3")
//	private Collection<KiWiNamespace> removedNamespaces;
//	
//	@GenericGenerator(name="gg4",strategy="hilo")
//	@ManyToMany
//	@JoinTable(name = "addedNamespace_metadataUpdate",
//    		joinColumns = { @JoinColumn(name = "metadataUpdate_id") },
//    		inverseJoinColumns = { @JoinColumn(name = "addedNamespaces_id") } )
//    @CollectionId(
//    		columns = @Column(name="addedNS_MDU_id"),
//    		type = @Type(type="long"),
//    		generator = "gg4"
//    		)
//	private Collection<KiWiNamespace> addedNamespaces;
	
	/**
	 * default constructor
	 */
	public MetadataUpdate() {
		super();
	}

	/**
	 * @return the removedTriples
	 */
	public Collection<KiWiTriple> getRemovedTriples() {
		if(removedTriples == null) {
			removedTriples = new HashSet<KiWiTriple>();
		}
		return removedTriples;
	}

	/**
	 * @param removedTriples the removedTriples to set
	 */
	public void setRemovedTriples(Collection<KiWiTriple> removedTriples) {
		Log log = Logging.getLog(MetadataUpdate.class);
		if(log.isDebugEnabled()) {
			log.debug("----------> removed triples:");
			for(KiWiTriple t : removedTriples) {
				if(t.getId() != null) {
					log.debug("--------------> id: #0", t.getId());
				}
				log.debug("--------------> #0", t);
			}
		}
		this.removedTriples = removedTriples;
	}

	/**
	 * @return the addedTriples
	 */
	public Collection<KiWiTriple> getAddedTriples() {
		if(addedTriples == null) {
			addedTriples = new HashSet<KiWiTriple>();
		}
		return addedTriples;
	}

	/**
	 * @param addedTriples the addedTriples to set
	 */
	public void setAddedTriples(Collection<KiWiTriple> addedTriples) {
		Log log = Logging.getLog(MetadataUpdate.class);
		if(log.isDebugEnabled()) {
			log.debug("----------> added triples:");
			for(KiWiTriple t : addedTriples) {
				if(t.getId() != null) {
					log.debug("--------------> id: #0", t.getId());
				}
				log.debug("--------------> #0", t);
			}
		}
		this.addedTriples = addedTriples;
	}

//	/**
//	 * @return the removedNamespaces
//	 */
//	public Collection<KiWiNamespace> getRemovedNamespaces() {
//		if(removedNamespaces == null) {
//			removedNamespaces = new HashSet<KiWiNamespace>();
//		}
//		return removedNamespaces;
//	}
//
//	/**
//	 * @param removedNamespaces the removedNamespaces to set
//	 */
//	public void setRemovedNamespaces(Collection<KiWiNamespace> removedNamespaces) {
//		this.removedNamespaces = removedNamespaces;
//	}
//
//	/**
//	 * @return the addedNamespaces
//	 */
//	public Collection<KiWiNamespace> getAddedNamespaces() {
//		if(addedNamespaces == null) {
//			addedNamespaces = new HashSet<KiWiNamespace>();
//		}
//		return addedNamespaces;
//	}
//
//	/**
//	 * @param addedNamespaces the addedNamespaces to set
//	 */
//	public void setAddedNamespaces(Collection<KiWiNamespace> addedNamespaces) {
//		this.addedNamespaces = addedNamespaces;
//	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
}
