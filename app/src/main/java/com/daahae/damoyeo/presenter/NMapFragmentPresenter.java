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
import com.daahae.damoyeo.view.function.GPSInfo;
import com.daahae.damoyeo.view.function.NMapPOIflagType;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;

public class NMapFragmentPresenter extends NMapPresenter{
    private final String TAG = "NMapViewer";

    private Fragment view;
    private NMapActivityPresenter parentPresenter;

    private TextView tvAddress;
    private LinearLayout layoutAddress, layoutAddMarker;

    private NMapPlacemark instantMarker;

    private boolean isFixedMarker = false;
    private Person targetMarker = null;

    public NMapFragmentPresenter(Fragment view, NMapContext mapContext, NMapActivityPresenter parentPresenter) {
        super(view, mapContext);
        this.view = view;
        this.parentPresenter = parentPresenter;
    }

    // set method
    public void setTvAddress(TextView tvAddress) {
        this.tvAddress = tvAddress;
    }

    public void setLayoutAddress(LinearLayout layoutAddress) {
        this.layoutAddress = layoutAddress;
    }

    private void setVisibleAddress(boolean isPickLocation) {
        if(isPickLocation)
            layoutAddress.setVisibility(View.VISIBLE);
        else {
            layoutAddress.setVisibility(View.GONE);
            tvAddress.setText("마커를 움직이세요");
        }
    }

    public void setLayoutAddMarker(LinearLayout layoutAddMarker) {
        this.layoutAddMarker = layoutAddMarker;
    }

    private void setInstantMarkerAddress(NMapPlacemark instantMarker) {
        this.instantMarker = instantMarker;
        if (instantMarker != null) {
            if(tvAddress!=null)
                tvAddress.setText(instantMarker.toString());
        }
    }

    private void setIsFixedMarker(boolean isFixedMarker) {
        if(isFixedMarker){
            this.isFixedMarker = true;
            layoutAddMarker.setBackground(view.getResources().getDrawable(R.drawable.btn_minus));
        } else {
            this.isFixedMarker = false;
            layoutAddMarker.setBackground(view.getResources().getDrawable(R.drawable.btn_plus));
        }
    }

    @Override
    public void init(Fragment view) {
        super.init(view);
        this.getMapContext().setMapDataProviderListener(onDataProviderListener);
    }

    @Override
    public void initLocation(ArrayList<Person> personList) {
        super.initLocation(personList);

        setVisibleAddress(false);
        setIsFixedMarker(false);
        targetMarker = null;
        personList.clear();
    }

    // 지도위 찍기, 드래그 가능한 마커
    private void setInstantFloatingMarker(Position startPosition) {
        int marker = NMapPOIflagType.PIN;

        NMapPOIdata poiData = new NMapPOIdata(1, this.getResourceProvider());
        poiData.beginPOIdata(1);
        NMapPOIitem item;

        if(startPosition != null)
            item = poiData.addPOIitem(startPosition.getX(), startPosition.getY(), null, marker, 0);
        else {
            item = poiData.addPOIitem(null, null, marker, 0);
            item.setPoint(this.getController().getMapCenter());
        }

        if (item != null) {
            item.setFloatingMode(NMapPOIitem.FLOATING_TOUCH | NMapPOIitem.FLOATING_DRAG);
        }

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = this.getOverlayManager().createPOIdataOverlay(poiData, null);
        if (poiDataOverlay != null) {
            poiDataOverlay.setOnFloatingItemChangeListener(this.getOnPOIdataFloatingItemChangeListener());
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
            poiDataOverlay.showAllPOIdata(11);
            poiDataOverlay.selectPOIitem(0, true);
        }
    }

    public void getGPSLocation(ArrayList<Person> personList) {
        if (getLocationOverlay() != null) {
            // GPS 깜빡이는 오버레이 표시
            if (!getOverlayManager().hasOverlay(getLocationOverlay())) {
                getOverlayManager().addOverlay(getLocationOverlay());
            }

            GPSInfo gps = new GPSInfo(view.getActivity());

            // GPS 퍼미션 한번더 확인
            gps.setGPSPermission(this);

            // GPS 사용유무 가져오기
            if (gps.isGetLocation()) {
                showSavedMarkersOnSaveState(personList);
                setVisibleAddress(true);

                Position pos = new Position(gps.getLongitude(), gps.getLatitude());
                setInstantFloatingMarker(pos);
            }
            // GPS 를 사용할수 없으므로
            else
                gps.showSettingsAlert();
        }
    }

    public void pickLocation(ArrayList<Person> personList) {
        setVisibleAddress(true);

        // GPS가 켜져있다면 끈다.
        this.stopGPSLocation();

        // 저장된 마커들 표시
        showSavedMarkersOnSaveState(personList);

        setInstantFloatingMarker(null);
    }

    public void fixMarker(ArrayList<Person> personList) {
        if(isFixedMarker)
            removeMarker(personList);
        else
            saveMarker(personList);
    }

    private void saveMarker(ArrayList<Person> personList) {
        if(instantMarker != null) {
            setVisibleAddress(true);
            this.getOverlayManager().clearOverlays();

            int id = personList.size()+1;
            String address = instantMarker.toString();
            Position position = new Position(instantMarker.longitude, instantMarker.latitude);

            Person person = new Person("guest"+id, address, position);
            person.setId(id);
            personList.add(person);

            setSavedMarkers(id, 11, personList);

            Toast.makeText(view.getContext(), person.getName() + "님의 마커가 추가되었습니다.", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(view.getContext(), "마커를 움직이세요", Toast.LENGTH_SHORT).show();
    }

    private void removeMarker(ArrayList<Person> personList) {
        if(targetMarker != null){
            if(personList.size() != 0)
                if(personList.contains(targetMarker)) {
                    personList.remove(targetMarker);
                    Toast.makeText(view.getContext(), targetMarker.getName()+"님의 마커가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    if(personList.size() > 0)
                        showSavedMarkersOnSaveState(personList);
                    else
                        initLocation(personList);
                    targetMarker = null;
                }
        } else {
            Toast.makeText(view.getContext(), "선택한 마커를 확인해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTargetMarker(NMapPOIitem nMapPOIitem, ArrayList<Person> personList) {
        if(personList.size()!=0) {
            if (nMapPOIitem.getId() != 0) {
                for (Person index : personList) {
                    if (index.getId() == nMapPOIitem.getId()) {
                        targetMarker = index;
                        tvAddress.setText(targetMarker.getAddress());
                        setIsFixedMarker(true);
                        break;
                    }
                }
                if (targetMarker == null) {
                    setIsFixedMarker(false);
                    Toast.makeText(view.getContext(), "올바르지 않은 마커가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                }
            } else
                setIsFixedMarker(false);
        }
    }

    public void setSavedMarkers(int id, int scale, ArrayList<Person> personList) {
        int markerId = NMapPOIflagType.PIN;

        NMapPOIdata poiData = new NMapPOIdata(id, this.getResourceProvider());
        poiData.beginPOIdata(id);
        for (Person index:personList)
            poiData.addPOIitem(index.getAddressPosition().getX(), index.getAddressPosition().getY(), null, markerId, index.getId());

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = this.getOverlayManager().createPOIdataOverlay(poiData, null);
        if(scale != -1)
            poiDataOverlay.showAllPOIdata(scale);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
        poiDataOverlay.selectPOIitem(0, true);
    }

    public void showSavedMarkersOnSaveState(ArrayList<Person> personList) {
        this.getOverlayManager().clearOverlays();

        if(personList.size() != 0) {
            int id = personList.size()+1;
            setSavedMarkers(id, -1, personList);
        }
    }

    public void showSavedMarkers(ArrayList<Person> personList) {
        this.getOverlayManager().clearOverlays();
        if(personList.size() != 0) {
            int id = personList.size()+1;
            setSavedMarkers(id, 0, personList);
        } else
            Toast.makeText(view.getContext(), "인원을 추가하세요", Toast.LENGTH_SHORT).show();
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
                return;
            }
            setInstantMarkerAddress(placeMark);
        }
    };

    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        @Override
        public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
            if (nMapPOIitem != null) {
                Log.i(TAG, "onFocusChanged: " + nMapPOIitem.toString());
                setTargetMarker(nMapPOIitem, parentPresenter.getPersonList());
            } else {
                Log.i(TAG, "onFocusChanged: ");
            }
        }

        @Override
        public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
            Toast.makeText(view.getContext(), "onCalloutClick: " + nMapPOIitem.getTitle(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "onFocusChanged: " + nMapPOIitem.toString());
        }
    };
}
