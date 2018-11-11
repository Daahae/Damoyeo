package com.daahae.damoyeo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BuildingArr {

    @SerializedName("buildingArr")
    private List<Building> buildingArr;

    public List<Building> getBuildingArr() {
        return buildingArr;
    }
}
