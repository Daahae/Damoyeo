package com.daahae.damoyeo.presenter.Contract;

import android.support.v4.app.Fragment;

import com.daahae.damoyeo.model.Person;

import java.util.ArrayList;

public interface BaseNMap {
    
    void init(Fragment view);

    void initLocation(ArrayList<Person> personList);

    void stopGPSLocation();

    void getPermission(Fragment view);

    void getGPSOverlay();
}
