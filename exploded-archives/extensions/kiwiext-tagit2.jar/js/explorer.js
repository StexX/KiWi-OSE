var map;
var curPoint = null;
var curCluster = null;
var currentTag = null;

var clusterInfoWindow = null;

var openByClick = false;
//init map(Controls and deefault settings)
function initialize() {

	if (GBrowserIsCompatible()) {
		
		//init map
		map = new GMap2(document.getElementById("mapPanel"));
		
		var customUI = map.getDefaultUI();
		map.setUI(customUI);
		//default map type
		map.setMapType(G_HYBRID_MAP);
		
		//map.disableDoubleClickZoom();
		
		/*
		//configure cluster 1
		GClusterConfig.onPointClick = openPointFromFlash;
		GClusterConfig.swfFolder =  "js/extension/GMap2/cluster/";
        GClusterConfig.basicImage = externalContextPath+"/seam/resource/services/tagit2/cluster/";
        GClusterConfig.onLeaveStage = closeCluster;
		*/
		ClusterInfoWindowConfig.service = externalContextPath+'/seam/resource/services/tagit2/cluster?cid='+conversationId;
		ClusterInfoWindowConfig.imageurl = externalContextPath+'/tagit/js/extension/GMap2/cluster2/';
		
		//check if there is a current poi,
		//mapSettings (center,zoom,currentID) are returned,
		//->setMap(mapSettings)
		ajaxCheckCurrent();
	}
	
}

//center map, set zoom and set currentPoint
function setMap(mapSettings) {

	if(mapSettings.currentPoiId != -1) {
		point = new GLatLng(mapSettings.center.latitude,mapSettings.center.longitude);
		curPoint = new Point(mapSettings.currentPoiId,point);
	} else {
		curPoint = null;
	}
	map.setCenter(new GLatLng(mapSettings.center.latitude,mapSettings.center.longitude),mapSettings.zoom);
	//set moveendListener
	GEvent.addListener(map,"moveend",boundsAndZoom);
	
	//maybe only on movestart?
	GEvent.addListener(map,"move",closeClusterInfoWindow);
	
	//get points by setting zoom and bounds
	boundsAndZoom();
}

function boundsAndZoom() {
	//send current map bounds and zoom to the server,
	//mapMarkers( location, id, title, icon, type ) are returned
	//->displayMarkers
	var swLatLng = map.getBounds().getSouthWest();
	var neLatLng = map.getBounds().getNorthEast();
	ajaxSetBoundsAndZoom(swLatLng.lat(),swLatLng.lng(),neLatLng.lat(),neLatLng.lng(),map.getZoom());
}

// display all markers in the list
function displayMarkers(markers) {
	//delete all markers
	map.clearOverlays();
	for( var i = 0; i < markers.length; i++ ) {
		//display a single marker depending on type
		switch(markers[i].type) {
		case 'point':
			displayAsPoint(markers[i]);
			break;
		case 'cluster':
			displayAsCluster(markers[i]);
			break;
		}
	}
	if( curPoint != null ) {
		openPointWindow();
	}
}

function displayAsPoint(m) {
	//define marker icon
	//TODO should use icons from backend
	var micon = createIcon(m.icon);
	//GLog.write(m.icon);
	var marker = new GMarker(new GLatLng(m.location.latitude,m.location.longitude),{titel:m.title,icon:micon});
	GEvent.addListener(marker,"click",function(point){
		closeClusterInfoWindow();
		curPoint = new Point(m.id,point);
		onPointClick();
	});
	
	//add tooltip
    var text = m.title;
    addMarkerTooltip(map,marker,text);
	
	map.addOverlay(marker);
}

function displayAsCluster(m){
	
	var clusterIcon = createClusterIcon(m.icon);
	
	var point = new GLatLng(m.location.latitude,m.location.longitude);
	var cluster = new GMarker(point,{icon:clusterIcon,title:"Doppelklicken, um den Cluster zu öffnen!"});
	GEvent.addListener(cluster,"dblclick",function(point){
			curCluster = point;
        	if(map.getZoom() < 16) {
        		ajaxGetClusterArea(m.id);
        	} else {
        		ajaxSelectCluster(m.id);
        	}
	});
	map.addOverlay(cluster);
}

/*
function displayAsCluster(m) {
	
	var point = new GLatLng(m.location.latitude,m.location.longitude);
	var cluster = new GCluster(point,m.icon);
	
	map.addOverlay(cluster);
	
    GEvent.addDomListener(cluster.clusterDiv,"click",function() {
        if(curCluster == null) {
        	curCluster = cluster;
        	if(map.getZoom() < 16) {
        		ajaxGetClusterArea(m.id);
        	} else {
        		ajaxSelectCluster(m.id);
        	}
        }
    });
}
*/