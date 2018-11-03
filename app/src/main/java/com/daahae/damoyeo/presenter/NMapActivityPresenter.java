package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.MidInfo;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.view.activity.NMapActivity;
import com.daahae.damoyeo.view.data.Constant;
import com.daahae.damoyeo.view.fragment.CategoryFragment;
import com.daahae.damoyeo.view.fragment.NMapFragment;
import com.daahae.damoyeo.view.fragment.SelectMidFragment;

import java.util.ArrayList;

public class NMapActivityPresenter {
    private NMapActivity view;

    private ArrayList<Person> personList;
    private MidInfo mid;
    private Building building;

    public NMapActivityPresenter(NMapActivity view){
        this.view = view;

        Fragment fragment = new NMapFragment(this);
        FragmentManager fragmentManager = view.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.fragmentHere, fragment );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        initPersonList();
        // TODO 서버와 통신
        initMidinfo();
        initBuilding();
    }

    public void backView(Fragment fragment){
        FragmentManager fragmentManager = view.getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragment).commit();
        fragmentManager.popBackStack();
    }

    public void changeView(int nextPageNumber){
        switch (nextPageNumber){
            case Constant.NMAP_PAGE:
                view.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentHere, new NMapFragment(this))
                        .addToBackStack(null)
                        .commit();
                break;

            case Constant.SELECT_MID_PAGE:
                view.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentHere, new SelectMidFragment(this))
                        .addToBackStack(null)
                        .commit();
                break;

            case Constant.CATEGORY_PAGE:
                view.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentHere, new CategoryFragment(this))
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    public ArrayList<Person> getPersonList() {
        return personList;
    }

    public MidInfo getMid() {
        return mid;
    }

    public Building getBuilding() {
        return building;
    }

    private void initPersonList() {
        this.personList = new ArrayList<Person>();
    }

    private void initMidinfo() {
        Position pos = new Position(Constant.longitude, Constant.latitude);
        mid = new MidInfo(pos, Constant.address);
    }

    private void initBuilding() {
        Position pos = new Position(Constant.longitude, Constant.latitude);
        building = new Building(pos, Constant.address, 0, pos, Constant.name, Constant.address, Constant.tel);
    }
}