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
 * A jQuery widget to display the KiWi stream of activities for a context content item. Can be
 * invoked on arbitrary elements selected by jQuery.
 * 
 * The widget takes the following options when initialising:
 * - uri: the URI of the currently displayed content item (either the currentContentItem in KiWi or 
 *        the URL of some external page using the widget)
 *      
 * - webServiceUrl: the URL to locate the KiWi ActivityWidgetWebService; defaults to
 *                  http://localhost:8080/KiWi/seam/resource/services/widgets/activity/list
 *                
 * - num: the number of results to return; defaults to 10             
 * 
 * Example:
 * 
 * <p id="activity">Test</p>
 * <script>
 *       $(document).ready(function(){
 *          $('#activity').activity({uri: 'http://localhost:8080/KiWi/content/FrontPage'});
 *       });
 * </script>
 *
 */
(function($){
	$.widget("kiwi.activity", {
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
            
            // compose the query to the webservice
            var url = $.param({ uri: context, num: num });
            
            this.element.html("Loading ...");
            
            var element = this.element;
            
            // call the ActivityWidgetWebService using JSONP and create a <ul> list of items
            $.getJSON(webservice + "?" + url + "&jsonpCallback=?",
                function(data) {
                    var html="";
                    $.each(data, function(i, item) {
                        html += "<li>" + item.activity.@created + ": " + item.activity.@message + "</li>";
                    });     
                
                    element.html("<ul class=\"kiwi-widget-activity\">"+html+"</ul>");         
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
		}
	});
	
	$.kiwi.activity.defaults = {
		webServiceUrl: 'http://localhost:8080/KiWi/seam/resource/services/widgets/activity/list',
        uri:           window.location.href,
		num:           10		
	};
})(jQuery);
