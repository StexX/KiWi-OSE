
/**
 *
 *	KWQL Query Builder 
 * 
 * 	diploma thesis work by Andreas Hartl, 2009
 * 	
 */ 


/**
 * this file contains the drag and drop functionality for KQB
 */


/**
 * KQBRectTree
 * 
 * this is a little helper class to store the rects of all KQBNode and KQBLabel html elements
 * for fast checks of point inclusion
 * 
 * invoke ReBuild() to create the tree on MouseDown events (when dragging starts)
 * and use GetHit() during MouseMove. This causes a lot less cpu load then querying the html
 * elements for their sizes/positions directly each mouse move
 */
KQBRectTree = function ()
{
	this.Children = new Array();
}

KQBRectTree.prototype.ReBuild = function ()
{
	this.Children.splice(0, this.Children.length);
	this.Rect = KQBUtilities.GetElemRect(KQB.Workspace);
	
	traverse(KQB.Workspace, this.Children);	
	
	function traverse ( elem, array )
	{
		var elems = elem.childNodes;
		if (elems)		
		for ( var i = 0; i < elems.length; i++ )
		{
			if (KQBDragDrop.SelectedElement)		// make sure we don't include the dragged object
				if (elems[i] == KQBDragDrop.SelectedElement)	
					continue;
			if ( KQBUtilities.IsHTMLClass(elems[i], "KQBNode") || KQBUtilities.IsHTMLClass(elems[i],"KQBLabel"))
			{
				var n = new KQBTreeNode ( KQBUtilities.GetElemRect(elems[i]), elems[i]);				
				traverse (elems[i], n.Children);				
				array.push(n);
			}
		}
	};
}


KQBTreeNode = function ( rect, htmlelem )
{
	this.Children = new Array();
	this.Rect = rect;
	this.HTMLElem = htmlelem;
}


KQBRectTree.prototype.GetHit = function (point)
{	
	var ret = check(this);
	if (ret!=null)
		return ret.HTMLElem;
	else return null;
	
	function check (elem)
	{	
		if ( !elem || !elem.Rect )
			return null;
		if (KQBUtilities.PointInRect(point, elem.Rect) != true) 
			return null;

		var elems = elem.Children;
	
		for ( var i = 0; i < elems.length; i++ )
		{
			var retval = check(elems[i]);
			if ( retval != null )
				return retval;				
		}
		return elem;
	};
}



var KQBDragDrop = new Object();


KQBDragDrop.SelectedElement = null;
KQBDragDrop.HighlightedElement = null;
KQBDragDrop.OffsetX = 0;
KQBDragDrop.OffsetY = 0;
KQBDragDrop.LastX = 0;
KQBDragDrop.LastY = 0;
KQBDragDrop.LastCursorPos = null;
KQBDragDrop.LastOffsets = {Left:0,Top:0};
KQBDragDrop.CursorPosOnLastMouseDown = {x: 0, y:0};
KQBDragDrop.LabelTextClicked = false;
KQBDragDrop.LabelResizeBoxClicked = false;
KQBDragDrop.Rects = new KQBRectTree();
KQBDragDrop.VarHighlight = false;
KQBDragDrop.HighVars = new Array();
KQBDragDrop.Resizing = false;
KQBDragDrop.ResizeOffsets = null;
KQBDragDrop.ResizeInitialWidth = 0;
KQBDragDrop.ResizeInitialHeight = 0;

/**
 * GetCursorPosition
 * 
 * returns the position of the mouse pointer/cursor given 
 * at the last event
 * works with IE and mozilla based browsers if e is supplied
 */
KQBDragDrop.GetCursorPosition = function (e)
{ 
   	e = e || window.event;
		
    var cursor = {x:0, y:0};
		
    if (e.pageX || e.pageY) 		// mozilla
	{
      	cursor.x = e.pageX;
        cursor.y = e.pageY;
    } 
    else 							// IE
	{
        cursor.x = e.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft) - document.documentElement.clientLeft;
        cursor.y = e.clientY + (document.documentElement.scrollTop || document.body.scrollTop) - document.documentElement.clientTop;
    }
    return cursor;
}

/**
 * GetEventSource
 * 
 * returns the source element of the last event
 * works with IE and mozilla based browsers if e is supplied
 */
KQBDragDrop.GetEventSource = function (e)
{
	e = e || window.event;
	return e.srcElement || e.target;
}


/**
 * GetElemBelowCursor
 * 
 * this method returns the element below the cursor
 * it will return the innermost one of nested elements
 * 
 * NOTE: Will only return dropzone elements ("KQBNode" and "KQBLabel" classes or the workarea)
 */
KQBDragDrop.GetElemBelowCursor = function (cursor)
{
	return KQBDragDrop.Rects.GetHit(cursor);
}

/**
 * GetElemBelowCursor
 * 
 * this method returns the element below the cursor
 * it will return the innermost one of nested elements
 * 
 * NOTE: Will only return dropzone elements ("KQBNode" and "KQBLabel" classes or the workarea)
 */
KQBDragDrop.GetElemBelowCursorOld = function (cursor)
{	
	return check(document.body);	
	
	function check ( elem )
	{
		if ( KQBUtilities.PointInElement(cursor, elem) != true )
			return null;
		// if we're not in some element, return, else check all children
		var elems = elem.childNodes;
		if (elems)		
		for ( var i = 0; i < elems.length; i++ )
		{
			if (KQBDragDrop.SelectedElement)		// make sure we don't always return the dragged object, which will 
				if (elems[i] == KQBDragDrop.SelectedElement)		// always be below the cursor when we're dragging
					continue;
			var retval = check(elems[i]);
			if ( retval != null )
				return retval;				
		}
		if ( KQBUtilities.IsHTMLClass(elem, "KQBNode") || KQBUtilities.IsHTMLClass(elem,"KQBLabel"))
			return elem;
		return null;
	};
}

/**
 * FindFreePosition
 *
 * attempts to find a free position in the workarea of the size of a single node
 */
KQBDragDrop.FindFreePosition = function ()
{
	var starty = KQB.Workspace.offsetTop;
	var startx = KQB.Workspace.offsetLeft;
	var xpos = startx;
	var ypos = starty;
	
	var xoffs = KQB.NodeInfo.Margin_Left + KQB.NodeInfo.Border_Left_Width + KQB.NodeInfo.Border_Right_Width;
	var yoffs = KQB.NodeInfo.Margin_Top + KQB.NodeInfo.Border_Bottom_Width + KQB.NodeInfo.Border_Top_Width;
	
	var stepx = 10;
	var stepy = 10;
	var maxy = KQB.Workspace.offsetHeight;
	KQBDragDrop.Rects.ReBuild();
	// check some points for overlap
	while ( KQBDragDrop.GetElemBelowCursor({x:xpos+xoffs,y:ypos+yoffs}) || 
		KQBDragDrop.GetElemBelowCursor({x:xpos+KQB.NodeInfo.Width+xoffs,y:ypos+yoffs}) 	|| 
		KQBDragDrop.GetElemBelowCursor({x:xpos+KQB.NodeInfo.Width+xoffs,y:ypos+KQB.NodeInfo.Height + yoffs}) ||
		KQBDragDrop.GetElemBelowCursor({x:xpos+xoffs,y:ypos+KQB.NodeInfo.Height + yoffs}) ||
		KQBDragDrop.GetElemBelowCursor({x:xpos+KQB.NodeInfo.Width/2+xoffs,y:ypos+KQB.NodeInfo.Height/2 + yoffs})
		)
	{
		
		if ( ypos > maxy )
		{
			ypos = starty; xpos = xpos + stepx;
		}
		else
		{
			ypos = ypos + stepy;
		}
	}
	return {x:xpos,y:ypos};

}

/**
 * InitTooltip
 *
 * create css tooltip containers
 */
KQBDragDrop.InitTooltip = function ()
{
	KQBDragDrop.Tooltip = document.createElement("div");
	var text = document.createTextNode("");
	KQBDragDrop.Tooltip.appendChild(text);
	document.body.appendChild(KQBDragDrop.Tooltip);
	
	KQBDragDrop.Tooltip2 = document.createElement("div");
	var text2 = document.createTextNode("");
	KQBDragDrop.Tooltip2.appendChild(text2);
	document.body.appendChild(KQBDragDrop.Tooltip2);
	
	KQBDragDrop.TooltipShown = false;
	KQBDragDrop.Tooltip2Shown = false;
	KQBDragDrop.Tooltip.style.display = "none";
	KQBDragDrop.Tooltip2.style.display = "none";
	KQBDragDrop.TooltipTempHidden = false;
}

KQBDragDrop.OnMousLeave = function (e)
{
	if (KQBDragDrop.VarHighlight == true)
	{
		KQBDragDrop.VarHighlight = false;
		for (var i = 0; i < KQBDragDrop.HighVars.length; i++)
			KQBUtilities.RemoveHTMLClass(KQBDragDrop.HighVars[i].HTMLNode.firstChild, "KQBHighlightVar");
		KQBDragDrop.HighVars = new Array();
	}
}

/**
 * OnMouseOver
 *
 * mousehandler that is invoked when moving the mouse over some html element.
 * if the element the mouse is over has a tooltip associated with it, the tooltip will be displayed
 */
KQBDragDrop.OnMouseOver = function (e)
{
	
	var id = KQBDragDrop.GetEventSource(e).getAttribute("id");
	var node = KQB.GetNodeByID(id);


	if (node) 
	{

				if (node.Type == KQB.NodeType.Variable)
				{ 
					KQBDragDrop.VarHighlight = true;
					var equalvars = KQB.GetEqualVariables (node);
					if (equalvars && equalvars.length > 1)
						for (var i = 0; i < equalvars.length; i++)
						{
							if (!KQBDragDrop.HighVars.contains(equalvars[i]))
							{
								KQBDragDrop.HighVars.push(equalvars[i]);
								KQBUtilities.AddHTMLClass(equalvars[i].HTMLNode.firstChild, "KQBHighlightVar");
								var cnode = equalvars[i];
								while (cnode.Type!=KQB.NodeType.Rule)
								{
									if (cnode.ContentHidden)
									{
										KQBDragDrop.HighVars.push(cnode);
										KQBUtilities.AddHTMLClass(cnode.HTMLNode.firstChild, "KQBHighlightVar");
									}
									cnode = cnode.Parent;
								}
							}
						}
				}
				else
				{
					if (KQBDragDrop.VarHighlight == true)
					{
						KQBDragDrop.VarHighlight = false;
						for (var i = 0; i < KQBDragDrop.HighVars.length; i++)
						KQBUtilities.RemoveHTMLClass(KQBDragDrop.HighVars[i].HTMLNode.firstChild, "KQBHighlightVar");
						KQBDragDrop.HighVars = new Array();
					}
				}

	
		
		
		var type = node.Type;
		KQB.ShowNodeTooltip(type);		// show the tooltip with info about the node
		if (e) 
			e.stopPropagation();
		if (window.event) 
			window.event.cancelBubble = true;
		// if present, show additional error/warning tooltip
		if (node.ErrorTooltip || node.WarningTooltip)
		{	
			var offs = KQBUtilities.GetOffsets (node.HTMLNode);
			if (node.ErrorTooltip)
			{
				KQBDragDrop.Tooltip.firstChild.data = node.ErrorTooltip;
				KQBDragDrop.Tooltip.className = "KQBTooltipError";
			}
			if (node.WarningTooltip)
			{
				KQBDragDrop.Tooltip.firstChild.data = node.WarningTooltip;
				KQBDragDrop.Tooltip.className = "KQBTooltipWarning";
			}
			KQBDragDrop.Tooltip.style.left = offs.Left + node.HTMLNode.offsetWidth + 5 + "px";
			KQBDragDrop.Tooltip.style.top = offs.Top + "px";
			if ( !KQBDragDrop.TooltipTempHidden )
				KQBDragDrop.Tooltip.style.display = "block";
			KQBDragDrop.TooltipShown = true;
		}
		else
		{
			if (KQBDragDrop.TooltipShown) 
			{
				KQBDragDrop.Tooltip.style.display = "none";
				KQBDragDrop.TooltipShown = false;
			}
		}
	}
	else 
	{
		if (KQBDragDrop.TooltipShown) 
		{
			KQBDragDrop.Tooltip.style.display = "none";
			KQBDragDrop.TooltipShown = false;
		}
		KQB.ShowTooltip("");
		if (KQBDragDrop.VarHighlight == true)
		{
			KQBDragDrop.VarHighlight = false;
			for (var i = 0; i < KQBDragDrop.HighVars.length; i++)
				KQBUtilities.RemoveHTMLClass(KQBDragDrop.HighVars[i].HTMLNode.firstChild, "KQBHighlightVar");
			KQBDragDrop.HighVars = new Array();
		}
	}
}

/**
 * MouseDown
 * 
 * mousedown event handler
 * picks up elements 
 */
KQBDragDrop.MouseDown = function (e)
{
	if (KQBDragDrop.GetEventSource(e) == KQB.ResizeArea)
	{
		KQBDragDrop.Resizing = true;
		KQBDragDrop.ResizeOffsets = KQBDragDrop.GetCursorPosition(e);
		KQBDragDrop.ResizeInitialHeight = KQB.Workspace.offsetHeight;
		KQBDragDrop.ResizeInitialWidth = KQB.Workspace.offsetWidth;
		return;
	}

	KQBDragDrop.Rects.ReBuild();
	var p = KQBDragDrop.GetCursorPosition(e);
	KQBDragDrop.CursorPosOnLastMouseDown = p;		// save current cursor pos, needed to check if we're clicked or dragged in MouseUp
	if ( !KQBDragDrop.LastCursorPos )
		KQBDragDrop.LastCursorPos = p;
		
	var selected = KQBDragDrop.GetEventSource(e);
	
	if ( KQBUtilities.IsHTMLClass(selected, "KQBNode") )
	{
		KQBDragDrop.SelectedElement = selected;
	}
	else if ( KQBUtilities.IsHTMLClass(selected, "KQBLabel") )
	{
		// go to the parent when a label is dragged to drag the whole node	
		KQBDragDrop.SelectedElement = selected.parentNode;
	}
	else if ( KQBUtilities.IsHTMLClass(selected, "KQBLabelText") )
	{
		// also drag the whohle node when dragging the label text
		// we're moving td->tr->tbody->table->label->node
		KQBDragDrop.SelectedElement = selected.parentNode.parentNode.parentNode.parentNode.parentNode;
		KQBDragDrop.LabelTextClicked = true;
	}
	else if ( KQBUtilities.IsHTMLClass(selected, "KQBLabelResizeBox") )
	{ 
		// also drag the whohle node when dragging the resize box
		// we're moving td->tr->tbody->table->label->node
		KQBDragDrop.SelectedElement = selected.parentNode.parentNode.parentNode.parentNode.parentNode;
		KQBDragDrop.LabelResizeBoxClicked = true;
	}	
	if (KQBDragDrop.SelectedElement != null) 
	{	
		KQBDragDrop.SelectedElement.style.zIndex = "100";	// raise z-index of selected element so it's always on top
		// one more hack for IE: to make sure the dragged element is on top of every other element,
		// in IE it's not enough to set the element's z-index higher, but it's highest parent element
		// has to be higher than that of any other nodes in the workarea
		KQBDragDrop.SelectedElementHighestParent = KQBDragDrop.SelectedElement;
		while ( KQBDragDrop.SelectedElementHighestParent.parentNode && (KQBDragDrop.SelectedElementHighestParent.parentNode != KQB.Workspace ) )
			KQBDragDrop.SelectedElementHighestParent = KQBDragDrop.SelectedElementHighestParent.parentNode;
		KQBDragDrop.SelectedElementHighestParent.style.zIndex = "100";
		
		// save current values needed for dragging or returning the element to it's original position
		KQBDragDrop.OffsetX = p.x - KQBDragDrop.SelectedElement.offsetLeft + KQB.NodeInfo.Margin_Left;
		KQBDragDrop.OffsetY = p.y - KQBDragDrop.SelectedElement.offsetTop + KQB.NodeInfo.Margin_Top;
		KQBDragDrop.LastX = KQBUtilities.GetStyleAsNumber ( KQBDragDrop.SelectedElement, "left"); 
		KQBDragDrop.LastY = KQBUtilities.GetStyleAsNumber ( KQBDragDrop.SelectedElement, "top");
		
		var o = KQBUtilities.GetOffsets(KQBDragDrop.SelectedElement);
		KQBDragDrop.LastOffsets.Left = p.x - o.Left;
		KQBDragDrop.LastOffsets.Top = p.y - o.Top;
	
		KQBDragDrop.Rects.ReBuild();
	}
	


}

/**
 * MouseUp
 * 
 * mouseup event handler
 * 
 * handles dropping of elements
 */
KQBDragDrop.MouseUp = function (e)
{
	if (KQBDragDrop.Resizing == true)
	{
		KQBDragDrop.ResizeOffsets = null;
		KQBDragDrop.Resizing = false;
		
	}
	var pos = KQBDragDrop.GetCursorPosition(e);
	// hide tooltips
	KQBDragDrop.TooltipTempHidden = false;
	if ( KQBDragDrop.TooltipShown )
	{
		KQBDragDrop.Tooltip.style.display = "none";
		KQBDragDrop.TooltipShown = false;
	}
	if ( KQBDragDrop.Tooltip2Shown )
	{
		KQBDragDrop.Tooltip2.style.display = "none";
		KQBDragDrop.Tooltip2Shown = false;
	}
	
	if (KQBDragDrop.HighlightedElement) 	// remove highlighting
	{
		KQBUtilities.RemoveHTMLClass(KQBDragDrop.HighlightedElement, "KQBHighlightRed");
		KQBUtilities.RemoveHTMLClass(KQBDragDrop.HighlightedElement, "KQBHighlightGreen");
		KQBUtilities.RemoveHTMLClass(KQBDragDrop.HighlightedElement, "KQBHighlightOrange");
		KQBDragDrop.HighlightedElement = null;
	}
	// restore z-indices
	if ( KQBDragDrop.SelectedElementHighestParent )
		KQBDragDrop.SelectedElementHighestParent.style.zIndex = "";
	if ( KQBDragDrop.SelectedElement )
		KQBDragDrop.SelectedElement.style.zIndex = "";
	
	if ( KQBDragDrop.LabelTextClicked || KQBDragDrop.LabelResizeBoxClicked )
	{
		// check if we're clicked on the labeltext, this needs special attention
		// since when clicked we fold/unfold the element, but we also allow
		// dragging of the element with the labeltext, but don't want to
		// have a 'click' event on it when dragging ends with the cursor over the labeltext
		
		// check if we've moved the mouse or just clicked
		if ( KQBDragDrop.SelectedElement && ( KQBDragDrop.CursorPosOnLastMouseDown.x == pos.x ) && ( KQBDragDrop.CursorPosOnLastMouseDown.y == pos.y ) )
		{
			var n = KQB.GetNodeByID(KQBDragDrop.SelectedElement.getAttribute("id"));
			if (KQBDragDrop.LabelTextClicked)
				n.ClickLabel();
			else
				n.ClickResizeBox ();
			
			KQBDragDrop.LabelTextClicked = false;
			KQBDragDrop.LabelResizeBoxClicked = false;
			KQBDragDrop.SelectedElement = null;
			return;			
		}
		KQBDragDrop.LabelTextClicked = false;
	}
	// from here on: we dragged
	if (KQBDragDrop.SelectedElement) 
	{	
		// if something is dragged outside the workarea it get's deleted
		if (!KQBUtilities.PointInElement(pos, KQB.Workspace)) 
		{
			KQB.DeleteNode(KQBDragDrop.SelectedElement.getAttribute("id"));
			KQBDragDrop.SelectedElement = null;
			KQB.ShowDebug("node deleted");
			KQB.UpdateOutput();
			return;
		}
		
		var x = KQBDragDrop.GetElemBelowCursor(pos);
		if (x != null) 
		{
			var parent = KQB.GetNodeByID(x.getAttribute("id"));
			var child = KQB.GetNodeByID(KQBDragDrop.SelectedElement.getAttribute("id"));
			
			if (KQBUtilities.IsHTMLClass(x, "KQBNode")) 
			{
				if (!child.DropOnNodeBody(parent)) 
				{	// return element to it's starting position when it was drop is not allowed here
					KQBDragDrop.SelectedElement.style.left = KQBDragDrop.LastX + "px";
					KQBDragDrop.SelectedElement.style.top = KQBDragDrop.LastY + "px";
				}
			}
			else 
				if (KQBUtilities.IsHTMLClass(x, "KQBLabel")) 
				{	// dropping a node on the label of another one will switch those two node's types
					// but the node itself must be returned to it's original location
					child.DropOnNodeLabel(parent);
					KQBDragDrop.SelectedElement.style.left = KQBDragDrop.LastX + "px";
					KQBDragDrop.SelectedElement.style.top = KQBDragDrop.LastY + "px";
				}
		}
		else 
		{	// we dropped on workarea, but outside any elements
			var position = {x: KQBDragDrop.LastCursorPos.x - KQBDragDrop.LastOffsets.Left, y: KQBDragDrop.LastCursorPos.y - KQBDragDrop.LastOffsets.Top };
			position.x -= KQB.NodeInfo.Margin_Left + KQB.NodeInfo.Border_Left_Width;
			position.y -= KQB.NodeInfo.Margin_Top + KQB.NodeInfo.Border_Top_Width;
			KQB.GetNodeByID(KQBDragDrop.SelectedElement.getAttribute("id")).DropOnWorkspace(position);
		}	
		KQB.UpdateOutput();	
	}
	
	KQBDragDrop.SelectedElement = null;
	
	// make sure to remove highlighting
	if (KQBDragDrop.HighlightedElement) 
	{
		KQBUtilities.RemoveHTMLClass(KQBDragDrop.HighlightedElement, "KQBHighlightRed");
		KQBUtilities.RemoveHTMLClass(KQBDragDrop.HighlightedElement, "KQBHighlightGreen");
		KQBDragDrop.HighlightedElement = null;
	}
	KQBDragDrop.Rects.ReBuild();
}

/**
 * MouseMove
 * 
 * handler that is executed on every mouse move
 * @param {Object} e
 */
KQBDragDrop.MouseMove = function (e)
{
	
	if (KQBDragDrop.Resizing == true)
	{
		var p = KQBDragDrop.GetCursorPosition(e);
		var width = KQBDragDrop.ResizeInitialWidth + (p.x-KQBDragDrop.ResizeOffsets.x) + "px";
		KQB.Workspace.style.height = KQBDragDrop.ResizeInitialHeight + (p.y - KQBDragDrop.ResizeOffsets.y) + "px";
		KQB.Workspace.style.width = width;
		KQB.HintArea.style.width = width;
		KQB.Hintpane.style.width = KQBDragDrop.ResizeInitialWidth + (p.x-KQBDragDrop.ResizeOffsets.x) - KQB.ResizeArea.offsetWidth + "px";
		KQB.Tooltippane.style.width = width;
		KQB.AutoResize = false;
		return;
	}
	
	
	
	if (KQBDragDrop.SelectedElement) 
	{	
		var n = KQB.GetNodeByID(KQBDragDrop.SelectedElement.getAttribute("id"));
		if (n.isFixed == true) 
		{
			// don't allow dragging of fixed nodes
			KQBDragDrop.SelectedElement = null;
			return;
		}
		// if we're dragging something, update it's position
		var p = KQBDragDrop.GetCursorPosition(e);
		KQBDragDrop.SelectedElement.style.left = (p.x - KQBDragDrop.OffsetX) + "px";
		KQBDragDrop.SelectedElement.style.top = (p.y - KQBDragDrop.OffsetY) + "px";	

		// if we're dragging a node that has a tooltip displayed, move the tooltip too
		if (KQBDragDrop.Tooltip2Shown)
		{
			var offs = KQBUtilities.GetOffsets (KQBDragDrop.SelectedElement);
			if (offs.Left+n.Width > KQB.Workspace.offsetWidth  )
				KQBDragDrop.Tooltip2.style.left = offs.Left - 200 + "px";
			else
				KQBDragDrop.Tooltip2.style.left = offs.Left + KQBDragDrop.SelectedElement.offsetWidth + 5 + "px";
			KQBDragDrop.Tooltip2.style.top = offs.Top + "px";
		}
		if (KQBDragDrop.TooltipShown)
		{
			var offs = KQBUtilities.GetOffsets(KQBDragDrop.SelectedElement);
			if (offs.Left+n.Width > KQB.Workspace.offsetWidth  )
				KQBDragDrop.Tooltip.style.left = offs.Left - 200 + "px";
			else
				KQBDragDrop.Tooltip.style.left = offs.Left + KQBDragDrop.SelectedElement.offsetWidth + 5 + "px";
			
			KQBDragDrop.Tooltip.style.left = offs.Left + KQBDragDrop.SelectedElement.offsetWidth + 5 + "px";
			KQBDragDrop.Tooltip.style.top = offs.Top + "px";
		}

		// since it's not possible to get cursor pos in all browsers without a window event,
		// but since we need the cursor pos on other places in the drawing code,
		// we need to save cursor pos here
		KQBDragDrop.LastCursorPos = p;
		
		// check if we're dragging outside the workspace
		if (!KQBUtilities.PointInElement(p, KQB.Workspace)) 
		{
			if ( !KQBDragDrop.OutsideWorkspace )
			{
				KQBDragDrop.OutsideWorkspace = true;
				DisplayTooltip2("", "KQBTooltipWarning", KQBStrings.ToolTips.DeleteNode);
							
			}
		}
		else
		{
			if (KQBDragDrop.OutsideWorkspace) 
			{
				KQBDragDrop.Tooltip2Shown = false;
				KQBDragDrop.Tooltip2.style.display = "none";
				KQBDragDrop.TooltipTempHidden = false;
				KQBDragDrop.OutsideWorkspace = false;
			}
		}
		
		
		// check if we're hovering over some node, if yes then highlight it
		var x = KQBDragDrop.GetElemBelowCursor(p);
		if (x) 
		{
			if ((x != KQBDragDrop.HighlightedElement) || (KQBDragDrop.NeedContinuousUpdate)) 
			{
				clear();
				
				KQBDragDrop.HighlightedElement = x;
				var typeparent = KQB.GetNodeByID(x.getAttribute("id"));
				var typechild = KQB.GetNodeByID(KQBDragDrop.SelectedElement.getAttribute("id"));
				if (KQBUtilities.IsHTMLClass(x, "KQBNode")) 
				{
					if (typechild.Parent == typeparent) 
					{
						DisplayTooltip2("KQBHighlightGreen", "KQBTooltipNormal", KQBStrings.ToolTips.AllowedAsChild + "\"" + typeparent.Name + "\"");
					}
					else 
					{
						if (KQB.CanHaveChild(typeparent, typechild) == true) 
						{
							if (!typechild.CanDropOnNodeBodyPosition(typeparent, p)) 
							{
								DisplayTooltip2("KQBHighlightOrange", "KQBTooltipWarning", KQBStrings.ToolTips.AllowedAsChildInOtherPos);
								KQBDragDrop.NeedContinuousUpdate = true;
							}
							else 
								DisplayTooltip2("KQBHighlightGreen", "KQBTooltipNormal", KQBStrings.ToolTips.AllowedAsChild + "\"" + typeparent.Name + "\"");
						}
						else 
							DisplayTooltip2("KQBHighlightRed", "KQBTooltipError", KQBStrings.ToolTips.NotAllowedAsChild);
					}
				}
				else // label
 				{
					if (KQB.CanSwitch(typeparent, typechild) == true) 
					//	KQBUtilities.AddHTMLClass(x, "KQBHighlightGreen");
						DisplayTooltip2("KQBHighlightGreen", "KQBTooltipNormal", KQBStrings.ToolTips.AllowedToSwitch );
					else 
					{
					//	KQBUtilities.AddHTMLClass(x, "KQBHighlightRed");
						DisplayTooltip2("KQBHighlightRed", "KQBTooltipError", KQBStrings.ToolTips.NotAllowedToSwitch );
					}
				}
			}
		}
		else // remove highlighting when we're not over any element anymore
 		{
			clear();
			KQBDragDrop.NeedContinuousUpdate = false;
		}	
	}
	
	// display the second tooltip, hide the first if shown
	// (first tooltip will always be info, while 2nd tooltip will display errors and thus
	// has higher precedence)
	function DisplayTooltip2 (htmlclass, tooltipclass, text)
	{
		if (x)
			KQBUtilities.AddHTMLClass(x, htmlclass);
		KQBDragDrop.Tooltip2Shown = true;
		KQBDragDrop.Tooltip2.className = tooltipclass;
		KQBDragDrop.Tooltip2.firstChild.data = text;
		KQBDragDrop.Tooltip2.style.display = "block";
		if (KQBDragDrop.TooltipShown) 
		{
			KQBDragDrop.TooltipTempHidden = true;
			KQBDragDrop.Tooltip.style.display = "none";
		}
	}
	
	// clear highlights and tooltips
	function clear ()
	{
		if (KQBDragDrop.HighlightedElement) 
		{			
			KQBUtilities.RemoveHTMLClass(KQBDragDrop.HighlightedElement, "KQBHighlightRed");
			KQBUtilities.RemoveHTMLClass(KQBDragDrop.HighlightedElement, "KQBHighlightGreen");
			KQBUtilities.RemoveHTMLClass(KQBDragDrop.HighlightedElement, "KQBHighlightOrange");
			KQBDragDrop.HighlightedElement = null;
	
			KQBDragDrop.TooltipShown = false;
			KQBDragDrop.Tooltip.style.display = "none";
			KQBDragDrop.Tooltip2Shown = false;
			KQBDragDrop.Tooltip2.style.display = "none";
			KQBDragDrop.TooltipTempHidden = false;
		}
	}
}
