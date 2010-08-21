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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.log.LogProvider;
import org.jboss.seam.log.Logging;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil2;

/**
 * A servlet for reading in resources from the classpath and/or the ordinary KiWi.war directory.
 * This servlet is an extension-aware way to serve web application resource files to the client.
 *
 * @author Sebastian Schaffert
 *
 */
public class KiWiResourceServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final LogProvider log = Logging.getLogProvider(KiWiResourceServlet.class);

    private ServletContext context;

    private HashMap<String,URL> url_cache;

    private boolean caching, proxy;

    private Cache data_cache;

    private MimeUtil2 mimeUtil;

    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = config.getServletContext();

        log.info("KiWi Resource Servlet starting up ...");

        this.url_cache  = new HashMap<String,URL>();
        this.data_cache = new Cache(true,true,false,true,"com.opensymphony.oscache.base.algorithm.LRUCache",1000);

        this.caching = false;
        this.proxy   = true;

        this.mimeUtil = new MimeUtil2();
        this.mimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector");

        
        // author : Mihai
        // Disclaimer : From the EJB point of view this is not the elegant
        // way to obtain the system relative path.
        // But, from the web application pov this the most used way to obtain
        // the system dependent path. This solution works also for archived
        // ear.
        String webDescrPath = "/WEB-INF/web.xml";
        final String realPath = context.getRealPath(webDescrPath);
        final StringBuffer result = new StringBuffer(realPath);
        final int indexOf = realPath.indexOf("KiWi.war");
        if (indexOf >= 0) {
            result.delete(indexOf, realPath.length());
            final String earRealPath = result.toString();

            // author : mihai
            // I know that this is not nice to do this but is a fast
            // way to exchange information for classes in the same
            // virtual machine. This particularity can create problems
            // if the web container runs in other JVM like the enterprise
            // container.
            // The most elegant way is to inject some beans and to store this
            // data in context.
            System.setProperty("ear.path", earRealPath);
        }
    }


    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String prefix = request.getContextPath();

        log.debug("requested resource: "+request.getRequestURI());
        
        if(request.getRequestURI().equals(prefix+"/")) {
            response.sendRedirect(prefix+"/home.seam");
        } else if (request.getRequestURI().startsWith(prefix) && request.getRequestURI().endsWith("/")) {
            response.sendRedirect(request.getRequestURI()+"home.seam");
        } else if (request.getRequestURI().startsWith(prefix)) {
            // check whether we are valid servlet to call ...
            String path = request.getRequestURI().replaceFirst(prefix, "");

            ResourceCacheEntry data = null;
            if(getFromCache(path) != null) {

                // lookup data in cache
                data = getFromCache(path);
            } else {
                // not in cache, find using resolveResource and read in from disk or library
                URL url = resolveResource(path);
                if(url != null) {
                    URLConnection con = url.openConnection();
                    InputStream   is  = con.getInputStream();

                    int length         = con.getContentLength();
                    String contentType = getMimeType(url);

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try {
                        byte[] buffer = new byte[1024];
                        int len = is.read(buffer, 0, 1024);
                        while(len > 0) {
                            out.write(buffer,0,len);
                            out.flush();
                            len = is.read(buffer, 0, 1024);
                        }
                        out.close();
                    } finally {
                        is.close();
                    }
                    data = new ResourceCacheEntry(out.toByteArray(), length, contentType);

                    if(caching) {
                        putInCache(path, data);
                    }
                }
            }

            // if there is data, send it to browser
            if(data != null) {
                if(proxy) {
                    response.setDateHeader("Expires", System.currentTimeMillis()+3600*1000);
                } else {
                    response.setHeader("Cache-Control", "no-store");
                    response.setHeader("Pragma", "no-cache");
                    response.setDateHeader("Expires", 0);
                }

                if(data.getContentType() != null && !data.getContentType().contains("unknown")) {
                    response.setContentType(data.getContentType());
                }
                response.setContentLength(data.getLength());

                response.getOutputStream().write( data.getData() );
                response.getOutputStream().flush();
                response.getOutputStream().close();

            } else {
                // otherwise, redirect to home.seam?uri=request.getRequestURI()
                // depending on accept-header, this should rather point to the Linked Open Data resource...
                // see http://www4.wiwiss.fu-berlin.de/bizer/pub/LinkedDataTutorial/

                String baseUri = request.getScheme() + "://" + request.getServerName();
                if(!(
                     (request.getScheme()=="http" && request.getServerPort()==80) ||
                     (request.getScheme()=="https" && request.getServerPort()==443)
                     )){
                    baseUri += (":" + request.getServerPort());
                }
                baseUri+= request.getRequestURI();


                String accept=request.getHeader("Accept");
                log.debug("accept header="+accept);

                if(accept!=null){
                    if(accept.equals("application/rdf+xml")) {
                        response.sendRedirect(prefix+"/linkeddata.seam?uri="+baseUri);
                    } else if(accept.contains("text/html")) {
                        response.sendRedirect(prefix+"/home.seam?uri="+baseUri);
                    }
                }else{
                    log.error("service: accept was null!");
                }
                // otherwise, send a 404
                //response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    private URL resolveResource(String resource) {
        log.debug("looking up resource "+resource);

        URL resourceUrl = url_cache.get(resource);

        if(resourceUrl == null) {

            // first, try to retrieve the JSF resource from the main KiWi application view
            String realPath = context.getRealPath(resource);
            if (realPath != null) {

                File file = new File(realPath);
                if(!file.isDirectory() && file.canRead()) {
                    log.debug("found in "+realPath);
                    try {
                        resourceUrl = file.toURI().toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }


            // if not found, search the extensions for the file
            if (resourceUrl == null) {
                if (resource.startsWith("/")) {
                    resource = resource.substring(1);
                }

                // the first part of the path is the extension identifier; we split the path at the
                // first "/" into the extension identifier and the actual resource ...
                if(resource.contains("/")) {
                    String[] components = resource.split("/",2);
                    log.debug("Application: "+components[0]);
                    log.debug("Resource:    "+components[1]);

                    // try to find all resources matching components[1] and check whether they are contained in
                    // the right jar file
                    try {
                        Enumeration<URL> en = Thread.currentThread().getContextClassLoader().getResources(components[1]);

                        while(en.hasMoreElements()) {
                            resourceUrl = en.nextElement();

                            if(resourceUrl.getPath().contains("kiwiext-"+components[0]+".jar")) {
                                log.debug("found in "+resourceUrl);
                                break;
                            }
                        }

                    } catch (IOException e) {
                        // should not happen
                        e.printStackTrace();
                    }

                    //resourceUrl = Thread.currentThread().getContextClassLoader().getResource(resource);
                }

            }


            url_cache.put(resource, resourceUrl);
        }
        log.debug("found resource at "+resourceUrl);
        return resourceUrl;

    }

    private ResourceCacheEntry getFromCache(String key) {
        try {
            if(caching) {
                return (ResourceCacheEntry)data_cache.getFromCache(key);
            } else {
                return null;
            }
        } catch(NeedsRefreshException ex) {
            return null;
        }
    }


    private void putInCache(String key, ResourceCacheEntry data) {
        boolean updated = false;
        try {
            // Store in the cache
            data_cache.putInCache(key, data);
            updated = true;
        } finally {
            if (!updated) {
                // It is essential that cancelUpdate is called if the
                // cached content could not be rebuilt
                data_cache.cancelUpdate(key);
            }
        }
    }
    
    private String getMimeType(URL resource) {
    	
    	Collection<MimeType> types = mimeUtil.getMimeTypes(resource);
    	
    	if(types.size() > 0) {
    		MimeType t = types.iterator().next();
    		return t.toString();
    	} else {
    		return null;
    	}
    	
    }

    private static class ResourceCacheEntry {
        byte[] data;
        int length;
        String contentType;

        public ResourceCacheEntry(byte[] data, int length, String contentType) {
            super();
            this.data = data;
            this.length = length;
            this.contentType = contentType;
        }

        /**
         * @return the data
         */
        public byte[] getData() {
            return data;
        }

        /**
         * @param data the data to set
         */
        public void setData(byte[] data) {
            this.data = data;
        }

        /**
         * @return the length
         */
        public int getLength() {
            return length;
        }

        /**
         * @param length the length to set
         */
        public void setLength(int length) {
            this.length = length;
        }

        /**
         * @return the contentType
         */
        public String getContentType() {
            return contentType;
        }

        /**
         * @param contentType the contentType to set
         */
        public void setContentType(String contentType) {
            this.contentType = contentType;
        }


    }
}
