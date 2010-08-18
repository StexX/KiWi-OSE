(function() {
	tinymce.create('tinymce.plugins.KiWiRDRaPlugin', {
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
			
			t.selectedPropertyElement = null;
			t.selectedAboutElement = null;
			t.editing = null;
			t.editingAbout = null;
			t.editingLink = null;
			t.editingIteration = null;
			t.editingIterationItem = null;
			t.creating = false;
			
			kiwirdfaJSLib._setProperty = function (t, property) {
				
				if (t.editing != null) {
					t.rdfa.setProperty (t.editing, property);
				}
				
				t.editing = null;
			}.partial(t);
			
			kiwirdfaJSLib._cancel = function (t) {
				if (t.editing != null) {
					
					/*If currently creating, the cancel method deletes the span,
					 If editing, cancel just do nothing.*/ 
					if (t.creating) {
						t.rdfa.deleteSpan (t.editing);
					}
					t.editing = null;
				}
			}.partial(t);
			
			kiwirdfaJSLib._delete = function (t) {
				if (t.editing != null) {
					t.rdfa.deleteSpan (t.editing);
					t.editing = null;
				}
			}.partial(t);
			
			kiwirdfaJSLib._setAboutProperties = function (t, uri, relation, type) {
				if (t.editingAbout != null) {
					t.rdfa.setAboutUri (t.editingAbout, uri);
					t.rdfa.setAboutRelation (t.editingAbout, relation);
					t.rdfa.setAboutType (t.editingAbout, type);
				}
				t.editingAbout = null;
			}.partial(t);
			
			kiwirdfaJSLib._cancelAbout = function(t) {
				if (t.editingAbout != null) {
					if (t.creating) {
						t.rdfa.deleteAbout (t.editingAbout);
					}
					t.editingAbout = null;
				}
			}.partial(t);
			
			kiwirdfaJSLib._deleteAbout = function (t) {
				if (t.editingAbout != null) {
					t.rdfa.deleteAbout (t.editingAbout);
					t.editingAbout = null;
				}
			}.partial(t);
			
			kiwirdfaJSLib._setLinkProperties = function (t, uri, relation, type, title) {
				if (t.editingLink != null) {
					t.rdfa.setLinkUri (t.editingLink, uri);
					t.rdfa.setLinkRelation (t.editingLink, relation);
					t.rdfa.setLinkType (t.editingLink, type);
					t.rdfa.setLinkTitle (t.editingLink, title);
				}
				t.editingLink = null;
			}.partial(t);
			
			kiwirdfaJSLib._cancelLink = function(t) {
				if (t.editingLink != null) {
					if (t.creating) {
						t.rdfa.deleteLink (t.editingLink);
					}
					t.editingLink = null;
				}
			}.partial(t);
			
			kiwirdfaJSLib._cancelLinkSelect = function(t) {
				if (t.editingLink != null) {
					t.editingLink = null;
				}
			}.partial(t);
			
			kiwirdfaJSLib._deleteLink = function (t) {
				if (t.editingLink != null) {
					t.rdfa.deleteLink (t.editingLink);
					t.editingLink = null;
				}
			}.partial(t);

			kiwirdfaJSLib._setIterationProperties = function (t, relation) {
				if (t.editingIteration != null) {
					t.rdfa.setIterationRelation(t.editingIteration, relation);
				}
				t.editingIteration = null;
			}.partial(t);

			kiwirdfaJSLib._cancelIteration = function (t) {
				if (t.editingIteration != null) {
					if (t.creating) {
						t.rdfa.deleteIteration (t.editingIteration);
					}
					t.editingIteration = null;
				}
			}.partial(t);
		
			kiwirdfaJSLib._deleteIteration = function (t) {
				if (t.editingIteration != null) {
					t.rdfa.deleteIteration (t.editingIteration);
					t.editingIteration = null;
				}
			}.partial(t);
		
			kiwirdfaJSLib._setIterationItemProperties = function (t, uri, relation, type, title) {
				if (t.editingIterationItem != null) {
					t.rdfa.setIterationItemUri(t.editingIterationItem, uri);
					t.rdfa.setIterationItemType(t.editingIterationItem, type);
					t.rdfa.setIterationItemTitle(t.editingIterationItem, title);
		
					/* relation is a property of the iteration element, and it should not be possible to change that by updating an iteration item */
					
					t.editingIterationItem = null;
				}
			}.partial(t);

			kiwirdfaJSLib._cancelIterationItem = function (t) {
				if (t.editingIterationItem != null) {
					if (t.creating) {
						t.rdfa.deleteIterationItem (t.editingIterationItem);
					}
					t.rdfa.editingIterationItem = null;
				}
			}.partial(t);

			kiwirdfaJSLib._deleteIterationItem = function (t) {
				if (t.editingIterationItem != null) {
					t.rdfa.deleteIterationItem (t.editingIterationItem);
					t.rdfa.editingIterationItem = null;
				}
			}.partial(t);
			
			/* Register the command so that it can be invoked by using tinyMCE.activeEditor.execCommand('mceKiWiRDFa'); */
			ed.addCommand('mceKiWiRDFa', function(t, ed) {
				
				/* Get the context (the URI of the curently edited "about" section)  */
				var context = null;
				if (t.selectedAboutElement != null) {
					context = t.rdfa.getAboutUri(t.selectedAboutElement);
				}
				
				if (t.selectedPropertyElement == null) {
					var selection = t.ed.selection.getRng();
					var span = t.rdfa.createPropertySpan(selection);
				
					t.editing = span;
					t.creating = true;
				
					kiwirdfaJSLib.showKiwirdfaPanel (context, null);
				}
				else {
					t.editing = t.selectedPropertyElement;
					t.creating = false;
					
					kiwirdfaJSLib.showKiwirdfaPanel (context, t.rdfa.getProperty(t.editing));
				}
			}.partial(t, ed));
			
			ed.addCommand('mceKiWiRDFaAbout', function(t, ed) {
				var selection = t.ed.selection.getRng();
				var div = t.rdfa.createAbout(selection);
				
				t.editingAbout = div;
				t.creating = true;
				
				/* Get the context (the URI of the curently edited "about" section)  */
				var context = null;
				if (t.selectedAboutElement != null) {
					context = t.rdfa.getAboutUri(t.selectedAboutElement);
				}
				
				kiwirdfaJSLib.showKiwirdfaAboutPanel (context, null, null, null);
				
			}.partial(t, ed));
			
			ed.addCommand('mceKiWiRDFaLink', function(t, ed) {
				var context = null;
				if (t.selectedAboutElement != null) {
					context = t.rdfa.getAboutUri(t.selectedAboutElement);
				}
				
				if (t.selectedLinkElement == null) {
					var selection = t.ed.selection.getRng();
					var span = t.rdfa.createLinkSpan(selection);
				
					t.editingLink = span;
					t.creating = true;
				
					kiwirdfaJSLib.showKiwirdfaLinkPanel (context, null, null, null, null);
				}
				else {
					t.editingLink = t.selectedLinkElement;
					t.creating = false;
					
					kiwirdfaJSLib.showKiwirdfaLinkPanel (context, t.rdfa.getLinkUri(t.editingLink), t.rdfa.getLinkRelation(t.editingLink), t.rdfa.getLinkType(t.editingLink), t.rdfa.getLinkTitle(t.editingLink));
				}
			}.partial(t, ed));

            ed.addCommand('mceKiWiRDFaIteration', function(t, ed) {
            	var selection = t.ed.selection.getRng();
				var div = t.rdfa.createIteration(selection);
				
				t.editingIteration = div;
				t.creating = true;
				
				/* Get the context (the URI of the curently edited "about" section)  */
				var context = null;
				if (t.selectedAboutElement != null) {
					context = t.rdfa.getAboutUri(t.selectedAboutElement);
				}
				
				kiwirdfaJSLib.showKiwirdfaIterationPanel (context, null);
 
            }.partial(t, ed));

			/* Register kiwirdfa button */
			ed.addButton('kiwirdfa', {
				title : 'Create/edit RDFa property',
				cmd : 'mceKiWiRDFa',
				image : url + '/rdfa_property.gif'
			});
			
			ed.addButton('kiwirdfaAbout', {
				title : 'Create component',
				cmd : 'mceKiWiRDFaAbout',
				image : url + '/rdfa_component.gif'
			});
			
			ed.addButton('kiwirdfaLink', {
				title : 'Create/edit RDFa link',
				cmd : 'mceKiWiRDFaLink',
				image : url + '/rdfa_link.gif'
			});

            ed.addButton('kiwirdfaIteration', {
                title : 'Create/edit component list',
                cmd : 'mceKiWiRDFaIteration',
                image : url + '/rdfa_iterated_include.gif'
            });
			
			ed.onInit.add( function(t, ed) {
				t.rdfa = new RDFa(ed.getDoc().body);
				
				t.rdfa.onAboutModify = function (t, div) {
					t.editingAbout = div;
					t.creating = false;
					
					/* if we modify the about div, the context is the about section "above" the edited section */
					var context = null;
					var contextElement = t.rdfa.getLeastRDFaAboutElement (div.parentNode);
					if (contextElement != null) {
						context = t.rdfa.getAboutUri(contextElement);
					}
					
					kiwirdfaJSLib.showKiwirdfaAboutPanel (context, t.rdfa.getAboutUri(div), t.rdfa.getAboutRelation(div), t.rdfa.getAboutType(div));
				}.partial(t);
				
				t.rdfa.onLinkModify = function (t, span) {
					
					var context = null;
					if (t.selectedAboutElement != null) {
						context = t.rdfa.getAboutUri(t.selectedAboutElement);
					}
					
					t.editingLink = span;
					t.creating = false;
					
					kiwirdfaJSLib.showKiwirdfaLinkSelectPanel (context, t.rdfa.getLinkUri(t.editingLink), t.rdfa.getLinkRelation(t.editingLink), t.rdfa.getLinkType(t.editingLink), t.rdfa.getLinkTitle(t.editingLink));
					
				}.partial(t);

				t.rdfa.onIterationModify = function (t, div) {
					t.editingIteration = div;
					t.creating = false;
					var context = null;
                    if (t.selectedAboutElement != null) {
                        context = t.rdfa.getAboutUri(t.selectedAboutElement);
                    }

					kiwirdfaJSLib.showKiwirdfaIterationPanel (context, t.rdfa.getIterationRelation(div));

				}.partial(t);

				t.rdfa.onIterationAdd = function (t, div) {
					var context = null;
					var contextElement = t.rdfa.getLeastRDFaAboutElement (div.parentNode);
					if (contextElement != null) {
						context = t.rdfa.getAboutUri(contextElement);
					}
					
					var item = t.rdfa.createIterationItem(div);
					t.creating = true;
					t.editingIterationItem = item;

					kiwirdfaJSLib.showKiwirdfaIterationItemPanel (context, null, t.rdfa.getIterationRelation(div),  null, null);

				}.partial(t);

				t.rdfa.onIterationItemModify = function (t, div) {
					t.editingIterationItem = div;
					t.creating = false;
					var context = null;
					var contextElement = t.rdfa.getLeastRDFaAboutElement (div.parentNode);
					if (contextElement != null) {
						context = t.rdfa.getAboutUri(contextElement);
					}

					var iterationElement = t.rdfa.getIterationItemIterationElement (div);
					kiwirdfaJSLib.showKiwirdfaIterationItemPanel (context, t.rdfa.getIterationItemUri(div), t.rdfa.getIterationRelation(iterationElement), t.rdfa.getIterationItemType(div), t.rdfa.getIterationItemTitle(div));

				}.partial(t);
				
				t.rdfa.postLoad(ed.getDoc().body);
				
			}.partial(t));
			
			ed.onNodeChange.add ( function(t, ed, cm, e) {
				
				t.selectedPropertyElement = t.rdfa.getLeastRDFaPropertyElement (e);
				t.rdfa.setSelectedPropertyElement (t.selectedPropertyElement);
				
				t.selectedLinkElement = t.rdfa.getLeastRDFaLinkElement (e);
				
				// alert (t.selectedLinkElement);
				
				t.rdfa.setSelectedLinkElement (t.selectedLinkElement);
				
				var newSelectedAboutElement = t.rdfa.getLeastRDFaAboutElement (e);
				if (t.selectedAboutElement != newSelectedAboutElement) {
					t.selectedAboutElement = newSelectedAboutElement;
					t.rdfa.setSelectedAboutElement (t.selectedAboutElement);
					/*if (t.selectedBlockContainerElement != null) {
						kiwirdfaJSLib.setContext (t.rdfa.getAboutUri(t.selectedBlockContainerElement));
					}
					else {
						kiwirdfaJSLib.setContext (null);
					}*/
				}
			}.partial(t));
			
			ed.onPreProcess.add ( function(t, ed, o) {
				t.rdfa.preStore(o.node);
			}.partial(t));		
		}
	});

	/* Register plugin */
	tinymce.PluginManager.add('kiwirdfa', tinymce.plugins.KiWiRDRaPlugin);
})();
