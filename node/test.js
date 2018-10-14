var search = require('./StartSearchInfo');

//최종적으로 호출하는 함수.
async function main(lat, lng, radius, type) {
    //var temp = await search.startSearch(lat,lng,radius,type);
    //return temp;
    console.log(await search.startSearch(lat,lng,radius,type));
}

main(37.5502596,127.0709503,15000,"쇼핑몰");