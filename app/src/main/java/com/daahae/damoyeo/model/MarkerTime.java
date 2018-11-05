package com.daahae.damoyeo.model;


public class MarkerTime{
    private String name;
    private String totalTime;

    public MarkerTime(String name, String totalTime) {
        this.name = name;
        this.totalTime = totalTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
}