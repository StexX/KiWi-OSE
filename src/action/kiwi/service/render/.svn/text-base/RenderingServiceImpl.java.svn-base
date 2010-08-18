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

package kiwi.service.render;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateless;

import kiwi.api.config.ConfigurationService;
import kiwi.api.event.KiWiEvents;
import kiwi.api.render.RenderingServiceLocal;
import kiwi.api.render.RenderingServiceRemote;
import kiwi.api.security.CryptoService;
import kiwi.config.Configuration;
import kiwi.config.UserConfiguration;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.content.TextContent;
import kiwi.model.user.User;
import kiwi.service.render.renderlet.MediaContentRenderlet;
import kiwi.service.render.renderlet.SourceRenderlet;
import kiwi.service.render.renderlet.XOMRenderlet;
import kiwi.util.KiWiStringUtils;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.XPathContext;
import nu.xom.canonical.Canonicalizer;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.cache.CacheProvider;
import org.jboss.seam.log.Log;

/**
 * The RenderingPipeline contains methods that transform the content item content contained in the database
 * into the HTML (or other presentation) that is sent to the browser. Typical tasks are rendering wiki links as
 * internal links, adding RDF/A annotations, and so on.
 * 
 * RenderingPipeline is implemented as a stateless bean and shared among all users; the rendering for each user
 * takes place in ContentRenderService, which calls the renderer methods with the current context item
 * 
 * @see StoringServiceImpl for the transformation process from the edited XHTML to the internal XHTML representation
 * 
 * TODO: need a renderlet that extracts the content of the HTML body so that we don't get nested <html> and <body> tags
 * 
 * @author sschaffe
 *
 */
@Stateless
@Name("renderingPipeline")
@Scope(ScopeType.STATELESS)
@AutoCreate
@Install(dependencies="configurationService")
public class RenderingServiceImpl implements RenderingServiceLocal, RenderingServiceRemote {

	
	@In(value="renderingService.htmlSourceRenderlets",scope=ScopeType.APPLICATION, required=false)
	@Out(value="renderingService.htmlSourceRenderlets",scope=ScopeType.APPLICATION)
	private List<SourceRenderlet> htmlSourceRenderlets;

	@In(value="renderingService.htmlXOMRenderlets",scope=ScopeType.APPLICATION, required=false)
	@Out(value="renderingService.htmlXOMRenderlets",scope=ScopeType.APPLICATION)
	private List<XOMRenderlet> htmlXOMRenderlets;

	@In(value="renderingService.annotationSourceRenderlets",scope=ScopeType.APPLICATION, required=false)
	@Out(value="renderingService.annotationSourceRenderlets",scope=ScopeType.APPLICATION)
	private List<SourceRenderlet> annotationSourceRenderlets;

	@In(value="renderingService.annotationXOMRenderlets",scope=ScopeType.APPLICATION, required=false)
	@Out(value="renderingService.annotationXOMRenderlets",scope=ScopeType.APPLICATION)
	private List<XOMRenderlet> annotationXOMRenderlets;
	
	@In(value="renderingService.editorSourceRenderlets",scope=ScopeType.APPLICATION, required=false)
	@Out(value="renderingService.editorSourceRenderlets",scope=ScopeType.APPLICATION)
	private List<SourceRenderlet> editorSourceRenderlets;
	
	@In(value="renderingService.editorXOMRenderlets",scope=ScopeType.APPLICATION, required=false)
	@Out(value="renderingService.editorXOMRenderlets",scope=ScopeType.APPLICATION)
	private List<XOMRenderlet> editorXOMRenderlets;
	
	@In(value="renderingService.mediaRenderlets",scope=ScopeType.APPLICATION, required=false)
	@Out(value="renderingService.mediaRenderlets",scope=ScopeType.APPLICATION)
	private List<MediaContentRenderlet> mediaRenderlets;
	
	@In
	private CacheProvider cacheProvider;
	
//	@In
//	private FacesMessages facesMessages;
	
	@Logger
	private static Log log;

	@In(value="configurationService", create=true) 
	ConfigurationService configurationService;
    
 	
	@Observer(KiWiEvents.CONFIGURATIONSERVICE_INIT)
    public void initialise(String dummyString) {
    	log.info("RenderingPipeline starting up ...");
    	
    	htmlSourceRenderlets   = new LinkedList<SourceRenderlet>();
    	htmlXOMRenderlets      = new LinkedList<XOMRenderlet>();

    	annotationSourceRenderlets   = new LinkedList<SourceRenderlet>();
    	annotationXOMRenderlets      = new LinkedList<XOMRenderlet>();
    	
    	editorSourceRenderlets = new LinkedList<SourceRenderlet>();
    	editorXOMRenderlets    = new LinkedList<XOMRenderlet>();
    	
    	mediaRenderlets        = new LinkedList<MediaContentRenderlet>();
    	
    	// initialise renderlets from configuration in kspace
    	// renderlets.html.source   -> htmlSourceRenderlets
    	// renderlets.html.xom      -> htmlXOMRenderlets
    	// renderlets.editor.source -> editorSourceRenderlets
    	// renderlets.editor.xom    -> editorXOMRenderlets
    	// renderlets.media         -> mediaRenderlets
    	for(String cls : configurationService.getConfiguration("renderlets.html.source").getListValue()) {
    		initRenderlet(htmlSourceRenderlets,cls);
    	}
    	log.info("rendering pipeline: #0 source renderlets for HTML rendering", htmlSourceRenderlets.size());
    	
    	for(String cls : configurationService.getConfiguration("renderlets.html.xom").getListValue()) {
    		initRenderlet(htmlXOMRenderlets,cls);
    	}
    	log.info("rendering pipeline: #0 XOM renderlets for HTML rendering", htmlXOMRenderlets.size());

    	
    	for(String cls : configurationService.getConfiguration("renderlets.annotation.source").getListValue()) {
    		initRenderlet(annotationSourceRenderlets,cls);
    	}
    	log.info("rendering pipeline: #0 source renderlets for Annotation View rendering", htmlSourceRenderlets.size());
    	
    	for(String cls : configurationService.getConfiguration("renderlets.annotation.xom").getListValue()) {
    		initRenderlet(annotationXOMRenderlets,cls);
    	}
    	log.info("rendering pipeline: #0 XOM renderlets for Annotation View rendering", htmlXOMRenderlets.size());
    	
    	for(String cls : configurationService.getConfiguration("renderlets.editor.source").getListValue()) {
    		initRenderlet(editorSourceRenderlets,cls);
    	}
    	log.info("rendering pipeline: #0 source renderlets for editor rendering", htmlSourceRenderlets.size());
    	
    	for(String cls : configurationService.getConfiguration("renderlets.editor.xom").getListValue()) {
    		initRenderlet(editorXOMRenderlets,cls);
    	}
    	log.info("rendering pipeline: #0 XOM renderlets for editor rendering", htmlXOMRenderlets.size());
    	
    	for(String cls : configurationService.getConfiguration("renderlets.media").getListValue()) {
    		initRenderlet(mediaRenderlets,cls);
    	}
    	log.info("rendering pipeline: #0 renderlets for media rendering", mediaRenderlets.size());
    	
    }
    
    private <T> void initRenderlet(List<T> list, String cls) {
    	try {
			//T savelet = (T)Class.forName(cls).newInstance();
			T renderlet = (T) Component.getInstance(cls);
    		
			if(renderlet != null)
				list.add(renderlet);
			else
				log.warn("warning: renderlet #0 was null after initialisation", cls);
			
		} catch (Exception e) {
			log.error("error while instantiating renderlet #0: #1",cls,e.getMessage());
		}
    }

    
    @Remove
    public void shutdown() {
    }
    
    /* (non-Javadoc)
	 * @see kiwi.api.render.RenderingService#renderHTML(java.lang.String, kiwi.model.content.ContentItem)
	 */
    public String renderHTML(String text, ContentItem ci) {

    	log.debug("rendering: " + text);
    	Document xom = string2xom(text);

    	log.debug(xom.getBaseURI());

    	// perform rendering: XOM renderlets
    	for(XOMRenderlet renderlet : htmlXOMRenderlets) {
    		xom = renderlet.apply(ci.getResource(), xom);
    	}

    	// select the element marked as kiwi:type="page" and take all elements below it
    	String result;
    	XPathContext namespaces = new XPathContext();
    	namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
    	Nodes nl = xom.query("//*[@kiwi:type='page']/*",namespaces);
    	if(nl.size() > 0) {
    		result = "";
    		for(int i = 0; i < nl.size(); i++) {
    			result += xom2string(nl.get(i),true);
    		}
    	} else {
    		result = xom2string(xom,true);
    	}

    	// perform rendering: source renderlets
    	for(SourceRenderlet renderlet : htmlSourceRenderlets) {
    		result = renderlet.apply(ci.getResource(), result);
    	}
    	return result;
    }
	
	public String renderHTML(ContentItem ci) {
		TextContent content = ci.getTextContent();
		if(content != null) {
			String html = content.getId() == null ? null : (String)cacheProvider.get("rendered.html", content.getId()+"");
			
			// cached rendering; text content is immutable, so the id is a good key for the renderer
			if(html == null) {
				html = renderGeneric(ci, htmlXOMRenderlets, htmlSourceRenderlets);
				
				if(content.getId() != null) {
					cacheProvider.put("rendered.html", content.getId()+"", html);
				}
			}
			return html;
		} else {
			return "";
		}
	}
	
	@Override
	public String renderHTML(ContentItem ci, User user) {
		TextContent content = ci.getTextContent();
		
		if(content != null) {
			String html = content.getId() == null ? null : (String)cacheProvider.get("rendered.html", content.getId()+"");

			//		String html = null;
			if(html == null) {
				PrivateKey priv = null;
				if(user != null && (priv = user.getPrivateKey()) != null) {
					UserConfiguration encryption = configurationService.getUserConfiguration(user,user.getLogin() + "-key:" + ci.getResource());
					String key = encryption.getEncryptedKey();
					if(ci != null && encryption != null && key != null) {
						byte[] encKeyBytes = KiWiStringUtils.fromHex(key,
								key.length());

						if(encKeyBytes != null) {
							// TODO: decrypt AES key from RSA private key
							CryptoService cryptoService = (CryptoService) Component.getInstance("cryptoService");

							byte[] keyBytes = cryptoService.decryptAsymmetric(encKeyBytes, priv);

							String cipher = KiWiStringUtils.extractAESCipher(content.getHtmlContent());
							if(cipher != null) {
								byte[] plainBytes = cryptoService.decryptSymmetric(
										KiWiStringUtils.fromHex(cipher,cipher.length()), 
										keyBytes, "AES");
								html = content.getHtmlContent().replaceAll(
										"-----BEGIN AES ENCRYPTED MESSAGE-----<br></br><br></br>" +
										".*" +
										"<br></br><br></br>------END AES ENCRYPTED MESSAGE------", 
										new String(plainBytes));
							}
						}
					}
				}
			}

			// cached rendering; text content is immutable, so the id is a good key for the renderer
			if(html == null) {
				html = renderGeneric(ci, htmlXOMRenderlets, htmlSourceRenderlets);

				if(content.getId() != null) {
					cacheProvider.put("rendered.html", content.getId()+"", html);
				}
			}
			return html;
		} else {
			return "";
		}
	}
	
	// KIWI-766, rendering is a function of textcontent AND metadata, so just clear cache if metadata changes.
	@Observer(KiWiEvents.METADATA_UPDATED)
	public void listenMetadataUpdate(ContentItem item) {
		log.info("onMetadataUpdate");
		if (item != null && item.getTextContent() != null) {
			cacheProvider.remove("rendered.html", item.getTextContent().getId()+"");
		}
	}
	
	@Override
	public String renderEditor(ContentItem ci, User user) {
		TextContent content = ci.getTextContent();
		if(content != null) {
			TextContent text = (TextContent)content;
			
			String html = text.getXmlString();
			PrivateKey priv = null;
			if(user != null && (priv = user.getPrivateKey()) != null) {
				UserConfiguration encryption = configurationService.getUserConfiguration(user,user.getLogin() + "-key:" + ci.getResource());
				String key = encryption.getEncryptedKey();
				if(ci != null && encryption != null && key != null) {
					byte[] encKeyBytes = KiWiStringUtils.fromHex(key,
							key.length());
					
					if(encKeyBytes != null) {
						// TODO: decrypt AES key from RSA private key
						CryptoService cryptoService = (CryptoService) Component.getInstance("cryptoService");
						
						byte[] keyBytes = cryptoService.decryptAsymmetric(encKeyBytes, priv);
						
						String cipher = KiWiStringUtils.extractAESCipher(html);
						if(cipher != null) {
							byte[] plainBytes = cryptoService.decryptSymmetric(
									KiWiStringUtils.fromHex(cipher,cipher.length()), 
									keyBytes, "AES");
							html = content.getHtmlContent().replaceAll(
									"-----BEGIN AES ENCRYPTED MESSAGE-----<br></br><br></br>" +
									".*" +
									"<br></br><br></br>------END AES ENCRYPTED MESSAGE------", 
									 new String(plainBytes));
						}
					}
					
					html = KiWiStringUtils.extractWithoutRSASignature(html);
					
					text.setXmlString(html);
				}
			}
	
			Document xom = text.copyXmlDocument();
	
			if(xom != null) {
				// perform rendering: XOM renderlets
				for(XOMRenderlet renderlet : editorXOMRenderlets) {
					xom = renderlet.apply(ci.getResource(), xom);
				}
	
				// select the element marked as kiwi:type="page" and take all elements below it
				String result = extractPage(xom);
	
				// perform rendering: source renderlets
				for(SourceRenderlet renderlet : editorSourceRenderlets) {
					result = renderlet.apply(ci.getResource(), result);
				}
	
				// workaround for tinyMCE bug which doubles all br tags.
				result=result.replaceAll("<br></br>", "<br/>");
	
				return result;
			} else {
				//				facesMessages.add("text content was null - displaying empty page");
				return "";
			}
		} else {
			return "";
		}
			
	}
	
	public String renderPreview(ContentItem ci) {
		TextContent content = ci.getTextContent();
		if(content==null) {
			return "";
		} else {
			TextContent text = (TextContent)content;
			
			// perform rendering
			
			return text.getHtmlContent();
		}
		
	}

	public String renderAnnotation(ContentItem ci) {
		
		return renderGeneric(ci, annotationXOMRenderlets, annotationSourceRenderlets);
	}
	
	
	private String renderGeneric(ContentItem ci, Collection<XOMRenderlet> xomRenderlets, Collection<SourceRenderlet> sourceRenderlets) {
		TextContent content = ci.getTextContent();
		if(content == null) {
			log.error("error rendering null content");
			return "";
		}
		
		log.info("rendering content (content item: #0)",ci.getTitle());

		long start = System.currentTimeMillis();
		
		if(content != null) {
			TextContent text = (TextContent)content;
			
			Document xom = text.copyXmlDocument();
			
			if(xom != null) {
				// perform rendering: XOM renderlets
				for(XOMRenderlet renderlet : xomRenderlets) {
					xom = renderlet.apply(ci.getResource(), xom);
				}
				
				// select the element marked as kiwi:type="page" and take all elements below it
		    	String result = extractPage(xom);
	
				// perform rendering: source renderlets
		    	// TODO: change to UIComponent instead of string
		    	for(SourceRenderlet renderlet : sourceRenderlets) {
		    		result = renderlet.apply(ci.getResource(), result);
		    	}
				
				log.info("rendering text content took #0ms",System.currentTimeMillis()-start);
	    	
				return result;
			} else {
//				facesMessages.add("text content was null - displaying empty page");
				return "";
			}
		} else {
			// currently no active content item, return empty string
			return "";
		}
		
	}
	
	
	public static String extractPage(Document xom) {
    	String result;
    	XPathContext namespaces = new XPathContext();
    	namespaces.addNamespace("kiwi", Constants.NS_KIWI_HTML);
    	Nodes nl = xom.query("//*[@kiwi:type='page']/*",namespaces);
    	if(nl.size() > 0) {
    		result = "";
    		for(int i = 0; i < nl.size(); i++) {
    			result += xom2string(nl.get(i),true);
    		}
    	} else {
    		result = xom2string(xom,true);
    	}
    	return result;
	}
	
    public static String xom2string(Node xom, boolean canonical) {
	    if(xom != null) {
	        if(canonical) {
	            // use a canonicalizer from XOM to create a canonical representation of the document
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	
	            Canonicalizer c14n = new Canonicalizer(out,Canonicalizer.CANONICAL_XML);
	
	            try {
	                c14n.write(xom);
	            } catch(IOException ex) {
	                throw new RuntimeException("I/O Exception while serialising XML document; this should never happen!",ex);
	            }
	
	            String result = "";
	            try {
	                    result = new String(out.toByteArray(), "UTF-8");
	            } catch (UnsupportedEncodingException exc) {}
	            return result;
	        } else {
	            // use the XOM serializer to create a String representation
	        	return xom.toXML();
	        } 
   	} else {
   		return null;
   	}
}

   /**
	 * Converts a string into a nu.xom.Document
	 * @param xml
	 * @return
	 */
	public static nu.xom.Document string2xom( String xml ) {
    	if(xml != null) { // Seam/JPA seems to need this
	        nu.xom.Builder builder = new nu.xom.Builder();
	        try {
	        	nu.xom.Document doc = builder.build(xml, "kiwi://");
	            return doc;
	        } catch(ParsingException ex) {
	        	log.error("string to xom converter could not parse string: #0", xml);
	            return null;
	        } catch(IOException ex) {
	        	log.error("string to xom run into an IOException. String: #0", xml);
	            return null;
	        }
    	} else {
    		return null;
    	}
    }
}
