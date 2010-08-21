(function() {
	tinymce.create('tinymce.plugins.FragmentsPlugin', {
		/**
		 * Initializes the plugin, this will be executed after the plugin has been created.
		 * This call is done before the editor instance has finished it's initialization so use the onInit event
		 * of the editor instance to intercept that event.
		 *
		 * @param {tinymce.Editor} ed Editor instance that the plugin is initialized in.
		 * @param {string} url Absolute URL to where the plugin is located.
		 */
		init : function(ed, url) {
		
			var t = this;
			t.ed = ed;
			t.selectedFragmentId = null;
			t.creating = false;
			t.editingFragmentId = null;
			
			fragmentJSLib._setFragment = function (t, fragmentId) {
				if (t.creating) {
					t.fragments.renameAnnotation ("__tmp__", fragmentId);
					t.fragments.setAnnotationStyle (fragmentId, {backgroundColor:"#E0E0F0"});
				}
			}.partial(t);
			
			fragmentJSLib._cancel = function (t) {
				if (t.creating) {
					t.fragments.removeAnnotation("__tmp__");
				}
			}.partial(t);
			
			fragmentJSLib._delete = function(t) {
				if (t.creating) {
					t.fragments.removeAnnotation("__tmp__");
				}
				else {
					t.fragments.removeAnnotation(t.editingFragmentId);
				}
			}.partial(t);
			
			// Register the command so that it can be invoked by using tinyMCE.activeEditor.execCommand('mceKiwibookmark');
			ed.addCommand('mceFragment', function(t) {

				if (t.selectedFragmentId != null) {
					t.creating = false;
					t.editingFragmentId = t.selectedFragmentId;
				}
				else {
					t.fragments.setAnnotationStyle ("__tmp__", {backgroundColor:"#E0E0F0"});
					t.fragments.createAnnotationAroundSelection ("__tmp__",  t.ed.selection.getRng());
					t.editingFragmentId = null;
					t.creating = true;
				}
				
				fragmentJSLib.showFragmentsPanel(null, t.editingFragmentId);
				
			}.partial(t));

			// Register kiwibookmark button
			ed.addButton('fragments', {
				title : 'Create/edit fragment',
				cmd : 'mceFragment',
				image : url + '/bookmark.png'
			});
			
			ed.onInit.add( function(t, ed) {
				t.fragments = new Annotations();
				t.fragments.init(ed.getDoc().body);
				
				t.fragments.deserializeAnnotations (ed.getDoc().body);
			}.partial(t));
			
			ed.onPreProcess.add ( function(t, ed, o) {
				// clear the temporary fragment before saving
				// t.fragments.removeAnnotation("__tmp__");
				t.fragments.serializeAnnotations(o.node);
			}.partial(t));	
			
			ed.onNodeChange.add ( function(t, ed, cm, e) {
				t.selectedFragmentId = null;
				var n = t.fragments.firstTextNode(e);
				if (n != null) {
					var fragmentIds = t.fragments.getAnnotations(n);
					// TODO: cycle through fragmentIds on onClick
					// for now just take the first one
					
					if (fragmentIds.length > 0) {
						t.selectedFragmentId = fragmentIds[0];
					}
				}
			}.partial(t));
		}
	});

	// Register plugin
	tinymce.PluginManager.add('fragments', tinymce.plugins.FragmentsPlugin);
})();
