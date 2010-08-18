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

package kiwi.service.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import kiwi.api.config.ConfigurationService;
import kiwi.api.query.sparql.SparqlUpdateService;
import kiwi.api.security.CryptoService;
import kiwi.api.transaction.TransactionService;
import kiwi.api.user.FoafSslLoginService;
import kiwi.exception.FoafProfileAssociationException;
import kiwi.model.user.User;
import kiwi.service.transaction.KiWiSynchronizationImpl;
import kiwi.util.KiWiStringUtils;

import org.bouncycastle.util.encoders.Base64;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import org.jboss.seam.transaction.Transaction;
import org.jboss.seam.web.AbstractResource;
import org.openrdf.query.QueryEvaluationException;

@Scope(ScopeType.APPLICATION)
@Name("foafsslServlet")
@BypassInterceptors
@Startup
public class FoafSslAuthenticationServlet extends AbstractResource {
	
	/**
     * the validateIDPrequest method identifies whether 
     * the request was truly done by the expected IDP 
     * server and that it has not been maliciously 
     * edited during the transmission
     * @param request
     * @return
     */
    private boolean validateIDPrequest(
    		String requestUrl, String webid, 
    		String timestamp, String sig) {
    	Log log = Logging.getLog(FoafSslAuthenticationServlet.class);
    	
    	/**
    	 * Testing the timestamp
    	 */
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
			Date authnDate = dateFormat.parse(timestamp);
			Calendar cal = Calendar.getInstance();
			cal.setTime(authnDate);
			if(cal.before(Calendar.getInstance())) {
				log.debug("Timestamp verification no. 1: Testing validation");
			} else {
				log.error("Timestamp is invalid");
				return false;
			}
			Calendar fiveSecsBef = Calendar.getInstance();
			fiveSecsBef.add(Calendar.SECOND, -5);
			if(cal.after(fiveSecsBef)) {
				log.debug("Timestamp verification no. 2: Testing expiration");
			} else {
				log.error("Timestamp has expired");
				return false;
			}
		} catch (ParseException e2) {
			log.error("System failure: Parsing timestamp produces exception");
			return false;
		}
		
		KeyStoreServlet ks = (KeyStoreServlet) Component.getInstance("keyStoreServlet");
		
		PublicKey pubKey = null;

		pubKey = ks.getPublicKey();		
		
		/**
		 *  converting signature string to byte array
		 */
		byte[] sigToVerify = Base64.decode(sig);
		
		/**
		 * Verifying signature
		 */
		Signature signature = null;
		try {
			signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(pubKey);
		} catch (NoSuchAlgorithmException e2) {
			log.error("System failure: No such algorithm");
			return false;
		} catch (InvalidKeyException e) {
			log.error("System failure: Invalid public key");
			return false;
		}
		
		StringBuilder bufferedUrl = new StringBuilder(requestUrl);
		try {
			bufferedUrl.append("?webid=");
			bufferedUrl.append(URLEncoder.encode(webid,"UTF-8"));
			bufferedUrl.append("&ts=");
			bufferedUrl.append(URLEncoder.encode(timestamp,"UTF-8"));
		} catch (UnsupportedEncodingException e3) {
			log.error("System failure: Unsupported Encoding");
			return false;
		}
		
		try {
			signature.update(bufferedUrl.toString().getBytes("UTF-8"));
			if(signature == null) {
				log.error("Signature is invalid (null)");
				return false;
			} else if (!signature.verify(sigToVerify)) {
				log.error("Signature cannot be verified");
				return false;
			}
		} catch (SignatureException e2) {
			log.error("Signature is invalid");
			return false;
		} catch (UnsupportedEncodingException e) {
			log.error("System failure: Unsupported Encoding");
			return false;
		}
		return true;
    }
	
    /**
     * just returns true (we do not need another 
     * authentication method, because the IDP 
     * server already authenticated the user)
     * @return always true
     */
//    public boolean authenticate() {
//		return true;
//    }
	
    
    
    
    /**
     * 
     * @param request
     * @param response
     */
	@SuppressWarnings("unchecked")
	public void doGet(final HttpServletRequest request, 
			final HttpServletResponse response) {
		
//		new RunAsOperation(){
//			@Override
//			public void execute() {
				try {
					/* instantiates several component services */
					Log log = Logging.getLog(FoafSslAuthenticationServlet.class);
					
					FoafSslLoginService foafSslLoginService = (FoafSslLoginService) 
						Component.getInstance("kiwi.user.foafSslLoginService");
					
					ConfigurationService configurationService =
						(ConfigurationService) Component.getInstance("configurationService");
			    	
					/* retrieves the request query parameters out of the request object */
			    	String webid = request.getParameter("webid");
			    	String timestamp = request.getParameter("ts");
			    	String sig = request.getParameter("sig");
			    	String error = request.getParameter("error");
			    	if(error != null) {
			    		log.error("The request failed due to the following problem: #0 ", error);
			    		response.sendError(403);
						return;
			    	}
			    	
			    	/* ensure the validity of the request */
					if(!validateIDPrequest(request.getRequestURL().toString(), webid, timestamp, sig)) {
						log.error("Validation failed.");
						response.sendError(403);
						return;
					}
					
					URL url;
					User user = null;
					try {
						url = new URL(webid);
						
						// load the user by extracting details from his/her foaf file
						foafSslLoginService.createUserInfoOutOfFoafFile(url);
							
						/* set the identity */
//						Identity identity = Identity.instance();
//						identity.getCredentials().setUsername(user.getLogin());
						
						/**
				    	 * register an authentication method that always returns true
				    	 * (we do not need another authentication since the IDP server 
				    	 * already authenticated the user)
				    	 */
//				    	MethodExpression methodExpression = 
//							Expressions.instance().createMethodExpression("#{foafsslServlet.authenticate()}");
//						identity.setAuthenticateMethod(methodExpression);
//						try {
//							identity.authenticate();
//						} catch (LoginException e) {
//							e.printStackTrace();
//							response.sendError(403);
//							return;
//						}
						
						/* login the new identity */
//						identity.login();
//						if(identity.isLoggedIn()) {
//							log.debug("User #0 has logged in", user.getLogin());
//						}
						
						
						
						/* the currentUserFactory needs a refresh to be able to load the new identity */
//						Conversation.instance().end(true);
//						CurrentUserFactory currentUserFactory = 
//							(CurrentUserFactory) Component.getInstance("currentUserFactory");
//						currentUserFactory.forceRefresh();
						
//						response.sendRedirect(configurationService.getBaseUri() + "/foaflogin/loginFOAFShowGenPassword.xhtml");
						
						
						response.sendRedirect(configurationService.getBaseUri() + "/dashboard/home.seam");
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
						response.sendError(403);
						return;
					} catch (QueryEvaluationException e) {
						e.printStackTrace();
						response.sendError(403);
						return;
					} catch (FoafProfileAssociationException e) {
						e.printStackTrace();
						response.sendError(403);
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
//			}
//		}.addRole("admin").run();
	}
	
	/**
	 * Overwritten method from AbstractResource, which starts a 
	 * new ContextualHttpServletRequest and calls the doGet() method
	 */
	@Override
	public void getResource(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException {
		new ContextualHttpServletRequest(request) {
			@Override
            public void process() throws IOException {
				try {
					/* Manually start and end the transaction management 
					 * to allow the login inside of a transaction */
					Transaction.instance().begin();
					TransactionService transactionService = (TransactionService) 
						Component.getInstance("transactionService");
					transactionService.registerSynchronization(
	                		KiWiSynchronizationImpl.getInstance(), 
	                		transactionService.getUserTransaction() );
					if(request.getMethod().equals("POST")) {
						doPost(request, response);
					} else {
						doGet(request, response);
					}
					Transaction.instance().commit();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (NotSupportedException e) {
					e.printStackTrace();
				} catch (SystemException e) {
					e.printStackTrace();
				} catch (RollbackException e) {
					e.printStackTrace();
				} catch (HeuristicMixedException e) {
					e.printStackTrace();
				} catch (HeuristicRollbackException e) {
					e.printStackTrace();
				}
            }

		}.run();
	}

	public void doPost(final HttpServletRequest request, 
			final HttpServletResponse response) {
		Log log = Logging.getLog(FoafSslAuthenticationServlet.class);
		ConfigurationService configurationService =
			(ConfigurationService) Component.getInstance("configurationService");
		SparqlUpdateService sparqlUpdateService =
			(SparqlUpdateService) Component.getInstance("sparqlUpdateService");
		log.info("!!!! POST request for servlet FoafSslAuthenticationServlet !!!!");
		
		Enumeration pn = request.getParameterNames();
		String query = null;
		String sig = null;
		while(pn.hasMoreElements()) {
			String param_name = (String) pn.nextElement();
			log.info("#0 :", param_name);
			if(param_name.equals("query")) {
				query = request.getParameter(param_name);
				log.info("#0", query);
			}
			if(param_name.equals("sig")) {
				sig = request.getParameter(param_name);
				log.info("#0", sig);
			}
		}
		
		if(sparqlUpdateService.parse(query, KiWiStringUtils.fromHex(sig, sig.length())))
			response.setStatus(HttpServletResponse.SC_OK);
		else
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//			response.sendRedirect(configurationService.getBaseUri() + "/wiki/home.seam");
	}
	
	/**
	 * Overwritten method that returns the 
	 * resource path, which is the url-ending 
	 * that redirects to the getResource() 
	 * method when it is called
	 */
	@Override
	public String getResourcePath() {
		return "/foafSsl";
	}

}
