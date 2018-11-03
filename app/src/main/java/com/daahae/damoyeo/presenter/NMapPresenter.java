package com.daahae.damoyeo.presenter;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.view.NMapContract;
import com.daahae.damoyeo.view.data.Constant;
import com.daahae.damoyeo.view.data.NMapViewerResourceProvider;
import com.daahae.damoyeo.view.function.GPSInfo;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import java.util.ArrayList;

public class NMapPresenter implements NMapContract.Presenter{
    private Fragment view;
    private NMapContext mapContext;

    private NMapView mapView;
    private NMapController controller;

    private NMapResourceProvider resourceProvider;
    private NMapOverlayManager overlayManager;

    private NMapLocationManager locationManager;
    private NMapMyLocationOverlay locationOverlay;

    public NMapPresenter(Fragment view, NMapContext mapContext) {
        this.view = view;
        this.mapContext = mapContext;
    }

    public NMapContext getMapContext() {
        return mapContext;
    }

    public NMapView getMapView() {
        return mapView;
    }

    public void setMapView(NMapView mapView) {
        this.mapView = mapView;
    }

    public NMapController getController() {
        return controller;
    }

    public NMapResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    public NMapOverlayManager getOverlayManager() {
        return overlayManager;
    }

    public NMapLocationManager getLocationManager() {
        return locationManager;
    }

    public NMapMyLocationOverlay getLocationOverlay() {
        return locationOverlay;
    }

    // Fragment에 포함된 NMapView 객체를 반환함
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

    @Override
    public void init(Fragment view){
        mapView = findMapView(view.getView());
        if (mapView == null) {
            throw new IllegalArgumentException(view.getResources().getString(R.string.exception_mapview));
        }

        mapView.setClientId(view.getResources().getString(R.string.NAVER_API_KEY));

        // initialize map view
        mapView.setClickable(true);

        // NMapActivity를 상속하지 않는 경우에는 NMapView 객체 생성후 반드시 setupMapView()를 호출해야함.
        mapContext.setupMapView(mapView);

        mapView.setScalingFactor(4.0F, true);

        // use map controller to zoom in/out, pan and set map center, zoom level etc.
        controller = mapView.getMapController();
        controller.setMapCenter(new NGeoPoint(Constant.longitude, Constant.latitude), 11);     //Default Data

        resourceProvider = new NMapViewerResourceProvider(view.getContext());
        overlayManager = new NMapOverlayManager(view.getActivity(), mapView, resourceProvider);

        locationManager = new NMapLocationManager(view.getActivity());;
        locationManager.setOnLocationChangeListener(onMyLocationChangeListener);

        locationOverlay = overlayManager.createMyLocationOverlay(locationManager, null);;
    }

    @Override
    public void stopGPSLocation() {
        if (locationOverlay != null) {
            locationManager.disableMyLocation();
            overlayManager.removeOverlay(locationOverlay);
        }
    }

    @Override
    public void initLocation(ArrayList<Person> personList) {
        stopGPSLocation();

        overlayManager.clearOverlays();
        controller.setMapCenter(new NGeoPoint(Constant.longitude, Constant.latitude), 11);
    }

    @Override
    public void getPermission(final Fragment view) {
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
                .setRationaleMessage(view.getResources().getString(R.string.permission_rationalemsg))
                .setDeniedMessage(view.getResources().getString(R.string.permission_deniedmsg))
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    @Override
    public void getGPSOverlay() {
        if (!overlayManager.hasOverlay(locationOverlay))
            overlayManager.addOverlay(locationOverlay);
    }

    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
            if (controller != null) {
                controller.animateTo(myLocation);
            }

            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {
            stopGPSLocation();
        }
    };

    public NMapActivity.OnDataProviderListener getOnDataProviderListener() {
        return onDataProviderListener;
    }

    public NMapPOIdataOverlay.OnStateChangeListener getOnPOIdataStateChangeListener() {
        return onPOIdataStateChangeListener;
    }

    public NMapPOIdataOverlay.OnFloatingItemChangeListener getOnPOIdataFloatingItemChangeListener() {
        return onPOIdataFloatingItemChangeListener;
    }

    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        @Override
        public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
        }

        @Override
        public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
        }
    };

    private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

        @Override
        public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {
        }
    };

    private final NMapPOIdataOverlay.OnFloatingItemChangeListener onPOIdataFloatingItemChangeListener = new NMapPOIdataOverlay.OnFloatingItemChangeListener() {
        @Override
        public void onPointChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            NGeoPoint point = item.getPoint();

            // 좌표에 따른 주소
            mapContext.findPlacemarkAtLocation(point.longitude, point.latitude);
            item.setTitle(null);
        }
    };
}
