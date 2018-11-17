package com.daahae.damoyeo.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Person;
import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.view.adapter.TransportAdapter;

public class TransportActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView listTransport;

    private ImageButton btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        initView();
        initListener();
    }

    private void initView() {
        listTransport = findViewById(R.id.list_transport_detail);
        btnBack = findViewById(R.id.btn_back_transport);
        TransportAdapter transportAdapter = new TransportAdapter(TransportInfoList.getInstance().getUserArr(), Person.getInstance(),this);
        listTransport.setAdapter(transportAdapter);
    }

    private void initListener(){
        btnBack.setOnClickListener(this);
        listTransport.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back_transport:
                setResult(-1);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setResult(position);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(-1);
        finish();
    }
}
