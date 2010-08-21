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
package kiwi.service.importexport.exporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.Stateless;

import kiwi.api.event.KiWiEvents;
import kiwi.api.importexport.ExportService;
import kiwi.api.importexport.exporter.ExporterLocal;
import kiwi.api.importexport.exporter.ExporterRemote;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiAnonResource;
import kiwi.model.kbase.KiWiTriple;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.tagging.Tag;
import kiwi.service.triplestore.TripleStoreUtil;
import kiwi.util.KiWiFormatUtils;
import kiwi.util.MD5;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.sail.memory.MemoryStore;

/**
* An exporter that allows to export items and triples in KiWi's own exchange format.
* <p>
* The KiWi exchange format is a ZIP file with file extenstion .kiwi and the following
* subdirectories:
* <ul>
* <li>items - contains for each content item an XML file describing the properties of the content item; file
*     names are MD5 hashes of the KiWi identifier </li>
* <li>media - contains all multimedia content that is part of the export; for each media content, the directory
*     contains a file representing the binary media file with the original file suffix (e.g. .jpg or .png) and an
*     XML file describing the media content with suffix ".meta"; the part of the file name before the suffix is
*     the same MD5 hash of the KiWi identifier as used for the item the media content belongs to.</li>
* <li>metadata - contains a single RDF/XML file containing all the triple data that is part of this export</li>
* </ul> 
* 
* @author Sebastian Schaffert
*
*/
@Stateless
@Name("kiwi.service.exporter.kiwi")
@Scope(ScopeType.STATELESS)
public class KiWiExporterImpl implements ExporterLocal, ExporterRemote {

	@Logger
	private Log log;

	@In(create = true)
	private TripleStore tripleStore;
	
	@In
	private TaggingService taggingService;

	private static String[] mime_types = {
		"application/x-kiwi"
	};

	@Observer(KiWiEvents.EXPORTSERVICE_INIT) 
	@BypassInterceptors
	public void initialise() {
		log.info("registering KiWi exporter ...");
		
		ExportService ies = (ExportService) Component.getInstance("kiwi.core.exportService");
		
		ies.registerExporter(this.getName(),"kiwi.service.exporter.kiwi",this);
	}
	
	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#exportItems(java.lang.Iterable, java.lang.String)
	 */
	@Override
	public String exportItems(Iterable<ContentItem> items, String format) {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		exportItems(items,format,out);
		try {
			return new String(out.toByteArray(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#exportItems(java.lang.Iterable, java.lang.String, java.io.OutputStream)
	 */
	@Override
	public void exportItems(Iterable<ContentItem> items, String format, OutputStream out) {
		try {
			ZipOutputStream zout = new ZipOutputStream(out);
			
			writeItems(items, zout);
			writeMedia(items, zout);
			
			Repository rep = createRepositoryItems(items);
			writeMetadata(rep, zout);
			
			
			zout.close();
		} catch(IOException ex) {
			log.error("I/O error while exporting KiWi content: #0", ex.getMessage());
		} catch (RepositoryException e) {
			log.error("repository error while exporting KiWi content: #0", e.getMessage());
		}

	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#exportTriples(java.lang.Iterable, java.lang.String)
	 */
	@Override
	public String exportTriples(Iterable<KiWiTriple> triples, String format) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		exportTriples(triples,format,out);
		try {
			return new String(out.toByteArray(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#exportTriples(java.lang.Iterable, java.lang.String, java.io.OutputStream)
	 */
	@Override
	public void exportTriples(Iterable<KiWiTriple> triples, String format, OutputStream out) {
		Set<ContentItem> items = new HashSet<ContentItem>();
		for(KiWiTriple t : triples) {
			items.add(t.getSubject().getContentItem());
		}
		
		try {
			ZipOutputStream zout = new ZipOutputStream(out);
			
			writeItems(items, zout);
			writeMedia(items, zout);
			
			Repository rep = createRepositoryTriples(triples);
			writeMetadata(rep, zout);
			
			
			zout.close();
		} catch(IOException ex) {
			log.error("I/O error while exporting KiWi content: #0", ex.getMessage());
		} catch (RepositoryException e) {
			log.error("repository error while exporting KiWi content: #0", e.getMessage());
		}
		
		
	}
	
	@Override
	public void exportUpdateOntology(Iterable<ContentItem> items, String format, OutputStream out, Date since) {
		log.info("Not implemented yet");
	}
	

	/**
	 * Create the items/ directory in the ZIP file and write all content item XML representations to it.
	 * @param items
	 * @param zout
	 * @throws IOException
	 */
	private void writeItems(Iterable<ContentItem> items, ZipOutputStream zout) throws IOException {
		ZipEntry entry = new ZipEntry("items/");
		zout.putNextEntry(entry);
		zout.closeEntry();
		
		for(ContentItem i : items) {
			writeItem(i,zout);
		}
	}
	
	private void writeMedia(Iterable<ContentItem> items, ZipOutputStream zout) throws IOException {
		ZipEntry entry = new ZipEntry("media/");
		zout.putNextEntry(entry);
		zout.closeEntry();
		
		for(ContentItem i : items) {
			if(i.getMediaContent() != null) {
				writeMedia(i,zout);
			}
		}
	}
	
	private void writeMetadata(Repository repository, ZipOutputStream zout) throws IOException {
		ZipEntry entry = new ZipEntry("metadata/");
		zout.putNextEntry(entry);
		zout.closeEntry();
		
		ZipEntry rdfFile = new ZipEntry("metadata/metadata.rdf");
		zout.putNextEntry(rdfFile);
		
		try {
			RDFHandler handler = new RDFXMLPrettyWriter(zout);

			RepositoryConnection myCon = repository.getConnection();
			myCon.export(handler);
			myCon.close();
		} catch (RDFHandlerException ex) {
			log.error("error while exporting data to RDF/XML", ex);
		} catch (RepositoryException ex) {
			log.error("error while exporting data to RDF/XML", ex);
		}
	        
		zout.closeEntry();
	}
	
	/**
	 * Write the XML representation of the ContentItem into the ZipOutputStream. Creates a new
	 * ZIP entry, writes the XML content to the stream, and closes the entry.
	 * 
	 * @param item
	 * @param zout
	 * @throws IOException
	 */
	private void writeItem(ContentItem item, ZipOutputStream zout) throws IOException {
		String filename = MD5.md5sum(item.getKiwiIdentifier()) + ".xml";
		
		ZipEntry entry = new ZipEntry("items/"+filename);
		zout.putNextEntry(entry);
		
		Element root = createContentItemXML(item);
		Document doc = new Document(root);
		
		Serializer serializer = new Serializer(zout, "UTF-8");
		serializer.setIndent(4);
		serializer.write(doc);
		
		zout.closeEntry();
	}
	
	/**
	 * Write the media content associated with a content item to the media directory.
	 * Creates a new entry in the ZIP stream for the binary content of the media content
	 * and a new entry for the metadata associated with the media file in XML format;
	 * both entries are contained in the media directory.
	 * 
	 * @param item
	 * @param zout
	 * @throws IOException
	 */
	private void writeMedia(ContentItem item, ZipOutputStream zout) throws IOException {
		// write the metadata of the media content to a file ending with .meta
		String metaName = MD5.md5sum(item.getKiwiIdentifier()) + ".meta";
		ZipEntry entry1 = new ZipEntry("media/"+metaName);
		zout.putNextEntry(entry1);
		Element root = createMediaContentXML(item);
		Document doc = new Document(root);
		
		Serializer serializer = new Serializer(zout, "UTF-8");
		serializer.setIndent(4);
		serializer.write(doc);
		zout.closeEntry();
		
		// write the binary data of the media content to a file ending with .bin
		String mediaName = MD5.md5sum(item.getKiwiIdentifier()) + ".bin";
		ZipEntry entry2 = new ZipEntry("media/"+mediaName);
		zout.putNextEntry(entry2);
		zout.write(item.getMediaContent().getData());
		zout.closeEntry();
	}
	
	
	
	/**
	 * Create the XML representation of a Content Item. The format of this representation is as 
	 * follows:
	 * <pre><![CDATA[
		<content-item xmlns="http://www.kiwi-project.eu/kiwi/export">
			<uri>URI of Resource</uri>
			<anon-id>Anonymous ID of Resource</anon-id>
			<title>Title of Content Item</title>
			<modified>...</modified>
			<created>...</created>
			<author>KiWi-ID of Author</author>
			<content>
			   XML Content of Content Item
			</content>
			<taggings>
			   <tagging>
			      <tagged-by>KiWi-ID of author</tagged-by>
			      <tagging-item>KiWi-ID of content item used as tag</tagging-item>
			      <tagging-date>...</tagging-date>
			   </tagging>
			   ...
			</taggings>
		</content-item>
	 ]]></pre>
	 * @param item
	 * @return
	 */
	private Element createContentItemXML(ContentItem item) {
		Element root = new Element("content-item", Constants.NS_KIWI_EXPORT);
		
		if(item.getResource().isAnonymousResource()) {
			Element anonId = new Element("anon-id", Constants.NS_KIWI_EXPORT);
			anonId.appendChild( ((KiWiAnonResource)item.getResource()).getAnonId() );
			root.appendChild(anonId);
		} else {
			Element uri    = new Element("uri", Constants.NS_KIWI_EXPORT);
			uri.appendChild( ((KiWiUriResource)item.getResource()).getUri());
			root.appendChild(uri);
		}
		
		if(item.getTitle() != null) {
			Element title = new Element("title", Constants.NS_KIWI_EXPORT);
			title.appendChild(item.getTitle());
			root.appendChild(title);
		}
		
		if(item.getModified() != null) {
			Element modified = new Element("modified", Constants.NS_KIWI_EXPORT);
			modified.appendChild(KiWiFormatUtils.ISO8601FORMAT.format(item.getModified()));
			root.appendChild(modified);
		}
		
		if(item.getCreated() != null) {
			Element created = new Element("created", Constants.NS_KIWI_EXPORT);
			created.appendChild(KiWiFormatUtils.ISO8601FORMAT.format(item.getCreated()));
			root.appendChild(created);
		}
		
		if(item.getAuthor() != null) {
			Element author = new Element("author", Constants.NS_KIWI_EXPORT);
			author.appendChild(item.getAuthor().getResource().getKiwiIdentifier());
			root.appendChild(author);
		}
		
		if(item.getTextContent() != null) {
			Element content = new Element("content", Constants.NS_KIWI_EXPORT);
			content.appendChild(item.getTextContent().getXmlDocument().getRootElement().copy());
			root.appendChild(content);
		}
		
		List<Tag> taggings = taggingService.getTags(item);
		if(taggings.size() > 0) {
			Element el_taggings = new Element("taggings", Constants.NS_KIWI_EXPORT);
			for(Tag t : taggings) {
				Element el_tagging = new Element("tagging", Constants.NS_KIWI_EXPORT);
				
				Element tagged_by = new Element("tagged-by", Constants.NS_KIWI_EXPORT);
				tagged_by.appendChild(t.getTaggedBy().getResource().getKiwiIdentifier());
				el_tagging.appendChild(tagged_by);
				
				Element tagged_on = new Element("tagging-date", Constants.NS_KIWI_EXPORT);
				tagged_on.appendChild(KiWiFormatUtils.ISO8601FORMAT.format(t.getCreationTime()));
				el_tagging.appendChild(tagged_on);
				
				Element tagging_item = new Element("tagging-item", Constants.NS_KIWI_EXPORT);
				tagging_item.appendChild(t.getTaggingResource().getKiwiIdentifier());
				el_tagging.appendChild(tagging_item);
				
				el_taggings.appendChild(el_tagging);
				
			}
			root.appendChild(el_taggings);
		}
		return root;
	}
	
	/**
	 * Create the metadata descriptor in XML format for media content.
	 * The XML format for the metadata of media content is as follows:
	 * <pre><![CDATA[
		<media-content xmlns="http://www.kiwi-project.eu/kiwi/export/">
			<uri>URI of Resource</uri>
			<anon-id>Anonymous ID of Resource</anon-id>
			<file-name>The original filename of the media file</file-name>
			<mime-type>The MIME type of the media content</mime-type>
			<created>...</created>
		</media-content>
	 ]]> 
	 * @param item
	 * @return
	 */
	private Element createMediaContentXML(ContentItem item) {
		Element root = new Element("media-content", Constants.NS_KIWI_EXPORT);
		
		if(item.getResource().isAnonymousResource()) {
			Element anonId = new Element("anon-id", Constants.NS_KIWI_EXPORT);
			anonId.appendChild( ((KiWiAnonResource)item.getResource()).getAnonId() );
			root.appendChild(anonId);
		} else {
			Element uri    = new Element("uri", Constants.NS_KIWI_EXPORT);
			uri.appendChild( ((KiWiUriResource)item.getResource()).getUri());
			root.appendChild(uri);
		}
		
		if(item.getCreated() != null) {
			Element created = new Element("created", Constants.NS_KIWI_EXPORT);
			created.appendChild(KiWiFormatUtils.ISO8601FORMAT.format(item.getCreated()));
			root.appendChild(created);
		}
		
		Element file_name = new Element("file-name", Constants.NS_KIWI_EXPORT);
		file_name.appendChild(item.getMediaContent().getFileName());
		root.appendChild(file_name);
		
		Element mime_type = new Element("mime-type", Constants.NS_KIWI_EXPORT);
		mime_type.appendChild(item.getMediaContent().getMimeType());
		root.appendChild(mime_type);
		
		return root;
	}
	
	
	/**
	 * Create a in-memory Sesame repository containing the triples concerning the items passed as 
	 * argument.
	 * <p>
	 * The collection of triples is constructed as follows:
	 * <ul>
	 * <li>for each item, all outgoing triples are included</li>
	 * <li>for each item, the triples of all taggings that have this item as "taggedItem" are included</li>
	 * </ul>
	 * 
	 * @param triples
	 * @return
	 */
	private final Repository createRepositoryItems(Iterable<ContentItem> items) throws RepositoryException {
		Repository myRepository = new SailRepository(new MemoryStore());

		myRepository.initialize();
		RepositoryConnection myCon = myRepository.getConnection();

		ValueFactory f = myRepository.getValueFactory();

		for(ContentItem item : items) {
		
			for(KiWiTriple t : item.getResource().listOutgoing()) {
				myCon.add(TripleStoreUtil.transformKiWiToSesame(f, t));
			}
			
			for(Tag t : taggingService.getTags(item)) {
				for(KiWiTriple triple : t.getResource().listOutgoing()) {
					myCon.add(TripleStoreUtil.transformKiWiToSesame(f, triple));
				}
				
			}
		}

		myCon.close();

		return myRepository;
	}
	
	/**
	 * Create a in-memory Sesame repository containing the triples passed as argument.
	 * 
	 * @param triples
	 * @return
	 */
	private final Repository createRepositoryTriples(Iterable<KiWiTriple> triples) throws RepositoryException {
		Repository myRepository = new SailRepository(new MemoryStore());

		myRepository.initialize();
		RepositoryConnection myCon = myRepository.getConnection();

		ValueFactory f = myRepository.getValueFactory();

		for(KiWiTriple t : triples) {
			myCon.add(TripleStoreUtil.transformKiWiToSesame(f, t));
		}

		myCon.close();

		return myRepository;
	}
	
	/**
	 * Return a collection of mime types (as string) that represent the mime types supported by
	 * this exporter for exporting content items.
	 * 
	 * @see kiwi.api.importexport.exporter.Exporter#getAcceptItemTypes()
	 */
	@Override
	@BypassInterceptors
	public Set<String> getAcceptItemTypes() {
		return new HashSet<String>(Arrays.asList(mime_types));
	}

	/**
	 * Return a collection of mime types (as string) that represent the mime types supported by
	 * this exporter for exporting triples.
	 * 
	 * @see kiwi.api.importexport.exporter.Exporter#getAcceptTripleTypes()
	 */
	@Override
	@BypassInterceptors
	public Set<String> getAcceptTripleTypes() {
		return new HashSet<String>(Arrays.asList(mime_types));
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#getDescription()
	 */
	@Override
	@BypassInterceptors
	public String getDescription() {
		return "An Exporter supporting to export data into KiWi's native format";
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#getName()
	 */
	@Override
	@BypassInterceptors
	public String getName() {
		return "KiWi Exporter";
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#isItemExportSupported()
	 */
	@Override
	@BypassInterceptors
	public boolean isItemExportSupported() {
		return true;
	}

	/* (non-Javadoc)
	 * @see kiwi.api.importexport.exporter.Exporter#isTripleExportSupported()
	 */
	@Override
	@BypassInterceptors
	public boolean isTripleExportSupported() {
		return true;
	}

}
