(function() {
	var each = tinymce.each;

	tinymce.create('tinymce.plugins.KiwibookmarkPlugin', {
		/**
		 * Initializes the plugin, this will be executed after the plugin has been created.
		 * This call is done before the editor instance has finished it's initialization so use the onInit event
		 * of the editor instance to intercept that event.
		 *
		 * @param {tinymce.Editor} ed Editor instance that the plugin is initialized in.
		 * @param {string} url Absolute URL to where the plugin is located.
		 */
		init : function(ed, url) {
			// Register the command so that it can be invoked by using tinyMCE.activeEditor.execCommand('mceKiwibookmark');
			ed.addCommand('mceKiwibookmark', function() {
				var bookmarkAction = Seam.Component.getInstance("bookmarkAction");
			
				// Create the annotation that will represent this bookmark
				bookmarkJSLib.annot = {};
				bookmarkJSLib.annot.type = "bookmark";
				var bookmarkId = annotationJSLib.createAnnotationFromSelection (bookmarkJSLib.annot);
			
				bookmarkAction.setBookmarkId (bookmarkId);
				
				// Display the Bookmark dialog
				bookmarkJSLib.showBookmarkPanel();
			});

			// Register kiwibookmark button
			ed.addButton('kiwibookmark', {
				title : 'Create/edit bookmark',
				cmd : 'mceKiwibookmark',
				image : url + '/bookmark.png'
			});
		}
	});

	// Register plugin
	tinymce.PluginManager.add('kiwibookmark', tinymce.plugins.KiwibookmarkPlugin);
})();
