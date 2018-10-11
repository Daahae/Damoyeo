package com.daahae.damoyeo.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.presenter.NMapActivityPresenter;

public class NMapActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmap);

        new NMapActivityPresenter(this);
    }
}