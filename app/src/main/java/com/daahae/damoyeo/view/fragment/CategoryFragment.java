package com.daahae.damoyeo.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.exception.ExceptionHandle;
import com.daahae.damoyeo.exception.ExceptionService;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.model.Landmark;
import com.daahae.damoyeo.model.MidInfo;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.presenter.CategoryPresenter;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.activity.MainActivity;
import com.daahae.damoyeo.view.activity.TransportActivity;
import com.daahae.damoyeo.view.adapter.BuildingAdapter;
import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class CategoryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener
,SlidingDrawer.OnDrawerOpenListener,SlidingDrawer.OnDrawerCloseListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private MainActivity parentView;
    private CategoryPresenter presenter;

    private MapView mapView = null;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient = null;
    private LatLngBounds.Builder builder;

    private ImageButton btnBack;

    private ArrayList<String> totalTimes = null;
    private BuildingArr buildingArr;

    private MarkerTimeAdapter markerTimeAdapter;
    private ListView listMarkerTime;
    private Button btnAllMarkerList;

    private SlidingDrawer slidingDrawer;

    private LinearLayout linearContent;
    private LinearLayout linearHandleMenu;
    private LinearLayout linearMarkerTime;
    private RelativeLayout relativeMap;

    private BuildingAdapter buildingAdapter;
    private ListView listCategory;

    private FloatingActionButton fabMid;
    public static boolean isMid = false;

    private ImageButton btnDownSlidingDrawer;
    private ImageButton btnDepartment, btnShopping, btnStadium, btnZoo, btnMuseum, btnTheater, btnAquarium, btnCafe, btnDrink, btnRestaurant, btnEtc;

    private TextView txtDefault;
    private TextView txtSelectedCategory;

    private ImageView imgLoading;

    private Button btnSortScore, btnSortDistance;

    public CategoryFragment(MainActivity parentView) {
        this.parentView = parentView;
    }

    public boolean isMid() {
        return isMid;
    }

    public ListView getListMarkerTime() {
        return listMarkerTime;
    }

    public ListView getListCategory() {
        return listCategory;
    }

    public MarkerTimeAdapter getMarkerTimeAdapter() {
        return markerTimeAdapter;
    }

    public BuildingAdapter getBuildingAdapter() {
        return buildingAdapter;
    }

    public RelativeLayout getRelativeMap() {
        return relativeMap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new CategoryPresenter(this);
        markerTimeAdapter = new MarkerTimeAdapter();
        buildingAdapter = new BuildingAdapter();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_category, container, false);

        initView(rootView);
        initListener();

        setLoadingAnimation();

        setMarkerTimeList(markerTimeAdapter);
        listMarkerTime.setAdapter(markerTimeAdapter);

        presenter.startCallback();

        return rootView;
    }

    private void initView(View rootView){

        relativeMap = rootView.findViewById(R.id.relative_map);
        mapView = rootView.findViewById(R.id.map_category);
        mapView.getMapAsync(this);
        fabMid = rootView.findViewById(R.id.fab_mid);

        btnBack = rootView.findViewById(R.id.btn_back_category);
        listMarkerTime = rootView.findViewById(R.id.list_marker_time);
        btnAllMarkerList = rootView.findViewById(R.id.btn_all_marker_list);

        listCategory = rootView.findViewById(R.id.list_category);

        linearContent = rootView.findViewById(R.id.content);

        linearHandleMenu = rootView.findViewById(R.id.linear_handle_menu);
        linearMarkerTime = rootView.findViewById(R.id.linear_marker_time);
        slidingDrawer = rootView.findViewById(R.id.slide);

        btnDownSlidingDrawer = rootView.findViewById(R.id.btn_down_sliding_drawer_category);
        btnDepartment = rootView.findViewById(R.id.btn_department_store_category);
        btnShopping = rootView.findViewById(R.id.btn_shopping_category);
        btnStadium = rootView.findViewById(R.id.btn_stadium_category);
        btnZoo = rootView.findViewById(R.id.btn_zoo_category);
        btnMuseum = rootView.findViewById(R.id.btn_museum_category);
        btnTheater = rootView.findViewById(R.id.btn_theater_category);
        btnAquarium = rootView.findViewById(R.id.btn_aquarium_store_category);
        btnCafe = rootView.findViewById(R.id.btn_cafe_category);
        btnDrink = rootView.findViewById(R.id.btn_drink_category);
        btnRestaurant = rootView.findViewById(R.id.btn_restaurant_store_category);
        btnEtc = rootView.findViewById(R.id.btn_etc_category);

        txtSelectedCategory = rootView.findViewById(R.id.txt_selected_category);
        txtDefault = rootView.findViewById(R.id.txt_list_category_default);
        imgLoading = rootView.findViewById(R.id.img_loading_category);

        btnSortScore = rootView.findViewById(R.id.btn_score_sort_building);
        btnSortDistance = rootView.findViewById(R.id.btn_distance_sort_building);
    }

    private void initListener(){

        btnBack.setOnClickListener(this);

        btnAllMarkerList.setOnClickListener(this);
        fabMid.setOnClickListener(this);

        //각 리스트 아이템 클릭
        listMarkerTime.setOnItemClickListener(this);
        listCategory.setOnItemClickListener(this);

        //SlidingDrawer 내려가는 기능
        linearContent.setOnTouchListener(this);
        btnDownSlidingDrawer.setOnTouchListener(this);
        btnDownSlidingDrawer.setOnClickListener(this);

        //SlidingDrawer 내려갔을때 view
        slidingDrawer.setOnDrawerCloseListener(this);

        //SlidingDrawer 올라갔을때 view
        slidingDrawer.setOnDrawerOpenListener(this);

        btnDepartment.setOnClickListener(this);
        btnShopping .setOnClickListener(this);
        btnStadium.setOnClickListener(this);
        btnZoo.setOnClickListener(this);
        btnMuseum.setOnClickListener(this);
        btnTheater.setOnClickListener(this);
        btnAquarium.setOnClickListener(this);
        btnCafe .setOnClickListener(this);
        btnDrink.setOnClickListener(this);
        btnRestaurant.setOnClickListener(this);
        btnEtc.setOnClickListener(this);

        btnSortScore.setOnClickListener(this);
        btnSortDistance.setOnClickListener(this);

    }


    private void setLoadingAnimation(){
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
        imgLoading.setAnimation(anim);
    }

    public void convertList(boolean flag){
        if(flag) {
            listCategory.setVisibility(View.VISIBLE);
            txtDefault.setVisibility(View.GONE);
            imgLoading.setVisibility(View.GONE);
            imgLoading.clearAnimation();
        } else{
            txtDefault.setVisibility(View.VISIBLE);
            listCategory.setVisibility(View.GONE);
            imgLoading.setVisibility(View.GONE);
            imgLoading.clearAnimation();
        }
    }

    private void setLoading(){
        listCategory.setVisibility(View.GONE);
        txtDefault.setVisibility(View.GONE);
        imgLoading.setVisibility(View.VISIBLE);

        setLoadingAnimation();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

    public MainActivity getParentView() {
        return parentView;
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

        if(MainActivity.LOGIN_FLG == Constant.GOOGLE_LOGIN) {
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
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

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
        CameraUpdate point = CameraUpdateFactory.newLatLngZoom(Constant.DEFAULT_LOCATION, 15.0f);
        googleMap.moveCamera(point);
    }

    @Override
    public void onDrawerClosed() {
        linearHandleMenu.setVisibility(View.VISIBLE);
        linearMarkerTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDrawerOpened() {
        RetrofitCommunication.getInstance().setBuildingsData(Constant.DEPARTMENT_STORE);
        linearHandleMenu.setVisibility(View.GONE);
        linearMarkerTime.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int id = view.getId();
        switch (id){
            case R.id.btn_down_sliding_drawer_category:
            case R.id.content:
            case R.id.list_category:
                int action = motionEvent.getAction();

                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        slidingDrawer.animateClose();
                        break;
                }

                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.CATEGORY_PAGE:

                if(!isMid) {
                    if(resultCode == -1)
                        showAllMarkers();
                    else
                        showEachMarker(resultCode);
                } else {
                    if(resultCode == -1)
                        showLandmarkAllMarkers();
                    else
                        showLandmarkEachMarker(resultCode);
                }
                setCameraState(relativeMap);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(parent.equals(listMarkerTime)){
            Intent intent = new Intent(getActivity(), TransportActivity.class);
            startActivityForResult(intent, Constant.CATEGORY_PAGE);
        } else {
            parentView.changeView(Constant.DETAIL_PAGE);
            presenter.getBuildingDetailFromServer(position);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_back_category:
                totalTimes.clear();
                parentView.changeView(Constant.MAPS_PAGE);
                Constant.existLandmarkTransport = false;
                break;
            case R.id.btn_all_marker_list:
                if(!isMid) {
                    showAllMarkers();
                    setCameraState(relativeMap);
                } else {
                    showLandmarkAllMarkers();
                    setCameraState(relativeMap);
                }
                break;
            case R.id.fab_mid:
                if (isMid) {
                    presenter.setMidInfoTransport();
                    showAllMarkers();
                    setCameraState(relativeMap);
                    fabMid.setImageResource(R.drawable.btn_selected_landmark_orange);
                    isMid = false;
                } else {
                    if(!Constant.existLandmarkTransport){
                        presenter.clickLandmark();
                        Constant.existLandmarkTransport = true;
                    }
                    else presenter.setLandmarkTransport();
                    showLandmarkAllMarkers();
                    setCameraState(relativeMap);
                    fabMid.setImageResource(R.drawable.btn_selected_mid_orange);
                    isMid = true;
                }
                break;

            case R.id.btn_down_sliding_drawer_category:
                slidingDrawer.animateClose();
                break;

            case R.id.btn_department_store_category:
                presenter.setSelectCategory(Constant.DEPARTMENT_STORE);
                setLoading();

                btnDepartment.setImageResource(R.drawable.ic_department_store_orange);
                btnShopping .setImageResource(R.drawable.ic_shopping_gray);
                btnStadium.setImageResource(R.drawable.ic_stadium_gray);
                btnZoo.setImageResource(R.drawable.ic_zoo_gray);
                btnMuseum.setImageResource(R.drawable.ic_museum_gray);
                btnTheater.setImageResource(R.drawable.ic_theater_gray);
                btnAquarium.setImageResource(R.drawable.ic_aquarium_gray);
                btnCafe.setImageResource(R.drawable.ic_cafe_gray);
                btnDrink.setImageResource(R.drawable.ic_drink_gray);
                btnRestaurant.setImageResource(R.drawable.ic_restaurant_gray);
                btnEtc.setImageResource(R.drawable.btn_etc_gray);

                txtSelectedCategory.setText(getResources().getString(R.string.category_department_store));
                break;
            case R.id.btn_shopping_category:
                presenter.setSelectCategory(Constant.SHOPPING_MALL);
                setLoading();

                btnDepartment.setImageResource(R.drawable.ic_department_store_gray);
                btnShopping .setImageResource(R.drawable.ic_shopping_mall_orange);
                btnStadium.setImageResource(R.drawable.ic_stadium_gray);
                btnZoo.setImageResource(R.drawable.ic_zoo_gray);
                btnMuseum.setImageResource(R.drawable.ic_museum_gray);
                btnTheater.setImageResource(R.drawable.ic_theater_gray);
                btnAquarium.setImageResource(R.drawable.ic_aquarium_gray);
                btnCafe.setImageResource(R.drawable.ic_cafe_gray);
                btnDrink.setImageResource(R.drawable.ic_drink_gray);
                btnRestaurant.setImageResource(R.drawable.ic_restaurant_gray);
                btnEtc.setImageResource(R.drawable.btn_etc_gray);

                txtSelectedCategory.setText(getResources().getString(R.string.category_shopping_mall));
                break;
            case R.id.btn_stadium_category:
                presenter.setSelectCategory(Constant.STADIUM);
                setLoading();

                btnDepartment.setImageResource(R.drawable.ic_department_store_gray);
                btnShopping .setImageResource(R.drawable.ic_shopping_gray);
                btnStadium.setImageResource(R.drawable.ic_stadium_orange);
                btnZoo.setImageResource(R.drawable.ic_zoo_gray);
                btnMuseum.setImageResource(R.drawable.ic_museum_gray);
                btnTheater.setImageResource(R.drawable.ic_theater_gray);
                btnAquarium.setImageResource(R.drawable.ic_aquarium_gray);
                btnCafe.setImageResource(R.drawable.ic_cafe_gray);
                btnDrink.setImageResource(R.drawable.ic_drink_gray);
                btnRestaurant.setImageResource(R.drawable.ic_restaurant_gray);
                btnEtc.setImageResource(R.drawable.btn_etc_gray);

                txtSelectedCategory.setText(getResources().getString(R.string.category_stadium));
                break;
            case R.id.btn_zoo_category:
                presenter.setSelectCategory(Constant.ZOO);
                setLoading();

                btnDepartment.setImageResource(R.drawable.ic_department_store_gray);
                btnShopping .setImageResource(R.drawable.ic_shopping_gray);
                btnStadium.setImageResource(R.drawable.ic_stadium_gray);
                btnZoo.setImageResource(R.drawable.ic_zoo_orange);
                btnMuseum.setImageResource(R.drawable.ic_museum_gray);
                btnTheater.setImageResource(R.drawable.ic_theater_gray);
                btnAquarium.setImageResource(R.drawable.ic_aquarium_gray);
                btnCafe.setImageResource(R.drawable.ic_cafe_gray);
                btnDrink.setImageResource(R.drawable.ic_drink_gray);
                btnRestaurant.setImageResource(R.drawable.ic_restaurant_gray);
                btnEtc.setImageResource(R.drawable.btn_etc_gray);
                txtSelectedCategory.setText(getResources().getString(R.string.category_zoo));
                break;
            case R.id.btn_museum_category:
                presenter.setSelectCategory(Constant.MUSEUM);
                setLoading();

                btnDepartment.setImageResource(R.drawable.ic_department_store_gray);
                btnShopping .setImageResource(R.drawable.ic_shopping_gray);
                btnStadium.setImageResource(R.drawable.ic_stadium_gray);
                btnZoo.setImageResource(R.drawable.ic_zoo_gray);
                btnMuseum.setImageResource(R.drawable.ic_museum_orange);
                btnTheater.setImageResource(R.drawable.ic_theater_gray);
                btnAquarium.setImageResource(R.drawable.ic_aquarium_gray);
                btnCafe.setImageResource(R.drawable.ic_cafe_gray);
                btnDrink.setImageResource(R.drawable.ic_drink_gray);
                btnRestaurant.setImageResource(R.drawable.ic_restaurant_gray);
                btnEtc.setImageResource(R.drawable.btn_etc_gray);
                txtSelectedCategory.setText(getResources().getString(R.string.category_museum));
                break;
            case R.id.btn_theater_category:
                presenter.setSelectCategory(Constant.MOVIE_THEATER);
                setLoading();

                btnDepartment.setImageResource(R.drawable.ic_department_store_gray);
                btnShopping .setImageResource(R.drawable.ic_shopping_gray);
                btnStadium.setImageResource(R.drawable.ic_stadium_gray);
                btnZoo.setImageResource(R.drawable.ic_zoo_gray);
                btnMuseum.setImageResource(R.drawable.ic_museum_gray);
                btnTheater.setImageResource(R.drawable.ic_theater_orange);
                btnAquarium.setImageResource(R.drawable.ic_aquarium_gray);
                btnCafe.setImageResource(R.drawable.ic_cafe_gray);
                btnDrink.setImageResource(R.drawable.ic_drink_gray);
                btnRestaurant.setImageResource(R.drawable.ic_restaurant_gray);
                btnEtc.setImageResource(R.drawable.btn_etc_gray);
                txtSelectedCategory.setText(getResources().getString(R.string.category_theater));
                break;
            case R.id.btn_aquarium_store_category:
                presenter.setSelectCategory(Constant.AQUARIUM);
                setLoading();

                btnDepartment.setImageResource(R.drawable.ic_department_store_gray);
                btnShopping .setImageResource(R.drawable.ic_shopping_gray);
                btnStadium.setImageResource(R.drawable.ic_stadium_gray);
                btnZoo.setImageResource(R.drawable.ic_zoo_gray);
                btnMuseum.setImageResource(R.drawable.ic_museum_gray);
                btnTheater.setImageResource(R.drawable.ic_theater_gray);
                btnAquarium.setImageResource(R.drawable.ic_aquarium_orange);
                btnCafe.setImageResource(R.drawable.ic_cafe_gray);
                btnDrink.setImageResource(R.drawable.ic_drink_gray);
                btnRestaurant.setImageResource(R.drawable.ic_restaurant_gray);
                btnEtc.setImageResource(R.drawable.btn_etc_gray);
                txtSelectedCategory.setText(getResources().getString(R.string.category_aquarium));
                break;
            case R.id.btn_cafe_category:
                presenter.setSelectCategory(Constant.CAFE);
                setLoading();

                btnDepartment.setImageResource(R.drawable.ic_department_store_gray);
                btnShopping .setImageResource(R.drawable.ic_shopping_gray);
                btnStadium.setImageResource(R.drawable.ic_stadium_gray);
                btnZoo.setImageResource(R.drawable.ic_zoo_gray);
                btnMuseum.setImageResource(R.drawable.ic_museum_gray);
                btnTheater.setImageResource(R.drawable.ic_theater_gray);
                btnAquarium.setImageResource(R.drawable.ic_aquarium_gray);
                btnCafe.setImageResource(R.drawable.ic_cafe_orange);
                btnDrink.setImageResource(R.drawable.ic_drink_gray);
                btnRestaurant.setImageResource(R.drawable.ic_restaurant_gray);
                btnEtc.setImageResource(R.drawable.btn_etc_gray);
                txtSelectedCategory.setText(getResources().getString(R.string.category_cafe));
                break;
            case R.id.btn_drink_category:
                presenter.setSelectCategory(Constant.DRINK);
                setLoading();

                btnDepartment.setImageResource(R.drawable.ic_department_store_gray);
                btnShopping .setImageResource(R.drawable.ic_shopping_gray);
                btnStadium.setImageResource(R.drawable.ic_stadium_gray);
                btnZoo.setImageResource(R.drawable.ic_zoo_gray);
                btnMuseum.setImageResource(R.drawable.ic_museum_gray);
                btnTheater.setImageResource(R.drawable.ic_theater_gray);
                btnAquarium.setImageResource(R.drawable.ic_aquarium_gray);
                btnCafe.setImageResource(R.drawable.ic_cafe_gray);
                btnDrink.setImageResource(R.drawable.ic_drink_orange);
                btnRestaurant.setImageResource(R.drawable.ic_restaurant_gray);
                btnEtc.setImageResource(R.drawable.btn_etc_gray);
                txtSelectedCategory.setText(getResources().getString(R.string.category_drink));
                break;
            case R.id.btn_restaurant_store_category:
                presenter.setSelectCategory(Constant.RESTAURANT);
                setLoading();

                btnDepartment.setImageResource(R.drawable.ic_department_store_gray);
                btnShopping .setImageResource(R.drawable.ic_shopping_gray);
                btnStadium.setImageResource(R.drawable.ic_stadium_gray);
                btnZoo.setImageResource(R.drawable.ic_zoo_gray);
                btnMuseum.setImageResource(R.drawable.ic_museum_gray);
                btnTheater.setImageResource(R.drawable.ic_theater_gray);
                btnAquarium.setImageResource(R.drawable.ic_aquarium_gray);
                btnCafe.setImageResource(R.drawable.ic_cafe_gray);
                btnDrink.setImageResource(R.drawable.ic_drink_gray);
                btnRestaurant.setImageResource(R.drawable.ic_restaurant_orange);
                btnEtc.setImageResource(R.drawable.btn_etc_gray);
                txtSelectedCategory.setText(getResources().getString(R.string.category_restaurant));
                break;
            case R.id.btn_etc_category:
                presenter.setSelectCategory(Constant.ETC);
                setLoading();

                btnDepartment.setImageResource(R.drawable.ic_department_store_gray);
                btnShopping .setImageResource(R.drawable.ic_shopping_gray);
                btnStadium.setImageResource(R.drawable.ic_stadium_gray);
                btnZoo.setImageResource(R.drawable.ic_zoo_gray);
                btnMuseum.setImageResource(R.drawable.ic_museum_gray);
                btnTheater.setImageResource(R.drawable.ic_theater_gray);
                btnAquarium.setImageResource(R.drawable.ic_aquarium_gray);
                btnCafe.setImageResource(R.drawable.ic_cafe_gray);
                btnDrink.setImageResource(R.drawable.ic_drink_gray);
                btnRestaurant.setImageResource(R.drawable.ic_restaurant_gray);
                btnEtc.setImageResource(R.drawable.btn_etc_orange);
                txtSelectedCategory.setText(getResources().getString(R.string.category_etc));
                break;


            case R.id.btn_score_sort_building:
                buildingAdapter.sortScore();
                break;
            case R.id.btn_distance_sort_building:
                buildingAdapter.sortDistance();
                break;

        }
    }

    public void initMarkerTime(ArrayList<String> totalTimes){
        this.totalTimes = totalTimes;
        Log.v("시간",this.totalTimes.toString());
    }

    public void setMarkerTimeList(MarkerTimeAdapter markerTimeAdapter) {

        markerTimeAdapter.resetList();
        if(totalTimes!=null){
            for(int i=0; i < totalTimes.size();i++) {
                Log.v("시간", totalTimes.get(i));
                markerTimeAdapter.add(Person.getInstance().get(i).getName(), totalTimes.get(i));
            }
        }
    }

    public void initBuildingInfo(BuildingArr buildingArr){
        this.buildingArr = buildingArr;
    }

    public boolean setBuildingInfo(BuildingAdapter buildingAdapter) {

        buildingAdapter.resetList();
        try {
            ExceptionService.getInstance().isExistBuilding(buildingArr);
        } catch (ExceptionHandle exceptionHandle) {
            exceptionHandle.printStackTrace();
            return false;
        }

        for (Building data:buildingArr.getBuildingArr())
            buildingAdapter.add(data);

        return true;
    }

    public void setCameraState(RelativeLayout relativeMap) {
        LatLngBounds bounds = builder.build();
        int width = relativeMap.getWidth();
        int height = relativeMap.getHeight();
        int padding = (int) (height * 0.10);
        CameraUpdate point = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            googleMap.animateCamera(point);
        else
            googleMap.moveCamera(point);
    }

    public void showAllMarkers() {
        googleMap.clear();
        builder = new LatLngBounds.Builder();

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(MidInfo.getInstance().getLatLng());
        markerOption.title(Constant.DEFAULT_MIDINFO_NAME);
        markerOption.snippet(MidInfo.getInstance().getAddress());
        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(markerOption).showInfoWindow();

        builder.include(markerOption.getPosition());

        for (Person person : Person.getInstance()) {
            String markerTitle = person.getName();
            String markerSnippet = person.getAddress();
            LatLng latLng = new LatLng(person.getAddressPosition().getX(), person.getAddressPosition().getY());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(markerTitle);
            markerOptions.snippet(markerSnippet);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            googleMap.addMarker(markerOptions);

            builder.include(markerOptions.getPosition());
        }
    }

    public void showEachMarker(int index) {
        googleMap.clear();
        builder = new LatLngBounds.Builder();

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(MidInfo.getInstance().getLatLng());
        markerOption.title(Constant.DEFAULT_MIDINFO_NAME);
        markerOption.snippet(MidInfo.getInstance().getAddress());
        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(markerOption).showInfoWindow();

        builder.include(markerOption.getPosition());

        Person person = Person.getInstance().get(index);
        String markerTitle = person.getName();
        String markerSnippet = person.getAddress();
        LatLng latLng = new LatLng(person.getAddressPosition().getX(), person.getAddressPosition().getY());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        googleMap.addMarker(markerOptions);

        builder.include(markerOptions.getPosition());

        // drawRoute(MidInfo.getInstance().getLatLng(), latLng);
    }

    public void showLandmarkAllMarkers() {
        googleMap.clear();
        builder = new LatLngBounds.Builder();

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(Landmark.getInstance().getLatLng());
        markerOption.title(Landmark.getInstance().getName());
        markerOption.snippet(Landmark.getInstance().getAddress());
        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(markerOption).showInfoWindow();

        builder.include(markerOption.getPosition());

        for (Person person : Person.getInstance()) {
            String markerTitle = person.getName();
            String markerSnippet = person.getAddress();
            LatLng latLng = new LatLng(person.getAddressPosition().getX(), person.getAddressPosition().getY());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(markerTitle);
            markerOptions.snippet(markerSnippet);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            googleMap.addMarker(markerOptions);

            builder.include(markerOptions.getPosition());
        }
    }

    public void showLandmarkEachMarker(int index) {
        googleMap.clear();
        builder = new LatLngBounds.Builder();

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(Landmark.getInstance().getLatLng());
        markerOption.title(Landmark.getInstance().getName());
        markerOption.snippet(Landmark.getInstance().getAddress());
        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(markerOption).showInfoWindow();

        builder.include(markerOption.getPosition());

        Person person = Person.getInstance().get(index);
        String markerTitle = person.getName();
        String markerSnippet = person.getAddress();
        LatLng latLng = new LatLng(person.getAddressPosition().getX(), person.getAddressPosition().getY());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        googleMap.addMarker(markerOptions);

        builder.include(markerOptions.getPosition());

        // drawRoute(Landmark.getInstance().getLatLng(), latLng);
    }

    private void drawRoute(LatLng startLatlng, LatLng endLatLng) {
        PolylineOptions options = new PolylineOptions().add(startLatlng).add(endLatLng).width(10).color(Color.GRAY).geodesic(true);
        googleMap.addPolyline(options);
    }
}
