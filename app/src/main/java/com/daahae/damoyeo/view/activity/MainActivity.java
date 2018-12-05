package com.daahae.damoyeo.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.view.Constant;
import com.daahae.damoyeo.view.fragment.CategoryFragment;
import com.daahae.damoyeo.view.fragment.DetailFragment;
import com.daahae.damoyeo.view.fragment.MapsFragment;

public class MainActivity extends FragmentActivity {

    public static int LOGIN_FLG = Constant.GUEST_LOGIN;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // set LoginFlg
        LOGIN_FLG = getIntent().getIntExtra(Constant.LOGIN, Constant.GUEST_LOGIN);

        Constant.context = this;
        setFragmentInitialization();
    }

    @Override
    public void onBackPressed() {

    }

    public void setFragmentInitialization(){

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentHere, new MapsFragment(this));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void backView(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragment).commit();
        fragmentManager.popBackStack();
    }

    private void setViewFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentHere, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void changeView(int nextPageNumber){

        switch (nextPageNumber){
            case Constant.MAPS_PAGE:
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
}
