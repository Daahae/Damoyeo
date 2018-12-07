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

    public int getSectionTime() {
        return sectionTime;
    }

    public String getTransportNumber() {
        return transportNumber;
    }
}
