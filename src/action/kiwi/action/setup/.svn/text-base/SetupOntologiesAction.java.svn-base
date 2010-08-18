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
 * sschaffe
 * 
 */
package kiwi.action.setup;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.setup.OntologyBean;
import kiwi.api.system.StatusService;
import kiwi.model.kbase.KiWiDataFormat;
import kiwi.model.status.SystemStatus;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.remoting.WebRemote;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

/**
 * SetupOntologiesAction - load initial ontologies into the KiWi system.
 *
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.setup.setupOntologiesAction")
@Scope(ScopeType.CONVERSATION)
public class SetupOntologiesAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private static Log log;

    @In FacesMessages facesMessages;
    
    @In("kiwi.core.statusService")
    private StatusService statusService;

	private List<OntologyBean> setupOntologyList;
	
	private boolean loading = false;
	
	private int count;

	/**
	 * A factory method for setting up the list of available ontologies.
	 *
	 * TODO: at a later state, this list should be generated from a configuration
	 */
	@Create
	@Begin(join=true)
	public void begin() {
		
		if(setupOntologyList == null) {
			setupOntologyList = new LinkedList<OntologyBean>();

			OntologyBean kiwiCore = new OntologyBean();
			kiwiCore.setName("KiWi Core");
			kiwiCore.setDescription("KiWi Core Ontology. Loading this ontology is mandatory.");
			kiwiCore.setUri("ontology_kiwi.owl");
			kiwiCore.setFormat(KiWiDataFormat.RDFXML);
			kiwiCore.setLoad(true);
			kiwiCore.setMandatory(true);
			setupOntologyList.add(kiwiCore);

			OntologyBean ceq = new OntologyBean();
			ceq.setName("Community Equity");
			ceq.setDescription("The Community Equity (CEQ) RDF vocabulary, described using W3C RDF Schema and the Web Ontology Language.");
			ceq.setUri("ceq/ceq.owl");
			ceq.setFormat(KiWiDataFormat.RDFXML);
			ceq.setLoad(true);
			ceq.setMandatory(true);
			setupOntologyList.add(ceq);

			OntologyBean foaf = new OntologyBean();
			foaf.setName("Friend of a Friend");
			foaf.setDescription("The Friend of a Friend (FOAF) RDF vocabulary, described using W3C RDF Schema and the Web Ontology Language.");
			foaf.setUri("imports/foaf.owl");
			foaf.setFormat(KiWiDataFormat.RDFXML);
			foaf.setLoad(true);
			foaf.setMandatory(true);
			setupOntologyList.add(foaf);
			
			OntologyBean ideator = new OntologyBean();
			ideator.setName("Ideator Ontology");
			ideator.setDescription("This is the Ideator ontology.");
			ideator.setUri("ideator/ideator_ont.owl");
			ideator.setFormat(KiWiDataFormat.RDFXML);
			ideator.setLoad(true);
			setupOntologyList.add(ideator);
			
			OntologyBean ideatorThesaurus = new OntologyBean();
			ideatorThesaurus.setName("Ideator Thesaurus");
			ideatorThesaurus.setDescription("This is the Ideator Thesaurus.");
			ideatorThesaurus.setUri("ideator/ideator_thesaurus.owl");
			ideatorThesaurus.setFormat(KiWiDataFormat.RDFXML);
			ideatorThesaurus.setLoad(true);
			setupOntologyList.add(ideatorThesaurus);
			
			OntologyBean calendar = new OntologyBean();
			calendar.setName("Simple Calendar ontology");
			calendar.setDescription("Calendar Ontology.");
			calendar.setUri("imports/calendar.owl");
			calendar.setFormat(KiWiDataFormat.RDFXML);
			calendar.setLoad(true);
			setupOntologyList.add(calendar);
			
			OntologyBean sioc = new OntologyBean();
			sioc.setName("SIOC Core Ontology");
			sioc.setDescription("SIOC (Semantically-Interlinked Online Communities) is an ontology for describing the information in online communities.");
			sioc.setUri("imports/sioc.owl");
			sioc.setFormat(KiWiDataFormat.RDFXML);
			sioc.setLoad(true);
			sioc.setMandatory(true);
			setupOntologyList.add(sioc);

			OntologyBean contact = new OntologyBean();
			contact.setName("Contact Ontology");
			contact.setDescription("Contact: Utility concepts for everyday life.");
			contact.setUri("imports/contact.owl");
			contact.setFormat(KiWiDataFormat.RDFXML);
			contact.setLoad(true);
			setupOntologyList.add(contact);

			OntologyBean hgtags = new OntologyBean();
			hgtags.setName("HG Tags");
			hgtags.setDescription("An ontology that describes tags, as used in the popular del.icio.us and Flickr systems, and allows for relationships between tags to be described.");
			hgtags.setUri("imports/hgtags.owl");
			hgtags.setFormat(KiWiDataFormat.RDFXML);
			hgtags.setLoad(true);
			hgtags.setMandatory(true);
			setupOntologyList.add(hgtags);

			OntologyBean skos_core = new OntologyBean();
			skos_core.setName("SKOS Core");
			skos_core.setDescription("SKOS Simple Knowledge Organization System. W3C Recommendation 18 August 2009.");
			skos_core.setUri("imports/skos-core.rdf");
			skos_core.setFormat(KiWiDataFormat.RDFXML);
			skos_core.setLoad(true);
			skos_core.setMandatory(true);
			setupOntologyList.add(skos_core);

			OntologyBean exif = new OntologyBean();
			exif.setName("EXIF");
			exif.setDescription("An ontology to describe an Exif format picture data. All Exif 2.2 tags are defined as OWL properties (annotated by 'exif:exifNumber' in this vocabulary), as well as several terms to help this schema.");
			exif.setUri("imports/exif.rdf");
			exif.setFormat(KiWiDataFormat.RDFXML);
			exif.setLoad(true);
			setupOntologyList.add(exif);

			OntologyBean tagitCore = new OntologyBean();
			tagitCore.setName("TagIT Core");
			tagitCore.setDescription("TagIT Core Ontology. Includes support for the TagIT specific relations.");
			tagitCore.setUri("tagit/tagit.owl");
			tagitCore.setFormat(KiWiDataFormat.RDFXML);
			tagitCore.setLoad(true);
			setupOntologyList.add(tagitCore);
			
			OntologyBean intereduCore = new OntologyBean();
			intereduCore.setName("Interedu Core");
			intereduCore.setDescription("Interedu Core Ontology. Includes support for the Interedu Artikel.");
			intereduCore.setUri("interedu/interedu.owl");
			intereduCore.setFormat(KiWiDataFormat.RDFXML);
			intereduCore.setLoad(false);
			setupOntologyList.add(intereduCore);

			OntologyBean iptc_newscodes = new OntologyBean();
			iptc_newscodes.setName("IPTC Newscodes (SKOS)");
			iptc_newscodes.setDescription("IPTC Newscodes");
			iptc_newscodes.setUri("thesaurus/thesaurus.rdf");
			iptc_newscodes.setFormat(KiWiDataFormat.RDFXML);
			iptc_newscodes.setLoad(false);
			setupOntologyList.add(iptc_newscodes);

			OntologyBean geo = new OntologyBean();
			geo.setName("WGS84 Geo Positioning");
			geo.setDescription("A vocabulary for representing latitude, longitude and altitude information in the WGS84 geodetic reference datum.");
			geo.setUri("imports/geo.rdf");
			geo.setFormat(KiWiDataFormat.RDFXML);
			geo.setLoad(true);
			setupOntologyList.add(geo);

			OntologyBean geonames = new OntologyBean();
			geonames.setName("Geonames Lite");
			geonames.setDescription("A vocabulary for representing information about locations in the Geonames Web Service; complements WGS84");
			geonames.setUri("imports/geonames_v2.0_lite.rdf");
			geonames.setFormat(KiWiDataFormat.RDFXML);
			geonames.setLoad(false);
			setupOntologyList.add(geonames);
			
			OntologyBean artaround = new OntologyBean();
			artaround.setName("Artaround Ontology");
			artaround.setDescription("This is the artaround ontology.");
			artaround.setUri("artaround/artaround.owl");
			artaround.setFormat(KiWiDataFormat.RDFXML);
			artaround.setLoad(true);
			setupOntologyList.add(artaround);
			
			OntologyBean artaroundThesaurus = new OntologyBean();
			artaroundThesaurus.setName("Artaround Thesaurus");
			artaroundThesaurus.setDescription("This is the artaround Thesaurus.");
			artaroundThesaurus.setUri("artaround/artaround_thesaurus.owl");
			artaroundThesaurus.setFormat(KiWiDataFormat.RDFXML);
			artaroundThesaurus.setLoad(true);
			setupOntologyList.add(artaroundThesaurus);
			
			OntologyBean icalCalendar = new OntologyBean();
			icalCalendar.setName("Calendar ontology / ical");
			icalCalendar.setDescription("W3C Calendar Ontology.");
			icalCalendar.setUri("imports/ical.rdf");
			icalCalendar.setFormat(KiWiDataFormat.RDFXML);
			icalCalendar.setLoad(false);
			setupOntologyList.add(icalCalendar);

			OntologyBean kategorienBaumLight = new OntologyBean();
			kategorienBaumLight.setName("Interedu Kategorienbaum  (SKOS)");
			kategorienBaumLight.setDescription("Interedu Kategorienbaum, very simplified version for testing purposes");
			kategorienBaumLight.setUri("interedu/kategorienbaum.rdf");
			kategorienBaumLight.setFormat(KiWiDataFormat.RDFXML);
			kategorienBaumLight.setLoad(false);
			setupOntologyList.add(kategorienBaumLight);

			OntologyBean logica = new OntologyBean();
			logica.setName("Logica");
			logica.setDescription("KiWi Logica Ontology. Based on the CMM model of Logica.");
			logica.setUri("logica/logica.owl");
			logica.setFormat(KiWiDataFormat.RDFXML);
			logica.setLoad(false);
			setupOntologyList.add(logica);

			OntologyBean riskCore = new OntologyBean();
			riskCore.setName("Risk Core");
			riskCore.setDescription("Risk Core Ontology. Includes support for the Risk specific forms.");
			riskCore.setUri("logica/risk.owl");
			riskCore.setFormat(KiWiDataFormat.RDFXML);
			riskCore.setLoad(false);
			setupOntologyList.add(riskCore);
			
			OntologyBean ways2know = new OntologyBean();
			ways2know.setName("Ways2Know");
			ways2know.setDescription("Ways2Know OWL Ontology.");
			ways2know.setUri("ways2know/waysknow.owl");
			ways2know.setFormat(KiWiDataFormat.RDFXML);
			ways2know.setLoad(false);
			setupOntologyList.add(ways2know);

			OntologyBean ways2knowData = new OntologyBean();
			ways2knowData.setName("Ways2Know Data Ontology");
			ways2knowData.setDescription("Ways2Know Data Ontology.");
			ways2knowData.setUri("ways2know/waysknow-data.owl");
			ways2knowData.setFormat(KiWiDataFormat.RDFXML);
			ways2knowData.setLoad(false);
			setupOntologyList.add(ways2knowData);
			
			OntologyBean ways2knowThesaurus = new OntologyBean();
			ways2knowThesaurus.setName("Ways2Know Thesaurus");
			ways2knowThesaurus.setDescription("Ways2Know Thesaurus.");
			ways2knowThesaurus.setUri("ways2know/futuremobility.owl");
			ways2knowThesaurus.setFormat(KiWiDataFormat.RDFXML);
			ways2knowThesaurus.setLoad(false);
			setupOntologyList.add(ways2knowThesaurus);
			
			OntologyBean demoThesaurus = new OntologyBean();
			demoThesaurus.setName("Demo Thesaurus");
			demoThesaurus.setDescription("Demo Thesaurus.");
			demoThesaurus.setUri("demo/demo_ont.owl");
			demoThesaurus.setFormat(KiWiDataFormat.RDFXML);
			demoThesaurus.setLoad(false);
			setupOntologyList.add(demoThesaurus);

		}
	}


	public String loadOntologies() {
		
		AsynchronousOntologyLoader loader = (AsynchronousOntologyLoader) Component.getInstance("kiwi.admin.ontologyLoader");
		
		loader.loadOntologies(setupOntologyList, false, (User) Component.getInstance("currentUser"), Identity.instance());
		
		count = 0;
		for(OntologyBean ob : setupOntologyList) {
			if(ob.isLoad())
				count++;
		}
		loading = true;
		
		facesMessages.add("#0 ontologies scheduled for background importing", count);
		log.info("#0 ontologies scheduled for background importing", count);
		
		return "success";
	}
	
	@End
	public String next() {
		log.info("load data ...");
		return "/setup/setupData.xhtml";
	}
	
	private int progress = 0;
	
	@WebRemote
	public int getProgress() {
		SystemStatus status = statusService.getSystemStatus("ontology import");
		
		if(status != null) {
			progress = status.getProgress();
			return progress;
		} else {
			if(progress == 0) {
				return progress;
			} else {
				return 100;
			}
		}
		
	}
	
	@WebRemote
	public String getProgressMessage() {
		SystemStatus status = statusService.getSystemStatus("ontology import");
		
		if(status != null) {
			return status.getMessage() + " ("+status.getProgress()+"%) ..." ;
		} else {
			return "";
		}
	}
	

	public void deselectAll(){
		for(OntologyBean ontologyBean:setupOntologyList)
			ontologyBean.setLoad(false);
	}
	
	public void selectAll(){
		for(OntologyBean ontologyBean:setupOntologyList)
			ontologyBean.setLoad(true);
	}
	
	public void selectMandatory(){
		for(OntologyBean ontologyBean:setupOntologyList) {
			ontologyBean.setLoad(ontologyBean.isMandatory());
		}
	}
	
	public void reset(){
		setupOntologyList = null;
		begin();
	}
	
	/**
	 * @return the setupOntologyList
	 */
	public List<OntologyBean> getSetupOntologyList() {
		return setupOntologyList;
	}


	/**
	 * @return the loading
	 */
	public boolean isLoading() {
		return loading;
	}


	/**
	 * @param loading the loading to set
	 */
	public void setLoading(boolean loading) {
		this.loading = loading;
	}


	
}
