package com.daahae.damoyeo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Position{

    @SerializedName("latitude")
    @Expose
    private double x;
    @SerializedName("longitude")
    @Expose
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" +
                "\"latitude\":" + x +
                ", \"longitude\":" + y +
                '}';
    }
}
