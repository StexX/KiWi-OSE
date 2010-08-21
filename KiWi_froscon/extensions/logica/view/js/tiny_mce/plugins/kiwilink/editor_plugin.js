(function() {
	var each = tinymce.each;
	
	// Load plugin specific language pack
	// tinymce.PluginManager.requireLangPack('kiwilink'); --> doesn't exist yet.

	tinymce.create('tinymce.plugins.KiwilinkPlugin', {
		/**
		 * Initializes the plugin, this will be executed after the plugin has been created.
		 * This call is done before the editor instance has finished it's initialization so use the onInit event
		 * of the editor instance to intercept that event.
		 *
		 * @param {tinymce.Editor} ed Editor instance that the plugin is initialized in.
		 * @param {string} url Absolute URL to where the plugin is located.
		 */
		init : function(ed, url) {

			// private functions
			function isKiwilink(node){
				return node.tagName == "A" && node.className=="kiwilink";
			}
			
			function txt2KiwilinkHtml(t){
				return t.replace(/(\[\[.*?\]\])/gi,"<a class='kiwilink' style='background:#ddd;'>$1</a>");
			}
			
			// Register the command so that it can be invoked by using tinyMCE.activeEditor.execCommand('mceKiwilink');
			ed.addCommand('mceKiwilink', function() {
				var kiwilinkAction = Seam.Component.getInstance("kiwilinkAction");

				// sets selection if wikilink not selected.
				if(isKiwilink(ed.selection.getNode())){
					ed.selection.select(ed.selection.getNode())
				}
				
				// Store the selected wikitext in the actionbean and
				// invoke the link editor dialog.
				kiwilinkAction.setSelectionHTML(ed.selection.getContent(),
					function(){
						kiwilinkJSLib.showKiwilinkPanel();
					});
				
				// save the editor instance
				kiwilinkJSLib.ed = ed;
				kiwilinkJSLib.range	= ed.selection.getRng();
				
				// define the callback to replace the wikitext after creating/editing
				// the link. This will replace the editor selection with the replacement.
				kiwilinkJSLib.createLink=function(replacementHTML){
					kiwilinkJSLib.ed.selection.setRng(kiwilinkJSLib.range);
					kiwilinkJSLib.ed.selection.setContent(txt2KiwilinkHtml(replacementHTML));
					kiwilinkJSLib.ed.selection.setRng(kiwilinkJSLib.range);
				};
			});

			// Register kiwilink button
			ed.addButton('kiwilink', {
				title : 'Create/edit internal link',
				cmd : 'mceKiwilink',
				image : url + '/link.gif'
			});

			// observers
			
			// Add a node change handler, selects the button in the UI when a image is selected
			ed.onNodeChange.add(function(ed, cm, n) {
				cm.setActive('kiwilink', isKiwilink(n));
			});

			// on loading a text into the editor mark cover the kiwilinks with a gray
			// backgrounded A tag and the classname 'kiwilink'. 
			ed.onBeforeSetContent.add(function(ed, o){
				
				// this is only for testing as long the wikilink parser is buggy..
				// o.content= '<div id="udata_0">irgendein [[neuer|neuer]] Text...</div><div><br/>[[asdf]] <br/>[[kjhg|kjhg]] </div>';
				
				o.content = txt2KiwilinkHtml(o.content);
			});
			
			// before cleaning classnames and other not-to-export elements from the
			// html we convert the kiwilinks back to plain text nodes, without the 
			// covering A tags.
			ed.onPreProcess.add(function(se, o) {
				var dom = se.dom;
				each(dom.select('A', o.node), function(n) {
					if (isKiwilink(n)) {
						n.parentNode.replaceChild(dom.doc.createTextNode(n.innerHTML),n);
					}
				});
			});
		}
	});

	// Register plugin
	tinymce.PluginManager.add('kiwilink', tinymce.plugins.KiwilinkPlugin);
})();
