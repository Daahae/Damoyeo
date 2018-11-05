package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.view.activity.NMapActivity;
import com.daahae.damoyeo.view.fragment.CategoryFragment;
import com.daahae.damoyeo.view.fragment.NMapFragment;
import com.daahae.damoyeo.view.fragment.SelectMidFragment;

import java.util.ArrayList;

public class NMapActivityPresenter {
    private NMapActivity view;// 뷰
    public static final int NMAP_PAGE = 1;
    public static final int SELECT_MID_PAGE = 2;
    public static final int CATEGORY_PAGE = 3;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    //모델은 각자 클래스 생성

    private ArrayList<Person> person;
    private ArrayList<String> totalTimes;

    public NMapActivityPresenter(NMapActivity view){
        this.view = view;

        init();
        setFragmentInitialization();

    }
    private void init(){

        totalTimes = new ArrayList<>();
        person = new ArrayList<>();
    }

    private void setFragmentInitialization(){
        fragment = new NMapFragment(this);
        fragmentManager = view.getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.fragmentHere, fragment );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void backView(Fragment fragment){
        fragmentManager = view.getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragment).commit();
        fragmentManager.popBackStack();
    }

    public void changeView(int nextPageNumber){

        switch (nextPageNumber){
            case NMAP_PAGE:
                setViewFragment(new NMapFragment(this));
                break;

            case SELECT_MID_PAGE:
                setViewFragment(new SelectMidFragment(this));
                break;

            case CATEGORY_PAGE:
                setViewFragment(new CategoryFragment(this));
                break;
        }
    }

    private void setViewFragment(Fragment fragment){
        view.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentHere, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void sendMarkerTimeMessage(ArrayList<Person> person,ArrayList<String> totalTimes){
        this.totalTimes = totalTimes;
        this.person = person;
    }

    public ArrayList<String> getTotalTimes() {
        return totalTimes;
    }

    public ArrayList<Person> getPerson() {
        return person;
    }

}