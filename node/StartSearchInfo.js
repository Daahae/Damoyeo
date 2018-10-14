var searchSightseeing = require('./SearchSightseeing');
var searchShopping = require('./SearchShopping');
var searchHealing = require('./SearchHealing');
var searchRestaurant = require('./SearchRestaurant');
var searchCafe = require('./SearchCafe');
var searchBar = require('./SearchBar');


exports.startSearch = async function(lat, lng, radius, type) {

    var jsonData;

    if(type == "쇼핑몰") {

        jsonData = await searchShopping.SearchMall(lat,lng,radius);
        return jsonData;

    } else if (type == "백화점") {

        jsonData = await searchShopping.SearchStore(lat, lng, radius);
        return jsonData;

    } else if(type == "음식점") {

        jsonData = await searchRestaurant.SearchRestaurant(lat, lng, radius);
        return jsonData;

    } else if(type == "카페") {

        jsonData = await searchCafe.SearchCafe(lat, lng, radius);
        return jsonData;

    } else if(type == "주점") {

        jsonData = await searchBar.SearchBar(lat,lng,radius);
        return jsonData;

    } else if(type == "박물관") { 

        jsonData = await searchSightseeing.SearchMuseum(lat,lng,radius);
        return jsonData;

    } else if(type == "동물원") {

        jsonData = await searchSightseeing.SearchZoo(lat,lng,radius);
        return jsonData;

    } else if(type == "경기장") {

        jsonData = await searchSightseeing.SearchStadium(lat,lng,radius);
        return jsonData;

    } else if(type == "영화관") {

        jsonData = await searchSightseeing.SearchTheater(lat,lng,radius);
        return jsonData;

    } else if(type == "아쿠아리움") {

        jsonData = await searchSightseeing.SearchAquarium(lat,lng,radius);
        return jsonData;

    } else if(type == "캠핑장") {

        jsonData = await searchHealing.SearchCampground(lat, lng, radius);
        return jsonData;

    } else if(type == "놀이공원") {

        jsonData = await searchHealing.SearchAmusementPark(lat,lng,radius);
        return jsonData;

    } else if(type == "공원") {

        jsonData = await searchHealing.SearchPark(lat, lng, radius);
        return jsonData;

    } else if(type == "스파") {

        jsonData = await searchHealing.SearchSpa(lat, lng, radius);
        return jsonData;

    } 

}
        
