//if a header exists the tabs migth be marged
var tab_margin = 100;

function addMarkerTooltip(map,marker,htmlText) {
	GEvent.addListener(marker,"mouseover", function() {
		//create tooltip
		jQuery("<div id='sn_gmapTooltip_front' class='sn_gmapTooltip'>"+htmlText+"</div><div id='sn_gmapTooltip_back' class='sn_gmapTooltip'>"+htmlText+"</div>").prependTo("body");
		//get position
		var point = map.fromLatLngToContainerPixel(marker.getLatLng());
		jQuery("#sn_gmapTooltip_front").css('top',(point.y+tab_margin)+"px");
		jQuery("#sn_gmapTooltip_front").css('left',(point.x+20)+"px");
		jQuery("#sn_gmapTooltip_back").css('top',(point.y+tab_margin)+"px");
		jQuery("#sn_gmapTooltip_back").css('left',(point.x+20)+"px");
		//show
		jQuery(".sn_gmapTooltip").toggle(100).animate({fontSize:"1em"},100).animate({fontSize:"1.2em"},100);
	});
	
	GEvent.addListener(marker,"mouseout", function() {
		jQuery(".sn_gmapTooltip").remove();
	});
}

function addClusterTooltip(map,cluster,htmlText) {
	
	GEvent.addDomListener(cluster.clusterDiv,"mouseover",function() {
		//create tooltip
		jQuery("<div id='sn_gmapTooltip_front' class='sn_gmapTooltip'>"+htmlText+"</div><div id='sn_gmapTooltip_back' class='sn_gmapTooltip'>"+htmlText+"</div>").prependTo("body");
		//get position
		var point = map.fromLatLngToContainerPixel(cluster.getLatLng());
		jQuery("#sn_gmapTooltip_front").css('top',(point.y+tab_margin)+"px");
		jQuery("#sn_gmapTooltip_front").css('left',(point.x+20)+"px");
		jQuery("#sn_gmapTooltip_back").css('top',(point.y+tab_margin)+"px");
		jQuery("#sn_gmapTooltip_back").css('left',(point.x+20)+"px");
		//show
		jQuery(".sn_gmapTooltip").toggle(100).animate({fontSize:"1em"},100).animate({fontSize:"1.2em"},100);
	});
	
	GEvent.addDomListener(cluster.clusterDiv,"mouseout",function() {
		jQuery(".sn_gmapTooltip").remove();
	});
	
	GEvent.addDomListener(cluster.clusterDiv,"click",function() {
		jQuery(".sn_gmapTooltip").remove();
	});
}