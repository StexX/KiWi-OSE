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
package kiwi.service.importexport.importer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.importexport.ImportService;
import kiwi.api.importexport.importer.ImporterLocal;
import kiwi.api.importexport.importer.ImporterRemote;
import kiwi.api.tagging.TaggingService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.UserService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.service.importexport.importer.rss.POI;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.RunAsOperation;

import com.sun.syndication.feed.module.georss.GeoRSSModule;
import com.sun.syndication.feed.module.georss.GeoRSSUtils;
import com.sun.syndication.feed.module.mediarss.MediaEntryModule;
import com.sun.syndication.feed.module.mediarss.MediaModule;
import com.sun.syndication.feed.module.mediarss.types.Category;
import com.sun.syndication.feed.module.mediarss.types.MediaContent;
import com.sun.syndication.feed.module.mediarss.types.UrlReference;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwi.service.importer.rss")
@Scope(ScopeType.STATELESS)
public class RSSImporterImpl implements ImporterLocal, ImporterRemote {

	
	@Logger
	private Log log;

	@In(create = true)
	private TripleStore tripleStore;
	
	@In(create = true)
	private EntityManager entityManager;

	@In(create = true)
	private KiWiEntityManager kiwiEntityManager;
	
	@In(create = true)
	private ContentItemService contentItemService;
	
	@In(create = true)
	private UserService userService;
	
	@In(create = true)
	private TaggingService taggingService;
	
	
	private static Pattern p_hashtag = Pattern.compile("#([A-Za-z0-9_]+)");

	
	private static String[] mime_types = {
		"application/rss+xml",
		"application/atom+xml"
	};
	
	
	@Observer(KiWiEvents.SEAM_POSTINIT) 
	@BypassInterceptors
	public void initialise() {
		log.info("registering RSS/ATOM importer ...");
		
		ImportService ies = (ImportService) Component.getInstance("kiwi.core.importService");
		
		ies.registerImporter(this.getName(),"kiwi.service.importer.rss",this);
	}
	
	
	/**
	 * Get a collection of all mime types accepted by this importer. Used for automatically 
	 * selecting the appropriate importer in ImportService.
	 * 
	 * @return a set of strings representing the mime types accepted by this importer
	 */
	@Override
	public Set<String> getAcceptTypes() {
		return new HashSet<String>(Arrays.asList(mime_types));
	}

	/**
	 * Get a description of this importer for presentation to the user.
	 * 
	 * @return a string describing this importer for the user
	 */
	@Override
	public String getDescription() {
		return "Importer for parsing the RSS and Atom formats";
	}

	/**
	 * Get the name of this importer. Used for presentation to the user and for internal 
	 * identification.
	 * 
	 * @return a string uniquely identifying this importer
	 */
	@Override
	public String getName() {
		return "RSS/ATOM";
	}

	/**
	 * Import data from the input stream provided as argument into the KiWi database. 
	 * 
	 * @param url the url from which to read the data
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 */
	@Override
	public int importData(URL url, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) {
		
		if(!url.toString().contains("flickr")) {
			try {
				
				String myUrl = "http://ws.geonames.org/rssToGeoRSS?feedUrl=" + URLEncoder.encode(url.toString());
				
				return importData(new URL(myUrl).openStream(), format, types, tags, user, output);
			} catch (Exception ex) {
				try {
					log.info("geocoding RSS feed failed (message: #0); trying normal RSS feed", ex.getMessage());
					
					return importData(url.openStream(), format, types, tags, user, output);			
				} catch(IOException e) {
					log.error("I/O error while importing data from URL #0: #1",url, e.getMessage());
					return 0;
				}
			}
		} else {
			// flickr: no geocoding as it strips media content
			try {
				log.info("importing flickr RSS feed");
				
				return importData(url.openStream(), format, types, tags, user, output);			
			} catch(IOException e) {
				log.error("I/O error while importing data from URL #0: #1",url, e.getMessage());
				return 0;
			}
			
		}
		
	}

	/**
	 * Import data from the input stream provided as argument into the KiWi database. 
	 * 
	 * @param is the input stream from which to read the data
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 */
	@Override
	public int importData(InputStream is, String format, Set<KiWiUriResource> types, Set<ContentItem> tags, User user, Collection<ContentItem> output) {
		SyndFeedInput input = new SyndFeedInput();

		try {
			SyndFeed feed = input.build(new XmlReader(is));
			
			return importData(feed,types,tags,user,output);
		} catch(FeedException ex) {
			log.error("RSS/Atom feed could not be parsed",ex);
		} catch(IOException ex) {
			log.error("I/O error while building feed from input stream source",ex);
		}
		return 0;
	}

	/**
	 * Import data from the reader provided as argument into the KiWi database.
	 * 
	 * @param reader the reader from which to read the data
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 */
	@Override
	public int importData(Reader reader, String format, final Set<KiWiUriResource> types, 	final Set<ContentItem> tags, final User user, final Collection<ContentItem> output) {
		SyndFeedInput input = new SyndFeedInput();

		int count = 0;
		try {
			final SyndFeed feed = input.build(reader);
			
			Identity.setSecurityEnabled(false);
			count = importData(feed,types,tags,user,output);
			
		} catch(FeedException ex) {
			log.error("RSS/Atom feed could not be parsed",ex);
		}
		return count;
	}

	/**
	 * Import data from an RSS or atom feed using the ROME SyndFeed representation.
	 * 
	 * TODO: maybe we need to import each content item individually in a nested transaction ...
	 * 
	 * @param feed the ROME rss/atom feed representation
	 * @param types the set of types to associate with each generated content item
	 * @param tags the set of content items to use as tags
	 * @param user the user to use as author of all imported data
	 */
//	@Transactional
	public int importData(final SyndFeed feed, Set<KiWiUriResource> types, Set<ContentItem> tags, final User user, final Collection<ContentItem> output) {
		log.info("importing entries from #0 feed '#1' found at '#2'",feed.getFeedType(),feed.getTitle(),feed.getUri());
		
		if(types == null) {
			types = new HashSet<KiWiUriResource>();
		}
		
		if(tags == null) {
			tags = new HashSet<ContentItem>();
		}
		final Set<ContentItem> my_tags = tags;
		final Set<KiWiUriResource> my_types = types;
		
		// a hack for importing facebook activity streams: if the type is kiwi:FacebookPost,
		// turn facebook activity stream mode on; in this mode, we will skip all entries where
		// the remote author name and local user name are not identical
		boolean facebookImport = false;
		String t_facebookPost = Constants.NS_KIWI_CORE+"FacebookPost";
		for(KiWiUriResource r : types) {
			if(r.getUri().equals(t_facebookPost)) {
				facebookImport = true;
				break;
			}
		}
		
		for(final SyndEntry entry : (List<SyndEntry>)feed.getEntries()) {
			
			// facebook hack ... (see above)
			if(facebookImport && !entry.getAuthor().equalsIgnoreCase(user.getFirstName()+" "+user.getLastName())) {
				log.info("Facebook import: skipping friend post with title", entry.getTitle());
				continue;
			}
			
			
			new RunAsOperation(){
				@Override
				public void execute() {
					importEntry(feed,entry,my_types,my_tags,user,output);
				}
			}.addRole("admin").run();
		}
		
//		entityManager.flush();
		
		log.info("#0 content items have been imported from RSS/Atom feed", feed.getEntries().size());

		return feed.getEntries().size();
	}
	
	
//	@Transactional
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void importEntry(final SyndFeed feed, final SyndEntry entry, final Set<KiWiUriResource> types, final Set<ContentItem> tags, 
			User user, final Collection<ContentItem> output) {
		if(user == null && entry.getAuthor() != null && !"".equals(entry.getAuthor())){
			if(userService.userExists(entry.getAuthor())) {
				user = userService.getUserByLogin(entry.getAuthor());
			} else {
				
//				user = userService.createUser(entry.getAuthor());
				/* In my opinion, it is not ok to create a user entity 
				 * without asking the person if he/she wants to be 
				 * created and persisted in the KiWi dataset. 
				 * Thus I'm changing the user to 'anonymous', 
				 * if he/she is'nt registered with the same nick that 
				 * is given in the rss entry.
				 */
				user = userService.getUserByLogin("anonymous");
				kiwiEntityManager.persist(user);
			}
		}
						
		log.debug("feed entry: #0 (#1)", entry.getTitle(), entry.getUri());
		
		// create a new content item and copy all data from the feed entry
		ContentItem item;
		if(entry.getLink() != null) {
			item = contentItemService.createExternContentItem(entry.getLink()); 			
		} else if(entry.getUri() != null) {
			try {
				// try parsing URI; if it is not valid, 
				URI uri = new URI(entry.getUri());
				item = contentItemService.createExternContentItem(entry.getUri()); 
			} catch (URISyntaxException e) {
				item = contentItemService.createExternContentItem(feed.getLink() + "#" + entry.getUri());
			}
		} else {
			item = contentItemService.createContentItem();
		}
		contentItemService.updateTitle(item, entry.getTitle());
		
		if(feed.getLanguage() != null)
			item.setLanguage(new Locale(feed.getLanguage()));
		
		if(entry.getPublishedDate() != null) {
			item.setCreated(entry.getPublishedDate());
			item.setModified(entry.getPublishedDate());
		}
		
		if(entry.getUpdatedDate() != null) {
			if(entry.getPublishedDate() == null) {
				item.setCreated(entry.getUpdatedDate());
				
			}
			item.setModified(entry.getUpdatedDate());
		}
		
		item.setAuthor(user);
		
		// read feed content and set it as item's text content
		List<SyndContent> contents = entry.getContents();
		if(contents.size() == 1) {
			log.debug("using RSS content section provided by item");
			contentItemService.updateTextContentItem(item, "<p>"+contents.get(0).getValue()+"</p>");
		} else if(contents.size() > 1) {
			log.warn("feed entry contained more than one content section");
			contentItemService.updateTextContentItem(item, "<p>"+contents.get(0).getValue()+"</p>");				
		} else if(contents.size() == 0) {
			if(entry.getDescription() != null && entry.getDescription().getValue() != null) {
				log.debug("using RSS description as no content section was available");
				contentItemService.updateTextContentItem(item, "<p>"+entry.getDescription().getValue()+"</p>");
			}
		}
		
		// save before tagging
		contentItemService.saveContentItem(item);
		
		// read feed categories and use them as tags
		for(SyndCategory cat : (List<SyndCategory>)entry.getCategories()) {
			ContentItem _cat;
			if(!taggingService.hasTag(item, cat.getName())) {
				if(cat.getTaxonomyUri() != null) {
					_cat = contentItemService.getContentItemByUri(cat.getTaxonomyUri());
					if(_cat == null) {
						_cat = contentItemService.createExternContentItem(cat.getTaxonomyUri());
						contentItemService.updateTitle(_cat, cat.getName());
						_cat.setAuthor(user);
						contentItemService.saveContentItem(_cat);
					}
					taggingService.createTagging(cat.getName(), item, _cat, user);
				} else {
					_cat = contentItemService.getContentItemByTitle(cat.getName());
					if(_cat == null) {
						_cat = contentItemService.createContentItem();
						contentItemService.updateTitle(_cat, cat.getName());
						_cat.setAuthor(user);
						contentItemService.saveContentItem(_cat);
					}
					taggingService.createTagging(cat.getName(), item, _cat, user);
				}
			}
		}
		// scan for Twitter-style hash tags in title (e.g. #kiwiknows, see KIWI-622)
		Matcher m_hashtag = p_hashtag.matcher(entry.getTitle()); 
		while(m_hashtag.find()) {
			String tag_label = m_hashtag.group(1);
			if(!taggingService.hasTag(item, tag_label)) {
				ContentItem tag = contentItemService.getContentItemByTitle(tag_label);
				if(tag == null) {
					tag = contentItemService.createContentItem();
					contentItemService.updateTitle(tag, tag_label);
					tag.setAuthor(user);
					contentItemService.saveContentItem(tag);
				}
				taggingService.createTagging(tag_label, item, tag, user);
			}
		}
		
		// check for geo information
		 GeoRSSModule geoRSSModule = GeoRSSUtils.getGeoRSS(entry);
		 if(geoRSSModule != null && geoRSSModule.getPosition() != null) {
			 POI poi = kiwiEntityManager.createFacade(item, POI.class);
			 poi.setLatitude(geoRSSModule.getPosition().getLatitude());
			 poi.setLongitude(geoRSSModule.getPosition().getLongitude());
			 kiwiEntityManager.persist(poi);
		 }
		
		 // check for media information
		 MediaEntryModule mediaModule = (MediaEntryModule) entry.getModule( MediaModule.URI );
		 if(mediaModule != null) {
			 MediaContent[] media = mediaModule.getMediaContents();
			 if(media.length > 0) {
				 MediaContent m = media[0];
				 if(m.getReference() instanceof UrlReference) {
					 URL url = ((UrlReference)m.getReference()).getUrl();
					 
					 String type = m.getType();
					 String name = url.getFile();
					 if(name.lastIndexOf("/") > 0) {
						 name = name.substring(name.lastIndexOf("/")+1);
					 }
					 
					 log.debug("importing media data from URL #0", url.toString());
					 
					 try {
						 InputStream is = url.openStream();
						 
						 ByteArrayOutputStream bout = new ByteArrayOutputStream();
						 
						 int c;
						 while((c = is.read()) != -1) {
							 bout.write(c);
						 }
						 
						 byte[] data = bout.toByteArray();
						 
						 contentItemService.updateMediaContentItem(item, data, type, name);
						 
						 
						 is.close();
						 bout.close();
					 } catch(IOException ex) {
						 log.error("error importing media content from RSS stream");
					 }
				 } else {
					 log.info("RSS importer can only import media with URL references");
				 }
			 } else {
				 log.warn("media module found without content");
			 }
			 
			 Category[] cats = mediaModule.getMetadata().getCategories();
			 for(Category cat : cats) {
				ContentItem _cat;
				
				String label = cat.getLabel() != null? cat.getLabel() : cat.getValue();
				
				if(!taggingService.hasTag(item, label)) {
					if(cat.getScheme() != null) {
						_cat = contentItemService.getContentItemByUri(cat.getScheme() + cat.getValue());
						if(_cat == null) {
							_cat = contentItemService.createExternContentItem(cat.getScheme() + cat.getValue());
							contentItemService.updateTitle(_cat, label);
							_cat.setAuthor(user);
							contentItemService.saveContentItem(_cat);
						}
						taggingService.createTagging(label, item, _cat, user);
					} else {
						_cat = contentItemService.getContentItemByTitle(label);
						if(_cat == null) {
							_cat = contentItemService.createContentItem();
							contentItemService.updateTitle(_cat, label);
							_cat.setAuthor(user);
							contentItemService.saveContentItem(_cat);
						}
						taggingService.createTagging(label, item, _cat, user);
					}
				}				 
			 }
		 }
		 
		
		// add parameter categories as tags
		for(ContentItem tag : tags) {
			if(!taggingService.hasTag(item, tag.getTitle())) {
				taggingService.createTagging(tag.getTitle(), item, tag, user);
			}
		}

		
		// add parameter types as types
		for(KiWiUriResource type : types) {
			item.addType(type);
		}
		
		// add kiwi:FeedPost type
		item.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"FeedPost"));

		/* the flush is necessary, because CIs or tags will 
		 * otherwise be created multiple times when they 
		 * appear more than once in one RSS feed */ 
		entityManager.flush();
		log.debug("imported content item '#0' with URI '#1'", item.getTitle(), item.getResource());
	}
}
