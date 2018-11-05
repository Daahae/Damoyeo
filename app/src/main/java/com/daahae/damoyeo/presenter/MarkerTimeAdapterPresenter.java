package com.daahae.damoyeo.presenter;

import android.view.View;
import android.widget.TextView;

import com.daahae.damoyeo.model.TransportInfoList;
import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;

import java.util.ArrayList;

public class MarkerTimeAdapterPresenter {

    private MarkerTimeAdapter adapter;

    public MarkerTimeAdapterPresenter(MarkerTimeAdapter adapter){
        this.adapter = adapter;
    }

    //각 리스트 요소의 text지정 //TODO: text 부분은 가져올 예정
    public void setMarkerListText(TextView MarkerNameView, TextView MarkerTime, String nameText, String timeText){
        MarkerNameView.setText(nameText);
        MarkerTime.setText(timeText);
    }
}
