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
import com.daahae.damoyeo.view.fragment.CategoryFragment;
import com.daahae.damoyeo.view.fragment.NMapFragment;
import com.daahae.damoyeo.view.fragment.SelectMidFragment;

import java.util.ArrayList;

public class NMapActivityPresenter {
    private NMapActivity view;
    public static final int NMAP_PAGE = 1;
    public static final int SELECT_MID_PAGE = 2;
    public static final int CATEGORY_PAGE = 3;

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
            case NMAP_PAGE:
                view.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentHere, new NMapFragment(this))
                        .addToBackStack(null)
                        .commit();
                break;

            case SELECT_MID_PAGE:
                view.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentHere, new SelectMidFragment(this))
                        .addToBackStack(null)
                        .commit();
                break;

            case CATEGORY_PAGE:
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
        Position pos = new Position(126.978371, 37.5666091);
        this.mid = new MidInfo(pos, "서울특별시 중구 명동");
    }

    private void initBuilding() {
        Position pos = new Position(126.978371, 37.5666091);
        this.building = new Building(pos, "서울특별시 중구 명동", 0, pos, "서울시청", "서울특별시 중구 명동 서울시청", "02-0000-0000");
    }
}