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
 package tagit2.action.query;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.config.ConfigurationService;
import kiwi.api.content.ContentItemService;
import kiwi.config.Configuration;
import kiwi.model.content.ContentItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

import tagit2.util.query.Layer;
import tagit2.util.query.LayerRangeUnit;

/**
 * this actions manages the layers. the user can define layers that are used by
 * <class>QueryAction</class>
 * @author tkurz
 *
 */
//@Transactional
@Scope(ScopeType.SESSION)
@Name("tagit2.layeringAction")
public class LayeringAction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Injections
	@Logger
	private Log log;
	
	@In
	private ConfigurationService configurationService;
	
	@In
	private ContentItemService contentItemService;
	
	//other properties
	/**
	 * all layers, that are defined by configuration Service
	 */
	private List<Layer> layers;
	/**
	 * all layers selected by user
	 */
	//private List<Layer> selectedLayers;
	/**
	 * the user can decide, if he wants to see all layers or just a part of it
	 */
	private boolean showAllLayers = false;
	
	public List<Layer> getLayers() {
		if(layers == null) {
			loadLayers();
		}
		return layers;
	}

	public void setLayers(List<Layer> layers) {
		this.layers = layers;
	}

//	public List<Layer> getSelectedLayers() {
//		if(layers == null) {
//			loadLayers();
//		}
//		return selectedLayers;
//	}
//
//	public void setSelectedLayers(List<Layer> selectedLayers) {
//		this.selectedLayers = selectedLayers;
//	}

	//Getters and Setters
	public void setShowAllLayers(boolean showAllLayers) {
		this.showAllLayers = showAllLayers;
	}

	public boolean isShowAllLayers() {
		return showAllLayers;
	}
	
	//methods
	/**
	 * is called before start. this method load all layers configured by
	 * configuration service
	 */
	private void loadLayers() {
		
		//initialize lists
		layers = new LinkedList<Layer>();
		//selectedLayers = new LinkedList<Layer>();
		
		//set configured layers
		Configuration cfg = configurationService.getConfiguration("tagit.layers");
		for(String uri : cfg.getListValue()) {
			ContentItem item = contentItemService.getContentItemByUri(uri);
			if(item != null) {
				Layer l = new Layer(item);
				setUnitAndTimerange(l);
				layers.add( l );
				//selectedLayers.add( l );
			}
		}
		log.info("retrieved #0 TagIT layers",layers.size());
	}

	/**
	 * this method returns a list of layers that must be considered by querying
	 * @return
	 */
	public String getLayerQuery() {
		return getLayerQueryByList( layers );
	}
	
	private String getLayerQueryByList( List<Layer> lay ) {
		if( !showAllLayers ) {
			StringBuilder qString = new StringBuilder();
			qString.append("(");
			boolean isFirst = true;
			for(Layer item : lay) {
				if( item.isShow() ) {
					if( !isFirst ) {
						qString.append(" OR ");
					}
					isFirst = false;
					qString.append(item.getQueryString(true));
				}
			}
			if( qString.length() == 1 ) {
				return null;
			} else {
				qString.append(")");
				return qString.toString();
			}
		} else {
			StringBuilder qString = new StringBuilder();
			qString.append("(");
			boolean isFirst = true;
			for(Layer item : lay) {
				if( !isFirst ) {
					qString.append(" OR ");
				}
				isFirst = false;
				qString.append(item.getQueryString(false));
			}
			if( qString.length() == 1 ) {
				return null;
			} else {
				qString.append(")");
				return qString.toString();
			}
		}
	}
	
	//TODO should be configurable
	private void setUnitAndTimerange(Layer layer) {
		String title = layer.getTitle();
		if( title.equals("Nachrichten") ) {
			layer.setRange(LayerRangeUnit.DAY);
			layer.setTimerange("2DAYS");
		} else if( title.equals("Blog Post") ) {
			layer.setRange(LayerRangeUnit.MONTH);
			layer.setTimerange("7DAYS");
		} else if( title.equals("Orte") ) {
			layer.setRange(LayerRangeUnit.DAY);
			layer.setTimerange("7DAYS");
		} else {
			layer.setRange(LayerRangeUnit.NONE);
		}
	}
	
	public String getCssDisplay() {
		if( showAllLayers ) {
			return "none";
		} else {
			return "block;";
		}
	}

}
