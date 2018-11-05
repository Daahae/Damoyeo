package com.daahae.damoyeo.presenter;

import android.widget.TextView;

import com.daahae.damoyeo.view.adapter.BuildingAdapter;

public class BuildingAdapterPresenter {

    private BuildingAdapter adapter;

    public BuildingAdapterPresenter(BuildingAdapter adapter){
        this.adapter = adapter;
    }

    public void setBuildingListText(TextView companyName, TextView address, TextView distance, String strName, String strAddr,int strDist){
        companyName.setText(strName);
        address.setText(strAddr);
        distance.setText(String.valueOf(strDist));

    }
}
