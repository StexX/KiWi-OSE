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
package kiwi.api.revision;

import java.util.Date;
import java.util.List;
import java.util.Set;

import kiwi.model.content.ContentItem;
import kiwi.model.revision.Revision;

/**
 * Revision Service interface
 * implementation: RevisionServiceImpl
 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
 *
 */
public interface RevisionService {
	
	/**
	 * Reverts all updates of a revision
	 * @param rev
	 */
	public boolean restore(Revision rev);
	
	/**
	 * Called when a transaction completes successfully.
	 * Persists the revision.
	 * @param rev
	 * @return
	 */
	public boolean commit(Revision rev);
	
	/**
	 * Called when a transaction completes unsuccessfully.
	 * @param rev
	 * @return
	 */
	public boolean rollback(Revision rev);
	
	/**
	 * Creates a revision with a list of updates.
	 * @param updates
	 * @return
	 */
	public Revision createRevision();

	/**
	 * Returns all ContentItem-Revisions that have been created 
	 * since a certain time (java.util.Date)
	 * @param since
	 * @return
	 */
	public List<Revision> getRevisions(Date since);

	/**
	 * Returns all revision for a given ContentItem that have been created 
	 * since a certain time (java.util.Date)
	 * @param since
	 * @param ci
	 * @return
	 */
	public List<Revision> getRevisions(Date since, ContentItem ci);

	/**
	 * Returns all revision for a set of given ContentItems that have been created 
	 * since a certain time (java.util.Date)
	 * @param since
	 * @param cis
	 * @return
	 */
	public List<Revision> getRevisions(Date since, Set<ContentItem> cis);

	/**
	 * Returns all ContentItem-Revisions that have been created 
	 * in between a certain time (between two java.util.Date`s)
	 * @param since
	 * @param until
	 * @return
	 */
	public List<Revision> getRevisions(Date since, Date until);

	/**
	 * Returns all ContentItem-Revisions for a given ContentItem that have been created 
	 * in between a certain time (between two java.util.Date`s)
	 * @param since
	 * @param until
	 * @param ci
	 * @return
	 */
	public List<Revision> getRevisions(Date since, Date until, ContentItem ci);

	/**
	 * Returns all revision for a set of given ContentItems that have been created 
	 * in between a certain time (between two java.util.Date`s)
	 * @param since
	 * @param cis
	 * @return
	 */
	public List<Revision> getRevisions(Date since, Date until, Set<ContentItem> cis);

	/**
	 * returns all revisions of a specific ContentItem
	 * @param ci
	 * @return
	 */
	public List<Revision> getAllRevisions(ContentItem ci);
}
