package com.daahae.damoyeo.model;

import com.google.gson.annotations.SerializedName;

public class Building{

    @SerializedName("buildingLat")
    private double latitude;

    @SerializedName("buildingLng")
    private double longitude;

    @SerializedName("buildingName")
    private String name;

    @SerializedName("buildingAddress")
    private String buildingAddress;

    @SerializedName("buildingTel")
    private String tel;

    @SerializedName("buildingDescription")
    private String description;

    @SerializedName("buildingDistance")
    private double distance;

    public Building(double latitude, double longitude, String name, String buildingAddress, String tel, String description, double distance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.buildingAddress = buildingAddress;
        this.tel = tel;
        this.description = description;
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public double getDistance() {
        return distance;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
