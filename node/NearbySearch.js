/*
var GetInfoByCategory = require('./GetInfoByCategory.js');
var settingCategory = require('./SetCategory.js');
var deasync = require('deasync');

//lat, lng는 알고리즘에서 나온 결과값, radius는 고정값
//type은 userRequest: {type : 30} 과 같은 형식의 json오브젝트에서 따오기.

exports.getInfo = function(req, lat, lng) { //위도, 경도, 반경, 타입(cafe, shopping_mall, bar 등등)
    var reqObject = new Object();
    console.log(req.body);
    reqObject = JSON.parse(req.body.userRequest);// b 소문자로


    var category = settingCategory.setCategory(reqObject.type);
    var jsonTest = GetInfoByCategory.getInfoByCategory(lat, lng, 500, category );
    var jsonObject = JSON.parse(jsonTest);

    console.log(jsonObject);

    return jsonObject;
}
*/
var GetInfoByCategory = require('./GetInfoByCategory.js');
var SetCategory = require('./SetCategory.js');
var deasync = require('deasync');


//lat, lng는 알고리즘에서 나온 결과값, radius는 고정값


exports.nearbySearch = function(lat, lng, radius, type) { //위도, 경도, 반경, 타입(cafe, shopping_mall, bar 등등)
    var category = SetCategory.setCategory(type);
    var jsonTest = GetInfoByCategory.getInfoByCategory(lat, lng, radius, category);

    var jsonObject = JSON.parse(jsonTest);
    
    return jsonObject;
}