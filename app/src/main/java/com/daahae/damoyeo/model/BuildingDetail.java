package com.daahae.damoyeo.model;

import com.google.gson.annotations.SerializedName;

public class BuildingDetail {

    @SerializedName("buildingTel")
    private String buildingTel;

    @SerializedName("buildingDescription")
    private String buildingDescription;

    public String getBuildingTel() {
        return buildingTel;
    }

    public String getBuildingDescription() {
        return buildingDescription;
    }
}
