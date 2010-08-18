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


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kiwi.api.extension.KiWiApplication;


/**
 * Used to create <code>KiWiApplication</code> instances for one
 * or more configuration files. This configuration files are java
 * properties files and they must follow the
 * <code>java.util.Properites</code> specification. This
 * configuration files are also named extension descriptors or
 * simple descriptors.<br>
 * Here is a descriptor example :
 * 
 * <pre>
 * name            MyPlugin
 * identifier      MyPlugin
 * version         2.02.01
 * project         myProject
 * author          the author
 * class           yyy.xxx.ttt.MyPlugin
 * dependsOn       XXXXXX
 * </pre>
 * 
 * <br>
 * Only the <i>name</i> and the <i>identifier</i> are mandatory.
 * The descriptors must be placed in classpath (or in jars placed
 * in classapth). The package(directory) where the descriptors
 * are placed can be specified.
 * 
 * @author Mihai Radulescu
 * @version 00.01-16
 * @since 00.01-16
 * @see KiWiApplication
 */
final class DescriptorFactory {

    /**
     * The key for the name property.
     */
    private static final String NAME_KEY = "name";

    /**
     * The key for the identifier property.
     */
    private static final String IDENTIFIER_KEY = "identifier";

    /**
     * Don't let anybody to instantiate this class.
     */
    private DescriptorFactory() {
        // UNIMPLEMENTED
    }

    /**
     * Builds a list of <code>KiWiApplication</code> for a given
     * package (directory); if the return list is empty then
     * there are no descriptors in the given package. More
     * precisely this method loads all the descriptors from the
     * given package and build for each descriptor a
     * <code>KiWiApplication</code>.<br>
     * The <code>pack</code> must be specified java like (e.g.
     * tagit.cfg).
     * 
     * @param pack the package (java like) from where the
     *            descriptors configuration file are loaded.
     * @return a list of <code>KiWiApplication</code> for a given
     *         package.
     * @throws IOException by any IO error.
     */
    static List<KiWiApplication> getDescriptors(String pack) throws IOException {
        final List<KiWiApplication> result = new ArrayList<KiWiApplication>();

        final PropertiesLoader loader = new PropertiesLoader(pack);
        final List<Map<String, String>> properties = loader.getProperties();
        for (final Map<String, String> prop : properties) {
            if (!prop.containsKey(NAME_KEY)) {
                throw new IllegalStateException(
                        "The actaul descriptor does not contains name property.");
            }

            if (!prop.containsKey(IDENTIFIER_KEY)) {
                throw new IllegalStateException(
                        "The actaul descriptor does not contains idetifier property.");
            }

            final String name = prop.get(NAME_KEY);
            final String id = prop.get(IDENTIFIER_KEY);

            final KiWiApplication extensionDescriptor =
                    new PropertiesApplicationDescriptor(name, id);
            result.add(extensionDescriptor);
        }

        return result;
    }

    /**
     * Used to describe the an KIWi Extension. Sebastian
     * Schaffert: converted to inner class to emphasise
     * modularisation and factories...
     * 
     * @author Mihai Radulescu
     * @version 00.01-16
     * @since 00.01-16
     */
    private final static class PropertiesApplicationDescriptor extends
            KiWiApplication {

        /**
         * A version number for this class so that serialization
         * can occur without worrying about the underlying class
         * changing between serialization and deserialization.
         */
        private static final long serialVersionUID = 9130626308879218127L;

        /**
         * The name for this extension descriptor.
         */
        private final String name;

        /**
         *The identifier for this extension descriptor.
         */
        private final String identifier;

        /**
         * The description for this KiWi application.
         */
        private final String description;

        /**
         * Build an <code>ExtensionDescriptor</code> for the
         * given name and identifier.
         * 
         * @param name name for this extension descriptor, it can
         *            be null.
         * @param identifier the identifier for this extension
         *            descriptor, it can be null.
         * @throws NullPointerException if the <code>name</code>
         *             or identifier <code>parameter</code> is
         *             null.
         */
        private PropertiesApplicationDescriptor(String name, String identifier) {
            if (name == null) {
                throw new NullPointerException("The name is null");
            }
            this.name = name;

            if (identifier == null) {
                throw new NullPointerException("The identifier is null");
            }
            this.identifier = identifier;

            this.description = "";
        }

        /**
         * Build an <code>ExtensionDescriptor</code> for the
         * given name and identifier.
         * 
         * @param name name for this extension descriptor, it can
         *            be null.
         * @param identifier the identifier for this extension
         *            descriptor, it can be null.
         * @throws NullPointerException if the <code>name</code>
         *             or identifier <code>parameter</code> is
         *             null.
         */
        private PropertiesApplicationDescriptor(String name, String identifier,
                String description) {
            if (name == null) {
                throw new NullPointerException("The name is null");
            }
            this.name = name;

            if (identifier == null) {
                throw new NullPointerException("The identifier is null");
            }
            this.identifier = identifier;

            this.description = description;
            if (description == null) {
                throw new NullPointerException("The description is null");
            }
        }

        @Override
        public String getIdentifier() {
            return identifier;
        }

        @Override
        public String getName() {
            return name;
        }

        /**
         * @return the description
         */
        @Override
        public String getDescription() {
            return description;
        }

		@Override
		public Set<String> getPermissibleRoles() {
			return null;
		}

    }
}
