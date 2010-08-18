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
package kiwi.model.informationextraction;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import kiwi.model.kbase.KiWiResource;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

/**
 * Entity storing alternative labels for resources, so it can easily be 
 * used to build a gazetteer. It may be potentially used also for synonyms, 
 * and labels derived from links.
 * 
 * We need to persist them, because getting some of them (links) may be 
 * costly, and we also want to index those alternative labels (only titles 
 * are indexed in the content items)
 * 
 * @author Marek Schmidt
 *
 */
@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@Immutable
@NamedQueries({
	@NamedQuery(name  = "kiwi.informationextraction.labelService.deleteLabels",
			    query = "delete kiwi.model.informationextraction.Label l"),
	@NamedQuery(name  = "kiwi.informationextraction.labelService.deleteLabelsByResource",
				query = "delete kiwi.model.informationextraction.Label l where l.resource = :resource"),
	@NamedQuery(name="kiwi.informationextraction.labelService.listResourceLabels",
				query="select label.string, label.resource.id " +
					  "from kiwi.model.informationextraction.Label label"),
	@NamedQuery(name = "kiwi.informationextraction.labelService.listResourcesByString",
				query = "select label.resource from kiwi.model.informationextraction.Label label " +
						"where label.string = :string"),
	@NamedQuery(name = "kiwi.informationextraction.labelService.countLabels",
				query = "select count(*) from kiwi.model.informationextraction.Label")
})
public class Label implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * The label string
	 */
	@Index(name="idx_label_string")
	private String string;
	
	public static int TYPE_TITLE = 0;
	public static int TYPE_ALTLABEL = 1;
	
	/**
	 * The origin of the label, so that it's possible to disable 
	 */
	private int type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name="idx_label_resource")
	private KiWiResource resource;
	
	@Version
	private Long version;
	
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

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setResource(KiWiResource resource) {
		this.resource = resource;
	}

	public KiWiResource getResource() {
		return resource;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}
}
