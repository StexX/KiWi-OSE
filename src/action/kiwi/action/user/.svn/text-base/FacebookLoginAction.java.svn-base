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
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.security.auth.login.LoginException;

import kiwi.api.content.ContentItemService;
import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.event.KiWiEvents;
import kiwi.api.group.GroupService;
import kiwi.api.multimedia.MultimediaService;
import kiwi.api.user.CityFacade;
import kiwi.api.user.KiWiProfileFacade;
import kiwi.api.user.PasswordGeneratorService;
import kiwi.api.user.ProfileService;
import kiwi.api.user.UserService;
import kiwi.context.CurrentUserFactory;
import kiwi.exception.GroupExistsException;
import kiwi.exception.UserExistsException;
import kiwi.model.content.ContentItem;
import kiwi.model.user.Group;
import kiwi.model.user.User;
import kiwi.service.user.IdentityManagerService;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.core.Expressions.MethodExpression;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJsonRestClient;
import com.google.code.facebookapi.ProfileField;

/**
 * @author Sebastian Schaffert
 *
 */
@Name("kiwi.user.facebookLoginAction")
@AutoCreate
@Scope(ScopeType.CONVERSATION)
//@Transactional
public class FacebookLoginAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	private static Log log;

	@In
	protected UserService userService;
	
	@In
	private GroupService groupService;
	
	@In
	private CurrentUserFactory currentUserFactory;

	// the created user
	private User user;

	@In
	private KiWiEntityManager kiwiEntityManager;

	@In
	private ProfileService profileService;
	
	@In
	private ContentItemService contentItemService;

	@In(create=true)
	private PasswordGeneratorService passwordGenerator;
	
	@In 
	FacesMessages facesMessages;


	private String firstName;
	private String lastName;
	private String email;
	private String fbId;
	private String username;
	private String password;
	
	@Create
	@Begin(nested=true)
	public void begin() {
		
		FacebookJsonRestClient fbClient = (FacebookJsonRestClient)Contexts.getSessionContext().get("facebook.user.client");

		try {
			final long userId = fbClient.users_getLoggedInUser();

			List<ProfileField> fields=new ArrayList<ProfileField>();
			fields.add(ProfileField.FIRST_NAME);
			fields.add(ProfileField.LAST_NAME);
			fields.add(ProfileField.EMAIL_HASHES);
			fields.add(ProfileField.PROXIED_EMAIL);

			List<Long> userIds=new ArrayList<Long>();
			userIds.add(userId);
			JSONArray ja=(JSONArray)fbClient.users_getInfo(userIds,fields);
			JSONObject jo=null;

			if(ja!=null)
				jo=ja.getJSONObject(0);

			if(jo!=null){
				password = passwordGenerator.generatePassword();
				log.info("facebook response:"+jo.toString(2));
				firstName=jo.getString(ProfileField.FIRST_NAME.toString());
				lastName=jo.getString(ProfileField.LAST_NAME.toString());
				email=jo.getString(ProfileField.PROXIED_EMAIL.toString());
				fbId = Long.toHexString(userId);
				username="fb_"+fbId;

				
				log.info("identified facebook user #0 #1 (login #2)", firstName, lastName, username);
			}	
		} catch(JSONException ex) {
			log.error("error while retrieving facebook response",ex);
			facesMessages.add("the response received from Facebook is invalid; please try again later");
		} catch(FacebookException ex) {
			log.error("error while retrieving facebook response",ex);
			facesMessages.add("the response received from Facebook is invalid; please try again later");
		}	
	}

	@Observer(value=KiWiEvents.ACTIVITY_REGISTER_FB)
	public void register() {
		try {
			user = userService.createUser(username, firstName, lastName, password);
			if(email != null && !email.equals("null")) {
				user.setEmail(email);
			}
//			kiwiEntityManager.flush();
			user.setFbId(fbId);
//			kiwiEntityManager.flush();
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
			facesMessages.add("Created user (#0 #1)",firstName, lastName);
			log.info("successfully created user #0 #1 #2 #3", username, firstName, lastName, password);
			
			login();
			
		} catch (UserExistsException e) {
			return;
		}
	}

	@Observer(value=KiWiEvents.ACTIVITY_LOGIN_FB)
	@End
	public String login() {
		if(user == null) {
			user = userService.getUserByFbId(fbId);
		}
		
		IdentityManagerService identityManagerService = 
		(IdentityManagerService) Component.getInstance("identityManagerService");
		
		MethodExpression customAuthentication = 
			Expressions.instance().createMethodExpression("#{kiwi.user.facebookLoginAction.authenticate()}");
		try {
			identityManagerService.login(user.getLogin(), "", customAuthentication);
		} catch (LoginException e) {
			e.printStackTrace();
			return "failure";
		}
		
		return "success";
		
//		new RunAsOperation(){
//			@Override
//			public void execute() {
				
//				IdentityManagerService identityManagerService = 
//					(IdentityManagerService) Component.getInstance("identityManagerService");
//				if(user == null) {
//					if(userService.userExists(username)) {
//						facesMessages.add("User #0 already exists", username);
//						log.error("could not create user #0; user already exists", username);
//					} else {
//						
//						facesMessages.add("First time you try to authenticate via Facebook? " +
//								"Your generated password for the login is #0 ", password);
//							try {
//								Identity.instance().getCredentials().setPassword(password);
//								user = userService.createUser(username, firstName, lastName, password);
//								user.setEmail(email);
//								user.setFbId(fbId);
//								facesMessages.add("Created user (#0 #1)",firstName, lastName);
//								log.info("successfully created user #0 ", username);
//							} catch (UserExistsException e) {
//								facesMessages.add("User #0 already exists", username);
//								log.error("could not create user #0; user already exists", username);
//								throw new RuntimeException();
//							}
//						}		
//					
//					/** TODO: redirect to a form, pre-insert user-details and let the user confirm the details **/
//				} else {
				
					// TODO: compare values from FB and KiWi and ask user if he wants to change them.
//					user.setLogin(username);
//					user.setFirstName(firstName);
//					user.setLastName(lastName);
//					user.setEmail(email);
					
//					try {
//						identityManagerService.login(user.getLogin(), password);
//					} catch (LoginException e) {
//						e.printStackTrace();
//					}
//					Identity.instance().getCredentials().setUsername(username);
//					Identity.instance().getCredentials().setPassword(password);
//					Identity.instance().login();
//				}
//			}
//		}.addRole("admin").run();
		
//		if(user != null) {
//			log.info("created new KiWi user: #0" , user.getLogin());
//		} else {
//			// TODO: handle
//			return;
//		}
	}

	public boolean authenticate() {
		return true;
	}

	public static String getLocationFromJSONObject(JSONObject jo) {
		if (jo == null)
			return null;
		try {
			String country = jo.getString("country");
			String state = jo.getString("state");
			String city = jo.getString("city");
			return city + ", " + state + ", " + country;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String checkIfFBIDExists() {
		user = userService.getUserByFbId(fbId);
		if(user != null) {
			login();
			return "/dashboard/home.xhtml";
		} else {
			return "fbIdNotExists";
		}
	}
	
	public String checkIfUserExists() {
		user = userService.getUserByLogin(username);
		if(user != null) {
			return "fbUserExists";
		} else {
			return "fbUserNotExists";
		}
	}
	
	public String authenticateKiWiUser() {
		if(username != null && password != null) {
			IdentityManagerService identityManagerService = 
				(IdentityManagerService) Component.getInstance("identityManagerService");
			try {
				identityManagerService.login(username, password);
				return "authenticated";
			} catch(LoginException ex) {
				return "notAuthenticated";
			}
		} else {
			return "notAuthenticated";
		}
	}


	// not necessary anymore...
//	/**
//	 * Observer that is called when the identityManager has successfully created a user account. Will add additional
//	 * information to the user object, persist the user object in both, triple store and entity manager, and finally
//	 * set the userAccount's user.
//	 * 
//	 * @param userAccount
//	 */
//	@Deprecated
//	@Observer(value=JpaIdentityStore.EVENT_USER_CREATED, create=false)
//	public void userCreated(final User user) {
//		if(user != null && user.getLogin() != null) {
//			new RunAsOperation(){
//				@Override
//				public void execute() {
//					try {
//						
//						User _user = userService.createUser(user.getLogin(), user.getFirstName(), user.getLastName());
//						_user.setEmail(user.getEmail());
//
//						kiwiEntityManager.persist(_user);
//
//						userAccount.setUser(_user);
//
//						log.info("persisted facebook user #0 after first login", _user);
//
//						// create user profile
//						updateUserProfile(_user);
//
//						performLogin();
//					} catch(UserExistsException ex) {
//						log.error("user already exists: #0",user.getLogin());
//					}
//				}
//			}.addRole("admin").run();
//		}
//	}

	/**
	 * Create a KiWiProfileFacade with the information retrieved from Facebook
	 * @param u
	 */
	private void updateUserProfile(User u) {
		KiWiProfileFacade profile = profileService.getProfile(u);


		FacebookJsonRestClient fbClient = (FacebookJsonRestClient)Contexts.getSessionContext().get("facebook.user.client");

		try {
			final long userId = fbClient.users_getLoggedInUser();

			List<ProfileField> fields=new ArrayList<ProfileField>();
			fields.add(ProfileField.PIC_BIG);
			fields.add(ProfileField.EMAIL_HASHES);
			fields.add(ProfileField.PROXIED_EMAIL);
			fields.add(ProfileField.HOMETOWN_LOCATION);
			fields.add(ProfileField.CURRENT_LOCATION);
			fields.add(ProfileField.INTERESTS);
			fields.add(ProfileField.BIRTHDAY);
			fields.add(ProfileField.ABOUT_ME);
			fields.add(ProfileField.ACTIVITIES);
			fields.add(ProfileField.SEX);
			fields.add(ProfileField.LOCALE);
			fields.add(ProfileField.PROFILE_URL);

			List<Long> userIds=new ArrayList<Long>();
			userIds.add(userId);
			JSONArray ja=(JSONArray)fbClient.users_getInfo(userIds,fields);
			JSONObject jo=null;

			if(ja!=null)
				jo=ja.getJSONObject(0);

			if(jo!=null){
				log.info("facebook response (extended profile query):"+jo.toString(2));
				
				String facebookId = Long.toString(userId);
				profile.setFacebookAccount(facebookId);
				
				
				// set hometown location for user
				JSONObject location = null;
				if(!jo.isNull(ProfileField.CURRENT_LOCATION.toString())) {
					location = jo.getJSONObject(ProfileField.CURRENT_LOCATION.toString());
				} else if(!jo.isNull(ProfileField.HOMETOWN_LOCATION.toString())) {
					location = jo.getJSONObject(ProfileField.HOMETOWN_LOCATION.toString());
				}
				
				if(location != null) {

					String country = location.getString("country");
					String city = location.getString("city");
					String zip = location.getString("zip");

					if(city != null) {
						CityFacade cityf = profileService.getCity(zip, city, country);
						profile.setCity(cityf);

						// update user location
						profile.setLatitude(cityf.getLatitude());
						profile.setLongitude(cityf.getLongitude());
					}
				}
				
				// we combine facebook activities and interests into kiwi:interests
				String activities = jo.getString("activities");
				String interests  = jo.getString("interests");
				
				activities = activities == null?"":activities;
				interests  = interests == null?"":interests;
				
				activities.replace('\n', ',');
				interests.replace('\n',',');
				
				profile.setInterests(profileService.getInterests(activities+","+interests));
				
				// about string is used as user description when logging in from facebook
				String about = jo.getString(ProfileField.ABOUT_ME.toString());
				
				contentItemService.updateTextContentItem(profile, about);
				
				
				// some of the fields are localized and need parsing depending on the user locale
				String loc      = jo.getString(ProfileField.LOCALE.toString());
				if(loc == null) {
					loc = "en_US";
				}
				
				String[] _loc = loc.split("_");
				
				Locale l;
				
				if(_loc.length > 1) {
					l = new Locale(_loc[0],_loc[1]);
				} else {
					l = new Locale(loc);
				}

				// birthday
				String birthday = jo.getString(ProfileField.BIRTHDAY.toString());
				if(birthday != null) {
					DateFormat f = DateFormat.getDateInstance(DateFormat.LONG, l);
					try {
						Date d_birthday = f.parse(birthday);
						profile.setBirthday(d_birthday);
					} catch (ParseException e) {
						
						DateFormat f2 = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
						
						try {
							Date d_birthday = f2.parse(birthday);
							profile.setBirthday(d_birthday);
						} catch (ParseException e2) {
							log.error("could not parse date '#0' in locale '#1'",birthday, l.toString());
						}
					}
				}
				
				// gender
				String gender = jo.getString(ProfileField.SEX.toString());
				String gender_en;
				if(gender != null) {
					log.info("gender = #0, language = #1", gender, l.getLanguage());
					if(l.getLanguage().equals("de")) {
						if(gender.equals("männlich") || gender.endsWith("nnlich")) {
							gender_en = "male";
						} else if(gender.equals("weiblich")){
							gender_en = "female";
						} else {
							gender_en = "unknown";
						}
					} else {
						gender_en = gender;
					}
					profile.setGender(gender_en);
				}
				
				// fetch profile photo if it is new or updated
				String pic_url = jo.getString(ProfileField.PIC_BIG.toString());
				String pic_file = pic_url.substring(pic_url.lastIndexOf("/")+1);
				
				if(u.getProfilePhoto() == null || !u.getProfilePhoto().getMediaContent().getFileName().equals(pic_file)) {
					log.info("importing profile photo #0", pic_file);
					try {
						
						URL url = new URL(pic_url);
						InputStream is = url.openStream();
	
						ByteArrayOutputStream bout = new ByteArrayOutputStream();
	
						int c;
						while((c = is.read()) != -1) {
							bout.write(c);
						}
	
						byte[] data = bout.toByteArray();
	
						MultimediaService ms = (MultimediaService) Component.getInstance("multimediaService");
						String mimeType = ms.getMimeType(pic_file, data);
						
						ContentItem photo = contentItemService.createMediaContentItem(data, mimeType, pic_file);
						contentItemService.updateTitle(photo, 
								u.getFirstName()+" "+u.getLastName()+"'s Photo");
						photo.getResource().setLabel(Locale.getDefault(), u.getFirstName()+" "+u.getLastName()+"'s Photo");
						u.setProfilePhoto(photo);
						profile.setProfilePhoto(photo);
						kiwiEntityManager.persist(photo);
						
					} catch(Exception ex) {
						log.error("error while retrieving profile photo from Facebook",ex);
					}
				}

				
				// import a user's friends
				// friends should get the URI configurationService.getBaseUri()+"/user/fb_"+Long.toHexString(userId)
				JSONArray friendIds = (JSONArray)fbClient.friends_get();
				log.info("friends: #0", friendIds);
				HashSet<ContentItem> friends = new HashSet<ContentItem>();
				for(int i = 0; i<friendIds.length(); i++) {
					long friendId = friendIds.getLong(i);
					log.info("importing friend: #0", friendId);
					
					ContentItem friend = contentItemService.createContentItem("user/fb_"+Long.toHexString(friendId));
					
					if(friend.getTitle() != null && !friend.getTitle().equals("")) {
						log.info("found already registered friend #0", friend.getTitle());
					}
					friends.add(friend);
				}
				profile.setFriends(friends);
				
				
				// import a user's photos
//				JSONArray photoAlbums = (JSONArray)fbClient.photos_getAlbums(userId);
//				log.info("photo albums: #0", photoAlbums);
//				for(int i=0; i<photoAlbums.length(); i++) {
//					JSONObject album = photoAlbums.getJSONObject(i);
//					long albumId = album.getLong("aid");
//					log.info("importing album #0", albumId);
//					
//					try {
//						JSONArray photos = fbClient.photos_get(null,albumId);
//						log.info("photos: #0 #1", photos.getClass(), photos);
//					} catch(ClassCastException ex) {
//						log.warn("could not import album #0, bug in facebook API",albumId);
//					}
//				}
				
				kiwiEntityManager.persist(profile);
			}	
		} catch(JSONException ex) {
			log.error("error while retrieving facebook response",ex);
			facesMessages.add("the response received from Facebook is invalid; please try again later");
		} catch(FacebookException ex) {
			log.error("error while retrieving facebook response",ex);
			facesMessages.add("the response received from Facebook is invalid; please try again later");

		}		
	}
	
	private void performLogin() {
		/* set the identity */
//		Identity.setSecurityEnabled(false);
		Identity identity = Identity.instance();
		identity.getCredentials().setUsername(user.getLogin());

		/**
		 * register an authentication method that always returns true
		 * (we do not need another authentication since the IDP server 
		 * already authenticated the user)
		 */
		identity.setAuthenticateMethod(
				Expressions.instance()
					.createMethodExpression("#{kiwi.user.facebookLoginAction.authenticate()}")
					);
		try {
			identity.authenticate();
		} catch (LoginException e) {
			log.error("error while authenticating Facebook user (should never happen!)",e);
		}

		/* login the new identity */
		identity.login();
		if(identity.isLoggedIn()) {
			log.debug("User #0 has logged in", user.getLogin());
		}

		/* the currentUserFactory needs a refresh to be able to load the new identity */
		Conversation.instance().end(true);
		CurrentUserFactory currentUserFactory = 
			(CurrentUserFactory) Component.getInstance("currentUserFactory");
		currentUserFactory.forceRefresh();

	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}


	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	/*
	 * is that allowed?!
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



}
