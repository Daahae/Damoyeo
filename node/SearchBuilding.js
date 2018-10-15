var getInfo = require('./GetInfoByCategory.js');

exports.SearchBuilding = async function (lat, lng, radius, type) {
    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,type);
    var jsonObject = JSON.parse(jsonTest);

    while(JSON.stringify(jsonObject.Info) == "[]") {
        var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,type);
        var jsonObject = JSON.parse(jsonTest);
    }

    return jsonObject;
}