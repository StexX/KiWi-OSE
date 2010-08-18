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
 * A jQuery widget to display the KiWi tags for a context content item. Can be
 * invoked on arbitrary elements selected by jQuery. The widget currently supports two modes:
 * - list:            returns the list of currently assigned tags of the content item
 * - recommendations: returns the list of tag recommendations for the content item based on text
 *                    analysis
 * 
 * The widget takes the following options when initialising:
 * - uri: the URI of the currently displayed content item (either the currentContentItem in KiWi or 
 *        the URL of some external page using the widget); defaults to currently displayed page
 *        
 * - webServiceUrl: the URL to locate the KiWi ActivityWidgetWebService; defaults to
 *                  http://localhost:8080/KiWi/seam/resource/services/widgets/tags/{mode}
 *                
 * - num: the number of results to return (recommendations only); defaults to 10
 * 
 * - mode: one of list/recommendations; defaults to 'list'        
 * 
 * - callback: a function that takes the returned JSON data as arguments and returns the HTML to
 *             be inserted in the tag cloud; defaults to a simple list that renders each tag as
 *             a link to the search function    
 * 
 * Example:
 * 
 * <p id="tags">Test</p>
 * <script>
 *       $(document).ready(function(){
 *          $('#tags').tags({uri: 'http://localhost:8080/KiWi/content/FrontPage'});
 *       });
 * </script>
 *
 */
(function($){
	$.widget("kiwi.tags", {
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
			var mode       = this.getMode();
			var callback   = this.getCallback();
			
			if(!callback) {
				callback = function(data) {
					if(data.length == 0) {
                        element.html("<em>no tags</em>");
                    } else {
						var html="";
                        $.each(data, function(i, item) {
                            html += "<a href=\"\">" + item.tag.@title + "</a> ";
                        });
					}
					return html;
				}
			}
            
			if(webservice.charAt(webservice.length-1) != '/') {
				webservice += '/';
			}
			
            // compose the query to the webservice
            var url = webservice + mode + "?" + $.param({ uri: context, num: num, user: user }) + "&jsonpCallback=?";
            
            this.element.html("Loading ... ");
            
            var element = this.element;
            
            // call the TagsWidgetWebService using JSONP and create a <ul> list of items
            $.getJSON(url,
                function(data) {
                    var html= callback(data);
	                element.html(html);         
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
		 * The mode of the recommendation widget; one of simple/multifactor/personal
		 * 
		 */
		getMode: function() {
			return this._getData("mode");
		}
	});
	
	$.kiwi.tags.defaults = {
		webServiceUrl: 'http://localhost:8080/KiWi/seam/resource/services/widgets/tags',
		mode:          'list',
		uri:           window.location.href,
		num:           10		
	};
})(jQuery);
