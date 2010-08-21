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
 * sschaffe
 * 
 */
package kiwi.model.perspective;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.OptimisticLockType;

/**
 * Descriptor of a perspective, representing a certain viewpoint on the contentitem that is the current context.
 * <p>
 * A content item <b>always</b> has the default perspective associated with it. Each content item in addition can have 
 * several custom perspectives that are manually associated by users and can be selected using tabs or similar in the user
 * interface.  A perspective is always composed of the following 
 * attributes:
 * <ul>
 * <li>a unique name, identifying the perspective and used as label for switching/adding a perspective to the current
 *     view</li>
 * <li>a description, documenting the perspective. Shown e.g. in a tooltip or when selecting a new perspective.</li>
 * <li>a JSF/XHTML file to be used when <b>viewing</b> a content item with the perspective; the path for the XHTML file
 *     is automatically derived from the path field of the class.</li>
 * <li>a JSF/XHTML file to be used when <b>editing</b> a content item with the perspective; the path for the XHTML file
 *     is automatically derived from the path field of the class.</li>
 * <li>a JSF/XHTML file to be used when <b>searching</b> content items and displaying them in the search results; the 
 *     path for the XHTML file is automatically derived from the path field of the class.</li>
 * </ul>
 * The JSF/XHTML files for viewing and editing can access the complete KiWi/Seam/Java EE context, particularly the
 * currentContentItem and the RDF context of the currently active item. XHTML files for perspectives are located
 * beneath the perspectives/ directory of the main view in KiWi.war.
 * 
 * <p>
 * For example, a contentitem representing a meeting at a certain location could - in addition to the default
 * perspective - also have specialised perspectives for displaying/editing the location on a map and the 
 * meeting details in a tabular form.
 * <p>
 *
 * @author Sebastian Schaffert
 *
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Immutable
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
@NamedQueries({
	  @NamedQuery(name  = "perspectiveService.perspectiveByName",
				  query = "from Perspective p " +
			 		       "where p.name = :name "),
	  @NamedQuery(name  = "perspectiveService.perspectiveList",
			      query = "from Perspective p "),
	  @NamedQuery(name  = "perspectiveService.perspectiveUpdate",
		          query = "update Perspective p " +
		          		  "set p.path = :path, p.name = :name, p.description = :description " +
		          		  "where p.id = :id"),
	  @NamedQuery(name  = "perspectiveService.perspectiveDelete",
		          query = "delete from Perspective p " +
		          		  "where p.id = :id")
	}	
)
public class Perspective {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)    
    private Long id;

	
    @Column(nullable=false)
	private String name;
	
	/**
	 * The path beneath view/perspectives where the view.xhtml and edit.xhtml files are located.
	 */
    @Column(nullable=false)
	private String path;
	
	/**
	 * The description of this perspective, to be displayed e.g. in tooltips.
	 */
	@Lob
	private String description;
	
	/**
	 * Flags to indicate whether the perspective supports the respective features.
	 */
	private boolean search = true, view = true, edit = true, widget = true;
	
    @Version
    private Long version;
	
	/**
	 * 
	 */
	public Perspective() {
		super();
	}


	/**
	 * @param name
	 * @param path
	 */
	public Perspective(String name, String path) {
		super();
		this.name = name;
		this.path = path;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}


	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	
	
	
	
	/**
	 * @return the search
	 */
	public boolean isSearch() {
		return search;
	}


	/**
	 * @param search the search to set
	 */
	public void setSearch(boolean search) {
		this.search = search;
	}


	/**
	 * @return the view
	 */
	public boolean isView() {
		return view;
	}


	/**
	 * @param view the view to set
	 */
	public void setView(boolean view) {
		this.view = view;
	}


	/**
	 * @return the edit
	 */
	public boolean isEdit() {
		return edit;
	}


	/**
	 * @param edit the edit to set
	 */
	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	
	

	/**
	 * @return the widget
	 */
	public boolean isWidget() {
		return widget;
	}


	/**
	 * @param widget the widget to set
	 */
	public void setWidget(boolean widget) {
		this.widget = widget;
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
	
	
	/**
	 * Return the view template for this perspective. The view template is used for rendering the display
	 * view on the current content item.
	 * @return
	 */
	public String getViewTemplate() {
		return path + "/view.xhtml";
	}
	
	
	/**
	 * Return the edit template for this perspective. The edit template is used for rendering the edit view
	 * on the current content item.
	 * @return
	 */
	public String getEditTemplate() {
		return path + "/edit.xhtml";		
	}

	
	public String getWidgetTemplate() {
		return path + "/widgets.xhtml";
	}
	

	/**
	 * Return the search template for this perspective. The search template is used for rendering search
	 * results.
	 * @return
	 */
	public String getSearchTemplate() {
		return path + "/search.xhtml";				
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Perspective other = (Perspective) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
	
	
	
}
