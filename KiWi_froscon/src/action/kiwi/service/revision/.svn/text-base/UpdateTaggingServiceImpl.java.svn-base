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
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.mail.MethodNotSupportedException;
import javax.persistence.EntityManager;

import kiwi.api.revision.UpdateTaggingServiceLocal;
import kiwi.api.revision.UpdateTaggingServiceRemote;
import kiwi.api.tagging.TaggingService;
import kiwi.exception.ContentItemDoesNotExistException;
import kiwi.model.revision.CIVersion;
import kiwi.model.revision.TaggingUpdate;
import kiwi.model.tagging.Tag;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * Stores transactional added and deleted tags in a 
 * TaggingUpdate object and enables functionality to 
 * rollback, undo and commit the update
 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
 *
 */
@Stateless
@Name("updateTaggingService")
@AutoCreate
@Scope(ScopeType.STATELESS)
public class UpdateTaggingServiceImpl implements Serializable,
		UpdateTaggingServiceLocal, UpdateTaggingServiceRemote {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3983714187225482083L;
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.UpdateTaggingService#updateTagging(java.util.Set, java.util.Set)
	 */
	public TaggingUpdate updateTagging(List<Tag> removedTags, List<Tag> addedTags) {
		TaggingUpdate tu = new TaggingUpdate();
		tu.setAddedTags(addedTags);
		tu.setRemovedTags(removedTags);
		return tu;
	}
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.UpdateTaggingService#commitUpdate(kiwi.model.revision.TaggingUpdate)
	 */
	@Override
	public void commitUpdate(CIVersion version) {
		if(version.getTaggingUpdate() != null) {
			EntityManager em = (EntityManager) Component.getInstance("entityManager");
			TaggingUpdate tu = version.getTaggingUpdate();
			
			em.persist(tu);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.UpdateTaggingService#rollbackUpdate(kiwi.model.revision.TaggingUpdate)
	 */
	@Override
	public void rollbackUpdate(CIVersion version) {
		version.setTaggingUpdate(null);
	}

	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.UpdateTaggingService#undo(kiwi.model.revision.TaggingUpdate)
	 */
	public void restore(CIVersion version) {
		try {
			throw new MethodNotSupportedException("UpdateTaggingService.restore(CIVersion version) is not supported");
		} catch (MethodNotSupportedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void undo(CIVersion version) throws ContentItemDoesNotExistException {
		if(version.getTaggingUpdate() != null) {
			TaggingUpdate tu = version.getTaggingUpdate();
			TaggingService ts = (TaggingService) 
				Component.getInstance("taggingService");
			List<Tag> addedTags = new LinkedList<Tag>(tu.getAddedTags());
			for(Tag t : addedTags) {
				ts.removeTagging(t);
			}
			List<Tag> removedTags = new LinkedList<Tag>(tu.getRemovedTags());
			for(Tag t : removedTags) {
				ts.addTagging(t);
			}
		}
	}
}
