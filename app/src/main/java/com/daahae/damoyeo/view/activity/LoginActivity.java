package com.daahae.damoyeo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.communication.RetrofitCommunication;
import com.daahae.damoyeo.model.BuildingRequest;
import com.daahae.damoyeo.presenter.LoginActivityPresenter;
import com.daahae.damoyeo.presenter.RetrofitPresenter;
import com.daahae.damoyeo.view.data.Constant;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnGuestLogin;
    private LoginActivityPresenter presenter;
    private Context context;

    public LoginActivity(){
        context = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initListener();

        /*
        BuildingRequest test = new BuildingRequest();
        test.setType(Constant.CAFE);
        RetrofitPresenter retrofitPresenter = new RetrofitPresenter();
        retrofitPresenter.sendBuildingInfo(test);
        */
    }

    private void initView(){
        btnGuestLogin = findViewById(R.id.btn_guest_login);
    }

    private void initListener(){
        btnGuestLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_guest_login:
                changeView();
                break;
        }
    }

    private void changeView(){
        Intent intent = new Intent(LoginActivity.this, NMapActivity.class);
        startActivity(intent);
    }
}
