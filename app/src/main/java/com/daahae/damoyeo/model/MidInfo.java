package com.daahae.damoyeo.model;

import com.daahae.damoyeo.view.Constant;
import com.google.android.gms.maps.model.LatLng;

public class MidInfo{

    private static MidInfo instance = new MidInfo(Constant.DEFAULT_LOCATION, null);

    public static synchronized MidInfo getInstance() { return instance; }

    public static void setMidInfo(MidInfo midInfo) {
        instance = midInfo;
    }

    private LatLng latLng;
    private Position pos;
    private String address;

    private MidInfo(LatLng latLng, String address) {
        this.latLng = latLng;
        this.address = address;
    }

    private MidInfo(Position pos, String address) {
        this.pos = pos;
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
