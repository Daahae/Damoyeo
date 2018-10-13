package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.MidInfo;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;
import com.daahae.damoyeo.view.fragment.SelectMidFragment;
import com.daahae.damoyeo.view.function.NMapPOIflagType;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;

public class SelectMidFragmentPresenter extends NMapPresenter{
    private final String TAG = "NMapViewer";

    public static final int MID_ALGORITHM = 1;
    public static final int LANDMARK = 2;

    private int selectMidFlg = 1;

    private Fragment view;

    private MarkerTimeAdapter markerTimeAdapter;

    public SelectMidFragmentPresenter(Fragment view, NMapContext mapContext) {
        super(view, mapContext);
        this.view = view;
    }

    public int getSelectMidFlg() {
        return selectMidFlg;
    }

    public void selectMid(int selectMidMenu, MidInfo mid, Building building, ArrayList<Person> personList){
        switch (selectMidMenu){
            case MID_ALGORITHM:
                selectMidAlgorithm(mid, personList);
                selectMidFlg = MID_ALGORITHM;
                break;
            case LANDMARK:
                selectMidFlg = LANDMARK;
                selectLandmark(building, personList);
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
        for(Person person:personList){
            markerTimeAdapter.addDummy(person);
        }
    }

    @Override
    public void init(Fragment view) {
        super.init(view);
        this.getMapContext().setMapDataProviderListener(onDataProviderListener);
    }

    public void showSavedMidInfoMarkers(int scale, MidInfo mid, ArrayList<Person> personList) {
        if(personList.size() != 0) {
            int markerId = NMapPOIflagType.PIN;
            int id = personList.size()+1;

            NMapPOIdata poiData = new NMapPOIdata(id, this.getResourceProvider());
            poiData.beginPOIdata(id);
            poiData.addPOIitem(mid.getPos().getX(), mid.getPos().getY(), null, markerId, 0);
            for (Person index:personList)
                poiData.addPOIitem(index.getAddressPosition().getX(), index.getAddressPosition().getY(), null, markerId, index.getId());

            poiData.endPOIdata();

            NMapPOIdataOverlay poiDataOverlay = this.getOverlayManager().createPOIdataOverlay(poiData, null);
            poiDataOverlay.showAllPOIdata(scale);
            poiDataOverlay.setOnStateChangeListener(this.getOnPOIdataStateChangeListener());
            poiDataOverlay.selectPOIitem(0, true);
        }
    }

    public void showSelectMidInfoMaker(MidInfo mid, Position pos) {
        this.getOverlayManager().clearOverlays();

        int markerId = NMapPOIflagType.PIN;
        int id = 2;

        NMapPOIdata poiData = new NMapPOIdata(id, this.getResourceProvider());
        poiData.beginPOIdata(id);
        poiData.addPOIitem(mid.getPos().getX(), mid.getPos().getY(), null, markerId, 0);
        poiData.addPOIitem(pos.getX(), pos.getY(), null, markerId, 1);

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = this.getOverlayManager().createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(this.getOnPOIdataStateChangeListener());
        poiDataOverlay.selectPOIitem(0, true);
    }

    public void showSavedBuildingMarkers(int scale, Building building, ArrayList<Person> personList) {
        if(personList.size() != 0) {
            int markerId = NMapPOIflagType.PIN;
            int id = personList.size()+1;

            NMapPOIdata poiData = new NMapPOIdata(id, this.getResourceProvider());
            poiData.beginPOIdata(id);
            poiData.addPOIitem(building.getBuildingPos().getX(), building.getBuildingPos().getY(), null, markerId, 0);
            for (Person index:personList)
                poiData.addPOIitem(index.getAddressPosition().getX(), index.getAddressPosition().getY(), null, markerId, index.getId());

            poiData.endPOIdata();

            NMapPOIdataOverlay poiDataOverlay = this.getOverlayManager().createPOIdataOverlay(poiData, null);
            poiDataOverlay.showAllPOIdata(scale);
            poiDataOverlay.setOnStateChangeListener(this.getOnPOIdataStateChangeListener());
            poiDataOverlay.selectPOIitem(0, true);
        }
    }

    public void showSelectBuildingMaker(Building building, Position pos) {
        this.getOverlayManager().clearOverlays();

        int markerId = NMapPOIflagType.PIN;
        int id = 2;

        NMapPOIdata poiData = new NMapPOIdata(id, this.getResourceProvider());
        poiData.beginPOIdata(id);
        poiData.addPOIitem(building.getBuildingPos().getX(), building.getBuildingPos().getY(), null, markerId, 0);
        poiData.addPOIitem(pos.getX(), pos.getY(), null, markerId, 1);

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = this.getOverlayManager().createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(this.getOnPOIdataStateChangeListener());
        poiDataOverlay.selectPOIitem(0, true);
    }

    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {
            Log.i(TAG, "onReverseGeocoderResponse: placeMark="
                    + ((placeMark != null) ? placeMark.toString() : null));
            Log.i(TAG, "onReverseGeocoderResponse: placeMark="
                    + ((placeMark != null) ? placeMark.latitude : null));
            Log.i(TAG, "onReverseGeocoderResponse: placeMark="
                    + ((placeMark != null) ? placeMark.longitude : null));

            if (errInfo != null) {
                Log.e(TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

                Toast.makeText(view.getContext(), errInfo.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };
}
