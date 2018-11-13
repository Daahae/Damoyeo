package com.daahae.damoyeo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TransportInfoList {

    @SerializedName("userArr")
    private List<Data> userArr;

    public List<Data> getUserArr() {
        return userArr;
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
        @SerializedName("landmark")
        private ArrayList<Landmark> landmark;
        @SerializedName("midLat")
        private double midLat;
        @SerializedName("midLng")
        private double midLng;


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

        public ArrayList<Landmark>  getLandmark() {
            return landmark;
        }

        public double getMidLat() {
            return midLat;
        }

        public double getMidLng() {
            return midLng;
        }
    }
}