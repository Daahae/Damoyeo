package com.daahae.damoyeo.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.presenter.CategoryFragmentPresenter;
import com.daahae.damoyeo.presenter.MapsActivityPresenter;
import com.daahae.damoyeo.view.Constant;
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

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class CategoryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener
,SlidingDrawer.OnDrawerOpenListener,SlidingDrawer.OnDrawerCloseListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private CategoryFragmentPresenter presenter;
    private MapsActivityPresenter parentPresenter;

    private GoogleMap googleMap = null;
    private MapView mapView = null;
    private GoogleApiClient googleApiClient = null;

    private ImageButton btnBack;

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
    private boolean isMid = false;

    private ImageButton btnDownSlidingDrawer;
    private ImageButton btnDepartment, btnShopping, btnStadium, btnZoo, btnMuseum, btnTheater, btnAquarium, btnCafe, btnDrink, btnRestaurant;

    private TextView txtDefault;
    private TextView txtSelectedCategory;

    private ImageView imgLoading;

    public CategoryFragment(MapsActivityPresenter parentPresenter) {
        this.parentPresenter = parentPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new CategoryFragmentPresenter(this);
        buildingAdapter = new BuildingAdapter();
        markerTimeAdapter = new MarkerTimeAdapter();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_category, container, false);

        RetrofitCommunication.getInstance().setBuildingsData(Constant.DEPARTMENT_STORE);

        initView(rootView);
        initListener();

        setLoadingAnimation();

        presenter.setMarkerTimeList(markerTimeAdapter);
        listMarkerTime.setAdapter(markerTimeAdapter);

        RetrofitCommunication.UserCallBack userCallBack = new RetrofitCommunication.UserCallBack() {
            @Override
            public void userDataPath(ArrayList<String> totalTimes) {
                presenter.initMarkerTime(totalTimes);
                presenter.setMarkerTimeList(markerTimeAdapter);
                listMarkerTime.setAdapter(markerTimeAdapter);
                Log.v("데이터","들어감");
            }
        };
        RetrofitCommunication.getInstance().setUserData(userCallBack);
        RetrofitCommunication.BuildingCallBack buildingCallBack = new RetrofitCommunication.BuildingCallBack() {
            @Override
            public void buildingDataPath(BuildingArr buildingArr) {
                presenter.initBuildingInfo(buildingArr);
                convertList(presenter.setBuildingInfo(buildingAdapter));
                listCategory.setAdapter(buildingAdapter);
            }
        };
        RetrofitCommunication.getInstance().setBuildingData(buildingCallBack);

        relativeMap = rootView.findViewById(R.id.relative_map);

        return rootView;
    }

    private void initView(View rootView){

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

        txtSelectedCategory = rootView.findViewById(R.id.txt_selected_category);
        txtDefault = rootView.findViewById(R.id.txt_list_category_default);
        imgLoading = rootView.findViewById(R.id.img_loading_category);
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

    }

    private void setLoadingAnimation(){
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
        imgLoading.setAnimation(anim);
    }

    private void convertList(boolean flag){
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

    public void setBuildingList(){
        presenter.setBuildingInfo(buildingAdapter); // 빌딩 정보 넣기
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

        Toast.makeText(getActivity(), "위치정보 가져올 수 없음\n위치 퍼미션과 GPS활성 여부 확인", Toast.LENGTH_SHORT).show();
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
        }
        presenter.setGMapSetting(googleMap);
        presenter.showAllMarkers();
        presenter.setCameraState(relativeMap);
    }

    @Override
    public void onDrawerClosed() {
        linearHandleMenu.setVisibility(View.VISIBLE);
        linearMarkerTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDrawerOpened() {
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
        if(!isMid) {
            presenter.showEachMarker(resultCode);
            presenter.setCameraState(relativeMap);
        } else {
            presenter.showLandmarkEachMarker(resultCode);
            presenter.setCameraState(relativeMap);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(parent.equals(listMarkerTime)){
            Intent intent = new Intent(getActivity(), TransportActivity.class);
            startActivityForResult(intent, 0);
        } else {
            parentPresenter.changeView(Constant.DETAIL_PAGE);
            RetrofitCommunication.getInstance().clickItem(buildingAdapter.getItem(position));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_back_category:
                parentPresenter.changeView(Constant.NMAP_PAGE);
                break;
            case R.id.btn_all_marker_list:
                if(!isMid) {
                    presenter.showAllMarkers();
                    presenter.setCameraState(relativeMap);
                } else {
                    presenter.showLandmarkAllMarkers();
                    presenter.setCameraState(relativeMap);
                }
                break;
            case R.id.fab_mid:
                if(isMid){
                    presenter.showAllMarkers();
                    presenter.setCameraState(relativeMap);
                    fabMid.setImageResource(R.drawable.btn_selected_landmark_orange);
                    isMid = false;
                } else {
                    presenter.showLandmarkAllMarkers();
                    presenter.setCameraState(relativeMap);
                    fabMid.setImageResource(R.drawable.btn_selected_mid_orange);
                    isMid = true;
                }
                break;

            case R.id.btn_down_sliding_drawer_category:
                slidingDrawer.animateClose();
                break;

            case R.id.btn_department_store_category:
                setLoading();
                RetrofitCommunication.getInstance().setBuildingsData(Constant.DEPARTMENT_STORE);

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

                txtSelectedCategory.setText(getResources().getString(R.string.department_store));
                break;
            case R.id.btn_shopping_category:
                RetrofitCommunication.getInstance().setBuildingsData(Constant.SHOPPING_MALL);
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

                txtSelectedCategory.setText(getResources().getString(R.string.category_shopping_mall));
                break;
            case R.id.btn_stadium_category:
                RetrofitCommunication.getInstance().setBuildingsData(Constant.STADIUM);
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

                txtSelectedCategory.setText(getResources().getString(R.string.category_stadium));
                break;
            case R.id.btn_zoo_category:
                RetrofitCommunication.getInstance().setBuildingsData(Constant.ZOO);
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
                txtSelectedCategory.setText(getResources().getString(R.string.category_zoo));
                break;
            case R.id.btn_museum_category:
                RetrofitCommunication.getInstance().setBuildingsData(Constant.MUSEUM);
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
                txtSelectedCategory.setText(getResources().getString(R.string.category_museum));
                break;
            case R.id.btn_theater_category:
                setLoading();
                RetrofitCommunication.getInstance().setBuildingsData(Constant.MOVIE_THEATER);

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
                txtSelectedCategory.setText(getResources().getString(R.string.category_theater));
                break;
            case R.id.btn_aquarium_store_category:
                setLoading();
                RetrofitCommunication.getInstance().setBuildingsData(Constant.AQUARIUM);

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
                txtSelectedCategory.setText(getResources().getString(R.string.category_aquarium));
                break;
            case R.id.btn_cafe_category:
                RetrofitCommunication.getInstance().setBuildingsData(Constant.CAFE);
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
                txtSelectedCategory.setText(getResources().getString(R.string.category_cafe));
                break;
            case R.id.btn_drink_category:
                setLoading();
                RetrofitCommunication.getInstance().setBuildingsData(Constant.DRINK);

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
                txtSelectedCategory.setText(getResources().getString(R.string.category_drink));
                break;
            case R.id.btn_restaurant_store_category:
                setLoading();
                RetrofitCommunication.getInstance().setBuildingsData(Constant.RESTAURANT);

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
                txtSelectedCategory.setText(getResources().getString(R.string.category_restaurant));
                break;

        }
    }
}
