

/**
 *
 *	KWQL Query Builder 
 * 
 * 	diploma thesis work by Andreas Hartl, 2009
 * 	
 */ 


// namespace that contains all code, so no accidental overlapping with
// KiWi js can happen
if (typeof(KQB)=="undefined") KQB = {};

// global vars

KQB.LastNodeID = -1;	// used to give each newly created node a unique id

// node categories
KQB.NodeCategories = {};
KQB.NodeCategories.Resource = 1;
KQB.NodeCategories.Qualifier = 2;
KQB.NodeCategories.Operator = 3;
KQB.NodeCategories.Other = 4;

// node types
KQB.NodeType = {};

KQB.NodeType.Root = 0;
KQB.NodeType.Test = 1;
KQB.NodeType.Keyword = 2;
KQB.NodeType.Variable = 3;
KQB.NodeType.Compound = 4;
KQB.NodeType.Rule = 5;
KQB.NodeType.AND = 6;
KQB.NodeType.OR = 7;
KQB.NodeType.Author = 8;
KQB.NodeType.Created = 9;
KQB.NodeType.LastEdited = 10;
KQB.NodeType.Tag = 11;
KQB.NodeType.Title = 12;
KQB.NodeType.Text = 13;
KQB.NodeType.NumberEdits = 14;
KQB.NodeType.Link = 15;
KQB.NodeType.Fragment = 16;
KQB.NodeType.Descendant = 17;
KQB.NodeType.Child = 18;
KQB.NodeType.Target = 19;
KQB.NodeType.Origin = 20;
KQB.NodeType.AnchorText = 21;
KQB.NodeType.Name = 22;
KQB.NodeType.NOT = 23;
KQB.NodeType.OPTIONAL = 24;
KQB.NodeType.ContentItem = 25;
KQB.NodeType.URI = 26;
KQB.NodeType.ALL = 27;
KQB.NodeType.SOME = 28;
KQB.NodeType.AVG = 29;
KQB.NodeType.COUNT = 30;
KQB.NodeType.Disagree = 31;
KQB.NodeType.SPARQL = 32;
KQB.NodeType.Binding = 33;
KQB.NodeType.TagGrouping = 34;
KQB.NodeType.TagCharacterization = 35;
KQB.NodeType.Agree = 36;

/*
 * NodeDescription:
 * a struct that holds all information about a specific node
 */
KQB.NodeDescription = function ( type, name, tooltip )
{
	this.Type = type;				// node's type
	this.Name = name;				// node's name
	this.Tooltip = tooltip;				// node's tooltip
	this.isUnary = false;				// true if the node is unary, meaning it can have only one child
	this.isLeaf = false;				// true if the node is a leaf, meaning it can have no children
	this.isResizeable = false;			// true if the node is resizeable
	this.hasText = false;				// true if the node contains a 'keyword' child on creation
	this.hasVariable = false;			// true if the node contains a 'variable' child on creation
	this.hasVarAndText = false;			// true if the node contains both 'keyword' and 'variable' children
	this.SwitchAllowed = null;			// Array that holds all the nodetypes with which a type switch is allowed
	this.AllowedChildren = null;			// Array that holds all allowed children types
	this.InheritAllowedChildren = false;		// true if the node should be allowed to contain any children types it's parent is allowed to have (this is for operators)
	this.FixedWidth2 = false;			// true if this node's width is always wide enough to hold exactly two children
	this.FixedWidth3 = false;			// true if this node's width is always wide enough to hold exactly three children
	this.Category = KQB.NodeCategories.Other;	// node's category
	this.DisallowOperators = false;			// true if this node is not allowed to contain or be contained in an operator node
	this.AdditionalCSSClass = null;			// additional css classes for this node 
	this.LabelTextSmall = false;			// true if the node has a long label text that needs to be smaller to ensure the text isn't wider than the node
	this.isSingular = true;				// true if the qualifier must be unique within a resource
}


/**
 * InitNodeDescriptions
 * 
 * this function fills the Array KQB.NodeDescriptions with the needed values for all node types
 */
KQB.InitNodeDescriptions = function()
{
	KQB.NodeDescriptions = new Array();

	// first set types, names and tooltips
	for ( var type in KQB.NodeType )
	{
		if (type == KQB.NodeType.Root )
			continue;
			
		var name = "";
		var tip = "";
		
		// read localized names and tooltips from KQBStrings
		for ( var names in KQBStrings.NodeNames )
			if ( names == type )
				name = names;
		
		for ( var tips in KQBStrings.ToolTips )
			if ( tips == type )
				tip = tips;
	
		KQB.NodeDescriptions[KQB.NodeType[type]] = new KQB.NodeDescription(KQB.NodeType[type], KQBStrings.NodeNames[name], KQBStrings.ToolTips[tip]);
	}

	// node-specific values from here on
	// don't forget to convert type numbers to strings when initializing SwitchAllowed or AllowedChildren
	// with a single element. auto-conversion happens only when there are more then one specified, else
	// the number is interpreted as the length of the new array
	KQB.NodeDescriptions[KQB.NodeType.Test].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.Test].isLeaf = true;
	
	KQB.NodeDescriptions[KQB.NodeType.Keyword].Category = KQB.NodeCategories.Other;
	KQB.NodeDescriptions[KQB.NodeType.Keyword].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.Keyword].isLeaf = true;
	KQB.NodeDescriptions[KQB.NodeType.Keyword].SwitchAllowed = new Array(KQB.NodeType.Variable, KQB.NodeType.Keyword);
	KQB.NodeDescriptions[KQB.NodeType.Keyword].AllowedChildren = new Array();
	
	KQB.NodeDescriptions[KQB.NodeType.Variable].Category = KQB.NodeCategories.Other;
	KQB.NodeDescriptions[KQB.NodeType.Variable].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.Variable].isLeaf = true;
	KQB.NodeDescriptions[KQB.NodeType.Variable].SwitchAllowed = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable);
	KQB.NodeDescriptions[KQB.NodeType.Variable].AllowedChildren = new Array();
	
	KQB.NodeDescriptions[KQB.NodeType.SPARQL].Category = KQB.NodeCategories.Other;
	KQB.NodeDescriptions[KQB.NodeType.SPARQL].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.SPARQL].isLeaf = true;
	KQB.NodeDescriptions[KQB.NodeType.SPARQL].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.SPARQL].AllowedChildren = new Array();
	
	KQB.NodeDescriptions[KQB.NodeType.Compound].Category = KQB.NodeCategories.Other;
	KQB.NodeDescriptions[KQB.NodeType.Compound].isResizeable = true;
	KQB.NodeDescriptions[KQB.NodeType.Compound].LabelTextSmall = true;
//	KQB.NodeDescriptions[KQB.NodeType.Compound].InheritAllowedChildren = true;
	KQB.NodeDescriptions[KQB.NodeType.Compound].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.Compound].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.ALL, KQB.NodeType.SOME, KQB.NodeType.AVG, KQB.NodeType.COUNT, KQB.NodeType.ContentItem, KQB.NodeType.SPARQL);

	KQB.NodeDescriptions[KQB.NodeType.Rule].Category = KQB.NodeCategories.Other;
	KQB.NodeDescriptions[KQB.NodeType.Rule].FixedWidth2 = true;
	KQB.NodeDescriptions[KQB.NodeType.Rule].DisallowOperators = true;
	KQB.NodeDescriptions[KQB.NodeType.Rule].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.Rule].AllowedChildren = new Array(KQB.NodeType.ALL, KQB.NodeType.SOME, KQB.NodeType.COUNT, KQB.NodeType.AVG,
									    KQB.NodeType.ContentItem, KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.URI, KQB.NodeType.Author, 
		KQB.NodeType.Created, KQB.NodeType.LastEdited, KQB.NodeType.Tag, KQB.NodeType.Title, KQB.NodeType.Text, KQB.NodeType.NumberEdits, KQB.NodeType.Link,
		KQB.NodeType.Fragment, KQB.NodeType.Agree, KQB.NodeType.Descendant, KQB.NodeType.Child, KQB.NodeType.Disagree, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.AND].Category = KQB.NodeCategories.Operator;
	KQB.NodeDescriptions[KQB.NodeType.AND].isResizeable = true;
	KQB.NodeDescriptions[KQB.NodeType.AND].InheritAllowedChildren = true;
	KQB.NodeDescriptions[KQB.NodeType.AND].SwitchAllowed = new Array(KQB.NodeType.OR + '');
	KQB.NodeDescriptions[KQB.NodeType.AND].AllowedChildren = new Array();
	
	KQB.NodeDescriptions[KQB.NodeType.OR].Category = KQB.NodeCategories.Operator;
	KQB.NodeDescriptions[KQB.NodeType.OR].isResizeable = true;
	KQB.NodeDescriptions[KQB.NodeType.OR].InheritAllowedChildren = true;
	KQB.NodeDescriptions[KQB.NodeType.OR].SwitchAllowed = new Array(KQB.NodeType.AND + '');
	KQB.NodeDescriptions[KQB.NodeType.OR].AllowedChildren = new Array();
	
	KQB.NodeDescriptions[KQB.NodeType.Author].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.Author].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.Author].isSingular = false;
	KQB.NodeDescriptions[KQB.NodeType.Author].hasText = true;
	KQB.NodeDescriptions[KQB.NodeType.Author].SwitchAllowed = new Array( KQB.NodeType.AnchorText, KQB.NodeType.Author, KQB.NodeType.Created, KQB.NodeType.Disagree, KQB.NodeType.Agree, KQB.NodeType.NumberEdits, KQB.NodeType.LastEdited, KQB.NodeType.Name, KQB.NodeType.Origin, KQB.NodeType.Target, KQB.NodeType.Text, KQB.NodeType.Title);
	KQB.NodeDescriptions[KQB.NodeType.Author].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.Disagree].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.Disagree].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.Disagree].isSingular = false;
	KQB.NodeDescriptions[KQB.NodeType.Disagree].hasText = true;
	KQB.NodeDescriptions[KQB.NodeType.Disagree].SwitchAllowed = new Array( KQB.NodeType.AnchorText, KQB.NodeType.Author, KQB.NodeType.Created, KQB.NodeType.Disagree, KQB.NodeType.Agree, KQB.NodeType.NumberEdits, KQB.NodeType.LastEdited, KQB.NodeType.Name, KQB.NodeType.Origin, KQB.NodeType.Target, KQB.NodeType.Text, KQB.NodeType.Title);
	KQB.NodeDescriptions[KQB.NodeType.Disagree].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.Agree].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.Agree].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.Agree].isSingular = false;
	KQB.NodeDescriptions[KQB.NodeType.Agree].hasText = true;
	KQB.NodeDescriptions[KQB.NodeType.Agree].SwitchAllowed = new Array( KQB.NodeType.AnchorText, KQB.NodeType.Author, KQB.NodeType.Created, KQB.NodeType.Disagree, KQB.NodeType.Agree, KQB.NodeType.NumberEdits, KQB.NodeType.LastEdited, KQB.NodeType.Name, KQB.NodeType.Origin, KQB.NodeType.Target, KQB.NodeType.Text, KQB.NodeType.Title);
	KQB.NodeDescriptions[KQB.NodeType.Agree].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.Created].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.Created].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.Created].hasText = true;
	KQB.NodeDescriptions[KQB.NodeType.Created].SwitchAllowed = new Array( KQB.NodeType.AnchorText, KQB.NodeType.Author, KQB.NodeType.Created, KQB.NodeType.Disagree, KQB.NodeType.Agree, KQB.NodeType.NumberEdits, KQB.NodeType.LastEdited, KQB.NodeType.Name, KQB.NodeType.Origin, KQB.NodeType.Target, KQB.NodeType.Text, KQB.NodeType.Title);
	KQB.NodeDescriptions[KQB.NodeType.Created].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding);
	
	KQB.NodeDescriptions[KQB.NodeType.LastEdited].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.LastEdited].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.LastEdited].hasText = true;
	KQB.NodeDescriptions[KQB.NodeType.LastEdited].SwitchAllowed = new Array( KQB.NodeType.AnchorText, KQB.NodeType.Author, KQB.NodeType.Created, KQB.NodeType.Disagree, KQB.NodeType.Agree, KQB.NodeType.NumberEdits, KQB.NodeType.LastEdited, KQB.NodeType.Name, KQB.NodeType.Origin, KQB.NodeType.Target, KQB.NodeType.Text, KQB.NodeType.Title);
	KQB.NodeDescriptions[KQB.NodeType.LastEdited].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding);
	
	KQB.NodeDescriptions[KQB.NodeType.Tag].Category = KQB.NodeCategories.Resource;
	KQB.NodeDescriptions[KQB.NodeType.Tag].isResizeable = true;
	KQB.NodeDescriptions[KQB.NodeType.Tag].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.Tag].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding, KQB.NodeType.Name, KQB.NodeType.Author, KQB.NodeType.Created, KQB.NodeType.URI, KQB.NodeType.Disagree, KQB.NodeType.Agree);
	
	KQB.NodeDescriptions[KQB.NodeType.Title].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.Title].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.Title].hasText = true;
	KQB.NodeDescriptions[KQB.NodeType.Title].SwitchAllowed = new Array( KQB.NodeType.AnchorText, KQB.NodeType.Author, KQB.NodeType.Created, KQB.NodeType.Disagree, KQB.NodeType.Agree, KQB.NodeType.NumberEdits, KQB.NodeType.LastEdited, KQB.NodeType.Name, KQB.NodeType.Origin, KQB.NodeType.Target, KQB.NodeType.Text, KQB.NodeType.Title);
	KQB.NodeDescriptions[KQB.NodeType.Title].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.Text].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.Text].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.Text].hasText = true;
	KQB.NodeDescriptions[KQB.NodeType.Text].SwitchAllowed = new Array( KQB.NodeType.AnchorText, KQB.NodeType.Author, KQB.NodeType.Created, KQB.NodeType.Disagree, KQB.NodeType.Agree, KQB.NodeType.NumberEdits, KQB.NodeType.LastEdited, KQB.NodeType.Name, KQB.NodeType.Origin, KQB.NodeType.Target, KQB.NodeType.Text, KQB.NodeType.Title);
	KQB.NodeDescriptions[KQB.NodeType.Text].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.NumberEdits].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.NumberEdits].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.NumberEdits].hasText = true;
	KQB.NodeDescriptions[KQB.NodeType.NumberEdits].SwitchAllowed = new Array( KQB.NodeType.AnchorText, KQB.NodeType.Author, KQB.NodeType.Created, KQB.NodeType.Disagree, KQB.NodeType.Agree, KQB.NodeType.NumberEdits, KQB.NodeType.LastEdited, KQB.NodeType.Name, KQB.NodeType.Origin, KQB.NodeType.Target, KQB.NodeType.Text, KQB.NodeType.Title);
	KQB.NodeDescriptions[KQB.NodeType.NumberEdits].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding);
	
	KQB.NodeDescriptions[KQB.NodeType.Link].Category = KQB.NodeCategories.Resource;
	KQB.NodeDescriptions[KQB.NodeType.Link].isResizeable = true;
	KQB.NodeDescriptions[KQB.NodeType.Link].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.Link].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding, KQB.NodeType.Tag, KQB.NodeType.Target, KQB.NodeType.Origin, KQB.NodeType.AnchorText, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.Fragment].Category = KQB.NodeCategories.Resource;
	KQB.NodeDescriptions[KQB.NodeType.Fragment].isResizeable = true;
	KQB.NodeDescriptions[KQB.NodeType.Fragment].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.Fragment].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding, KQB.NodeType.Author, KQB.NodeType.Child, KQB.NodeType.Descendant, KQB.NodeType.Created, KQB.NodeType.Tag, KQB.NodeType.URI, KQB.NodeType.Compound, KQB.NodeType.Link);
	
	KQB.NodeDescriptions[KQB.NodeType.Descendant].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.Descendant].isSingular = false;
	KQB.NodeDescriptions[KQB.NodeType.Descendant].SwitchAllowed = new Array(KQB.NodeType.Child+'');
	KQB.NodeDescriptions[KQB.NodeType.Descendant].AllowedChildren = new Array( KQB.NodeType.ContentItem, KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.URI, KQB.NodeType.Author, 
		KQB.NodeType.Created, KQB.NodeType.LastEdited, KQB.NodeType.Tag, KQB.NodeType.Title, KQB.NodeType.Text, KQB.NodeType.NumberEdits, KQB.NodeType.Link,
		KQB.NodeType.Fragment, KQB.NodeType.Agree, KQB.NodeType.Descendant, KQB.NodeType.Child, KQB.NodeType.Disagree, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.Child].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.Child].isSingular = false;
	KQB.NodeDescriptions[KQB.NodeType.Child].SwitchAllowed = new Array(KQB.NodeType.Descendant+'');
	KQB.NodeDescriptions[KQB.NodeType.Child].AllowedChildren = new Array( KQB.NodeType.ContentItem, KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.URI, KQB.NodeType.Author, 
		KQB.NodeType.Created, KQB.NodeType.LastEdited, KQB.NodeType.Tag, KQB.NodeType.Title, KQB.NodeType.Text, KQB.NodeType.NumberEdits, KQB.NodeType.Link,
		KQB.NodeType.Fragment, KQB.NodeType.Agree, KQB.NodeType.Descendant, KQB.NodeType.Child, KQB.NodeType.Disagree, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.Target].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.Target].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.Target].AllowedChildren = new Array( KQB.NodeType.ContentItem, KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.URI, KQB.NodeType.Author, 
		KQB.NodeType.Created, KQB.NodeType.LastEdited, KQB.NodeType.Tag, KQB.NodeType.Title, KQB.NodeType.Text, KQB.NodeType.NumberEdits, KQB.NodeType.Link,
		KQB.NodeType.Fragment, KQB.NodeType.Agree, KQB.NodeType.Descendant, KQB.NodeType.Child, KQB.NodeType.Disagree, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.Origin].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.Origin].SwitchAllowed = new Array( KQB.NodeType.AnchorText, KQB.NodeType.Author, KQB.NodeType.Created, KQB.NodeType.Disagree, KQB.NodeType.Agree, KQB.NodeType.NumberEdits, KQB.NodeType.LastEdited, KQB.NodeType.Name, KQB.NodeType.Origin, KQB.NodeType.Target, KQB.NodeType.Text, KQB.NodeType.Title);
	KQB.NodeDescriptions[KQB.NodeType.Origin].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.ContentItem, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.AnchorText].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.AnchorText].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.AnchorText].hasText = true;
	KQB.NodeDescriptions[KQB.NodeType.AnchorText].SwitchAllowed = new Array( KQB.NodeType.AnchorText, KQB.NodeType.Author, KQB.NodeType.Created, KQB.NodeType.Disagree, KQB.NodeType.Agree, KQB.NodeType.NumberEdits, KQB.NodeType.LastEdited, KQB.NodeType.Name, KQB.NodeType.Origin, KQB.NodeType.Target, KQB.NodeType.Text, KQB.NodeType.Title);
	KQB.NodeDescriptions[KQB.NodeType.AnchorText].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Fragment, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.Name].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.Name].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.Name].hasText = true;
	KQB.NodeDescriptions[KQB.NodeType.Name].SwitchAllowed = new Array(KQB.NodeType.AnchorText, KQB.NodeType.Agree, KQB.NodeType.Disagree, KQB.NodeType.Author, KQB.NodeType.Origin, KQB.NodeType.Target, KQB.NodeType.Text, KQB.NodeType.Title, KQB.NodeType.URI);
	KQB.NodeDescriptions[KQB.NodeType.Name].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding, KQB.NodeType.TagGrouping, KQB.NodeType.TagCharacterization, KQB.NodeType.Compound);

	KQB.NodeDescriptions[KQB.NodeType.TagGrouping].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.TagGrouping].isResizeable = true;
	KQB.NodeDescriptions[KQB.NodeType.TagGrouping].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.TagGrouping].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.TagCharacterization);
	
	KQB.NodeDescriptions[KQB.NodeType.TagCharacterization].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.TagCharacterization].FixedWidth2 = true;
	KQB.NodeDescriptions[KQB.NodeType.TagCharacterization].hasText = true;
	KQB.NodeDescriptions[KQB.NodeType.TagCharacterization].LabelTextSmall = true;
	KQB.NodeDescriptions[KQB.NodeType.TagCharacterization].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.TagCharacterization].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.TagGrouping);
	
	
	KQB.NodeDescriptions[KQB.NodeType.NOT].Category = KQB.NodeCategories.Operator;
	KQB.NodeDescriptions[KQB.NodeType.NOT].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.NOT].InheritAllowedChildren = true;
	KQB.NodeDescriptions[KQB.NodeType.NOT].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.NOT].AllowedChildren = new Array();
	
	KQB.NodeDescriptions[KQB.NodeType.OPTIONAL].Category = KQB.NodeCategories.Operator;
	KQB.NodeDescriptions[KQB.NodeType.OPTIONAL].FixedWidth2 = true;
	KQB.NodeDescriptions[KQB.NodeType.OPTIONAL].InheritAllowedChildren = true;
	KQB.NodeDescriptions[KQB.NodeType.OPTIONAL].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.OPTIONAL].AllowedChildren = new Array();
	
	KQB.NodeDescriptions[KQB.NodeType.ContentItem].Category = KQB.NodeCategories.Resource;
	KQB.NodeDescriptions[KQB.NodeType.ContentItem].isResizeable = true;
	KQB.NodeDescriptions[KQB.NodeType.ContentItem].LabelTextSmall = true;
	KQB.NodeDescriptions[KQB.NodeType.ContentItem].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.ContentItem].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.URI, KQB.NodeType.Author, 
		KQB.NodeType.Created, KQB.NodeType.LastEdited, KQB.NodeType.Tag, KQB.NodeType.Title, KQB.NodeType.Text, KQB.NodeType.NumberEdits, KQB.NodeType.Link,
		KQB.NodeType.Fragment, KQB.NodeType.Agree, KQB.NodeType.Descendant, KQB.NodeType.Child, KQB.NodeType.Disagree, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.URI].Category = KQB.NodeCategories.Qualifier;
	KQB.NodeDescriptions[KQB.NodeType.URI].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.URI].hasText = true;
	KQB.NodeDescriptions[KQB.NodeType.URI].SwitchAllowed = new Array( KQB.NodeType.AnchorText, KQB.NodeType.Agree, KQB.NodeType.Author, KQB.NodeType.Created, KQB.NodeType.Disagree, KQB.NodeType.NumberEdits, KQB.NodeType.LastEdited, KQB.NodeType.Name, KQB.NodeType.Origin, KQB.NodeType.Target, KQB.NodeType.Text, KQB.NodeType.Title);
	KQB.NodeDescriptions[KQB.NodeType.URI].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable, KQB.NodeType.Binding, KQB.NodeType.Compound);
	
	KQB.NodeDescriptions[KQB.NodeType.ALL].Category = KQB.NodeCategories.Operator;
	KQB.NodeDescriptions[KQB.NodeType.ALL].hasVarAndText = true;
	KQB.NodeDescriptions[KQB.NodeType.ALL].FixedWidth2 = true;
	KQB.NodeDescriptions[KQB.NodeType.ALL].DisallowOperators = true;
	KQB.NodeDescriptions[KQB.NodeType.ALL].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.ALL].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable);
	
	KQB.NodeDescriptions[KQB.NodeType.SOME].Category = KQB.NodeCategories.Operator;
	KQB.NodeDescriptions[KQB.NodeType.SOME].FixedWidth3 = true;
	KQB.NodeDescriptions[KQB.NodeType.SOME].DisallowOperators = true;
	KQB.NodeDescriptions[KQB.NodeType.SOME].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.SOME].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable);
	
	KQB.NodeDescriptions[KQB.NodeType.AVG].Category = KQB.NodeCategories.Operator;
	KQB.NodeDescriptions[KQB.NodeType.AVG].hasVariable = true;
	KQB.NodeDescriptions[KQB.NodeType.AVG].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.AVG].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.AVG].AllowedChildren = new Array(KQB.NodeType.Variable + '');
	
	KQB.NodeDescriptions[KQB.NodeType.COUNT].Category = KQB.NodeCategories.Operator;
	KQB.NodeDescriptions[KQB.NodeType.COUNT].hasVariable = true;
	KQB.NodeDescriptions[KQB.NodeType.COUNT].isUnary = true;
	KQB.NodeDescriptions[KQB.NodeType.COUNT].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.COUNT].AllowedChildren = new Array(KQB.NodeType.Variable + '');
	
	KQB.NodeDescriptions[KQB.NodeType.Binding].Category = KQB.NodeCategories.Other;
	KQB.NodeDescriptions[KQB.NodeType.Binding].hasVarAndText = true;
	KQB.NodeDescriptions[KQB.NodeType.Binding].FixedWidth2 = true;
	KQB.NodeDescriptions[KQB.NodeType.Binding].DisallowOperators = true;
	KQB.NodeDescriptions[KQB.NodeType.Binding].SwitchAllowed = new Array();
	KQB.NodeDescriptions[KQB.NodeType.Binding].AllowedChildren = new Array(KQB.NodeType.Keyword, KQB.NodeType.Variable);
	
	// add category-specific css classes (to color resources, qualifiers, operators and others differently)
	for ( var i = 0; i < KQB.NodeDescriptions.length; i++ )
	{
		switch (KQB.NodeDescriptions[i].Category)
		{
			case KQB.NodeCategories.Resource:
				KQB.NodeDescriptions[i].AdditionalCSSClass = "KQBNodeResource";
				break;
			case KQB.NodeCategories.Qualifier:
				KQB.NodeDescriptions[i].AdditionalCSSClass = "KQBNodeQualifier";
				KQB.NodeDescriptions[i].AllowedChildren.push(KQB.NodeType.ALL);
				KQB.NodeDescriptions[i].AllowedChildren.push(KQB.NodeType.SOME);
				break;
			case KQB.NodeCategories.Operator:
				KQB.NodeDescriptions[i].AdditionalCSSClass = "KQBNodeOperator";
				break;
			case KQB.NodeCategories.Other:
				KQB.NodeDescriptions[i].AdditionalCSSClass = "KQBNodeOther";
				break;
		}
	}
	KQB.NodeDescriptions[KQB.NodeType.Variable].AdditionalCSSClass = "KQBNodeVariable";
}

/*
 * Init
 * 
 * this function initializes the KiWi Query Builder
 * 
 * IMPORTANT: this must be called on document.load !
 */
KQB.Init = function ()
{
	// first initialize the node descriptions
	KQB.InitNodeDescriptions();
	
	// fill variables with pointers to document elements
	KQB.Workspace 	= document.getElementById("workspace");
	KQB.Workspace.onmouseover = KQBDragDrop.OnMouseOver;
	// disable text selection in IE. Else dragging operations will be interpreted as text selection and
	// select label texts which is unwanted
	if (typeof KQB.Workspace.onselectstart!="undefined")
		KQB.Workspace.onselectstart=function(){return false}
	KQB.Tooltippane = document.getElementById("tooltippane");
	KQB.Hintpane 	= document.getElementById("hintpane");
	KQB.Warningpane = document.getElementById("warningpane");
	KQB.Outputpane 	= document.getElementById("outputpane");
	KQB.Debugpane 	= document.getElementById("debugpane");
	KQB.ResizeArea	= document.getElementById("resizearea");
	KQB.HintArea	= document.getElementById("hintarea");

	// the outputpane will display errors in red. remove the red text color when typing into it
	KQB.Outputpane.onKeyDown = function(){KQBUtilities.RemoveHTMLClass(KQB.Outputpane, "outputerror");};
	
	// setup mouse handlers for drag&drop
	document.body.onmousedown = KQBDragDrop.MouseDown;	// note: this must be on body and not on workspace,
	document.body.onmousemove = KQBDragDrop.MouseMove;  // else we don't get the event when something is dropped
	document.body.onmouseup   = KQBDragDrop.MouseUp;	// outside the workspace to delete it 

	// add node tooltips to the items in the drop down menu
	KQB.InitMenuTooltips ();
	
	
	// init the invisible 'root' node, which is the parent of all nodes in the workarea
	KQB.Query = new KQB.Node();
	KQB.Query.Type = KQB.NodeType.Root;
	KQB.Query.Name = "root";
	KQB.Query.ID = -1;
	
	// arrays to hold nodes with errors/warnings/that need to be ignored in the output
	KQB.ErrorNodes = new Array();
	KQB.WarningNodes = new Array();
	KQB.IgnoreNodes = new Array();
	
	// init css tooltip
	KQBDragDrop.InitTooltip();
		
	// init node infos and update the output
	KQB.InitNodeInfo();
	KQB.UpdateOutput();
	
	// init the undo buffer
	KQB.UndoBuffer = new Array();
	KQB.UndoBufferHTML = new Array();
	KQB.UndoPos = -1;
	KQB.UpdateUndoBuffer();
	
	KQB.ShowSavedQueries();
	
	KQB.AutoResize = true;
	if (typeof KQB.ResizeArea.onselectstart!="undefined") // IE
		KQB.ResizeArea.onselectstart=function(){return false};
	else if (typeof KQB.ResizeArea.style.MozUserSelect!="undefined") // FF
		KQB.ResizeArea.style.MozUserSelect="none";
	KQB.Workspace.style.height = KQBUtilities.GetWindowHeight() - 275 + "px";
	window.onresize = function () { if (KQB.AutoResize==true) KQB.Workspace.style.height = KQBUtilities.GetWindowHeight() - 275 + "px"; }
	
	// some trickery needed to get tooltip pane and resize area aligned correctly across all browsers
	var wswidth = KQB.Workspace.offsetWidth  + "px";
	var resizewidth = KQB.Workspace.offsetWidth - KQB.ResizeArea.offsetWidth   + "px";
	KQB.Workspace.style.width = wswidth;	// since some browsers include paddings in offsetWidth and some don't, this is neccessary
	KQB.HintArea.style.width = wswidth;
	KQB.Hintpane.style.width = resizewidth;
	KQB.Tooltippane.style.width = wswidth;
	var inputstring = document.getElementById('kwqlSearchbarForm:searchField').value;
    if (inputstring!="")
    {
	KQB.ParseText(inputstring);
    }
}

/**
 * InitMenuTooltips
 * 
 * this function adds node descriptions/tooltips to the elements in the drop down menu
 * 
 * if the title value of a html element in the drop down menu contains the name of a node,
 * the tooltip of this node is added to that html element
 */
KQB.InitMenuTooltips = function ()
{
	var menu = document.getElementById("menulevel1");
	traverse (menu);

	function traverse(elem)
	{
		var title = null;
		if ( elem.getAttribute )
			title = elem.getAttribute("title");
		if (title)
			if (KQBStrings.ToolTips[title])
				elem.setAttribute ("title", KQBStrings.ToolTips[title]);
	
		var children = elem.childNodes;
		for ( var i = 0; i < children.length; i++ )
			traverse (children[i]);
	}
}



/**
 * ShowTooltip
 * 
 * displays the given string in the tooltip pane
 */
KQB.ShowTooltip = function (str)
{ 
	if (KQB.Tooltippane)
		KQB.Tooltippane.innerHTML = str;
}


/**
 * ShowHint
 * 
 * displays the given string in the hint pane
 */
KQB.ShowHint = function (str)
{
	if(KQB.Hintpane)
		KQB.Hintpane.innerHTML = str;
}

/**
 * Show Warning
 * 
 * displays the given string in the warnings pane
 */
KQB.ShowWarning = function (str)
{
	if(KQB.Hintpane)
		KQB.Hintpane.innerHTML = str;
}

/**
 * ShowOutput
 * 
 * displays the given string in the output pane
 */
KQB.ShowOutput = function (str)
{
	if (!KQB.Outputpane)
		return;
	KQBUtilities.RemoveHTMLClass(KQB.Outputpane, "outputerror");
	KQB.Outputpane.value = str;
}

/**
 * Show Error
 * 
 * displays the given string as an error (with css class outputerror) in the output pane
 * @param {Object} str
 */
KQB.ShowError = function (str)
{
	if (!KQB.Outputpane)
		return;
	KQB.Outputpane.value = str;
	KQBUtilities.AddHTMLClass(KQB.Outputpane, "outputerror");
}

/**
 * ShowDebug
 * 
 * displays the given string in the debug pane
 */
KQB.ShowDebug = function (str)
{
	if(KQB.Debugpane)
		KQB.Debugpane.innerHTML = str;
}

/**
 * UpdateUndoBuffer
 *
 * this function must be called whenever the undo buffer should get updated
 * it saves the current state of KQB, to be restored via Undo or Redo
 */
KQB.UpdateUndoBuffer = function ()
{
	if (!KQB.UndoBuffer)
		return;
	if (!KQB.UndoBufferHTML)
		return;
	KQB.UndoPos = KQB.UndoPos + 1;
	// clone the workspace and put it into the html undo buffer
	var ws = document.getElementById("workspace");
	var newnode = ws.cloneNode(true);
	KQB.UndoBufferHTML.splice(KQB.UndoPos, 0, newnode);
	// clone the internal kqb query tree and put it into the undobuffer
	KQB.UndoBuffer.InsertElement(KQB.UndoPos, KQB.Query.clone());

}

/**
 * Undo
 *
 * restores the previous state of KQB from the undo buffer
 */
KQB.Undo = function ()
{
	if (!KQB.UndoBuffer)
		return;
	if (KQB.UndoPos < 1)
		return;
	KQB.UndoPos = KQB.UndoPos - 1;
	KQB.RestoreState(KQB.UndoPos);
}

/**
 * Redo
 *
 * restores the state from the undo buffer that was last undone
 */
KQB.Redo = function ()
{
	if (!KQB.UndoBuffer)
		return;
	if (KQB.UndoPos < 0)
		return;
	if (KQB.UndoPos > KQB.UndoBuffer.length-2)
		return;
	KQB.UndoPos = KQB.UndoPos + 1;
	KQB.RestoreState(KQB.UndoPos);
}

/**
 * RestoreState
 *
 * restores the state at <pos> in the UndoBuffer
 * this function should only be called from Undo and Redo
 */
KQB.RestoreState = function (pos)
{	
	if (pos < 0)
		return;
	if (pos > KQB.UndoBuffer.length-1)
		return;
	
	

	// load workspace and internal Query tree from buffers
	var data = KQB.UndoBuffer[pos].clone();
	var data2 = KQB.UndoBufferHTML[pos].cloneNode(true);
	var ws = document.getElementById("workspace");
	document.body.replaceChild(data2, ws);
	KQB.Query = data;
	
	// restore workspace
	KQB.Workspace = document.getElementById("workspace");
	KQB.Workspace.onmouseover = KQBDragDrop.OnMouseOver;
	
	// have to restore the return false onselectstart function in IE,
	// else when dragging label text will get selected sometimes
	if (typeof KQB.Workspace.onselectstart!="undefined")
		KQB.Workspace.onselectstart=function(){return false}
	// update everything
	UpdateChildren(KQB.Query);
	KQB.Query.Refresh();
	// restore tooltips
	KQB.ShowTooltip("");
	KQBDragDrop.InitTooltip();
	
	Clear(KQB.Query);
	
	KQB.UpdateOutput(true);


	function Clear (node)
	{
		for ( var i = 0; i < node.Children.length; i++)
			Clear(node.Children[i]);
		if (!node) return;
		if (!node.HTMLNode) return;
		KQBUtilities.RemoveHTMLClass(node.HTMLNode, "KQBNodeError");
		KQBUtilities.RemoveHTMLClass(node.HTMLNode.firstChild, "KQBLabelError");
		node.ErrorTooltip = null;
		KQBUtilities.RemoveHTMLClass(node.HTMLNode, "KQBNodeWarning");
		KQBUtilities.RemoveHTMLClass(node.HTMLNode.firstChild, "KQBLabelWarning");	
		node.WarningTooltip = null;
		
	}
	
	/**
	 * UpdateChildren
	 *
	 * this function connects the restored html document tree with the restored internal KQB Query tree,
	 * and restores things like html event handler
	 */
	function UpdateChildren (node)
	{
		// restore textbox event handler
		if(node.TextBox)
		{ 
			var tb = findNode(node.ID,"KQBTextBox");
			if (tb==null)
				alert("error");
			else
			{
				tb.onkeyup = function () { this.title=this.value; KQB.UpdateOutput(); };
				tb.onmouseover = KQBDragDrop.OnMouseOver;
				node.TextBox = tb.parentNode;
			}
				
		}
		// connect html elements with query elements via their IDs
		if (node.Separator)
		{
			var sep = findNode(node.ID,"KQBSeparator");
			node.Separator = sep;
		}
		
		if (node.ResizeBox)
		{
			var rb = findNode(node.ID,"KQBLabelResizeBox");
			node.ResizeBox = rb;
		}
		
		for (var n = 0; n < node.Children.length; n++)
		{
			var id = node.Children[n].ID; 
			var idnode = findNode (id,"KQBNode");
	
			if (!idnode)
				alert("error, node not found");
	
			node.Children[n].HTMLNode = idnode;
			node.Children[n].HTMLNode.onmouseover = KQBDragDrop.OnMouseOver;
			node.Children[n].Parent = node;
			
			UpdateChildren(node.Children[n]);
		}
	}
	
	/**
	 * findNode
	 * find a html node given it's ID and type (since e.g. a node and it's label both have the same id but different types)
	 */
	function findNode(id,type)
	{
		return find(id, document.body, type);
		
		function find (id, node, type)
		{
			if (KQBUtilities.IsHTMLClass(node,type))
				if (node.getAttribute("id") == id)
				    return node;
				
			for (var i = 0; i < node.childNodes.length; i++)
			{
				var f = find(id, node.childNodes[i], type);
				if (f)
					return f;
			}
			return null;
		}
	}
}



/**
 * GetNodeByID
 * 
 * returns a node given it's id
 * @param {Object} id
 */
KQB.GetNodeByID = function(id)
{
	return GetChildNodeByID(KQB.Query,id);
	
	function GetChildNodeByID (node, id)
	{
		if (!node)
			return null;
		if (node.ID == id )
			return node;
		for ( var j = 0; j < node.Children.length; j++ )
		{
			var res = GetChildNodeByID(node.Children[j],id);
			if (res!=null)
				return res;		
		}
		return null;
	}
	return null;
}

/**
 * AddChild
 * 
 * adds one node as child of another, given their ids
 * @param {Object} idparent
 * @param {Object} idchild
 */
KQB.AddChild = function (idparent, idchild)
{
	var parent = KQB.GetNodeByID(idparent);
	var child = KQB.GetNodeByID(idchild);	
	parent.AddChild(child);
}

/**
 * GetNextNodeID
 * 
 * returns a new unused number to be used as an id for a new node
 */
KQB.GetNextNodeID = function()
{
	KQB.LastNodeID++;
	return  KQB.LastNodeID;
}


/**
 * CreateNode
 * 
 * creates a new node of the given type
 * 
 * if the second parameter is given (and 'true'), the node will be created empty,
 * meaning even nodes that have 'hasText' or 'hasVariable' true, and would thus normally
 * be created with keyword or variable children, will be created without those
 */
KQB.CreateNode = function (NodeType, empty)
{
	// create a new node and set it's values to those specified in the NodeDescriptions array
	var desc = KQB.NodeDescriptions[NodeType];
	var newnode = new KQB.Node();
	newnode.Name = desc.Name;
	newnode.Type = desc.Type;
	newnode.isLeaf = desc.isLeaf;
	newnode.isUnary = desc.isUnary;
	newnode.isSingular = desc.isSingular;
	newnode.isResizeable = desc.isResizeable;
	newnode.FixedWidth2 = desc.FixedWidth2;
	newnode.FixedWidth3 = desc.FixedWidth3;
	newnode.LabelTextSmall = desc.LabelTextSmall;
	newnode.Category = desc.Category;
	if (newnode.FixedWidth2)
		newnode.ShowFreeSpace = true;
	newnode.ID = KQB.GetNextNodeID();
	
	// create the html node for this node
	newnode.CreateHTMLNode();
	if ( empty == true )
		return newnode;
		
	// if this node is not forced empty, then create children if this nodetype has any
	if ( desc.hasText && (desc.Type != KQB.NodeType.Keyword) )
	{
		var textnode = KQB.CreateNode( KQB.NodeType.Keyword );
		newnode.AddChild(textnode);
	}
	
	if ( desc.hasVariable )
	{
		var varnode = KQB.CreateNode(KQB.NodeType.Variable);
		newnode.AddChild(varnode);
	}
	
	if (desc.hasVarAndText )
	{
		var varnode = KQB.CreateNode(KQB.NodeType.Variable);
		var textnode = KQB.CreateNode( KQB.NodeType.Keyword );
		newnode.AddChild(varnode);
		newnode.AddChild(textnode);
	}
	
	if (NodeType == KQB.NodeType.SOME)
	{
		var varnode = KQB.CreateNode(KQB.NodeType.Variable);
		var textnode = KQB.CreateNode( KQB.NodeType.Keyword );
		var textnode2 = KQB.CreateNode( KQB.NodeType.Keyword );
		newnode.AddChild(textnode);
		newnode.AddChild(varnode);
		newnode.AddChild(textnode2);
	}
	
	if (NodeType == KQB.NodeType.OPTIONAL)
	{
		var textnode = KQB.CreateNode(KQB.NodeType.Keyword);
		newnode.AddChild(textnode);
	}
	
	// special cases: nodes that should be created with named children, given textbox values, special children
	// or need children to be fixed/unmoveable
	switch (NodeType)
	{
		case KQB.NodeType.SOME:
			newnode.Children[0].ChangeName("Num");
			newnode.Children[0].MakeFixed(true);
			newnode.Children[1].MakeFixed(true);
			newnode.Children[2].ChangeName("Delim");
			newnode.Children[2].MakeFixed(true);
			break;
		case KQB.NodeType.ALL:
			newnode.Children[1].ChangeName("Delim");
			newnode.Children[1].SetTextValue(",");
			newnode.Children[0].MakeFixed(true);
			newnode.Children[1].MakeFixed(true);
			break;
		case KQB.NodeType.TagCharacterization:
			newnode.Children[0].ChangeName("Name");
			newnode.Children[0].MakeFixed(true);
			break;
		case KQB.NodeType.AVG:
			newnode.Children[0].MakeFixed(true);
			break;
		case KQB.NodeType.COUNT:
			newnode.Children[0].MakeFixed(true);
			break;
		case KQB.NodeType.Binding:
			newnode.Children[0].MakeFixed(true);
			newnode.Children[1].MakeFixed(true);
			break;
		case KQB.NodeType.OPTIONAL:
			newnode.Children[0].MakeFixed(true);
			newnode.Children[0].ChangeName("default");
			break;
	}
	
	return newnode;
}


/**
 * ShowNodeTooltip
 * 
 * display the tooltip of the given type of node in the tooltip pane
 * @param {Object} nodetype
 */
KQB.ShowNodeTooltip = function (nodetype)
{
	KQB.ShowTooltip (KQB.NodeDescriptions[nodetype].Tooltip);
}

/**
 * NewNode
 * 
 * creates a new node, given it's type, and puts it on the workspace
 * @param {Object} NodeType
 */
KQB.NewNode = function (NodeType)
{
	if (!NodeType)
		return; 
	var newnode = KQB.CreateNode(NodeType);
	if (newnode)
		newnode.DropOnWorkspace();
	KQB.UpdateOutput();
}

KQB.GetEqualVariables = function (node)
{
	if (node.Type != KQB.NodeType.Variable)
		return null;
	var top = null;
	var n = node;
	while (n.Parent)
	{
		n = n.Parent;
		top = n;
	}
	var res = new Array();
	FindVars (node.GetTextValue(), top, res);
	return res;

	function FindVars (name, n, array)
	{
		if (n.Type == KQB.NodeType.Variable)
			if (n.GetTextValue() == name)
				array.push(n);
		for (var i = 0; i < n.GetNumChildren(); i++)
			FindVars (name, n.Children[i], array);
	}
}

/**
 * DeleteNode
 * 
 * deletes a node given it's id
 * @param {Object} nodeid
 */
KQB.DeleteNode = function (nodeid)
{
	var node = KQB.GetNodeByID(nodeid);
	if (node)
		node.Parent.RemoveChild(node);
}

/**
 * CanHaveChild
 * 
 * checks if a the 'parent' node is allowed to be added 'child' as a child node
 * checks both if the types are compatible, and if there is free space in the parent node
 * 
 * @param {Object} parent
 * @param {Object} child
 * @param {Object} sourceparent
 */
KQB.CanHaveChild = function (parent, child, sourceparent)
{	
	if (!sourceparent)			// since this function needs to be called recursively in some cases we need sourceparent to hold the original parent
		sourceparent = parent;
	if ( parent == KQB.Query )		// the workspace can contain all nodes as children
		return true;
	if (child.Type == KQB.NodeType.Rule)
		return false;      // rules are top level nodes and can not be children of anything
	if (child.Type == KQB.NodeType.SPARQL)	// sparql nodes can only be top level or children of a rule
	{
		if ( (parent.Type != KQB.NodeType.Rule) && (parent.Type != KQB.NodeType.Compound)  )
			return false;
	}
	if (child.Type == KQB.NodeType.OPTIONAL)
	{
		if (parent.Category != KQB.NodeCategories.Resource)
			return false;
	}
	var parenttype = parent.Type;
	var childtype = child.Type;
	
	// check if types are compatible
	if ( !KQB.NodeDescriptions[parenttype] || !KQB.NodeDescriptions[childtype] || !KQB.NodeDescriptions[parenttype].AllowedChildren )
		return false;
	if (!KQB.NodeDescriptions[parenttype].InheritAllowedChildren && !KQB.NodeDescriptions[childtype].InheritAllowedChildren) 
	{
		for (var i = 0; i < KQB.NodeDescriptions[parenttype].AllowedChildren.length; i++) 
		{
			if (KQB.NodeDescriptions[parenttype].AllowedChildren[i] == childtype) 
			{
				if (sourceparent.isUnary && (sourceparent.GetNumChildren()==1))
					return false;
				if ( KQB.NodeDescriptions[parenttype].FixedWidth2 && (parent.GetNumChildren()==2))
					return false;
				if ( KQB.NodeDescriptions[parenttype].FixedWidth3 && (parent.GetNumChildren()==3))
					return false;
				return true;
			}
		}
		return false;
	}
	//alert("blub");
	// if we're here, InheritAllowedChildren is true
	
	// check if operators are allowed
	if (KQB.NodeDescriptions[parenttype].DisallowOperators == true)
		return false;
	if (KQB.NodeDescriptions[childtype].DisallowOperators == true)
		return false;

	// if we have an inheriting node like 'AND' we need to check parents
	if ( KQB.NodeDescriptions[parenttype].InheritAllowedChildren == true )
		if (sourceparent.isUnary && (sourceparent.GetNumChildren()==1))
			return false;
		else return KQB.CanHaveChild(parent.Parent, child, parent);
	// and all children
	if ( KQB.NodeDescriptions[childtype].InheritAllowedChildren == true )
	{
		for ( var i = 0; i < child.Children.length; i++ )
		{
			if ( !KQB.CanHaveChild(parent, child.Children[i], parent) )
				return false;
		}
		return true;
	}
}


/**
 * ValidChild
 *
 * checks if the child is a valid child of it's parent node
 * 
 * KQB doesn't allow dropping children on invalid parents,
 * however when parsing textual KWQL queries it has been opted to allow parsing
 * queries that contain resources with invalid qualifiers and mark them as erroneus to allow the user
 * to change them, rather than to report a parser error and do nothing
 */
KQB.ValidChild = function (parent, child, sourceparent)
{	
	if (!sourceparent)			// since this function needs to be called recursively in some cases we need sourceparent to hold the original parent
		sourceparent = parent;
	if ( parent == KQB.Query )		// the workspace can contain all nodes as children
		return true;
	if (child.Type == KQB.NodeType.Rule)
		return false;      // rules are top level nodes and can not be children of anything
	if (child.Type == KQB.NodeType.SPARQL)	// sparql nodes can only be top level or children of a rule
	{
		if ( (parent.Type != KQB.NodeType.Rule) && (parent.Type != KQB.NodeType.Compound)  )
			return false;
	}

	var parenttype = parent.Type;
	var childtype = child.Type;
	
	// check if types are compatible
	if ( !KQB.NodeDescriptions[parenttype] || !KQB.NodeDescriptions[childtype] || !KQB.NodeDescriptions[parenttype].AllowedChildren )
		return false;
	if (!KQB.NodeDescriptions[parenttype].InheritAllowedChildren && !KQB.NodeDescriptions[childtype].InheritAllowedChildren) 
	{
		for (var i = 0; i < KQB.NodeDescriptions[parenttype].AllowedChildren.length; i++) 
		{
			if (KQB.NodeDescriptions[parenttype].AllowedChildren[i] == childtype) 
				return true;
		}
		return false;
	}
	// if we're here, InheritAllowedChildren is true
	
	// check if operators are allowed
	if (KQB.NodeDescriptions[parenttype].DisallowOperators == true)
		return false;
	if (KQB.NodeDescriptions[childtype].DisallowOperators == true)
		return false;
	
	// if we have an inheriting node like 'AND' we need to check parents
	if ( KQB.NodeDescriptions[parenttype].InheritAllowedChildren == true )
		return KQB.ValidChild(parent.Parent, child, parent);

	// and all children
	if ( KQB.NodeDescriptions[childtype].InheritAllowedChildren == true )
	{
		for ( var i = 0; i < child.Children.length; i++ )
		{
			if ( !KQB.ValidChild(parent, child.Children[i], parent) )
				return false;
		}
		return true;
	}
	return true;
}


/**
 * CanSwitch
 * 
 * checks if two nodes are allowed to switch names
 * @param {Object} node1
 * @param {Object} node2
 */
KQB.CanSwitch = function (node1, node2)
{
	var type1 = node1.Type;
	var type2 = node2.Type;
	if ( !KQB.NodeDescriptions[type1] || !KQB.NodeDescriptions[type2] || !KQB.NodeDescriptions[type1].SwitchAllowed )
		return false;
	for ( var i = 0; i < KQB.NodeDescriptions[type1].SwitchAllowed.length; i++ )
	{
		if ( KQB.NodeDescriptions[type1].SwitchAllowed[i] == type2 )
			return true;		
	}
	return false;
}

/**
 * ShowKWQL
 * 
 * this function traverses the node tree to create a textual KWQL query from the visual query  
 */
KQB.ShowKWQL = function ()
{
	var kwql = "";	
	var node1 = KQB.Query.Children[0];
	var node2 = KQB.Query.Children[1];
	
	if (!node1)
	{
		KQB.ShowOutput("");
		return;
	}
	// usually kwql is displayed for the first element on the workspace. however since sparql
	// nodes are special (they cannot be children of anything but a rule) they need special handling here
	if (node1.Type == KQB.NodeType.SPARQL)
	{
		if (node2)
			kwql = NodeToKWQL(node2);
		kwql += " " + NodeToKWQL(node1);
	}
	else
	{
		kwql = NodeToKWQL(node1);
		for ( var i = 0; i < KQB.Query.Children.length; i++)
			if (KQB.Query.Children[i].Type == KQB.NodeType.SPARQL)
			{
				kwql += " " + NodeToKWQL(KQB.Query.Children[i]);
				break;
			}
	}
	
	KQB.ShowOutput(kwql);
	return kwql;

	/**
	 * NodeToKWQL
	 * 
	 * this function 'translates' a given node into KWQL
	 * @param {Object} node
	 */
	function NodeToKWQL (node)
	{
		if ( !node )
			return "";
		
		// nodes with empty children are ignored in the output	
		if ( ValidChildren(node) == false )
			return "";
		
		// also nodes that are in the IgnoreNodes array are ignored in the output
		// (these are nodes with warnings, put there by the GetWarning function)
		if ( KQB.IgnoreNodes.contains(node) )
			return "";
		
		// translate nodes according to their type
		switch (node.Type)
		{
			case KQB.NodeType.Keyword:
				return node.GetTextValue();
				break;
			case KQB.NodeType.Variable:
				return "$" + node.GetTextValue();
				break;
			case KQB.NodeType.Compound:
				return ParseChildren(node,true);
				break;
			case KQB.NodeType.ContentItem:
				return "ci" + ParseChildren(node, true);
				break;
			case KQB.NodeType.Link:
				return "link" + ParseChildren(node, true);
				break;
			case KQB.NodeType.Fragment:
				return "fragment" + ParseChildren(node,true);
				break;
			case KQB.NodeType.Tag:
				return "tag" + ParseChildren(node,true);
				break;
			case KQB.NodeType.AND:
				return ParseChildren(node,true,"AND");
				break;
			case KQB.NodeType.OR:
				return ParseChildren(node,true,"OR");
				break;
			case KQB.NodeType.NOT:
				return "NOT (" + ParseChildren(node,false) + ")";
				break;
			case KQB.NodeType.OPTIONAL:
				return "OPTIONAL (" + node.Children[0].GetTextValue() + ") " + NodeToKWQL(node.Children[1]);
				break;
			case KQB.NodeType.COUNT:
				return "COUNT" + ParseChildren(node,true);
				break;
			case KQB.NodeType.AVG:
				return "AVG" + ParseChildren(node,true);
				break;
			case KQB.NodeType.SOME:
				return "SOME(" + NodeToKWQL(node.Children[0]) + "," + NodeToKWQL(node.Children[1]) + ",\"" + NodeToKWQL(node.Children[2]) + "\")"; 
				break;
			case KQB.NodeType.ALL:
				return "ALL(" + NodeToKWQL(node.Children[0]) + ",\"" + NodeToKWQL(node.Children[1]) + "\")"; 
				break;
			case KQB.NodeType.Rule:
				return NodeToKWQL(node.Children[0]) + " @ " + NodeToKWQL(node.Children[1]);
				break;
			case KQB.NodeType.Author:
				return "author:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.Created:
				return "created:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.LastEdited:
				return "lastEdited:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.Text:
				return "text:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.Title:
				return "title:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.NumberEdits:
				return "numberEdits:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.Descendant:
				return "descendant:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.Child:
				return "child:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.Target:
				return "target:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.Origin:
				return "origin:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.AnchorText:
				return "anchorText:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.Name:
				return "name:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.Disagree:
				return "disagree:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.Agree:
				return "agree:" + ParseChildren(node,false);
				break;
			case KQB.NodeType.SPARQL:
				return "SPARQL:(" + node.GetTextValue() + ")";
				break;
			case KQB.NodeType.Binding:
				return NodeToKWQL(node.Children[1]) + " -> " + NodeToKWQL(node.Children[0]); 
				break;
			case KQB.NodeType.TagGrouping:
				return ParseChildren(node,false, ",");
				break;
			case KQB.NodeType.TagCharacterization:
				return NodeToKWQL(node.Children[0]) + ":" + NodeToKWQL(node.Children[1]); 
				break;
			default: return "";
		}
		
		/**
		 * ParseChildren
		 * 
		 * this function parses the childen of the node currently being translated
		 * @param {Object} node				// the node whose children to parse
		 * @param {Object} parentheses		// true if the result shoudl be wrapped in parentheses
		 * @param {Object} delim			// when given, the strings for each child will be delimited by this string
		 */
		function ParseChildren(node, parentheses, delim)
		{
			if (!delim)	// when  there is no delimiter given, we delimit the different children by whitespace
				var delim = " ";
			else
				delim = " " + delim + " ";
			
			var allres = true;
			for (var i = 0; i < node.GetNumChildren(); i++)
				if (node.Children[i].Category != KQB.NodeCategories.Resource )
					allres = false;
		//	if (allres == true)
		//			parentheses = false;
					
			if ( (node.Type == KQB.NodeType.OR) || (node.Type == KQB.NodeType.AND) )
			{
				if (node.GetNumChildren() == 1)
					parentheses = false;
				if (allres == true)
					parentheses = false;
				if (node.Parent.Type == KQB.NodeType.Root)
					parentheses = false;
			}
			
			var res = "";
			var hasp = false;
			switch (node.GetNumChildren())
			{
				case 0: res = "";
						break;
				case 1: res = NodeToKWQL(node.Children[0]);
						if (parentheses)
							res = "(" + res + ")";						
						break;
				default:
					res = NodeToKWQL(node.Children[0]);
					for (var i = 1; i < node.GetNumChildren(); i++) 
					{
						var x = NodeToKWQL(node.Children[i]);
						if ( x != "" )
							res += delim + x;
					}
					if (parentheses)
						res = "("+res+")";

					break;
			}

			if (res.length>3)
				while ( (res.charAt(0) == "(") && (res.charAt(1) == "(")
				       && (res.charAt(res.length-1) == ")") && (res.charAt(res.length-2) == ")") )
					res = res.substring(1,res.length-1);
			return res;
		}
		
		/**
		 * ValidChildren
		 * 
		 * checks if all children of the given node are non-empty
		 * @param {Object} node
		 */
		function ValidChildren(node)
		{
			if ( (node.GetNumChildren() == 0) && node.isLeaf )
				return true;
			for ( var i = 0; i < node.GetNumChildren(); i++ )
			{
				if ( ValidChildren(node.Children[i]) == true )
					return true;
			}
			return false;
		}
	}
}

/**
 * GetError
 * 
 * checks the given node for errors
 * returns an {error: , hint: , tooltip: } struct if one is found
 * @param {Object} node
 */
KQB.GetError = function (node)
{
	if (!node)
		return null;
	if ( node == KQB.Query )
		return null;
	if (!node.Children)
		return null;
	
	if (!KQB.ValidChild(node.Parent, node))
	{ 
		var ret = {
			error: KQBStrings.Errors.InvalidChild,
			hint: KQBStrings.Hints.InvalidChild,
			tooltip: node.Name + KQBStrings.ToolTips.InvalidChild + node.Parent.Name
		}; 
		return ret;
	}

	switch ( node.Type )
	{
		case KQB.NodeType.Variable:
			// check that variables are typed correctly and do not contain spaces
			var pattern = new RegExp("^[a-zA-Z0-9_]+$");
			var str = node.GetTextValue();
			var rc = RuleChild(node);
			// and make sure it isn't empty
			if (str=="")
				var ret = { error: KQBStrings.Errors.EmptyText, hint: KQBStrings.Hints.EmptyText, tooltip: KQBStrings.ToolTips.ErrorEmptyText };
			else if (!pattern.test(str)) 
				var ret = {
					error: "\"" + str + "\"" + KQBStrings.Errors.InvalidVariableName,
					hint: KQBStrings.Hints.InvalidVariableName,
					tooltip: "\"" + str + "\"" + KQBStrings.Errors.InvalidVariableName
				};
			else if (rc)
			{
				// if this variable node is inside the construct part of a rule, check if a variable
				// of the same name is defined in the rule's query part
				if ( (rc.rule.GetNumChildren() == 2) && (rc.index == 0) )
				{
					if (!FindVar(str,rc.rule.Children[1]))
						var ret = {
							error: "\"" + str + "\"" + KQBStrings.Errors.UndefVar,
							hint: KQBStrings.Hints.UndefVar,
							tooltip: KQBStrings.ToolTips.ErrorUndefVar
						}
				}
			}
			break;
		case KQB.NodeType.Keyword:
			var str = node.GetTextValue();
			// check that keywords are not empty
			if (str=="")
			{
				var ret = { error: KQBStrings.Errors.EmptyText, hint: KQBStrings.Hints.EmptyText, tooltip: KQBStrings.ToolTips.ErrorEmptyText };
				break;
			}
			// check for pattern
			var pattern = new  RegExp ("^[a-zA-Z0-9_*<>,.|!#+-\\\\@]+$");
			var pattern2 = new RegExp ("^\"[a-zA-Z0-9_*<>,.|!#+-\\\\@ ]+\"$");
			
			if (!pattern.test(str) && !pattern2.test(str) )				
				var ret = {
					error: "\"" + str + "\"" + KQBStrings.Errors.InvalidKeywordName,
					hint: KQBStrings.Hints.InvalidKeywordName,
					tooltip: "\"" + str + "\"" + KQBStrings.Errors.InvalidKeywordName
				}; 
			if (node.Parent)
				if ( node.Parent.Type == KQB.NodeType.SOME )
					if (node.Parent.Children[0]==node)
				{
					// if the current keyword is the 'num' child of a some node, make sure it contains
					// an integer
					var pattern = new RegExp ("^[0-9]+$");
					if (!pattern.test(str))
					var ret = { error: "\"" + str + "\"" + KQBStrings.Errors.NonNumber,
						hint: KQBStrings.Hints.NonNumber,
						tooltip: "\"" + str + "\"" + KQBStrings.Errors.NonNumber
						}
				}
		break;  
		case KQB.NodeType.OPTIONAL:
			// make sure thet no 'OPTIONAL' node is placed in the construct part of a rule
			var rule = RuleChild(node);
			if (rule)
				if ( (rule.rule.GetNumChildren() == 2) && (rule.index == 0) )
					var ret = 
					{
						error: KQBStrings.Errors.MisplacedOptional,
						hint: KQBStrings.Hints.MisplacedOptional,
						tooltip: KQBStrings.ToolTips.ErrorMisplacedOptional
					};
		break;
		case KQB.NodeType.ALL:
			// make sure no ALL node is placed in the query part of a rule
			var rule = RuleChild(node);
			if (rule)
				if ( (rule.rule.GetNumChildren() == 2) && (rule.index == 1) )
					var ret = 
					{
						error: KQBStrings.Errors.MisplacedAll,
						hint: KQBStrings.Hints.MisplacedAll,
						tooltip: KQBStrings.ToolTips.ErrorMisplacedAll
					};
		break;
		case KQB.NodeType.SOME:		
			// make sure no 'SOME' node is placed in the query part of a rule
			var rule = RuleChild(node);
			if (rule)
				if ( (rule.rule.GetNumChildren() == 2) && (rule.index == 1) )
					var ret = 
					{
						error: KQBStrings.Errors.MisplacedSome,
						hint: KQBStrings.Hints.MisplacedSome,
						tooltip: KQBStrings.ToolTips.ErrorMisplacedSome
					};
		break;
		case KQB.NodeType.AVG:		// again, not allowed in the query part of a rule
			var rule = RuleChild(node);
			if (rule)
				if ( (rule.rule.GetNumChildren() == 2) && (rule.index == 1) )
					var ret = 
					{
						error: KQBStrings.Errors.MisplacedAvg,
						hint: KQBStrings.Hints.MisplacedAvg,
						tooltip: KQBStrings.ToolTips.ErrorMisplacedAvg
					};
		break;
		case KQB.NodeType.COUNT:	// and again not allowed in a rule's query part
			var rule = RuleChild(node);
			if (rule)
				if ( (rule.rule.GetNumChildren() == 2) && (rule.index == 1) )
					var ret = 
					{
						error: KQBStrings.Errors.MisplacedCount,
						hint: KQBStrings.Hints.MisplacedCount,
						tooltip: KQBStrings.ToolTips.ErrorMisplacedCount
					};
		break;
		case KQB.NodeType.TagCharacterization:		// tag characterizations are invalid with an incorrect number of children
			if (node.GetNumChildren() != 2) 
			{
				var ret = 
				{
					error: KQBStrings.Errors.NeedTwoChildren,
					hint: KQBStrings.Hints.NeedTwoChildren,
					tooltip: KQBStrings.ToolTips.NeedTwoChildren
				};
			}
			break;
	
	}
	
	if ( (node.Category == KQB.NodeCategories.Qualifier) && (node.isSingular == true))
	{
		if ( (node.Parent.Category == KQB.NodeCategories.Resource) || (node.Parent.Type == KQB.NodeType.AND))
		{
			for (var i = 0; i < node.Parent.GetNumChildren(); i++)
			{
				if ( node.Type == node.Parent.Children[i].Type )
					if (node.ID != node.Parent.Children[i].ID)
						var ret =
						{
							error: KQBStrings.Errors.SingularChild,
							hint: KQBStrings.Hints.SingularChild,
							tooltip: KQBStrings.ToolTips.SingularChild
						};
			}
		}
	}
	
	return ret;
	
	/**
	 * RuleChild
	 * check if the given node is contained in a rule. returns the rule node and this node's index in the rule
	 * @param {Object} node
	 */
	function RuleChild (node)
	{
		while (node != KQB.Query)
		{
			if ( node.Parent.Type == KQB.NodeType.Rule )
			{
				if (node == node.Parent.Children[0]) 
					return { rule: node.Parent, index: 0 };
				else 
					return { rule: node.Parent, index: 1 };
			}
			node = node.Parent;
		}
		return null;
	}
	
	/**
	 * check if a variable of the given name exists in the given node or one of it's descendants
	 * @param {Object} name
	 * @param {Object} node
	 */
	function FindVar (name, node)
	{
		if (node.Type == KQB.NodeType.Variable)
			if (node.GetTextValue() == name)
				return true;
		for (var i = 0; i < node.GetNumChildren(); i++)
		{
			if (FindVar (name, node.Children[i]) == true)
				return true;
		}
		return false;
	}
}

/**
 * GetWarning
 * 
 * this function checks the given node if there is any warning associated with it
 * if true, it returs the warning and a hint
 * if the warning is severe enough to make this node and it's children completely invalid,
 * the node is put in the KQB.IgnoreNodes array, so that this node will be ignored when generating kwql
 * @param {Object} node
 */
KQB.GetWarning = function (node)
{
	if (!node)
		return null;
	if ( node == KQB.Query )
		return null;
		
	switch (node.Type)
	{
		case KQB.NodeType.AND:			// AND nodes have a warning when they have only one child
			if (node.GetNumChildren() == 1) 
				var ret = 
				{
					warning: KQBStrings.Warnings.ANDWithOneChild,
					hint: KQBStrings.Hints.ANDWithOneChild
				};
		break;
		case KQB.NodeType.OR:			// OR nodes have a warning when they have only one child
			if (node.GetNumChildren() == 1) 
				var ret = 
				{
					warning: KQBStrings.Warnings.ORWithOneChild,
					hint: KQBStrings.Hints.ORWithOneChild
				};
		break;
		case KQB.NodeType.Variable:		// variables can not occur on their own or as a child of a resource
			if ( (node.Parent.Category != KQB.NodeCategories.Qualifier) && (node.Parent.Category != KQB.NodeCategories.Operator) && (node.Parent.Category != KQB.NodeCategories.Other) ) 
			{
				var ret =
				{
					warning: KQBStrings.Warnings.VarWithoutCorrectParent,
					hint: KQBStrings.Hints.VarWithoutCorrectParent
				};
				KQB.IgnoreNodes.push(node);
			}
			break;
		case KQB.NodeType.Rule:			// RULE nodes have a warning when they only have one child
			if (node.GetNumChildren() == 1) 
			{
				var ret = 
				{
					warning: KQBStrings.Warnings.RuleWithOneChild,
					hint: KQBStrings.Hints.RuleWithOneChild
				};
				KQB.IgnoreNodes.push(node);	// rules with only one child generate to useful kwql, so ignore them
			}
			if ( node.GetNumChildren() == 2)	// ignore rules that contain empty children
				if ( !ValidChildren(node.Children[0]) || !ValidChildren(node.Children[1]) )
					KQB.IgnoreNodes.push(node);
		break;
		case KQB.NodeType.OPTIONAL:	// OPTIONAL nodes have a warning when they are not placed inside a rule
			if (RuleChild(node) == -1) 
			{
				var ret = 
				{
					warning: KQBStrings.Warnings.UnplacedOptional,
					hint: KQBStrings.Hints.UnplacedOptional
				};
				KQB.IgnoreNodes.push(node);
			}
			else if (node.GetNumChildren() != 2)
			{
				var ret =
				{
					warning: KQBStrings.Warnings.OptionalNotEnoughChildren,
					hint: KQBStrings.Hints.OptionalNotEnoughChildren
				};
				KQB.IgnoreNodes.push(node);
			}
		break;
		case KQB.NodeType.ALL:	// warning when not placed inside a rule
			if (RuleChild(node) == -1) 
			{
				var ret = 
				{
					warning: KQBStrings.Warnings.UnplacedAll,
					hint: KQBStrings.Hints.UnplacedAll
				};
				KQB.IgnoreNodes.push(node);
			}
			else if (node.GetNumChildren()!=2)
				KQB.IgnoreNodes.push(node);
		break;
		case KQB.NodeType.SOME:	// see ALL
			if (RuleChild(node) == -1) 
			{
				var ret = 
				{
					warning: KQBStrings.Warnings.UnplacedSome,
					hint: KQBStrings.Hints.UnplacedSome
				};
				KQB.IgnoreNodes.push(node);
			}
			else if (node.GetNumChildren()!=3)
				KQB.IgnoreNodes.push(node);
		break;
		case KQB.NodeType.AVG:	// see ALL
			if (RuleChild(node) == -1) 
			{
				var ret = 
				{
					warning: KQBStrings.Warnings.UnplacedAvg,
					hint: KQBStrings.Hints.UnplacedAvg
				};
				KQB.IgnoreNodes.push(node);
			}
		break;
		case KQB.NodeType.COUNT:	// see ALL
			if (RuleChild(node) == -1) 
			{
				var ret = 
				{
					warning: KQBStrings.Warnings.UnplacedCount,
					hint: KQBStrings.Hints.UnplacedCount
				};
				KQB.IgnoreNodes.push(node);
			}
		break;

		case KQB.NodeType.TagCharacterization:
		
			// tag characterization is special: usually when a node has two children, one of which is empty,
			// the node with the non-empty child is in the kwql output, but since TagCharacterization's first child is
			// it's name and no real child, the node is invalid if the second child is empty. this has to reflect
			// to the whole tag that contains this characterization
			if (node.GetNumChildren() == 2) 
				if (!ValidChildren(node.Children[0]) || !ValidChildren(node.Children[1])) 
				{
					KQB.IgnoreNodes.push(node);
					var x = node.Parent;
					while (x!=KQB.Query)
					{
						if (x.Type == KQB.NodeType.Name) 
						{
							KQB.IgnoreNodes.push(x);
							if ( x.Parent && (x.Parent.Type == KQB.NodeType.Tag) && (x.Parent.GetNumChildren()==1))
								KQB.IgnoreNodes.push(x.Parent);
							break;
						}
						x = x.Parent;
					}
				}
		break;

	}
	
	// generate warnings for empty nodes
	if ( ( node.GetNumChildren() == 0 ) && (node.isLeaf == false ) )
	{
		var ret = 	{
						warning: KQBStrings.Warnings.EmptyLeaf,
						hint: KQBStrings.Hints.EmptyLeaf
					};
	}
	
	
	return ret;
	
	/**
	 * check if a node is child or descendant of a rule
	 * @param {Object} node
	 */
	function RuleChild (node)
	{
		while (node != KQB.Query)
		{
			if ( node.Parent.Type == KQB.NodeType.Rule )
			{
				if (node == node.Parent.Children[0])
					return 0;
				else return 1;
			}
			node = node.Parent;
		}
		return -1;
	}
	
	/**
	 * check if the given node contains only valid(non-empty) children
	 * @param {Object} node
	 */
		function ValidChildren(node)
		{
			if ( (node.GetNumChildren() == 0) && node.isLeaf )
				return true;
			for ( var i = 0; i < node.GetNumChildren(); i++ )
			{
				if ( ValidChildren(node.Children[i]) == true )
					return true;
			}
			return false;
		}
}

/**
 * UpdateOutput
 * 
 * this function parses the whole visual query, generates warnings and errors and displays them and
 * the KWQL output when possible
 */
KQB.UpdateOutput = function (IgnoreUndo)
{
	if (!IgnoreUndo)
		KQB.UpdateUndoBuffer();
	KQBDragDrop.Rects.ReBuild();
	// update errors and warnings
	var error = UpdateErrors();
	var warning = UpdateWarnings();
	
	// errors have precedence, so remove the warning indicators on error nodes to make sure
	// we have no nodes that have both warning and error css classes
	for (var i = 0; i < KQB.ErrorNodes.length; i++) 
	{
		KQBUtilities.RemoveHTMLClass(KQB.ErrorNodes[i].HTMLNode.firstChild, "KQBLabelWarning");
		KQBUtilities.RemoveHTMLClass(KQB.ErrorNodes[i].HTMLNode, "KQBNodeWarning");
		KQB.ErrorNodes[i].WarningTooltip = null;
	}
	
	if (error) 
	{
		KQB.ShowError(error.error);
		KQB.ShowHint(error.hint);
	}
	else 
	{
		if ( warning )
			KQB.ShowHint(warning.hint);
		else 
			UpdateHints();
		KQB.ShowKWQL();		
	
	}
	
	/**
	 * UpdateErrors
	 * 
	 * this function checks the visual query for errors
	 * all nodes containing errors are added to the KQL.ErrorNodes array and their HTML nodes receive
	 * KQBNodeError/KQBLabelError css classes
	 */
	function UpdateErrors ()
	{
		// first clear old errors
		for (var i = 0; i < KQB.ErrorNodes.length; i++) 
		{
			KQBUtilities.RemoveHTMLClass(KQB.ErrorNodes[i].HTMLNode, "KQBNodeError");
			KQBUtilities.RemoveHTMLClass(KQB.ErrorNodes[i].HTMLNode.firstChild, "KQBLabelError");
			KQB.ErrorNodes[i].ErrorTooltip = null;
		}
		KQB.ErrorNodes.splice(0,KQB.ErrorNodes.length);

		// now check for error starting at the 'root' node
		var result = CheckForErrors(KQB.Query);
			
		// add css classes to indicate errors to all nodes containing errors
		for (var i = 0; i < KQB.ErrorNodes.length; i++) 
		{
			KQBUtilities.AddHTMLClass(KQB.ErrorNodes[i].HTMLNode, "KQBNodeError");	
			KQBUtilities.AddHTMLClass(KQB.ErrorNodes[i].HTMLNode.firstChild, "KQBLabelError");			
		}
		return result;
		
		/**
		 * CheckForErrors
		 * 
		 * this function checks a kqb query for errors, starting at the given node and working it's way
		 * through all of it's descendants
		 * 
		 * @param {Object} node
		 */
		function CheckForErrors(node)
		{
			if (!node)
				return null;
			// first get the error for the current node
			var error = KQB.GetError(node); 
			if (error) 
			{
				// if there is one, push the node on the ErrorNodes array, add a tooltip indicating the error
				// and mark all it's predecessors
				var n = node;
				KQB.ErrorNodes.push(n); 
				n.ErrorTooltip = error.tooltip;
				n = n.Parent;
				while (n != KQB.Query) 
				{
					if ( !KQB.ErrorNodes.contains(n))
					{
						KQB.ErrorNodes.push(n); 
						n.ErrorTooltip = KQBStrings.Errors.ErrorInChild;
					}
					n = n.Parent;	
				}
			}
			// check all children of the node for errors
		//	if (!error)
		//	{
				for ( var i = 0; i < node.Children.length; i++ )
				{
					var childerr = CheckForErrors(node.Children[i]);
						if (childerr)
							if (!error)
								error = childerr;
				}
		//	}
			return error;
		}
	}
	
	/**
	 * UpdateWarnings
	 * 
	 * checks all nodes for warnings, adds them to the WarningNodes array and colors their html nodes
	 */
	function UpdateWarnings ()
	{
		// first clear old warnings
		for (var i = 0; i < KQB.WarningNodes.length; i++) 
		{
			KQBUtilities.RemoveHTMLClass(KQB.WarningNodes[i].HTMLNode, "KQBNodeWarning");
			KQBUtilities.RemoveHTMLClass(KQB.WarningNodes[i].HTMLNode.firstChild, "KQBLabelWarning");	
			KQB.WarningNodes[i].WarningTooltip = null;
		}
		KQB.WarningNodes.splice(0,KQB.WarningNodes.length);
		KQB.IgnoreNodes.splice(0,KQB.IgnoreNodes.length);

		var result = CheckForWarnings(KQB.Query);
		
		// color all nodes containing warnings by assigning additional css classes to them			
		for (var i = 0; i < KQB.WarningNodes.length; i++) 
		{
		//	KQBUtilities.AddHTMLClass(KQB.WarningNodes[i].HTMLNode, "KQBNodeWarning");
			KQBUtilities.AddHTMLClass(KQB.WarningNodes[i].HTMLNode.firstChild, "KQBLabelWarning");
		}
		return result;
		
		/**
		 * CheckForWarnings
		 * checks the given node and it's children for warnings
		 * @param {Object} node
		 */
		function CheckForWarnings(node)
		{
			if (!node)
				return null;
				
			var warning = KQB.GetWarning(node);
			if (warning) 
			{
				// if the node contains a warning, put it on the WarningNodes array,
				// display warning tooltip and mark all predecessors
				var n = node;
				KQB.WarningNodes.push(n);
				n.WarningTooltip = warning.warning;
				n = n.Parent;
				while (n != KQB.Query) 
				{
					KQB.WarningNodes.push(n);
					n.WarningTooltip = KQBStrings.Warnings.WarningInChild;
					n = n.Parent;	
				}
			}
			// check all children for warnings
			for ( var i = 0; i < node.Children.length; i++ )
			{
				var childwarning = CheckForWarnings(node.Children[i]);
					if (childwarning)
						if (!warning)
							warning = childwarning;
			}
			return warning;
		}
	}
	
	/**
	 * UpdateHints
	 * 
	 * function to display a hint if there is no error/warning hint
	 */
	function UpdateHints()
	{
		var hint = KQBStrings.Hints.Start;
				
		KQB.ShowHint(hint);
	}
}

/**
 * ParserError
 *
 * report an error from the parser
 */
KQB.ParserError = function (text)
{
	KQB.ParsErr = true;
	var output = document.getElementById("parsererrorpane");
	output.innerHTML = text;
}

/**
 * Parse
 *
 * this function translates textual KWQL code into a KQB query tree
 *
 * the actual parsing is done via the ANTLR-generated KWQLLexer.js and KWQLParser.js files
 */
KQB.Parse = function ()
{
	KQB.ParsErr = false;
	document.getElementById("parsererrorpane").innerHTML = "";
	
	KWQLLexer.prototype.emitErrorMessage = KQB.ParserError;
	KWQLParser.prototype.emitErrorMessage = KQB.ParserError;
	
	var inputstring = KQB.Outputpane.value;
	

	
	inputstring = inputstring.replace(/\r\n?/g,"\n");
	var input = new org.antlr.runtime.ANTLRStringStream(inputstring);
	var lexer = new KWQLLexer(input);
	var tokens = new org.antlr.runtime.CommonTokenStream(lexer);
	var parser = new KWQLParser(tokens);
	var r = parser.start();
	
	if (!KQB.ParsErr)
	{
		KQB.Clear();
		KQB.ParseTree(r.getTree());
	}
	KQB.UpdateOutput();
}

/**
 * ParseText
 *
 * parses the textual string given as input
 */
KQB.ParseText = function (input)
{
//	KQB.Clear();
	KQB.Outputpane.value = input;
	KQB.Parse();
}

/**
 * ParseTree
 *
 * this function converts the AST generated by the ANTLR parser into a KQB tree
 */
KQB.ParseTree = function (tree)
{
	if (tree)
	{
		var result = ParseNode(tree);
		if (result)
			result.DropOnWorkspace();
	}
	
	/**
	 * ParseNode
	 *
	 * parses a single node of the AST
	 *
	 * NOTE: some of the code in here is propably superfluous, since the grammar changes often
	 * and some special cases in the code here are propably cleaned from the grammar meanwhile
	 */
	function ParseNode (node, parentnode)
	{
		var newnode = null;
		var parent = node;
		
		// OPTIONAL nodes have the string form OPTIONAL:d  where d is the default value
	/*	if (node.toString().indexOf("OPTIONAL") == 0)
		{
			if (node.getChildCount()!=2)
			{
				error ("optional with invalid num of children");
				return null;
			}
			var def = node.toString().substring(9, node.toString().length);
			var n = KQB.CreateNode(KQB.NodeType.OPTIONAL);
			n.Children[0].SetTextValue(def);
			n.AddChild(ParseNode(node.getChild(0),n));
			n.AddChild(ParseNode(node.getChild(1),n));
			return n;
			return null;
		}
	*/	
		switch (node.toString())
		{
			case "RULE":
				if (firstChildEmpty(node))
					return ParseNode(node.getChild(1), node);
				newnode = KQB.CreateNode(KQB.NodeType.Rule);
				newnode.AddChild(ParseNode(node.getChild(0),newnode));
				newnode.AddChild(ParseNode(node.getChild(1),newnode));		 
				break;
			case "HEAD":
				if (node.getChildCount() == 1 )
					return ParseNode(node.getChild(0),node);
				newnode = KQB.CreateNode(KQB.NodeType.Compound);
				for ( var i = 0; i < node.getChildCount(); i++ )
					newnode.AddChild (ParseNode(node.getChild(i),newnode));
				break;
			case "BODY":
				if (firstChildEmpty(node))
					return ParseNode(node.getChild(1),node);
				return ParseNode(node.getChild(0),node);
				break;
			case "OPTIONAL":
				var n = KQB.CreateNode(KQB.NodeType.OPTIONAL);
				n.Children[0].SetTextValue(node.getChild(0).toString());
				n.AddChild(ParseNode(node.getChild(1),n));
			return n;
				break;
			case "NOT":
				newnode = KQB.CreateNode(KQB.NodeType.NOT);
				newnode.AddChild(ParseNode(node.getChild(0),newnode));
				break;
			case "STRING":
				var child = node.getChild(0);
				if (( child == "STRING") || (child=="OR") || (child=="AND") || (child=="RESOURCE") )
				    return ParseNode(child,node);
				if (child == "NOT")
				{
					var n = KQB.CreateNode(KQB.NodeType.NOT);
					node.deleteChild(0);
					n.AddChild(ParseNode(node,n));
					return n;
				}
				
				newnode = KQB.CreateNode(KQB.NodeType.Keyword);
				newnode.SetTextValue(node.getChild(0).toString());
				break;
			case "INTEGER":
				newnode = KQB.CreateNode(KQB.NodeType.Keyword);
				newnode.SetTextValue(node.getChild(0).toString());
				break;
			case "UR":
				newnode = KQB.CreateNode(KQB.NodeType.Keyword);
				newnode.SetTextValue(node.getChild(0).toString());
				break;
			case "DATE":
				newnode = KQB.CreateNode(KQB.NodeType.Keyword);
				newnode.SetTextValue(node.getChild(0).toString());
				break;
			case "VAR":
				newnode = KQB.CreateNode(KQB.NodeType.Variable);
				var text = node.getChild(0).toString();
				newnode.SetTextValue(text);
				break;
			case "QUALIFIER":
				if (node.getChild(0) == "NOT")
				{
					var nnode = KQB.CreateNode(KQB.NodeType.NOT);
					node.deleteChild(0);
					nnode.AddChild(ParseNode(node,nnode));
					return nnode;	       
				}
				if (node.getChild(0) == "QUALIFIER")
					return ParseNode(node.getChild(0),parentnode);
				if (firstChildEmpty(node))
					return ParseNode(node.getChild(1),parentnode);
				if (node.getChild(0) == "LABEL")
				{
					var name = node.getChild(0).getChild(0).toString();
					var n = CreateNodeFromName(name);
					if (n)
					{ 
						if (node.getChildCount()==2)
							n.AddChild(ParseNode(node.getChild(1),n));
						else if (node.getChildCount()==3)
						{
							var bin = KQB.CreateNode(KQB.NodeType.Binding,true);
							bin.AddChild(ParseNode(node.getChild(1),bin));
							bin.AddChild(ParseNode(node.getChild(2),bin));
							n.AddChild(bin);
						}
						return n;
					}
					else error ("unknown qualifier: " + name); break;
				}
				return ParseNode(node.getChild(0),parentnode);  // need this in case of something like NOT(text:b AND c) ,
					// which will be parsed as QUALIFIER(NOT, AND(QUALIFIER(..),c))  currently 
				break;
	
			case "OR":
				if (parentnode.Type == KQB.NodeType.OR)
				{
					var pa = true;
					newnode = parentnode;
				}
				else
					newnode = KQB.CreateNode(KQB.NodeType.OR);
				if (node.getChildCount() == 2)
				{
					newnode.AddChild(ParseNode(node.getChild(0),newnode));
					newnode.AddChild(ParseNode(node.getChild(1),newnode));
					if (pa)
						return null;
					return newnode;
				}
				index = 0;
				while (index < node.getChildCount())
				{
					if (node.getChild(index)== "NOT")
					{
						var nnode = KQB.CreateNode(KQB.NodeType.NOT);
						nnode.AddChild(ParseNode(node.getChild(index+1),nnode));
						newnode.AddChild(nnode);
						index += 2;
					}
					else
					{
						newnode.AddChild(ParseNode(node.getChild(index),newnode));
						index += 1;
					}
				}
				if (pa)
					return null;
				
				return newnode;
				break;
			case "AND":
				if (parentnode.Type == KQB.NodeType.AND)
				{
					var pa = true;
					newnode = parentnode;
				}
				else
					newnode = KQB.CreateNode(KQB.NodeType.AND);
					
				if (node.getChildCount() == 2)
				{
					newnode.AddChild(ParseNode(node.getChild(0),newnode));
					newnode.AddChild(ParseNode(node.getChild(1),newnode));
					
					if (pa)
						return null;
					
					return newnode;
				}
				index = 0;
				while (index < node.getChildCount())
				{
					if (node.getChild(index)== "NOT")
					{
						var nnode = KQB.CreateNode(KQB.NodeType.NOT);
						nnode.AddChild(ParseNode(node.getChild(index+1),nnode));
						newnode.AddChild(nnode);
						index += 2;
					}
					else
					{
						newnode.AddChild(ParseNode(node.getChild(index),newnode));
						index += 1;
					}
				}
				if (pa)
					return null;
				return newnode;
				break;
			case "COUNT":
				newnode = KQB.CreateNode(KQB.NodeType.COUNT, true);
				var varnode = KQB.CreateNode(KQB.NodeType.Variable);
				varnode.SetTextValue(node.getChild(0).toString());
				newnode.AddChild(varnode);
				return newnode;
				break;
			case "AVG":
				newnode = KQB.CreateNode(KQB.NodeType.AVG, true);
				var varnode = KQB.CreateNode(KQB.NodeType.Variable);
				varnode.SetTextValue(node.getChild(0).toString());
				newnode.AddChild(varnode);
				return newnode;
				break;
			case "ALL":
				newnode = KQB.CreateNode(KQB.NodeType.ALL);
				newnode.Children[0].SetTextValue(node.getChild(0).getChild(0));
				newnode.Children[1].SetTextValue(node.getChild(1).getChild(0));
				return newnode;
				break;
			case "SOME":
				newnode = KQB.CreateNode(KQB.NodeType.SOME);
				newnode.Children[0].SetTextValue(node.getChild(0).getChild(0));
				newnode.Children[1].SetTextValue(node.getChild(1).getChild(0));
				newnode.Children[2].SetTextValue(node.getChild(2).getChild(0));
				return newnode;
				break;
			case "RESOURCE":
				var type = "";
				
				if (node.getChild(0) == "TYPE")
					if (node.getChild(0).getChild(0))
						type = node.getChild(0).getChild(0).toString();
				if ( (node.getChild(0) == "ci") || (node.getChild(0) == "tag") || (node.getChild(0) == "link")
				    || (node.getChild(0) == "fragment") )
					type = node.getChild(0).toString();
				else if (firstChildEmpty(node))
					return ParseNode(node.getChild(1), node);
				switch (type)
				{
					case "ci": newnode = KQB.CreateNode(KQB.NodeType.ContentItem); break;
					case "link": newnode = KQB.CreateNode(KQB.NodeType.Link); break;
					case "tag": newnode = KQB.CreateNode(KQB.NodeType.Tag); break;
					case "fragment": newnode = KQB.CreateNode(KQB.NodeType.Fragment); break;
					default: newnode = null;
				} 
				if (newnode == null)
				{
					if (type=="")
					{
						newnode = KQB.CreateNode(KQB.NodeType.ContentItem); break;
					}
					else
					{
						error ("unknown resource: " + type);
						return null;
					}
				}
				if (node.getChildCount() == 2 )
				{
					if (node.getChild(1).toString()=="AND")
					{
						var nn = ParseNode(node.getChild(1),newnode); 
						var count = nn.Children.length;
						for (var i = 0; i < count; i++)		// if used in AddChild, a node will be removed from it's old parent's Children array,
							newnode.AddChild(nn.Children[0]);  // so repeatedly add index 0 of the children array
						
						return newnode;
					}
					else if (node.getChild(1).toString()=="NOT")
					{
						newnode.AddChild(ParseNode(node.getChild(1),newnode));
						return newnode;
					}
					else if (node.getChild(1).toString()=="OR")
					{
						newnode.AddChild(ParseNode(node.getChild(1),newnode));
						return newnode;
					}
					else if (node.getChild(1).toString()=="RESOURCE")
					{
						newnode.AddChild(ParseNode(node.getChild(1),newnode));
						return newnode;
					}
					else if (node.getChild(1).toString()=="QUALIFIER")
						if (node.getChild(1).getChild(1))
							if ((node.getChild(1).getChild(1).toString()=="AND") &&
							 (node.getChild(1).getChild(0).getChildCount()==0))
					{
						
						var nn = ParseNode(node.getChild(1).getChild(1),newnode); 
						var count = nn.Children.length;
						for (var i = 0; i < count; i++)		// if used in AddChild, a node will be removed from it's old parent's Children array,
							newnode.AddChild(nn.Children[0]);  // so repeatedly add index 0 of the children array
						
						return newnode;
					}
					else
						newnode.AddChild(ParseNode(node.getChild(1),newnode));
					return newnode;
				}
				var lastnode = newnode;
				var index = 1;
				while (node.getChild(index) == "NOT")
				{
					var nnode = KQB.CreateNode(KQB.NodeType.NOT);
					lastnode.AddChild(nnode);
					lastnode = nnode;
					index++;
				}
				lastnode.AddChild(ParseNode(node.getChild(index),lastnode));
				return newnode;
				
				break;
			case "FUNC":
				return ParseNode(node.getChild(0),node);
				break;
		
			default: newnode = KQB.CreateNode(KQB.NodeType.Keyword);
				newnode.SetTextValue(node.toString());
		}
		return newnode;
	
		function error(str)
		{
			KQB.ParserError(str);
		}
		function isEmpty(node)
		{
			return node.getChildCount() == 0;
		}
		function firstChildEmpty(node)
		{
			return node.getChild(0).getChildCount() == 0;
		}
	}
	
	/**
	 * CreateNodeFromName
	 *
	 * creates a qualifier node given it's name
	 */
	function CreateNodeFromName (name)
	{
		switch (name)
		{
			case "author": return KQB.CreateNode(KQB.NodeType.Author, true); break;
			case "created": return KQB.CreateNode(KQB.NodeType.Created, true); break;
			case "lastEdited": return KQB.CreateNode(KQB.NodeType.LastEdited, true); break;
			case "title": return KQB.CreateNode(KQB.NodeType.Title, true); break;
			case "text": return KQB.CreateNode(KQB.NodeType.Text, true); break;
			case "numberEdits": return KQB.CreateNode(KQB.NodeType.NumberEdits, true); break;
			case "descendant": return KQB.CreateNode(KQB.NodeType.Descendant, true); break;
			case "child": return KQB.CreateNode(KQB.NodeType.Child, true); break;
			case "URI": return KQB.CreateNode(KQB.NodeType.URI, true); break;
			case "disagree": return KQB.CreateNode(KQB.NodeType.Disagree, true); break;
			case "agree": return KQB.CreateNode(KQB.NodeType.Agree, true); break;
			case "target": return KQB.CreateNode(KQB.NodeType.Target, true); break;
			case "origin": return KQB.CreateNode(KQB.NodeType.Origin, true); break;
			case "anchorText": return KQB.CreateNode(KQB.NodeType.AnchorText, true); break;
			case "name": return KQB.CreateNode(KQB.NodeType.Name, true); break;
			
		}
		return null;
	}
}

/**
 * LoadSavedQuery
 *
 * loads a saved query from the saved queries listbox
 */

KQB.LoadSavedQuery = function ()
{
	var s = document.getElementById("KQBSavedQueriesBox");
         var index = s.selectedIndex;
         if (index<0)
         {
             alert (KQBStrings.KiWiIntegration.NoQuery);
             return;
         }
	var item = s.options[index];
	var value = item.value;
	if (value && value != "" && value != "error")
	{
		KQB.Outputpane.value = value;
		KQB.Parse();
	}
	else
		alert (KQBStrings.KiWiIntegration.NoValidQuery);
 
}

/**
 * ShowSavedQueries
 *
 * queries KiWi via seam for saved queries and puts them into the savedqueries listbox
 */
KQB.ShowSavedQueries = function ()
{ 
	var s = document.getElementById("KQBSavedQueriesBox");
	for (var i = 0; i < s.options.length;i++)
		s.options[i] = null;
		
		
	if (typeof(Seam)=="undefined")
	{
		 s.options[0] = new Option (KQBStrings.KiWiIntegration.NoConnShort, "error");
		return;
	}

	var callback = function(result) 
	{ 
		if (result=="error")
			alert(result);
		else if (result == "notloggedin")
		{
			s.options[0] = new Option (KQBStrings.KiWiIntegration.NoLogin, "error");
			return; 
		}
		else 
			updatelistbox(result);
	};

	var seam = Seam.Component.getInstance("KQBAction");
	if (!seam)
	{ 
		s.options[0] = new Option (KQBStrings.KiWiIntegration.NoConnShort, "error");
		return; 
	}
	else
	{	
		seam.ShowQueries("dummy", callback);
	}

	function updatelistbox(input)
	{ 
		var s = document.getElementById("KQBSavedQueriesBox");
		for (var i = 0; i < s.options.length;i++)
			s.options[i] = null;
		if (input==null)
			return;
		if (input=="")
			return;
		var elems = input.split(String.fromCharCode(191));
		var j = 0;
		for ( var i = 0; i < elems.length/2; i++)
		{
			j = i*2;
			if (elems[j] != "")
				s.options[i] = new Option(elems[j], elems[j+1]);
		}
	}
}

/**
 * SaveCurrentQuery
 *
 * saves the current query on KiWi
 */
KQB.SaveCurrentQuery = function ()
{
	if (KQB.ErrorNodes.length>0)
	{
		alert (KQBStrings.KiWiIntegration.ErrorQuery);
		return;
	}
        if (typeof(Seam)=="undefined")
        {
        	alert(KQBStrings.KiWiIntegration.NoConn);
        	return;
        }
	var name = prompt(KQBStrings.KiWiIntegration.EnterName);
	if (name == null) return;
	if (name == "") return;
	var s = document.getElementById("KQBSavedQueriesBox");
	var exists = false;
	for (var i = 0; i < s.options.length; i++)
	{
		if (s.options[i].text == name)
			exists = true;
	}
	var overwrite = false;
	if (exists == true)
	{
		overwrite = confirm (KQBStrings.KiWiIntegration.QueryExists);
		if (!overwrite)
			return;
	}
	var callback = function(result) { alert(result); };
	var value = KQB.Outputpane.value;
	var seam = Seam.Component.getInstance("KQBAction");
	if (!seam)
		alert (KQBStrings.KiWiIntegration.NoConn);
	else
		seam.SaveQuery(name, value, overwrite, callback);
	KQB.ShowSavedQueries();
	window.setTimeout(KQB.ShowSavedQueries,1000);
}

/**
 * DeleteSavedQuery
 *
 * deletes a query from the saved queries
 */
KQB.DeleteSavedQuery = function()
{
        if (typeof(Seam)=="undefined")
        {
		alert(KQBStrings.KiWiIntegration.NoConn);
		return;
        }
	var s = document.getElementById("KQBSavedQueriesBox");
         var index = s.selectedIndex;
         if (index<0)
         {
             alert ("no query selected");
             return;
         }
         var seam = Seam.Component.getInstance("KQBAction");
 	if (seam==null || seam.DeleteQuery == null)
 	{
 	 	alert (KQBStrings.KiWiIntegration.NoConn);
 	 	return;
 	} 
        var option = s.options[index];
        var callback = function(result) { alert(result); };
	   if (option.text == KQBStrings.KiWiIntegration.NoLogin)
		{
			alert (KQBStrings.KiWiIntegration.NoLogin);
			return;
		}
        if (confirm(KQBStrings.KiWiIntegration.ConfirmDelete + option.text + ": " + option.value + " ?") )
                seam.DeleteQuery(option.text, callback);
        KQB.ShowSavedQueries();
	window.setTimeout(KQB.ShowSavedQueries,1000);
}


/**
 * a simple function that clears the workspace
 */
KQB.Clear = function ()
{
	var x = KQB.Query.Children.length;
	for (var i = 0; i < x; i++)
		KQB.DeleteNode (KQB.Query.Children[0].ID);	// must be done like this since the children array will get updated during deletenode
	KQB.ParsErr = false;
	document.getElementById("parsererrorpane").innerHTML = "";
	KQB.UpdateOutput();
}



/**
 * create example queries
 * 
 * NOTE: these were written before the implementation of the parser, and thus construct
 * queries 'by hand'.
 * For additional examples, it would suffice to just call Parse on a textual KWQL query
 * 
 * @param {Object} i
 */
KQB.Example = function (i)
{
	switch (i)
	{
		case 1: 	//   	ci(text:Java title:XML)
			var n1 = KQB.CreateNode(KQB.NodeType.Text);
			n1.Children[0].SetTextValue("KWQL");
			var n2 = KQB.CreateNode(KQB.NodeType.Title);
			n2.Children[0].SetTextValue("KiWi");
			var n3 = KQB.CreateNode(KQB.NodeType.ContentItem);
			n3.AddChild(n1);
			n3.AddChild(n2);
			n3.DropOnWorkspace();
			break;
		case 2:	
            KQB.ParseText("ci(tag(name:KWQL) child:ci(tag(Example)))");
			break;
		case 3:	//  	ci(Java AND NOT XML)
			var n1 = KQB.CreateNode(KQB.NodeType.Keyword);
			n1.SetTextValue("KWQL");
			var n2 = KQB.CreateNode(KQB.NodeType.Keyword);
			n2.SetTextValue("KiWi");
			var n3 = KQB.CreateNode(KQB.NodeType.NOT);
			n3.AddChild(n1);
			var n4 = KQB.CreateNode(KQB.NodeType.AND);
			n4.AddChild(n2);
			n4.AddChild(n3);
			var n5 = KQB.CreateNode(KQB.NodeType.ContentItem);
			n5.AddChild(n4);	
			n5.DropOnWorkspace();
			break;
		case 4: KQB.ParseText("ci(tag(name:www))");
			break;
		case 5: KQB.ParseText("ci(tag(name:$r) text:$r)");
			break;
		case 6: KQB.ParseText("ci(link(target:ci(KiWi)))");
			break;
		case 7: KQB.ParseText("ci(tag(name:$t) link(target:ci(tag(name:$t))))");
			break;
	}
	KQB.UpdateOutput();
}

