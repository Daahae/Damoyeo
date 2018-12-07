package com.daahae.damoyeo.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class UserRequest {
    @SerializedName("type")
    private int type;

    @SerializedName("midLat")
    private double midLat;
    @SerializedName("midLng")
    private double midLng;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getMidLat() {
        return midLat;
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

    @Override
    public String toString() {
        return "{" +
                "\"type\":" + type +
                ", \"midLat\":" + midLat +
                ", \"midLng\":" + midLng +
                "}";
    }
}
