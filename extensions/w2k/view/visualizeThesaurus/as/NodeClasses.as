/* ---------------------------------------------------------
   
	Person class 
	An example of how to introduce a new node type
	 
--------------------------------------------------------- */

// define class
function Person() {}

// inherit from Node class
Person.prototype = new Node();

// register the class for the movieclip symbol of the SAME name
// don't forget to add it to your library and check the "export for actionscript" option!

Object.registerClass ("Person", Person);

// a new node type might have new kinds of details to display in the sidebar:
Person.prototype.getDetails = function (){
	
	// will be called by the sidebar to get the details to be displayed

	var d=[];
	d.push({label:"Name", value:this.name});
	d.push({label:"URL", value:this.URL, contentType:"URL"});
	d.push({label:"Description", value:this._text});
	return d;
};

// see Node.as for all the methods you can overwrite or extend


/* ---------------------------------------------------------
   
	Comment class 

--------------------------------------------------------- */

function Comment() {
	
	// for non-round shapes, you can hardcode a radius here
	// it will be used for drawing the relations and the spotlight
	this.radius=45;
}
Comment.prototype = new Node();
Object.registerClass ("Comment", Comment);

/* ---------------------------------------------------------
   
	Document class 

--------------------------------------------------------- */

// constructor
function Document() {}

// inheritance
Document.prototype = new Node();
Object.registerClass ("Document", Document);

/* ---------------------------------------------------------
   
	Patch: add a generic getDetails() function 
	to all nodes for use with the sidebar detail view

--------------------------------------------------------- */

Node.prototype.getDetails=function (){
	
	// will be called by the sidebar to get the details to be displayed
	
	var d=[];
	d.push({label:"Name", value:this.name});
	d.push({label:"URL", value:this.URL, contentType:"URL"});
	d.push({label:"Description", value:this._text});
	return d;
};
