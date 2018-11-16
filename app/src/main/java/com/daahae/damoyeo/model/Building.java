package com.daahae.damoyeo.model;

import com.google.gson.annotations.SerializedName;

public class Building{
    // targetBuilding
    private static Building instance = null;

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
