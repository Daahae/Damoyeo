package com.daahae.damoyeo.model;

public class Transport{
    private int type;
    private int time;
    private String startStation;
    private String endStation;
    private String transportNumber;

    public Transport(int type, int time, String startStation, String endStation, String transportNumber) {
        this.type = type;
        this.time = time;
        this.startStation = startStation;
        this.endStation = endStation;
        this.transportNumber = transportNumber;
    }


    public Transport(int type, int time, String startStation, String endStation) {
        this.type = type;
        this.time = time;
        this.startStation = startStation;
        this.endStation = endStation;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTransportNumber() {
        return transportNumber;
    }

    public void setTransportNumber(String transportNumber) {
        this.transportNumber = transportNumber;
    }
}
