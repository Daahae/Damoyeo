package com.daahae.damoyeo.view;

import com.google.android.gms.maps.model.LatLng;

public class Constant {
    public static final String TAG = "googlemap_damoyeo";

    public static final int GPS_ENABLE_REQUEST_CODE = 2001;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    public static final int UPDATE_INTERVAL_MS = 15000;
    public static final int FASTEST_UPDATE_INTERVAL_MS = 15000;

    // default location
    public static final double latitude = 37.5666091;
    public static final double longitude = 126.978371;
    public static final LatLng DEFAULT_LOCATION = new LatLng(latitude, longitude);
    public static final String name = "세종대로";
    public static final String address = "서울특별시 중구";
    public static final String tel = "02-1234-5678";
    public static final String description = null;
    public static final double distance = 0;

    public static final double landmark_latitude = 37.563228;
    public static final double landmark_longitude = 126.9851932;
    public static final LatLng LANDMARK_LOCATION = new LatLng(landmark_latitude, landmark_longitude);
    public static final String landmark_name = "명동성당";
    public static final String landmark_address = "서울특별시 중구 저동1가 명동길 74";
    public static final String landmark_tel = "02-774-1784";

    // MapsActivityPresenter
    public static final int NMAP_PAGE = 101;
    public static final int SELECT_MID_PAGE = 102;
    public static final int CATEGORY_PAGE = 103;
    public static final int DETAIL_PAGE = 104;

    // SelectMidFragmentPresenter
    public static final int MID_ALGORITHM = 111;
    public static final int LANDMARK = 112;

    // DetailFragmentPresenter
    public static final int DEPARTMENT_STORE = 11;
    public static final int SHOPPING_MALL = 12;
    public static final int STADIUM = 21;
    public static final int ZOO = 22;
    public static final int MUSEUM = 23;
    public static final int MOVIE_THEATER = 24;
    public static final int AQUARIUM = 25;
    public static final int CAFE = 30;
    public static final int BAR = 40;
    public static final int RESTAURANT = 50;

    public static final int SEARCH_FRIENDS_MODE = 1000;
    public static final int FRIENDS_LIST_MODE = 2000;
}
