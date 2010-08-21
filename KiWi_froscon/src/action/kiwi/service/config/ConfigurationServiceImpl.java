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

package kiwi.service.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.servlet.http.HttpServletRequest;

import kiwi.api.config.ConfigurationServiceLocal;
import kiwi.api.config.ConfigurationServiceRemote;
import kiwi.api.event.KiWiEvents;
import kiwi.api.extension.KiWiApplication;
import kiwi.config.Configuration;
import kiwi.config.UserConfiguration;
import kiwi.model.user.User;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.packaging.PersistenceMetadata;
import org.hibernate.ejb.packaging.PersistenceXmlLoader;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.cache.CacheProvider;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.Events;
import org.jboss.seam.log.Log;
import org.jboss.seam.web.ServletContexts;

/**
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("configurationService")
@Scope(ScopeType.STATELESS)
@AutoCreate
//@Transactional
public class ConfigurationServiceImpl implements ConfigurationServiceLocal, ConfigurationServiceRemote {

    private static final String VERSION = "0.9";
    
    // default configuration (components names)
    private static final String[] savelets_source = {
        "kiwi.service.render.savelet.ExtractLinksSavelet",
        "kiwi.service.render.savelet.HtmlCleanerSavelet"
    };

    private static final String[] savelets_text = {
        "kiwi.service.render.savelet.NavigationalLinksSavelet",
        "kiwi.service.render.savelet.RdfaSavelet",
        "kiwi.service.render.savelet.FragmentsSavelet",
        "kiwi.service.render.savelet.ComponentSavelet"
    };

    private static final String[] renderlets_html_xom = {
        "kiwi.service.render.renderlet.ComponentRenderlet",
		"kiwi.service.render.renderlet.ComponentDisplayRenderlet",
        "kiwi.service.render.renderlet.RdfaRenderlet",
        "kiwi.service.render.renderlet.HtmlLinkRenderlet",
        "kiwi.service.render.renderlet.HtmlRdfaRenderlet",
        "kiwi.service.render.renderlet.HtmlFragmentRenderlet",
        "kiwi.service.render.renderlet.ImageLinkRenderlet",
        "kiwi.service.render.renderlet.QueryRenderlet"
    };

    private static final String[] renderlets_editor_xom = {
        "kiwi.service.render.renderlet.ComponentRenderlet",
        "kiwi.service.render.renderlet.RdfaRenderlet",
        "kiwi.service.render.renderlet.EditorLinkRenderlet"
    };

    private static final String[] renderlets_annotation_xom = {
        "kiwi.service.render.renderlet.HtmlLinkRenderlet",
        "kiwi.service.render.renderlet.AnnotationLinksRenderlet"
    };


    @Logger
    private Log log;

    // @In is not working, because it is only injected when component is called, not on startup!
    @In(create=true)
    private EntityManager entityManager;




     /**
     * The cache provider defined in Seam. Used for caching
     * configuration values in order to avoid unnecessary
     * database access.
     */
 	@In(create=true)
 	private CacheProvider cacheProvider;
 	
 	
 	public static boolean needsSetup      = false;
 	public static boolean setupInProgress = false;
 	public static boolean configurationInProgress = false;
 	
 	// indicates whether we are in testing mode
 	public static boolean testing = false;

    /**
     * Transforms a given path from relative path to an absolute
     * path. More precisely if the given <code>path</code>
     * argument does not start with / then the given path is
     * considered relative path and it is prefixed with the
     * current running directory.<br>
     * If the path starts with / then the same string is
     * returned.
     * 
     * @param path the path to analyze.
     * @return a absolute path, if the given <code>path</code>
     *         argument starts with / character or the same
     *         String if it starts.
     */
    private String getPath(String path) {

        if (new File(path).isAbsolute()) {
            return path;
        }

        final String prefix = System.getProperty("user.dir");
        final StringBuffer result = new StringBuffer(prefix);
        result.append(File.separator);
        result.append(path);

        return result.toString();
    }
    
	@Observer("org.jboss.seam.postInitialization")
	public void initialise() {
		log.info("KiWi ConfigurationService starting up ...");
		ConfigurationServiceImpl.configurationInProgress = true;

		final Configuration version = getConfiguration("version");
		
		initPersistenceConfiguration();
		
		if (getConfiguration("kiwi.solr.home") != null
                && getConfiguration("kiwi.solr.home").getStringValue() != null) {

		    // in this way the solr can use relative and absolute
            // path.
            final String solrHomeConf =
                    getConfiguration("kiwi.solr.home").getStringValue();
            final String realPath = getPath(solrHomeConf);
            System.setProperty("solr.solr.home", realPath);
		} else {
			System.setProperty("solr.solr.home", getWorkDir() + "/solr");
		}

		System.setProperty("solr.data.dir",System.getProperty("solr.solr.home")+"/data");

		
//		if(version.getStringValue() == null || !version.getStringValue().equals(VERSION)) {
		
		// if the setup configuration is not yet set, this indicates that the system needs
		// setup; we set the appropriate flag in the application context; this flag is processed
		// by CheckSetupFilter and redirects to the setup process
		Configuration setup = getConfiguration("kiwi.setup");
		if(setup.getStringValue() == null) {
			ConfigurationServiceImpl.needsSetup = true;
		}
		

		if (version.getStringValue() == null) {
			log.info("no configuration found; initialising with default values ...");

			// initialise configuration
			version.setStringValue(VERSION);
			setConfiguration(version);

			initWiklet("savelets.source", savelets_source);
			initWiklet("savelets.text", savelets_text);
			initWiklet("savelets.media", new String[0]);
			
			initWiklet("renderlets.html.source", new String[0]);
			initWiklet("renderlets.html.xom", renderlets_html_xom);
			
			initWiklet("renderlets.editor.source", new String[0]);
			initWiklet("renderlets.editor.xom", renderlets_editor_xom);
			
			initWiklet("renderlets.annotation.source", new String[0]);
			initWiklet("renderlets.annotation.xom", renderlets_annotation_xom);

			initWiklet("renderlets.media", new String[0]);

			
			//entityManager.flush();
		}
		
		setConfiguration("RELATIVE_PATH","seam/resource/services");
		
		Events.instance().raiseAsynchronousEvent(KiWiEvents.CONFIGURATIONSERVICE_INIT,"init");
		Events.instance().raiseAsynchronousEvent(KiWiEvents.CONFIGURATIONSERVICE_CREATE_ADMIN,"init");
	}
	
	private void initWiklet(String configurationName, String[] componentNames) {
        final Configuration cfg = getConfiguration(configurationName);
        for(String cmp_name : componentNames) {
            cfg.getListValue().add(cmp_name);
        }
        setConfiguration(cfg);

    }

    private void initPersistenceConfiguration() {
        log.info("KiWi Configuration Service: looking up Hibernate configuration...");
        final Ejb3Configuration cfg = new Ejb3Configuration();
        try {
            final Enumeration<URL> xmls = Thread.currentThread()
            .getContextClassLoader()
            .getResources( "META-INF/persistence.xml" );
            if ( ! xmls.hasMoreElements() ) {
                log.info( "Could not find any META-INF/persistence.xml file in the classpath");
            }
            while ( xmls.hasMoreElements() ) {
                final URL url = xmls.nextElement();
                log.info( "Analysing persistence.xml: #0", url );
                try {
                    final List<PersistenceMetadata> metadataFiles = PersistenceXmlLoader.deploy(
                            url,
                            new HashMap(),
                            cfg.getHibernateConfiguration().getEntityResolver(),
                            PersistenceUnitTransactionType.RESOURCE_LOCAL );
                    for ( final PersistenceMetadata metadata : metadataFiles ) {

                        for(final Object key : metadata.getProps().keySet()) {
                            if("kiwi".equalsIgnoreCase(key.toString().substring(0, 4))) {
                                log.info( "#0 = #1", key, metadata.getProps().get(key) );
                                setConfiguration(key.toString(),metadata.getProps().get(key).toString());
                            }
                        }
                    }
                } catch(Exception ex) {
                    log.error("error reading #0: is this a valid persistence configuration?",url.toString());
                }
            }
        } catch(final Exception ex) {
            log.error("could not read persistence.xml",ex);
        }
    }


	public String getSetupPage() {
		return getConfiguration("setupPage_1", getBaseUri()+"/content/SetupPage").getStringValue();
	}


	/* (non-Javadoc)
	 * @see kiwi.api.kspace.ConfigurationService#getStartPage()
	 */
	public String getStartPage() {
		return getConfiguration("startpage", getBaseUri()+"/content/FrontPage").getStringValue();
	}

    /**
     * Get the base URI out of the current request. The base URI
     * is used e.g. to generate URIs of internal content items
     * 
     * @see kiwi.api.config.ConfigurationService#getBaseUri()
     */
	@BypassInterceptors
	public String getBaseUri() {

		String baseUri = (String) Contexts.getSessionContext().get("baseUri");
		
		if(baseUri == null) {
			final ServletContexts servletContexts = ServletContexts.getInstance();
			final HttpServletRequest request = servletContexts.getRequest();

			if(request != null && request.getScheme() != null && request.getServerName() != null) {
				baseUri = request.getScheme() + "://" + request.getServerName();
				if(!(
		             (request.getScheme()=="http" && request.getServerPort()==80) ||
		             (request.getScheme()=="https" && request.getServerPort()==443)
		             )){
		            baseUri += (":" + request.getServerPort());
				}
			    baseUri+= request.getContextPath();
			} else {
				return "http://localhost";
			}
			Contexts.getSessionContext().set("baseUri", baseUri);
		}
		return baseUri;
	}

	private String serverUri;

	@BypassInterceptors
	public String getServerUri() {
		if(serverUri == null) {
			final ServletContexts servletContexts = ServletContexts.getInstance();
			final HttpServletRequest request = servletContexts.getRequest();

			if(request != null) {
				serverUri = request.getScheme() + "://" + request.getServerName();
				if(!(
		             (request.getScheme()=="http" && request.getServerPort()==80) ||
		             (request.getScheme()=="https" && request.getServerPort()==443)
		             )){
		            serverUri += (":" + request.getServerPort());
				}
				serverUri += "/";
			} else {
				return "http://localhost/";
			}
		}

	    return serverUri;
	}

	/**
	 *
	 *
	 */
	public Configuration getConfiguration(String key) {
		Configuration result = (Configuration) cacheProvider.get("conf", key);

		if(result == null) {
			final javax.persistence.Query q = entityManager.createNamedQuery("configuration.byKey");
			q.setParameter("key", key);
			q.setHint("org.hibernate.cacheable", true);
			q.setMaxResults(1);

			final List results = q.getResultList();
			if(results.size() > 0) {
				result = (Configuration) results.get(0);
			} else  {
				log.info("creating new configuration for key #0", key);
				result = new Configuration(key);
				log.info("(getConfiguration(String key)) New configuration #0 created"); 
			}
			cacheProvider.put("conf", key, result);
		}

		return result;
	}

	public Configuration getConfiguration(String key, String defaultValue) {
		if(key != null) {
			Configuration result = (Configuration) cacheProvider.get("conf", key);

			if(result == null) {
				final javax.persistence.Query q = entityManager.createNamedQuery("configuration.byKey");
				q.setParameter("key", key);
				q.setHint("org.hibernate.cacheable", true);
				q.setMaxResults(1);

				final List results = q.getResultList();
				if(results.size() > 0) { 
					result = (Configuration) results.get(0);
				} else  {
					log.info("creating new configuration for key #0", key);
					final Configuration cfg = new Configuration(key);
					log.info("(setConfiguration(String key, String defaultValue)) " +
							"new configuration for key #0 and " +
							"default-value #1 created", key, defaultValue);
					cfg.setStringValue(defaultValue);
					setConfiguration(cfg);
					result = cfg;
				}
				cacheProvider.put("conf", key, result);
			} else {
				log.debug("returning cached configuration for key #0",key);
			}

			return result;
		} else {
			log.warn("the passed key to getConfiguration was null!");
			return null;
		}
	}

	// ensure flushing of data by running in a nested transaction
//	@Transactional(TransactionPropagationType.REQUIRED)
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void removeConfiguration(String key) {
		entityManager.setFlushMode(FlushModeType.COMMIT);
		Configuration c = getConfiguration(key);
		if (c != null) {
			if(!entityManager.contains(c) && c.getId() != null) {
				log.info("Merging Entity #0 ", c);
				c = entityManager.find(c.getClass(), c.getId());
			}
			log.info("Removing Entity #0 ", c);
			entityManager.remove(c);
		}

		cacheProvider.remove("conf",key);
	}

	// ensure flushing of data by running in a nested transaction
//	@Transactional(TransactionPropagationType.REQUIRED)
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void setConfiguration(Configuration config) {
//		entityManager.setFlushMode(FlushModeType.COMMIT);
		if(config.getId() != null && !entityManager.contains(config)) {
			config = entityManager.merge(config);
		} else {
			entityManager.persist(config);
		}

		cacheProvider.put("conf",config.getKiwikey(), config);

		Events.instance().raiseEvent("configurationChanged");
	}

	public void setConfiguration(String key, String value) {
		Configuration conf = getConfiguration(key);
		if(conf == null) {
			conf = new Configuration(key);
		}
		conf.setStringValue(value);
		setConfiguration(conf);
	}

	public void setConfiguration(String key, List<String> values) {
		Configuration conf = getConfiguration(key);
		if(conf == null) {
			conf = new Configuration(key);
		}
		conf.setListValue(values);
		setConfiguration(conf);
	}
	
	
	


	@Override
	public UserConfiguration getUserConfiguration(User user, String key, String defaultValue) {
		UserConfiguration result = (UserConfiguration) cacheProvider.get("conf-"+user.getLogin(), key);

		if(result == null) {
			final javax.persistence.Query q = entityManager.createNamedQuery("configuration.byUserKey");
			q.setParameter("key", key);
			q.setParameter("user", user);
			q.setHint("org.hibernate.cacheable", true);
			q.setMaxResults(1);

			final List results = q.getResultList();
			if(results.size() > 0) { 
				result = (UserConfiguration) results.get(0);
			} else  {
				log.info("creating new user configuration for user #0 key #1", user.getLogin(), key);
				final UserConfiguration cfg = new UserConfiguration(user,key);
				cfg.setStringValue(defaultValue);
				setUserConfiguration(cfg);
				result = cfg;
			}
			cacheProvider.put("conf-"+user.getLogin(), key, result);
		} else {
			log.debug("returning cached user configuration for user #0 key #1",user.getLogin(), key);
		}

		return result;
	}
	
	public Configuration getConfiguration(String key, double defaultValue) {

        if (key == null) {
            log.warn("the passed key to getConfiguration was null!");
            return null;
        }

        Configuration result = (Configuration) cacheProvider.get("conf", key);

        if(result == null) {
            final javax.persistence.Query q = entityManager.createNamedQuery("configuration.byKey");
            q.setParameter("key", key);
            q.setHint("org.hibernate.cacheable", true);
            q.setMaxResults(1);

            final List results = q.getResultList();
            if(results.size() > 0) {
                result = (Configuration) results.get(0);
            } else  {
                log.info("creating new configuration for key #0", key);
                final Configuration cfg = new Configuration(key);
                cfg.setDoubleValue(defaultValue);
                setConfiguration(cfg);
                result = cfg;
            }
            
            cacheProvider.put("conf", key, result);
        } else {
            log.debug("returning cached configuration for key #0",key);
        }

        return result;
    }

	@Override
	public UserConfiguration getUserConfiguration(User user, String key) {
		UserConfiguration result = (UserConfiguration) cacheProvider.get("conf-"+user.getLogin(), key);

		if(result == null) {
			final javax.persistence.Query q = entityManager.createNamedQuery("configuration.byUserKey");
			q.setParameter("user", user);
			q.setParameter("key", key);
			q.setHint("org.hibernate.cacheable", true);
			q.setMaxResults(1);

			final List results = q.getResultList();
			if(results.size() > 0) {
				result = (UserConfiguration) results.get(0);
			} else  {
				log.info("creating new configuration for key #0", key);
				result = new UserConfiguration(user,key);
			}
			cacheProvider.put("conf-"+user.getLogin(), key, result);
		} else {
			log.info("Result has been cached");
		}

		return result;
	}

	@Override
	public void removeUserConfiguration(User user, String key) {
		entityManager.setFlushMode(FlushModeType.COMMIT);
		UserConfiguration c = getUserConfiguration(user,key);
		if (c != null) {
			if(c.getId() != null && !entityManager.contains(c)) {
				log.info("Merging conf");
				c = entityManager.merge(c);
//				c = entityManager.find(UserConfiguration.class, c.getId());
			}
			log.info("Removing conf");
			entityManager.remove(c);
		}
		//entityManager.flush();

		cacheProvider.remove("conf-"+user.getLogin(),key);
	}

	@Override
	public void setUserConfiguration(User user, String key, List<String> values) {
		UserConfiguration conf = getUserConfiguration(user,key);
		if(conf == null) {
			conf = new UserConfiguration(user,key);
		}
		conf.setListValue(values);
		setUserConfiguration(conf);
	}

	@Override
	public void setUserConfiguration(User user, String key, String value) {
		UserConfiguration conf = getUserConfiguration(user,key);
		if(conf == null) {
			conf = new UserConfiguration(user,key);
		}
		conf.setStringValue(value);
		setUserConfiguration(conf);
	}
	
	@Override
	public void setUserRSAConfiguration(User user, String key, String value) {
		UserConfiguration conf = getUserConfiguration(user,key);
		if(conf == null) {
			conf = new UserConfiguration(user,key);
		}
		conf.setEncryptedKey(value);
		setUserConfiguration(conf);
	}

	@Override
//	@Transactional(TransactionPropagationType.REQUIRED)
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void setUserConfiguration(UserConfiguration config) {
//		entityManager.setFlushMode(FlushModeType.COMMIT);
		if(config.getId() != null && !entityManager.contains(config)) {
			config = entityManager.merge(config);
		} else {
			entityManager.persist(config);
		}
		//entityManager.flush();

		cacheProvider.put("conf-"+config.getUser().getLogin(),config.getKiwikey(), config);

		Events.instance().raiseEvent("configurationChanged");
	}

	/**
     * The work directory of the Sesame 2 native store. Sesame will create its own subdirectory
     * beneath this directory called "triples" and store the native database there.
     */
	public String getWorkDir() {
		final String value = getConfiguration("kiwi.work.dir").getStringValue();
		return value!=null?value:"/tmp/kiwi";
	}

	@Override
	@BypassInterceptors
	public String getApplicationPrefix(KiWiApplication app) {
		return "kiwi.app."+app.getIdentifier()+".";
	}
	
	/**
     * redirect to the IDP server, 
     * which should then redirect to the FoafSslAuthenticationServlet
     */
    public void redirectToIDP() {

        /**
         * end conversation
         */
        Conversation.instance().end(true);

        try {
            FacesContext.getCurrentInstance().getExternalContext()
                .redirect(getBaseUri()+"/seam/resource/idp?authreqissuer="+
                        getBaseUri()+"/seam/resource/foafSsl");
        } catch (IOException e) {
            log.error("Redirect was not possible: #0 ", e.getMessage());
            e.printStackTrace();
        }
    }

}
