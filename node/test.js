var getInfo = require('./MakeBuildingInfo');

module.exports.getInfo = function(req, midLat, midLng) {
    var reqObject = new Object();
    var resObject = new Object();

    reqObject = JSON.parse(req.body.userRequset);
    resObject = main(midLat, midLng, 500, reqObject.type);
    return resObject;
}
/*
//안드에서 서버로 넘겨주는 buildingType의 예시 jsonData
var jsonData = { userRequest: {
    type : 30 //cafe
}};
*/
//최종적으로 호출하는 함수.
async function main(lat, lng, radius, type) {
    console.log(await getInfo.makeBuildingInfo(lat,lng,radius,type));
}

//main(37.5502596,127.0709503,500, jsonData);
