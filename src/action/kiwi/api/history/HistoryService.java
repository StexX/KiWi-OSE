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

package kiwi.api.history;

import java.util.List;

import kiwi.model.activity.AddTagActivity;
import kiwi.model.activity.CommentActivity;
import kiwi.model.activity.EditActivity;
import kiwi.model.activity.SearchActivity;
import kiwi.model.activity.VisitActivity;
import kiwi.model.user.User;


/**
 * @author Fred Durao, Nilay Coskun
 *
 */
public interface HistoryService {

	/**
	 * return last visits of given user
	 * @param user
	 * @return
	 */
	public List<VisitActivity> listLastVisitsByUser(User user);
	
	/**
	 * return last distinct visits of given user
	 * @param user
	 * @return
	 */
	public List<VisitActivity> listLastDistinctsVisitsByUser(User user);	
	
	/**
	 * return  visits of given user
	 * @param user
	 * @return
	 */
	public List<VisitActivity> listVisitsByUser(User user);
	
	/**
	 * return last edits of given user
	 * @param user
	 * @return
	 */
	public List<EditActivity> listLastEditsByUser(User user);
	
	/**
	 * return  edits of given user
	 * @param user
	 * @return
	 */
	public List<EditActivity> listEditsByUser(User user);
	
	/**
	 * return last comments of given user
	 * @param user
	 * @return
	 */
	public List<CommentActivity> listLastCommentsByUser(User user);
	
	/**
	 * return comments of given user
	 * @param user
	 * @return
	 */
	public List<CommentActivity> listCommentsByUser(User user);	
	
	/**
	 * return last searches of given user
	 * @param user
	 * @return
	 */
	public List<SearchActivity> listLastSearchesByUser(User user);
	
	/**
	 * return  searches of given user
	 * @param user
	 * @return
	 */
	public List<SearchActivity> listSearchesByUser(User user);
	
	/**
	 * return last tags of given user
	 * @param user
	 * @return
	 */
	public List<AddTagActivity> listLastTagsByUser(User user);
	
	/**
	 * return tags of given user
	 * @param user
	 * @return
	 */
	public List<AddTagActivity> listTagsByUser(User user);
	
	/**
	 * return  last distinct searches of given user
	 * @param user
	 * @return
	 */
	public List<SearchActivity> listLastDistinctSearchesByUser(User user);	
	
	/**return last visted page by user
	 * @param user
	 * @return
	 */
	public VisitActivity listLastVisitByUser(User user);
	
	/**
	 * retunr uri of last visited page
	 * @return
	 */
	public String getLastVisitedPage();
}
