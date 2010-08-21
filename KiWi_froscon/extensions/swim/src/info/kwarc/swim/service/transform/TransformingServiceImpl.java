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

package info.kwarc.swim.service.transform;

import info.kwarc.swim.api.transform.TransformingServiceLocal;
import info.kwarc.swim.api.transform.TransformingServiceRemote;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;

import kiwi.model.kbase.KiWiResource;
import net.sf.saxon.Configuration;
import nu.xom.Document;
import nu.xom.NodeFactory;
import nu.xom.Nodes;
import nu.xom.XMLException;
import nu.xom.xslt.XSLTransform;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.cache.CacheProvider;
import org.jboss.seam.log.Log;

/**
 * @author christoph.lange
 *
 */
@Name("transformer")
@Scope(ScopeType.APPLICATION)
@AutoCreate
public class TransformingServiceImpl implements TransformingServiceLocal,
		TransformingServiceRemote {
	@Logger
	private static Log log;
 	
 	/**
 	 * The cache provider defined in Seam. Used extensively for caching triples and nodes in order to 
 	 * avoid unnecessary database access.
 	 */
 	@In
 	private CacheProvider<Templates> cacheProvider;

	/*
	 * @In(value="configurationService", create=true) ConfigurationService
	 * configurationService;
	 */

	private TransformerFactory tFactory = TransformerFactory.newInstance();

//	private ObjectCache<Templates> transformer_cache;
//	private LookupFunction<Templates> cache_lookup;

	private javax.xml.transform.URIResolver resolver;
	private net.sf.saxon.OutputURIResolver outputURIResolver;

	public TransformingServiceImpl() {
		log.info("Transformer cache starting up ...");
	}

	private void setupXSLTProcessor() {
		// use Saxon as XSLT transformer
		System.setProperty("java.xml.transform.TransformerFactory",
				"net.sf.saxon.TransformerFactoryImpl");
		System.setProperty("javax.xml.transform.TransformerFactory",
				"net.sf.saxon.TransformerFactoryImpl");
		// and as XPath processor
		System.setProperty("javax.xml.xpath.XPathFactory",
				"net.sf.saxon.xpath.XPathFactoryImpl");
		System.setProperty("javax.xml.xpath.XPathFactory:"
				+ XPathConstants.DOM_OBJECT_MODEL,
				"net.sf.saxon.xpath.XPathFactoryImpl");
	}

	private Constructor<?> xomResultConstructor;
    private Method xomResultGetResultMethod;
    private Constructor<?> xomSourceConstructor;
    private DocumentBuilderFactory documentBuilderFactory;
    private boolean useReflection = true;

	private void hackXOM() {
		/*
		 * we love Elliotte Rusty Harold :-)
		 * 
		 * Workaround that makes XOMResult accessible, taken from Spring's
		 * AbstractXomPayloadEndpoint
		 */
		try {
	        Class<?> xomResultClass = Class.forName("nu.xom.xslt.XOMResult");
	        xomResultConstructor = xomResultClass.getDeclaredConstructor(new Class[]{NodeFactory.class});
	        xomResultConstructor.setAccessible(true);
	        xomResultGetResultMethod = xomResultClass.getDeclaredMethod("getResult", new Class[0]);
	        xomResultGetResultMethod.setAccessible(true);
	        Class<?> xomSourceClass = Class.forName("nu.xom.xslt.XOMSource");
	        xomSourceConstructor = xomSourceClass.getDeclaredConstructor(new Class[]{Document.class});
	        xomSourceConstructor.setAccessible(true);
		} catch (ClassNotFoundException exc) {
			// does not occur, as we have XOM in the classpath
		} catch (NoSuchMethodException exc) {
			// does not occur, as we have XOM in the classpath
		}
 	}

    private Source createXomSource(Document document) throws IllegalAccessException, InvocationTargetException, InstantiationException {
    	return (Source) xomSourceConstructor.newInstance(new Object[]{document});
    }
    
    private Result createXomResult() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return (Result) xomResultConstructor.newInstance(new Object[]{new NodeFactory()});
    }

    private Nodes xomResultGetResult(Result xomResult) throws IllegalAccessException, InvocationTargetException {
    	return (Nodes) xomResultGetResultMethod.invoke(xomResult, new Object[0]);
    }
    
	@Create
	public void initialise() {
		setupXSLTProcessor();
		hackXOM();
		
//		try {
//			resolver = new URIResolver(main.getStorageBackend(), this);
//			outputURIResolver = new OutputURIResolver(main.getStorageBackend(),
//					this, main.getPermissionManager());
//		} catch (DBException exc) {
//			// does not occur, as the storage backend will already have been
//			// initialised at this point
//		}
//
		Configuration saxonConfiguration = new Configuration();
//		saxonConfiguration.setOutputURIResolver(outputURIResolver);
//		factory.setAttribute(FeatureKeys.CONFIGURATION, saxonConfiguration);
//		factory.setURIResolver(resolver);
	}

		
	private Templates getCachedTemplates(String key) {
		synchronized(cacheProvider) {
			return (Templates) cacheProvider.get("templates", key);
		}		
	}
	
	private void putCachedTemplates(String key, Templates value) {
		synchronized(cacheProvider) {
			cacheProvider.put("templates", key, value);
		}		
	}
		
	public Transformer getTransformer(String tname) throws TransformerConfigurationException, IOException {
		Templates t = getCachedTemplates(tname);
		
		if (t == null) {
			URL resource = this.getClass().getResource(tname);
			StreamSource stylesheet = null;

			try {
				if (resource == null) {
					throw new IOException("could not find transformer source file " + tname);
				}

				stylesheet = new StreamSource(resource.openStream(),
					/* systemId = */resource.toExternalForm());
			} catch (IOException ex) {
				log.error("error while creating XSLT transformer " + tname,
						ex);
				throw ex;
			}

			try {
				t = tFactory.newTemplates(stylesheet);
				putCachedTemplates(tname, t);
			} catch (TransformerConfigurationException ex) {
				log.error("error while creating XSLT transformer " + tname,	ex);
				throw ex;
			}
		}
			
		try {
			return t.newTransformer();
		} catch (TransformerConfigurationException ex) {
			log.error(ex);
			throw new TransformerConfigurationException(
					"error: XSLT transformer "
							+ tname
							+ " contains an error; investigate logfile for details",
					ex);
		}
	}

//	/**
//	 * Transform an article in structured text to a suitable HTML
//	 * representation, i.e. one of the possible values of {@link OutputMode}.
//	 * 
//	 * This method is a wrapper method for the three pipeline transformations,
//	 * and should be used by external programs.
//	 * 
//	 * This method transforms an article using the default locale; to pass a
//	 * locale for transformation, call
//	 * {@link #transform(Article, OutputMode, Locale)}.
//	 * 
//	 * @param request
//	 * 
//	 * @param a
//	 *            the article
//	 * @param mode
//	 *            the output mode
//	 * @return the result of the transformation
//	 * @throws RenderingException
//	 */
//	public String transform(HttpServletRequest request, Article a,
//			OutputMode mode) throws RenderingException {
//		return transform(request, a, mode, Locale.getDefault());
//	}

//	/**
//	 * Transform an article in structured text to a suitable HTML
//	 * representation, i.e. one of the possible values of {@link OutputMode}.
//	 * 
//	 * This method is a wrapper method for the three pipeline transformations,
//	 * and should be used by external programs.
//	 * 
//	 * @param request
//	 * 
//	 * @param a
//	 *            the article
//	 * @param mode
//	 *            the output mode
//	 * @param locale
//	 *            the locale, required in the cases
//	 *            <code>mode = HTML | ANNOTATIONS | EDITOR</code>,
//	 * @return the result of the transformation
//	 * @throws RenderingException
//	 */
//	public String transform(HttpServletRequest request, Article a,
//			OutputMode mode, Locale locale) throws RenderingException {
//		StringWriter out = new StringWriter();
//		transformXML(request, a, mode, locale, new StreamResult(out));
//		String result = out.toString();
//
//		if (mode == OutputMode.EDITOR) {
//			/* Post-process document for WYSIWYG editing */
//			result = result.substring(result.indexOf("<div>") + 5, result
//					.lastIndexOf("</div>"));
//			result = editorLayoutHacks(result);
//			log.debug("Result for Editor: " + result);
//		} else if (mode == OutputMode.ANNOTATIONS || mode == OutputMode.HTML) {
//			/*
//			 * Making MathML Dojo compliant: post-processing See
//			 * http://wiki.kiwi-project.eu/atlassian-jira/browse/SWIM-11
//			 */
//			result = result.replaceAll("(<script[^>]*>)<!\\[CDATA\\[", "$1");
//			result = result.replaceAll("\\]\\]></script>", "</script>");
//		}
//
//		return result;
//	}

//	public void transformXML(HttpServletRequest request, Article a,
//			OutputMode mode, Locale locale, Result result)
//			throws RenderingException {
//		long start = System.currentTimeMillis();
//
//		/* Create a DOM, either the base DOM or the extended DOM */
//		Document d;
//		switch (mode) {
//		case SEARCH_RESULT:
//		case RECENT_CHANGES_RESULT:
//		case RECENT_CHANGES_RESULT_RSS:
//		case ARTICLE_CONTENT:
//			/*
//			 * do not apply the complex "Wiklets" but only render the structured
//			 * text
//			 */
//			if (a.getBaseDom() == null) {
//				createDOM(a);
//			}
//			d = a.getBaseDom(true);
//			break;
//
//		/*
//		 * TODO is the extended DOM really needed for the cases POST, TOOLTIP,
//		 * and DYNAMONT_TYPE_TEMPLATE? The javadoc said no, but the
//		 * implementation generated the extended DOM.
//		 */
//		default:
//			if (EnumSet.of(OutputMode.Type.MATH_EXPORT,
//					OutputMode.Type.RDF_EXTRACTION,
//					OutputMode.Type.STRIP_METADATA).contains(mode.getType())) {
//				/*
//				 * same as in the other cases above, just that we don't want to
//				 * enumerate a finite set of cases for these output modes
//				 */
//				if (a.getBaseDom() == null) {
//					createDOM(a);
//				}
//				d = a.getBaseDom(true);
//
//				if (OutputMode.Type.RDF_EXTRACTION == mode.getType()) {
//					/*
//					 * RDF extraction ignores the wif:page wrapper, it only
//					 * operates on the first child of wif:content, which is
//					 * supposed to contain semantic markup
//					 */
//					DOMResult contentResult = new DOMResult();
//					processStylesheetXML(request, d, a, XSLT_EXTRACT_CONTENT,
//							locale, contentResult);
//					d = (Document) contentResult.getNode();
//					d.setDocumentURI(a.getUri());
//
//					/*
//					 * TODO here, split the document (by running import-...)!
//					 * Then, extract RDF For the splitting, we have to (somewhat
//					 * redundantly) examine the root element of the document and
//					 * see of what type T it is, so we can run import-T.xsl
//					 */
//				}
//			} else {
//				d = createExtendedDOM(a);
//			}
//		}
//
//		processStylesheetXML(request, d, a, stylesheet_for_output_mode
//				.get(mode), locale, result);
//
//		long end = System.currentTimeMillis();
//
//		log.debug("transformation from structured text to " + mode + " took "
//				+ (end - start) + " ms");
//	}

//	/**
//	 * Generic transformation of DOM document to another representation; used by
//	 * subsequent methods. This method applies the transformation from the
//	 * (enriched) XML representation of the page to an HTML representation, as
//	 * requested for the given {@link OutputMode}.
//	 * 
//	 * @param d
//	 *            Document to transform
//	 * @param context
//	 *            the WikiResource that is the context of this process
//	 * @param stylesheet
//	 *            the name of the stylesheet to apply
//	 * @return a string representing the transformed document
//	 * @throws RenderingException
//	 */
//	protected String processStylesheet(HttpServletRequest request, Document d,
//			Article context, String stylesheet, Locale locale)
//			throws RenderingException {
//		StringWriter out = new StringWriter();
//		processStylesheetXML(request, d, context, stylesheet, locale,
//				new StreamResult(out));
//		return out.toString();
//	}

//	/**
//	 * Generic transformation of DOM document to another XML representation;
//	 * used by subsequent methods. This method applies the transformation from
//	 * the (enriched) XML representation of the page to the representation
//	 * requested by the given {@link OutputMode}.
//	 * 
//	 * @param d
//	 *            Document to transform
//	 * @param context
//	 *            the WikiResource that is the context of this process
//	 * @param stylesheet
//	 *            the name of the stylesheet to apply
//	 * @param result
//	 *            the object receiving the result of the transformation
//	 * @return a string representing the transformed document
//	 * @throws RenderingException
//	 */
//	protected void processStylesheetXML(HttpServletRequest request, Document d,
//			Article context, String stylesheet, Locale locale, Result result)
//			throws RenderingException {
//		try {
//			Transformer t = getTransformer(stylesheet);
//
//			synchronized (d) { // Saxon appears to be not thread safe at
//				// times...
//				t.setParameter("base_uri", main.getBaseUri(request));
//				t.setParameter("language", locale.getLanguage());
//
//				DOMSource ds = new DOMSource(d);
//				ds.setSystemId(d.getDocumentURI() != null ? d.getDocumentURI()
//						: "http://ikewiki.srfg.at/Internal");
//
//				if (stylesheet.equals("transform-html.xsl")
//						|| stylesheet.equals("transform-annotations.xsl")) {
//					DOMResult dr = new DOMResult();
//					t.transform(ds, dr);
//
//					/*
//					 * first post-processing step: MathML symbol linking
//					 * (parallel markup)
//					 */
//					t = getTransformer(XSLT_MATHML_LINKSYMBOLS);
//					t.setParameter("base_uri", main.getBaseUri(request));
//					ds = new DOMSource(dr.getNode());
//					ds.setSystemId(d.getDocumentURI() != null ? d
//							.getDocumentURI()
//							: "http://ikewiki.srfg.at/Internal");
//					DOMResult dr2 = new DOMResult();
//					t.transform(ds, dr2);
//
//					/*
//					 * the processing to HTML results in XHTML with embedded
//					 * MathML fragments, which is incompatible with Dojo. This
//					 * stylesheet post-processes the document, changing all
//					 * MathML fragments into JavaScript code that inserts them
//					 * at runtime.
//					 * 
//					 * http://wiki.kiwi-project.eu/atlassian-jira/browse/SWIM-11
//					 */
//					t = getTransformer(XSLT_MATHML_JAVASCRIPT);
//					t.setParameter("dojo-output", true);
//					ds = new DOMSource(dr2.getNode());
//					ds.setSystemId(d.getDocumentURI() != null ? d
//							.getDocumentURI()
//							: "http://ikewiki.srfg.at/Internal");
//				}
//
//				t.transform(ds, result);
//			}
//		} catch (TransformerException ex) {
//			log.error("error while applying transformation (" + stylesheet
//					+ ")", ex);
//			throw new RenderingException(ex);
//		}
//	}

	public Document transform(KiWiResource context, Document document, String stylesheet) {
		return transform(context, document, stylesheet, null);
	}
	
	public Document transform(KiWiResource context, Document document, String stylesheet, Map<String, Object> parameters) {
		if (document == null) {
			log.warn("document is null at context #0", context);
			return null;
		}
		
		Transformer t = null;
		Nodes nodes = null;
		
		try {
			t = getTransformer(stylesheet);
		} catch (TransformerConfigurationException e) {
			// FIXME handle properly SWIM-67
			throw new RuntimeException("could not get transformer", e);
		} catch (IOException e) {
			// FIXME handle properly SWIM-67
			throw new RuntimeException("could not get transformer", e);
		}

		synchronized (document) { // Saxon appears to be not thread safe at times...
			if (parameters != null) {
				for (Map.Entry<String, Object> p: parameters.entrySet()) {
					t.setParameter(p.getKey(), p.getValue());
				}
			}
			
			/*
			 * TODO when enabling these again, make sure that it is documented
			 * that either these parameters override the ones passed into the
			 * method, or vice versa.  Document in the super method
			 */
			// t.setParameter("base_uri", main.getBaseUri(request));
			// t.setParameter("language", locale.getLanguage());
		
			Source xs = null;
			Result xr = null;
			try {
				log.info("input: #0", document.toXML());
				
				xs = createXomSource(document);
				xr = createXomResult();
			} catch (IllegalAccessException e) {
				// FIXME handle properly SWIM-67
				throw new RuntimeException("could not create XOM source or result", e);
			} catch (InvocationTargetException e) {
				// FIXME handle properly SWIM-67
				throw new RuntimeException("could not create XOM source or result", e);
			} catch (InstantiationException e) {
				// FIXME handle properly SWIM-67
				throw new RuntimeException("could not create XOM source or result", e);
			}
			
			// TODO verify -- in IkeWiki, a special string constant was used when the base URI was null SWIM-68
			xs.setSystemId(document.getBaseURI());
			try {
				t.transform(xs, xr);
			} catch (TransformerException e) {
				// FIXME handle properly SWIM-67
				throw new RuntimeException("could not transform document", e);
			}
			
			try {
				nodes = xomResultGetResult(xr);
			} catch (IllegalAccessException e) {
				// FIXME handle properly SWIM-67
				throw new RuntimeException("could not obtain result node set", e);
			} catch (InvocationTargetException e) {
				// FIXME handle properly SWIM-67
				throw new RuntimeException("could not obtain result node set", e);
			}
		}

		// transform the node set back to a document
		Document result = null;
		try {
			result = XSLTransform.toDocument(nodes);
		} catch (XMLException e) {
			log.error("transformation with #0 did not return a proper document for context #1", stylesheet, context);
			throw e;
		}
		return result;
	}

	@Destroy
	public void shutdown() {
	}
}
