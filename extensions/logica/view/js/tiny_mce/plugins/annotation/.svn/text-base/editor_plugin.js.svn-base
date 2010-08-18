/**
 * Plugin Editor-Annotation component for TinyMCE
 * Michal Sebek, 2008
 * KiWi project 
 * www.fit.vutbr.cz
 *
 * File contains kernel of editor-annotation plugin for TinyMCE. Requires 
 * file wdg_annottools.js to be loaded (lib wwith shared methods).
 * 
 * Last modif: 10.09.2008
 */


var MAX_NEW_ANNOTS_SHOW_LIMIT = 10;


(function() {
	tinymce.PluginManager.requireLangPack('annotation');


	/**
	 * initialization of plugin
 	 */
	tinymce.create('tinymce.plugins.AnnotationPlugin', {
		init : function(ed, url) {
			var t = this;

			t.ed = ed;
			t.url = url;

			if ( t.ed.settings.state == null)
				t.ed.settings.state = {};

			// Creates inner state and settings object and presets it
			var teds = t.ed.settings.state;

			if (teds.activeAnnotsList == null)
				teds.activeAnnotsList  = new Array();
			if (teds.annotationTypes == null)
				teds.annotationTypes   = new Array();
			if (teds.annotReqsQueue == null)
				teds.annotReqsQueue    = new Array();
			if (teds.annotChangesQueue == null)
				teds.annotChangesQueue = new Array();
			if (teds.annotServiceBusy == null)
				teds.annotServiceBusy  = false;
			if (teds.prevContent == null)
				teds.prevContent = "";
			if (teds.showSigns == null)
				teds.showSigns = false;
			if (teds.emphAnnotsInPar == null)
				teds.emphAnnotsInPar = true;
			/*if (teds.showAnnotsEmphPar == null)
				teds.emphAnnotsInPar = true;
			*/

			// t.annotServiceURL   = __SERVER_ANNOTATION_SERVICE;
			t.instid	    = ed.settings.instid;
			t.widgetid	    = ed.settings.widgetid;
			t.docuri	    = ed.settings.docuri;
			t.editmode          = ed.settings.editmode;
			t.state = {
				collapsed 	: false,
				range		: null,
				curoffset	: null,
				hidden		: false,
				lastnode	: null,
				loading		: true,
				removed		: false,
				checkChangesOnNodeChange: false,
				ignNextOnNodeChange	: false,
				movedAnnotationTag	: null
			};
						
			// Register listeners for plugin

			ed.onInit.add(function(ed) {
				if (ed.settings.content_css !== false)
					ed.dom.loadCSS(url + "/css/annots.css");
			});

			ed.onSubmit.add(function(t, ed, e) {
				t._saveContent();
				return true;
			}.partial(t));

			t.json = new tinymce.util.JSONRequest({
				url : t.annotServiceURL
			});

			ed.onRemove.add(function(t, ed) {
				t.ed.settings.editmode = ! t.state.hidden;
	   			t.state.removed = true;
			}.partial(t));


			ed.onClick.add(function(t, ed, e) {
				var teds = t.ed.settings.state;
				annotTools.insertLog('onClick');
				if (t.state.movedAnnotationTag != null || teds.showSigns){
					t.state.movedAnnotationTag = null;
					t._toggleShowSignsInText(false);
					t._refreshAnnotationBar();
				}
			}.partial(t));

			ed.onChange.add(function(t, ed, l) {
				annotTools.insertLog('onChange');
				t._onTextChange(null);			
			}.partial(t));


			ed.onKeyPress.add(function(t, ed, e) {
				var teds = t.ed.settings.state;
				if (t.state.movedAnnotationTag != null || teds.showSigns){
					t.state.movedAnnotationTag = null;
					t._toggleShowSignsInText(false);
					t._refreshAnnotationBar();
				}
				t._onTextChange( e );
			}.partial(t));


			ed.onInit.add(function(t, ed) {
				
				t.state.createlocal = true;
				
				annotationJSLib.t = t;
				annotationJSLib.ed = ed;
				
				var teds = t.ed.settings.state;
				t.ed.controlManager.setActive('annotshowpar', teds.emphAnnotsInPar);
				t.ed.controlManager.setActive('annotsigns', teds.showSigns);

				teds.activeAnnotsList = annotTools.deserializeAnnotations (t.ed.getDoc().body);
				
				if ( !teds.firstInitialize ) {
					t._createResetRequest();
					// t._synchronizeText( ed, t );
					
					// MS: hack, initialization from the modified content
					var innerTags = new Array();
					annotTools.getContentLengthForEachElement(t.ed.getBody(), innerTags);
					var parElems = t.ed.getBody().childNodes;
					teds.prevContent = '';
					for ( var i = 0; i<parElems.length; i++){
						annotTools.saveContentLength(parElems[i]);
						teds.prevContent += annotTools.backupParagraphData(parElems[i]);
					}
					// MS: end of hack
					
					t.loadAnnotationTypes();
				}

				
				
				/*for (var i=0; i < teds.activeAnnotsList.length; ++i) {
					alert(teds.activeAnnotati)
				}*/

				// Firefox 3+ supported listener!! 
				t.ed.getDoc().addEventListener('DOMSubtreeModified', function(t, e){
					
					if ( t.state.movedAnnotationTag != null ) {
						var eID = t.state.movedAnnotationTag;
						var startingElm = eID[1]=='s';
						var annotID = eID.replace(/^a[se]/,"");
						var newpos = annotTools.getElementPosition(t.ed.getBody(), 
							t.ed.getDoc().getElementById(t.state.movedAnnotationTag) );

						var annot = teds.activeAnnotsList[ t._getAnnotationIndex(annotID)];
						if (newpos && startingElm)
							annot.annotStart = newpos;
						else if (newpos && !startingElm)
							annot.annotEnd = newpos;

						if (annot.annotStart>annot.annotEnd){
							var tmp = annot.annotEnd;
							annot.annotEnd = annot.annotStart;
							annot.annotStart = tmp;
						}
					}

					if ( annotTools.hasAnnotationClass(e.target) ) {
						t.state.movedAnnotationTag = e.target.id;
					}
						
				}.partial(t), false);
					
				if (window.observer){
					observer.addVariableListener("mce-editor-command", function(event) { 
						// alert("on mce-editor-command");
						/*if(layoutManager.getComponent(t.widgetid) == null || 
							observer.getStateVariable('mce-editor-command').editorid  != t.instid )
			   				return;*/			
						
					t._processCommandFromWidgetsAPI( observer.getStateVariable('mce-editor-command') );
					});
				}
				else {
				}

				// Finally load content, alternativly hides an editor
				if ( /*t.docuri && t.docuri != "" && */ !t.ed.settings.firstInitialize ) {
					t.ed.settings.firstInitialize = true;
					// t._loadContent();
				}
				
				t._refreshAnnotationsTags ();
				t._refreshAnnotationBar();
			}.partial(this));

			ed.onNodeChange.add(function(t, ed, cm, e) {
				var teds = t.ed.settings.state;
				annotTools.insertLog('onNodeChange:'+e.nodeName)
				//if ( annotTools.hasAnnotationClass(e) )
				//	disableSelection(e);

				if (t.state.checkChangesOnNodeChange){
					t._synchronizeText(null) ;
					t.state.checkChangesOnNodeChange = false;
				}

				if (e && !t.state.ignNextOnNodeChange  && t.state.lastnode != e )
					t._refreshAnnotationBar(e, false);
				t.state.ignNextOnNodeChange = false;
	

				// Timeout hack becauseof mouse down-lossing during selecting
				if (teds.emphAnnotsInPar && t.state.lastnode != e)
					window.setTimeout( function () {t._emphAllAnnotsInPar(e)}, 10);

				//cm.setDisabled('annotmake', ed.selection.isCollapsed() );
				t._eventOnNodeChange(cm, e);
				t.state.lastnode = e;
			}.partial(t));


			ed.addButton('annotsigns', {
				title : 'annotation.show_annotation_tags',
				onclick : function() {
				// Display an alert when the user clicks the button				
					t._toggleShowSignsInText(!teds.showSigns) ;
				},
				image : url + '/img/annot.gif'
			});



			ed.addButton('annotshowpar', {
				title : 'annotation.show_annotation_in_paragraph',
				onclick : function() {
				
					//MS: for debugging
					var annots = t.ed.settings.state.activeAnnotsList;
					for (var i = 0; i < annots.length; ++i) {
						var a = annots[i];
						alert("annot " + a.id + " " + a.annotStart + " " + a.annotEnd);
					}
					//
				
				// Display an alert when the user clicks the button				
					t._toggleShowAnnotsInParagraph();
				},
				image : url + '/img/showpar.gif'
			});

			ed.addButton('annotmakeglobal', {
				title : 'annotation.make_annotation_global',
				onclick : function(){
					t.state.createlocal = false;
					t._openAnnotNewEditDlg(null);
				}, 
				image : url + '/img/add_page.png'
			});

			ed.addButton('annotmakelocal', {
				title : 'annotation.make_annotation_local',
				onclick : function(){
					t.state.createlocal = true;
					t._openAnnotNewEditDlg(null);
				}, 
				image : url + '/img/add.png'
			});

			ed.addButton('annotsave', {
				title : 'annotation.save_annotation_doc', 
				onclick : function(){
					t._saveContent();
					
				},	 
				'class': 'mce_save' 
			});

			ed.addButton('annotsettings', {
				title : 'annotation.open_settings', 
				onclick : function(){
						ed.windowManager.open({
						file : url + '/annotsettings.htm',
						width : 510 ,
						height : 280,
						inline : 1
					}, {
						plugin_url : url,
						settings   : {
							docuri : t.docuri,
							annotServiceURL : t.annotServiceURL,
							widgetid : t.widgetid 
					}
					});
				}, 
				image: url + '/img/settings.gif' 
			});


			// Context menu 
			t.onContextMenu = new tinymce.util.Dispatcher(this);

			ed.onContextMenu.add(function(t, ed, e) {
				if (!e.ctrlKey) {
					t._getMenu(ed).showMenu(e.clientX, e.clientY);
					tinymce.dom.Event.add(ed.getDoc(), 'click', hide);
					tinymce.dom.Event.cancel(e);
				}
			}.partial(t));
	
			function hide(t) {
				if (t._menu) {
					t._menu.removeAll();
					t._menu.destroy();
					tinymce.dom.Event.remove(ed.getDoc(), 'click', hide);
				}
				if (t._menuAnnots) {
					t._removeEmphAnnots(t.ed.getDoc().body);
					t._menuAnnots.removeAll();
					t._menuAnnots.destroy();
					tinymce.dom.Event.remove(ed.getDoc(), 'click', hide);
				}
			};

			ed.onMouseDown.add(hide.partial(t));
			ed.onKeyDown.add(hide.partial(t));	

			ed.onClick.add(function(t, ed, e) {
				//t._removeEmphAnnots(t.ed.getDoc().body);
				if (e.target.nodeName.toLowerCase() == 'span' && annotTools.hasAnnotationClass(e.target) ) {
					var annotid = e.target.id.replace(/as|ae/,'');
					t._emphAnnotation(annotid);
					t._getMenuAnnots(ed, annotid).showMenu(e.clientX, e.clientY);
					tinymce.dom.Event.add(ed.getDoc(), 'click', hide);
					tinymce.dom.Event.cancel(e);
				}
			}.partial(t));

			ed.addCommand('annotsCreateNew', function(t, ui, v) {
				t._annotateGo(v);
			}.partial(t));

			ed.addCommand('annotsRefreshStyles', function(t, ui, v) {
				t.loadAnnotationTypes();
				if (teds.showSigns) 		
					t._emphAllAnnotsInPar(null);

			}.partial(t));
		},


		/**
		 * Action performed on node change, backups actual state of carret.
 		 */
		_eventOnNodeChange : function (cm, e) {
			this.state.collapsed 	= this.ed.selection.isCollapsed();
			this.state.range	= this.ed.selection.getRng();
		},



		/**
		 * Function to toogle showing annotaiton emphasing in actual paragraph.
 		 */
		_toggleShowAnnotsInParagraph : function() {
			var t = this, ed = t.ed, teds = t.ed.settings.state;

			teds.emphAnnotsInPar = !teds.emphAnnotsInPar;
			ed.controlManager.setActive('annotshowpar', teds.emphAnnotsInPar);

			if (teds.emphAnnotsInPar) {				
				t._emphAllAnnotsInPar(null);
			} else {
				t._removeEmphAnnots(t.ed.getDoc().body);
			}
		},




		/**
		 * Function to toogle showing anotation marks in text.
 		 */
		_toggleShowSignsInText : function(val) {
			var t = this, ed = t.ed, teds = t.ed.settings.state;

			teds.showSigns = val;
			ed.controlManager.setActive('annotsigns', teds.showSigns);

			if (teds.showSigns) {				
				t._refreshAnnotationsTags();
			} else {
				annotTools.removeAnnotMarks(t.ed.getDoc().body);
			}
		},




		/**
		 * Removes all annots from text and then refresh then.
		 */
		_refreshAnnotationsTags : function(){
			var t = this;
			
			annotTools.removeAnnotMarks(t.ed.getDoc().body);
			t._markAllAnnotations();
		},

		/**
		 * Calls draw method for all annotations in the list.
		 */
		_markAllAnnotations : function() {
			var t = this;
			var teds = t.ed.settings.state;

			var i = 0;
			
			//for each draw...
			for (i=0; i< teds.activeAnnotsList.length; i++) {
				var annot = teds.activeAnnotsList[i];
				if (annot.state != __STATE_DENIED)
					t._makeAnnotationRange(annot);
				
			}
		},



		/**
		 * Creates sign for annotation in spans at the cursor possition placed by range param.
		 *
		 */
		_createAnnotationTag : function(id, textElem, textOffset, /*text, */className, isAtTheEndFlg) {
			var t = this;

			var tagName   = 'span';
			var tagSuffix = '';

			var s = t.ed.selection.getContent();

			if (className == 'annotation-start')
				h =  '<img src="js/tiny_mce/plugins/annotation/img/annotstart.gif" alt="ANNOTATION" class="' 
						+ ANNOTATION_CLASS + ' ' + className + '" id="' + id + '" />' 
			else

				h =  '<img src="js/tiny_mce/plugins/annotation/img/annotend.gif" alt="ANNOTATION" class="' 
						+ ANNOTATION_CLASS + ' ' + className + '" id="' + id + '" />' 
		
			t._insertNodeIntoTextElement( textElem, textOffset, h );
		
		},

		/**
		 * Prepares range object for sign and than calls placement of sign.
		 *
		 */
		_insertSign : function(id, pos, /*text,*/ className, isAtTheEndFlg) {
			var t = this;
			var offs = null;

			offs = annotTools.findPositionInDOM(t.ed.getDoc().body, pos);
			if ( !offs.elem || offs.elem.nodeName != '#text' ) {
				//alert('ue: Bad position for select: ' +offs.elem);
				return ;
			}



			t._createAnnotationTag(id, offs.elem, offs.offset, /*text, */className, isAtTheEndFlg);


			return;
		},

		/**
		 * Creates annotation signs around the text.
		 *
		 * @param annot annotation object to be placed 
		 */
		_makeAnnotationRange : function(annot) {
			var t = this, teds = t.ed.settings.state;

			var isAtTheEndFlg = false;
			if (teds.showSigns) {
				var selBck = t._mceBookmarkBugFix(t.ed.selection.getBookmark());

				// e[e|s] is id prefix for AnnotationStart and AnnotationEnd
				t._insertSign('ae' + annot.id, annot.annotEnd  , 'annotation-end',   isAtTheEndFlg);
				t._insertSign('as' + annot.id, annot.annotStart, 'annotation-start', isAtTheEndFlg);

				t.ed.selection.moveToBookmark(selBck);
			}
		},



		/**
		 * Emph annotation content if exists, annotID is annotation id. Function takes all
		 * tree successors of element elem and puts inner text into emph span element.
		 * Repeates it until offEnd wasnt found.
 		 */
		/*
		_emphAnnotationcontent : function(annotID, elem, offsEnd) {
			var t = this, teds = t.ed.settings.state;
			var annot = teds.activeAnnotsList[t._getAnnotationIndex(annotID)];
			var selTypeI = annotTools.getSelectedAnnotTypeIndex(teds.annotationTypes, annot.type);

			while (elem != offsEnd.elem) {

				if (elem.nextSibling) {// If had next siglink
					elem = elem.nextSibling;
				} else if (elem.parentNode) { // othervise go up
					elem = elem.parentNode;
					continue; // retry with parent step-left
				} else {
					// bad way in tree ??
					break;
				}

				// go down how its possible in tree

				while ( elem.nodeName != NODE_TEXT_TYPE ) {
					if ( elem.childNodes.length ){
						elem = elem.childNodes[0];
					} else
						break; // break if no text-node
				}

				
				

				if (elem.nodeName == NODE_TEXT_TYPE && elem != offsEnd.elem &&
						!annotTools.hasAnnotationClass(elem.parentNode) ) { // if is text-node
					e = t.ed.dom.create('span');
					tinymce.DOM.setAttrib(e, 'class', ANNOTATION_EMPH_CLASS);
					if (selTypeI >= 0){
						annotTools.setStyleToElement(e, teds.annotationTypes[selTypeI].style);
					}


					e.innerHTML = elem.nodeValue;
					t.ed.dom.replace(e, elem, 1);
					elem = e;
				}
			}

			if(offsEnd.elem)
				// finally selects last element
				t._replaceElementBySpan(offsEnd.elem,  
					'<span class="'+ANNOTATION_EMPH_CLASS+'" '
						+'style="'+ annotTools.serializeStyleObject(teds.annotationTypes[selTypeI].style) +'">' 	
					+offsEnd.elem.nodeValue.substr(0, offsEnd.offset) 
					+ '</span>'
					+ offsEnd.elem.nodeValue.substr(offsEnd.offset, offsEnd.elem.length
					)

				);

		},
		*/

		/**
		 * Initialize emphasing of annotation with annotID as its id. If annotation is over more
		 * than one element than calls _emphAnnotationContent which emph all next elements until end of annotation,
 		 */

		_emphAnnotation : function(annotID){

			var t = this, teds = t.ed.settings.state;
			var annot = teds.activeAnnotsList[t._getAnnotationIndex(annotID)];

			if (annot.annotStart==null || annot.annotEnd==null)
				return ;

			var selBck = t._mceBookmarkBugFix(t.ed.selection.getBookmark());

			annotTools.emphAnnotation(t.ed.getBody(), teds.annotationTypes, annot);

			t.ed.selection.moveToBookmark(selBck);
		},


		/**
		 * Select all undenied annotations in element elem and calls function to emphase them. bodyelem has to be
		 * body element of document.
 		 */
		_emphAllAnnotsInElement : function (elem, bodyelem) {
			var t = this, teds = t.ed.settings.state;
			
			t._removeEmphAnnots(bodyelem); 

			var parIntrv = annotTools.getParagraphInterval(elem, bodyelem);

			var i = 0;
			var selAnnots = annotTools.getAnnotsInRange(teds.activeAnnotsList, parIntrv.start, parIntrv.end);
			for (i=0; i< selAnnots.length; i++) {
				if (selAnnots[i].state != __STATE_DENIED)
					t._emphAnnotation(selAnnots[i].id);
			}		

		},



		/**
		 * Emphase all annotations in top-most paragraph getted by elem as any child of 
		 * this paragraph.
 		 */
		_emphAllAnnotsInPar : function(elem) {
			var t = this;

			// MS> hack to emph all annots
			//t._emphAllAnnotsInElement(t.ed.getDoc().body, t.ed.getDoc().body);
			//return;
			
			if (!elem)
				elem = t.ed.selection.getNode() ;

			// Finds top-most paragraph for actual caret possition
			while ( elem && t.ed.getBody() != elem.parentNode) {
				if (elem.parentNode)
					elem = elem.parentNode;
				else
					break;			
			}
			
			t._emphAllAnnotsInElement(elem, t.ed.getDoc().body);

		},



		/**
		 * Removed all emphases of annotations in element elem.
 		 */
		_removeEmphAnnots : function(elem){
			var t = this;

			var selBck = t._mceBookmarkBugFix(t.ed.selection.getBookmark());

			elem = annotTools.removeEmphAnnots(elem);

			t.ed.selection.moveToBookmark(selBck);

			return elem;

	
	},




		/* -----------------------------------------------------------------------------------------------*/
		/*          				TEXT MANIPULATION METHODS				  */
		/* -----------------------------------------------------------------------------------------------*/
		








		/**
		 * Event listener for text changes, call clinet-server synchronization func 
		 *
		 * @param {ed} editor object
		 * @param {t} plugin component
		 * @param {chr} typed character
		 */
		_onTextChange : function(chr) {
			var t = this;

			t._synchronizeText(chr);
			return null;		
		},



		/**
		 * Synchronize some block of document passed by ebl. forceRequest is co immediate send
		 * of any change to server. prevContent mean previous content on place of this block,
		 * actlContent means actual content. blOffs is offset of this block relatively to beginning.
 		 */
		__synchronizeBlock : function (ebl, forceRequest, prevContent, actlContent, blOffs, innerTags) {
			var t = this;
			var ed = t.ed;

			var diffStart = 0, diffEnd = 0;
			var i = 0;

			// Finds difference in block
			var shorterLen = Math.min(prevContent.length, actlContent.length );

			while ( i < shorterLen ) {
				if ( prevContent[i] != actlContent[i] ) {
					diffStart = i;
					break;
				}
				i++;
			}

			if ( diffStart == 0 && prevContent.length != actlContent.length )
				diffStart = i;

			var i = 0;
			var lengthOfSameEnd = 0;

			while ( shorterLen-i >= 0 && shorterLen-i >= diffStart) {
				diffEnd = actlContent.length-i;
				lengthOfSameEnd = i;
				if ( prevContent[prevContent.length-i-1] != actlContent[actlContent.length-i-1] ) {
					break;
				}
				i++;
			}

			// saves difference
			if (!(diffStart == 0 && actlContent.length-lengthOfSameEnd==0)) {
				var insText = actlContent.substr(diffStart,diffEnd-diffStart);
				t._addTextDifference(blOffs+diffStart, blOffs+prevContent.length-lengthOfSameEnd, blOffs+diffStart, 
					insText.length, insText, forceRequest, innerTags );
				return true;
			}

			return false; // no change


		},



		/**
		 * Synchronize content of paragraph par of the text. This is for time-optimising
		 * comparing while synchronizing the texts.
 		 */
		_synchronizeParagraph : function (par, forceRequest){
			var t = this, ed = t.ed;

			var retVal = false;

			if (!par)
				return;

			var parOffs = annotTools.getParagraphOffset(par);
			var prevContent = annotTools.getPrevParagraphData(par);
			prevContent = prevContent ? prevContent : '';
			var actlContent = annotTools.backupParagraphData(par);
			
			retVal = t.__synchronizeBlock(par, forceRequest, prevContent, actlContent, parOffs, null);

			if (retVal) // is any change??
				annotTools.saveContentLength(par, actlContent.length);

			return retVal;
		},



		/**
		 * Synchronize whole text. Means that its finding changed over whole
		 * document, not only some part. If longer text, more time-consuming function.
		 * Param e is unused param for event.
 		 */		
		_synchronizeWholeText : function(e) {
			var t = this, teds = t.ed.settings.state;
			var ed = t.ed;
			var isChanged = false;

			var prevContent = teds.prevContent,
			    actlContent = 
				annotTools.removeTagsFromHTML(
				  /*annotTools.removeAnnotTags*/(
				    ed.getBody().cloneNode(true)
				  ).innerHTML
				);

			var innerTags = new Array();
			annotTools.getContentLengthForEachElement(ed.getBody(), innerTags);
annotTools.insertLog('whole: '+actlContent);
			isChanged = t.__synchronizeBlock(t.ed.getBody(), true, prevContent, actlContent, 0, innerTags);
	
			// prepocita odstavce a zazalohuje jejich stavy
			var parElems = t.ed.getBody().childNodes;

			if (isChanged)
			{
				teds.prevContent = '';
				for ( var i = 0; i<parElems.length; i++){
					annotTools.saveContentLength(parElems[i]);
					teds.prevContent += annotTools.backupParagraphData(parElems[i]);				
				}
			}
		
		},

		/**
		 * Call this function in general change in text. Due to previous state of selection, etc. (means
		 * if collapsed or any function key pressed) decide, if use paragraph text comparing or full text
		 * comparing. Then makes update actions.
		 */

		_synchronizeText : function(e) {
			var t = this, teds = t.ed.settings.state;
			var ed = t.ed;
			var curoffset = 0;
			var fastRecogn = false;
			var fastRecogn = ( e!=null && !e.altKey && !e.ctrlKey &&  !t.state.checkChangesOnNodeChange &&
						t.state.collapsed && String.fromCharCode(e.which) != '' );
			annotTools.insertLog('test: '+t.state.collapsed + ' / fR: '+fastRecogn);


			//
			if ( fastRecogn && e && !t.state.collapsed){
				t.state.checkChangesOnNodeChange = true;
				// backup state
				this.state.collapsed 	= this.ed.selection.isCollapsed();
				this.state.range	= this.ed.selection.getRng();
				return;
			}

			// only local changes
			if (fastRecogn) {	
				var isChanged = t._synchronizeParagraph(
					annotTools.getParagraphElement(t.ed.getBody(), t.ed.selection.getRng().endContainer ),
					( e && String.fromCharCode(e.which).search(/\s|\.|\,|\!|\?/)==0 )
				);
				
				if (isChanged)
				{
					teds.prevContent = '';
					var parElems = ed.getBody().childNodes;
					for (var i = 0; i<parElems.length; i++ ){
						teds.prevContent += annotTools.getPrevParagraphData(parElems[i]);
					}
				}

			}else{ // global changes
				t._synchronizeWholeText(null);
			}


		},




		/**
		 * Adds new text difference to be send to server. Tries to compress this request to smaller number 
		 * if changes are inserting and on the same place. Means while typing longer text it is usefull
		 * to send changes not by chars but by words. Parameter localize new change of text to be added.
 		 */
		_addTextDifference : function (delStart, delStop, insStart, insLength, insText, forceRequest, innerTags ) {
			var t = this, teds = t.ed.settings.state;
			annotTools.insertLog('delStart: '+delStart+' delStop: '+delStop+' insStart: '
						+insStart+' insLength: '+insLength+' insText: \''+insText+'\' ');
			// Insert change into queue
			teds.annotChangesQueue.push(  
				{
					_delStart:  delStart,
					_delStop:   delStop,
					_insStart:  insStart,
					_insLength: insLength,
					_insText:   insText
				}
			);
			t._annotationsMove(delStart, (delStop-delStart), insStart, insLength);
			// try to compress 2 last requests
				
			if ( teds.annotChangesQueue.length>1 )
			{
				var difference1 = teds.annotChangesQueue[teds.annotChangesQueue.length - 2];
				var difference2 = teds.annotChangesQueue[teds.annotChangesQueue.length - 1];
		
				// conditions thats make compression possible to do
				if (    difference1._delStart   == difference1._delStop    && 
					difference2._delStart   == difference2._delStop    &&
					difference2._insLength  == 1                       &&
					difference1._insStart   == difference2._insStart - difference1._insLength
					) {
					// compression is possible
					teds.annotChangesQueue.pop();
					teds.annotChangesQueue.pop();
					teds.annotChangesQueue.push(  
						{
							_delStart:  difference1._delStart,
							_delStop:   difference1._delStop,
							_insStart:  difference1._insStart,
							_insLength: difference1._insLength+difference2._insText.length,
							_insText:   difference1._insText+difference2._insText
						}
					);			
				}
				else {
					// compression is not possible, force new request
					forceRequest = true;
				}
			}


			if ( forceRequest == true ) {
				var firstChangeObj = teds.annotChangesQueue.shift();
				t._createNewRequest(firstChangeObj._delStart, firstChangeObj._delStop, 
							firstChangeObj._insStart, firstChangeObj._insLength, 
							firstChangeObj._insText, innerTags);	 // nahrazeni carky za WhSp
			//				firstChangeObj._insText.replace(/\.|\,|\(|\)/g,' '),innerTags ); 
				t._doNextRequest();
			}
	
		},










		/* -----------------------------------------------------------------------------------------------*/
		/*          				JSON-LIST REQUEST METHODS				  */
		/* -----------------------------------------------------------------------------------------------*/
		









		/**
		 * Creates new synchronization request and adds its info synchro queue.
 		 */
		_createNewRequest : function (delStart, delStop, insStart, insLength, insText, innerTags ) {
			var t = this, teds = t.ed.settings.state;
			/*
			MS: disable hints for now...
			teds.annotReqsQueue.push( {method: 'hintsModifyTextWithHtml', 
				params: [delStart, delStop, insStart, insLength, insText, innerTags]} );	
			t._doNextRequest();*/
		},



		/**
		 * Creates new init-reset request and puts it into queue.
 		 */
		_createResetRequest : function ( ) {
			var t = this, teds = t.ed.settings.state;

			teds.annotReqsQueue.push( {method: 'hintsRestart', params: []} );
			t._doNextRequest();
		},



		/**
		 * Because of sending synchro-requests in right order, it can be send one-by-finished other. No all immediately.
		 * This function allows to send request only if channel if free, if not, waits in queue.
 		 */
		_doNextRequest : function () { 
			var t = this, teds = t.ed.settings.state;

			if ( !teds.annotServiceBusy && teds.annotReqsQueue.length ) {
				if (t.json) {
					try {
						var req = teds.annotReqsQueue.shift();

						
						/*annotTools.insertLog( "("+req.method+", "+
							req.params[0]+", "+req.params[1]+", "+req.params[2]+", "+req.params[3]+
							", "+req.params[4]+", '"+req.params[5]+"')" );*/
						


						// JSON request....
						teds.annotServiceBusy = true;
						annotComm.sendReq(
							req.method, 
							req.params, 
							function(result) {
								if (result)
								t._processJSONResponse(result);
								//document.getElementById('echo_buffer').innerHTML = result;
								teds.annotServiceBusy = false;
								t._doNextRequest();
							},
							function(e, x) {
								// Handle error
								teds.annotServiceBusy = false;
							}
						);	
						

					}
 					catch (e) {
      						alert("Server connection error!!."+e);
    					}
  				}
			}
		},




		/**
		 * Proccess response to hint server service. Adds new annots into array and emph them.
 		 */
		_processJSONResponse : function ( result ) { 
			var t = this, teds = t.ed.settings.state;
			
			var i = 0;
			if (result)
			{
				if (result.length && !teds.emphAnnotsInPar )
					t._removeEmphAnnots(t.ed.getDoc().body);

				for (i = 0; i<result.length; i++)
				{
					teds.activeAnnotsList.push( result[i] );

					// Vykonove narocne pri prichodu hodne anotaci
					if ( result.length - i < MAX_NEW_ANNOTS_SHOW_LIMIT)
						t._emphAnnotation(teds.activeAnnotsList[teds.activeAnnotsList.length-1].id);
				}
				if (result.length)
					t._refreshAnnotationBar( t.ed.selection.getNode(), true );
			}

		},







		/* -----------------------------------------------------------------------------------------------*/
		/*          			ANNOTATION LIST MANUPULATION METHODS				  */
		/* -----------------------------------------------------------------------------------------------*/
		





		/**
		 * Returns an index of annotation in annotation list passed by its id.
 		 */
		_getAnnotationIndex : function(annotID) {
			var t = this, teds = t.ed.settings.state;


			var i = 0;
			for (i=0; i< teds.activeAnnotsList.length; i++) {
				if ( teds.activeAnnotsList[i].id == annotID  ) {
					return i;
				}

			}
			return -1;
		},


		/**
		 * Moves borders of all annotations due to text changes. 
		 *
		 */
		_annotationsMove : function(delPos, delLen, insPos, insLen) {
			var t = this, teds = t.ed.settings.state;
			var chFlg = false;
			var i = 0;

			for (i=0; i< teds.activeAnnotsList.length; i++) {
				var annot = teds.activeAnnotsList[i];

				if (annot.annotStart == null || annot.annotEnd == null) // ignore whole-text annots
					continue; 

				if ( delPos < annot.annotStart  && delPos+delLen <= annot.annotStart  ) {
					// only move to front
					annot.annotStart -= delLen;
					annot.annotEnd   -= delLen;
				}
				else if ( delPos <= annot.annotStart && delPos+delLen >= annot.annotEnd) {
					// delete annot
					t._deleteAnnotation(annot.id);
					i--;
					chFlg = true;
					t._refreshAnnotationsTags();
					continue;
				}
				else if ( delPos <= annot.annotStart  && delPos+delLen > annot.annotStart && 
					delPos+delLen < annot.annotEnd  ) {
					// del starts before annot, ends before end of annot
					annot.annotStart  =  delPos;
					annot.annotEnd	=  ( annot.annotEnd - delLen );
					chFlg = true;	

				}

				else if (delPos > annot.annotStart && delPos < annot.annotEnd) {
					// del starts in the annotation
					annot.annotEnd 	= delPos + Math.max(0, annot.annotEnd-(delPos+delLen) );
					chFlg = true;	

				}

				if ( insPos < annot.annotStart  ) {
					// only move
					annot.annotStart += insLen;	
					annot.annotEnd   += insLen; 
	
			
				}
				else if ( insPos <= annot.annotEnd ) {
					annot.annotEnd 	+= insLen;
					chFlg = true;	


				}

			}
			if ( chFlg ) 
				t._refreshAnnotationBar( t.ed.selection.getNode(), true );		
		},



		/**
		 * Removes an annotation object passed by id from annotation list.
 		 */
		_deleteAnnotation : function(annotID) {
			var t = this, teds = t.ed.settings.state;

			teds.activeAnnotsList.splice(t._getAnnotationIndex(annotID),1);		
 	
			t._removeEmphAnnots(t.ed.getDoc().body);
			t._refreshAnnotationsTags();
		},

		_getNextUserAnnotID : function(annotList){

			var freeID = 0;

			for (var i = 0; i<annotList.length; i++){
				if (annotList[i].id.match==null)
					continue;
				if ( annotList[i].id.match( new RegExp('^'+__USER_ANNOTATION_ID_PREFIX+'[0-9]+$','') ) )	{
					var id = annotList[i].id.replace( new RegExp('^'+__USER_ANNOTATION_ID_PREFIX,''),'' );
					if (id>freeID)
						freeID = id;
				}

			}

			return __USER_ANNOTATION_ID_PREFIX+(++freeID);

		},

		/**
		 * Creates a new annotation around the selected text. Gets a position of selection
		 * and creates an annotation. Finally refresh annotation view.
 		 */
		_annotateGo : function (annot) {
			// alert("annotateGo");
			var t = this, teds = t.ed.settings.state;

			selPos = annotTools.getSelectionPosition(t.ed.getBody(), t.ed.selection);

			annot.id  	  = t._getNextUserAnnotID(teds.activeAnnotsList);
			if (t.state.createlocal || selPos==false ){
				annot.annotStart  = selPos.start;
				annot.annotEnd	  = selPos.end;
			}
			annot.state	  = __STATE_CONFIRMED;
			// Creates new annotation and adds it into list
 			teds.activeAnnotsList.push(annot);

			t._makeAnnotationRange(teds.activeAnnotsList[teds.activeAnnotsList.length-1]);
			t._refreshAnnotationBar( t.ed.selection.getNode(), true );
			
			// alert("created annotation " + annot.type + ", now have: " + teds.activeAnnotsList.length);
			
			return annot;
		},
	





		/**
		 * Makes link on place of annotaiton pro annotaiton URI.
 		 */
		_makeLinkFromAnnot : function(annot) {
			var t = this;
	
			if (!annot || !annot.uri) {
				alert('Error: No valid annotation selected.');
				return;
			}

			var range = t.ed.getDoc().createRange();

			var offsStart = annotTools.findPositionInDOM(t.ed.getDoc().body, annot.annotStart);
			var offsEnd   = annotTools.findPositionInDOM(t.ed.getDoc().body, annot.annotEnd);

			var selBck = tinyMCE.activeEditor.selection.getRng();
			try {
				if ( offsStart.elem.nodeName != '#text' || offsEnd.elem.nodeName != '#text' ) {
					//alert('ue: Bad position for select');
					return ;
				}
				range.setStart(offsStart.elem, offsStart.offset);
				range.setEnd(offsEnd.elem, offsEnd.offset);
				tinyMCE.activeEditor.selection.setRng(range);


				tinyMCE.activeEditor.execCommand('mceBeginUndoLevel');
				t.ed.execCommand("CreateLink", false, annot.uri, {skip_undo : 1});
				tinyMCE.activeEditor.execCommand('mceEndUndoLevel');
			}
			catch (e){ alert(e); }
			tinyMCE.activeEditor.selection.setRng(selBck);
		},






		/**
		 * Loads annotaiton types for the document from server.
 		 */
		loadAnnotationTypes : function(){
			var t = this, teds = t.ed.settings.state;

			annotComm.sendReq(
				'getAnnotationTypes', 
				[], 
				function(result) {
					// JSON RPC result
					teds.annotationTypes = result;
					
    				},
				function(e, x) {
					// Handle error
					t.ed.windowManager.alert('Error while loading types list!');
				}
			);	
		},








		/* -----------------------------------------------------------------------------------------------*/
		/*          				CONTEXT MENU METHODS					  */
		/* -----------------------------------------------------------------------------------------------*/






		/**
		 * Creates context menu.
 		 */
		_getMenu : function(ed) {
			var t = this, teds = t.ed.settings.state;

			var t = this, 
				m = t._menu, 
				se = ed.selection, 
				col = se.isCollapsed(), 
				el = se.getNode() || ed.getBody(), 
				am, p1, p2;

			if (m) {
				m.removeAll();
				m.destroy();
			}

			p1 = tinymce.DOM.getPos(ed.getContentAreaContainer());
			p2 = tinymce.DOM.getPos(ed.getContainer());

			m = ed.controlManager.createDropMenu('contextmenu', {
				offset_x : p1.x + ed.getParam('contextmenu_offset_x', 0),
				offset_y : p1.y + ed.getParam('contextmenu_offset_y', 0),
				constrain : 1
			});

			t._menu = m;

			m.add({title : 'advanced.cut_desc', icon : 'cut', cmd : 'Cut'}).setDisabled(col);
			m.add({title : 'advanced.copy_desc', icon : 'copy', cmd : 'Copy'}).setDisabled(col);
			m.add({title : 'advanced.paste_desc', icon : 'paste', cmd : 'Paste'});

			if ((el.nodeName == 'A' && !ed.dom.getAttrib(el, 'name')) || !col) {
				m.addSeparator();
				m.add({title : 'advanced.link_desc', icon : 'link', cmd : ed.plugins.advlink ? 'mceAdvLink' : 'mceLink', ui : true});
				m.add({title : 'advanced.unlink_desc', icon : 'unlink', cmd : 'UnLink'});
			}

			m.addSeparator();
			m.add({title : 'advanced.image_desc', icon : 'image', cmd : ed.plugins.advimage ? 'mceAdvImage' : 'mceImage', ui : true});

			m.addSeparator();
			am = m.addMenu({title : 'Annotations'});
			am.add({title : 'Refresh annotations', onclick : function (){ t._refreshAnnotationsTags(); } });
			m.addSeparator();

			// Select annotations, thats are including position of cursor
			var i = 0;
			var actPos = annotTools.getCursorPosition(t.ed.getDoc(), t.ed.selection);
			for (i=0; i< teds.activeAnnotsList.length; i++) {

				var annot = teds.activeAnnotsList[i];
				if ( annot.annotStart<=actPos && annot.annotEnd>actPos ) {
					if (annot.state == __STATE_DENIED)
						continue;
					cam = am.addMenu({title : 'Annotation "'+ annot.id +'" '});
					t._createMenuForAnnot(annot.id, cam);	
				}
			}


			t.onContextMenu.dispatch(t, m, el, col);

			return m;
		},


		/**
		 * Creates context menu part for one annotaiton passed by id.
 		 */
		_getMenuAnnots : function(ed, id) {

			var t = this, 
				m = t._menuAnnots, 
				se = ed.selection, 
				col = se.isCollapsed(), 
				el = se.getNode() || ed.getBody(), 
				am, p1, p2;

			if (m) {
				m.removeAll();
				m.destroy();
			}

			p1 = tinymce.DOM.getPos(ed.getContentAreaContainer());
			p2 = tinymce.DOM.getPos(ed.getContainer());

			m = ed.controlManager.createDropMenu('contextmenu', {
				offset_x : p1.x + ed.getParam('contextmenu_offset_x', 0),
				offset_y : p1.y + ed.getParam('contextmenu_offset_y', 0),
				constrain : 1
			});

			t._menuAnnots = m;

			t._createMenuForAnnot(id, m);

			t.onContextMenu.dispatch(t, m, el, col);

			return m;
		},



		/**
		 * Creates menu items for annotataion
 		 */
		_createMenuForAnnot : function(annotID, menuitem) {		
			var t = this, teds = t.ed.settings.state;

			if (  teds.activeAnnotsList[ t._getAnnotationIndex(annotID)].state == __STATE_UNCONFIRMED )
			menuitem.add({title : 'Commit annotation "'+ annotID +'" ', 
				onclick : function (){  
					teds.activeAnnotsList[ t._getAnnotationIndex(annotID)].state = __STATE_CONFIRMED;
					t._emphAnnotation(annotID);
					t._refreshAnnotationBar( t.ed.selection.getNode(), true );
				}
			} );

			menuitem.add({title : 'Delete annotation "'+ annotID +'" ', 
				onclick : function (){  
					t._deleteAnnotation  (annotID);} 
			} );	

		},



		/**
		 * Calls method to send state of editor annotaitons to other widgets and
		 * backups state of carret.
 		 */
		_refreshAnnotationBar : function(e, forceSend){
			var t = this;
			if (true || forceSend || t.prevCursorPosElement!=e)
				t._sendAnnotations(e);
			t.prevCursorPosElement=e;
		},






		/* -----------------------------------------------------------------------------------------------*/
		/*          				SUPPORT METHODS						  */
		/* -----------------------------------------------------------------------------------------------*/







		/**
		 * Returns new Range-object of document.
 		 */
		_createRange : function(){
			var t = this;
			return (t.ed.getDoc().createRange)?t.ed.getDoc().createRange():t.ed.selection.getSel().createRange();
		},



		/**
		 * Replaces element e with new span element with passed contend.
 		 */		
		_replaceElementBySpan : function (e, content) { 
			annotTools.replaceElementBySpan(e, content);
		},



		/**
		 * Method to insert element dirextly into text node textElem. New element
		 * will be inserted on possition textOffset and will be tHTML content.
 		 */
		_insertNodeIntoTextElement : function ( textElem, textOffset, tHTML ) {
			var t = this;

			t._replaceElementBySpan(textElem, textElem.nodeValue.substr(0, textOffset) 
					+ tHTML 	
					+ textElem.nodeValue.substr(textOffset, textElem.length)
				);
		},
		


		/**
		 * Fixes an bug of tinyMCE bookmark function. Also change source of tinyMCE!
 		 */
		_mceBookmarkBugFix : function (bmrk){

			// !!!!!!!!!!!!!!!!!!!!!!!!1
			// BUG FIX FOR TINYMCE - code of tinymce has to be modified!!! 
			// func getBookmark(), section // Caret or selection
			// into returned object has to be added properity wb:wb !!!!!!!!!
			if (bmrk && bmrk.wb) {
				bmrk.end += bmrk.wb; 
				bmrk.start += bmrk.wb;
			}
			// END: BUG FIX FOR TINYMCE !!! 			
		
			return bmrk;
		},

	

		_serializeAnnotations : function() {
			var t = this, teds = t.ed.settings.state;
			var annots = teds.activeAnnotsList;
			
			// MS: hack, at serialization annotations itself can't be in the content, because the content is modified and annotations would move also...
			teds.activeAnnotsList = [];
			
			annotTools.serializeAnnotations (t.ed.getDoc(), annots);
		
		},
	

		/**
		 * Saves content and annotations to server and switch to viewer-mode.
 		 */
		_saveContent : function() { 
			var t = this, teds = t.ed.settings.state;
			t._serializeAnnotations ();
			
			var doc  = t.ed.getDoc();
			teds.saving = 0;
			
			t.ed.save ();
			
			// TODO: do this in a standard complaint way
			// var xs = new XMLSerializer ();
			// var content = xs.serializeToString(doc.body);
			
			/*annotComm.sendReq(
				'setContent', 
				[content], 
				function(result) {
					// JSON RPC result
					/},
				function(e, x) {
					// Handle error
					alert('Error while saving! '+e);
				}
			);*/
			
			/*var t = this, teds = t.ed.settings.state;

			annotTools.removeAnnotTags(t.ed.getDoc().body);
			*/
			/*var content  = t.ed.getContent();
			teds.saving = 0;
			
			
			annotComm.sendReq(
				'setContentRemote', 
				[content], 
				function(result) {
					alert("result");
					// JSON RPC result
					//if ( ++teds.saving == 2)
					//	t._swapToViewMode(); 
    				},
				function(e, x) {
					// Handle error
					alert('Error while saving! '+e);
				}
			);
			annotComm.sendReq(
				'setAnnotations', 
				[teds.activeAnnotsList], 
				function(result) {
					// JSON RPC result
					if ( ++teds.saving == 2)
						t._swapToViewMode();					
    				},
				function(e, x) {
					// Handle error
					alert('Error while saving! '+e);
				}
			);*/

			// content send, continue....

		},



		/**
		 * Loads an annotaitons and content from the server.
 		 */
		_loadContent : function() {
			var t = this, teds = t.ed.settings.state;

            teds.activeAnnotsList = annotTools.deserializeAnnotations (t.ed.getDoc().body);

			
			
			/*annotComm.sendReq(
				'getContent', 
				[], 
				function(result) {
					// JSON RPC result
					t.ed.setContent(result);
					t._synchronizeText(null) ;

					//scroll if set
					if (t.ed.settings.scrolly)
						t.ed.getWin().scrollTo(0, t.ed.settings.scrolly);

					annotComm.sendReq(
						'getAnnotations', 
						[t.widgetid, t.docuri], 
						function(result) {
							// JSON RPC result
							if (result instanceof Array)
								teds.activeAnnotsList = result;
					
    						},
						function(e, x) {
							// Handle error
							t.ed.windowManager.alert('Error while loading content!');
						}
					);

					// Pokud nacitame a nema ze zobrazit editor,pak skryjeme
					if ( t.state.loading && !t.editmode || t.editmode == 0 ){
						t._swapToViewMode(); 
					}
					t.state.loading = false;
					///t._refreshAnnotationBar(t.ed.selection.getNode(), true);
					
    				},
				function(e, x) {
					// Handle error
					t.ed.windowManager.alert('Error while loading content!');
				}
			);*/
			t._synchronizeText(null);
			
			t._refreshAnnotationsTags();
			
			t._refreshAnnotationBar();
			
			/*annotComm.sendReq(
				'getAnnotations', 
				[], 
				function(result) {
					// JSON RPC result
					if (result instanceof Array)
						teds.activeAnnotsList = result;	
    				},
				function(e, x) {
					// Handle error
					t.ed.windowManager.alert('Error while loading content!');
				}
			);*/
		},






		/**
		 * Swaps editor to view mode. Creates listeners to restore editing mode and
		 * localize clicking while swapping into editing mode.
 		 */
		_swapToViewMode : function () {
			var t = this;

			// window.location.href = __VIEW_MODE_URI+'?editorid='+t.instid;

		},

		/* Prepinani javascriptem; Neprakticke pro prepinani (probliknuti pri deaktivaci), ale funguje...
		_swapToViewMode : function () {
			var t = this;

			var ediv = document.getElementById('viewer-'+t.instid.replace(/editor-/,""));
			ediv.style.display = 'block';
			var etextar = document.getElementById(t.ed.id);
			ediv.innerHTML = t.ed.getContent();
			var eeddiv = document.getElementById(t.instid);
			eeddiv.style.display = 'none';
			ediv.ondblclick = function () {
				eeddiv.style.display = 'block';
				// If editor was removed druring hide state, then add new instance of it
				if ( ! t.state.removed){
					var sel = annotTools.getSelectionPosition(ediv, 
						{
							getRng : function () { return window.getSelection().getRangeAt(0); }
						} );
					t.state,hidden = false;
					t.ed.show();

					// scroll to clicked position
					var range = t.ed.getDoc().createRange();

					var offsStart = annotTools.findPositionInDOM(t.ed.getDoc().body, sel.start);
					var offsEnd   = annotTools.findPositionInDOM(t.ed.getDoc().body, sel.end);
  
					var posY = 0;
					if (offsStart.elem) {
						posY = annotTools.getRenderedPositionY(offsStart.elem);
					}
					t.ed.getWin().scrollTo(0, posY);
				}else
					tinyMCE.execCommand('mceAddControl', false, t.ed.id);
				ediv.style.display = 'none';
			};
			t.ed.hide();
			t.state.hidden = true;

		},*/











		/* -----------------------------------------------------------------------------------------------*/
		/*    				WIDGETS API METHODS						  */
		/* -----------------------------------------------------------------------------------------------*/






		/**
		 * Send annotation state to other widgets by observer service.
 		 */
		_sendAnnotations : function(acte){

			var t = this, teds = t.ed.settings.state;

			var dom = t.ed.getDoc();//.cloneNode(true);

			var sobj =  {
				editorid : t.instid, 
				srcdom : dom,
				docposition : {
					elm: acte ? acte :t.ed.selection.getRng().endContainer, 
					offs: acte ? 0    :t.ed.selection.getRng().endOffset
				}, 
				annotationscoll : teds.activeAnnotsList,
				stylecoll : teds.annotationTypes
			};
			if (window.observer)
				observer.update(t.widgetid, 'annotation-set', sobj); 
	

		},



		/**
		 * Listening function to accept calls from other widgets over observer command object.
 		 */
		_processCommandFromWidgetsAPI : function( commandObj ) {
			var t = this, teds = t.ed.settings.state;
			
			// alert('processing cmds from widgets api');

			switch (commandObj.command) {
			
				case "annot-serialize":
				
					t._saveContent ();
				
					break;
			
				case "annot-serialize":
					
					t._serializeAnnotations ();
					
					break;

				case "annot-select":
									
					t._removeEmphAnnots(t.ed.getDoc().body);
					t._emphAnnotation( commandObj.params.annotID );

					t.state.ignNextOnNodeChange = true;

					// scroll to annot
					var offsStart = annotTools.findPositionInDOM(t.ed.getDoc().body, 
							      teds.activeAnnotsList[t._getAnnotationIndex(commandObj.params.annotID)].annotStart);
					var posY = 0;
					if (offsStart.elem) 
						posY = annotTools.getRenderedPositionY(offsStart.elem);
					t.ed.getWin().scrollTo(0, posY);

					break;

				case "annot-make-link":
					
					t._makeLinkFromAnnot(
					    teds.activeAnnotsList[t._getAnnotationIndex(commandObj.params.annotID)]
					  );

					break;

				case "annot-confirm":

					teds.activeAnnotsList[t._getAnnotationIndex(commandObj.params.annotID)].state=__STATE_CONFIRMED;
					t._refreshAnnotationBar( t.ed.selection.getNode(), true );

					break;

				case "annot-edit":

					annot = teds.activeAnnotsList[t._getAnnotationIndex(commandObj.params.annotID)];
					t._openAnnotNewEditDlg(annot);

					break

				case "annot-delete":

					teds.activeAnnotsList[t._getAnnotationIndex(commandObj.params.annotID)].state=__STATE_DENIED;
					t._refreshAnnotationBar( t.ed.selection.getNode(), true );

					break;
			
				case "swap-to-view-mode":

					t._swapToViewMode();

			}

		},


		_openAnnotNewEditDlg : function(annot){
			var t=this;
			t.ed.windowManager.open({
				file : t.url + '/annotnew.htm',
				width : 310 ,
				height : 200,
				inline : 1
			}, {
				plugin_url : t.url,
				settings   : {
					docuri : t.docuri,
					annotServiceURL : t.annotServiceURL,
					widgetid : t.widgetid,
					editAnnot : annot,
					ed:t.ed
			}
			});
		},


		/**
		 * Creates control instances based in the incomming name. This method is normally not
		 * needed since the addButton method of the tinymce.Editor class is a more easy way of adding buttons
		 * but you sometimes need to create more complex controls like listboxes, split buttons etc then this
		 * method can be used to create those.
		 *
		 * @param {String} n Name of the control to create.
		 * @param {tinymce.ControlManager} cm Control manager to use inorder to create new control.
		 * @return {tinymce.ui.Control} New control instance or null if no control was created.
		 */
		createControl : function(n, cm) {
			return null;
		},

		

	

		/**
		 * Returns information about the plugin as a name/value array.
		 * The current keys are longname, author, authorurl, infourl and version.
		 *
		 * @return {Object} Name/value array containing information about the plugin.
		 */
		getInfo : function() {
			return {
				longname : 'Annotation plugin for TinyMCE',
				author : 'Michal Sebek',
				authorurl : 'http://www.fit.vubtr.cz',
				infourl : 'http://www.fit.vutbr.cz',
				version : "1.0"
			};
		}

	});

	// Register plugin
	tinymce.PluginManager.add('annotation', tinymce.plugins.AnnotationPlugin);
})();

/**
 * Method for tinyMCE object, which converts setted my-editor-id to inner tinyMCE id.
 */
tinyMCE.getEditorIDByMyEditorID = function(myeditorid){
	var editors=this.editors;
	for (editor in editors)
		if ( 'editor-'+myeditorid == editors[editor].settings.instid )
			return editors[editor].id;

	return null;
};
