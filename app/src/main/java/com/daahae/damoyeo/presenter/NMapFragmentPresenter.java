package com.daahae.damoyeo.presenter;

import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.view.fragment.NMapFragment;
import com.daahae.damoyeo.view.function.GPSInfo;
import com.daahae.damoyeo.view.function.NMapPOIflagType;
import com.daahae.damoyeo.view.function.NMapViewerResourceProvider;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import java.util.ArrayList;

public class NMapFragmentPresenter {
    private final String TAG = "NMapViewer";

    private NMapFragment view;
    private NMapContext mapContext;

    private NMapController mapController;
    private NMapView mapView;

    private NMapResourceProvider mapResourceProvider;
    private NMapOverlayManager mapOverlayManager;

    private NMapLocationManager mapLocationManager;
    private NMapMyLocationOverlay myLocationOverlay;

    private NMapPOIitem floatingPOIitem;

    private TextView tvAddress;
    private LinearLayout layoutAddress, layoutAddMarker;

    private NMapPlacemark mapPlacemark;

    private ArrayList<Person> personList;

    private boolean isFixedMarker = false;
    private Person targetPerson;

    private GPSInfo gps;

    public NMapFragmentPresenter(NMapFragment view, NMapContext context) {
        this.view = view;
        this.mapContext = context;
        this.personList = new ArrayList<Person>();
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

    /**
     * Fragment에 포함된 NMapView 객체를 반환함
     */
    public NMapView findMapView(View v) {
        if (!(v instanceof ViewGroup)) {
            return null;
        }

        ViewGroup vg = (ViewGroup)v;
        if (vg instanceof NMapView) {
            return (NMapView)vg;
        }

        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            if (!(child instanceof ViewGroup)) {
                continue;
            }
            NMapView mapView = findMapView(child);
            if (mapView != null) {
                return mapView;
            }
        }
        return null;
    }

    public void init(){
        // Fragment에 포함된 NMapView 객체 찾기
        mapView = findMapView(view.getView());
        if (mapView == null) {
            throw new IllegalArgumentException("NMapFragment dose not have an instance of NMapView.");
        }

        mapView.setClientId(view.getResources().getString(R.string.NAVER_API_KEY));// 클라이언트 아이디 설정

        // initialize map view
        mapView.setClickable(true);

        // NMapActivity를 상속하지 않는 경우에는 NMapView 객체 생성후 반드시 setupMapView()를 호출해야함.
        mapContext.setupMapView(mapView);

        mapView.setScalingFactor(4.0F, true);

        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mapController = mapView.getMapController();
        mapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11);     //Default Data

        mapResourceProvider = new NMapViewerResourceProvider(view.getContext());
        mapOverlayManager = new NMapOverlayManager(view.getContext(), mapView, mapResourceProvider);

        // set data provider listener
        mapContext.setMapDataProviderListener(onDataProviderListener);

        // location manager
        mapLocationManager = new NMapLocationManager(view.getContext());;
        mapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        // create my location overlay
        myLocationOverlay = mapOverlayManager.createMyLocationOverlay(mapLocationManager, null);;
    }

    public void getGPSLocation() {
        if (myLocationOverlay != null) {
            if (!mapOverlayManager.hasOverlay(myLocationOverlay)) {
                mapOverlayManager.addOverlay(myLocationOverlay);
            }

            if (mapLocationManager.isMyLocationEnabled()) {
                if (!mapView.isAutoRotateEnabled()) {
                    myLocationOverlay.setCompassHeadingVisible(true);

                    mapView.setAutoRotateEnabled(true, false);
                } else {
                    stopGPSLocation();
                }
                mapView.postInvalidate();
            } else {
                boolean isMyLocationEnabled = mapLocationManager.enableMyLocation(true);
                if (!isMyLocationEnabled) {
                    Toast.makeText(view.getContext(), "Please enable a My Location source in system settings",
                            Toast.LENGTH_LONG).show();

                    Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    view.startActivity(goToSettings);

                    return;
                }

                setVisibleAddress(true);

                gps = new GPSInfo(view.getContext());
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {
                    instantFloatingPOIdataOverlay();
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        }
    }

    public void stopGPSLocation() {
        if (myLocationOverlay != null) {
            mapLocationManager.disableMyLocation();

            if (mapView.isAutoRotateEnabled()) {
                myLocationOverlay.setCompassHeadingVisible(false);

                mapView.setAutoRotateEnabled(false, false);
            }
        }
    }

    public void pickLocation() {
        instantFloatingPOIdataOverlay();
    }

    public void initLocation() {
        setVisibleAddress(false);
        isFixedMarker = false;
        layoutAddMarker.setBackground(view.getResources().getDrawable(R.drawable.btn_plus));
        targetPerson = null;
        personList.clear();
        if (myLocationOverlay != null) {
            stopGPSLocation();
            mapOverlayManager.removeOverlay(myLocationOverlay);
        }

        mapController.setMapViewMode(NMapView.VIEW_MODE_VECTOR);

        mapOverlayManager.clearOverlays();
        mapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11);
    }

    public void clickMarker() {
        if(isFixedMarker)
            removeMarker();
        else
            fixMarker();
    }

    public void fixMarker() {
        if(mapPlacemark != null) {
            setVisibleAddress(false);
            mapOverlayManager.clearOverlays();

            int markerId = NMapPOIflagType.PIN;
            int id = personList.size()+1;
            String address = mapPlacemark.toString();
            Position position = new Position(mapPlacemark.longitude, mapPlacemark.latitude);

            // JSON으로 id, latitude, longitude를 서버로 보내기
            // 중간지점 찾기 누르면 latitude, longitude를 받기
            Person person = new Person("guest"+id, address, position);
            person.setId(id);
            personList.add(person);

            // set POI data
            NMapPOIdata poiData = new NMapPOIdata(id, mapResourceProvider);
            poiData.beginPOIdata(id);
            for (Person index:personList) {
                poiData.addPOIitem(index.getAddressPosition().getX(), index.getAddressPosition().getY(), null, markerId, index.getId());
            }

            poiData.endPOIdata();

            // create POI data overlay
            NMapPOIdataOverlay poiDataOverlay = mapOverlayManager.createPOIdataOverlay(poiData, null);
            // 해당 오버레이 객체에 포함된 전체 아이템이 화면에 표시되도록 지도 중심 및 축적 레벨을 변경하려면 아래와 같이 구현합니다.
            poiDataOverlay.showAllPOIdata(0);
            // 아이템의 선택 상태가 변경되거나 말풍선이 선택되는 경우를 처리하기 위하여 이벤트 리스너를 등록합니다.
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

            poiDataOverlay.selectPOIitem(0, true);
            Toast.makeText(view.getContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(view.getContext(), "마커를 움직이세요", Toast.LENGTH_SHORT).show();
    }

    public void removeMarker() {
        if(targetPerson != null){
            if(personList.size() != 0)
                if(personList.contains(targetPerson)) {
                    personList.remove(targetPerson);
                    Toast.makeText(view.getContext(), targetPerson.getName()+"님의 마커가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    if(personList.size()>0)
                        viewAllMarkerFull();
                    else
                        initLocation();
                    targetPerson = null;
                }
        } else {
            Toast.makeText(view.getContext(), "선택한 마커를 확인해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    // 지도 위 오버레이(마커) 아이템 위치 이동
    public void instantFloatingPOIdataOverlay() {
        int marker = NMapPOIflagType.PIN;

        NMapPOIdata poiData = new NMapPOIdata(1, mapResourceProvider);
        poiData.beginPOIdata(1);
        NMapPOIitem item = poiData.addPOIitem(null, null, marker, 0);
        if (item != null) {
            item.setPoint(mapController.getMapCenter());
            item.setFloatingMode(NMapPOIitem.FLOATING_TOUCH | NMapPOIitem.FLOATING_DRAG);

            floatingPOIitem = item;
        }
        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = mapOverlayManager.createPOIdataOverlay(poiData, null);
        if (poiDataOverlay != null) {
            poiDataOverlay.setOnFloatingItemChangeListener(onPOIdataFloatingItemChangeListener);
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

            poiDataOverlay.selectPOIitem(0, false);
        }
    }

    public void viewAllMarkerFull() {
        if(personList.size() != 0) {
            setVisibleAddress(true);

            mapOverlayManager.clearOverlays();
            int markerId = NMapPOIflagType.PIN;
            int id = personList.size()+1;

            // set POI data
            NMapPOIdata poiData = new NMapPOIdata(id, mapResourceProvider);
            poiData.beginPOIdata(id);
            for (Person index:personList) {
                poiData.addPOIitem(index.getAddressPosition().getX(), index.getAddressPosition().getY(), null, markerId, index.getId());
            }

            poiData.endPOIdata();

            NMapPOIdataOverlay poiDataOverlay = mapOverlayManager.createPOIdataOverlay(poiData, null);
            // 해당 오버레이 객체에 포함된 전체 아이템이 화면에 표시되도록 지도 중심 및 축적 레벨을 변경하려면 아래와 같이 구현합니다.
            poiDataOverlay.showAllPOIdata(0);
            // 아이템의 선택 상태가 변경되거나 말풍선이 선택되는 경우를 처리하기 위하여 이벤트 리스너를 등록합니다.
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

            poiDataOverlay.selectPOIitem(0, true);
        } else
            Toast.makeText(view.getContext(), "인원을 추가하세요", Toast.LENGTH_SHORT).show();
    }

    /* NMapDataProvider Listener */
    private final com.nhn.android.maps.NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {
            //if (DEBUG) {
            Log.i(TAG, "onReverseGeocoderResponse: placeMark="
                    + ((placeMark != null) ? placeMark.toString() : null));
            Log.i(TAG, "onReverseGeocoderResponse: placeMark="
                    + ((placeMark != null) ? placeMark.latitude : null));
            Log.i(TAG, "onReverseGeocoderResponse: placeMark="
                    + ((placeMark != null) ? placeMark.longitude : null));
            //}

            mapPlacemark = placeMark;

            if (errInfo != null) {
                Log.e(TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

                Toast.makeText(view.getContext(), errInfo.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            if (floatingPOIitem != null) {
                if (placeMark != null) {
                    floatingPOIitem.setTitle(placeMark.toString());
                    if(tvAddress!=null)
                        tvAddress.setText(placeMark.toString());
                }
            }
        }
    };

    /* MyLocation Listener */
    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
            if (mapController != null) {
                mapController.animateTo(myLocation);
            }

            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {
            Toast.makeText(view.getContext(), "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {
            Toast.makeText(view.getContext(), "Your current location is unavailable area.", Toast.LENGTH_LONG).show();
            stopGPSLocation();
        }
    };

    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        @Override
        public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
            if (nMapPOIitem != null) {
                setVisibleAddress(true);
                Log.i(TAG, "onFocusChanged: " + nMapPOIitem.toString());
                if(personList.size()!=0)
                    if(nMapPOIitem.getId()!=0) {
                        for (Person index:personList) {
                            if(index.getId() == nMapPOIitem.getId()) {
                                targetPerson = index;
                                tvAddress.setText(targetPerson.getAddress());
                                isFixedMarker = true;
                                layoutAddMarker.setBackground(view.getResources().getDrawable(R.drawable.btn_minus));
                                break;
                            }
                        }
                        if(targetPerson == null) {
                            isFixedMarker = false;
                            layoutAddMarker.setBackground(view.getResources().getDrawable(R.drawable.btn_plus));
                            Toast.makeText(view.getContext(), "올바르지 않은 마커가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        isFixedMarker = false;
                        layoutAddMarker.setBackground(view.getResources().getDrawable(R.drawable.btn_plus));
                    }
            } else {
                setVisibleAddress(true);
                Log.i(TAG, "onFocusChanged: ");
            }
        }

        @Override
        public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
            Toast.makeText(view.getContext(), "onCalloutClick: " + nMapPOIitem.getTitle(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "onFocusChanged: " + nMapPOIitem.toString());

        }
    };

    private final NMapPOIdataOverlay.OnFloatingItemChangeListener onPOIdataFloatingItemChangeListener = new NMapPOIdataOverlay.OnFloatingItemChangeListener() {
        @Override
        public void onPointChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            NGeoPoint point = item.getPoint();

            //if (DEBUG) {
            Log.i(TAG, "onPointChanged: point=" + point.toString());
            //}

            mapContext.findPlacemarkAtLocation(point.longitude, point.latitude);

            item.setTitle(null);
        }
    };
}
