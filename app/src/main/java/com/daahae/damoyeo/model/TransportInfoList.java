package com.daahae.damoyeo.model;

import java.util.ArrayList;

public class TransportInfoList{
    private ArrayList<Transport> transportInfo;
    private int totalTime;
    private Person person;

    public TransportInfoList(ArrayList<Transport> transportInfo, int totalTime, Person person) {
        this.transportInfo = transportInfo;
        this.totalTime = totalTime;
        this.person = person;
    }

    public ArrayList<Transport> getTransportInfo() {
        return transportInfo;
    }

    public void setTransportInfo(ArrayList<Transport> transportInfo) {
        this.transportInfo = transportInfo;
        this.totalTime = getTotal();
    }

    public int getTotalTime() {
        return totalTime;
    }

    // TODO 메소드명 변경
    private int getTotal(){
        int totalTotal = 0;

        for(Transport transport : transportInfo)
            totalTotal += transport.getTime();

        return totalTotal;
    }
    public Person getPerson(){
        return person;
    }


}