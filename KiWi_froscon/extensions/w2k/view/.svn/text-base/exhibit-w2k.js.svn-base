 function w2kConverter( json ) {
	 var hiddenProps=[
 		 "kiwi:hasRatingDataFacades",
         "kiwi:Database-Primary-Key",
 		 "kiwi:hasTextContent",
 		 "kiwi:language",
 		 "resourceUri",
 		 "kiwi:createdOn"
         ];

   var items = json.items;
   // iterate through all items
   for (var i = 0; item = items[i]; i++) {
		// extract the year
		if(item["waysknow:starts-at"]!=null)
			item.startYear = String(parseDate(item["waysknow:starts-at"]).getFullYear());

		// extract the year
		if(item["waysknow:ends-at"]!=null)
			item.endYear = String(parseDate(item["waysknow:ends-at"]).getFullYear());
		
		var lat = item["geo:lat"]!=null?item["geo:lat"]:item["geo:latitude"];
		var long = item["geo:long"]!=null?item["geo:long"]:item["geo:longitude"];		
		if(lat!=null & long!=null)
			item.latlong=lat + "," + long;
		// concatenate the lists in .tags
		item["tags"]=
			concatList(
				concatList(
					concatList(item["waysknow:theme"],
						item["hgtags:taggedWithTag"]),
				item["waysknow:research-area"]),
			item["waysknow:target-area"]);

		if(item.resourceUri != null){
			var link = kiwiRootPath + "/home.seam?uri=" + escape(item.resourceUri);
			item.link="<a href='" + link + "'>" +item.label + "</a>" ;
		}

		for(var j=0; prop = hiddenProps[j];j++)
			delete item[prop];
   }
   return json;
 }

 // primitive list concatenation 
 // return [a1,a2,..,an,b1,b2,..,bm]
 function concatList(a, b){
	var res = [];
	if(a==null)a=[];
	if(b==null)b=[];
	
	for(var i=0; i < a.length; i++){
		res[res.length] = a[i];
	}
	for(var i=0; i < b.length;i++){
		res[res.length] = b[i];
	}
	return res;
 }
 
 // ISO-8601 Date parser (from http://dev.ektron.com/blogs.aspx?id=14140)
 function parseDate(xmlDate)
 {
       if (!/^[0-9]{4}\-[0-9]{2}\-[0-9]{2}/.test(xmlDate)) {
            throw new RangeError("xmlDate must be in ISO-8601 format YYYY-MM-DD.");
       }
       return new Date(xmlDate.substring(0,4), xmlDate.substring(5,7)-1, xmlDate.substring(8,10));
 }
