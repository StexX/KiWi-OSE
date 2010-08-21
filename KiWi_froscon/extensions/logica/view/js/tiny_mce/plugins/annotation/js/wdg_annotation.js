/**
 * Plugin Editor-Annotation component for TinyMCE
 *   widget to show annotations
 * Michal Sebek, 2008
 * KiWi project 
 * www.fit.vutbr.cz
 * 
 * File contains functions to display annotation bar. Requires 
 * file wdg_annottools.js to be loaded (lib wwith shared methods).
 *
 * Last modif: 10.09.2008
 */


var imgPlusIcon  = "js/tiny_mce/plugins/annotation/img/plus.gif";
var imgMinusIcon = "js/tiny_mce/plugins/annotation/img/minus.gif";
var imgRightTag = "<img src='js/tiny_mce/plugins/annotation/img/arrowr.gif' />";
var imgDownTag  = "<img src='js/tiny_mce/plugins/annotation/img/arrowd.gif' />";


var annotWdgTools = {
		packUnpack : function (elm) {
			var i, pe = elm.parentNode;
	
			for (i = 0; i < pe.childNodes.length; i++) {
				var e = pe.childNodes[i];
				if (annotTools.hasClass(e, 'hidable'))
					if (e.style.display=='block'){
					 	e.style.display='none';
						elm.firstChild.src = imgPlusIcon;
					} else{
						e.style.display='block';
						elm.firstChild.src = imgMinusIcon;
					}
			}
		}
	
	};

function createAnnotationWidget(widgetID, editorID, showGlobalAnnotations){

	var annotationWidget = {

		init : function (widgetID, editorID, showGlobalAnnotations) {
			var t = this;
			t.widgetID = widgetID;
			t.editorID = editorID;
			t.globalAnnotations = showGlobalAnnotations;

			t.state    = null;
			t.statePrev= null;


			observer.addListener("stateChanged", function(event) {
				/*if(layoutManager.getComponent(widgetID) == null || 
					event.changedVariable != 'annotation-set' ||
					observer.getStateVariable('annotation-set').editorid  != t.editorID )
			   		return;*/			

				t.state = observer.getStateVariable('annotation-set');				
				t._updateContent();
				t.statePrev=t.state;
			});

		},

		_getStyleDisplay : function(displayB){
			return ' style=\'display:'+(displayB?'block':'none')+'\' ';
		},

		_getHTMLAnnotationsPar : function (annot, active, textActl){
			var t = this;

			// makes an annotation details HTML
			var annotText = '';
			var selTypeI = annotTools.getSelectedAnnotTypeIndex(t.state.stylecoll, annot.type);


			annotText += '<div class="annot-subitems hidable" '+t._getStyleDisplay(active)+' >';

				if (! t.globalAnnotations )
					annotBody = textActl.substr( annot.annotStart, annot.annotEnd-annot.annotStart );
				else
					annotBody = annot.type;

				if (annotBody.length > MAX_ANNOT_PREVIEW_LEN) {

				   annotBody = annotBody.substr(0, MAX_ANNOT_PREVIEW_LEN/2) + ' ... ' +
				     annotBody.substr(annotBody.length-MAX_ANNOT_PREVIEW_LEN/2, MAX_ANNOT_PREVIEW_LEN/2);
				}

						

				annotText += '<a href="#" onclick="hideAllAnnotationDescription(this);displayDescription(this);return false;">' + imgDownTag + '</a>'

					+' <a href="#" class="annot-sel-link" onclick="observer.update('+t.widgetID+', \'mce-editor-command\', {editorid : \''+t.editorID+'\', command: \'annot-select\', params: {annotID: \''+annot.id+'\'} }); return false;" style="'+ annotTools.serializeStyleObject(t.state.stylecoll[selTypeI].style) +'">';
				annotText += annotBody;
				annotText += '</a>';

				if (annot.state == __STATE_CONFIRMED) {
					annotText += ' <img src="js/tiny_mce/plugins/annotation/img/accept.png" alt="Commited" class="actbutton" title="Commited annotation"  /> ';
				} else {
					annotText += ' <a href="#" onclick="observer.update('+t.widgetID+', \'mce-editor-command\', {editorid : \''+t.editorID+'\', command: \'annot-confirm\', params: {annotID: \''+annot.id+'\'} });return false;"><img src="js/tiny_mce/plugins/annotation/img/help.png" class="actbutton" alt="Accept now" title="Accept uncorfirmed annotation" /></a>&nbsp;<a href="#" onclick="observer.update('+t.widgetID+', \'mce-editor-command\', {editorid : \''+t.editorID+'\', command: \'annot-delete\', params: {annotID: \''+annot.id+'\'} });return false;"><img src="js/tiny_mce/plugins/annotation/img/remove.png" class="actbutton" alt="Delete" title="Remove annotation"  /></a> ';
				}

				annotText += '<div class="annot-desc hidable" name="annot-desc" style=\'display:none;\'>';
				//annotText += annot.type + ': ' + annot.id;
				annotText += '<table>';
				if (annot.label)
					annotText += '<tr><td><strong>Label:</strong></td><td>'+annot.label+'</td></tr>';
				annotText += '<tr><td><strong>Type:</strong></td><td>'+ annot.type + '</td></tr>';
				annotText += '<tr><td><strong>ID:</strong></td><td>'+ annot.id + '</td></tr>';
				//annotText += '<tr><td><strong>start:</strong></td><td>'+ annot.annotStart + '</td></tr>';
				//annotText += '<tr><td><strong>end:</strong></td><td>'+ annot.annotEnd + '</td></tr>';
				if (annot.uri)
					annotText += '<tr><td><strong>URI:</strong></td><td><a href="#" title="Make link from URI" onclick="observer.update('+t.widgetID+', \'mce-editor-command\', {editorid : \''+t.editorID+'\', command: \'annot-make-link\', params: {annotID: \''+annot.id+'\'} });return false;" >'+annot.uri+'</a></tr>';	
				if (annot.note)
					annotText += '<tr><td><strong>Note:</strong></td><td>'+annot.note+'</tr>';	
				annotText += '<tr><td><strong>Actions:</strong></td><td>'
					+'<a href="#" onclick="observer.update('+t.widgetID+', \'mce-editor-command\', {editorid : \''+t.editorID+'\', command: \'annot-delete\', params: {annotID: \''+annot.id+'\'} });return false;">Delete</a> '
					+' <a href="#" onclick="observer.update('+t.widgetID+', \'mce-editor-command\', {editorid : \''+t.editorID+'\', command: \'annot-edit\', params: {annotID: \''+annot.id+'\'} });return false;">Edit</a>'
					+'</td></tr>';
				annotText += '</table>';
				annotText += '</div>';

				annotText += '</div>';

				//annotCnt++;
			return annotText;
		},

		_updateContent : function(){

			var t = this,
			    eapar = t.state.docposition.elm, i, j;



			var textActl = 
				annotTools.removeTagsFromHTML(
				  /*annotTools.removeAnnotTags*/(
				    t.state.srcdom.body
				  ).innerHTML
				);

			// Finds top-most paragraph for actual caret possition
			while ( eapar && t.state.srcdom.body != eapar.parentNode) {
				if (eapar.parentNode)
					eapar = eapar.parentNode;
				else
					return;			
			}


			var annotBarContent = '';
			var prevParSkipped = false;

			for (j=0; j<t.state.srcdom.body.childNodes.length; j++) { 

				var par = t.state.srcdom.body.childNodes[j];

				if (par.nodeName == NODE_TEXT_TYPE  || par.nodeName == NODE_COMMENT_TYPE) continue;

				// Gets an interval of paragraph content text
				var parIntrv = annotTools.getParagraphInterval(par, t.state.srcdom.body);

				// Shows annotation text
				var i = 0;
				var annotText = '';
				
				var selAnnots = new Array();
				if (! t.globalAnnotations )
					selAnnots = annotTools.getAnnotsInRange(t.state.annotationscoll, parIntrv.start, parIntrv.end);
				else {
					for (var i=0; i<t.state.annotationscoll.length; i++) {
						if (t.state.annotationscoll[i].annotStart == null || t.state.annotationscoll[i].annotEnd == null )
							selAnnots.push(t.state.annotationscoll[i]);
					}
				}


				for (i=0; i< selAnnots.length; i++) {
					if (selAnnots[i].state != __STATE_DENIED)
						annotText += t._getHTMLAnnotationsPar(selAnnots[i], (par == eapar || t.globalAnnotations), textActl);
				}
				
				if (! t.globalAnnotations )
				{
					var rollunrollLink = '';
					if (par == eapar)
						rollunrollLink = '<a href="#" onclick="annotWdgTools.packUnpack(this);return false;" ><img src=\''+imgMinusIcon+'\'></a>';
					else 
						rollunrollLink = '<a href="#" onclick="annotWdgTools.packUnpack(this);return false;" ><img src=\''+imgPlusIcon+'\'></a>';


					if (annotText != '' || par == eapar )	{
						annotBarContent += '<div> '+ rollunrollLink +' paragraph <strong>' + par.nodeName 
							+ '</strong> <span class="annot-cnt">(' + selAnnots.length + ')</span>';
		
	
						annotBarContent +=annotText;
						if (annotText == '')
							annotBarContent += '<div class="annot-subitems hidable" '
								+ t._getStyleDisplay(par == eapar)
								+ '><span class="no-annots"> \u00bb no annotation</span></div>';
					
						annotBarContent += '</div>';
						prevParSkipped = false;
					}	
					else {
						if ( ! prevParSkipped)
							annotBarContent += '<div>...</div>';
						prevParSkipped = true;
					}
				}else{
					// global annots
					annotBarContent +=annotText;
					if (annotText == '')
						annotBarContent += '<div class="annot-subitems"><span class="no-annots"> \u00bb no annotation</span></div>';
					break;  // globals are once, cycle only for locals
				}	
			
			}



			// layoutManager.getComponentBody(widgetID).dom.innerHTML = annotBarContent+'<br/>';
			document.getElementById("annotations_widget_content").innerHTML = annotBarContent+'<br/>';
		}



	};

	// run me
	annotationWidget.init(widgetID, editorID, showGlobalAnnotations);
}


function hideAllAnnotationDescription(elm){

}

function displayDescription(link){
	var i = 0;
	for (i=0; i< link.parentNode.childNodes.length; i++) {
	    if ( annotTools.hasClass(link.parentNode.childNodes[i], "annot-desc"))
              if ( link.parentNode.childNodes[i].style.display=='none' )
		link.parentNode.childNodes[i].style.display='block';
	      else
		link.parentNode.childNodes[i].style.display='none';
	}
}	   




