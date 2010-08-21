/*
 *Require:
 *- jquery.js - 1.2.6 and greater
 *- jquery.template.js
 *- search_templates.js
 *
 *
 *Usage:
 * jQuery.noConflict();
 * var search = new jQuery.Search("container_div");
 * search.deploy();
 *
 *
 * TODOs:
 * - Should cancel request when user resubmits a query
 * - Design: not sure if results pane should be cleared everytime pagination is used;
 *           perhaps a gray out while loading would "perform" better?
 * - Bookmarkability of search results (perhaps via location.hash)


 */
(function( $ ) {

  $.Search = function(CS, endpointUrl){
  
    this.messages = {
        'searchtext':'Suchtext',
        'no_result':'Keine Ergebnisse',
        'next':'Vorw&auml;rts',
        'previous':'Zur&uuml;ck',
        'all':'Alle',
        'type':'Typ',
        'tag': 'Tag',
        'category':'Kategorie',
        'start':'Von',
        'end':'Bis',
        'no_facets':'Keine Facetten vorhanden',
        'no_tags':'Keine Tags vorhanden',
		'page':'Seite',
		'tags':'Stichworte'
    }
  
	this.ENDPOINT_URL = endpointUrl;
	this.PAGE_SIZE = 5;

	// templates
	this.template_widget = $.template(TEMPLATE_SN_SEARCH_WIDGET);

	//--------MEMBER VARS
	this.CS = CS; //container selector
	this.currentPage = 1;
	this.count = 0;
	this.orderBy = "modified";
    this.type = "default";
    this.facets = [];
    this.tags = [];
    this.query = "";
    this.order = "desc";
    
    this.tempData = {   'query': {
                            'string':'peter',
                            'type':{
                                'title':'Blog',
                                'uri':'http://abc.de/Blog'
                            },
                            'facets':[
                                {
                                    'uri':'http://xyz.de/facet1',
                                    'title':'facet_1',
                                    'values':[{
                                        'uri':'http://kiwi.de/a_a',
                                        'title':'a_eins'
                                        },{
                                        'uri':'http://kiwi.de/a_b',
                                        'title':'a_zwei'
                                    }]
                                },{
                                    'uri':'http://xyz.de/facet2',
                                    'title':'facet_2',
                                    'values':[{
                                        'uri':'http://kiwi.de/b_a',
                                        'title':'b_eins'
                                        
                                    }]
                                }
                            ],
                            'range':{
                                'start':'2010-12-20 13:14:00',
                                'end':'2010-12-20 14:14:00'
                            }
                                
                        },
                        'results': {
							'order':'desc',
                            'total':465,
                            'page':1,
							'items':[
								{
                                    'type':'http://abc.de/Article',
                                    'title':'128dc1aa256',
									'url':'http://localhost/content/3dad3dbb-43d3-46a6-9c15-1df72f828950',
									'description':' dummy_text ',
									'author_name':'Peter.340',
									'rating':0.0,
									'views':3,
                                    'ecid':'1234asdf'
								},
								{
                                    'type':'http://abc.de/Image',
									'title':'ArtAround',
									'url':'http://users.fh-salzburg.ac.at/~fhs12575/ontology/',
									'description':' This ontology describes the <span class="highlight">KiWi</span> artaround ontology ',
									'author_name':'admin',
									'rating':3.0,
									'views':6,
                                    'ecid':'5678asdf'
                                }],
                            'facets':[
                                {
                                    'uri':'http://xyz.de/facet1',
                                    'title':'facet_a',
                                    'values':[{
                                            'uri':'http://kiwi.de/a_a',
                                            'title':'a_eins',
                                            'count':12
                                    },{
                                            'uri':'http://kiwi.de/a_b',
                                            'title':'a_eins',
                                            'count':24
                                    }]
                                },{
                                    'uri':'http://xyz.de/facet2',
                                    'title':'facet_b',
                                    'values':[{
                                            'uri':'http://kiwi.de/b_a',
                                            'title':'b_eins',
                                            'count':10
                                    }]

                                }],
                            'types':[
                                {
                                    'title':'Artikel',
                                    'uri':'http://abc.de/Article',
                                    'count':123
                                },{
                                    'title':'Image',
                                    'uri':'http://abc.de/Image',
                                    'count':234
                                },{
                                    'title':'Blog',
                                    'uri':'http://abc.de/Blog',
                                    'count':345
                                }],
                            'tags':[
                                {
                                    'title':'Artikel',
                                    'uri':'http://abc.de/Article',
                                    'count':123
                                },{
                                    'title':'Image',
                                    'uri':'http://abc.de/Image',
                                    'count':234
                                },{
                                    'title':'Blog',
                                    'uri':'http://abc.de/Blog',
                                    'count':345
                                }
                            ]
                        }
                    };



//------------------------------------------------------------------------------
//-------------------- general UI
//------------------------------------------------------------------------------

	/**
	 * Populates the search results
	 *
	 */
	this.populate = function(data) {
        this.populateTypes(data);
        this.populateItems(data.results.items);
        this.populatePagination(data);
        //this.populateFaccets(data.results.facets);
        this.populateTags(data.results.tags);
        this.populateQuery(data.query);
	}
    
    this.populateTypes = function(data) {
        
		this.count = data.results.total;
		
        var canvas = $("#type_tabs");
        var self = this;
        
        var liClass = "type_tab_inactive";
		this.type = data.query.type;
        if(this.type == 'default') {
            liClass = "type_tab_active";
        } 
        var link = $('<a></a>').text(this.messages.all+'('+data.results.total+')').click(function(){self.setType('default');});
        $('<li></li>').addClass(liClass).append(link).appendTo(canvas);

        $.each(data.results.types, function(i, type){
            var liClass = "type_tab_inactive";
            if(self.type.uri == type.uri) {
                liClass = "type_tab_active";
				self.count = type.count;
            } 
            var link = $('<a></a>').text(type.title+'('+type.count+')').click(function(){self.setType(type.title, type.uri);});
            $('<li></li>').addClass(liClass).append(link).appendTo(canvas);
        });
        

    }
    
    this.populateItems = function(items) {
        
        var self = this;

		// For each data item, create one table row and populate it with a widget
		$.each(items, function(i, item){
			var single_result = jQuery("<div></div>").appendTo("#single_results");
           
            //templating
            var itemtype = item.type;
            if( self.type != "default" ) {
                itemtype = self.type.uri;
            }
			switch (itemtype) {
				case "http://www.newmedialab.at/fcp/NewsItem":
					$.Search.ArticleItem.deploy(single_result,item);
					break;
				case "http://www.kiwi-project.eu/kiwi/core/Image":
					$.Search.ImageItem.deploy(single_result,item);
					break;
				case "http://www.newmedialab.at/fcp/BlogItem":
					$.Search.BlogItem.deploy(single_result,item);
					break;
                default:
					$.Search.DefaultItem.deploy(single_result,item);
			}
			
		});
    }
    
    this.populateFaccets = function(facets) {

        var self = this;
		$.each(facets, function(i, facet){
            var content = $("<p></p>");
            
            $.each(facet.values, function(i, value){
                content.append($("<a></a>").click(function() {
                    //function on selection
                    self.selectFacet(facet.uri,value.uri);
                }).text(value.title+"("+value.count+") "));
            });
            
            $("<div class='right_box'></div>").appendTo("#facets").append($("<h3></h3>").text(facet.title)).append(content);
        });
        
        if(facets.length == 0) {
            $("<div class='right_box'>"+this.messages.no_facets+"</div>").appendTo("#facets");
        }
    }
    
    this.populateTags = function(tags) {
        var self = this;
        var content = $("<p></p>");
		$.each(tags, function(i, tag){
            content.append($("<a></a>").click(function() {
                //function on selection
                self.selectTag(tag.title);
            }).text(tag.title+"("+tag.count+") "));
        });
        $("<div class='right_box'></div>").appendTo("#facets").append($("<h3></h3>").text('Tags')).append(content);
        
        if(tags.length == 0) {
            $("<div class='right_box'>"+this.messages.no_tags+"</div>").appendTo("#facets");
        }
    }
    
    this.populatePagination = function(data){
		// Keep the current page number for prev/next functionality
		
        this.currentPage = data.query.page;

		// Populate the UI
		var pagEl = $("#search_paging");
		var h = "";
        
		// Compute page count, obviously rounding up
		var pageCount = Math.ceil(this.count / this.PAGE_SIZE);
		
		// If there is just one page, don't show any pagination
		if (pageCount == 1)
			return;

		// If we are not on the first page, show "previous" link
		if (data.query.page > 1) {
			h += "<a class='prev'>"+this.messages.previous+"</a> ";
		} else {
            h += "<span class='disabled'>"+this.messages.previous+"</span> ";
        }

		// Show 11 page numbers; ideally 5 before and 5 after current page
		var leftBoundary = data.query.page - 5;
		if (leftBoundary < 1)
			leftBoundary = 1;

		var rightBoundary = leftBoundary + 10;
		if (rightBoundary > pageCount)
			rightBoundary = pageCount;

		for (var i = leftBoundary; i <= rightBoundary; i++) {
			if (i > leftBoundary)
				h += " ";

			if (i != data.query.page) {
				h += "<a class='inactive'>" + i + "</a>";
			} else {
				h += "<a class='active'>" + i + "</a>";
			}

		}

		// If we are not on the last page, show "next" link
		if (data.query.page < pageCount) {
			h += " <a class='next'>"+this.messages.next+"</a> ";
		} else {
            h += " <span class='disabled'>"+this.messages.next+"</span> ";
        }

		pagEl.html(h);
		
		//show top paging
		$("#search_paging_top").html(this.messages.page+" "+this.currentPage+"/"+pageCount);
        
	}
    
    this.populateQuery = function(query) {
        var sf = $("<table></table>");
        
        if(query.string != "") sf.append("<tr><td class='title'>"+this.messages.searchtext+":</td><td id='searchtext_right' class='value'>"+query.string+"</td></tr>");
        if(query.type != 'default') sf.append("<tr><td class='title'>"+this.messages.type+":</td><td id='searchtext_right' class='value'>"+query.type.title+"</td></tr>");
        /*
        if(query.range != null) {
            sf.append("<tr><td class='title'>"+this.messages.start+":</td><td id='searchtext_right' class='value'>"+query.range.start+"</td></tr>");
            sf.append("<tr><td class='title'>"+this.messages.end+":</td><td id='searchtext_right' class='value'>"+query.range.end+"</td></tr>");
        }
        */
        $.each(query.facets, function(i, facet){
            var values = "";
            $.each(facet.values, function(i, value){
                values += value.title + " ";
            });
            sf.append("<tr><td class='title'>"+facet.title+":</td><td id='searchtext_right' class='value'>"+values+"</td></tr>");
        });
		
		sf.append(this.setTags());
        
        $("#selected_facets").append(sf);
    }

	/**
	 * Binds event handlers to DOM elements
	 *
	 */
	this.bindHandlers = function() {
		var self = this;

		// Submitting the form (via button or Enter key) initiates the search
		$(this.CS).find("form").submit(function(event){
            
			var query = $(self.CS).find("input[type='text']").attr("value");
           
			// clear by new search
            //TODO
			if (query == "") {
                self.clearResults();
                self.clear();
                return false;
			}
            
            self.currentPage = 1;
            
            self.query = query;
            self.initiateSearch();
    
			// We actually don't want to submit the form; we're doing Ajax
			return false;
		});

		// orderBy options handlers
		/*
        $(this.CS).find("td.orderBy a").click(function(event){
			self.changeOrderBy(event.target.title);
		});
        */

		// Pagination handlers

		var pagEl = $(this.CS).find("#search_paging");
		pagEl.click(function(event){
			window.scrollTo(0,0);
			var aEl = event.target;
            
            if (aEl.tagName != "A")
				return;

			if (aEl.className == "prev") {
				self.goToPage(self.currentPage - 1);

			} else if (aEl.className == "next") {
				self.goToPage(self.currentPage + 1);

			} else {
				var pageNum = aEl.textContent;
				self.goToPage(parseInt(pageNum));
			}
		});
        
	}


//------------------------------------------------------------------------------
//-------------------- SEARCH
//------------------------------------------------------------------------------

	/**
	 * Initiates search with given query
	 *
	 */
	this.initiateSearch = function() {
		this.showProgress();
		//$(this.CS).find("#single_results").show();

		var self = this;
		// TODO: Hook up onFailureSearch callback
        var facetString = "&facets=[";
        for( var i=0;i<this.facets.length;i++ ) {
            facetString += "'"
                + this.facets[i].name
                + "':'"
                + this.facets[i].value
                + "'";
            if(i+1 != this.facets.length) facetString += ",";
        }
        facetString += "]";
        
		var tagString = "&tags=[";
        for (var i = 0; i < this.tags.length; i++) {
			tagString += this.tags[i];
			if(i+1 != this.tags.length) tagString += ",";
		}
		tagString += "]";
		
        typeString = "";
        if( this.type != "default" ) typeString = "&type="+this.type.uri;
        
		var requestURL = this.ENDPOINT_URL
			+ "query=" + encodeURIComponent(this.query)
			+ "&pageSize=" + this.PAGE_SIZE
			+ "&page=" + this.currentPage
			+ "&orderBy=" + this.orderBy
            + facetString
			+ tagString
            + typeString
			+ "&jsonpCallback=?";
        
        //console.log(requestURL);

        //self.onSuccessSearch(self.tempData);

		//$.getJSON(requestURL,function(data){
		//	self.onSuccessSearch(data);
		//});
        $.ajax({url:requestURL, dataType:'json',timeout: 7000, success:function(data){self.onSuccessSearch(data)}, error:function(request, textStatus, errorThrown){self.onFailureSearch(textStatus, errorThrown)}});
	}

	/**
	 * Success callback for search
	 *
	 */
	this.onSuccessSearch = function(data) {

        this.clearResults();
		// TODO: remove console.log
		//console.log("Received search response:");
		//console.log(data);

		// Populate the UI with results (or no results)
		if (data.results.items.length > 0) {
			this.populate(data);
		} else {
			this.showNoResults();
		}
        
        this.hideProgress();
	}

	this.onFailureSearch = function(textStatus, errorThrown) {
    
        this.clearResults();
        
        if(textStatus == 'timeout') {
            this.hideProgress();
            alert("We are sorry, but we couldn't load your search results");
        } else {
            alert(errorThrown);
        }
	}

	/**
	 * Clears "search results" pane
	 */
	this.clearResults = function(){

        $("#type_tabs").empty();

        //clear query
        $("#selected_facets").empty();
        
        //clear pages
        $("#search_paging").empty();

		// Also clear pagination
		$("#single_results").empty();
        
        $("#facets").empty();
	}
    
    this.clear = function() {
        this.currentPage = 1;
        this.orderBy = "modified";
        this.type = "default";
        this.facets = [];
        this.tags = [];
    }

	/**
	 * Shows progress indication
	 */
	this.showProgress = function(){
		$(this.CS).find("#loading").show();
	}

	/**
	 * Hides progress indication
	 */
	this.hideProgress = function(){
		$(this.CS).find("#loading").hide();
	}

	/**
	 * Shows "no results" message
	 */
	this.showNoResults = function(){
		$("#single_results").html("<span id='no_result'>"+this.messages.no_result+"</span>");
	}


    /**
     *  tag values
     */
    this.selectFacet = function(name,value) {
        this.currentPage = 1;
        this.facets.push({'name':name,'value':value});
        this.initiateSearch();
    }
    
    this.selectTag = function(title) {
        var t = true;
		for(var i = 0; i < this.tags.length; i++) {
			if(this.tags[i] == title) {
				t = false;
                break;
			}
		}
        if(t){
            this.currentPage = 1;
            this.tags.push(title);
            this.initiateSearch();
        }
    }
    
    this.setType = function(title, uri) {
        this.currentPage = 1;
        this.type = {'uri':uri,'title':title};
        this.initiateSearch();
    }
	
	this.setTags = function() {
		
		var self = this;

		if(this.tags.length != 0) {
			var tags_str = $("<tr></tr>");
			var tag_values = $("<td class='value'></td>");
			var tag_title = $("<td class='title'></td>").text(this.messages.tags+":");
			for(var i = 0; i<this.tags.length; i++) {
				var tag = $("<span></span>").text(self.tags[i]);
				var link = $("<img />").attr('src','img/delete.png');
				link.attr('style','cursor:pointer');
                link.attr('alt',self.tags[i]);
				//var tt = self.tags[i];
				link.click(function(event){
					self.deleteTag(event.target);
				});
				tag_values.append(tag);
				tag_values.append(link);
			}
			
			tags_str.append(tag_title);
			tags_str.append(tag_values);
			
			return tags_str;
		} else return "";
	}
	
	this.deleteTag = function(img) {
        var tag = img.alt;
		var t = [];
		for(var i = 0; i < this.tags.length; i++) {
			if(this.tags[i] != tag) {
				t.push(this.tags[i]);
			}
		}
		this.tags = t;
		this.currentPage = 1;
        this.initiateSearch();
	}
    
	/**
	 * Initiates search for a specific page
	 */
	this.goToPage = function(page){
		// Change the page number, keep same query and other options
		this.currentPage = page;

		// Initiate search
		this.initiateSearch();
	}

	/**
	 * Changes the orderBy option
	 *
	 * @param	value	"ceq" or "modified"
	 *
	 */
	this.changeOrderBy = function(value) {
		// "deselect" all options
        /*
        $(this.CS).find("td.orderBy a").removeClass("selected");

		// highlight the user choice
		$(this.CS).find("td.orderBy a[title='" + value + "']").addClass("selected");

		// Perform search; change the orderBy value, start with page 1,
		// but the same query and other options
		this.orderBy = value;
		this.currentPage = 1;
		this.initiateSearch();
        */
	}

	/**
	 * Populates filters UI
	 */
	this.populateFilters = function(relatedObj){
       
	}
    
    this.changeType = function(type) {
        //TODO
        //$.each(, function(i, item){}
    }

	/**
	 * Builds initial UI
	 *
	 */
	this.deploy = function(){
        
        //check out url params
        var q = $.getUrlVar('query');

		// Create basic DOM skeleton
		$(this.CS).append(this.template_widget, {});

		// Bind DOM event handlers
		this.bindHandlers();

		// Put focus into text field
		$(this.CS).find("input[type='text']").focus();
        
        if(q != null) {
            $(this.CS).find("input[type='text']").attr("value",q);
            $(this.CS).find("form").submit();
        }
	}

  };
})(jQuery);

/**
 * get URL params
 */
$.extend({
    getUrlVars: function(){
        var vars = [], hash;
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        for(var i = 0; i < hashes.length; i++) {
            hash = hashes[i].split('=');
            vars.push(hash[0]);
            vars[hash[0]] = hash[1];
        }
        return vars;
    },
    getUrlVar: function(name){
        return jQuery.getUrlVars()[name]
    }
});