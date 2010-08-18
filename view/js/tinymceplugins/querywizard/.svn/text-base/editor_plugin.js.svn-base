(function() {
	var each = tinymce.each;
	/**
	 * This plugin's job is to deal with 
	 *  <div kiwi:kind="query"
	 *        kiwi:query="QUERY_STRING"
	 *        kiwi:lang="QUERY_LANG"
	 *        kiwi:format="RESULT_FORMAT">
	 *  </div>
	 *  kind of links in the editor
	 *  
	 */

	tinymce.create('tinymce.plugins.QueryWizardPlugin', {
		/**
		 * Initializes the plugin, this will be executed after the plugin has been created.
		 * This call is done before the editor instance has finished it's initialization so use the onInit event
		 * of the editor instance to intercept that event.
		 *
		 * @param {tinymce.Editor} ed Editor instance that the plugin is initialized in.
		 * @param {string} url Absolute URL to where the plugin is located.
		 */
		init : function(ed, url) {

// ********************** begin plugin functionalities ******************			
			// **** LoadContent ****
			// on loading a text into the editor mark cover the kiwilinks with a gray
			// backgrounded A tag and a classname. 
			ed.onSetContent.add(function(ed, o) {
//				console.info("hook: onLoadContent");
				
				each(ed.dom.select('DIV'), function(n) {
					if(isQuery(n)){
						//ed.dom.addClass(n,'kiwiQuery');
						// ed.dom.addClass(n,'mceNonEditable');
						var format = n.attributes.getNamedItem("kiwi:format").value;
						var query = n.attributes.getNamedItem("kiwi:query").value;
						var lang = n.attributes.getNamedItem("kiwi:lang").value;
						
						// n.innerHTML = queryTagContent(format, query, lang); //"&lt;" + format + " " + query + "&gt;";
						
						ed.dom.setOuterHTML(n, buildQueryWikiTag(format, query, lang));
					}
				});
			});

			function buildQueryWikiTag(format, query, lang){ 
//				console.info("buildQueryWikiTag");
				var res = "<a class=\"kiwiQuery\" style=\"background-color:#ccc;\">[[? " + 
					"kiwi:format=\"" + format + "\" " +
					"kiwi:query=\"" + query + "\" " +
					"kiwi:lang=\"" + lang + "\"]]</a>";
				return res;
			}
			// Content loaded
			
			
			// Add a node change hook, selects the button in the UI when a image is selected
			ed.onNodeChange.add(function(ed, cm, n) {
				var res = isQuery(n);
				cm.setActive('queryEditor', res);
			});

			// Double use: 
			//  - at setContent it recognises a DIV tag with kiwi:kind=query
			//  - at edit time an A tag with className=kiwiQuery.
			function isQuery(node){
//				console.info("isQuery");
				var res = false;
				// console.info(node);
				if(node.tagName=="DIV" && 
						node.attributes.getNamedItem("kiwi:kind")!=null && 
						node.attributes.getNamedItem("kiwi:kind").value == "query"){
					res = true;
				}else if(node.tagName=="A" && tinymce.DOM.hasClass(node, "kiwiQuery")){
					res=true;
				}
				
				return res;
			}
			
			// Register the command so that it can be invoked by using 
			// tinyMCE.activeEditor.execCommand('queryEditor') or an editor button.
			ed.addCommand('queryEditor', function() {
//				console.info("mceCommand: queryEditor");
				
				// var queryWizardAction = Seam.Component.getInstance("queryWizardAction");
				// var queryWizardAction = {};
				// window.queryWizardJSLib = {};
				
				// set selection in case the cursor is on a query, but the
				// query is not selected. So the pointed query will be edited.
				if(isQuery(ed.selection.getNode())){
					ed.selection.select(ed.selection.getNode())
				}
				
				var n = ed.selection.getNode();
				
				// Store the selected wikitext in the actionbean and
				// invoke the link editor dialog.
				if(isQuery(n)){
					parseWikiQueryTag(n,function(format, query, lang){
						wizardSetQuery(format, query, lang);
					});
				} else{
					wizardSetQuery("","","");
					//queryWizardJSLib.showQuerywizardPanel();
				}
				
				
				// save the editor instance
				window.queryWizardJSLib.ed = ed;
				window.queryWizardJSLib.range	= ed.selection.getRng();
				
				// define the callback to replace the html after creating/editing
				// the query. This will replace the editor selection.
				window.queryWizardJSLib.createQuery=function(data){
					// console.info(data);
					format = data[0]; 
					query  = data[1]; 
					lang   = data[2];
//					console.info("queryWizardJSLib.createQuery");
					var tagHtml = buildQueryWikiTag(format, query, lang); 
					
					window.queryWizardJSLib.ed.selection.setRng(window.queryWizardJSLib.range);
					window.queryWizardJSLib.ed.selection.setContent(tagHtml);
					// window.queryWizardJSLib.ed.selection.setRng(window.queryWizardJSLib.range);
				};
			});

			function buildQueryHtmlTag(format, query, lang){
				var res = "";
				res+="<div xmlns:kiwi=\"http://www.kiwi-project.eu/kiwi/html/\"";
				res+=" kiwi:kind=\"query\"";
				res+=" kiwi:query=\"" + query + "\""; 
				res+=" kiwi:lang= \"" + lang  + "\"";
				res+=" kiwi:format= \"" + format  + "\"";
				res+=" >The stored QUERY</div>";
				return res;

			}
				
			// extracts the query parameters from the parameter (node)
			function parseWikiQueryTag(n, cb){
//				console.info("parseWikiQueryTag");
				var s = n.innerHTML;
//				if(!tinymce.trim(ed.selection.getContent()))
//					return;
				var format = /kiwi:format="(.*?)"/im.exec(s)[1];
				var query = /kiwi:query="(.*?)"/im.exec(s)[1];
				var lang = /kiwi:lang="(.*?)"/im.exec(s)[1];
//				console.info("selected query: format, query, lang: " + format + ", " + query + ", " + lang);
				cb(format, query, lang);
				// TODO inform the Bean about the change!
			}

			// before cleaning classnames and other not-to-export elements from the
			// html we convert the query back to plain text nodes, without the 
			// covering A tags.
			ed.onPreProcess.add(function(se, o) {
//				console.info("hook onPreProcess:");
				each(se.dom.select('A', o.node), function(n) {
					if (isQuery(n)) {
						//n.parentNode.replaceChild(dom.doc.createTextNode(n.innerHTML),n);
						parseWikiQueryTag(n,function(format, query, lang){
							var html = buildQueryHtmlTag(format, query, lang);
							ed.dom.setOuterHTML(n, html);
						});
					}
				});
			});
// ******************************************			
			// Register queryEditor button
			ed.addButton('queryEditor', {
				title : 'Create/edit an inline query',
				cmd : 'queryEditor',
				image : url + '/queryWizard.gif'
			});

		}
	});

	// Register plugin
	tinymce.PluginManager.add('querywizard', tinymce.plugins.QueryWizardPlugin);
})();
