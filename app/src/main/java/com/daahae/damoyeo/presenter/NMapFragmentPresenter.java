package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
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

public class NMapFragmentPresenter extends NMapPresenter{
    private Fragment view;
    private NMapActivityPresenter parentPresenter;

    private TextView tvAddress;
    private LinearLayout layoutAddress, layoutAddMarker;

    private NMapPlacemark instantMarker;
    private boolean isFixedMarker;
    private Person targetMarker;

    public NMapFragmentPresenter(Fragment view, NMapContext mapContext, NMapActivityPresenter parentPresenter) {
        super(view, mapContext);
        this.view = view;
        this.parentPresenter = parentPresenter;
    }

    // get/set
    private NMapPlacemark getInstantMarker() {
        return instantMarker;
    }

    private void setInstantMarker(NMapPlacemark instantMarker) {
        this.instantMarker = instantMarker;
    }

    private Person getTargetMarker() {
        return targetMarker;
    }

    private void setTargetMarker(Person targetMarker) {
        this.targetMarker = targetMarker;
    }

    public void setTvAddress(TextView tvAddress) {
        this.tvAddress = tvAddress;
    }

    private void setAddress(String address){
        tvAddress.setText(address);
    }

    public void setLayoutAddress(LinearLayout layoutAddress) {
        this.layoutAddress = layoutAddress;
    }

    public void setLayoutAddMarker(LinearLayout layoutAddMarker) {
        this.layoutAddMarker = layoutAddMarker;
    }

    private void setVisibleAddress(boolean isVisible) {
        if(isVisible)
            layoutAddress.setVisibility(View.VISIBLE);
        else {
            layoutAddress.setVisibility(View.GONE);
            setAddress(view.getResources().getString(R.string.msg_default));
        }
    }

    private void setInstantMarkerAddress(NMapPlacemark instantMarker) {
        setInstantMarker(instantMarker);
        if (instantMarker != null)
            setAddress(instantMarker.toString());
    }

    private void setIsFixedMarker(boolean isFixedMarker) {
        this.isFixedMarker = isFixedMarker;
        if(isFixedMarker)
            layoutAddMarker.setBackground(view.getResources().getDrawable(R.drawable.btn_minus, null));
        else
            layoutAddMarker.setBackground(view.getResources().getDrawable(R.drawable.btn_plus, null));
    }

    @Override
    public void init(Fragment view) {
        super.init(view);
        getMapContext().setMapDataProviderListener(onDataProviderListener);
    }

    @Override
    public void initLocation(ArrayList<Person> personList) {
        super.initLocation(personList);

        setVisibleAddress(false);
        setIsFixedMarker(false);
        setInstantMarker(null);
        setTargetMarker(null);
        personList.clear();
    }

    private void setInstantFloatingMarker(Position pos) {
        int marker = NMapPOIflagType.PIN;

        NMapPOIdata poiData = new NMapPOIdata(1, getResourceProvider());
        poiData.beginPOIdata(1);
        NMapPOIitem item;

        if(pos != null)
            item = poiData.addPOIitem(pos.getX(), pos.getY(), null, marker, 0);
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

    private void setSavedMarkers(ArrayList<Person> personList, int scale) {
        int markerId = NMapPOIflagType.PIN;

        int id = personList.size() + 1;
        NMapPOIdata poiData = new NMapPOIdata(id, getResourceProvider());
        poiData.beginPOIdata(id);
        for (Person index:personList)
            poiData.addPOIitem(index.getAddressPosition().getX(), index.getAddressPosition().getY(), null, markerId, index.getId());

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = getOverlayManager().createPOIdataOverlay(poiData, null);
        if(scale >= 0)
            poiDataOverlay.showAllPOIdata(scale);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
        poiDataOverlay.selectPOIitem(0, true);
    }

    public void getGPSLocation(Fragment view, ArrayList<Person> personList) {
        if (getLocationOverlay() != null) {
            GPSInfo gps = new GPSInfo(view.getActivity());
            // GPS 퍼미션 한번더 확인
            gps.setGPSPermission(this);

            if (gps.isGetLocation()) {
                // GPS 깜빡이는 오버레이 표시
                getGPSOverlay();

                showSavedMarkersOnSaveState(personList);

                Position pos = new Position(gps.getLongitude(), gps.getLatitude());
                setInstantFloatingMarker(pos);

                setVisibleAddress(true);
            }
            else
                gps.showSettingsAlert();
        }
    }

    public void pickLocation(ArrayList<Person> personList) {
        // GPS가 켜져있다면 끈다.
        stopGPSLocation();

        showSavedMarkersOnSaveState(personList);

        setInstantFloatingMarker(null);
        setVisibleAddress(true);
    }

    public void fixMarker(Fragment view, ArrayList<Person> personList) {
        if(isFixedMarker)
            removeMarker(view, getTargetMarker(), personList);
        else
            saveMarker(view, getInstantMarker(), personList);
    }

    private void saveMarker(Fragment view, NMapPlacemark instantMarker, ArrayList<Person> personList) {
        if(instantMarker != null) {
            getOverlayManager().clearOverlays();

            int id = personList.size() + 1;
            String address = instantMarker.toString();
            Position position = new Position(instantMarker.longitude, instantMarker.latitude);

            Person person = new Person(view.getResources().getString(R.string.guest)+id, address, position);
            person.setId(id);
            personList.add(person);

            setSavedMarkers(personList, 11);
            setInstantMarker(null);

            Toast.makeText(view.getActivity(), person.getName() + view.getResources().getString(R.string.msg_savemarker), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(view.getActivity(), view.getResources().getString(R.string.msg_default), Toast.LENGTH_SHORT).show();
    }

    private void removeMarker(Fragment view, Person targetMarker, ArrayList<Person> personList) {
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

    private void setTargetMarkerById(Fragment view, int id, Person targetMarker, ArrayList<Person> personList) {
        if (id > 0) {
            if(personList.size() > 0) {
                for (Person index : personList) {
                    if (index.getId() == id) {
                        setTargetMarker(index);
                        setAddress(targetMarker.getAddress());
                        setIsFixedMarker(true);
                        break;
                    }
                }
                if (targetMarker == null) {
                    setIsFixedMarker(false);
                    Toast.makeText(view.getActivity(), view.getResources().getString(R.string.msg_checkmarker), Toast.LENGTH_SHORT).show();
                }
            }
        } else
            setIsFixedMarker(false);
    }

    private void showSavedMarkersOnSaveState(ArrayList<Person> personList) {
        getOverlayManager().clearOverlays();
        if(personList.size() > 0)
            setSavedMarkers(personList, -1);
    }

    public void showSavedMarkers(Fragment view, ArrayList<Person> personList) {
        getOverlayManager().clearOverlays();
        if(personList.size() > 0)
            setSavedMarkers(personList, 0);
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
                setTargetMarkerById(view, nMapPOIitem.getId(), getTargetMarker(), parentPresenter.getPersonList());
        }

        @Override
        public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
        }
    };
}
