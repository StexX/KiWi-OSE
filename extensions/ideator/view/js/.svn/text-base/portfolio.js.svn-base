var currentID;

var plot;

var ideas;
var hiddenIdeas;

var fill = true;
var legend = true;

function setData(data){
	if (data == null) {
		alert("No data/axes defined");
	}
	else if (data.length == 0) {
		alert("Data is not displayable (maybe not evaluated yet)");
	} else {
		ideas = data;
		for (var i = 0; i < ideas.length; i++) {
			ideas[i].label = getLegendLabel(ideas[i]);
		}
		hiddenIdeas = [];
		drawPlot();
	}
}

function drawPlot() {
    
    var options = {
    
    series: {
		bubbles: {
			active: true,
			show: true,
			fill: fill,
			lineWidth: 2
		}
	},

    grid: {
        clickable: true,
        autoHighlight: false
    },
    
    legend: {
        show: true,
        container: jQuery('#legend')
    },
    
    zoom: {
        interactive: true,
        trigger: "dblclick", // or "click" for single click
        amount: 2.0         // 2 = 200% (zoom in), 0.5 = 50% (zoom out)
    },
  
    pan: {
        interactive: true
    },

    xaxis: {
        zoomRange:  [0, 200],
        panRange: null,
        min: 0,
        max: 110
    },
    yaxis: {
        zoomRange:  [0, 200],
        panRange: null,
        min: 0,
        max: 110
    }
   };
    plot = jQuery.plot(jQuery("#placeholder"), ideas, options);
    jQuery("#placeholder").bind("plotclick", selectListener);

}

function redrawPlot() {
    jQuery("#placeholder").unbind("plotclick");
    drawPlot();
}

function selectListener(event, pos, item) {
        if( item ) {
            selectIdea(item.series.cid);
        }
}

function selectIdea(id) {
    currentID = id;
    print(id);
}

function unfill() {
    fill=!fill;
    ideas = ideas.concat(hiddenIdeas);
    hiddenIdeas = [];
    redrawPlot();
}

function setlegend() {
    legend = !legend;
    ideas = ideas.concat(hiddenIdeas);
    hiddenIdeas = [];
    if(legend) {
        for(var i = 0; i < ideas.length; i++) {
            ideas[i].label = getLegendLabel(ideas[i]);
        }
    } else {
        for(var i = 0; i < ideas.length; i++) {
            ideas[i].label = getSmallLegendLabel(ideas[i]);
        }
    }
    redrawPlot();
}

function plusBubbleSize() {
    changeSize(ideas,10);
    changeSize(hiddenIdeas,10);
    plot.setData(ideas);
    plot.draw();
}

function minusBubbleSize() {
    changeSize(ideas,-10);
    changeSize(hiddenIdeas,-10);
    plot.setData(ideas);
    plot.draw();
}

function changeSize(list,percent) {
    for( var i=0; i<list.length; i++ ) {
        list[i].data[0][2] = list[i].data[0][2] + ((list[i].data[0][2]/100)*percent);
    }
}

function getLegendLabel(item) {
    return "<table width='100%'><tr><td width='250px'>" +
    "<a href='#' onclick='selectIdea("+item.cid+");' style='text-decoration:none;color:#555555'><b>"+item.title+"</b><br/>von <i>"+item.author+"</i> am "+item.created+"</a>"+
    "</td><td><input type='checkbox' checked='checked' disabled='disabled' onclick='legendSelection("+item.cid+")' /></td></tr></table>";
}

function getSmallLegendLabel(item) {
    return "<table width='100%'><tr><td width='250px'>" +
    "<a href='#' onclick='selectIdea("+item.cid+");' style='text-decoration:none;color:#555555'><b>"+item.title+"</b></a>"+
    "</td><td><input type='checkbox' checked='checked' disabled='disabled' onclick='legendSelection("+item.cid+")' /></td></tr></table>";
}

function legendSelection(id){
    var z = -1;
    for( var i=0; i < ideas.length; i++ ) {
        if(ideas[i].cid == id) {
            z = i;
            break;
        }
    }
    if(z != -1) {
        hiddenIdeas.push(ideas[z]);
        ideas.splice(z,1);
    } else {
        ideas.push(getHiddenIdeaById(id));
    }

    plot.setData(ideas);
    plot.draw();
}

function getHiddenIdeaById(id) {
    var idea = null;
    for( var i=0; i<hiddenIdeas.length; i++ ) {
        if(hiddenIdeas[i].cid == id) {
            idea = hiddenIdeas[i];
            hiddenIdeas.splice(i,1);
            break;
        }
    }
    return idea;
}
