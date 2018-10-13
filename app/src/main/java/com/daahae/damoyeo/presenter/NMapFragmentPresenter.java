package com.daahae.damoyeo.presenter;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.view.fragment.NMapFragment;
import com.daahae.damoyeo.view.function.GPSInfo;
import com.daahae.damoyeo.view.function.NMapPOIflagType;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;

public class NMapFragmentPresenter{
    private final String TAG = "NMapViewer";

    private NMapFragment view;
    private NMapActivityPresenter parentPresenter;

    private NMapPresenter map;

    private TextView tvAddress;
    private LinearLayout layoutAddress, layoutAddMarker;

    private NMapPlacemark mapPlacemark;

    private boolean isFixedMarker = false;
    private Person targetPerson;

    public NMapFragmentPresenter(NMapFragment view, NMapActivityPresenter parentPresenter, NMapContext context) {
        this.view = view;
        this.map = new NMapPresenter(view, context);
        this.parentPresenter = parentPresenter;
        this.parentPresenter.setPersonList(new ArrayList<Person>());
    }

    public void setTvAddress(TextView tvAddress) {
        this.tvAddress = tvAddress;
    }

    public void setLayoutAddress(LinearLayout layoutAddress) {
        this.layoutAddress = layoutAddress;
    }

    public void setVisibleAddress(boolean isPickLocation) {
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

    private void setPlaceMark(NMapPlacemark placeMark) {
        mapPlacemark = placeMark;
        if (placeMark != null) {
            if(tvAddress!=null)
                tvAddress.setText(placeMark.toString());
        }
    }

    public void init(Fragment view){
        map.init(view);
        map.getMapContext().setMapDataProviderListener(onDataProviderListener);
    }

    public void getPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                view.getActivity().finish();
            }
        };

        // GPS 위치정보를 받기위해 권한을 설정
        TedPermission.with(view.getActivity())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("지도 서비스를 사용하기 위해서는 위치 접근 권한이 필요해요")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    public void getGPSLocation() {
        if (map.getLocationOverlay() != null) {
            if (!map.getOverlayManager().hasOverlay(map.getLocationOverlay())) {
                map.getOverlayManager().addOverlay(map.getLocationOverlay());
            }

            GPSInfo gps = new GPSInfo(view.getContext());

            // GPS 퍼미션 한번더 확인
            gps.setGPSPermission(map);

            // GPS 사용유무 가져오기
            if (gps.isGetLocation()) {
                showSavedMarkersOnSaveState(map, parentPresenter.getPersonList());
                setInstantFloatingMarker();
            }
            // GPS 를 사용할수 없으므로
            else
                gps.showSettingsAlert();
        }
    }

    public void pickLocation() {
        // GPS가 켜져있다면 끈다.
        showSavedMarkersOnSaveState(map, parentPresenter.getPersonList());
        map.stopGPSLocation();
        map.removeOverlay();

        setInstantFloatingMarker();
    }

    public void initLocation() {
        setVisibleAddress(false);
        isFixedMarker = false;
        layoutAddMarker.setBackground(view.getResources().getDrawable(R.drawable.btn_plus));
        targetPerson = null;
        parentPresenter.getPersonList().clear();

        map.initLocation();
    }

    public void fixMarker() {
        if(isFixedMarker)
            removeMarker();
        else
            saveMarker();
    }

    public void saveMarker() {
        if(mapPlacemark != null) {
            setVisibleAddress(false);
            map.getOverlayManager().clearOverlays();

            int id = parentPresenter.getPersonList().size()+1;
            String address = mapPlacemark.toString();
            Position position = new Position(mapPlacemark.longitude, mapPlacemark.latitude);

            Person person = new Person("guest"+id, address, position);
            person.setId(id);
            parentPresenter.getPersonList().add(person);

            setSavedMarkers(id, 11);

            Toast.makeText(view.getContext(), person.getName() + "님의 마커가 추가되었습니다.", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(view.getContext(), "마커를 움직이세요", Toast.LENGTH_SHORT).show();
    }

    public void removeMarker() {
        if(targetPerson != null){
            if(parentPresenter.getPersonList().size() != 0)
                if(parentPresenter.getPersonList().contains(targetPerson)) {
                    parentPresenter.getPersonList().remove(targetPerson);
                    Toast.makeText(view.getContext(), targetPerson.getName()+"님의 마커가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    if(parentPresenter.getPersonList().size() > 0)
                        showSavedMarkersOnSaveState(map, parentPresenter.getPersonList());
                    else
                        initLocation();
                    targetPerson = null;
                }
        } else {
            Toast.makeText(view.getContext(), "선택한 마커를 확인해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    // 지도위 찍기, 드래그 가능한 마커
    public void setInstantFloatingMarker() {
        setVisibleAddress(true);

        int marker = NMapPOIflagType.PIN;

        NMapPOIdata poiData = new NMapPOIdata(1, map.getResourceProvider());
        poiData.beginPOIdata(1);
        NMapPOIitem item = poiData.addPOIitem(null, null, marker, 0);
        if (item != null) {
            item.setPoint(map.getController().getMapCenter());
            item.setFloatingMode(NMapPOIitem.FLOATING_TOUCH | NMapPOIitem.FLOATING_DRAG);
        }
        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = map.getOverlayManager().createPOIdataOverlay(poiData, null);
        if (poiDataOverlay != null) {
            poiDataOverlay.setOnFloatingItemChangeListener(map.getOnPOIdataFloatingItemChangeListener());
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

            poiDataOverlay.selectPOIitem(0, false);
        }
    }

    public void setTargetMarker(NMapPOIitem nMapPOIitem) {
        if(parentPresenter.getPersonList().size()!=0) {
            if (nMapPOIitem.getId() != 0) {
                for (Person index : parentPresenter.getPersonList()) {
                    if (index.getId() == nMapPOIitem.getId()) {
                        targetPerson = index;
                        tvAddress.setText(targetPerson.getAddress());
                        isFixedMarker = true;
                        layoutAddMarker.setBackground(view.getResources().getDrawable(R.drawable.btn_minus));
                        break;
                    }
                }
                if (targetPerson == null) {
                    isFixedMarker = false;
                    layoutAddMarker.setBackground(view.getResources().getDrawable(R.drawable.btn_plus));
                    Toast.makeText(view.getContext(), "올바르지 않은 마커가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                isFixedMarker = false;
                layoutAddMarker.setBackground(view.getResources().getDrawable(R.drawable.btn_plus));
            }
        }
    }

    public void setSavedMarkers(int id, int scale) {
        int markerId = NMapPOIflagType.PIN;

        NMapPOIdata poiData = new NMapPOIdata(id, map.getResourceProvider());
        poiData.beginPOIdata(id);
        for (Person index:parentPresenter.getPersonList()) {
            poiData.addPOIitem(index.getAddressPosition().getX(), index.getAddressPosition().getY(), null, markerId, index.getId());
        }

        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = map.getOverlayManager().createPOIdataOverlay(poiData, null);
        if(scale != -1)
            poiDataOverlay.showAllPOIdata(scale);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
        poiDataOverlay.selectPOIitem(0, true);
    }

    public void showSavedMarkersOnSaveState(NMapPresenter map, ArrayList<Person> personList) {
        map.getOverlayManager().clearOverlays();

        if(personList.size() != 0) {
            int id = personList.size()+1;
            setSavedMarkers(id, -1);
        }
    }

    public void showSavedMarkers(ArrayList<Person> personList) {
        map.getOverlayManager().clearOverlays();
        if(personList.size() != 0) {
            int id = personList.size()+1;
            setSavedMarkers(id, 0);
        } else
            Toast.makeText(view.getContext(), "인원을 추가하세요", Toast.LENGTH_SHORT).show();
    }

    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {
            map.setDataProviderListenerMessage(placeMark, errInfo);
            setPlaceMark(placeMark);
        }
    };

    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        @Override
        public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
            if (nMapPOIitem != null) {
                Log.i(TAG, "onFocusChanged: " + nMapPOIitem.toString());
                setTargetMarker(nMapPOIitem);
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
