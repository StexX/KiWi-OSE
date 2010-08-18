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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import kiwi.api.content.ContentItemService;
import kiwi.api.revision.CIVersionServiceLocal;
import kiwi.api.revision.CIVersionServiceRemote;
import kiwi.api.revision.KiWiUpdateService;
import kiwi.api.revision.UpdateTextContentService;
import kiwi.api.revision.UpdateTextContentService.PreviewStyle;
import kiwi.exception.ContentItemDoesNotExistException;
import kiwi.exception.VersionDoesNotExistException;
import kiwi.model.content.ContentItem;
import kiwi.model.revision.CIVersion;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * The CIVersionServiceImpl class is able to create, commit, rollback, restore and undo CIVersions 
 * and all the updates that the CIVersion contains (TextContentUpdate, MetadataUpdate, TagginUpdate,
 * RenamingUpdate, RuleUpdate, DeletionUpdate, etc...).
 * It is called by the RevisionServiceImpl.
 * 
 * @author Stephanie Stroka
 *			(stephanie.stroka@salzburgresearch.at)
 *
 */
@Stateless
@Name("ciVersionService")
@AutoCreate
public class CIVersionServiceImpl implements CIVersionServiceLocal, CIVersionServiceRemote, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3798760110330896033L;

	@Logger
	private Log log;

	@In
	private EntityManager entityManager;
	
	private Set<KiWiUpdateService> updateServicesDiscrete;
	
	private Set<KiWiUpdateService> updateServicesContinuous;
	
	/**
	 * Initialises the discrete and the continuous updateServices. 
	 * Continuous and discrete updates differe from each other in that 
	 * continuous updates must be undone step by step, whereas discrete 
	 * updates simply must be restored
	 */
	public void initialise() {
		updateServicesDiscrete = new HashSet<KiWiUpdateService>();
		updateServicesContinuous = new HashSet<KiWiUpdateService>();
		
		initUpdateServices(updateServicesDiscrete, "updateTextContentService");
		initUpdateServices(updateServicesContinuous, "updateTaggingService");
		initUpdateServices(updateServicesDiscrete, "updateMediaContentService");
		initUpdateServices(updateServicesContinuous, "updateRulesService");
		initUpdateServices(updateServicesContinuous, "updateMetadataService");
		initUpdateServices(updateServicesDiscrete, "deleteContentItemService");
		initUpdateServices(updateServicesDiscrete, "renameContentItemService");
	}
	
	/**
	 * Calls the component with the name cls and adds it to the specific set of updateServices.
	 * @param <T>
	 * @param updateServices
	 * @param cls
	 */
	@SuppressWarnings("unchecked")
	private <T> void initUpdateServices(Set<T> updateServices, String cls) {
    	try {
			T service = (T)Component.getInstance(cls);
    		
			if(service != null)
				updateServices.add(service);
			else
				log.warn("warning: savelet #0 was null after initialisation", cls);
			
		} catch (Exception e) {
			log.error("error while instantiating savelet #0: #1",cls,e.getMessage());
		}
    }
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#createRevision(java.util.List, 
	 * 		java.util.List, java.util.List, java.util.List, java.util.List, 
	 * 		java.util.List, kiwi.model.content.ContentItem)
	 */
	public CIVersion createCIVersion(ContentItem revisedContentItem) {
		if(revisedContentItem != null) {
			long count = 0;
			// fetch current number of versions to compute title
			if(revisedContentItem.getId() != null) {
				try {
					Query q = entityManager.createNamedQuery("ciVersionService.ciVersionCount");
					q.setParameter("ci_id", revisedContentItem.getId());
					
					count = (Long) q.getSingleResult();
				} catch(NoResultException ex) {
					
				}
			}
			
			// TODO: how can this happen? a generated content item as place holder for the ontology?
			// if it should not happen, title should have the annotation @NotNull
			if(revisedContentItem.getTitle() == null) {
				ContentItemService contentItemService = (ContentItemService)Component.getInstance("contentItemService");
				contentItemService.updateTitle(revisedContentItem, Long.toHexString(System.currentTimeMillis()));
			}
			CIVersion vers = new CIVersion();

			vers.setTitle(revisedContentItem.getTitle() + ", Version "+(count+1));
			vers.setVersionId(count);
			vers.setRevisedContentItem(revisedContentItem);
			
			return vers;
		} else {
			log.error("error: currentContentItem was null");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#undo(kiwi.model.revision.Revision)
	 */	
	public boolean restore(CIVersion vers) {
		if(updateServicesContinuous == null || updateServicesDiscrete == null) {
			initialise();
		}
		
		ContentItem currentContentItem = (ContentItem) Component.getInstance("currentContentItem");
		
		if(currentContentItem != null) {
			
			for(KiWiUpdateService kus : updateServicesDiscrete) {
				try {
					kus.restore(vers);
				} catch (ContentItemDoesNotExistException e) {
					e.printStackTrace();
					log.error("[restore] ContentItem did not exist #0 ", e.getMessage());
				}
			}
			return true;
		} else {
			log.error("Current content item could not be injected");
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#undo(kiwi.model.revision.Revision)
	 */	
	public boolean undo(CIVersion vers) {
		if(updateServicesContinuous == null || updateServicesDiscrete == null) {
			initialise();
		}
		ContentItem currentContentItem = (ContentItem) Component.getInstance("currentContentItem");
		
		if(currentContentItem != null) {
			
			for(KiWiUpdateService kus : updateServicesContinuous) {
				try {
					kus.undo(vers);
				} catch (ContentItemDoesNotExistException e) {
					e.printStackTrace();
					log.error("[restore] ContentItem did not exist #0 ", e.getMessage());
				}
			}
			return true;
		} else {
			log.error("Current content item could not be injected");
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#commit(kiwi.model.revision.Revision)
	 */
	public boolean commit(CIVersion vers) {
		if(updateServicesContinuous == null || updateServicesDiscrete == null) {
			initialise();
		}
		if(vers != null) {
			
			//vers.setVersionId(new Long(vers.getRevisedContentItem().getVersions().size()));
			
			for(KiWiUpdateService kus : updateServicesDiscrete) {
				kus.commitUpdate(vers);
			}
			for(KiWiUpdateService kus : updateServicesContinuous) {
				kus.commitUpdate(vers);
			}
			log.debug("Persiting CIVersion");
			entityManager.persist(vers);
			
			return true;
			
		} else {
			log.error("Revision does not exist");
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#commit(kiwi.model.revision.Revision)
	 */
	public boolean rollback(CIVersion vers) {
		if(updateServicesContinuous == null || updateServicesDiscrete == null) {
			initialise();
		}
		if(vers != null) {
			
			for(KiWiUpdateService kus : updateServicesDiscrete) {
				kus.rollbackUpdate(vers);
			}
			for(KiWiUpdateService kus : updateServicesContinuous) {
				kus.rollbackUpdate(vers);
			}
			
			entityManager.persist(vers);
//			entityManager.flush();

			ContentItem revisedContentItem = null;
			if((revisedContentItem = vers.getRevisedContentItem()) == null) {
				log.error("Revised contentItem does not exist");
				return false;
			} else {
				// fixes: KIWI-244
				vers.setRevisedContentItem(revisedContentItem);
//				entityManager.flush();
				entityManager.refresh(revisedContentItem);
				
				return true;
			}
			
		} else {
			log.error("Revision does not exist");
			return false;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see kiwi.api.revision.RevisionService#createTextContentPreview(kiwi.model.revision.Revision, kiwi.api.revision.UpdateTextContentService.PreviewStyle, boolean)
	 */
	public String createPreview(CIVersion vers, PreviewStyle style, 
			boolean showDeleted) throws VersionDoesNotExistException, 
			ContentItemDoesNotExistException {
		ContentItem currentContentItem = (ContentItem) 
				Component.getInstance("currentContentItem");
		
		// currentContentItem mustn't be null
		if(currentContentItem != null) {
			String preview = "<div></div>";
			// get the revisions
			List<CIVersion> revisions = currentContentItem.getVersions();
			if(revisions == null) {
				throw new VersionDoesNotExistException("It seems that there " +
						"was a problem with persisting the revision " + 
						vers.getId());
			}
			// if revisions isn't null and contains the revision we want to see...
			if(revisions.contains(vers)) {
				CIVersion lastRev = revisions.get(revisions.size()-1);
				
				if(lastRev.getRevisedContentItem() == null) {
					throw new ContentItemDoesNotExistException("the revised " +
							"content item does not exist anymore");
				} else {
					UpdateTextContentService updateTextService = (UpdateTextContentService) 
						Component.getInstance("updateTextContentService");
					
					preview = updateTextService.createPreview(vers, style, showDeleted);
					
				}
			} else {
				throw new VersionDoesNotExistException("It seems that there " +
						"was a problem with persisting the revision " + 
						vers.getId());
			}
			return preview;
		} else {
			log.error("Current content item could not be injected");
			return null;
		}
	}
}
