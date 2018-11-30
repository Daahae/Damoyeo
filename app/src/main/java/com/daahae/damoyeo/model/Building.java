package com.daahae.damoyeo.model;

import com.daahae.damoyeo.view.Constant;
import com.google.gson.annotations.SerializedName;

public class Building{

    // DetailBuilding
    private static Building instance = new Building(Constant.landmark_latitude, Constant.landmark_longitude, Constant.landmark_name, Constant.landmark_address, Constant.distance);

    public static synchronized Building getInstance() {
        return instance;
    }

    public static void setInstance(Building instance) {
        Building.instance = instance;
    }

    @SerializedName("buildingLat")
    private double latitude;

    @SerializedName("buildingLng")
    private double longitude;

    @SerializedName("buildingName")
    private String name;

    @SerializedName("buildingAddress")
    private String buildingAddress;

    @SerializedName("buildingDistance")
    private double distance;

    public Building(double latitude, double longitude, String name, String buildingAddress, double distance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.buildingAddress = buildingAddress;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    @Override
    public String toString() {
        return "Building{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", name='" + name + '\'' +
                ", buildingAddress='" + buildingAddress + '\'' +
                ", distance=" + distance +
                '}';
    }
}
