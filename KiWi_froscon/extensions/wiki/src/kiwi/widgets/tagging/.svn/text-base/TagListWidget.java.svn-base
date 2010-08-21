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

package kiwi.widgets.tagging;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.tagging.Tag;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * @author Sebastian Schaffert
 *
 */
@Name("tagListWidget")
@Scope(ScopeType.EVENT)
public class TagListWidget implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8165086482901368870L;

	@Logger
	private static Log log;

    @In FacesMessages facesMessages;

	// the triple store used by this KiWi system
	@In(value = "tripleStore", create = true)
	private TripleStore tripleStore;

	// the entity manager used by this KiWi system
	@In
	private EntityManager entityManager;

    
    // search parameter: label
	@In
	private String label;

	
	@DataModel(value="tagList")	
	private Set<Tag> tags;
	
	
	@DataModelSelection
	private Tag currentTag;
	
	
	
	/**
	 * returns a tag for the parameter 'label'
	 */
	@Factory("tagList")
	public void listTags() {
		if(label != null) {
			javax.persistence.Query q = entityManager
					.createQuery("select t from kiwi.model.tagging.Tag t where t.label = :n");
			q.setParameter("n", label);
			tags = new HashSet<Tag>((Collection<? extends Tag>) q.getResultList());
			for(Tag t : tags) {
				tripleStore.refresh(t, false);
			}
		} else {
			javax.persistence.Query q = entityManager
					.createQuery("select t from kiwi.model.tagging.Tag t");
			tags = new HashSet<Tag>((Collection<? extends Tag>) q.getResultList());
			for(Tag t : tags) {
				tripleStore.refresh(t, false);
			}
		}
	}


}
