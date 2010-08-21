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
 *   Szaby Gruenwald
 * 
 */

/**
 * A jQuery widget to rate a contentItem using stars and also to display 
 * the actual average rating of that contentItem. Features of the widget:
 * - show average and count of ratings
 * - all users can rate only once
 * - remove button for the active users rating optional
 * - 
 * 
 * The widget takes following options when initializing:
 * - uri: the URI of the currently displayed content item (either the currentContentItem in KiWi or 
 *        the URL of some external page using the widget); defaults to currently displayed page
 *        
 * - user: the login name of a user for whom to calculate recommendations; defaults to "anonymous"       
 *      
 * - webServiceUrl: the URL to locate the KiWi ActivityWidgetWebService; defaults to
 *                  http://localhost:8080/KiWi/seam/resource/services/widgets/rating/{mode}
 *                
 * - 
 */
(function($){
	$.widget("kiwi.rating", {
		/**
		 * Constructor; 
		 */
		_init: function() { 
			this.refresh();
		},
		
		refresh: function() {
			var webservice = this.getWebServiceUrl();
			var context    = this.getUri();
			// var num        = this.getNum();
			var user       = this.getUser();
			var mode       = this.getMode();
			var label	   = this.getLabel();
           
			if(webservice.charAt(webservice.length-1) != '/') {
				webservice += '/';
			}
			
           // 	compose the query to the webservice
           var url = webservice + "get" + "?" + $.param({ uri: context, user: user }) + "&jsonpCallback=?";
           
           this.element.html("Loading ... ");
           
           
           $(this.element).css("padding-bottom", "5px");
           
           var element = this.element;
           var st_cap = escape(context).replace(/[\/%]/g,"") + "_cap";
           var starWidgetHtml = this.starWidgetHtml.replace('%stars-cap%',st_cap);
           var starWidgetHtml = starWidgetHtml.replace('%stars-label%',this.getLabel());
           
           var self = this, o = this.options;
			
           self.appendAverage = function(){
        	   var max = self.starsInstance.options.items;
        	   function fmt(x,digits){
        		   q = Math.pow(10,digits);
        		   return Math.round(x*q)/q;
        	   }
        	   var res = " " + fmt(o.average, 2) + "/" + max + " (" + o.ratings + ")";
        	   self.starsInstance.options.captionEl.append(res);
           };
        	   
           // call the ratingWidgetWebService using JSONP and create a <ul> list of items
           $.getJSON(url,
               function(data) {
                   var html="";
                   if(data.rating.@ciExists == "false") {
                	   	
                	   	// debugger;
                	   element.html("The content item doesn't exist on the server.");
						/*
						element.html(starWidgetHtml);
						element.stars({
								captionEl: $("#"+st_cap, element),
								disabled: false,
								oneVoteOnly: true
						});
						var instance = element.data("stars");
						console.info(instance);
						*/
                   } else {

                	    // debugger;
						element.html(starWidgetHtml);
						element.stars({
								captionEl: $("#"+st_cap, element),
								disabled: false,
								oneVoteOnly: false,
								callback: function(ui, type, value){
									url = webservice + "set" + "?" + $.param({ uri: context, user: user, rating: value }) + "&jsonpCallback=?";
									$.getJSON(url, function(data){
										o.average = data.rating.@averageRating;
										o.ratings = data.rating.@nrOfRatings;
										self.starsInstance.select(Math.round(o.average));
										self.appendAverage();
									});
								}
						});
						
						o.average = data.rating.@averageRating;
						o.ratings = data.rating.@nrOfRatings;
						
						self.starsInstance = element.data("stars");
						// TODO The widget shows the the rounded average value right now.
						self.starsInstance.select(Math.round(o.average));
						self.appendAverage();
						// console.info(this.starsInstance);

						self.starsInstance.$stars.bind("mouseout.rating", function(){
							if(self.starsInstance.options.disabled) return false;
							self.appendAverage();
						});
					
	                }
				}
           );			
		},
		
		starWidgetHtml:
			'<form>' +
				'%stars-label%<span id="%stars-cap%"></span>' +
				'<div id="stars">' +
					'<input type="radio" name="newrate" value="1" title="Poor" />' +
					'<input type="radio" name="newrate" value="2" title="Fair" />' +
					'<input type="radio" name="newrate" value="3" title="Average" />' +
					'<input type="radio" name="newrate" value="4" title="Good" />' +
					'<input type="radio" name="newrate" value="5" title="Excellent" />' +
					'<input type="radio" name="newrate" value="6" title="Perfect" />' +
				'</div>' +
			'</form>',
		
		/**
		 * The URI of the currently visited page
		 */
		getUri: function() {
			return this._getData("uri");
		},
		
		setUri: function(data) {
			this._setData("uri",data);
		},
		
		/**
		 * The URL of the web service to connect to
		 */
		getWebServiceUrl: function() {
			return this._getData("webServiceUrl");
		},
		
	
		/**
		 * The currently active user
		 */
		getUser: function() {
			return this._getData("user");
		},
		
		/**
		 * The currently active user
		 */
		getLabel: function() {
			return this._getData("label");
		},
		
		/**
		 * The mode of the recommendation widget; one of simple/multifactor/personal
		 * 
		 */
		getMode: function() {
			return this._getData("mode");
		}
	});
	
	$.kiwi.rating.defaults = {
		webServiceUrl: 'http://localhost:8080/KiWi/seam/resource/services/widgets/rating',
		user:          'anonymous',
		mode:          'simple',
		uri:           window.location.href,
		label:         'Rating'		
	};
})(jQuery);
