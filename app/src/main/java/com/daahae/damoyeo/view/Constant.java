package com.daahae.damoyeo.view;

import android.content.Context;

import com.daahae.damoyeo.model.Position;
import com.google.android.gms.maps.model.LatLng;

public class Constant {
    public static final String TAG = "damoyeo_gmap";
    public static final String URL = "http://13.125.192.103/";
    public static final String ALGORITHM_ERROR = "{\"error\":\"Algorithm Error\"}";

    public static final int LOG_IN = 9001;
    public static final int LOG_OUT = 9999;

    public static final String LOGIN = "Login";
    public static final int GOOGLE_LOGIN = 901;
    public static final int GUEST_LOGIN = 902;

    public static final int GPS_ENABLE_REQUEST_CODE = 2001;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    public static final int UPDATE_INTERVAL_MS = 15000;
    public static final int FASTEST_UPDATE_INTERVAL_MS = 15000;

    // default location
    public static final double latitude = 37.56666056421257;
    public static final double longitude = 126.97835471481085;
    public static final Position DEFAULT_POSITION = new Position(latitude, longitude);
    public static final LatLng DEFAULT_LOCATION = new LatLng(latitude, longitude);
    public static final String name = "세종대로";
    public static final String address = "서울특별시 중구";
    public static final String tel = "02-1234-5678";
    public static final String description = null;
    public static final double distance = 0;

    public static final String DEFAULT_MIDINFO_NAME = "중간지점";

    public static final double landmark_latitude = 37.56318972577066;
    public static final double landmark_longitude = 126.98734413832425;
    public static final LatLng LANDMARK_LOCATION = new LatLng(landmark_latitude, landmark_longitude);
    public static final String landmark_name = "명동성당";
    public static final String landmark_address = "서울특별시 중구 저동1가 명동길 74";
    public static final String landmark_tel = "02-774-1784";

    // MainActivity
    public static final int MAPS_PAGE = 101;
    public static final int SELECT_MID_PAGE = 102;
    public static final int CATEGORY_PAGE = 103;
    public static final int DETAIL_PAGE = 104;

    // Category
    public static final int DEPARTMENT_STORE = 11;
    public static final int SHOPPING_MALL = 12;
    public static final int STADIUM = 21;
    public static final int ZOO = 22;
    public static final int MUSEUM = 23;
    public static final int MOVIE_THEATER = 24;
    public static final int AQUARIUM = 25;
    public static final int CAFE = 30;
    public static final int DRINK = 40;
    public static final int RESTAURANT = 50;
    public static final int ETC = 60;

    public static final int SEARCH_FRIENDS_MODE = 1000;
    public static final int FRIENDS_LIST_MODE = 2000;

    public static final int SUBWAY = 1;
    public static final int BUS = 2;
    public static final int WALK = 3;

    public static int displayWidth;

    public static Context context;

    public static boolean existPerson = false;
    public static boolean existLandmarkTransport = false;
    public static boolean existTransport= false;
    public static boolean existBuilding = false;
}
