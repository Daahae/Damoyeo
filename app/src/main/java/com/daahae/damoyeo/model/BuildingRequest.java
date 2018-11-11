package com.daahae.damoyeo.model;

import com.google.gson.annotations.SerializedName;

public class BuildingRequest {

    @SerializedName("type")
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{" +
                "type=" + type +
                '}';
    }
}
