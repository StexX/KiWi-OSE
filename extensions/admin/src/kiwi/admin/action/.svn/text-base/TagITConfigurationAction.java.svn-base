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
package kiwi.admin.action;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.api.triplestore.TripleStore;
import kiwi.config.Configuration;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;

import org.hibernate.validator.Length;
import org.hibernate.validator.Range;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

/**
 * An action for changing the TagIT configuration.
 * <p>
 * Currently allows setting the google key and the default center position.
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.admin.tagitConfigurationAction")
@Scope(ScopeType.PAGE)
public class TagITConfigurationAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private Log log;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private ContentItemService contentItemService;
	
	@In
	private TripleStore tripleStore;
	
	@In
	private EntityManager entityManager;
	
	@Length(min=86,max=86)
	private String googleKey;
	
	
	@Range(min=-90, max=90)
	private double centerLat;
	
	@Range(min=-180, max=180)
	private double centerLng;
	
	@Range(min=0, max=20)
	private int zoom;
	
	@Range(min=100,max=1000)
	private int maxTrackpoints;

	private List<ContentItem> layers;
	
	private String addedLayer;
	
	private String geoCoder;
	
	private List<String> geoCoders;

	@Create
	public void begin() {
		googleKey = configurationService.getConfiguration("tagit.googlekey", Constants.GOOGLE_KEY).getStringValue();
		
		centerLat = configurationService.getConfiguration("tagit.center.lat", "47.8").getDoubleValue();
		centerLng = configurationService.getConfiguration("tagit.center.lng", "13.03").getDoubleValue();
		
		zoom      = configurationService.getConfiguration("tagit.zoom", "13").getIntValue();

		maxTrackpoints = configurationService.getConfiguration("tagit.route.maxTrackpoints", "100").getIntValue();
		
		geoCoder  = configurationService.getConfiguration("tagit.geocoder","Google").getStringValue();
		geoCoders = new LinkedList<String>();
		geoCoders.add("Google");
		geoCoders.add("Geonames");
		
		
		if(layers == null) {
			layers = new LinkedList<ContentItem>();
			Configuration cfg = configurationService.getConfiguration("tagit.layers");
			for(String uri : cfg.getListValue()) {
				ContentItem item = contentItemService.getContentItemByUri(uri);
				if(item != null) {
					layers.add(item);
				}
			}
			log.info("retrieved #0 TagIT layers",layers.size());
		}

	}
	
	
	public String submit() {
		configurationService.setConfiguration("tagit.googlekey", googleKey);
		configurationService.setConfiguration("tagit.center.lat", String.valueOf(centerLat));
		configurationService.setConfiguration("tagit.center.lng", String.valueOf(centerLng));
		configurationService.setConfiguration("tagit.zoom", String.valueOf(zoom));
		configurationService.setConfiguration("tagit.route.maxTrackpoints", String.valueOf(maxTrackpoints));
		configurationService.setConfiguration("tagit.geocoder",geoCoder);
		
		List<String> layerUris = new LinkedList<String>();
		for(ContentItem layer : layers) {
			layerUris.add(((KiWiUriResource)layer.getResource()).getUri());
		}
		configurationService.setConfiguration("tagit.layers", layerUris);
		
		
		log.debug("TagIT configuration: changed google key to #0",googleKey);
		log.debug("TagIT configuration: changed map center to #0,#1",centerLat,centerLng);
		log.debug("TagIT configuration: configured #0 layers",layerUris.size());
		
		return "success";
	}
	
	public void addLayer() {
		if(addedLayer != null && !"".equals(addedLayer)) {
			ContentItem item = contentItemService.getContentItemByTitle(addedLayer);
			if(item == null) {
				// create new Content Item of type "tag" if the tag does not yet exist
				item = contentItemService.createContentItem();
				item.addType(tripleStore.createUriResource(Constants.NS_TAGIT+"Layer"));
				contentItemService.updateTitle(item, addedLayer);
				entityManager.persist(item);
				tripleStore.persist(item);
				log.info("created new content item for non-existant layer");
			}
			layers.add(item);
			addedLayer = "";
		}
	}
	
	public void removeLayer(ContentItem layer) {
		layers.remove(layer);
	}

	public String getGoogleKey() {
		return googleKey;
	}

	public void setGoogleKey(String googleKey) {
		this.googleKey = googleKey;
	}

	public double getCenterLat() {
		return centerLat;
	}

	public void setCenterLat(double centerLat) {
		this.centerLat = centerLat;
	}

	public double getCenterLng() {
		return centerLng;
	}

	public void setCenterLng(double centerLng) {
		this.centerLng = centerLng;
	}


	public int getZoom() {
		return zoom;
	}


	public void setZoom(int zoom) {
		this.zoom = zoom;
	}


	public int getMaxTrackpoints() {
		return maxTrackpoints;
	}


	public void setMaxTrackpoints(int maxTrackpoints) {
		this.maxTrackpoints = maxTrackpoints;
	}


	public List<ContentItem> getLayers() {
		return layers;
	}


	public void setLayers(List<ContentItem> layers) {
		this.layers = layers;
	}
	
	
	
	public String getAddedLayer() {
		return addedLayer;
	}


	public void setAddedLayer(String addedLayer) {
		this.addedLayer = addedLayer;
	}


	public List<String> autocomplete(Object param) {
		javax.persistence.Query q = 
			entityManager.createNamedQuery("taggingAction.autocompleteClasses");
        q.setParameter("n", param.toString().toLowerCase()+"%");
        q.setHint("org.hibernate.cacheable", true);
		return q.getResultList();
	}


	/**
	 * @return the geoCoder
	 */
	public String getGeoCoder() {
		return geoCoder;
	}


	/**
	 * @param geoCoder the geoCoder to set
	 */
	public void setGeoCoder(String geoCoder) {
		this.geoCoder = geoCoder;
	}


	/**
	 * @return the geoCoders
	 */
	public List<String> getGeoCoders() {
		return geoCoders;
	}


	/**
	 * @param geoCoders the geoCoders to set
	 */
	public void setGeoCoders(List<String> geoCoders) {
		this.geoCoders = geoCoders;
	}
	
	
}
