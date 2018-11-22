package com.daahae.damoyeo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TransportInfoList {

    private static TransportInfoList instance = new TransportInfoList();

    public static synchronized TransportInfoList getInstance() {
        return instance;
    }

    @SerializedName("userArr")
    private List<Data> userArr;
    @SerializedName("landmark")
    private Landmark landmark;
    @SerializedName("midInfo")
    private MidInfo midInfo;

    public List<Data> getUserArr() {
        return userArr;
    }

    public void setUserArr(List<Data> userArr) {
        this.userArr = userArr;
    }
    public Landmark getLandmark() {
        return landmark;
    }

    public void setLandmark(Landmark landmark) {
        this.landmark = landmark;
    }

    public MidInfo getMidInfo() {
        return midInfo;
    }

    public void setMidInfo(MidInfo midInfo) {
        this.midInfo = midInfo;
    }

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

    }
}