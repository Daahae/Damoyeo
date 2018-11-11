package com.daahae.damoyeo.presenter;

import android.widget.Button;

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

    private ArrayList<String> totalTimes;

    private BuildingAdapter buildingAdapter;

    private LatLngBounds.Builder builder;
    private GoogleMap googleMap;

    public void initMarkerTime(ArrayList<String> totalTimes){
        this.totalTimes = totalTimes;
    }

    public void setMarkerTimeList(MarkerTimeAdapter markerTimeAdapter) {

        for(int i=0; i < totalTimes.size();i++)
            markerTimeAdapter.add(Person.getInstance().get(i).getName(), totalTimes.get(i));
    }

    public void setBuildingInfo(BuildingAdapter buildingAdapter) {
        this.buildingAdapter = buildingAdapter;

        buildingAdapter.resetList();
        makeDummy();
    }

    public void setClickFirstButton(Button button){
        button.setClickable(true);
    }

    //TODO: 삭제예정
    private void makeDummy(){
        for(int i=0;i<3;i++){
            buildingAdapter.addDummy();
        }
    }

    public void setGMapSetting(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void setCameraState() {
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
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
