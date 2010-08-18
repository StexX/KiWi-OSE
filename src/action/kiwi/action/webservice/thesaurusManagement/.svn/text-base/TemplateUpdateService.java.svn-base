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

import java.util.ArrayList;
import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import kiwi.api.ontology.TemplateService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiTriple;
import net.wimpi.pim.util.Base64;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.log.Log;

@Path("/templateUpdateService")
@Name("templateUpdateService")
public class TemplateUpdateService {

	@Logger
	private static Log log;
	
	@In(create=true)
	private TemplateService templateService;
	
	@Out(required = false)
	private WS ws;
	
	/**
	 * RESTful webservice to query all available templates
	 * @return
	 */
	@GET
	@Path("template")
	@Produces("application/rdf+xml")
	public Response getTemplatesHTML() {
		log.debug("Queried all templates");
		LinkedList<ContentItem> templates = templateService.getTemplates();
		if(templates == null) {
			templates = new LinkedList<ContentItem>();
		}
		return Response.ok(templates).build();
	}
	
	/**
	 * RESTful webservice to query all fields of a template or template instance
	 * @param title, the title of the ContentItem
	 * @return
	 * 
	 * TODO: change from title to resource
	 */
	@GET
	@Path("template/{base64encodedurl}/fields")
	@Produces("application/rdf+xml")
	public Response getTemplateFieldsHTML(@PathParam("base64encodedurl") String base64encodedurl) {
	    	ws = WS.ArrKTriples;
		String url = new String(Base64.decode(base64encodedurl.getBytes()));
		log.debug("Queried all fields of template #0 ", url);
		ArrayList<KiWiTriple> instances = templateService.getTemplateFields(url);
		if(instances == null) {
			instances = new ArrayList<KiWiTriple>();
		}
		return Response.ok(instances).build();
	}
	
	/**
	 * RESTful webservice to query all fields of a template or template instance
	 * @param title, the title of the ContentItem
	 * @return
	 * 
	 * TODO: change from title to resource
	 */
	@GET
	@Path("template/test/fields")
	@Produces("application/rdf+xml")
	public Response getTemplateFieldsHTMLTest() {
		log.debug("Test query for fields ");
		ws = WS.ArrKTriples;
		ArrayList<KiWiTriple> instances = new ArrayList<KiWiTriple>();
		
		TripleStore triplestore = (TripleStore) Component.getInstance("tripleStore");
		
		instances.add(triplestore.createTriple(
				triplestore.createUriResource("http://ex.org/sub1"), 
				triplestore.createUriResource("http://ex.org/pred1"), 
				triplestore.createUriResource("http://ex.org/obj1")));
		instances.add(triplestore.createTriple(
				triplestore.createUriResource("http://ex.org/sub2"), 
				triplestore.createUriResource("http://ex.org/pred2"), 
				triplestore.createUriResource("http://ex.org/obj2")));
		if(instances == null) {
			instances = new ArrayList<KiWiTriple>();
		}
		return Response.ok(instances).build();
	}
	
	/**
	 * RESTful webservice to query all instances of a template
	 * @param title
	 * @return
	 */
	@GET
	@Path("template/{url}/instances")
	@Produces("application/rdf+xml")
	public Response getTemplateInstancesHTML(@PathParam("url") String url) {
		log.debug("Queried all instances of template #0 ", url);
		LinkedList<ContentItem> instances = templateService.getTemplateInstances(url);
		if(instances == null) {
			instances = new LinkedList<ContentItem>();
		}
		return Response.ok(instances).build();
	}
	
	public static void main(String[] args) {
		String url = "http://example.org/kiwi";
		byte[] test = Base64.encode(url.getBytes());
		System.out.println(new String(test));
	}
}
