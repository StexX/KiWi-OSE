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

package kiwi.api.skill;

import java.util.List;
import java.util.Map;

import kiwi.model.content.ContentItem;
import kiwi.model.skill.UserSkill;
import kiwi.model.user.User;


/**
 * @author Fred Durao, Nilay Coskun
 *
 */
public interface SkillService {

	/**
	 * Returns the skills and the score for a given user
	 * @param currentUser
	 * @return
	 */
	public Map<String, Float> computeSkillsByUser(User currentUser);

	/**
	 * It computes the skills of all users in the system
	 */
	public void computeUserSkills();
	
	/**
	 *  It computes the skills of a given user in the system 
	 * @param user
	 */
	public void computeUserSkill(User user);
	
	/**
	 * It gets the result by skill
	 */
	public UserSkill getSkillsByUser(User user);
	
	
	/**
	 * list user skills
	 * @return
	 */
	public List<UserSkill> listUserSkills();
	
	/**
	 * deletes user skills
	 * @return
	 */
	public boolean deleteUserSkills();

	/**
	 * list user contributions
	 * @param user
	 * @return
	 */
	public List<ContentItem> listUserContributions(User user);
	
	/**
	 * Remove skill from db and add to stopword list
	 * @param userSkill
	 * @return
	 */
	public boolean removeSkill(String skillName);	
	
	/**
	 * 
	 */
	public void loadFakeSkillList();
}
