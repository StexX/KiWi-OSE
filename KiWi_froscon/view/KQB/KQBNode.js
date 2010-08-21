

/**
 *
 *	KWQL Query Builder 
 * 
 * 	diploma thesis work by Andreas Hartl, 2009
 * 	
 */ 


/**
 * this file contains the KQBNode class
 */



if (typeof(KQB)=="undefined") KQB = {};


/**
 * InitNodeInfo
 * 
 * this sets up a data structure NodeInfo, that holds important values from the css
 * like node sizes, margins and borders
 */
KQB.InitNodeInfo = function ()
{
	KQB.NodeInfo = {};
				
  	var test = KQB.CreateNode(KQB.NodeType.Test);
	var testnode = test.CreateHTMLNode();
	KQB.Workspace.appendChild(testnode);		// need to append that node to an existing one, else IE will return null for the margins :(
	KQB.NodeInfo.Width = KQBUtilities.GetStyleAsNumber(testnode, "width" );
	KQB.NodeInfo.Height =  KQBUtilities.GetStyleAsNumber(testnode, "height" );
	KQB.NodeInfo.Margin_Left = KQBUtilities.GetStyleAsNumber(testnode, "margin-left" );
	KQB.NodeInfo.Margin_Top = KQBUtilities.GetStyleAsNumber(testnode, "margin-top" );
	KQB.NodeInfo.Margin_Right = KQBUtilities.GetStyleAsNumber(testnode, "margin-right" );
	KQB.NodeInfo.Margin_Bottom = KQBUtilities.GetStyleAsNumber(testnode, "margin-bottom" );
	KQB.NodeInfo.Label_Height = KQBUtilities.GetStyleAsNumber(testnode.firstChild, "height" );
	KQB.NodeInfo.Border_Left_Width = KQBUtilities.GetStyleAsNumber(testnode, "border-left-width" );
	KQB.NodeInfo.Border_Top_Width = KQBUtilities.GetStyleAsNumber(testnode, "border-top-width" );
	KQB.NodeInfo.Border_Right_Width = KQBUtilities.GetStyleAsNumber(testnode, "border-right-width" );
	KQB.NodeInfo.Border_Bottom_Width = KQBUtilities.GetStyleAsNumber(testnode, "border-bottom-width" );
	KQB.NodeInfo.Label_Border_Top_Width = KQBUtilities.GetStyleAsNumber(testnode.firstChild, "border-top-width" );
	KQB.NodeInfo.Label_Border_Bottom_Width = KQBUtilities.GetStyleAsNumber(testnode.firstChild, "border-bottom-width" );
	
	test.ClickLabel();	// and now for the values when the node's content is hidden
	
	KQB.NodeInfo.Border_Left_Width_Collapsed = KQBUtilities.GetStyleAsNumber(testnode, "border-left-width" );
	KQB.NodeInfo.Border_Top_Width_Collapsed = KQBUtilities.GetStyleAsNumber(testnode, "border-top-width" );
	KQB.NodeInfo.Border_Right_Width_Collapsed = KQBUtilities.GetStyleAsNumber(testnode, "border-right-width" );
	KQB.NodeInfo.Border_Bottom_Width_Collapsed = KQBUtilities.GetStyleAsNumber(testnode, "border-bottom-width" );
	KQB.NodeInfo.Label_Border_Top_Width_Collapsed = KQBUtilities.GetStyleAsNumber(testnode.firstChild, "border-top-width" );
	KQB.NodeInfo.Label_Border_Bottom_Width_Collapsed = KQBUtilities.GetStyleAsNumber(testnode.firstChild, "border-bottom-width" );

	KQB.Workspace.removeChild(testnode);
}

/**
 * Node
 * 
 * the main component of the KQB 
 * every query is a tree of Nodes
 */
KQB.Node = function () 
{
	this.Name = new String;
	this.Type = new Number;
	this.Children = new Array();
	this.Parent = null;
	this.isLeaf = new Boolean;		// leaf in the query tree, has no children
	this.isUnary = new Boolean;             // non-unary nodes would be ones like and/or that can have >1 children
	this.ID = new Number;	
	this.HTMLNode = null;
	this.ContentHidden = false;
	this.Width = 0;
	this.Height = 0;
	this.isResizeable = false;
	this.FreeSpaceIndex = 0;		// used in nodes with like AND that can have multiple children
	this.ShowFreeSpace = false;
	this.FixedWidth2 = false;
	this.FixedWidth3 = false;
	this.Category = 0;
	this.isFixed = false;
	this.TextValue = "";
	
	this.Border = {Left: 0, Right: 0, Top: 0, Bottom: 0, Label_Top: 0, Label_Bottom: 0};
}

/**
 * clone
 *
 * creates a copy of this node (but with a null html node)
 */
KQB.Node.prototype.clone = function ()
{
	var n = new KQB.Node();
	for (var prop in this)
		n[prop] = this[prop];
	n.Children = new Array();
	for (var i = 0; i < this.Children.length; i++)
	{
		n.Children[i] = this.Children[i].clone();
		n.Children[i].Parent = this;
	}
	n.HTMLNode = null;
	return n;
}

/**
 * GetNumChildren
 * 
 * returns the number of children this node currently has
 */
KQB.Node.prototype.GetNumChildren = function ()
{
	if ( !this.Children )
		return 0;
	return this.Children.length;
}

/**
 * Node.CreateHTMLNode
 * 
 * creates a div element representing this node
 */
KQB.Node.prototype.CreateHTMLNode = function ()
{ 
	var node = document.createElement("div");
	node.className = "KQBNode KQBNodeContentShown";
	KQBUtilities.AddHTMLClass(node, KQB.NodeDescriptions[this.Type].AdditionalCSSClass);
	var freepos = KQBDragDrop.FindFreePosition ();
	if (!this.Parent || (this.Parent == KQB.Query)) 
	{
		node.style.top = freepos.y + "px";
		node.style.left = freepos.x + "px";
	}
	node.setAttribute("id", this.ID);
	var label = document.createElement("div");
	label.className = "KQBLabel KQBLabelContentShown";

	label.setAttribute("id", this.ID);

	var table = document.createElement("table"); 
	table.setAttribute ("id", this.ID);
	// disable text selection in the label. without this, we have unwanted text selections when 
	// one node is dragged over the label of another
	if (typeof table.onselectstart!="undefined") // IE
		table.onselectstart=function(){return false}
	else if (typeof table.style.MozUserSelect!="undefined") // FF
		table.style.MozUserSelect="none";
		
		table.width = "100%";
		table.style.borderCollapse="collapse";
		
		var tb = document.createElement("tbody");
		var tr = document.createElement("tr");
		var td1 = document.createElement("td");
		td1.className = "KQBLabelText";
		if (this.LabelTextSmall)
			KQBUtilities.AddHTMLClass (td1, "KQBLabelTextSmall" );
		td1.setAttribute ("id", this.ID );
		var labeltext = document.createTextNode(this.Name);
		td1.appendChild(labeltext);
	
		tr.appendChild(td1);
		// create resizebox
		if (this.isResizeable) 
		{
			var td2 = document.createElement("td");
	
			td2.className = "KQBLabelResizeBox";
			td2.setAttribute("id", this.ID);
			td2.setAttribute("title", KQBStrings.Misc.NodeResizeTooltip);
			tr.appendChild(td2);
			this.ResizeBox = td2;
		}
		tb.appendChild(tr);
		table.appendChild(tb);

		label.appendChild(table);

	node.appendChild(label);
	
	// add separator if the node is a rule
	if ( this.Type == KQB.NodeType.Rule )
	{
		var sep = document.createElement("div");
		sep.className = "KQBSeparator";
		sep.setAttribute("id", this.ID);
		node.appendChild(sep);
		this.Separator = sep;
	}
	
	// if we have a leaf node, add a textbox
	if (this.isLeaf) 
	{
		var textbox = document.createElement("input");
		textbox.type = "text";
		if (this.TextValue=="")
			this.TextValue = this.Name;
		textbox.value = this.TextValue;
		textbox.className = "KQBTextBox";
		textbox.onmouseover = KQBDragDrop.OnMouseOver;
		textbox.title=this.TextValue;
		textbox.onkeyup = function () { this.title=this.value; KQB.UpdateOutput(); };
		textbox.setAttribute("id", this.ID);
		var span = document.createElement("span"); // need to wrap input element in a span (or other inline element)
		// else the lovely internet explorer will inherit the margin-left
		// from the node div and display this wrong.....
		span.appendChild(textbox);
		node.appendChild(span);
		this.TextBox = span;
	}
	
	this.HTMLNode = node;
	this.HTMLNode.title = "";

	this.Width = KQB.NodeInfo.Width;
	this.Height = KQB.NodeInfo.Height;
	
	if (!this.isLeaf) 
	{	// if this is not a leaf node, we make it a bit taller
		this.Height = this.Height + KQB.NodeInfo.Label_Height;
		this.HTMLNode.style.height = this.Height + "px";
	}
		
	if ( this.FixedWidth2)
	{
		this.Width = ( KQB.NodeInfo.Width * 2) + ( KQB.NodeInfo.Margin_Left * 2) + KQB.NodeInfo.Margin_Right;
		this.HTMLNode.style.width = this.Width + "px";
	}
	
	if ( this.FixedWidth3)
	{
		this.Width = ( KQB.NodeInfo.Width * 3) + ( KQB.NodeInfo.Margin_Left * 3) + KQB.NodeInfo.Margin_Right;
		this.HTMLNode.style.width = this.Width + "px";
	}
	
	this.Border.Left = KQB.NodeInfo.Border_Left_Width;
	this.Border.Right = KQB.NodeInfo.Border_Right_Width;
	this.Border.Top = KQB.NodeInfo.Border_Top_Width;
	this.Border.Bottom = KQB.NodeInfo.Border_Bottom_Width;
	this.Border.Label_Top = KQB.NodeInfo.Label_Border_Top_Width;
	this.Border.Label_Bottom = KQB.NodeInfo.Label_Border_Bottom_Width;
	var typenum = this.Type;
	this.HTMLNode.onmouseover = KQBDragDrop.OnMouseOver;

	
	return node;
}

/**
 * GetTextValue
 *
 * returns the value in the textbox if this node has one
 */
KQB.Node.prototype.GetTextValue = function ()
{
	if (!this.isLeaf)
		return null;
	this.TextValue = this.TextBox.firstChild.value;
	return this.TextBox.firstChild.value;
}

/**
 * SetTextValue
 *
 * sets the value in the textbox if this node has one
 */
KQB.Node.prototype.SetTextValue = function (text)
{
	if (!this.isLeaf)
		return;
	this.TextValue = text;
	this.TextBox.firstChild.value = text;
	this.TextBox.firstChild.title = text;
}

/**
 * ClickLabel
 * 
 * executed when a nodelabel is clicked;
 * hides or shows contents of that node
 */
KQB.Node.prototype.ClickLabel = function ()
{
	
	if ( this.ContentHidden )		// show node contents
	{	// restore size and borders
		this.Width = this.LastWidth; 
		this.Height = this.LastHeight;
		this.Border.Left = KQB.NodeInfo.Border_Left_Width;
		this.Border.Right = KQB.NodeInfo.Border_Right_Width;
		this.Border.Top = KQB.NodeInfo.Border_Top_Width;
		this.Border.Bottom = KQB.NodeInfo.Border_Bottom_Width;
		this.Border.Label_Top = KQB.NodeInfo.Label_Border_Top_Width;
		this.Border.Label_Bottom = KQB.NodeInfo.Label_Border_Bottom_Width;
		this.HTMLNode.style.width = this.Width + "px";		// restore previous size
		this.HTMLNode.style.height = this.Height + "px";
		KQBUtilities.RemoveHTMLClass ( this.HTMLNode, "KQBNodeContentHidden" );
		KQBUtilities.RemoveHTMLClass ( this.HTMLNode.firstChild, "KQBLabelContentHidden" );
		KQBUtilities.AddHTMLClass ( this.HTMLNode, "KQBNodeContentShown" );
		KQBUtilities.AddHTMLClass ( this.HTMLNode.firstChild, "KQBLabelContentShown" );
		// display children again
		for ( var i = 0; i < this.Children.length; i++ )
			this.Children[i].HTMLNode.style.display = "block";
		
		if (this.Separator)
			this.Separator.style.display = "block";	
			
		if ( this.isLeaf )
			this.TextBox.style.display="inline";
		
		// show the resize box if we have one
		if ( this.ResizeBox )
			this.ResizeBox.style.display = "table-cell";

		
		this.ContentHidden = false;
	}
	else			// hide contents
	{
		this.LastHeight = this.Height; //this.HTMLNode.offsetHeight;		// save size and label's borders
		this.LastWidth = this.Width; //this.HTMLNode.offsetWidth;
	
		this.Border.Left = KQB.NodeInfo.Border_Left_Width_Collapsed;
		this.Border.Right = KQB.NodeInfo.Border_Right_Width_Collapsed;
		this.Border.Top = KQB.NodeInfo.Border_Top_Width_Collapsed;
		this.Border.Bottom = KQB.NodeInfo.Border_Bottom_Width_Collapsed;
		this.Border.Label_Top = KQB.NodeInfo.Label_Border_Top_Width_Collapsed;
		this.Border.Label_Bottom = KQB.NodeInfo.Label_Border_Bottom_Width_Collapsed;
		
		// hide children
		for ( var i = 0; i < this.Children.length; i++ )
			this.Children[i].HTMLNode.style.display = "none";
		
		if (this.Separator)
			this.Separator.style.display = "none";	
			
		if ( this.isLeaf )
		{
			this.TextBox.style.display="none";
		}
		// hide the resize box if we have one
		if ( this.ResizeBox )
			this.ResizeBox.style.display = "none";
	
		// make the whole node the size of the label and hide the label's borders so we don't have two borders
		this.Width = KQB.NodeInfo.Width;
		this.Height = KQB.NodeInfo.Label_Height;
		this.HTMLNode.style.width = this.Width + "px";
		this.HTMLNode.style.height = this.Height + "px";
	
		KQBUtilities.RemoveHTMLClass ( this.HTMLNode, "KQBNodeContentShown" );
		KQBUtilities.RemoveHTMLClass ( this.HTMLNode.firstChild, "KQBLabelContentShown" );
		KQBUtilities.AddHTMLClass ( this.HTMLNode, "KQBNodeContentHidden" );
		KQBUtilities.AddHTMLClass ( this.HTMLNode.firstChild, "KQBLabelContentHidden" );
	
		this.ContentHidden = true;
	}
	if ( this.Parent )
		this.Parent.Refresh();		// make sure parents get resized too
	KQB.UpdateUndoBuffer();
}

/**
 * ClickResizeBox
 *
 * this function is called when the resize box of a node is clicked
 * it switches the showing of an empty free position
 */
KQB.Node.prototype.ClickResizeBox = function ()
{
	this.ShowFreeSpace = !this.ShowFreeSpace;
	this.Refresh();
	KQB.UpdateUndoBuffer();
}

/**
 * RemoveChild
 * 
 * remove a child node
 * @param {Object} child
 */
KQB.Node.prototype.RemoveChild = function (child)
{ 
	var firstchild = child==this.Children[0];
	// remove child node and it's html node
	for (var i = 0; i < this.Children.length; i++) 
		if (this.Children[i] == child) 
		{
			this.Children.splice(i,1);
			child.HTMLNode.parentNode.removeChild(child.HTMLNode);
		}
	if ( this == KQB.Query )	// if we're not in the workspace node..
		return;
	// .. then resize this node
	
	if (this.isUnary) 
		this.HTMLNode.style.width = KQB.NodeInfo.Width + "px";
	else 
	{
		this.Width = ( KQB.NodeInfo.Width * 2) + ( KQB.NodeInfo.Margin_Left * 2) + KQB.NodeInfo.Margin_Right;
		this.HTMLNode.style.width = this.Width + "px";
		if (this.FixedWidth2)
		{	// need a special case if we're removing the first child of a fixed with node, else the second child
			// will be placed at position 1
			if ( (this.FreeSpaceIndex == 2) && firstchild )
				this.FreeSpaceIndex = 0;
			else this.FreeSpaceIndex = this.GetNumChildren();
		}
		else this.FreeSpaceIndex = this.GetNumChildren();	
	}
	this.HTMLNode.style.height = KQB.NodeInfo.Height + "px";

	this.Refresh();	// and it's parent nodes
}


/**
 * AddChild
 * 
 * appends a child node
 * @param {Object} child
 */
KQB.Node.prototype.AddChild = function (child)
{
	if (!child)
		return false;
	if ( this == child.Parent )
		return false;
	if (this.Type == KQB.NodeType.Root)
		return false;
	if ( ( this.isUnary == true) && (this.GetNumChildren() > 0) )
			return false;
	if ( this.FixedWidth2 == true)
		if ( this.GetNumChildren() > 1 )
			return false;
	if (child.Parent)
		child.Parent.RemoveChild(child);
	
	child.Parent = this;
	
	this.Children.push(child);
			
	if (!this.isUnary ) 
	{
		this.RearrangeChildren(child);
		this.FreeSpaceIndex = this.GetNumChildren();
	}	
	var htmlchild = child.HTMLNode;
	if (!htmlchild ) htmlchild = child.CreateHTMLNode();
	if (!this.HTMLNode)
		this.CreateHTMLNode();


	this.HTMLNode.appendChild(htmlchild);
	
	if ( (this.Type == KQB.NodeType.AND) || (this.Type == KQB.NodeType.OR) )
	{
		// show the free space when first child is added to and and node so the user
		// sees that he is supposed to add another child
	//	if ( this.GetNumChildren() == 1 )
	//		this.ShowFreeSpace = true;
	}
	
	child.Refresh();

	return true;
}


/**
 * Refresh
 * 
 * updates the node display
 */
KQB.Node.prototype.Refresh = function ()
{
	if (!this.HTMLNode) return;
	if ( this == KQB.Query ) 
		return;
	
	// don't hide empty space in nodes with fixed width		
	if ( this.FixedWidth2 )
	{
		var num = this.GetNumChildren();
		if ( num == 2 )
			this.ShowFreeSpace = false;
		if ( (num==0) || (num==1) )
			this.ShowFreeSpace = true;
	}	
	
	if ( this.FixedWidth3 )
	{
		var num = this.GetNumChildren();
		if ( num == 3 )
			this.ShowFreeSpace = false;
		if ( (num==0) || (num==1) || (num==2) )
			this.ShowFreeSpace = true;
	}
	
	if (this.GetNumChildren() == 0) 
	{
		this.Width = KQB.NodeInfo.Width;

		if (!this.isLeaf) 
		{	// if this is not a leaf node, we make it a bit taller
			this.Height = KQB.NodeInfo.Height + KQB.NodeInfo.Label_Height;
			this.HTMLNode.style.height = this.Height + "px";
		}
	}
	else 
	{
		if (!this.ContentHidden) 
		{
			this.Width = GetChildrenWidthSums(this);
			this.Height = GetHighestChildHeight(this) + KQB.NodeInfo.Label_Height + KQB.NodeInfo.Margin_Top +
			KQB.NodeInfo.Margin_Bottom +
			this.Border.Label_Top +
			this.Border.Label_Bottom;
		}	
	}
	if (!this.isUnary && this.ShowFreeSpace )
		this.Width += KQB.NodeInfo.Width + KQB.NodeInfo.Margin_Left + this.Border.Left + this.Border.Right;
	

	this.HTMLNode.style.width = this.Width + "px";
	this.HTMLNode.style.height = this.Height + "px";
	
	// update siblings
	if ( this.GetNumChildren() > 0 )
	{
		var left = 0;
		if ( !this.isUnary && (this.FreeSpaceIndex == 0) && this.ShowFreeSpace )
			left = KQB.NodeInfo.Width + KQB.NodeInfo.Margin_Left + this.Children[0].Border.Left + this.Children[0].Border.Right;
		var top = KQB.NodeInfo.Label_Height + this.Border.Label_Bottom + this.Border.Label_Top;
		if (!this.Children[0].HTMLNode)
			this.Children[0].CreateHTMLNode();
		this.Children[0].HTMLNode.style.left = left + "px";
		this.Children[0].HTMLNode.style.top = top + "px";
		for ( var i = 1; i < this.GetNumChildren(); i++ )
		{
			if ( this.ShowFreeSpace )
				if ( this.FreeSpaceIndex == i )
					left += KQB.NodeInfo.Width + KQB.NodeInfo.Margin_Left + KQB.NodeInfo.Border_Left_Width + KQB.NodeInfo.Border_Right_Width;
			left += this.Children[i-1].Width + KQB.NodeInfo.Margin_Left + this.Children[i-1].Border.Left + this.Children[i-1].Border.Right; 
			if (!this.Children[i].HTMLNode)
				this.Children[i].CreateHTMLNode();
			this.Children[i].HTMLNode.style.left = left + "px";
			this.Children[i].HTMLNode.style.top = top + "px";
		}
	}
	
	if ( this.Separator )
	{
		var height = this.Height - KQB.NodeInfo.Label_Height - KQB.NodeInfo.Label_Border_Bottom_Width;
		this.Separator.style.height = height + "px";
		var left = this.Width/2 - 1;
		if (this.GetNumChildren() == 2)
			left = this.Children[0].Width + KQB.NodeInfo.Margin_Left + this.Children[0].Border.Left + this.Children[0].Border.Right;
		if (this.GetNumChildren() == 1)
			if (this.FreeSpaceIndex == 1)
				left = 	this.Children[0].Width + KQB.NodeInfo.Margin_Left + this.Children[0].Border.Left + this.Children[0].Border.Right;
			else
				left =  KQB.NodeInfo.Width + KQB.NodeInfo.Margin_Left + KQB.NodeInfo.Border_Left_Width + KQB.NodeInfo.Border_Right_Width;
		this.Separator.style.left = left + "px"; 
	}	
	
	if (this.Parent)
		this.Parent.Refresh();	
	
	
	// compute the sum of the children widths and margins
	function GetChildrenWidthSums (node)
	{
		var num = node.GetNumChildren(); 
		var res = KQB.NodeInfo.Margin_Left;
		for ( var i = 0; i < num; i++ )
		{
			res += node.Children[i].Width + KQB.NodeInfo.Margin_Left + node.Children[i].Border.Left + node.Children[i].Border.Right;
		}
		return res;
	}
	// get the highest height value of all children of a node
	function GetHighestChildHeight (node)
	{
		var max = 0;
		var num = node.GetNumChildren();
		for ( var i = 0; i < num; i++ )
		{
			var x = node.Children[i].Height + node.Children[i].Border.Bottom + node.Children[i].Border.Top;
			if ( x > max )
				max = x;
		}
		return max;
	}
}

/**
 * DropOnWorkspace
 * 
 * handler to call when this node gets dropped on the workspace
 */
KQB.Node.prototype.DropOnWorkspace = function (position)
{

		if ( this.Parent == null )		// we're adding a new node to the workspace
		{
			this.Parent = KQB.Query;
			KQB.Query.Children.push(this);
			
			CheckChildren(this);
			
			KQB.Workspace.appendChild(this.HTMLNode);	
			CheckChildren(this);

			return true;
		}
		// we're trying to add a child to the main workspace area, and child has a parent element
		if ( this.Parent == KQB.Query )
			return true;		// don't do anything if this is already the child's parent
		// else add it as a new child
		
		this.Parent.RemoveChild(this);
		this.Parent = KQB.Query;
		KQB.Query.Children.push(this);

		KQB.Workspace.appendChild(this.HTMLNode);

		if (!position)
			position = KQBDragDrop.FindFreePosition();
		this.HTMLNode.style.left = position.x + "px";
		this.HTMLNode.style.top = position.y + "px";
		
		this.Refresh();
		
		
		return true;
		
		// make sure all children have a html node. if not, create one
		function CheckChildren (node)
		{
			if ( node.Children )
			{
				for ( var i = 0; i < node.Children.length; i++ )
				{
					if (!node.Children[i].HTMLNode) 
					{
						node.Children[i].CreateHTMLNode();
					}	node.HTMLNode.appendChild(node.Children[i].HTMLNode);
						node.Children[i].Refresh();
					
					CheckChildren(node.Children[i]);						
					
				}
			}
		}
}

/**
 * PointInFreeSpace
 * 
 * checks if the given point lies within the 'free space' of a non-unary node
 * @param {Object} point
 */
KQB.Node.prototype.PointInFreeSpace = function (point)
{
	if (!this.ShowFreeSpace)
		return false;
	var left = 0;
	for ( var i = 0; i < this.FreeSpaceIndex; i++ )
	{
		left += this.Children[i].Width + KQB.NodeInfo.Margin_Left + KQB.NodeInfo.Border_Left_Width + KQB.NodeInfo.Border_Right_Width;
	}
	return ( (point.x >= left) && ( point.x <= (left+KQB.NodeInfo.Width+KQB.NodeInfo.Border_Left_Width+KQB.NodeInfo.Border_Right_Width)));
}

/**
 * RearrangeChildren
 * 
 * moves the given child to the currently free position indicated by Node.FreeSpaceIndex
 * @param {Object} child
 */
KQB.Node.prototype.RearrangeChildren = function (child)
{ 	
	var index = 0;
	// find child's index
	for ( var i = 0; i < this.Children.length; i++ )
		if ( this.Children[i] == child )
		{
			index = i;
			break;
		}
	var s = this.FreeSpaceIndex + " " + index;

	if ( this.FreeSpaceIndex > index )
		this.Children.MoveElement(index, this.FreeSpaceIndex-1);
	else
		this.Children.MoveElement(index, this.FreeSpaceIndex);
	if ( this.FreeSpaceIndex <= index )
		this.FreeSpaceIndex = index + 1;
	else
		this.FreeSpaceIndex = index; 
	this.Refresh();
	return true;
}



/**
 * DropOnNodeBody
 * 
 * this is the handler that should get called when this node is dropped
 * on the body of another node. 
 * it will check if this node can be a child of the given node, if yes, it is appended
 * 
 * @param {Object} node
 * @return true if node could be dropped, false if not
 */
KQB.Node.prototype.DropOnNodeBody = function (node)
{
	if ( node.isLeaf )
		return false; 
	if ( !KQB.CanHaveChild(node, this) )
		return false;
	if ( node.FixedWidth2 == true)
		if ( node.GetNumChildren() > 1 )
			return false;
	
	if ( (node.isUnary == true) || ( node.GetNumChildren() == 0 ) )
		return node.AddChild(this);
	
	// handle drop of a child element on a free part in the parent element
	// which will place it there
	var offs = KQBUtilities.GetOffsets(node.HTMLNode);
	var point = {x: KQBDragDrop.LastCursorPos.x - offs.Left, y: KQBDragDrop.LastCursorPos.y - offs.Top };
	if ( node.PointInFreeSpace(point) )
	{
		if (this.Parent == node) 
			return node.RearrangeChildren(this);
		else 
			return node.AddChild(this);
		
	}
	return false;
}

/**
 * CanDropOnNodeBodyPosition
 *
 * checks if the given node can be dropped on the given position within the parent node
 * this function makes sure both that the child is a valid child of this parent, and
 * that the given coordinates mark a free position within the parent
 */
KQB.Node.prototype.CanDropOnNodeBodyPosition = function (node, pos)
{
	if ( node.isLeaf )
		return false; 
	if ( !KQB.CanHaveChild(node, this) )
		return false;
	if ( node.FixedWidth2 == true)
		if ( node.GetNumChildren() > 1 )
			return false;
	if ( node.FixedWidth3 == true)
		if ( node.GetNumChildren() > 2 )
			return false;
		
	if ( (node.isUnary == true) || ( node.GetNumChildren() == 0 ) )
		return true;
	
	// handle drop of a child element on a free part in the parent element
	// which will place it there
	var offs = KQBUtilities.GetOffsets(node.HTMLNode);
	var point = {x: pos.x - offs.Left, y: pos.y - offs.Top };
	if ( node.PointInFreeSpace(point) )
		return true;
	return false;
}

/**
 * DropOnNodeLabel
 * 
 * this is the handler that should get called when this node is dropped
 * on the label of another node
 * the default operation for this event is to switch the types of the two nodes involved
 * @param {Object} node
 */
KQB.Node.prototype.DropOnNodeLabel = function (node)
{
	if ( this == node )
		return;
	//  check if this operation is valid
	if ( !KQB.CanSwitch(node, this) )
		return;
		
	// if we're dragging a keyword(or value) onto another, change their text values
	if ( ( (node.Type == KQB.NodeType.Keyword) && ( this.Type == KQB.NodeType.Keyword ) )
		||  ( (node.Type == KQB.NodeType.Variable) && ( this.Type == KQB.NodeType.Variable ) ) )
	{
		var text1 = this.GetTextValue();
		var text2 = node.GetTextValue();
		this.SetTextValue(text2);
		node.SetTextValue(text1);
		return;
	}
	// switch node types and names
	var name = this.Name;
	var type = this.Type;
	var sing = this.isSingular;
	this.Type = node.Type;
	node.Type = type;
	this.ChangeName(node.Name);
	node.ChangeName(name);
	this.isSingular = node.isSingular;
	node.isSingular = sing;

}

/**
 * ChangeName
 * changes the name of the current node and updates it in the html
 * @param {Object} newname
 */
KQB.Node.prototype.ChangeName = function (newname)
{
	this.Name = newname;
	if (this.HTMLNode)
		this.HTMLNode.firstChild.firstChild.firstChild.firstChild.firstChild.firstChild.data = newname;  
}

/**
 * MakeFixed
 *
 * sets the isFixed property of the node to the given value
 */
KQB.Node.prototype.MakeFixed = function (fixed)
{
	this.isFixed = fixed;
}


