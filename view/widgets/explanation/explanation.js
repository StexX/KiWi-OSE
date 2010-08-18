var Log = {
    elem: false,
    write: function(text){
        if (!this.elem) 
            this.elem = document.getElementById('log');
        this.elem.innerHTML = text;
        this.elem.style.left = (500 - this.elem.offsetWidth / 2) + 'px';
    }
};

function addEvent(obj, type, fn) {
    if (obj.addEventListener) obj.addEventListener(type, fn, false);
    else obj.attachEvent('on' + type, fn);
};

function normalizeJSON(data) {
	var newJSON = new Array(data.length);

	for (i = 0 ; i < data.length ; i++) {
		newJSON[i] = data[i].node;
		
		if (newJSON[i].adjacencies == undefined)
			newJSON[i].adjacencies = new Array();
		else
			if (typeof newJSON[i].adjacencies == "string") {
				tmp = newJSON[i].adjacencies;
				newJSON[i].adjacencies = new Array();
				newJSON[i].adjacencies[0] = tmp;
			}
		
		//convert the stupid JAXB JSON to something more agreable
		if (!(newJSON[i].data == undefined)) {
			oldData = newJSON[i].data;
			newData = {};
			for (j = 0; j < oldData.length ; j++) {
				newData[oldData[j].key] = oldData[j].value;
			}
			newJSON[i].data = newData;
		}
	}

	return newJSON;
}

function initJIT(id) {
	//in the first request the rootid is the id to be explained because there's nothing else yet
	url = "http://localhost:8080/KiWi/seam/resource/services/widgets/explanation/explain?id="+id+"&rootid="+id+"&jsonpCallback=?";
	jQuery.getJSON(url,
			function(data) {
				if (data.length == 0) {
					alert("Triple id "+id+" is a base triple.");
					return;
				}
				
				var newJSON = normalizeJSON(data);

				debug(JSON.stringify(newJSON));
				init(newJSON,id);
		    }
	);
	
};

function debug(msg) {
//	var el = document.getElementById('debug');
//	el.innerHTML += "<br/><p>"+msg+"</p>";
}


function init(json,rootid){
    function get(id) {
      return document.getElementById(id);  
    };

    function getContainingDiv(el) {
        while (el.nodeName != "DIV") {
            el = el.parentNode;
            if (el.nodeName == "BODY")
                return null;
        }

        return el;
    }

    function scrollTo(id) {
        el = document.getElementById(id);
        div = getContainingDiv(el).parentNode;
        div.scrollTop = el.parentNode.offsetTop;
    }

    function highlightOn(id) {
        jQuery("#"+ id).parent().addClass("txt-expl-highlight");
    }

    function highlightOff(id) {
        jQuery("#"+ id).parent().removeClass("txt-expl-highlight");
    }

    //init data
    //var json = {"asdf"}; 
    	
    //end
    var infovis = document.getElementById('infovis');
    
    var child;
    while(child=infovis.firstChild)
     infovis.removeChild(child);
    
    
    var w = infovis.offsetWidth, h = infovis.offsetHeight;
    //init canvas
    //Create a new canvas instance.
    var canvas = new Canvas('mycanvas', {
        'injectInto': 'infovis',
        'width': w,
        'height': h,
        'backgroundColor': '#1a1a1a'
    });
    //end
    
    //init st
    //Create a new ST instance
    var st = new ST(canvas, {
        orientation: "top",
        //set duration for the animation
        duration: 800,
        //set animation transition type
        transition: Trans.Quart.easeInOut,
        //set distance between node and its children
        levelDistance: 50,
        //set node and edge styles
        //set overridable=true for styling individual
        //nodes or edges
        Node: {
            height: 20,
            width: 85,
            type: 'rectangle',
            color: '#aaa',
            overridable: true
        },
        
        Edge: {
            type: 'bezier',
            overridable: true
        },
        
        onBeforeCompute: function(node){
            Log.write("loading " + node.name);
        },
        
        onAfterCompute: function(){
            Log.write("done");
        },
        
        //This method is called on DOM label creation.
        //Use this method to add event handlers and styles to
        //your node.
        onCreateLabel: function(label, node){
        	function loadSubtree(node) {
        		//alert(node.data.type+": "+node.data.id);
        		url = "http://localhost:8080/KiWi/seam/resource/services/widgets/explanation/explain?id="+node.data.kiwiid+"&rootid="+node.data.id+"&jsonpCallback=?";

        		//alert(url);
        		jQuery.getJSON(url,
        				function(data) {				
        					if (data.length == 0) {
        						alert("Triple "+node.data.kiwiid+" is an explicit, not derived, triple.");
        						return;
        					}
        				
        					var json = normalizeJSON(data);
        					
        					if (node.loaded == undefined || node.loaded == false) {
        						st.addSubtree(json, 'animate');
        						node.loaded = true;
        					} else {
        						st.removeSubtree(json[0].id, false, 'animate');
        						node.loaded = false;
        					}
        	                
        					st.refresh();
        					
        	                debug(JSON.stringify(json));
        				}//function(data)
        		);
        		
        		node.data.leaf = false;

                        Seam.Component.getInstance("explanationAction").addTripleId(node.data.kiwiid);

                        addTripleScript();
        			
        	};
        	
            label.id = node.id;
            if (node.data.type == "triple") {
                label.innerHTML = "<span title=\""+node.data.tooltip+"\">"+ node.name.substr(2) + "</span>";
                    label.onmouseover = function () {
                        elid = 'kiwiidj'+node.data.kiwiidj+'kiwiid'+node.data.kiwiid;
                        scrollTo('kiwiidj'+node.data.kiwiidj);
                        highlightOn(elid);
                    }
                    label.onmouseout = function () {
                        elid = 'kiwiidj'+node.data.kiwiidj+'kiwiid'+node.data.kiwiid;
                        highlightOff(elid);
                    }
            } else {
                label.innerHTML = "<span title=\""+node.data.tooltip+"\">"+ node.name + "</span>";
                if (node.data.type == "justification") {                 

                    debug("adding highlighting to "+node.data.kiwiid);
                    label.onmouseover = function () {
                        elid = 'kiwiidj'+node.data.kiwiid;
                        scrollTo(elid);
                        highlightOn(elid);
                    }
                    label.onmouseout = function () {
                        elid = 'kiwiidj'+node.data.kiwiid;
                        highlightOff(elid);
                    }
                }
            }
            label.onclick = function() {
            	if (node.data.leaf == true) {
                    if (node.data.kiwiid == rootid) {
            		debug("Not loading node "+node.id+" because it is the root of this tree = cycle detected.");
                        alert("Cycle detected: the node you clicked is also the root node of the whole tree.");
                        return;
                    }

            		debug("Loading subtree of "+node.data.kiwiid);
            		loadSubtree(node);
            	} else {
            		st.onClick(node.id);
            		debug("Not loading node "+node.id+" it is not a triple leaf.");
            	}
            	Log.write("node:"+node.data.kiwiid);
            };
            
            //label.ondblclick = 
            
            //set label styles
            var style = label.style;
            style.width = 60 + 'px';
            style.height = 17 + 'px';            
            style.cursor = 'pointer';
            style.color = '#333';
            style.fontSize = '0.8em';
            style.textAlign= 'center';
            style.paddingTop = '3px';
        },
        
        //This method is called right before plotting
        //a node. It's useful for changing an individual node
        //style properties before plotting it.
        //The data properties prefixed with a dollar
        //sign will override the global node style properties.
        onBeforePlotNode: function(node){
            //add some color to the nodes in the path between the
            //root node and the selected node.
            if (node.selected) {
                node.data.$color = "#ff7";
            }
            else {
                delete node.data.$color;
                
                if (node.data.type == "rule") {
                	node.data.$color = "#5f8";
                } else {
	                var GUtil = Graph.Util;
	                //if the node belongs to the last plotted level
	                if(!GUtil.anySubnode(node, "exist")) {
	                    //count children number
	                    var count = 0;
	                    GUtil.eachSubnode(node, function(n) {count++;});
	                    //assign a node color based on
	                    //how many children it has
	                    node.data.$color = ['#aaa', '#baa', '#caa', '#daa', '#eaa', '#faa'][count];                    
	                }
                }
            }
        },
        
        //This method is called right before plotting
        //an edge. It's useful for changing an individual edge
        //style properties before plotting it.
        //Edge data proprties prefixed with a dollar sign will
        //override the Edge global style properties.
        onBeforePlotLine: function(adj){
            if (adj.nodeFrom.selected && adj.nodeTo.selected) {
                adj.data.$color = "#eed";
                adj.data.$lineWidth = 3;
            }
            else {
                delete adj.data.$color;
                delete adj.data.$lineWidth;
            }
        }
    });
    //load json data
    st.loadJSON(json);
    //compute node positions and layout
    st.compute();
    //optional: make a translation of the tree
    st.geom.translate(new Complex(-200, 0), "startPos");
    //emulate a click on the root node.
    st.onClick(st.root);
    //end
    //Add event handlers to switch spacetree orientation.
/*    var top = get('r-top'), 
    left = get('r-left'), 
    bottom = get('r-bottom'), 
    right = get('r-right');
    
    function changeHandler() {
        if(this.checked) {
            top.disabled = bottom.disabled = right.disabled = left.disabled = true;
            st.switchPosition(this.value, "animate", {
                onComplete: function(){
                    top.disabled = bottom.disabled = right.disabled = left.disabled = false;
                }
            });
        }
    };
    
    top.onchange = left.onchange = bottom.onchange = right.onchange = changeHandler;
    //end
*/
}

