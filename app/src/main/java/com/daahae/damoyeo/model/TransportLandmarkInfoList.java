package com.daahae.damoyeo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TransportLandmarkInfoList {

    private static TransportLandmarkInfoList instance = new TransportLandmarkInfoList();

    public static synchronized TransportLandmarkInfoList getInstance() {
        return instance;
    }

    @SerializedName("userArr")
    private List<Data> userArr;

    private ArrayList<String> error;

    public List<Data> getUserArr() {
        return userArr;
    }

    public void setUserArr(List<Data> userArr) {
        this.userArr = userArr;
    }

}