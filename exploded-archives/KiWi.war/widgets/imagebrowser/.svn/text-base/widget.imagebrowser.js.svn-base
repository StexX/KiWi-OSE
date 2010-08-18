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
 * A jQuery widget for browsing stored images and selecting one of them for inserting
 * using the resource uri. 
 * 
 * The widget takes following options when initializing:
 * - user: the login name of a user for whom to calculate the results; 
 * 		defaults to "anonymous"
 *      
 * - webServiceUrl: the URL to locate the KiWi ActivityWidgetWebService; defaults to
 *                  http://localhost:8080/KiWi/seam/resource/services/widgets/rating/{mode}
 *                
 * - 
 */
(function($){
	$.widget("kiwi.imagebrowser", {
		/**
		 * Constructor; 
		 */
		_init: function() { 
			this.refresh();
		},
		
		refresh: function() {
			var webservice = this.getWebServiceUrl();
			var context    = this.getUri();
			var user       = this.getUser();
			var query      = this.getQuery();
			var label	   = this.getLabel();
			var initCallback = this.getInitCallback();
           
			if(webservice.charAt(webservice.length-1) != '/') {
				webservice += '/';
			}
			
           // 	compose the query to the webservice
           var url = webservice + "getStatus" + "?" + $.param({ user: user }) + "&jsonpCallback=?";
//           var url = webservice + "getList" + "?" + $.param({ query: query, user: user }) + "&jsonpCallback=?";
           if($.jqm == null){
	           initCallback({
	    		   status: 1,
	    		   errorMsg: "jqModal is not loaded"
	    	   });
	           return;
	       }
           this.element.html("Loading ... ");
           
           var element = this.element;
           var self = this, o = this.options;
           window._imagebrowserElement = this;
			
           // call the ratingWidgetWebService using JSONP and ...
           $.getJSON(url,
               function(data) {
                   if(data.imagebrowser.@status == 0) {
                	   self.element.html(self.imagebrowser_popup_html);
                	   
                	   // define what the imagebrowser() function will do 
                	   window.imagebrowser = self.showImagebrowser;
                	   
                	   initCallback({status:0});
                   } else {
                	   self.element.html("connection failure");
                	   initCallback({
                		   status: data.imagebrowser.@status,
                		   errorMsg: data.imagebrowser.@error
                	   });
	               }
				}
           );
		},
		
		showImagebrowser: function(callback){
			var self = window._imagebrowserElement ;
			self.imageSelectorCallback = callback;
 		   $('#imagebrowserDialog').jqm({
 			   modal: true,
 			   overlayClass: 'whiteOverlay'})
 		   .jqDrag('.jqDrag')
 		   .jqmShow();

 		   // Close Button Highlighting.
 		   $('input.jqmdX')
 		   .hover(
 			   function(){ $(this).addClass('jqmdXFocus'); }, 
 			   function(){ $(this).removeClass('jqmdXFocus'); })
 		   .focus( 
 			   function(){ this.hideFocus=true; $(this).addClass('jqmdXFocus'); })
 		   .blur( 
 			   function(){ $(this).removeClass('jqmdXFocus'); });

 		   var hash = {
 				   uri:"whatever the selection will be..."
 		   };
 		   
 		   self.loadImages();
 		   // self.imageSelectorCallback(hash);
 	   	},
 	   	
 	   	loadImages: function(h){
 	   		var self = window._imagebrowserElement ;
 	   		var webservice = self.getWebServiceUrl();

 	   		if(webservice.charAt(webservice.length-1) != '/') {
				webservice += '/';
			}
 	   		
 	   		var url = webservice + "query" + "?" + 
 	   			$.param({ user: self.getUser(), query: "type:kiwi:Image", page: 1, pageSize: 10 })
 	   			+ "&jsonpCallback=?";
	 	   	$.getJSON(url,
                function(data) {
                    if(data.imagebrowser.@status == 0) {
                    	alert("ok");
                    } else {
                    	alert("wrong");
 	               }
 				}
            );
 	   	},
 	   	
 	    imagebrowser_popup_html: "<div id='imagebrowserDialog' class='jqmDialog'>" +
 	   		'<div class="jqmdTL"><div class="jqmdTR"><div class="jqmdTC jqDrag">Dialog Title</div></div></div>' +
 	   		'<div class="jqmdBL"><div class="jqmdBR"><div class="jqmdBC">' +

	 	   		'<div class="jqmdMSG" id="imagebrowserContent">' +"Styled windows or dialogs are easy!<br /><br />" +
	 	   		'<a class="jqmClose" href="javascript:void(0);">close</a>' +
	 	   		'</div>'+

 	   		"</div></div></div>" +
 	   		'<input type="image" src="dialog/close.gif" class="jqmdX jqmClose"/>' +
 	   		"</div>",
		
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
		 * The URL of the web service to connect to
		 */
		getLabel: function() {
			return this._getData("label");
		},
		
		/**
		 * The query to run on the backend for filtering images
		 */
		getQuery: function() {
			return this._getData("query");
		},
		
		/**
		 * The currently active user
		 */
		getUser: function() {
			return this._getData("user");
		},
		getInitCallback: function() {
			return this._getData("initCallback");
		}
		
	});
	
	$.kiwi.imagebrowser.defaults = {
		webServiceUrl: 'http://localhost:8080/KiWi/seam/resource/services/widgets/imagebrowser',
		user:          'anonymous',
		query:		   '',
		uri:           window.location.href,
		callback:	   function(hash){alert(hash.status)}
	};
})(jQuery);
