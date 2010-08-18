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
package kiwi.extension;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * Used to load properties files and transform then in
 * <code>java.util.Map</code> where each map entry correspond
 * whit an entry in the properties file.<br>
 * More precisely this class loads all the properties files
 * <ul>
 * <li>which are placed in a certain package (specified in the
 * constructor)
 * <li>the the file name ends with "-descriptor".
 * <ul>
 * The usage is simple just instantiate the class with the proper
 * package name and then you can use the
 * <code>getProperties</code> method to load the the needed
 * properties.
 *
 * @author Mihai Raduelscu
 * @version 00.01-16
 * @since 00.01-16
 */
final class PropertiesLoader {

    /**
     * The extension for the file descriptor file.
     */
    private static final String EXTENSION = "-descriptor.properties";

    /**
     * The base for this factory, from here are all the file
     * descriptors loaded.
     */
    private final String base;

    /**
     * The class loader used to load the properties.
     */
    private final ClassLoader classLoader;

    /**
     * Builds a <code>PropertiesLoader</code> for a given
     * package.
     *
     * @param pack the package from where the properties are
     *            loaded, it can be null.
     * @throws NullPointerException if the <code>pack</code> is
     *             null.
     */
    PropertiesLoader(String pack) {
        if (pack == null) {
            throw new NullPointerException("The package is null.");
        }

        this.base = pack;
        classLoader = getClass().getClassLoader();
    }

    /**
     * Loads all the properties files from the package specified
     * in the constructor and transform them in
     * <code>java.util.Map</code>. If this map returns an empty
     * list then there are no descriptor files in the specified
     * package.
     *
     * @return a list which contains all the properties files
     *         (from the given package) transformed in Map
     *         instances.
     * @throws IOException by any kind of io error.
     */
    List<Map<String, String>> getProperties() throws IOException {
        final Set<String> descriptorList = getDescriptorSet();
        final List<Map<String, String>> result =
                new ArrayList<Map<String, String>>();

        final Properties properties = new Properties();
        for (final String descriptor : descriptorList) {
            properties.clear();
            final InputStream istreamStream =
                    classLoader.getResourceAsStream(descriptor);
            properties.load(istreamStream);
            // I use raw type here because I deal onyly with
            // Strings -> I deal with properties files.
            final HashMap toAdd = new HashMap();
            toAdd.putAll(properties);
            result.add(Collections.unmodifiableMap(toAdd));
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * Find all the descriptors which are matching the specified
     * filter.
     *
     * @param filter the filter for this load session.
     * @return a <code>Set</code> which contains all the
     *         canonical names for all the descriptors which are
     *         matching the specified filter.
     * @throws IOException If I/O errors occur during the load
     *             process.
     */
    private Set<String> getDescriptorSet() throws IOException {

        final List<URL> entries = new ArrayList<URL>();
        final String path = base.replace('.', '/');

        for (final Enumeration<URL> urls = classLoader.getResources(path); urls
                .hasMoreElements();) {

            final URL url = urls.nextElement();
            entries.add(url);
        }

        final Set<String> descriptors = new HashSet<String>();
        for (final URL entry : entries) {

            if ("jar".equalsIgnoreCase(entry.getProtocol())) {
                final URLConnection connection = entry.openConnection();
                final Set<String> pluginNames =
                        getDescriptorNames((JarURLConnection) connection);

                descriptors.addAll(pluginNames);
            } else if ("file".equalsIgnoreCase(entry.getProtocol())) {
                final Set<String> pluginNames =
                        getDescriptorNames(entry);
                descriptors.addAll(pluginNames);
            }
        }

        return descriptors;
    }

    /**
     * Extracts from a specified <code>URL</code> all the
     * descriptors which are matching the specified filter and
     * store them in to a <code>Set</code>. If the set is empty
     * then the specified <code>JarURLConnection</code> does not
     * contains any descriptors.
     *
     * @param url the URL.
     * @param filter the filter for the descriptors.
     * @return a list with all the descriptors for the specified
     *         <code>URL</code>.
     * @throws IOException if an IOException occurs while trying
     *             to connect to the specified URL.
     */
    private Set<String> getDescriptorNames(URL url) throws IOException {

        final Set<String> result = new HashSet<String>();
        final File dir = new File(URLDecoder.decode(url.getPath(), "UTF-8"));

        for (final File entry : dir.listFiles()) {
            if (entry.isDirectory()) {
                continue;
            }

            final String entryName = entry.getAbsolutePath();
            final int lastIndex = entryName.lastIndexOf('/');

            if (lastIndex > 0 && entryName.endsWith(EXTENSION)) {

                final String pluginName = entryName.substring(lastIndex + 1);

                result.add(getCanonicalName(pluginName));
            }
        }

        return result;
    }

    /**
     * Extracts from a specified <code>JarURLConnection</code>
     * all the descriptors which are matching the specified
     * filter and store them in to a <code>Set</code>. If the set
     * is empty then the specified <code>JarURLConnection</code>
     * does not contains any descriptors.
     *
     * @param jarConnection the jar connection.
     * @param filter the filter for the descriptors.
     * @return a list with all the descriptors for the specified
     *         <code>JarURLConnection</code>.
     * @throws IOException if an IOException occurs while trying
     *             to connect to the JAR file for this
     *             connection.
     */
    private Set<String> getDescriptorNames(JarURLConnection jarConnection)
            throws IOException {

        final Set<String> result = new HashSet<String>();
        final JarFile file = jarConnection.getJarFile();

        for (final Enumeration<JarEntry> entries = file.entries(); entries
                .hasMoreElements();) {
            final String entryName = entries.nextElement().toString();
            final int lastIndex = entryName.lastIndexOf('/');

            if (lastIndex > 0 && entryName.endsWith(EXTENSION)) {

                final String pluginName = entryName.substring(lastIndex + 1);

                result.add(getCanonicalName(pluginName));

            }
        }

        return result;
    }

    /**
     * Builds the full name (base + descriptor name) for a
     * specified name.
     *
     * @param name the name
     * @return the canonical path for the specified name.
     */
    private String getCanonicalName(String name) {
        final String path = base.replace('.', '/');
        final StringBuilder result = new StringBuilder(path);
        result.append('/');
        result.append(name);

        return result.toString();
    }
}
