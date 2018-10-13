package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

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

    private Fragment view;

    private MarkerTimeAdapter markerTimeAdapter;

    public SelectMidFragmentPresenter(Fragment view, NMapContext mapContext) {
        super(view, mapContext);
        this.view = view;
    }

    public void selectMid(int selectMidMenu){
        switch (selectMidMenu){
            case MID_ALGORITHM:
                selectMidAlgorithm();
                break;
            case LANDMARK:
                selectLandmark();
                break;
        }
    }

    public void selectMidAlgorithm(){

    }

    public void selectLandmark(){

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
    public void initLocation(ArrayList<Person> personList) {
        super.initLocation(personList);

        this.getMapContext().setMapDataProviderListener(onDataProviderListener);

        showSavedMarkers(8, personList);
    }

    public void showSavedMarkers(int scale, ArrayList<Person> personList) {
        if(personList.size() != 0) {
            int markerId = NMapPOIflagType.PIN;
            int id = personList.size()+1;

            NMapPOIdata poiData = new NMapPOIdata(id, this.getResourceProvider());
            poiData.beginPOIdata(id);
            for (Person index:personList)
                poiData.addPOIitem(index.getAddressPosition().getX(), index.getAddressPosition().getY(), null, markerId, index.getId());

            poiData.endPOIdata();

            NMapPOIdataOverlay poiDataOverlay = this.getOverlayManager().createPOIdataOverlay(poiData, null);
            poiDataOverlay.showAllPOIdata(scale);
            poiDataOverlay.setOnStateChangeListener(this.getOnPOIdataStateChangeListener());
            poiDataOverlay.selectPOIitem(0, true);
        }
    }

    public void showSelectMaker(Position pos) {
        this.getOverlayManager().clearOverlays();

        int markerId = NMapPOIflagType.PIN;
        int id = 2;

        NMapPOIdata poiData = new NMapPOIdata(id, this.getResourceProvider());
        poiData.beginPOIdata(id);
        poiData.addPOIitem(126.978371, 37.5666091, null, markerId, 1);
        poiData.addPOIitem(pos.getX(), pos.getY(), null, markerId, 2);

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
