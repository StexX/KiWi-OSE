/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 * 
 * Contributor(s):
 * 
 * 
 */

package kiwi.service.vmt;

import static kiwi.model.kbase.KiWiQueryLanguage.SPARQL;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.vmt.IVMTService;
import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * @author Sebastian Schaffert
 * 
 */
@Stateless
@Name("vmtService")
@AutoCreate
@Scope(ScopeType.STATELESS)
public class VMTServiceImpl implements IVMTService {

	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@Logger
	private static Log log;

	@In
	FacesMessages facesMessages;

	List<ContentItem> categories;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kiwi.backend.vmt.ISesameQuery#go()
	 */
	public List<ContentItem> getAllTopLabels() {
		Query query = kiwiEntityManager.createQuery(
				"SELECT ?C WHERE { ?X skos:hasTopConcept ?C }", SPARQL,
				ContentItem.class);
		categories = query.getResultList();
		return categories;
	}

	public List<ContentItem> getSubcategories(ContentItem selectedCategory) {

		List<ContentItem> subcategories = null;

		if (selectedCategory != null) {
			log.info("listing subcategories for category #0", selectedCategory
					.getTitle());
			Query query = kiwiEntityManager.createQuery(
					"SELECT ?C WHERE { ?C skos:broader :top }", SPARQL,
					ContentItem.class);
			query.setParameter("top", selectedCategory);
			subcategories = query.getResultList();
		} else {
			subcategories = new LinkedList<ContentItem>();
		}
		return subcategories;
	}
	
	public ContentItem getTopCategory(ContentItem selectedCategory) {

		ContentItem topCategory = null;
		if (selectedCategory != null) {
			log.info("listing TopCategroy for category #0", selectedCategory
					.getTitle());
			Query query = kiwiEntityManager.createQuery(
					"SELECT ?C WHERE { ?C skos:narrower :top }", SPARQL,
					ContentItem.class);
			query.setParameter("top", selectedCategory);
			topCategory = (ContentItem) query.getSingleResult();
		} else {
			// TODO: this is a bug, new ContentItems should always be created through contentItemService
			topCategory = new ContentItem();
		}
		return topCategory;
	}
	
	public List<ContentItem> getSynonyms(ContentItem selectedCategory) {

		List<ContentItem> subcategories = null;

		if (selectedCategory != null) {
			log.info("listing synonyms for category #0", selectedCategory
					.getTitle());
			Query query = kiwiEntityManager.createQuery(
					"SELECT ?C WHERE { :top owl:sameAs ?C }", SPARQL,ContentItem.class);
					//"SELECT ?C WHERE { ?C skos:broader :top }", SPARQL,
					//ContentItem.class);
			query.setParameter("top", selectedCategory);
			subcategories = query.getResultList();
		} else {
			subcategories = new LinkedList<ContentItem>();
		}
		return subcategories;
	}

	public boolean hasSubCategory(ContentItem selectedCategory) {
		boolean value;

		log.info("listing subcategories for category #0", selectedCategory
				.getTitle());
		Query query = kiwiEntityManager.createQuery(
				"SELECT ?C WHERE { ?C skos:broader :top }", SPARQL,
				ContentItem.class);
		query.setParameter("top", selectedCategory);

		try {
			query.getSingleResult();
			value = true;
		} catch (javax.persistence.NoResultException e) {
			value = false;
		}
		return value;
	}
	
	
	
//	public ContentItem getCategoryByID(){
		
//	}
}