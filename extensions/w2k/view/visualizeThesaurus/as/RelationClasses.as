/* ---------------------------------------------------------
   
	MyCustomRelation class
	
	A minimal example of how to introduce a new relation type
	based on a DirectedRelation (has an arrowhead), but you could also extend
	the UndirectedRelation or Relation classes
	
	No movieclip definition is necessary for relations, 
	only a class of the same name 
	
--------------------------------------------------------- */


function MyCustomRelation(relObj, fromMC, toMC, drawTarget, viewManager){
	super(relObj, fromMC, toMC, drawTarget, viewManager);
}

MyCustomRelation.prototype=new DirectedRelation();
