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

	// CONSTANTS & SETTINGS
//	this.ENDPOINT_URL = "http://kiwidev.sunsolutioncenter.de/KiWi/seam/resource/services/sun/search/query.json?";
//	this.ENDPOINT_URL = "http://kiwi.sunsolutioncenter.de/KiWi/seam/resource/services/sun/search/query.json?";
//	if (document.location.protocol =='file:'){
//		this.ENDPOINT_URL = "http://kiwi.sunsolutioncenter.de/KiWi/seam/resource/services/sun/search/query.json?";
//	}else{
//		this.ENDPOINT_URL = document.location.host + "/KiWi/seam/resource/services/sun/search/query.json?";
//	}
//	this.ENDPOINT_URL = "http://localhost:8080/KiWi/seam/resource/services/sun/search/query.json?";
	this.ENDPOINT_URL = endpointUrl;
	this.PAGE_SIZE = 10;

	// templates
	this.template_widget = $.template(TEMPLATE_SEARCH_WIDGET);

	//--------MEMBER VARS
	this.CS = CS; //container selector
	this.currentPage = 0;
	this.orderBy = "modified";


//------------------------------------------------------------------------------
//-------------------- general UI
//------------------------------------------------------------------------------

	/**
	 * Populates the search results pane with results
	 *
	 */
	this.populate = function(data) {
		var tableEl = $(this.CS).find("td.sunspace_search_columnmiddle table")[0];

		// For each data item, create one table row and populate it with a widget
		$.each(data.results.array, function(i, item){
			var rowEl = tableEl.insertRow(-1);

			switch (item.type) {
				case "contentTypeItem":
					$.Search.ContentItem.deploy(rowEl, item);
					break;
				case "personTypeItem":
					$.Search.PersonItem.deploy(rowEl, item);
					break;
				case "communityTypeItem":
					$.Search.CommunityItem.deploy(rowEl, item);
					break;
			}

			// And add a separator
			rowEl = tableEl.insertRow(-1);
			$(rowEl).html(TEMPLATE_SEARCH_Separator);
			
		});
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

			// Time for an easter egg? Doing it the Google way ...
			if (query == "") {
				// Although, in theory, you might want to just reset filters or
				// do nothing
				$(self.CS).find("input[type='text']").attr("value", "undefined");
			} else {
				// Set the query, start with page 1, but keep other options
				self.query = query;
				self.currentPage = 1;
				self.initiateSearch();
			}

			// We actually don't want to submit the form; we're doing Ajax
			return false;
		});

		// orderBy options handlers
		$(this.CS).find("td.orderBy a").click(function(event){
			self.changeOrderBy(event.target.title);
		});

		// Pagination handlers
		var pagEl = $(this.CS).find("td.sunspace_search_columnmiddle div.pagination");
		pagEl.click(function(event){
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
		this.clear();
		this.showProgress();
		$(this.CS).find("table").removeClass("initial");
		$(this.CS).find("tr.results").show();

		var self = this;
		// TODO: Hook up onFailureSearch callback
		var requestURL = this.ENDPOINT_URL
			+ "query=" + encodeURIComponent(this.query)
			+ "&pageSize=" + this.PAGE_SIZE
			+ "&page=" + this.currentPage
			+ "&orderBy=" + this.orderBy
			+ "&jsonpCallback=?";

		// TODO: Remove this console.log
		//console.log("Sending request:");
		//console.log(requestURL);


		$.getJSON(requestURL,function(data){
			self.onSuccessSearch(data);
		});
	}

	/**
	 * Success callback for search
	 *
	 */
	this.onSuccessSearch = function(data) {
		this.hideProgress();

		// TODO: remove console.log
		//console.log("Received search response:");
		//console.log(data);

		// Populate the UI with results (or no results)
		if (data.results.array.length > 0) {
			this.populate(data);
			this.populatePagination(data);
		} else {
			this.showNoResults();
		}
	}

	this.onFailureSearch = function() {
		this.hideProgress();
		alert("We are sorry, but we couldn't load your search results");
	}

	/**
	 * Clears "search results" pane; is called when user submits a new query
	 */
	this.clear = function(){
		var tableEl = $(this.CS).find("td.sunspace_search_columnmiddle table")[0];
		while (tableEl.rows.length > 0) {
			tableEl.deleteRow(0);
		}

		// Also clear pagination
		$(this.CS).find("td.sunspace_search_columnmiddle div.pagination").empty();
	}

	/**
	 * Shows progress indication
	 */
	this.showProgress = function(){
		$(this.CS).find("td.sunspace_search_columnmiddle div.noResultsContainer").hide();
		$(this.CS).find("td.sunspace_search_columnmiddle div.loadingContainer").show();
	}

	/**
	 * Hides progress indication
	 */
	this.hideProgress = function(){
		$(this.CS).find("td.sunspace_search_columnmiddle div.loadingContainer").hide();
	}

	/**
	 * Shows "no results" message
	 */
	this.showNoResults = function(){
		$(this.CS).find("td.sunspace_search_columnmiddle div.noResultsContainer").show();
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
		$(this.CS).find("td.orderBy a").removeClass("selected");

		// highlight the user choice
		$(this.CS).find("td.orderBy a[title='" + value + "']").addClass("selected");

		// Perform search; change the orderBy value, start with page 1,
		// but the same query and other options
		this.orderBy = value;
		this.currentPage = 1;
		this.initiateSearch();
	}

	/**
	 * 
	 */
	this.populatePagination = function(data){
		// Keep the current page number for prev/next functionality
		this.currentPage = data.results.page;

		// Populate the UI
		var pagEl = $(this.CS).find("td.sunspace_search_columnmiddle div.pagination");
		var h = "";

		// Compute page count, obviously rounding up
		var pageCount = Math.ceil(data.results.total / this.PAGE_SIZE);
		
		// If there is just one page, don't show any pagination
		if (pageCount == 1)
			return;

		// If we are not on the first page, show "previous" link
		if (data.results.page > 1) {
			h += "<a class='prev'>&#171;</a> ";
		}

		// Show 11 page numbers; ideally 5 before and 5 after current page
		var leftBoundary = data.results.page - 5;
		if (leftBoundary < 1)
			leftBoundary = 1;

		var rightBoundary = leftBoundary + 10;
		if (rightBoundary > pageCount)
			rightBoundary = pageCount;

		for (var i = leftBoundary; i <= rightBoundary; i++) {
			if (i > leftBoundary)
				h += ", ";

			if (i != data.results.page) {
				h += "<a>" + i + "</a>";
			} else {
				h += "<a class='current'>" + i + "</a>";
			}

		}

		// If we are not on the last page, show "next" link
		if (data.results.page < pageCount) {
			h += " <a class='next'>&#187;</a> ";
		}

		pagEl.html(h);
	}

	/**
	 * Populates filters UI
	 */
	this.populateFilters = function(relatedObj){
		var tableEl = $(this.CS).find("td.sunspace_search_columnright table");

		if (relatedObj.communities.length > 0) {
			var h = "";
			var i = 0;
			while (i < relatedObj.communities.length && i < 5) {
				var community = relatedObj.communities[i];
				h += "<a>" + community.name + "</a><br/>";
				i++;
			}

			if (i < relatedObj.communities.length) {
				h += "<a style='float:right'>more</a>";
			}

			var divEl = tableEl.find("div.communities");
			divEl.html(h);
			$(divEl[0].parentNode.parentNode).show();
		} else {
			//var divEl = tableEl.find("div.communities");
			//$(divEl[0].parentNode.parentNode).hide();
		}
	}

	/**
	 * Builds initial UI
	 *
	 */
	this.deploy = function(){

		// Create basic DOM skeleton
		$(this.CS).append(this.template_widget, {});

		// Bind DOM event handlers
		this.bindHandlers();

		// Put focus into text field
		$(this.CS).find("input[type='text']").focus();


		/*
		var tempData = {'queryStr':{'query':'peter'},
						'templates':{'contentTypeItem':'contentTypeItemTemplateUrl'},
						'results':
							{
							'order':'desc','total':465,'page':1,
							'array':[
								{
									'type':'contentTypeItem','title':'128dc1aa256',
									'url':'http://localhost/content/3dad3dbb-43d3-46a6-9c15-1df72f828950',
									'description':' dummy_text ',
									'author_name':'Peter.340',
									'author_url':'http://localhost/user/Peter.340',
									'rating':0.0,
									'iq':39.91771666666667,
									'views':3
								},
								{
									'type':'contentTypeItem','title':'ArtAround',
									'url':'http://users.fh-salzburg.ac.at/~fhs12575/ontology/',
									'description':' This ontology describes the <span class="highlight">KiWi</span> artaround ontology ',
									'author_name':'admin','author_url':'http://kiwidev.sunsolutioncenter.de/KiWi/user/admin',
									'rating':3.0,'iq':35.83493333333333,
									'views':6},

								{
									  "type"       :"personTypeItem",
									  "url"        :"http://simoneHabegger.com",
									  "avatar_url" :"./widgets/search/temp/UI_Code/img/sunspace_search_person.jpg",
									  "first_name" :"Simone",
									  "last_name"  : "Habegger",
									  "title"	   : "Project Manager, Community Driver",
									  "phone"      :"x79134",
									  "email"      :"simone.habegger@sun.com",
									  "ceq"        :500,
									  "peq"        :450,
									  "top_contributions":[
									  ]
								},

								{
									  "type"       :"personTypeItem",
									  "url"        :"http://simoneHabegger.com",
									  "avatar_url" :"./widgets/search/temp/UI_Code/img/sunspace_search_person.jpg",
									  "first_name" :"Simone",
									  "last_name"  : "Habegger",
									  "title"	   : "Project Manager, Community Driver",
									  "phone"      :"x79134",
									  "email"      :"simone.habegger@sun.com",
									  "ceq"        :500,
									  "peq"        :450,
									  "top_contributions":[
									  ]
								},

								{
								  "type"			:"communityTypeItem",
								  "url"				:"http://usprofessional.com",
								  "name"			:"US Professional Sevices West Community",
								  "title"			:"USPWEST",
								  "member_count"	: 270,
								  "admins":[
									{
									  "name":"John Doe"
									}
								  ],
								  "top_contributions" : [
								  ],
								  "top_contributor":{
									"name":"Charles Fortune",
									"url" :"http://charlesfortune.com"
								  },
								  "ceq"				:53,
								  "contribs_count"	:15
								},


								{'type':'contentTypeItem','title':'128dc1a9975','url':'http://localhost/content/f654bdbb-1814-4abf-a7b6-cb21ae6d8a91','description':' dummy_text ','author_name':'Peter.340','author_url':'http://localhost/user/Peter.340','rating':0.0,'iq':39.813916666666664,'views':3},
								{'type':'contentTypeItem','title':'128dcb5dae3','url':'http://localhost/content/530d58c5-0982-4f83-8e34-3f52fa8d8fa8','description':' dummy_text ','author_name':'Peter.707','author_url':'http://localhost/user/Peter.707','rating':0.0,'iq':39.79416666666667,'views':3},
								{'type':'contentTypeItem','title':'128dcb153d2','url':'http://localhost/content/30d3e407-0b86-40c0-bbfc-acdf5db15350','description':' dummy_text ','author_name':'Peter.707','author_url':'http://localhost/user/Peter.707','rating':0.0,'iq':39.75911666666667,'views':3},
								{'type':'contentTypeItem','title':'128dc93da74','url':'http://localhost/content/a9adf390-0b16-4761-9c45-ef6b6f41d06e','description':' dummy_text ','author_name':'Peter.340','author_url':'http://localhost/user/Peter.340','rating':0.0,'iq':39.759,'views':3},
								{'type':'contentTypeItem','title':'128dd28811a','url':'http://localhost/content/d17bd2e5-6028-4f48-a741-f29bf86fde9f','description':' dummy_text ','author_name':'Peter.340','author_url':'http://localhost/user/Peter.340','rating':0.0,'iq':39.71973333333333,'views':3},
								{'type':'contentTypeItem','title':'128dd1b457f','url':'http://localhost/content/b69428f9-2570-44ad-85f6-32d36a931ea4','description':' dummy_text ','author_name':'Peter.1231','author_url':'http://localhost/user/Peter.1231','rating':0.0,'iq':39.70433333333334,'views':3},
								{'type':'contentTypeItem','title':'128dcfef66f','url':'http://localhost/content/0c63a6a5-a445-49bc-b986-95000fffc53c','description':' dummy_text ','author_name':'Peter.340','author_url':'http://localhost/user/Peter.340','rating':0.0,'iq':39.692233333333334,'views':3},
								{'type':'contentTypeItem','title':'128dd1b640c','url':'http://localhost/content/4a482100-1336-4d76-b77b-4d273bb1ae6e','description':' dummy_text ','author_name':'Peter.1231','author_url':'http://localhost/user/Peter.1231','rating':0.0,'iq':39.6581,'views':3},
								{'type':'contentTypeItem','title':'128dcb19b26','url':'http://localhost/content/ce6bbc10-90d7-4d81-81a4-fb8cd059c1da','description':' dummy_text ','author_name':'Peter.707','author_url':'http://localhost/user/Peter.707','rating':0.0,'iq':39.65246666666667,'views':3}
							]}};
		this.populate(tempData);
		$(this.CS).find("tr.results").show();
		*/
		
		
		var relatedObj = {
			communities: [
				/*
				{
					"selected": true,        
					"name":"comm1",
					"key" :"comm1key",
					"URL" :"http://comm1url"
				},
				{
					"selected": true,        
					"name":"comm2",
					"key" :"comm2key",
					"URL" :"http://comm2url"
				},
				{
					"selected": true,        
					"name":"comm3",
					"key" :"comm3key",
					"URL" :"http://comm3url"
				},				{
					"selected": true,        
					"name":"comm4",
					"key" :"comm4key",
					"URL" :"http://comm4url"
				},				{
					"selected": true,        
					"name":"comm5",
					"key" :"comm5key",
					"URL" :"http://comm5url"
				},				{
					"selected": true,
					"name":"comm6",
					"key" :"comm6key",
					"URL" :"http://comm6url"
				}
				*/
			],
			people: [],
			tags: []
		}
		this.populateFilters(relatedObj);
	}

  };
})(jQuery);