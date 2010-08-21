//include the main app

#include "../as/der-mo.net/relationBrowser/RelationBrowserApp.as"

// include special patches and extensions from local folder

#include "NodeClasses.as"
#include "RelationClasses.as"

// see readme.txt for tips on extending the basic functionality

GraphView.prototype.defineLayoutStrategy =function (){
	// this function is used to define the way the layout of the nodes is calculated
	// you can overwrite it for your custom implementations
	
	// DEFAULT SETTING -------------------------------------
	
	// PARAMETERS
	
	// maximum number of total nodes displayed, 
	// however level 1 connected nodes are ALWAYS displayed
	// regardless of max. number
	this.maxNodes=15;
	
	// how deep shall we look for connected nodes
	this.connectionDepth=1;

	// shall only relations to center node or all relations be displayed?
	this.showAllRelations=false;

	// classical radial layout  
	this.layoutManagerClassName="RadialLayoutManager";
	
	// iterative springs and repulsion layout
	// this.layoutManagerClassName="SpringsLayoutManager";
	
	// first springs layout, then radial based on the calculated positions
	// this.layoutManagerClassName="CombinedLayoutManager";
	
	// END DEFAULT SETTING ---------------------------------
};