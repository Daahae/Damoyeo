package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.MidInfo;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.presenter.Contract.SelectMidFragmentContract;
import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;
import com.daahae.damoyeo.view.data.Constant;
import com.daahae.damoyeo.view.data.NMapPOIflagType;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;

public class SelectMidFragmentPresenter extends NMapPresenter implements SelectMidFragmentContract.Presenter {
    private int selectMidFlg = Constant.MID_ALGORITHM;

    private NMapActivityPresenter parentPresenter;

    private MarkerTimeAdapter markerTimeAdapter;
    private ArrayList<String> totalTimes;

    public SelectMidFragmentPresenter(Fragment view, NMapContext mapContext, NMapActivityPresenter parentPresenter) {
        super(view, mapContext);
        this.parentPresenter = parentPresenter;
    }

    @Override
    public void init(Fragment view) {
        super.init(view);
        getMapContext().setMapDataProviderListener(getOnDataProviderListener());
    }

    public void initMarkerTime(ArrayList<String> totalTimes){
        this.totalTimes = totalTimes;
    }

    public void setResetList(){
        markerTimeAdapter.resetList();
    }

    @Override
    public void setMarkerTimeList(MarkerTimeAdapter markerTimeAdapter) {
        this.markerTimeAdapter = markerTimeAdapter;

        for(int i=0; i < totalTimes.size();i++)
            markerTimeAdapter.add(parentPresenter.getPersonList().get(i).getName(), totalTimes.get(i));
    }

    @Override
    public int getSelectMidFlg() {
        return selectMidFlg;
    }

    @Override
    public void setSelectMidFlg(int selectMidMenu, MidInfo mid, Building building, ArrayList<Person> personList) {
        selectMidFlg = selectMidMenu;

        switch (selectMidMenu){
            case Constant.MID_ALGORITHM:
                showMidInfoAllMarkers(0, mid, personList);
                break;
            case Constant.LANDMARK:
                showLandmarkAllMarkers(0, building, personList);
                break;
        }
    }

    @Override
    public void showMidInfoAllMarkers(int scale, MidInfo mid, ArrayList<Person> personList) {
        if(personList.size() > 0) {
            int markerId = NMapPOIflagType.PIN;
            int id = personList.size()+1;

            NMapPOIdata poiData = new NMapPOIdata(id, getResourceProvider());
            poiData.beginPOIdata(id);
            poiData.addPOIitem(mid.getPos().getY(), mid.getPos().getX(), null, markerId, 0);
            for (Person index:personList)
                poiData.addPOIitem(index.getAddressPosition().getY(), index.getAddressPosition().getX(), null, markerId, index.getId());

            poiData.endPOIdata();

            NMapPOIdataOverlay poiDataOverlay = getOverlayManager().createPOIdataOverlay(poiData, null);
            poiDataOverlay.showAllPOIdata(scale);
            poiDataOverlay.setOnStateChangeListener(getOnPOIdataStateChangeListener());
            poiDataOverlay.selectPOIitem(0, true);
        }
    }

    @Override
    public void showMidInfoEachMarker(MidInfo mid, Position pos) {
        getOverlayManager().clearOverlays();

        int markerId = NMapPOIflagType.PIN;
        int id = 2;

        NMapPOIdata poiData = new NMapPOIdata(id, getResourceProvider());
        poiData.beginPOIdata(id);
        poiData.addPOIitem(mid.getPos().getY(), mid.getPos().getX(), null, markerId, 0);
        poiData.addPOIitem(pos.getY(), pos.getX(), null, markerId, 1);

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = getOverlayManager().createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(getOnPOIdataStateChangeListener());
        poiDataOverlay.selectPOIitem(0, true);
    }

    @Override
    public void showLandmarkAllMarkers(int scale, Building building, ArrayList<Person> personList) {
        if(personList.size() > 0) {
            int markerId = NMapPOIflagType.PIN;
            int id = personList.size()+1;

            NMapPOIdata poiData = new NMapPOIdata(id, getResourceProvider());
            poiData.beginPOIdata(id);
            poiData.addPOIitem(building.getBuildingPos().getY(), building.getBuildingPos().getX(), null, markerId, 0);
            for (Person index:personList)
                poiData.addPOIitem(index.getAddressPosition().getY(), index.getAddressPosition().getX(), null, markerId, index.getId());

            poiData.endPOIdata();

            NMapPOIdataOverlay poiDataOverlay = getOverlayManager().createPOIdataOverlay(poiData, null);
            poiDataOverlay.showAllPOIdata(scale);
            poiDataOverlay.setOnStateChangeListener(getOnPOIdataStateChangeListener());
            poiDataOverlay.selectPOIitem(0, true);
        }
    }

    @Override
    public void showLandmarkEachMarker(Building building, Position pos) {
        getOverlayManager().clearOverlays();

        int markerId = NMapPOIflagType.PIN;
        int id = 2;

        NMapPOIdata poiData = new NMapPOIdata(id, getResourceProvider());
        poiData.beginPOIdata(id);
        poiData.addPOIitem(building.getBuildingPos().getY(), building.getBuildingPos().getX(), null, markerId, 0);
        poiData.addPOIitem(pos.getY(), pos.getX(), null, markerId, 1);

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = getOverlayManager().createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(getOnPOIdataStateChangeListener());
        poiDataOverlay.selectPOIitem(0, true);
    }
}