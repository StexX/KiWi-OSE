/**
 * $Id: editor_plugin_src.js 201 2007-02-12 15:56:56Z spocke $
 *
 * @author Moxiecode
 * @author Christoph Lange
 * @author Gordan Ristovski
 * @copyright Copyright © 2004-2008, Moxiecode Systems AB, All rights reserved.
 * @copyright Copyright © 2008, Christoph Lange, All rights reserved.
 */

(function() {
    tinymce.create('tinymce.plugins.OMDocEditorPlugin', {
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

	    t.editor = ed;

            ed.onInit.add(function() {
		if (ed.settings.content_css !== false) {
            	    ed.dom.loadCSS(url + "/css/content.css");
		}
            	/* for the toolbar icon
		tinymce.DOM.loadCSS(url + "/css/ui.css");
		*/
            });

	    ed.addCommand('mceRemoveOMDocAnnotation', function() {
		var ed = t.editor;

		var where_am_I = ed.selection.getNode();
		// Obtain the owner document, since only document doesn't work because of the structure of SWiM/tinyMCE
		var owndoc = where_am_I.ownerDocument;
		//Get to the table from the current position and obtain the table node as manipulatable DOM object
		var path_table = "ancestor::table[contains(@class, 'omedit-annotation')][1]";
		var table = owndoc.evaluate(path_table, where_am_I,  null, XPathResult.ANY_UNORDERED_NODE_TYPE, null).singleNodeValue;
		if (table) {
		    //If the table exists, then go down to the td node
		    var path_node = "tbody/tr[1]/td[1]";
		    var context_node = owndoc.evaluate(path_node, table, null, XPathResult.ANY_UNORDERED_NODE_TYPE, null).singleNodeValue;
		    var ultparent = table.parentNode;
		    while (context_node.childNodes[0] != null) {
			//Eventually, remove all the children one by one, until none is left
			ultparent.insertBefore(context_node.childNodes[0], table);
		    }
		    //After removing the children, remove the table
		    ultparent.removeChild(table);
		}
	    });

	    ed.addButton('omeditremoveannotation', {
		title : 'Remove Annotation',
		cmd : 'mceRemoveOMDocAnnotation',
		// image : ...
	    });
        },

	     /*
				<tr>
					<td colspan="2">
						<button dojoType="dijit.form.Button" onclick="editorRemoveTable();">Delete Table</button>
						<button dojoType="dijit.form.Button" onclick="editorAutoCompleteFW();">Start Autocompletion</button>
						<button dojoType="dijit.form.Button" onclick="editorAutoCompleteBCK();">Set Text</button>
						<!--<textarea dojoType="dijit.form.Textarea" onChange="alert('Change');" onKeyPress="alert('KEY PRESS');" onKeyUp="alert('KEY UP');" id="autocomplete">
						</textarea>-->
						<input type="text" name="autocomplete_text" onKeyUp="editorCreateList();" id="autocomplete"/>
					</td>
				</tr>
//testing with global variables
var att, val, type_name, arr;
//Instead of JSON, I have usual array
//Because: json has pairs, suppose line one from data is {"name":"assertion", "type":"axiom",...,"style":"something"}
//And then, in my table I have type=axiom..., so how do I search the JSON now? I have var x = "type", and then I can't say data[0].x...
//So, as I was limited with time, I decided to use simple array


//	Works if the attributes are of the form sth1=sth2 <br/> ... <br/>sthn=sthn+1
//	Click - ClickAway - Click back doesn't work
//	Thing in mail
// * editorAutoCompleteFW serves to start the autocompletion process. If we are in the proper field of a table, it will get the necessary information and
// * pass it to the autocompletion mechanism.  When invoked, the focus is given to the text box and the text box's value becomes the part of the string
// * before the cursor
var editorAutoCompleteFW = function()
{
	var inst = tinyMCE.getInstanceById('richtext');
	//This is the node I'm in
	var where_am_I = inst.selection.getNode();
	//I am checking if I am in the third th element of the table by going to the table, then reaching the element, and comparing it to the element I have
	//NOTE (self) there must be a more elegant way for this, think about it after you finish the initial implementation
	var owndoc = where_am_I.ownerDocument;
	var path_table = "ancestor::table[contains(@class, 'omdoc')][1]";
	var table = owndoc.evaluate(path_table, where_am_I,  null, XPathResult.ANY_UNORDERED_NODE_TYPE, null).singleNodeValue;
	if(table)
	{
		var path_node = "thead/tr[1]/th[3]";
		var context_node = owndoc.evaluate(path_node, table, null, XPathResult.ANY_UNORDERED_NODE_TYPE, null).singleNodeValue;
		if(context_node==where_am_I)
		{
			var path_to_type = "thead/tr[1]/th[1]";
			//1 of 4 arguments to be passed :D
			type_name = owndoc.evaluate(path_to_type, table, null, XPathResult.ANY_UNORDERED_NODE_TYPE, null).singleNodeValue.textContent;
			
			var text_at_pointer = inst.selection.getSel().focusNode;
			var numberOfCharsToCursor = inst.selection.getRng().startOffset;
			var substring = text_at_pointer.substringData(0, numberOfCharsToCursor);
			
			//obtaining 2 of the 4 arguments to be passed :D 3 so far
			//GIGO ... so I will assume the string is of type x=y
			att = substring.split('=')[0];
			val = substring.split('=')[1];
			
			
			//making the array
			arr = [];
			var i=0;
			//in the array, only the already complete attributes are inserted
			while(where_am_I.childNodes[i]!=null)
			{
				if(where_am_I.childNodes[i]!=text_at_pointer && where_am_I.childNodes[i].textContent!="")
				{
					var x = where_am_I.childNodes[i].textContent.split('=');
					if(x[1]!="" && x[1]!=null)
					arr.push(x);
				}
				i++;
			}
			//getting to the text node and setting its value to be what is left of the cursor on the time of clicking the button
			var text_node = document.getElementById('autocomplete');
			//var text_node = dijit.byId("autocomplete");
			
			if(val==null)
			//text_node.setValue(att+"=");
			text_node.value = att+"=";
			else
			//text_node.setValue(att+'='+val);
			text_node.value = att+'='+val;
			
			text_node.focus();
		}
	}
}



var editorAutoCompleteBCK = function()
{
	var text_node_value = document.getElementById('autocomplete').value;
	//var text_node_value = dijit.byId("autocomplete").getValue();
	var inst = tinyMCE.getInstanceById('richtext');
	//This is the node I'm in
	var where_am_I = inst.selection.getNode();
	//I am checking if I am in the third th element of the table by going to the table, then reaching the element, and comparing it to the element I have
	//NOTE (self) there must be a more elegant way for this, think about it after you finish the initial implementation
	var owndoc = where_am_I.ownerDocument;
	var path_table = "ancestor::table[contains(@class, 'omdoc')][1]";
	var table = owndoc.evaluate(path_table, where_am_I,  null, XPathResult.ANY_UNORDERED_NODE_TYPE, null).singleNodeValue;
	if(table)
	{
		var path_node = "thead/tr[1]/th[3]";
		var context_node = owndoc.evaluate(path_node, table, null, XPathResult.ANY_UNORDERED_NODE_TYPE, null).singleNodeValue;
		if(context_node==where_am_I)
		{
			var path_to_type = "thead/tr[1]/th[1]";
			//1 of 4 arguments to be passed :D
			
			var text_at_pointer = inst.selection.getSel().focusNode;
			var numberOfCharsToCursor = inst.selection.getRng().startOffset;
			var substring = text_at_pointer.substringData(0, numberOfCharsToCursor);
			//!!!!!!!!!
			var replacedText = text_at_pointer.textContent.replace(substring, text_node_value);
			text_at_pointer.textContent = replacedText;
			
			//pass the focus back
			inst.focus();
			
		}
	}
}

var data = [
            ["assertion", ["type", "axiom"], ["xref", "omdoc"], ["style", "something"]],
            ["assertion", ["type", "ababa"], ["xref", "omdoc"], ["style", "something"]],
            ["assertion", ["type", "axioma"], ["xref", "omdoc"], ["style", "something"]],
            ["assertion", ["type", "abibi"], ["xref", "omdoc"], ["style", "something"]],
            ["assertion", ["type", "definition"], ["xref", "omdoc"]],
            ["gordan", ["type", "ristovski"], ["xref", "foo"], ["style", "somethingelse"]]
];

var list;
var editorCreateList = function()
{
	
	list=[];
	
	var text_node_value = document.getElementById('autocomplete').value;
	//search for appropriate thing in data
	var i = 0;
	while(data[i]!=null)
	{
		//we found a name match
		if(data[i][0]==type_name)
		{
			var x = text_node_value.split('=');
			var re = new RegExp(x[1]);
			var k = 1;
			while(data[i][k]!=null)
			{
				if(data[i][k][0] == x[0])
				{
					if(data[i][k][1].match(re)!=null)
					list.push(data[i][k][1]);
				}
				k++;
			}
		}
		i++;
	}
	alert('LISTATA: '+list);
	
}

//Used by the "Delete Table" button in ../edt_content.jsp
var editorRemoveTable = function()
{
	var inst = tinyMCE.getInstanceById('richtext');
	var where_am_I = inst.selection.getNode();
	//Obtain the owner document, since only document doesn't work because of the structure of SWiM/tinyMCE
	var owndoc = where_am_I.ownerDocument;
	//Get to the table from the current position and obtain the table node as manipulatable DOM object
	var path_table = "ancestor::table[contains(@class, 'omdoc')][1]";
	var table = owndoc.evaluate(path_table, where_am_I,  null, XPathResult.ANY_UNORDERED_NODE_TYPE, null).singleNodeValue;
	if(table)
	{
		//If the table exists, then go down to the td node
		var path_node = "tbody/tr[1]/td[1]";
		var context_node = owndoc.evaluate(path_node, table, null, XPathResult.ANY_UNORDERED_NODE_TYPE, null).singleNodeValue;
		var ultparent = table.parentNode;
		while(context_node.childNodes[0]!=null)
		{
			//Eventually, remove all the children one by one, until none is left
			ultparent.insertBefore(context_node.childNodes[0], table);
		}
		//After removing the children, remove the table
		ultparent.removeChild(table);
	}
}
				*/

	annotationMenu : { "OMDoc" : [
		{ "Document Structure" : [
		    { "Document Unit (omgroup)" : [ 
			{ "(untyped)" : "omgroup" },
			{ "Abstract" : ["omgroup", "abstract"] },
			{ "Introduction" : ["omgroup", "introduction"] },
			{ "Background" : ["omgroup", "background"] },
			{ "Motivation" : ["omgroup", "motivation"] },
			{ "Scenario" : ["omgroup", "scenario"] },
			{ "Contribution" : ["omgroup", "contribution"] },
			{ "Evaluation" : ["omgroup", "evaluation"] },
			{ "Results" : ["omgroup", "results"] },
			{ "Discussion" : ["omgroup", "discussion"] },
			{ "Conclusion" : ["omgroup", "conclusion"] },
			{ "Entities" : ["omgroup", "entities"] } ] },
		    { "Reference (ref)": "ref" },
		    { "XInclude (non-OMDoc)": "xi:include" } ] },
		{ "Theories" : [
		    { "Theory" : "theory" },
		    { "Import (OMDoc 1.6)" : "import" },
		    { "meta" : "meta" },
		    { "Import (OMDoc 1.2)" : "imports" },
		    { "View" : "view" },
		    { "Substitution" : "substitution" },
		    { "hides" : "hides" },
		    { "maps" : "maps" } ] },
		{ "Formal Statements" : [
		    { "Constitutive Statements" : [
			{ "Axiom" : "axiom" },
			{ "Definition" : "definition" },
			{ "Symbol" : "symbol" } ] },
		    { "Nonconstitutive Statements" : [
			{ "Alternative Definition" : "alternative" },
			{ "Assertion" : [
			    { "(untyped)" : "assertion" },
			    { "Assumption" : ["assertion", "assumption"] },
			    { "Conjecture" : ["assertion", "conjecture"] },
			    { "Corollary" : ["assertion", "corollary"] },
			    { "False Conjecture" : ["assertion", "false-conjecture"] },
			    { "Formula" : ["assertion", "formula"] },
			    { "Lemma" : ["assertion", "lemma"] },
			    { "Obligation" : ["assertion", "obligation"] },
			    { "Postulate" : ["assertion", "postulate"] },
			    { "Proposition" : ["assertion", "proposition"] },
			    { "Rule" : ["assertion", "rule"] },
			    { "Theorem" : ["assertion", "theorem"] } ] },
			{ "Example" : "example" },
			{ "Type Assertion" : "type" } ] } ] },
		{ "Informal Statements (omtext)" : [
		    { "(untyped)" : "omtext" },
		    { "Mathematical Statements" : [
			{ "Axiom" : ["omtext", "axiom"] },
			{ "Definition" : ["omtext", "definition"] },
			{ "Example" : ["omtext", "example"] },
			{ "Proof" : ["omtext", "proof"] },
			{ "Hypothesis" : ["omtext", "hypothesis"] },
			{ "Assertion" : [
			    { "(untyped)" : ["omtext", "assertion"] },
			    { "Assumption" : ["omtext", "assumption"] },
			    { "Conjecture" : ["omtext", "conjecture"] },
			    { "Corollary" : ["omtext", "corollary"] },
			    { "False Conjecture" : ["omtext", "false-conjecture"] },
			    { "Formula" : ["omtext", "formula"] },
			    { "Lemma" : ["omtext", "lemma"] },
			    { "Obligation" : ["omtext", "obligation"] },
			    { "Postulate" : ["omtext", "postulate"] },
			    { "Proposition" : ["omtext", "proposition"] },
			    { "Rule" : ["omtext", "rule"] },
			    { "Theorem" : ["omtext", "theorem"] } ] } ] },
		    { "Rhetorical Blocks" : [
			{ "Introduction" : ["omtext", "introduction"] },
			{ "Background" : ["omtext", "background"] },
			{ "Motivation" : ["omtext", "motivation"] },
			{ "Scenario" : ["omtext", "scenario"] },
			{ "Contribution" : ["omtext", "contribution"] },
			{ "Evaluation" : ["omtext", "evaluation"] },
			{ "Results" : ["omtext", "results"] },
			{ "Discussion" : ["omtext", "discussion"] },
			{ "Conclusion" : ["omtext", "conclusion"] } ] } ] },
		{ "Text" : [
		    { "Commented Mathematical Property (CMP)" : "CMP" },
		    { "Term" : "term" },
		    { "Phrase" : [
			{ "(untyped)" : "phrase" },
			{ "Nucleus" : ["phrase", "nucleus"] },
			{ "Satellite" : [
			    { "Antithesis" : ["phrase", "satellite", "antithesis"] },
			    { "Circumstance" : ["phrase", "satellite", "circumstance"] },
			    { "Concession" : ["phrase", "satellite", "concession"] },
			    { "Condition" : ["phrase", "satellite", "condition"] },
			    { "Evidence" : ["phrase", "satellite", "evidence"] },
			    { "Means" : ["phrase", "satellite", "means"] },
			    { "Preparation" : ["phrase", "satellite", "preparation"] },
			    { "Purpose" : ["phrase", "satellite", "purpose"] },
			    { "Cause" : ["phrase", "satellite", "cause"] },
			    { "Consequence" : ["phrase", "satellite", "consequence"] },
			    { "Elaboration" : ["phrase", "satellite", "elaboration"] },
			    { "Restatement" : ["phrase", "satellite", "restatement"] },
			    { "Solutionhood" : ["phrase", "satellite", "solutionhood"] } ] } ] } ] },
		{ "Mathematical Objects" : [
		    { "Formal Mathematical Property (FMP)" : "FMP" },
		    { "Assumption (as part of FMP)" : "assumption" },
		    { "Conclusion (as part of FMP)" : "conclusion" },
		    // TODO this as function?
		    { "mark formula as OMOBJ" : "om:OMOBJ" } ] },
		{ "Proofs" : [
		    { "Proof" : "proof" },
		    { "Proof Step" : [
			{ "Definition" : "definition" },
			{ "Derivation Step" : [
			    { "(untyped)" : "derive" },
			    { "Conclusion" : ["derive", "conclusion"] },
			    { "Gap" : ["derive", "gap"] } ] },
			{ "Hypothesis" : "hypothesis" },
			{ "Method" : "method" },
			{ "Premise" : "premise" },
			{ "Informal Text" : ["omtext", "proof"] },
			{ "Symbol" : "symbol" } ] },
		    { "Proof Object" : "proofobject" } ] } ],
	    "OpenMath" : [
		// TODO declare namespace prefix on this level already
		{ "Content Dictionaries" : [
		    { "Content Dictionary (CD)" : "cd:CD" },
		    { "Symbol (CDDefinition)" : "cd:CDDefinition" },
		    { "Properties" : [
			{ "Property Block (property)" : "cd:property" },
			{ "Commented Mathematical Property (CMP)" : "cd:CMP" },
			{ "Formal Mathematical Property (FMP)" : "cd:FMP" } ] },
		    { "Example" : "cd:Example" } ] },
		{ "Signature Dictionaries" : [
		    { "Signature Dictionary (CDSignatures)" : "ocds:CDSignatures" },
		    { "Signature" : "ocds:Signature" } ] },
		{ "CD Groups" : [
		    { "CD Group" : "ocdg:CDGroup" },
		    { "CD Group Member" : "ocdg:CDGroupMember" },
		    { "CD by Name (CDName)" : "ocdg:CDName" } ] } ],
	    "MathML" : [
		{ "Notation Definitions" : [
		    { "Notation" : "mcd:notation" },
		    { "Prototype" : "mcd:prototype" },
		    { "Rendering" : "mcd:rendering" },
		    { "Expression" : "mcd:expr" },
		    { "Expression List" : "mcd:exprlist" },
		    { "Iteration" : "mcd:iterate" },
		    { "Separator" : "mcd:separator" } ] } ],
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
	    var t = this;

	    function populateMenu(m, data) {
		for (var i = 0; i < data.length; i++) {
		    menuOrItem = data[i];
		    for (var title in menuOrItem) {
			/* there is only one, but we wanted a convenient JSON syntax */
			(function(title, value) {
			    /* create a proper closure */
			    if (typeof value === 'string') {
				m.add({title : title, onclick : function() {
				    t._insertOMDoc(value);
				}});
			    } else if (typeof value[0] === 'string') {
				/* single menu item (with additional parameters) */
				m.add({title : title, onclick : function() {
				    t['_insertOMDoc'].apply(t, value);
				}});
			    } else {
				/* submenu */
				var sub = m.addMenu({title : title});
				populateMenu(sub, value);
			    }
			})(title, menuOrItem[title]);
		    }
		}
	    }

	    var matcher = /omedit_(.+)/.exec(n);
	    if (matcher) {
		var language = matcher[1];

		var c = cm.createSplitButton(n + '_menu', {
		    title: language + " Annotation",
		    image: null,
		    onclick: function() {
			t._insertOMDoc('_generic');
		    },
		});
		c.onRenderMenu.add(function(c, m) {
		    m.add({title : language, 'class' : 'mceMenuItemTitle'}).setDisabled(1);

		    populateMenu(m, t.annotationMenu[language]);
		});
		return c;
            }
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
                longname : 'OMDoc editor',
                author : 'Christoph Lange',
                authorurl : 'http://kwarc.info/clange/',
                infourl : 'http://kwarc.info/projects/swim/',
                version : "0.1"
            };
        },

        /* private methods */
        
        _wrapIntoOMDocElement : function(name, html, attr, rel, pre) {
	    var attrCell;
	    var preContent;
	    if (attr) {
		attrCell = attr;
		if (rel) {
		    attrCell += '<br/>' + rel;
		}
	    } else {
		attrCell = '<br/>';
	    }
	    if (pre) {
		preContent = pre;
	    } else {
		preContent = '';
	    }
	    return '<table class="omedit-annotation">' +
		'<thead><tr><th>' + name + '</th><th><br/></th><th>' + attrCell + '</th></tr></thead>' +
		// FIXME add class="omedit-spacer" once we can remove those cleanly
		'<tbody><tr><td colspan="3">' + preContent + '<span/>' + html +
		'</td></tr></tbody></table>';
        },

	_metadataField : function(name) {
	    return '<div class="omedit-meta">' +
		'<strong class="omedit-meta-label">' + name +
		'</strong>value</div>'
	},

	_insertOMDoc : function(name, type, relation) {
	    var t = this, ed = t.editor;

	    ed.execCommand('mceBeginUndoLevel');

	    if (name == 'om:OMOBJ') {
		ed.execCommand('mceReplaceContent', false, '<span class="omedit-mobj">{$selection}</span>');
	    } else {
		// FIXME if there is a <span|p class="omedit-spacer"/> around the selection, kill it
		var oldHTML = ed.selection.getContent();
		if (oldHTML == '') oldHTML = '<br/>';

		var newHTML, newAttr, newRelation, newPre;

		// some common attributes that the user really should set
		// TODO transform this into a data structure
		// http://wiki.kiwi-project.eu/atlassian-jira/browse/SWIM-3
		switch (name) {
		case 'imports':
		    newAttr = 'from=';
		    break;
		case 'term':
		    newAttr = 'cd=<br/>name=';
		    break;
		case 'assertion':
		    if (type != null) {
			newAttr = 'type=' + type;
		    }
		    break;
		case 'omtext':
		    if (type != null) {
			newAttr = 'type=' + type;
		    }
		    break;
		case 'phrase':
		    if(type != null) {
			newAttr = 'type=' + type;
		    }
		    if (relation != null) {
			newRelation = 'relation=' + relation;
		    }
		    break;
		case 'omgroup':
		    if (type != null) {
			newAttr = 'type=' + type;
		    }
		    break;
		case 'derive':
		    if (type != null) {
			newAttr = 'type=' + type;
		    }
		    break;
		case 'definition':
		case 'example':
		case 'proof':
		case 'proofobject':
		    newAttr = 'for=';
		    break;
		case 'symbol':
		case 'mcd:expr':
		    newAttr = 'name=';
		    break;
		case 'cd:CDDefinition':
		    newPre = t._metadataField('cd:Name');
		}
		
		// prepare the table representing the element
		switch (name) {
		case '_generic':
		    newHTML = t._wrapIntoOMDocElement(' ', oldHTML);
		    break;
		case 'mcd:notation':
		    newHTML = t._wrapIntoOMDocElement('mcd:notation',
			t._wrapIntoOMDocElement('mcd:prototype', oldHTML) + t._wrapIntoOMDocElement('mcd:rendering', ''));
		    break;
		default:
		    newHTML = t._wrapIntoOMDocElement(name, oldHTML, newAttr, newRelation, newPre);
		    break;
		}
		
		// append an empty paragraph to facilitate insertion of other annotations
		// FIXME add class="omedit-spacer" once we can remove those cleanly
		newHTML += '<p/>';

		// insert the element into the editor and ensure that it is displayed properly
		ed.execCommand('mceInsertContent', false, newHTML);
		// tinyMCE.handleVisualAid(ed.getBody(), true, tinyMCE.settings['visual']);
	    }
	    
	    ed.execCommand('mceEndUndoLevel');
	}    

    });

    // Register plugin
    tinymce.PluginManager.add('omedit', tinymce.plugins.OMDocEditorPlugin);
})();

/*
vim:ai:sts=4:sw=4:
*/
