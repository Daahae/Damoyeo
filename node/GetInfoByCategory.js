var request = require('sync-request');

exports.getInfoByCategory = function (lat, lng, radius, type) {
      //var key = "AIzaSyBI4jrohgui2UIXOPf-qRmZi8wSbu4GX6Q";
      var key = "AIzaSyBjmChvkWBs63KJPrp5bu1dCY3H-ON3Idc";
      var url = `https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=`+lat+`,`+lng+`&radius=` + radius + `&type=`+ type +`&key=` + key + `&language=ko`;
      
      var res = request('GET', url);
      
      var jsonObject = JSON.parse(res.getBody());
      
      var object = new Object();
      var item = 'Info';
      object[item] = [];
      
      var data;
      
      for(var i=0; i<jsonObject.results.length; i++) {
         if(type == "cafe" || type == "bar") { // 간혹 결과에 헤어샵이 포함되는 경우가 있어, 걸러주는 작업
            if(jsonObject.results[i].types.toString().indexOf("hair") == -1) {
               data = {
                  name : jsonObject.results[i].name,
                  lat : jsonObject.results[i].geometry.location.lat,
                  lng : jsonObject.results[i].geometry.location.lng
               };
               
               object[item].push(data);
            }  
         } else {
            data = {
               name : jsonObject.results[i].name,
               lat : jsonObject.results[i].geometry.location.lat,
               lng : jsonObject.results[i].geometry.location.lng
            };
            
            object[item].push(data);
         }
	  }
        return JSON.stringify(object);
   
}