package com.daahae.damoyeo.model;

public class MidInfo{
    private Position pos;
    private String address;
    // 구이름(Zone) 찾아내라!

    public MidInfo(Position pos, String address) {
        this.pos = pos;
        this.address = address;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}