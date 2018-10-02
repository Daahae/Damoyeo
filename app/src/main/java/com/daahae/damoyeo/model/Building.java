package com.daahae.damoyeo.model;

public class Building{
    private Position pos;
    private String address;
    private int type;
    private Position buildingPos;
    private String name;
    private String buildingAddress;
    private String tel;

    public Building(Position pos, String address, int type, Position buildingPos, String name, String buildingAddress, String tel) {
        this.pos = pos;
        this.address = address;
        this.type = type;
        this.buildingPos = buildingPos;
        this.name = name;
        this.buildingAddress = buildingAddress;
        this.tel = tel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Position getBuildingPos() {
        return buildingPos;
    }

    public void setBuildingPos(Position buildingPos) {
        this.buildingPos = buildingPos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}