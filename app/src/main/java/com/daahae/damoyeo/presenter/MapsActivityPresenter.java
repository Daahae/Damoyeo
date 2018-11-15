package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.communication.RetrofitService;
import com.daahae.damoyeo.exception.PositionNumberServices;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.BuildingArr;
import com.daahae.damoyeo.model.BuildingDetail;
import com.daahae.damoyeo.model.BuildingRequest;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.model.UserRequest;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.fragment.CategoryFragment;
import com.daahae.damoyeo.view.fragment.DetailFragment;
import com.daahae.damoyeo.view.fragment.MapsFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;

public class MapsActivityPresenter {
    private FragmentActivity view;

    private ArrayList<String> totalTimes;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Building targetBuilding;

    private List<TransportInfoList.Data> transportData;
    private List<Building> buildings;
    private ArrayList<Person> persons;

    private BuildingDetail detail;

    public MapsActivityPresenter(FragmentActivity view) {
        this.view = view;
        setFragmentInitialization();
    }

    public void setFragmentInitialization(){
        fragment = new MapsFragment(this);
        fragmentManager = view.getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentHere, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void backView(Fragment fragment) {
        fragmentManager = view.getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragment).commit();
        fragmentManager.popBackStack();
    }

    private void setViewFragment(Fragment fragment){
        view.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentHere, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void changeView(int nextPageNumber){

        switch (nextPageNumber){
            case Constant.NMAP_PAGE:
                setViewFragment(new MapsFragment(this));
                break;

            case Constant.CATEGORY_PAGE:
                setViewFragment(new CategoryFragment(this));
                break;

            case Constant.DETAIL_PAGE:
                setViewFragment(new DetailFragment(this));
                break;
        }
    }

    public ArrayList<String> getTotalTimes() {
        return totalTimes;
    }

    public void sendMarkerTimeMessage(){
        RetrofitCommunication.getInstance().setPersonList(Person.getInstance());
        ArrayList<String> totalTimes = RetrofitCommunication.getInstance().sendPersonLocation();
        Log.v("GMAP", "보냄");
        this.totalTimes = totalTimes;
    }

    public void clickItem(Building building){
        targetBuilding = building;
    }

    public Building getTargetBuilding() {
        return targetBuilding;
    }

    public void setTransportData(){
        TransportInfoList list = RetrofitCommunication.getInstance().getList();
        for (int i = 0; i < list.getUserArr().size(); i++) {
            transportData = list.getUserArr();
        }
    }


    public List<TransportInfoList.Data> getTransportData(){
        setTransportData();
        return transportData;
    }

    public List<Building> getBuildings(int buildingType){
        setBuildingsData(buildingType);
        return buildings;
    }

    public BuildingDetail getBuildingDetail(String name, Position position){
        setBuildingDetail(name, position);
        return detail;
    }

    private void setBuildingsData(int buildingType){
        UserRequest request = new UserRequest();
        request.setType(buildingType);
        BuildingArr arr = RetrofitCommunication.getInstance().sendBuildingInfo(request);
    }

    public void setBuildingDetail(String name, Position position){
        BuildingRequest buildingRequest = new BuildingRequest();
        buildingRequest.setName(name);
        buildingRequest.setLatitude(position.getX());
        buildingRequest.setLongitude(position.getY());
        BuildingDetail detail = RetrofitCommunication.getInstance().sendBuildingDetail(buildingRequest);
        this.detail = detail;
    }

}