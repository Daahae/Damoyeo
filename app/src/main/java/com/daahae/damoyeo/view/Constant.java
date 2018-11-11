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

    public static final double landmark_latitude = 37.563228;
    public static final double landmark_longitude = 126.9851932;
    public static final LatLng LANDMARK_LOCATION = new LatLng(landmark_latitude, landmark_longitude);
    public static final String landmark_name = "명동성당";
    public static final String landmark_address = "서울특별시 중구 저동1가 명동길 74";
    public static final String landmark_tel = "02-774-1784";

    // NMapActivityPresenter
    public static final int NMAP_PAGE = 101;
    public static final int SELECT_MID_PAGE = 102;
    public static final int CATEGORY_PAGE = 103;

    // SelectMidFragmentPresenter
    public static final int MID_ALGORITHM = 111;
    public static final int LANDMARK = 112;
}
