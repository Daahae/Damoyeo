package com.daahae.damoyeo.model;

import com.daahae.damoyeo.view.Constant;
import com.google.gson.annotations.SerializedName;

public class Landmark {

    private static Landmark instance = new Landmark(Constant.landmark_address, Constant.landmark_latitude, Constant.landmark_longitude, Constant.landmark_name);

    private static synchronized Landmark getInstance() { return instance; }

    public static void setLandMark(Landmark landMark) {
        instance = landMark;
    }

    @SerializedName("address")
    private String address;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("name")
    private String name;

    public Landmark(String address, double latitude, double longitude, String name) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
}
