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
package kiwi.action.webservice.thesaurusManagement;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import kiwi.api.importexport.ExportService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.kbase.KiWiTriple;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Name("kiwiResourceListWriter") 
@Scope(ScopeType.APPLICATION)
@Provider
@Produces("application/rdf+xml")
public class KiWiResourceListWriter implements MessageBodyWriter<ArrayList<KiWiTriple>> {

	@Logger
	private static Log log;
	
	@In(required = false)
	private WS ws;
	
	@Override
	public long getSize(ArrayList<KiWiTriple> arg0, Class<?> arg1,
			Type arg2, Annotation[] arg3, MediaType arg4) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
		Annotation[] annotations, MediaType mediaType) {
		//log.info(type.getComponentType().getName());	
		if( type.equals(ArrayList.class) && (ws == WS.ArrKTriples)) {
//			if(type.getComponentType() != null) {
//				System.out.println(type.getComponentType().getName());
//			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void writeTo(ArrayList<KiWiTriple> st, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, 
			OutputStream resourceStream)
			throws IOException, WebApplicationException {
		
		log.info("processingx ResourceListWriter");
		
		TripleStore   tripleStore = (TripleStore) Component.getInstance("tripleStore");
		ExportService      ies = (ExportService) Component.getInstance("kiwi.core.exportService"); 		
		
		if(tripleStore == null){
			log.info("Triplestore not initialized");
		}

		ies.exportTriples(st, "application/rdf+xml",resourceStream);
	}

}
