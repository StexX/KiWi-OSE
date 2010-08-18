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

package kiwi.action.setup;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.importexport.ImportService;
import kiwi.api.setup.OntologyBean;
import kiwi.api.triplestore.TripleStore;
import kiwi.context.CurrentUserFactory;
import kiwi.model.kbase.KiWiDataFormat;
import kiwi.model.user.User;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

/**
 * @author Sebastian Schaffert
 *
 */
@Name("ontologyAction")
@Scope(ScopeType.CONVERSATION)
public class OntologyAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private static Log log;

    @In FacesMessages facesMessages;

	@In("kiwi.core.importService")
	private ImportService importService;

	@In
	private TripleStore tripleStore;	

	@DataModel
	private List<OntologyBean> setupOntologyList;

	private int progress = 0;

	private int progressMax = 1;

	private String status = "";
	
	private boolean clear = false;

	private boolean processing = false;

	/**
	 * A factory method for setting up the list of available ontologies.
	 *
	 * TODO: at a later state, this list should be generated from a configuration
	 */
	@Factory("setupOntologyList")
	public void initOntologyList() {
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
			
			OntologyBean ideator = new OntologyBean();
			ideator.setName("Ideator Ontology");
			ideator.setDescription("This is the Ideator ontology.");
			ideator.setUri("ideator/ideator.owl");
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

			OntologyBean icalCalendar = new OntologyBean();
			icalCalendar.setName("Calendar ontology / ical");
			icalCalendar.setDescription("W3C Calendar Ontology.");
			icalCalendar.setUri("imports/ical.rdf");
			icalCalendar.setFormat(KiWiDataFormat.RDFXML);
			icalCalendar.setLoad(false);
			setupOntologyList.add(icalCalendar);

//			OntologyBean kategorienBaum = new OntologyBean();
//			kategorienBaum.setName("Interedu Kategorienbaum (SKOS)");
//			kategorienBaum.setDescription("Interedu Kategorienbaum");
//			kategorienBaum.setUri("interedu/kategorienbaum.rdf");
//			kategorienBaum.setFormat(KiWiDataFormat.RDFXML);
//			kategorienBaum.setLoad(false);
//			setupOntologyList.add(kategorienBaum);
			
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

			/*
			OntologyBean wordnet_minimal = new OntologyBean();
			wordnet_minimal.setName("WordNet (Minimal)");
			wordnet_minimal.setDescription("WordNet minimal fragment (nouns with synonyms)");
			wordnet_minimal.setUri("wordnet/WordNetNounsWithSynonyms.rdf");
			wordnet_minimal.setFormat(KiWiDataFormat.RDFXML);
			wordnet_minimal.setLoad(false);
			setupOntologyList.add(wordnet_minimal);
			*/

			/*
			OntologyBean kiwiPages = new OntologyBean();
			kiwiPages.setName("KiWi Default Pages");
			kiwiPages.setDescription("KiWi Default Pages. Includes the FrontPage and the KiWi documentation. Loading these ontologies is mandatory.");
			kiwiPages.setUri("kiwi_pages.xml");
			kiwiPages.setFormat(KiWiDataFormat.KiWi);
			setupOntologyList.add(kiwiPages);
			*/
			
			OntologyBean demoThesaurus = new OntologyBean();
			demoThesaurus.setName("Demo Thesaurus");
			demoThesaurus.setDescription("Demo Thesaurus.");
			demoThesaurus.setUri("demo/demo_ont.owl");
			demoThesaurus.setFormat(KiWiDataFormat.RDFXML);
			demoThesaurus.setLoad(false);
			setupOntologyList.add(demoThesaurus);
		}
	}

	public void start() {
		// would use flushMode=MANUAL, but entityManager.persist automatically flushes when creating a new entity id
		if(!Conversation.instance().isNested()) {
			Conversation.instance().beginNested();
			Conversation.instance().setDescription("setupAction's nested conversation");
		}
	}

	@End
	public String commit() {
		return "success";
	}
	

	@Asynchronous
	public String startProgress(OntologyAction setupAction, User currentUser, Identity identity) {
		CurrentUserFactory currentUserFactory = 
			(CurrentUserFactory) Component.getInstance("currentUserFactory");
		currentUserFactory.setCurrentUser(currentUser);
		
		setupAction.setProcessing(true);
		int myProgressMax = 0;
		for(OntologyBean bean : setupAction.getSetupOntologyList()) {
			if(bean.isLoad()) {
				myProgressMax++;
			}
		}
		setupAction.setProgressMax(myProgressMax);
		setupAction.loadOntologies();
		setupAction.setProcessing(false);
		return null;
	}


	public String loadOntologies() {
		
		AsynchronousOntologyLoader loader = (AsynchronousOntologyLoader) Component.getInstance("kiwi.admin.ontologyLoader");
		
		if(setupOntologyList == null || setupOntologyList.size() == 0) {
			initOntologyList();
		}
		loader.loadOntologies(setupOntologyList, clear, (User) Component.getInstance("currentUser"), Identity.instance());
		
		int count = 0;
		for(OntologyBean ob : setupOntologyList) {
			if(ob.isLoad())
				count++;
		}
		facesMessages.add("#0 ontologies scheduled for background importing", count);
		
		/*		
		ReasoningService reasoner = (ReasoningService) Component.getInstance("kiwi.core.reasoningService");
		reasoner.setOnlineReasoningAllowed(false);
		
		try {
			Transaction.instance().setTransactionTimeout(60000);
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("current transaction: #0", Transaction.instance().hashCode());

		if(clear) {
			status = "clearing triplestore";
			// this is now transaction save
			importService.clearTripleStore();
		}

		progress = 0;
		for(OntologyBean bean : setupOntologyList) {
			if(bean.isLoad()) {
				log.info("loading ontology '#0'", bean.getName());
				status = "loading ontology "+bean.getName();
				importService.importData(Thread.currentThread().getContextClassLoader().getResourceAsStream(bean.getUri()), bean.getFormat());
				progress ++;
			} else {
				log.info("not loading ontology '#0'", bean.getName());
			}
		}

		reasoner.setOnlineReasoningAllowed(true);

		
		progress++;
		log.info("import finished successfully");
		*/
		return "success";		
	}

	public void deselectAll(){
		for(OntologyBean ontologyBean:setupOntologyList)
			ontologyBean.setLoad(false);
		setClear(false);
	}
	
	public void selectAll(){
		for(OntologyBean ontologyBean:setupOntologyList)
			ontologyBean.setLoad(true);
		setClear(false);
	}
	
	public void selectMandatory(){
		for(OntologyBean ontologyBean:setupOntologyList) {
			ontologyBean.setLoad(ontologyBean.isMandatory());
		}
		setClear(false);
	}
	
	public void reset(){
		setupOntologyList = null;
		initOntologyList();
		setClear(false);
	}
	
	/**
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}

	/**
	 * @return the progressMax
	 */
	public int getProgressMax() {
		return progressMax;
	}

	/**
	 * @param progressMax the progressMax to set
	 */
	public void setProgressMax(int progressMax) {
		this.progressMax = progressMax;
	}

	public String getStatus() {
		return status;
	}
	
	/**
	 * @return the clear
	 */
	public boolean isClear() {
		return clear;
	}

	/**
	 * @param clear the clear to set
	 */
	public void setClear(boolean clear) {
		this.clear = clear;
	}

	/**
	 * @return the processing
	 */
	public boolean isProcessing() {
		return processing;
	}

	/**
	 * @param processing the processing to set
	 */
	public void setProcessing(boolean processing) {
		this.processing = processing;
	}

	/**
	 * @return the setupOntologyList
	 */
	public List<OntologyBean> getSetupOntologyList() {
		return setupOntologyList;
	}


	public long size() {
		//tripleStore.flush();
		return tripleStore.size();
	}
}
