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
 * Szaby Gruenwald
 *
 */

package kiwi.api.config;

import java.util.List;

import kiwi.api.extension.KiWiApplication;
import kiwi.config.Configuration;
import kiwi.config.UserConfiguration;
import kiwi.exception.SystemNotYetSetUpException;
import kiwi.model.user.User;

/**
 * @author Sebastian Schaffert
 *
 */
public interface ConfigurationService {

    public void initialise() throws SystemNotYetSetUpException;

    /**
     * Get the start content item of this KiWi system.
     *
     * @return
     */
    public String getStartPage();

    /**
     * Get the base uri of the system, i.e. a uri that can be entered in the browser to access the
     * start page of this KiWi installation. The base uri is calculated based on the request URI
     * given by the user. The base uri is used by the KiWi system to generate new URIs of content
     * items. In this way, all KiWi resources are "Linked Open Data" compatible.
     *
     * @return
     */
    public String getBaseUri();


    /**
     * Get the server uri of the system, i.e. a uri that when entered in the browser accesses the
     * server that runs the KiWi (and SOLR) applications. Can be used to compute the paths of
     * other applications relative to the current application. Computed like the base uri.
     * @return
     */
    public String getServerUri();

    /**
     * Get the configuration for the given key. If there is no such configuration, a new one is
     * created with empty value (returns null).
     *
     * @param key  unique configuration key for lookup
     * @return a configuration object with either the configured value or null as value
     */
    public Configuration getConfiguration(String key);

    /**
     * Get the configuration for the given key. If there is no such configuration, a new one is
     * created using the provided defaultValue as string value.
     *
     * @param key unique configuration key for lookup
     * @param defaultValue default value if configuration not found
     * @return a configuration object with either the configured value or defaultValue
     */
    public Configuration getConfiguration(String key, String defaultValue);

    /**
     * Get the configuration for the given key. If there is no such configuration, a new one is
     * created using the provided defaultValue as double value.
     *
     * @param key unique configuration key for lookup
     * @param defaultValue default value if configuration not found
     * @return a configuration object with either the configured value or defaultValue
     */
    public Configuration getConfiguration(String key, double defaultValue);

    /**
     * Get the configuration for the given user and key. If there is no such configuration, a new one is
     * created with empty value (returns null).
     *
     * @param user  the user for whom to get the configuration
     * @param key  unique configuration key for lookup
     * @return a configuration object with either the configured value or null as value
     */
    public UserConfiguration getUserConfiguration(User user, String key);

    /**
     * Get the configuration for the given user and key. If there is no such configuration, a new one is
     * created using the provided defaultValue as string value.
     *
     * @param user  the user for whom to get the configuration
     * @param key unique configuration key for lookup
     * @param defaultValue default value if configuration not found
     * @return a configuration object with either the configured value or defaultValue
     */
    public UserConfiguration getUserConfiguration(User user, String key, String defaultValue);

    /**
     * Store the configuration passed as argument in the database.
     *
     * @param config
     */
    public void setConfiguration(Configuration config);

    /**
     * Set the configuration "key" to the string value "value".
     * @param key
     * @param value
     */
    public void setConfiguration(String key, String value);

    /**
     * Set the configuration "key" to the string value "value".
     * @param key
     * @param value
     */
    public void setConfiguration(String key, List<String> values);

    /**
     * Store the configuration passed as argument in the database.
     *
     * @param config
     */
    public void setUserConfiguration(UserConfiguration config);

    /**
     * Set the configuration "key" to the string value "value".
     * @param key
     * @param value
     */
    public void setUserConfiguration(User user, String key, String value);

    /**
     * Set the configuration "key" to the string value "value".
     * @param key
     * @param value
     */
    public void setUserConfiguration(User user, String key, List<String> values);


    /**
     * Remove the configuration identified by "key" from the database.
     * @param key
     */
    public void removeConfiguration(String key);

    /**
     * Remove the user configuration identified by "key" from the database.
     * @param key
     */
    public void removeUserConfiguration(User user, String key);

    /**
     * @return a string representation of work direction
     */
    public String getWorkDir();

    /**
     * For storing application specific settings, build the key for the configuration with a prefix.
     * @param app
     * @return
     */
    public String getApplicationPrefix(KiWiApplication app);

    /**
     * redirect to the IDP server,
     * which should then redirect to the FoafSslAuthenticationServlet
     */
    public void redirectToIDP();

	public void setUserRSAConfiguration(User user, String key, String value);

}
