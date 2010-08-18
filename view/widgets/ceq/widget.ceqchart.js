(function($){
	$.widget("kiwi.ceqchart", {
		/**
		 * Constructor; 
		 */
		_init: function() { 
			this.refresh();
		},
		
		refresh: function() {
            var webservice = this.getWebServiceUrl();
			var uri = this.getUri();
			var start = this.getStart();
			var end = this.getEnd();
			
			var startString = "";
			var endString = "";
			if( start != null ) {
				startString = "&start="+start;
			}
			if( end != null ) {
				endString = "&end="+end;
			}
			
			var chartbox_left = this.element.offset().left+80;
			
			var imgString = webservice + "/image?uri=" + uri + startString + endString;
			
			var box = jQuery('<div style="width:800px;height:301px;border:1px solid black;" />');
			
			//add image to div
			var imgBox = jQuery('<div style="width:600px;height:300px;float:left;" />');
			var img = jQuery('<img src="'+imgString+'" />');
			imgBox.append(img);
			
			//text box
			var textBox = jQuery('<div style="width:200px;height:300px;float:right;" />');
			var dates = jQuery('<p></p>');
			var startdate = jQuery("<input type='text' style='width:75px;' />");
			var starttime = jQuery("<input type='text' value='00:00:00' style='width:60px;margin-left:5px'/>");
			var enddate = jQuery("<input type='text' style='margin-top:10px;margin-left:6px;width:75px;' />");
			var endtime = jQuery("<input type='text' value='00:00:00' style='width:60px;margin-left:5px'/>");
			dates.append("start: ");
			dates.append(startdate);
			dates.append(starttime);
			dates.append("<br/>end:  ");
			dates.append(enddate);
			dates.append(endtime);
			textBox.append("<h4 style='margin-bottom:-10px'>Dates:</h4>");
			textBox.append(dates);
			
			var selButton = jQuery('<button style="margin-left:145px;">ok</button>');
			textBox.append(selButton);
			
			var text = jQuery('<div style="border:1px solid black;width:185px;height:155px;overflow:auto;"></div>');
			text.html("<p style='margin:50px;'>click on chart!</p>");
			textBox.append("<h4 style='margin:0'>Activities:</h4>");
			textBox.append(text);
			
			//append Datepicker
			startdate.datepicker();
			enddate.datepicker();
			
			//append to box
			box.append(imgBox);
			box.append(textBox);
			
			//image overlay box
			var imgOverlay = jQuery("<div style='width:520px;height:223px;position:absolute;left:70px;top:29px;overflow:hidden;' />")
			var imgOverlayLine = jQuery("<div style='width:10px;height:100%;background-color:navy;opacity:0.2;position:absolute;top:0;left:100px;display:none;' />")
			imgOverlay.append(imgOverlayLine);
			box.append(imgOverlay);

			this.element.append(box);
			
			//on mousemove
			imgOverlay.mousemove(function(event){
				imgOverlayLine.css('left',event.pageX-chartbox_left);
			});
			
			imgOverlay.mouseover(function(){
				imgOverlayLine.show();
			});
			
			imgOverlay.mouseout(function(){
				imgOverlayLine.hide();
			});
			
			var getEventFor = function(i) {
				var stString = "";
				var enString = "";
				if( start != null ) {
					stString = "&start="+start;
				}
				if( end != null ) {
					enString = "&end="+end;
				}
				var urlStringAct = webservice + "/activities?uri="+uri+stString+enString+"&position="+i+"&width=5&jsonpCallback=?";
				jQuery.getJSON(urlStringAct, function(data) {
					var actText = "";
					if( data.length == 0 ) {
						actText = "no activities";
					}
					for( var i = 0; i < data.length; i++ ) {
						actText += "<span style='font-size:10px;font-weight:bold'>"+data[i].activity.smallDate+"</span><br/>";
						actText += "<span style='padding-left:10px'>"+data[i].activity.type+"</span><br/>";
					}
					text.html("<p style='margin:4px;'>"+actText+"</p>");
				});
			}
			
			imgOverlay.click(function(event){
					var y = parseInt(imgOverlayLine.css('left'));
					if( y < 5 ) {
						getEventFor(0)
					} else if( y > 500 ) {
						getEventFor(1);
					} else {
						getEventFor((y-5)/495);
					}
			});
			
			//in range selection
			selButton.click(function(){
				//validates
				if (startdate.val() == "") {
					alert('select a startdate first');
					return null;
				}
				
				var sdat = new Date(startdate.val()+" "+starttime.val());
				if (isNaN(sdat.getTime())) {
					alert("invalid date-time format for start");
					return null;
				}

				if (enddate.val() == "") {
					alert('select an enddate first');
					return null;
				}
				
				var edat = new Date(enddate.val()+" "+endtime.val());
				if (isNaN(edat.getTime())) {
					alert("invalid date-time format for end");
					return null;
				}
				
				if( sdat >= edat ) {
					alert("start must be befor end");
					return null;
				}
				
				if( edat > new Date() ) {
					alert("end lies ahead");
					return null;
				}
				
				//reload img
				start = Date.parse(sdat);
				end = Date.parse(edat);
				startString = "&start="+start;
				endString = "&end="+end;
				imgString = webservice + "/image?uri=" + uri + startString + endString;
				img.attr('src',imgString);
			});
		},

		getWebServiceUrl: function() {
			return this._getData("webServiceUrl");
		},
	
		getUri: function() {
			return this._getData("uri");
		},

		getStart: function() {
			return this._getData("start");
		},
		
		getEnd: function() {
			return this._getData("end");
		}
	});
	
	$.kiwi.ceqchart.defaults = {
		webServiceUrl: 'http://localhost:8080/KiWi/seam/resource/services/widgets/ceq/chart',
        uri:           'http://localhost/content/FrontPage',
		start:         null,
		end:		   null
	};
})(jQuery);
