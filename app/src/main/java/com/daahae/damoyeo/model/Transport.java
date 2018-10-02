package com.daahae.damoyeo.model;

public class Transport{
    private int type;
    private int time;
    private String start;
    private String end;
    private int transportNumber;

    public Transport(int type, int time, String start, String end, int transportNumber) {
        this.type = type;
        this.time = time;
        this.start = start;
        this.end = end;
        this.transportNumber = transportNumber;
    }


    public Transport(int type, int time, String start, String end) {
        this.type = type;
        this.time = time;
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }



    public void setEnd(String end) {
        this.end = end;
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

    public int getTransportNumber() {
        return transportNumber;
    }

    public void setTransportNumber(int transportNumber) {
        this.transportNumber = transportNumber;
    }
}
