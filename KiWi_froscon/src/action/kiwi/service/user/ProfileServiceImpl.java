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
package kiwi.service.user;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.geo.GeoCodingFailedException;
import kiwi.api.geo.GeoCodingService;
import kiwi.api.geo.Location;
import kiwi.api.importexport.importer.RDFImporter;
import kiwi.api.multimedia.MultimediaService;
import kiwi.api.search.KiWiSearchCriteria;
import kiwi.api.search.KiWiSearchResults;
import kiwi.api.search.SolrService;
import kiwi.api.search.KiWiSearchResults.SearchResult;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.CityFacade;
import kiwi.api.user.CountryFacade;
import kiwi.api.user.KiWiProfileFacade;
import kiwi.api.user.ProfileServiceLocal;
import kiwi.api.user.ProfileServiceRemote;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.User;
import kiwi.util.MD5;

import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;


/**
 * @author Sebastian Schaffert
 *
 */
@Name("profileService")
@AutoCreate
@Stateless
@Scope(ScopeType.STATELESS)
public class ProfileServiceImpl implements ProfileServiceLocal, ProfileServiceRemote {

	@Logger
	private Log log;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private TripleStore tripleStore;
	
	@In
	private SolrService solrService;
	
	@In
	private ContentItemService contentItemService;
	
	@In("kiwi.core.geoCodingService")
	private GeoCodingService geoCodingService;
	
	/**
	 * 
	 * @see kiwi.api.user.ProfileService#getCity(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public CityFacade getCity(String postalCode, String name, String country) {
		// search for city by name and ZIP, if it does not exist, create a new city...
		KiWiSearchCriteria crit = new KiWiSearchCriteria();
		if(postalCode != null && !postalCode.equals("")) {
			crit.getRdfLiteralProperties().put(Constants.NS_KIWI_CORE + "cityPostalCode", Collections.singleton(postalCode));
		}
		crit.getRdfLiteralProperties().put(Constants.NS_KIWI_CORE + "cityName", Collections.singleton(name));
		
		// TODO: need to add resources to search index for this to function
		// crit.getRdfLiteralProperties().put(Constants.NS_KIWI_CORE + "cityCountry", cityCountry);
		
		KiWiSearchResults cities = solrService.search(crit);
		
		
		if(cities.getResultCount() > 0) {
			// existing city; update profile
			
			CityFacade city = kiwiEntityManager.createFacade(cities.getResults().get(0).getItem(),CityFacade.class);
			
			// update geo-coordinates if they are not set; note that double comparison with == is unsafe...
			if(city.getLongitude() == 0.0 && city.getLatitude() == 0.0) {
				String google_geo_search = 
					((postalCode != null && !postalCode.equals("")) ? postalCode + " " : "") + 
					name + 
					((country != null && !country.equals("")) ? ", " + country : "");

				try {
					//Location loc = getLocation(google_geo_search);
					Location loc = geoCodingService.getLocationGoogle(google_geo_search);
					
					city.setLatitude(loc.getLatitude());
					city.setLongitude(loc.getLongitude());
				} catch(GeoCodingFailedException ex) {
					log.warn("could not retrieve geolocation for city #0; an error occurred.",name,ex);
				}
				
			}
			
			return city;
			
		} else {
			// non-existing city; create new resource and set properties
			CityFacade city = 
				kiwiEntityManager.createFacade(
						contentItemService.createContentItem(),
						CityFacade.class);
			
			if(postalCode != null && !postalCode.equals("")) {
				city.setPostalCode(postalCode);
			}
			city.setName(name);
			contentItemService.updateTitle(city, name);
			
			if(country != null && !country.equals("")) {
				city.setCountry(getCountry(country));
			}
			
			
			String google_geo_search = 
				((postalCode != null && !postalCode.equals("")) ? postalCode + " " : "") + 
				name + 
				((country != null && !country.equals("")) ? ", " + country : "");

			try {
				//Location loc = getLocation(google_geo_search);
				Location loc = geoCodingService.getLocationGoogle(google_geo_search);
				
				city.setLatitude(loc.getLatitude());
				city.setLongitude(loc.getLongitude());
			} catch(GeoCodingFailedException ex) {
				log.warn("could not retrieve geolocation for city #0; an error occurred.",name,ex);
			}
			
			kiwiEntityManager.persist(city);
			
			return city;
		}
	}

	/**
	 * 
	 * @see kiwi.api.user.ProfileService#getCountryISO(java.lang.String)
	 */
	@Override
//	@Transactional
	public CountryFacade getCountry(String identifier) {
		
		Locale searchLocale = null;
		for(Locale l : Locale.getAvailableLocales()) {
			if(l.getCountry().equalsIgnoreCase(identifier)) {
				searchLocale = l;
				break;
			} else if(l.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(identifier)) {
				searchLocale = l;
				break;
			}
		}
		String searchISO     = searchLocale != null? searchLocale.getCountry() : identifier;
		String searchCountry = searchLocale != null? searchLocale.getDisplayCountry(Locale.ENGLISH) : identifier;

		log.info("searching for country with code #0", searchISO);
		
		KiWiSearchCriteria crit2 = new KiWiSearchCriteria();
		crit2.getRdfLiteralProperties().put(Constants.NS_KIWI_CORE + "countryISOCode", Collections.singleton(searchISO));
		KiWiSearchResults countries = solrService.search(crit2);

		if(countries.getResultCount() > 0) {
			// search for country by ISO code
			
			return kiwiEntityManager.createFacade(countries.getResults().get(0).getItem(), CountryFacade.class);
		} else {
			// search for country by name
			
			KiWiSearchCriteria crit3 = new KiWiSearchCriteria();
			crit3.getRdfLiteralProperties().put(Constants.NS_KIWI_CORE + "countryName", Collections.singleton(searchCountry));
			countries = solrService.search(crit3);
			
			if(countries.getResultCount() > 0) {
				return kiwiEntityManager.createFacade(countries.getResults().get(0).getItem(), CountryFacade.class);
			
			} else {
				// create country using the Java Locale system
				
				Locale countryLocale = searchLocale;
				if(countryLocale != null) {
					CountryFacade country = 
						kiwiEntityManager.createFacade(
								contentItemService.createContentItem(),
								CountryFacade.class);
					
					country.setISOCode(countryLocale.getCountry());
					country.setName(countryLocale.getDisplayCountry(Locale.ENGLISH));
					contentItemService.updateTitle(country, countryLocale.getDisplayCountry(Locale.ENGLISH));
					
					kiwiEntityManager.persist(country);
					
					return country;
					
				}
			}
		}					
		return null;
	}

	/**
	 * 
	 * @see kiwi.api.user.ProfileService#getInterest(java.lang.String)
	 */
	@Override
	public ContentItem getInterest(String name) {
		// TODO: query by uri instead of by title
		ContentItem interestItem = contentItemService.getContentItemByTitle(name);

		if(interestItem == null) {
			// create new Content Item of type "tag" if the tag does not yet exist
			interestItem = contentItemService.createContentItem();
			interestItem.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Tag"));
			interestItem.addType(tripleStore.createUriResource(Constants.NS_KIWI_CORE+"Interest"));
			contentItemService.updateTitle(interestItem, name);
			
			kiwiEntityManager.persist(interestItem);
			log.info("created new content item for non-existant interest");
		}

		return interestItem;
	}

	/**
	 * 
	 * @see kiwi.api.user.ProfileService#getInterests(java.lang.String)
	 */
	@Override
	public HashSet<ContentItem> getInterests(String interests) {
		// parse and set interests
		String[] components = interests.split(",");
		HashSet<ContentItem> interestsCis = new HashSet<ContentItem>();
		
		for(String component : components) {
		
			if(!component.trim().equals("")) {
				log.info("adding interest #0",component);
				
				String interest = component.trim();
				
				interestsCis.add(getInterest(interest));
			}
		}
		return interestsCis;
	}

	/**
	 * 
	 * @see kiwi.api.user.ProfileService#getProfile(kiwi.model.user.User)
	 */
	@Override
	public KiWiProfileFacade getProfile(User user) {
		return kiwiEntityManager.createFacade(user.getContentItem(), KiWiProfileFacade.class);
	}

	

	/**
	 * Return all imported blog posts of the user passed as argument. If "limit" is bigger than 0,
	 * at most "limit" results are returned. The result is created using the search functionality
	 * of the KiWi system. 
	 * <p>
	 * The returned content items all have the rdf type kiwi:BlogPost or one of its 
	 * subclasses.
	 * 
	 * @param user   a KiWiProfileFacade identifying the user whose imported blog posts to retrieve
	 * @param limit  if greater than 0, the maximum number of results to return
	 * @return       a list of content items ordered by modification date, first item is newest.
	 */
	@Override
	public List<ContentItem> getBlogPosts(KiWiProfileFacade user, int limit) {
		KiWiSearchCriteria crit = new KiWiSearchCriteria();
		
		crit.setPerson(user.getNickname());
		crit.getTypes().add("uri::"+Constants.NS_KIWI_CORE+"BlogPost");
		crit.setLimit(limit);
		crit.setOffset(0);
		crit.setSortField("modified", ORDER.desc);
		
		
		KiWiSearchResults qResult = solrService.search(crit);
		
		LinkedList<ContentItem> result = new LinkedList<ContentItem>();
		for(SearchResult r : qResult.getResults()) {
			result.add(r.getItem());
		}
		return result;
	}

	/**
	 * Return all microblogging posts of the user passed as argument. If "limit" is bigger than 0,
	 * at most "limit" results are returned. The result is created using the search functionality
	 * of the KiWi system. 
	 * <p>
	 * The returned content items all have the rdf type kiwi:MicrobloggingPost or one of its 
	 * subclasses (kiwi:TwitterPost, kiwi:FacebookPost, ...).
	 * 
	 * @param user   a KiWiProfileFacade identifying the user whose microblogging posts to retrieve
	 * @param limit  if greater than 0, the maximum number of results to return
	 * @return       a list of content items ordered by modification date, first item is newest.
	 */
	@Override
	public List<ContentItem> getMicrobloggingPosts(KiWiProfileFacade user, int limit) {
		KiWiSearchCriteria crit = new KiWiSearchCriteria();
		
		crit.setPerson(user.getNickname());
		crit.getTypes().add("uri::"+Constants.NS_KIWI_CORE+"MicrobloggingPost");
		crit.setLimit(limit);
		crit.setOffset(0);
		crit.setSortField("modified", ORDER.desc);
		
		
		KiWiSearchResults qResult = solrService.search(crit);
		
		LinkedList<ContentItem> result = new LinkedList<ContentItem>();
		for(SearchResult r : qResult.getResults()) {
			result.add(r.getItem());
		}
		return result;
	}

	/**
	 * Return all images, that were added by the user.The result is created using the search functionality
	 * of the KiWi system. 
	 * <p>
	 * The returned content items all have the rdf type kiwi:image
	 * @param user a KiWiProfileFacade identifying the user whose imported images to retrieve
	 * @return
	 */
	@Override
	public List<ContentItem> getImages(KiWiProfileFacade user) {
		KiWiSearchCriteria crit = new KiWiSearchCriteria();
		
		crit.setPerson(user.getNickname());
		crit.getTypes().add("uri::"+Constants.NS_KIWI_CORE+"Image");
		crit.setOffset(0);
		
		
		KiWiSearchResults qResult = solrService.search(crit);
		
		LinkedList<ContentItem> result = new LinkedList<ContentItem>();
		for(SearchResult r : qResult.getResults()) {
			result.add(r.getItem());
		}
		return result;
	}   
	
	@Override
	public boolean importProfileInformation(RepositoryConnection myCon, String base, User user) throws RepositoryException {
		try {
			TupleQuery givennameQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_FOAF+"givenname> ?l . }");
			TupleQuery givenNameQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_FOAF+"givenName> ?l . }");
			TupleQuery firstnameQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_FOAF+"firstName> ?l . }");
			TupleQuery familynameQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_FOAF+"family_name> ?l . }");
			TupleQuery surnameQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_FOAF+"surname> ?l . }");
			TupleQuery depictionQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_FOAF+"depiction> ?l . }");
			TupleQuery genderQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_FOAF+"gender> ?l . }");
			TupleQuery skypeQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_KIWI_CORE+"skypeAccount> ?l . }");
			TupleQuery mboxQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_FOAF+"mbox> ?l . }");
			TupleQuery facebookQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_KIWI_CORE+"facebookAccount> ?l . }");
			TupleQuery mobileQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_KIWI_CORE+"mobile> ?l . }");
			TupleQuery birthdayQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_BIO+"event> ?bioevent . " +
							"?bioevent <"+Constants.NS_BIO+"date> ?l . }");
//			TupleQuery favArtistsQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
//					"SELECT ?r " +
//					"WHERE { <"+base+"> <"+Constants.NS_LASTFM+"favouriteArtist> ?r . }");
			TupleQuery nickQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_FOAF+"nick> ?l . }");
			
			TupleQueryResult nickQR = nickQ.evaluate();
			TupleQueryResult givenNameQR = givenNameQ.evaluate();
			TupleQueryResult givennameQR = givennameQ.evaluate();
			TupleQueryResult firstnameQR = firstnameQ.evaluate();
			TupleQueryResult familynameQR = familynameQ.evaluate();
			TupleQueryResult surnameQR = surnameQ.evaluate();
			TupleQueryResult depicitionQR = depictionQ.evaluate();
			TupleQueryResult genderQR = genderQ.evaluate();
			TupleQueryResult skypeQR = skypeQ.evaluate();
			TupleQueryResult facebookQR = facebookQ.evaluate();
			TupleQueryResult mboxQR = mboxQ.evaluate();
			TupleQueryResult mobileQR = mobileQ.evaluate();
			TupleQueryResult birthdayQR = birthdayQ.evaluate();
//			TupleQueryResult favArtistsQR = favArtistsQ.evaluate();
			
			String firstName = null;
			String surname = null;
			String nick = null;
			
			String depiction = null;
			String gender = null;
			String skype = null;
			String facebook = null;
			String mbox = null;
			String mobile = null;
			String birthday = null;
				
			KiWiProfileFacade profile = getProfile(user);
				
			if(!firstnameQR.hasNext()) {
				if(!givennameQR.hasNext()) {
					log.warn("No first name provided in the foaf file < #0 >",base);
				} else {
					final List<String> bindingNames = givennameQR.getBindingNames();
		            final String binding = bindingNames.get(0);
					BindingSet bs = givennameQR.next();
					firstName = bs.getValue(binding).stringValue();
					
				}
			} else {
				final List<String> bindingNames = firstnameQR.getBindingNames();
	            final String binding = bindingNames.get(0);
				BindingSet bs = firstnameQR.next();
				firstName = bs.getValue(binding).stringValue();
				
			}
			if(!familynameQR.hasNext()) {
				if(!surnameQR.hasNext()) {
					if(!givenNameQR.hasNext()) {
						if(!givennameQR.hasNext()) {
							log.warn("No family name provided in the foaf file < #0 >",base);
						} else {
							final List<String> bindingNames = givennameQR.getBindingNames();
				            final String binding = bindingNames.get(0);
							BindingSet bs = givennameQR.next();
							surname = bs.getValue(binding).stringValue();
						}
					} else {
						final List<String> bindingNames = givenNameQR.getBindingNames();
			            final String binding = bindingNames.get(0);
						BindingSet bs = givenNameQR.next();
						surname = bs.getValue(binding).stringValue();
						
					}
				} else {
					final List<String> bindingNames = surnameQR.getBindingNames();
		            final String binding = bindingNames.get(0);
					BindingSet bs = surnameQR.next();
					surname = bs.getValue(binding).stringValue();
					
				}
			} else {
				final List<String> bindingNames = familynameQR.getBindingNames();
	            final String binding = bindingNames.get(0);
				BindingSet bs = familynameQR.next();
				surname = bs.getValue(binding).stringValue();
				
			}
			
			
			if(!nickQR.hasNext()) {
				log.warn("No nick provided in the foaf file < #0 >",base);
			} else {
				final List<String> bindingNames = nickQR.getBindingNames();
	            final String binding = bindingNames.get(0);
				BindingSet bs = nickQR.next();
				nick = bs.getValue(binding).stringValue();
			}
			
			user.setLogin(nick);
			user.setFirstName(firstName);
			user.setLastName(surname);
			profile.setFirstName(firstName);
			profile.setLastName(surname);
			profile.setTitle("Profile: " + firstName + " " + surname);
			
			
				/** querying gender and updating profile **/
				if(genderQR.hasNext()) {
					final List<String> bindingNames = genderQR.getBindingNames();
					final String binding = bindingNames.get(0);
					BindingSet bs = genderQR.next();
					gender = bs.getValue(binding).stringValue();
					
					profile.setGender(gender);
				}
				
				/** querying skype contact and updating profile **/
				if(skypeQR.hasNext()) {
					final List<String> bindingNames = skypeQR.getBindingNames();
					final String binding = bindingNames.get(0);
					BindingSet bs = skypeQR.next();
					skype = bs.getValue(binding).stringValue();
					
					profile.setSkype(skype);
				}
				
				/** querying facebook contact and updating profile **/
				if(facebookQR.hasNext()) {
					final List<String> bindingNames = facebookQR.getBindingNames();
					final String binding = bindingNames.get(0);
					BindingSet bs = facebookQR.next();
					facebook = bs.getValue(binding).stringValue();
					
					profile.setFacebookAccount(facebook);
				}
				
				/** querying email address and updating profile **/
				if(mboxQR.hasNext()) {
					final List<String> bindingNames = mboxQR.getBindingNames();
					final String binding = bindingNames.get(0);
					BindingSet bs = mboxQR.next();
					mbox = bs.getValue(binding).stringValue();
					
					profile.setEmail(mbox);
				}
				
				/** querying mobile phone and updating profile **/
				if(mobileQR.hasNext()) {
					final List<String> bindingNames = mobileQR.getBindingNames();
					final String binding = bindingNames.get(0);
					BindingSet bs = mobileQR.next();
					mobile = bs.getValue(binding).stringValue();
					
					profile.setMobile(mobile);
				}
				
				/** querying birthday and updating profile **/
				if(birthdayQR.hasNext()) {
					final List<String> bindingNames = birthdayQR.getBindingNames();
					final String binding = bindingNames.get(0);
					BindingSet bs = birthdayQR.next();
					birthday = bs.getValue(binding).stringValue();
					
					String[] birthdayArray = birthday.split("-");
					
					if(birthdayArray.length == 3) {
						Calendar cal = Calendar.getInstance();
						cal.set(new Integer(birthdayArray[0]), new Integer(birthdayArray[1]) -1, 
								new Integer(birthdayArray[2]));
						profile.setBirthday(cal.getTime());
					}
				}
				if(depicitionQR.hasNext()) {
					final List<String> bindingNames = depicitionQR.getBindingNames();
					final String binding = bindingNames.get(0);
					BindingSet bs = depicitionQR.next();
					depiction = bs.getValue(binding).stringValue();
					
					try {
						URL depictionURL = new URL(depiction);
						InputStream is = depictionURL.openStream();
		
						ByteArrayOutputStream bout = new ByteArrayOutputStream();
		
						int c;
						while((c = is.read()) != -1) {
							bout.write(c);
						}
		
						byte[] data = bout.toByteArray();
		
						MultimediaService ms = (MultimediaService) Component.getInstance("multimediaService");
						String mimeType = ms.getMimeType(depiction, data);
						
						ContentItem photo = contentItemService.createMediaContentItem(data, mimeType, MD5.md5sum(depiction));
						contentItemService.updateTitle(photo, 
								user.getFirstName()+" "+user.getLastName()+"'s Photo");
						photo.getResource().setLabel(Locale.getDefault(), user.getFirstName()+" "+user.getLastName()+"'s Photo");
						user.setProfilePhoto(photo);
						profile.setProfilePhoto(photo);
						kiwiEntityManager.persist(photo);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			
				/* store the user in the database and the triplestore */
				kiwiEntityManager.persist(user);
				kiwiEntityManager.persist(profile);
//				kiwiEntityManager.flush();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		} catch (RepositoryException e) {
			e.printStackTrace();
			return false;
		} catch (MalformedQueryException e) {
			e.printStackTrace();
			return false;
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
			return false;
		}
		
		// load data from the foaf file into kiwi
		RDFImporter rdfImporter = (RDFImporter) 
			Component.getInstance("kiwi.service.importer.rdf");
		
		/* now we import the RDF data from out temporary 
		 * repository into the KiWi Triplestore */
		rdfImporter.importDataSesame(myCon, null, null, user, null);
		return true;
	}

	@Override
	public String getProfilePhotoDownloadUrl(User u) {
		String res = "seam/resource/services/images/thumb?uri=";
		if(u.getProfilePhoto() == null){
			return "";
		}
		else {
			String uri = ((KiWiUriResource)u.getProfilePhoto().getResource()).getUri();
			return res + uri;
		}
	}
    
}
