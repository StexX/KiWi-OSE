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
 *   Sebastian Schaffert
 * 
 */

/**
 * A jQuery widget to display the KiWi recommendations for a context content item and/or user. Can be
 * invoked on arbitrary elements selected by jQuery. The widget currently supports three modes:
 * - simple: returns simple recommendations for a given user and content item
 * - multifactor: returns multifactor recommendations for a given user and content item
 * - personal: returns personal recommendations for a given user
 * 
 * The widget takes the following options when initialising:
 * - uri: the URI of the currently displayed content item (either the currentContentItem in KiWi or 
 *        the URL of some external page using the widget); defaults to currently displayed page
 *        
 * - user: the login name of a user for whom to calculate recommendations; defaults to "anonymous"       
 *      
 * - webServiceUrl: the URL to locate the KiWi ActivityWidgetWebService; defaults to
 *                  http://localhost:8080/KiWi/seam/resource/services/widgets/recommendations/{mode}
 *                
 * - num: the number of results to return; defaults to 10
 * 
 * - mode: one of simple/multifactor/personal; defaults to 'simple'             
 * 
 * - noRecLabel: text that is displayed if there are no recommendations
 * 
 * Example:
 * 
 * <p id="recommendations">Test</p>
 * <script>
 *       $(document).ready(function(){
 *          $('#recommendations').recommendations({uri: 'http://localhost:8080/KiWi/content/FrontPage', user: 'anonymous'});
 *       });
 * </script>
 *
 */
(function(jQuery){
	jQuery.widget("kiwi.tagitRecommendations", {
		/**
		 * Constructor; 
		 */
		_init: function() { 
			this.refresh();
		},
		
		refresh: function() {
            var webservice = this.getWebServiceUrl();
            var context    = this.getUri();
            var num        = this.getNum();
			var user       = this.getUser();
			var mode       = this.getMode();
			var noRecLabel = this.getNoRecLabel();
            
			if(webservice.charAt(webservice.length-1) != '/') {
				webservice += '/';
			}
			
            // compose the query to the webservice
            var url = webservice + mode + "?" + jQuery.param({ uri: context, num: num, user: user }) + "&jsonpCallback=?";
            
            this.element.html("Loading ... ");
            
            var element = this.element;

            // call the RecommendationsWidgetWebService using JSONP and create a <ul> list of items
            jQuery.getJSON(url,
                function(data) {
            		
                    var html="<div class='noRecommendations'>"+noRecLabel+"</div>";
					
					if(data.length == 0) {
						element.html();
					} else {
						html="";
	                    jQuery.each(data, function(i, item) {
	                       html += "<li>" + 
							             "<a href=\"/KiWi/home.seam?kiwiid=uri::"+item.recommendation['@uri']+"\">" +
							             item.recommendation['@title'] + 
										 "</a><br /><span class='recomPerson'>von " + 
							             item.recommendation.user['@firstName'] + 
										 " " +
									     item.recommendation.user['@lastName'] + 
									"</span></li>";
						
	                    });     
	                
	                    element.html("<ul class=\"kiwi-widget-recommendations\">"+html+"</ul>");         
	                }
				}
            );			
		},
		
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
	     * The result count to return.
	     */	
		getNum: function() {
			return this._getData("num");
		},
		
		/**
		 * The currently active user
		 */
		getUser: function() {
			return this._getData("user");
		},
		
		/**
		 * The mode of the recommendation widget; one of simple/multifactor/personal/vectors
		 * 
		 */
		getMode: function() {
			return this._getData("mode");
		},
		
		setNoRecLabel: function(data) {
			this._setData("noRecLabel",data);
		},
		
		getNoRecLabel: function() {
			return this._getData("noRecLabel");
		}
	});
	
	jQuery.kiwi.tagitRecommendations.defaults = {
		webServiceUrl: 'http://localhost:8080/KiWi/seam/resource/services/widgets/recommendations',
		user:          'anonymous',
		mode:          'simple',
		uri:           window.location.href,
		num:           10,
		noRecLabel:		'No recommendations so far.'
	};
})(jQuery);
