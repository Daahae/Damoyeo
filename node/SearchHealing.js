var getInfo = require('./googleTest.js');

exports.SearchCampground = async function (lat, lng, radius) {
    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"campground");
    var jsonObject = JSON.parse(jsonTest);
    
    while(JSON.stringify(jsonObject.Info) == "[]") {
        var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"campground");
        var jsonObject = JSON.parse(jsonTest);
    }
    
    return jsonObject;
}

exports.SearchAmusementPark = async function (lat, lng, radius) {
    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"amusement_park");
    var jsonObject = JSON.parse(jsonTest);
    
    while(JSON.stringify(jsonObject.Info) == "[]") {
        var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"amusement_park");
        var jsonObject = JSON.parse(jsonTest);
    }
    
    return jsonObject;
}

exports.SearchPark = async function (lat, lng, radius) {
    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"park");
    var jsonObject = JSON.parse(jsonTest);
    
    while(JSON.stringify(jsonObject.Info) == "[]") {
        var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"park");
        var jsonObject = JSON.parse(jsonTest);
    }
    
    return jsonObject;
}

exports.SearchSpa = async function (lat, lng, radius) {
    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"spa");
    var jsonObject = JSON.parse(jsonTest);
    
    while(JSON.stringify(jsonObject.Info) == "[]") {
        var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"spa");
        var jsonObject = JSON.parse(jsonTest);
    }
    
    return jsonObject;
}