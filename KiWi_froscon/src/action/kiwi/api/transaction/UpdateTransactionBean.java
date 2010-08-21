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
package kiwi.api.transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiEntity;
import kiwi.model.kbase.KiWiNamespace;
import kiwi.model.revision.ConfigurationUpdate;

/**
 * A storage bean that holds data which is needed when the transaction completes
 * 
 * @author Stephanie Stroka (sstroka@salzburgresearch.at)
 * 
 * Changes:
 * 03/08/09: changed data structures back from list to set, because we need to be careful regarding
 *           performance here; methods in UpdateTransactionBean are called *very* frequently (Sebastian Schaffert)
 *
 */
public class UpdateTransactionBean {
	
	/**
	 * Each thread gets its own UpdateTransactionBean for keeping track of all changes happening 
	 * inside the thread and transaction. We use a ThreadLocal storage to store different instances 
	 * local to the respective thread.
	 */
	private static ThreadLocal<UpdateTransactionBean> instance = new ThreadLocal<UpdateTransactionBean>();
	
	// changed back to Set, because what you say is not correct (see explanation in 
	// KiWiSynchronization) and the code with the indexes didn't work anyways; Sets are here
	// because they give considerably better performance
	
	private Set<KiWiEntity> 			entities;
	
	// the revision that will be created
	private Map<ContentItem,CIVersionBean> contentItemVersionMap;
	
	private ConfigurationUpdate configurationUpdate;
	
	private Set<KiWiNamespace> removedNS;
	
	private Set<KiWiNamespace> addedNS;
	
	// the number of retries that can be done if the transaction fails
	public static final long timeout = 5;
	
	private long retryCount;
	
	// the content item of the current transaction
//	private CIVersion originCIVersion;
	
//	private ContentItem currentContentItem;
	
	/**
	 * 
	 */
	public UpdateTransactionBean() {
		this.entities = new HashSet<KiWiEntity>();
		
		addedNS = new HashSet<KiWiNamespace>();
		removedNS = new HashSet<KiWiNamespace>();
		
		retryCount = 0;
	}

	/**
	 * @return the entities
	 */
	public Set<KiWiEntity> getEntities() {
		return entities;
	}

	/**
	 * @param entities the entities to set
	 */
	public void setEntities(Set<KiWiEntity> entities) {
		this.entities = entities;
	}
	/**
	 * @param configurationUpdate the configurationUpdate to set
	 */
	public void setConfigurationUpdate(ConfigurationUpdate configurationUpdate) {
		this.configurationUpdate = configurationUpdate;
	}

	/**
	 * @return the configurationUpdate
	 */
	public ConfigurationUpdate getConfigurationUpdate() {
		return configurationUpdate;
	}
	
	public void addTransactionAddedNamespaces(KiWiNamespace ns) {
		addedNS.add(ns);
	}

	public Set<KiWiNamespace> getTransactionAddedNamespaces() {
		return addedNS;
	}
	
	public void addTransactionRemovedNamespaces(KiWiNamespace ns) {
		removedNS.add(ns);
	}

	public Set<KiWiNamespace> getTransactionRemovedNamespaces() {
		return removedNS;
	}
	
	/**
	 * remove a namespace from the list of namespaces that 
	 * have been removed during the transaction
	 * @param namespace
	 * @return
	 */
	public boolean removeTransactionRemovedNamespaces(KiWiNamespace ns) {
		return removedNS.remove(ns);
	}
	
	/**
	 * remove a namespace from the list of namespaces that 
	 * have been added during the transaction
	 * @param namespace
	 * @return
	 */
	public boolean removeTransactionAddedNamespaces(KiWiNamespace ns) {
		return addedNS.remove(ns);
	}
	
	/**
	 * @return the contentItemVersionMap
	 */
	public Map<ContentItem, CIVersionBean> getContentItemVersionMap() {
		if(contentItemVersionMap == null) {
			contentItemVersionMap = new HashMap<ContentItem, CIVersionBean>();
		}
		return contentItemVersionMap;
	}


	/**
	 * @param contentItemVersionMap the contentItemVersionMap to set
	 */
	public void setContentItemVersionMap(
			Map<ContentItem, CIVersionBean> contentItemVersionMap) {
		this.contentItemVersionMap = contentItemVersionMap;
	}

	/**
	 * @return the originCIVersion
	 * @throws CyclicDependencyException 
	 */
//	public CIVersion getOriginCIVersion() throws CyclicDependencyException {
//		if(originCIVersion == null) {
//			if(contentItemVersionMap.values() != null
//					&& contentItemVersionMap.values().size() > 0) {
//				List<CIVersionBean> sortChilds = new ArrayList<CIVersionBean>();
//				sortChilds.addAll(contentItemVersionMap.values());
//				originCIVersion = sortChilds.get(0).getCIVersion();
//			} else {
//				CIVersionService civ = (CIVersionService) Component.getInstance("ciVersionService");
//				//ContentItem currentContentItem = (ContentItem) Component.getInstance("currentContentItem");
//				CIVersion version = civ.createCIVersion(currentContentItem);
//				originCIVersion = version;
//			}
//		}
//		return originCIVersion;
//	}

	/**
	 * @param originCIVersion the originCIVersion to set
	 */
//	public void setOriginCIVersion(CIVersion originCIVersion) {
//		this.originCIVersion = originCIVersion;
//	}
	
	
//	public ContentItem getCurrentContentItem() {
//		return currentContentItem;
//	}
//
//	public void setCurrentContentItem(ContentItem currentContentItem) {
//		this.currentContentItem = currentContentItem;
//	}

	/**
	 * @param retryCount the retryCount to set
	 */
	public void setRetryCount(long retryCount) {
		this.retryCount = retryCount;
	}

	/**
	 * @return the retryCount
	 */
	public long getRetryCount() {
		return retryCount;
	}

	/**
	 * Return a thread-local instance of the UpdateTransactionBean.
	 * @return
	 */
	public static UpdateTransactionBean getInstance() {
		return instance.get();
	}
	
	public static void setInstance(UpdateTransactionBean utb) {
		instance.set(utb);
	}
	
	public static void clearInstance() {
		instance.remove();
	}
}
