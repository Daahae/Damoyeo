package com.daahae.damoyeo.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data{

    @SerializedName("subPathArr")
    private ArrayList<Transport> transportInfo;
    @SerializedName("totalTime")
    private int totalTime;
    @SerializedName("timeBySubway")
    private int timeBySubway;
    @SerializedName("timeByBus")
    private int timeByBus;
    @SerializedName("timeByWalk")
    private int timeByWalk;
    @SerializedName("error")
    private String error;

    public ArrayList<Transport> getTransportInfo() {
        return transportInfo;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getTimeBySubway() {
        return timeBySubway;
    }

    public int getTimeByBus() {
        return timeByBus;
    }

    public int getTimeByWalk() {
        return timeByWalk;
    }

    public String getError() {
        return error;
    }
}