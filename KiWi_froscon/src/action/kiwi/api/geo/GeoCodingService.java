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
package kiwi.api.geo;

/**
 * GeoCodingService. A KiWi service for resolving textual location descriptions into 
 * latitude/longitude pairs. The GeoCodingService currently offers to resolve locations using
 * either the Google Geocoding API or the Geonames Web Services.
 *
 * @author Sebastian Schaffert
 *
 */
public interface GeoCodingService {

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
	public Location getLocationGoogle(String address) throws GeoCodingFailedException;
	
	
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
	public Location getLocationGoogle(String address, Location center) throws GeoCodingFailedException;
	
	/**
	 * Resolve the location of the address given as parameter using the Geonames Web Services. If
	 * more than one location matches the address, the service returns the first match.
	 * 
	 * @param address the textual address of the location to resolve
	 * @throws GeoCodingFailedException when the address cannot be resolved
	 * @return
	 */
	public Location getLocationGeonames(String address) throws GeoCodingFailedException;
	
	/**
	 * Resolve the location of the address given as parameter using the Google Web Services. If
	 * more than one location matches the address, the service returns the match closest to the
	 * centre location given as argument.
	 * 
	 * @param address the textual address of the location to resolve
	 * @param center  the center point for deciding which location to return
	 * @throws GeoCodingFailedException when the address cannot be resolved
	 * @return
	 */
	public Location getLocationGeonames(String address, Location center) throws GeoCodingFailedException;

}
