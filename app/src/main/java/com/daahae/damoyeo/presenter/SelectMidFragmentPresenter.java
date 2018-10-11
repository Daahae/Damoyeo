package com.daahae.damoyeo.presenter;

import com.daahae.damoyeo.view.adapter.MarkerTimeAdapter;
import com.daahae.damoyeo.view.fragment.SelectMidFragment;

import java.util.ArrayList;

public class SelectMidFragmentPresenter {


    public static final int MID_ALGORITHM = 1;
    public static final int LANDMARK = 2;

    private SelectMidFragment view;

    private MarkerTimeAdapter markerTimeAdapter;

    public SelectMidFragmentPresenter(SelectMidFragment view) {
        this.view = view;
    }

    public void selectMid(int selectMidMenu){
        switch (selectMidMenu){
            case MID_ALGORITHM:
                selectMidAlgorithm();
                break;
            case LANDMARK:
                selectLandmark();
                break;
        }
    }

    private void selectMidAlgorithm(){

    }

    private void selectLandmark(){

    }

    public void setMarkerTimeList(MarkerTimeAdapter markerTimeAdapter){
        this.markerTimeAdapter = markerTimeAdapter;

        markerTimeAdapter.resetList();
        makeDummy();
    }

    //TODO: 삭제예정
    private void makeDummy(){

        for(int i=0;i<3;i++){
            markerTimeAdapter.addDummy();
        }
    }

}
