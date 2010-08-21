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

package kiwi.service.multimedia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.ejb.Stateless;

import kiwi.api.multimedia.MultimediaServiceLocal;
import kiwi.api.multimedia.MultimediaServiceRemote;
import kiwi.api.query.sparql.SparqlService;
import kiwi.api.triplestore.TripleStore;
import kiwi.exception.InvalidArgumentException;
import kiwi.exception.NamespaceResolvingException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.content.MediaContent;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiResource;
import kiwi.service.multimedia.aperture.FileCrawler;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.HWPFDocument;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.ontoware.rdf2go.exception.ModelException;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentInformation;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;

/**
 * @author Sebastian Schaffert
 * 
 */
@Stateless
@Name("multimediaService")
@AutoCreate
//@Transactional
public class MultimediaServiceImpl implements MultimediaServiceLocal, MultimediaServiceRemote {

	@Logger
	private Log log;

	@In("kiwi.query.sparqlService")
	private SparqlService sparqlService;

	@In
	private TripleStore tripleStore;

	/**
	 * Extract RDF metadata from the media content object passed as argument.
	 * Currently works for images, for PDF documents, and for MS Word documents.
	 * The method expects a fully specified media content with correct mime
	 * type.
	 */
	public void extractMetadata(ContentItem item) {
		MediaContent mediaContent = item.getMediaContent();

		if (mediaContent != null) {

			String mime_type = mediaContent.getMimeType();

			if (mime_type == null || mime_type.equals("")) {
				throw new InvalidArgumentException(
						"the mime type of the content item was not set correctly");
			}

			String iw_type = "MultimediaObject";
			if (mime_type.startsWith("image")) {
				iw_type = "Image";
			} else if (mime_type.startsWith("video/flash")) {
				iw_type = "FlashVideo";
			} else if (mime_type.startsWith("video")) {
				iw_type = "Video";
			} else if (mime_type.startsWith("application/pdf")) {
				iw_type = "PDFDocument";
			} else if (mime_type.startsWith("application/msword")) {
				iw_type = "MSWordDocument";
			} else if (mime_type
					.startsWith("application/vnd.oasis.opendocument")
					|| mime_type.startsWith("application/postscript")
					|| mime_type.startsWith("application/vnd.ms-")) {
				iw_type = "Document";
			} else if (mime_type.startsWith("audio/mpeg")
					|| mime_type.startsWith("audio/mp3")) {
				iw_type = "MP3Audio";
			} else if (mime_type.startsWith("audio")) {
				iw_type = "Audio";
			}

			KiWiResource subject = item.getResource();
			KiWiResource t_image = tripleStore.createUriResource(Constants.NS_KIWI_CORE + iw_type);

			// set type to image so that the renderer can determine the type
			// correctly
			try {
				subject.addOutgoingNode("<" + Constants.NS_RDF + "type" + ">", t_image);

				// set the mime type properly
				subject.setProperty(
					"<" + Constants.NS_KIWI_CORE + "mimeType" + ">", mime_type);
			} catch (NamespaceResolvingException e1) {
				e1.printStackTrace();
			}
			
			// ontology.begin();

			if (mime_type.startsWith("image")) {

				// Store EXIF metadata, if the exif ontology is loaded
				try {

					Metadata metadata = JpegMetadataReader
							.readMetadata(mediaContent.getDataInputStream());

					// iterate through metadata directories
					Iterator directories = metadata.getDirectoryIterator();
					while (directories.hasNext()) {
						Directory directory = (Directory) directories.next();
						// iterate through tags and print to System.out
						Iterator tags = directory.getTagIterator();
						while (tags.hasNext()) {
							Tag tag = (Tag) tags.next();
							// use Tag.toString()

							KiWiResource prop = getEXIFProperty(tag.getTagType(),tag.getTagName().toLowerCase());

							if (prop != null) {
								try {
									try {
										subject.setProperty(prop.getSeRQLID(), tag.getDescription());
									} catch (NamespaceResolvingException e) {
										e.printStackTrace();
									}
								} catch (MetadataException e) {
									log.warn("error extracting metadata for tag #0  (id: #1)", tag.getTagName(), tag.getTagType());
								}
							}
						}
					}

				} catch (JpegProcessingException ex) {
					log.error(ex);
				}
			} else if (mime_type.startsWith("application/pdf")) {
				// for PDF documents, we extract the metadata using PDFBox and
				// store it in appropriate
				// Dublin Core elements
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

					PDDocument doc = PDDocument.load(mediaContent.getDataInputStream());
					PDDocumentInformation info = doc.getDocumentInformation();

					if (info.getAuthor() != null
							&& !info.getAuthor().equals(""))
						try {
							subject.setProperty("<" + Constants.NS_DC + "creator" + ">", info.getAuthor());
						} catch (NamespaceResolvingException e) {
							e.printStackTrace();
						}

					if (info.getSubject() != null
							&& !info.getSubject().equals(""))
						try {
							subject.setProperty("<" + Constants.NS_DC + "subject" + ">", info.getSubject());
						} catch (NamespaceResolvingException e) {
							e.printStackTrace();
						}

					if (info.getTitle() != null && !info.getTitle().equals(""))
						try {
							subject.setProperty("<" + Constants.NS_DC + "title" + ">", info.getTitle());
						} catch (NamespaceResolvingException e) {
							e.printStackTrace();
						}

					if (info.getModificationDate() != null) {
						try {
							subject.setProperty("<" + Constants.NS_DC + "date" + ">", 
									            df.format(info.getModificationDate().getTime()));
						} catch (NamespaceResolvingException e) {
							e.printStackTrace();
						}
					}

					doc.close();
				} catch (IOException ex) {
					log.error("I/O error while extracting PDF metadata (should never happen): ",ex);
				} 
			} else if (mime_type.startsWith("application/msword")
					|| mime_type.startsWith("application/vnd.ms-")) {
				// for MS Office documents, we extract the metadata using POI
				// and store it in appropriate
				// Dublin Core elements
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

					HWPFDocument doc = new HWPFDocument(mediaContent.getDataInputStream());

					SummaryInformation info = doc.getSummaryInformation();
					DocumentSummaryInformation info2 = doc
							.getDocumentSummaryInformation();

					if (info.getAuthor() != null && !info.getAuthor().equals(""))
						try {
							subject.setProperty("<" + Constants.NS_DC + "creator" + ">", info.getAuthor());
						} catch (NamespaceResolvingException e) {
							e.printStackTrace();
						}

					if (info.getSubject() != null && !info.getSubject().equals(""))
						try {
							subject.setProperty("<" + Constants.NS_DC + "subject" + ">", info.getSubject());
						} catch (NamespaceResolvingException e) {
							e.printStackTrace();
						}

					if (info.getTitle() != null && !info.getTitle().equals(""))
						try {
							subject.setProperty("<" + Constants.NS_DC + "title" + ">", info.getTitle());
						} catch (NamespaceResolvingException e) {
							e.printStackTrace();
						}

					if (info.getLastSaveDateTime() != null)
						try {
							subject.setProperty("<" + Constants.NS_DC + "date" + ">", df.format(info.getLastSaveDateTime().getTime()));
						} catch (NamespaceResolvingException e) {
							e.printStackTrace();
						}

					if (info.getKeywords() != null && !info.getKeywords().equals(""))
						try {
							subject.setProperty("<" + Constants.NS_DC + "subject" + ">", info.getTitle());
						} catch (NamespaceResolvingException e) {
							e.printStackTrace();
						}

					if (info2.getCompany() != null && !info2.getCompany().equals(""))
						try {
							subject.setProperty("<" + Constants.NS_DC + "publisher" + ">", info.getTitle());
						} catch (NamespaceResolvingException e) {
							e.printStackTrace();
						}

				} catch (IOException ex) {
					log.error("I/O error while extracting MS Office metadata (should never happen): ", ex);
				}
			}
		} else {
			log.error("the content item #0 does not contain media content", item.getResource().getKiwiIdentifier());
		}
	}

	/**
	 * Retrieve the EXIF property associated with the given tag id
	 * 
	 * @param tag_id
	 * @return
	 */
	private KiWiResource getEXIFProperty(int tag_id, String tag_name) {
		String query = "SELECT P FROM {P} <" + Constants.NS_EXIF
				+ "exifNumber> {\"" + tag_id + "\"} ";

		Iterator<KiWiResource> results = sparqlService.queryResource(query,
				KiWiQueryLanguage.SERQL).iterator();

		KiWiResource result = null;
		
		while (results.hasNext()) {
			result = results.next();
			if( result.getLabel().toLowerCase().equals(tag_name) ) {
				break;
			}
		}
		
		if( result != null && result.getLabel().toLowerCase().equals(tag_name) ) {
			log.info("EXIF property '#0' found for ID #1", result.getLabel(), tag_id);
			return result;
		} else {
			log.info("No EXIF property found for ID #0 (#1)", tag_id, tag_name);
			return null;
		}
	}

	/**
	 * Determine the mime type of the file with the given name and content.
	 * Filename may be null, in which case the system has to solely rely on
	 * magic detection of the content, which may be slow and unreliable.
	 * 
	 * @param filename
	 * @param data
	 * @return a string representing the mime type of the content
	 */
	public String getMimeType(String filename, byte[] data) {
		String type;

		// only run jmimemagic if file is smaller than 10MB; for larger files,
		// jmimemagic blows up the heap too much
		if (data.length < 10 * 1024 * 1024) {
			try {
				new Magic();
				MagicMatch match = Magic.getMagicMatch(data, false);
				type = match.getMimeType();
			} catch (MagicException ex) {
				log.error("error while determining MIME type (no match)", ex);
				type = getMimeTypeByFileName(filename);
				log.info("guessed MIME type based on filename: #0", type);
			} catch (MagicParseException e) {
				log.error("error while determining MIME type (no match)", e);
				type = getMimeTypeByFileName(filename);
				log.info("guessed MIME type based on filename: #0", type);
			} catch (MagicMatchNotFoundException e) {
				log.error("error while determining MIME type (no match)", e);
				type = getMimeTypeByFileName(filename);
				log.info("guessed MIME type based on filename: #0", type);
			}
		} else {
			type = getMimeTypeByFileName(filename);
			log.info("guessed MIME type based on filename: #0", type);
		}
		return type;
	}

	/**
	 * Determine the mime type of the file provided as argument.
	 * 
	 * @param file
	 * @return a string representing the mime type of the content
	 */
	public String getMimeType(File file) {
		byte[] data = null;
		try {
			data = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			log.error("error reading file #0", file.getAbsolutePath());
		}
		if (data != null) {
			return getMimeType(file.getName(), data);
		} else {
			return getMimeTypeByFileName(file.getName());
		}
	}

	private static String getMimeTypeByFileName(String filename) {
		// TODO: read mime.types instead of hard coding
		if (filename.trim().toLowerCase().endsWith(".mp3")) {
			return "audio/mp3";
		} else if (filename.trim().toLowerCase().endsWith(".flv")) {
			return "video/flash";
		} else {
			return "application/unknown";
		}
	}

	public void apertureAnalysis(ContentItem item) {
		
		File file = null;
		try {
			file = File.createTempFile("kiwi_contentItem", "dat");
			
		} catch (IOException e) {
			log.error("Could not create TempFile for Aperture.", e);
		}
		
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			log.error("Temp representation file of ContentItem not found.", e);
		}
		try {
			outputStream.write(item.getMediaContent().getData());
			outputStream.close();
		} catch (IOException e) {
			log.error("Could not write MediaData to TempFile.", e);
		}
		
		FileCrawler fileCrawler = new FileCrawler();
		fileCrawler.setRootFile(file);
		try {
			fileCrawler.crawl();
		} catch (ModelException e) {
			log.error("ModelException during Aperture analysis.", e);
		}
		
		// TODO: Save results to ContentItem
	}
}
