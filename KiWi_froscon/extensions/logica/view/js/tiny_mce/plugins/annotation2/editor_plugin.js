( function() {
	
	function onABSpanClone (t, operation, key, data, src, dst) {
		if (operation == 1) {
			/* NODE_CLONED */
			dst.setUserData("annotations", t.array_clone(src
					.getUserData("annotations")), onABSpanClone.partial(t));
		}
		else if (operation == 3) {
			/* NODE_DELETED */
			
		}
	}

	tinymce.create('tinymce.plugins.Annotation2Plugin', {
		/**
		 * Initializes the plugin, this will be executed after the plugin has
		 * been created. This call is done before the editor instance has
		 * finished it's initialization so use the onInit event of the editor
		 * instance to intercept that event.
		 * 
		 * @param {tinymce.Editor}
		 *            ed Editor instance that the plugin is initialized in.
		 * @param {string}
		 *            url Absolute URL to where the plugin is located.
		 */
		init : function(ed, url) {
			var t = this;
			
			annotationJSLib._createAnnotationFromSelection = function(t, annot) {
				var newid = t.getLargestId() + 1;
				var style = t.type2style[annot.type];
				t.annotation_set_style (newid, style);
				
				annot.id = newid;
				
				t.createAnnotationAroundSelection (newid);
				
				t.annot2obj[newid] = annot;
				
				return newid;
			}.partial(t);
			
			annotationJSLib._deleteAnnotation = function (t, id) {
				t.removeAnnotation (id);
				
				t.annot2obj[id] = null;
			}.partial(t);
			
			annotationJSLib._setAnnotationType = function (t, id, type) {
				var style = t.type2style[type];
				
				var textNodes = t.getAnnotationTextNodes (id);
				for (var i = 0; i < textNodes.length; ++i) {
					var abspan = t.getABSpan(textNodes[i]);
					if (abspan != null) {
						var annots = abspan.getUserData ("annotations");
						t.clear_ab_style(abspan.style, annots);
					}
				}
				
				t.annotation_set_style (id, style);
				t.annot2obj[id].type = type;
				
				for (var i = 0; i < textNodes.length; ++i) {
					var abspan = t.getABSpan(textNodes[i]);
					if (abspan != null) {
						var annots = abspan.getUserData ("annotations");
						t.set_ab_style(abspan.style, annots);
					}
				}
				
			}.partial(t);
			
			annotationJSLib._setTypeStyle = function (t, type, style) {
				t.type2style[type] = style;
			}.partial(t);
			
			annotationJSLib._getAnnotatedText = function (t, id) {
				var textNodes = t.getAnnotationTextNodes (id);
				var ret = "";
				for (var i = 0; i < textNodes.length; ++i) {
					ret += textNodes[i].nodeValue;
				}
				
				return ret;
			}.partial(t);
			
			annotationJSLib._getAnnotationType = function (t, id) {
				return t.annot2obj[id].type;
			}.partial(t);
			
			annotationJSLib._getAnnotations = function (t) {
				return t.getAllAnnotations (t.getRoot());
			}.partial(t);
			
			annotationJSLib._getText = function (t) {
				return t.getText (t.getRoot());
			}.partial(t);

			t.dom_events = true;
			t.styles = {};
			
			t.type2style = {};
			t.annot2obj = {};
			
			t.changes = [];
			t.ablocks = [];
			t.largest_id = 0;
			
			t.order = 0;

			t.ed = ed;
			
			/*
			t.type2style.bookmark = {backgroundColor:"green"};
			t.type2style.user = {borderBottom:"blue dotted 1px"};
			t.type2style.issue = {borderBottom:"red dotted 1px"};
			t.type2style.action_item = {backgroundColor:"Orange"};
			*/
			ed.onInit.add( function(t, ed) {
				var doc = ed.getDoc();

				doc.body.addEventListener("DOMNodeInserted", function(t, event) {
					t.onNodeInserted(event);
				}.partial(t), false);

				doc.body.addEventListener("DOMNodeRemoved", function(t, event) {
					t.onNodeRemoved(event);
				}.partial(t), false);

				doc.body.addEventListener("DOMCharacterDataModified", function(t,
						event) {
					t.onCharacterDataModified(event);
				}.partial(t), false);

				doc.body.addEventListener("DOMSubtreeModified", function(t, event) {
					t.onSubtreeModified(event);
				}.partial(t), false);
				
				// Seam.Remoting.setDebug(true);
				
				t.deserializeAnnotations (t.getRoot());
				
				// Seam.Remoting.setDebug(true);
				
				
				var change = new Seam.Remoting.Map();
				change.put("type", "InitialContent");
				change.put("value", t.encodeDom(t.getRoot()).toJSON());
				t.addChange (change);
				
			}.partial(t));
			
			ed.onPreProcess.add ( function(t, ed, o) {
				t.serializeAnnotations (o.node);
			}.partial(t));
			
			ed.onNodeChange.add ( function(t, ed, cm, e) {
				var e = t.firstTextNode (e);
				var abspan = t.getABSpan(e);
				var annots = [];
				if (abspan != null) {
					annots = abspan.getUserData("annotations");
				}
				
				window.observer.fireEvent ("annotationChangeCurrentAnnotations", annots);
				
			}.partial(t));
			
			/*ed.addButton('mockup_annotate', {
				title : 'Create/edit annotation',
				cmd : 'mceMockupAnnotate',
				image : url + '/bookmark.png'
			});*/
			
			/*ed.addCommand('mceMockupAnnotate', function(t) {
				var bookmarkAction = Seam.Component.getInstance("bookmarkAction");
			
				// Create the annotation that will represent this bookmark
				bookmarkJSLib.annot = {};
				bookmarkJSLib.annot.type = "bookmark";
				var bookmarkId = annotationJSLib.createAnnotationFromSelection (bookmarkJSLib.annot);
			
				bookmarkAction.setBookmarkId (bookmarkId);
				
				// Display the Bookmark dialog
				bookmarkJSLib.showBookmarkPanel();
			}.partial(t));*/
		},
		
		getLargestId : function() {
			return this.largest_id;
		},
		
		createAnnotationAroundSelection : function (id) {
			
			if (id > this.largest_id) {
				this.largest_id = id;
			}
			
			var range = this.ed.selection.getRng ();
			this.insertAnnotation (id, range);
		},

		disableDOMEvents : function() {
			this.dom_events = false;
		},

		enableDOMEvents : function() {
			this.dom_events = true;
		},

		annotation_set_style : function(id, style) {
			this.styles[id] = style;
		},

		annotation_get_style : function(id) {
			return this.styles[id];
		},

		/**
		 * Generates a CSS style for a AB span
		 * 
		 * @annots Array of annots
		 */
		set_ab_style : function(style, annots) {
			for ( var i = 0; i < annots.length; ++i) {
				var s = this.annotation_get_style(annots[i]);
				for ( var key in s) {
					style[key] = s[key];
				}
			}
		},
		
		clear_ab_style : function(style, annots) {
			for ( var i = 0; i < annots.length; ++i) {
				var s = this.annotation_get_style(annots[i]);
				for ( var key in s) {
					style[key] = null;
				}
			}
		},


		getRoot : function() {
			return this.ed.getDoc().body;
		},

		array_clone : function(a) {
			var ret = [];
			for ( var i = 0; i < a.length; ++i) {
				ret.push(a[i]);
			}
			return ret;
		},

		/**
		 * Return an object that represents a path from root to the node.
		 */
		getPath : function(root, node) {
			if (root.isSameNode(node)) {
				return [];
			}

			var parent = node.parentNode;
			var ret = this.getPath(root, parent);

			var n = parent.firstChild;
			var i = 0;
			while (n != null) {
				if (node.isSameNode(n)) {
					ret.push(i);
					break;
				}

				++i;
				n = n.nextSibling;
			}

			return ret;
		},

		getNode : function(root, path) {
			var node = root;
			for ( var i = 0; i < path.length; ++i) {
				var n = path[i];
				node = node.childNodes.item(n);
			}

			return node;
		},
		
		encodeDom : function(root) {
			
			if (root == null) return null;
			
			var ret = [];
			ret.push (root.nodeName);
			ret.push (root.nodeValue);
			var children = []; 
			for (var i = 0; i < root.childNodes.length; ++i) {
				children.push (this.encodeDom (root.childNodes.item(i)));
			}
			
			ret.push (children);
			
			return ret;
		},

		previousTextNode : function(root, node) {

			if (node == null) {
				alert("null");
			}

			if (root.isSameNode(node))
				return null;
			while (true) {
				while (node.previousSibling == null) {
					node = node.parentNode;
					if (node == null || node.isSameNode(root))
						return null;
				}

				node = node.previousSibling;

				while (node.nodeType != Node.TEXT_NODE
						&& node.lastChild != null) {
					node = node.lastChild;
				}

				if (node.nodeType == Node.TEXT_NODE)
					return node;
			}
		},

		firstTextNode : function(root) {

			var node = root;

			while (true) {
				while (node.nodeType != Node.TEXT_NODE
						&& node.firstChild != null) {
					node = node.firstChild;
				}
				
				if (node.nodeType == Node.TEXT_NODE)
					return node;
				
				if (node.isSameNode(root))
					return null;

				while (node.nextSibling == null) {
					node = node.parentNode;
					if (node.isSameNode(root))
						return null;
				}

				node = node.nextSibling;
			}
		},

		lastTextNode : function(root) {
			var node = root;

			while (true) {
				while (node.nodeType != Node.TEXT_NODE
						&& node.lastChild != null) {
					node = node.lastChild;
				}

				if (node.nodeType == Node.TEXT_NODE)
					return node;

				if (node.isSameNode(root))
					return null;
				
				while (node.previousSibling == null) {
					node = node.parentNode;
					if (node.isSameNode(root))
						return null;
				}

				node = node.previousSibling;
			}
		},

		nextTextNode : function(root, node) {

			if (root.isSameNode(node))
				return null;
			while (true) {
				while (node.nextSibling == null) {
					node = node.parentNode;
					if (node == null || node.isSameNode(root))
						return null;
				}

				node = node.nextSibling;

				while (node.nodeType != Node.TEXT_NODE
						&& node.firstChild != null) {
					node = node.firstChild;
				}

				if (node.nodeType == Node.TEXT_NODE)
					return node;
			}
		},

		getABSpan : function(n) {
		
			if (n == null) return null;
		
			var p = n.parentNode;
			if (p == null) return null;
			
			if (p.getUserData("annotations") != null) {
				return p;
			}
			return null;
		},
		
		/**
		 * Returns a list of textnodes for this annotation
		 */
		getAnnotationTextNodes : function (id) {
			var root = this.getRoot();
			var n = this.firstTextNode(root);
			var ret = [];
			while (n != null) {
				var abspan = this.getABSpan (n);
				if (abspan != null) {
					var annots = abspan.getUserData("annotations");
					if (annots != null) {
						if (this.obj_in_array (id, annots)) {
							ret.push (n);
						}
					}
				}
				n = this.nextTextNode(root, n);
			}
			
			return ret;
		},

		getAnnotations : function(n) {
			var ab = this.getABSpan(n);
			if (ab == null) {
				return [];
			}
			var ret = ab.getUserData("annotations");
			if (ret == null) {
				return [];
			}
			return ret;
		},

		deleteABSpan : function(abspan) {
			var p = abspan.parentNode;

			var child = abspan.childNodes;
			while (abspan.firstChild != null) {
				p.insertBefore(abspan.firstChild, abspan);
			}
		},

		splitTextNode : function(n, offset) {
			var abspan = this.getABSpan(n);
			if (abspan == null) {
				var p = n.parentNode;
				var text = n.nodeValue;
				var n1 = document.createTextNode(text.substring(0, offset));
				var n2 = document.createTextNode(text.substring(offset,
						n.length));

				p.replaceChild(n2, n);
				p.insertBefore(n1, n2);

				var ret = [];
				ret.push(n1);
				ret.push(n2);
				return ret;
			} else {
				var abspan1 = document.createElement("span");
				var abspan2 = document.createElement("span");

				var annotations = abspan.getUserData("annotations");
				abspan1.setUserData("annotations", annotations,
						onABSpanClone.partial(this));
				this.set_ab_style(abspan1.style, annotations);

				abspan2.setUserData("annotations", this
						.array_clone(annotations), onABSpanClone.partial(this));
				this.set_ab_style(abspan2.style, annotations);

				var p = abspan.parentNode;

				var text = n.nodeValue;
				var n1 = document.createTextNode(text.substring(0, offset));
				var n2 = document.createTextNode(text.substring(offset,
						n.length));

				p.replaceChild(abspan2, abspan);
				p.insertBefore(abspan1, abspan2);

				abspan1.appendChild(n1);
				abspan2.appendChild(n2);

				var ret = [];
				ret.push(n1);
				ret.push(n2);
				return ret;
			}
		},

		insertAnnotationToTextNode : function(annotation, n) {
			var abspan = this.getABSpan(n);

			if (abspan == null) {
				abspan = document.createElement("span");
				n.parentNode.replaceChild(abspan, n);
				abspan.appendChild(n);

				abspan.setUserData("annotations", [ annotation ],
						onABSpanClone.partial(this));
				this.set_ab_style(abspan.style, [ annotation ]);
			} else {
				var alist = abspan.getUserData("annotations");
				alist.push(annotation);
				this.set_ab_style(abspan.style, alist);
			}
		},
		
		removeAnnotationFromTextNode : function (annotation, n) {
			var abspan = this.getABSpan(n);
			if (abspan != null) {
				/* build a new array without the annotation */
				var annots = abspan.getUserData("annotations");
				var newannots = [];
				
				for (var i = 0; i < annots.length; ++i) {
					if (annots[i] == annotation) {
					}
					else {
						newannots.push (annots[i]);
					}
				}
				
				if (annots.length != newannots.length) {
					if (newannots.length == 0) {
						/* no annotations left, remove the whole ABSpan */
						while (abspan.firstChild != null) {
							var n = abspan.firstChild;
							abspan.removeChild (n);
							abspan.parentNode.insertBefore (n, abspan);
						}
						abspan.parentNode.removeChild (abspan);
					}
					else {
						abspan.setUserData ("annotations", newannots, onABSpanClone.partial(this));
						this.clear_ab_style(abspan.style, [annotation]);
						this.set_ab_style(abspan.style, newannots);
					}
				}
			}
		},
		
		removeAllAnnotationsFromTextNode : function (n) {
			var abspan = this.getABSpan(n);
			if (abspan != null) {
				while (abspan.firstChild != null) {
					var n = abspan.firstChild;
					abspan.removeChild (n);
					abspan.parentNode.insertBefore (n, abspan);
				}
				abspan.parentNode.removeChild (abspan);
			}
		},

		insertAnnotation : function(annotation, sel) {
			this.disableDOMEvents();
			Seam.Remoting.startBatch();

			var f = this.firstTextNode(sel.startContainer);
			var e = this.lastTextNode(sel.endContainer);

			if (f != e) {
				if (sel.startOffset != 0) {
					f = this.splitTextNode(f, sel.startOffset)[1];
				}

				if (sel.endOffset != 0) {
					e = this.splitTextNode(e, sel.endOffset)[0];
				} else {
					e = this.previousTextNode(this.getRoot(), e);
				}
			} else {
				var diff = sel.endOffset - sel.startOffset;
				rest = this.splitTextNode(f, sel.startOffset)[1];
				e = f = this.splitTextNode(rest, diff)[0];
			}

			while (f != null) {
				this.insertAnnotationToTextNode(annotation, f);
				if (f == e)
					break;

				f = this.nextTextNode(this.getRoot(), f);
			}

			Seam.Remoting.executeBatch();
			this.enableDOMEvents();
		},
		
		removeAnnotation : function (id) {
			/* go through all the DOM and remove the annots 
			 * or whole AB spans */
			var n = this.firstTextNode(this.getRoot());
			while (n != null) {
				this.removeAnnotationFromTextNode (id, n);
				
				n = this.nextTextNode(this.getRoot(), n);
			}
		},
		
		removeAllAnnotations : function (root) {
			var n = this.firstTextNode(root);
			while (n != null) {
				this.removeAllAnnotationsFromTextNode (n);
				n = this.nextTextNode(root, n);
			}
		},
		
		getAllAnnotations : function(root) {
			var n = this.firstTextNode(root);
			var ret = [];
			while (n != null) {
				var annots = this.getAnnotations (n);
				for (var i = 0; i < annots.length; ++i) {
					if (!this.obj_in_array(annots[i], ret)) {
						ret.push (annots[i]);
					}
				}
				n = this.nextTextNode(root, n);
			}
			
			return ret;
		},
		
		getText : function(root) {
			var ret = "";
			var n = this.firstTextNode(root);
			while (n != null) {
				ret += n.nodeValue;
				n = this.nextTextNode(root, n);
			}
			
			return ret;
		},
		
		obj_in_array : function(obj, array) {
			for ( var i = 0; i < array.length; ++i) {
				if (obj == array[i])
					return true;
			}		
			return false;
		},

		/**
		 * Go through all the tree from beginNode to endNode and fixes all
		 * annotations
		 */
		fixAnnotations : function(beginNode, endNode) {

			this.disableDOMEvents();

			var lr = {};
			var rl = {};

			var beginNodeText = this.lastTextNode(beginNode);
			var endNodeText = this.firstTextNode(endNode);

			var e = beginNodeText;
			while (true) {
				var annots = this.getAnnotations(e);
				for ( var i = 0; i < annots.length; ++i) {
					var a = annots[i];
					if (lr[a] == null) {
						lr[a] = e;
					}
				}

				if (e == endNodeText)
					break;
				e = this.nextTextNode(this.getRoot(), e);
			}

			var e = endNodeText;
			while (true) {
				var annots = this.getAnnotations(e);
				for ( var i = 0; i < annots.length; ++i) {
					var a = annots[i];
					if (rl[a] == null) {
						rl[a] = e;
					}
				}

				if (e == beginNodeText)
					break;
				e = this.previousTextNode(this.getRoot(), e);
			}

			var open = {};

			var e = beginNodeText;
			while (true) {
				var annots = this.getAnnotations(e);

				for ( var a in open) {
					if (open[a] == true) {
						if (this.obj_in_array(a, annots)) {
						} else {
							this.insertAnnotationToTextNode(a, e);
						}
					}
				}

				for ( var i = 0; i < annots.length; ++i) {
					var a = annots[i];
					if (open[a] == null) {
						open[a] = true;
					}

					if (rl[a] == e) {
						open[a] = null;
					}
				}

				if (e == endNodeText)
					break;
				e = this.nextTextNode(this.getRoot(), e);
			}

			this.enableDOMEvents();
		},

		onNodeInserted : function(event) {
			
			/*var change = new Seam.Remoting.Map();
			change.put("type", "NodeInserted");
			change.put("target", this.getPath(this.getRoot(), event.target).toJSON());
			change.put("value", this.encodeDom(event.target).toJSON());
			this.addChange (change);
			*/
			if (!this.dom_events)
				return;

			switch (event.target.nodeType) {
			case Node.TEXT_NODE:
			case Node.ELEMENT_NODE: {
				/* We need to fix possibly splitted annotations */

				var et = event.target;
				var fc = et.firstChild;
				
				var tf = this.firstTextNode(event.target);
				var tl = this.lastTextNode(event.target);
				
				/* node has no text nodes inside... 
				 * if only one is null, that is some error... */
				if (tf == null && tl == null) break;
				
				var f = this.previousTextNode(this.getRoot(), tf)
				var t = this.nextTextNode(this.getRoot(), tl);
				
				/* if at the beginning or end, no possible splits... */
				if (f == null || t == null) break;

				this.fixAnnotations(f, t);
				break;
			}
			}
		},

		onNodeRemoved : function(event) {
			/*var change = new Seam.Remoting.Map();
			change.put("type", "NodeRemoved");
			change.put("target", this.getPath(this.getRoot(), event.target).toJSON());
			this.addChange (change);
		*/
			if (!this.dom_events)
				return;

			switch (event.target.nodeType) {
			case Node.TEXT_NODE:
			case Node.ELEMENT_NODE: {
				break;
			}
			}
		},

		onCharacterDataModified : function(event) {
			/*var change = new Seam.Remoting.Map();
			change.put("type", "CharacterDataModified");
			change.put("target", this.getPath(this.getRoot(), event.target).toJSON());
			change.put("text", event.target.nodeValue);
			this.addChange (change);
			 */
			if (!this.dom_events)
				return;

			switch (event.target.nodeType) {
			case Node.TEXT_NODE: {
				break;
			}
			}
		},
		
		onSubtreeModified : function(event) {
			var change = new Seam.Remoting.Map();
			change.put("type", "SubtreeModified");
			var target = this.getPath(this.getRoot(), event.target).toJSON();
			var value = this.encodeDom(event.target).toJSON();
			change.put("target", target);
			change.put("value", value);
			this.addChange (change);

			if (!this.dom_events)
				return;

			switch (event.target.nodeType) {
			case Node.TEXT_NODE:
			case Node.ELEMENT_NODE: {
				break;
			}
			}
		},
		
		serializeAnnotations : function(root) {
			this.disableDOMEvents();
			var lr = {};
			var rl = {};
			var as = [];

			var beginNodeText = this.firstTextNode(root);
			var endNodeText = this.lastTextNode(root);

			var e = beginNodeText;
			while (true) {
				var annots = this.getAnnotations(e);
				for ( var i = 0; i < annots.length; ++i) {
					var a = annots[i];
					if (lr[a] == null) {
						lr[a] = e;
						as.push (a);
					}
				}

				if (e == endNodeText)
					break;
				e = this.nextTextNode(root, e);
			}

			var e = endNodeText;
			while (true) {
				var annots = this.getAnnotations(e);
				for ( var i = 0; i < annots.length; ++i) {
					var a = annots[i];
					if (rl[a] == null) {
						rl[a] = e;
					}
				}

				if (e == beginNodeText)
					break;
				e = this.previousTextNode(root, e);
			}
			
			for (var i = 0; i < as.length; ++i) {
				var a = as[i];
				var begin_e = lr[a];
				var end_e = rl[a];
				
				begin_e.nodeValue = "[[BookmarkStart:" + a + "]]" + begin_e.nodeValue;
				end_e.nodeValue = end_e.nodeValue + "[[BookmarkEnd:" + a + "]]";
			}
			
			this.removeAllAnnotations (root);
			this.enableDOMEvents();
		},
		
		deserializeAnnotations : function (root) {
			
			this.disableDOMEvents();
			
			var re_bookmark_start_1 = /\[\[\s*BookmarkStart:([^\]]+?)\s*\]\]/;
			var re_bookmark_end = /\[\[\s*BookmarkEnd:([^\]]+?)\s*\]\]/;
    		
			var begins = {};
			var ends = {};
			var annots = [];
			
			var beginNodeText = this.firstTextNode(root);
			
			var e = beginNodeText;
			
			if (e == null) return;
			
			while (e != null) {	
						
				while(true) {		
        			var m_bm_start_1 = re_bookmark_start_1.exec (e.nodeValue);
        			var m_bm_end = re_bookmark_end.exec (e.nodeValue);
        			
        			var bm_nearest = null;
        			var bm_nearest_index = null;
        			
        			if (m_bm_start_1) {
        				if (bm_nearest_index == null || bm_nearest_index > m_bm_start_1.index) {
        					bm_nearest = m_bm_start_1;
        					bm_nearest_index = m_bm_start_1.index;
        				}
        			}
        			if (m_bm_end) {
        				if (bm_nearest_index == null || bm_nearest_index > m_bm_end.index) {
        					bm_nearest = m_bm_end;
        					bm_nearest_index = m_bm_end.index;
        				}
        			}
        			
        			if (bm_nearest == null) break;
        			
        			var isBegin = null;
        			var id = parseInt(bm_nearest[1]);
        			if (bm_nearest == m_bm_start_1) {
        				isBegin = true;
        				annots.push(id);
        				
        			}
        			else if (bm_nearest == m_bm_end) {
        				isBegin = false;
        			}
        			
        			
        			
        			var ret = this.splitTextNode (e, bm_nearest_index);
        			var pre = ret[0];
        			var tag_rest = ret[1];
        			
        			ret = this.splitTextNode (tag_rest, bm_nearest[0].length);
        			var tag = ret[0];
        			var rest = ret[1];
        			
        			/* remove the tag */
        			this.removeAllAnnotationsFromTextNode (tag);
        			tag.parentNode.removeChild (tag);
        			
        			if (isBegin) {
        				begins[id] = pre;
        			}
        			else {
        				ends[id] = pre;
        			}
        			
        			e = rest;
        			
        		}
	
			
				e = this.nextTextNode(root, e);
			}
			
			for (var i = 0; i < annots.length; ++i) {
				var id = annots[i];
				
				if (begins[id] != null && ends[id] != null) {
					var begin_e = this.nextTextNode(root, begins[id]);
					var end_e = ends[id];
					
					this.annotation_set_style (id, this.type2style["bookmark"]);
					
					this.insertAnnotationToTextNode (id, begin_e);
					if (begin_e != end_e) {
						this.insertAnnotationToTextNode (id, end_e);
					}
				}
			}
			
			// fix annotations
			this.fixAnnotations (this.firstTextNode(root), this.lastTextNode(root));
			this.enableDOMEvents();
		},
		
		
		addChange : function(change) {
			change.put("order", this.order);
			this.order ++;
			
			//Seam.Remoting.log ("addChange: " + change.get("order") + " " + change.get("type") + "\n" + change.get("target") + "\n" + change.get("value") + "\n\n");
			var editorAction = Seam.Component.getInstance("editorAction");
			// editorAction.hintsUpdateLocal (change);
		}
		
	});

	// Register plugin
	tinymce.PluginManager.add('annotation2', tinymce.plugins.Annotation2Plugin);
})();
