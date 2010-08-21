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
package kiwi.action.user;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.group.GroupService;
import kiwi.api.importexport.importer.RDFImporter;
import kiwi.api.multimedia.MultimediaService;
import kiwi.api.query.sparql.SparqlService;
import kiwi.api.triplestore.TripleStore;
import kiwi.api.user.FoafSslLoginServiceLocal;
import kiwi.api.user.FoafSslLoginServiceRemote;
import kiwi.api.user.KiWiProfileFacade;
import kiwi.api.user.PasswordGeneratorService;
import kiwi.api.user.ProfileService;
import kiwi.api.user.UserService;
import kiwi.context.CurrentUserFactory;
import kiwi.exception.FoafProfileAssociationException;
import kiwi.exception.GroupExistsException;
import kiwi.exception.UserExistsException;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.kbase.KiWiNode;
import kiwi.model.kbase.KiWiQueryLanguage;
import kiwi.model.kbase.KiWiUriResource;
import kiwi.model.user.Group;
import kiwi.model.user.User;
import kiwi.service.user.IdentityManagerService;
import kiwi.util.KiWiCollections;
import kiwi.util.MD5;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.MethodExpression;
import org.jboss.seam.log.Log;
import org.openrdf.model.Resource;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.memory.MemoryStore;

/**
 * 
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.user.foafSslLoginService")
@Scope(ScopeType.STATELESS)
@AutoCreate
@Stateless
//@Transactional
public class FoafSslLoginServiceImpl implements FoafSslLoginServiceRemote, FoafSslLoginServiceLocal {

	@Logger
	private Log log;
	
	@In
	private UserService userService;
	
	@In
	private GroupService groupService;
	
	@In("kiwi.query.sparqlService")
	private SparqlService sparqlService;
	
	@In
	private ProfileService profileService;

	@In
	private ContentItemService contentItemService;
	
	@In
	private KiWiEntityManager kiwiEntityManager;
	
	@In
	private PasswordGeneratorService passwordGenerator;
	
	
	@In
	private CurrentUserFactory currentUserFactory;
	
	/**
	 * Create a KiWiProfileFacade with the information retrieved from Foaf File
	 * @param u
	 */
//	private void updateUserProfile(User u) {
//		KiWiProfileFacade profile = profileService.getProfile(u);
//		
//		profile.setWebId(u.getWebId());
//		
//		// update location
//		// update gender
//		// update 
//	}
	
	/**
	 * Imports data about a user from his/her foaf file and maps information to 
	 * @param webid
	 * @return
	 * @throws FoafProfileAssociationException
	 * @throws QueryEvaluationException
	 */
	public String createUserInfoOutOfFoafFile(URL webid) throws FoafProfileAssociationException, QueryEvaluationException {
		
		User user = null;
		/* create a new temporary repository */
		Repository myRepository = new SailRepository(new MemoryStore());
		RepositoryConnection myCon = null;
		try {
			myRepository.initialize();
		
			myCon = myRepository.getConnection();
		
			/* import the data from the foaf file */
			String base = importFoafFileInformation(myCon, webid);
			
			/* retrieve the user by his/her webid/foaf-file */
			user = userService.getUserByWebId(base);
				
			/* queries that retrieve the information about the nick and name of the user
			 * thanks to foaf (-_-) we have to query for both versions for first and last name
			 * (= givenname, firstname, surname, family name)
			 * TODO: think about querying foaf:name, too */
			
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
			TupleQuery pubkey_modQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { ?cert <"+Constants.NS_CERT+"identity> <"+base+"> . " +
							"?cert <"+Constants.NS_RSA+"modulus> ?mod . " +
							"?mod <"+Constants.NS_CERT+"hex> ?l . }");
			TupleQuery pubkey_expQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { ?cert <"+Constants.NS_CERT+"identity> <"+base+"> . " +
							"?cert <"+Constants.NS_RSA+"public_exponent> ?exp . " +
							"?exp <"+Constants.NS_CERT+"decimal> ?l . }");
			TupleQuery birthdayQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
					"SELECT ?l " +
					"WHERE { <"+base+"> <"+Constants.NS_BIO+"event> ?bioevent . " +
							"?bioevent <"+Constants.NS_BIO+"date> ?l . }");
//				TupleQuery favArtistsQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
//						"SELECT ?r " +
//						"WHERE { <"+base+"> <"+Constants.NS_LASTFM+"favouriteArtist> ?r . }");
			
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
			TupleQueryResult pubkey_modQR = pubkey_modQ.evaluate();
			TupleQueryResult pubkey_expQR = pubkey_expQ.evaluate();
//				TupleQueryResult favArtistsQR = favArtistsQ.evaluate();
			
			String surname = null;
			String firstName = null;
			String depiction = null;
			String gender = null;
			String skype = null;
			String facebook = null;
			String mbox = null;
			String mobile = null;
			String birthday = null;
			String pubkey_mod = null;
			String pubkey_exp = null;
//			HashSet<ContentItem> favArtists = new HashSet<ContentItem>();
			try {
				
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
				
				/* if the user does not yet exist in the KiWi 
				 * System or if he/she is not yet linked with the 
				 * webid, we query the information  */
				if(user == null) {
					TupleQuery nickQ = myCon.prepareTupleQuery(QueryLanguage.SPARQL, 
							"SELECT ?l " +
							"WHERE { <"+base+"> <"+Constants.NS_FOAF+"nick> ?l . }");
					
					TupleQueryResult nickQR = nickQ.evaluate();
					
					String nick = null;
					if(!nickQR.hasNext()) {
						log.error("No nick provided in the foaf file < #0 >",base);
						/* TODO: if no nick is provided in the foaf file, 
						 * we have to raise an event that asks the user to chose a 
						 * nick and maybe to fill out some other personal information.
						 * The further personal information can also be displayed 
						 * with filled up form fields, if this information is 
						 * available in the foaf file */
					} else {
						final List<String> bindingNames = nickQR.getBindingNames();
			            final String binding = bindingNames.get(0);
						BindingSet bs = nickQR.next();
						nick = bs.getValue(binding).stringValue();
						
						/* create a new user */
						try {
							final String password = passwordGenerator.generatePassword();
							
							// TODO: replace that with actual password
							user = userService.createUser(nick,firstName, surname, password);
							
							user.setWebId(base);
							
							Group users = null;
							if((users = groupService.getGroupByName("users")) == null) {
								try {
									users = groupService.createGroup("users");
								} catch (GroupExistsException e) {
									e.printStackTrace();
								}
							} 
							groupService.addUserToGroup(users, user);
							groupService.store(users);
							
						} catch (UserExistsException e1) {
							e1.printStackTrace();
							/* a user with the same nick already existed 
							 * TODO: how to proceed? How do we find out 
							 * that the user is or is not one and the same 
							 * person as in the foaf file? 
							 * Maybe we should ask for the user-password if this happens */
							user = userService.getUserByLogin(nick);
						}
					}
				}
				
				/* login and currentUser refresh */
				IdentityManagerService identityManagerService = 
					(IdentityManagerService) Component.getInstance("identityManagerService");
					
				MethodExpression customAuthentication = 
					Expressions.instance().createMethodExpression("#{identityManagerService.authenticate()}");
				
				try {
					identityManagerService.login(user.getLogin(), "", customAuthentication);
				} catch (LoginException e) {
					e.printStackTrace();
				}
				currentUserFactory.forceRefresh();
				
				
				KiWiProfileFacade profile = profileService.getProfile(user);
				
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
				if(pubkey_expQR.hasNext()) {
					final List<String> bindingNames = pubkey_expQR.getBindingNames();
					final String binding = bindingNames.get(0);
					BindingSet bs = pubkey_expQR.next();
					pubkey_exp = bs.getValue(binding).stringValue();
					
					profile.setPublic_exponent(pubkey_exp);
				}
				
				/** querying public key exponent and updating profile **/
				if(pubkey_modQR.hasNext()) {
					final List<String> bindingNames = pubkey_modQR.getBindingNames();
					final String binding = bindingNames.get(0);
					BindingSet bs = pubkey_modQR.next();
					pubkey_mod = bs.getValue(binding).stringValue();
					
					profile.setModulus(pubkey_mod);
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
					
					URL depictionURL = new URL(depiction);
					InputStream is;
					try {
						is = depictionURL.openStream();
	
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
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			
				/* store the user in the database and the triplestore */
				kiwiEntityManager.persist(user);
				kiwiEntityManager.persist(profile);
//				kiwiEntityManager.flush();
			
			} finally {
				TripleStore tripleStore = (TripleStore) Component.getInstance("tripleStore");
				tripleStore.createTriple(user.getResource(), 
						tripleStore.createUriResource(Constants.NS_KIWI_CORE + "foafprofile"), 
						tripleStore.createUriResource(base));
			}
			
			// load data from the foaf file into kiwi
			RDFImporter rdfImporter = (RDFImporter) 
				Component.getInstance("kiwi.service.importer.rdf");
			
//			try {
//				passwordGenerator.sendPasswordToEmailAddress(user);
//			} catch (NoEmailAddressGivenException e) {
//				e.printStackTrace();
//				facesMessages.add("Please specify your email address, " +
//						"so that we can send you your password");
//				// We have to catch an event and send the password
//			}
			
			
			/* now we import the RDF data from out temporary 
			 * repository into the KiWi Triplestore */
			rdfImporter.importDataSesame(myCon, null, null, user, null);
			
		} catch (RepositoryException e) {
			e.printStackTrace();
			log.error("RepositoryException #0 ", e.getMessage());
		} catch (MalformedQueryException e) {
			e.printStackTrace();
			log.error("MalformedQueryException #0 ", e.getMessage());
		} catch (RDFParseException e) {
			e.printStackTrace();
			log.error("RDFParseException #0 ", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("IOException #0 ", e.getMessage());
		}
		return "success";
	}

	public boolean authenticate() {
		return true;
	}
	
	/**
     * Imports the information about a person from his/her personal foaf file
     * @param myCon
     * @param url
     * @return
     * @throws RDFParseException
     * @throws RepositoryException
     * @throws IOException
     * @throws MalformedQueryException
     */
	private String importFoafFileInformation(
			RepositoryConnection myCon, URL url) throws RDFParseException, RepositoryException, IOException, MalformedQueryException {
		
		String[] url_split = url.toString().split("/");
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<url_split.length-1; i++) {
			sb.append(url_split[i]);
			sb.append("/");
		}
		
		/* we extract the base URI (the URI which belongs to the 
		 * information that is available in the document) in order to 
		 * import the information from the persons foaf file */
        String base = extractBaseResource(url);
        if(base != null && !base.startsWith(sb.toString())) {
        	URL baseURL = new URL(base);
        	myCon.add(baseURL.openStream(), base, RDFFormat.RDFXML);
        }
		myCon.add(url.openStream(), base, RDFFormat.RDFXML);
        
		return base;
	}
	
	/**
     * Extracts the base resource by querying the top resource of 
     * the graph on the one hand, and by querying for the certificate 
     * identity on the other hand.
     * This is necessary to retrieve information about a user, 
     * who sings in with his/her certificate file. 
     * The reason why it is not sufficient to just import data from the 
     * certificate file, which is linked in the certificate itself is, 
     * that sometimes the personal foaf file is not directly linked in the certificate, 
     * but linked via another RDF file (a certificate RDF file). 
     * This method therefore helps to extract the URL of the personal foaf file.
     * @param url - the url that is linked in the certificate
     */
    private String extractBaseResource(URL url) {
		
    	/* s will be returned */
		String s = null;
		try {
			/* temporary repository that contains the data 
			 * from the RDF file which is linked in the certificate */
			Repository myRepository = new SailRepository(new MemoryStore());
			myRepository.initialize();
			RepositoryConnection myCon = myRepository.getConnection();
			// TODO: extend for non-XML RDF!!
			myCon.add(url.openStream(), url.toString(), RDFFormat.RDFXML, (Resource) null);
			
			/* Retrieve the base resource of the rdf file 
			 * the base resource is the resource that is 
			 * placed on the top of the RDF graph, 
			 * thus it may not be the object of anything 
			 * (expressed as !BOUND(anyProp) and 
			 * !BOUND(anySub) in the query ).
			 * And of course it must be a IRI */
			TupleQuery tq1 = myCon.prepareTupleQuery(QueryLanguage.SPARQL,
				"SELECT DISTINCT ?s " +
				"WHERE {" +
					" ?s ?p ?o ." +
					" OPTIONAL { ?anySub ?anyProp ?s } ." +
					" FILTER (!BOUND(?anyProp) " +
					" && !BOUND(?anySub) " +
					" && isIRI(?s)) . " +
				"}");
			
			TupleQueryResult res1 = tq1.evaluate();
			final List<String> bindingNames1 = res1.getBindingNames();
            final String varSubj1 = bindingNames1.get(0);

            BindingSet bindingSet1 = null;
            while(res1.hasNext()) {
                bindingSet1 = res1.next();
                while(res1.hasNext()) {
                	BindingSet bindingSet2 = res1.next();
                	String v1 = null;
                	String v2 = null;
                	/* If there exist more than one URIs that are not an object of another node, the URI, that  */
                	if(bindingSet1.getValue(varSubj1) != null && 
                		(v1 = bindingSet1.getValue(varSubj1).stringValue()) != null &&
                		bindingSet2.getValue(varSubj1) != null &&
                		(v2 = bindingSet2.getValue(varSubj1).stringValue()) != null) { 
                		if(v1.startsWith(v2)) {
                			bindingSet1 = bindingSet2;
                		} else if(!v2.startsWith(v1)) {
                			//fail
                        	return null;
                		}
                	}
                }
                if(bindingSet1.getValue(varSubj1) != null) {
                	/* s is now the top resource of the RDF graph */
                	s = bindingSet1.getValue(varSubj1).stringValue();
                	
                	/* In foaf files, which are generated with foaf.me, 
                	 * the top resource of the RDF file, which is linked 
                	 * in the certificate, has another fragment (#cert) 
                	 * than the actual base resource with the fragment (#me), 
                	 * which is the resource that links to the personal 
                	 * information (e.g. nick, name, friends).
                	 * Both resources (with the fragments #cert and #me)
                	 * are linked via the property cert:identity. 
                	 * Therefore, we query the object of the triple
                	 * <top_resource with fragment #cert> <cert:identity> ?o */
                	TupleQuery tq2 = myCon.prepareTupleQuery(QueryLanguage.SPARQL,
            			"SELECT DISTINCT ?o " +
        				"WHERE {" +
        					" <"+s+"> <"+Constants.NS_CERT+"identity> ?o ." +
        				"}");
                	TupleQueryResult res2 = tq2.evaluate();
        			final List<String> bindingNames2 = res2.getBindingNames();
                    final String varObj2 = bindingNames2.get(0);
                    
                    BindingSet bindingSet2 = null;
                    if(res2.hasNext()) {
                    	bindingSet2 = res2.next();
                    	if(res2.hasNext()) {
                        	//fail
                        	return null;
                        }
                    	if(bindingSet2.getValue(varObj2) != null) {
                    		/* if we got a result, we replace s with the identity (#me) resource */
                        	s = bindingSet2.getValue(varObj2).stringValue();
                    	}
                    }
                    // closes the query result res2
                    res2.close();
                }
            }
            // closes the query result res1
            res1.close();
            
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (RDFParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!s.endsWith("#me") && !s.contains("#")) {
			s = s + "#me";
		}
		return s;
	}
    
    /**
	 * Returns a user via the openId
	 * @param openId
	 * @return
	 * @throws FoafProfileAssociationException 
	 */
	private User queryUserByFoafProfile(String foafprofileURI) throws FoafProfileAssociationException {
		Set<KiWiNode> nodes = KiWiCollections.toSet(sparqlService.queryNode("SELECT ?U WHERE { ?U <"+Constants.NS_KIWI_CORE+"foafprofile> <"+ foafprofileURI +"> }", 
				KiWiQueryLanguage.SPARQL));
		KiWiUriResource userResource = null;
		if(nodes.size() == 1) {
			userResource = (KiWiUriResource) nodes.iterator().next();
		} else if(nodes.size() > 1) {
			throw new FoafProfileAssociationException("More than one user associated with the same foafprofile");
		}
		User u = null;
		if(userResource != null) {
			u = (User) userService.getUserByUri(userResource.getUri());
		}
		return u;
	}
}
