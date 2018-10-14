var getInfo = require('./googleTest.js');

exports.SearchCafe = async function (lat, lng, radius) {
    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"cafe");
    var jsonObject = JSON.parse(jsonTest);

    while(JSON.stringify(jsonObject.Info) == "[]") {
        var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"cafe");
        var jsonObject = JSON.parse(jsonTest);
    }

    return jsonObject;
}