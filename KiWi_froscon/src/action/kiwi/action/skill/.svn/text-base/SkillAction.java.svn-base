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
package kiwi.action.skill;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import kiwi.api.search.SemanticIndexingService;
import kiwi.api.search.SolrService;
import kiwi.api.skill.SkillService;
import kiwi.model.skill.UserSkill;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.faces.FacesMessages;

/**
 * 
 * 
 * @author Fred Durao, Nilay Coskun
 * 
 */
@Name("userSkillAction")
@Scope(ScopeType.CONVERSATION)
public class SkillAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In
	private SolrService solrService;

	@In("kiwi.core.semanticIndexingService")
	private SemanticIndexingService semanticIndexingService;

	@In
	private FacesMessages facesMessages;
	@In
	private SkillService skillService;

	private List<UserSkill> userSkills = new ArrayList<UserSkill>();

	public SkillService getSkillService() {
		return skillService;
	}

	/**
	 * computeUserSkills
	 */
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void indexAndComputeUserSkills() {
		solrService.rebuildIndex();
		semanticIndexingService.reIndex();
		
		skillService.computeUserSkills();
		facesMessages.add("User skills computed");
	}

	/**
	 * computeUserSkills
	 */
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void computeUserSkills() {
		skillService.computeUserSkills();
		this.initTagList();
		facesMessages.add("User skills computed");		
	}
	
	/**
	 * deleteUserSkills
	 */
	public void deleteUserSkills() {
		skillService.deleteUserSkills();
	}
	
	/**
	 * deleteUserSkills
	 */
	public void removeSkill(String skillName) {
		skillService.removeSkill(skillName);
		this.initTagList();
		facesMessages.add("Skill removed");
	}	

	@Factory("userSkillAction.userSkills")
	public List<UserSkill> getUserSkills() {
		userSkills = skillService.listUserSkills();
		if (userSkills == null) {
			userSkills = skillService.listUserSkills();
		}
		return userSkills;
	}
	
	
	@DataModel
	private List<SkillBean> skillList;
	
	/**
	 * A factory method for setting up the list of my tags.
	 *
	 */
	@Factory("skillList")
	public void initTagList() {
			skillList = new LinkedList<SkillBean>();
			List<UserSkill> userSkills = getUserSkills();
			for (UserSkill userSkill2 : userSkills) {
				Map<String, Float> userSkill2Map = userSkill2.getSkills();
				for (String skillKey : userSkill2Map.keySet()) {
					SkillBean skillBean = new SkillBean();
					skillBean.setUser(userSkill2.getUser());
					skillBean.setSkill(skillKey);
					skillBean.setValue(userSkill2Map.get(skillKey).toString());
					skillList.add(skillBean);
				}
			}
	}

	public List<SkillBean> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<SkillBean> skillList) {
		this.skillList = skillList;
	}
	
	public void loadFakeSkillList(){
		skillService.loadFakeSkillList();
		facesMessages.add("Fake Skill List Loaded");		
	}	
	
}
