package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.widget.Button;

import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.adapter.BuildingAdapter;
import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class CategoryFragmentPresenter {
    private Fragment view;
    private ArrayList<String> totalTimes;
    private BuildingArr buildingArr;

    private BuildingAdapter buildingAdapter;

    private LatLngBounds.Builder builder;
    private GoogleMap googleMap;

    public CategoryFragmentPresenter(Fragment view) {
        this.view = view;
    }

    public void initMarkerTime(ArrayList<String> totalTimes){
        this.totalTimes = totalTimes;
    }

    public void setMarkerTimeList(MarkerTimeAdapter markerTimeAdapter) {
        markerTimeAdapter.resetList();
        for(int i=0; i < totalTimes.size();i++)
            markerTimeAdapter.add(Person.getInstance().get(i).getName(), totalTimes.get(i));
    }

    public void initBuildingInfo(BuildingArr buildingArr){
        this.buildingArr = buildingArr;
    }

    public boolean setBuildingInfo(BuildingAdapter buildingAdapter) {
        this.buildingAdapter = buildingAdapter;

        buildingAdapter.resetList();
        //TODO: Exception 데이터 없을때,
        if(buildingArr.getBuildingArr().size()==0) return false;
        for(int i=0;i<buildingArr.getBuildingArr().size();i++){
            buildingAdapter.add(buildingArr.getBuildingArr().get(i));
        }
        return true;
    }

    public void setClickFirstButton(Button button){
        button.setClickable(true);
    }

    public void setGMapSetting(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void setCameraState() {
        LatLngBounds bounds = builder.build();
        int width = view.getResources().getDisplayMetrics().widthPixels;
        int height = view.getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.animateCamera(cu);
    }

    public void showAllMarkers() {
        googleMap.clear();
        builder = new LatLngBounds.Builder();

        //TODO 중간지점 위치
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(Constant.DEFAULT_LOCATION);
        markerOption.title(Constant.name);
        markerOption.snippet(Constant.address);
        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(markerOption).showInfoWindow();

        builder.include(markerOption.getPosition());

        //현재위치의 위도 경도 가져옴
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

    public void showEachMarker(int position) {
        googleMap.clear();
        builder = new LatLngBounds.Builder();

        //TODO 중간지점 위치
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(Constant.DEFAULT_LOCATION);
        markerOption.title(Constant.name);
        markerOption.snippet(Constant.address);
        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(markerOption).showInfoWindow();

        builder.include(markerOption.getPosition());

        //현재위치의 위도 경도 가져옴
        Person person = Person.getInstance().get(position);
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

    public void showLandmarkAllMarkers() {
        googleMap.clear();
        builder = new LatLngBounds.Builder();

        //TODO 랜드마크 위치
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(Constant.LANDMARK_LOCATION);
        markerOption.title(Constant.landmark_name);
        markerOption.snippet(Constant.landmark_address);
        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(markerOption).showInfoWindow();

        builder.include(markerOption.getPosition());

        //현재위치의 위도 경도 가져옴
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

    public void showLandmarkEachMarker(int position) {
        googleMap.clear();
        builder = new LatLngBounds.Builder();

        //TODO 중간지점 위치
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(Constant.LANDMARK_LOCATION);
        markerOption.title(Constant.landmark_name);
        markerOption.snippet(Constant.landmark_address);
        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        googleMap.addMarker(markerOption).showInfoWindow();

        builder.include(markerOption.getPosition());

        //현재위치의 위도 경도 가져옴
        Person person = Person.getInstance().get(position);
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
