function onPointClick() {
	//selelct poi on serverside
	//-> blowUpContent
	openByClick = true;
	ajaxSelectPoi(curPoint.id);
}

/*
function openFlashCluster() {
	var url = externalContextPath + '/seam/resource/services/tagit2/cluster?cid='+conversationId;
	curCluster.open(url);
}
*/

function openClusterInfoWindow() {
	closeClusterInfoWindow();
	clusterInfoWindow = new ClusterInfoWindow(curCluster);
	map.addOverlay(clusterInfoWindow);
}

function closeClusterInfoWindow() {
	if( clusterInfoWindow != null ) {
		try {
			map.removeOverlay(clusterInfoWindow);
		} catch(e) {
			GLog.write("no such element");
		}
	}
}

function zoomToCluster(area) {
	
	if(area != null) {
	var bounds = new GLatLngBounds(new GLatLng(area.swLatLng.latitude,area.swLatLng.longitude),
			new GLatLng(area.neLatLng.latitude,area.neLatLng.longitude));
	var newZoom = map.getBoundsZoomLevel(bounds);
	if( newZoom > 16 ) {
		newZoom = 16;
	}
	curCluster = null;

	//set newZoom and center
	map.setCenter(new GLatLng(area.center.latitude,area.center.longitude),newZoom);
	} else {
		//cluster should be oppend
		//openFlashCluster();
		openClusterInfoWindow();
	}
}


function openPointFromFlash( id, lat, lng ) {
	curPoint = new Point(id,new GLatLng(lat,lng));
	onPointClick();
}

function closeCluster() {
    curCluster.close();
    curCluster = null;
    ajaxSelectCluster(-1);
}


//--------error message------------------------
//alert box
function openAlertBox(text) {
	jQuery("<div id='alertBoxBack'></div><div id='alertBox'><div class='alertContent'>"+text+"</div><div style='width:100%;height:30px;'><center><b><a href='#' onclick='closeAlertBox();return false;'>OK</a></b></center></div></div>").appendTo('body');
}

function closeAlertBox() {
	jQuery("#alertBox").remove();
	jQuery("#alertBoxBack").remove();
}

//to close point
function closePoint() {
	jQuery("#point").fadeOut("slow",function(){
		jQuery("#back").fadeOut("slow",function(){
			curPoint = null;
			openByClick = false;
			ajaxUnselectPoi();
		});
	});
}

var newPoint = null;

function closePointOnLocation(location) {
	newPoint = location;
	jQuery("#point").fadeOut("slow",function(){
		jQuery("#back").fadeOut("slow",function(){
			curPoint = null;
			openByClick = false;
			if(newPoint == null) {
				ajaxUnselectPoiAndReRender();
			} else {
				ajaxUnselectPoiAndRelocate();
			}
		});
	});	
}

function closePointByTagSearch(tag) {	
	jQuery("#point").fadeOut("slow",function(){
		jQuery("#back").fadeOut("slow",function(){
			curPoint = null;
			openByClick = false;
			currentTag = tag;
			ajaxUnselectPoiAndSearchTag();
		});
	});
}