package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.view.View;

import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;
import com.daahae.damoyeo.view.adapter.TransportAdapter;
import com.daahae.damoyeo.view.fragment.DetailFragment;
import com.nhn.android.maps.NMapContext;

import java.util.ArrayList;
import java.util.List;

public class DetailFragmentPresenter {

    private DetailFragment view;
    private TransportAdapter transportAdapter;

    private List<TransportInfoList.Data> transport;
    private ArrayList<Person> person;

    public DetailFragmentPresenter(DetailFragment view) {
        this.view = view;

    }

    public void initData(ArrayList<Person> person, List<TransportInfoList.Data> transport){
        this.person = person;
        this.transport = transport;
    }

}
