





var formula_editor;formula_editor=null;var transformer;transformer=null;var formula_omobj;formula_omobj=null;var message;var plugin_url;if("tinyMCEPopup"in window){tinyMCEPopup.requireLangPack=function(){var u=this.getWindowArg('plugin_url')||this.getWindowArg('theme_url');if(u&&this.editor.settings.language){u+='/langs/'+this.editor.settings.language+'_dlg.js';tinymce.ScriptLoader.load(u);}};tinyMCEPopup.requireLangPack();message=window.parent.message;formula_editor=window.parent.formula_editor;plugin_url=tinyMCEPopup.getWindowArg('plugin_url');}else{message=alert;plugin_url=tinymce.baseURL+'/plugins/sentido';}var ed;if(! Array.prototype.push){Array.prototype.push=function(){for(var i in arguments)this[this.length]=arguments[i];return this.length;};}var previous_input;function input_changed(input){if(input.value!=previous_input){previous_input=input.value;formula_editor.linear_to_openmath(input.value);put_formula_in_title(input.value);}}var formula_search_variables;formula_search_variables={free:{},bound:{}};var formula_search_variables_header;function formula_changed(omobj){var formula_display;formula_display=document.getElementById("formula-display-mathml");if(formula_display){var node;node=transformer.transformToFragment(omobj,document);node=node.firstChild;while(node&&node.nodeType!=Node.ELEMENT_NODE){node=node.nextSibling;}if(node.firstChild&&"math"==node.firstChild.localName){node.firstChild.setAttribute("mode","display");}formula_display.replaceChild(node,formula_display.firstChild);}formula_omobj=omobj;var variable_types_tbody;variable_types_tbody=document.getElementById("formula-search-variable-types");var variables;variables=formula_editor.get_variables();var i;for(i=variable_types_tbody.childNodes.length;i > 2;--i){variable_types_tbody.removeChild(variable_types_tbody.lastChild);}var variable_name;var variable_row;var previous_variable_row;var row_item;var input;previous_variable_row=null;for(i in variables.free){variable_name=variables.free[i].getAttribute("name");variable_row=formula_search_variables.free[variable_name];if(!variable_row){variable_row=document.createElement("tr");variable_row.setAttribute("class","symbol-row");variable_row.generic=false;variable_row.anycount=false;row_item=document.createElement("th");row_item.appendChild(document.createTextNode(variable_name));variable_row.appendChild(row_item);row_item=document.createElement("th");input=document.createElement("input");input.setAttribute("type","checkbox");input.setAttribute("name","formula-search-free-variable-generic-"+variable_name);input.setAttribute("onchange","formula_search_variable_event(event)");row_item.appendChild(input);variable_row.appendChild(row_item);row_item=document.createElement("th");input=document.createElement("input");input.setAttribute("type","checkbox");input.setAttribute("name","formula-search-free-variable-anycount-"+variable_name);input.setAttribute("onchange","formula_search_variable_event(event)");row_item.appendChild(input);variable_row.appendChild(row_item);row_item=document.createElement("th");input=document.createElement("input");input.setAttribute("type","checkbox");input.setAttribute("name","formula-search-free-variable-function-"+variable_name);input.setAttribute("onchange","formula_search_variable_event(event)");row_item.appendChild(input);variable_row.appendChild(row_item);formula_search_variables.free[variable_name]=variable_row;}variable_types_tbody.insertBefore(variable_row,previous_variable_row);previous_variable_row=variable_row;}var separator;variable_row=document.createElement("tr");variable_row.setAttribute("class","symbol-row");row_item=document.createElement("th");row_item.setAttribute("colspan","4");separator=document.createElement("hr");separator.setAttribute("style","padding:0; margin:0");row_item.appendChild(separator);variable_row.appendChild(row_item);variable_types_tbody.appendChild(variable_row);previous_variable_row=null;for(i in variables.bound){variable_name=variables.bound[i].getAttribute("name");variable_row=formula_search_variables.bound[variable_name];if(!variable_row){variable_row=document.createElement("tr");variable_row.setAttribute("class","symbol-row");variable_row.generic=true;variable_row.anycount=false;row_item=document.createElement("th");row_item.appendChild(document.createTextNode(variable_name));variable_row.appendChild(row_item);row_item=document.createElement("th");input=document.createElement("input");input.setAttribute("type","checkbox");input.setAttribute("checked","checked");input.setAttribute("name","formula-search-bound-variable-generic-"+variable_name);input.setAttribute("onchange","formula_search_variable_event(event)");row_item.appendChild(input);variable_row.appendChild(row_item);row_item=document.createElement("th");input=document.createElement("input");input.setAttribute("type","checkbox");input.setAttribute("name","formula-search-bound-variable-anycount-"+variable_name);input.setAttribute("onchange","formula_search_variable_event(event)");row_item.appendChild(input);variable_row.appendChild(row_item);row_item=document.createElement("th");input=document.createElement("input");input.setAttribute("type","checkbox");input.setAttribute("name","formula-search-bound-variable-function-"+variable_name);input.setAttribute("onchange","formula_search_variable_event(event)");row_item.appendChild(input);variable_row.appendChild(row_item);formula_search_variables.bound[variable_name]=variable_row;}variable_types_tbody.insertBefore(variable_row,previous_variable_row);previous_variable_row=variable_row;}}function formula_search_variable_event(event){var name;name=event.target.getAttribute("name");var property;property=name.replace(/.*-([^-]*)-[^-]*$/,"$1");var variable_name;variable_name=name.replace(/.*-([^-]*)$/,"$1");var value;value=event.target.checked;var variable_row;variable_row=event.target.parentNode.parentNode;switch(property){case"generic":if(value)variable_row.generic=true;else       variable_row.generic=false;break;case"anycount":if(value)variable_row.anycount=true;else       variable_row.anycount=false;break;case"function":if(value)formula_editor.declare_variable(variable_name,"morphism");else       formula_editor.declare_variable(variable_name,"object");break;default:message("Error: property="+property+", name="+name);}}function init(){if("tinyMCEPopup"in window){ed=tinyMCEPopup.editor;tinyMCEPopup.resizeToInnerSize();}else{ed=window.tinyMCE.activeEditor;}var f,fe;f=document.forms[0];fe=ed.selection.getNode();if(/mceItemSentido/.test(ed.dom.getAttrib(fe,'class'))){f.formula.value=fe.textContent;var options;options=f.syntax.options;for(var i=0;i < options.length;++i){if("x-qmath-"+options[i].value==fe.lang){f.syntax.selectedIndex=i;break;}}}TinyMCE_EditableSelects.init();var input_text_field;input_text_field=document.getElementById('sentido-embedded-input-editor-textarea');if(!formula_editor){formula_editor=new Formula_editor(input_text_field);formula_editor.init(plugin_url+"/sentido",plugin_url+"/sentido/contexts");formula_editor.onchange=formula_changed;formula_editor.enable_edit(true);}formula_editor.change_to_context(document.getElementById('sentido-embedded-input-editor-syntax').value);if(!transformer){transformer=build_transformer(plugin_url+"/sentido/om_to_pmml.xsl");}if(input_text_field.value)formula_editor.linear_to_openmath(input_text_field.value);input_text_field.focus();}function put_formula_in_title(linear_formula){if("undefined"==typeof linear_formula){linear_formula=document.getElementById("sentido-embedded-input-editor-textarea").value;}document.title=linear_formula;}function toggle_palette(palette_item){var palette_table=palette_item;while(palette_table.parentNode&&palette_table.localName!='table')palette_table=palette_table.parentNode;var class_name='symbol-palette';if(class_name==palette_table.getAttribute('class'))class_name='symbol-palette-folded';palette_table.setAttribute('class',class_name);}var message_count;message_count=0;function message(text){if(++message_count < 10)alert(text);}var sentido={serializer:new XMLSerializer(),parser:new DOMParser(),getSerializedXmlFormula:function(){if(false){var variables;variables=formula_editor.get_variables();var variable;var variable_name;var variable_row;var i;for(i in variables.free){variable=variables.free[i];variable_name=variable.getAttribute("name");variable_row=formula_search_variables.free[variable_name];if(variable_row.generic){variable.setAttribute("mq:generic",variable_name);}else{variable.removeAttribute("mq:generic");}if(variable_row.anycount){variable.setAttribute("mq:anycount","yes");}else{variable.removeAttribute("mq:anycount");}}for(i in variables.bound){variable=variables.bound[i];variable_name=variable.getAttribute("name");variable_row=formula_search_variables.bound[variable_name];if(variable_row.generic){variable.setAttribute("mq:generic",variable_name);}else{variable.removeAttribute("mq:generic");}if(variable_row.anycount){variable.setAttribute("mq:anycount","yes");}else{variable.removeAttribute("mq:anycount");}}formula_omobj.setAttribute("xmlns:mq","http://mathweb.org/MathQuery");}var query=formula_omobj;if(false){om_to_cmml_translator=build_transformer(plugin_url+"/sentido/om_to_cmml.xsl");query=om_to_cmml_translator.transformToFragment(query,query.ownerDocument);}return this.serializer.serializeToString(query);},setSerializedXmlFormula:function(serialized_XML_formula){var xml_document;xml_document=this.parser.parseFromString(serialized_XML_formula,"text/xml");if("OMOBJ"==xml_document.documentElement.localName){formula_omobj=xml_document.documentElement;formula_editor.select_node(formula_omobj);}}};function insertSentido(){var fe,f=document.forms[0],h;tinyMCEPopup.restoreSelection();if(!AutoValidator.validate(f)){alert(ed.getLang('invalid_data'));return false;}fe=ed.selection.getNode();var formula_title,formula_presentation,formula_content;switch(f.syntax.options[f.syntax.selectedIndex].value){case"en":formula_title="Syntax: QMath/English";break;case"maxima":formula_title="Syntax: Maxima";break;case"yacas":formula_title="Syntax: Yacas";break;case"mma":formula_title="Syntax: Mathematica\u00AE";break;case"mpl":formula_title="Syntax: Maple\u2122";break;}formula_presentation=f.formula.value;formula_content=sentido.getSerializedXmlFormula();var formula_display,formula_mathml;formula_display=document.getElementById("formula-display-mathml");formula_mathml=sentido.serializer.serializeToString(formula_display.firstChild.firstChild);formula_language="x-qmath-"+f.syntax.options[f.syntax.selectedIndex].value;if(fe!=null&&/mceItemSentido/.test(ed.dom.getAttrib(fe,'class'))){fe.title=formula_title;fe.textContent=formula_presentation;fe.lang=formula_language;fe.setAttribute("openmath",formula_content);fe.setAttribute("mathml",formula_mathml);fe.setAttribute("linear",formula_presentation);}else{h='<span class="mceItemSentido"';h+=' title="'+formula_title+'"';h+=' lang="'+formula_language+'"';h+=' openmath="'+formula_content.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/\"/g,"&quot;")+'"';h+=' mathml="'+formula_mathml.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/\"/g,"&quot;")+'"';h+=">";h+=formula_presentation;h+='</span>';ed.execCommand('mceInsertContent',false,h);}tinyMCEPopup.close();}if("tinyMCEPopup"in window)tinyMCEPopup.onInit.add(init);function build_transformer(stylesheet_url){var request,transformer;try{request=new XMLHttpRequest();request.open("GET",stylesheet_url,false);request.send(null);transformer=new XSLTProcessor();transformer.importStylesheet(request.responseXML);}catch(error){alert("Error when loading the XSL stylesheet '"+stylesheet_url+"'\n"+error);}request=null;return transformer;}//


//




//




//





var namespace_map = {};

function register_namespace_map(additional_namespace_map)
{
  var prefix;
  var uri;
  for (var i in additional_namespace_map)
    {
      prefix = i;
      uri = additional_namespace_map[i];
      if (prefix in namespace_map
          && namespace_map[prefix] != uri)
        {
          message("Error: namespace declaration clash:\n"
                  + prefix + ": " + namespace_map[prefix] + "\n"
                  + prefix + ": " + uri);
        }
      if (uri in namespace_map
          && namespace_map[uri] != prefix
          
          && (namespace_map[uri] && prefix))
        {
          message("Error: namespace declaration clash:\n"
                  + namespace_map[uri] + ": " + uri + "\n"
                  + prefix                    + ": " + uri);
        }
      namespace_map[prefix] = uri;
      namespace_map[uri] = prefix;
    }
}

function namespace_resolver(prefix)
{
  var uri;
  uri = namespace_map[prefix];
  if (undefined === uri)
    {
      message("Warning: " + document.title + ": Unhandled namespace prefix: "
              + prefix);
    }
  
  return uri;
}

function namespace_URI_resolver(uri)
{
  var prefix;
  prefix = namespace_map[uri];
  if (undefined === prefix)
    {
      message("Warning: " + document.title + ": Unhandled namespace URI: "
              + uri);
    }
  
  return prefix;
}

function xpath_first(expression, node)
{
  var result;
  try
    {
      result = xpath(expression, node, XPathResult.FIRST_ORDERED_NODE_TYPE);
      if (result) result = result.singleNodeValue;
      else        result = null;
    }
  catch (exception)
    {
      message("Error: expression = \"" + expression + "\", node = " + node
              + ": " + exception);
      result = null;
    }
  
  return result;
}

function xpath_number(expression, node)
{
  var result;
  try
    {
      result = xpath(expression, node, XPathResult.NUMBER_TYPE);
      if (result) result = result.numberValue;
      else        result = undefined;
    }
  catch (exception)
    {
      message("Error: expression = \"" + expression + "\", node = " + node
              + ": " + exception);
      result = undefined;
    }
  
  return result;
}

function xpath_boolean(expression, node)
{
  var result;
  try
    {
      result = xpath(expression, node, XPathResult.BOOLEAN_TYPE);
      if (result) result = result.booleanValue;
      else        result = undefined;
    }
  catch (exception)
    {
      message("Error: expression = \"" + expression + "\", node = " + node
              + ": " + exception);
      result = undefined;
    }
  
  return result;
}

function xpath_string(expression, node)
{
  var result;
  try
    {
      result = xpath(expression, node, XPathResult.STRING_TYPE);
      if (result) result = result.stringValue;
      else        result = null;
    }
  catch (exception)
    {
      message("Error: expression = \"" + expression + "\", node = " + node
              + ": " + exception);
      result = null;
    }
  
  return result;
}

function xpath(expression, node, result_type)
{
  var document_node;
  if (document.DOCUMENT_NODE == node.nodeType) document_node = node;
  else if ("ownerDocument" in node) document_node = node.ownerDocument;
  else message("Error: can't get document for " + node);
  
  if ("[DocumentFragment]" == node.constructor)
    {
      
      
      
      
      message("Error: Element.evaluate(xpath, node, nsres, type, result) does not accept DocumentFragment as root.");
    }
  
  var result;
  try
    {
      result = document_node.evaluate(expression, node,
                                      namespace_resolver,
                                      result_type, null);
    }
  catch (exception)
    {
      message("Error: expression = \"" + expression + "\", node = " + node
              + ", result_type = " + result_type
              + ": " + exception);
      result = null;
    }
  
  return result;
}

function build_xpath(element)
{
  var path;
  if (element && document.ELEMENT_NODE == element.nodeType)
    {
      var e = element;
      path = path_node(e);
      while ((e = e.parentNode))
        {
          if (document.ELEMENT_NODE == e.nodeType)
            {
              path = path_node(e) + "/" + path;
            }
        }
      path = "/" + path;
    }
  else
    {
      path = "/";
    }
  
  return path;
}

function path_node(element)
{
  var full_name;
  if (element.prefix)
    {
      full_name = element.prefix + ":" + element.localName;
    }
  else if (false && element.namespaceURI)
    {
      full_name = namespace_URI_resolver(element.namespaceURI) + ":" + element.localName;
    }
  else
    {
      full_name = element.nodeName;
    }
  
  var selector;
  if (element.hasAttribute("xml:id"))
    {
      selector = full_name + "[@xml:id='" + element.getAttribute("xml:id") + "']";
    }
  else
    {
      var siblings_count;
      siblings_count = xpath_number("count(preceding-sibling::"
                                    + full_name
                                    + ")+count(following-sibling::"
                                    + full_name
                                    + ")",
                                    element
                                    );
      if (true || siblings_count > 0)
        {
          var index;
          index = xpath_number("count(preceding-sibling::"
                               + full_name
                               + ")",
                               element
                               ) + 1;
          selector = full_name + "[" + index + "]";
        }
      else
        {
          selector = full_name;
        }
    }
  
  return selector;
}

//


//




//




//












var parser_namespace = "http://www.matracas.org/ns/cascada";

function Ternary_tree()
{
  var self = this;
  
  function get_tree() { return tree; }
  
  function Entry(code_point)
  {
    this.code_point = code_point;

    this.smaller = undefined;
    this.same = undefined;
    this.bigger = undefined;
  }
  
  function add_entry(pattern, data)
  {
    var entry;
    
    var match;
    match = search(pattern);
    
    var code_point;
    if (!tree)
      {
        code_point = pattern.charCodeAt(0);
        entry = new Entry(code_point);
        match.last_tested_entry  = entry;
        match.last_matched_entry = entry;
        match.length = 0;
        match.index = 1;
        tree = entry;
      }
    else if (match.last_matched_entry != match.last_tested_entry)
      {
        var parent;
        code_point = pattern.charCodeAt(match.index);
        entry = new Entry(code_point);
        parent = match.last_tested_entry;
        if      (code_point < parent.code_point) parent.smaller = entry;
        else if (code_point > parent.code_point) parent.bigger  = entry;
        ++match.index;
        match.last_tested_entry = entry;
      }
    
    while (match.index < pattern.length)
      {
        code_point = pattern.charCodeAt(match.index);
        entry = new Entry(code_point);
        match.last_tested_entry.same = entry;
        
        
        ++match.index;
        match.last_tested_entry = entry;
      }
    
    match.length = match.index;
    
    
    
    
    
    match.last_tested_entry.data = data;
  }
  
  function search(string, begin)
  {
    var match;
    match = {"length":0,
             "last_tested_entry":undefined, "last_matched_entry":undefined};
    
    if (!begin) begin = 0;
    
    match.index = begin;
    
    var node;
    node = tree;
    var string_length;
    string_length = string.length;
    var code_point;
    if (match.index < string_length) code_point = string.charCodeAt(match.index);
    while (node && match.index < string_length)
      {
        match.last_tested_entry = node;
        if (code_point == node.code_point)
          {
            ++match.index;
            code_point = string.charCodeAt(match.index);
            if ("data" in node)
              {
                
                
                
                match.length = match.index - begin;
                match.data = node.data;
              }
            node = node.same;
            match.last_matched_entry = match.last_tested_entry;
          }
        else if (code_point < node.code_point)
          {
            node = node.smaller;
          }
        else
          {
            node = node.bigger;
          }
      }
    
    return match;
  }
  
  
  var tree;
  
  
  this.get_tree = get_tree;
  this.Entry = Entry;
  this.add_entry = add_entry;
  this.search = search;
}

function Symbol(parser, replacement, identifier)
{
  var self = this;
  this.replacement = replacement;
  this.identifier = identifier;
}

function Term_class(parser, subclasses, replacement, identifier, complement)
{
  var self = this;
  
  function add(subclasses)
  {
    if (!subclasses) return;
    
    for (var c in subclasses)
      {
        if (!subclasses[c]) alert("Undefined tokens in class "
                                  + self.identifier + ":\n"
                                  + subclasses.toSource());
        if (parser.is_token(subclasses[c]))
          {
            self.subclasses.push(subclasses[c]);
            var term_subclass;
            term_subclass = parser.get_class(subclasses[c]);
            if (term_subclass) term_subclass.superclasses[identifier] = self;
          }
        else
          {
            self.subclasses.push(parser.symbol(subclasses[c]));
          }
      }
    
    recompile_dependent_rules();
  }
  
  function recompile_dependent_rules()
  {
    for (var r in self.rules) self.rules[r].compile();
    for (var s in self.superclasses)
      {
        self.superclasses[s].recompile_dependent_rules();
      }
  }
  
  
  this.replacement = replacement;
  this.identifier = identifier;
  this.complement = (complement?true:false);
  this.rules = {};
  this.superclasses = {};
  this.subclasses = [];
  add(subclasses);
  
  
  this.add = add;
  this.recompile_dependent_rules = recompile_dependent_rules;
}

function Rule(parser, unexpanded_pattern, replacement, identifier, context)
{
  var self = this;
  
  function expand_pattern(pattern, pattern_chars)
  {
    var token;
    var i;
    for (i in pattern)
      {
        token = pattern[i];
        var subclass;
        subclass = parser.get_class(token);
        if (subclass)
          {
            subclass.rules[replacement] = self;
            var subclass_tokens;
            subclass_tokens = [];
            parser.expand_subclasses(subclass, subclass_tokens);
            pattern_chars.push("[");
            if (subclass.complement) pattern_chars.push("^");
            pattern_chars.push(subclass_tokens.join(""));
            pattern_chars.push("]");
          }
        else
          {
            pattern_chars.push(token);
          }
      }
  }
  
  function compile()
  {
    var pattern_chars;
    pattern_chars = [];
    var expanded_pattern;
    var context_before;
    var context_after;
    
    pattern_chars.length = 0;
    expand_pattern(unexpanded_pattern, pattern_chars);
    expanded_pattern = pattern_chars.join("");
    
    if (context)
      {
        
        if (context[0] instanceof Array)
          {
            pattern_chars.length = 0;
            expand_pattern(context[0].join("|"), pattern_chars);
            context_before = pattern_chars.join("");
          }
        else
          {
            pattern_chars.length = 0;
            expand_pattern(context[0], pattern_chars);
            context_before = pattern_chars.join("");
          }
        if (context[1] instanceof Array)
          {
            pattern_chars.length = 0;
            expand_pattern(context[1].join("|"), pattern_chars);
            context_after = pattern_chars.join("");
          }
        else
          {
            pattern_chars.length = 0;
            expand_pattern(context[1], pattern_chars);
            context_after = pattern_chars.join("");
          }
        expanded_pattern
          = "(" + context_before + ")"
          + expanded_pattern
          + "(" + context_after + ")";
      }
    
    self.pattern = new RegExp(expanded_pattern, "g");
  }
  
  function match(string)         { return self.pattern.exec(string); }
  
  function last_index()          { return self.pattern.lastIndex; }
  
  function set_last_index(index) { self.pattern.lastIndex = index; }
  
  
  this.pattern = null;
  this.context = context;
  this.replacement = replacement;
  this.identifier = identifier;
  compile();
  
  
  this.compile = compile;
  this.match = match;
  this.last_index = last_index;
  this.set_last_index = set_last_index;
}

Rule.util =
  {
    any: function (pattern)
    {
      if (pattern.length > 1) return "(?:" + pattern + ")+";
      else                    return pattern + "+";
    },
    
    star: function (pattern)
    {
      if (pattern.length > 1) return "(?:" + pattern + ")*";
      else                    return pattern + "*";
    },
    
    or: function (patterns)
    {
      if (patterns instanceof Array) return "(?:" + patterns.join("|") + ")";
      else alert("Parser.or(patterns): patterns is not an array.");
      return patterns.toString();
    },
    
    opt: function (pattern)
    {
      if (pattern.length > 1) return "(?:" + pattern + ")?";
      else                    return pattern + "?";
    },
    
    not: function (patterns)
    {
      if (patterns instanceof Array) return "[^" + patterns.join("") + "]";
      else alert("Parser.not(patterns): patterns is not an array.");
      return patterns.toString();
    },
    
    literal: function (text)
    {
      return text.replace(/(\.|\?|\||\+|\*|\(|\)|\[|\]|\{|\}|\\|\^|\$)/g,
                          "\\$1");
    },
    
    input_begin: function ()
    {
      return "^";
    },
    
    input_end: function ()
    {
      return "$";
    }
    
  };

function Stage(model)
{
  if (model)
    {
      this.string = model.string;
      this.data = model.data.slice(0);
    }
  else
    {
      this.string = "";
      this.data  = [];
    }
}

function Parser()
{
  var self = this;
  
  function is_token(token)
  {
    return unicode_private_area_regexp.test(token);
  }
  
  function get_classes_with(token)
  {
    var classes;
    classes = [];
    var subclass;
    var term;
    term = get_term(token);
    
    for (var c in term_classes)
      {
        subclass = term_classes[c];
        for (var i in subclass.subclasses)
          if (token == subclass.subclasses[i])
            {
              classes.push(subclass.identifier);
              break;
            }
      }
    
    return classes;
  }
  
  function symbol_with_substring_comparison(identifier, is_regexp)
  {
    if (!identifier) throw "Parser.symbol(): no token given.";
    
    if (identifier in symbols) return symbols[identifier].replacement;
    
    ++last_term_index_assigned;
    var replacement;
    replacement = String.fromCharCode(last_term_index_assigned);
    var new_symbol;
    new_symbol = new Symbol(self, replacement, identifier);
    symbols[identifier] = new_symbol;
    terms_back[replacement] = new_symbol;
    
    if (identifier.length > longest_symbol_length)
      {
        longest_symbol_length = identifier.length;
      }
    
    token_table[identifier] = replacement;
    
    return replacement;
  }
  
  function symbol_with_ternary_tree(identifier, is_regexp)
  {
    if (!identifier) throw "Parser.symbol(): no token given.";
    
    if (identifier in symbols) return symbols[identifier].replacement;
    
    var match;
    match = ternary_tree.search(identifier);
    if (match.length == identifier.length) return match.data.replacement;
    
    ++last_term_index_assigned;
    var replacement;
    replacement = String.fromCharCode(last_term_index_assigned);
    var new_symbol;
    new_symbol = new Symbol(self, replacement, identifier);
    symbols[identifier] = new_symbol;
    ternary_tree.add_entry(identifier, new_symbol);
    terms_back[replacement] = new_symbol;
    
    token_table[identifier] = replacement;
    
    return replacement;
  }
  
  function symbol_with_rules(identifier, is_regexp)
  {
    var pattern;
    if (is_regexp) pattern = identifier;
    else           pattern = Rule.util.literal(identifier);
    
    var replacement;
    if (identifier in token_table) replacement = token_table[identifier];
    if (!replacement)
      {
        ++last_term_index_assigned;
        replacement = String.fromCharCode(last_term_index_assigned);
      }
    
    var new_rule;
    new_rule = new Rule(self, pattern, replacement, identifier, null);
    symbol_rules.push(new_rule);
    terms_back[replacement] = new_rule;
    
    token_table[identifier] = replacement;
    return replacement;
  }
  
  function term_class(identifier, subclasses)
  {
    return term_class_generic(identifier, subclasses, false);
  }
  
  function complement_term_class(identifier, subclasses)
  {
    return term_class_generic(identifier, subclasses, true);
  }
  
  function term_class_generic(identifier, subclasses, complement)
  {
    var replacement;
    if (identifier in token_table) replacement = token_table[identifier];
    if (!replacement)
      {
        ++last_term_index_assigned;
        replacement = String.fromCharCode(last_term_index_assigned);
      }
    var new_term_class;
    if (replacement in term_classes)
      {
        new_term_class = term_classes[replacement];
        if (new_term_class.complement != complement)
          {
            message("Error: new_term_class.complement != complement");
          }
        new_term_class.add(subclasses);
      }
    else
      {
        new_term_class = new Term_class(self,
                                        subclasses, replacement, identifier,
                                        complement);
        term_classes[replacement] = new_term_class;
      }
    
    token_table[identifier] = replacement;
    return replacement;
  }
  
  function rule(identifier, pattern, context)
  {
    var replacement;
    if (identifier in token_table) replacement = token_table[identifier];
    if (!replacement)
      {
        ++last_term_index_assigned;
        replacement = String.fromCharCode(last_term_index_assigned);
      }
    
    var new_rule;
    new_rule = new Rule(self, pattern, replacement, identifier, context);
    rules.push(new_rule);
    terms_back[replacement] = new_rule;
    
    token_table[identifier] = replacement;
    return replacement;
  }
  
  function action_rule(action, pattern, context)
  {
    var new_rule;
    new_rule = new Rule(self, pattern, action, action.name, context);
    rules.push(new_rule);
    
    return null;
  }
  
  function expand_subclasses(subclass, subclass_tokens)
  {
    var tokens;
    tokens = subclass.subclasses;
    subclass_tokens.push(tokens.join(""));
    var i;
    var token;
    for (i in tokens)
      {
        token = tokens[i];
        if (token in term_classes)
          {
            subclass = term_classes[token];
            if (subclass) expand_subclasses(subclass, subclass_tokens);
          }
      }
  }
  
  function current()
  {
    return String.fromCharCode(last_term_index_assigned + 1);
  }
  
  function reserve(identifier)
  {
    var replacement;
    replacement = String.fromCharCode(++last_term_index_assigned);
    if (identifier) token_table[identifier] = replacement;
    return replacement;
  }
  
  function get_term(token)
  {
    if (token in terms_back) return terms_back[token];
    else                     return null;
  }
  
  function get_class(token)
  {
    if (token in term_classes) return term_classes[token];
    else                       return null;
  }
  
  function parse(text)
  {
    var stage_stack;
    stage_stack = [];
    var last_stage;
    last_stage = tokenize(text);
    stage_stack.push(last_stage);
    var fence_match;
    do
      {
        last_stage = stage_stack[stage_stack.length - 1];
        fence_match = fence_regexp.exec(last_stage.string);
        if (fence_match && fence_match.index >= 0)
          {
            if (apply_rules(stage_stack, fence_match.index, fence_regexp.lastIndex))
              {
                fence_regexp.lastIndex = 0;
              }
          }
        else
          {
            apply_rules(stage_stack);
          }
      }
    while (fence_match && fence_match.index >= 0);
    
    return stage_stack;
  }
  
  function parse_to_XML(text)
  {
    var result;
    result = parse(text);

    var stage;
    stage = result[result.length - 1];
    var container_document_fragment;
    container_document_fragment = document.createDocumentFragment();
    var container;
    container = document.createElementNS(parser_namespace, "result");
    container_document_fragment.appendChild(container);
    
    for (var i in stage.string)
      {
        append_XML_node(container, stage.data[i], 1);
      }
    
    return container_document_fragment;
  }
  
  function append_XML_node(parent, data, level)
  {
    if (!data || level > 100)
      {
        parent.appendChild(document.createTextNode("[NO DATA]"));
        return;
      }
    
    var max_level;
    max_level = 8;
    var node;
    if (data.stage_index > 0)
      {
        node = document.createElementNS(parser_namespace, data.identifier);
      }
    else
      {
        if (data.identifier)
          {
            node = document.createElementNS(parser_namespace, "token");
            node.setAttribute("literal", data.identifier);
          }
        else
          {
            node = document.createElementNS(parser_namespace, "text");
            node.setAttribute("literal", data.token);
          }
      }
    node.setAttribute("begin", data.begin);
    node.setAttribute("end",   data.end);
    if ("children" in data)
      {
        for (var i in data.children)
          {
            append_XML_node(node, data.children[i], level + 1);
          }
        node.setAttribute("end", node.lastChild.getAttribute("end"));
      }
    parent.appendChild(node);
  }
  
  function unparse_from_XML(parse_tree)
  {
    
    
    var linear_content;
    linear_content = "";
    var literal;
    var tokens;
    tokens = [];
    collect_tokens_from_parse_tree(parse_tree, tokens);
    if (tokens.length)
      {
        var recognized_tokens;
        var recognized_tokens_lengths;
        var tentative_linear_content;
        tentative_linear_content = "";
        var i;
        for (i = 0; i < tokens.length; ++i)
          {
            tentative_linear_content += tokens[i].replace(/\uEEEE/g, "");
          }
        var original_text = tentative_linear_content;
        recognized_tokens = tokenize(tentative_linear_content).data;
        for (i = 0;
             i < tokens.length && i < recognized_tokens.length;
             ++i)
          {
            linear_content += tokens[i];
            var given_token_length;
            var recognized_token_length;
            given_token_length = tokens[i].replace(/\uEEEE/g, "").length;
            recognized_token_length = recognized_tokens[i].end - recognized_tokens[i].begin;
            if (given_token_length < recognized_token_length)
              {
                tentative_linear_content = tentative_linear_content.substring(linear_content.length);
                linear_content += " ";
                recognized_tokens.splice(i+1);
                recognized_tokens = recognized_tokens.concat(tokenize(tentative_linear_content).data);
              }
            else if (given_token_length > recognized_token_length)
              {
                var powerful_message;
                powerful_message = "\n";
                powerful_message += "\nAt position " + i + ", token [" + tokens[i] + "] [" + recognized_tokens[i].token + "]\n" + original_text + "\n";
                for (var n = 0; n <= i; ++n)
                  powerful_message += " " + n + " [" + tokens[n] + "] [" + recognized_tokens[n].token + "]\n";
                message("Error: the given parse tree contains tokens that would not be recognized by the parser." + powerful_message);
              }
          }
      }
    
    return linear_content;
  }
  function collect_tokens_from_parse_tree(parse_tree, tokens)
  {
    var child;
    child = parse_tree.firstChild;
    while (child)
      {
        switch (child.localName)
          {
          case "token":
          case "text":
            tokens.push(child.getAttribute("literal"));
            break;
          case "num_int":
          case "num_dec":
            var token_string;
            for (var digit = child.firstChild; digit; digit = digit.nextSibling)
              {
                token_string = digit.getAttribute("literal");
                if (token_string.match(/\uEEEE/))
                  {
                    digit = digit.nextSibling;
                    if (digit) token_string += digit.getAttribute("literal");
                    else       token_string = "PLACEHOLDER_ERROR";
                  }
                tokens.push(token_string);
              }
            break;
          default:
            collect_tokens_from_parse_tree(child, tokens);
            break;
          }
        
        child = child.nextSibling;
      }
  }
  
  
  
  function apply_rules(stage_stack, begin, end)
  {
    var at_least_one_rule_applied;
    at_least_one_rule_applied = false;
    
    var stage;
    var stage_index;
    stage = new Stage();
    stage_index = stage_stack.length;
    var last_stage;
    var string;
    last_stage = stage_stack[stage_stack.length - 1];
    string = last_stage.string;
    var initial_string_length;
    initial_string_length = string.length;
    
    if (!begin) begin = 0;
    if (!end)   end   = 0;
    var rules_applied_in_iteration;
    do
      {
        var match_begin;
        var match_end;
        var substring_to_match;
        if (begin >= 0 && end > begin)
          {
            match_begin        = begin;
            match_end          = end - (initial_string_length - string.length);
            substring_to_match = string.substring(0, match_end);
          }
        else
          {
            match_begin        = 0;
            match_end          = string.length;
            substring_to_match = string;
          }
        
        var r;
        for (r = 0; r < rules.length; ++r)
          {
            rules[r].set_last_index(match_begin);
          }
        rules_applied_in_iteration = 0;
        for (r = 0; r < rules.length; ++r)
          {
            var rule;
            var data;
            
            var match;
            var p;
            
            
            
            

            rule = rules[r];
            
            if ((match = rule.match(substring_to_match)))
              {
                if (rule.context)
                  {
                    var last_group_index;
                    last_group_index = match.length - 1;
                    if (!match[1] && !match[last_group_index])
                      {
                        message("Warning: there is a null context match in " + rule.identifier + ":\n"
                                + match.toSource()
                                );
                      }
                    
                    
                    if (match[1]) match.index += match[1].length;
                    if (match[last_group_index]) rule.set_last_index(rule.last_index() - match[last_group_index].length);
                    
                    
                    
                    if (match.index == rule.last_index())
                      {
                        alert("The rule " + rule.identifier +
                              " has a context match, but its own match is void:\n"
                              + rule);
                        rule.set_last_index(match.index + 1);
                        continue;
                      }
                  }
                
                var replacement;
                replacement = rule.replacement;
                if ("function" == typeof(replacement))
                  {
                    message("Matched function rule " + rule.identifier);
                    
                    
                    
                    replacement = rule.replacement(self, rule, substring_to_match.substring(match.index, rule.last_index()));
                    if (!replacement)
                      {
                        r = 0;
                        continue;
                      }
                  }
                
                
                at_least_one_rule_applied = true;
                ++rules_applied_in_iteration;
                
                data = {};
                data.token = replacement;
                data.identifier = rule.identifier;
                data.begin = match.index;
                data.end   = rule.last_index();
                data.stage_index = stage_index;
                data.children = [];
                var is_mergeable;
                is_mergeable = self.merge_stages;
                var last_stage_data;
                for (p = data.begin; p < data.end; ++p)
                  {
                    last_stage_data = last_stage.data[p];
                    data.children.push(last_stage_data);
                    is_mergeable &= (last_stage_data.stage_index + 1
                                     < stage_index);
                  }
                var replacement_count;
                replacement_count = data.end - data.begin;
                if (is_mergeable)
                  {
                    
                    last_stage.data.splice(data.begin,
                                           replacement_count, data);
                    
                    last_stage.string
                      = string.substring(0, data.begin)
                      + data.token
                      + string.substring(data.end);
                    string = last_stage.string;
                  }
                else
                  {
                    
                    stage.data  = last_stage.data.slice(0);
                    stage.data.splice(data.begin, replacement_count, data);
                    
                    stage.string
                      = string.substring(0, data.begin)
                      + data.token
                      + string.substring(data.end);
                    
                    stage_stack.push(stage);
                    
                    stage = new Stage();
                    stage_index = stage_stack.length;
                    last_stage = stage_stack[stage_stack.length - 1];
                    string = last_stage.string;
                  }
                
                break;
              }
          }
      }
    while (rules_applied_in_iteration > 0);
    
    return at_least_one_rule_applied;
  }
  
  function tokenize_with_ternary_tree(text)
  {
    var tokens;
    tokens = new Stage();
    var cursor;
    cursor = 0;
    var test_string;
    var test_length;
    
    var symbol;
    var data;
    while (cursor < text.length)
      {
        symbol = null;
        var match;
        match = ternary_tree.search(text, cursor);
        if ("data" in match) symbol = match.data;
        if (implicit_variable_name_regexp)
          {
            test_string = text.substr(cursor);
            var variable_name_match;
            variable_name_match = implicit_variable_name_regexp.exec(test_string);
            if (variable_name_match
                && variable_name_match[0].length > match.length)
              {
                match = variable_name_match[0];
                test_string = test_string.substr(0, match.length);
                self.term_class("object_variable", self.symbol(test_string));
                symbol = symbols[test_string];
              }
          }
        if (symbol)
          {
            
            
            
            
            test_length = match.length;
            data = {};
            data.token = symbol.replacement;
            data.identifier = symbol.identifier;
            data.begin = cursor;
            data.end   = cursor + match.length;
            data.stage_index = 0;
            tokens.data.push(data);
            tokens.string += symbol.replacement;
            cursor += test_length;
          }
        else
          {
            if (self.ignore_space && text[cursor].match(/\s/))
              {
                cursor += 1;
              }
            else
              {
                data = {};
                data.token = text[cursor];
                data.identifier = null;
                data.begin = cursor;
                data.end   = cursor + 1;
                data.stage_index = 0;
                tokens.data.push(data);
                tokens.string += text[cursor];
                cursor += 1;
              }
          }
      }
    
    return tokens;
  }
  
  function tokenize_with_substring_comparison(text)
  {
    var tokens;
    tokens = new Stage();
    var cursor;
    cursor = 0;
    var test_string;
    var test_length;
    
    var symbol;
    var data;
    while (cursor < text.length)
      {
        test_length = longest_symbol_length;
        if (cursor+test_length > text.length) test_length = text.length-cursor;
        symbol = null;
        while (!symbol && test_length > 0)
          {
            test_string = text.substr(cursor, test_length--);
            symbol = symbols[test_string];
            if (!symbol && implicit_variable_name_regexp)
              {
                var variable_name_match;
                variable_name_match = implicit_variable_name_regexp.exec(test_string);
                if (variable_name_match && variable_name_match[0].length == test_length)
                  {
                    
                    
                    
                    
                    self.term_class("object_variable", self.symbol(test_string));
                    symbol = symbols[test_string];
                  }
              }
          }
        if (symbol)
          {
            
            
            
            
            test_length = test_string.length;
            data = {};
            data.token = symbol.replacement;
            data.identifier = symbol.identifier;
            data.begin = cursor;
            data.end   = cursor + test_length;
            data.stage_index = 0;
            tokens.data.push(data);
            tokens.string += symbol.replacement;
            cursor += test_length;
          }
        else
          {
            if (self.ignore_space && text[cursor].match(/\s/))
              {
                cursor += 1;
              }
            else
              {
                data = {};
                data.token = text[cursor];
                data.identifier = null;
                data.begin = cursor;
                data.end   = cursor + 1;
                data.stage_index = 0;
                tokens.data.push(data);
                tokens.string += text[cursor];
                cursor += 1;
              }
          }
      }
    
    return tokens;
  }
  
  
  

  var implementation = "ternary_tree";
  var symbol;
  var tokenize;
  switch (implementation)
    {
    case "substring_comparison":
      symbol = symbol_with_substring_comparison;
      tokenize = tokenize_with_substring_comparison;
      
      break;
    case "ternary_tree":
      symbol = symbol_with_ternary_tree;
      tokenize = tokenize_with_ternary_tree;
      
      break;
    case "symbols_as_rules":
      symbol = symbol_with_rules;
      tokenize = tokenize_with_rules;
      
      break;
    default:
      message("Unknown implementation: " + implementation);
      symbol = symbol_with_substring_comparison;
      tokenize = tokenize_with_strings;
      
    }
  
  var implicit_variable_name_regexp = null;
  if ("watch" in this)
    {
      
      
      this.watch("implicit_variable_name_pattern", function (property, oldValue, newValue)
                 {
                   try
                     {
                       if (newValue) implicit_variable_name_regexp = new RegExp("^" + newValue);
                       else          implicit_variable_name_regexp = null;
                     }
                   catch (exception)
                     {
                       message("Error: implicit_variable_name_pattern: " + exception);
                       newValue = oldValue;
                     }
                   
                   return newValue;
                 }
                 );
    }
  
  var token_table = {};
  this.token_table = token_table;
  
  
  var symbols = {};
  var longest_symbol_length = 0;
  
  
  var ternary_tree = new Ternary_tree();
  
  
  var symbol_rules = [];
  
  var rules = [];
  var terms_back = {};
  var term_classes = {};
  var unicode_private_area_begin = 0xE000;
  var unicode_private_area_end   = 0xF8FF;
  var unicode_private_area_regexp = /[\uE000-\uF8FF]/;
  var last_term_index_assigned = unicode_private_area_begin - 1;
  
  
  var fence_regexp;
  fence_regexp = new RegExp
    (symbol("(")+"[^"+symbol("(")+symbol(")")+"]+"+symbol(")") + "|" +
     symbol("[")+"[^"+symbol("[")+symbol("]")+"]+"+symbol("]") + "|" +
     symbol("{")+"[^"+symbol("{")+symbol("}")+"]+"+symbol("}") + "|" +
     "^"        +"[^"+symbol("(")+symbol(")")+"]+"+symbol(")") + "|" +
     "^"        +"[^"+symbol("[")+symbol("]")+"]+"+symbol("]") + "|" +
     "^"        +"[^"+symbol("{")+symbol("}")+"]+"+symbol("}") + "|" +
     symbol("(")+"[^"+symbol("(")+symbol(")")+"]+"+        "$" + "|" +
     symbol("[")+"[^"+symbol("[")+symbol("]")+"]+"+        "$" + "|" +
     symbol("{")+"[^"+symbol("{")+symbol("}")+"]+"+        "$",
     "g"
     );

  
  
  this.is_token    = is_token;
  this.get_classes_with = get_classes_with;
  this.symbol      = symbol;
  this.term_class  = term_class;
  this.complement_term_class = complement_term_class;
  this.rule        = rule;
  this.action_rule = action_rule;
  this.any         = Rule.util.any;
  this["+"]        = Rule.util.any;
  this.star        = Rule.util.star;
  this.opt_any     = Rule.util.star;
  this["*"]        = Rule.util.star;
  this.or          = Rule.util.or;
  this["|"]        = Rule.util.or;
  this.opt         = Rule.util.opt;
  this["?"]        = Rule.util.opt;
  this.not         = Rule.util.not;
  this.input_begin = Rule.util.input_begin;
  this.input_end   = Rule.util.input_end;
  this.current = current;
  this.reserve = reserve;
  this.get_term = get_term;
  this.get_class = get_class;
  this.expand_subclasses = expand_subclasses;
  this.parse = parse;
  this.parse_to_XML = parse_to_XML;
  this.unparse_from_XML = unparse_from_XML;
  
  this.ignore_space = false;
  this.merge_stages = true;
  this.implicit_variable_name_pattern = null;
}

//


//




//




//





var timer =
  {
  ms: 0,
  start_ms: 0,
  start: function() { this.start_ms = (new Date()).getTime(); },
  stop: function() { this.ms = (new Date()).getTime() - this.start_ms; }
  };

function QMath(base_url)
{
  var self = this;
  
  this.rebuild_context = rebuild_context;
  this.declare_variable = declare_variable;
  this.linear_to_openmath = linear_to_openmath;
  this.linear_to_parse_tree = linear_to_parse_tree;
  this.parse_tree_to_openmath = parse_tree_to_openmath;
  this.openmath_to_linear = openmath_to_linear;
  this.openmath_to_parse_tree = openmath_to_parse_tree;
  this.parse_tree_to_linear = parse_tree_to_linear;
  this.measure_performance = measure_performance;

  var openmath_namespace = "http://www.openmath.org/OpenMath";
  var    qmath_namespace = "http://www.matracas.org/ns/qmath";
  var   mathml_namespace = "http://www.w3.org/1998/Math/MathML";
  var    xhtml_namespace = "http://www.w3.org/1999/xhtml";

  var namespace_prefix_map =
    {
      "om"  : openmath_namespace,
      "q"   :    qmath_namespace,
      "m"   :   mathml_namespace,
      "h"   :    xhtml_namespace
    };

  var xml_serializer = new XMLSerializer();
  var gClipboardHelper;
  if ("Components" in window)
    {
      try
        {
          gClipboardHelper =
            Components.classes["@mozilla.org/widget/clipboardhelper;1"]
            .getService(Components.interfaces.nsIClipboardHelper);
        }
      catch (exception)
        {
          
          
          gClipboardHelper = null;
        }
    }

  var parser;
  var transformer_parse;
  var transformer_unparse;
  var stylesheet_unparse;
  var stylesheet_parse;
  var linear_as_xml;

  var transformer_build_stylesheet_parse;
  var debug = false;

  
  if (! base_url) base_url = "";
  else if (! base_url.match(/\/$/))base_url+="/";
  linear_as_xml=false;
  register_namespace_map(namespace_prefix_map);
  transformer_build_stylesheet_parse=new XSLTProcessor();
  var stylesheet;
  stylesheet=load_xml(base_url+"context_to_parse_stylesheet.xsl");
  if(!stylesheet)message("Can't load file: "+base_url+"context_to_parse_stylesheet.xsl");
  transformer_build_stylesheet_parse.importStylesheet(stylesheet);
  function grammar_action_declare_variable(parser,rule,matched_input){parser.term_class("object_variable",[matched_input]);
      return parser.symbol(matched_input);
  }function initialize_parser(p){var t=p.token_table;
      var symbol=p.symbol;
      var rule=p.rule;
      var action_rule=p.action_rule;
      var term_class=p.term_class;
      var complement_term_class=p.complement_term_class;
      var reserve=p.reserve;
      var space="\\s*";
      rule("line_separator",";");
	      term_class("string_open",['"']);
	      term_class("string_close",['"']);
	      complement_term_class("string_content",[t.string_close]);
	      rule("string",t.string_open+p.any(t.string_content)+t.string_close);
	      reserve("num_dec");
	      reserve("num_int");
	      reserve("num_rat");
	      reserve("num_com");
	      reserve("group_object");
	      reserve("tuple_object");
	      reserve("juxt_app_type_oo_o");
	      reserve("juxt_app_type_mm_m");
	      reserve("op_fact_app");
	      reserve("op_exp_app");
	      reserve("op_not_app");
	      reserve("op_prod_app");
	      reserve("op_plus_app");
	      reserve("op_plus_app_unary");
	      reserve("op_func_app");
	      reserve("op_interval_app");
	      reserve("op_eq_app");
	      reserve("op_and_app");
	      reserve("op_or_app");
	      reserve("op_impl_app");
	      reserve("binding_app");
	      reserve("openmath_OMA");
	      reserve("openmath_OMS");
	      reserve("openmath_OMR");
	      term_class("object",[]);
	      term_class("object_morphism",[]);
	      term_class("object_morphism_morphism",[]);
	      term_class("object_variable",[]);
	      term_class("object_morphism_variable",[]);
	      term_class("object_morphism_morphism_variable",[]);
	      term_class("op_fact",[]);
	      term_class("op_exp",[]);
	      term_class("op_prod",[]);
	      term_class("op_plus",[]);
	      term_class("op_interval",[]);
	      term_class("op_eq",[]);
	      term_class("op_not",[]);
	      term_class("op_and",[]);
	      term_class("op_or",[]);
	      term_class("op_impl",[]);
	      term_class("binding",[]);
	      term_class("morphism_app",[t.op_fact_app,t.op_exp_app,t.op_not_app,t.op_prod_app,t.juxt_app_type_oo_o,t.juxt_app_type_mm_m,t.op_plus_app,t.op_func_app,t.op_interval_app,t.op_eq_app,t.op_and_app,t.op_or_app,t.op_impl_app]);
	      term_class("object",[t.string,t.num_int,t.num_dec,t.num_rat,t.num_com,t.tuple_object,t.group_object,t.morphism_app,t.binding_app,t.object_variable,t.object_morphism_variable,t.object_morphism_morphism_variable,t.openmath_OMA,t.openmath_OMS,t.openmath_OMR]);
	      term_class("decimal_separator",["."]);
	      term_class("list_separator",[","]);
	      rule("num_dec","[0-9][0-9 ]*"+t.decimal_separator+"[0-9][0-9 ]*");
	      rule("num_int","[0-9][0-9 ]*");
	      rule("group_object_parenthesis",p.or([symbol("(")+space+t.object+space+symbol(")"),p.input_begin()+space+t.object+space+symbol(")"),symbol("(")+space+t.object+space+p.input_end()]));
	      rule("group_object_brackets",p.or([symbol("[")+space+t.object+space+symbol("]"),p.input_begin()+space+t.object+space+symbol("]"),symbol("[")+space+t.object+space+p.input_end()]));
	      rule("group_object_braces",p.or([symbol("{")+space+t.object+space+symbol("}"),p.input_begin()+space+t.object+space+symbol("}"),symbol("{")+space+t.object+space+p.input_end()]));
	      p.term_class("group_object",[t["group_object_parenthesis"],t["group_object_brackets"],t["group_object_braces"]
                  ]);
	      rule("openmath_OMA",symbol("OMA")+t["group_object_parenthesis"]);
	      rule("openmath_OMS",symbol("OMS")+t["group_object_parenthesis"]);
	      rule("openmath_OMR",symbol("OMR")+t["group_object_parenthesis"]);
	      rule("op_func_app",p.or([t.object_morphism_variable+t.object,t.object_morphism+space+t.object
               ]));
	      rule("op_fact_app",p.any(space+t.object+space+t.op_fact));
	      rule("op_plus_app_unary_in_exponent",t.op_plus+t.object,[t.op_exp,""]);
	      rule("op_exp_app",t.object+p.any(space+t.op_exp+space+p.or([t.object,t.op_plus_app_unary_in_exponent])));
	      rule("op_not_app",space+t.op_not+space+t.object);
	      rule("op_prod_app",p.or([t.object+p.any(space+t.op_prod+space+t.object)]));
	      term_class("multiplicative_object",[t.string,t.num_int,t.num_dec,t.num_rat,t.num_com,t.tuple_object,t.group_object,t.morphism_app,t.binding_app,t.object_variable,t.object_morphism_variable,t.object_morphism_morphism_variable]);
	      rule("juxt_app_type_oo_o",t.object+p.any(space+t.object));
	      rule("juxt_app_type_mm_m",t.morphism_app+p.any(space+t.morphism_app));
	      rule("op_plus_app_unary",t.op_plus+t.object);
	      rule("op_plus_app",p.opt(t.object)+p.any(t.op_plus_app_unary));
	      rule("op_interval_app",t.object+p.any(space+t.op_interval+space+t.object));
	      rule("op_eq_app",t.object+p.any(space+t.op_eq+space+t.object));
	      rule("op_and_app",t.object+p.any(space+t.op_and+space+t.object));
	      rule("op_or_app",t.object+p.any(space+t.op_or+space+t.object));
	      rule("op_impl_app",t.object+p.any(space+t.op_impl+space+t.object));
	      rule("binding_app",t.binding+space+t.object+space+"."+space+t.object);
	      rule("tuple_object",t.object+space+p.any(symbol(",")+space+t.object));
  }var current_context_url_array;
  function rebuild_context(context_url_array){debugger;
      if(context_url_array)current_context_url_array=context_url_array;
      else                   context_url_array=current_context_url_array;
      try{
		      parser=new Parser();
		      initialize_parser(parser);
		      stylesheet_parse=load_xml(base_url+"parse_tree_to_openmath.xsl");
		      linear_as_xml=false;
		      stylesheet_unparse=stylesheet_parse;
		      if("string"==typeof context_url_array){load_context(parser,stylesheet_parse,stylesheet_unparse,context_url_array);
		      }else{for(var i in context_url_array)
			  load_context(parser,stylesheet_parse,stylesheet_unparse,context_url_array[i]);
		      }if(debug){copy_to_clipboard(serialize_to_string(stylesheet_parse));
			  alert("templates from context copied to the clipboard");
		      }}catch(exception){message("Error: "+exception);
			  stylesheet_parse=null;
			  stylesheet_unparse=null;
		      }transformer_parse=null;
		      transformer_unparse=null;
		      if(!stylesheet_parse||!stylesheet_unparse){message("Error: could not build the stylesheet.");
			  return;
		      }try{transformer_parse=new XSLTProcessor();
			  transformer_parse.importStylesheet(stylesheet_parse);
			  transformer_unparse=transformer_parse;
		      }catch(exception){message("Error: "+exception);
			  copy_to_clipboard(serialize_to_string(stylesheet_parse));
		      }}function declare_variable(name,type){var symbol;
			  symbol=parser.symbol(name);
			  switch(type){case"object":parser.term_class("object_variable",symbol);
			      break;
			      case"morphism":parser.term_class("object_morphism_variable",symbol);
			      break;
			      case"morphism_morphism":parser.term_class("object_morphism_morphism_variable",symbol);
			      break;
			      default:message("Unknown variable type: "+type);
			  }}function linear_to_openmath(text){var parse_tree;
			      var omobj;
			      parse_tree=linear_to_parse_tree(text);
			      if(!parse_tree){if(parser){message("QMath::linear_to_openmath("+text+"): no result from parser");
			      }else{message("QMath::linear_to_openmath("+text+"): no parser");
			      }omobj=null;
			      }else{omobj=parse_tree_to_openmath(parse_tree);
			      }if(!omobj){message("QMath::linear_to_openmath("+text+"): no result from apply_transformer(transformer_parse, result)");
				  omobj=document.createElementNS(openmath_namespace,"OMOBJ");
			      }return omobj;
			  }function linear_to_parse_tree(text){var parse_tree;
			      if(parser)parse_tree=parser.parse_to_XML(text);
			      else        parse_tree=null;
			      return parse_tree;
			  }function parse_tree_to_openmath(parse_tree){var omobj;
			      omobj=apply_transformer(transformer_parse,parse_tree);
			      return omobj;
			  }function openmath_to_linear(omobj){return parse_tree_to_linear(openmath_to_parse_tree(omobj));
			  }function openmath_to_parse_tree(omobj){if(!omobj){message("Warning: omobj is null.");
			      return null;
			  }var parse_tree;
			  parse_tree=apply_transformer(transformer_unparse,omobj);
			  return parse_tree;
			  }function parse_tree_to_linear(parse_tree){return parser.unparse_from_XML(parse_tree);
			  }function apply_transformer(transformer,tree){var xslt_result;
			      try{xslt_result=transformer.transformToDocument(tree).documentElement;
				  xslt_result.nodeName;
			      }catch(exception){var name;
				  var stylesheet;
				  if(transformer==transformer_parse){name="transformer_parse";
				      stylesheet=stylesheet_parse;
				  }else{name="transformer_unparse";
				      stylesheet=stylesheet_unparse;
				  }message("Error: name = "+name+": "+exception);
				  xslt_result=null;
				  var xml_content;
				  xml_content=serialize_to_string(stylesheet);
				  copy_to_clipboard(xml_content);
				  message("Faulty XSLT stylesheet copied to clipboard.");
			      }return xslt_result;
			  }function load_xml(url){var result;
			      try{var request;
				  request=new XMLHttpRequest();
				  request.open("GET",url,false);
				  request.send(null);
				  result=request.responseXML;
			      }catch(exception){message("Error: "+url+":\n"+exception);
			      }return result;
			  }function serialize_to_string(node){return xml_serializer.serializeToString(node);
			  }function copy_to_clipboard(text){if(!gClipboardHelper)return;
			      gClipboardHelper.copyString(text);
			  }function load_context(parser,stylesheet_parse,stylesheet_unparse,url){try{var context_document;
			      context_document=load_xml(url);
			      var templates_from_context;
			      templates_from_context=transformer_build_stylesheet_parse.transformToFragment(context_document.documentElement,stylesheet_parse);
			      stylesheet_parse.documentElement.appendChild(templates_from_context);
			      var settings_iterator;
			      settings_iterator=xpath('q:document/q:tokenizer-settings/@*',context_document,XPathResult.ORDERED_NODE_ITERATOR_TYPE);
			      if(settings_iterator){var tokenizer_setting;
				  while((tokenizer_setting=settings_iterator.iterateNext())){switch(tokenizer_setting.name){case"ignore-space":parser.ignore_space=("true"==tokenizer_setting.value);
				      break;
				      case"implicit-variable-name-pattern":parser.implicit_variable_name_pattern=tokenizer_setting.value;
				      if(parser.implicit_variable_name_pattern&&'^'!=parser.implicit_variable_name_pattern[0]){parser.implicit_variable_name_pattern="^(?:"+parser.implicit_variable_name_pattern+")";
				      }break;
				      default:message("Error: unknown tokenizer setting '"+tokenizer_setting.name+"'");
				  }}}var symbol_iterator;
				  symbol_iterator=xpath('//q:symbol',context_document,XPathResult.ORDERED_NODE_ITERATOR_TYPE);
				  if(symbol_iterator){var term_classes;
				      term_classes={};
				      var attributes;
				      attributes={};
				      var attribute_names;
				      attribute_names=["token","type","cd","name"];
				      var op_plus_implicit_symbol_token;
				      var symbol;
				      while((symbol=symbol_iterator.iterateNext())){for(var i in attribute_names){var name;
					  name=attribute_names[i];
					  if(symbol.hasAttribute(name)){attributes[name]=symbol.getAttribute(name);
					  }else{var attribute_node;
					      attribute_node=xpath_first("ancestor::q:group/@"+name,symbol);
					      if(attribute_node){attributes[name]=attribute_node.value;
					      }else switch(attributes["type"]){case"object_variable":case"object_morphism_variable":case"object_morphism_morphism_variable":if("cd"==name||"name"==name)break;
						  default:message("Error: "+url+": missing attribute '"+name+"' at "+build_xpath(symbol));
					      }}}if("op_plus"==attributes.type&&(!(symbol.hasAttribute("unary_cd")&&symbol.hasAttribute("unary_name")))){if(op_plus_implicit_symbol_token){message("Error in context: "+url+"\n"+"The symbol '"+attributes.token+"' does not have an explicit unary variant, and there is already a symbol with an implicit unary variant: '"+op_plus_implicit_symbol_token+"'");
					      }else{op_plus_implicit_symbol_token=attributes.token;
					      }}var token_array;
					      token_array=term_classes[attributes.type];
					      if(!token_array){token_array=new Array();
						  term_classes[attributes.type]=token_array;
					      }token_array.push(attributes.token);
				      }for(var type in term_classes){parser.term_class(type,term_classes[type]);
				      }}}catch(exception){message("Error: url = '"+url+"': "+exception);
				      }}function measure_performance(text){if(!text)text="413x^7-11x^6+8172x^5-311x^4+8873x^3-12x^2+1132x+892/112+(124/pi^66)+413x^7-11x^6+8172x^5-311x^4+8873x^3-12x^2+1132x+892/112+(124/pi^66)+413x^7-11x^6+8172x^5-311x^4+8873x^3-12x^2+1132x+892/112+(124/pi^66)+413x^7-11x^6+8172x^5-311x^4+8873x^3-12x^2+1132x+892/112+(124/pi^66)";
					  var results;
					  results=[];
					  var timer;
					  timer=new Progress_reporter();
					  timer.begin_wait();
					  var parse_tree;
					  parse_tree=linear_to_parse_tree(text);
					  timer.end_wait();
					  results.push("linear_to_parse_tree(): "+timer.get_wait_length()+"ms");
					  var new_omobj;
					  timer.begin_wait();
					  new_omobj=parse_tree_to_openmath(parse_tree);
					  timer.end_wait();
					  results.push("parse_tree_to_openmath(): "+timer.get_wait_length()+"ms");
					  return results;
				      }}//


//




//




//





function Formula_editor(formula_field)
  {
    var self = this;
    this.init = init;
    this.select_node = select_node;
    this.change_to_context = change_to_context;
    this.get_variables = get_variables;
    this.declare_variable = declare_variable;
    this.linear_to_openmath = linear_to_openmath;
    this.enable_edit = enable_edit;
    this.set_visibility = set_visibility;
    this.insert = insert;
    this.key_down = key_down;
    this.key_press = key_press;
    this.set_parse_tree_display = set_parse_tree_display;
    this.set_zoom = set_zoom;
    this.resize_to_fit_formula = resize_to_fit_formula;
    this.focus = focus;

    this.onchange = null;
    this.exit_start = null;
    this.exit_end = null;

    var xul_namespace = "http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul";
    var openmath_namespace = "http://www.openmath.org/OpenMath";
    var    qmath_namespace = "http://www.matracas.org/ns/qmath";
    var   mathml_namespace = "http://www.w3.org/1998/Math/MathML";
    var    xhtml_namespace = "http://www.w3.org/1999/xhtml";

    var namespace_prefix_map =
    {
      "xul" :      xul_namespace,
      "om"  : openmath_namespace,
      "q"   :    qmath_namespace,
      "m"   :   mathml_namespace,
      "h"   :    xhtml_namespace
    };
    
    


    var omobj;
    var linear;
    var linear_is_plain_text_field;
    var editable;
    var contexts_directory_url;
    var qmath;
    var declared_variables;

    var parse_tree_visualization;

    var resize_threshold = 2;
    var scrollbar_height = 14;
    var minimum_formula_height = scrollbar_height * 6;
    var margin = 0;

    function init(parser_path, contexts_path)
    {


      qmath = new QMath(parser_path);
      declared_variables = {};
      contexts_directory_url = contexts_path;
      
      
      if (contexts_directory_url[contexts_directory_url.length - 1] != "/")
        {
          contexts_directory_url += "/";
        }
      get_linear();
    }

    function get_linear()
    {
      if (!linear)
        {
          switch (formula_field.localName)
            {
            case "iframe":
            case "browser":
            case "editor":
              formula_field = formula_field.contentDocument;
              linear = formula_field.documentElement;
              linear_is_plain_text_field = false;
              break;
            case "textbox":
            case "textarea":
            case "input":
            case "searchbox":
              linear = formula_field;
              linear_is_plain_text_field = true;
              break;
            default:
              message("Error: linear input element is a '"
                      + formula_field.nodeName + "'");
              break;
            }
        }

      return linear;
    }

    function linear_to_openmath(text)
    {
      self.parse_tree = qmath.linear_to_parse_tree(text);
      build_parse_tree_visualization(self.parse_tree, parse_tree_visualization);
      var new_omobj;
      new_omobj = qmath.parse_tree_to_openmath(self.parse_tree);
      if (editable)
        {
          var input_field;
          if ("mInputField" in formula_field) input_field = formula_field.mInputField;
          else                                input_field = formula_field;

          if ("result" == new_omobj.localName)
            {


              
              
              
              
              
              
              new_omobj = null;
              input_field.style.backgroundColor = "#FFCCCC";
            }
          else
            {
              input_field.style.backgroundColor = "transparent";
            }

          if (new_omobj)
            {
              omobj = new_omobj;
              if (self.onchange) self.onchange(omobj);
            }
        }
      else
        {
          message(omobj);
        }

      resize_to_fit_formula();

      return omobj;
    }

    function set_zoom(factor)
    {
      if (! linear_is_plain_text_field)
        {
          formula_field.markupDocumentViewer.textZoom = factor;
        }
    }

    function key_press(event)
    {





      event.stopPropagation();

      switch (event.charCode)
        {
        case 0:
          switch (event.keyCode)
            {
            case event.DOM_VK_RETURN:
            case event.DOM_VK_ENTER:
              message("TODO: break the equation here. It's not trivial.");

              break;
            }
          break;
        case 36:
          self.exit_end();
          event.preventDefault();
          break;
        }

      return false;
    }

    function key_down(event)
    {
      var input;
      var start;
      var end;
      input = get_linear();
      start = input.selectionStart;
      end   = input.selectionEnd;

      switch (event.keyCode)
        {
        case event.DOM_VK_RIGHT:
          if (start == end)
            {
              if (start == input.value.length)
                {
                  self.exit_end();
                  event.preventDefault();
                }
            }
          break;
        case event.DOM_VK_LEFT:
          if (start == end)
            {
              if (start == 0)
                {
                  self.exit_start();
                  event.preventDefault();
                }
            }
          break;
        }

      return false;
    }

    function resize_to_fit_formula()
    {
      if (linear_is_plain_text_field)
        {
          if ("inputField" in formula_field && formula_field.multiline)
            {
              if (margin < 1) margin = formula_field.boxObject.height - formula_field.inputField.scrollHeight;
              formula_field.height = formula_field.inputField.scrollHeight + margin;
            }
        }
      else
        {
          if ("true" == window.frameElement.getAttribute("collapsed")) return;

          var formula;
          formula = get_linear();
          if (!formula) {message("Error: formula="+formula);return;}

          var qmath_frame;
          qmath_frame = window.frameElement;
          var formula_height;
          formula_height = formula.clientHeight + scrollbar_height;
          if (formula_height < minimum_formula_height)
            {
              formula_height = minimum_formula_height;
            }
          var qmath_frame_height;
          qmath_frame_height = qmath_frame.clientHeight;
          var qmath_frame_overhead;
          qmath_frame_overhead = qmath_frame_height - formula.parentNode.clientHeight;
          var size_delta;
          size_delta = formula_height - formula.parentNode.clientHeight;
          var size_delta_ems;
          size_delta_ems = formula_height * parseFloat(window.getComputedStyle(qmath_frame, null).getPropertyValue("height")) /size_delta;
	  message("resize_to_fit_formula(): size_delta = "+size_delta+", resize_threshold = "+resize_threshold);
	  if(Math.abs(size_delta)> resize_threshold){message("resize(), qmath_frame = "+qmath_frame.nodeName);
	      resize(qmath_frame,size_delta);
	  }}}function resize(element,delta){element.style.height=(element.boxObject.height+delta).toString()+"px";
	  }function select_node(new_omobj){if(new_omobj!=omobj){omobj=new_omobj;
	      update_linear();
	  }}function change_to_context(context){qmath.rebuild_context(contexts_directory_url+context+".xml");
	      for(v in declared_variables){qmath.declare_variable(v,declared_variables[v]);
	      }if(omobj)update_linear();
	  }function get_variables(){var result;
	      result={free:[],bound:[]};
	      if(!omobj)return result;
	      var variable_iterator;
	      variable_iterator=xpath("descendant::om:OMV",omobj);
	      var variable;
	      while((variable=variable_iterator.iterateNext())){if(xpath_boolean("ancestor::om:OMBIND/om:OMBVAR/om:OMV[@name='"+variable.getAttribute("name")+"']",variable)){result.bound.push(variable);
	      }else{result.free.push(variable);
	      }}return result;
	  }function declare_variable(name,type){if(name in declared_variables){if(declared_variables[name]!=type){declared_variables[name]=type;
	      qmath.rebuild_context();
	      for(v in declared_variables){qmath.declare_variable(v,declared_variables[v]);
	      }}}else{declared_variables[name]=type;
		  qmath.declare_variable(name,type);
	      }linear_to_openmath(formula_field.value);
	  }function update_linear(){if(!get_linear()){message("Error: QMath: no linear container available.");
	      return;
	  }while(linear.lastChild)linear.removeChild(linear.lastChild);
	  if(omobj){var new_content;
	      if(linear_is_plain_text_field){self.parse_tree=qmath.openmath_to_parse_tree(omobj);
		  new_content=qmath.parse_tree_to_linear(self.parse_tree);
		  linear.value="mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm";
		  linear.value="mmm";
		  linear.value=new_content;
	      }else{new_content=qmath.openmath_to_parse_tree(omobj);
		  if(new_content){linear.appendChild(new_content);
		  }else{message("Error: no new_content.");
		  }}}else{message("Error: no omobj.");
		  }resize_to_fit_formula();
	  }function enable_edit(value){editable=value;
	      if(editable){if("textbox"==formula_field.localName){formula_field.removeAttribute("readonly");
	      }}}function set_visibility(visible){document.getElementById("formula-editor-container").hidden=!visible;
		  document.getElementById("qmath-formula-field").hidden=!visible;
	      }function insert(omobj){if(typeof(omobj)=='string'){alert(omobj);
		  return;
	      }var template;
	      template=" "+qmath.openmath_to_linear(omobj)+" ";
	      var start;
	      var end;
	      start=formula_field.selectionStart;
	      end=formula_field.selectionEnd;
	      var selection_text;
	      selection_text=formula_field.value.substring(start,end);
	      var placeholder_index;
	      placeholder_index=template.indexOf("\uEEEE");
	      if(placeholder_index < 0)placeholder_index=template.indexOf("\u25A0");
	      var text;
	      var new_start,new_end;
	      if(placeholder_index >=0){if(selection_text){text=template.replace(/\uEEEE./,selection_text).replace(/\u25A0/,selection_text);
		  new_start=start;
		  new_end=start+text.length;
	      }else{text=template.replace(/\uEEEE/,selection_text).replace(/\u25A0/,"\u25A1");
		  new_start=start+placeholder_index;
		  new_end=new_start+1;
	      }}else{text=template;
		  new_start=start;
		  new_end=start+text.length;
	      }formula_field.value=formula_field.value.substring(0,start)+text+formula_field.value.substring(end);
	      linear_to_openmath(formula_field.value);
	      formula_field.setSelectionRange(new_start,new_end);
	      formula_field.focus();
	      }function focus(){formula_field.focus();
	      }function add_context(context_path){var contexts;
		  contexts=context_path.split(",");
		  var context_list;
		  context_list=document.getElementById('document-context-list');
		  var i;
		  for(i=0;
			  i < contexts.length;
			  ++i){context_list.appendItem(contexts[i]);
		      context_list.getItemAtIndex(context_list.getRowCount()-1).label=contexts[i];
		  }qmath.rebuild_context(get_context_array(contexts_directory_url));
		  update_linear();
	      }function remove_context(item){var list;
		  list=item.parentNode;
		  list.removeItemAt(list.getIndexOfItem(item));
		  qmath.rebuild_context(get_context_array(contexts_directory_url));
		  update_linear();
	      }function get_context_array(prefix){var notation_menu;
		  notation_menu=document.getElementById("notation-menu-button");
		  prefix+=notation_menu.value+"/";
		  return application.get_context_array(prefix,omobj);
	      }function set_parse_tree_display(display){if(display){if(! parse_tree_visualization){parse_tree_visualization=document.getElementById("parse-tree-visualization");
		  document.getElementById("qmath-formula-field").doCommand();
	      }}else{parse_tree_visualization=null;
	      }}function build_parse_tree_visualization(parse_tree,parse_tree_visualization){if(!parse_tree_visualization)return;
		  while(parse_tree_visualization.lastChild){parse_tree_visualization.removeChild(parse_tree_visualization.lastChild);
		  }for(var i in parse_tree.childNodes){var parseNode;
		      parseNode=parse_tree.childNodes[i];
		      if(Node.ELEMENT_NODE!=parseNode.nodeType)continue;
		      var visualizationNode;
		      var label;
		      var content;
		      switch(parseNode.localName){case"token":case"text":visualizationNode=document.createElementNS(xul_namespace,"label");
			  visualizationNode.setAttribute("class","token");
			  visualizationNode.setAttribute("value",parseNode.getAttribute("literal"));
			  visualizationNode.setAttribute("tooltiptext",parseNode.localName+" @literal="+parseNode.getAttribute("literal"));
			  parse_tree_visualization.appendChild(visualizationNode);
			  break;
			  case"string":visualizationNode=document.createElementNS(xul_namespace,"vbox");
			  visualizationNode.setAttribute("class","string");
			  label=document.createElementNS(xul_namespace,"label");
			  label.setAttribute("value",parseNode.localName);
			  content=document.createElementNS(xul_namespace,"hbox");
			  content.setAttribute("class","content");
			  visualizationNode.appendChild(label);
			  visualizationNode.appendChild(content);
			  build_parse_tree_visualization(parseNode,content);
			  break;
			  case"num_dec":case"num_int":case"num_rat":case"num_com":visualizationNode=document.createElementNS(xul_namespace,"vbox");
			  visualizationNode.setAttribute("class","number");
			  label=document.createElementNS(xul_namespace,"label");
			  label.setAttribute("value",parseNode.localName);
			  content=document.createElementNS(xul_namespace,"hbox");
			  content.setAttribute("class","content");
			  visualizationNode.appendChild(label);
			  visualizationNode.appendChild(content);
			  build_parse_tree_visualization(parseNode,content);
			  break;
			  case"group_object_parenthesis":case"group_object_brackets":case"group_object_braces":case"tuple_object_parenthesis":case"tuple_object_brackets":case"tuple_object_braces":case"tuple_object":case"unfenced_tuple_object":case"juxt_app_type_oo_o":case"juxt_app_type_mm_m":visualizationNode=document.createElementNS(xul_namespace,"vbox");
			  visualizationNode.setAttribute("class","grouping");
			  label=document.createElementNS(xul_namespace,"label");
			  label.setAttribute("value",parseNode.localName);
			  content=document.createElementNS(xul_namespace,"hbox");
			  content.setAttribute("class","content");
			  visualizationNode.appendChild(label);
			  visualizationNode.appendChild(content);
			  build_parse_tree_visualization(parseNode,content);
			  break;
			  case"op_fact_app":case"op_exp_app":case"op_not_app":case"op_prod_app":case"op_plus_app":case"op_plus_app_unary":case"op_func_app":case"op_interval_app":case"op_eq_app":case"op_and_app":case"op_or_app":case"op_impl_app":visualizationNode=document.createElementNS(xul_namespace,"vbox");
			  visualizationNode.setAttribute("class","application");
			  label=document.createElementNS(xul_namespace,"label");
			  label.setAttribute("value",parseNode.localName);
			  content=document.createElementNS(xul_namespace,"hbox");
			  content.setAttribute("class","content");
			  visualizationNode.appendChild(label);
			  visualizationNode.appendChild(content);
			  build_parse_tree_visualization(parseNode,content);
			  break;
			  case"binding_app":visualizationNode=document.createElementNS(xul_namespace,"vbox");
			  visualizationNode.setAttribute("class","binding");
			  label=document.createElementNS(xul_namespace,"label");
			  label.setAttribute("value",parseNode.localName);
			  content=document.createElementNS(xul_namespace,"hbox");
			  content.setAttribute("class","content");
			  visualizationNode.appendChild(label);
			  visualizationNode.appendChild(content);
			  build_parse_tree_visualization(parseNode,content);
			  break;
			  case"openmath_OMA":case"openmath_OMS":case"openmath_OMR":case"openmath_OMBIND":case"openmath_OMBVAR":visualizationNode=document.createElementNS(xul_namespace,"vbox");
			  visualizationNode.setAttribute("class","openmath-literal");
			  label=document.createElementNS(xul_namespace,"label");
			  label.setAttribute("value",parseNode.localName);
			  content=document.createElementNS(xul_namespace,"hbox");
			  content.setAttribute("class","content");
			  visualizationNode.appendChild(label);
			  visualizationNode.appendChild(content);
			  build_parse_tree_visualization(parseNode,content);
			  break;
			  case"result":visualizationNode=null;
			  build_parse_tree_visualization(parseNode,parse_tree_visualization);
			  break;
			  default:if(parseNode&&parseNode["childNodes"]){visualizationNode=document.createElementNS(xul_namespace,"hbox");
				      visualizationNode.setAttribute("class","unknown");
				      build_parse_tree_visualization(parseNode,visualizationNode);
				  }else{visualizationNode=document.createElementNS(xul_namespace,"label");
				      visualizationNode.setAttribute("class","unknown");
				      visualizationNode.appendChild(document.createTextNode(parseNode.nodeName));
				  }}if(visualizationNode){parse_tree_visualization.appendChild(visualizationNode);
				  }}}function build_parse_tree_visualization_boxes(parse_tree,parse_tree_visualization){while(parse_tree_visualization.lastChild){parse_tree_visualization.removeChild(parse_tree_visualization.lastChild);
				  }for(var i in parse_tree.childNodes){var parseNode;
				      parseNode=parse_tree.childNodes[i];
				      if(Node.ELEMENT_NODE!=parseNode.nodeType)continue;
				      var visualizationNode;
				      var auxNode;
				      switch(parseNode.localName){case"token":visualizationNode=document.createElementNS(xul_namespace,"label");
					  visualizationNode.setAttribute("class","token");
					  visualizationNode.appendChild(document.createTextNode(parseNode.getAttribute("literal")));
					  parse_tree_visualization.appendChild(visualizationNode);
					  break;
					  case"num_dec":case"num_int":case"num_rat":case"num_com":visualizationNode=document.createElementNS(xul_namespace,"hbox");
					  visualizationNode.setAttribute("class","number");
					  build_parse_tree_visualization_boxes(parseNode,visualizationNode);
					  break;
					  case"group_object":case"set_object":case"tuple_object":case"unfenced_tuple_object":case"juxt_app_type_oo_o":case"juxt_app_type_mm_m":visualizationNode=document.createElementNS(xul_namespace,"hbox");
					  visualizationNode.setAttribute("class","grouping");
					  build_parse_tree_visualization_boxes(parseNode,visualizationNode);
					  break;
					  case"op_fact_app":case"op_exp_app":case"op_not_app":case"op_prod_app":case"op_plus_app":case"op_plus_app_unary":case"op_func_app":case"op_interval_app":case"op_eq_app":case"op_and_app":case"op_or_app":case"op_impl_app":visualizationNode=document.createElementNS(xul_namespace,"hbox");
					  visualizationNode.setAttribute("class","application");
					  build_parse_tree_visualization_boxes(parseNode,visualizationNode);
					  break;
					  case"binding_app":visualizationNode=document.createElementNS(xul_namespace,"hbox");
					  visualizationNode.setAttribute("class","binding");
					  build_parse_tree_visualization_boxes(parseNode,visualizationNode);
					  break;
					  case"result":visualizationNode=null;
					  build_parse_tree_visualization_boxes(parseNode,parse_tree_visualization);
					  break;
					  default:if(parseNode&&parseNode["childNodes"]){visualizationNode=document.createElementNS(xul_namespace,"hbox");
						      visualizationNode.setAttribute("class","unknown");
						      build_parse_tree_visualization_boxes(parseNode,visualizationNode);
						  }else{visualizationNode=document.createElementNS(xul_namespace,"label");
						      visualizationNode.setAttribute("class","unknown");
						      visualizationNode.appendChild(document.createTextNode(parseNode.nodeName));
						  }}if(visualizationNode){parse_tree_visualization.appendChild(visualizationNode);
						  }}}this.measure_performance=measure_performance;
						  function measure_performance(){message("Measuring performace when parsing "+formula_field.value);
						      var results;
						      results=qmath.measure_performance(formula_field.value);
						      for(var i=0;
							      i < results.length;
							      ++i)message(results[i]);
						  }}if(!"message"in window||!window.message)window.message=alert;

