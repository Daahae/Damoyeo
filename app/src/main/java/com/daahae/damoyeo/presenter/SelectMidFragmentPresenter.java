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

public class SelectMidFragmentPresenter {

    public static final int MID_ALGORITHM = 1;
    public static final int LANDMARK = 2;

    private NMapActivityPresenter parentPresenter;
    private SelectMidFragment view;

    private NMapPresenter map;

    private MarkerTimeAdapter markerTimeAdapter;

    public SelectMidFragmentPresenter(SelectMidFragment view, NMapActivityPresenter parentPresenter, NMapContext context) {
        this.view = view;
        this.parentPresenter = parentPresenter;
        this.map = new NMapPresenter(view, context);
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

    public void setMarkerTimeList(MarkerTimeAdapter markerTimeAdapter){
        this.markerTimeAdapter = markerTimeAdapter;

        markerTimeAdapter.resetList();
        makeDummy();
    }

    //TODO: 삭제예정
    public void makeDummy(){
        for(Person person:parentPresenter.getPersonList()){
            markerTimeAdapter.addDummy(person);
        }
    }

    public void init(Fragment view){
        map.init(view);
        map.getMapContext().setMapDataProviderListener(onDataProviderListener);

        showSavedMarkers(parentPresenter.getPersonList());
    }

    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {
            map.setDataProviderListenerMessage(placeMark, errInfo);
        }
    };

    public void showSavedMarkers(ArrayList<Person> personList) {
        if(personList.size() != 0) {
            int markerId = NMapPOIflagType.PIN;
            int id = personList.size()+1;

            NMapPOIdata poiData = new NMapPOIdata(id, map.getResourceProvider());
            poiData.beginPOIdata(id);
            for (Person index:personList) {
                poiData.addPOIitem(index.getAddressPosition().getX(), index.getAddressPosition().getY(), null, markerId, index.getId());
            }

            poiData.endPOIdata();

            NMapPOIdataOverlay poiDataOverlay = map.getOverlayManager().createPOIdataOverlay(poiData, null);
            poiDataOverlay.showAllPOIdata(7);
            poiDataOverlay.setOnStateChangeListener(map.getOnPOIdataStateChangeListener());
            poiDataOverlay.selectPOIitem(0, true);
        }
    }

    public void showSelectMaker(Position pos) {
        map.getOverlayManager().clearOverlays();

        int markerId = NMapPOIflagType.PIN;
        int id = 2;

        NMapPOIdata poiData = new NMapPOIdata(id, map.getResourceProvider());
        poiData.beginPOIdata(id);
        poiData.addPOIitem(126.978371, 37.5666091, null, markerId, 1);
        poiData.addPOIitem(pos.getX(), pos.getY(), null, markerId, 2);

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = map.getOverlayManager().createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(map.getOnPOIdataStateChangeListener());
        poiDataOverlay.selectPOIitem(0, true);
    }
}
