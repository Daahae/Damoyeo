package com.daahae.damoyeo.presenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.fragment.CategoryFragment;
import com.daahae.damoyeo.view.fragment.MapsFragment;

import java.util.ArrayList;

public class MapsActivityPresenter {
    private FragmentActivity view;

    private ArrayList<String> totalTimes;

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public MapsActivityPresenter(FragmentActivity view) {
        this.view = view;
        setFragmentInitialization();
    }

    private void setFragmentInitialization(){
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
        }
    }

    public ArrayList<String> getTotalTimes() {
        return totalTimes;
    }

    public void sendMarkerTimeMessage(){
        RetrofitPresenter.getInstance().setPersonList(Person.getInstance());
        ArrayList<String> totalTimes = RetrofitPresenter.getInstance().sendPersonMessage();
        Log.v("GMAP", "보냄");
        this.totalTimes = totalTimes;
    }
}