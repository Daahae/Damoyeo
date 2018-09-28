package com.daahae.damoyeo.model;

import java.util.ArrayList;

public class Group {
    private ArrayList<Person> users;

    public Group(ArrayList<Person> users) {
        this.users = users;
    }

    public ArrayList<Person> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Person> users) {
        this.users = users;
    }
}
