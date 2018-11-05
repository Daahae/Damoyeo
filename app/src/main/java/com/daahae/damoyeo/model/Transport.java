package com.daahae.damoyeo.model;

import com.google.gson.annotations.SerializedName;

public class Transport{

    @SerializedName("trafficType")
    private int trafficType;
    @SerializedName("sectionTime")
    private int sectionTime;
    @SerializedName("transportNumber")
    private String transportNumber;
    @SerializedName("startStation")
    private String startStation;
    @SerializedName("endStation")
    private String endStation;

    public int getTrafficType() {
        return trafficType;
    }

    public void setTrafficType(int trafficType) {
        this.trafficType = trafficType;
    }

    public int getSectionTime() {
        return sectionTime;
    }

    public void setSectionTime(int sectionTime) {
        this.sectionTime = sectionTime;
    }

    public String getTransportNumber() {
        return transportNumber;
    }

    public void setTransportNumber(String transportNumber) {
        this.transportNumber = transportNumber;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }
}
