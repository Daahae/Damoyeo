package com.daahae.damoyeo.model;

import com.google.gson.annotations.SerializedName;

public class Landmark {
    @SerializedName("address")
    private String address;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("name")
    private String name;

}
