var getInfo = require('./googleTest.js');

exports.SearchMuseum = async function (lat, lng, radius) {
    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"museum");
    var jsonObject = JSON.parse(jsonTest);
    
    while(JSON.stringify(jsonObject.Info) == "[]") {
        var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"museum");
        var jsonObject = JSON.parse(jsonTest);
    }
    
    return jsonObject;
}

exports.SearchZoo = async function (lat, lng, radius) {
    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"zoo");
    var jsonObject = JSON.parse(jsonTest);
    
    while(JSON.stringify(jsonObject.Info) == "[]") {
        var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"zoo");
        var jsonObject = JSON.parse(jsonTest);
    }
    
    return jsonObject;
}

exports.SearchStadium = async function (lat, lng, radius) {
    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"stadium");
    var jsonObject = JSON.parse(jsonTest);
    
    while(JSON.stringify(jsonObject.Info) == "[]") {
        var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"stadium");
        var jsonObject = JSON.parse(jsonTest);
    }
    
    return jsonObject;
}

exports.SearchTheater = async function (lat, lng, radius) {
    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"movie_theater");
    var jsonObject = JSON.parse(jsonTest);
    
    while(JSON.stringify(jsonObject.Info) == "[]") {
        var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"movie_theater");
        var jsonObject = JSON.parse(jsonTest);
    }
    
    return jsonObject;
}

exports.SearchAquarium = async function (lat, lng, radius) {
    var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"aquarium");
    var jsonObject = JSON.parse(jsonTest);
    
    while(JSON.stringify(jsonObject.Info) == "[]") {
        var jsonTest = await getInfo.getInfoByCategory(lat,lng, radius,"aquarium");
        var jsonObject = JSON.parse(jsonTest);
    }
    
    return jsonObject;
}
