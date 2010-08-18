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
 * Contributor(s): Jakub Kotowski
 * 
 * 
 */
package kiwi.service.reasoning;

import java.util.HashMap;
import java.util.Map;

import kiwi.api.config.ConfigurationService;
import kiwi.config.Configuration;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/** An auxiliary class to hold reasoning configuration.
 * 
 * @author Jakub Kotowski
 */
@Name("reasoningConfiguration")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class ReasoningConfiguration {
	public enum ReasoningFeature { 
		ONLINE_REASONING("kiwi.reasoning.onlineReasoning"), 
		HYBRID_REASONING("kiwi.reasoning.hybridReasoning");
	
		private final String configurationKey;
		
		ReasoningFeature(String configurationKey) {
			this.configurationKey = configurationKey;
		}

		public String getConfigurationKey() {
			return configurationKey;
		}
	}

	private static String ENABLED = "enabled";
	private static String DISABLED = "disabled";
	private Map<ReasoningFeature, Boolean> featureEnabled = new HashMap<ReasoningFeature, Boolean>();
	
	@In(value = "configurationService")
	private static ConfigurationService configurationService;
	
	@Create
	public void init() {
		for (ReasoningFeature feature : ReasoningFeature.values()) {
			String enabledDefault;
			if (feature.equals(ReasoningFeature.HYBRID_REASONING))
				enabledDefault = DISABLED;
			else
				enabledDefault = ENABLED;
			
			Configuration conf = configurationService.getConfiguration(feature.getConfigurationKey(), enabledDefault);
			featureEnabled.put(feature, ENABLED.equals(conf.getStringValue()));
		}
		
	}

	public boolean isEnabled(ReasoningFeature feature) {
		return featureEnabled.get(feature);
	}
	
	public void setEnabled(ReasoningFeature feature, boolean enabled) {
		String enabledStr = enabled ? ENABLED : DISABLED;
		
		configurationService.setConfiguration(feature.getConfigurationKey(), enabledStr);
		featureEnabled.put(feature, enabled);
	}
	
	public void enable(ReasoningFeature feature) {
		setEnabled(feature, true);
	}
	
	public void disable(ReasoningFeature feature) {
		setEnabled(feature, false);
	}	
}




