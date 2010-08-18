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
 * This widgets allows do display an article, which is imported in and located by Tagit2.
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
 * 		- insert the following lines of code in your websites head and alter the id
 * 		  and the hostname value (to host of tagit).
 * 		  ATTENTION: id and host must be set
 * 
 * 			<script type="text/javascript">
 * 				jQuery(document).ready(function(){
 *					jQuery("#myCanvas").sn_singlepointmap({
 *						host:"http://localhost:8080",
 *						id:1
 *					});
 *				})
 * 			</script>
 * 		- thats it!
 * 
 * How can i configure the widget?
 * 		- you are able to configure the widget by adding some options while initialization:
 * 			-> host: the hostname of the server, where tagit is running (important)
 * 			-> id : the id (number) of the requested id
 * 			-> zoom: the zoom level when the point is displayed
 * 			-> label:a JSON object with properties 'loading' and 'nocontent'
 * 			   e.g. {loading:"Loading...",nocontent:"No content to display"}
 * 			-> link: where the user is linked to, when he clicks the marker on the map (default is a link to tagit)
 *			   e.g. "http://www.mypage.com/home?id=|&type=article" ( | is automatically substituted by article id)
 *
 * HAVE FUN!
 */
(function($){
	$.widget("tagit.sn_singlepointmap", {
		/**
		 * Constructor; 
		 */
		_init: function() {
			//get local vars
			var host = this.options.host;
			var id = this.options.id;
			var label = this.options.label;
			var zoom = this.options.zoom;
			var link = this.options.link;
			
			//get canvas id and element
			var canvasId = jQuery(this.element).attr("id");
			var element = this.element;
			element.html(label.loading);
			
			//basic url for service on localhost is
			//http://localhost:8080/KiWi/seam/resource/services/widgets/tagit2/points/single
			
			//get point
			var url=host+"/KiWi/seam/resource/services/widgets/tagit2/points/single?id="+id+"&jsonpCallback=?";
			jQuery.getJSON(url,function(data){
				if(data.point['@title'] != null) {
					//set map
					var map = new GMap2(document.getElementById(canvasId));
					map.enableScrollWheelZoom();
					//create icon
					var iconUrl = host+"/KiWi/tagit/image/icon16x16/"+data.point['@iconUrl'];
					var my_icon = new GIcon(G_DEFAULT_ICON);
					my_icon.image = iconUrl;

					//set map and add point
					var point = new GLatLng(data.point['@latitude'],data.point['@longitude']);
					map.setCenter(point,zoom);
					var marker = new GMarker(point,{icon:my_icon,title:data.point['@title']});
					map.addOverlay(marker);
				
					//set Link
					GEvent.addListener(marker,"click",function(){
						if( link == null ) {	
							//TODO link should be maybe different
							window.location=host+"/KiWi/home.seam?kiwiid=uri::"+data.point['@uri'];
						} else {
							var splitLink = link.split("|");
							window.location=splitLink[0]+data.point['@id']+splitLink[1];
						}
					});
				} else {
					element.html(label.nocontent);
				}
			});
		}
		
	});
	
	$.tagit.sn_singlepointmap.defaults = {
		host: 'http://localhost:8080',
		id:	   '0',
		label:	{loading:"Loading...",nocontent:"Nothing to display!"},
		zoom:	4,
		link:	null
	};
})(jQuery);
