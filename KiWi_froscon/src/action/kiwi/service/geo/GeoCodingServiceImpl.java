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
package kiwi.service.geo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;

import kiwi.api.config.ConfigurationService;
import kiwi.api.geo.GeoCodingFailedException;
import kiwi.api.geo.GeoCodingServiceLocal;
import kiwi.api.geo.GeoCodingServiceRemote;
import kiwi.api.geo.Location;
import kiwi.model.Constants;

import org.geonames.FeatureClass;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.cache.CacheProvider;
import org.jboss.seam.log.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * GeoCodingServiceImpl
 *
 * @author Sebastian Schaffert
 *
 */
@Stateless
@Name("kiwi.core.geoCodingService")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class GeoCodingServiceImpl implements GeoCodingServiceLocal, GeoCodingServiceRemote {
	
	@Logger
	private Log log;
	
	@In
	private CacheProvider cacheProvider;
	
	@In
	private ConfigurationService configurationService;

	/**
	 * Resolve the location of the address given as parameter using the Google Geocoding API. If
	 * more than one location matches the address, the service returns the first match.
	 * <p>
	 * Note that using the Google API requires a Google API key to be configured in the admin
	 * interface. The Google API has a limit of 5000 geocoding calls per day, which this service
	 * tries to extend a bit using caching.
	 * 
	 * @param address the textual address of the location to resolve
	 * @throws GeoCodingFailedException when the address cannot be resolved
	 * @return
	 */
	@Override
	public Location getLocationGeonames(String address) throws GeoCodingFailedException {
		Location result = getLocationsGeonames(address).get(0);
		log.debug("Geonames: returning first location #0 for address #1",result,address);
		
		return result;
	}

	/**
	 * Resolve the location of the address given as parameter using the Google Geocoding API. If
	 * more than one location matches the address, the service returns the match closest to the
	 * centre location given as argument.
	 * <p>
	 * Note that using the Google API requires a Google API key to be configured in the admin
	 * interface. The Google API has a limit of 5000 geocoding calls per day, which this service
	 * tries to extend a bit using caching.
	 * 
	 * @param address the textual address of the location to resolve
	 * @param center  the center point for deciding which location to return
	 * @throws GeoCodingFailedException when the address cannot be resolved
	 * @return
	 */
	@Override
	public Location getLocationGeonames(String address, Location center) throws GeoCodingFailedException {
		Location nearest = null;
		for(Location loc : getLocationsGeonames(address)) {
			double distance_new = distance(loc,center);
			double distance_old = nearest != null ? distance(nearest,center) : Double.MAX_VALUE;
				
			if(distance_new < distance_old) {
				nearest = loc;
			}
		}
		log.debug("Geonames: returning nearest location #0 for address #1",nearest,address);
		return nearest;
	}
	
	/**
	 * Resolve a location using the GeoNames web service API. Returns a list of Location objects 
	 * representing alternative locations for the given address. The result list is always ensured
	 * to contain at least one element; if no results have been returned, a GeoCodingFailedException
	 * is thrown.
	 * 
	 * @param address
	 * @return
	 * @throws GeoCodingFailedException
	 */
	private List<Location> getLocationsGeonames(String address) throws GeoCodingFailedException {
        // look up address in cache
        if (cacheProvider.get("kiwi.core.geoCodingService.geonames",address) != null) {
            return (List<Location>)cacheProvider.get("kiwi.core.geoCodingService.geonames",address);
        }

		
		final ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
        searchCriteria.setQ(address);
        searchCriteria.setFeatureClass(FeatureClass.P); // only look for cities and places
        try {
        	final ToponymSearchResult searchResult = org.geonames.WebService.search(searchCriteria);

        	final int resultsCount = searchResult.getTotalResultsCount();

        	LinkedList<Location> results = new LinkedList<Location>();
        	
        	if (resultsCount == 0) {
        		throw new GeoCodingFailedException("no results", address, searchCriteria.toString());
        	}
        	
        	for(Toponym t : searchResult.getToponyms()) {
        		final double longitude = t.getLongitude();
        		final double latitude = t.getLatitude();
        		results.add(new Location(latitude, longitude));
        	}
        	
            cacheProvider.put("kiwi.core.geoCodingService.geonames",address,results);

        	return results;
        } catch (final Exception e) {
        	throw new GeoCodingFailedException(e);
        }
		
	}
	

	/**
	 * Resolve the location of the address given as parameter using the Google Geocoding API. If
	 * more than one location matches the address, the service returns the first match.
	 * <p>
	 * Note that using the Google API requires a Google API key to be configured in the admin
	 * interface. The Google API has a limit of 5000 geocoding calls per day, which this service
	 * tries to extend a bit using caching.
	 * 
	 * @param address the textual address of the location to resolve
	 * @return
	 */
	@Override
	public Location getLocationGoogle(String address) throws GeoCodingFailedException {
		try {
			Location result = getLocationsGoogle(address).get(0);
			log.debug("Google: returning first location #0 for address #1",result,address);
			return result;
		} catch(IOException ex) {
			throw new GeoCodingFailedException(ex);
		}
	}

	/**
	 * Resolve the location of the address given as parameter using the Geonames Web Services. If
	 * more than one location matches the address, the service returns the first match.
	 * 
	 * @param address the textual address of the location to resolve
	 * @return
	 */
	@Override
	public Location getLocationGoogle(String address, Location center) throws GeoCodingFailedException {
		// we simply use Pythargoras' theorem for finding the nearest point from the list
		try {
			Location nearest = null;
			for(Location loc : getLocationsGoogle(address)) {
				double distance_new = distance(loc,center);
				double distance_old = nearest != null ? distance(nearest,center) : Double.MAX_VALUE;
					
				if(distance_new < distance_old) {
					nearest = loc;
				}
			}
			log.debug("Google: returning nearest location #0 for address #1",nearest,address);
			return nearest;
		} catch (IOException e) {
			throw new GeoCodingFailedException(e);
		}
		
	}

	/**
	 * Generic method to retrieve locations from Google. Returns a list of locations matching the address.
	 * The list is ensured to always contain at least one element; otherwise a GeoCodingFailedException is
	 * thrown.
	 * 
	 * @param address the textual address of the location to resolve
	 * @return
	 * @throws GeoCodingFailedException when the query did not return any results for some reason
	 * @throws IOException should never actually happen
	 */
	private List<Location> getLocationsGoogle(String address) throws GeoCodingFailedException, IOException {
        if (address == null || address.isEmpty()) {
            throw new NullPointerException("The address to locate is null or enpty.");
        }

        // look up address in cache
        if (cacheProvider.get("kiwi.core.geoCodingService.google",address) != null) {
            return (List<Location>)cacheProvider.get("kiwi.core.geoCodingService.google",address);
        }

		LinkedList<Location> result = new LinkedList<Location>();
		
		final String url =
			"http://maps.google.com/maps/geo?q="
			+ URLEncoder.encode(address, "UTF-8")
			+ "&output=json&key=" + getGmapkey();
		try {
			final InputStream inputStream = new URL(url).openStream();
			final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			StringBuilder source = new StringBuilder();


			//        int statusCode = -1;
			//        while ((line = in.readLine()) != null) {
			//            // Format: 200,6,42.730070,-73.690570
			//            statusCode = Integer.parseInt(line.substring(0, 3));
			//            if (statusCode == 200) {
			//                final int length_200 = "200,6,".length();
			//                final String latStr  = line.substring(length_200, line.indexOf(',', length_200));
			//                final String longStr = line.substring(line.indexOf(',', length_200) + 1, line.length());
			//                result.add(new Location(Double.parseDouble(latStr), Double.parseDouble(longStr)));
			//            }
			//        }
			while ((line = in.readLine()) != null) {
				source.append(line);
			}

			// parse JSON result
			JSONObject gresult = new JSONObject(source.toString());

			int statusCode = gresult.getJSONObject("Status").getInt("code");
			
			// iterate over place marks for result
			JSONArray placemarks = gresult.getJSONArray("Placemark");
			for(int i = 0; i<placemarks.length(); i++) {
				JSONObject placemark = placemarks.getJSONObject(i);
				JSONObject point     = placemark.getJSONObject("Point");
				JSONArray  coords    = point.getJSONArray("coordinates");
				double lng = coords.getDouble(0);
				double lat = coords.getDouble(1);
				double alt = coords.getDouble(2);
				
				result.add(new Location(lat,lng));
			}

			if (result.size() == 0) {
				switch (statusCode) {
				case 400:
					log.error("error while resolving location using google: bad request (address: #0, query: #1)", address, url);
					throw new GeoCodingFailedException("bad request", address, url);
				case 500:
					log.error("error while resolving location using google: unknown server error (address: #0, query: #1)", address, url);
					throw new GeoCodingFailedException("unknown server error", address, url);
				case 601:
					log.error("error while resolving location using google: missing query (address: #0, query: #1)", address, url);
					throw new GeoCodingFailedException("missing query", address, url);
				case 602:
					throw new GeoCodingFailedException("unknown error 602", address, url);
				case 603:
					log.error("error while resolving location using google: legal problem (address: #0, query: #1)", address, url);
					throw new GeoCodingFailedException("legal problem", address, url);
				case 604:
					log.error("error while resolving location using google: no route (address: #0, query: #1)", address, url);
					throw new GeoCodingFailedException("no route", address, url);
				case 610:
					log.error("error while resolving location using google: bad key (address: #0, query: #1)", address, url);
					throw new GeoCodingFailedException("bad key", address, url);
				case 620:
					log.error("error while resolving location using google: too many queries, limit of 5000 google geocodings/day exceeded (address: #0, query: #1)", address, url);
					throw new GeoCodingFailedException("Google query limit exceeded", address, url);
				default:
					throw new GeoCodingFailedException("no results", address, url);
				}
			}

			cacheProvider.put("kiwi.core.geoCodingService.google",address,result);
			return result;
		} catch(JSONException ex) {
			log.error("error while parsing JSON response from Google",ex);
			throw new GeoCodingFailedException("error while parsing JSON response from Google",address,url);
		}
		
	}
	
	
    private String getGmapkey() {
        return configurationService.getConfiguration("tagit.googlekey", Constants.GOOGLE_KEY).getStringValue();
    }

    
    private static double distance(Location a, Location b) {
    	// apply Pythargoras' theorem...
    	
		double sq_distance_new = 
			((a.getLatitude() - b.getLatitude()) * (a.getLatitude() - b.getLatitude())) +
			((a.getLongitude() - b.getLongitude()) * (a.getLongitude() - b.getLongitude()));
    	
		return Math.sqrt(sq_distance_new);
    }
}
