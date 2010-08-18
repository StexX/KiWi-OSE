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

package logica.action.risk;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.render.RenderingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.user.User;
import logica.api.risk.RiskFacade;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

/**
 * @author devadmin
 * 
 */
//@Transactional
@Scope(ScopeType.CONVERSATION)
@Name("formRisk")
public class FormRisk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	Log log;

	private RiskFacade risk;

	@In(required = false)
	@Out(required = false)
	private ContentItem currentContentItem;

	@In
	private KiWiEntityManager kiwiEntityManager;

	@In(value = "configurationService")
	private ConfigurationService configurationService;

	@In
	private TripleStore tripleStore;

	@DataModel
	private List<RiskFacade> risks;

	@DataModelSelection
	@Out(required = false)
	private RiskFacade selectedRisk;

	private String riskTitle;

	private String riskGroup;

	private String riskStatus;

	private Date dateIdentified;

	private String dateIdentifiedString;

	private Date dateLastReview;

	private String dateLastReviewString;

	private String likelihood;

	private String categoryName;

	private String currentContentHtml;

	@In
	private RenderingService renderingPipeline;

	private String htmlRef;

	@In(create = true)
	private User currentUser;

	@In
	private ContentItemService contentItemService;

	@In
	private EntityManager entityManager;

	@Factory("risks")
	public void risks() {
		log.info("loading risks ...");
		javax.persistence.Query q = kiwiEntityManager.createQuery(
				"SELECT ?risk WHERE { ?risk <" + Constants.NS_RDF + "type> <"
						+ Constants.NS_LOGICA + "Risk> }",
				KiWiQueryLanguage.SPARQL, RiskFacade.class);
		risks = (List<RiskFacade>) q.getResultList();
		log.info(risks.size());
	}

	public void save() {
		log.info("Content will be saved");
		risk = kiwiEntityManager.createFacade(new ContentItem(),
				RiskFacade.class);
		risk.setResource(tripleStore.createUriResource(configurationService
				.getBaseUri()
				+ "/logica/risk/"
				+ Long.toHexString(System.currentTimeMillis())));

		log.info("Riskname #0, RiskCategory #1", riskTitle, riskGroup);

		if (riskGroup != null) {
			risk.setRiskGroup(riskGroup);
		}
		if (riskTitle != null) {
			contentItemService.updateTitle(risk, riskTitle);
		}
		if (riskStatus != null) {
			risk.setRiskStatus(riskStatus);
		}
		if (likelihood != null) {
			risk.setLikelihood(likelihood);
		}
		DateFormat df = new SimpleDateFormat();
		if (dateIdentified != null) {
			dateIdentifiedString = df.format(dateIdentified);
			log.info(dateIdentifiedString);
			risk.setDateIdentified(dateIdentifiedString);
		}
		if (dateLastReview != null) {
			dateLastReviewString = df.format(dateLastReview);
			log.info(dateLastReviewString);
			risk.setDateLastReview(dateLastReviewString);
		}

		risk.setFileReference("details.xhtml");
		kiwiEntityManager.persist(risk);
//		kiwiEntityManager.flush();
		FacesMessages.instance().add("New entry successfully added");
	}

	public void update() {

		for (Iterator iterator = risks.iterator(); iterator.hasNext();) {

			RiskFacade risk = (RiskFacade) iterator.next();

			if (likelihood != null) {
				risk.setLikelihood(likelihood);
			}

			if (categoryName != null) {
				risk.setCategory(categoryName);
			}
		}
		FacesMessages.instance().add("Entries updated");
	}



	public void begin() {

		if (currentContentItem != null && risk == null) {
			risk = kiwiEntityManager.createFacade(currentContentItem,
					RiskFacade.class);
			log.info(risk.getTitle());
			log.info(risk.getCreated());
			log.info(risk.getRiskStatus());
			log.info(risk.getFileReference());
		} else {
			// risk is the created risk and not the currentContentItem and must be set to null 
			risk = null;
		}

		if (currentContentItem.getTextContent() != null) {
			currentContentHtml = renderingPipeline.renderHTML(currentContentItem);
			log.info("rendering html content: #0", currentContentHtml);
		} else {
			currentContentHtml = "<p>Please add initial content</p>";
		}

		if (risk != null) {
			if (risk.getFileReference() != null) {
				log.info("Reference exists");
				htmlRef = risk.getFileReference();
				log.info(htmlRef);
			}
		}
	}

	public String getHtmlRef() {
		return htmlRef;
	}

	public void setHtmlRef(String htmlRef) {
		this.htmlRef = htmlRef;
	}

	public List<RiskFacade> getRisks() {
		return risks;
	}

	public String getRiskTitle() {
		return riskTitle;
	}

	public void setRiskTitle(String riskTitle) {
		this.riskTitle = riskTitle;
	}

	public String getRiskGroup() {
		return riskGroup;
	}

	public void setRiskGroup(String riskGroup) {
		this.riskGroup = riskGroup;
	}

	public String getRiskStatus() {
		return riskStatus;
	}

	public void setRiskStatus(String riskStatus) {
		this.riskStatus = riskStatus;
	}

	public Date getDateIdentified() {
		return dateIdentified;
	}

	public void setDateIdentified(Date dateIdentified) {
		this.dateIdentified = dateIdentified;
	}

	public void setDateLastReview(Date dateLastReview) {
		this.dateLastReview = dateLastReview;
	}

	public Date getDateLastReview() {
		return dateLastReview;
	}

	public String getLikelihood() {
		return likelihood;
	}

	public void setLikelihood(String likelihood) {
		this.likelihood = likelihood;
	}

	public String getDateIdentifiedString() {
		return dateIdentifiedString;
	}

	public void setDateIdentifiedString(String dateIdentifiedString) {
		this.dateIdentifiedString = dateIdentifiedString;
	}

	public String getDateLastReviewString() {
		return dateLastReviewString;
	}

	public void setDateLastReviewString(String dateLastReviewString) {
		this.dateLastReviewString = dateLastReviewString;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public RiskFacade getSelectedRisk() {
		return selectedRisk;
	}

	public void setSelectedRisk(RiskFacade selectedRisk) {
		this.selectedRisk = selectedRisk;
	}

	public String getCurrentContentHtml() {
		return currentContentHtml;
	}

	public void setCurrentContentHtml(String currentContentHtml) {
		this.currentContentHtml = currentContentHtml;
	}
}
