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
 package tagit2.action.content;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.api.geo.Location;
import kiwi.api.render.RenderingService;
import kiwi.api.user.CityFacade;
import kiwi.api.user.KiWiProfileFacade;
import kiwi.api.user.ProfileService;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.facades.PointOfInterestFacade;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.user.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import tagit2.api.query.IconService;
import tagit2.util.exchange.BlogPost;
import tagit2.util.exchange.MicroBlogPost;

@Name("tagit2.personAction")
@Scope(ScopeType.CONVERSATION)
public class PersonAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private KiWiProfileFacade profile;

	private String birthday;
	private String firstName, lastName, gender, street;
	private CityFacade city;
	private String email, mobile, phone, skype, facebook, twitter;
	private String cityName, cityPostalCode, country;
	private List<ContentItem> interests;
	private String about;
	private Location location;

	private List<MicroBlogPost> microBlogs;
	private List<BlogPost> blogs;

	private List<ContentItem> images;

	private boolean curUsr;

	private ContentItem profilePhoto;

	@In(create = true)
	private ContentItem currentContentItem;

	@In(create = true)
	private ProfileService profileService;

	@In(value = "tagit2.iconService", create = true)
	private IconService iconService;

	@Logger
	private Log log;

	@In
	private KiWiEntityManager kiwiEntityManager;

	@In
	private RenderingService renderingPipeline;

	@In
	private User currentUser;

	@Begin(join = true)
	public void begin() {
		if (profile == null || profile.getId() != currentContentItem.getId()) {
			profile = kiwiEntityManager.createFacade(currentContentItem,
					KiWiProfileFacade.class);
			if (profile.getBirthday() != null) {
				DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				birthday = formatter.format(profile.getBirthday());
			} else {
				birthday = "";
			}
			setFirstName(profile.getFirstName());
			setLastName(profile.getLastName());
			if (profile.getGender() != null) {
				if (profile.getGender().equals("male")) {
					gender = "männlich";
				} else {
					gender = "weiblich";
				}
			} else {
				gender = "";
			}
			street = profile.getStreet();
			email = profile.getEmail();
			mobile = profile.getMobile();
			phone = profile.getPhone();
			skype = profile.getSkype();
			facebook = profile.getFacebookAccount();
			twitter = profile.getTwitterAccount();
			interests = new LinkedList<ContentItem>();
			interests.addAll(profile.getInterests());
			about = renderingPipeline.renderEditor(profile.getDelegate(), currentUser);
			location = new Location(profile.getLatitude(), profile
					.getLongitude());
			log.info("startet personAction witdh item #0", profile.getTitle());

			// set city
			if (profile.getCity() != null) {
				if (profile.getCity().getCountry() != null) {
					country = profile.getCity().getCountry().getISOCode();
				} else {
					country = "";
				}
				cityPostalCode = profile.getCity().getPostalCode();
				cityName = profile.getCity().getName();
			} else {
				cityName = "";
				cityPostalCode = "";
				country = "";
			}

			profilePhoto = profile.getProfilePhoto();

			setCurUsr(currentUser.getContentItem().getId() == currentContentItem
					.getId());

			microBlogs = null;
			blogs = null;
			images = null;

			log.info("user #0 is currentUser? #1", profile.getNickname(),
					curUsr);
		}
	}

	@End
	public void close() {
		// profile.setLatitude(location.getLatitude());
		// profile.setLongitude(location.getLongitude());
		// kiwiEntityManager.persist(profile);
		// TODO maybe it should possible for currentUser to change his/her
		// profile
	}

	public String getCityPostalCode() {
		return cityPostalCode;
	}

	public void setCityPostalCode(String cityPostalCode) {
		this.cityPostalCode = cityPostalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public ContentItem getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(ContentItem profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityName() {
		return cityName;
	}

	public List<ContentItem> getInterests() {
		return interests;
	}

	public void setInterests(List<ContentItem> interests) {
		this.interests = interests;
	}

	public void setCurUsr(boolean curUsr) {
		this.curUsr = curUsr;
	}

	public boolean isCurUsr() {
		return curUsr;
	}

	public List<MicroBlogPost> getMicroBlogPosts() {
		if (microBlogs == null) {
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy - hh:mm");
			microBlogs = new LinkedList<MicroBlogPost>();
			List<ContentItem> posts = profileService.getMicrobloggingPosts(
					profile, 20);
			for (ContentItem c : posts) {
				String m = formatter.format(c.getModified());
				microBlogs.add(new MicroBlogPost(getMicroBlogType(c),
						renderingPipeline.renderEditor(c, currentUser), m));
			}
			log.info("Person has #0 microBlogPosts", microBlogs.size());
		}
		return microBlogs;
	}

	private String getMicroBlogType(ContentItem c) {
		for (KiWiResource type : c.getTypes()) {
			String seRQLID = type.getSeRQLID();
			if (seRQLID.contains(Constants.NS_KIWI_CORE + "TwitterPost")) {
				return "twitter";
			}
			if (seRQLID.contains(Constants.NS_KIWI_CORE + "FacebookPost")) {
				return "facebook";
			}
		}
		return "microblog";
	}

	public List<BlogPost> getBlogPosts() {
		if (blogs == null) {
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy - hh:mm");
			blogs = new LinkedList<BlogPost>();
			List<ContentItem> posts = profileService.getBlogPosts(profile, 10);
			for (ContentItem c : posts) {
				PointOfInterestFacade blog = kiwiEntityManager.createFacade(c,
						PointOfInterestFacade.class);
				String m = formatter.format(c.getModified());
				String blogDesc = renderingPipeline.renderHTML(c);
				blogs.add(new BlogPost(blog.getId(),iconService
						.getIconStringForBlogPost(blog), c.getTitle(),
						getBlogTeaser(blogDesc), blogDesc, blog.getLatitude(),
						blog.getLongitude(), m));
			}
			log.info("Person has #0 blog entries", blogs.size());
		}
		return blogs;
	}

	// TODO
	private String getBlogTeaser(String description) {
		return "teaser";
	}

	public List<ContentItem> getImages() {
		if (images == null) {
			images = profileService.getImages( profile );
			log.info("user has #0 images", images.size());
		}

		return images;
	}

}
