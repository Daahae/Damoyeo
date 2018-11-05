var getInfo = require('./GetInfoByCategory.js');

exports.nearbySearch = async function(lat, lng, radius, type) { //위도, 경도, 반경, 타입(cafe, shopping_mall, bar 등등)

    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,type);
    var jsonObject = JSON.parse(jsonTest);

    while(JSON.stringify(jsonObject.Info) == "[]") { // 요청한 값을 제대로 받지 못하면, 다시 요청
        jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,type);
        jsonObject = JSON.parse(jsonTest);
    }

    return jsonObject;
}