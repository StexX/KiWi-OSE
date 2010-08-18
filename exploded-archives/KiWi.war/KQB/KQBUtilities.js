

/**
 *
 *	KWQL Query Builder 
 * 
 * 	diploma thesis work by Andreas Hartl, 2009
 * 	
 */ 


/**
 * this file contains various utility and helper functions 
 * for KQB
 */

var KQBUtilities = {};


/**
 * just make an array insert function ourselves since there is none in the 
 * standard array object
 * @param {Object} item
 * @param {Object} index
 */
Array.prototype.InsertElement = function (index, item)
{
	if ( this.length == 0 )
	{ 
		this[0] = item; 
		return;
	}
	for ( var i = this.length; i >= 0; i-- )
	{
		if ( i == index )
			this[i] = item;
		if ( i > index )
			this[i] = this[i-1];				
	}	
};

/**
 * moves one element in an array to a new position
 * @param {Object} from
 * @param {Object} to
 */
Array.prototype.MoveElement = function(from, to)
{
	var temp = this[from];
	this.splice(from,1); 
	if ( to >= this.length )
		this.InsertElement(this.length,temp);
	else	
		this.InsertElement(to,temp);		
}

/**
 * check if the given element exists in the array
 * @param {Object} elem
 */
Array.prototype.contains = function (elem)
{
	for (var i = 0; i < this.length; i++)
		if ( this[i] == elem )
			return true;
		return false;
}


/**
 * IsHTMLClass
 * 
 * returns true, if elem (given as html element or element name) 
 * contains 'htmlclass' as a class
 */
KQBUtilities.IsHTMLClass = function (elem, htmlclass)
{
	if (typeof elem == "string") 
		elem = document.getElementById(elem);

    // Before doing a regexp search, optimize for a couple of common cases.
    var classes = elem.className;
    if (!classes) return false;
    if (classes == htmlclass) return true;
    return elem.className.search("\\b" + htmlclass + "\\b") != -1;
}


/**
 * AddHTMLClass
 * 
 * adds a new class to a html element
 * 
 * @param {Object} elem
 * @param {Object} htmlclass
 */
KQBUtilities.AddHTMLClass = function (elem, htmlclass)
{
    if (typeof elem == "string") elem = document.getElementById(elem);
    if (KQBUtilities.IsHTMLClass(elem, htmlclass)) return;
    if (elem.className) 
		htmlclass = " " + htmlclass;  // Whitespace if needed
    elem.className += htmlclass;
};

/**
 * RemoveHTMLClass
 * 
 * removes a html class from an element
 * 
 * @param {Object} elem
 * @param {Object} htmlclass
 */
KQBUtilities.RemoveHTMLClass = function (elem, htmlclass)
{
    if (typeof elem == "string") 
		elem = document.getElementById(elem);
    elem.className = elem.className.replace(new RegExp("\\b"+ htmlclass +"\\b\\s*", "g"), "");
};

/**
 * PointInRect
 * 
 * checks if a point is contained in a html element
 * @param {Object} point an object with x and y members
 * @param {Object} elem an html element
 */
KQBUtilities.PointInElement = function ( point, elem )
{
	var width = elem.offsetWidth;
	var height = elem.offsetHeight;
	var l = 0;
	var t = 0;
	do
	{
		l += elem.offsetLeft + KQBUtilities.GetStyleAsNumber(elem,"border-left-width");
		t += elem.offsetTop + KQBUtilities.GetStyleAsNumber(elem,"border-top-width");
	
	}
	while (elem = elem.offsetParent);
	var r = l + width;
	var b = t + height;
	return (point.x >= l) && (point.x <= r) && (point.y>=t) && (point.y<=b);
}

/**
 * GetElemRect
 * 
 * returns the (absolute) position and size of an html element
 * @param {Object} elem
 */
KQBUtilities.GetElemRect = function ( elem )
{
	var l = 0;
	var t = 0;
	var w = elem.offsetWidth;
	var h = elem.offsetHeight;
	do
	{
		l += elem.offsetLeft + KQBUtilities.GetStyleAsNumber(elem,"border-left-width");
		t += elem.offsetTop + KQBUtilities.GetStyleAsNumber(elem,"border-top-width");
	
	}
	while (elem = elem.offsetParent);
	var rect = {UpperLeftX: l, UpperLeftY: t, Width: w, Height: h };
	return rect;
}

/**
 * PointInRect
 * 
 * checks if given point lies in given rect
 * @param {Object} point
 * @param {Object} rect
 */
KQBUtilities.PointInRect = function ( point, rect )
{
//	if (!point || !rect )
//		return false;
	var l = rect.UpperLeftX;
	var r = l + rect.Width;
	var t = rect.UpperLeftY;
	var b = t + rect.Height;
	return (point.x >= l) && (point.x <= r) && (point.y>=t) && (point.y<=b);
}

/**
 * GetOffsets
 * 
 * returns the absolute offset of an html element, 
 * that is the combination off all it's offsetParents offsets
 * @param {Object} elem
 */
KQBUtilities.GetOffsets = function (elem)
{
	var l = 0;
	var t = 0;
	do
	{
		l += elem.offsetLeft + KQBUtilities.GetStyleAsNumber(elem,"border-left-width");
		t += elem.offsetTop + KQBUtilities.GetStyleAsNumber(elem,"border-top-width");
	}
	while (elem = elem.offsetParent);
	var offsets = {Left: l, Top: t };
	return offsets;
}



/**
 * GetMaxChildWidth
 * 
 * returns the width of the largest child element of a html element
 * @param {Object} elem
 */
KQBUtilities.GetMaxChildWidth = function (elem)
{
	var mw = 0;
	var c = elem.childNodes;
	for ( var i = 0; i < c.length; i++ )
		if ( c[i].offsetWidth )
			if (c[i].offsetWidth > mw )
				mw = c[i].offsetWidth;
	return mw;
}

/**
 * GetMaxChildHeight
 * 
 * returns the height of the largest child element of a html element
 * @param {Object} elem
 */
KQBUtilities.GetMaxChildHeight = function (elem)
{
	var mw = 0;
	var c = elem.childNodes;
	for ( var i = 0; i < c.length; i++ )
		if ( c[i].offsetHeight )
			if (c[i].offsetHeight > mw )
				mw = c[i].offsetHeight;
	return mw;
}

/**
 * GetStyle
 * 
 * returns the rendered style (no matter if defined in stylesheet or inline)
 * of the given html element, cross browser compatible
 * @param {Object} elem
 * @param {Object} style
 */
KQBUtilities.GetStyle = function (elem, style)
{
	if (!elem ) 
		return "";
	if (elem.nodeType != 1) // only continue if elem is a html element node, since e.g. on document nodes
		return "";			// getComputedStyle will throw an exception on some browsers (but not on all)
	var s = "";
	if(document.defaultView && document.defaultView.getComputedStyle)	// mozilla
	{
		var x = document.defaultView.getComputedStyle(elem, "");
		if (!x)
			return "";
		s = x.getPropertyValue(style);
	}
	else if(elem.currentStyle)		// IE
	{
		style = style.replace(/\-(\w)/g, function (strMatch, p1){
			return p1.toUpperCase();
		});
		s = elem.currentStyle[style];
	}
	return s;
}

/**
 * GetStyleAsNumber
 * 
 * returns the rendered style (no matter if defined in stylesheet or inline)
 * of the given html element, cross browser compatible, as a number
 * if the style is not a number, or undefined, 0 will be returned
 * @param {Object} elem
 * @param {Object} style
 */
KQBUtilities.GetStyleAsNumber = function (elem, style)
{
	var s = parseInt(KQBUtilities.GetStyle(elem,style));
	if ( isNaN(s) )
		s = 0;
	return s;
}

/**
 * returns the height of the browser window, cross-browser
 */
KQBUtilities.GetWindowHeight = function ()
{
	var Height = 0;
	if( typeof (window.innerHeight) == 'number' )
		Height = window.innerHeight;	// non-IE
	else if ( document.documentElement && ( document.documentElement.clientHeight || document.documentElement.clientHeight ) )
		Height = document.documentElement.clientHeight;	//IE standards compliant mode
	else if ( document.body && ( document.body.clientHeight || document.body.clientHeight ) )
		Height = document.body.clientHeight;
	return Height;
}

