package com.daahae.damoyeo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.presenter.MapsActivityPresenter;
import com.daahae.damoyeo.view.Constant;

public class MapsActivity extends FragmentActivity {

    public static int LOGIN_FLG = Constant.GUEST_LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        new MapsActivityPresenter(this);

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        LOGIN_FLG = requestCode;
    }
}
