package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.presenter.Contract.NMapFragmentContract;
import com.daahae.damoyeo.view.fragment.NMapFragment;
import com.daahae.damoyeo.view.function.GPSInfo;
import com.daahae.damoyeo.view.data.NMapPOIflagType;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;

public class NMapFragmentPresenter extends NMapPresenter implements NMapFragmentContract.Presenter {

    private NMapFragment view;
    private NMapActivityPresenter parentPresenter;

    private NMapPlacemark instantMarker;
    private Person targetMarker;

    private RetrofitPresenter retrofitPresenter;

    public NMapFragmentPresenter(Fragment view, NMapContext mapContext, NMapActivityPresenter parentPresenter) {
        super(view, mapContext);
        this.view = (NMapFragment) view;
        this.parentPresenter = parentPresenter;
        retrofitPresenter = parentPresenter.getRetrofitPresenter();
    }

    public ArrayList<String> sendRetrofit(){
        parentPresenter.addPerson();
        ArrayList<String> totalTimes = retrofitPresenter.sendPersonMessage();
        Log.v("NMAP", "보냄");
        return totalTimes;
    }

    // get/set
    public NMapPlacemark getInstantMarker() {
        return instantMarker;
    }

    private void setInstantMarker(NMapPlacemark instantMarker) {
        this.instantMarker = instantMarker;
    }

    public Person getTargetMarker() {
        return targetMarker;
    }

    private void setTargetMarker(Person targetMarker) {
        this.targetMarker = targetMarker;
    }

    @Override
    public void setInstantMarkerAddress(NMapPlacemark instantMarker) {
        setInstantMarker(instantMarker);
        if (instantMarker != null)
            view.setAddress(instantMarker.toString());
    }

    @Override
    public void init(Fragment view) {
        super.init(view);
        getMapContext().setMapDataProviderListener(onDataProviderListener);
    }

    @Override
    public void initLocation(ArrayList<Person> personList) {
        super.initLocation(personList);

        view.setVisibleAddress(false);
        view.setIsFixedMarker(false);
        setInstantMarker(null);
        setTargetMarker(null);
        personList.clear();
    }

    @Override
    public void setInstantFloatingMarker(Position pos) {

        int marker = NMapPOIflagType.PIN;

        NMapPOIdata poiData = new NMapPOIdata(1, getResourceProvider());
        poiData.beginPOIdata(1);
        NMapPOIitem item;

        if(pos != null)
            item = poiData.addPOIitem(pos.getY(), pos.getX(), null, marker, 0);
        else {
            item = poiData.addPOIitem(null, null, marker, 0);
            item.setPoint(getController().getMapCenter());
        }

        item.setFloatingMode(NMapPOIitem.FLOATING_TOUCH | NMapPOIitem.FLOATING_DRAG);

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = getOverlayManager().createPOIdataOverlay(poiData, null);
        poiDataOverlay.setOnFloatingItemChangeListener(getOnPOIdataFloatingItemChangeListener());
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
        poiDataOverlay.showAllPOIdata(11);
        poiDataOverlay.selectPOIitem(0, true);
    }

    @Override
    public void setSavedMarkers(int scale, ArrayList<Person> personList) {

        int markerId = NMapPOIflagType.PIN;

        int id = personList.size() + 1;
        NMapPOIdata poiData = new NMapPOIdata(id, getResourceProvider());
        poiData.beginPOIdata(id);
        for (Person index:personList)
            poiData.addPOIitem(index.getAddressPosition().getY(), index.getAddressPosition().getX(), null, markerId, index.getId());

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = getOverlayManager().createPOIdataOverlay(poiData, null);
        if(scale >= 0)
            poiDataOverlay.showAllPOIdata(scale);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
        poiDataOverlay.selectPOIitem(0, true);
    }

    @Override
    public void getGPSLocation(NMapFragment view, ArrayList<Person> personList) {

        if (getLocationOverlay() != null) {
            GPSInfo gps = new GPSInfo(view.getActivity());
            // GPS 퍼미션 한번더 확인
            gps.setGPSPermission(this);

            if (gps.isGetLocation()) {
                // GPS 깜빡이는 오버레이 표시
                getGPSOverlay();

                showSavedMarkersOnSaveState(personList);

                Position pos = new Position(gps.getLatitude(), gps.getLongitude());
                setInstantFloatingMarker(pos);

                view.setVisibleAddress(true);
            }
            else
                gps.showSettingsAlert();
        }
    }

    @Override
    public void pickLocation(ArrayList<Person> personList) {

        // GPS가 켜져있다면 끈다.
        stopGPSLocation();

        showSavedMarkersOnSaveState(personList);

        setInstantFloatingMarker(null);
        view.setVisibleAddress(true);
    }

    @Override
    public void fixMarker(boolean isFixedMarker, NMapFragment view, NMapPlacemark instantMarker, Person targetMarker, ArrayList<Person> personList) {

        if(isFixedMarker)
            removeMarker(view, targetMarker, personList);
        else
            saveMarker(view, instantMarker, personList);
    }

    @Override
    public void saveMarker(NMapFragment view, NMapPlacemark instantMarker, ArrayList<Person> personList) {

        if(instantMarker != null) {
            getOverlayManager().clearOverlays();

            int id = personList.size() + 1;
            String address = instantMarker.toString();
            Position position = new Position(instantMarker.latitude, instantMarker.longitude);

            Person person = new Person(view.getResources().getString(R.string.guest)+id, address, position);
            person.setId(id);
            personList.add(person);

            setSavedMarkers(11, personList);
            setInstantMarker(null);

            Toast.makeText(view.getActivity(), person.getName() + view.getResources().getString(R.string.msg_savemarker), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(view.getActivity(), view.getResources().getString(R.string.msg_default), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeMarker(NMapFragment view, Person targetMarker, ArrayList<Person> personList) {

        if(targetMarker != null){
            if(personList.contains(targetMarker)) {
                personList.remove(targetMarker);

                if(personList.size() > 0)
                    showSavedMarkersOnSaveState(personList);
                else
                    initLocation(personList);

                Toast.makeText(view.getContext(), targetMarker.getName() + view.getResources().getString(R.string.msg_removemarker), Toast.LENGTH_SHORT).show();
                setTargetMarker(null);
            }
        } else
            Toast.makeText(view.getActivity(), view.getResources().getString(R.string.msg_checkmarker), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTargetMarkerById(NMapFragment view, int id, ArrayList<Person> personList) {

        if (id > 0) {
            if(personList.size() > 0) {
                for (Person index : personList) {
                    if (index.getId() == id) {
                        setTargetMarker(index);
                        view.setAddress(getTargetMarker().getAddress());
                        view.setIsFixedMarker(true);
                        break;
                    }
                }
                if (getTargetMarker() == null) {
                    view.setIsFixedMarker(false);
                    Toast.makeText(view.getActivity(), view.getResources().getString(R.string.msg_checkmarker), Toast.LENGTH_SHORT).show();
                }
            }
        } else
            this.view.setIsFixedMarker(false);
    }

    @Override
    public void showSavedMarkersOnSaveState(ArrayList<Person> personList) {

        getOverlayManager().clearOverlays();

        if(personList.size() > 0)
            setSavedMarkers(-1, personList);
    }

    @Override
    public void showSavedMarkers(NMapFragment view, ArrayList<Person> personList) {

        getOverlayManager().clearOverlays();

        if(personList.size() > 0)
            setSavedMarkers(0, personList);
        else
            Toast.makeText(view.getActivity(), view.getResources().getString(R.string.msg_cannotshow), Toast.LENGTH_SHORT).show();
    }

    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {
        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

            setInstantMarkerAddress(placeMark);
        }
    };

    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
            if (nMapPOIitem != null)
                setTargetMarkerById(view, nMapPOIitem.getId(), parentPresenter.getPersonList());
        }

        @Override
        public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
        }
    };
}