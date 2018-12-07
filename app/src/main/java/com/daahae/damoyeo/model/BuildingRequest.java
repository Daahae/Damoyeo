package com.daahae.damoyeo.model;

import com.google.gson.annotations.SerializedName;

public class BuildingRequest {
    @SerializedName("name")
    private String name;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lng")
    private double longitude;

    public BuildingRequest() {
    }

    public BuildingRequest(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + "\""+
                ", \"lat\":" + latitude +
                ", \"lng\":" + longitude +
                "}";
    }
}
