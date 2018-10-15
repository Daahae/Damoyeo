var search = require('./NearbySearch');

//최종적으로 호출하는 함수.
async function main(lat, lng, radius, type) {
    console.log(await search.nearbySearch(lat,lng,radius,type));
}

main(37.5502596,127.0709503,1500,"movie_theater");