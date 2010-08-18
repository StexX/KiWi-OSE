/**
 * Plugin Editor-Annotation component for TinyMCE
 *    Support source file with shared functions and variables for widgets.
 * Michal Sebek, 2008
 * KiWi project 
 * www.fit.vutbr.cz
 * 
 * File contains objects annotTools, annotComm and other methods thats are shared
 * between components working with annotations. This lib has to be loaded.
 *
 * Last modif: 10.09.2008
 */


/** Shared variables and globals settings */
var ANNOTATION_CLASS		= 'annotation';
var ANNOTATION_EMPH_CLASS	= 'annot-emph';
var NODE_TEXT_TYPE 		= '#text';
var NODE_COMMENT_TYPE 		= '#comment';
var MAX_ANNOT_PREVIEW_LEN 	= 50;
// var __SERVER_ANNOTATION_SERVICE = "widgets/editor/service";
var __VIEW_MODE_URI		= "/index";
var __EDIT_MODE_URI		= "/edit";
var __VIEW_MODE_PREFIX		= "viewer-";
var __EDIT_MODE_PREFIX		= "editor-"
var __STATE_CONFIRMED   	= "confirmed";
var __STATE_UNCONFIRMED 	= "unconfirmed";
var __STATE_DENIED      	= "denied";

var __LOGGING			= false;



var __USER_ANNOTATION_ID_PREFIX		= 'user';
var __TAGS_TO_SEND_TO_HINTS_SERVICE 	= 'BR|P|H[1-6]';





/****** Preprocessing of set of inline elements ******/
var intr = 'A|BR|SPAN|BDO|OBJECT|APPLET|IMG|MAP|IFRAME|TT|I|B|U|S|STRIKE|BIG|SMALL|FONT|BASEFONT|EM|STRONG|DFN|CODE|Q|SAMP|KBD|VAR|CITE|ABBR|ACRONYM|SUB|SUP|INPUT|SELECT|TEXTAREA|LABEL|BUTTON|#text|#comment|BODY';

var inlines = new Array(), inlineTagsAr = intr.split('|');
var i;
for (i=0; i<inlineTagsAr.length; i++) {
	inlines[inlineTagsAr[i].toLowerCase()] = true;
}
/****** Preprocessing of set of inline elements ******/

/**
 * Object containing shared functions accesible by global variable annotTools.
 * 
 */
var annotTools = {



		/* -----------------------------------------------------------------------------------------------*/
		/*     				CURSOR AND TEXT SELECTION METHODS				  */
		/* -----------------------------------------------------------------------------------------------*/


		/**
		 * Find an pair text-element and offset into text-elment in element elem
		 * on position offset in linearised-text meaning of tree.
		 * Funciton have to be call throw function findPositionInDOM()
		 */
		__recursiveFindPositionInDOM : function(elem, offset) {
			var t = this;

			if (!elem)
				return {elem: null, offset:offset};

			var eChilds = elem.childNodes;
			var i = 0;

			var inlineElem = ( inlines[elem.nodeName.toLowerCase()] ) ? true : false;

			if ( !inlineElem  ) {
				--offset;
			}

			for (i=0; i< eChilds.length; i++) {
				if ( eChilds[i].nodeName != NODE_TEXT_TYPE) {
					if ( ! t.hasAnnotationClass(eChilds[i]) )
					{
						var fndRes = t.__recursiveFindPositionInDOM(eChilds[i], offset);
						if ( fndRes.elem )
							return fndRes;
						offset = fndRes.offset;
					}
				} else {
					/*if ( offset>eChilds[i].nodeValue.replace(/[\t\n\f\r]/g,' ').length ) {
						offset -= eChilds[i].nodeValue.replace(/[\t\n\f\r]/g,' ').length;	
						//alert(eChilds[i] + ': odecteno na ' + offset);					
					}*/
					if (offset > eChilds[i].nodeValue.length) {
						offset -= eChilds[i].nodeValue.length;
					}
					else {
						//alert('nalezeno');
						return { elem: eChilds[i], offset: Math.max(offset,0) };
					}
				}
			}

			if ( !inlineElem )
				--offset;

			return { elem: null, offset: offset };

		},		


		/**
		 * Returns 2-tuple text-element and offset meaning position of offset in linearised
		 * text of the body element. Searching is index-sequential (using paragraph dividing of text).
		 */
		findPositionInDOM : function(bodyElem, offset) {
			var t = this;
			var i = 0;

			while (bodyElem.childNodes.length>i && offset >= this.getContentLength(bodyElem.childNodes[i]) ) {
				offset -= this.getContentLength(bodyElem.childNodes[i++]);
			}

			var fndRes = t.__recursiveFindPositionInDOM(bodyElem.childNodes[i], offset);
			if (fndRes) {			
				return fndRes;
			}
			return null;
			
		},


		/**
		 * Returns offset position of carret in editor.
		 */
		getCursorPosition : function(doc, sel){
			var t = this;

			return  (t.recursiveGetContentLength(doc.body, 
					{pos: 0, found: false, fndElem: sel.getRng().endContainer} )).pos;	
					+ sel.getRng().endOffset;

		},

		/**
		 * Returns offset position of element in editor .
		 */
		getElementPosition : function(body, e){
			var t = this;
			var res =  (t.recursiveGetContentLength(body, {pos: 0, found: false, fndElem: e} ));
			if (!res.found)
				return false;
			return res.pos;

		},



		/**
		 * Returns offset positions of start and end of the selection object 
		 * of the editor. bodyElem is body element of document, sel is selection object.
		 */
		getSelectionPosition : function(bodyElem, sel){
			var t = this;
			this.insertLog(( sel.getRng().startContainter + sel.getRng().endContainer));

			if ( !sel.getRng().startContainter && !sel.getRng().endContainer)
				return false;

			var result = {start: 0, end: 0};
			var nodeStart 	= {container:null, offset: null},
			    nodeEnd	= {container:null, offset: null};

			nodeStart.container = sel.getRng().startContainer;
			nodeStart.offset    = sel.getRng().startOffset;


			if (sel.getRng().endContainer.nodeName == NODE_TEXT_TYPE) {
				nodeEnd.container = sel.getRng().endContainer;
				nodeEnd.offset    = sel.getRng().endOffset;
			} else {
				if( sel.getRng().endOffset>=sel.getRng().endContainer.childNodes.length ) {
					nodeEnd.container = sel.getRng().endContainer.childNodes[sel.getRng().endContainer.childNodes.length-1];
					nodeEnd.offset    = this.getContentLength(nodeEnd.container);
				} else {
					nodeEnd.container = sel.getRng().endContainer;
					nodeEnd.offset    = 0;

				}
			
			}


			result.start = (t.recursiveGetContentLength(bodyElem, 
					{pos: 0, found: false, fndElem: nodeStart.container} ));	


			result.end = (t.recursiveGetContentLength(bodyElem, 
					{pos: 0, found: false, fndElem: nodeEnd.container} ));	
		
			if (!result.start.found && ! result.end.found)
				return false;

			if (!result.start.found)
				result.start = result.end;
			if (!result.end.found)
				result.end = result.start;


			result.start = result.start.pos + nodeStart.offset;
			result.end   = result.end.pos + nodeEnd.offset;

			return result;
		},
		
		serializeAnnotations : function (dom, annots) {
			var body = dom.body;
			this.removeAnnotTags(body);

			var marks = [];
			for (var i=0; i< annots.length; i++) {
				var annot = annots[i];
				if (annot.state != __STATE_DENIED) {
					marks.push([annot.annotStart, 1, annot]);
					marks.push([annot.annotEnd, 0, annot]);
				}
			}
			
			marks.sort(function (a,b) { return b[0] - a[0];});
			
			this.serializeAnnotationsRecursive (body, marks, 0);
		},
		
		serializeAnnotationsRecursive : function (elem, marks, offset) {
			if (marks.length == 0) return offset;
			
			if (elem.nodeName == NODE_TEXT_TYPE) {
				var added = 0;
				var orig_len = elem.length;
				while (marks.length > 0 && (marks[marks.length - 1][0] - offset) <= (elem.length - added)) {		
					var mark = marks.pop();
					var pos = mark[0];
					var isEnd = mark[1] == 0;
					var annot = mark[2];
					var add = null;
					if (isEnd) {
						add = "[[BookmarkEnd:" + annot.id + "]]";
					}
					else {
						add = "[[BookmarkStart:" + annot.id + "]]";
					}
					
					elem.nodeValue = 
						elem.nodeValue.substr(0, pos - offset + added) + 
						add + 
						elem.nodeValue.substr(pos - offset + added, elem.length);

					added += add.length;
				}
				
				offset += orig_len;
			}
			else {
				var inlineElem = ( inlines[elem.nodeName.toLowerCase()] ) ? true : false;
				
				if (!inlineElem)
					++offset;
				
				var child = elem.firstChild;
	            while (child) {
	                var nextChild = child.nextSibling;
	                offset = this.serializeAnnotationsRecursive (child, marks, offset);
	                child = nextChild;
	            }
				
				if (!inlineElem)
					++offset;	
			}
			
			return offset;
		},

        deserializeAnnotations : function (dom) {
            var ret = new Array();
            var annots = new Object();
            this.deserializeAnnotationsRecursive (dom, 0, annots);
            for (var id in annots) {
                var a = annots[id];
                ret.push (a);
            }

            return ret;
        },
        
        deserializeAnnotationsRecursive : function (elem, offset, annots) {
        	if (elem.nodeName == NODE_TEXT_TYPE) {
        		var re_bookmark_start_1 = /\[\[\s*BookmarkStart:([^\]]+?)\s*\]\]/;
        		var re_bookmark_end = /\[\[\s*BookmarkEnd:([^\]]+?)\s*\]\]/;
        		
        		var orig_offset = offset;
        		var change = false;
        		var text = elem.nodeValue;
        		var result_text = "";
        		while(true) {
        			var m_bm_start_1 = re_bookmark_start_1.exec (text);
        			var m_bm_end = re_bookmark_end.exec (text);
        			
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
        			
        			// offset += bm_nearest_index;
        			if (bm_nearest == m_bm_start_1) {
        				var id = m_bm_start_1[1];
        				var annot = new Object();
        				annot.id = id;
        				annot.annotStart = offset + bm_nearest_index;
        				annot.type = "bookmark";
        				annot.uri = null;
        				annot.state = __STATE_CONFIRMED;
        				annots[id] = annot;
        			}
        			else if (bm_nearest == m_bm_end) {
        				var id = m_bm_end[1];
        				var annot = annots[id];
        				annot.annotEnd = offset + bm_nearest_index;
        			}
        			
        			result_text += text.substr(0, bm_nearest_index);
        			text = text.substr(bm_nearest_index + bm_nearest[0].length, text.length);
        			offset = offset + bm_nearest_index;
        		}
        		
        		offset += text.length;
        		result_text += text;
        		elem.nodeValue = result_text;
        	}
        	else {
        		var inlineElem = ( inlines[elem.nodeName.toLowerCase()] ) ? true : false;
						
				if (!inlineElem)
					++offset;
				
				var child = elem.firstChild;
	            while (child) {
	                var nextChild = child.nextSibling;
	                offset = this.deserializeAnnotationsRecursive (child, offset, annots);
	                child = nextChild;
	            }
				
				if (!inlineElem)
					++offset;	
        	}
        	
        	return offset;
        },

        /*deserializeAnnotationsRecursive : function (elem, offset, annots) {
            var t = this;
            if (!elem)
                return offset;

            // TODO: check namespace instead of prefix
            if (elem.localName == "annotationStart" && elem.prefix == "kiwi") {
                var a = new Object();
                a.id = elem.attributes["id"];
                a.type = elem.attributes["type"];
                a.uri = elem.attributes["uri"];
                a.annotStart = offset;
                annots[a.id] = a;

                // remove the element from the DOM
                elem.parentNode.removeChild (elem);
                return offset;
            }
            else if (elem.localName == "annotationEnd" && elem.prefix == "kiwi") {
                var a = annots[elem.attributes["id"]];
                a.annotEnd = offset;

                // remove the element from the DOM
                elem.parentNode.removeChild (elem);
                return offset;
            }

            var eChilds = elem.childNodes;
            var inlineElem = ( inlines[elem.nodeName.toLowerCase()] ) ? true : false;

			if ( !inlineElem  ) 		
				++offset;			

            var child = elem.firstChild;
            while (child) {
                var nextChild = child.nextSibling;
                offset = this.deserializeAnnotationsRecursive (elem, offset, annots);
                child = nextChild;
			}

			if (!eChilds.length && elem.nodeName == NODE_TEXT_TYPE)
				offset += elem.nodeValue.replace(/[\t\n\f\r]/g,'').length;

			if ( !inlineElem )
				++offset;
			
            return offset;
        },*/


		/**
		 * Returns length of content of element elem. Also during walking throw tree
		 * collects informations about visited elements into elemArray. This function
		 * have recursive kernel, time-consuming funciton.
		 */
		getContentLengthForEachElement : function(elem, elemArray){
			var t = this;

			return (t.recursiveGetContentLength(elem, 
				{pos: 0, found: false, fndElem: null}, elemArray )).pos;
		},



		/**
		 * Recursive kernel of funciton getContentLengthForEachElement(), can be called only
		 * throw this function. not directly!
		 */
		recursiveGetContentLength : function(elem, params, elemArray) {
			var t = this;
			var elemStart = 0, elemEnd = 0;

			if (!elem)
				return params;

			if (elem==params.fndElem) {
				params.found = true;
				return params;	
			}
			var eChilds = elem.childNodes;
			var i = 0;

			var inlineElem = ( inlines[elem.nodeName.toLowerCase()] ) ? true : false;

			elemStart = params.pos;

			if ( !inlineElem  ) 		
				++params.pos;			

			for (i=0; i< eChilds.length; i++) {
				if ( eChilds[i] == params.fndElem || params.found )
				{
					params.found = true;
					return params;
				}
				if ( eChilds[i].nodeName != NODE_TEXT_TYPE) {
					if ( ! t.hasAnnotationClass(eChilds[i]) )					
						params = t.recursiveGetContentLength(eChilds[i], params, elemArray);
					if (params.found)
						return params;
				} else {
					params.pos += eChilds[i].nodeValue.length;
					//params.pos += eChilds[i].nodeValue.replace(/[\t\n\f\r]/g,' ').length;
				}
			}

			if ( params.found )
				return params;

			if (!eChilds.length && elem.nodeName == NODE_TEXT_TYPE)
				params.pos += elem.nodeValue.length;
				//params.pos += elem.nodeValue.replace(/[\t\n\f\r]/g,' ').length;

			if ( !inlineElem )
				++params.pos;
			
			elemEnd = params.pos;

			// collects informations about visited nodes
			if (elemArray){
				if ( elem.nodeName.toLowerCase().match(new RegExp('^('+__TAGS_TO_SEND_TO_HINTS_SERVICE.toLowerCase()+')$', '')) )
					elemArray.push({
						type: 		elem.nodeName,
						annotStart: 	elemStart,
						annotEnd: 	elemEnd
						});
			}
			return params;

		},





		/* -----------------------------------------------------------------------------------------------*/
		/*          				SUPPORT METHODS						  */
		/* -----------------------------------------------------------------------------------------------*/





		/**
		 * Test if the element e has an annotation class means it is annotation sign.
		 *
		 * @param e tested elemenet
		 */
		hasAnnotationClass : function (e){
			return this.hasClass(e, ANNOTATION_CLASS);
		},


		/**
		 * Tests if the value is in array.
		 */

		isValueInArray : function(arr, value) {
			var t = this;

			var i = 0;
			for ( i = 0; i < arr.length; i++) {
				if (value.toLowerCase()==arr[i].toLowerCase())
					return true;
			}
			return false;
		},


		/**
		 * Removes all attributes of tags in innerHTML code passed by text and return result.
		 */
		removeTagAttributesFromHTML : function(text){
			if (!text)			
				return '';
			// Replace spaces and removes unprintable chars or declaration marks
			text = text.replace(/&nbsp;/g," ")/*.replace(/[\t\n\f\r]/g,'')*/;
			text = text.replace( /<![^>]*>/g, "" ); // decl. tags - comments etc.
			
			return text.replace( new RegExp('<(/?)([a-zA-Z0-9]+)[\ \s][^>/]*(/?)>','ig'),"<$1$2$3>");
		},


		/**
		 * Returns string without any tags. This function is main in linearisation process of DOM
		 * into plain text. Block elements substitues by space, line elements removes.
		 */
		removeTagsFromHTML : function(text) {
			if (!text)			
				return '';
			
			// Replace spaces and removes unprintable chars or declaration marks
			text = text.replace(/&nbsp;/g," ")/*.replace(/[\t\n\f\r]/g,'')*/;
			text = text.replace( /<![^>]*>/g, "" ); // decl. tags - comments etc.
			
			// replace inline tags by nothing
			text = text.replace(new RegExp('</?('+intr.toLowerCase()+')(\ [^>]*/?>|/?>)', 'g'),"");
			// replace by space
			text = text.replace(/<[^>]*>/g," ");

			return text;
		},


		/**
		 * Removes all annotation signs from element elem in DOM.
		 *
		 * @param elem element in which tag will be removed
		 */
		removeAnnotMarks : function(elem) {
			//return elem;

			var t = this;

			var i, j;

			if (!elem || !elem.getElementsByTagName )
				return '';

			var emphEl = elem.getElementsByTagName('img');
			for (i=0; i<emphEl.length; i++){
				if ( t.hasAnnotationClass(emphEl[i]) ) {
					emphEl[i].parentNode.removeChild(emphEl[i]);
					i--;
				}
					
			}

			return elem;
		},



		/**
		 * Creates and returnes SPAN element with set properties to emphase annotation.
		 */
		_getAnnotSpanObj : function (annotationTypes, annot, content){
			var selTypeI = annotTools.getSelectedAnnotTypeIndex(annotationTypes, annot.type);

			var e = tinymce.DOM.create('span');
			tinymce.DOM.setAttrib(e, 'class', ANNOTATION_EMPH_CLASS);
			if (selTypeI >= 0){
				annotTools.setStyleToElement(e, annotationTypes[selTypeI].style);
			}
			e.innerHTML = content;
			if (annot.label)
				e.title = annot.label;
			if (annot.note)
				e.title += ((e.title!=null)?" / ":"")+annot.note;

			return e;
		},


		/**
		 * Emph annotation content if exists, annotID is annotation id. Function takes all
		 * tree successors of element elem and puts inner text into emph span element.
		 * Repeates it until offEnd wasnt found.
 		 */
		_emphAnnotationcontent : function(annotationTypes, annot, elem, offsEnd) {

			// var selTypeI = annotTools.getSelectedAnnotTypeIndex(annotationTypes, annot.type);

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
					!annotTools.hasAnnotationClass(elem.parentNode) &&  // if is text-node
					elem.nodeValue.length != 0 ){
						var e = this._getAnnotSpanObj(annotationTypes, annot, elem.nodeValue);
						tinymce.DOM.replace(e, elem, false);
						elem = e;				
				}
			}

			if(offsEnd.elem)
				// finally selects last element
				var e = this._getAnnotSpanObj(annotationTypes, annot, offsEnd.elem.nodeValue.substr(0, offsEnd.offset) );
				var textAfter = document.createTextNode( offsEnd.elem.nodeValue.substr(offsEnd.offset, offsEnd.elem.length) );
				tinymce.DOM.replace(textAfter, offsEnd.elem, false);
				textAfter.parentNode.insertBefore(e, textAfter);


		},


		/**
		 * Initialize emphasing of annotation with annotID as its id. If annotation is over more
		 * than one element than calls _emphAnnotationContent which emph all next elements until end of annotation,
 		 */
		emphAnnotation : function(bodyElem, annotationTypes, annot){
			if (annot.annotStart==null || annot.annotEnd==null)
				return ;

			// var selTypeI = annotTools.getSelectedAnnotTypeIndex(annotationTypes, annot.type);

			var offsStart = annotTools.findPositionInDOM(bodyElem, annot.annotStart);
			var offsEnd = annotTools.findPositionInDOM(bodyElem, annot.annotEnd);
			
			var endPos = 0;
			if (offsStart.elem != offsEnd.elem)
				endPos = offsStart.elem.length;
			else
				endPos = offsEnd.offset;

			if ( offsStart.elem == offsEnd.elem || (offsStart.elem != offsEnd.elem && offsStart.offset != endPos-offsStart.offset) ) {
				var e = this._getAnnotSpanObj(annotationTypes, annot, offsStart.elem.nodeValue.substr(offsStart.offset, endPos-offsStart.offset) );
				var textBefore = document.createTextNode(offsStart.elem.nodeValue.substr(0, offsStart.offset));
				var textAfter = document.createTextNode(offsStart.elem.nodeValue.substr(endPos, offsStart.elem.length-endPos));

				tinymce.DOM.replace(textAfter, offsStart.elem, false);
				textAfter.parentNode.insertBefore(textBefore, textAfter);				
				textAfter.parentNode.insertBefore(e, textAfter);

				var offsStart = annotTools.findPositionInDOM(bodyElem, annot.annotStart);
				var offsEnd = annotTools.findPositionInDOM(bodyElem, annot.annotEnd);

			}
	
			if (offsStart.elem != offsEnd.elem) {
				this._emphAnnotationcontent(annotationTypes, annot, offsStart.elem, offsEnd);
			}


		},




		/**
		 * Removes all annotation emphases in subtree of element elem.
		 */
		removeEmphAnnots : function(elem) {
			var i, j;

			var emphEl = elem.getElementsByTagName('span');
			for (i=0; i<emphEl.length; i++){
				if ( this.hasClass(emphEl[i], ANNOTATION_EMPH_CLASS) ) {
					while (emphEl[i].childNodes.length) {
						var tmpEl = emphEl[i].firstChild;
						emphEl[i].removeChild(tmpEl);
						emphEl[i].parentNode.insertBefore(tmpEl, emphEl[i]);
					}
					emphEl[i].parentNode.removeChild(emphEl[i]);
					i--;
				}
					
			}

			return elem;

		},



		/**
		 * Removes all annotation signs and emphases in subtree of element elem.
		 */		
		removeAnnotTags : function(elem){
			this.removeEmphAnnots(elem);
			this.removeAnnotMarks(elem);
			return elem;
		},


		/**
		 * Test, if element n has classname c.
		 */
		hasClass : function(n, c) {
			
			if (!n || !n.className || !c)
				return false;

			return (' ' + n.className + ' ').indexOf(' ' + c + ' ') !== -1;
		},


		/**
		 * Returns index of selected annotatation givven ba ftype name.
		 */
		getSelectedAnnotTypeIndex : function(annotationTypes, ftype){	
			for (i=0; i<annotationTypes.length; i++){
				if ( annotationTypes[i].type == ftype ) {
					return i;
				}
			}

			return -1;

		},

		/**
		 * Replaces element e with new span element with passed contend.
 		 */
		replaceElementBySpan : function (e, content) { 
			var t = this, espan = null;
			var tmpTagName = '__tmp_tag';

			espan = tinymce.DOM.create('span');
			espan.id = tmpTagName;
			espan.innerHTML = content;
			e.parentNode.replaceChild(espan, e);
			
			tinymce.DOM.remove( tmpTagName , true);
		},


		/**
		 * Sets style preperties to element e.
		 */
		setStyleToElement : function(e, style){
			if (!style)
				return;
			e.style.fontSize 		= style.fontSize;
			e.style.fontWeight 		= style.fontWeight;
			e.style.fontStyle 		= style.fontStyle;
			e.style.textTransform 		= style.textTransform;
			e.style.color 			= style.color;
			e.style.backgroundColor		= style.backgroundColor;
			e.style.borderBottomStyle	= style.borderBottomStyle;
			e.style.borderBottomWidth	= style.borderBottomWidth;
			e.style.borderBottomColor	= style.borderBottomColor;
		},


		/**
		 * Serualize all style properities given by style to style-string.
		 */
		serializeStyleObject : function(style){
			if (!style)
				return '';

			return    ';font-size: '+style.fontSize
				+ ';font-weight: '+ style.fontWeight
				+ ';font-style: ' + style.fontStyle
				+ ';text-transform: ' + style.textTransform
				+ ';color: '+ style.color
				+ ';background-color: ' + style.backgroundColor
				+ ';border-bottom-style: ' + style.borderBottomStyle
				+ ';border-bottom-width: ' + style.borderBottomWidth
				+ ';border-bottom-color: ' + style.borderBottomColor;
		},

		
		/**
		 * Returns list of annotations that are (particulary or completely) in given range.
		 * Annots-list of annotations to test, rangeStart-rangeEnd - range to be tested.
		 */
		getAnnotsInRange : function(annots, rangeStart, rangeEnd) {

				var selannots = new Array();

				for (i=0; i< annots.length; i++) {
					var annot = annots[i];
					if (annot.state == __STATE_DENIED || annot.annotStart==null || annot.annotEnd==null)
						continue;
					if (	
					    (annot.annotStart <= rangeStart && annot.annotEnd >= rangeStart)||
					    (annot.annotStart <= rangeEnd   && annot.annotEnd >= rangeEnd)  ||
					    (annot.annotStart >= rangeStart && annot.annotEnd <= rangeEnd) 
					   ){
						selannots.push(annot);
					}
				}

				return selannots;
		},
		

		/**
		 * Returns start-end interval of element elem relativelly to inElem node.
		 */
		getElementInterval : function (elem, inElem) {

			var intrv = {start:0, end:0};

			// Gets an interval of paragraph content text
			intrv.start = (annotTools.recursiveGetContentLength(inElem, 
				{pos: 0, found: false, fndElem: elem} )).pos;

			
			if (elem.nextSibling)
				intrv.end = (annotTools.recursiveGetContentLength(inElem, 
					{pos: 0, found: false, fndElem: elem.nextSibling} )).pos-1;
			else
				intrv.end = (annotTools.recursiveGetContentLength(inElem, 
					{pos: 0, found: false, fndElem: null} )).pos;


			return intrv;
		},


		/**
		 * Returns interval of paragraph epar in body.
		 */
		getParagraphInterval : function (epar) {
			var intrv = {start:0, end:0};

			// Gets an interval of paragraph content text
			intrv.start = this.getParagraphOffset(epar);
			intrv.end = intrv.start+this.getContentLength(epar)-1;


			return intrv;
			
		},


		/**
		 * Returns top-most paragraph of subtree given by elem. Result will
		 * be (has to be) son of bodyElem.
		 */
		getParagraphElement : function (bodyElem, elem) {
			while(elem && elem.parentNode != bodyElem)
				elem = elem.parentNode;
			return elem;

		},


		/**
		 * Saves content of paragraph epar.
		 */
		backupParagraphData: function(epar) {

			if (!epar)
				return;

			if (epar.nodeName != NODE_TEXT_TYPE)
				setUserData(epar, 'prevContent', 
					annotTools.removeTagsFromHTML(
					  getOuterHTML(
						/*annotTools.removeAnnotTags*/(
					    	  epar.cloneNode(true)
				        	)
				  	)
				), null);
			else
				setUserData(epar, 'prevContent', annotTools.removeTagsFromHTML( new String(epar.nodeValue) ), null );

			return this.getPrevParagraphData(epar);
		},


		/**
		 * Returns previous saved data about paragraph epar.
		 */
		getPrevParagraphData : function(epar) {
			return getUserData(epar, 'prevContent');
		},


		/**
		 * Return offset of paragraph epar.
		 */
		getParagraphOffset : function (epar) {
			var offs = 0;

			if (!epar)
				return 0;
			
			while ( (epar=epar.previousSibling) != null ){
				offs += this.getContentLength(epar);
			}
			return offs;

		},
	

		/**
		 * Saves length of elem. LEngth is optional, if null, than will be
		 * automaticly recognized (slower!!).
		 */
		saveContentLength : function(elem, length){
			var elemArray = new Array();

			if (length == null)
				length = this.getContentLengthForEachElement(elem, elemArray);
			setUserData(elem, 'length', length );	
		},


		/**
		 * Returns saved content length of paragraph.
		 */
		getContentLength : function(elem){	
			if (  getUserData(elem, 'length') == null )
				return this.getContentLengthForEachElement(elem);
			return  getUserData(elem, 'length');
		},


		/**
		 * Returns y-position relativelly to eroot, where e has been rendered.
		 */
		getRenderedPositionY : function(e, eroot){
			var posY = 0;
			if (!(e=e.parentNode))
				return 0;

			while( e && e != eroot){
    				posY += e.offsetTop;
				e = e.offsetParent;
			}

			return posY;
			
		},


		/**
		 * Loads content on docuri as content od blElem.
		 */
		loadContent : function(widgetid, docuri, blElem){

			annotComm.sendReq(
				'getContent', 
				[], 
				function(result) {
					// JSON RPC result
					blElem.innerHTML = result;
					
    				},
				function(e, x) {
					// Handle error
					alert('Error while loading content!');
				}
			);


		},





		/**
		 * Insert message into log if exists.
 		 */
		insertLog : function(mess){ 
			if (__LOGGING && document.getElementById('logs'))
				document.getElementById('logs').innerHTML = 
					(mess +"<br>"+document.getElementById('logs').innerHTML).substr(0, 1000);
		}
};


/**
 * Initialize viewer for displaying content.
 */
function initViewMode(widgetid, docuri, editorid){
	var vdiv = document.getElementById(__VIEW_MODE_PREFIX+editorid);
	vdiv.style.display = 'block';
	annotTools.loadContent(widgetid, docuri, vdiv);	


	ne = document.createElement("input");
	ne.type = "button";	
	ne.value= "show annotations";
	ne.onclick= function (e) {

		annotComm.sendReq(
			'getAnnotationTypes', 
			[], 
			function(result) {
				// JSON RPC result
				var annotationTypes = result;
				annotComm.sendReq(
					'getAnnotations', 
					[], 
					function(result) {
						// JSON RPC result
						if (result instanceof Array) {
							for (var i=0;i<result.length;i++)
								annotTools.emphAnnotation(vdiv, annotationTypes, result[i]);
						}
						
	    				},
					function(e, x) {
						// Handle error
						alert('Error while loading annotations!');
					}
				);
				
    			},
			function(e, x) {
				// Handle error
				alert('Error while loading types list!');
			}
		);	

	};

	vdiv.parentNode.appendChild(ne);

	ne = document.createElement("input");
	ne.type = "button";	
	ne.value= "Edit mode...";
	ne.onclick= function (e) {
		window.location.href=__EDIT_MODE_URI+'?editorid='+editorid;
	};
	vdiv.parentNode.appendChild(ne);

	
	vdiv.ondblclick = function (e){

		var sel = annotTools.getSelectionPosition(vdiv, {
				getRng : function () { return window.getSelection().getRangeAt(0); }
			} );
		// scroll to clicked position
		var range = document.createRange();
		var offsStart = annotTools.findPositionInDOM(vdiv, sel.start);
		var offsEnd   = annotTools.findPositionInDOM(vdiv, sel.end);
  
		var posY = 0;
		posYDiv = annotTools.getRenderedPositionY(vdiv, vdiv);
		if (offsStart.elem) {
			posY = annotTools.getRenderedPositionY(offsStart.elem);
		}
		window.location.href = __EDIT_MODE_URI+'?editorid='+editorid+'&scrolly='+(posY-posYDiv);

	}
}


/**
 * Initialize and starts editor to text edit-mode.
 */ 
function initEditMode(layoutManager, widgetid, docuri, editorid, scrolly){
	var myWidget = layoutManager.getComponent(widgetid);

	myWidget.add(
		new Ext.form.FormPanel({	
			autoHeight: true,
			autoScroll: true,
			items: [ {
				xtype		: "tinymce",
				fieldLabel	: "Rich text",
				hideLabel	: true,
				id		: __EDIT_MODE_PREFIX+editorid,
				name		: __EDIT_MODE_PREFIX+editorid,
				width		: 690,
				height		: 500,
				tinymceSettings: {
					instid 		: __EDIT_MODE_PREFIX+editorid,
				 	widgetid 	: widgetid,
					docuri 		: docuri,
					editmode	: 1,
					scrolly		: scrolly,
				 	theme 		: "advanced",
					plugins 	: 
						"test1,annotation,safari,pagebreak,style,layer,table,save,advhr,advimage"+
						",advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace"+
						",print,paste,directionality,noneditable,visualchars,nonbreaking,xhtmlxtras,template",
				 	// Theme options
					theme_advanced_buttons1 : 
						"annotmakelocal,annotmakeglobal,annotsave,annotsettings,annotshowpar,annotsigns,|,styleprops"+
						",newdocument,preview,|,cut,copy,paste,pastetext,pasteword,charmap,|,search"+
						",replace,|,undo,redo,|,link,unlink,anchor,image,hr,removeformat,help,code",
					theme_advanced_buttons2 : 
						"formatselect,fontselect,fontsizeselect,|,bold,italic,underline,strikethrough,|"+
						",justifyleft,justifycenter,justifyright,justifyfull,|,bullist,numlist,|,sub,sup,|"+
						",outdent,indent,blockquote,cite",
				 	theme_advanced_buttons3 : ""/*tablecontrols"*/,
				 	theme_advanced_buttons4 : "",
				 	theme_advanced_toolbar_location : "top",
				 	theme_advanced_toolbar_align : "left",	
				 	extended_valid_elements : 
						"a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|"+
						"height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|"+
						"color|style],span[class|align|style]",
				 	template_external_list_url : "example_template_list.js"
				 	}
			}]
		})
	,0);

	myWidget.doLayout();

	var editorids = new Array();

	function stopEditor(myeditorid){
		editorids[myeditorid] = tinyMCE.getEditorIDByMyEditorID(myeditorid);
		tinyMCE.settings = tinyMCE.editors[editorids[myeditorid]].settings;
		tinyMCE.execCommand('mceRemoveControl', false, editorids[myeditorid]);
	}
	function startEditor(myeditorid){
		//var editorid = tinyMCE.getEditorIDByMyEditorID(myeditorid);
	 	tinyMCE.execCommand('mceAddControl', false, editorids[myeditorid]);
	}	
				 
	observer.addListener("layoutReady", function(event) {				 				 
		if (Ext.isIE6) 
			return;
		
		myWidget.addListener('beforecollapse',	function(){stopEditor(editorid); });
		myWidget.addListener('collapse',	function(){startEditor(editorid);});
		myWidget.addListener('beforeexpand',	function(){stopEditor(editorid); });
		myWidget.addListener('expand',		function(){startEditor(editorid);});
		myWidget.dd.onMouseDown = 		function(){stopEditor(editorid); };
		myWidget.dd.onMouseUp = 		function(){startEditor(editorid);};


		layoutManager.registerEventListener(widgetid,"layoutChanged", function(event) {
			if (layoutManager.getComponentLocation(widgetid).area != event.areaLocation){
				return;
			}
			if (event.action == "areaWillBeCollapsed")	{ stopEditor(editorid);  }
			else if (event.action == "areaCollapsed") 	{ startEditor(editorid); }
			else if (event.action == "areaWillBeExpanded") 	{ stopEditor(editorid);  }
			else if (event.action == "areaExpanded") 	{ startEditor(editorid); }	 	
		});
	});

}


/**
 * Function inserts node nodeNew before node in DOM.
 */
function insertAfter(nodeNew, node) {

    parentNode = nodeNew.parentNode;

    if ( !node.nextSibling )
        node.parentNode.appendChild(nodeNew);
    else
        node.parentNode.insertBefore(nodeNew, node.nextSiblink);

}


/**
 * Function simulates outerHTML property of element. Return outerHTML of tdom element.
 */
function getOuterHTML(tdom) {

	if ( !tdom || inlines[tdom.nodeName.toLowerCase()] ) return tdom.innerHTML;

	var ncnt = document.createElement( 'div' );

	ncnt.appendChild(tdom);

	return ncnt.innerHTML;

}


/**
 * Disables selection on tagret element.
 */
function disableSelection(target){
	if (typeof target.onselectstart!="undefined") //IE route
		target.onselectstart=function(){return false}
	else if (typeof target.style.MozUserSelect!="undefined") //Firefox route
		target.style.MozUserSelect="none"
	else //All other route (ie: Opera)
		target.onmousedown=function(){return false}
	target.style.cursor = "help"
}

function describe (obj, s) {
	var ret = s + "type: " + typeof(obj) + " {\n";
	if (obj instanceof Array) {
		ret += "is Array\n";
	}
	if (obj instanceof g_top_window.Seam.Remoting.Map) {
		ret += "is Seam.Remoting.Map\n";
	}
	
	for (var i in obj) {
		ret += s + s;
		ret += "" + i + ": " + typeof(obj[i]) /*describe(obj[i], s + "  ")*/ + "\n";
	}
	ret += "}\n";
	
	return ret;
}

function json2seam (obj) {

    if (obj == null) return null;
    
    if (typeof(obj) == "object") {
        if (obj instanceof Array) {
            var ret = new Array();
            for (var i = 0; i < obj.length; ++i) {
                ret[i] = json2seam(obj[i]);
            }
            
            return ret;
        }
        else {
            // non-array objects as a Map
            var ret = new g_top_window.Seam.Remoting.Map();
            for (var i in obj) {
                ret.put(i, json2seam(obj[i]));
            }
            
            return ret;
        }
    }
    
    return obj;
}

function seam2json (obj) {
    
    if (obj == null) return null;
    
    if (typeof(obj) == "object") {
        if (obj instanceof Array ) {
            var ret = new Array();
            for (var i = 0; i < obj.length; ++i) {
                ret[i] = seam2json(obj[i]);
            }
            
            return ret;
        }
        else if (obj instanceof g_top_window.Seam.Remoting.Map) {
            var ret = new Object();
            var keyset = obj.keySet();
            for (var i = 0; i < keyset.length; ++i) {
                ret[keyset[i]] = seam2json(obj.get(keyset[i]));
            }
            
            return ret;
        }
        else {
            alert ("unknown object type");
        }
    }
    
    return obj;
}


/**
 * This object abstracting sending requests to server. Middleman between application
 * and server-side protocol. 
 * Actually used JSON protocol
 *
 * Parameters:
 *  method       - remote method to call
 *  paramsArray  - array of parameters of remote procedure
 *  fnSucc       - callback fnc on success
 *  fnErr        - callback fnc on error
 */
var annotComm = {

	sendReq: function(method, paramsArray, fnSucc, fnErr) {
	
		var myCallbackSucc = function (fnSucc, args) {
            if (args != null) {
                //alert ("ret: " + args.toString());
            }
            else {
                //alert ("ret: null");
            }
            
            var jsonargs = seam2json(args);
            // alert("calling fnSucc");
            fnSucc(jsonargs);
            
            // alert("Returning... (cid=" + g_top_window.Seam.Remoting.getContext().getConversationId() + ")");
            // g_top_window.SeamConversationId = g_top_window.Seam.Remoting.getContext().getConversationId();
        };
        
        var myCallback = myCallbackSucc.partial (fnSucc);
        
        // g_top_window.Seam.Remoting.setDebug(true);
        
        var _args = json2seam (paramsArray);

        // g_top_window.Seam.Remoting.getContext().setConversationId( g_top_window.SeamConversationId );
        var _instance = g_top_window.Seam.Component.getInstance("editorAction");
        // alert ("Calling (cid=" + g_top_window.Seam.Remoting.getContext().getConversationId() + ") " +  method + " " + paramsArray.toString());     
        var _method = _instance[method];

        _args.push (myCallback);

        _method.apply (_instance, _args);      
	}
};




// Storage for element.userData
if (!window.USER_DATA_STORAGE)
	var USER_DATA_STORAGE = new Array();


/**
 * Function to use setUserData node method on browsers, that not support
 * this method yet. Creates id attribute on element and then saves
 * data into data store. If setUserData() supported, use it.
 *
 * Parameters:
 *  elem  - element to use
 *  key   - key of data
 *  value - data
 */
function setUserData(elem, key, value) {
	if (!elem)
		return;

	try {
		return elem.setUserData(key, value, null);
	} catch (e){

		if (!elem.id || !USER_DATA_STORAGE[elem.id] ) {
			var newid = tinymce.DOM.uniqueId("udata_");
			elem.id = newid;
			USER_DATA_STORAGE[newid] = new Array();
		}

		USER_DATA_STORAGE[elem.id][key] = value;

		return;
	}
}


/**
 * Like setUSerData(), but equal to getUserData().
 *
 */

function getUserData(elem, key) {
	if (!elem)
		return;

	try {
		return elem.getUserData(key);
	} catch(e){
		if (!elem.id || !USER_DATA_STORAGE[elem.id] )
			return null;
		
		return  USER_DATA_STORAGE[elem.id][key];
	}
}


/**
 * Function that returns array of nodes that have specified class.
 */

function getElementsByTypeAndClass(doc, tp, cl) {
	var retnode = [];
	var myclass = new RegExp('\\b'+cl+'\\b');
	var elem = doc.getElementsByTagName(tp);
	for (var i = 0; i < elem.length; i++) {
		var classes = elem[i].className;
		if (myclass.test(classes)) 
			retnode.push(elem[i]);
	}
	return retnode;
}

/************ Time TestBench Fcs*******/

var TIME_BENCH_START_TIMER = 0;

function tbSTm(){
	TIME_BENCH_START_TIMER = (new Date() ).getTime();
}

function tbFTm(){
	alert('Execution time: '+ ((new Date() ).getTime() - TIME_BENCH_START_TIMER  )+ 'ms');
}
