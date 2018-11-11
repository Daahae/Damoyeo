package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.MidInfo;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.presenter.Contract.NMapActivityContract;
import com.daahae.damoyeo.view.activity.NMapActivity;
import com.daahae.damoyeo.view.data.Constant;
import com.daahae.damoyeo.view.fragment.CategoryFragment;
import com.daahae.damoyeo.view.fragment.DetailFragment;
import com.daahae.damoyeo.view.fragment.NMapFragment;
import com.daahae.damoyeo.view.fragment.SelectMidFragment;

import java.util.ArrayList;
import java.util.List;

public class NMapActivityPresenter implements NMapActivityContract.Presenter {
    private FragmentActivity view;

    private ArrayList<Person> personList;
    private MidInfo mid;
    private Building building;

    private ArrayList<String> totalTimes;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private RetrofitPresenter retrofitPresenter;

    private Building targetBuilding;

    private List<TransportInfoList.Data> transportDatas;

    public NMapActivityPresenter(NMapActivity view){
        this.view = view;

        init();
        setFragmentInitialization();
    }

    @Override
    public void init() {
        initPersonList();
        initMidinfo();
        initBuilding();

        retrofitPresenter = new RetrofitPresenter();
    }

    private void setFragmentInitialization(){
        fragment = new NMapFragment(this);
        fragmentManager = view.getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.fragmentHere, fragment );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
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

    @Override
    public void changeView(int nextPageNumber){

        switch (nextPageNumber){
            case Constant.NMAP_PAGE:
                setViewFragment(new NMapFragment(this));
                break;

            case Constant.SELECT_MID_PAGE:
                setViewFragment(new SelectMidFragment(this));
                break;

            case Constant.CATEGORY_PAGE:
                setViewFragment(new CategoryFragment(this));
                break;

            case Constant.DETAIL_PAGE:
                setViewFragment(new DetailFragment(this));
                break;
        }
    }

    @Override
    public void initPersonList() {
        this.personList = new ArrayList<Person>();
    }

    // TODO 통신 후 set
    private void initMidinfo() {
        Position pos = new Position(Constant.latitude, Constant.longitude);
        mid = new MidInfo(pos, Constant.address);
    }

    private void initBuilding() {
        Position pos = new Position(Constant.latitude, Constant.longitude);
        building = new Building(Constant.latitude,Constant.longitude,Constant.name, Constant.address, Constant.tel, Constant.description, Constant.distance);
    }

    @Override
    public ArrayList<Person> getPersonList() {
        return personList;
    }

    @Override
    public MidInfo getMid() {
        return mid;
    }

    @Override
    public Building getBuilding() {
        return building;
    }

    public ArrayList<String> getTotalTimes() {
        return totalTimes;
    }

    public void sendMarkerTimeMessage(ArrayList<String> totalTimes){
        this.totalTimes = totalTimes;
    }

    public RetrofitPresenter getRetrofitPresenter() {
        return retrofitPresenter;
    }

    public void setRetrofitPersonList(){
        retrofitPresenter.setPersonList(personList);
    }

    public void clickItem(Building building){
        targetBuilding = building;
    }

    public Building getTargetBuilding() {
        return targetBuilding;
    }

    public void setTransportData(){
        TransportInfoList list = retrofitPresenter.getList();
        for (int i = 0; i < list.getUserArr().size(); i++) {
            transportDatas = list.getUserArr();
        }
    }

    public List<TransportInfoList.Data> getTransportData(){
        setTransportData();
        return transportDatas;
    }
}