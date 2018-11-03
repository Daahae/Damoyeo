package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.MidInfo;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;
import com.daahae.damoyeo.view.data.Constant;
import com.daahae.damoyeo.view.data.NMapPOIflagType;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;

public class SelectMidFragmentPresenter extends NMapPresenter{
    private int selectMidFlg = Constant.MID_ALGORITHM;

    private MarkerTimeAdapter markerTimeAdapter;

    public SelectMidFragmentPresenter(Fragment view, NMapContext mapContext) {
        super(view, mapContext);
    }

    public int getSelectMidFlg() {
        return selectMidFlg;
    }

    public void selectMid(int selectMidMenu, MidInfo mid, Building building, ArrayList<Person> personList){
        switch (selectMidMenu){
            case Constant.MID_ALGORITHM:
                selectMidAlgorithm(mid, personList);
                selectMidFlg = Constant.MID_ALGORITHM;
                break;
            case Constant.LANDMARK:
                selectLandmark(building, personList);
                selectMidFlg = Constant.LANDMARK;
                break;
        }
    }

    private void selectMidAlgorithm(MidInfo mid, ArrayList<Person> personList){
        showSavedMidInfoMarkers(0, mid, personList);
    }

    private void selectLandmark(Building building, ArrayList<Person> personList){
        showSavedBuildingMarkers(0, building, personList);
    }

    public void setMarkerTimeList(MarkerTimeAdapter markerTimeAdapter, ArrayList<Person> personList){
        this.markerTimeAdapter = markerTimeAdapter;

        markerTimeAdapter.resetList();
        showListView(personList);
    }

    private void showListView(ArrayList<Person> personList){
        for(Person person:personList)
            markerTimeAdapter.addDummy(person);
    }

    @Override
    public void init(Fragment view) {
        super.init(view);
        getMapContext().setMapDataProviderListener(getOnDataProviderListener());
    }

    public void showSavedMidInfoMarkers(int scale, MidInfo mid, ArrayList<Person> personList) {
        if(personList.size() != 0) {
            int markerId = NMapPOIflagType.PIN;
            int id = personList.size()+1;

            NMapPOIdata poiData = new NMapPOIdata(id, getResourceProvider());
            poiData.beginPOIdata(id);
            poiData.addPOIitem(mid.getPos().getX(), mid.getPos().getY(), null, markerId, 0);
            for (Person index:personList)
                poiData.addPOIitem(index.getAddressPosition().getX(), index.getAddressPosition().getY(), null, markerId, index.getId());

            poiData.endPOIdata();

            NMapPOIdataOverlay poiDataOverlay = getOverlayManager().createPOIdataOverlay(poiData, null);
            poiDataOverlay.showAllPOIdata(scale);
            poiDataOverlay.setOnStateChangeListener(getOnPOIdataStateChangeListener());
            poiDataOverlay.selectPOIitem(0, true);
        }
    }

    public void showSelectMidInfoMarker(MidInfo mid, Position pos) {
        getOverlayManager().clearOverlays();

        int markerId = NMapPOIflagType.PIN;
        int id = 2;

        NMapPOIdata poiData = new NMapPOIdata(id, getResourceProvider());
        poiData.beginPOIdata(id);
        poiData.addPOIitem(mid.getPos().getX(), mid.getPos().getY(), null, markerId, 0);
        poiData.addPOIitem(pos.getX(), pos.getY(), null, markerId, 1);

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = getOverlayManager().createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(getOnPOIdataStateChangeListener());
        poiDataOverlay.selectPOIitem(0, true);
    }

    public void showSavedBuildingMarkers(int scale, Building building, ArrayList<Person> personList) {
        if(personList.size() != 0) {
            int markerId = NMapPOIflagType.PIN;
            int id = personList.size()+1;

            NMapPOIdata poiData = new NMapPOIdata(id, getResourceProvider());
            poiData.beginPOIdata(id);
            poiData.addPOIitem(building.getBuildingPos().getX(), building.getBuildingPos().getY(), null, markerId, 0);
            for (Person index:personList)
                poiData.addPOIitem(index.getAddressPosition().getX(), index.getAddressPosition().getY(), null, markerId, index.getId());

            poiData.endPOIdata();

            NMapPOIdataOverlay poiDataOverlay = getOverlayManager().createPOIdataOverlay(poiData, null);
            poiDataOverlay.showAllPOIdata(scale);
            poiDataOverlay.setOnStateChangeListener(getOnPOIdataStateChangeListener());
            poiDataOverlay.selectPOIitem(0, true);
        }
    }

    public void showSelectBuildingMarker(Building building, Position pos) {
        this.getOverlayManager().clearOverlays();

        int markerId = NMapPOIflagType.PIN;
        int id = 2;

        NMapPOIdata poiData = new NMapPOIdata(id, getResourceProvider());
        poiData.beginPOIdata(id);
        poiData.addPOIitem(building.getBuildingPos().getX(), building.getBuildingPos().getY(), null, markerId, 0);
        poiData.addPOIitem(pos.getX(), pos.getY(), null, markerId, 1);

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = getOverlayManager().createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(getOnPOIdataStateChangeListener());
        poiDataOverlay.selectPOIitem(0, true);
    }
}
