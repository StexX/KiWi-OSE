/*
 * Filename     : Route.java
 * Version      : 0.00.01-01
 * Date         : Nov 26, 2008
 * Copyright :
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2008 The KiWi Project. All rights reserved.
 * http://www.kiwi-project.eu
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  KiWi designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 */


package tagit2.api.content.route;


import java.io.Serializable;
import java.util.LinkedList;

import org.hibernate.validator.Range;

import kiwi.model.Constants;
import kiwi.model.annotations.KiWiFacade;
import kiwi.model.annotations.RDF;
import kiwi.model.annotations.RDFFilter;
import kiwi.model.annotations.RDFType;
import kiwi.model.content.ContentItem;
import kiwi.model.content.ContentItemI;


/**
 * Defines a route. Formally a route is a list of
 * <code>RoutePointFacade</code> instances, the user decides
 * about the order. A route <b>must</b> have at least two points. <br>
 * The route <b>must</b> have a start point, this point can be
 * used to describe the route.<br>
 * More the user is able to add more description points to an
 * existing route, a route can have none or more description
 * points. The description points are used to add additional
 * description to a given route.
 * 
 * @author mradules
 * @version 03-pre
 * @since 03-pre
 * @see TrackPointFacade
 */
@KiWiFacade
@RDFType( {Constants.NS_TAGIT + "Route", Constants.NS_TAGIT+"PointOfInterest", Constants.NS_GEO+"Point"})
@RDFFilter( Constants.NS_GEO+"Point" )
public interface RouteFacade extends ContentItemI, Serializable {

    public static enum Type {Hiking, Jogging, Mountainbike, Roadbike}
    
    /**
     * Returns the type for this route fragment.
     * TODO: should be an Enum (FacadeUtils should be extended)
     */
    @RDF(Constants.NS_ROUTEIT + "routeType")
    String getRouteType();

    void setRouteType(String type);
    
	/**
	 * The longitude of this point of interest. Maps to geo:long of the geo ontology
	 * in the triple store.
	 * 
	 * @return
	 */
	@Range(min=-180, max=180)
	@RDF(Constants.NS_GEO+"long")
	public double getLongitude();

	public void setLongitude(double longitude);

	/**
	 * The latitude of this point of interest. Maps to geo:lat of the geo ontology 
	 * in the triple store.
	 * @return
	 */
	@Range(min=-90, max=90)
	@RDF(Constants.NS_GEO+"lat")
	public double getLatitude();

	public void setLatitude(double latitude);

    /**
     * Register a new list of point for this route fragment, if
     * the route fragment contains already some point then this
     * value will be lost..
     */
    @RDF(Constants.NS_ROUTEIT + "trackPoints")
    LinkedList<TrackPointFacade> getTrackPoints();

    void setTrackPoints(LinkedList<TrackPointFacade> list);
    
    /**
     * waypoints are points, that are additional displayed with the route
     */
    @RDF(Constants.NS_ROUTEIT + "wayPoints")
    LinkedList<WayPointFacade> getWayPoints();

    void setWayPoints(LinkedList<WayPointFacade> list);

	/**
	 * The content items that are comments for this point of interest. Maps to sioc:has_reply 
	 * of the SIOC ontology in the triple store.
	 * 
	 * @return
	 */
    @RDF(Constants.NS_SIOC+"has_reply")
	public LinkedList<ContentItem> getComments();

	public void setComments(LinkedList<ContentItem> comments);

	/**
	 * The content items representing multimedia content that is associated with this point of
	 * interest. Maps to sioc:attachment.
	 * @return
	 */
    @RDF(Constants.NS_SIOC+"attachment")
	public LinkedList<ContentItem> getMultimedia();

	public void setMultimedia(LinkedList<ContentItem> multimedia);
    
    /**
     * this is the vertical climb (an addition of all climbs) of the route
     * @return
     */
    @RDF(Constants.NS_ROUTEIT + "verticalClimb")
    double getVerticalClimb();
    
    void setVerticalClimb(double verticalClimb);
    /**
     * The distance of the route
     * @return
     */
    @RDF(Constants.NS_ROUTEIT + "distance")
    double getDistance();
    
    void setDistance(double distance);
    
}
