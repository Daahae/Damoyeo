package com.daahae.damoyeo.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.presenter.MapsActivityPresenter;

public class MapsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        new MapsActivityPresenter(this);
    }
}
