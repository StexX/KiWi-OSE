/*
 * jQuery RTE plugin 0.3 - create a rich text form for Mozilla, Opera, and Internet Explorer
 *
 * Copyright (c) 2007 Batiste Bieler
 * Distributed under the GPL (GPL-LICENSE.txt) licenses.
 */

// define the rte light plugin
jQuery.fn.rte = function(css_url, media_url, width) {

	var iframe;
	
    if(document.designMode || document.contentEditable)
    {
        jQuery(this).each( function(){
            var textarea = jQuery(this);
            enableDesignMode(textarea);
        });
        return iframe;
    }
    
    function formatText(iframe, command, option) {
        iframe.contentWindow.focus();
        try{
            iframe.contentWindow.document.execCommand(command, false, option);
        }catch(e){console.log(e)}
        iframe.contentWindow.focus();
    }
    
    function tryEnableDesignMode(iframe, doc, callback) {
        try {
            iframe.contentWindow.document.open();
            iframe.contentWindow.document.write(doc);
            iframe.contentWindow.document.close();
        } catch(error) {
            console.log(error);
        }
        if (document.contentEditable) {
            iframe.contentWindow.document.designMode = "On";
            callback();
            return true;
        }
        else if (document.designMode != null) {
            try {
                iframe.contentWindow.document.designMode = "on";
                callback();
                return true;
            } catch (error) {
                console.log(error);
            }
        }
        setTimeout(function(){tryEnableDesignMode(iframe, doc, callback)}, 250);
        return false;
    }
    
    function enableDesignMode(textarea) {
        // need to be created this way
        iframe = document.createElement("iframe");
        iframe.frameBorder=0;
        iframe.frameMargin=0;
        iframe.framePadding=0;
        iframe.height=200;
        iframe.width=width;
        if(textarea.attr('class'))
            iframe.className = textarea.attr('class');
        if(textarea.attr('id'))
            iframe.id = textarea.attr('id');
        if(textarea.attr('name'))
            iframe.name = textarea.attr('name');
        textarea.after(iframe);
        var css = "";
        if(css_url)
            var css = "<link type='text/css' rel='stylesheet' href='"+css_url+"' />";
        var content = textarea.val();
        // Mozilla need this to display caret
        if(jQuery.trim(content)=='')
            content = '<br>';
        var doc = "<html><head>"+css+"</head><body class='frameBody'>"+content+"</body></html>";
        tryEnableDesignMode(iframe, doc, function() {
            jQuery("#toolbar-"+iframe.title).remove();
            jQuery(iframe).before(toolbar(iframe));
            textarea.remove();
        });
    }
    
    function disableDesignMode(iframe, submit) {
        var content = iframe.contentWindow.document.getElementsByTagName("body")[0].innerHTML;
        if(submit==true)
            var textarea = jQuery('<input type="hidden" />');
        else
            var textarea = jQuery('<textarea cols="40" rows="10"></textarea>');
        textarea.val(content);
        t = textarea.get(0);
        if(iframe.className)
            t.className = iframe.className;
        if(iframe.id)
            t.id = iframe.id;
        if(iframe.title)
            t.name = iframe.title;
        jQuery(iframe).before(textarea);
        if(submit!=true)
            jQuery(iframe).remove();
        return textarea;
    }
    
    function toolbar(iframe) {
        
        var tb = jQuery("<div class='rte-toolbar' id='toolbar-"+iframe.title+"'><div>\
            <p>\
                <a href='#' class='bold'><img src='"+media_url+"bold.gif' alt='bold' /></a>\
                <a href='#' class='italic'><img src='"+media_url+"italic.gif' alt='italic' /></a>\
            </p>\
            <p>\
                <a href='#' class='unorderedlist'><img src='"+media_url+"unordered.gif' alt='unordered list' /></a>\
                <a href='#' class='link'><img src='"+media_url+"link.png' alt='link' /></a>\
                <a href='#' class='image'><img src='"+media_url+"image.png' alt='image' /></a>\
            </p></div></div>");
          //<a href='#' class='disable'><img src='"+media_url+"close.gif' alt='close rte' /></a>
          //  <p>\
          //  <select>\
          //      <option value=''>Bloc style</option>\
          //      <option value='p'>Paragraph</option>\
          //      <option value='h3'>Title</option>\
          //  </select>\
          //</p>\
            
//            jQuery('select', tb).change(function(){
//            var index = this.selectedIndex;
//            if( index!=0 ) {
//                var selected = this.options[index].value;
//                formatText(iframe, "formatblock", '<'+selected+'>');
//            }
//        });
        jQuery('.bold', tb).click(function(){ formatText(iframe, 'bold');return false; });
        jQuery('.italic', tb).click(function(){ formatText(iframe, 'italic');return false; });
        jQuery('.unorderedlist', tb).click(function(){ formatText(iframe, 'insertunorderedlist');return false; });
        jQuery('.link', tb).click(function(){ 
            var p=prompt("Geben Sie eine Link-URL ein:","http://");
            if(p) {
            	p = "javascript:window.open('"+p+"');";
                formatText(iframe, 'CreateLink', p);
            }
            return false; });
        jQuery('.image', tb).click(function(){ 
            var p=prompt("Geben Sie eine Bild-URL ein:");
            if(p)
                formatText(iframe, 'InsertImage', p);
            return false; });
        jQuery('.disable', tb).click(function() {
            var txt = disableDesignMode(iframe);
            var edm = jQuery('<a href="#">Enable design mode</a>');
            tb.empty().append(edm);
            edm.click(function(){
                enableDesignMode(txt);
                return false;
            });
            return false; 
        });
        jQuery(iframe).parents('form').submit(function(){
            disableDesignMode(iframe, true); });
//        var iframeDoc = jQuery(iframe.contentWindow.document);
//        
//        //var select = jQuery('select', tb)[0];
//        iframeDoc.mouseup(function(){ 
//            setSelectedType(getSelectionElement(iframe), select);
//            return true;
//        });
//        iframeDoc.keyup(function(){ 
//            setSelectedType(getSelectionElement(iframe), select);
//            var body = jQuery('body', iframeDoc);
//            if(body.scrollTop()>0)
//                iframe.height = Math.min(350, parseInt(iframe.height)+body.scrollTop());
//            return true;
//        });
        
        return tb;
    }
        
    function setSelectedType(node, select) {
        while(node.parentNode) {
            var nName = node.nodeName.toLowerCase();
            for(var i=0;i<select.options.length;i++) {
                if(nName==select.options[i].value){
                    select.selectedIndex=i;
                    return true;
                }
            }
            node = node.parentNode;
        }
        select.selectedIndex=0;
        return true;
    }
    
    function getSelectionElement(iframe) {
        if (iframe.contentWindow.document.selection) {
            // IE selections
            selection = iframe.contentWindow.document.selection;
            range = selection.createRange();
            try {
                node = range.parentElement();
            }
            catch (e) {
                return false;
            }
        } else {
            // Mozilla selections
            try {
                selection = iframe.contentWindow.getSelection();
                range = selection.getRangeAt(0);
            }
            catch(e){
                return false;
            }
            node = range.commonAncestorContainer;
        }
        return node;
    }
}
