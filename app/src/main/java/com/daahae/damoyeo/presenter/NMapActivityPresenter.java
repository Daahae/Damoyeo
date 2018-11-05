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
import com.daahae.damoyeo.presenter.Contract.NMapActivityContract;
import com.daahae.damoyeo.view.activity.NMapActivity;
import com.daahae.damoyeo.view.data.Constant;
import com.daahae.damoyeo.view.fragment.CategoryFragment;
import com.daahae.damoyeo.view.fragment.NMapFragment;
import com.daahae.damoyeo.view.fragment.SelectMidFragment;

import java.util.ArrayList;

public class NMapActivityPresenter implements NMapActivityContract.Presenter {
    private FragmentActivity view;

    private ArrayList<Person> personList;
    private MidInfo mid;
    private Building building;

    private ArrayList<Person> person;
    private ArrayList<String> totalTimes;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private RetrofitPresenter retrofitPresenter;
    public NMapActivityPresenter(NMapActivity view){
        this.view = view;

        init();
        setFragmentInitialization();
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
    public void init() {

        initPersonList();
        initMidinfo();
        initBuilding();

        totalTimes = new ArrayList<>();
        person = new ArrayList<>();
        retrofitPresenter = new RetrofitPresenter();
    }

    public RetrofitPresenter getRetrofitPresenter() {
        return retrofitPresenter;
    }

    @Override
    public void backView(Fragment fragment) {
        fragmentManager = view.getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragment).commit();
        fragmentManager.popBackStack();
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
        }
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

    @Override
    public void initPersonList() {
        this.personList = new ArrayList<Person>();
    }

    private void initMidinfo() {
        Position pos = new Position(Constant.latitude, Constant.longitude);
        mid = new MidInfo(pos, Constant.address);
    }

    private void initBuilding() {
        Position pos = new Position(Constant.latitude, Constant.longitude);
        building = new Building(pos, Constant.address, 0, pos, Constant.name, Constant.address, Constant.tel);
    }

    private void setViewFragment(Fragment fragment){
        view.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentHere, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void sendMarkerTimeMessage(ArrayList<String> totalTimes){
        this.totalTimes = totalTimes;
    }

    public ArrayList<String> getTotalTimes() {
        return totalTimes;
    }

    public void addPerson(){
        retrofitPresenter.addPerson(personList);
    }

}