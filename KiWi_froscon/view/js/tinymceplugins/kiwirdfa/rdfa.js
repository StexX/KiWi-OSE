
/* 
 * RDFa visualization and manipulation JavaScript Library, 
 * 
 * Marek Schmidt
*/


function RDFa(root) {
	this.root = root;
	this.document = root.ownerDocument;
	
	this.property_css_style = {};
	this.property_css_style["backgroundColor"] = "#aaffaa";
	this.property_css_style["padding"] = "3px";
	this.property_css_style["MozBorderRadius"] = "3px";
	this.property_css_style["WebkitBorderRadius"] = "3px";
	
	this.property_css_style_selected = {};
	this.property_css_style_selected["backgroundColor"] = "#ff8800";
	this.property_css_style_selected["padding"] = "3px";
	this.property_css_style_selected["MozBorderRadius"] = "3px";
	this.property_css_style_selected["WebkitBorderRadius"] = "3px";
	
	this.link_css_style = {};
	this.link_css_style["backgroundColor"] = "#aaffaa";
	this.link_css_style["padding"] = "3px";
	this.link_css_style["MozBorderRadius"] = "3px";
	this.link_css_style["WebkitBorderRadius"] = "3px";
	
	this.link_css_style_selected = {};
	this.link_css_style_selected["backgroundColor"] = "#ff8800";
	this.link_css_style_selected["padding"] = "3px";
	this.link_css_style_selected["MozBorderRadius"] = "3px";
	this.link_css_style_selected["WebkitBorderRadius"] = "3px";
	
	this.link_img_url = "./img/actionmenu.png";
	
	this.block_css_style = {};
	this.block_css_style["backgroundColor"] = "#ccffcc";
	this.block_css_style["padding"] = "3px";
	this.block_css_style["MozBorderRadius"] = "3px";
	this.block_css_style["WebkitBorderRadius"] = "3px";
	
	this.block_css_style_selected = {};
	this.block_css_style_selected["backgroundColor"] = "#ffcc00";
	this.block_css_style_selected["padding"] = "3px";
	this.block_css_style_selected["MozBorderRadius"] = "3px";
	this.block_css_style_selected["WebkitBorderRadius"] = "3px";
	
	this.block_control_css_style = {};
	this.block_control_css_style["backgroundColor"] = "#ccffcc";
	this.block_control_css_style["padding"] = "3px";
	this.block_control_css_style["MozBorderRadius"] = "3px";
	this.block_control_css_style["WebkitBorderRadius"] = "3px";
	
	this.block_control_css_style_selected = {};
	this.block_control_css_style_selected["backgroundColor"] = "#ffcc00";
	this.block_control_css_style_selected["padding"] = "3px";
	this.block_control_css_style_selected["MozBorderRadius"] = "3px";
	this.block_control_css_style_selected["WebkitBorderRadius"] = "3px";
	
	this.selectedPropertyElement = null;
	this.selectedBlockContainerElement = null;
}

RDFa.prototype.setSelectedPropertyElement = function (element) {
	if (this.selectedPropertyElement != null) {
		for (var p in this.property_css_style) {
			this.selectedPropertyElement.style[p] = this.property_css_style[p];
		}
	}
	
	this.selectedPropertyElement = element;
	
	if (this.selectedPropertyElement != null) {
		for (var p in this.property_css_style) {
			this.selectedPropertyElement.style[p] = this.property_css_style_selected[p];
		}
	}
}

RDFa.prototype.setSelectedLinkElement = function (element) {
	if (this.selectedLinkElement != null) {
		for (var p in this.link_css_style) {
			this.selectedLinkElement.style[p] = this.link_css_style[p];
		}
	}
	
	this.selectedLinkElement = element;
	
	if (this.selectedLinkElement != null) {
		for (var p in this.link_css_style_selected) {
			this.selectedLinkElement.style[p] = this.link_css_style_selected[p];
		}
	}
}

RDFa.prototype.getControlElementByBlockContainerElement = function (element) {
	var e = element.firstChild;
	while(e != null && !e.hasAttribute("rdfa_block_control")) {
		e = e.nextSibling;
	}
	
	return e;
}

/* either block or iteration item */
RDFa.prototype.getAboutControlElement = function (element) {
	var e = element.firstChild;
	while(e != null && !e.hasAttribute("rdfa_block_control") && !e.hasAttribute("rdfa_iteration_item_control")) {
		e = e.nextSibling;
	}
	
	return e;
}

RDFa.prototype.getContentElementByBlockContainerElement = function (element) {
	var e = element.firstChild;
	while(e != null && !e.hasAttribute("rdfa_block_content")) {
		e = e.nextSibling;
	}
	
	return e;
}

RDFa.prototype.getIterationContentElement = function (element) {
	var e = element.firstChild;
	while(e != null && !e.hasAttribute("rdfa_iteration_content")) {
		e = e.nextSibling;
	}
	
	return e;
}

RDFa.prototype.getIterationItemIterationElement = function (item) {
	/* iteration div -> iteration content div -> iteration item div */
	return item.parentNode.parentNode;
}

RDFa.prototype.getIterationItemControlElement = function (element) {
	var e = element.firstChild;
	while(e != null && !e.hasAttribute("rdfa_iteration_item_control")) {
		e = e.nextSibling;
	}
	
	return e;
}

RDFa.prototype.getIterationItemContentElement = function (element) {
	var e = element.firstChild;
	while(e != null && !e.hasAttribute("rdfa_iteration_item_content")) {
		e = e.nextSibling;
	}
	
	return e;
}



RDFa.prototype.getLinkControlElement = function (element) {
	var e = element.firstChild;
	while(e != null) {
		if (e.nodeType == 1 && e.hasAttribute("rdfa_link_control")) {
			break;
		}

		e = e.nextSibling;
	}
	
	return e;
}


/* about element is can either be "block container" or "iteration item" */
RDFa.prototype.setSelectedAboutElement = function (element) {
	if (this.selectedAboutElement != null) {
		for (var p in this.block_css_style) {
			this.selectedAboutElement.style[p] = this.block_css_style[p];
		}
		
		var selectedAboutControlElement = this.getAboutControlElement (this.selectedAboutElement);
		if (selectedAboutControlElement != null) {
			for (var p in this.block_control_css_style) {
				selectedAboutControlElement.style[p] = this.block_control_css_style[p];
			}
		}
	}
	
	this.selectedAboutElement = element;
	
	if (this.selectedAboutElement != null) {
		for (var p in this.block_css_style) {
			this.selectedAboutElement.style[p] = this.block_css_style_selected[p];
		}
		
		var selectedAboutControlElement = this.getAboutControlElement (this.selectedAboutElement);
		if (selectedAboutControlElement != null) {
			for (var p in this.block_control_css_style_selected) {
				selectedAboutControlElement.style[p] = this.block_control_css_style_selected[p];
			}
		}
	}
}

RDFa.prototype.getRoot = function() {
	return this.root;
}

/*
 * Set the property of the selected span
 */
RDFa.prototype.setProperty = function (element, property) {
	 element.setAttribute("property", property);
}
 
RDFa.prototype.getProperty = function (element) {
	 return element.getAttribute ("property");
}
 
RDFa.prototype.setAboutUri = function (element, uri) {
	element.setAttribute("about", uri);
}

RDFa.prototype.getAboutUri = function (element) {
	return element.getAttribute ("about");
}

RDFa.prototype.setAboutType = function (element, type) {
	element.setAttribute("typeof", type);
}

RDFa.prototype.getAboutType = function (element) {
	return element.getAttribute ("typeof");
}

RDFa.prototype.setAboutRelation = function (element, relation) {
	element.setAttribute("rel", relation);
}

RDFa.prototype.getAboutRelation = function (element) {
	return element.getAttribute ("rel");
}

RDFa.prototype.clearElement = function(element) {
	while (element.firstChild != null) {
		element.removeChild(element.firstChild);
	}
}

RDFa.prototype.setAboutTitle = function (element, title) {
	/* title span is the first child of the control element. */
	var controlDiv = this.getControlElementByBlockContainerElement (element);
	var titleSpan = controlDiv.childNodes.item(0);
	
	this.clearElement (titleSpan);
	titleSpan.appendChild(this.document.createTextNode(title));
}

RDFa.prototype.getLinkUri = function (element) {
	return element.getAttribute("resource");
}

RDFa.prototype.setLinkUri = function (element, uri) {
	element.setAttribute("resource", uri);
}

RDFa.prototype.getLinkRelation = function (element) {
	return element.getAttribute("rel");
}

RDFa.prototype.setLinkRelation = function (element, relation) {
	element.setAttribute ("rel", relation);
}

RDFa.prototype.getLinkType = function (element) {
	return element.getAttribute ("typeof");
}

RDFa.prototype.setLinkType = function (element, type) {
	element.setAttribute("typeof", type);
}

RDFa.prototype.getLinkTitle = function (element) {
	return element.firstChild.nodeValue;
}

RDFa.prototype.setLinkTitle = function (element, title) {
	element.replaceChild(element.ownerDocument.createTextNode(title), element.firstChild);
}

RDFa.prototype.setIterationRelation = function (element, relation) {
	element.setAttribute("rel", relation);
}

RDFa.prototype.getIterationRelation = function (element) {
	return element.getAttribute("rel");
}

RDFa.prototype.setIterationItemUri = function (element, uri) {
	element.setAttribute("about", uri);
}

RDFa.prototype.getIterationItemUri = function (element) {
	return element.getAttribute("about");
}

RDFa.prototype.setIterationItemType = function (element, type) {
	element.setAttribute ("typeof", type);
}

RDFa.prototype.getIterationItemType = function (element) {
	return element.getAttribute("typeof");
}

RDFa.prototype.setIterationItemTitle = function (element, title) {
	/* title span is the first child of the control element. */
	var controlDiv = this.getIterationItemControlElement (element);
	var titleSpan = controlDiv.childNodes.item(0);
	
	this.clearElement (titleSpan);
	titleSpan.appendChild(this.document.createTextNode(title));
}

RDFa.prototype.getIterationItemTitle = function (element) {
	var controlDiv = this.getIterationItemControlElement (element);
	var titleSpan = controlDiv.childNodes.item(0);
	if (titleSpan.firstChild != null) {
		return titleSpan.firstChild.nodeValue;
	}
	else {
		return null;
	}
}


RDFa.prototype.isRDFaPropertyElement = function (element) {

    // IEBUG: elmenet.ELEMENT_NODE is not defined in IE
    var ELEMENT_NODE = 1;	
	if (element.nodeType != ELEMENT_NODE) return false;
	
	if (element.hasAttribute("property")) {
		return true;
	}
	
	return false;
}

RDFa.prototype.isRDFaLinkElement = function (element) {
	// IEBUG: elmenet.ELEMENT_NODE is not defined in IE
    var ELEMENT_NODE = 1;	
	if (element.nodeType != ELEMENT_NODE) return false;
	
	// alert("element " + element.nodeName + " typeof: " + element.hasAttribute("typeof") + " rel:" + element.hasAttribute("rel"));
	
	if (element.nodeName.toLowerCase() == "span" && element.hasAttribute("typeof") && element.hasAttribute("rel")) {
		return true;
	}
	
	return false;
}

RDFa.prototype.isRDFaBlockContainerElement = function (element) {
	// IEBUG: elmenet.ELEMENT_NODE is not defined in IE
    var ELEMENT_NODE = 1;	
	if (element.nodeType != ELEMENT_NODE) return false;
	
	if (element.hasAttribute("rdfa_block_container")) {
		return true;
	}
	
	return false;
}

RDFa.prototype.isRDFaIterationContainerElement = function (element) {
	var ELEMENT_NODE = 1;	
	if (element.nodeType != ELEMENT_NODE) return false;
	
	if (element.hasAttribute("rdfa_iteration_container")) {
		return true;
	}
	
	return false;

}

RDFa.prototype.isRDFaIterationItemContainerElement = function (element) {
    var ELEMENT_NODE = 1;	
	if (element.nodeType != ELEMENT_NODE) return false;
	
	if (element.hasAttribute("rdfa_iteration_item_container")) {
		return true;
	}
	
	return false;
}

/*
 * Get the nearest RDFa property element on the way up the tree
 */
RDFa.prototype.getLeastRDFaPropertyElement = function (element) {
	while (element != null && element != this.getRoot() && !this.isRDFaPropertyElement(element)) {
		element = element.parentNode;
	}
	
	if (element == this.getRoot()) return null;
	
	return element;
}

RDFa.prototype.getLeastRDFaLinkElement = function (element) {
	while (element != null && element != this.getRoot() && !this.isRDFaLinkElement(element)) {
		element = element.parentNode;
	}
	
	if (element == this.getRoot()) return null;
	
	return element;
}
 
 /*
  * Get the nearest RDFa block container element on the way up the tree
  */
 RDFa.prototype.getLeastRDFaAboutElement = function (element) {
 	while (element != null && element != this.getRoot() && !this.isRDFaBlockContainerElement(element) && !this.isRDFaIterationItemContainerElement(element)) {
 		element = element.parentNode;
 	}
 	
 	if (element == this.getRoot()) return null;
 	
 	return element;
 }

RDFa.prototype.selectRDFaPropertyElement = function (element) {
	this.selectedPropertyElement = this.getLeastRDFaPropertyElement (element);
}

RDFa.prototype.selectRDFaPropertyElement = function (element) {
	this.selectedBlockContainerElement = this.getLeastRDFaBlockContainerElement (element);
}



RDFa.prototype.isSameNode = function (n1, n2) {
    if (n1.isSameNode) {
        return n1.isSameNode(n2);
    }
    else {
        return n1 == n2;
    }
}

/* 
* returns a previous text node, or null if outside the subtree defined by "root"
*/
RDFa.prototype.previousTextNode = function(root, node) {

    
	if (this.isSameNode(root, node))
		return null;

    // IEBUG: IE does not support Node.TEXT_NODE
    var TEXT_NODE = 3; 

	while (true) {
		while (!node.previousSibling) {
			node = node.parentNode;
			if (!node || this.isSameNode(root, node))
				return null;
		}

		node = node.previousSibling;

		while (node.nodeType != TEXT_NODE
				&& node.lastChild != null) {
			node = node.lastChild;
		}

		if (node.nodeType == TEXT_NODE)
			return node;
	}
}

/**
* returns a first text node below the "root" element, or null if none exists
*/
RDFa.prototype.firstTextNode = function(root) {

	var node = root;
	
	// IEBUG: IE does not support Node.TEXT_NODE
	var TEXT_NODE = 3;

	while (true) {
		while (node.nodeType != TEXT_NODE
				&& node.firstChild != null) {
			node = node.firstChild;
		}

		if (node.nodeType == TEXT_NODE)
			return node;

		if (this.isSameNode(node, root))
			return null;

		while (node.nextSibling == null) {
			node = node.parentNode;
			if (this.isSameNode(node, root))
				return null;
		}

		node = node.nextSibling;
	}
}

/*
* returns the last text node below some "root" element or null if none exists
*/
RDFa.prototype.lastTextNode = function(root) {
	var node = root;
	
	// IEBUG: IE does not support Node.TEXT_NODE
	var TEXT_NODE = 3;

	while (true) {
		while (node.nodeType != TEXT_NODE
				&& node.lastChild != null) {
			node = node.lastChild;
		}

		if (node.nodeType == TEXT_NODE)
			return node;

		if (this.isSameNode(node, root))
			return null;

		while (node.previousSibling == null) {
			node = node.parentNode;
			if (this.isSameNode(node, root))
				return null;
		}

		node = node.previousSibling;
	}
}

/*
* same as previousTextNode, but different 
*/
RDFa.prototype.nextTextNode = function(root, node) {

	// IEBUG: IE does not support Node.TEXT_NODE
	var TEXT_NODE = 3;
	
	if (this.isSameNode(root, node))
		return null;
	while (true) {
		while (node.nextSibling == null) {
			node = node.parentNode;
			if (node == null || this.isSameNode(node, root))
				return null;
		}

		node = node.nextSibling;

		while (node.nodeType != TEXT_NODE
				&& node.firstChild != null) {
			node = node.firstChild;
		}

		if (node.nodeType == TEXT_NODE)
			return node;
	}
}

/*
* Splits the text node at offset
* returns an array of two newly created text nodes
*/
RDFa.prototype.splitTextNode = function(n, offset) {
	var p = n.parentNode;
	var text = n.nodeValue;

    var doc = n.ownerDocument;
	var n1 = doc.createTextNode(text.substring(0, offset));
	var n2 = doc.createTextNode(text.substring(offset,
			n.length));

	p.replaceChild(n2, n);
	p.insertBefore(n1, n2);

	var ret = [];
	ret.push(n1);
	ret.push(n2);
	return ret;
}

RDFa.prototype.getLCP = function (n1, n2) {
	return null;
}

RDFa.prototype.deleteSpan = function (span) {
	var p = span.parentNode;
	while (span.firstChild != null) {
		p.insertBefore (span.firstChild, span);
	}
	p.removeChild (span);
}

RDFa.prototype.deleteAbout = function (div) {
	var p = div.parentNode;
	p.removeChild (div);
}

RDFa.prototype.deleteLink = function (span) {
	var p = span.parentNode;
	p.removeChild (span);
}

RDFa.prototype.deleteIteration = function (div) {
	var p = div.parentNode;
	p.removeChild(div);
}

RDFa.prototype.deleteIterationItem = function (item) {
	var p = item.parentNode;
	p.removeChild(item);
}


RDFa.prototype.createAboutInternal = function (containerDiv, controlDiv, contentDiv) {
	var doc = this.root.ownerDocument;
	var titleSpan = doc.createElement("span");
	
	controlDiv.appendChild(titleSpan);
	
	var controlModify = doc.createElement("span");
	controlModify.appendChild(doc.createTextNode(" [modify] "));
	controlDiv.appendChild(controlModify);
	
	var controlDelete = doc.createElement("span");
	controlDelete.appendChild(doc.createTextNode(" [delete] "));
	controlDiv.appendChild(controlDelete);
	
	controlModify.addEventListener("click", function(t, container, event) {
		t.onAboutModify(container);
	}.partial(this, containerDiv), false);
	
	controlDelete.addEventListener("click", function(t, container, event) {
		// t.onAboutDelete(container);
		var p = container.parentNode;
		p.removeChild (container);
	}.partial(this, containerDiv), false);
	
	
	for (var p in this.block_css_style) {
		containerDiv.style[p] = this.block_css_style[p];
		controlDiv.style[p] = this.block_css_style[p];
	}
	
	controlDiv.setAttribute("class", "mceNonEditable");

	containerDiv.setAttribute("rdfa_block_container", "true");
	controlDiv.setAttribute("rdfa_block_control", "true");
	contentDiv.setAttribute("rdfa_block_content", "true");
	
	containerDiv.addEventListener("DOMSubtreeModified", function(t, container, event) {
		/* Check consistency */
		var contentDiv = t.getContentElementByBlockContainerElement (container);
		if (contentDiv == null) {
			// contentDiv was deleted... delete the whole damn thing
			var p = container.parentNode;
			p.removeChild (container);
		}
	}.partial(this, containerDiv), false);
	
}

RDFa.prototype.createAbout = function (sel) {
	var doc = this.root.ownerDocument;	
	var contentDiv = doc.createElement("div");
	var contentDivWrapper = doc.createElement("div");
	var controlDiv = doc.createElement("div");
	var containerDiv = doc.createElement("div");
	
	
	contentDiv = this.createElementFromSelection (sel, contentDiv);
	
	/* Add a br element to the beginning, so the user have to do one more backspace to delete the whole content... */
	contentDiv.appendChild(doc.createElement("br"));
	
	var p = contentDiv.parentNode;
	p.replaceChild(containerDiv, contentDiv);
	containerDiv.appendChild(controlDiv);
	containerDiv.appendChild(contentDivWrapper);
	contentDivWrapper.appendChild (contentDiv);
	
	this.createAboutInternal (containerDiv, controlDiv, contentDivWrapper);
	
	return containerDiv;
}

/* 
 * do whatever DOM manipulations necessary to convert true RDFa to internal representation (for visualizations, etc...) 
 */
RDFa.prototype.postLoad = function (o) {
	/* Add CSS style for visualization */
	
	setStyleRecursive = function(t, e) {
		if (t.isRDFaPropertyElement(e)) {
			for (var p in t.property_css_style) {
				e.style[p] = t.property_css_style[p];
			}
		}
		
		if (t.isRDFaLinkElement(e)) {
			t.createLinkInternal(e);
		}
		
		/* Construct about or iteration item */
		if(e.nodeType == 1 && e.hasAttribute("about")) {
			var doc = t.root.ownerDocument;
			var containerDiv = e;
			var contentDiv = doc.createElement("div");
			var controlDiv = doc.createElement("div");
			
			while (e.firstChild != null) {
				var c = e.firstChild;
				e.removeChild(c);
				contentDiv.appendChild(c);
			}
			
			containerDiv.appendChild(controlDiv);
			containerDiv.appendChild(contentDiv);
			
			if (e.parentNode != null && e.parentNode.parentNode != null && e.parentNode.parentNode.hasAttribute("rdfa_iteration_container")) {
				t.createIterationItemInternal(containerDiv, controlDiv, contentDiv);
			}
			else {
				t.createAboutInternal(containerDiv, controlDiv, contentDiv);
			}
			/* skip the controlDiv in the recursion... */
			e = contentDiv;
		}

		/* Iteration, I don't know why the first hasAttribute works nor why the second does not */
		if (e.nodeType == 1 && (e.hasAttribute("kiwi:iteratedinclude") || e.hasAttributeNS("http://www.kiwi-project.eu/kiwi/html/", "iteratedinclude"))) {
			var doc = t.root.ownerDocument;
			var containerDiv = e;
			var contentDiv = doc.createElement("div");
			var controlDiv = doc.createElement("div");
			
			while (e.firstChild != null) {
				var c = e.firstChild;
				e.removeChild(c);
				contentDiv.appendChild(c);
			}
			
			containerDiv.appendChild(controlDiv);
			containerDiv.appendChild(contentDiv);
			
			t.createIterationInternal(containerDiv, controlDiv, contentDiv);

			// At the editor side, we use the "rel" attribute, instead of "kiwi:iteratedinclude"...
			// again, some weird behavior of namespaces in tinymce...
			var rel = e.getAttribute("kiwi:iteratedinclude");
			if (rel == null) {
				rel = e.getAttributeNS("http://www.kiwi-project.eu/kiwi/html/", "iteratedinclude");
			}

			containerDiv.setAttribute("rel", rel);
	
			/* skip the controlDiv in the recursion... */
			e = contentDiv;		
		}

		var children = e.childNodes;
		for (var i = 0; i < children.length; ++i) {
			var child = children.item(i);
			setStyleRecursive (t, child);
		}
	}
	
	setStyleRecursive (this, o);
}

/*
 * Do whatever DOM manipulations to convert the editor representation into a true RDFa format
 */
RDFa.prototype.preStore = function (o) {
	 clearStyleRecursive = function (t, e) {
		if (t.isRDFaPropertyElement(e)) {
			for (var p in t.property_css_style) {
				e.style[p] = "";
			}
		}
		
		if (t.isRDFaLinkElement(e)) {
			var control = t.getLinkControlElement (e);
			if (control != null) {
				e.removeChild(control);
			}
			
			for (var p in t.link_css_style) {
				e.style[p] = "";
			}
		}
		
		/* Delete the control elements in components and move the children of the content element... */
		if (t.isRDFaBlockContainerElement(e)) {
			var content = t.getContentElementByBlockContainerElement(e);
			t.clearElement(e);
			
			while (content.firstChild != null) {
				var c = content.firstChild;
				content.removeChild(c);
				e.appendChild(c);
			}
			
			e.removeAttribute("rdfa_block_container");
			
			for (var p in t.block_css_style) {
				e.style[p] = "";
			}
		}

		if (t.isRDFaIterationContainerElement(e)) {
			var content = t.getIterationContentElement(e);
			t.clearElement(e);
		
			while (content.firstChild != null) {
				var c = content.firstChild;
				content.removeChild(c);
				e.appendChild(c);
			}
			
			e.removeAttribute("rdfa_iteration_container");
			
			for (var p in t.block_css_style) {
				e.style[p] = "";
			}

			e.setAttributeNS("http://www.kiwi-project.eu/kiwi/html/", "kiwi:iteratedinclude", e.getAttribute("rel"));
		}

		if (t.isRDFaIterationItemContainerElement(e)) {
			var content = t.getIterationItemContentElement(e);
			t.clearElement(e);
	
			while (content.firstChild != null) {
				var c = content.firstChild;
				content.removeChild(c);
				e.appendChild(c);
			}
			
			e.removeAttribute("rdfa_iteration_item_container");
			
			for (var p in t.block_css_style) {
				e.style[p] = "";
			}
		}
			
		var children = e.childNodes;
		for (var i = 0; i < children.length; ++i) {
			var child = children.item(i);
			clearStyleRecursive (t, child);
		}
	 }
	 
	 clearStyleRecursive (this, o);
}

RDFa.prototype.createElementFromSelectionIE = function (sel, element) {
    var d = sel.duplicate();
    
    var p = sel.parentElement();
    d.moveToElementText (p);
    
    var f = this.firstTextNode(p);
    var e = this.lastTextNode(p);
    
    var fi = 0;
    while (true) {
        if (d.compareEndPoints ("StartToStart", sel) == 0) {
            break;
        }

        fi += 1;
        d.moveStart("character");

        if (fi >= f.length) {
            f = this.nextTextNode(p, f);
            fi = 0;

            if (f == null) break;
        }
    }

    if (f == null) {
        alert("f is null");
        return null;
    }

    var ei = e.length;
    while (true) {
        if (d.compareEndPoints ("EndToEnd", sel) == 0) {
            break;
        }

        ei -= 1;
        d.moveEnd("character", -1);

        if (ei <= 0) {
            e = this.previousTextNode(p, e);
            ei = e.length;

            if (e == null) break;
        }
    }

    if (e == null) {
        alert ("e is null!");
        return null;
    }

    
    if (f != e) {
		if (fi != 0) {
			f = this.splitTextNode(f, fi)[1];
		}

		if (ei != 0) {
			e = this.splitTextNode(e, ei)[0];
		} else {
			e = this.previousTextNode(this.getRoot(), e);
		}
	} else {
		var diff = ei - fi;
		rest = this.splitTextNode(f, fi)[1];
		e = f = this.splitTextNode(rest, diff)[0];
	}

    /*
    var doc = f.ownerDocument;	
	var span = doc.createElement("span");
	
	for (var p in this.css_style) {
		span.style[p] = this.css_style[p];
	}*/
	
	var lst = [];
	while (f != null) {
		
		lst.push (f);
		
		if (f == e)
			break;

		f = this.nextTextNode(this.getRoot(), f);
	}
	
	f.parentNode.replaceChild(element, f);
	
	/* Now import all of those to the span */
	for (var i = 0; i < lst.length; ++i) {
		var node = lst[i];
		element.appendChild (node);
	}
	
	return element;

}

RDFa.prototype.createElementFromSelection = function (sel, element) {
	// IEBUG: IE does not support w3c Range
    if (!window.getSelection && document.selection) {
        return this.createElementFromSelectionIE (sel, element);
    }
    

    var ret = null;
    
	var f = this.firstTextNode(sel.startContainer);
	var e = this.lastTextNode(sel.endContainer);
		
	if (f == null && e == null && sel.startContainer == sel.endContainer) {
		// Special case: empty selection on empty element
		sel.startContainer.appendChild(element);
		ret = element;
	}
	else 
	{
		// selections in between of elements (comtainer is empty element), for f and e nodes...
		if (f == null) {
			f = this.nextTextNode(this.getRoot(), f);
		}
		
		if (e == null) {
			e = this.previousTextNode(this.getRoot(), e);
		}

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
	
		var lst = [];
		while (f != null) {
			lst.push (f);
			if (f == e)
				break;

			f = this.nextTextNode(this.getRoot(), f);
		}
	
		f.parentNode.replaceChild(element, f);
	
		/* Now import all of those to the span */
		for (var i = 0; i < lst.length; ++i) {
			var node = lst[i];
			element.appendChild (node);
		}
	
		ret = element;
	}

	return ret;
}

/*
 * Create a span out of selection
 * @param sel
 * @return
 */
RDFa.prototype.createPropertySpan = function (sel) {
	var doc = this.root.ownerDocument;	
	var span = doc.createElement("span");
	
	for (var p in this.property_css_style) {
		span.style[p] = this.property_css_style[p];
	}
	
	return this.createElementFromSelection (sel, span);
}

RDFa.prototype.createLinkInternal = function (span) {
	var doc = this.root.ownerDocument;
	
	for (var p in this.link_css_style) {
		span.style[p] = this.link_css_style[p];
	}
	
	var control = doc.createElement("span");
	control.setAttribute("class", "mceNonEditable");
	control.setAttribute("rdfa_link_control", "true");
	control.style.backgroundImage = "url('" + this.link_img_url + "')";
	control.style.width = "20px";
	control.style.height = "16px";
	control.style.display = "inline-block";
	control.style.backgroundRepeat = "no-repeat";

	span.appendChild(control);
	
	control.addEventListener("click", function(t, span, event) {
		t.onLinkModify(span);
	}.partial(this, span), false);
}

RDFa.prototype.createLinkSpan = function (sel) {
	var doc = this.root.ownerDocument;	
	var span = doc.createElement("span");
	
	var ret = this.createElementFromSelection (sel, span);
	
	ret.setAttribute("rel", "");
	ret.setAttribute("typeof", "");
	
	this.createLinkInternal(ret);
	
	return ret;
}

RDFa.prototype.createIteration = function (sel) {
	var doc = this.root.ownerDocument;	
	var contentDiv = doc.createElement("div");
	var controlDiv = doc.createElement("div");
	var containerDiv = doc.createElement("div");
	
	contentDiv = this.createElementFromSelection (sel, contentDiv);

	/* TODO: Insert all nested components inside the selection to the iteration? or something, but deleting the selection is probably not a best way to go...*/
	this.clearElement(contentDiv);

	var p = contentDiv.parentNode;
	p.replaceChild(containerDiv, contentDiv);
	containerDiv.appendChild(controlDiv);
	containerDiv.appendChild(contentDiv);
	
	this.createIterationInternal (containerDiv, controlDiv, contentDiv);
	
	return containerDiv;
}

RDFa.prototype.createIterationInternal = function (containerDiv, controlDiv, contentDiv) {
	var doc = this.root.ownerDocument;
	var titleSpan = doc.createElement("span");
	
	controlDiv.appendChild(titleSpan);
	
	var controlModify = doc.createElement("span");
	controlModify.appendChild(doc.createTextNode(" [modify] "));
	controlDiv.appendChild(controlModify);
	
	var controlDelete = doc.createElement("span");
	controlDelete.appendChild(doc.createTextNode(" [delete] "));
	controlDiv.appendChild(controlDelete);

	var controlAdd = doc.createElement("span");
	controlAdd.appendChild(doc.createTextNode(" [add] "));
	controlDiv.appendChild(controlAdd);
	
	controlModify.addEventListener("click", function(t, container, event) {
		t.onIterationModify(container);
	}.partial(this, containerDiv), false);
	
	controlDelete.addEventListener("click", function(t, container, event) {
		var p = container.parentNode;
		p.removeChild (container);
	}.partial(this, containerDiv), false);

	controlAdd.addEventListener("click", function(t, container, event) {
		t.onIterationAdd(container);
	}.partial(this, containerDiv), false);
	
	for (var p in this.block_css_style) {
		containerDiv.style[p] = this.block_css_style[p];
		controlDiv.style[p] = this.block_css_style[p];
	}
	
	controlDiv.setAttribute("class", "mceNonEditable");

	containerDiv.setAttribute("rdfa_iteration_container", "true");
	controlDiv.setAttribute("rdfa_iteration_control", "true");
	contentDiv.setAttribute("rdfa_iteration_content", "true");
	
	containerDiv.addEventListener("DOMSubtreeModified", function(t, container, event) {
		/* Check consistency */
		var contentDiv = t.getIterationContentElement (container);
		if (contentDiv == null) {
			// contentDiv was deleted... delete the whole damn thing
			var p = container.parentNode;
			p.removeChild (container);
		}
	}.partial(this, containerDiv), false);
}


RDFa.prototype.createIterationItemInternal = function(containerDiv, controlDiv, contentDiv) {
	var doc = this.root.ownerDocument;
	var titleSpan = doc.createElement("span");
	
	controlDiv.appendChild(titleSpan);
	
	var controlModify = doc.createElement("span");
	controlModify.appendChild(doc.createTextNode(" [modify] "));
	controlDiv.appendChild(controlModify);
	
	var controlDelete = doc.createElement("span");
	controlDelete.appendChild(doc.createTextNode(" [delete] "));
	controlDiv.appendChild(controlDelete);
	
	controlModify.addEventListener("click", function(t, container, event) {
		t.onIterationItemModify(container);
	}.partial(this, containerDiv), false);
	
	controlDelete.addEventListener("click", function(t, container, event) {
		// t.onAboutDelete(container);
		var p = container.parentNode;
		p.removeChild (container);
	}.partial(this, containerDiv), false);
	
	
	for (var p in this.block_css_style) {
		containerDiv.style[p] = this.block_css_style[p];
		controlDiv.style[p] = this.block_css_style[p];
	}
	
	controlDiv.setAttribute("class", "mceNonEditable");

	containerDiv.setAttribute("rdfa_iteration_item_container", "true");
	controlDiv.setAttribute("rdfa_iteration_item_control", "true");
	contentDiv.setAttribute("rdfa_iteration_item_content", "true");
	
	containerDiv.addEventListener("DOMSubtreeModified", function(t, container, event) {
		/* Check consistency */
		var contentDiv = t.getIterationItemContentElement (container);
		if (contentDiv == null) {
			// contentDiv was deleted... delete the whole damn thing
			var p = container.parentNode;
			p.removeChild (container);
		}
	}.partial(this, containerDiv), false);
}

RDFa.prototype.createIterationItem = function(iteration) {
	var doc = this.root.ownerDocument;	
	var contentDiv = doc.createElement("div");
	var contentDivWrapper = doc.createElement("div");
	var controlDiv = doc.createElement("div");
	var containerDiv = doc.createElement("div");
	
	/* Add a br element to the beginning, so the user have to do one more backspace to delete the whole content... */
	contentDiv.appendChild(doc.createElement("br"));
	
	containerDiv.appendChild(controlDiv);
	containerDiv.appendChild(contentDivWrapper);
	contentDivWrapper.appendChild (contentDiv);
	
	this.createIterationItemInternal (containerDiv, controlDiv, contentDivWrapper);

	var iterationContent = this.getIterationContentElement (iteration);	
	iterationContent.appendChild (containerDiv);

	return containerDiv;
}


