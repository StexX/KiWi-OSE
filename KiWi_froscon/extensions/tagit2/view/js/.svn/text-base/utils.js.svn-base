function Waypoint(marker,id) {
	this.marker = marker;
	this.id = id;
}

function Point( id, point ) {
	this.id = id;
	this.point = point;
	this.toString = function() {
		return "Point(id:"+this.id+", point:"+this.point+")";
	};
}

//some markers
var blueIcon = new GIcon(G_DEFAULT_ICON);
blueIcon.image = "http://www.google.com/intl/en_us/mapfiles/ms/micons/blue.png";

var greenIcon = new GIcon(G_DEFAULT_ICON);
greenIcon.image = "http://www.google.com/intl/en_us/mapfiles/ms/micons/green.png";

var personIcon = new GIcon(G_DEFAULT_ICON);
personIcon.image = "image/icon16x16/user.png";

var icon_routeStart = new GIcon(G_DEFAULT_ICON);
icon_routeStart.image = "http://www.google.com/intl/en_us/mapfiles/ms/micons/pink.png";

var icon_routeEnd = new GIcon(G_DEFAULT_ICON);
icon_routeEnd.image = "http://www.google.com/intl/en_us/mapfiles/ms/micons/orange.png";

var icon_routeStartEnd = new GIcon(G_DEFAULT_ICON);
icon_routeStartEnd.image = "http://www.google.com/intl/en_us/mapfiles/ms/micons/orange-dot.png";

var icon_wp = new GIcon(G_DEFAULT_ICON);
icon_wp.image = "image/icon16x16/wp.png";

var icon_wp_plus = new GIcon(G_DEFAULT_ICON);
icon_wp_plus.image = "image/icon16x16/wp_plus.png";


function createIcon(filename) {
	var my_icon = new GIcon(G_DEFAULT_ICON);
	my_icon.image = "image/icon16x16/"+filename;
	return my_icon;
}

function createClusterIcon(icon) {
	var my_icon = new GIcon();
	my_icon.image = externalContextPath+"/seam/resource/services/tagit2/cluster/"+icon;
	my_icon.iconSize = new GSize(60,60);
	my_icon.shadowSize = new GSize(0,0);
	my_icon.iconAnchor = new GPoint(30,30);
	//my_icon.imageMap = [0,0,0,60,10,60,10,0];
	return my_icon;
}

function getLetterIcon(letter) {
		var icon = new GIcon(G_DEFAULT_ICON);
		icon.image = "http://www.google.com/mapfiles/marker" + letter + ".png";
		return icon;
}
