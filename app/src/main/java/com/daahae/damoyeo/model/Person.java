package com.daahae.damoyeo.model;

import java.util.ArrayList;

public class Person{
    private static ArrayList<Person> instance = new ArrayList<Person>();
    private int id;
    private String name;
    private String address;
    private Position addressPosition; // 주소의 좌표

    public Person(String name, String address, Position addressPosition) {
        this.name = name;
        this.address = address;
        this.addressPosition = addressPosition;
    }

    public static synchronized ArrayList<Person> getInstance(){
        return instance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Position getAddressPosition() {
        return addressPosition;
    }

    public void setAddressPosition(Position addressPosition) {
        this.addressPosition = addressPosition;
    }
}
