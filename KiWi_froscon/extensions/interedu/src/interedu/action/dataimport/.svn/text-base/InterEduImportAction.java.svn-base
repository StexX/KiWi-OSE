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


package interedu.action.dataimport;


import interedu.api.dataimport.InterEduImportService;

import java.io.File;
import java.io.Serializable;

import javax.transaction.SystemException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.transaction.Transaction;


/**
 * @author Rolf Sint
 */

@Name("interEdu.interEduImportAction")
@Scope(ScopeType.CONVERSATION)
public class InterEduImportAction implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
    private Log log;

    @In(create = true, value = "interedu.intereduImportService")
    private InterEduImportService interEduImportService;

    @In
    private FacesMessages facesMessages;

    /**
     * The path for the import, the import will read all the
     * filed from this directory.
     */
    private String path;

    /**
     * The number of imported articles.
     */
    private int articleCount;

    public InterEduImportAction() {
        articleCount = InterEduImportService.ALL;
    }

    public void submit() {
        try {
            Transaction.instance().setTransactionTimeout(60000);
        } catch (final SystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (path == null) {
            log.warn("The path to import is null");
            facesMessages.add("The path to import is null.");
            return;
        }

        final File dir = new File(path);
        if (!dir.exists() || dir.isFile()) {
            log.warn("The path to import is not a valid directory.");
            facesMessages.add("The path to import is not a valid directory.");
            return;
        }

        interEduImportService.importDir(dir, articleCount);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

}
