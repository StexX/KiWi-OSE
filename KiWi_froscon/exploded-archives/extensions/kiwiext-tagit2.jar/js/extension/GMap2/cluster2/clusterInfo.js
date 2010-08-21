// A Rectangle is a simple overlay that outlines a lat/lng bounds on the
    // map. It has a border of the given weight and color and can optionally
    // have a semi-transparent background color.
	var ClusterInfoWindowConfig = {
		'service':'http://localhost:8080/KiWi/seam/resource/services/tagit2/cluster',
		'imageurl':'http://localhost:8080/KiWi/tagit/js/extension/GMap2/cluster2',
		'size':6
	};
	
    function ClusterInfoWindow(point) {
      this.point_ = point;
	  this.start_ = 0;
	  this.size_ = ClusterInfoWindowConfig.size;
	  this.curLiteral_ = null;
    }
    ClusterInfoWindow.prototype = new GOverlay();

    // Creates the DIV representing this rectangle.
    ClusterInfoWindow.prototype.initialize = function(map) {
		//disable some things
		map.disableScrollWheelZoom();
		map.disableDragging();
      // Create the DIV representing our rectangle
   
	  var div = document.createElement("div");
	  
	  var closeimg = document.createElement("img");
	  closeimg.style.position = "absolute";
	  closeimg.style.left = "440px";
	  closeimg.style.top = "20px";
	  closeimg.style.zIndex = "30";
	  closeimg.style.cursor = "pointer";
	  closeimg.onclick = function(){ closeClusterInfoWindow();};
      
	  div.setAttribute("class","cluster");
	  
	  var dimage = document.createElement("div");
	  dimage.setAttribute("class","clusterimage");
	  
	  var pimage = document.createElement("img");
	  
	  dimage.appendChild(pimage);
	  
	  var dcontent = document.createElement("div");
	  dcontent.setAttribute("class","clustercontent");
	  
	  var dinnercontent = document.createElement("div");
	  dinnercontent.setAttribute("class","clustercontent_inner");
	  
	  var ina = document.createElement("div");
	  ina.setAttribute("class","cluster_letters");
	  dinnercontent.appendChild(ina);
	  
	  var inb = document.createElement("div");
	  inb.setAttribute("class","cluster_list");
	  dinnercontent.appendChild(inb);
	  
	  var ind = document.createElement("table");
	  ind.setAttribute("class","cluster_contentList");
	  ind.innerHtml = "<tr><td>Laden...</td></tr>";
	  inb.appendChild(ind);
	  
	  var ina = document.createElement("table");
	  ina.setAttribute("class","cluster_pages");
	  dinnercontent.appendChild(ina);
	  
	  dcontent.appendChild(dinnercontent);
	  div.appendChild(closeimg);
	  
	  //TODO load content
	  
	  div.appendChild(dimage);
	  div.appendChild(dcontent);
   
      //get position
      var bounds = map.getBounds();
	  var width = map.fromLatLngToContainerPixel(bounds.getNorthEast()).x;
	  var height = map.fromLatLngToContainerPixel(bounds.getSouthWest()).y;
	
	  var pixPoint = map.fromLatLngToContainerPixel(this.point_);

	  function posDiv(pos,point) {
	  	  var p = map.fromLatLngToDivPixel(point);

		  div.style.left = p.x+"px";
		  div.style.top = p.y+"px";
		  
		  switch(pos) {
		  	case 1:
				dimage.style.top = "0px";
				dimage.style.left = "0px";
		  		pimage.src = ClusterInfoWindowConfig.imageurl+"top-left.png";
		  		break;
			case 2:
				dimage.style.top = "0px";
				pimage.src = ClusterInfoWindowConfig.imageurl+"top.png";
				if( pixPoint.x < 300 ) {
					dimage.style.left = "60px";
					div.style.marginLeft = "-100px";
				} else if( pixPoint.x < width-300 ) {
					dimage.style.left = "210px";
					div.style.marginLeft = "-250px";
				} else {
					dimage.style.left = "360px";
					div.style.marginLeft = "-400px";
				}
				break;
			case 3:
				dimage.style.top = "0px";
				dimage.style.left = "420px";
				closeimg.style.left = "20px";
				pimage.src = ClusterInfoWindowConfig.imageurl+"top-right.png";
				div.style.marginLeft = "-500px";
				break;
			case 4:
				dimage.style.left = "0px";
				pimage.src = ClusterInfoWindowConfig.imageurl+"left.png";
				if( pixPoint.y < 200 ) {
					dimage.style.top = "60px";
					div.style.marginTop = "-100px";
				} else if ( pixPoint.y < height-200 ) {
					dimage.style.top = "110px";
					div.style.marginTop = "-150px";
				} else {
					dimage.style.top = "160px";
					div.style.marginTop = "-200px";
				}
				break;
			case 5:
				dimage.style.left = "420px";
				pimage.src = ClusterInfoWindowConfig.imageurl+"right.png";
				if( pixPoint.y < 200 ) {
					dimage.style.top = "60px";
					div.style.marginTop = "-100px";
				} else if ( pixPoint.y < height-200 ) {
					dimage.style.top = "110px";
					div.style.marginTop = "-150px";
				} else {
					dimage.style.top = "160px";
					div.style.marginTop = "-200px";
				}
				div.style.marginLeft = "-500px";
				break;
		  	case 6:
				dimage.style.top = "220px";
				dimage.style.left = "0px";
				pimage.src = ClusterInfoWindowConfig.imageurl+"bottom-left.png";
		  		div.style.marginTop = "-300px";
				break;
			case 7:
				dimage.style.top = "220px";
				pimage.src = ClusterInfoWindowConfig.imageurl+"bottom.png";
				if( pixPoint.x < 300 ) {
					dimage.style.left = "60px";
					div.style.marginLeft = "-100px";
				} else if( pixPoint.x < width-300 ) {
					dimage.style.left = "210px";
					div.style.marginLeft = "-250px";
				} else {
					dimage.style.left = "360px";
					div.style.marginLeft = "-400px";
				}
				div.style.marginTop = "-300px";
				break;
			case 8:
				dimage.style.top = "220px";
				dimage.style.left = "420px";
				pimage.src = ClusterInfoWindowConfig.imageurl+"bottom-right.png";
				div.style.marginLeft = "-500px";
				div.style.marginTop = "-300px";
				break;
		  }
		  
	  }
	  
	  
	  var pos = 4;
	  if( pixPoint.y < 100 ) {
		  	if( pixPoint.x < 100 ) pos=1;
		  	else if( pixPoint.x < width-100 ) pos=2;
		  	else pos = 3;
		} else if( pixPoint.y < height-100 ) {
			if( pixPoint.x < width/2 ) pos=4;
			else pos = 5;
		} else {
			if( pixPoint.x < 100 ) pos=6;
			else if( pixPoint.x < width-100 ) pos=7;
			else pos = 8;
		}

	  posDiv(pos,this.point_);
	  
	  closeimg.src = ClusterInfoWindowConfig.imageurl+"close.png";
	
	 // load clustercontent
	 /*
	 jQuery.getJSON(ClusterInfoWindowConfig.service+"?start=0&size=5",function(ps){
	 	var data = ps.points;
	 	var ul = document.createElement("ul");
	 	for( var i = 0; i < data.length;i++ ) {
			var li = document.createElement("li");
			var alink = document.createElement("a");
			var id = data[i].id;
			eval('alink.onclick = function(){curPoint = new Point('+id+',new GLatLng('+data[i].lat+','+data[i].lng+'));onPointClick();closeClusterInfoWindow();}');
			alink.innerHTML = data[i].title;
			li.appendChild(alink);
			ul.appendChild(li);
		}
		dinnercontent.innerHTML = "<a href='#' onclick='clusterInfoWindow.prevPage()'>pref</a> <a href='#' onclick='clusterInfoWindow.nextPage()'>next</a>";
		dinnercontent.appendChild(ul);
		div.focus();
	 });
	 */	

      // Our rectangle is flat against the map, so we add our selves to the
      // MAP_PANE pane, which is at the same z-index as the map itself (i.e.,
      // below the marker shadows)
      map.getPane(G_MAP_FLOAT_PANE).appendChild(div);
	  
	  //load letters
	  var les='<a href="#" onclick="clusterInfoWindow.setLiteral(null)">alle</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'A'"+')">A</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'B'"+')">B</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'C'"+')">C</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'D'"+')">D</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'E'"+')">E</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'F'"+')">F</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'G'"+')">G</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'H'"+')">H</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'I'"+')">I</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'J'"+')">J</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'K'"+')">K</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'L'"+')">L</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'M'"+')">M</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'N'"+')">N</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'O'"+')">O</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'P'"+')">P</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'Q'"+')">Q</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'R'"+')">R</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'S'"+')">S</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'T'"+')">T</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'U'"+')">U</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'V'"+')">V</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'W'"+')">W</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'X'"+')">X</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'Y'"+')">Y</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral('+"'Z'"+')">Z</a>&#160;';
	  les+='<a href="#" onclick="clusterInfoWindow.setLiteral(null)">alle</a>&#160;';
	  jQuery(".cluster_letters").html(les);

	  //load buttons
	  var older = "&#228;ltere >";
	  var newer = "< aktuellere";
	  var pas = '<tr><td width="80"><a href="#" id="clusterPrevPage" onclick="clusterInfoWindow.prevPage()">'+newer+'</a><a href="#" id="clusterPrevPageHidden" style="display:none;color:lightgrey">'+newer+'</a></td><td align="center" id="clusterPageInfo"></td><td align="right" width="50"><a href="#" id="clusterNextPage" onclick="clusterInfoWindow.nextPage()">'+older+'</a><a href="#" id="clusterNextPageHidden" style="display:none;color:lightgrey;">'+older+'</a></td></tr>';
	  jQuery(".cluster_pages").html(pas);
	  
      this.map_ = map;
      this.div_ = div;
	  
	  this.content_ = dinnercontent;
	  
	  this.ajaxGetContent();
    }

    // Remove the main DIV from the map pane
    ClusterInfoWindow.prototype.remove = function() {
	  this.map_.enableScrollWheelZoom();
	  this.map_.enableDragging();
      this.div_.parentNode.removeChild(this.div_);
    }

    // Copy our data to a new Rectangle
    ClusterInfoWindow.prototype.copy = function() {
      return new ClusterInfoWindow(this.id_,this.point_);
    }

    // Redraw the rectangle based on the current projection and zoom level
    ClusterInfoWindow.prototype.redraw = function(force) {
      // We only need to redraw if the coordinate system has changed
      if (!force) return;
      //nothing to do

    }
	
	ClusterInfoWindow.prototype.nextPage = function() {
		this.start_ += this.size_;
		this.ajaxGetContent();
	}
	
	ClusterInfoWindow.prototype.prevPage = function() {
		this.start_ -= this.size_;
		this.ajaxGetContent();
	};
	
	ClusterInfoWindow.prototype.ajaxGetContent = function(){
		var l = "";
		if( this.curLiteral_ != null ) {
			l = "&c="+this.curLiteral_;
		}
		jQuery.getJSON(ClusterInfoWindowConfig.service+"&start="+this.start_+"&size="+this.size_+l,function(data){
			clusterInfoWindow.writeContent(data);
		});
	};
	
	ClusterInfoWindow.prototype.setLiteral = function(literal){
		this.curLiteral_ = literal;
		this.start_ = 0;
		this.ajaxGetContent();
	};
	
	ClusterInfoWindow.prototype.writeContent = function(data) {
		var con = "";
		for( var i = 0; i < data.points.length; i++ ) {
			var p = data.points[i];
			con += "<tr height='20'><td valign='top' width='16px' style='font-size:8px'>"+p.date+"</td><td><a href='#' onclick='curPoint = new Point("+p.id+",new GLatLng("+p.lat+","+p.lng+"));onPointClick();closeClusterInfoWindow();'>"+p.title+"</a></td></tr>";
		}
		jQuery(".cluster_contentList").html(con);
		jQuery("#clusterPageInfo").html(data.page+" / "+data.size);
		if( data.page == 1 ) {
			jQuery("#clusterPrevPage").hide();
			jQuery("#clusterPrevPageHidden").show();
		} else {
			//enable back button
			jQuery("#clusterPrevPageHidden").hide();
			jQuery("#clusterPrevPage").show();
		}

		if( data.page == data.size ) {
			//disable next button
			jQuery("#clusterNextPage").hide();
			jQuery("#clusterNextPageHidden").show();
		} else {
			//enable next button
			jQuery("#clusterNextPageHidden").hide();
			jQuery("#clusterNextPage").show();
		}
		
	};
	
	
