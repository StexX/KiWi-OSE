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
package kiwi.model.importexport;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.validator.NotNull;

/**
 * An ImportTask describes an importing task to be executed in regular intervals, similar to
 * a cron job. It is persisted in the database and retrieved by the ImportService, which
 * polls for tasks every minute.
 * 
 * @author Sebastian Schaffert
 *
 */
@Entity
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
	@NamedQuery(
			name = "importTask.listScheduledTasks",
			query = "from ImportTask t where current_timestamp() - t.lastExecuted > t.interval"
	),
	@NamedQuery(
			name = "importTask.listAllTasks",
			query = "from ImportTask t left outer join fetch t.tags left outer join fetch t.types order by t.created"
	),
	@NamedQuery(
			name = "importTask.listTasksByUser",
			query = "from ImportTask t left outer join fetch t.tags left outer join fetch t.types where t.owner = :user order by t.created"
	)
})
public class ImportTask {

	@Id
	@NotNull
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;

	/**
	 * The date when this task has been created
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date created;
	
	/**
	 * The date when this task has been last executed
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date lastExecuted;
	
	/**
	 * The interval in which to execute this task.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Column(name="interval_time")
	private Date interval;
	
	/**
	 * The user who owns the importing task
	 */
	@ManyToOne
	private User owner;
	
	/**
	 * The types to use for each content item in the import
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<KiWiUriResource> types;
	
	/**
	 * The tags to use for each content item in the import
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<ContentItem> tags;
	
	/**
	 * The data format to import
	 */
	@NotNull
	private String format;
	
	
	/**
	 * The URL to import in regular intervals.
	 */
	@NotNull
	private String url;

	
	private boolean autoGeoCoding;
	
	/**
	 * A human-readable description of the task
	 */
	private String description;
	
	@Version
	private Long version;
	
	
	public ImportTask() {
		
	}
	
	public ImportTask(String description, String url, String format, Date interval, User owner) {
		this.owner    = owner;
		this.format   = format;
		this.url      = url;
		this.interval = interval;
		this.description = description;
		
		this.created      = new Date();
		this.lastExecuted = new Date(0);
	}
	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Date getCreated() {
		return created;
	}


	public void setCreated(Date created) {
		this.created = created;
	}


	public Date getLastExecuted() {
		return lastExecuted;
	}


	public void setLastExecuted(Date lastExecuted) {
		this.lastExecuted = lastExecuted;
	}


	public Date getInterval() {
		return interval;
	}


	public void setInterval(Date interval) {
		this.interval = interval;
	}


	public User getOwner() {
		return owner;
	}


	public void setOwner(User owner) {
		this.owner = owner;
	}


	public String getFormat() {
		return format;
	}


	public void setFormat(String format) {
		this.format = format;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Set<KiWiUriResource> getTypes() {
		return types;
	}


	public void setTypes(Set<KiWiUriResource> types) {
		this.types = types;
	}


	public Set<ContentItem> getTags() {
		return tags;
	}


	public void setTags(Set<ContentItem> tags) {
		this.tags = tags;
	}

	public boolean isAutoGeoCoding() {
		return autoGeoCoding;
	}

	public void setAutoGeoCoding(boolean autoGeoCoding) {
		this.autoGeoCoding = autoGeoCoding;
	}
	
    /**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}
	
}
