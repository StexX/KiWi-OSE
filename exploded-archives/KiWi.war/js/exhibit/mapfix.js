// Fixing autozoom
Exhibit.MapView.prototype._rePlotItems = function(unplottableItems) {
	    var self = this;
	    var collection = this._uiContext.getCollection();
	    var database = this._uiContext.getDatabase();
	    var settings = this._settings;
	    var accessors = this._accessors;

	    var currentSet = collection.getRestrictedItems();
	    var locationToData = {};
	    var hasColorKey = (this._accessors.getColorKey != null);
	    var hasSizeKey = (this._accessors.getSizeKey != null);
	    var hasIconKey = (this._accessors.getIconKey != null);
	    var hasIcon = (this._accessors.getIcon != null);
	    
	    currentSet.visit(function(itemID) {
	        var latlngs = [];
	        self._getLatlng(itemID, database, function(v) { if (v != null && "lat" in v && "lng" in v) latlngs.push(v); });
	        
	        if (latlngs.length > 0) {
	            var colorKeys = null;
	            if (hasColorKey) {
	                colorKeys = new Exhibit.Set();
	                accessors.getColorKey(itemID, database, function(v) { colorKeys.add(v); });
	            }
	            var sizeKeys = null;
	            if (hasSizeKey) {
	                sizeKeys = new Exhibit.Set();
	                accessors.getSizeKey(itemID, database, function(v) { sizeKeys.add(v); });
	            }
	            var iconKeys = null;
	            if (hasIconKey) {
	                iconKeys = new Exhibit.Set();
	                accessors.getIconKey(itemID, database, function(v) { iconKeys.add(v); });
	            }
	            for (var n = 0; n < latlngs.length; n++) {
	                var latlng = latlngs[n];
	                var latlngKey = latlng.lat + "," + latlng.lng;
	                if (latlngKey in locationToData) {
	                    var locationData = locationToData[latlngKey];
	                    locationData.items.push(itemID);
	                    if (hasColorKey) { locationData.colorKeys.addSet(colorKeys); }
	                    if (hasSizeKey) { locationData.sizeKeys.addSet(sizeKeys); }
	                    if (hasIconKey) { locationData.iconKeys.addSet(iconKeys); }
	                } else {
	                    var locationData = {
	                        latlng:     latlng,
	                        items:      [ itemID ]
	                    };
	                    if (hasColorKey) { locationData.colorKeys = colorKeys;}
	                    if (hasSizeKey) { locationData.sizeKeys = sizeKeys; }
	                    if (hasIconKey) { locationData.iconKeys = iconKeys; }
	                    locationToData[latlngKey] = locationData;
	                }
	            }
	        } else {
	            unplottableItems.push(itemID);
	        }
	    });
	    
	    var colorCodingFlags = { mixed: false, missing: false, others: false, keys: new Exhibit.Set() };
	    var sizeCodingFlags = { mixed: false, missing: false, others: false, keys: new Exhibit.Set() };
	    var iconCodingFlags = { mixed: false, missing: false, others: false, keys: new Exhibit.Set() };
	    var bounds, maxAutoZoom = Infinity;
	    var addMarkerAtLocation = function(locationData) {
	        var itemCount = locationData.items.length;
	        if (!bounds) {
	            bounds = new GLatLngBounds();
	        }
	        
	        var shape = self._settings.shape;
	        
	        var color = self._settings.color;
	        if (hasColorKey) {
	            color = self._colorCoder.translateSet(locationData.colorKeys, colorCodingFlags);
	        }
	        var iconSize = self._settings.iconSize;
	        if (hasSizeKey) {
	            iconSize = self._sizeCoder.translateSet(locationData.sizeKeys, sizeCodingFlags);
	        }
	        
	        var icon = null;
	        if (itemCount == 1) {
	            if (hasIcon) {
	                accessors.getIcon(locationData.items[0], database, function(v) { icon = v; });
	            }
	        }
	        if (hasIconKey) {
	            icon = self._iconCoder.translateSet(locationData.iconKeys, iconCodingFlags);
	        }
	        
	        var icon = Exhibit.MapView._makeIcon(
	            shape, 
	            color, 
	            iconSize,
	            itemCount == 1 ? "" : itemCount.toString(),
	            icon,
	            self._settings
	        );
	        
	        var point = new GLatLng(locationData.latlng.lat, locationData.latlng.lng);
	        var marker = new GMarker(point, icon);
	        if (maxAutoZoom > locationData.latlng.maxAutoZoom) {
	            maxAutoZoom = locationData.latlng.maxAutoZoom;
	        }
	        bounds.extend(point);
	        
	        GEvent.addListener(marker, "click", function() { 
	            marker.openInfoWindow(self._createInfoWindow(locationData.items));
	            if (self._selectListener != null) {
	                self._selectListener.fire({ itemIDs: locationData.items });
	            }
	        });
	        self._map.addOverlay(marker);
	        
	        for (var x = 0; x < locationData.items.length; x++) {
	            self._itemIDToMarker[locationData.items[x]] = marker;
	        }
	    }
	    for (var latlngKey in locationToData) {
	        addMarkerAtLocation(locationToData[latlngKey]);
	    }
	    if (hasColorKey) {
	        var legendWidget = this._dom.legendWidget;
	        var colorCoder = this._colorCoder;
	        var keys = colorCodingFlags.keys.toArray().sort();
	        if (settings.colorLegendLabel !== null) {
	            legendWidget.addLegendLabel(settings.colorLegendLabel, 'color');
	        }
	        if (colorCoder._gradientPoints != null) {
	            var legendGradientWidget = this._dom.legendWidget;
	            legendGradientWidget.addGradient(this._colorCoder._gradientPoints);
	        } else {
	            for (var k = 0; k < keys.length; k++) {
	                var key = keys[k];
	                var color = colorCoder.translate(key);
	                legendWidget.addEntry(color, key);
	            }
	        }
	        
	        if (colorCodingFlags.others) {
	            legendWidget.addEntry(colorCoder.getOthersColor(), colorCoder.getOthersLabel());
	        }
	        if (colorCodingFlags.mixed) {
	            legendWidget.addEntry(colorCoder.getMixedColor(), colorCoder.getMixedLabel());
	        }
	        if (colorCodingFlags.missing) {
	            legendWidget.addEntry(colorCoder.getMissingColor(), colorCoder.getMissingLabel());
	        }
	    }
	    
	    if (hasSizeKey) {
	        var legendWidget = this._dom.legendWidget;
	        var sizeCoder = this._sizeCoder;
	        var keys = sizeCodingFlags.keys.toArray().sort();    
	        if (settings.sizeLegendLabel !== null) {
	            legendWidget.addLegendLabel(settings.sizeLegendLabel, 'size');
	        }
	        if (sizeCoder._gradientPoints != null) {
	            var points = sizeCoder._gradientPoints;
	            var space = (points[points.length - 1].value - points[0].value)/5;
	            keys = [];
	            for (var i = 0; i < 6; i++) { keys.push(Math.floor(points[0].value + space*i)); }
	            for (var k = 0; k < keys.length; k++) {
	                var key = keys[k];
	                var size = sizeCoder.translate(key);
	                legendWidget.addEntry(size, key, 'size');
	            }
	           } else {       
	            for (var k = 0; k < keys.length; k++) {
	                var key = keys[k];
	                var size = sizeCoder.translate(key);
	                legendWidget.addEntry(size, key, 'size');
	            }
	            if (sizeCodingFlags.others) {
	                legendWidget.addEntry(sizeCoder.getOthersSize(), sizeCoder.getOthersLabel(), 'size');
	            }
	            if (sizeCodingFlags.mixed) {
	                legendWidget.addEntry(sizeCoder.getMixedSize(), sizeCoder.getMixedLabel(), 'size');
	            }
	            if (sizeCodingFlags.missing) {
	                legendWidget.addEntry(sizeCoder.getMissingSize(), sizeCoder.getMissingLabel(), 'size');
	            }
	        }
	    }        

	    if (hasIconKey) {
	        var legendWidget = this._dom.legendWidget;
	        var iconCoder = this._iconCoder;
	        var keys = iconCodingFlags.keys.toArray().sort();    
	        if (settings.iconLegendLabel != null) {
	            legendWidget.addLegendLabel(settings.iconLegendLabel, 'icon');
	        }      
	        for (var k = 0; k < keys.length; k++) {
	            var key = keys[k];
	            var icon = iconCoder.translate(key);
	            legendWidget.addEntry(icon, key, 'icon');
	        }
	        if (iconCodingFlags.others) {
	            legendWidget.addEntry(iconCoder.getOthersIcon(), iconCoder.getOthersLabel(), 'icon');
	        }
	        if (iconCodingFlags.mixed) {
	            legendWidget.addEntry(iconCoder.getMixedIcon(), iconCoder.getMixedLabel(), 'icon');
	        }
	        if (iconCodingFlags.missing) {
	            legendWidget.addEntry(iconCoder.getMissingIcon(), iconCoder.getMissingLabel(), 'icon');
	        }
	    }  
	    
	    if (bounds) { // && typeof settings.zoom == "undefined") {
	    	// add bounds extension by margin instead of -1 on getBoundsZoomLevel
	    	var margin = 0.3; // HACK - too lazy to calculate it based upon the zoom level
	    	var p = new GLatLng(bounds.getSouthWest().lat(), bounds.getSouthWest().lng() - margin);
	    	bounds.extend(p);
	    	p = new GLatLng(bounds.getNorthEast().lat() + margin, bounds.getNorthEast().lng() + margin);
	        bounds.extend(p);
	    	var zoom = Math.max(0, self._map.getBoundsZoomLevel(bounds));
	    	if (settings.maxAutoZoom == undefined) settings.maxAutoZoom = Infinity;
	        zoom = Math.min(zoom, maxAutoZoom, settings.maxAutoZoom);
	        self._map.setZoom(zoom);
	    }
	    if (bounds) { // && typeof settings.center == "undefined") {
	        self._map.setCenter(bounds.getCenter());
	    }
	};
