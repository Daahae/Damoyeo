package com.daahae.damoyeo.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.exception.PositionNumberServices;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.presenter.MapsActivityPresenter;
import com.daahae.damoyeo.presenter.MapsFragmentPresenter;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.FloatingActionBtn;
import com.daahae.damoyeo.view.function.GPSInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class MapsFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private MapsActivityPresenter parentPresenter;
    private MapsFragmentPresenter presenter;

    private GoogleMap googleMap = null;
    private MapView mapView = null;
    private GoogleApiClient googleApiClient = null;
    private Marker currentMarker = null;
    private Marker clickedMarker = null;
    private LatLngBounds.Builder builder;

    private ArrayList<Marker> markerList;

    private FloatingActionBtn fabtn;

    private GPSInfo gps;

    public MapsFragment(MapsActivityPresenter parentPresenter) {
        this.parentPresenter = parentPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gps = new GPSInfo(getActivity());
        markerList = new ArrayList<Marker>();
        fabtn = new FloatingActionBtn();

        presenter = new MapsFragmentPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mapView = (MapView)rootView.findViewById(R.id.map);
        mapView.getMapAsync(this);

        setPlaceAutoComplete();

        fabtn.setFabOpen(AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open));
        fabtn.setFabClose(AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close));

        fabtn.setFabMenu((FloatingActionButton) rootView.findViewById(R.id.fab_menu));
        fabtn.setFabGPS((FloatingActionButton) rootView.findViewById(R.id.fab_gps));
        fabtn.setFabPick((FloatingActionButton) rootView.findViewById(R.id.fab_pick));
        fabtn.setFabClear((FloatingActionButton) rootView.findViewById(R.id.fab_clear));
        fabtn.setFabFull((FloatingActionButton) rootView.findViewById(R.id.fab_full));
        fabtn.setFabFix((FloatingActionButton) rootView.findViewById(R.id.fab_fix));

        fabtn.getFabMenu().setOnClickListener(this);
        fabtn.getFabGPS().setOnClickListener(this);
        fabtn.getFabPick().setOnClickListener(this);
        fabtn.getFabClear().setOnClickListener(this);
        fabtn.getFabFull().setOnClickListener(this);
        fabtn.getFabFix().setOnClickListener(this);

        LinearLayout linearBtnSearchMid = rootView.findViewById(R.id.linear_search_mid);
        linearBtnSearchMid.setOnClickListener(this);

        return rootView;
    }

    // 검색 자동완성
    private void setPlaceAutoComplete(){

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                Toast.makeText(getContext(), place.getAddress().toString() + place.getPhoneNumber() + "", Toast.LENGTH_SHORT).show();

                setCurrentMarker(true, latLng, place.getName().toString(), place.getAddress().toString());
            }

            @Override
            public void onError(Status status) {
                Log.i(Constant.TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수
        MapsInitializer.initialize(getActivity().getApplicationContext());

        if(mapView != null)
            mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();

        if ( googleApiClient != null && googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        if ( googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

        if ( googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        if ( googleApiClient != null ) {
            googleApiClient.unregisterConnectionCallbacks(this);
            googleApiClient.unregisterConnectionFailedListener(this);

            if ( googleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
                googleApiClient.disconnect();
            }
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if ( !checkLocationServicesStatus()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("위치 서비스 비활성화");
            builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" +
                    "위치 설정을 수정하십시오.");
            builder.setCancelable(true);
            builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent callGPSSettingIntent =
                            new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(callGPSSettingIntent, Constant.GPS_ENABLE_REQUEST_CODE);
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(Constant.UPDATE_INTERVAL_MS);
        locationRequest.setFastestInterval(Constant.FASTEST_UPDATE_INTERVAL_MS);

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                LocationServices.FusedLocationApi
                        .requestLocationUpdates(googleApiClient, locationRequest, this);
            }
        } else {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(googleApiClient, locationRequest, this);

            this.googleMap.getUiSettings().setCompassEnabled(false);
            this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

    }

    @Override
    public void onConnectionSuspended(int cause) {
        if ( cause ==  CAUSE_NETWORK_LOST )
            Log.e(Constant.TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED )
            Log.e(Constant.TAG,"onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        setCurrentMarker(true, Constant.DEFAULT_LOCATION, "위치정보 가져올 수 없음",
                "위치 퍼미션과 GPS활성 여부 확인");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(Constant.TAG, "onLocationChanged call..");
    }
    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // OnMapReadyCallback implements 해야 mapView.getMapAsync(this); 사용가능. this 가 OnMapReadyCallback
        this.googleMap = googleMap;

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에 지도의 초기위치를 서울로 이동
        CameraUpdate point = CameraUpdateFactory.newLatLngZoom(Constant.DEFAULT_LOCATION, 15.0f);
        googleMap.moveCamera(point);
        googleMap.animateCamera(point);

        //  API 23 이상이면 런타임 퍼미션 처리 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 사용권한체크
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

            if ( hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {
                //사용권한이 없을경우
                //권한 재요청
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                //사용권한이 있는경우
                if ( googleApiClient == null)
                    buildGoogleApiClient();
            }
        } else {
            if ( googleApiClient == null)
                buildGoogleApiClient();

            //googleMap.setMyLocationEnabled(true);
        }

        googleMap.setOnMapClickListener(null);
        googleMap.setOnMarkerClickListener(this);
        builder = new LatLngBounds.Builder();
    }

    @Override
    public void onMapClick(LatLng point) {

        Point screenPt = googleMap.getProjection().toScreenLocation(point);

        LatLng latLng = googleMap.getProjection().fromScreenLocation(screenPt);
        Log.d(Constant.TAG, "" + latLng.latitude + latLng.longitude);

        setCurrentMarker(false, latLng, getResources().getString(R.string.msg_add), null);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        clickedMarker = marker;
        if(markerList.contains(clickedMarker))
            fabtn.getFabFix().setImageDrawable(getResources().getDrawable(R.drawable.ic_fab_minus, null));
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_menu:
                fabtn.anim();
                break;
            case R.id.fab_gps:
                setGPS();
                fabtn.anim();
                break;
            case R.id.fab_pick:
                pickMarker();
                fabtn.anim();
                break;
            case R.id.fab_clear:
                setGoogleMapClear();
                fabtn.anim();
                break;
            case R.id.fab_full:
                showAllMarkers();
                fabtn.anim();
                break;
            case R.id.fab_fix:
                fixMarker();
                break;
            case R.id.linear_search_mid:
                parentPresenter.sendMarkerTimeMessage();
                PositionNumberServices positionNumberServices = new PositionNumberServices();
                try {
                    positionNumberServices.isPosition(parentPresenter.getTotalTimes().size());
                    parentPresenter.changeView(Constant.CATEGORY_PAGE);
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    private void setGPS() {
        if (gps.isGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);
            setCurrentMarker(true, latLng, getResources().getString(R.string.msg_add), null);
        } else
            Toast.makeText(getActivity(),  getResources().getString(R.string.msg_gps_disable), Toast.LENGTH_SHORT).show();
    }

    private void pickMarker() {
        googleMap.setOnMapClickListener(this);
        LatLng latLng = googleMap.getCameraPosition().target;
        setCurrentMarker(false, latLng, getResources().getString(R.string.msg_default), null);
    }

    private void setDataClear() {
        currentMarker = null;
        clickedMarker = null;
        fabtn.getFabFix().setImageDrawable(getResources().getDrawable(R.drawable.ic_fab_plus, null));
        googleMap.setOnMapClickListener(null);
    }

    private void setGoogleMapClear() {
        googleMap.clear();
        Person.getInstance().clear();
        markerList.clear();
        setDataClear();
        CameraUpdate point = CameraUpdateFactory.newLatLngZoom(Constant.DEFAULT_LOCATION, 15.0f);
        googleMap.moveCamera(point);
        googleMap.animateCamera(point);
        builder = new LatLngBounds.Builder();
    }

    private void fixMarker() {
        if(clickedMarker != null) {
            if(markerList.contains(clickedMarker))
                removeMarker();
            else
                saveMarker(clickedMarker);
            setDataClear();
        } else {
            if(currentMarker != null) {
                saveMarker(currentMarker);
                currentMarker = null;
            }
            else
                Toast.makeText(getActivity(), getResources().getString(R.string.msg_checkmarker), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveMarker(Marker marker) {
        ArrayList<Person> personList = Person.getInstance();
        int id = personList.size() + 1;
        String address = marker.getSnippet();
        Position position = new Position(marker.getPosition().latitude, marker.getPosition().longitude);
        Person person = new Person(getResources().getString(R.string.guest) + id, address, position);
        personList.add(person);
        markerList.add(marker);
        marker.setTitle(person.getName());
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        marker.showInfoWindow();

        builder.include(marker.getPosition());

        Toast.makeText(getActivity(), person.getName() + getResources().getString(R.string.msg_savemarker), Toast.LENGTH_SHORT).show();
    }

    private void removeMarker() {
        int targetIndex = markerList.indexOf(clickedMarker);
        markerList.remove(targetIndex);
        ArrayList<Person> personList = Person.getInstance();
        Person person = personList.remove(targetIndex);
        Toast.makeText(getActivity(), person.getName() + getResources().getString(R.string.msg_removemarker), Toast.LENGTH_SHORT).show();
        clickedMarker.remove();
    }

    private void showAllMarkers() {
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.animateCamera(cu);
    }

    // 마커 찍기
    private void setCurrentMarker(boolean flag, LatLng latLng, String markerTitle, String markerSnippet) {
        clickedMarker = null;
        fabtn.getFabFix().setImageDrawable(getResources().getDrawable(R.drawable.ic_fab_plus, null));
        if ( currentMarker != null ) currentMarker.remove();

        if ( latLng != null) {
            //현재위치의 위도 경도 가져옴

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(markerTitle);
            markerOptions.snippet(markerSnippet);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            currentMarker = googleMap.addMarker(markerOptions);
            currentMarker.showInfoWindow();

            if(flag)
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            return;
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(Constant.DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = googleMap.addMarker(markerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Constant.DEFAULT_LOCATION));
    }
}
