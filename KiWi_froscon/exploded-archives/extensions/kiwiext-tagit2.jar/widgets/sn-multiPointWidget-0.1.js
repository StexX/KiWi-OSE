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
 *   Thomas Kurz
 */
/**
 * This widgets allows do display a list of articles, which were imported in and located by Tagit2.
 * 
 * What do i need to use this widget?
 * 		- include some scripts in your websites header if they are not yet included
 * 			-> jQuery (download current release on http://docs.jquery.com/Downloading_jQuery or link on it)
 * 			-> jQuery UI (download on http://jqueryui.com/download - at least core)
 * 			-> GMaps2 API (see http://code.google.com/intl/de/apis/maps/documentation/reference.html)
 * 			   with your own google key that you can get here (http://code.google.com/intl/de/apis/maps/signup.html)
 * 		- include this script
 * 		- define a canvas (for example a div) with an id (for this example 'myCanvas') inside
 * 		  the body of your page. The canvas must have a well-defined height and width.
 * 		- insert the following lines of code in your websites head and alter the list of ids
 * 		  and the hostname value (to host of tagit).
 * 		  ATTENTION: ids and host must be set
 * 
 * 			<script type="text/javascript">
 * 				jQuery(document).ready(function(){
 *					jQuery("#myCanvas").sn_multipointmap({
 *						host:"http://localhost:8080",
 *						ids:1,2,3
 *					});
 *				})
 * 			</script>
 * 		- thats it!
 * 
 * How can i configure the widget?
 * 		- you are able to configure the widget by adding some options while initialization:
 * 			-> host: the hostname of the server, where tagit is running (important)
 * 			-> ids : a comma-separated list of ids (important)
 * 			-> zoom: the zoom level if there is just one point on the map
 * 			-> label:a JSON object with properties 'loading' and 'nocontent'
 * 			   e.g. {loading:"Loading...",nocontent:"No content to display"}
 * 			-> link: where the user is linked to, when he clicks a marker on the map (default is a link to tagit)
 *			   e.g. "http://www.mypage.com/home?id=|&type=article" ( | is automatically substituted by article id)
 *
 * HAVE FUN!
 */
(function($){
	$.widget("tagit.sn_multipointmap", {
		/**
		 * Constructor; 
		 */
		_init: function() { 
			this.refresh();
		},
		
		refresh: function() {
			//get local vars
			var host = this.options.host;
			var ids = this.options.ids;
			var label = this.options.label;
			var zoom = this.options.zoom;
			var link = this.options.link;
			
			//get canvas id and init map
			var canvasId = jQuery(this.element).attr("id");
			var element = this.element;
			element.html(label.loading);
			
			//basic url for service on localhost is
			//http://localhost:8080/KiWi/seam/resource/services/widgets/tagit2/points/multi
			
			//get point
			var url=host+"/KiWi/seam/resource/services/widgets/tagit2/points/multi?ids="+ids+"&jsonpCallback=?";

			jQuery.getJSON(url,function(data){
				if(data.length > 0) {
					
					var getMarker = function(jsmarker) {
						//create icon
						var iconUrl = host+"/KiWi/tagit/image/icon16x16/"+jsmarker.point['@iconUrl'];
						var my_icon = new GIcon(G_DEFAULT_ICON);
						my_icon.image = iconUrl;

						//set map and add point
						var point = new GLatLng(jsmarker.point['@latitude'],jsmarker.point['@longitude']);
						var marker = new GMarker(point,{icon:my_icon,title:jsmarker.point['@title']});
						GEvent.addListener(marker,"click",function(){
							if(link == null) {
								//TODO link should be maybe different
								window.location=host+"/KiWi/home.seam?kiwiid=uri::"+jsmarker.point['@uri'];
							} else {
								var splitLink = link.split("|");
								window.location=splitLink[0]+jsmarker.point['@id']+splitLink[1];
							}
						});
						
						return marker;		
					}
					
					//init map
					var map = new GMap2(document.getElementById(canvasId));
					map.enableScrollWheelZoom();
					
					//if there is just one point
					if(data.length == 1) {
						var m = getMarker(data[0]);
						map.setCenter(m.getLatLng(),zoom);
						map.addOverlay( m );
					} else {
						var bounds = null;
						var markers = [];
						//set markers
						for(var i = 0; i < data.length; i++) {
							var m = getMarker(data[i]);
							if( bounds == null ) {
								bounds = new GLatLngBounds(m.getLatLng(),m.getLatLng());
							} else {
								bounds.extend(m.getLatLng());
							}
							markers.push(m);
						}
						
						//set map
						map.setCenter(bounds.getCenter(),map.getBoundsZoomLevel(bounds) );
						
						//add markers
						for(var i = 0; i < markers.length; i++) {
							map.addOverlay(markers[i]);
						}
					}
				} else {
					element.html(label.nocontent);
				}

			});
		}
	});
	
	$.tagit.sn_multipointmap.defaults = {
		host: 'http://localhost:8080',
		ids:   '1,2,3',
		zoom:	13,
		label:	{loading:"Loading...",nocontent:"nothing to display"},
		link:	null
	};
})(jQuery);