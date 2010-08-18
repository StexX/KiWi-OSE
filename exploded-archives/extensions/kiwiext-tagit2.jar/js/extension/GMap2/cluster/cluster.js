    var GClusterConfig = {
        'onPointClick' : function(id){alert("click on point("+id+") - define own function by GClusterConfig");},
        'swfFolder' : '',
        'basicImage' : '',
        'onLeaveStage' : function(){alert("define close function by GClusterConfig");}
    };
    
    //the first try of a cluster overlay
    function GCluster(center,icon) {
      this.center = center;
      this.icon = icon;
      this.getLatLng = function() {
    	 return this.center;
      };
      this.open = function(url){
        //disable map draging (bugfix in firefox)
        this.map.disableDragging();
        //set div
	  var position = this.map.fromLatLngToDivPixel(this.center);
	 
        this.clusterDiv.style.left = (position.x - 365) + "px";
        this.clusterDiv.style.top = (position.y - 100) + "px";
        this.clusterDiv.style.width="730px";
        this.clusterDiv.style.height="125px";
        
        this.flashDiv = document.createElement("div");
        this.flashDiv.style.zIndex = "1";
	  this.flashDiv.style.position = "absolute";
	  this.flashDiv.style.top = "0px";
	  this.flashDiv.style.left = "0px";
        
        //create inner html
        var _param = "?url="+url;
        var _sn_cluster_flashInnerHtml = '<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=10,0,0,0" width="730" height="125" id="cluster" align="middle"><param name="always" value="sameDomain" /><param name="allowFullScreen" value="false" /><param name="movie" value="'+GClusterConfig.swfFolder+'cluster.swf'+_param+'" /><param name="quality" value="high" /><param name="scale" value="noborder" /><param name="wmode" value="transparent" /><param name="bgcolor" value="#ffffff" /><embed id="embededFlash" src="'+GClusterConfig.swfFolder+'cluster.swf'+_param+'" quality="high" scale="noborder" wmode="transparent" bgcolor="#ffffff" width="730" height="125" name="cluster" align="middle" allowScriptAccess="always" allowFullScreen="false" type="application/x-shockwave-flash" pluginspage="http://www.adobe.com/go/getflashplayer_de" /></object>';
        
        //load flash by innerHTML
        this.flashDiv.innerHTML = _sn_cluster_flashInnerHtml;
        
        this.clusterDiv.appendChild(this.flashDiv);
      };
      
      this.close = function() {
        //clear listener
        this.map.enableDragging();
        //this.listener = null;
        //delete flash
        this.clusterDiv.removeChild(this.flashDiv);
        //enable map dragging
        this.map.enableDragging()
        //reset div
        this.clusterDiv.style.width="60px";
        this.clusterDiv.style.height="60px";
	  	var position = this.map.fromLatLngToDivPixel(this.center);
	 
        this.clusterDiv.style.left = (position.x - 30) + "px";
        this.clusterDiv.style.top = (position.y - 30) + "px";
      }
    }
    
    GCluster.prototype = new GOverlay();

    // Creates the DIV representing this cluster with a img in it.
    GCluster.prototype.initialize = function(map) {
      //TODO Create flash here
      this.clusterDiv = document.createElement("div");

      //TODO width Events
      this.clusterDiv.style.width = "60px";
      this.clusterDiv.style.height = "60px";
      this.clusterDiv.style.cursor = "pointer";
      this.clusterDiv.style.position = "absolute";
      
      var imageDiv = document.createElement("div");
      imageDiv.style.zIndex = "0";
	imageDiv.style.position = "absolute";
	imageDiv.style.bottom = "0px";
	imageDiv.style.left = "50%";
	imageDiv.style.marginLeft = "-30px";

      //define inner html
      var _sn_cluster_imageInnerHtml = '<img src="'+GClusterConfig.basicImage+this.icon+'" width="50" height="50" style="border:none;" />';
      imageDiv.innerHTML = _sn_cluster_imageInnerHtml;
      
      this.clusterDiv.appendChild(imageDiv);
      //onClick
      this.clusterDiv.onclick = this.onClick;
      //apend new dom element
      map.getPane(G_MAP_MARKER_PANE).appendChild(this.clusterDiv);
      //map
      this.map = map;
    };

    // Remove the main DIV from the map pane
    GCluster.prototype.remove = function() {
      this.clusterDiv.parentNode.removeChild(this.clusterDiv);
      this.map.enableDragging();
    };

    // Copy our data to a new Cluster
    GCluster.prototype.copy = function() {
      return new GCluster(this.center);
    };

    // Redraw cluster based on the current projection and zoom level
    GCluster.prototype.redraw = function(force) {
      // We only need to redraw if the coordinate system has changed

      if (!force) return;
	  //TODO something went wrong when coordiantionSystem changed
      // Now position our DIV based on the center point
	  var position = this.map.fromLatLngToDivPixel(this.center);
	 
      this.clusterDiv.style.left = (position.x - 30) + "px";
      this.clusterDiv.style.top = (position.y - 30) + "px";
    };