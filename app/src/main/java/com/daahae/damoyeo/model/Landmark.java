package com.daahae.damoyeo.model;

import com.daahae.damoyeo.view.Constant;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Landmark {

    private static Landmark instance = new Landmark(Constant.LANDMARK_LOCATION, Constant.landmark_name, Constant.landmark_address);

    public static synchronized Landmark getInstance() { return instance; }

    public static void setLandMark(Landmark landMark) {
        instance = landMark;
    }

    private LatLng latLng;

    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;


    public Landmark (LatLng latLng, String name, String address) {
        this.latLng = latLng;
        this.name = name;
        this.address = address;
    }

    public Landmark(double latitude, double longitude, String name, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
