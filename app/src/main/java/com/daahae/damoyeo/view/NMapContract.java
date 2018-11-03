package com.daahae.damoyeo.view;

import android.support.v4.app.Fragment;

import com.daahae.damoyeo.model.Person;

import java.util.ArrayList;

public interface NMapContract {

    interface Presenter extends BasePresenter {

        void initLocation(ArrayList<Person> personList);

        void stopGPSLocation();

        void getPermission(Fragment view);

        void getGPSOverlay();
    }
}
