/*
 *Require:
 *- jquery.js - 1.2.6 and greater
 *- jquery.template.js
 *- tags_editor_templates.js
 *
 *
 *Usage:
 * jQuery.noConflict();
 * jQuery.listing_table = new jQuery.SimpleListing("listing_table");
 * jQuery.listing_table.deploy();
 *
 *
 * TODOs:
 * - style for a button?
 * - how to fill in selects before "analyze me" service is invoked?
 * - is web service "imune" to adding a duplicate tag?
 * - Q: Not sure about controlled=0:1 in response of GetTags.
 *     Is it that controlled==0 is a "free tag", while controlled==1 implies
 *	   a required category?
 * - Remove all console.log


 */
(function( $ ) {

//    $.SimpleListing = function(container_id){
  $.TagsEditor = function(CS){
//--------CONSTANTS

	this.ENDPOINT_TAGGING = "http://kiwi.sunsolutioncenter.de/KiWi/seam/resource/services/widgets/tagging/";
	this.FUNC_ADDTAGS = "addTags";
	this.FUNC_GETTAGS = "listTags.json";
	this.FUNC_REMTAGS = "removeTags";
	
	this.ENDPOINT_TAG_EXTRACT = "http://kiwi.sunsolutioncenter.de/KiWi/seam/resource/services/ie/tagExtraction/";
	this.FUNC_GET_LIST_REQ = "listOfRequiredTags";
	this.FUNC_EXTRACT_TAX_TAGS = "extractTaxonomyTags";

	this.MAX_SCORE				= 300.0;

	// Tags with MAX_SCORE get this size in "em"
    this.MAX_TAG_FONT_SIZE      = 6;

	// Tags with low score resulting with display size lower than specified
	// below are not shown in the UI (again, in "em")
	this.MIN_TAG_FONT_SIZE		= 0.5;
	
//--------MEMBER VARS
	this.CS = CS; //container selector
//	this.RESOURCE_URI = "http://localhost/content/7e366380-b147-455e-b2ab-5f8f1cd99b6c";//uri of the resource to be tagged...
//        this.RESOURCE_URI = "http://localhost/content/0b5ae249-b42a-4e99-9c9f-ad9dd1fccd23";
//        this.RESOURCE_URI = "http://kiwi.sunsolutioncenter.de/KiWi/content/65834035-935c-4d78-9fe2-e544190d1bff";
	this.RESOURCE_URI = "http://kiwi.sunsolutioncenter.de/KiWi/content/4bc5290e-1062-4264-b198-c9f364a3baae";

	// templates
	this.template_widget = $.template(TEMPLATE_TAGS_EDITOR_WIDGET);

	// Map of "tag" labels to DOM elements representing editable free tags
	this.freeTags = {};
	// Map of "tag" labels to DOM elements representing recommended tags in tag cloud
	this.optionalTags = {};
	// Map of a category label to a tag object, may be null
	this.currentCategoryValues = {};
	// Map of required category option URIs to tag objects and option elements
	this.requiredTags = {};

//------------------------------------------------------------------------------
//-------------------- AJAX queue
//------------------------------------------------------------------------------
	this.ajaxQueue = [];

	/**
	 * Invokes a function in a "concurrency-safe" way; i.e. if there is a pending
	 * asynchronous request, the function is not called but stored in a queue,
	 * until ajaxDone is called in a callback.
	 *
	 * Adding and Removing a tag needs to be done in this way; otherwise, e.g.
	 * a click on a tag in tag cloud followed by immediate click on the same tag
	 * might end up in an inconsistent state on the server (Remove might be performed
	 * before Add on the server)
	 */
	this.safeInvoke = function(f){
		this.showProgress();

		// Put the function into the queue
		this.ajaxQueue.push(f);

		// If function f is the only element in the queue, invoke it immediately
		// (and wait for the callback to remove it from the queue)
		if (this.ajaxQueue.length == 1) {
			f.apply(this);
		} else {
//			console.log("Queuing only");
		}
	}

	/**
	 * Needs to be called in a callback of an AJAX request sent via
	 * safeInfoke. This performs other calls in sequential way until
	 * the queue is empty
	 */
	this.ajaxDone = function(){
		this.hideProgress();
		
//		console.log("Ajax done");
		// Remove current function from the queue; and ...
		this.ajaxQueue.shift();

		// ... if there is something more in the queue, invoke it
		if (this.ajaxQueue.length > 0) {
//			console.log("Invoking more ... deferred");
			var f = this.ajaxQueue[0];
			f.apply(this);
		}
	}

	this.progressSemaphor = 0;

	/*
	 * Shows progress indication on this widget;
	 *
	 * Actually works as a semaphor; i.e. need to call as many times hideProgress
	 * as showProgress to effectively hide the progress indication.
	 *
	 * This is useful as there are potentially two non-coordinated background
	 * efforts. First, the queued Add/Remove invocations; and then
	 * loading data (initial, or recommended tags)
	 */
	this.showProgress = function(){
		$(this.CS).find("div.tagsEditor_loading").show();
		this.progressSemaphor++;
	}

	/**
	 * Hides progress indication on this widget
	 */
	this.hideProgress = function(){
		this.progressSemaphor--;
		if (this.progressSemaphor == 0)
			$(this.CS).find("div.tagsEditor_loading").hide();
	}

//------------------------------------------------------------------------------
//-------------------- TAGS
//------------------------------------------------------------------------------

	/**
	 * Invokes GetTags web service and populates UI with tags
	 */
	this.fetchTags = function(){
		this.showProgress();

		// If we don't have live data, invoke a web service
		var self = this;
		if (!this.testData) {
			$.ajax({
						url: self.ENDPOINT_TAGGING + self.FUNC_GETTAGS + "?resource=" + self.RESOURCE_URI,
						dataType: "jsonp",
						jsonp: "jsonpCallback",
						success: function(data){
							self.onSuccessFetchTags(data);
						},
						error: function(xhr, textStatus, errorThrown){
							self.onFailureFetchTags(xhr, textStatus, errorThrown);
						}
			});
		}
		// Otherwise, simulate invocation with test data; call success callback
		// in a deferred way
		else {
			window.setTimeout(function(){
				self.onSuccessFetchTags(self.testData);
			}, 800);
		}
	}

	/**
	 * Success callback for GetTags web service invocation.
	 * Starts populating UI if we have all necessary data
	 */
	this.onSuccessFetchTags = function(data){
		this.hideProgress();

		this.tags = data;

		// Only when we got both - tags assigned to this document and
		// data for the required categories, we can populate the UI
		if (this.tags && this.requiredCategoryOptions) {
			this.buildCategoryOptions();
			this.buildFreeTags();
			this.setRequiredCategoryValues();
		}
	}
	
	this.onFailureFetchTags = function(xhr, textStatus, errorThrown){
		this.ajaxDone();
		alert("Error getting tags:" + xhr.responseText);
	}

	/**
	 * Returns true, if given tag belongs to a required category
	 *
	 * @param	tag		A tag object, with a property "uri"
	 *
	 */
	this.isRequiredCategoryTag = function(tag) {
		return this.requiredTags[tag.uri] != null;
	}

	/**
	 *
	 */
	this.invokeAddTag = function(tagsString) {
		var self = this;

		this.safeInvoke(function(){
			// If we are not in test mode, invoke a web service
			if (!self.testAddRemove) {
				$.ajax({
					url: self.ENDPOINT_TAGGING + self.FUNC_ADDTAGS + "?resource=" + self.RESOURCE_URI + "&tags=" + tagsString,
					dataType: "jsonp",
					jsonp: "jsonpCallback",
					success: function(data){
						self.onSuccessAddTag(data);
					},
					error: function(xhr, textStatus, errorThrown){
						self.onFailureAddTag(xhr, textStatus, errorThrown);
					}
				});
			}
			// otherwise, just dummy deferred
			else {
				console.log("TODO:Would invoke AddTag webservice with tags=" + tagsString);
				window.setTimeout(function(){
					self.onSuccessAddTag();
				},500);
			}
		});
	}

	/**
	 *
	 *
	 */
	this.invokeRemoveTag = function(tagsString) {
		var self = this;
		this.safeInvoke(function(){

			// If we are not in test mode, invoke a web service
			if (!self.testAddRemove) {
				$.ajax({
					url: self.ENDPOINT_TAGGING + self.FUNC_REMTAGS + "?resource=" + self.RESOURCE_URI + "&tags=" + tagsString,
					dataType: "jsonp",
					jsonp: "jsonpCallback",
					success: function(data){
						self.onSuccessRemoveTag(data);
					},
					error: function(xhr, textStatus, errorThrown){
						self.onFailureRemoveTag(xhr, textStatus, errorThrown);
					}
				});
			}
			// otherwise, just dummy deferred
			else {
				console.log("TODO: Would invoke RemoveTag webservice with tags=" + tagsString);

				window.setTimeout(function(){
					self.onSuccessRemoveTag();
				},500);
			}
		});
	}


//------------------------------------------------------------------------------
//-------------------- FREE TAGS
//------------------------------------------------------------------------------

	/**
	 * An initialization method; binds DOM event handlers to "free tags" UI;
	 * this includes:
	 * - (x) buttons to remove a tag
	 * - Add button to add tags based on text field value
	 */
	this.bindFreeTagsHandlers = function(){
		var self = this;

		// Hookup event handlers for free tags removal
		var freeTagsContainer = $(this.CS).find(".tagsEditor_freeTags");
		freeTagsContainer.click(function(event){
			var el = $(event.target);
			if (el.attr("tagName") == "IMG") {
				var tagUri = $(el.parent()[0]).attr("taguri");
				var tag = el.parent()[0].firstChild.textContent;
				self.removeFreeTag(tag, tagUri);
				event.stopPropagation();
			}
		});

		// Hookup event handlers for free tags "Add" button
		$(this.CS).find(".tagsEditor_freeTagsEditor input[type='button']").click(function(event){		
			if($(this.CS).find(".tagsEditor_freeTagsEditor input[type='text']").attr("value")!=""){
				self.confirmFreeTagsEntry();
			}			
		});

		// Hookup event handleres for key events (Enter, Esc) in free tags text field
		$(this.CS).find(".tagsEditor_freeTagsEditor input[type='text']").keyup(function(event){
			if (event.keyCode == 13 && $(this.CS).find(".tagsEditor_freeTagsEditor input[type='text']").attr("value")!="") {
				self.confirmFreeTagsEntry();
			} else if (event.keyCode == 27) {
				self.cancelFreeTagsEntry();
			}
		});
	}

	/**
	 * Ends editing of tags in a text field; saves the new data in the back-end
	 * and closes the editing UI
	 */
	this.confirmFreeTagsEntry = function(){
		var tagsString = $(this.CS).find(".tagsEditor_freeTagsEditor input[type='text']").attr("value");

		// We are optimistic about the web service call; we'll invoke the
		// service, clear the UI and add the tags to the view immediately;
		// If the web service call fails (rare), we will show an error message
		// (and reloading the page will recover)

		// Populate free tags UI
		var tags = tagsString.split(",");
		var self = this;
		$.each(tags, function(i, tag){
			tag = $.trim(tag);
			self.buildFreeTag({label:tag});

			// If this tag is being added manually and it is also
			// in the tag cloud, we may need to make it "selected"
			if (self.optionalTags[tag]) {
				self.optionalTags[tag].addClass("selectedTag");
			}
		});

		this.invokeAddTag(tagsString);
		// Done with text-entry tags; clear the text field; switch the UI state
		this.cancelFreeTagsEntry();
	}

	/**
	 * Cancels the free tag key entry and hides the UI (the text field and Add button)
	 */
	this.cancelFreeTagsEntry = function(){
		$(this.CS).find(".tagsEditor_freeTagsEditor input[type='text']").attr("value", "");
		$(this.CS).find(".tagsEditor_freeTagsEditor").hide();
		$(this.CS).find("A.tagsEditor_addTags").show();
	}

	/**
	 * Populates UI with free tags
	 */
	this.buildFreeTags = function() {
		$(this.CS).find(".tagsEditor_freeTags").empty();
		var self = this;
		$.each(this.tags.items, function(i, tag){
			if (!self.isRequiredCategoryTag(tag))
				self.buildFreeTag(tag);
		});
	}

	this.buildFreeTag = function(tag) {
		// Ignore, if we already have this tag in the UI
		if ((this.freeTags[tag.label]) || (tag.label == ""))
			return;

		// Create element and add it to the DOM
		var tagEl = $(document.createElement("SPAN"));
		tagEl.attr("tagUri", tag.uri);
		tagEl.html(tag.label + "<img src='./img/x.gif' /> ");
		//tagEl.html(tag.label + "<a>(x)</a> ");

		if (tag.controlled == 1) {
			tagEl.addClass("tagsEditor_controlled");
		}

		var freeTagsContainer = $(this.CS).find(".tagsEditor_freeTags");
		freeTagsContainer.append(tagEl);

		// Keep reference to the DOM element
		this.freeTags[tag.label] = tagEl;
	}

	/**
	 * Adds a tag based on a tag object. Adds the tag in the backend and in the UI
	 *
	 * Is called when user selects a tag in the tag cloud
	 */
	this.addFreeTag = function(tag){
		this.buildFreeTag(tag);

		this.invokeAddTag(tag.label);
	}

	/**
	 * Success callback for AddTag; does not need to do anything
	 * (besides working with the AJAX queue)
	 */
	this.onSuccessAddTag = function(){
		this.ajaxDone();
	}

	/**
	 * Failure callback for AddTag web service invocation
	 *
	 */
	this.onFailureAddTag = function(xhr, textStatus, errorThrown){
		this.ajaxDone();
		alert("Ooops, we failed to save your new tags:"  + xhr.responseText);
	}

	/**
	 * Removes a tag. Removes it from the UI and in the backend.
	 */
	this.removeFreeTag = function(tag, tagUri) {
		// Remove the free tag from the UI aggressively;
		if (this.freeTags[tag]) {
			this.freeTags[tag].remove();
			// Also remote the reference to removed DOM element
			delete this.freeTags[tag];
		}

		if (this.optionalTags[tag]) {
			this.optionalTags[tag].removeClass("selectedTag");
		}

		// We are optimistic about the webservice not failing;
		// If it fails (rarely), we'll show an error message; reloading the page
		// would fix the problem
		this.invokeRemoveTag(tagUri);
	}

	/**
	 * Does not need to do much ...
	 *
	 */
	this.onSuccessRemoveTag = function(){
		this.ajaxDone();
	}

	/**
	 * Shows an error message
	 *
	 */
	this.onFailureRemoveTag = function(xhr, textStatus, errorThrown){
		this.ajaxDone();
		alert("Ooops, we failed to remove a tag:" + xhr.responseText);
	}

//------------------------------------------------------------------------------
//-------------------- REQUIRED TAGS
//------------------------------------------------------------------------------

	/**
	 * Invokes a web service that returns the list of allowed tags in
	 * required categories and populates the UI (pulldowns)
	 */
	this.fetchRequiredCategoryOptions = function() {
		this.showProgress();

		var self = this;
		// If we don't have test data, invoke a web service'
		if (!this.testDataRequiredCategories) {
			$.ajax({
				url: self.ENDPOINT_TAG_EXTRACT + self.FUNC_GET_LIST_REQ + "?uri=" + self.RESOURCE_URI,
				dataType: "jsonp",
				jsonp: "jsonpCallback",
				success: function(data){
					self.onSuccessFetchRequiredCategoryOptions(data);
				},
				error: function(xhr, textStatus, errorThrown){
					self.onFailureFetchRequiredCategoryOptions(xhr, textStatus, errorThrown);
				}
			});
		}
		// Otherwise, push in the test data in a deferred way
		else {
			window.setTimeout(function(){
				self.onSuccessFetchRequiredCategoryOptions(self.testDataRequiredCategories);
			}, 600);
		}
	}

	/**
	 * Success handler for web service call to retrieve possible
	 * values of required categories
	 */
	this.onSuccessFetchRequiredCategoryOptions = function(data) {
		this.hideProgress();
		this.requiredCategoryOptions = data.required;

		// Only when we got both - tags assigned to this document and
		// data for the required categories, we can populate the UI
		if (this.tags && this.requiredCategoryOptions) {
			this.buildCategoryOptions();
			this.buildFreeTags();
			this.setRequiredCategoryValues();
		}
	}
	
	this.onFailureFetchRequiredCategoryOptions = function(xhr, textStatus, errorThrown){
		this.ajaxDone();
		alert("Error getting list of possible required categories: " + xhr.responseText);
	}

	/**
	 * Builds a set of SELECT compoments based on the required tags.
	 * One select per each required category.
	 *
	 * May be called repeatedly; subsequent calls clear the component and
	 * start over.
	 */
	this.buildCategoryOptions = function(){
		var requiredTagsContainer = $(this.CS).find(".tagsEditor_requiredTags");
		requiredTagsContainer.empty();

		// Reset map of required tag URIs to OPTION elements
		this.requiredTags = {};

		var self = this;
		// For each category, build one select element
		$.each(this.requiredCategoryOptions, function(i, category){
			var selectEl = document.createElement("SELECT");
			selectEl.title = category.label;
			selectEl.id = "SELECT_" + category.label; 

			// Every select has a blank option ... (at least initially)
			selectEl.options[0] = new Option("", "");

			// ... and a set of options with allowed tags
			$.each(category.narrower, function(j, tag){
				var optionEl = new Option(tag.label, tag.uri);
				selectEl.options[selectEl.options.length] = optionEl;
				self.requiredTags[tag.uri] = {
					tag: tag,
					el: optionEl
				}
			});

			// When all is done, attach to the DOM
			requiredTagsContainer.append("<label for=\"" + selectEl.id + "\">" + category.label + "</label>&nbsp;");
			requiredTagsContainer.append(selectEl);
			requiredTagsContainer.append("<br>");
		});
	}

	/**
	 * Sets currently selected values in required categories (pulldowns)
	 * based on data retrieved from GetTags web service
	 */
	this.setRequiredCategoryValues = function() {
		// Go through all tags belonging to this document;
		var self = this;
		$.each(this.tags.items, function(i, tag){
			// If a tag is in a required category, find the respective
			// OPTION element and set it as currently selected in its parent SELECT
			if (self.isRequiredCategoryTag(tag)) {
				var optionEl = self.requiredTags[tag.uri].el;
				var selectEl = optionEl.parentNode;
				selectEl.value = optionEl.value;

				// Keep reference to the currently selected tag object per each category
				self.currentCategoryValues[selectEl.title] = tag;

				// Also, since this required category already has a value
				// remove the blank option - i.e. don't let user select
				// a blank option again
				if (selectEl.options[0].value == "") {
					selectEl.remove(0);
				}
			}
		});
	}

	/**
	 * Binds DOM event handlers to the pulldowns representing required
	 * categories.
	 *
	 * If user selects different option, we'll remove the current tag
	 * and add the newly selected one
	 */
	this.bindRequiredTagsHandlers = function(){
		var self = this;
		$(this.CS).find(".tagsEditor_requiredTags").bind("change", function(event){
			var selectEl = event.target;
			self.saveSelectedValue(selectEl);
		});

		$(this.CS).find(".tagsEditor_requiredTags").bind("keypress", function(event){
			var selectEl = event.target;
			self.saveSelectedValue(selectEl);
		});
	}

	this.setCategoryTag = function(uri) {
		var tag = this.requiredTags[uri];
		if (!tag)
			return;

		// Select it in the UI
		var optionEl = tag.el;
		var selectEl = optionEl.parentNode;
		selectEl.value = optionEl.value;

		// Also, since this required category already has a value
		// remove the blank option - i.e. don't let user select
		// a blank option again
		if (selectEl.options[0].value == "") {
			selectEl.remove(0);
		}


		// And save to the back-end
		this.saveSelectedValue(selectEl);

	}

	/**
	 * Removes the old tag and adds the newly selected tag in given SELECT
	 * element
	 */
	this.saveSelectedValue = function(selectEl) {
		// Lookup the previously selected tag for this category
		var oldTag = this.currentCategoryValues[selectEl.title];

		// Lookup the newly selected tag (by URI)
		var newTag = null;
		var newTagPairObject = this.requiredTags[selectEl.value];
		if (newTagPairObject) {
			newTag = newTagPairObject.tag;
		}

		// Keep reference to the newly selected tag in the map
		this.currentCategoryValues[selectEl.title] = newTag;

		// Proceed with backend changes only if new tag is different from
		// old tag; this is needed as this method may be called multiple times
		// in case of keypress

		// Old tag may be null -> need to save if new tag is not null
		if (!(
			((oldTag == null) && (newTag != null))
			|| ((oldTag != null) && (newTag != null) && (oldTag.uri != newTag.uri))
			))
		{
			return;
		}


		if (oldTag)
			this.invokeRemoveTag(oldTag.uri);

		if (newTag)
			this.invokeAddTag(newTag.label);

		// Also, if we have just selected a value remove the blank value
		// from options
		if (selectEl.options[0].value == "") {
			selectEl.remove(0);
		}
	}

//------------------------------------------------------------------------------
//-------------------- RECOMMENDED TAGS
//------------------------------------------------------------------------------

	/**
	 * Invokes a web service and populates the tag cloud with recommended tags
	 * in a callback;
	 *
	 * also sets tags into required categories if they had a blank value.
	 *
	 */
	this.fetchRecommendedTags = function(){
		this.showProgress();

		var self = this;

		// If we don't have test data, invoke a web service ...
		if (!this.testDataTagExtract) {
			$.ajax({
				url: self.ENDPOINT_TAG_EXTRACT + self.FUNC_EXTRACT_TAX_TAGS + "?uri=" + self.RESOURCE_URI,
				dataType: "jsonp",
				jsonp: "jsonpCallback",
				success: function(data){
					self.onSuccessFetchRecommendedTags(data);
				},
				error: function(xhr, textStatus, errorThrown){
					self.onFailureFetchRecommendedTags(xhr, textStatus, errorThrown);
				}
			});
		}
		// ... otherwise set the test data in
		else {
			self.onSuccessFetchRecommendedTags(self.testDataTagExtract);
		}
	}

	/**
	 * Web service success callback
	 *
	 */
	this.onSuccessFetchRecommendedTags = function(data){
		this.hideProgress();
		$(this.CS).find(".tagsEditor_tagCloud").show();

		// Option 1: Values in pulldowns are automatically adjusted after recommendation
		this.adjustRequiredCategoryValues(data);

		// Option 2: Recommended categories are only shown above tag cloud
		//this.buildRecommendedCategories(data);

		this.buildRecommendedTags(data);
	}

	this.onFailureFetchRecommendedTags = function(xhr, textStatus, errorThrown){
		this.ajaxDone();
		alert("Error extracting tags from the document: " + xhr.responseText);
	}

	/**
	 * Adjusts tags in required categories, if they are blank,
	 * based on the recommendations.
	 *
	 * If user has previously selected a value, that is different from
	 * the recommended one, a hint is shown.
	 */
	this.adjustRequiredCategoryValues = function(data){
		var self = this;
		// For each category, find SELECT with matching title
		// and possibly adjust its value
		$.each(data.required, function(i, category){

			var selectEl = $(this.CS).find(".tagsEditor_requiredTags")
				.find("SELECT[title='" + category.label + "']");

			// There should be exactly one SELECT element per each category;
			// if not, it's a system-error; not much to do about it on the UI
			// level
			if (selectEl.length == 1) {
				selectEl = selectEl[0];

				// Find the recommended tag for this category
				// i.e. the one with highest score
				var maxScore = -1;
				var maxScoreValue = null;
				$.each(category.narrower, function(j, tag){
					if (maxScore < tag.score) {
						maxScore = tag.score;
						maxScoreValue = tag.uri;
					}
				});
				//The recommended value is selected everytime + fadeOut/set/fadeIn effect.
				if (maxScoreValue) {
					$(selectEl).animate({opacity: 0.1},500,function(){
						selectEl.value = maxScoreValue;
						self.saveSelectedValue(selectEl);						
						$(selectEl).animate({opacity: 1},500);
					});
				};			
			}
		});
	}

	this.buildRecommendedCategories = function(data) {
		// Clear the UI
		var requiredTagsContainer = $(this.CS).find(".tagsEditor_tagCloud div.tagsEditor_required div");
		requiredTagsContainer.empty();

		var tagsCount = 0;
		var self = this;
		// For each category, find the one tag with highest score (recommended)
		$.each(data.required, function(i, category){

			var selectEl = $(self.CS).find(".tagsEditor_requiredTags")
				.find("SELECT[title='" + category.label + "']");

			// There should be exactly one SELECT element per each category;
			// if not, it's a system-error; not much to do about it on the UI
			// level
			if (selectEl.length == 1) {
				selectEl = selectEl[0];


				// Find the recommended tag for this category
				// i.e. the one with highest score
				var maxScore = -1;
				var maxScoreTag = null;
				$.each(category.narrower, function(j, tag){
					if (maxScore < tag.score) {
						maxScore = tag.score;
						maxScoreTag = tag;
					}
				});

				// If the current value in the category is different from
				// the recommended one, show a hint
				if (selectEl.value != maxScoreTag.uri) {
					var tagEl = $(document.createElement("A"));
					tagEl.html(category.label + ": " + maxScoreTag.label);
					tagEl.attr("title", maxScoreTag.uri);
					requiredTagsContainer.append(tagEl);
					tagsCount++;
				}

			}
		});

		if (tagsCount > 0)
			$(this.CS).find(".tagsEditor_tagCloud div.tagsEditor_required").show();

	}

	/*
	 * Builds a tag cloud based on optional tags.
	 *
	 * May be called repeatedly; subsequent calls clear the component and
	 * start over.
	 */
	this.buildRecommendedTags = function(data){

		// Clear the UI and the internal map
		var optionalTagsContainer = $(this.CS).find(".tagsEditor_tagCloud div.tagsEditor_optional div");
		optionalTagsContainer.empty();
		this.optionalTags = {};

		// Compute average score and max score
		var totalScore = 0;
		var avgScore = 0;
		var maxScore = 0;
		$.each(data.optional, function(i, tag){
			totalScore += tag.score;

			if (tag.score > maxScore)
				maxScore = tag.score;
		});
		avgScore = totalScore / data.optional.length;
		
		// Compute average deviation of tags higher than average
		var sumDeviation = 0;
		var countDeviation = 0;
		$.each(data.optional, function(i, tag){
			if (tag.score > avgScore) {
				sumDeviation += tag.score - avgScore;
				countDeviation++;
			}
		});
		var avgDeviation = sumDeviation / countDeviation;

		// If the maximal score is 4x higher than average score + average deviation,
		// let the maximal score be average score + 4x avg_deviation
		if (maxScore > avgScore + 4*avgDeviation) {
			maxScore = avgScore + 4*avgDeviation;
		}

		var tagsCount = 0;
		var self = this;
		// For each tag, build one "optional" tag UI
		$.each(data.optional, function(i, tag){
			// Compute the font size for this tag;
			// effectively only show tags with sizes above threshold
			var fontSize = 0;

			// If the tag score is lower than the above computed and limited max
			// compute proportional size;
			if (tag.score < maxScore) {
				fontSize = tag.score / maxScore * self.MAX_TAG_FONT_SIZE;
			} 
			// otherwise, just use max size
			else {
				fontSize = self.MAX_TAG_FONT_SIZE;
			}

			if (fontSize >= self.MIN_TAG_FONT_SIZE) {
				var tagEl = $(document.createElement("A"));
				tagEl.attr("tagUri", tag.uri);
				if (tag.controlled == 1) {
					tagEl.addClass("tagsEditor_controlled");
				}

				tagEl.html(tag.label);
				tagEl.css("font-size", fontSize + "em");
				optionalTagsContainer.append(tagEl);
				optionalTagsContainer.append(" ");

				// Keep reference to the DOM element in a map
				self.optionalTags[tag.label] = tagEl;

				tagsCount++;
			}
		});

		if (tagsCount > 0)
			$(this.CS).find(".tagsEditor_tagCloud div.tagsEditor_optional").show();
	}

	/**
	 * Binds DOM event handlers for the tag cloud
	 *
	 */
	this.bindRecommendedTagsHandlers = function(){
		var self = this;

		// Hookup event handler for recommended categories
		var categoriesContainer = $(this.CS).find(".tagsEditor_tagCloud div.tagsEditor_required div");
		categoriesContainer.click(function(event){
			// URI of the recommended required tag is stored in title
			var uri = event.target.title;
			self.setCategoryTag(uri);

			$(event.target).remove();

			if (categoriesContainer[0].childElementCount == 0) {
				$(self.CS).find(".tagsEditor_tagCloud div.tagsEditor_required").hide();
			}
		});

		// Hookup event handler for tag cloud
		var optionalTagsContainer = $(this.CS).find(".tagsEditor_tagCloud div.tagsEditor_optional div");
		optionalTagsContainer.click(function(event){
			var el = $(event.target);
			el.toggleClass("selectedTag");

			var tag = {
				label: el[0].textContent,
				controlled: el.hasClass("tagsEditor_controlled")
			}

			if (el.hasClass("selectedTag")) {
				self.addFreeTag(tag);
			} else {
				var tagUri = el.attr("taguri");
				self.removeFreeTag(tag.label, tagUri);
			}
		});
	}


//------------------------------------------------------------------------------
//-------------------- DEPLOY
//------------------------------------------------------------------------------


	/**
	 * Disables ability to select and drag text in a DOM element.
	 * This is useful to disable in tag cloud where a subsequent click
	 * on a tag may result in unwanted text selection
	 *
	 * Courtesy of:
	 * http://www.dynamicdrive.com/dynamicindex9/noselect.htm
	 */
	this.disableSelection = function(target){
		if (typeof target.onselectstart!="undefined") //IE route
			target.onselectstart=function(){return false}
		else if (typeof target.style.MozUserSelect!="undefined") //Firefox route
			target.style.MozUserSelect="none"
		else //All other route (ie: Opera)
			target.onmousedown=function(){return false}
			target.style.cursor = "default"
	}

	/**
	 * Builds the DOM of this component and initiates data fetching
	 *
	 */
	this.deploy = function(){
		this.disableSelection($(this.CS)[0]);


		// Create basic DOM skeleton
		$(this.CS).append(this.template_widget, {});

		// Hook up handlers for Add Tags and Recommend Tags links
		var self = this;
		$(this.CS).find("A.tagsEditor_addTags").click(function(event){
			$(self.CS).find("A.tagsEditor_addTags").hide();
			$(self.CS).find(".tagsEditor_freeTagsEditor").show();
			$(self.CS).find(".tagsEditor_freeTagsEditor input[type='text']").focus();
		});
		$(this.CS).find("A.tagsEditor_recommendTags").click(function(event){
			$(self.CS).find("A.tagsEditor_recommendTags").hide();
			self.fetchRecommendedTags();
		});

		// Hookup some more DOM event handlers
		this.bindFreeTagsHandlers();
		this.bindRecommendedTagsHandlers();
		this.bindRequiredTagsHandlers();

		// Start fetching initial data
		this.fetchRequiredCategoryOptions();
		this.fetchTags(this.RESOURCE_URI);
	}
	

  };
})(jQuery);