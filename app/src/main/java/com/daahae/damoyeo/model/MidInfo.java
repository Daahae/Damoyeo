package com.daahae.damoyeo.model;

import com.daahae.damoyeo.view.Constant;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class MidInfo{

    private static MidInfo instance = new MidInfo(Constant.DEFAULT_LOCATION,Constant.address);

    public static synchronized MidInfo getInstance() { return instance; }

    public static void setMidInfo(MidInfo midInfo) {
        instance = midInfo;
    }

    private LatLng latLng;
    private Position pos;
    @SerializedName("midLat")
    private double midLat;
    @SerializedName("midLng")
    private double midLng;
    @SerializedName("address")
    private String address;

    public MidInfo(LatLng latLng, String address) {
        this.latLng = latLng;
        this.address = address;
    }

    public MidInfo(double midLat, double midLng, String address) {
        this.midLat = midLat;
        this.midLng = midLng;
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public double getMidLat() {
        return midLat;
    }

    public Position getPos() {
        return pos;
    }
    public void setMidLat(double midLat) {
        this.midLat = midLat;
    }

    public double getMidLng() {
        return midLng;
    }

    public void setMidLng(double midLng) {
        this.midLng = midLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
