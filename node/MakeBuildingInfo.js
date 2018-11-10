var search = require('./NearbySearch.js');
var getInfo = require('./GetDetailInfo.js');
/* 최종 JSON 오브젝트 형식
{ buildingArr:
    [ { buildingLat:,
        buildingLng:,
        buildingName:,
        buildingAddress:,
        buildingTel:,
        buildingDescription:,
        buildingDistance:},
        {...}
    ]
}
*/
exports.makeBuildingInfo = async function(lat, lng, radius, type) {
    var jsonObject_From_Google = await search.nearbySearch(lat, lng, radius, type);
    var object = new Object();
    var item = 'buildingArr';
    object[item] = [];
    var data;

    var jsonObject_From_Naver = new Array();
    
    for(var i=0; i<jsonObject_From_Google.Info.length; i++) {
        jsonObject_From_Naver[i] = await getInfo.getDetailInfo(jsonObject_From_Google.Info[i].name);        
    }
    
    for(var i=0; i<jsonObject_From_Google.Info.length; i++) {
        if(jsonObject_From_Naver[i].total == 0) continue;
        data = {
            buildingLat : jsonObject_From_Google.Info[i].lat,
            buildingLng : jsonObject_From_Google.Info[i].lng,
            buildingName : jsonObject_From_Google.Info[i].name,
            buildingAddress : jsonObject_From_Naver[i].items[0]["address"],
            buildingTel : jsonObject_From_Naver[i].items[0]["telephone"],
            buildingDescription : jsonObject_From_Naver[i].items[0]["description"],
            buildingDistance : calculateDistance(lat, lng, jsonObject_From_Google.Info[i].lat, jsonObject_From_Google.Info[i].lng)
        };

        object[item].push(data);
    }
    
    return object;
}

//위도와 경도로 실제 거리 구하는 함수
function calculateDistance(lat1, lon1, lat2, lon2) {
    var R = 6371; // km
    var dLat = (lat2-lat1).toRad();
    var dLon = (lon2-lon1).toRad(); 
    var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
            Math.cos(lat1.toRad()) * Math.cos(lat2.toRad()) * 
            Math.sin(dLon/2) * Math.sin(dLon/2); 
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
    var d = R * c;
    return d;
  }

  Number.prototype.toRad = function() {
    return this * Math.PI / 180;
  }