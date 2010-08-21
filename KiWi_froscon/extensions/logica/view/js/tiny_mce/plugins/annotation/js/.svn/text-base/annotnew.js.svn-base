tinyMCEPopup.requireLangPack();

window.Array = parent.window.Array; /* Array compatibility hack */

var annotationTypes = null ;
var annotToEdit	    = null ;

function init() {
	var settings = tinyMCEPopup.getWindowArg('settings');
	annotToEdit = settings.editAnnot;

	loadAnnotationTypesByURI();

	if (annotToEdit != null) {
		if (annotToEdit.label)
			document.getElementById('annot_label').value	= annotToEdit.label;
		if (annotToEdit.uri)
			document.getElementById('annot_uri').value	= annotToEdit.uri;
		if (annotToEdit.note)
			document.getElementById('annot_note').innerHTML = annotToEdit.note;
	}
}

function loadAnnotationTypesByURI(){
		var settings = tinyMCEPopup.getWindowArg('settings');
		annotationTypes = new Array();

		annotComm.sendReq(
			'getAnnotationTypes', 
			new Array(), 
			function(result) {

				// alert("result");
				
				// JSON RPC result
				annotationTypes = result;
				loadAnnotationTypes();
				if (annotToEdit != null && annotToEdit.type){
					selectByValue(document.forms[0], 'annot_types', annotToEdit.type, true, true);
				}
    			},
			function(e, x) {
				// Handle error
				alert('Error while loading list!');
			}
		);	
}


function loadAnnotationTypes(){

	var i, typestr='';

	for (i=0; i<annotationTypes.length; i++)
		typestr += (i>0?";":"")+annotationTypes[i].type;

	fillSelect(0, 'annot_types', 'style_annot_types', typestr, ';', true);
	if (annotationTypes.length)
		selectByValue(document.forms[0], 'annot_types', annotationTypes[0].type, true, true);

}	




function updateAction() {
	//alert("updateAction");
	if (!annotToEdit) {
		// create new
		//alert("create new");
		tinyMCEPopup.execCommand('annotsCreateNew' , false, {
				type: 	getValueOrNull(document.getElementById('annot_types').value),
				label: 	getValueOrNull(document.getElementById('annot_label').value),
				uri: 	getValueOrNull(document.getElementById('annot_uri').value),
				note:	getValueOrNull(document.getElementById('annot_note').value)
			}
		);
	} else {
		// upd actual
		annotToEdit.type	= getValueOrNull(document.getElementById('annot_types').value),
		annotToEdit.label	= getValueOrNull(document.getElementById('annot_label').value),
		annotToEdit.uri		= getValueOrNull(document.getElementById('annot_uri').value),
		annotToEdit.note	= getValueOrNull(document.getElementById('annot_note').value)
	}
	tinyMCEPopup.close();
}


function getValueOrNull(value){
	if ( ! value || value == "" ) 	return null;
	else				return value;
}

/********************** FUNKCE PREVZATE Z PLUGINU STYLE TINYMCE ************************/
function fillSelect(f, s, param, dval, sep, em) {
	var i, ar, p, se;

	f = document.forms[f];
	sep = typeof(sep) == "undefined" ? ";" : sep;

	if (em)
		addSelectValue(f, s, "", "");

	ar = tinyMCEPopup.getParam(param, dval).split(sep);
	for (i=0; i<ar.length; i++) {
		se = false;

		if (ar[i].charAt(0) == '+') {
			ar[i] = ar[i].substring(1);
			se = true;
		}

		p = ar[i].split('=');

		if (p.length > 1) {
			addSelectValue(f, s, p[0], p[1]);

			if (se)
				selectByValue(f, s, p[1]);
		} else {
			addSelectValue(f, s, p[0], p[0]);

			if (se)
				selectByValue(f, s, p[0]);
		}
	}
}


function selectByValueOrNull (f, e, v, p1, p2) {
			if (v)
				selectByValue(f, e.id, v, p1, p2);
			else
				e.selectedIndex=0;
}


tinyMCEPopup.onInit.add(init);
