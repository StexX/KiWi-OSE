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
package interedu.action.explorer;

import java.io.Serializable;

import interedu.api.dataimport.InterEduArtikelFacade;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.context.CurrentContentItemFactory;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

@Scope(ScopeType.SESSION)
@Name("interedu.currentArticleSelector")
@AutoCreate
//@Transactional
public class CurrentArticleSelector implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
    private KiWiEntityManager kiwiEntityManager;
    
    @In(create=true)
    private CurrentContentItemFactory currentContentItemFactory;

	private InterEduArtikelFacade article;
	
	public String selectArticle(InterEduArtikelFacade article) {
//		article = kiwiEntityManager.createFacade(ci, InterEduArtikelFacade.class);
		//select current content item
//        currentContentItemFactory.setCurrentItemId(ci.getId());
//        currentContentItemFactory.refresh();
		
		this.article = article;
        
        return "/interedu/article.xhtml";
	}
	
	public InterEduArtikelFacade getCurrentArticle() {
		return article;
	}
}
