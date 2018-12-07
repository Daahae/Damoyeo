package com.daahae.damoyeo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BuildingArr {

    @SerializedName("buildingArr")
    private List<Building> buildingArr;
    /*
    @SerializedName("midLat")
    private double midLat;
    @SerializedName("midLng")
    private double midLng;
    */
    public List<Building> getBuildingArr() {
        return buildingArr;
    }
    /*
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
    */
}
