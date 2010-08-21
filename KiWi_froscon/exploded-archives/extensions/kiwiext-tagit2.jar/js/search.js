var inMapSection = true;

function changeMapSection() {
	if(inMapSection)
		inMapSection = false;
	else
		inMapSection = true;
}

function changeLayerVisibility() {
	if (jQuery('#layersBody').is(":hidden")) {
		jQuery('#layersBody').slideDown("fast");
	} else {
		jQuery('#layersBody').slideUp("fast");
	}
}

function search() {
	if( inMapSection ) {
		ajaxSearch();
	} else {
		ajaxGetBounds();
	}
}

function searchCurrentTag() {
	//set current tag to keyword
	jQuery("#formHdrSearch\\:hdrSearchBox").attr({value:'tag:"'+currentTag+'"'});
	inMapSection = false;
	jQuery('#checkBoxMapSection:checkbox').attr("checked",false);
	ajaxGetBounds();
}

/**
 * to set new bounds from an given area to the map (zoom and center)
 * @param area
 * @return
 */
function setBounds(area) {
	if(area == null) {
		//means that there are no such points -> search for this section to clear map and tagcloud
		ajaxSearch();
	} else {
		var bounds = new GLatLngBounds(new GLatLng(area.swLatLng.latitude,area.swLatLng.longitude),
									new GLatLng(area.neLatLng.latitude,area.neLatLng.longitude));
		var newZoom = map.getBoundsZoomLevel(bounds);
	
		//just that zoom is not to close
		if( newZoom > 16 ) {
			newZoom = 16;
		}
	
		//set newZoom and center
		map.setCenter(new GLatLng(area.center.latitude,area.center.longitude),newZoom);
	}
}