//네이버 오픈 api 테스트
//네이버 api에 장소이름을 보내서 주소, 전화번호, description를 받아와야함

var request = require('sync-request');

exports.getDetailInfo = function (name) {
    //var clientID = "";
    //var secret = "";

    var clientID = "";
    var secret = "";
    
    var url = `https://openapi.naver.com/v1/search/local.json?query=` + encodeURI(name) + `&display=1&start=1`;
    var options = {
        headers: {
            'X-Naver-Client-Id': clientID,
            'X-Naver-Client-Secret': secret
        }
    };

    var res = request('GET', url, options);  
    var jsonObject = JSON.parse(res.getBody());

    
    //return JSON.stringify(jsonObject);
    return jsonObject;
}

