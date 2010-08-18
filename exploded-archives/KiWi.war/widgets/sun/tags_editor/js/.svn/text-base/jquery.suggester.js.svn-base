(function( $ ) {
	$.Suggester = function(CS){
		var selectedResult = null;
		var selectedSuggestions = {};
		this.pendingRequest = false;
		///////////////
		// Private functions
		///////////////

		var startLoadingSuggestions = jQuery.proxy(function(){

			// TODO: Show "suggestions" box,
			// show progress indication

			if(this.pendingRequest){
				return;
			}
			this.pendingRequest = true;
			// And call "integrator-provided" method to start loading
			// suggestions
			this.loadingTimer = null;
			//Empty div with results
			this.resultsContainer.empty();
			this.resultsContainer.css("visibility","visible");
			//Show progress indication
			this.resultsContainer.append("<span class=\"progress_bar\">Please wait... <img src=\"img/wait.gif\" ></span> ");
			this.loadSuggestions(this.elTxt.value);
		},this);


		///////////////
		// Public interface
		///////////////

		/**
	 * Shows the suggester positioned below the given element
	 *
	 */
		this.show = function(options) {
			// If the component hasn't been initialized yet, do it
			if (this.el == null)
				this.init(options);

			// If "below" property was specified, position the component below
			// the specified element
			if (options && options.below) {
				var offset = $(options.below).offset();
				offset.top += 20;

				this.el.css("left", offset.left + "px");
				this.el.css("top", offset.top + "px");
				this.el.css("position", "absolute");
			}

			// And show it
			this.el.show();

			// Put focus into text field
			this.elTxt.focus();
		}

		/**
	 * Initialize DOM elements
	 */
		this.init = function(options){
			// Create basic structure, set references to some elements
			this.el = $(document.createElement("DIV"));
			this.el.addClass("suggester");
			this.el.html("<input type='text'>"
				+ "<input type='button' value='Add'>"
				+ "<div class=\"resultsContainer\"></div>");
			this.elTxt = this.el.children("input[type='text']")[0];
			//Selecting results container
			this.resultsContainer = this.el.children("div");
			// Initially hidden
			this.el.hide();

			// TODO: Hookup some event handlers; e.g.
			// 500ms pause after last keystroke should call
			// this.startLoadingSuggestions()
			$(this.elTxt).keydown(jQuery.proxy(function(e){
				//Getting number of results
				var childrenNumber = this.resultsContainer.children("div").length;
				//True HEIGHT of the each result. Top/bottom border + top/bottom margin = 2. We have to include them there, otherwise scolling would screw up.
				var trueResultHeight = parseInt(this.resultsContainer.children(1).css("height"))+2;
				//Number of result per container height - used for scrolling stuff.
				var resultsPerPage = Math.ceil( parseInt( this.el.children("div").css("height")) / trueResultHeight );

				//40 = down arrow, 38 = up arrow, 37 = left arrow, 39 = right arrow

				//65 = 'a' - temporary u can get suggestions only by 'a'-key
				if(e.keyCode==65){
					if(this.loadingTimer!=null){
						window.clearTimeout(this.loadingTimer);
					}
					this.loadingTimer = window.setTimeout(function(){
						startLoadingSuggestions()
						}, 500);

				}
				// key down
				else if(e.keyCode==40){
					if(selectedResult==null){
						//Selecting very first child
						this.resultsContainer.children("div").eq(0).addClass("highlighted");
						selectedResult = 0;
					}else if(selectedResult<childrenNumber-1){
						this.resultsContainer.children("div").eq(selectedResult).removeClass("highlighted");
						//$("#result"+selectedResult).removeClass("highlighted");
						selectedResult++;
						//$("#result"+selectedResult).addClass("highlighted");
						this.resultsContainer.children("div").eq(selectedResult).addClass("highlighted");

						//Scrolling of result container we will start scrolling half of the page from the top of the container div
						if(selectedResult+1 > resultsPerPage/2){
							this.el.children("div").scrollTop(trueResultHeight *(selectedResult-Math.floor(resultsPerPage/2)));
						}

					}
				}
				// key up
				else if(e.keyCode==38){
					if(selectedResult>0){
						this.resultsContainer.children("div").eq(selectedResult).removeClass("highlighted");
						selectedResult--;
						this.resultsContainer.children("div").eq(selectedResult).addClass("highlighted");

						//Scrolling of result container, we will start scrolling half of the page from the end of the container div
						if(selectedResult+1 < (childrenNumber-resultsPerPage/2) ){
							this.el.children("div").scrollTop(trueResultHeight *(selectedResult-Math.floor(resultsPerPage/2)));
						}
					}
				}
				// key left
				else if(e.keyCode==37 && selectedResult!=null && this.suggestions[selectedResult].controlled==1){
					e.preventDefault();
					startLoadingParents(selectedResult);
				}
				// key right
				else if(e.keyCode==39 && selectedResult!=null && this.suggestions[selectedResult].hasChildren==true){
					e.preventDefault();
					startLoadingChildren(selectedResult);
				}
				//spacebar
				else if(e.keyCode==32){
					e.preventDefault();
					//Prevents selecting if no result hasn't been selected
					if(selectedResult != null){
						this.selectSuggestion(this.suggestions[selectedResult].uri);
					}
				}
				//ESC key
				else if(e.keyCode==27){
					//Hiding resultsContainer and emptying him
					this.resultsContainer.css("visibility","hidden").empty();
				}
			},this));

			// Append to the document; or to a parent element if specified
			if (options && options.parent) {
				$(options.parent).append(this.el);
			} else {
				$(document.body).append(this.el);
			}
		}

		///////////////
		// Public interface - to be overriden by implementors
		///////////////

		/**
	 * This method is called by the compoment when it needs to load suggestions;
	 * integrators need to override this method
	 *
	 */
		this.loadSuggestions = function(txt){
			// This is dummy implementation;
			// In reality, this method should use AJAX to start fetching
			// data and call onLoadSuggestions in a success callback
			this.onLoadSuggestions([]);
		}

		/**
	 * This method should be called when the suggestions are ready
	 *
	 */
		this.onLoadSuggestions = function(suggestions){
			//Hiding progress indication
			this.el.children("div").empty();
			this.pendingRequest = false;
			this.suggestions = suggestions;
			this.fillTheDiv();
		}

		var startLoadingChildren = jQuery.proxy(function(selRes){
			if(this.pendingRequest){
				return;
			}
			this.pendingRequest = true;
			//Hide partents - EDIT:newly hiding parents in loadChildren function
			//var _child = $(this.el).find("div");
			//_child.empty();
			//Showing hidden waitin spinner
			this.resultsContainer.children("div").eq(selRes).children(".result_wait_icon").css("visibility","visible");
			this.loadChildren(selRes);
		},this);


		this.onLoadChildren = function(children){
			//After simulated 2s hide the parents
			this.el.find("div").empty();
			//Load children into the divs
			selectedResult =null;
			this.suggestions = children;
			this.fillTheDiv();
		}

		var startLoadingParents = jQuery.proxy(function(selRes){
			if(this.pendingRequest){
				return;
			}
			this.pendingRequest = true;
			//Hide children - EDIT:newly hiding children in loadParents function
			//var _child = $(this.el).find("div");
			//_child.empty();

			//Showing hidden waitin spinner
			//$("#result_wait_icon"+selRes).css("visibility","visible");
			this.resultsContainer.children("div").eq(selRes).children(".result_wait_icon").css("visibility","visible");
			this.loadParents(selRes);
		},this);


		this.onLoadParents = function(parents){
			//After simulated 2s hide the parents
			this.el.find("div").empty();
			//Load children into the divs
			selectedResult = null;
			this.suggestions = parents;
			this.fillTheDiv();
		}

		/**
		 * Function to be overriden by integrators;
		 * called by the component when a tag needs to be added
		 */
		this.addTag = function(){

		}

		/**
		 * Function to be overriden by integrators;
		 * called by the component when a tag needs to be removed
		 */
		this.removeTag = function(tagURI){

		}

		this.fillTheDiv = function(){
			//Feeding up resultsContainer
			for(var i=0;i<this.suggestions.length;i++){
				//If suggestion is controlled we also have to show 'prefix' along with 'label'
				if(this.suggestions[i].controlled==1){
					this.resultsContainer.append("<div class=\"result_item\">   <span class=\"left_arrow\"></span>   <span class=\"label\">"+this.suggestions[i].prefix+":"+this.suggestions[i].label+"</span>   <span class=\"result_wait_icon\"></span>   <span class=\"right_arrow\"></span>   </div>");
				}else{
					this.resultsContainer.append("<div class=\"result_item\">   <span class=\"left_arrow\"></span>   <span class=\"label\">"+this.suggestions[i].label+"</span>   <span class=\"result_wait_icon\"></span>   <span class=\"right_arrow\"></span>   </div>");
				}
				//Checking if the suggestion isn't already selected
				if(selectedSuggestions[this.suggestions[i].uri]){
					this.resultsContainer.children("div").eq(i).addClass("selected");
				}
//				for(var j = 0;j<selectedSuggestions.length;j++){
//					if(selectedSuggestions[j].uri == this.suggestions[i].uri){
//						this.resultsContainer.children("div").eq(i).addClass("selected");
//					}
//				}
				

				//IS CONTROLLED
				if(this.suggestions[i].controlled==1){
					this.resultsContainer.children("div").eq(i).addClass("isControlled");
					//If suggestion has a children, right arrow get visible condition via class
					if(this.suggestions[i].hasChildren==true){
						this.resultsContainer.children("div").eq(i).addClass("hasChildren");
					}
					//If suggestion has a parent, left arrow get visible condition via class
					if(this.suggestions[i].parent!=null){
						this.resultsContainer.children("div").eq(i).addClass("hasParent");
					}
				}
				//IS NOT CONTROLLED
				else{
					this.resultsContainer.children("div").eq(i).addClass("notControlled");
				}
			}
			//Hovering over results does highlighting
			$(".result_item").
			mouseover(function(){
				//Changing selectedResult - so the keyboard can properly react on hovered(highlighted) result
				selectedResult = $(this).parent().children("div").index(this);
				//In this case we can use this instead of mouseout() function
				$(this).parent().children().removeClass("highlighted");
				//Adding proper class
				$(this).addClass("highlighted");
			})
			//Assing some mouse control over label-span
			$(".label").click(function(){
				selectedResult = $(this).parent().parent().children("div").index($(this).parent());
				labelClicked();
			});
			//Assigning some mouse control over left arrow - loading parent
			$(".left_arrow").click(function(){
				selectedResult = $(this).parent().parent().children("div").index($(this).parent());
				startLoadingParents(selectedResult);
			});
			//Assigning some mouse control over right arrow - loading children
			$(".right_arrow").click(function(){
				selectedResult = $(this).parent().parent().children("div").index($(this).parent());
				startLoadingChildren(selectedResult);
			});
			this.pendingRequest = false;
		}

		this.selectSuggestion = function(uri){
			if(selectedSuggestions[uri]){
				this.onRemoveTag(uri);
				delete selectedSuggestions[uri];
				for(var i = 0;i<this.suggestions.length;i++){
					if(this.suggestions[i].uri == uri){
						this.resultsContainer.children("div").eq(i).removeClass("selected");												
						return;
					}
				}
			}else{
				for(var j = 0;j<this.suggestions.length;j++){
					if(this.suggestions[j].uri == uri){
						this.onAddTag(this.suggestions[selectedResult].label, uri);
						selectedSuggestions[uri] = true;
						this.resultsContainer.children("div").eq(j).addClass("selected");												
						return;
					}
				}
				
			}
//			//First search array with already selected suggestions
//			for(var i = 0;i<selectedSuggestions.length;i++){
//				//If suggestion has been already selected -> deselect it
//				if(selectedSuggestions[i].uri == this.suggestions[selectedResult].uri){
//					//Deselect it by removing proper class
//					//$("#result"+selectedResult).removeClass("selected");
//					this.resultsContainer.children("div").eq(selectedResult).removeClass("selected");
//					//Also remove it from selected suggestions array
//					selectedSuggestions.splice(i,1);
//					return;
//				}
//			}
//			//This point tell us that suggestion hasn't been selected yet
//			//So select it by adding proper class
//			//$("#result"+selectedResult).addClass("selected");
//			this.resultsContainer.children("div").eq(selectedResult).addClass("selected");
//			//Also we have to add selected suggestion to the array
//			selectedSuggestions.push(this.suggestions[selectedResult]);
		}

		this.setSelectedTags = function(tags){
			$.each(tags, function(index, tag){
				selectedSuggestions[tag.uri] = true;
			});
		}

		this.onRemoveTag = function(uri){
			//Dummy implementation?
		}

		this.onAddTag = function(label,uri){
			//Dummy implementation?
		}

		var labelClicked = jQuery.proxy(function(){
			// Put focus back into text field
			this.elTxt.focus();
			this.selectSuggestion(this.suggestions[selectedResult].uri);
		},this);
	};
})(jQuery);