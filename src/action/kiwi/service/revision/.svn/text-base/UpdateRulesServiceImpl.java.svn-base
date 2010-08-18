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
package kiwi.service.revision;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.mail.MethodNotSupportedException;
import javax.persistence.EntityManager;

import kiwi.api.revision.UpdateRulesServiceLocal;
import kiwi.api.revision.UpdateRulesServiceRemote;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.RuleUpdate;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * The UpdateRuleService holds the current status of the system 
 * rules for the reasoner.
 * 
 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
 *
 */
@Name("updateRulesService")
@AutoCreate
@Stateless
@Scope(ScopeType.STATELESS)
public class UpdateRulesServiceImpl implements Serializable,
		UpdateRulesServiceLocal, UpdateRulesServiceRemote {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public RuleUpdate createRuleUpdate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateRulesService#commitUpdate(kiwi.model.revision.RuleUpdate)
	 */
	public void commitUpdate(CIVersion version) {
		if(version.getRuleUpdate() != null) {
			EntityManager em = (EntityManager) Component.getInstance("entityManager");
			RuleUpdate ru = version.getRuleUpdate();
			
			em.persist(ru);
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateRulesService#createPreview(kiwi.model.revision.RuleUpdate, java.lang.String)
	 */
	public String createPreview(RuleUpdate ru, String text) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateRulesService#rollbackUpdate(kiwi.model.revision.RuleUpdate)
	 */
	public void rollbackUpdate(CIVersion version) {
		version.setRuleUpdate(null);
	}

	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateRulesService#undo(kiwi.model.revision.RuleUpdate)
	 */
	public void restore(CIVersion version) {
		try {
			throw new MethodNotSupportedException("UpdateTaggingService.restore(CIVersion version) is not supported");
		} catch (MethodNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.api.revision.UpdateRulesService#undo(kiwi.model.revision.RuleUpdate)
	 */
	public void undo(CIVersion version) {
		// TODO implement
	}
}
