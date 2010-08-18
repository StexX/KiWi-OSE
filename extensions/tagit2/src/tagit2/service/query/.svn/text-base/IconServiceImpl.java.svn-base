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
 package tagit2.service.query;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;

import kiwi.api.entity.KiWiEntityManager;
import kiwi.model.Constants;
import kiwi.model.content.ContentItem;
import kiwi.model.facades.PointOfInterestFacade;
import kiwi.model.kbase.KiWiResource;
import kiwi.model.ontology.SKOSConcept;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;

import tagit2.api.category.PointOfSubcategoryFacade;
import tagit2.api.category.PointWithCategoryFacade;
import tagit2.api.query.IconService;
import tagit2.util.query.Point;

@Scope(ScopeType.PAGE)
//@Transactional
@Name("tagit2.iconService")
@AutoCreate
public class IconServiceImpl implements IconService, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private enum Type { newsItem, blogPost, location, person, route, none }
	
	private Properties properties;
	
	@In
	private EntityManager entityManager;
	
    @In
    private KiWiEntityManager kiwiEntityManager;
    
    @Logger
    Log log;
    
    @Create
    public void create() {
    	properties = new Properties();
    	
    	InputStream stream;
		try {
			stream = getClass().getClassLoader().getResourceAsStream("properties/icon.properties");
			properties.load(stream);
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("created IconService");

    }
	
    //TODO set others than location
	public void setIconsOf(List<Point> points) {
		for( Point point : points ) {
			ContentItem c = entityManager.find(ContentItem.class, point.getId());
			switch( getType( c ) ) {
			case newsItem:
				setIconStringForNewsItem(point,c);
				break;
			case blogPost:
				point.setIcon("blog_default.png");
				break;
			case location:
				setIconStringForLocation(point,c);
				break;
			case person:
				point.setIcon("user.png");
				break;
			case route: 
				setIconStringForRoute(point,c);
				break;
			default:
				//type is none
				point.setIcon( properties.getProperty( "loc_Default" ) );
			}
		}
	}
	
	public String getIconOf( ContentItem contentItem ) {
		//TODO implement
		return "nachricht_default.png";
	}
	
	private Type getType(ContentItem c) {
		Type type = Type.none;
		//if( c != null && c.getTypes() != null ) {
			for( KiWiResource t : c.getTypes() ) {
				String seRQLID = t.getSeRQLID();
				if(seRQLID.contains(Constants.NS_TAGIT+"PointOfInterest")) {
					type = Type.location;
				} else if(seRQLID.contains(Constants.NS_KIWI_CORE+"BlogPost")) {
					type = Type.blogPost;
					break;
				} else if(seRQLID.contains(Constants.NS_FCP_CORE+"NewsItem")) {
					type = Type.newsItem;
					break;
				} else if(seRQLID.contains(Constants.NS_TAGIT + "Route")) {
					type = Type.route;
					break;
				} else if(seRQLID.contains(Constants.NS_KIWI_CORE + "User")) {
					type = Type.person;
				}
			}
		//}
		return type;
	}
	
	private void setIconStringForLocation(Point p, ContentItem c) {
		PointWithCategoryFacade pwc = kiwiEntityManager.createFacade(c, PointWithCategoryFacade.class);
		SKOSConcept concept = pwc.getCategory();
		if( concept != null ) {
			p.setIcon( properties.getProperty( "loc_"+concept.getPreferredLabel()  ));
		} else {
			p.setIcon( properties.getProperty( "loc_Default" ) );
		}
	}
	
	private void setIconStringForNewsItem(Point p, ContentItem c) {
		PointOfSubcategoryFacade pwc = kiwiEntityManager.createFacade(c, PointOfSubcategoryFacade.class);
		SKOSConcept concept = pwc.getSubCategory();
		if( concept != null ) {
			String ic =  properties.getProperty( "news_"+concept.getPreferredLabel() );
			if( ic == null || ic.equals("") ) {
				p.setIcon(properties.getProperty( "news_default" ));
			} else 
				p.setIcon( ic );
		} else {
			p.setIcon( properties.getProperty( "news_default" ) );
		}
	}
	
	private void setIconStringForRoute(Point p, ContentItem c) {
		PointWithCategoryFacade pwc = kiwiEntityManager.createFacade(c, PointWithCategoryFacade.class);
		SKOSConcept concept = pwc.getCategory();
		if( concept != null ) {
			p.setIcon( properties.getProperty( "route_"+concept.getPreferredLabel() ) );
		} else {
			p.setIcon( properties.getProperty( "route_Default" ) );
		}
	}
	
	public String getIconStringForBlogPost(PointOfInterestFacade blog) {
		SKOSConcept concept = blog.getCategory();
		if( concept != null ) {
			return properties.getProperty( "blog_"+concept.getPreferredLabel() ) ;
		} else {
			return properties.getProperty( "blog_Default" ) ;
		}
	}

}
