package com.daahae.damoyeo.model;

public class Landmark extends MidInfo{//MidInfo의 'sectorNmae' 을 받아 검색하기 위함

    private Position landmarkPos;
    private String sectorName;


    public Landmark(Position pos, String address, Position landmarkPos, String sectorName) {
        super(pos, address);
        this.landmarkPos = landmarkPos;
        this.sectorName = sectorName;
    }

    public Position getLandmarkPos() {
        return landmarkPos;
    }

    public void setLandmarkPos(Position landmarkPos) {
        this.landmarkPos = landmarkPos;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

}
