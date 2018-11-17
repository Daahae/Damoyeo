package com.daahae.damoyeo.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.model.BuildingDetail;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.presenter.DetailFragmentPresenter;
import com.daahae.damoyeo.presenter.MapsActivityPresenter;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.activity.MapsActivity;
import com.daahae.damoyeo.view.adapter.TransportAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class DetailFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private DetailFragmentPresenter presenter;
    private MapsActivityPresenter parentPresenter;

    private GoogleMap googleMap = null;
    private MapView mapView = null;
    private GoogleApiClient googleApiClient = null;
    private Marker currentMarker = null;

    private Building building;

    private TextView txtBuildingName, txtBuildingAddress, txtBuildingTel, txtDescription, txtBuildingDistance;

    private ImageButton btnBack;
    private View view;

    private FloatingActionButton fabReset;

    public DetailFragment(MapsActivityPresenter parentPresenter){
        this.parentPresenter = parentPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DetailFragmentPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (View) inflater.inflate(R.layout.fragment_detail, container, false);

        RetrofitCommunication.BuildingDetailCallBack buildingDetailCallBack = new RetrofitCommunication.BuildingDetailCallBack() {
            @Override
            public void buildingDetailDataPath(BuildingDetail buildingDetail) {

                initView(view);
                initListener();

                getBuildingInfo();
                setBuildingInfo();

                setBuildingDetail(buildingDetail);
                Log.v("상세 데이터",buildingDetail.getBuildingTel());
                Log.v("상세 데이터",buildingDetail.getBuildingDescription());

                if(googleMap != null) {
                    LatLng latLng = new LatLng(building.getLatitude(), building.getLongitude());
                    setLocation(latLng);
                }
                showBuilding(building);

                presenter.initData(Person.getInstance());

            }
        };
        RetrofitCommunication.getInstance().setBuildingDetailData(buildingDetailCallBack);

        mapView = view.findViewById(R.id.map_detail);
        mapView.getMapAsync(this);

        return view;
    }

    private void initView(View view){

        txtBuildingName = view.findViewById(R.id.txt_building_name_detail);
        txtBuildingAddress = view.findViewById(R.id.txt_building_address_datail);
        txtBuildingTel = view.findViewById(R.id.txt_building_tel_detail);
        txtDescription = view.findViewById(R.id.txt_building_description_detail);
        txtBuildingDistance = view.findViewById(R.id.txt_building_distance_detail);

        btnBack = view.findViewById(R.id.btn_back_building_transport);

        fabReset = view.findViewById(R.id.fab_reset);
    }

    private void initListener(){
        btnBack.setOnClickListener(this);
        fabReset.setOnClickListener(this);
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

        Toast.makeText(getActivity(), "위치정보 가져올 수 없음\n위치 퍼미션과 GPS활성 여부 확인", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(Constant.TAG, "onLocationChanged call..");

        if(MapsActivity.LOGIN_FLG == Constant.GOOGLE_LOGIN) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                // 다이어로그 로그인 토큰 만료 로 인한 재 로그인 유도
                getActivity().setResult(Constant.LOG_OUT);
                getActivity().finish();
            }
        }
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        setLocation(Constant.DEFAULT_LOCATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

            if ( hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                if ( googleApiClient == null)
                    buildGoogleApiClient();
            }
        } else {
            if ( googleApiClient == null)
                buildGoogleApiClient();
        }
    }

    private void setLocation(LatLng latLng) {
        CameraUpdate point = CameraUpdateFactory.newLatLngZoom(latLng, 15.0f);
        googleMap.moveCamera(point);
    }

    private void setBuildingDetail(BuildingDetail buildingDetail){
        if(buildingDetail.getBuildingTel()!=null) txtBuildingTel.setText("tel. "+buildingDetail.getBuildingTel());
        else txtBuildingTel.setVisibility(View.GONE);
        txtDescription.setText(buildingDetail.getBuildingDescription());

    }

    private void getBuildingInfo(){
        building = Building.getInstance();
    }

    private void setBuildingInfo(){
        txtBuildingName.setText(building.getName());
        txtBuildingAddress.setText(building.getBuildingAddress());
        txtBuildingDistance.setText(String.format("%.2f",building.getDistance()));
    }

    public void showBuilding(Building building) {

        MarkerOptions markerOption = new MarkerOptions();
        LatLng latLng = new LatLng(building.getLatitude(), building.getLongitude());
        markerOption.position(latLng);
        markerOption.title(building.getName());
        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentMarker = googleMap.addMarker(markerOption);
        currentMarker.showInfoWindow();
    }

    public void showCurrentMarker() {
        CameraUpdate point = CameraUpdateFactory.newLatLng(currentMarker.getPosition());
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            googleMap.animateCamera(point);
        else
            googleMap.moveCamera(point);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back_building_transport:
                parentPresenter.backView(this);
                break;
            case R.id.fab_reset:
                showCurrentMarker();
                break;
        }
    }
}