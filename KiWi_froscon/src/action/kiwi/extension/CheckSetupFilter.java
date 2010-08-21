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
 * sschaffe
 * 
 */
package kiwi.extension;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kiwi.service.config.ConfigurationServiceImpl;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.web.Filter;
import org.jboss.seam.log.Log;
import org.jboss.seam.web.AbstractFilter;

/**
 * CheckSetupFilter. Checks whether the system needs to perform setup (or upgrade) and redirects appropriately
 * to the setup screens if this is the case.
 *
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.core.checkSetupFilter")
@Scope(ScopeType.APPLICATION)
@BypassInterceptors
@Filter
public class CheckSetupFilter extends AbstractFilter {


	@Logger
	private Log log;
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	      if (!(request instanceof HttpServletRequest)) 
	      {
	         throw new ServletException("This filter can only process HttpServletRequest requests");
	      }

	      HttpServletRequest httpRequest = (HttpServletRequest) request;
	      HttpServletResponse httpResponse = (HttpServletResponse) response;

	      
	      String pathInfo = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
	      
	      if(ConfigurationServiceImpl.configurationInProgress) {
//	    	 StartupIdentityManager startupIdentityManager =
//	    		 (StartupIdentityManager) Component.getInstance("startupIdentityManager");
//	    	 startupIdentityManager.init();
	    	 ConfigurationServiceImpl.configurationInProgress = false;
	      }
	      
	      if(!ConfigurationServiceImpl.testing &&
	         ConfigurationServiceImpl.needsSetup && 
	    	 (pathInfo.equals("") || pathInfo.endsWith("/") || pathInfo.endsWith(".seam")) &&
	    	 !pathInfo.startsWith("/setup") ) {
        	
        	String baseUri = httpRequest.getScheme() + "://" + httpRequest.getServerName();
        	if(!(
        			(httpRequest.getScheme()=="http" && httpRequest.getServerPort()==80) ||
        			(httpRequest.getScheme()=="https" && httpRequest.getServerPort()==443)
        	)){
        		baseUri += (":" + request.getServerPort());
        	}
        	baseUri+= httpRequest.getContextPath();
        	
        	baseUri+= "/setup/setupUser.seam";
        	
            log.info("redirecting to " + baseUri.toString());
            httpResponse.sendRedirect(baseUri.toString());
	      } else {
	    	  chain.doFilter(request, response);
	      }
	}


}
