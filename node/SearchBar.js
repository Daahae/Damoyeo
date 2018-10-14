var getInfo = require('./googleTest.js');

exports.SearchBar = async function (lat, lng, radius) {
    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"bar");
    var jsonObject = JSON.parse(jsonTest);
    
    while(JSON.stringify(jsonObject.Info) == "[]") {
        var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"bar");
        var jsonObject = JSON.parse(jsonTest);
    }

    return jsonObject;
}