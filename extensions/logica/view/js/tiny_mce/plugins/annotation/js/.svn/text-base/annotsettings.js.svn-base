tinyMCEPopup.requireLangPack();

var defaultSizes = "x-small;small;medium;large;x-large;smaller;larger";
var defaultWeight = "normal;bolder;lighter";
var defaultTextStyle = "normal;italic;oblique";

var defaultBorderStyle = "none;solid;dashed;dotted;double;groove;ridge;inset;outset";
var defaultBorderWidth = "thin;medium;thick";

window.Array = parent.window.Array; /* Array compatibility hack */

var annotationTypes = null ;

function init() {
	document.getElementById('text_color_pickcontainer').innerHTML = getColorPickerHTML('text_color_pick','text_color');
	document.getElementById('background_color_pickcontainer').innerHTML = getColorPickerHTML('background_color_pick','background_color');
	document.getElementById('border_color_bottom_pickcontainer').innerHTML = getColorPickerHTML('border_color_bottom_pick','border_color_bottom');

	fillSelect(0, 'text_size', 'style_font_size', defaultSizes, ';', true);
	fillSelect(0, 'text_case', 'style_text_case', "capitalize;uppercase;lowercase", ';', true);
	fillSelect(0, 'text_weight', 'style_font_weight', defaultWeight, ';', true);
	fillSelect(0, 'text_style', 'style_font_style', defaultTextStyle, ';', true);

	fillSelect(0, 'border_style_bottom', 'style_border_style_bottom', defaultBorderStyle, ';', true);
	fillSelect(0, 'border_width_bottom', 'style_border_width_bottom', defaultBorderWidth, ';', true);

	TinyMCE_EditableSelects.init();

	// inits types and first value
	loadAnnotationTypesByURI();
	showDisabledControls();
}

function loadAnnotationTypesByURI(){
		var settings = tinyMCEPopup.getWindowArg('settings');
		annotationTypes = new Array();

		annotComm.sendReq(
			'getAnnotationTypes', 
			new Array(settings.widgetid, settings.docuri), 
			function(result) {
				// JSON RPC result
				annotationTypes = result;
				loadAnnotationTypes();
    			},
			function(e, x) {
				// Handle error
				t.ed.windowManager.alert('Error while loading list!');
			}
		);	
}

function saveAnnotatonTypesByURI() {
		var settings = tinyMCEPopup.getWindowArg('settings');
	
		annotComm.sendReq(
			'setAnnotationTypes', 
			new Array(settings.widgetid, settings.docuri, annotationTypes), 
			function(result) {
				// JSON RPC result
				tinyMCEPopup.execCommand('annotsRefreshStyles');
				tinyMCEPopup.close();
    			},
			function(e, x) {
				// Handle error
				alert('Error while saving list! '+e);
			}
		);	
}

function loadAnnotationTypes(){

	var i, typestr='';

	for (i=0; i<annotationTypes.length; i++)
		typestr += (i>0?";":"")+annotationTypes[i].type;

	fillSelect(0, 'annotation_type', 'style_annotation_type', typestr, ';', true);
	if (annotationTypes.length)
		selectByValue(document.forms[0], 'annotation_type', annotationTypes[0].type, true, true);

	loadDataByType(annotationTypes);
	setExampleText();
}	

function onTypeChange () {	
	loadDataByType(annotationTypes);

	setExampleText();
}



function loadDataByType( annotationTypes ) {

	selectOptionsByStyle( {} );
	for (i=0; i<annotationTypes.length; i++){
		if ( annotationTypes[i].type == document.getElementById('annotation_type').value ) {
			// type found, set properities
			if ( ! annotationTypes[i].style )
				annotationTypes[i].style = {}; //construct a new object for style
			selectOptionsByStyle( annotationTypes[i].style );
		}
	}
		
}

function selectByValueOrNull (f, e, v, p1, p2) {
			if (v)
				selectByValue(f, e.id, v, p1, p2);
			else
				e.selectedIndex=0;
}

function updateColorOrNull (f, e, v, p1, p2) {
			if (v)
				selectByValue(f, e.id, v, p1, p2);
			else
				e.selectedIndex=0;
}



function updateAction() {
	packChanges();
	saveAnnotatonTypesByURI();

}


function selectOptionsByStyle(style){
	var f = document.forms[0];


	selectByValueOrNull(f, f.text_size, 	style.fontSize, 	true, true);
	selectByValueOrNull(f, f.text_weight, 	style.fontWeight, 	true, true);
	selectByValueOrNull(f, f.text_style, 	style.fontStyle, 	true, true);
	selectByValueOrNull(f, f.text_case, 	style.textTransform, 	true, true);

	f.text_color.value = (style.color)?style.color:"";
	updateColor('text_color_pick', 'text_color');

	f.background_color.value = (style.backgroundColor)?style.backgroundColor:"";
	updateColor('background_color_pick', 'background_color');

	selectByValueOrNull(f, f.border_style_bottom, style.borderBottomStyle, true, true);
	selectByValueOrNull(f, f.border_width_bottom, style.borderBottomWidth, true, true);

	f.border_color_bottom.value = (style.borderBottomColor)?style.borderBottomColor:"";
	updateColor('border_color_bottom_pick', 'border_color_bottom');


}


function onPropsChange() {
	packChanges();

	setExampleText();
}

function setExampleText(){
	var selType = -1;
	var elEx = document.getElementById('example_text');
	var f = document.forms[0];
	if ( (selType = getSelectedAnnotTypeIndex()) >= 0 ) {
		elEx.style.fontSize 		= f.text_size.value;
		elEx.style.fontWeight 		= f.text_weight.value;
		elEx.style.fontStyle 		= f.text_style.value;
		elEx.style.textTransform 	= f.text_case.value;
		elEx.style.color 		= f.text_color.value;
		elEx.style.backgroundColor	= f.background_color.value;
		elEx.style.borderBottomStyle	= f.border_style_bottom.value;
		elEx.style.borderBottomWidth	= f.border_width_bottom.value;
		elEx.style.borderBottomColor	= f.border_color_bottom.value;
	}
}

function getSelectedAnnotTypeIndex(){

	for (i=0; i<annotationTypes.length; i++){
		if ( annotationTypes[i].type == document.getElementById('annotation_type').value ) {
			return i;
		}
	}

	return -1;

}


/* collects settings from page into object */
function packChanges() {
	var f = document.forms[0];

	for (i=0; i<annotationTypes.length; i++){
		if ( annotationTypes[i].type == document.getElementById('annotation_type').value ) {
			
			if ( ! annotationTypes[i].style )
				annotationTypes[i].style = {}; //construct a new object for style
			var style = annotationTypes[i].style;

			style.fontSize   = f.text_size.value;
			style.fontStyle  = f.text_style.value;
			style.textTransform  = f.text_case.value;
			style.fontWeight = f.text_weight.value;
			style.color = f.text_color.value;

			style.backgroundColor = f.background_color.value;

			style.borderBottomStyle = f.border_style_bottom.value;
			style.borderBottomWidth = f.border_width_bottom.value 
			style.borderBottomColor = f.border_color_bottom.value;

		}
	}

}


/********************** FUNKCE PREVZATE Z PLUGINU STYLE TINYMCE ************************/

function setupFormData() {
	var ce = document.getElementById('container'), f = document.forms[0], s, b, i;

	// Setup text fields

	selectByValue(f, 'text_font', ce.style.fontFamily, true, true);
	selectByValue(f, 'text_size', getNum(ce.style.fontSize), true, true);
	selectByValue(f, 'text_size_measurement', getMeasurement(ce.style.fontSize));
	selectByValue(f, 'text_weight', ce.style.fontWeight, true, true);
	selectByValue(f, 'text_style', ce.style.fontStyle, true, true);
	selectByValue(f, 'text_lineheight', getNum(ce.style.lineHeight), true, true);
	selectByValue(f, 'text_lineheight_measurement', getMeasurement(ce.style.lineHeight));
	selectByValue(f, 'text_case', ce.style.textTransform, true, true);
	selectByValue(f, 'text_variant', ce.style.fontVariant, true, true);
	f.text_color.value = tinyMCEPopup.editor.dom.toHex(ce.style.color);
	updateColor('text_color_pick', 'text_color');
	f.text_underline.checked = inStr(ce.style.textDecoration, 'underline');
	f.text_overline.checked = inStr(ce.style.textDecoration, 'overline');
	f.text_linethrough.checked = inStr(ce.style.textDecoration, 'line-through');
	f.text_blink.checked = inStr(ce.style.textDecoration, 'blink');

	// Setup background fields

	f.background_color.value = tinyMCEPopup.editor.dom.toHex(ce.style.backgroundColor);
	updateColor('background_color_pick', 'background_color');
	f.background_image.value = ce.style.backgroundImage.replace(new RegExp("url\\('?([^']*)'?\\)", 'gi'), "$1");
	selectByValue(f, 'background_repeat', ce.style.backgroundRepeat, true, true);
	selectByValue(f, 'background_attachment', ce.style.backgroundAttachment, true, true);
	selectByValue(f, 'background_hpos', getNum(getVal(ce.style.backgroundPosition, 0)), true, true);
	selectByValue(f, 'background_hpos_measurement', getMeasurement(getVal(ce.style.backgroundPosition, 0)));
	selectByValue(f, 'background_vpos', getNum(getVal(ce.style.backgroundPosition, 1)), true, true);
	selectByValue(f, 'background_vpos_measurement', getMeasurement(getVal(ce.style.backgroundPosition, 1)));


	// Setup border fields

	setupBox(f, ce, 'border_style', 'border', 'Style');
	setupBox(f, ce, 'border_width', 'border', 'Width');
	setupBox(f, ce, 'border_color', 'border', 'Color');

	updateColor('border_color_top_pick', 'border_color_top');
	updateColor('border_color_right_pick', 'border_color_right');
	updateColor('border_color_bottom_pick', 'border_color_bottom');
	updateColor('border_color_left_pick', 'border_color_left');

	f.elements.border_color_top.value = tinyMCEPopup.editor.dom.toHex(f.elements.border_color_top.value);
	f.elements.border_color_right.value = tinyMCEPopup.editor.dom.toHex(f.elements.border_color_right.value);
	f.elements.border_color_bottom.value = tinyMCEPopup.editor.dom.toHex(f.elements.border_color_bottom.value);
	f.elements.border_color_left.value = tinyMCEPopup.editor.dom.toHex(f.elements.border_color_left.value);


//	setupBox(f, ce, '', 'border', 'Color');
}

function getMeasurement(s) {
	return s.replace(/^([0-9]+)(.*)$/, "$2");
}

function getNum(s) {
	if (new RegExp('^[0-9]+[a-z%]+$', 'gi').test(s))
		return s.replace(/[^0-9]/g, '');

	return s;
}

function inStr(s, n) {
	return new RegExp(n, 'gi').test(s);
}

function getVal(s, i) {
	var a = s.split(' ');

	if (a.length > 1)
		return a[i];

	return "";
}

function setValue(f, n, v) {
	if (f.elements[n].type == "text")
		f.elements[n].value = v;
	else
		selectByValue(f, n, v, true, true);
}


function isSame(e, pr, sf, b) {
	var a = [], i, x;

	if (typeof(b) == "undefined")
		b = ['Top', 'Right', 'Bottom', 'Left'];

	if (typeof(sf) == "undefined" || sf == null)
		sf = "";

	a[0] = e.style[pr + b[0] + sf];
	a[1] = e.style[pr + b[1] + sf];
	a[2] = e.style[pr + b[2] + sf];
	a[3] = e.style[pr + b[3] + sf];

	for (i=0; i<a.length; i++) {
		if (a[i] == null)
			return false;

		for (x=0; x<a.length; x++) {
			if (a[x] != a[i])
				return false;
		}
	}

	return true;
};

function hasEqualValues(a) {
	var i, x;

	for (i=0; i<a.length; i++) {
		if (a[i] == null)
			return false;

		for (x=0; x<a.length; x++) {
			if (a[x] != a[i])
				return false;
		}
	}

	return true;
}

function applyAction() {
	var ce = document.getElementById('container'), ed = tinyMCEPopup.editor;

	generateCSS();

	tinyMCEPopup.restoreSelection();
	ed.dom.setAttrib(ed.selection.getNode(), 'style', tinyMCEPopup.editor.dom.serializeStyle(tinyMCEPopup.editor.dom.parseStyle(ce.style.cssText)));
}


function isNum(s) {
	return new RegExp('[0-9]+', 'g').test(s);
}

function showDisabledControls() {
	var f = document.forms, i, a;

	for (i=0; i<f.length; i++) {
		for (a=0; a<f[i].elements.length; a++) {
			if (f[i].elements[a].disabled)
				tinyMCEPopup.editor.dom.addClass(f[i].elements[a], "disabled");
			else
				tinyMCEPopup.editor.dom.removeClass(f[i].elements[a], "disabled");
		}
	}
}

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

tinyMCEPopup.onInit.add(init);
