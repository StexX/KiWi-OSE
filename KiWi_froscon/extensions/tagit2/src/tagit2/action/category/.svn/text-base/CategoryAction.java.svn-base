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
package tagit2.action.category;

import java.io.Serializable;
import java.util.List;

import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import tagit2.api.category.CategoryService;

/**
 * this class manages the SKOSConcept display and selection
 * @author tkurz
 *
 */
@Scope(ScopeType.CONVERSATION)
@Name("tagit2.categoryAction")
public class CategoryAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CAT_DEFAULT = "Bitte ausw\u00E4hlen";
	public static final String SUBCAT_DEFAULT = "Bitte ausw\u00E4hlen";
	
	@In(value="tagit2.categoryService")
	private CategoryService categoryService;

	private String categoryString;

	private String subcategoryString;
	
	private SKOSConcept category;
	
	public List<String> getAllCategories() {
		return categoryService.listCategoryStrings();
	}
	
	public List<String> getAllSubcategories() {
		return categoryService.listSubcategoryStrings(category);
	}

	public String getCategoryString() {
		return categoryString;
	}

	public void setCategoryString(String categoryString) {
		this.categoryString = categoryString;
		//set category concept
		category = categoryService.getCategory( categoryString );
	}

	public String getSubcategoryString() {
		return subcategoryString;
	}

	public void setSubcategoryString(String subcategoryString) {
		this.subcategoryString = subcategoryString;
	}
	
	public SKOSConcept getCategory() {
		return category;
	}
	
	//for view
	public static String getCAT_DEFAULT() {
		return CAT_DEFAULT;
	}

	public static String getSUBCAT_DEFAULT() {
		return SUBCAT_DEFAULT;
	}
}
