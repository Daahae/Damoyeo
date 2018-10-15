var searchBuilding = require('./SearchBuilding');

exports.nearbySearch = async function(lat, lng, radius, type) { //위도, 경도, 반경, 타입(cafe, shopping_mall, bar 등등)

    var jsonData = await searchBuilding.SearchBuilding(lat, lng, radius, type);
    return jsonData;

}